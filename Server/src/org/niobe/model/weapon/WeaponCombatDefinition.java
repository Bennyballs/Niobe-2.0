package org.niobe.model.weapon;

import org.niobe.model.Animation;
import org.niobe.model.weapon.Weapon.AttackStyle;
import org.niobe.model.weapon.Weapon.CombatStyle;
import org.niobe.model.weapon.Weapon.WeaponSpeed;
import org.niobe.model.weapon.Weapon.WeaponExperienceStyle;

/**
 * Represents a {@link Weapon}'s combat attributes, such as their delay, attack style,
 * experience style and combat-related animations.
 * 
 * @author relex lawl
 */
public final class WeaponCombatDefinition {

	/**
	 * The WeaponCombatDefinition constructor.
	 * @param speed					The weapon's attack speed. Determines the speed at which the associated player will attack their opponent.
	 * @param strength				The weapon's strength.
	 * @param button				The button id for each attack style/type.
	 * @param combatStyle			The combat attack style, in respective order with their button id.
	 * @param experience			The experience style, in respective order with the attack style.
	 * @param attackAnimation		The attack animations, in respective order with attack styles.
	 * @param blockAnimation		The animation the associated player will perform when being damaged.
	 */
	public WeaponCombatDefinition(WeaponSpeed speed, int[] button, CombatStyle[] combatStyle, AttackStyle[] attackStyle, WeaponExperienceStyle[] experience, Animation[] attackAnimation, Animation blockAnimation) {
		this.speed = speed;
		this.button = button;
		this.combatStyle = combatStyle;
		this.attackStyle = attackStyle;
		this.experience = experience;
		this.attackAnimation = attackAnimation;
		this.blockAnimation = blockAnimation;
	}
	
	/**
	 * The weapon's attack speed. Determines the speed at which the associated player will attack their opponent.
	 */
	private WeaponSpeed speed;

	/**
	 * The button id for each attack style/type.
	 */
	private final int[] button;
	
	/**
	 * The combat attack style, in respective order with their button id.
	 */
	private final CombatStyle[] combatStyle;
	
	/**
	 * The attack style, in respective order with the combat style.
	 */
	private final AttackStyle[] attackStyle;
	
	/**
	 * The experience style, in respective order with the attack style.
	 */
	private final WeaponExperienceStyle[] experience;
	
	/**
	 * The attack animations, in respective order with attack styles.
	 */
	private final Animation[] attackAnimation;
	
	/**
	 * The animation the associated player will perform when being damaged.
	 */
	private final Animation blockAnimation;
	
	/**
	 * Gets the weapon's attack delay.
	 * @return	The attack delay.
	 */
	public WeaponSpeed getSpeed() {
		return speed;
	}
	
	/**
	 * Sets the weapon's speed. Used to not make
	 * another {@link WeaponCombatDefinition} just to change
	 * speed.
	 */
	public WeaponCombatDefinition setSpeed(WeaponSpeed speed) {
		this.speed = speed;
		return this;
	}
	
	/**
	 * Gets the weapon's attack button id's.
	 * @return	The id's of the attack style buttons.
	 */
	public int[] getButton() {
		return button;
	}

	/**
	 * Gets the style in respective order to the button array.
	 * @return	The combat attack styles of the weapon.
	 */
	public CombatStyle[] getCombatStyle() {
		return combatStyle;
	}
	
	/**
	 * Gets the attack style in respective order to the button array.
	 * @return	The attack style.
	 */
	public AttackStyle[] getAttackStyle() {
		return attackStyle;
	}

	/**
	 * Gets the experience style in respective order to the style order.
	 * @return	The experience styles of the weapon.
	 */
	public WeaponExperienceStyle[] getExperience() {
		return experience;
	}
	
	/**
	 * Gets the weapon's attack animations in respective order to the attack styles.
	 * @return	The animations of the weapon.
	 */
	public Animation[] getAnimations() {
		return attackAnimation;
	}

	/**
	 * Gets the weapon's block animation player will perform when being damaged.
	 * @return	The weapon's block animation.
	 */
	public Animation getBlockAnimation() {
		return blockAnimation;
	}
}
