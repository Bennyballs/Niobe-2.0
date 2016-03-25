package org.niobe.model.weapon.impl;

import org.niobe.world.content.combat.special.SpecialAttack;
import org.niobe.world.content.combat.special.impl.MagicShortbowSpecialAttack;

/**
 * An implementation of a {@link BowWeapon} that
 * represents a magic short bow weapon.
 *
 * @author relex lawl
 */
public final class MagicShortbowWeapon extends BowWeapon {

	/**
	 * The MagicShortbowWeapon constructor.
	 * @param id	The weapon's item id.
	 */
	public MagicShortbowWeapon(int id) {
		super(id);
	}
	
	@Override
	public SpecialAttack getSpecialAttack() {
		return SPECIAL_ATTACK;
	}
	
	@Override
	public int[] getInterfaceId() {
		return INTERFACE_ID;
	}

	/**
	 * The interface id's used for the weapon
	 * game frame tab.
	 */
	private static final int[] INTERFACE_ID = {
		1764,
		7549,
		7561,
		1767,
		3983,
		7537
	};
	
	/**
	 * The {@link org.niobe.world.content.combat.special.SpecialAttack}
	 * this weapon uses.
	 */
	private static final SpecialAttack SPECIAL_ATTACK = new MagicShortbowSpecialAttack();
}
