package org.niobe.model.weapon;

import org.niobe.model.Item;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * Represents a game weapon, which a player can use to inflict a greater
 * amount of damage to their opponent.
 * 
 * @author relex lawl
 */
public abstract class Weapon extends Item {
	
	/**
	 * The Weapon constructor.
	 * @param id	The weapon's game item id.
	 */
	public Weapon(int id) {
		super(id);
		for (int i = 0; i < getCombatDefinition().getButton().length; i++) {
			int button = getCombatDefinition().getButton()[i];
			if (!WeaponLoader.getButtons().containsKey(button)) {
				WeaponLoader.getButtons().put(button, i);
			}
		}
		if (getInterfaceId().length > 5) {
			if (!WeaponLoader.getSpecialAttackButtons().contains(getInterfaceId()[Weapon.SPECIAL_ATTACK_BAR_BUTTON_INDEX])) {
				WeaponLoader.getSpecialAttackButtons().add(getInterfaceId()[Weapon.SPECIAL_ATTACK_BAR_BUTTON_INDEX]);
			}
		}
	}
	
	/**
	 * The interface child id's in the weapon's attack tab interface.
	 */
	public abstract int[] getInterfaceId();
	
	/**
	 * The animations the player will perform whilst wielding the weapon.
	 */
	public abstract WeaponAnimation getAnimations();
	
	/**
	 * The combat definitions of the weapon.
	 */
	public abstract WeaponCombatDefinition getCombatDefinition();
	
	/**
	 * The default distance required from the enemy
	 * in order to engage in combat.
	 */
	public int getAttackDistance() {
		return 1;
	}
	
	/**
	 * The default weapon special.
	 * @return	The special attack, if any, of the weapon.
	 */
	public SpecialAttack getSpecialAttack() {
		return null;
	}
	
	/**
	 * The default flag that checks if the weapon
	 * is used in ranged combat.
	 * @return	If {@code true} the weapon does ranged damage.
	 */
	public boolean isRanged() {
		return false;
	}
	
	/**
	 * Represents an attack style used in combat.
	 *
	 * @author relex lawl
	 */
	public enum AttackStyle {
		/**
		 * A style that grants more strength
		 * in an attack.
		 */
		AGGRESSIVE,
		
		/**
		 * A style that grants more accuracy
		 * in an attack.
		 */
		ACCURATE,
		
		/**
		 * A style that grants more defense
		 * while in combat.
		 */
		DEFENSIVE,
		
		/**
		 * A style that grants an equivalent
		 * balance of strength and defense.
		 */
		CONTROLLED,
		
		/**
		 * A style that is used in ranged combat
		 * that fires faster than other {@link AttackStyle}s.
		 */
		RAPID_RANGED,
				
		/**
		 * A style that is used in a distance
		 * of the interacting game character.
		 */
		LONG_RANGE;
	}
	
	/**
	 * Represents one of many attack styles, many different mobs 
	 * are weaker against (a) certain type of attack styles.
	 * 
	 * @author relex lawl
	 */

	public enum CombatStyle {
		/**
		 * A stab-type attack style.
		 */
		STAB,
		
		/**
		 * A slash-type attack style. Can also be used 'slash' webs, etc.
		 */
		SLASH,
		
		/**
		 * A crush-type attack style.
		 */
		CRUSH,
		
		/**
		 * A magic-type attack style.
		 */
		MAGIC,
		
		/**
		 * A ranged-type attack style.
		 */
		RANGED;
	}

	/**
	 * Represents a weapon speed used in the player's
	 * attack delay while in combat.
	 *
	 * @author relex lawl
	 */
	public enum WeaponSpeed {
		/**
		 * The slowest kind of weapon, used mostly 
		 * for two-handed weapons
		 */
		SLOWEST(10),
		
		/**
		 * The second slowest kind of weapon.
		 */
		SLOW(8),
		
		/**
		 * The normal speed weapon.
		 */
		MEDIUM(6),
		
		/**
		 * The fastest type of weapon.
		 */
		FAST(4);
		
		/**
		 * The WeaponSpeed constructor.
		 * @param delay	The delay of the weapon.
		 */
		private WeaponSpeed(int delay) {
			this.delay = delay;
		}
		
		/**
		 * The value used in the attack delays.
		 */
		private final int delay;
		
		/**
		 * Gets the delay for the weapon.
		 * @return	The delay.
		 */
		public int getDelay() {
			return delay;
		}
	}

	/**
	 * Represents a weapon's attack style's experience earn.
	 * In simpler terms, what skill the attack style earns.
	 * 
	 * @author relex lawl
	 */

	public enum WeaponExperienceStyle {

		/**
		 * Earns experience in the strength skill.
		 */
		STRENGTH,
		
		/**
		 * Earns experience in the attack skill.
		 */
		ATTACK,
		
		/**
		 * Earns experience in the defense skill.
		 */
		DEFENCE,
		
		/**
		 * Earns experience in the strength, attack and defense skill.
		 */
		MELEE_SHARED,
		
		/**
		 * Earns experience in the ranged skill.
		 */
		RANGED,
		
		/**
		 * Earns experience in the ranged and defense skill.
		 */
		RANGED_SHARED,
		
		/**
		 * Earns experience in the magic skill.
		 */
		MAGIC,
		
		/**
		 * Earns experience in the magic and defense skill.
		 */
		MAGIC_SHARED;
	}	

	/**
	 * The index constants for user-friendly fetching of the
	 * {@link #getInterfaceId()} array values.
	 */
	public static final int ATTACK_INTERFACE_INDEX = 0, SPECIAL_BAR_INDEX = 1,
							SPECIAL_AMOUNT_INDEX = 2, WEAPON_NAME_INDEX = 3,
							COMBAT_LEVEL_INDEX = 4, SPECIAL_ATTACK_BAR_BUTTON_INDEX = 5;
}
