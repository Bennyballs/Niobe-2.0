package org.niobe.model;

/**
 * Represents a hit dealt to a {@link GameCharacter}.
 *
 * @author relex lawl
 */
public final class Damage {
	
	/**
	 * The Damage constructor.
	 * @param hits	The {@link Hit} instance(s), length affects the update mask
	 * 				which will either be single or double hit.
	 */
	public Damage(Hit... hits) {
		if (hits.length > 2 || hits.length <= 0)
			throw new IllegalArgumentException("Hit array length cannot be less than 1 and cannot be greater than 2!");
		this.hits = hits;
	}

	/**
	 * The hits done in this damage.
	 */
	private final Hit[] hits;
	
	/**
	 * Gets the hits done by the damage.
	 * @return	The hits array.
	 */
	public Hit[] getHits() {
		return hits;
	}
	
	/**
	 * Represents a damage's combat icon.
	 * 
	 * @author relex lawl
	 */
	public enum CombatIcon {
		
		/**
		 * No combat icon will be drawn.
		 */
		NONE,
		
		/**
		 * A sword icon will be drawn next to the hit.
		 */
		MELEE,
		
		/**
		 * A bow icon will be drawn next to the hit.
		 */
		RANGED,
		
		/**
		 * A magic hat will be drawn next to the hit.
		 */
		MAGIC,
		
		/**
		 * An arrow-like object will be drawn next to the hit.
		 */
		DEFLECT,
		
		/**
		 * A cannon ball will be drawn next to the hit.
		 */
		CANNON;

		/**
		 * Gets the id that will be sent to client for said CombatIcon.
		 * @return	The index that will be sent to client.
		 */
		public int getId() {
			return ordinal() - 1;
		}
		
		/**
		 * Gets the CombatIcon object for said id, being compared
		 * to it's ordinal (so ORDER IS CRUCIAL).
		 * @param id	The ordinal index of the combat icon.
		 * @return		The CombatIcon who's ordinal equals id.
		 */
		public static CombatIcon forId(int id) {
			for (CombatIcon icon : CombatIcon.values()) {
				if (icon.getId() == id)
					return icon;
			}
			return CombatIcon.NONE;
		}
	}
	
	/**
	 * Represents a Hit mask type/color.
	 * 
	 * @author relex lawl
	 */

	public enum Hitmask {
		
		/**
		 * A normal red hitmask.
		 */
		NORMAL,
		
		/**
		 * An orange-like hitmask, representing a high-hit.
		 */
		CRITICAL,
		
		/**
		 * A green hitmask, representing poison.
		 */
		POISON,
		
		/**
		 * A yellow hitmask, representing a disease.
		 */
		DISEASE,
		
		/**
		 * A purple hitmask, used for healing in dungeoneering.
		 */
		HEAL,
		
		/**
		 * A dull grey-red hitmask, representing the lowest-hits.
		 */
		LOWEST_DAMAGE,
		
		/**
		 * A grey-red hitmask, representing a low-hit done by a critical damage.
		 */
		LOW_CRITICAL_DAMAGE,
		
		/**
		 * A grey-green hitmask, representing a low poison hit.
		 */
		LOW_POISON_DAMAGE,
		
		/**
		 * A grey-yellow hitmask, representing a low disease hit.
		 */
		LOW_DISEASE_DAMAGE,
		
		/**
		 * A grey-pruple hitmask, representing a low heal amount.
		 */
		LOW_HEAL;
	}
	
	/**
	 * Represents the actual hit of a {@link Damage}.
	 *
	 * @author relex lawl
	 */
	public static final class Hit {

		/**
		 * The Hit constructor.
		 * @param damage		The numerical value the associated {@link GameCharacter} was hit for.
		 * @param absorption	The amount of {@param damage} that will be absorbed.
		 * @param combatIcon	The combat icon that will be sent to the client to show.
		 * @param hitmask		The hit mask color that will be sent to client to show.
		 */
		public Hit(int damage, int absorption, CombatIcon combatIcon, Hitmask hitmask) {
			this.damage = damage;
			this.absorption = absorption;
			this.combatIcon = combatIcon;
			this.hitmask = hitmask;
		}
		
		/**
		 * The Hit constructor.
		 * @param damage		The numerical value the associated {@link GameCharacter} was hit for.
		 * @param combatIcon	The combat icon that will be sent to the client to show.
		 * @param hitmask		The hit mask color that will be sent to client to show.
		 */
		public Hit(int damage, CombatIcon combatIcon, Hitmask hitmask) {
			this(damage, 0, combatIcon, hitmask);
		}

		/**
		 * The numerical value the 
		 */
		private int damage;
		
		/**
		 * The combat icon that will be shown besides the damage.
		 */
		private CombatIcon combatIcon;
		
		/**
		 * The hit mask color that will be shown.
		 */
		private final Hitmask hitmask;
		
		/**
		 * The amount of damage that will be absorbed.
		 */
		private final int absorption;
		
		/**
		 * Gets the numerical damage.
		 * @return	The damage dealt.
		 */
		public int getDamage() {
			return damage;
		}
		
		/**
		 * Sets the damage dealt to the associated {@link GameCharacter}.
		 * @param damage	The numerical damage to set to.
		 * @return			The Hit instance.
		 */
		public Hit setDamage(int damage) {
			this.damage = damage;
			return this;
		}

		/**
		 * Gets the combat icon that will be 
		 * shown besides the damage.
		 * @return	The combat icon.
		 */
		public CombatIcon getCombatIcon() {
			return combatIcon;
		}

		/**
		 * Sets the {@link #combatIcon} that will
		 * be shown besides the {@link #damage}.
		 * @return	The {@link Hit} instance.
		 */
		public Hit setCombatIcon(CombatIcon combatIcon) {
			this.combatIcon = combatIcon;
			return this;
		}
		
		/**
		 * Gets the hit mask color that will
		 * be shown.
		 * @return	The hit mask color/type.
		 */
		public Hitmask getHitmask() {
			return hitmask;
		}

		/**
		 * Gets the amount of damage to absorb - if any.
		 * @return	The absorption value.
		 */
		public int getAbsorption() {
			return absorption;
		}
	}
}