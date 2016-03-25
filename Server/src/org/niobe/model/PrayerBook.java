package org.niobe.model;

/**
 * Represents a player's prayer book.
 * 
 * @author relex lawl
 */
public enum PrayerBook {
	
	NORMAL(5608, "You sense a surge of purity flow through your body!"),
	CURSES(21356, "You sense a surge of power flow through your body!");
	
	/**
	 * The PrayerBook constructor.
	 * @param interfaceId	The interface id to switch prayer tab to.
	 * @param message		The message received upon switching prayers.
	 */
	private PrayerBook(int interfaceId, String message) {
		this.interfaceId = interfaceId;
		this.message = message;
	}
	
	/**
	 * The interface id to switch prayer tab to.
	 */
	private final int interfaceId;
	
	/**
	 * The message received upon switching prayers.
	 */
	private final String message;
	
	/**
	 * Gets the interface id to set prayer tab to.
	 * @return	The new prayer tab interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}
	
	/**
	 * Gets the message received when switching to said prayer book.
	 * @return	The message player will receive.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Gets the PrayerBook instance for said id.
	 * @param id	The id to match to prayer book's ordinal.
	 * @return		The prayerbook who's ordinal is equal to id.
	 */
	public static PrayerBook forId(int id) {
		for (PrayerBook book : PrayerBook.values()) {
			if (book.ordinal() == id) {
				return book;
			}
		}
		return NORMAL;
	}
}
