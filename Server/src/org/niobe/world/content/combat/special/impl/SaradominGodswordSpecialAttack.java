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
 * that handles the special attack of a saradomin godsword.
 *
 * @author relex lawl
 */
public final class SaradominGodswordSpecialAttack extends SpecialAttack {

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
	public void specialAction(Player player, GameCharacter victim, Damage damage) {
		int constitutionHeal = (damage.getHits()[0].getDamage() / 2) > 100 ? (damage.getHits()[0].getDamage() / 2) : 100;
		int prayerHeal = (damage.getHits()[0].getDamage() /4) > 50 ? (damage.getHits()[0].getDamage() / 4) : 50;
		
		constitutionHeal = (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) + constitutionHeal);
		prayerHeal = (player.getSkillManager().getCurrentLevel(Skill.PRAYER) + prayerHeal);
		
		if (constitutionHeal > player.getSkillManager().getMaxLevel(Skill.CONSTITUTION)) {
			constitutionHeal = player.getSkillManager().getMaxLevel(Skill.CONSTITUTION);
		}
		if (prayerHeal > player.getSkillManager().getMaxLevel(Skill.PRAYER)) {
			prayerHeal = player.getSkillManager().getMaxLevel(Skill.PRAYER);
		}
		player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, constitutionHeal);
		player.getSkillManager().setCurrentLevel(Skill.PRAYER, prayerHeal);
	}

	/**
	 * The {@link org.niobe.model.Animation} used for this 
	 * special attack.
	 */
	private static final Animation ANIMATION = new Animation(7071);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(2109);
}
