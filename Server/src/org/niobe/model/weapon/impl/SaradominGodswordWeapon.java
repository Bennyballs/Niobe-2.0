package org.niobe.model.weapon.impl;

import org.niobe.world.content.combat.special.SpecialAttack;
import org.niobe.world.content.combat.special.impl.SaradominGodswordSpecialAttack;

/**
 * An implementation of {@link org.niobe.model.weapon.Weapon} 
 * representing a saradomin godsword.
 *
 * @author relex lawl
 */
public final class SaradominGodswordWeapon extends GodswordWeapon {

	/**
	 * The SaradominGodswordWeapon constructor.
	 * @param id	The weapon id.
	 */
	public SaradominGodswordWeapon(int id) {
		super(id);
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SPECIAL_ATTACK;
	}
	
	/**
	 * The special attack used for armadyl godswords.
	 */
	private static final SpecialAttack SPECIAL_ATTACK = new SaradominGodswordSpecialAttack();
}