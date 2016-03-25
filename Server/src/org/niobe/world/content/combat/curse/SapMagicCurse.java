package org.niobe.world.content.combat.curse;

import org.niobe.model.Graphic;
import org.niobe.model.SkillManager.Skill;

/**
 * An implementation of {@link LeechCurse} that represents
 * the sap magic curse prayer.
 *
 * @author relex lawl
 */
public final class SapMagicCurse extends LeechCurse {

	@Override
	public String getName() {
		return "magic";
	}

	@Override
	public LeechType getLeechType() {
		return LeechType.SAP;
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
		new Graphic(2220), //source graphic
		new Graphic(2221), //projectile graphic
		new Graphic(2222) //victim graphic
	};
}
