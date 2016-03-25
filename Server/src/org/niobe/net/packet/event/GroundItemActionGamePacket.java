package org.niobe.net.packet.event;

import org.niobe.GameServer;
import org.niobe.model.Item;
import org.niobe.model.Position;
import org.niobe.model.action.DestinationListener;
import org.niobe.model.action.impl.GroundItemClickGameAction;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.GroundItem;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * used when a {@link org.niobe.world.Player} clicks on a {@link org.niobe.world.GroundItem}.
 *
 * @author relex lawl
 */
public final class GroundItemActionGamePacket implements GamePacketEvent {

	@Override
	public void read(final Player player, GamePacket packet) {
		final int y = packet.readLEShort();
		final int itemId = packet.readShort();
		final int x = packet.readLEShort();
		final Position position = new Position(x, y, player.getPosition().getZ());
		GroundItem item = GroundItem.find(itemId, position);
		if (item != null && item.getOwner() != null) {
			item = GroundItem.find(itemId, position, player);
		}
		final GroundItem groundItem = item;
		if (groundItem == null)
			return;
		player.setAction(new GroundItemClickGameAction<Player>(player, groundItem, new DestinationListener() {
			@Override
			public void reachDestination() {
				if (groundItem != null && groundItem.getItem().getId() == itemId) {
					if (!player.getInventory().isFull()) {
						Item item = groundItem.getItem();
						GameServer.getWorld().unregister(groundItem);
						player.getInventory().add(item);
					} else {
						player.getInventory().sendContainerFullMessage();
					}
				}
			}
		}));
	}
}
