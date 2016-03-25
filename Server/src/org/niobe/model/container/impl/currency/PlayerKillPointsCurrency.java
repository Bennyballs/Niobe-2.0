package org.niobe.model.container.impl.currency;

import org.niobe.model.Item;
import org.niobe.model.container.impl.Shop;
import org.niobe.model.container.impl.Shop.Currency;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.model.container.impl.Shop.Currency}
 * that represents an {@link org.niobe.model.GameCharacter#getFields().getCombatAttributes.getPkPoints()}
 * currency for shops.
 *
 * @author relex lawl
 */
public final class PlayerKillPointsCurrency implements Currency {

	@Override
	public String getName() {
		return "pk points";
	}

	@Override
	public int getAmount(Player player) {
		return player.getFields().getCombatAttributes().getPkPoints();
	}

	@Override
	public void delete(Player player, int amount) {
		if (amount > 0) {
			int delete = player.getFields().getCombatAttributes().getPkPoints() - amount;
			player.getFields().getCombatAttributes().setPkPoints(delete);
			player.getPacketSender().sendMessage("You now have: " + delete + " pk points.");
		}
	}

	@Override
	public void add(Player player, int amount) {
		if (amount > 0) {
			int add = player.getFields().getCombatAttributes().getPkPoints() + amount;
			player.getFields().getCombatAttributes().setPkPoints(add);
			player.getPacketSender().sendMessage("You now have: " + add + " pk points.");
		}
	}

	@Override
	public int getSellPrice(Item item) {
		return getPointPrice(item);
	}
	
	@Override
	public int getBuyPrice(Item item) {
		return getPointPrice(item) / 2;
	}
	
	@Override
	public int getSpriteId() {
		return Shop.TOKEN_SPRITE_ID;
	}
	
	/**
	 * Gets the pk points price for said {@link item}.
	 * @param item	The {@link org.niobe.model.Item} to get special price for.
	 * @return		The player-kill points needed in order to purchase said item.
	 */
	private static int getPointPrice(Item item) {
		switch (item.getId()) {
		default:
			return 50;
		}
	}
}
