package org.niobe.world.update;

/**
 * This abstract class handles the updating procedure
 * which can either be an instance of {@link org.niobe.world.update.ParallelGameUpdate}
 * which is used for computers with multi-cores and {@link org.niobe.world.update.SequentialGameUpdate}
 * used for single-core computers.
 * 
 * To edit which updating implementation will be used
 * change {@link org.niobe.GameServer.UPDATE_CORES} variable.
 * 
 * #1 -> {@link org.niobe.world.update.SequentialGameUpdate}.
 * #2+ -> {@link org.niobe.world.update.ParallelGameUpdate}.
 *
 * @author relex lawl
 */

public abstract class GameUpdate {

	/**
	 * The actual updating for each procedure 
	 * implementation.
	 */
	public abstract void update();
}
