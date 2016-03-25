package org.niobe.world.content.combat.special.impl;

import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.model.Damage.CombatIcon;
import org.niobe.world.Player;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * An implementation of {@link org.niobe.world.content.combat.special.SpecialAttack}
 * that handles the special attack of a saradomin sword.
 *
 * @author relex lawl
 */
public final class SaradominSwordSpecialAttack extends SpecialAttack {
	
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
	public boolean isDoubleHit() {
		return true;
	}
	
	@Override
	public boolean victimGraphic() {
		return true;
	}
	
	@Override
	public void specialAction(Player player, GameCharacter victim, Damage damage) {
		damage.getHits()[0].setCombatIcon(CombatIcon.MELEE);
		damage.getHits()[1].setCombatIcon(CombatIcon.MAGIC);
	}

	/**
	 * The {@link org.niobe.model.Animation} used for this 
	 * special attack.
	 */
	private static final Animation ANIMATION = new Animation(11993);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(1194);
}
