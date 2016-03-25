package org.niobe.net.packet.event;

import java.util.logging.Logger;

import org.niobe.model.Item;
import org.niobe.model.MovementQueue.MovementFlag;
import org.niobe.model.UpdateFlag.Flag;
import org.niobe.model.action.impl.ItemWieldGameAction;
import org.niobe.model.container.impl.Bank;
import org.niobe.model.container.impl.Equipment;
import org.niobe.model.container.impl.Inventory;
import org.niobe.model.container.impl.Shop;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.model.weapon.WeaponSpecialBar;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Player;
import org.niobe.world.content.BonusManager;
import org.niobe.world.content.Consumables;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * used when an {@link org.niobe.model.Item} in an {@link org.niobe.model.container.ItemContainer}
 * is clicked upon.
 *
 * @author relex lawl
 */
public final class ItemContainerActionGamePacket implements GamePacketEvent {

	/**
	 * The {@link ItemContainerActionGamePacket} logger to debug information and print out errors.
	 */
	private final static Logger logger = Logger.getLogger(ItemContainerActionGamePacket.class.getName());

	/**
	 * Manages an item's first action.
	 * @param player	The player clicking the item.
	 * @param packet	The packet to read values from.
	 */
	private static void firstAction(Player player, GamePacket packet) {
		int interfaceId = packet.readShortA();
		int slot = packet.readShortA();
		int id = packet.readShortA();
		Item item = new Item(id);
		switch (interfaceId) {
		case Equipment.INVENTORY_INTERFACE_ID:
			if (player.getMovementQueue().getMovementFlag() == MovementFlag.STUNNED)
				break;
			item = player.getEquipment().getItems()[slot];
			player.setAction(new ItemWieldGameAction<Player>(player, item));
			int inventorySlot = player.getInventory().getEmptySlot();
			final boolean hasInventoryItem = item.getDefinition().isStackable() && player.getInventory().contains(item.getId());
			if (inventorySlot != -1 || hasInventoryItem) {
				if (hasInventoryItem) {
					item.setAmount(item.getAmount() + player.getInventory().getAmount(item.getId()));
				}
				player.getEquipment().setItem(slot, new Item(-1));
				player.getInventory().setItem(hasInventoryItem ? player.getInventory().getSlot(item.getId()) : inventorySlot, item);
				if (item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT) {
					WeaponSpecialBar.update(player);
					if (player.getFields().getCombatAttributes().getAttackStyle() > 2) {
						player.getFields().getCombatAttributes().setAttackStyle(2);
						player.getPacketSender().sendConfig(43, player.getFields().getCombatAttributes().getAttackStyle());
					}
					if (player.getFields().getCombatAttributes().hasStaffOfLightEffect() 
							&& item.getDefinition().getName().toLowerCase().contains("staff of light")) {
						player.getFields().getCombatAttributes().setStaffOfLightEffect(false);
						player.getPacketSender().sendMessage("You feel the spirit of the staff of light begin to fade away...");
					}
				}
				BonusManager.update(player);
				player.getEquipment().refreshItems();
				player.getInventory().refreshItems();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			} else {
				player.getInventory().sendContainerFullMessage();
			}
			break;
		case Bank.INTERFACE_ID:
			if (!player.getFields().isBanking())
				return;
			player.getBank().switchItem(player.getInventory(), item, slot, true, true);
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			if (!player.getFields().isBanking())
				return;
			player.getInventory().switchItem(player.getBank(), item, slot, false, true);
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			Shop shop = player.getFields().getShop();
			if (shop != null) {
				item = player.getInventory().getItems()[slot].copy().setAmount(10);
				if (!shop.buys(item)) {
					player.getPacketSender().sendMessage("You cannot sell this here!");
					return;
				}
				ItemDefinition definition = ItemDefinition.forId(item.getId());
				player.getPacketSender().sendMessage(definition.getName() + ": the store will currently buy this for " + shop.getCurrency().getBuyPrice(item) + "x " + shop.getCurrency().getName() + ".");
			}
			break;
		case Shop.ITEM_CHILD_ID:
			shop = player.getFields().getShop();
			if (shop != null) {
				item = shop.getItems()[slot];
				ItemDefinition definition = ItemDefinition.forId(item.getId());
				player.getPacketSender().sendMessage(definition.getName() + ": currently costs " + shop.getCurrency().getSellPrice(item) + "x " + shop.getCurrency().getName() + ".");
			}
			break;
		default:
			logger.info("Unhandled first item container action - interfaceId: " + interfaceId);
			break;
		}
	}

