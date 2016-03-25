package org.niobe.world.content;

import org.niobe.model.Item;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.world.Player;

/**
 * Manages the equipment bonus information
 * for players.
 *
 * @author relex lawl
 */
public final class BonusManager {
	
	/**
	 * Updates a player's equipment interface with
	 * corresponding values.
	 * @param player	The player to update interface for.
	 */
	public static void update(Player player) {
		double[] bonuses = new double[18];
		for (Item item : player.getEquipment().getItems()) {
			ItemDefinition definition = ItemDefinition.forId(item.getId());
			for (int i = 0; i < definition.getBonus().length; i++) {
				bonuses[i] += definition.getBonus()[i];
			}
		}
		for (int i = 0; i < STRING_ID.length; i++) {
			String bonus = String.valueOf(bonuses[i]);
			bonus = bonus.substring(0, bonus.length() - 2);
			if (i <= 4) {
				player.getFields().getBonusManager().attackBonus[i] = bonuses[i];
			} else if (i <= 13) {
				int index = i - 5;
				player.getFields().getBonusManager().defenceBonus[index] = bonuses[i];
			} else if (i <= 17) {
				int index = i - 14;
				player.getFields().getBonusManager().otherBonus[index] = bonuses[i];
				if (index == 1 || index == 3)
					bonus = bonus + "%";
			}
			if (bonuses[i] > 0) {
				bonus = "+" + bonus;
			}
			player.getPacketSender().sendString(Integer.valueOf(STRING_ID[i][0]), STRING_ID[i][1] + ": " + bonus);
		}
	}
	
	/**
	 * Gets the attack bonus values.
	 * @return	The attack bonus array.
	 */
	public double[] getAttackBonus() {
		return attackBonus;
	}
	
	/**
	 * Adds {@value addition} to {@link AttackBonus}.
	 * @param attackBonus	The attack bonus to add to.
	 * @param addition		The value to add to said bonus.
	 * @return				The BonusManager instance.
	 */
	public BonusManager addAttackBonus(AttackBonus attackBonus, int addition) {
		this.attackBonus[attackBonus.ordinal()] += addition;
		return this;
	}
	
	/**
	 * Gets the defense bonus.
	 * @return	The defense bonus array.
	 */
	public double[] getDefenceBonus() {
		return defenceBonus;
	}
	
	/**
	 * Adds {@value addition} to {@link DefenceBonus}.
	 * @param defenceBonus	The defense bonus to add to.
	 * @param addition		The amount to add to said bonus.
	 * @return				The BonusManager instance.
	 */
	public BonusManager addDefenceBonus(DefenceBonus defenceBonus, int addition) {
		this.defenceBonus[defenceBonus.ordinal()] += addition;
		return this;
	}
	
	/**
	 * Gets the uncategorized bonus.
	 * @return	The other bonus array.
	 */
	public double[] getOtherBonus() {
		return otherBonus;
	}
	
	/**
	 * Adds {@value addition} to {@link OtherBonus}.
	 * @param otherBonus	The other bonus to add to.
	 * @param addition		The amount to add to said bonus.
	 * @return				The BonusManager instance.
	 */
	public BonusManager addOtherBonus(OtherBonus otherBonus, int addition) {
		this.otherBonus[otherBonus.ordinal()] += addition;
		return this;
	}
	
	/**
	 * The {@link AttackBonus} equipment bonus array.
	 */
	private double[] attackBonus = new double[AttackBonus.values().length];
	
	/**
	 * The {@link DefenceBonus} equipment bonus array.
	 */
	private double[] defenceBonus = new double[DefenceBonus.values().length];
	
	/**
	 * The {@link OtherBonus} equipment bonus array.
	 */
	private double[] otherBonus = new double[OtherBonus.values().length];
	
	/**
	 * Represents an attack bonus in the 
	 * equipment interface.
	 *
	 * @author relex lawl
	 */
	public enum AttackBonus {
		/**
		 * A stab attack bonus.
		 */
		STAB,

		/**
		 * A slash attack bonus.
		 */
		SLASH,
		
		/**
		 * A crush attack bonus.
		 */
		CRUSH,
		
		/**
		 * A magic attack bonus.
		 */
		MAGIC,
		
		/**
		 * A ranged attack bonus.
		 */
		RANGE,
	}
	
	/**
	 * Represents an defense bonus in the 
	 * equipment interface.
	 *
	 * @author relex lawl
	 */
	public enum DefenceBonus {
		/**
		 * A stab defense bonus.
		 */
		STAB,
		
		/**
		 * A slash defense bonus.
		 */
		SLASH,
		
		/**
		 * A crush defense bonus.
		 */
		CRUSH,
		
		/**
		 * A magic defense bonus.
		 */
		MAGIC,
		
		/**
		 * A ranged defense bonus.
		 */
		RANGE,
		
		/**
		 * A summoning defense bonus.
		 */
		SUMMONING,
		
		/**
		 * A melee absorption bonus.
		 */
		ABSORB_MELEE,
		
		/**
		 * A magic absorption bonus.
		 */
		ABSORB_MAGIC,
		
		/**
		 * A ranged absorption bonus.
		 */
		ABSORB_RANGED;
	}

	/**
	 * Represents an uncategorized bonus
	 * in the equipment interface.
	 *
	 * @author relex lawl
	 */
	public enum OtherBonus {
		/**
		 * The strength bonus.
		 */
		STRENGTH,
		
		/**
		 * The ranged strength bonus.
		 */
		RANGED_STRENGTH,
		
		/**
		 * The prayer bonus.
		 */
		PRAYER,
		
		/**
		 * The magic damage bonus.
		 */
		MAGIC_DAMAGE;
	}

	/**
	 * The array contains all the information necessary
	 * to update the equipment interface.
	 */
	private static final String[][] STRING_ID = {
		{"1675", "Stab"},
		{"1676", "Slash"},
		{"1677", "Crush"},
		{"1678", "Magic"},
		{"1679", "Range"},
		
		{"1680", "Stab"},
		{"1681", "Slash"},
		{"1682", "Crush"},
		{"1683", "Magic"},
		{"1684", "Range"},
		{"16522", "Summoning"},
		{"16523", "Absorb Melee"},
		{"16524", "Absorb Magic"},
		{"16525", "Absorb Ranged"},
		
		{"1686", "Strength"},
		{"16526", "Ranged Strength"},
		{"1687", "Prayer"},
		{"16527", "Magic Damage"}
	};
}
