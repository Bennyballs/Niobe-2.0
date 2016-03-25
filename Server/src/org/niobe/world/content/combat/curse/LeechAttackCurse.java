package org.niobe.world.content.combat.curse;

import org.niobe.model.Graphic;
import org.niobe.model.SkillManager.Skill;

/**
 * An implementation of {@link LeechCurse} that represents
 * the leech attack curse prayer.
 *
 * @author relex lawl
 */
public final class LeechAttackCurse extends LeechCurse {

	@Override
	public String getName() {
		return "attack";
	}

	@Override
	public LeechType getLeechType() {
		return LeechType.LEECH;
	}

	@Override
	public Skill getLeechedSkill() {
		return Skill.ATTACK;
	}

	@Override
	public Graphic[] getGraphic() {
		return GRAPHICS;
	}
	
	/**
	 * The {@link org.niobe.model.Graphic} that will be
	 * performed throughout the curse effect.
	 */
	private static final Graphic[] GRAPHICS = {
		new Graphic(-1), //source graphic
		new Graphic(2231), //projectile graphic
		new Graphic(2232) //victim graphic
	};
}
