package org.niobe.model;

/**
 * Represents a {@link org.niobe.world.Player}'s 
 * privilege rights.
 * 
 * @author relex lawl
 */
public enum PlayerRights {

	/**
	 * A regular member of the server.
	 */
	PLAYER,
	
	/**
	 * A member who has donated to the server. 
	 */
	DONATOR,
	
	/**
	 * A moderator who has more privilege than other regular members and donators.
	 */
	MODERATOR,
	
	/**
	 * The highest-privileged member of the server.
	 */
	ADMINISTRATOR;
	
	/**
	 * Gets the rank for a certain id.
	 * 
	 * @param id	The id (ordinal()) of the rank.
	 * @return		rights.
	 */
	public static PlayerRights forId(int id) {
		for (PlayerRights rights : PlayerRights.values()) {
			if (rights.ordinal() == id) {
				return rights;
			}
		}
		return null;
	}
}
