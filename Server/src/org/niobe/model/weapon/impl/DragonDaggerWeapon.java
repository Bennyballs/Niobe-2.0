package org.niobe.model.weapon.impl;

import org.niobe.model.Animation;
import org.niobe.world.content.combat.special.SpecialAttack;
import org.niobe.world.content.combat.special.impl.DragonDaggerSpecialAttack;

/**
 * An implementation of {@link org.niobe.model.weapon.impl.DaggerWeapon} 
 * representing a dragon dagger.
 *
 * @author relex lawl
 */
public final class DragonDaggerWeapon extends DaggerWeapon {

	/**
	 * The DragonDaggerWeapon constructor.
	 * @param id	The weapon id.
	 */
	public DragonDaggerWeapon(int id) {
		super(id);
	}

	@Override
	public int[] getInterfaceId() {
		return INTERFACE_ID;
	}
	
	@Override
	public Animation[] getAttackAnimation() {
		return ATTACK_ANIMATION;
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
		2276,
		7574,
		7586,
		2279,
		2280,
		7562
	};
	
	/**
	 * The animations used for the weapon.
	 */
	private static final Animation[] ATTACK_ANIMATION = {
		new Animation(376),
		new Animation(376),
		new Animation(377),
		new Animation(376)
	};
	
	/**
	 * The special attack this weapon uses.
	 */
	private static final SpecialAttack SPECIAL_ATTACK = new DragonDaggerSpecialAttack();
}
