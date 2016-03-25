package org.niobe.model.action.impl;

import org.niobe.model.GameCharacter;
import org.niobe.model.action.DistancedGameAction;
import org.niobe.model.action.DestinationListener;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.model.action.DistancedGameAction}
 * that handles player to {@link org.niobe.world.Player} interaction.
 *
 * @author relex lawl
 */
public final class PlayerClickGameAction<T extends GameCharacter> extends DistancedGameAction<T> {

	/**
	 * The PlayerClickGameAction constructor.
	 * @param character			The {@link org.niobe.model.GameCharacter} clicking on other.	
	 * @param other				The {@link org.niobe.world.Player} being clicked on.
	 * @param reachDestination	The {@link org.niobe.model.action.DestinationListener} to execute upon arrival.
	 */
	public PlayerClickGameAction(T character, Player other, DestinationListener reachDestination) {
		super(0, character, other.getPosition(), 1, 1, reachDestination);
		character.setEntityInteraction(other);
	}

}