	/**
	 * Manages an item's second action.
	 * @param player	The player clicking the item.
	 * @param packet	The packet to read values from.
	 */
	private static void secondAction(Player player, GamePacket packet) {
		int interfaceId = packet.readLEShortA();
		int id = packet.readLEShortA();
		int slot = packet.readLEShort();
		switch (interfaceId) {
		case Bank.INTERFACE_ID:
			Item item = player.getBank().forSlot(slot).copy().setAmount(5);
			if (!player.getFields().isBanking() || item.getId() != id)
				return;
			player.getBank().switchItem(player.getInventory(), item, slot, true, true);
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			item = player.getInventory().forSlot(slot).copy().setAmount(5).copy();
			if (!player.getFields().isBanking() || item.getId() != id)
				return;
			player.getInventory().switchItem(player.getBank(), item, slot, false, true);
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			Shop shop = player.getFields().getShop();
			if (shop != null) {
				item = player.getInventory().getItems()[slot].copy().setAmount(1);
				if (!shop.buys(item)) {
					player.getPacketSender().sendMessage("You cannot sell this here!");
					return;
				}
				player.getInventory().switchItem(shop, item, slot, false, true);
			}
			break;
		case Shop.ITEM_CHILD_ID:
			shop = player.getFields().getShop();
			if (shop != null) {
				item = shop.getItems()[slot].copy().setAmount(1);
				shop.switchItem(player.getInventory(), item, slot, false, true);
			}
			break;
		default:
			logger.info("Unhandled second item container action - interfaceId: " + interfaceId);
			break;
		}
	}

	/**
	 * Manages an item's third action.
	 * @param player	The player clicking the item.
	 * @param packet	The packet to read values from.
	 */
	private static void thirdAction(Player player, GamePacket packet) {
		int interfaceId = packet.readLEShort();
		int id = packet.readShortA();
		int slot = packet.readShortA();
		switch (interfaceId) {
		case Bank.INTERFACE_ID:
			Item item = player.getBank().forSlot(slot).copy().setAmount(10);
			if (!player.getFields().isBanking() || item.getId() != id)
				return;
			player.getBank().switchItem(player.getInventory(), item, slot, true, true);
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			item = player.getInventory().forSlot(slot).copy().setAmount(10).copy();
			if (!player.getFields().isBanking() || item.getId() != id)
				return;
			player.getInventory().switchItem(player.getBank(), item, slot, false, true);
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			Shop shop = player.getFields().getShop();
			if (shop != null) {
				item = player.getInventory().getItems()[slot].copy().setAmount(5);
				if (!shop.buys(item)) {
					player.getPacketSender().sendMessage("You cannot sell this here!");
					return;
				}
				player.getInventory().switchItem(shop, item, slot, false, true);
			}
			break;
		case Shop.ITEM_CHILD_ID:
			shop = player.getFields().getShop();
			if (shop != null) {
				item = shop.getItems()[slot].copy().setAmount(5);
				shop.switchItem(player.getInventory(), item, slot, false, true);
			}
			break;
		default:
			logger.info("Unhandled third item container action - interfaceId: " + interfaceId);
			break;
		}
	}

	/**
	 * Manages an item's fourth action.
	 * @param player	The player clicking the item.
	 * @param packet	The packet to read values from.
	 */
	private static void fourthAction(Player player, GamePacket packet) {
		int slot = packet.readShortA();
		int interfaceId = packet.readShort();
		int id = packet.readShortA();
		switch (interfaceId) {
		case Bank.INTERFACE_ID:
			Item item = player.getBank().forSlot(slot).copy();
			if (!player.getFields().isBanking() || item.getId() != id)
				return;
			player.getBank().switchItem(player.getInventory(), item, slot, true, true);
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			item = player.getInventory().forSlot(slot).setAmount(player.getInventory().getAmount(id)).copy();
			if (!player.getFields().isBanking() || item.getId() != id)
				return;
			player.getInventory().switchItem(player.getBank(), item, slot, false, true);
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			Shop shop = player.getFields().getShop();
			if (shop != null) {
				item = player.getInventory().getItems()[slot].copy().setAmount(10);
				if (!shop.buys(item)) {
					player.getPacketSender().sendMessage("You cannot sell this here!");
					return;
				}
				player.getInventory().switchItem(shop, item, slot, false, true);
			}
			break;
		case Shop.ITEM_CHILD_ID:
			shop = player.getFields().getShop();
			if (shop != null) {
				item = shop.getItems()[slot].copy().setAmount(10);
				shop.switchItem(player.getInventory(), item, slot, false, true);
			}
			break;
		default:
			logger.info("Unhandled fourth item container action - interfaceId: " + interfaceId);
			break;
		}
	}
	
