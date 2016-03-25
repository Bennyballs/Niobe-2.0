package org.niobe.world.content.combat.formula.melee;

import org.niobe.model.AttackType;
import org.niobe.model.GameCharacter;
import org.niobe.model.PrayerBook;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.container.impl.Equipment;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.model.definition.MobDefinition;
import org.niobe.model.weapon.Weapon;
import org.niobe.model.weapon.Weapon.AttackStyle;
import org.niobe.model.weapon.WeaponLoader;
import org.niobe.util.MathUtil;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.content.BonusManager.OtherBonus;
import org.niobe.world.content.combat.CombatCurses;
import org.niobe.world.content.combat.CombatPrayers;
import org.niobe.world.content.combat.formula.PrayerBonus;
import org.niobe.world.content.combat.formula.ArmourBonus;

public class MeleeHitFormula {

	public static int getRandomHit(GameCharacter source, GameCharacter victim) {
		return MathUtil.random(getMaxHit(source, victim));
	}
	
	public static int getMaxHit(GameCharacter source, GameCharacter victim) {
		int max = MeleeAccuracyFormula.successfulHit(source, victim) ? 
					(int) (getBaseDamage(source, victim) * getProtection(source, victim)) : 0;
		return max;
	}
	
	private static double getBaseDamage(GameCharacter source, GameCharacter victim) {
		double base = 0;
		double bonus = source.getFields().getBonusManager().getOtherBonus()[OtherBonus.STRENGTH.ordinal()];
		base = 5 + getEffectiveStrength(source, victim) * (1 + bonus / 64);
		if (source.isPlayer()) {
			Player player = (Player) source;
			Weapon weapon = WeaponLoader.forId(player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId());
			if (weapon.getSpecialAttack() != null && player.getFields().getCombatAttributes().isUsingSpecialAttack()) {
				int extraDamage = weapon.getSpecialAttack().getExtraDamage(player, victim);
				if (extraDamage > 0) {
					base += extraDamage;
				}
			}
		}
		base = getBonusDamage(source, victim, base);
		return base;
	}
	
	private static double getEffectiveStrength(GameCharacter source, GameCharacter victim) {
		double strength = 0;
		if (source.isPlayer()) {
			Player player = (Player) source;
			double prayerBonus = 1 + PrayerBonus.getStrength(player);
			double strengthLevel = player.getSkillManager().getCurrentLevel(Skill.STRENGTH);
			double styleBonus = 0;
			Weapon weapon = WeaponLoader.forId(player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId());
			AttackStyle style = weapon.getCombatDefinition()
									.getAttackStyle()[player.getFields().getCombatAttributes().getAttackStyle()];
			switch (style) {
			case AGGRESSIVE:
				styleBonus += 3;
				break;
			case CONTROLLED:
				styleBonus += 1;
				break;
			}
			if (player.getFields().getPrayerActive()[CombatCurses.TURMOIL] 
					&& player.getFields().getPrayerBook() == PrayerBook.CURSES
					&& victim.isPlayer()) {
				Player other = (Player) victim;
				strength += (other.getSkillManager().getCurrentLevel(Skill.STRENGTH) / 10);
			}
			strength += 8 + Math.floor((strengthLevel * prayerBonus) + styleBonus);
			if (ArmourBonus.hasVoidBonus(player, AttackType.MELEE)) {
				strength = Math.floor(strength * 1.1);
			}
		} else if (source.isMob()) {
			MobDefinition definition = MobDefinition.forId(((Mob)source).getId());
			strength = definition.getLevel() + definition.getAttackLevel() * 3;
		}
		return strength;
	}
	
