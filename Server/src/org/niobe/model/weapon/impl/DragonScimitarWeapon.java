package org.niobe.model.weapon.impl;

import org.niobe.world.content.combat.special.SpecialAttack;
import org.niobe.world.content.combat.special.impl.DragonScimitarSpecialAttack;

/**
 * An implementation of {@link org.niobe.model.weapon.impl.ScimitarWeapon} 
 * representing a dragon scimitar.
 *
 * @author relex lawl
 */
public class DragonScimitarWeapon extends ScimitarWeapon {

	/**
	 * The DragonScimitarWeapon constructor.
	 * @param id	The weapon id.
	 */
	public DragonScimitarWeapon(int id) {
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
		2423,
		7599,
		7611,
		2426,
		2427,
		7587
	};
	
	/**
	 * The special attack this weapon uses.
	 */
	private static final SpecialAttack SPECIAL_ATTACK = new DragonScimitarSpecialAttack();
}
