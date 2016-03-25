package org.niobe.net.security.credential;

import org.niobe.net.security.IsaacRandomPair;

/**
 * This object contains all the credentials retrieved from 
 * the login protocol.
 *
 * @author relex lawl
 */
public final class LoginCredential {

	/**
	 * The LoginCredential constructor.
	 * @param credentials		The player credentials used for this login.
	 * @param isaacs			The encode and decode number generators for the login.
	 * @param lowMemory			Checks if the client is using low memory.
	 * @param reconnecting		Checks if player has lost connection and is reconnecting.
	 * @param revision			The revision of the client.
	 * @param crcs				The cache archive crcs.
	 */
	public LoginCredential(PlayerCredential credentials, IsaacRandomPair isaacs, boolean lowMemory, 
							boolean reconnecting, int revision, int[] crcs) {
		this.credentials = credentials;
		this.isaacs = isaacs;
		this.lowMemory = lowMemory;
		this.reconnecting = reconnecting;
		this.revision = revision;
		this.crcs = crcs;
	}
	
	/**
	 * The player credentials.
	 */
	private final PlayerCredential credentials;
	
	/**
	 * The IsaacRandomPair used for encoding and decoding.
	 */
	private final IsaacRandomPair isaacs;
	
	/**
	 * Flag that checks if client is running on low memory usage.
	 */
	private final boolean lowMemory;
	
	/**
	 * Flag that checks if player has lost connection
	 * and is reconnecting.
	 */
	private final boolean reconnecting;
	
	/**
	 * The client's revision.
	 */
	private final int revision;
	
	/**
	 * The cache archive crcs.
	 */
	private final int[] crcs;
	
	/**
	 * Gets the player credentials.
	 * @return	The credentials.
	 */
	public PlayerCredential getCredentials() {
		return credentials;
	}
	
	/**
	 * Gets the pair of isaac random.
	 * @return	The IsaacRandomPair used for encoding and decoding.
	 */
	public IsaacRandomPair getIsaacs() {
		return isaacs;
	}
	
	/**
	 * Checks if client is running on low memory usage.
	 * @return	If <code>true</code> client is using low memory.
	 */
	public boolean isLowMemory() {
		return lowMemory;
	}
	
	/**
	 * Checks if player is reconnecting from a lost connection
	 * state.
	 * @return	If <code>true</code> player is regaining connection.
	 */
	public boolean isReconnecting() {
		return reconnecting;
	}
	
	/**
	 * Gets the client's revision number.
	 * @return	The revision client is using.
	 */
	public int getRevision() {
		return revision;
	}
	
	/**
	 * Gets the cache's archive crcs.
	 * @return	The archive crcs.
	 */
	public int[] getCrcs() {
		return crcs;
	}
}
