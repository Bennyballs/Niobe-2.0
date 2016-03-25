package org.niobe.model.action.impl;

import org.niobe.model.GameCharacter;
import org.niobe.model.Item;
import org.niobe.model.action.GameAction;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.model.action.GameAction}
 * used when a {@link org.niobe.world.Player} wields or wears and item.
 *
 * @author relex lawl
 */
public final class ItemWieldGameAction<T extends GameCharacter> extends GameAction<T> {

	/**
	 * The ItemWieldGameAction constructor.
	 * @param character	The {@link org.niobe.world.Player} clicking the item.
	 * @param item		The {@link org.niobe.model.Item} being clicked.
	 */
	public ItemWieldGameAction(T character, Item item) {
		super(0, character);
		this.item = item;
		if (character.isPlayer()) {
			Player player = (Player) character;
			if (getCharacter().getFields().getDialogue() != null) {
				getCharacter().getFields().getDialogue().finish(player);
			}
		}
	}
	
	/**
	 * The current {@link org.niobe.model.Item} being clicked.
	 */
	private final Item item;

	@Override
	public void execute() {
		stop();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object.getClass() != getClass()) {
			return false;
		}
		ItemWieldGameAction<?> other = (ItemWieldGameAction<?>) object;
		return other.item == item;
	}
}
