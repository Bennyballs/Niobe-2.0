package org.niobe.world.content.combat.special.impl;

import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.model.Graphic.GraphicHeight;
import org.niobe.world.Player;
import org.niobe.world.content.combat.CombatManager;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * An implementation of {@link org.niobe.world.content.combat.special.SpecialAttack}
 * that handles the special of a dragon halberd.
 *
 * @author relex lawl
 */
public final class DragonHalberdSpecialAttack extends SpecialAttack {

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
		return 30;
	}
	
	@Override
	public double getMultiplier() {
		return 1.1;
	}
	
	@Override
	public void specialAction(Player player, GameCharacter victim, Damage damage) {
		if (victim.getSize() <= 2) {
			victim.performGraphic(new Graphic(254, GraphicHeight.HIGH));
		} else {
			CombatManager.damage(player, victim, new Damage(CombatManager.getHit(player, victim)));
		}
	}
	
	/**
	 * The {@link org.niobe.model.Animation} used for this 
	 * special attack.
	 */
	private static final Animation ANIMATION = new Animation(1665);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(282);
}