package org.niobe.model.container.impl;

import org.niobe.model.Item;
import org.niobe.model.container.ItemContainer;
import org.niobe.model.container.impl.currency.ItemCurrency;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.world.Player;

/**
 * Represents a player's inventory item container.
 * 
 * @author relex lawl
 */
public final class Inventory extends ItemContainer {

	/**
	 * The Inventory constructor.
	 * @param player	The player who's inventory is being represented.
	 */
	public Inventory(Player player) {
		super(player);
	}
	
	@Override
	public Inventory switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		if (getItems()[slot].getId() != item.getId())
			return this;
		if (to.getFreeSlots() <= 0 && !(to.contains(item.getId()) && item.getDefinition().isStackable())) {
			to.sendContainerFullMessage();
			return this;
		}
		int maxAmount = getAmount(item.getId());
		if (item.getAmount() > maxAmount)
			item.setAmount(maxAmount);
		delete(item, slot, refresh, to);
		if (to instanceof Shop) {
			Shop shop = (Shop) to;
			if (shop.getCurrency().getClass().getName().equals(ItemCurrency.class.getName())
					&& shop.getCurrency().getAmount(getPlayer()) <= 0 && getFreeSlots() <= 0
					&& item.getDefinition().isStackable() && getItems()[slot].getAmount() > 0) {
				sendContainerFullMessage();
				return this;
			}
			int price = shop.getCurrency().getBuyPrice(item) * item.getAmount();
			shop.getCurrency().add(getPlayer(), price);
		}
		if (to instanceof Bank && ItemDefinition.forId(item.getId()).isNoted() && !ItemDefinition.forId(item.getId() - 1).isNoted())
			item.setId(item.getId() - 1);
		to.add(item);
		if (sort && getAmount(item.getId()) <= 0)
			sortItems();
		if (refresh) {
			refreshItems();
			to.refreshItems();
		}
		return this;
	}

	@Override
	public int capacity() {
		return 28;
	}

	@Override
	public StackType stackType() {
		return StackType.DEFAULT;
	}

	@Override
	public Inventory refreshItems() {
		getPlayer().getPacketSender().sendItemContainer(this, INTERFACE_ID);
		return this;
	}

	@Override
	public Inventory sendContainerFullMessage() {
		getPlayer().getPacketSender().sendMessage("Not enough space in your inventory.");
		return this;
	}
	
	/**
	 * The interface used send the item container.
	 */
	public static final int INTERFACE_ID = 3214;
}
