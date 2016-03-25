package org.niobe.model.action.impl;

import org.niobe.model.GameCharacter;
import org.niobe.model.action.DistancedGameAction;
import org.niobe.model.action.DestinationListener;
import org.niobe.model.definition.GameObjectDefinition;
import org.niobe.world.GameObject;

/**
 * An implementation of {@link org.niobe.model.action.DistancedGameAction}
 * that handles player {@link org.niobe.world.GameObject} interaction.
 *
 * @author relex lawl
 */
public final class GameObjectClickGameAction<T extends GameCharacter> extends DistancedGameAction<T> {

	/**
	 * The GameObjectClickGameAction constructor.
	 * @param character			The {@link org.niobe.model.GameCharacter} clicking on the game object.
	 * @param gameObject		The {@link org.niobe.world.GameObject} being clicked.
	 * @param reachDestination	The {@link org.niobe.model.action.DestinationListener} to be executed 
	 * 								on arrival of destination.
	 */
	public GameObjectClickGameAction(T character, GameObject gameObject, DestinationListener reachDestination) {
		super(0, character, gameObject.getPosition(), getDistance(character, gameObject), getDistance(character, gameObject), reachDestination);
		character.getFields().setInteractingGameObject(gameObject);
	}
	
	/**
	 * Calculates the distance needed from said game object
	 * in order to complete the {@link org.niobe.model.action.GameAction}.
	 * @param character		The {@link org.niobe.model.GameCharacter} clicking on {@link org.niobe.world.GameObject}.
	 * @param gameObject	The {@link org.niobe.world.GameObject} being clicked on.
	 * @return				The distance required to perform {@link org.niobe.model.action.GameAction}.
	 */
	private static int getDistance(GameCharacter character, GameObject gameObject) {
		int distanceX = (character.getPosition().getX() - gameObject.getPosition().getX());
		int distanceY = (character.getPosition().getY() - gameObject.getPosition().getY());
		if (distanceX < 0)
			distanceX = -(distanceX);
		if (distanceY < 0)
			distanceY = -(distanceY);
		final GameObjectDefinition definition = GameObjectDefinition.forId(gameObject.getId());
		int size = distanceX > distanceY ? definition.getSizeX() : definition.getSizeY();
		if (size <= 0)
			size = 1;
		return size;
	}
}