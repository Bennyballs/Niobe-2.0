package org.niobe.world.content.combat.special.impl;

import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.world.Player;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * Represents an abyssal whip special attack.
 * 
 * @author relex lawl
 */
public final class AbyssalWhipSpecialAttack extends SpecialAttack {

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
	public boolean victimGraphic() {
		return true;
	}

	@Override
	public void specialAction(Player player, GameCharacter victim, Damage damage) {
		int totalRunEnergy = victim.getFields().getRunEnergy() - 25;
		if (totalRunEnergy < 0)
			totalRunEnergy = 0;
		victim.getFields().setRunEnergy(totalRunEnergy);
	}
	
	/**
	 * The {@link org.niobe.model.Animation} used for this 
	 * special attack.
	 */
	private static final Animation ANIMATION = new Animation(11971);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(2108);
}
