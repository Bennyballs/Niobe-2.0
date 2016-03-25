package org.niobe.model.action;

/**
 * This interface is used for pending actions in
 * a {@link DistancedGameAction} that need to be performed
 * upon reaching the {@link DistancedGameAction#destination}.
 *
 * @author relex lawl
 */
public interface DestinationListener {

	/**
	 * The actions that will be performed once the
	 * destination has been reached in the {@link DistancedGameAction}.
	 */
	public void reachDestination();
}
