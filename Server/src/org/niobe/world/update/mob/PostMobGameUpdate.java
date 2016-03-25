package org.niobe.world.update.mob;

import org.niobe.world.Mob;
import org.niobe.world.update.AbstractGameUpdate;

/**
 * An implementation of {@link org.niobe.world.update.AbstractGameUpdate}
 * where the mob flags are reset for the usage of the update procedure in
 * the next game tick.
 *
 * @author relex lawl
 */
public final class PostMobGameUpdate extends AbstractGameUpdate {
	
	/**
	 * The PostMobGameUpdate constructor.
	 * @param mob	The {@link org.niobe.world.Mob} to update for.
	 */
	public PostMobGameUpdate(Mob mob) {
		this.mob = mob;
	}
	
	/**
	 * The {@link org.niobe.world.Mob} to handle
	 * post updating for.
	 */
	private final Mob mob;

	@Override
	public void run() {
		mob.getUpdateFlag().reset();
		mob.getFields().setTeleporting(false).setForcedChat("");
		mob.getMovementQueue().setDirections(-1, -1);
	}

}
