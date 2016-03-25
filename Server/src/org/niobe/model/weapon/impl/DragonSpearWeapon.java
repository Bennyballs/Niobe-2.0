package org.niobe.model.weapon.impl;

import org.niobe.world.content.combat.special.SpecialAttack;
import org.niobe.world.content.combat.special.impl.DragonSpearSpecialAttack;

/**
 * An implementation of {@link org.niobe.model.weapon.Weapon} 
 * representing a dragon spear.
 *
 * @author relex lawl
 */
public final class DragonSpearWeapon extends SpearWeapon {

	/**
	 * The DragonSpearWeapon constructor.
	 * @param id	The weapon's item id.
	 */
	public DragonSpearWeapon(int id) {
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
		4679,
		7674,
		7686,
		4682,
		4683,
		7662
	};
	
	/**
	 * The {@link org.niobe.world.content.combat.special.SpecialAttack}
	 * this weapon uses.
	 */
	private static final SpecialAttack SPECIAL_ATTACK = new DragonSpearSpecialAttack();
}
