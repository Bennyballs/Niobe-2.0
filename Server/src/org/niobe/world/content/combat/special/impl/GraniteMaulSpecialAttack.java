package org.niobe.world.content.combat.special.impl;

import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.model.Graphic.GraphicHeight;
import org.niobe.world.Player;
import org.niobe.world.content.clan.ClanChat.MessageColor;
import org.niobe.world.content.combat.CombatManager;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * An implementation of {@link org.niobe.world.content.combat.special.SpecialAttack}
 * that handles the special attack of a granite maul.
 *
 * @author relex lawl
 */
public final class GraniteMaulSpecialAttack extends SpecialAttack {

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
		return 50;
	}
	
	@Override
	public boolean isImmediate(Player player) {
		GameCharacter victim = player.getFields().getCombatAttributes().getAttacking();
		if (victim == null) {
			if (player.getFields().getCombatAttributes().isUsingSpecialAttack()) {
				String warning = "<col=" + MessageColor.RED.getRGB()[2] + ">Warning: " + "<col=000000>";
				player.getPacketSender().sendMessage(warning + "Since the maul's special is an instant attack, it will be wasted when used on a");
				player.getPacketSender().sendMessage("first strike.");
			}
			return false;
		}
		CombatManager.damage(player, victim, new Damage(CombatManager.getHit(player, victim)));
		return true;
	}
	
	/**
	 * The {@link org.niobe.model.Animation} used for this 
	 * special attack.
	 */
	private static final Animation ANIMATION = new Animation(1667);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(340, 96 << 16, GraphicHeight.LOW);
}
