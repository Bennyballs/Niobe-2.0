package org.niobe.model.weapon.impl;

import org.niobe.world.content.combat.special.SpecialAttack;
import org.niobe.world.content.combat.special.impl.BandosGodswordSpecialAttack;

/**
 * An implementation of {@link org.niobe.model.weapon.impl.GodswordWeapon} 
 * representing a bandos godsword.
 *
 * @author relex lawl
 */
public final class BandosGodswordWeapon extends GodswordWeapon {

	/**
	 * The BandosGodswordWeapon constructor.
	 * @param id	The weapon id.
	 */
	public BandosGodswordWeapon(int id) {
		super(id);
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SPECIAL_ATTACK;
	}
	
	/**
	 * The special attack used for armadyl godswords.
	 */
	private static final SpecialAttack SPECIAL_ATTACK = new BandosGodswordSpecialAttack();
}