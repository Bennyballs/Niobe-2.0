package org.niobe.model.weapon;

import org.niobe.model.Animation;

/**
 * Represents a weapon's animation set.
 * 
 * @author relex lawl
 */
public final class WeaponAnimation {

	/**
	 * The WeaponAnimation constructor.
	 * @param standId			The stand animation id the weapon has.
	 * @param walkId			The walk animation id the weapon has.
	 * @param runId				The run animation id the weapon has.
	 * @param turnId			The turn animation id the weapon has.
	 * @param turnLeftId		The left turn animation id the weapon has.
	 * @param turnRightId		The right turn animation id the weapon has.
	 * @param completeTurnId	The 180 degree turn animation id the weapon has.
	 */
	public WeaponAnimation(int standId, int walkId, int runId, int turnId, int turnLeftId, int turnRightId, int completeTurnId) {
		this.stand = new Animation(standId);
		this.walk = new Animation(walkId);
		this.run = new Animation(runId);
		this.turn = new Animation(turnId);
		this.turnLeft = new Animation(turnLeftId);
		this.turnRight = new Animation(turnRightId);
		this.completeTurn = new Animation(completeTurnId);
	}

	/**
	 * The stand animation the player will perform whilst wielding the associated weapon.
	 */
	private final Animation stand;
	
	/**
	 * The walk animation the player will perform whilst wielding the associated weapon.
	 */
	private final Animation walk;
	
	/**
	 * The run animation the player will perform whilst wielding the associated weapon.
	 */
	private final Animation run;
	
	/**
	 * The turn animation the player will perform whilst wielding the associated weapon.
	 */
	private final Animation turn;
	
	/**
	 * The left turn animation the player will perform whilst wielding the associated weapon.
	 */
	private final Animation turnLeft;
	
	/**
	 * The right turn animation the player will perform whilst wielding the associated weapon.
	 */
	private final Animation turnRight;
	
	/**
	 * The 180 degree turn animation the player will perform whilst wielding the associated weapon.
	 */
	private final Animation completeTurn;
	
	/**
	 * Gets the associated weapon's stand animation.
	 * @return	The stand animation for player wielding associated weapon to perform.
	 */
	public Animation getStand() {
		return stand;
	}

	/**
	 * Gets the associated weapon's walk animation.
	 * @return	The walk animation for player wielding associated weapon to perform.
	 */
	public Animation getWalk() {
		return walk;
	}

	/**
	 * Gets the associated weapon's run animation.
	 * @return	The run animation for player wielding associated weapon to perform.
	 */
	public Animation getRun() {
		return run;
	}

	/**
	 * Gets the associated weapon's 90 degree turn animation.
	 * @return	The turn animation for player wielding associated weapon to perform.
	 */
	public Animation getTurn() {
		return turn;
	}

	/**
	 * Gets the associated weapon's left turn animation.
	 * @return	The left turn animation for player wielding associated weapon to perform.
	 */
	public Animation getLeftTurn() {
		return turnLeft;
	}

	/**
	 * Gets the associated weapon's right turn animation.
	 * @return	The right turn animation for player wielding associated weapon to perform.
	 */
	public Animation getRightTurn() {
		return turnRight;
	}

	/**
	 * Gets the associated weapon's 180 degree turn animation.
	 * @return	The 180 degree turn animation for player wielding associated weapon to perform.
	 */
	public Animation getCompleteTurn() {
		return completeTurn;
	}
}
