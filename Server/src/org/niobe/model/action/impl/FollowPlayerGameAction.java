package org.niobe.model.action.impl;

import org.niobe.model.GameCharacter;
import org.niobe.model.Position;
import org.niobe.model.action.DistancedGameAction;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.model.action.DistancedGameAction}
 * that handles the following interaction between two {@link org.niobe.world.Player}s.
 *
 * @author relex lawl
 */
public final class FollowPlayerGameAction<T extends GameCharacter> extends DistancedGameAction<T> {

	/**
	 * The FollowPlayerGameAction constructor.
	 * @param character	The {@link org.niobe.model.GameCharacter} that wants to follow other.
	 * @param other		The {@link org.niobe.world.Player} that is being followed.
	 */
	public FollowPlayerGameAction(T character, Player other) {
		super(0, character, other.getPosition(), 1, 1, null);
		this.other = other;
		character.setFollowEntity(other);
	}
	
	/**
	 * The {@link org.niobe.world.Player} that is 
	 * being followed by {@link #getCharacter()}.
	 */
	private final Player other;
	
	@Override
	public void execute() {
		if (getCharacter().getFollowEntity() != other) {
			stop();
			return;
		}
		final Position position = getCharacter().getFollowEntity().getPosition();
		getCharacter().setEntityInteraction(other);
		if (getCharacter().getPosition().getDistance(position) != 1) {
			if ((position.getY() - getCharacter().getPosition().getY()) >= position.getX() - getCharacter().getPosition().getX()) {
				final int y = (position.getY() - getCharacter().getPosition().getY()) > 0 ? (position.getY() - getCharacter().getPosition().getY()) - 1: (position.getY() - getCharacter().getPosition().getY()) + 1;
				getCharacter().getMovementQueue().walk(position.getX() - getCharacter().getPosition().getX(), y);
			} else {
				final int x = (position.getX() - getCharacter().getPosition().getX()) > 0 ? (position.getX() - getCharacter().getPosition().getX()) - 1: (position.getX() - getCharacter().getPosition().getX()) + 1;
				getCharacter().getMovementQueue().walk(x, position.getY() - getCharacter().getPosition().getY());
			}
		}
	}
}
