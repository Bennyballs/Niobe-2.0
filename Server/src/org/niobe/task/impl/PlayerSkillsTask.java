package org.niobe.task.impl;

import org.niobe.model.PrayerBook;
import org.niobe.model.SkillManager.Skill;
import org.niobe.task.Task;
import org.niobe.world.Player;
import org.niobe.world.content.combat.CombatCurses;

/**
 * An implementation of {@link org.niobe.task.Task} submitted
 * when a {@link org.niobe.world.Player}'s is registered to the
 * {@link org.niobe.world.World} to set most of the player's skills
 * back to normal.
 *
 * @author relex lawl
 */
public final class PlayerSkillsTask extends Task {

	/**
	 * The PlayerSkillsTask constructor.
	 * @param player	The player to normalize skills for.
	 */
	public PlayerSkillsTask(Player player) {
		super(10);
		this.player = player;
	}
	
	/**
	 * The player associated with this task.
	 */
	private Player player;
	
	/**
	 * The current ticks ran in {@link #execute()}.
	 * Used for berserker curse effect.
	 */
	private int ticks;

	@Override
	public void execute() {
		if (player == null) {
			stop();
			return;
		}
		int tickRequired = player.getFields().getPrayerBook() == PrayerBook.CURSES &&
							player.getFields().getPrayerActive()[CombatCurses.BERSERKER] ? 10 : 5;
		if (ticks++ >= tickRequired) {
			for (Skill skill : Skill.values()) {
				if (player.getSkillManager().getCurrentLevel(skill) != player.getSkillManager().getMaxLevel(skill) && skill != Skill.PRAYER) {
					int difference = player.getSkillManager().getCurrentLevel(skill) - player.getSkillManager().getMaxLevel(skill);
					player.getSkillManager().setCurrentLevel(skill, difference > 0 ? (player.getSkillManager().getCurrentLevel(skill) - 1) 
							: player.getSkillManager().getCurrentLevel(skill) + 1);
				}
			}
			ticks = 0;
		}
	}
}
