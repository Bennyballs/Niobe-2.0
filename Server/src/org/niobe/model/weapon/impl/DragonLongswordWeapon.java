package org.niobe.model.weapon.impl;

import org.niobe.world.content.combat.special.SpecialAttack;
import org.niobe.world.content.combat.special.impl.DragonLongswordSpecialAttack;

/**
 * An implementation of {@link org.niobe.model.weapon.Weapon} that
 * represents a dragon longsword.
 *
 * @author relex lawl
 */
public final class DragonLongswordWeapon extends LongswordWeapon {

	/**
	 * The DragonLongswordWeapon constructor.
	 * @param id	The weapon's item id.
	 */
	public DragonLongswordWeapon(int id) {
		super(id);
	}

	@Override
	public int[] getInterfaceId() {
		return INTERFACE_ID;
	}
	
	@Override
	public SpecialAttack getSpecialAttack() {
		return SPECIAL_ATTACK;
	}
	
	/**
	 * The interface id's used for the weapon
	 * game frame tab.
	 */
	private static final int[] INTERFACE_ID = {
		2423,
		7599,
		7611,
		2426,
		2427,
		7587
	};
	
	/**
	 * The {@link org.niobe.world.content.combat.special.SpecialAttack}
	 * this weapon uses.
	 */
	private static final SpecialAttack SPECIAL_ATTACK = new DragonLongswordSpecialAttack();
}
