package org.niobe.model.weapon.impl;

import org.niobe.world.content.combat.special.SpecialAttack;
import org.niobe.world.content.combat.special.impl.DragonPickaxeSpecialAttack;

/**
 * An implementation of {@link PickaxeWeapon} that
 * represents a dragon pickaxe.
 *
 * @author relex lawl
 */
public final class DragonPickaxeWeapon extends PickaxeWeapon {

	/**
	 * The DragonPickaxeWeapon constructor.
	 * @param id	The weapon's item id.
	 */
	public DragonPickaxeWeapon(int id) {
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
		5570,
		0, //TODO
		0, //TODO
		5573,
		3983,
		0 //TODO
	};
	
	/**
	 * The {@link org.niobe.world.content.combat.special.SpecialAttack}
	 * this weapon uses.
	 */
	private static final SpecialAttack SPECIAL_ATTACK = new DragonPickaxeSpecialAttack();
}
