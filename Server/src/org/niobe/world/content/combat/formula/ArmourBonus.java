package org.niobe.world.content.combat.formula;

import org.niobe.model.AttackType;
import org.niobe.model.container.impl.Equipment;
import org.niobe.world.Player;

/**
 * This class contains all the combat-related equipment
 * flags, to keep things organized.
 *
 * @author relex lawl
 */
public final class ArmourBonus {
	
	/**
	 * Checks if the {@link org.niobe.world.Player} has a barrows
	 * type armour equipped.
	 * @param player		The player to check for.
	 * @param barrowsType	The {@link BarrowsType} to look for in {@link org.niobe.model.container.impl.Equipment}.
	 * @return				If {@code true} the player has barrows equipped.
	 */
	public static boolean hasBarrows(Player player, BarrowsType barrowsType) {
		String name = barrowsType.toString().toLowerCase();
		int[] parts = { 
				Equipment.HEAD_SLOT,
				Equipment.WEAPON_SLOT,
				Equipment.BODY_SLOT,
				Equipment.LEG_SLOT,
		};
		for (int index : parts) {
			if (!player.getEquipment().getItems()[index].getDefinition().getName().toLowerCase().startsWith(name)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the player has void knight armor equipped.
	 * @param player		The {@link org.niobe.world.Player} to check void bonus for.
	 * @param attackType	The {@link org.niobe.model.AttackType} to get void knight armour for.
	 * @return				{@code true} if {@value player} has void knight armour equipped.
	 */
	public static boolean hasVoidBonus(Player player, AttackType attackType) {
		int correctEquipment = 0;
		int helmet = attackType == AttackType.MAGIC ? MAGE_VOID_HELM :
						attackType == AttackType.RANGED ? RANGED_VOID_HELM : MELEE_VOID_HELM;
		for (int armour[] : VOID_ARMOUR) {
			if (player.getEquipment().getItems()[armour[0]].getId() == armour[1] ||
					player.getEquipment().getItems()[armour[0]].getId() == ELITE_VOID_ARMOUR[0]) {
				correctEquipment++;
			}
		}
		if (player.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == VOID_KNIGHT_DEFLECTOR) {
			correctEquipment++;
		}
		return correctEquipment >= 3 && player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == helmet;
	}
	
	/**
	 * Represents a barrows type armour
	 * set.
	 *
	 * @author relex lawl
	 */
	public enum BarrowsType {
		/**
		 * Guthan the Infested.
		 */
		GUTHAN,
		
		/**
		 * Torag the Corrupted.
		 */
		TORAG,
		
		/**
		 * Karil the Tainted.
		 */
		KARIL,
		
		/**
		 * Ahrim the Blighted.
		 */
		AHRIM,
		
		/**
		 * Dharok the Wretched.
		 */
		DHAROK,
		
		/**
		 * Verac the Defiled.
		 */
		VERAC;
	}
		
	/**
	 * The magic void knight armour helmet
	 * {@link org.niobe.model.Item#getId()}.
	 */
	private static final int MAGE_VOID_HELM = 11663;
	
	/**
	 * The ranged void knight armour helmet
	 * {@link org.niobe.model.Item#getId()}.
	 */
	private static final int RANGED_VOID_HELM = 11664;
	
	/**
	 * The melee void knight armour helmet
	 * {@link org.niobe.model.Item#getId()}.
	 */
	private static final int MELEE_VOID_HELM = 11665;
	
	/**
	 * The void knight armour deflector
	 * {@link org.niobe.model.Item#getId()}.
	 */
	private static final int VOID_KNIGHT_DEFLECTOR = 19712;
	
	/**
	 * The main void knight armour
	 * {@link org.niobe.model.Item#getId()}s.
	 */
	public static final int[][] VOID_ARMOUR = {
		{Equipment.BODY_SLOT, 8839},
		{Equipment.LEG_SLOT, 8840},
		{Equipment.HANDS_SLOT, 8842}
	};
	
	/**
	 * The elite void knight armour
	 * {@link org.niobe.model.Item#getId()}s.
	 */
	public static final int[] ELITE_VOID_ARMOUR = {
		19785,
		19786,
		8842
	};
}
