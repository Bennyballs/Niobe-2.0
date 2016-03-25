package org.niobe.model.weapon;

import org.niobe.model.container.impl.Equipment;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.world.Player;
import org.niobe.world.util.GameConstants;

/**
 * Manages an attack tab's special bar, filling and display state.
 * 
 * @author relex lawl
 */
public final class WeaponSpecialBar {

	/**
	 * Updates the player's attack tab interface.
	 * @param player	The player updating interface for.
	 */
	public static void update(Player player) {
		Weapon weapon = WeaponLoader.forId(player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId());
		if (weapon == WeaponLoader.getDefaultWeapon()) {
			removeSpecialBar(player);
			player.getPacketSender().sendString(5858, "Combat Level: " + player.getSkillManager().getCombatLevel());
			player.getPacketSender().sendString(5857, "None");
			player.getPacketSender().sendTabInterface(GameConstants.ATTACK_TAB, 5855);
		} else {
			player.getPacketSender().sendString(weapon.getInterfaceId()[Weapon.COMBAT_LEVEL_INDEX], "Combat Level: " + player.getSkillManager().getCombatLevel());
			player.getPacketSender().sendString(weapon.getInterfaceId()[Weapon.WEAPON_NAME_INDEX], ItemDefinition.forId(weapon.getId()).getName());
			if (weapon.getInterfaceId().length > 5) {
				setSpecialBar(player, weapon);
			} else {
				removeSpecialBar(player);
			}
			player.getPacketSender().sendInterfaceDisplayState(weapon.getInterfaceId()[Weapon.SPECIAL_BAR_INDEX], false);
			player.getPacketSender().sendTabInterface(GameConstants.ATTACK_TAB, weapon.getInterfaceId()[Weapon.ATTACK_INTERFACE_INDEX]);
		}
	}
	
	/**
	 * Sets the attack tab's special attack amount.
	 * @param player	The player to set special attack amount for.
	 * @param weapon	The weapon the player has.
	 */
	public static void setSpecialBar(Player player, Weapon weapon) {
		if (weapon == WeaponLoader.getDefaultWeapon())
			return;
		int specialBarId = weapon.getInterfaceId()[Weapon.SPECIAL_AMOUNT_INDEX];
		int specialAmount = player.getFields().getCombatAttributes().getSpecialAttackAmount();
		player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 100 ? 500 : 0), 0);
		//player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 9.5 ? 500 : 0), 0);
		player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 90 ? 500 : 0), 0);
		//player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 8.5 ? 500 : 0), 0);
		player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 80 ? 500 : 0), 0);
		//player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 7.5 ? 500 : 0), 0);
		player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 70 ? 500 : 0), 0);
		//player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 6.5 ? 500 : 0), 0);
		player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 60 ? 500 : 0), 0);
		//player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 5.5 ? 500 : 0), 0);
		player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 50 ? 500 : 0), 0);
		//player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 4.5 ? 500 : 0), 0);
		player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 40 ? 500 : 0), 0);
		//player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 3.5 ? 500 : 0), 0);
		player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 30 ? 500 : 0), 0);
		//player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 2.5 ? 500 : 0), 0);
		player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 20 ? 500 : 0), 0);
		//player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 1.5 ? 500 : 0), 0);
		player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= 10 ? 500 : 0), 0);
		//player.getPacketSender().sendInterfaceComponentMoval(--specialBarId, (specialAmount >= .5 ? 500 : 0), 0);
		int childId = weapon.getInterfaceId()[Weapon.SPECIAL_AMOUNT_INDEX];
		String color = player.getFields().getCombatAttributes().isUsingSpecialAttack() ? "@yel@ " : "@bla@ ";
		player.getPacketSender().sendString(childId, color + "Special Attack (" + player.getFields().getCombatAttributes().getSpecialAttackAmount() + "%)");
	}
	
	/**
	 * Removes the special bar from the game frame
	 * tab.
	 * @param player	The {@link org.niobe.world.Player} to remove special bar for.
	 */
	public static void removeSpecialBar(Player player) {
		player.getPacketSender()
				.sendInterfaceDisplayState(7624, true)
				.sendInterfaceDisplayState(7474, true)
				.sendInterfaceDisplayState(7499, true)
				.sendInterfaceDisplayState(7549, true)
				.sendInterfaceDisplayState(7574, true)
				.sendInterfaceDisplayState(7599, true)
				.sendInterfaceDisplayState(8493, true)
				.sendInterfaceDisplayState(12323, true);
	}
}
