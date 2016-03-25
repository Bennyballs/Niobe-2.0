package org.niobe.world.content.combat.action;

import org.niobe.model.AttackType;
import org.niobe.model.Damage;
import org.niobe.model.Damage.Hit;
import org.niobe.model.GameCharacter;
import org.niobe.model.container.impl.Equipment;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.model.definition.MobDefinition;
import org.niobe.world.Mob;
import org.niobe.world.Player;

/**
 * An implementation of {@link CombatAction} used in magic
 * combat.
 *
 * @author relex lawl
 */
public final class MagicCombatAction extends CombatAction {
	
	@Override
	public boolean canInitiateCombat(GameCharacter source, GameCharacter victim) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public int getDistanceRequirement(GameCharacter source, GameCharacter victim) {
		// TODO
		return 1;
	}

	@Override
	public int getHitDelay(GameCharacter source, GameCharacter victim) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void hit(GameCharacter source, GameCharacter victim, Damage damage) {
		if (source.isMob() && victim.isPlayer()) {
			Mob mob = (Mob) source;
			Player player = (Player) victim;
			MobDefinition definition = MobDefinition.forId(mob.getId());
			if (definition.getName().toLowerCase().contains("dragon")
					&& mob.getFields().getCombatAttributes().getAttackType() == AttackType.DRAGON_FIRE) {
				ItemDefinition shieldDefinition = ItemDefinition.forId(player.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId());
				String name = shieldDefinition.getName().toLowerCase();
				if (name.contains("anti-drag shield")) {
					player.getPacketSender().sendMessage("Your shield absorbs most of the dragon-fire");
					for (Hit hit : damage.getHits()) {
						hit.setDamage((int) (hit.getDamage() * .25));
					}
				} else if (name.contains("dragon-fire shield")) {
					player.getPacketSender().sendMessage("Your shield absorbs most of the dragon-fire");
					for (Hit hit : damage.getHits()) {
						hit.setDamage((int) (hit.getDamage() * .1));
					}
					//TODO dfs charges
				} else {
					player.getPacketSender().sendMessage("You are badly burnt by the dragon fire!");
				}
			}
		}
	}

	@Override
	public void specialHitAction(GameCharacter source, GameCharacter victim, Damage damage) {
		super.specialHitAction(source, victim, damage);
	}
	
	@Override
	public void addExperience(Player player, Damage damage) {
		// TODO Auto-generated method stub
	}
}
