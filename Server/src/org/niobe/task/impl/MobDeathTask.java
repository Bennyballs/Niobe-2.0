package org.niobe.task.impl;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.GameCharacter;
import org.niobe.model.Item;
import org.niobe.task.Task;
import org.niobe.world.GroundItem;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.content.clan.ClanChat.DropShare;
import org.niobe.world.content.clan.ClanChatManager;
import org.niobe.world.content.combat.CombatManager;

/**
 * An implementation of {@link org.niobe.task.Task}
 * submitted on a death hook of a {@link org.niobe.world.Mob}.
 *
 * @author relex lawl
 */
public final class MobDeathTask extends Task {

	/**
	 * The MobDeathTask constructor.
	 * @param mob	The mob being killed.
	 */
	public MobDeathTask(Mob mob) {
		this.mob = mob;
	}
	
	/**
	 * The {@link org.niobe.world.Mob} setting off
	 * the death task.
	 */
	private Mob mob;

	/**
	 * The amount of ticks on the task.
	 */
	private int ticks = 3;
	
	@Override
	public void execute() {
		switch (ticks) {
		case 3:
			mob.getFields().setDead(true);
			mob.performAnimation(new Animation(2261));
			break;
		case 1:
			GameCharacter killer = CombatManager.getKillerByMostDamage(mob);
			if (killer != null && killer.isPlayer()) {
				Player player = (Player) killer;
				if (player.getFields().getClanChat() != null &&
						player.getFields().getClanChat().getDropShare() != DropShare.NONE) {
					ClanChatManager.dropShareLoot(player, mob);
				} else {
					for (Item item : mob.getDrops()) {
						GroundItem groundItem = new GroundItem(item, mob.getPosition(), player);
						GameServer.getWorld().register(groundItem);
					}
				}
			} else {
				for (Item item : mob.getDrops()) {
					GroundItem groundItem = new GroundItem(item, mob.getPosition());
					GameServer.getWorld().register(groundItem);
				}
			}
			break;
		case 0:
			final Mob respawnMob = new Mob(mob.getId(), mob.getPosition(), mob.getDirection(), mob.getRespawnTimer());
			GameServer.getWorld().unregister(mob);
			if (mob.getRespawnTimer() >= 0) {
				GameServer.getTaskManager().submit(new Task(mob.getRespawnTimer()) {
					@Override
					public void execute() {
						GameServer.getWorld().register(respawnMob);
						stop();
					}
				});
			}
			stop();
			break;
		}
		ticks--;
	}

}
