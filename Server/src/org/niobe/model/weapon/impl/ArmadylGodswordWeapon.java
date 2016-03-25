package org.niobe.model.weapon.impl;

import org.niobe.world.content.combat.special.SpecialAttack;
import org.niobe.world.content.combat.special.impl.ArmadylGodswordSpecialAttack;

/**
 * An implementation of {@link org.niobe.model.weapon.impl.GodswordWeapon} 
 * representing an armadyl godsword.
 *
 * @author relex lawl
 */
public final class ArmadylGodswordWeapon extends GodswordWeapon {

	/**
	 * The ArmadylGodswordWeapon constructor.
	 * @param id	The weapon id.
	 */
	public ArmadylGodswordWeapon(int id) {
		super(id);
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SPECIAL_ATTACK;
	}
	
	/**
	 * The special attack used for armadyl godswords.
	 */
	private static final SpecialAttack SPECIAL_ATTACK = new ArmadylGodswordSpecialAttack();
}