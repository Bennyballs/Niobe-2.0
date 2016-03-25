package org.niobe.world.content;

import org.niobe.model.Item;
import org.niobe.world.Player;

public final class StarterKit {
	
	public static void give(Player player) {
		for (Item item : STARTER_ITEMS) {
			if (!player.getInventory().isFull()) {
				player.getInventory().add(item);
			} else {
				player.getBank().add(item);
			}
		}
	}
	
	private static final Item[] STARTER_ITEMS = {
		new Item(995, 100000),
		new Item(392, 200),
		new Item(15273, 50),
	};
}