	/**
	 * Manages an item's fifth action.
	 * @param player	The player clicking the item.
	 * @param packet	The packet to read values from.
	 */
	private static void fifthAction(Player player, GamePacket packet) {
		int slot = packet.readLEShort();
		int interfaceId = packet.readShortA();
		int id = packet.readLEShort();
		Item item = new Item(id);
		switch (interfaceId) {
		case Shop.INVENTORY_INTERFACE_ID://TODO this is buy x bro.
			if (player.getFields().getShop() == null)
				return;
			item = player.getInventory().getItems()[slot].copy();
			player.getFields().setItemToSell(item);
			player.getPacketSender().sendEnterAmountPrompt();
			break;
		case Shop.ITEM_CHILD_ID:
			Shop shop = player.getFields().getShop();
			if (shop != null) {
				item = shop.getItems()[slot].copy().setAmount(20);
				shop.switchItem(player.getInventory(), item, slot, false, true);
			}
			break;
		default:
			logger.info("Unhandled fifth item container action - interfaceId: " + interfaceId);
			break;
		}
	}
	
	/**
	 * Used for "use" item actions - first click.
	 * @param player	The {@link org.niobe.world.Player} clicking the item.
	 * @param packet	The {@link org.niobe.net.packet.GamePacket} received from the client.
	 */
	private static void firstUseAction(Player player, GamePacket packet) {
		int interfaceId = packet.readLEShortA();
		int slot = packet.readShortA();
		int itemId = packet.readLEShort();
		switch (interfaceId) {
		case Inventory.INTERFACE_ID:
			Item item = player.getInventory().getItems()[slot];
			if (item == null)
				return;
			if (Consumables.isFood(player, item, slot))
				return;
			break;
		}
		switch (itemId) {
		default:
			logger.info("Unhandled second item action - [interfaceId, itemId, slot] : [" + interfaceId + ", " + itemId + ", " + slot + "]");
			break;
		}
	}
	
	/**
	 * Used for "use" item actions - second click.
	 * @param player	The {@link org.niobe.world.Player} clicking the item.
	 * @param packet	The {@link org.niobe.net.packet.GamePacket} received from the client.
	 */
	private static void secondUseAction(Player player, GamePacket packet) {
		int interfaceId = packet.readLEShortA();
		int slot = packet.readLEShort();
		int itemId = packet.readShortA();
		if (interfaceId == Inventory.INTERFACE_ID) {
			Item item = player.getInventory().forSlot(slot);
			if (item == null || item.getId() != itemId)
				return;
		}
		switch (itemId) {
		default:
			logger.info("Unhandled first item action - [interfaceId, itemId, slot] : [" + interfaceId + ", " + itemId + ", " + slot + "]");
			break;
		}
	}
	
	@Override
	public void read(Player player, GamePacket packet) {
		if (player.getFields().isDead())
			return;
		switch (packet.getOpcode()) {
		case FIRST_ITEM_ACTION_OPCODE:
			firstAction(player, packet);
			break;
		case SECOND_ITEM_ACTION_OPCODE:
			secondAction(player, packet);
			break;
		case THIRD_ITEM_ACTION_OPCODE:
			thirdAction(player, packet);
			break;
		case FOURTH_ITEM_ACTION_OPCODE:
			fourthAction(player, packet);
			break;
		case FIFTH_ITEM_ACTION_OPCODE:
			fifthAction(player, packet);
			break;
		case FIRST_USE_ITEM_ACTION:
			firstUseAction(player, packet);
			break;
		case SECOND_USE_ITEM_ACTION:
			secondUseAction(player, packet);
			break;
		}
	}
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode for
	 * first click item actions.
	 */
	public static final int FIRST_ITEM_ACTION_OPCODE = 145;
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode for
	 * second click item actions.
	 */
	public static final int SECOND_ITEM_ACTION_OPCODE = 117;
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode for
	 * third click item actions.
	 */
	public static final int THIRD_ITEM_ACTION_OPCODE = 43;
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode for
	 * fourth click item actions.
	 */
	public static final int FOURTH_ITEM_ACTION_OPCODE = 129;
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode for
	 * fifth click item actions.
	 */
	public static final int FIFTH_ITEM_ACTION_OPCODE = 135;
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode for
	 * the first "use" item click actions.
	 */
	public static final int FIRST_USE_ITEM_ACTION = 122;
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode for
	 * the second "use" item click actions.
	 */
	public static final int SECOND_USE_ITEM_ACTION = 75;
}
