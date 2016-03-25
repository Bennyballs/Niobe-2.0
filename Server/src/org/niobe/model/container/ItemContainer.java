package org.niobe.model.container;

import java.util.ArrayList;
import java.util.List;

import org.niobe.GameServer;
import org.niobe.model.Item;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.world.GroundItem;
import org.niobe.world.Player;

/**
 * An abstract class that contains a set 
 * amount of {@link org.niobe.model.Item}s for
 * different purposes in-game.
 * 
 * @author relex lawl
 */
public abstract class ItemContainer {
	
	/**
	 * The amount of items the container can hold, such as 28 for inventory.
	 */
	public abstract int capacity();

	/**
	 * The container's type enum, see enum for information.
	 */
	public abstract StackType stackType();
	
	/**
	 * The refresh method to send the container's interface on addition or deletion of an item.
	 */
	public abstract ItemContainer refreshItems();
	
	/**
	 * The full method which contains the content a player will receive upon container being full,
	 * such as a message when inventory is full.
	 */
	public abstract ItemContainer sendContainerFullMessage();
	
	/**
	 * ItemContainer constructor to create a new instance and to define the player.
	 * @param player	Player who owns the item container.
	 */
	public ItemContainer(Player player) {
		this.player = player;
		for (int i = 0; i < capacity(); i++) {
			items[i] = new Item(-1, 0);
		}
	}
	
	/**
	 * Player who owns the item container.
	 */
	private Player player;
	
	/**
	 * Gets the owner's player instance.
	 * @return	player.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Sets the player viewing the container, used
	 * for containers such as Shops.
	 */
	public ItemContainer setPlayer(Player player) {
		this.player = player;
		return this;
	}
	
	/**
	 * The items located in the container.
	 */
	private Item[] items = new Item[capacity()];
	
	/**
	 * Gets the items in the container.
	 * @return	items.
	 */
	public Item[] getItems() {
		return items;
	}
	
	/**
	 * Gets the valid items in the container,
	 * @return items in a list format.
	 */
	public List<Item> getValidItems() {
		List<Item> items = new ArrayList<Item>();
		for (Item item : this.items) {
			if (item != null && item.getId() > 0) {
				items.add(item);
			}
		}
		return items;
	}

	/**
	 * Sets all the items in the container.
	 * @param items		The item array to which set the container to hold.
	 */
	public ItemContainer setItems(Item[] items) {
		this.items = items;
		return this;
	}
	
	/**
	 * Sets the item in said slot.
	 * @param slot	Slot to set item for.
	 * @param item	Item that will occupy the slot.
	 */
	public ItemContainer setItem(int slot, Item item) {
		items[slot] = item;
		return this;
	}
	
	/**
	 * Checks if the slot contains an item.
	 * @param slot	The container slot to check.
	 * @return		items[slot] != null.
	 */
	public boolean isSlotOccupied(int slot) {
		return items[slot] != null && items[slot].getId() > 0 && items[slot].getAmount() > 0;
	}
	
	/**
	 * Swaps two item slots.
	 * @param fromSlot 	From slot.
	 * @param toSlot 	To slot.
	 */
	public ItemContainer swap(int fromSlot, int toSlot) {
		Item temporaryItem = getItems()[fromSlot];
		setItem(fromSlot, getItems()[toSlot]);
		setItem(toSlot, temporaryItem);
		return this;
	}
	
	/**
	 * Gets the amount of free slots the container has.
	 * @return	Total amount of free slots in container.
	 */
	public int getFreeSlots() {
		int space = 0;
		for (Item item : items) {
			if (item.getId() == -1) {
				space++;
			}
		}
		return space;
	}

	/**
	 * Checks if the container is out of available slots.
	 * @return	No free slot available.
	 */
	public boolean isFull() {
		return getEmptySlot() == -1;
	}
	
