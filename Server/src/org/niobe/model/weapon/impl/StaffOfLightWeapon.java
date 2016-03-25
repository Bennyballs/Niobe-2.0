package org.niobe.model.weapon.impl;

import org.niobe.model.Animation;
import org.niobe.model.weapon.Weapon;
import org.niobe.model.weapon.WeaponAnimation;
import org.niobe.model.weapon.WeaponCombatDefinition;
import org.niobe.world.content.combat.special.SpecialAttack;
import org.niobe.world.content.combat.special.impl.StaffOfLightSpecialAttack;

/**
 * An implementation of {@link org.niobe.model.weapon.Weapon} 
 * representing a staff of light.
 *
 * @author relex lawl
 */
public final class StaffOfLightWeapon extends Weapon {

	/**
	 * The StaffOfLightWeapon constructor.
	 * @param id	The weapon id.
	 */
	public StaffOfLightWeapon(int id) {
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
	public SpecialAttack getSpecialAttack() {
		return SPECIAL_ATTACK;
	}

	/**
	 * The interface id's used for the weapon
	 * game frame tab.
	 */
	private static final int[] INTERFACE_ID = {
		2276,
		7574,
		7586,
		2279,
		2280,
		7562
	};
	
	/**
	 * The animations used for the weapon.
	 */
	private static final int[] ANIMATIONS = {
		12010, //stand
		12012, //walk
		12016, //run
		12014, //turn
		12014, //turnLeft
		12015, //turnRight
		12015, //completeTurn
	};
	
	/**
	 * The attack buttons located in the weapon
	 * game frame tab.
	 */
	private static final int[] ATTACK_BUTTON = {
		2282,
		2285,
		2284,
		2283
	};
	
	/**
	 * The combat styles correspondent to {@code ATTACK_BUTTON}.
	 */
	private static final CombatStyle[] COMBAT_ATTACK_STYLE = {
		CombatStyle.STAB,
		CombatStyle.STAB,
		CombatStyle.SLASH,
		CombatStyle.STAB
	};
	
	/**
	 * The attack styles correspondent to {@code ATTACK_BUTTON}.
	 */
	private static final AttackStyle[] ATTACK_STYLE = {
		AttackStyle.ACCURATE,
		AttackStyle.AGGRESSIVE,
		AttackStyle.AGGRESSIVE,
		AttackStyle.DEFENSIVE
	};
	
	/**
	 * The weapon experience style correspondent 
	 * to {@code ATTACK_BUTTON}.
	 */
	private static final WeaponExperienceStyle[] ATTACK_EXPERIENCE = {
		WeaponExperienceStyle.ATTACK,
		WeaponExperienceStyle.STRENGTH,
		WeaponExperienceStyle.STRENGTH,
		WeaponExperienceStyle.DEFENCE
	};
	
	/**
	 * The animations used by the weapon while used
	 * in combat.
	 */
	private static final Animation[] ATTACK_ANIMATION = {
		new Animation(13044),
		new Animation(13047),
		new Animation(13048),
		new Animation(13049)
	};
	
	/**
	 * The speed of the weapon.
	 */
	private static final WeaponSpeed SPEED = WeaponSpeed.MEDIUM;
	
	/**
	 * The animation used when wielder is being attacked.
	 */
	private static final Animation BLOCK_ANIMATION = new Animation(12806);
	
	/**
	 * The special attack this weapon uses.
	 */
	private static final SpecialAttack SPECIAL_ATTACK = new StaffOfLightSpecialAttack();
	
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
