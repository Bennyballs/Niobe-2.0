package org.niobe.world.content.combat.formula.melee;

import org.niobe.model.AttackType;
import org.niobe.model.GameCharacter;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.weapon.Weapon;
import org.niobe.model.weapon.Weapon.CombatStyle;
import org.niobe.util.MathUtil;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.content.combat.formula.ArmourBonus;
import org.niobe.world.content.combat.formula.PrayerBonus;

public final class MeleeAccuracyFormula {
	
	public static boolean successfulHit(GameCharacter source, GameCharacter victim) {
		double attack = getLevel(Role.ATTACKER, source) + getAttackBonus(source);
		double defence = getLevel(Role.DEFENDER, victim) + getDefenceBonus(source, victim);
		defence += (defence * victim.getFields().getCombatAttributes().getInvisibleDefenceMultiplier()); //TODO is this fine?
		attack = getBonusAccuracy(source, attack);
		int attackRoll = MathUtil.random((int) Math.floor(attack));
		int defenceRoll = MathUtil.random((int) Math.floor(defence));
		return attackRoll > defenceRoll;
	}
	
	private static double getLevel(Role role, GameCharacter source) {
		double level = 0;
		double prayerBonus = 1;
		if (source.isPlayer()) {
			Player player = (Player) source;
			level = player.getSkillManager().getCurrentLevel(role == Role.ATTACKER ? Skill.ATTACK : Skill.DEFENCE);
			prayerBonus += role == Role.ATTACKER ? PrayerBonus.getAttack(player) : PrayerBonus.getDefence(player);
		} else if (source.isMob()) {
			Mob Mob = (Mob) source;
			level = role == Role.ATTACKER ? Mob.getDefinition().getAttackLevel() : Mob.getDefinition().getDefenceLevel();
		}
		level = 8 + Math.floor(level * prayerBonus);
		return level;
	}
	
	private static double getAttackBonus(GameCharacter source) {
		double bonus = 0;
		if (source.isPlayer()) {
			Player player = (Player) source;
			Weapon weapon = player.getWeapon();
			CombatStyle style = weapon.getCombatDefinition().getCombatStyle()[player.getFields().getCombatAttributes().getAttackStyle()];
			bonus = player.getFields().getBonusManager().getAttackBonus()[style.ordinal()];
		} else if (source.isMob()) {
			Mob Mob = (Mob) source;
			bonus = Mob.getDefinition().getAttackLevel() + (Mob.getDefinition().getLevel() / 3);
		}
		return bonus;
	}
	
	private static double getDefenceBonus(GameCharacter source, GameCharacter victim) {
		double bonus = 0;
		if (victim.isPlayer()) {
			if (source.isPlayer()) {
				Player player = (Player) source;
				Weapon weapon = player.getWeapon();
				CombatStyle style = weapon.getCombatDefinition().getCombatStyle()[player.getFields().getCombatAttributes().getAttackStyle()];
				bonus = victim.getFields().getBonusManager().getDefenceBonus()[style.ordinal()];
			}
		}
		if (victim.isMob()) {
			Mob Mob = (Mob) victim;
			bonus = Mob.getDefinition().getAttackLevel() + (Mob.getDefinition().getLevel() / 3);
		}
		return bonus;
	}
	
	private static double getBonusAccuracy(GameCharacter source, double roll) {
		if (source.isPlayer()) {
			Player player = (Player) source;
			if (ArmourBonus.hasVoidBonus(player, AttackType.MELEE)) {
				roll = Math.floor(roll * 1.1);
			}
			Weapon weapon = player.getWeapon();
			if (player.getFields().getCombatAttributes().isUsingSpecialAttack()
					&& weapon.getSpecialAttack() != null) {
				roll *= weapon.getSpecialAttack().getAccuracy();
			}
		}
		return roll;
	}

	private enum Role {
		ATTACKER,
		DEFENDER;
	}
}