	/**
	 * Checks if container contains a certain item id.
	 * @param id	The item id to check for in container.
	 * @return		Container contains item with the specified id.
	 */
	public boolean contains(int id) {
		for (Item items : this.items) {
			if (items != null && items.getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if container contains a certain item.
	 * @param item	The item to check for in container.
	 * @return		Container has the item.
	 */
	public boolean contains(Item item) {
		for (Item items : this.items) {
			if (items != null && items == item) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the next empty slot for an item to equip.
	 * @return	The next empty slot index.
	 */
	public int getEmptySlot() {
		for (int i = 0; i < capacity(); i++) {
			if (items[i].getId() <= 0 || items[i].getAmount() <= 0) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Gets the first slot found for an item with said id.
	 * @param id	The id to loop through items to find.
	 * @return		The slot index the item is located in.
	 */
	public int getSlot(int id) {
		for (int i = 0; i < capacity(); i++) {
			if (items[i].getId() > 0 && items[i].getId() == id && items[i].getAmount() > 0) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the total amount of items in the container with the specified id.
	 * @param id	The id of the item to search for.
	 * @return 		The total amount of items in the container with said id.
	 */
	public int getAmount(int id) {
		int totalAmount = 0;
		for (Item item : items) {
			if (item.getId() == id) {
				totalAmount += item.getAmount();
			}
		}
		return totalAmount;
	}
	
	/**
	 * Gets the total amount of items in the container in the specified slot
	 * @param id	The slot of the item to search for.
	 * @return 		The total amount of items in the container with said slot.
	 */
	public int getAmountForSlot(int slot) {
		return items[slot].getAmount();
	}

	/**
	 * Resets items in the container.
	 * @return The ItemContainer instance.
	 */
	public ItemContainer resetItems() {
		for (int i = 0; i < capacity(); i++) {
			items[i] = new Item(-1, 0);
		}
		return this;
	}
	
	/**
	 * Gets an item by their slot index.
	 * @param slot	Slot to check for item.
	 * @return		Item in said slot.
	 */
	public Item forSlot(int slot) {
		return items[slot];
	}
	
	/**
	 * Switches an item from one item container to another.
	 * @param from		The item container to get item 
	 * @param to		The item container to put item on.
	 * @param item		The item to put from one container to another.
	 * @param slot		The slot of the item to switch from one container to another.
	 * @param sort		This flag checks whether or not to sort items, such as for bank.
	 * @return			The ItemContainer instance.
	 */
	public ItemContainer switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		if (getItems()[slot].getId() != item.getId())
			return this;
		if (to.getFreeSlots() <= 0 && !(to.contains(item.getId()) && item.getDefinition().isStackable())) {
			to.sendContainerFullMessage();
			return this;
		}
		delete(item, slot, refresh, to);
		to.add(item);
		if (sort && getAmount(item.getId()) <= 0)
			sortItems();
		if (refresh) {
			refreshItems();
			to.refreshItems();
		}
		return this;
	}
	
	/**
	 * Sorts this item container's array of items to leave no empty spaces.
	 * @return	The ItemContainer instance.
	 */
	public ItemContainer sortItems() {
		for (int i = 0; i < (capacity() - 1); i++) {
			if (getItems()[i] == null || getItems()[i].getId() <= 0 || getItems()[i].getAmount() <= 0) {
				swap((i + 1), i);
			}
		}
		return this;
	}

	/**
	 * Adds an item to the item container.
	 * @param item	The item to add.
	 * @return		The ItemContainer instance.
	 */
	public ItemContainer add(Item item) {
		return add(item, true);
	}

	/**
	 * Adds an item to the item container.
	 * @param id		The id of the item.
	 * @param amount	The amount of the item.
	 * @return			The ItemContainer instance.
	 */
	public ItemContainer add(int id, int amount) {
		return add(new Item(id, amount));
	}

	/**
	 * Adds an item to the item container.
	 * @param item		The item to add.
	 * @param refresh	If <code>true</code> the item container interface will be refreshed.
	 * @return			The ItemContainer instance.
	 */
	public ItemContainer add(Item item, boolean refresh) {
		if (item == null)
			return this;
		if (ItemDefinition.forId(item.getId()).isStackable() || stackType() == StackType.STACKS) {
			int slot = getSlot(item.getId());
			if (slot == -1)
				slot = getEmptySlot();
			if (slot == -1) {
				sendContainerFullMessage();
				return this;
			}
			long totalAmount = items[slot].getAmount() + item.getAmount();
			if (totalAmount > Integer.MAX_VALUE) {
				int notAdded = (int) totalAmount - Integer.MAX_VALUE;
				totalAmount = Integer.MAX_VALUE;
				item.setAmount(notAdded);
				items[slot].setId(item.getId());
				items[slot].setAmount(Integer.MAX_VALUE);
			} else {
				items[slot].setId(item.getId());
				items[slot].setAmount(items[slot].getAmount() + item.getAmount());
			}
		} else {
			int amount = item.getAmount();
			while (amount > 0) {
				int slot = getEmptySlot();
				if (slot == -1) {
					sendContainerFullMessage();
					break;
				}
				items[slot].setId(item.getId());
				items[slot].setAmount(1);
				amount--;
			}
		}
		if (refresh)
			refreshItems();
		return this;
	}

	/**
	 * Adds an item to the item container.
	 * @param item		The item to add.
	 * @param refresh	If <code>true</code> the item container interface will be refreshed.
	 * @return			The ItemContainer instance.
	 */
	public ItemContainer add(Item item, boolean refresh, boolean dropOnGround) {
		if (item == null)
			return this;
		if (ItemDefinition.forId(item.getId()).isStackable() || stackType() == StackType.STACKS) {
			int slot = getSlot(item.getId());
			if (slot == -1)
				slot = getEmptySlot();
			if (slot == -1) {
				sendContainerFullMessage();
				if (dropOnGround) {
					GroundItem groundItem = new GroundItem(item, player.getPosition().copy(), player);
					GameServer.getWorld().register(groundItem);
				}
				return this;
			}
			long totalAmount = items[slot].getAmount() + item.getAmount();
			if (totalAmount > Integer.MAX_VALUE) {
				int notAdded = (int) totalAmount - Integer.MAX_VALUE;
				totalAmount = Integer.MAX_VALUE;
				item.setAmount(notAdded);
				items[slot].setId(item.getId());
				items[slot].setAmount(Integer.MAX_VALUE);
			} else {
				items[slot].setId(item.getId());
				items[slot].setAmount(items[slot].getAmount() + item.getAmount());
			}
		} else {
			int amount = item.getAmount();
			while (amount > 0) {
				int slot = getEmptySlot();
				if (slot == -1) {
					sendContainerFullMessage();
					if (dropOnGround) {
						GroundItem groundItem = new GroundItem(item, player.getPosition().copy(), player);
						GameServer.getWorld().register(groundItem);
					}
					break;
				}
				items[slot].setId(item.getId());
				items[slot].setAmount(1);
				amount--;
			}
		}
		if (refresh)
			refreshItems();
		return this;
	}
	
	/**
	 * Deletes an item from the item container.
	 * @param item	The item to delete.
	 * @return		The ItemContainer instance.
	 */
	public ItemContainer delete(Item item) {
		return delete(item.getId(), item.getAmount());
	}
	
	/**
	 * Deletes an item from the item container.
	 * @param item	The item to delete.
	 * @param slot	The slot of the item (used to delete the item from said slot, not the first one found).
	 * @return		The ItemContainer instance.
	 */
	public ItemContainer delete(Item item, int slot) {
		return delete(item, slot, true);
	}

	/**
	 * Deletes an item from the item container.
	 * @param id		The id of the item to delete.
	 * @param amount	The amount of the item to delete.
	 * @return			The ItemContainer instance.
	 */
	public ItemContainer delete(int id, int amount) {
		return delete(id, amount, true);
	}
	
	/**
	 * Deletes an item from the item container.
	 * @param id		The id of the item to delete.
	 * @param amount	The amount of the item to delete.
	 * @param refresh	If <code>true</code> the item container interface will refresh.
	 * @return			The ItemContainer instance.
	 */
	public ItemContainer delete(int id, int amount, boolean refresh) {
		return delete(new Item(id, amount), getSlot(id), refresh);
	}
	
	/**
	 * Deletes an item from the item container.
	 * @param item		The item to delete.
	 * @param slot		The slot of the item to delete.
	 * @param refresh	If <code>true</code> the item container interface will refresh.
	 * @return			The ItemContainer instance.
	 */
	public ItemContainer delete(Item item, int slot, boolean refresh) {
		return delete(item, slot, refresh, null);
	}
	
	/**
	 * Deletes an item from the item container.
	 * @param item			The item to delete.
	 * @param slot			The slot of the item to delete.
	 * @param refresh		If <code>true</code> the item container interface will refresh.
	 * @param toContainer	To check if other container has enough space to continue deleting said amount from this container.
	 * @return				The ItemContainer instance.
	 */
	public ItemContainer delete(Item item, int slot, boolean refresh, ItemContainer toContainer) {
		if (item == null || slot < 0)
			return this;
		if (item.getAmount() > getAmount(item.getId()))
			item.setAmount(getAmount(item.getId()));
		if (item.getDefinition().isStackable() || stackType() == StackType.STACKS) {
			if (toContainer != null && !item.getDefinition().isStackable() && item.getAmount() > toContainer.getFreeSlots())
				item.setAmount(toContainer.getFreeSlots());
			items[slot].setAmount(items[slot].getAmount() - item.getAmount());
			if (items[slot].getAmount() < 1)
				items[slot].setId(-1);
		} else {
			int amount = item.getAmount();
			while (amount > 0) {
				if (slot == -1 || (toContainer != null && toContainer.isFull()))
					break;
				items[slot].setId(-1);
				items[slot].setAmount(0);
				slot = getSlot(item.getId());
				amount--;
			}
		}
		if (refresh)
			refreshItems();
		return this;
	}
	
	/**
	 * Represents an ItemContainer's stack type,
	 * 
	 * @author relex lawl
	 */
	public enum StackType {
		/**
		 * Default type, items that will not stack, such as inventory items (excluding noted/stackable items).
		 */
		DEFAULT,
		
		/**
		 * Stacks type, items that will stack, such as shops or banks.
		 */
		STACKS,
	}
}
