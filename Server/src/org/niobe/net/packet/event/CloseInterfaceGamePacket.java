package org.niobe.net.packet.event;

import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * that is received when a {@link org.niobe.world.Player} exits out of an
 * in-game interface.
 *
 * @author relex lawl
 */
public final class CloseInterfaceGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		player.getFields().setBanking(false).setItemToSell(null)
				.setShop(null).setDialogue(null);
	}
}
