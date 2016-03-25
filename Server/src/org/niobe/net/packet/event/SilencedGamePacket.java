package org.niobe.net.packet.event;

import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent} that
 * is used for packets such as camera movement, which won't be needed.
 *
 * @author relex lawl
 */
public final class SilencedGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		
	}
}
