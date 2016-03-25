package org.niobe.world.content.combat.special.impl;

import org.niobe.model.Animation;
import org.niobe.model.Graphic;
import org.niobe.model.Graphic.GraphicHeight;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * An implementation of {@link org.niobe.world.content.combat.special.SpecialAttack}
 * that handles the special of a dragon longsword.
 *
 * @author relex lawl
 */
public final class DragonLongswordSpecialAttack extends SpecialAttack {

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
		return 25;
	}
	
	@Override
	public double getMultiplier() {
		return 1.25;
	}

	/**
	 * The {@link org.niobe.model.Animation} used for this 
	 * special attack.
	 */
	private static final Animation ANIMATION = new Animation(12033);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(2117, GraphicHeight.HIGH);
}
