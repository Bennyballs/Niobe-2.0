package org.niobe.model;

/**
 * Represents a player's magic spellbook.
 * 
 * @author relex lawl
 */
public enum MagicSpellBook {
	
	NORMAL(1151, "You sense a surge of power flow through your body!"),
	ANCIENT(-1, ""),
	LUNAR(-1, "");
	
	/**
	 * The MagicSpellBook constructor.
	 * @param interfaceId	The spellbook's interface id.
	 * @param message		The message received upon switching to said spellbook.
	 */
	private MagicSpellBook(int interfaceId, String message) {
		this.interfaceId = interfaceId;
		this.message = message;
	}
	
	/**
	 * The spellbook's interface id
	 */
	private final int interfaceId;
	
	/**
	 * The message received upon switching to said spellbook.
	 */
	private final String message;
	
	/**
	 * Gets the interface to switch tab interface to.
	 * @return	The interface id of said spellbook.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}
	
	/**
	 * Gets the message gotten when switching to said spellbook.
	 * @return	The message received.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Gets the MagicSpellBook for said id.
	 * @param id	The ordinal of the SpellBook to fetch.
	 * @return		The MagicSpellBook who's ordinal is equal to id.
	 */
	public static MagicSpellBook forId(int id) {
		for (MagicSpellBook book : MagicSpellBook.values()) {
			if (book.ordinal() == id) {
				return book;
			}
		}
		return NORMAL;
	}
}
