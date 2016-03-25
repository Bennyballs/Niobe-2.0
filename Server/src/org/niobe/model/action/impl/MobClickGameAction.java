package org.niobe.model.action.impl;

import org.niobe.model.GameCharacter;
import org.niobe.model.action.DistancedGameAction;
import org.niobe.model.action.DestinationListener;
import org.niobe.world.Mob;

/**
 * An implementation of {@link org.niobe.model.action.DistancedGameAction}
 * used for a click interaction that is done to a {@link org.niobe.world.Mob}
 * by a {@link org.niobe.world.Player}.
 *
 * @author relex lawl
 */
public final class MobClickGameAction<T extends GameCharacter> extends DistancedGameAction<T> {

	/**
	 * The MobClickGameAction constructor.
	 * @param character		The {@link org.niobe.model.GameCharacter} clicking the {@link org.niobe.world.Mob}.
	 * @param mob			The {@link org.niobe.world.Mob} being clicked.
	 * @param reachListener	The action that should be executed upon arrival.
	 */
	public MobClickGameAction(T character, Mob mob, DestinationListener reachListener) {
		super(0, character, mob.getPosition(), 1, mob.getDefinition().getSize(), reachListener);
		character.setEntityInteraction(mob);
	}
}
