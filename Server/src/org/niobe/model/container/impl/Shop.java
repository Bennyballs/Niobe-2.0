package org.niobe.model.container.impl;

import org.niobe.GameServer;
import org.niobe.model.Item;
import org.niobe.model.container.ItemContainer;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.world.Player;
import org.niobe.world.content.ShopManager;

/**
 * An implementation of {@link org.niobe.model.container.ItemContainer}
 * in which players can purchase or sell {@link org.niobe.model.Item}.
 *
 * @author relex lawl
 */
public final class Shop extends ItemContainer {

	/**
	 * The Shop constructor.
	 * @param player	The player viewing the shop.
	 * @param id		The unique shop id to retrieve from {@link org.niobe.world.content.ShopManager}'s shop map.
	 * @param name		The name that will appear as the title on the shop interface.
	 * @param currency	The {@link org.niobe.model.Item} used as currency in the shop.
	 * @param stock		All of the {@link org.niobe.model.Item}s that can be purchased or sold to the shop.
	 */
	public Shop(Player player, int id, String name, Currency currency, Item[] stock) {
		super(player);
		if (stock.length > 24)
			throw new ArrayIndexOutOfBoundsException("Stock cannot have more than 24 items; check shop[" + id + "]: stockLength: " + stock.length);
		this.id = id;
		this.name = name != null && name.length() > 0 ? name : "General Store";
		this.stock = stock;
		this.currency = currency;
		for (Item item : stock) {
			add(item.copy(), false);
		}
	}
	
	/**
	 * The unique shop id.
	 */
	private final int id;
	
	/**
	 * The name of the shop used as the title
	 * in the shop interface.
	 */
	private String name;
	
	/**
	 * The currency the shop is receiving in exchange
	 * for goods.
	 */
	private Currency currency;
	
	/**
	 * The stock the shop currently has.
	 */
	private Item[] stock;

