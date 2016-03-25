package org.niobe.net.security.credential;

import org.niobe.util.NameUtil;

/**
 * This object holds information retrieved from a player's
 * login connection.
 *
 * @author relex lawl
 */
public final class PlayerCredential {

	/**
	 * The PlayerCredential constructor.
	 * @param username			The player's username.
	 * @param password			The player's password.
	 * @param usernameHash		The player's username as a hash.
	 * @param uid				The player's unique id.
	 */
	public PlayerCredential(String username, String password, int usernameHash, int uid) {
		this.username = username;
		this.password = password;
		this.usernameHash = usernameHash;
		this.uid = uid;
		this.longUsername = NameUtil.stringToLong(username);
	}
	
	/**
	 * The user name used by the player.
	 */
	private String username;
	
	/**
	 * The {@link username} as a long.
	 */
	private long longUsername;
	
	/**
	 * The password used by the player.
	 */
	private String password;
	
	/**
	 * The player's username as a hash.
	 */
	private final int usernameHash;
	
	/**
	 * The player's unique id.
	 */
	private final int uid;
	
	/**
	 * Gets the player's user name.
	 * @return	The user name string.
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets the player's user name.
	 * @param username	The new user name.
	 */
	public PlayerCredential setUsername(String username) {
		this.username = username;
		return this;
	}
	
	/**
	 * Gets the player's {@link #username} as a long.
	 * @return	The user name as a long.
	 */
	public long getLongUsername() {
		return longUsername;
	}
	
	/**
	 * Gets the player's password.
	 * @return	The password string.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets the player's password.
	 * @param password	The new password.
	 */
	public PlayerCredential setPassword(String password) {
		this.password = password;
		return this;
	}
	
	/**
	 * Gets the player's username as a hash.
	 * @return	The username hash.
	 */
	public int getUsernameHash() {
		return usernameHash;
	}
	
	/**
	 * Gets the player's unique id.
	 * @return	The unique id.
	 */
	public int getUniqueIdentifier() {
		return uid;
	}
}
