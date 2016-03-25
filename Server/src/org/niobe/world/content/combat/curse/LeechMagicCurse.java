package org.niobe.world.content.combat.curse;

import org.niobe.model.Graphic;
import org.niobe.model.SkillManager.Skill;

/**
 * An implementation of {@link LeechCurse} that represents
 * the leech magic curse prayer.
 *
 * @author relex lawl
 */
public final class LeechMagicCurse extends LeechCurse {

	@Override
	public String getName() {
		return "magic";
	}

	@Override
	public LeechType getLeechType() {
		return LeechType.LEECH;
	}

	@Override
	public Skill getLeechedSkill() {
		return Skill.MAGIC;
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
		new Graphic(2240), //projectile graphic
		new Graphic(2242) //victim graphic
	};
}
