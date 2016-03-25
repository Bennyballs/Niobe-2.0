package org.niobe.world.update.mob;

import org.niobe.model.Position;
import org.niobe.model.MovementQueue.MovementFlag;
import org.niobe.util.MathUtil;
import org.niobe.world.Mob;
import org.niobe.world.update.AbstractGameUpdate;

/**
 * An implementation of {@link org.niobe.world.update.AbstractGameUpdate}
 * that handles all of the pre-mob updating procedures.
 *
 * @author relex lawl
 */

public final class PreMobGameUpdate extends AbstractGameUpdate {
	
	/**
	 * The PreMobGameUpdate constructor.
	 * @param mob	The {@link org.niobe.world.Mob} to update.
	 */
	public PreMobGameUpdate(Mob mob) {
		this.mob = mob;
	}
	
	/**
	 * The {@link org.niobe.world.Mob} to handle
	 * pre-updating for.
	 */
	private final Mob mob;
	
	@Override
	public void run() {
		final MovementFlag flag = mob.getMovementQueue().getMovementFlag();
		if (!mob.getFields().isDead() && flag != MovementFlag.CANNOT_MOVE && flag != MovementFlag.STUNNED &&
				flag != MovementFlag.FROZEN && mob.getFields().getCombatAttributes().getAttacking() == null) {
			if (mob.getWalkDistance() > 0 && MathUtil.random(10) == 0) {
				int randomX = MathUtil.random(2), x = randomX == 2 ? -1 : randomX;
				int randomY = MathUtil.random(2), y = randomY == 2 ? -1 : randomY;
				Position destination = mob.getPosition().copy().add(x, y);
				if (!mob.getStartPosition().isWithinDistance(destination, mob.getWalkDistance()))
					return;
				mob.getMovementQueue().walk(x, y);
			}
		}
		mob.getMovementQueue().pulse();
		mob.pulse();
	}
}
