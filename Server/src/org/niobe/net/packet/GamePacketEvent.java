package org.niobe.net.packet;

import org.niobe.world.Player;

/**
 * Represents an event that occurs when a player/connection
 * is sending information from client to the game server.
 *
 * @author relex lawl
 */

public interface GamePacketEvent {

	/**
	 * Reads the informations received by the event and
	 * instructs the server on what to do with said information.
	 * @param player	The player who set off the event.
	 * @param packet	The packet that was sent from the client.
	 */
	public void read(Player player, GamePacket packet);
}
