package org.niobe.model.weapon.impl;

import org.niobe.model.Animation;
import org.niobe.model.weapon.Weapon;
import org.niobe.model.weapon.WeaponAnimation;
import org.niobe.model.weapon.WeaponCombatDefinition;
import org.niobe.world.content.combat.special.SpecialAttack;
import org.niobe.world.content.combat.special.impl.GraniteMaulSpecialAttack;

/**
 * An implementation of {@link org.niobe.model.weapon.Weapon} that
 * represents a granite maul.
 *
 * @author relex lawl
 */
public final class GraniteMaulWeapon extends Weapon {

	/**
	 * The GraniteMaulWeapon constructor.
	 * @param id	The weapon's item id.
	 */
	public GraniteMaulWeapon(int id) {
		super(id);
	}

	@Override
	public int[] getInterfaceId() {
		return INTERFACE_ID;
	}

	@Override
	public WeaponAnimation getAnimations() {
		return WEAPON_ANIMATION;
	}

	@Override
	public WeaponCombatDefinition getCombatDefinition() {
		return 	COMBAT_DEFINITION;
	}
	
	@Override
	public SpecialAttack getSpecialAttack() {
		return SPECIAL_ATTACK;
	}
	
	/**
	 * The interface id's used for the weapon
	 * game frame tab.
	 */
	private static final int[] INTERFACE_ID = {
		425,
		7474,
		7486,
		428,
		3983,
		7462
	};
	
	/**
	 * The animations used for the weapon.
	 */
	private static final int[] ANIMATIONS = {
		1662, //stand
		1663, //walk
		1664, //run
		0x334, //turn
		0x335, //turnLeft
		0x336, //turnRight
		0x334, //completeTurn
	};
	
	/**
	 * The attack buttons located in the weapon
	 * game frame tab.
	 */
	private static final int[] ATTACK_BUTTON = {
		433,
		432,
		431
	};
	
	/**
	 * The combat styles correspondent to {@code ATTACK_BUTTON}.
	 */
	private static final CombatStyle[] COMBAT_ATTACK_STYLE = {
		CombatStyle.CRUSH,
		CombatStyle.CRUSH,
		CombatStyle.CRUSH
	};
	
	/**
	 * The attack styles correspondent to {@code ATTACK_BUTTON}.
	 */
	private static final AttackStyle[] ATTACK_STYLE = {
		AttackStyle.ACCURATE,
		AttackStyle.ACCURATE,
		AttackStyle.DEFENSIVE
	};
	
	/**
	 * The weapon experience style correspondent 
	 * to {@code ATTACK_BUTTON}.
	 */
	private static final WeaponExperienceStyle[] ATTACK_EXPERIENCE = {
		WeaponExperienceStyle.STRENGTH,
		WeaponExperienceStyle.STRENGTH,
		WeaponExperienceStyle.MELEE_SHARED
	};
	
	/**
	 * The animations used by the weapon while used
	 * in combat.
	 */
	private static final Animation[] ATTACK_ANIMATION = {
		new Animation(1665),
		new Animation(1665),
		new Animation(1665),
	};
	
	/**
	 * The speed of the weapon.
	 */
	private static final WeaponSpeed SPEED = WeaponSpeed.SLOW;
	
	/**
	 * The animation used when wielder is being attacked.
	 */
	private final static Animation BLOCK_ANIMATION = new Animation(1666);
	
	/**
	 * The special attack used for this weapon.
	 */
	private static final SpecialAttack SPECIAL_ATTACK = new GraniteMaulSpecialAttack();

	/**
	 * The weapon animation instance to avoid creating new
	 * {@link org.niobe.model.weapon.Weapon.WeaponAnimation} objects.
	 */
	private static final WeaponAnimation WEAPON_ANIMATION = new WeaponAnimation(ANIMATIONS[0], ANIMATIONS[1], ANIMATIONS[2], ANIMATIONS[3], ANIMATIONS[4], ANIMATIONS[5], ANIMATIONS[6]);

	/**
	 * The weapon combat definition instance to avoid creating new
	 * {@link org.niobe.model.weapon.WeaponCombatDefinition} objects.
	 */
	private static final WeaponCombatDefinition COMBAT_DEFINITION = new WeaponCombatDefinition(SPEED, ATTACK_BUTTON, COMBAT_ATTACK_STYLE, ATTACK_STYLE, ATTACK_EXPERIENCE, ATTACK_ANIMATION, BLOCK_ANIMATION);
}
