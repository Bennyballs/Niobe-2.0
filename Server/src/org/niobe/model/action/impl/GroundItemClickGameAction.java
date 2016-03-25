package org.niobe.model.action.impl;

import org.niobe.model.GameCharacter;
import org.niobe.model.action.DistancedGameAction;
import org.niobe.model.action.DestinationListener;
import org.niobe.world.GroundItem;

/**
 * An implementation of {@link org.niobe.model.action.DistancedGameAction}
 * used for {@link org.niobe.world.GroundItem} click actions.
 *
 * @author relex lawl
 */
public final class GroundItemClickGameAction<T extends GameCharacter> extends DistancedGameAction<T> {

	/**
	 * The GroundItemClickGameAction constructor.
	 * @param character				The {@link org.niobe.model.GameCharacter} interacting with ground item.
	 * @param groundItem			The {@link org.niobe.world.GroundItem} being clicked on.
	 * @param destinationListener	The {@link org.niobe.model.action.DestinationListener} that will be executed upon arrival.
	 */
	public GroundItemClickGameAction(T character, final GroundItem groundItem, DestinationListener destinationListener) {
		super(0, character, groundItem.getPosition(), 0, 0, destinationListener);
	}
}
