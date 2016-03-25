package org.niobe.net.packet.event;

import java.util.logging.Logger;

import org.niobe.GameServer;
import org.niobe.model.Item;
import org.niobe.model.UpdateFlag.Flag;
import org.niobe.model.container.impl.Equipment;
import org.niobe.model.weapon.Weapon;
import org.niobe.model.weapon.WeaponLoader;
import org.niobe.model.weapon.WeaponSpecialBar;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Player;
import org.niobe.world.content.BonusManager;
import org.niobe.world.content.EmotesTab;
import org.niobe.world.content.SkillCapeEmotes;
import org.niobe.world.content.clan.ClanChat.MessageColor;
import org.niobe.world.content.clan.ClanChatManager;
import org.niobe.world.content.combat.CombatCurses;
import org.niobe.world.content.combat.CombatManager;
import org.niobe.world.content.combat.CombatPrayers;
import org.niobe.world.content.dialogue.Dialogue;
import org.niobe.world.content.dialogue.OptionDialogue;
import org.niobe.world.content.dialogue.Dialogue.DialogueType;
import org.niobe.world.util.GameConstants;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * that handles the clicking of client interface buttons.
 *
 * @author relex lawl
 */
public final class ButtonClickGamePacket implements GamePacketEvent {
	
	/**
	 * The PacketListener logger to debug information and print out errors.
	 */
	private static final Logger logger = Logger.getLogger(ButtonClickGamePacket.class.getName());

	@Override
	public void read(Player player, GamePacket packet) {
		int id = packet.readShort();
		if (!defaultButton(player, id))
			return;
		switch (id) {
		case 2461:
		case 2471:
		case 2494:
		case 2482:
			Dialogue dialogue = player.getFields().getDialogue();
			if (dialogue != null && dialogue.getType() == DialogueType.OPTIONS) {
				OptionDialogue option = (OptionDialogue) dialogue;
				option.firstOption(player);
			}
			break;
		case 2462:
		case 2472:
		case 2495:
		case 2483:
			dialogue = player.getFields().getDialogue();
			if (dialogue != null && dialogue.getType() == DialogueType.OPTIONS) {
				OptionDialogue option = (OptionDialogue) dialogue;
				option.secondOption(player);
			}
			break;
		case 2496:
		case 2473:
		case 2484:
			dialogue = player.getFields().getDialogue();
			if (dialogue != null && dialogue.getType() == DialogueType.OPTIONS) {
				OptionDialogue option = (OptionDialogue) dialogue;
				option.thirdOption(player);
			}
			break;
		case 2497:
		case 2485:
			dialogue = player.getFields().getDialogue();
			if (dialogue != null && dialogue.getType() == DialogueType.OPTIONS) {
				OptionDialogue option = (OptionDialogue) dialogue;
				option.fourthOption(player);
			}
			break;
		case 2498:
			dialogue = player.getFields().getDialogue();
			if (dialogue != null && dialogue.getType() == DialogueType.OPTIONS) {
				OptionDialogue option = (OptionDialogue) dialogue;
				option.fifthOption(player);
			}
			break;
		case 22845:
			player.getFields().getCombatAttributes().setAutoRetaliation(!player.getFields().getCombatAttributes().isAutoRetaliation());
			player.getPacketSender().sendConfig(172, player.getFields().getCombatAttributes().isAutoRetaliation() ? 1 : 0);
			break;
		case 7852:
			MessageColor next = MessageColor.forId(player.getFields().getClanChatMessageColor().ordinal() + 1);
			if (next == null)
				next = MessageColor.forId(0);
			player.getFields().setClanChatMessageColor(next);
			player.getPacketSender().sendInterfaceColor(7856, next.getRGB()[player.getFields().getClientSize()]);
			break;
		case 7851:
			MessageColor previous = MessageColor.forId(player.getFields().getClanChatMessageColor().ordinal() - 1);
			if (previous == null)
				previous = MessageColor.forId(MessageColor.values().length - 1);
			player.getFields().setClanChatMessageColor(previous);
			player.getPacketSender().sendInterfaceColor(7856, previous.getRGB()[player.getFields().getClientSize()]);
			break;
		case 7853:
		case 7854:
			player.getPacketSender().sendTabInterface(GameConstants.OPTIONS_TAB, 904);
			break;
		case 18132:
			ClanChatManager.leave(player, false);
			break;
		case 18255:
			ClanChatManager.toggleDropShare(player);
			break;
		case 1017:
			/*if (player.getFields().getPrayerBook() == PrayerBook.CURSES)
				QuickCurses.reset(player);
			else if (player.getFields().getPrayerBook() == PrayerBook.NORMAL)
				QuickPrayers.reset(player);*/
			break;
		case 1015:
			/*if (player.getFields().getPrayerBook() == PrayerBook.CURSES)
				QuickCurses.showInterface(player);
			else if (player.getFields().getPrayerBook() == PrayerBook.NORMAL)
				QuickPrayers.showInterface(player);*/
			break;
		case 1016:
			/*if (player.getFields().getPrayerBook() == PrayerBook.CURSES)
				QuickCurses.toggle(player);
			else if (player.getFields().getPrayerBook() == PrayerBook.NORMAL)
				QuickPrayers.toggle(player);*/
			break;
		case 23048:
		case 22990:
			/*if (player.getFields().getPrayerBook() == PrayerBook.CURSES)
				QuickCurses.confirm(player);
			else if (player.getFields().getPrayerBook() == PrayerBook.NORMAL)
				QuickPrayers.confirm(player);*/
			break;
		case 19158:
			player.getFields().setRunning(!player.getFields().isRunning());
			player.getPacketSender().sendRunStatus();
			break;
		case 5079:
			player.getPacketSender().sendTabInterface(GameConstants.RELATIONS_TAB, 5715);
			break;
		case 5076:
			player.getPacketSender().sendTabInterface(GameConstants.RELATIONS_TAB, 5065);
			break;
		case 5386:
			for (Item item : player.getInventory().getItems()) {
				if (item == null || item.getId() < 0 || item.getAmount() <= 0)
					continue;
				player.getInventory().switchItem(player.getBank(), item.copy(), player.getInventory().getSlot(item.getId()), false, false);
			}
			player.getInventory().refreshItems();
			player.getBank().refreshItems();
			break;
		case 5390:
			for (Item item : player.getEquipment().getItems()) {
				if (item == null || item.getId() < 0 || item.getAmount() <= 0)
					continue;
				player.getEquipment().switchItem(player.getBank(), item.copy(), player.getEquipment().getSlot(item.getId()), false, false);
			}
			BonusManager.update(player);
			player.getEquipment().refreshItems();
			player.getBank().refreshItems();
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			if (!player.getEquipment().isSlotOccupied(Equipment.WEAPON_SLOT)) {
				player.getFields().getCombatAttributes().setUsingSpecialAttack(false);
				WeaponSpecialBar.update(player);
			}
			break;
		case 5387:
			player.getBank().setNoteWithdrawal(!player.getBank().withdrawAsNote());
			break;
		case 8130:
			player.getBank().setItemSwapping(!player.getBank().swapItems());
			break;
		case 19052:
			SkillCapeEmotes.doEmote(player);
			break;
		case 2458:
			if (!player.getFields().isDead()) {
				GameServer.getWorld().unregister(player);
			}
			break;
		case 27651:
		case 21341:
			player.getPacketSender().sendInterface(21172);
			break;
		case 2799:
		case 2798:
		case 1747:
			//Cooking.isButton(player, id);
			break;
		case 1748:
			player.getPacketSender().sendEnterAmountPrompt();
			break;
		default:
			logger.info("Unhandled button id: " + id);
			break;
		}
	}

