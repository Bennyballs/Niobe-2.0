package org.niobe.model.weapon.impl;

import org.niobe.model.Animation;
import org.niobe.model.weapon.Weapon;
import org.niobe.model.weapon.WeaponAnimation;
import org.niobe.model.weapon.WeaponCombatDefinition;

/**
 * An implementation of {@link org.niobe.model.weapon.Weapon} 
 * representing a halberd.
 *
 * @author relex lawl
 */
public class HalberdWeapon extends Weapon {

	/**
	 * The HalberdWeapon constructor.
	 * @param id	The weapon id.
	 */
	public HalberdWeapon(int id) {
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
	
	@Override
	public int getAttackDistance() {
		return 2;
	}

	/**
	 * The interface id's used for the weapon
	 * game frame tab.
	 */
	private static final int[] INTERFACE_ID = {
		8460,
		0,
		0,
		8463,
		8464
	};
	
	/**
	 * The animations used for the weapon.
	 */
	private static final int[] ANIMATIONS = {
		8980, //stand
		1146, //walk
		1210, //run
		0x337, //turn
		0x335, //turnLeft
		0x336, //turnRight
		0x334, //completeTurn
	};
	
	/**
	 * The attack buttons located in the weapon
	 * game frame tab.
	 */
	private static final int[] ATTACK_BUTTON = {
		8466,
		8468,
		8467,
	};
	
	/**
	 * The combat styles correspondent to {@code ATTACK_BUTTON}.
	 */
	private static final CombatStyle[] COMBAT_ATTACK_STYLE = {
		CombatStyle.STAB,
		CombatStyle.SLASH,
		CombatStyle.STAB,
	};
	
	/**
	 * The attack styles correspondent to {@code ATTACK_BUTTON}.
	 */
	private static final AttackStyle[] ATTACK_STYLE = {
		AttackStyle.CONTROLLED,
		AttackStyle.AGGRESSIVE,
		AttackStyle.DEFENSIVE
	};
	
	/**
	 * The weapon experience style correspondent 
	 * to {@code ATTACK_BUTTON}.
	 */
	private static final WeaponExperienceStyle[] ATTACK_EXPERIENCE = {
		WeaponExperienceStyle.MELEE_SHARED,
		WeaponExperienceStyle.STRENGTH,
		WeaponExperienceStyle.DEFENCE
	};
	
	/**
	 * The animations used by the weapon while used
	 * in combat.
	 */
	private static final Animation[] ATTACK_ANIMATION = {
		new Animation(428),
		new Animation(440),
		new Animation(428),
	};
	
	/**
	 * The speed of the weapon.
	 */
	private static final WeaponSpeed SPEED = WeaponSpeed.SLOWEST;
	
	/**
	 * The animation used when wielder is being attacked.
	 */
	private static final Animation BLOCK_ANIMATION = new Animation(430);
	
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
