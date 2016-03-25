package org.niobe.world.content.combat.special.impl;

import org.niobe.model.Animation;
import org.niobe.model.Graphic;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * Represents a powerful and accurate armadyl godsword
 * special attack.
 * 
 * @author relex lawl
 */
public final class ArmadylGodswordSpecialAttack extends SpecialAttack {

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
	public double getMultiplier() {
		return 1.375;
	}
	
	/**
	 * The {@link org.niobe.model.Animation} used for this 
	 * special attack.
	 */
	private static final Animation ANIMATION = new Animation(11989);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(2113);
}
