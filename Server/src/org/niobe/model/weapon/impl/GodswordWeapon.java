package org.niobe.model.weapon.impl;

/**
 * An implementation of {@link org.niobe.model.weapon.impl.TwoHandedSwordWeapon} 
 * representing a godsword weapon.
 *
 * @author relex lawl
 */
public class GodswordWeapon extends TwoHandedSwordWeapon {

	/**
	 * The GodswordWeapon constructor.
	 * @param id	The weapon id.
	 */
	public GodswordWeapon(int id) {
		super(id);
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
		4705,
		7699,
		7711,
		4708,
		4709,
		7687
	};
}