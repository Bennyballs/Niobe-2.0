package org.niobe.world.content.combat.special.impl;

import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.UpdateFlag.Flag;
import org.niobe.world.Player;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * An implementation of {@link org.niobe.world.content.combat.special.SpecialAttack}
 * that handles the special of a dragon battle axe.
 *
 * @author relex lawl
 */
public final class DragonBattleAxeSpecialAttack extends SpecialAttack {

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
	public boolean isImmediate(Player player) {
		return true;
	}
	
	@Override
	public void specialAction(Player player, GameCharacter victim, Damage damage) {
		double boost = player.getSkillManager().getMaxLevel(Skill.STRENGTH) * .2;
		player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 
				(int) (player.getSkillManager().getMaxLevel(Skill.STRENGTH) + boost));
		
		boost = player.getSkillManager().getMaxLevel(Skill.ATTACK) * .1;
		player.getSkillManager().setCurrentLevel(Skill.ATTACK, 
				(int) (player.getSkillManager().getMaxLevel(Skill.ATTACK) - boost));
		
		boost = player.getSkillManager().getMaxLevel(Skill.DEFENCE) * .1;
		player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 
				(int) (player.getSkillManager().getMaxLevel(Skill.DEFENCE) - boost));
		
		boost = player.getSkillManager().getMaxLevel(Skill.RANGED) * .1;
		player.getSkillManager().setCurrentLevel(Skill.RANGED, 
				(int) (player.getSkillManager().getMaxLevel(Skill.RANGED) - boost));
		
		boost = player.getSkillManager().getMaxLevel(Skill.MAGIC) * .1;
		player.getSkillManager().setCurrentLevel(Skill.MAGIC, 
				(int) (player.getSkillManager().getMaxLevel(Skill.MAGIC) - boost));
		
		player.getFields().setForcedChat("Raarrrrrgggggghhhhhhh!");
		player.getUpdateFlag().flag(Flag.FORCED_CHAT);
	}
	
	/**
	 * The {@link org.niobe.model.Animation} used for this 
	 * special attack.
	 */
	private static final Animation ANIMATION = new Animation(1056);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(246);
}
