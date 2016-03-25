package org.niobe.model.container.impl;

import org.niobe.model.Item;
import org.niobe.model.container.ItemContainer;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.world.Player;

/**
 * Represents a player's bank. A bank is used to store items that a player
 * does not want to risk loosing or just to open up free inventory spaces.
 * 
 * @author relex lawl
 */
public final class Bank extends ItemContainer {

	/**
	 * The Bank constructor.
	 * @param player	The associated player.
	 */
	public Bank(Player player) {
		super(player);
	}
		
	/**
	 * This flag checks if the player will withdraw bank items
	 * in their note form.
	 */
	private boolean noteWithdrawal;
	
	/**
	 * This flag TODO: find out what this does on rs.
	 */
	private boolean swapItems;
	
	/**
	 * Sets the note withdrawal flag, to check if a player will withdraw items
	 * in their note form.
	 * @param noteWithdrawal	Grab items as notes.
	 * @return					The Bank instance.		
	 */
	public Bank setNoteWithdrawal(boolean noteWithdrawal) {
		this.noteWithdrawal = noteWithdrawal;
		return this;
	}
	
	/**
	 * Sets the item swapping flag.
	 * @param swapItems		Swap bank items.
	 * @return				The Bank instance.
	 */
	public Bank setItemSwapping(boolean swapItems) {
		this.swapItems = swapItems;
		return this;
	}
	
	/**
	 * Checks if the player will withdraw the item as a note.
	 * @return	noteWithdrawal.
	 */
	public boolean withdrawAsNote() {
		return noteWithdrawal;
	}
	
	/**
	 * Checks if the player will swap items.
	 * @return	swapItems.
	 */
	public boolean swapItems() {
		return swapItems;
	}

	/**
	 * Opens the bank interface for the associated player.
	 * @return	The Bank instance.
	 */
	public Bank open() {
		getPlayer().getFields().setBanking(true);
		noteWithdrawal = swapItems = false;
		sortItems().refreshItems();
		getPlayer().getPacketSender().sendConfig(304, 0).sendConfig(115, 0).sendConfig(799, 0);
		getPlayer().getPacketSender().sendInterfaceSet(5292, 5063);
		return this;
	}
	
	@Override
	public Bank switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		if (getItems()[slot].getId() != item.getId())
			return this;
		if (to.getFreeSlots() <= 0 && !(to.contains(item.getId()) && item.getDefinition().isStackable())) {
			to.sendContainerFullMessage();
			return this;
		}
		delete(item, slot, refresh, to);
		if (withdrawAsNote()) {
			if (ItemDefinition.forId(item.getId() + 1).isNoted())
				item.setId(item.getId() + 1);
			else
				getPlayer().getPacketSender().sendMessage("This item cannot be withdrawn as a note!");
		}
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
		return 352;
	}

	@Override
	public StackType stackType() {
		return StackType.STACKS;
	}
	
	@Override
	public Bank refreshItems() {
		getPlayer().getPacketSender().sendString(23100, "" + getValidItems().size());
		getPlayer().getPacketSender().sendString(23101, "" + capacity());
		getPlayer().getPacketSender().sendItemContainer(this, INTERFACE_ID);
		getPlayer().getPacketSender().sendItemContainer(getPlayer().getInventory(), INVENTORY_INTERFACE_ID);
		return this;
	}

	@Override
	public Bank sendContainerFullMessage() {
		getPlayer().getPacketSender().sendMessage("Not enough space in your bank.");
		return this;
	}

	/**
	 * The bank interface id.
	 */
	public static final int INTERFACE_ID = 5382;
	
	/**
	 * The bank inventory interface id.
	 */
	public static final int INVENTORY_INTERFACE_ID = 5064;
}