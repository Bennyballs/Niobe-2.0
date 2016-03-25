package org.niobe.net.packet.event;

import java.util.logging.Logger;

import org.niobe.model.Item;
import org.niobe.model.container.impl.Shop;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.util.NameUtil;
import org.niobe.world.Player;
import org.niobe.world.content.clan.ClanChatManager;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * received when a player enters an input from a chat prompt. 
 *
 * @author relex lawl
 */
public final class EnterInputGamePacket implements GamePacketEvent {

	/**
	 * The {@link EnterInputGamePacket} logger to debug information and print out errors.
	 */
	private static final Logger logger = Logger.getLogger(EnterInputGamePacket.class.getName());
	
	@Override
	public void read(Player player, GamePacket packet) {
		switch (packet.getOpcode()) {
		case ENTER_AMOUNT_OPCODE:
			Shop shop = player.getFields().getShop();
			if (shop != null) {
				Item item = player.getFields().getItemToSell();
				if (item == null)
					return;
				if (!shop.buys(item)) {
					player.getPacketSender().sendMessage("You cannot sell this here!");
					return;
				}
				int amount = packet.readInt();
				if (amount > player.getInventory().getAmount(item.getId()))
					amount = player.getInventory().getAmount(item.getId());
				item.setAmount(amount);
				player.getInventory().switchItem(shop, item, player.getInventory().getSlot(item.getId()), false, true);
			}
			break;
		case ENTER_CLAN_CHAT_OPCODE:
			long input = packet.readLong();
			String name = NameUtil.longToString(input).replaceAll("_", " ");
			ClanChatManager.join(player, name);
			break;
		case SET_CLAN_CHAT_NAME_OPCODE:
			input = packet.readLong();
			name = NameUtil.longToString(input).replaceAll("_", " ");
			ClanChatManager.setName(player, name);
			break;
		default:
			logger.info("Unhandled input packet, packet opcode: " + packet.getOpcode());	
			break;
		}
	}
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode constants
	 * related to player relation actions.
	 */
	public static final int ENTER_AMOUNT_OPCODE = 208, ENTER_CLAN_CHAT_OPCODE = 60, SET_CLAN_CHAT_NAME_OPCODE = 61;
}