	private static double getBonusDamage(GameCharacter character, GameCharacter victim, double base) {
		if (character.isPlayer()) {
			Player player = (Player) character;
			Weapon weapon = WeaponLoader.forId(player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId());
			double specialBonus = 1;
			double otherBonus = 1;
			if (player.getFields().getCombatAttributes().isUsingSpecialAttack() &&
					weapon.getSpecialAttack() != null) {
				specialBonus *= weapon.getSpecialAttack().getMultiplier();
			}
			if (ItemDefinition.forId(player.getEquipment().getItems()[Equipment.AMULET_SLOT].getId())
					.getName().toLowerCase().equals("berserker necklace")) {
				for (String obsidian : OBSIDIAN_WEAPON) {
					if (ItemDefinition.forId(player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId())
							.getName().equals(obsidian)) {
						specialBonus += 1.2;
						break;
					}
				}
			}
			if (ItemDefinition.forId(player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId()).getName().equals(DHAROK_ARMOUR[0])
					&& ItemDefinition.forId(player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()).getName().equals(DHAROK_ARMOUR[1])
					&& ItemDefinition.forId(player.getEquipment().getItems()[Equipment.BODY_SLOT].getId()).getName().equals(DHAROK_ARMOUR[2])
					&& ItemDefinition.forId(player.getEquipment().getItems()[Equipment.LEG_SLOT].getId()).getName().equals(DHAROK_ARMOUR[3])) {
				base *= 2 - (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) / player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
			}
			//if amulet name == castlewar brace && player.getFields().currentMinigame() == Minigames.CASTLE_WARS.
			//specialBonus += 1.2;
			if (victim.isMob()) {
				Mob Mob = (Mob) victim;
				ItemDefinition definition = ItemDefinition.forId(player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId());
				if (player.getFields().getSlayerAssignment() != null &&
						player.getFields().getSlayerAssignment().getId() == Mob.getId()) {
					if (definition.getName().equals("Black mask") ||
							definition.getName().contains("Slayer helm")) {
						otherBonus *= 7/6;
					}
				}
				definition = ItemDefinition.forId(player.getEquipment().getItems()[Equipment.AMULET_SLOT].getId());
				if (isUndead(Mob)) {
					if (definition.getName().equals("Salve amulet")) {
						otherBonus *= 7/6;
					} else if (definition.getName().equals("Salve amulet (e)")) {
						otherBonus *= 1.2;
					}
				}
				if (MobDefinition.forId(Mob.getId()).getName().toLowerCase().contains("strykewyrm")
						&& player.getEquipment().getItems()[Equipment.CAPE_SLOT].getDefinition().getName().toLowerCase().contains("fire cape")) {
					otherBonus += 40;
				}
				for (String area : MobDefinition.forId(Mob.getId()).getAreas()) {
					if (area.contains("kuradal's dungeon")) {
						otherBonus += 40;
						break;
					}
				}
			}
			base = Math.floor(base * specialBonus * otherBonus);
		}
		return base;
	}
	
	private static double getProtection(GameCharacter source, GameCharacter victim) {
		if (victim.getFields().getCombatAttributes().hasStaffOfLightEffect()) {
			return .5;
		}
		if (victim.getFields().getPrayerActive()[CombatPrayers.PROTECT_FROM_MELEE]
				&& source.getFields().getCombatAttributes().getAttackType() == AttackType.MELEE) {
			return .75;
		}
		return 1;
	}
	
	private static boolean isUndead(Mob Mob) {
		String name = MobDefinition.forId(Mob.getId()).getName();
		return name.contains("zogre") || name.contains("zombie") || name.contains("undead") ||
				name.contains("aberrant spectre") || name.contains("banshee") ||
				name.contains("crawling hand") || name.contains("ghost") ||
				name.contains("ghast") || name.contains("mummy") || name.contains("mummy") ||
				name.contains("skeleton");
	}
	
	private static final String[] OBSIDIAN_WEAPON = {
		"Toktz-xil-ul",
		"Toktz-xil-ak",
		"Toktz-xil-ek",
		"Toktz-mej-tal",
		"Tzhaar-ket-em",
		"Tzhaar-ket-om"
	};
	
	private static final String[] DHAROK_ARMOUR = {
		"Dharok's helm",
		"Dharok's greataxe",
		"Dharok's platebody",
		"Dharok's platelegs"
	};
}
