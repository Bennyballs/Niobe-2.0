package org.niobe.world.content;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.niobe.world.Player;

/**
 * This file manages player punishments, such as mutes, bans, etc.
 * 
 * @author relex lawl
 */
public final class PlayerPunishment {
	
	/**
	 * Logger instance for debug information.
	 */
	private static final Logger logger = Logger.getLogger(PlayerPunishment.class.getName());
	
	/**
	 * Leads to directory where banned account files are stored.
	 */
	private static final String BAN_DIRECTORY = "./data/bans/";
	
	/**
	 * Leads to directory where muted account files are stored.
	 */
	private static final String MUTE_DIRECTORY = "./data/mutes/";
	
	/**
	 * The map containing all players' names currently muted in the world.
	 */
	private static final Map<String, Calendar> mutes = new HashMap<String, Calendar>();
	
	/**
	 * Loads up all the player mutes located in {@code MUTE_DIRECTORY}.
	 * Does not load bans as it's not needed, they will be logged out and will not be able
	 * to log in.
	 */
	public static void init() {
		long startup = System.currentTimeMillis();
		try {
			System.out.println("Loading player punishments...");
			for (File file : new File(MUTE_DIRECTORY).listFiles()) {
				FileInputStream inputStream = new FileInputStream(file);
				DataInputStream input = new DataInputStream(inputStream);
				String name = input.readUTF();
				@SuppressWarnings("unused")
				String reason = input.readUTF();
				String[] expires = {input.readUTF(), input.readUTF(), input.readUTF()};
				Calendar calendar = Calendar.getInstance();
				if (calendar.get(Calendar.MONTH) == Integer.parseInt(expires[0]) && calendar.get(Calendar.DATE) == Integer.parseInt(expires[1]) && calendar.get(Calendar.YEAR) == Integer.parseInt(expires[2])) {
					file.delete();
					input.close();
					return;
				}
				mutes.put(name, calendar);
				input.close();
			}
			System.out.println("Loaded " + mutes.size() + " mutes in " + (System.currentTimeMillis() - startup) + "ms");
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Mutes {@param target} if {@param player}'s rank has a higher privilege-level.
	 * @param player	The player muting the target.
	 * @param target	The player being muted.
	 * @param length	The length the mute will last.
	 * @param value		The amount of said length. (if length = Length.DAY and value = 1, they will be muted for 1 day).
	 */
	public static void mute(Player player, Player target, Length length, int value) {
		if (player.getRights().ordinal() > target.getRights().ordinal()) {
			mute(target, getCalendar(length, value), "");
		} else {
			player.getPacketSender().sendMessage("You cannot mute this player!");
		}
	}
	
	/**
	 * Bans {@param target} if {@param player}'s rank has a higher privilege-level.
	 * @param player	The player banning the target.
	 * @param target	The player being muted.
	 * @param length	The length the ban will last.
	 * @param value		The amount of said length. (if length = Length.DAY and value = 1, they will be banned for 1 day).
	 */
	public static void ban(Player player, Player target, Length length, int value) {
		if (player.getRights().ordinal() > target.getRights().ordinal()) {
			ban(target, getCalendar(length, value) , "");
		} else {
			player.getPacketSender().sendMessage("You cannot ban this player!");
		}
	}
	
	/**
	 * Checks if {@param player} is contained within the mutes map.
	 * @param player	The player to check.
	 * @return			If mutes map contains the player's user name return <code>true</code>.
	 */
	public static boolean isMuted(Player player) {
		return mutes.containsKey(player.getCredentials().getUsername());
	}
	
	/**
	 * Checks if a player is banned.
	 * @param directory	The player's name.
	 * @return			If <code>true</code> that means player is banned.
	 */
	public static boolean banned(String directory) {
		try {
			File file = new File(BAN_DIRECTORY + directory + ".txt");
			if (!file.exists()) {
				return false;
			}
			FileInputStream inputStream = new FileInputStream(file);
			DataInputStream input = new DataInputStream(inputStream);
			String name = input.readUTF();
			String reason = input.readUTF();
			String[] expires = {input.readUTF(), input.readUTF(), input.readUTF()};
			Calendar calendar = Calendar.getInstance();
			if (calendar.get(Calendar.MONTH) == Integer.parseInt(expires[0]) && calendar.get(Calendar.DATE) == Integer.parseInt(expires[1]) && calendar.get(Calendar.YEAR) == Integer.parseInt(expires[2])) {
				file.delete();
				input.close();
				return false;
			}
			logger.info("Name: " + name + ", reason: " + reason);
			logger.info("Expires: [month, day, year] [" + expires[0] + ", " + expires[1] + ", " + expires[2] + "]");
			input.close();
			return true;
			
		} catch (IOException exception) {
			exception.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Bans <code>Player</code> and adds a file to directory.
	 * @param player	Player being banned.
	 * @param expires	Date which ban will expire.
	 * @param reason	Reason for being banned.
	 */
	private static void ban(Player player, Calendar expires, String reason) {
		try {
			FileOutputStream outputStream = new FileOutputStream(new File(BAN_DIRECTORY + player.getCredentials().getUsername().toLowerCase() + ".txt"), true);
			DataOutputStream output = new DataOutputStream(outputStream);
			String[] date = new String[] {Integer.toString(expires.get(Calendar.MONTH)), Integer.toString(expires.get(Calendar.DATE)), Integer.toString(expires.get(Calendar.YEAR))};
			output.writeUTF(player.getCredentials().getUsername());
			output.writeUTF(reason);
			for (String expiration : date) {
				output.writeUTF(expiration);
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Mutes <code>Player</code> and adds a file to directory.
	 * @param player	Player being muted.
	 * @param expires	Date which ban will expire.
	 * @param reason	Reason for being muted.
	 */
	private static void mute(Player player, Calendar expires, String reason) {
		try {
			FileOutputStream outputStream = new FileOutputStream(new File(MUTE_DIRECTORY + player.getCredentials().getUsername().toLowerCase() + ".txt"), true);
			DataOutputStream output = new DataOutputStream(outputStream);
			String[] date = new String[] {Integer.toString(expires.get(Calendar.MONTH)), Integer.toString(expires.get(Calendar.DATE)), Integer.toString(expires.get(Calendar.YEAR))};
			output.writeUTF(player.getCredentials().getUsername());
			output.writeUTF(reason);
			for (String expiration : date) {
				output.writeUTF(expiration);
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the Calendar instance for said {@code length} and their value.
	 * @param length	The Length to get the calendar for.
	 * @param value		The value of the length.
	 * @return
	 */
	private static Calendar getCalendar(Length length, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(length.value, value);
		return calendar;
	}

	/**
	 * Represents a length in which a player can be punished.
	 * 
	 * @author relex lawl
	 */
	private enum Length {
		SECONDS(Calendar.SECOND),
		MINUTES(Calendar.MINUTE),
		HOURS(Calendar.HOUR_OF_DAY),
		DAYS(Calendar.DATE),
		MONTHS(Calendar.MONTH),
		YEARS(Calendar.YEAR);
		
		/**
		 * The Length constructor.
		 * @param value		The value the length holds.
		 */
		private Length(int value) {
			this.value = value;
		}
		
		/**
		 * The value the length holds as declared in
		 * {@link java.util.Calendar}.
		 */
		private int value;
	}
}
