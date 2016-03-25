package org.niobe.task.impl;

import org.niobe.model.container.impl.Equipment;
import org.niobe.model.weapon.WeaponLoader;
import org.niobe.model.weapon.WeaponSpecialBar;
import org.niobe.task.Task;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.task.Task} submitted
 * when a {@link org.niobe.world.Player}'s special bar amount is less
 * than a hundred.
 *
 * @author relex lawl
 */
public final class PlayerSpecialAmountTask extends Task {

	/**
	 * The PlayerSpecialAmountTask constructor.
	 * @param player	The {@link org.niobe.world.Player} to restore special attack for.
	 */
	public PlayerSpecialAmountTask(Player player) {
		super(20);
		this.player = player;
		player.getFields().getCombatAttributes().setRecoveringSpecialAttack(true);
	}
	
	/**
	 * The {@link org.niobe.world.Player} to setting off the
	 * {@link org.niobe.task.Task}.
	 */
	private final Player player;
	
	@Override
	public void execute() {
		if (player == null || player.getFields().getCombatAttributes().getSpecialAttackAmount() >= 100 ||
				!player.getFields().getCombatAttributes().isRecoveringSpecialAttack()) {
			player.getFields().getCombatAttributes().setRecoveringSpecialAttack(false);
			stop();
			return;
		}
		int amount = player.getFields().getCombatAttributes().getSpecialAttackAmount() + 5;
		if (amount >= 100) {
			amount = 100;
			player.getFields().getCombatAttributes().setRecoveringSpecialAttack(false);
			stop();
		}
		player.getFields().getCombatAttributes().setSpecialAttackAmount(amount);
		WeaponSpecialBar.setSpecialBar(player, WeaponLoader.forId(player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()));
		int special = player.getFields().getCombatAttributes().getSpecialAttackAmount();
		if(special % 50 == 0) {
			player.getPacketSender().sendMessage("<col=00D807>Your special attack energy is now " + special + "%.");
		}
	}
}
