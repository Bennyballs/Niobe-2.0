package org.niobe.world.content.combat.special.impl;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.task.Task;
import org.niobe.world.Player;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * An implementation of {@link org.niobe.world.content.combat.special.SpecialAttack}
 * that handles the special of a staff of light.
 *
 * @author relex lawl
 */
public final class StaffOfLightSpecialAttack extends SpecialAttack {
	
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
		return 100;
	}

	@Override
	public boolean victimGraphic() {
		return true;
	}
	
	@Override
	public boolean isImmediate(Player player) {
		return true;
	}
	
	@Override
	public void specialAction(final Player player, GameCharacter victim, Damage damage) {
		if (player.getFields().getCombatAttributes().hasStaffOfLightEffect()) {
			player.getPacketSender().sendMessage("You are already being protected by the Staff of light spirits!");
			return;
		}
		player.getFields().getCombatAttributes().setStaffOfLightEffect(true);
		player.getPacketSender().sendMessage("You are shielded by the spirits of the Staff of light!");
		GameServer.getTaskManager().submit(new Task(833) {
			@Override
			public void execute() {
				if (!player.getFields().getCombatAttributes().hasStaffOfLightEffect()) {
					stop();
					return;
				}
				player.getFields().getCombatAttributes().setStaffOfLightEffect(false);
				player.getPacketSender().sendMessage("Your staff of light shield has faded away!");
				stop();
			}
		});
	}
	
	/**
	 * The {@link org.niobe.model.Animation} used for this 
	 * special attack.
	 */
	private static final Animation ANIMATION = new Animation(10516);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(1958);
}
