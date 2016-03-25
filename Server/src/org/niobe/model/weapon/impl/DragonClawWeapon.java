package org.niobe.model.weapon.impl;

import org.niobe.world.content.combat.special.SpecialAttack;
import org.niobe.world.content.combat.special.impl.DragonClawSpecialAttack;

/**
 * An implementation of {@link org.niobe.model.weapon.impl.ClawWeapon} 
 * representing a pair of dragon claws.
 *
 * @author relex lawl
 */
public final class DragonClawWeapon extends ClawWeapon {

	/**
	 * The DragonClawWeapon constructor.
	 * @param id	The weapon id.
	 */
	public DragonClawWeapon(int id) {
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
		7762,
		7800,
		7812,
		7765,
		7766,
		7788
	};
	
	/**
	 * The special attack this weapon uses.
	 */
	private static final SpecialAttack SPECIAL_ATTACK = new DragonClawSpecialAttack();
}
