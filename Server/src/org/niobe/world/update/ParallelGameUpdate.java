package org.niobe.world.update;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

import org.niobe.GameServer;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.update.mob.MobGameUpdate;
import org.niobe.world.update.mob.PostMobGameUpdate;
import org.niobe.world.update.mob.PreMobGameUpdate;
import org.niobe.world.update.player.*;

/**
 * An implementation of {@link GameUpdate} that is used
 * for multi-core computers to handle updating more efficiently.
 *
 * @author relex lawl
 */
public final class ParallelGameUpdate extends GameUpdate {
	
	/**
	 * The ParallelGameUpdate constructor to initialize
	 * final variables.
	 */
	public ParallelGameUpdate() {
		phaser = new Phaser(1);
		executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}
	
	/**
	 * The phased used as a synchronization barrier.
	 */
	private final Phaser phaser;
	
	/**
	 * The executor service to submit {@link GameUpdates}
	 * implementations.
	 */
	private final ExecutorService executor;

	@Override
	public void update() {
		final int playerCount = GameServer.getWorld().getPlayersOnline();
		final int mobCount = GameServer.getWorld().getMobsRegistered();
		if (playerCount > 0) {
			phaser.bulkRegister(playerCount);
			
			for (Player player : GameServer.getWorld().getPlayers()) {
				if (player != null) {
					AbstractGameUpdate update = new PrePlayerGameUpdate(player);
					executor.submit(new PhasedGameUpdate(phaser, update));
				}
			}
			
			phaser.arriveAndAwaitAdvance();
			
			phaser.bulkRegister(mobCount);
			
			for (Mob mob : GameServer.getWorld().getMobs()) {
				if (mob != null) {
					AbstractGameUpdate update = new PreMobGameUpdate(mob);
					executor.submit(new PhasedGameUpdate(phaser, update));
				}
			}
			
			phaser.arriveAndAwaitAdvance();
			
			phaser.bulkRegister(playerCount);
			
			for (Player player : GameServer.getWorld().getPlayers()) {
				if (player != null) {
					AbstractGameUpdate update = new PlayerGameUpdate(player);
					executor.submit(new PhasedGameUpdate(phaser, update));
				}
			}
			
			phaser.arriveAndAwaitAdvance();
			
			phaser.bulkRegister(playerCount);
			
			for (Player player : GameServer.getWorld().getPlayers()) {
				if (player != null) {
					AbstractGameUpdate update = new MobGameUpdate(player);
					executor.submit(new PhasedGameUpdate(phaser, update));
				}
			}
			
			phaser.arriveAndAwaitAdvance();
			
			phaser.bulkRegister(playerCount);
			
			for (Player player : GameServer.getWorld().getPlayers()) {
				if (player != null) {
					AbstractGameUpdate update = new PostPlayerGameUpdate(player);
					executor.submit(new PhasedGameUpdate(phaser, update));
				}
			}
			
			phaser.arriveAndAwaitAdvance();
			
			phaser.bulkRegister(mobCount);
			
			for (Mob mob : GameServer.getWorld().getMobs()) {
				if (mob != null) {
					AbstractGameUpdate update = new PostMobGameUpdate(mob);
					executor.submit(new PhasedGameUpdate(phaser, update));
				}
			}
			
			phaser.arriveAndAwaitAdvance();
		}
	}

}