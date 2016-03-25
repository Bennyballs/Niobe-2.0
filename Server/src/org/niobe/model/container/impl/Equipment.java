package org.niobe.model.container.impl;

import org.niobe.model.container.ItemContainer;
import org.niobe.world.Player;

/**
 * Represents a player's equipment item container.
 * 
 * @author relex lawl
 */
public final class Equipment extends ItemContainer {
	
	/**
	 * The Equipment constructor.
	 * @param player	The player who's equipment is being represented.
	 */
	public Equipment(Player player) {
		super(player);
	}

	@Override
	public int capacity() {
		return 14;
	}

	@Override
	public StackType stackType() {
		return StackType.DEFAULT;
	}
	
	@Override
	public ItemContainer refreshItems() {
		getPlayer().getPacketSender().sendItemContainer(this, INVENTORY_INTERFACE_ID);
		return this;
	}
	
	@Override
	public Equipment sendContainerFullMessage() {
		return this;
	}
	
	/**
	 * The equipment inventory interface id.
	 */
	public static final int INVENTORY_INTERFACE_ID = 1688;
	
	/**
	 * The helmet slot.
	 */
	public static final int HEAD_SLOT = 0;
	
	/**
	 * The cape slot.
	 */
	public static final int CAPE_SLOT = 1;
	
	/**
	 * The amulet slot.
	 */
	public static final int AMULET_SLOT = 2;
	
	/**
	 * The weapon slot.
	 */
	public static final int WEAPON_SLOT = 3;
	
	/**
	 * The chest slot.
	 */
	public static final int BODY_SLOT = 4;
	
	/**
	 * The shield slot.
	 */
	public static final int SHIELD_SLOT = 5;
	
	/**
	 * The bottoms slot.
	 */
	public static final int LEG_SLOT = 7;
	
	/**
	 * The gloves slot.
	 */
	public static final int HANDS_SLOT = 9;
	
	/**
	 * The boots slot.
	 */
	public static final int FEET_SLOT = 10;
	
	/**
	 * The rings slot.
	 */
	public static final int RING_SLOT = 12;
	
	/**
	 * The arrows slot.
	 */
	public static final int AMMUNITION_SLOT = 13;
}