	private static boolean defaultButton(Player player, int id) {
		if (CombatPrayers.isButton(id)) {
			CombatPrayers.togglePrayerWithActionButton(player, id);
			return false;
		}
		if (WeaponLoader.getButtons().containsKey(id)) {
			player.getFields().getCombatAttributes().setAttackStyle(WeaponLoader.getButtons().get(id));
			return false;
		}
		if (WeaponLoader.getSpecialAttackButtons().contains(id)) {
			Weapon weapon = WeaponLoader.forId(player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId());
			if (weapon.getInterfaceId().length > 5 && weapon.getSpecialAttack() != null) {
				player.getFields().getCombatAttributes().setUsingSpecialAttack(!player.getFields().getCombatAttributes().isUsingSpecialAttack());
				if (weapon.getSpecialAttack().isImmediate(player)) {
					CombatManager.specialAttack(player, null);
				} else {
					int childId = weapon.getInterfaceId()[Weapon.SPECIAL_AMOUNT_INDEX];
					String color = player.getFields().getCombatAttributes().isUsingSpecialAttack() ? "@yel@" : "@bla@";
					player.getPacketSender().sendString(childId, color + " Special Attack (" + (int) (player.getFields().getCombatAttributes().getSpecialAttackAmount()) + "%)");
				}
			}
			return false;
		}
		if (EmotesTab.isButton(player, id))
			return false;
		if (CombatCurses.isButton(player, id))
			return false;
		/*if (QuickCurses.isButton(id) && player.getFields().getPrayerBook() == PrayerBook.CURSES) {
			QuickCurses.selectForButton(player, id);
			return false;
		}
		if (QuickPrayers.isButton(id) && player.getFields().getPrayerBook() == PrayerBook.NORMAL) {
			QuickPrayers.selectForButton(player, id);
			return false;
		}*/
		return true;
	}
}
