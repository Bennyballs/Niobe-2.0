package org.niobe.world.content.combat.formula;

import org.niobe.model.GameCharacter;
import org.niobe.model.PrayerBook;
import org.niobe.model.SkillManager.Skill;
import org.niobe.world.Player;
import org.niobe.world.content.combat.CombatCurses;
import org.niobe.world.content.combat.CombatPrayers;

public final class PrayerBonus {

	public static double getStrength(GameCharacter character) {
		boolean[] prayerActive = character.getFields().getPrayerActive();
		if (character.getFields().getPrayerBook() == PrayerBook.NORMAL) {
			if (prayerActive[CombatPrayers.BURST_OF_STRENGTH])
				return .05;
			if (prayerActive[CombatPrayers.SUPERHUMAN_STRENGTH])
				return .1;
			if (prayerActive[CombatPrayers.ULTIMATE_STRENGTH])
				return .15;
			if (prayerActive[CombatPrayers.CHIVALRY])
				return .18;
			if (prayerActive[CombatPrayers.PIETY])
				return .23;
		} else if (character.getFields().getPrayerBook() == PrayerBook.CURSES) {
			if (prayerActive[CombatCurses.LEECH_STRENGTH]) {
				if (character.getFields().getCombatAttributes().hasAttacked())
					return .1;
				return .05;
			}
			if (prayerActive[CombatCurses.TURMOIL])
				return .23;
		}
		return 0;
	}
	
	public static double getAttack(GameCharacter character) {
		boolean[] prayerActive = character.getFields().getPrayerActive();
		if (character.getFields().getPrayerBook() == PrayerBook.NORMAL) {
			if (prayerActive[CombatPrayers.CLARITY_OF_THOUGHT])
				return .05;
			if (prayerActive[CombatPrayers.IMPROVED_REFLEXES])
				return .1;
			if (prayerActive[CombatPrayers.INCREDIBLE_REFLEXES])
				return .15;
			if (prayerActive[CombatPrayers.CHIVALRY])
				return .15;
			if (prayerActive[CombatPrayers.PIETY])
				return .2;
		} else if (character.getFields().getPrayerBook() == PrayerBook.CURSES) {
			if (prayerActive[CombatCurses.LEECH_ATTACK])
				return .05;
			if (prayerActive[CombatCurses.TURMOIL])
				return .23;
		}
		return 0;
	}
	
	public static double getDefence(GameCharacter character) {
		boolean[] prayerActive = character.getFields().getPrayerActive();
		if (character.getFields().getPrayerBook() == PrayerBook.NORMAL) {
			if (prayerActive[CombatPrayers.THICK_SKIN])
				return .05;
			if (prayerActive[CombatPrayers.ROCK_SKIN])
				return .1;
			if (prayerActive[CombatPrayers.STEEL_SKIN])
				return .15;
			if (prayerActive[CombatPrayers.CHIVALRY])
				return .2;
			else if (prayerActive[CombatPrayers.PIETY] ||
						prayerActive[CombatPrayers.RIGOUR] || prayerActive[CombatPrayers.AUGURY])
				return .25;
		} else if (character.getFields().getPrayerBook() == PrayerBook.CURSES) {
			if (prayerActive[CombatCurses.LEECH_DEFENCE])
				return .05;
			if (prayerActive[CombatCurses.TURMOIL])
				return .25;
		}
		return 0;
	}
	
	public static double getRanged(Player player) {
		double totalBonus = 1;
		int ranged = player.getSkillManager().getCurrentLevel(Skill.RANGED);
		boolean[] prayerActive = player.getFields().getPrayerActive();
		if (prayerActive[CombatPrayers.SHARP_EYE])
			totalBonus = (ranged * .05) > 1 ? (ranged * .05) : 1;
		else if (prayerActive[CombatPrayers.HAWK_EYE])
			totalBonus = (ranged * .1) > 2 ? (ranged * .1) : 2;
		else if (prayerActive[CombatPrayers.EAGLE_EYE])
			totalBonus = (ranged * .15);
		else if (prayerActive[CombatPrayers.RIGOUR])
			totalBonus = (ranged * .2);
		return totalBonus;
	}
	
	public static double getMagic(Player player) {
		double totalBonus = 1;
		int magic = player.getSkillManager().getCurrentLevel(Skill.MAGIC);
		boolean[] prayerActive = player.getFields().getPrayerActive();
		if (prayerActive[CombatPrayers.MYSTIC_WILL])
			totalBonus = (magic * .05) > 1 ? (magic * .05) : 1;
		else if (prayerActive[CombatPrayers.MYSTIC_LORE])
			totalBonus = (magic * .1) > 2 ? (magic * .1) : 2;
		else if (prayerActive[CombatPrayers.MYSTIC_MIGHT])
			totalBonus = (magic * .15);
		else if (prayerActive[CombatPrayers.AUGURY])
			totalBonus = (magic * .2);
		return totalBonus;
	}
}
