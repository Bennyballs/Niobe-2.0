package org.niobe.model;

import org.niobe.model.definition.ItemDefinition;

/**
 * Represents an in-game "item" that can be held
 * in different {@link org.niobe.model.container.ItemContainer}s.
 * 
 * @author relex lawl
 */
public class Item {
	
	/**
	 * An Item object constructor.
	 * @param id		Item id.
	 * @param amount	Item amount.
	 */
	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
		this.definition = ItemDefinition.forId(id);
	}
	
	/**
	 * An Item object constructor.
	 * @param id		Item id.
	 */
	public Item(int id) {
		this(id, 1);
	}
	
	/**
	 * The item id.
	 */
	private int id;

	/**
	 * Gets the item's id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the item's id.
	 * @param id	New item id.
	 */
	public Item setId(int id) {
		this.id = id;
		return this;
	}
	
	/**
	 * Amount of the item.
	 */
	private int amount;
	
	/**
	 * Gets the amount of the item.
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * Sets the amount of the item.
	 */
	public Item setAmount(int amount) {
		this.amount = amount;
		return this;
	}
	
	/**
	 * The item's definition, checks things like value, is stackable, is noted, etc.
	 */
	private ItemDefinition definition;

	/**
	 * Gets item's definition.
	 */
	public ItemDefinition getDefinition() {
		if (definition != null) {
			return definition;
		}
		return (definition = ItemDefinition.forId(id));
	}
	
	/**
	 * Copying the item by making a new item with same values.
	 * @return	The new {@link Item} instance.
	 */
	public Item copy() {
		return new Item(id, amount);
	}
}