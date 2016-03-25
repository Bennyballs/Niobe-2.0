package org.niobe.world.content.combat.curse;

import org.niobe.model.Graphic;
import org.niobe.model.SkillManager.Skill;

/**
 * An implementation of {@link LeechCurse} that represents
 * the sap spirit curse prayer.
 *
 * @author relex lawl
 */
public final class SapSpiritCurse extends LeechCurse {

	@Override
	public boolean leechSpecial() {
		return true;
	}
	
	@Override
	public String getName() {
		return "special attack energy";
	}

	@Override
	public LeechType getLeechType() {
		return LeechType.SAP;
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
		new Graphic(2223), //source graphic
		new Graphic(2224), //projectile graphic
		new Graphic(2225) //victim graphic
	};
}
