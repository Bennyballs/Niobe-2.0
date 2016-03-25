package org.niobe.world.content.combat.special.impl;

import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.definition.MobDefinition;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * An implementation of {@link org.niobe.world.content.combat.special.SpecialAttack}
 * that handles the special attack of a bandos godsword.
 *
 * @author relex lawl
 */
public final class BandosGodswordSpecialAttack extends SpecialAttack {

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
	public double getMultiplier() {
		return 1.1;
	}
	
	@Override
	public void specialAction(Player player, GameCharacter victim, Damage damage) {
		int drain = damage.getHits()[0].getDamage() / 10;
		if (victim.isPlayer()) {
			Player other = (Player) victim;
			other.getSkillManager().setCurrentLevel(Skill.DEFENCE, (other.getSkillManager().getCurrentLevel(Skill.DEFENCE) - drain));
			other.getSkillManager().setCurrentLevel(Skill.STRENGTH, (other.getSkillManager().getCurrentLevel(Skill.STRENGTH) - drain));
			other.getSkillManager().setCurrentLevel(Skill.PRAYER, (other.getSkillManager().getCurrentLevel(Skill.PRAYER) - drain));
			other.getSkillManager().setCurrentLevel(Skill.ATTACK, (other.getSkillManager().getCurrentLevel(Skill.ATTACK) - drain));
			other.getSkillManager().setCurrentLevel(Skill.MAGIC, (other.getSkillManager().getCurrentLevel(Skill.MAGIC) - drain));
			other.getSkillManager().setCurrentLevel(Skill.RANGED, (other.getSkillManager().getCurrentLevel(Skill.RANGED) - drain));
		} else if (victim.isMob()) {
			Mob mob = (Mob) victim;
			MobDefinition definition = mob.getDefinition();
			definition.setAttackLevel(definition.getAttackLevel() - drain).setDefenceLevel(definition.getDefenceLevel() - drain)
							.setMagicLevel(definition.getMagicLevel() - drain).setRangedLevel(definition.getRangedLevel() - drain);
		}
	}

	/**
	 * The {@link org.niobe.model.Animation} used for this 
	 * special attack.
	 */
	private static final Animation ANIMATION = new Animation(11991);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(2114);
}
