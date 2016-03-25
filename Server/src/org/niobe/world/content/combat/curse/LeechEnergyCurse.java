package org.niobe.world.content.combat.curse;

import org.niobe.model.Graphic;
import org.niobe.model.SkillManager.Skill;

/**
 * An implementation of {@link LeechCurse} that represents
 * the leech run energy curse prayer.
 *
 * @author relex lawl
 */
public final class LeechEnergyCurse extends LeechCurse {
	
	@Override
	public boolean leechRun() {
		return true;
	}

	@Override
	public String getName() {
		return "run energy";
	}

	@Override
	public LeechType getLeechType() {
		return LeechType.LEECH;
	}

	@Override
	public Skill getLeechedSkill() {
		return null;
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
		new Graphic(2252), //projectile graphic
		new Graphic(2254) //victim graphic
	};
}
