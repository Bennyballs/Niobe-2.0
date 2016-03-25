package org.niobe.net.packet.event;

import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * used to send the client's current screen size to the server for a
 * {@link org.niobe.world.Player}.
 *
 * #1 -> Fixed screen size
 * #2 -> Resizeable screen size
 * #3 -> Full screen size
 * 
 * @author relex lawl
 */
public final class ScreenSizeGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		int size = packet.readByte();
		player.getFields().setClientSize(size);
	}
}
