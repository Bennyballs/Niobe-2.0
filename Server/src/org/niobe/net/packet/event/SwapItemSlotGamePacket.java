package org.niobe.net.packet.event;

import org.niobe.model.container.impl.Inventory;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * used when a {@link org.niobe.world.Player} swaps an {@link org.niobe.model.Item}
 * slot in an item container interface.
 *
 * @author relex lawl
 */
public final class SwapItemSlotGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		if (player.getFields().isDead())
			return;
		int interfaceId = packet.readLEShortA();
		packet.readByteC();
		int fromSlot = packet.readLEShortA();
		int toSlot = packet.readLEShort();
		if (fromSlot == toSlot)
			return;
		switch (interfaceId) {
		case Inventory.INTERFACE_ID:
			if(fromSlot >= 0 && fromSlot < player.getInventory().capacity() && toSlot >= 0 && toSlot < player.getInventory().capacity() && toSlot != fromSlot) {
				player.getInventory().swap(fromSlot, toSlot);
			}
			break;
		}
	}
}