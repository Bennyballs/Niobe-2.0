package org.niobe.net.login;

/**
 * This file contains all information related
 * to niobe's login protocol.
 *
 * @author relex lawl
 */
public final class LoginConstants {
	
	/**
	 * This opcode is used for data (session keys, name, password, etc)
	 * exchange.
	 */
	public static final int LOGIN_EXCHANGE_DATA = 0;
	
	/**
	 * This login opcode is used for a 2000ms delay,
	 * after which a reconnection is attempted.
	 */
	public static final int LOGIN_DELAY = 1;

	/**
	 * This login opcode signifies a successful login.
	 */
	public static final int LOGIN_SUCCESSFUL = 2;
	
	/**
	 * This login opcode is used when the player
	 * has entered an invalid username and/or password.
	 */
	public static final int LOGIN_INVALID_CREDENTIALS = 3;
	
	/**
	 * This login opcode is used when the account
	 * has been disabled.
	 */
	public static final int LOGIN_DISABLED_ACCOUNT = 4;
	
	/**
	 * This login opcode is used when the account
	 * attempting to connect is already online in the server.
	 */
	public static final int LOGIN_ACCOUNT_ONLINE = 5;
	
	/**
	 * This login opcode is used when the game has been or
	 * is being updated.
	 */
	public static final int LOGIN_GAME_UPDATE = 6;
	
	/**
	 * This login opcode is used when the world being
	 * connected to is full.
	 */
	public static final int LOGIN_WORLD_FULL = 7;
	
	/**
	 * This login opcode is used when server is offline.
	 */
	public static final int LOGIN_SERVER_OFFLINE = 8;
	
	/**
	 * This login opcode is used when the connections
	 * from an ip address has exceeded {@link org.niobe.net.NetworkConstants.MAXIMUM_CONNECTIONS}.
	 */
	public static final int LOGIN_CONNECTION_LIMIT = 9;
	
	/**
	 * This login opcode is used when a connection
	 * has received a bad session id.
	 */
	public static final int LOGIN_BAD_SESSION_ID = 10;
	
	/**
	 * This login opcode is used when the login procedure
	 * has rejected the session.
	 */
	public static final int LOGIN_REJECT_SESSION = 11;
	
	/**
	 * This login opcode is used when a non-member player
	 * is attempting to login to a members world.
	 */
	public static final int LOGIN_WORLD_MEMBER_ACCOUNT_REQUIRED = 12;
	
	/**
	 * This login opcode is used when the login procedure 
	 * could not be completed.
	 */
	public static final int LOGIN_COULD_NOT_COMPLETE = 13;
	
	/**
	 * This login opcode is used when the game is being updated.
	 */
	public static final int LOGIN_WORLD_UPDATE = 14;
	
	/**
	 * This login opcode is received upon a reconnection
	 * so that chat messages will not dissappear.
	 */
	public static final int LOGIN_RECONNECTION = 15;
	
	/**
	 * This login opcode is used when a player
	 * has exceeded their attempts to login.
	 */
	public static final int LOGIN_EXCESSIVE_ATTEMPTS = 16;
	
	/**
	 * This login opcode is used when a member is attempting
	 * to login to a non-members world in a members-only area.
	 */
	public static final int LOGIN_AREA_MEMBER_ACCOUNT_REQUIRED = 17;
	
	/**
	 * This login opcode is used when the login server
	 * is invalid.
	 */
	public static final int LOGIN_INVALID_LOGIN_SERVER = 20;
	
	/**
	 * This login opcode is used when a player has just
	 * left another world and has to wait a delay before
	 * entering a new world.
	 */
	public static final int LOGIN_WORLD_DELAY = 21;
	
	/**
	 * The new connection login request
	 * byte data sent by the client.
	 */
	public static final int NEW_CONNECTION_LOGIN_REQUEST = 16;
	
	/**
	 * The reconnection login request
	 * byte data sent by the client.
	 */
	public static final int RECONNECTING_LOGIN_REQUEST = 18;
}
