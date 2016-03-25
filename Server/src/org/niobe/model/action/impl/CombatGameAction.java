package org.niobe.model.action.impl;

import org.niobe.model.GameCharacter;
import org.niobe.model.action.DistancedGameAction;
import org.niobe.model.action.DestinationListener;
import org.niobe.world.content.combat.CombatManager;

/**
 * An implementation of {@link org.niobe.model.action.DistancedGameAction}
 * that handles the combat interaction between {@link #getCharacter()} and another
 * {@link org.niobe.model.GameCharacter} in the world.
 *
 * @author relex lawl
 */
public final class CombatGameAction<T extends GameCharacter> extends DistancedGameAction<T> {

	/**
	 * The CombatGameAction constructor.
	 * @param character	The {@link org.niobe.model.GameCharacter} engaging in combat.
	 * @param victim	The {@link org.niobe.world.GameObject} being clicked.
	 * @param distance	The distance required by the {@link #getCharacter()} to engage in combat.
	 */
	public CombatGameAction(final T character, final GameCharacter victim, int maximumDistance) {
		super(0, character, victim.getPosition(), 1, maximumDistance, new DestinationListener() {
			@Override
			public void reachDestination() {
				character.setFollowEntity(null);
				CombatManager.startCombat(character, victim);
			}
		});
	}
}
