package org.niobe.world.content.combat.curse;

import org.niobe.model.Graphic;
import org.niobe.model.SkillManager.Skill;

/**
 * An implementation of {@link LeechCurse} that represents
 * the leech strength curse prayer.
 *
 * @author relex lawl
 */
public final class LeechStrengthCurse extends LeechCurse {

	@Override
	public String getName() {
		return "strength";
	}

	@Override
	public LeechType getLeechType() {
		return LeechType.LEECH;
	}

	@Override
	public Skill getLeechedSkill() {
		return Skill.STRENGTH;
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
		new Graphic(2248), //projectile graphic
		new Graphic(2250) //victim graphic
	};
}
