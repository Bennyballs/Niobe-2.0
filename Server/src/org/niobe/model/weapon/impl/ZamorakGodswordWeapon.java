package org.niobe.model.weapon.impl;

import org.niobe.world.content.combat.special.SpecialAttack;
import org.niobe.world.content.combat.special.impl.ZamorakGodswordSpecialAttack;

/**
 * An implementation of {@link org.niobe.model.weapon.impl.GodswordWeapon} 
 * representing a zamorak godsword.
 *
 * @author relex lawl
 */
public final class ZamorakGodswordWeapon extends GodswordWeapon {

	/**
	 * The ZamorakGodswordWeapon constructor.
	 * @param id	The weapon id.
	 */
	public ZamorakGodswordWeapon(int id) {
		super(id);
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SPECIAL_ATTACK;
	}
	
	/**
	 * The special attack used for armadyl godswords.
	 */
	private static final SpecialAttack SPECIAL_ATTACK = new ZamorakGodswordSpecialAttack();
}