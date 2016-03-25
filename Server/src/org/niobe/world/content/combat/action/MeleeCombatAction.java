package org.niobe.world.content.combat.action;

import org.niobe.model.Damage;
import org.niobe.model.GameCharacter;
import org.niobe.model.Damage.Hit;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.container.impl.Equipment;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.model.weapon.Weapon;
import org.niobe.model.weapon.Weapon.WeaponExperienceStyle;
import org.niobe.util.MathUtil;
import org.niobe.world.Player;
import org.niobe.world.content.combat.CombatManager;

/**
 * An implementation of {@link CombatAction} used in melee
 * combat.
 *
 * @author relex lawl
 */
public final class MeleeCombatAction extends CombatAction {
	
	@Override
	public boolean canInitiateCombat(GameCharacter source, GameCharacter victim) {
		return true;
	}

	@Override
	public int getDistanceRequirement(GameCharacter source, GameCharacter victim) {
		if (source.isPlayer()) {
			Player player = (Player) source;
			final String weapon = ItemDefinition.forId(player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()).getName().toLowerCase();
			switch (source.getFields().getCombatAttributes().getAttackType()) {
			case MELEE:
				if (weapon.contains("halberd")) {
					return 1 + victim.getSize();
				}
				return victim.getSize();
			}
		}
		return 1;
	}
	
	@Override
	public int getHitDelay(GameCharacter source, GameCharacter victim) {
		return 0;
	}
	
	@Override
	public void hit(GameCharacter source, GameCharacter victim, Damage damage) {
		
	}
	
	@Override
	public void specialHitAction(GameCharacter source, GameCharacter victim, Damage damage) {
		super.specialHitAction(source, victim, damage);
		if (source.isPlayer()) {
			Player player = (Player) source;
			Weapon weapon = player.getWeapon();
			String name = weapon.getDefinition().getName();
			if (victim.getFields().getPoisonDamage() <= 0) {
				int max = 68;
				if (name.contains("(p") && MathUtil.random(8) == 0) {
					if (name.contains("(p++)")) {
						CombatManager.poison(victim, max);
					} else if (name.contains("(p+)")) {
						CombatManager.poison(victim, max - 10);
					} else if (name.contains("(p)")) {
						CombatManager.poison(victim, max - 20);
					}
				}
			}
		}
	}

	@Override
	public void addExperience(Player player, Damage damage) {
		Weapon weapon = player.getWeapon();
		WeaponExperienceStyle experience = weapon.getCombatDefinition().getExperience()[player.getFields().getCombatAttributes().getAttackStyle()];
		Skill[] skills = experience == WeaponExperienceStyle.ATTACK ? 
				new Skill[] {Skill.ATTACK} : experience == WeaponExperienceStyle.DEFENCE ?
				new Skill[] {Skill.DEFENCE} : experience == WeaponExperienceStyle.MELEE_SHARED ?
				new Skill[] {Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE} : new Skill[] {Skill.STRENGTH};
		int shared = skills.length;
		for (Hit hits : damage.getHits()) {
			int hit = hits.getDamage();
			if (damage.getHits().length >= 2)
				hit += damage.getHits()[1].getDamage();
			for (Skill skill : skills) {
				player.getSkillManager().addExperience(skill, (int) (((hit * .40) * skill.getExperienceMultiplier()) / shared));
			}
			player.getSkillManager().addExperience(Skill.CONSTITUTION, ((int) (hit * .5) * Skill.CONSTITUTION.getExperienceMultiplier()));
		}
	}
}
