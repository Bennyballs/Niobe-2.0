package org.niobe;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.niobe.world.update.GameUpdate;
import org.niobe.world.update.ParallelGameUpdate;
import org.niobe.world.update.SequentialGameUpdate;
import org.niobe.world.util.GameConstants;

/**
 * An implementation of {@link Runnable} which
 * updates all world processes in a cycle of 
 * {@link org.niobe.world.util.GameConstants #GAME_TICK}.
 *
 * @author relex lawl
 */
public final class GamePulse implements Runnable {
	
	/**
	 * The {@link GamePulse} logger to throw exceptions.
	 */
	private static final Logger logger = Logger.getLogger(GamePulse.class.getName());

	/**
	 * The GamePulse constructor.
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public GamePulse() throws Exception {
		if (GameServer.UPDATE_CORES < 1) {
			throw new Exception("Error initializing game pulse; revise GameServer.UPDATE_CORES!");
		} else if (GameServer.UPDATE_CORES == 1) {
			update = new SequentialGameUpdate();
		} else {
			update = new ParallelGameUpdate();
		}
		scheduledExecutor.scheduleAtFixedRate(this, GameConstants.GAME_TICK, GameConstants.GAME_TICK, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * The {@link ExecutorService} where the game pulse will be handled.
	 */
	private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
	
	/**
	 * The abstract game update used in the server.
	 */
	private final GameUpdate update;
	
	@Override
	public void run() {
		try {
			GameServer.getWorld().pulse();
			update.update();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			logger.log(Level.SEVERE, "Exception caught while running pulse!");
		}
	}

}
