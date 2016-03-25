package org.niobe.world.content.combat.special.impl;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.model.PrayerBook;
import org.niobe.task.Task;
import org.niobe.world.Player;
import org.niobe.world.content.combat.CombatPrayers;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * An implementation of {@link org.niobe.world.content.combat.special.SpecialAttack}
 * that handles the special of a dragon scimitar.
 *
 * @author relex lawl
 */
public final class DragonScimitarSpecialAttack extends SpecialAttack {

	@Override
	public Animation getAnimation() {
		return ANIMATION;
	}

	@Override
	public Graphic getGraphic() {
		return GRAPHIC;
	}

	@Override
	public int getSpecialAmount() {
		return 55;
	}

	@Override
	public void specialAction(Player player, final GameCharacter victim, Damage damage) {
		final boolean[] active = victim.getFields().getPrayerActive();
		if (victim.getFields().getPrayerBook() == PrayerBook.NORMAL) {
			if (active[CombatPrayers.PROTECT_FROM_MAGIC] ||
					active[CombatPrayers.PROTECT_FROM_MELEE] || active[CombatPrayers.PROTECT_FROM_MISSILES] ||
					active[CombatPrayers.PROTECT_FROM_SUMMONING]) {
				CombatPrayers.resetPrayers(player, CombatPrayers.OVERHEAD_PRAYERS, -1);
				CombatPrayers.deactivatePrayer(player, CombatPrayers.PROTECT_FROM_SUMMONING);
			}
		} else if (victim.getFields().getPrayerBook() == PrayerBook.CURSES) {
			
		}
		if (victim.isPlayer()) {
			victim.getFields().setOverheadPrayerCap(true);
			((Player)victim).getPacketSender().sendMessage("You have been injured.");
			GameServer.getTaskManager().submit(new Task(8) {
				@Override
				public void execute() {
					victim.getFields().setOverheadPrayerCap(false);
					stop();
				}
			});
		}
	}
	
	/**
	 * The {@link org.niobe.model.Animation} used for this 
	 * special attack.
	 */
	private static final Animation ANIMATION = new Animation(12031);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(2118);
}
