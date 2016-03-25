package org.niobe.world.content.combat.special.impl;

import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.model.SkillManager.Skill;
import org.niobe.world.Player;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * An implementation of {@link org.niobe.world.content.combat.special.SpecialAttack}
 * that handles the special of a dragon pickaxe.
 *
 * @author relex lawl
 */
public final class DragonPickaxeSpecialAttack extends SpecialAttack {

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
	public void specialAction(Player player, GameCharacter victim, Damage damage) {
		final double drain = .05;
		if (victim.isPlayer()) {
			Player other = (Player) victim;
			other.getSkillManager().setCurrentLevel(Skill.ATTACK, (int) (player.getSkillManager().getCurrentLevel(Skill.ATTACK) * drain));
			other.getSkillManager().setCurrentLevel(Skill.RANGED, (int) (player.getSkillManager().getCurrentLevel(Skill.RANGED) * drain));
			other.getSkillManager().setCurrentLevel(Skill.MAGIC, (int) (player.getSkillManager().getCurrentLevel(Skill.MAGIC) * drain));
		}
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
