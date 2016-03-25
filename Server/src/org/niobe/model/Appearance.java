package org.niobe.model;

import org.niobe.model.UpdateFlag.Flag;
import org.niobe.world.Player;

/**
 * Represents a {@link org.niobe.world.Player}
 * in-game appearance that other {@link org.niobe.world.Player}s
 * located in {@link org.niobe.world.World#getPlayers()} will be able to
 * see.
 * 
 * @author relex lawl
 */
public final class Appearance {
	
	/**
	 * The Appearance constructor, also sets
	 * the player's default clothing.
	 * @param player	The associated player.
	 */
	public Appearance(Player player) {
		this.player = player;
		set();
	}
	
	/**
	 * Player's current gender.
	 */
	private Gender gender = Gender.MALE;
	
	/**
	 * The player's head icon hint.
	 */
	private int headHint = -1;
	
	/**
	 * The player's head skull hint.
	 */
	private int skullHint = -1;
	
	/**
	 * Gets the player's gender.
	 * @return	gender.
	 */
	public Gender getGender() {
		return gender;
	}
	
	/**
	 * Sets the player's gender.
	 * @param gender	Gender to set to.
	 */
	public Appearance setGender(Gender gender) {
		this.gender = gender;
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		return this;
	}
	
	/**
	 * Gets the player's current head hint index.
	 * @return	The player's head hint.
	 */
	public int getHeadHint() {
		return headHint;
	}

	/**
	 * Sets the player's head icon hint.
	 * @param headHint	The hint index to use.
	 * @return			The Appearance instance.
	 */
	public Appearance setHeadHint(int headHint) {
		this.headHint = headHint;
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		return this;
	}

	/**
	 * Gets the player's current skull hint index.
	 * @return	The player's skull hint.
	 */
	public int getSkullHint() {
		return skullHint;
	}

	/**
	 * Sets the player's skull hint.
	 * @param skullHint	The skull hint index to use.
	 * @return			The Appearance instance.
	 */
	public Appearance setSkullHint(int skullHint) {
		this.skullHint = skullHint;
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		return this;
	}

	/**
	 * Gets the look array, which is an array with 13 elements describing the
	 * look of a player.
	 * @return The look array.
	 */
	public int[] getLook() {
		return look;
	}
	
	/**
	 * Sets the look array.
	 * @param look The look array.
	 * @throws IllegalArgumentException if the array length is not 12.
	 */
	public void set(int[] look) {
		if(look.length != 12) {
			throw new IllegalArgumentException("Array length must be 12.");
		}
		this.look = look;
		player.getUpdateFlag().flag(Flag.APPEARANCE);
	}
	
	/**
	 * Sets a specific look.
	 * @param index		Array index to set.
	 * @param look		Value to change look[index] to.
	 */
	public void set(int index, int look) {
		this.look[index] = look;
		player.getUpdateFlag().flag(Flag.APPEARANCE);
	}
	
	/**
	 * The player's current character clothing.
	 */
	private int[] look = new int[13];
	
	/**
	 * Sets the player's default clothing.
	 */
	public void set() {
		if (gender == Gender.MALE) {
			look[HEAD] = 3;
			look[CHEST] = 18;
			look[ARMS] = 26;
			look[HANDS] = 34;
			look[LEGS] = 38;
			look[FEET] = 42;
			look[BEARD] = 14;
		} else {
			look[HEAD] = 48;
			look[CHEST] = 57;
			look[ARMS] = 65;
			look[HANDS] = 68;
			look[LEGS] = 77;
			look[FEET] = 80;
			look[BEARD] = 57;
		}
		look[HAIR_COLOUR] = 7;
		look[TORSO_COLOUR] = 8;
		look[LEG_COLOUR] = 9;
		look[FEET_COLOUR] = 5;
		look[SKIN_COLOUR] = 0;
		player.getUpdateFlag().flag(Flag.APPEARANCE);
	}
	
	/**
	 * The associated player.
	 */
	private final Player player;
	
	/**
	 * Represents a player's sex also known
	 * as gender.
	 * 
	 * @author relex lawl
	 */

	public enum Gender {
		/**
		 * A masculine character.
		 */
		MALE,
		
		/**
		 * A femenine character.
		 */
		FEMALE;
	}


	/**
	 * The index of said body part color in the look array.
	 */
	public static final int HAIR_COLOUR = 1, TORSO_COLOUR = 2, LEG_COLOUR = 3, FEET_COLOUR = 4, SKIN_COLOUR = 5,
							HEAD = 6, CHEST = 7, ARMS = 8, HANDS = 9, LEGS = 10, FEET = 11, BEARD = 12;
}
