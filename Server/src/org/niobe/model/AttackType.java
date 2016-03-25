package org.niobe.model;

/**
 * Represents a {@link GameCharacter}'s attack style.
 * 
 * @author relex lawl
 */
public enum AttackType {

	/**
	 * Close, hand-to-hand combat.
	 */
	MELEE,
	
	/**
	 * Far-ranged attacks with ammunition.
	 */
	RANGED,
	
	/**
	 * Far-ranged magical attacks.
	 */
	MAGIC,
	
	/**
	 * Medium-ranged magical fire attacks from dragons.
	 */
	DRAGON_FIRE;
}
