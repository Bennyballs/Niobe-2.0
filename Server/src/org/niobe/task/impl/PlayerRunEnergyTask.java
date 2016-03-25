package org.niobe.task.impl;

import org.niobe.model.MovementQueue;
import org.niobe.task.Task;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.task.Task} submitted
 * when a {@link org.niobe.world.Player}'s run energy is less
 * than a hundred.
 *
 * @author relex lawl
 */
public final class PlayerRunEnergyTask extends Task {

	/**
	 * The PlayerRunEnergyTask constructor.
	 * @param player	The {@link org.niobe.world.Player} to restore run energy for.
	 */
	public PlayerRunEnergyTask(Player player) {
		this.player = player;
		this.ticks = (int) (MovementQueue.getRecoveryRate(player));
		player.getFields().setRecoveringRunEnergy(true);
	}
	
	/**
	 * The {@link org.niobe.world.Player} to setting off the
	 * {@link org.niobe.task.Task}.
	 */
	private final Player player;
	
	/**
	 * The current ticks, used to calculate the amount of time
	 * for the run energy to be recovered.
	 */
	private int ticks;
	
	@Override
	public void execute() {
		if (player == null || player.getFields().getRunEnergy() >= 100 || !player.getFields().isRecoveringRunEnergy()) {
			player.getFields().setRecoveringRunEnergy(false);
			stop();
			return;
		}
		if (ticks-- <= 0) {
			int amount = player.getFields().getRunEnergy() + 1;
			if (amount >= 100) {
				amount = 100;
				player.getFields().setRecoveringRunEnergy(false);
				stop();
			}
			player.getFields().setRunEnergy(amount);
			player.getPacketSender().sendRunEnergy(amount);
			ticks = (int) (MovementQueue.getRecoveryRate(player));
		}
	}
}
