package org.niobe.model.weapon.impl;

import org.niobe.model.Animation;
import org.niobe.model.weapon.Weapon;
import org.niobe.model.weapon.WeaponAnimation;
import org.niobe.model.weapon.WeaponCombatDefinition;

/**
 * An implementation of {@link org.niobe.model.weapon.Weapon} 
 * representing a staff.
 *
 * @author relex lawl
 */
public final class StaffWeapon extends Weapon {

	/**
	 * The StaffWeapon constructor.
	 * @param id	The weapon id.
	 */
	public StaffWeapon(int id) {
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
		return COMBAT_DEFINITION;
	}
	
	/**
	 * The interface id's used for the weapon
	 * game frame tab.
	 */
	private static final int[] INTERFACE_ID = {
		328,
		0, //no special bar
		0, //no special bar
		331,
		3983
	};
	
	/**
	 * The animations used for the weapon.
	 */
	private static final int[] ANIMATIONS = {
		813,
		1205,
		1210,
		823,
		821,
		822,
		820
	};
	
	/**
	 * The attack buttons located in the weapon
	 * game frame tab.
	 */
	private static final int[] ATTACK_BUTTON = {
		336,
		335,
		334, 
	};
	
	/**
	 * The combat styles correspondent to {@code ATTACK_BUTTON}.
	 */
	private static final CombatStyle[] COMBAT_ATTACK_STYLE = {
		CombatStyle.STAB,
		CombatStyle.CRUSH,
		CombatStyle.CRUSH,
	};
	
	/**
	 * The attack styles correspondent to {@code ATTACK_BUTTON}.
	 */
	private static final AttackStyle[] ATTACK_STYLE = {
		AttackStyle.ACCURATE,
		AttackStyle.CONTROLLED,
		AttackStyle.DEFENSIVE
	};
	
	/**
	 * The weapon experience style correspondent 
	 * to {@code ATTACK_BUTTON}.
	 */
	private static final WeaponExperienceStyle[] ATTACK_EXPERIENCE = {
		WeaponExperienceStyle.ATTACK,
		WeaponExperienceStyle.STRENGTH,
		WeaponExperienceStyle.DEFENCE
	};
	
	/**
	 * The animations used by the weapon while used
	 * in combat.
	 */
	private static final Animation[] ATTACK_ANIMATION = {
		new Animation(400),
		new Animation(401),
		new Animation(401),
	};
	
	/**
	 * The speed of the weapon.
	 */
	private static final WeaponSpeed SPEED = WeaponSpeed.MEDIUM;
	
	/**
	 * The animation used when wielder is being attacked.
	 */
	private static final Animation BLOCK_ANIMATION = new Animation(403);
	
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