package org.niobe.world.content.combat.curse;

import org.niobe.model.Graphic;
import org.niobe.model.SkillManager.Skill;

/**
 * An implementation of {@link LeechCurse} that represents
 * the sap ranger curse prayer.
 *
 * @author relex lawl
 */
public final class SapRangerCurse extends LeechCurse {

	@Override
	public String getName() {
		return "ranged";
	}

	@Override
	public LeechType getLeechType() {
		return LeechType.SAP;
	}

	@Override
	public Skill getLeechedSkill() {
		return Skill.RANGED;
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
		new Graphic(2217), //source graphic
		new Graphic(2218), //projectile graphic
		new Graphic(2219) //victim graphic
	};
}
