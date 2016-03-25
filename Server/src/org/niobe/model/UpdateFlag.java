package org.niobe.model;

import java.util.BitSet;

/**
 * Represents a {@link GameCharacter}'s update flags
 * used in the updating procedure.
 * 
 * @author relex lawl
 */
public final class UpdateFlag {
	
	/**
	 * A set containing the entity's update flags.
	 */
	private BitSet flags =  new BitSet();
	
	/**
	 * Checks if {@code flag} is contained in the entity's flag set.
	 * @param flag	The flag to check.
	 * @return		The flags set contains said flag.
	 */
	public boolean flagged(Flag flag) {
		return flags.get(flag.ordinal());
	}
	
	/**
	 * Checks if an update is required by checking if flags set is empty.
	 * @return	Flags set is not empty.
	 */
	public boolean isUpdateRequired() {
		return !flags.isEmpty();
	}
	
	/**
	 * Puts a flag value into the flags set.
	 * @param flag	Flag to put into the flags set.
	 * @return		The UpdateFlag instance.
	 */
	public UpdateFlag flag(Flag flag) {
		flags.set(flag.ordinal(), true);
		return this;
	}
	
	/**
	 * Removes every flag in the flags set.
	 * @return	The UpdateFlag instance.
	 */
	public UpdateFlag reset() {
		flags.clear();
		return this;
	}
	
	/**
	 * Represents a Flag that a character entity can update.
	 * 
	 * @author relex lawl
	 */
	public enum Flag {
		/**
		 * This flag indicates the player is chatting.
		 */
		CHAT,
		
		/**
		 * This flag indicates the mob is chatting.
		 */
		FORCED_CHAT,
		
		/**
		 * This flag indicates the game character is
		 * forced to move to a specified {@link Position}.
		 */
		FORCED_MOVEMENT,
		
		/**
		 * This flag indicates the game character is
		 * interacting with an {@link Entity} in the world.
		 */
		ENTITY_INTERACTION,
		
		/**
		 * This flag indicates the game character
		 * is going to face a {@link Position}.
		 */
		FACE_POSITION,
		
		/**
		 * This flag indicates the player is
		 * changing his appearance.
		 */
		APPEARANCE,
		
		/**
		 * This flag indicates the game character
		 * is performing an animation.
		 */
		ANIMATION,
		
		/**
		 * This flag indicates the game character
		 * is performing a graphic.
		 */
		GRAPHIC,
		
		/**
		 * This flag indicates the game character
		 * is being damaged by a single hit. 
		 */
		SINGLE_HIT,
		
		/**
		 * This flag indicates the game character
		 * is being damaged by a double hit. 
		 */
		DOUBLE_HIT,
		
		/**
		 * This flag indicates the mob is
		 * going to transform into another mob type.
		 */
		TRANSFORM;
	}
}
