package org.niobe.model.weapon.impl;

/**
 * An implementation of {@link BowWeapon} used
 * for short bow weapons, only difference is their
 * {@link org.niobe.model.weapon.Weapon.WeaponSpeed}.
 *
 * @author relex lawl
 */
public class LongbowWeapon extends BowWeapon {

	/**
	 * The LongbowWeapon constructor.
	 * @param id	The weapon's item id.
	 */
	public LongbowWeapon(int id) {
		super(id);
		getCombatDefinition().setSpeed(WeaponSpeed.MEDIUM);
	}
}
