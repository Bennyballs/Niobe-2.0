package org.niobe.world.update.player;

import org.niobe.world.Player;
import org.niobe.world.update.AbstractGameUpdate;

/**
 * An implementation of {@link org.niobe.world.update.AbstractGameUpdate}
 * that handles all of the pre-player updating procedures.
 *
 * @author relex lawl
 */
public final class PrePlayerGameUpdate extends AbstractGameUpdate {
	
	/**
	 * The PrePlayerGameUpdate constructor.
	 * @param player	The player to prepare for the next updating procedure.
	 */
	public PrePlayerGameUpdate(Player player) {
		this.player = player;
	}
	
	/**
	 * The player to prepare for the next 
	 * {@link org.niobe.world.update.AbstractGameUpdate} implementation.
	 */
	private final Player player;
	
	@Override
	public void run() {
		player.getMovementQueue().pulse();
		if (player.getFields().isChangingRegion()) {
			player.getPacketSender().sendMapRegion();
		}
		if (player.getFields().getCombatAttributes().getAttackedBy() != null
				&& (System.currentTimeMillis() - player.getFields().getCombatAttributes().getDamageDelay() >= 5000)) {
			player.getFields().getCombatAttributes().setAttackedBy(null);
		}
		if (player.getFields().getCombatAttributes().getAttackDelay() > 0) {
			player.getFields().getCombatAttributes().setAttackDelay(player.getFields().getCombatAttributes().getAttackDelay() - 1);
		}
		if (player.getFields().getCombatAttributes().getHitDelay() > 0) {
			player.getFields().getCombatAttributes().setHitDelay(player.getFields().getCombatAttributes().getHitDelay() - 1);
		}
	}

}