	/**
	 * Gets the shop name used as the title.
	 * @return	The name of the shop.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the shop name.
	 * @param name	The name of the shop.
	 * @return		The Shop instance.
	 */
	public Shop setName(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * Gets the {@link Currency} that is used as the
	 * {@link Shop} trade currency.
	 * @return	The {@link #currency} value.
	 */
	public Currency getCurrency() {
		return currency;
	}
	
	/**
	 * Sets the {@link Currency}.
	 * @param currency	The {@link Currency} value.
	 * @return			The Shop instance.
	 */
	public Shop setCurrency(Currency currency) {
		this.currency = currency;
		return this;
	}
	
	/**
	 * Gets the {@link Shop} stock {@link org.niobe.model.Item}s.
	 * @return	The {@link #stock} array.
	 */
	public Item[] getStock() {
		return stock;
	}
	
	/**
	 * Sets the {@link Shop}'s {@link org.niobe.model.Item}s
	 * for sale.
	 * @param stock	The array containing the {@link org.niobe.model.Item}s.
	 * @return		The Shop instance.
	 */
	public Shop setStock(Item[] stock) {
		this.stock = stock;
		return this;
	}
	
	/**
	 * Opens a {@link Shop} interface for said
	 * {@link org.niobe.world.Player}.
	 * @param player	The {@link org.niobe.world.Player} to open {@link Shop} for.
	 * @return			The Shop instance.
	 */
	public Shop open(Player player) {
		setPlayer(player);
		getPlayer().getFields().setShop(ShopManager.getShops().get(id)).setInterfaceId(INTERFACE_ID);
		refreshItems();
		return this;
	}
	
	/**
	 * Checks if the {@link org.niobe.model.Item} can be
	 * sold to this {@link Shop}.
	 * @param item	The {@link org.niobe.model.Item} to sell.
	 * @return		{@code true} if this {@link Shop} sells/buys the {@link org.niobe.model.Item}.
	 */
	public boolean buys(Item item) {
		for (Item containerItem : getItems()) {
			if (containerItem != null && 
					(containerItem.getId() == item.getId() || ItemDefinition.forId(item.getId()).isNoted() && containerItem.getId() + 1 == item.getId())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Handles the refresh of the {@link Shop#getItems()}
	 * for every {@link org.niobe.world.Player} currently viewing
	 * this {@link Shop}.
	 */
	public void publicRefresh() {
		Shop publicShop = ShopManager.getShops().get(id);
		if (publicShop == null)
			return;
		publicShop.setItems(getItems());
		for (Player player : GameServer.getWorld().getPlayers()) {
			if (player != null && player.getFields().getShop() != null &&
					player.getFields().getShop().id == id) {
				player.getFields().getShop().setItems(publicShop.getItems());
			}
		}
	}
	
	/**
	 * Creates a new {@link Shop} with same variables
	 * as this {@link Shop}.
	 * @return	The new Shop instance.
	 */
	public Shop copy() {
		return new Shop(getPlayer(), id, name, currency, stock);
	}
	
	@Override
	public Shop switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		int freeSlots = to.getFreeSlots();
		if (freeSlots == 0 || freeSlots < item.getAmount() && !ItemDefinition.forId(item.getId()).isStackable()) {
			item.setAmount(freeSlots);
		}
		if (item.getAmount() > getItems()[slot].getAmount()) {
			item.setAmount(getItems()[slot].getAmount());
		}
		int playerCurrencyAmount = currency.getAmount(getPlayer());
		if (playerCurrencyAmount <= 0) {
			getPlayer().getPacketSender().sendMessage("You do not have enough " + currency.getName()+ " to purchase this!");
			return this;
		}
		int value = item.getAmount() * currency.getSellPrice(item);
		if (value > Integer.MAX_VALUE)
			value = Integer.MAX_VALUE;
		if (playerCurrencyAmount < value) {
			int amount = (int) Math.floor((playerCurrencyAmount * item.getAmount()) / value);
			item.setAmount(amount);
			value = item.getAmount() * currency.getSellPrice(item);
			getPlayer().getPacketSender().sendMessage("You do not have enough " + currency.getName() + " to purchase that amount.");
		}
		currency.delete(getPlayer(), value);
		super.switchItem(to, item, slot, sort, refresh);
		publicRefresh();
		return this;
	}

	@Override
	public Shop delete(Item item, int slot, boolean refresh, ItemContainer toContainer) {
		if (item == null || slot < 0)
			return this;
		if (item.getAmount() > getAmount(item.getId()))
			item.setAmount(getAmount(item.getId()));
		if (item.getDefinition().isStackable() || stackType() == StackType.STACKS) {
			if (toContainer != null && !item.getDefinition().isStackable() && item.getAmount() > toContainer.getFreeSlots())
				item.setAmount(toContainer.getFreeSlots());
			getItems()[slot].setAmount(getItems()[slot].getAmount() - item.getAmount());
			if (getItems()[slot].getAmount() < 0)
				getItems()[slot].setAmount(0);
		} else {
			int amount = item.getAmount();
			while (amount > 0) {
				if (slot == -1 || (toContainer != null && toContainer.isFull()))
					break;
				getItems()[slot].setAmount(0);
				slot = getSlot(item.getId());
				amount--;
			}
		}
		if (refresh)
			refreshItems();
		publicRefresh();
		return this;
	}
	
	@Override
	public Shop add(Item item, boolean refresh) {
		if (ItemDefinition.forId(item.getId()).isNoted())
			item.setId(item.getId() - 1);
		super.add(item, refresh);
		publicRefresh();
		return this;
	}
	
	@Override
	public int capacity() {
		return 30;
	}

	@Override
	public StackType stackType() {
		return StackType.STACKS;
	}

	@Override
	public Shop refreshItems() {
		for (Player player : GameServer.getWorld().getPlayers()) {
			if (player == null || player.getFields().getShop() == null || 
					player.getFields().getShop().id != id)
				continue;
			player.getPacketSender().sendString(NAME_INTERFACE_CHILD_ID, name);
			player.getPacketSender().sendItemContainer(ShopManager.getShops().get(id), ITEM_CHILD_ID);
			player.getPacketSender().sendInterfaceSet(INTERFACE_ID, INVENTORY_INTERFACE_ID - 1);
			player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);
		}
		return this;
	}

	@Override
	public Shop sendContainerFullMessage() {
		return this;
	}

	/**
	 * The shop interface id.
	 */
	private static final int INTERFACE_ID = 1200;
	
	/**
	 * The starting interface child id of items.
	 */
	public static final int ITEM_CHILD_ID = 1203;
	
	/**
	 * The interface child id of the shop's name.
	 */
	private static final int NAME_INTERFACE_CHILD_ID = 1204;
	
	/**
	 * The inventory interface id, used to set the items right click values
	 * to 'sell'.
	 */
	public static final int INVENTORY_INTERFACE_ID = 3823;
	
	/**
	 * This constants contains the sprite index for
	 * coin in the client's {@link SpriteLoader#sprites} array.
	 */
	public static final int COIN_SPRITE_ID = 586;
	
	/**
	 * This constants contains the sprite index for
	 * tokens in the client's {@link SpriteLoader#sprites} array.
	 */
	public static final int TOKEN_SPRITE_ID = 587;
	
	/**
	 * Represents what can be a {@link Shop#currency}, so you
	 * can have multiple currencies, such as points, items, etc.
	 * 
	 * @author relex lawl
	 */
	public static interface Currency {
		
		/**
		 * Gets the currency's name.
		 * @return	The name to handle things as message for fund issues.
		 */
		public String getName();
		
		/**
		 * Gets the amount of currency the {@link player}
		 * currently has.
		 * @param player	The {@link org.niobe.world.Player} to fetch amount for.
		 * @return			The amount of the currency.
		 */
		public int getAmount(Player player);
		
		/**
		 * Handles the deletion of the currency used in
		 * the shop.
		 * @param player	The {@link org.niobe.world.Player} to delete currency for.
		 * @param amount	The amount of this currency to delete.
		 */
		public void delete(Player player, int amount);
		
		/**
		 * Handles the addition of the currency back to
		 * the {@link player} - used when they sell items back to the shop.
		 * @param player	The {@link org.niobe.world.Player} selling the item back.
		 * @param amount	The amount of items they're selling back.
		 */
		public void add(Player player, int amount);
		
		/**
		 * Gets the price the {@link item} is sold for 
		 * in the shop.
		 * @param item	The {@link org.niobe.model.Item} to get value for.
		 * @return		The sell price.
		 */
		public int getSellPrice(Item item);
		
		/**
		 * Gets the price the {@link item} is bought for 
		 * in the shop.
		 * @param item	The {@link org.niobe.model.Item} to get value for.
		 * @return		The buy price.
		 */
		public int getBuyPrice(Item item);
		
		/**
		 * Gets the sprite id used in the client's
		 * sprite loader to fetch the currency sprite
		 * next to the item box.
		 * @return	The sprite's sprite loader index.
		 */
		public int getSpriteId();
	}
}
