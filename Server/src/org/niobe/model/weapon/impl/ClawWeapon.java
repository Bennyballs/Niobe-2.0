package org.niobe.model.weapon.impl;

import org.niobe.model.Animation;
import org.niobe.model.weapon.Weapon;
import org.niobe.model.weapon.WeaponAnimation;
import org.niobe.model.weapon.WeaponCombatDefinition;

/**
 * An implementation of {@link org.niobe.model.weapon.Weapon} 
 * representing a pair of claws.
 *
 * @author relex lawl
 */
public class ClawWeapon extends Weapon {

	/**
	 * The ClawWeapon constructor.
	 * @param id	The weapon id.
	 */
	public ClawWeapon(int id) {
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
		7762,
		0,
		0,
		7765,
		7766
	};
	
	/**
	 * The animations used for the weapon.
	 */
	private static final int[] ANIMATIONS = {
		808,
		819,
		824,
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
		7768,
		7771,
		7770, 
		7769
	};
	
	/**
	 * The combat styles correspondent to {@code ATTACK_BUTTON}.
	 */
	private static final CombatStyle[] COMBAT_ATTACK_STYLE = {
		CombatStyle.SLASH,
		CombatStyle.SLASH,
		CombatStyle.STAB,
		CombatStyle.SLASH
	};
	
	/**
	 * The attack styles correspondent to {@code ATTACK_BUTTON}.
	 */
	private static final AttackStyle[] ATTACK_STYLE = {
		AttackStyle.ACCURATE,
		AttackStyle.AGGRESSIVE,
		AttackStyle.CONTROLLED,
		AttackStyle.DEFENSIVE,
	};
	
	/**
	 * The weapon experience style correspondent 
	 * to {@code ATTACK_BUTTON}.
	 */
	private static final WeaponExperienceStyle[] ATTACK_EXPERIENCE = {
		WeaponExperienceStyle.ATTACK,
		WeaponExperienceStyle.STRENGTH,
		WeaponExperienceStyle.MELEE_SHARED,
		WeaponExperienceStyle.DEFENCE
	};
	
	/**
	 * The animations used by the weapon while used
	 * in combat.
	 */
	private static final Animation[] ATTACK_ANIMATION = {
		new Animation(393),
		new Animation(393),
		new Animation(393),
		new Animation(393)
	};
	
	/**
	 * The speed of the weapon.
	 */
	private static final WeaponSpeed SPEED = WeaponSpeed.FAST;

	/**
	 * The animation used when wielder is being attacked.
	 */
	private static final Animation BLOCK_ANIMATION = new Animation(397);
	
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
