package org.niobe.net.packet.event;

import org.niobe.GameServer;
import org.niobe.model.Item;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.GroundItem;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * used for dropping {@link org.niobe.model.Item}s and then registering
 * a new {@link org.niobe.world.GroundItem} into the {@link org.niobe.world.World}.
 *
 * @author relex lawl
 */
public final class DropItemGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		if (player.getFields().isDead())
			return;
		@SuppressWarnings("unused")
		int itemIndex = packet.readUnsignedShortA();
		@SuppressWarnings("unused")
		int interfaceIndex = packet.readUnsignedShort();
		int itemSlot = packet.readUnsignedShortA();
		Item item = player.getInventory().getItems()[itemSlot];
		if (item.getId() != -1) {
			GroundItem groundItem = new GroundItem(item, player.getPosition().copy(), player);
			GameServer.getWorld().register(groundItem);
			player.getInventory().setItem(itemSlot, new Item(-1, 0));
			player.getInventory().refreshItems();
		}
	}

}
