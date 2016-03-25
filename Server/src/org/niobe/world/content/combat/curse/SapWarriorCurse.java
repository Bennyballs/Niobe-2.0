package org.niobe.world.content.combat.curse;

import org.niobe.model.Graphic;
import org.niobe.model.SkillManager.Skill;

/**
 * An implementation of {@link LeechCurse} that represents
 * the sap warrior curse prayer.
 *
 * @author relex lawl
 */
public final class SapWarriorCurse extends LeechCurse {

	@Override
	public String getName() {
		return "attack";
	}
	
	@Override
	public LeechType getLeechType() {
		return LeechType.SAP;
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
		new Graphic(2214), //source graphic
		new Graphic(2215), //projectile graphic
		new Graphic(2216) //victim graphic
	};
}
