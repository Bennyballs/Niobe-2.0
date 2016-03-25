package org.niobe.model.weapon.impl;

import org.niobe.world.content.combat.special.SpecialAttack;
import org.niobe.world.content.combat.special.impl.KorasiSwordSpecialAttack;

/**
 * An implementation of {@link org.niobe.model.weapon.impl.DragonScimitarWeapon} 
 * representing a korasi's sword.
 *
 * @author relex lawl
 */
public final class KorasiSwordWeapon extends DragonScimitarWeapon {

	/**
	 * The KorasiSwordWeapon constructor.
	 * @param id	The weapon id.
	 */
	public KorasiSwordWeapon(int id) {
		super(id);
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SPECIAL_ATTACK;
	}
	
	/**
	 * The special attack this weapon uses.
	 */
	private static final SpecialAttack SPECIAL_ATTACK = new KorasiSwordSpecialAttack();
}
