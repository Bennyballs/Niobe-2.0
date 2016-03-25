package org.niobe.model.action;

import org.niobe.model.GameCharacter;
import org.niobe.model.Position;

/**
 * An implementation of {@link GameAction} where a {@link org.niobe.model.GameCharacter} 
 * is attempting to perform an action with a distanced entity and needs to walk over to it
 * for the action to be performed.
 *
 * @author relex lawl
 */
public abstract class DistancedGameAction<T extends GameCharacter> extends GameAction<T> {

	/**
	 * The DistanceGameAction constructor.
	 * @param ticks				The amount of ticks before starting the task.
	 * @param character			The character performing the distanced action.
	 * @param destination		The {@link org.niobe.model.Position} the character is going to reach.
	 * @param minimumDistance	The minimum distance required for the action to be performed.
	 * @param maximumDistance	The maximum distance required for the action to be performed.
	 */
	public DistancedGameAction(int ticks, T character, Position destination, 
			int minimumDistance, int maximumDistance, DestinationListener reachDestinationAction) {
		super(ticks, character);
		this.destination = destination.copy();
		this.minimumDistance = minimumDistance;
		this.maximumDistance = maximumDistance;
		this.destinationListener = reachDestinationAction;
	}

	/**
	 * The {@link org.niobe.model.Position} that character
	 * is aiming to reach.
	 */
	private final Position destination;
	
	/**
	 * The minimum distance required from {@link character #getPosition()} to
	 * {@link #destination} for the {@link DestinationListener} to
	 * call {@link DestinationListener #reachDestination()}.
	 */
	private final int minimumDistance;
	
	/**
	 * The maximum distance required from {@link character #getPosition()} to
	 * {@link #destination} for the {@link DestinationListener} to
	 * call {@link DestinationListener #reachDestination()}.
	 */
	private final int maximumDistance;
	
	/**
	 * The {@link DestinationListener} that is executed upon
	 * reaching the {@link #destination}.
	 */
	private DestinationListener destinationListener;
	
	/**
	 * Gets the destination {@link org.niobe.model.Position}.
	 * @return	The destination needed to be arrived to within a the {@link #distance}.
	 */
	public Position getDestination() {
		return destination;
	}

	@Override
	public void execute() {
		//TODO diagonal fix.
		final int distance = getCharacter().getPosition().getDistance(destination);
		if (distance >= minimumDistance && distance <= maximumDistance) {
			getCharacter().getMovementQueue().stopMovement();//TODO should this stop movement? it should for combat, not sure about others though.
			if (destinationListener != null) {
				destinationListener.reachDestination();
			}
			stop();
		} else if (distance < minimumDistance) {
			//TODO region step away
		} else if (distance > maximumDistance) {
			final int y = (destination.getY() - getCharacter().getPosition().getY()) > 0 ? (destination.getY() - getCharacter().getPosition().getY() - maximumDistance): (destination.getY() - getCharacter().getPosition().getY() + maximumDistance);
			getCharacter().getMovementQueue().walk((destination.getX() - getCharacter().getPosition().getX()), y);
		}
	}
	
	@Override
	public boolean equals(Object object) {
		if (object.getClass() != getClass()) {
			return false;
		}
		DistancedGameAction<?> other = (DistancedGameAction<?>) object;
		return other.destination.sameAs(destination);
	}
}
