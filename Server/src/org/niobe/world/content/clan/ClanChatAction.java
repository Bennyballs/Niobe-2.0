package org.niobe.world.content.clan;

/**
 * This interface handles {@link ClanChat} actions 
 * that will call {@link #execute()} once a scheduled
 * task hits 60 {@link org.niobe.world.util.GameConstants #GAME_TICK}s.
 *
 * @author relex lawl
 */
public interface ClanChatAction {

	/**
	 * Executes the content of said {@link ClanChatAction}
	 * for the associated {@link ClanChat}.
	 */
	public void execute();
}
