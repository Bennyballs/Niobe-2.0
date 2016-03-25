package org.niobe.world.content;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.niobe.model.Item;
import org.niobe.model.MagicSpellBook;
import org.niobe.model.PlayerRights;
import org.niobe.model.PrayerBook;
import org.niobe.model.SkillManager.Skill;
import org.niobe.world.Player;
import org.niobe.world.content.clan.ClanChat.MessageColor;;

/**
 * Manages the player saving and loading process.
 * 
 * @author relex lawl
 */
public final class PlayerSaving {
	
	/**
	 * The directory in which saved player files are stored.
	 */
	private static final String DIRECTORY = "./data/saves/";
	
	/**
	 * Saves {@code player}'s attributes to a file located in {@code DIRECTORY}.
	 * @param player	The player to which save the game for.
	 */
	public static void save(Player player) {
		File file = new File(DIRECTORY + player.getCredentials().getUsername().toLowerCase());
		DataOutputStream output = null;
		try {
			file.createNewFile();
			output = new DataOutputStream(new FileOutputStream(file));
			/*
			 * Writes player's details: username and password
			 */
			output.writeUTF(player.getCredentials().getUsername());
			output.writeUTF(player.getCredentials().getPassword());
			/*
			 * Writes the player's position on globe.
			 */
			output.writeShort(player.getPosition().getX());
			output.writeShort(player.getPosition().getY());
			output.writeShort(player.getPosition().getZ());
			/*
			 * Writes the player's attributes, such as, rights, total experience on experience counter, run energy, etc.
			 */
			output.write(player.getRights().ordinal());
			output.writeByte(player.getFields().getCombatAttributes().getSpecialAttackAmount());
			output.write(player.getFields().getPoisonDamage());
			output.writeBoolean(player.getFields().isRunning());
			output.write(player.getFields().getSpellbook().ordinal());
			output.write(player.getFields().getPrayerBook().ordinal());
			output.writeInt(player.getFields().getTotalExperienceOnCounter());
			output.write(player.getFields().getRunEnergy());
			output.write(player.getFields().getClanChatMessageColor().ordinal());
			output.write(player.getFields().getCombatAttributes().getAttackStyle());
			output.writeBoolean(player.getFields().getCombatAttributes().isAutoRetaliation());
			/*
			 * Writes the player's appearance look.
			 */
			for (int i = 0; i < player.getAppearance().getLook().length; i++) {
				output.writeShort(player.getAppearance().getLook()[i]);
			}
			/*
			 * Writes player's inventory data.
			 */
			for (int i = 0; i < player.getInventory().capacity(); i++) {
				Item item = player.getInventory().getItems()[i];
				if (item == null) {
					/*
					 * Writes the default value for items if the item is non-existent (slot is empty).
					 */
					output.writeInt(65535);
				} else {
					output.writeInt(item.getId());
					output.writeInt(item.getAmount());
				}
			}
			/*
			 * Writes player's equipment data.
			 */
			for (int i = 0; i < player.getEquipment().capacity(); i++) {
				Item item = player.getEquipment().getItems()[i];
				if (item == null) {
					/*
					 * Writes the default value for items if the item is non-existent (player isn't wielding anything in the slot)
					 */
					output.writeInt(65535);
				} else {
					output.writeInt(item.getId());
					output.writeInt(item.getAmount());
				}
			}
			/*
			 * Writes player's bank data.
			 */
			for (int i = 0; i < player.getBank().capacity(); i++) {
				Item item = player.getBank().getItems()[i];
				if (item == null) {
					/*
					 * Writes the default value for items if the item is non-existent (player doesn't have item in bank)
					 */
					output.writeInt(65535);
				} else {
					output.writeInt(item.getId());
					output.writeInt(item.getAmount());
				}
			}
			for (Skill skill : Skill.values()) {
				output.writeInt(player.getSkillManager().getCurrentLevel(skill));
				output.writeInt(player.getSkillManager().getMaxLevel(skill));
				output.writeInt(player.getSkillManager().getExperience(skill));
			}
			for (int i = 0; i < player.getFields().getNotes().length; i++) {
				if (player.getFields().getNotes()[i] != null) {
					output.writeByte(i);
					output.writeUTF(player.getFields().getNotes()[i]);
				} else {
					output.writeByte(-1);
				}
			}
			output.close();
		} catch (IOException exception) {
			exception.printStackTrace();
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Loads and checks if a player has a saved file located in {@code DIRECTORY}.
	 * @param player	The player to load file for.
	 * @return			File successfully loaded.
	 */
	public static LoginType load(Player player) {
		File file = new File(DIRECTORY + player.getCredentials().getUsername().toLowerCase());
		DataInputStream input = null;
		if (!file.exists()) {
			save(player);
			return LoginType.NEW_ACCOUNT;
		}
		try {
			input = new DataInputStream(new FileInputStream(file));
			String username = input.readUTF(), password = input.readUTF();
			if (!username.equalsIgnoreCase(player.getCredentials().getUsername())) {
				input.close();
				return LoginType.WRONG_CREDENTIALS;
			}
			player.getCredentials().setUsername(username);
			if (!password.equals(player.getCredentials().getPassword())) {
				input.close();
				return LoginType.WRONG_CREDENTIALS;
			}
			player.getCredentials().setPassword(password);
			int x = input.readShort(), y = input.readShort(), z = input.readShort();
			player.getPosition().set(x, y, z);
			player.setRights(PlayerRights.forId(input.read()));
			player.getFields().getCombatAttributes().setSpecialAttackAmount(input.readByte());
			player.getFields().setPoisonDamage(input.read());
			player.getFields().setRunning(input.readBoolean());
			player.getFields().setSpellbook(MagicSpellBook.forId(input.read()));
			player.getFields().setPrayerBook(PrayerBook.forId(input.read()));
			player.getFields().setTotalExperienceOnCounter(input.readInt());
			player.getFields().setRunEnergy(input.read());
			player.getFields().setClanChatMessageColor(MessageColor.forId(input.read()));
			player.getFields().getCombatAttributes().setAttackStyle(input.read());
			player.getFields().getCombatAttributes().setAutoRetaliation(input.readBoolean());
			for (int i = 0; i < player.getAppearance().getLook().length; i++) {
				int id = input.readShort();
				player.getAppearance().set(i, id);
			}
			for (int i = 0; i < player.getInventory().capacity(); i++) {
				int id = input.readInt();
				if (id != 65535) {
					/*
					 * If the id saved is not equal to the default empty value, then add the item to player's inventory container.
					 */
					Item item = new Item(id, input.readInt());
					player.getInventory().setItem(i, item);
				}
			}
			for (int i = 0; i < player.getEquipment().capacity(); i++) {
				int id = input.readInt();
				if (id != 65535) {
					/*
					 * If the id saved is not equal to the default empty value, then add the item to player's equipment container.
					 */
					Item item = new Item(id, input.readInt());
					player.getEquipment().setItem(i, item);
				}
			}
			for (int i = 0; i < player.getBank().capacity(); i++) {
				int id = input.readInt();
				if (id != 65535) {
					/*
					 * If the id saved is not equal to the default empty value, then add the item to player's bank container.
					 */
					Item item = new Item(id, input.readInt());
					player.getBank().setItem(i, item);
				}
			}
			for (Skill skill : Skill.values()) {
				player.getSkillManager().setCurrentLevel(skill, input.readInt(), false);
				player.getSkillManager().setMaxLevel(skill, input.readInt(), false);
				player.getSkillManager().setExperience(skill, input.readInt(), false);
			}
			for (int i = 0; i < player.getFields().getNotes().length; i++) {
				int index = input.readByte();
				if (index != -1) {
					String note = input.readUTF();
					if (note.length() > 0) {
						player.getFields().setNote(index, note);
					}
				}
			}
			input.close();
			return LoginType.SUCCESSFUL;
		} catch (IOException exception) {
			exception.printStackTrace();
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return LoginType.INPUT_FAILURE;
		}
	}
	
	/**
	 * Represents a login type when loading a player file.
	 * 
	 * @author relex lawl
	 */
	public enum LoginType {
		INPUT_FAILURE,
		NEW_ACCOUNT,
		SUCCESSFUL,
		WRONG_CREDENTIALS,;
	}

}
