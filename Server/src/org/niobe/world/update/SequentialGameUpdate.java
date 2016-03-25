package org.niobe.world.update;

import org.niobe.GameServer;
import org.niobe.world.Player;
import org.niobe.world.update.player.PlayerGameUpdate;
import org.niobe.world.update.player.PostPlayerGameUpdate;
import org.niobe.world.update.player.PrePlayerGameUpdate;

/**
 * An implementation of {@link GameUpdate} used for
 * single-core machines, as a result - this implementation
 * will be less efficient than {@link ParallelGameUpdate}.
 *
 * @author relex lawl
 */
public final class SequentialGameUpdate extends GameUpdate {

	@Override
	public void update() {
		final int count = GameServer.getWorld().getPlayersOnline();
		if (count > 0) {			
			for (Player player : GameServer.getWorld().getPlayers()) {
				if (player != null) {
					AbstractGameUpdate update = new PrePlayerGameUpdate(player);
					update.run();
				}
			}
			
			for (Player player : GameServer.getWorld().getPlayers()) {
				if (player != null) {
					AbstractGameUpdate update = new PlayerGameUpdate(player);
					update.run();
				}
			}
			
			for (Player player : GameServer.getWorld().getPlayers()) {
				if (player != null) {
					AbstractGameUpdate update = new PostPlayerGameUpdate(player);
					update.run();
				}
			}
		}
	}

}
