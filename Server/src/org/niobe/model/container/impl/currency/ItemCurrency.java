package org.niobe.model.container.impl.currency;

import org.niobe.model.Item;
import org.niobe.model.container.impl.Shop;
import org.niobe.model.container.impl.Shop.Currency;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.model.container.impl.Shop.Currency}
 * that represents an {@link org.niobe.model.Item} currency for shops.
 *
 * @author relex lawl
 */
public class ItemCurrency implements Currency {
	
	/**
	 * The ItemCurrency constructor.
	 * @param item	The {@link org.niobe.model.Item} being used as the currency.
	 */
	public ItemCurrency(Item item) {
		this.item = item;
	}
	
	/**
	 * The {@link org.niobe.model.Item} being used as the currency.
	 */
	private final Item item;

	@Override
	public String getName() {
		return item.getDefinition().getName().toLowerCase();
	}

	@Override
	public int getAmount(Player player) {
		return player.getInventory().getAmount(item.getId());
	}

	@Override
	public void delete(Player player, int amount) {
		player.getInventory().delete(new Item(item.getId(), amount));
	}

	@Override
	public void add(Player player, int amount) {
		Item currency = new Item(item.getId(), amount);
		player.getInventory().add(currency);
	}
	
	@Override
	public int getSellPrice(Item item) {
		return ItemDefinition.forId(item.getId()).getValue();
	}

	@Override
	public int getBuyPrice(Item item) {
		return ItemDefinition.forId(item.getId()).getLowAlchemyValue();
	}

	@Override
	public int getSpriteId() {
		return Shop.COIN_SPRITE_ID;
	}
}
