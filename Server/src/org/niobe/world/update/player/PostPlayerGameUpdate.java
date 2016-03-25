package org.niobe.world.update.player;

import org.niobe.world.Player;
import org.niobe.world.update.AbstractGameUpdate;

/**
 * An implementation of {@link org.niobe.world.update.AbstractGameUpdate}
 * where the player flags are reset for the usage of the update procedure in
 * the next game tick.
 *
 * @author relex lawl
 */
public final class PostPlayerGameUpdate extends AbstractGameUpdate {

	/**
	 * The PostPlayerGameUpdate constructor.
	 * @param player	The player to reset flags in this tick.
	 */
	public PostPlayerGameUpdate(Player player) {
		this.player = player;
	}

	/**
	 * The associated player to reset flags for.
	 */
	private final Player player;
	
	@Override
	public void run() {
		player.getUpdateFlag().reset();
		player.getFields().setRegionChange(false).setTeleportPosition(null).setTeleporting(false).setForcedChat("");
		player.getMovementQueue().setDirections(-1, -1);
	}

}
