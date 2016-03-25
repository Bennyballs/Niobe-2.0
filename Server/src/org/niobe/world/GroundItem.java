package org.niobe.world;

import org.niobe.GameServer;
import org.niobe.model.Entity;
import org.niobe.model.Item;
import org.niobe.model.Position;

/**
 * An implementation of {@link Entity} that represents
 * an ground item existent in the world.
 *
 * @author relex lawl
 */
public final class GroundItem extends Entity {
	
	/**
	 * The GroundItem constructor.
	 * @param item		The item that the ground item represents.
	 * @param position	The position of the groundItem.
	 */
	public GroundItem(Item item, Position position, Player owner) {
		super(position);
		this.item = item;
		this.owner = owner;
		this.ownerName = owner != null ? owner.getCredentials().getUsername() : "";
	}
	
	/**
	 * The GroundItem constructor.
	 * @param item		The item that the ground item represents.
	 * @param position	The position of the groundItem.
	 */
	public GroundItem(Item item, Position position) {
		this(item, position, null);
	}
	
	/**
	 * Gets a ground item located in {@code position}.
	 * 
	 * @param position	The position to check for a ground item.
	 * @return			groundItem.
	 */
	public static GroundItem find(Position position) {
		for (GroundItem groundItem : GameServer.getWorld().getGroundItems()) {
			if (groundItem != null && groundItem.getPosition().sameAs(position)) {
				return groundItem;
			}
		}
		return null;
	}
	
	/**
	 * Checks if a ground item with the id specified exists.
	 * 
	 * @param id			The id of the item to find.
	 * @param position		The position of the ground item to find.
	 * @return				groundItem.
	 */
	public static GroundItem find(int id, Position position) {
		for (GroundItem groundItem : GameServer.getWorld().getGroundItems()) {
			if (groundItem != null && groundItem.getPosition().sameAs(position) && groundItem.getItem().getId() == id) {
				return groundItem;
			}
		}
		return null;
	}
	
	/**
	 * Checks if a ground item with the id specified exists.
	 * 
	 * @param id			The id of the item to find.
	 * @param position		The position of the ground item to find.
	 * @return				groundItem.
	 */
	public static GroundItem find(int id, Position position, Player player) {
		for (GroundItem groundItem : GameServer.getWorld().getGroundItems()) {
			if (groundItem != null && groundItem.getPosition().equals(position) &&
					groundItem.getItem().getId() == id && (groundItem.getOwnerName().equals(player.getCredentials().getUsername()))) {
				return groundItem;
			}
		}
		return null;
	}
	
	/**
	 * Checks if a ground item's game item with specified item exists.
	 * 
	 * @param item			The item to find.
	 * @param position		The position of the ground item to find.
	 * @return				groundItem.
	 */
	public static GroundItem exists(Item item, Position position) {
		for (GroundItem groundItem : GameServer.getWorld().getGroundItems()) {
			if (groundItem != null && groundItem.getPosition().sameAs(position) && groundItem.getItem() == item) {
				return groundItem;
			}
		}
		return null;
	}

	/**
	 * The ground item's item, which it represents.
	 */
	private Item item;
	
	/**
	 * The player which will be the only character to see the item.
	 */
	private Player owner;
	
	/**
	 * The {@link #owner}'s user name, to recognize their ownership 
	 * if they re-log.
	 */
	private final String ownerName;
	
	/**
	 * The amount of {@link org.niobe.world.util.GameConstants#GAME_TICK}s
	 * for the {@link GroundItem} to appear or disappear, depending on their
	 * current situation.
	 */
	private int ticks;
	
	/**
	 * Gets the ground item's item instance.
	 * @return	item.
	 */
	public Item getItem() {
		return item;
	}
	
	/**
	 * Gets the ground item's owner.
	 * @return	The only player who will see the ground item.
	 */
	public Player getOwner() {
		return owner;
	}
	
	/**
	 * Sets the ground item's owner.
	 * @param owner	The only player who will see the ground item.
	 * @return		The GroundItem instance.
	 */
	public GroundItem setOwner(Player owner) {
		this.owner = owner;
		return this;
	}
	
	/**
	 * Gets the {@link #owner}'s user name.
	 * @return	The {@link #owner.getCredentials().getUsername()}.
	 */
	public String getOwnerName() {
		return ownerName;
	}
	
	/**
	 * Gets the {@link GroundItem}'s current tick
	 * to appear or disappear.
	 * @return	The {@link #ticks} value.
	 */
	public int getTicks() {
		return ticks;
	}
	
	/**
	 * Adds {@value 1} to the {@link #ticks} integer
	 * every {@link World#pulse()}.
	 */
	public void addTick() {
		ticks++;
	}
	
	@Override
	public int getFreeIndex() {
		for (int i = 0; i < GameServer.getWorld().getGroundItems().length; i++) {
			if (GameServer.getWorld().getGroundItems()[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean isGroundItem() {
		return true;
	}
	
	@Override
	public int getSize() {
		return 0;
	}
	
	@Override
	public boolean equals(Object other) {
		return ((GroundItem)other).getIndex() == getIndex();
	}
}
