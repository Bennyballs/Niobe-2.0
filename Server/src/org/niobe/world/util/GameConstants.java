package org.niobe.world.util;

import org.niobe.model.Position;

/**
 * Stores constants that will be used through out
 * the game.
 *
 * @author relex lawl
 */
public final class GameConstants {
	
	/**
	 * The delay in which the game ticks are set to
	 * in millisecond time unit.
	 */
    public static final int GAME_TICK = 600;
    
    /**
     * The server name that will be used through out
     * the game.
     */
    public static final String SERVER_NAME = "Niobe";
    
    /**
     * The amount of {@link #GAME_TICK}s for all of the
     * {@link org.niobe.world.World#getGroundItems()} to either
     * appear or disappear.
     */
    public static final int GROUND_ITEM_DELAY = 25;

	/**
	 * The maximum amount of players allowed in the world.
	 */
	public static final int MAX_PLAYERS = 2047;
	
	/**
	 * The maximum amount of npcs allowed in the world.
	 */
	public static final int MAX_MOBS = 2000;
	
	/**
	 * The maximum amount of ground items allowed in the world.
	 */
	public static final int MAX_GROUND_ITEMS = 2000;
	
	/**
	 * The maximum amount of game objects allowed in the world.
	 */
	public static final int MAX_GAME_OBJECTS = 2000;
	
	/**
	 * The maximum amount of projectiles allowed in the world.
	 */
	public static final int MAX_PROJECTILES = 5000;
	
	/**
	 * The default position a {@link org.niobe.model.GameCharacter}
	 * will be set to upon first login.
	 */
	public static final Position DEFAULT_POSITION = new Position(3087, 3493, 0);
	
	/**
	 * The amount of player kill points to give to a player when
	 * they kill another player.
	 */
	public static final int PK_POINTS_ADDITION = 1;
	
	/**
	 * The tab indexes.
	 */
	public static final int ATTACK_TAB = 0, ACHIEVEMENT_TAB = 1, SKILLS_TAB = 2, QUESTS_TAB = 3,
							INVENTORY_TAB = 4, EQUIPMENT_TAB = 5, PRAYER_TAB = 6, MAGIC_TAB = 7,
							SUMMONING_TAB = 8, RELATIONS_TAB = 9, CLAN_CHAT_TAB = 10,
							FRIENDS_CLAN_CHAT_TAB = 11, OPTIONS_TAB = 12, EMOTES_TAB = 13,
							MUSIC_TAB = 14, NOTES_TAB = 15;
	
	/**
	 * The side bar interface id's in their respective order.
	 */
	public static final int[] SIDEBAR_INTERFACES = {
		2423, //attack
		2000, //achievement
		31110, //skills
		638, //quest
		3213, //inventory
		1644, //equipment
		5608, //prayer
		1151, //spellbook
		-1, //summoning
		5065, //relations tab
		18128, //clan chat
		18128, //friends clan chat
		904, //options
		147, //emote
		2003, //music
		173, //notes tab
		2449 //logout
	};
}
