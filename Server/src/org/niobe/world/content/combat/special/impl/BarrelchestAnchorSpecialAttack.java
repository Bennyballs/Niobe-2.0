package org.niobe.world.content.combat.special.impl;

import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.model.SkillManager.Skill;
import org.niobe.util.MathUtil;
import org.niobe.world.Player;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * An implementation of {@link org.niobe.world.content.combat.special.SpecialAttack}
 * that handles the special of a barrelchest anchor.
 *
 * @author relex lawl
 */
public final class BarrelchestAnchorSpecialAttack extends SpecialAttack {

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
		return 2;
	}
	
	@Override
	public double getAccuracy() {
		return 2;
	}
	
	@Override
	public void specialAction(Player player, GameCharacter victim, Damage damage) {
		if (victim.isPlayer()) {
			Player other = (Player) victim;
			double drain = damage.getHits()[0].getDamage() * .01;
			int random = MathUtil.random(3);
			Skill skill = Skill.ATTACK;
			switch (random) {
			case 0:
				skill = Skill.DEFENCE;
				break;
			case 1:
				skill = Skill.RANGED;
				break;
			case 2:
				skill = Skill.MAGIC;
				break;
			}
			other.getSkillManager().setCurrentLevel(skill, (int) (other.getSkillManager().getCurrentLevel(skill) - (other.getSkillManager().getCurrentLevel(skill) * drain)));
		}
	}
	
	/**
	 * The {@link org.niobe.model.Animation} used for this 
	 * special attack.
	 */
	private static final Animation ANIMATION = new Animation(5870);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(1027);
}
