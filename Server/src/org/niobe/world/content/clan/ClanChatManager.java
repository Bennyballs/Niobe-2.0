package org.niobe.world.content.clan;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.niobe.GameServer;
import org.niobe.model.Item;
import org.niobe.model.PlayerRights;
import org.niobe.model.Position;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.util.MathUtil;
import org.niobe.util.NameUtil;
import org.niobe.world.GroundItem;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.content.clan.ClanChat.MessageColor;
import org.niobe.world.content.clan.ClanChat.DropShare;
import org.niobe.world.content.clan.ClanChat.Rank;

/**
 * Loads, saves and manages all the {@link ClanChat}s
 * in the world.
 *
 * @author relex lawl
 */
public final class ClanChatManager {
	
	/**
	 * The directory in which the clan chat files are located.
	 */
	private static final String FILE_DIRECTORY = "./data/clans/";

	/**
	 * The array containing all the {@link ClanChat}s
	 * in the world.
	 */
	private static final ClanChat[] clans = new ClanChat[5000];
	
	/**
	 * Loads all the files located in the directory {@link #FILE_DIRECTORY}
	 * and populates the {@link #clans} array.
	 */
	public static void init() {
		try {
			int amount = 0;
			long startup = System.currentTimeMillis();
			System.out.println("Loading clan chat channels...");
			for (File file : (new File(FILE_DIRECTORY)).listFiles()) {
				DataInputStream input = new DataInputStream(new FileInputStream(file));
				String name = input.readUTF();
				String owner = input.readUTF();
				int index = input.readShort();
				ClanChat clan = new ClanChat(name, owner, index);
				clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_ENTER, Rank.forId(input.read()));
				clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_KICK, Rank.forId(input.read()));
				int totalRanks = input.readShort();
				for (int i = 0; i < totalRanks; i++) {
					clan.getRankedNames().put(input.readUTF(), Rank.forId(input.read()));
				}
				clans[index] = clan;
				amount++;
				input.close();
			}
			System.out.println("Loaded " + amount + " clan chat channel" + (amount > 1 ? "s" : "") + " in " + (System.currentTimeMillis() - startup) + "ms");
			
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Writes a file with information from said
	 * {@link ClanChat}.
	 * @param clan	The clan chat to get information from.
	 */
	public static void writeFile(ClanChat clan) {
		try {
			File file = new File(FILE_DIRECTORY + clan.getOwner().getCredentials().getUsername());
			if (file.exists())
				file.createNewFile();
			DataOutputStream output = new DataOutputStream(new FileOutputStream(file));
			output.writeUTF(clan.getName());
			output.writeUTF(clan.getOwner().getCredentials().getUsername());
			output.writeShort(clan.getIndex());
			output.write(clan.getRankRequirement()[0] != null ? clan.getRankRequirement()[0].ordinal() : -1);
			output.write(clan.getRankRequirement()[1] != null ? clan.getRankRequirement()[0].ordinal() : -1);
			output.writeShort(clan.getRankedNames().size());
			for (Entry<String, Rank> iterator : clan.getRankedNames().entrySet()) {
				String name = iterator.getKey();
				int rank = iterator.getValue().ordinal();
				output.writeUTF(name);
				output.write(rank);
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves all {@link ClanChat}s that 
	 * populate the {@link #clans} array.
	 */
	public static void save() {
		for (ClanChat clan : clans) {
			if (clan != null) {
				writeFile(clan);
			}
		}
	}
	
	/**
	 * Handles the creation of a new {@link ClanChat}
	 * instance to add to {@link #clans}.
	 * @param player	The {@link org.niobe.world.Player} creating the clan.
	 */
	public static void create(Player player) {
		File file = new File(FILE_DIRECTORY + player.getCredentials().getUsername());
		if (file.exists()) {
			player.getPacketSender().sendMessage("Your clan channel is already public!");
			return;
		}
		int index = getFreeIndex();
		if (index == -1) {
			player.getPacketSender().sendMessage("Too many clan chats! Please contact an administrator and report this error.");
			return;
		}
		clans[index] = new ClanChat(player, player.getCredentials().getUsername(), index);
		clans[index].getRankedNames().put(player.getCredentials().getUsername(), Rank.OWNER);
		writeFile(clans[index]);
	}
	
	/**
	 * Handles the joining of a member to the {@link ClanChat} with said name.
	 * @param player	The {@link org.niobe.world.Player} joining channel.
	 * @param channel	The name of the {@link ClanChat}.
	 */
	public static void join(Player player, String channel) {
		if (player.getFields().getClanChat() != null) {
			player.getPacketSender().sendMessage("You are already in a clan channel!");
			return;
		}
		File file = new File(FILE_DIRECTORY + channel);
		if (!file.exists()) {
			player.getPacketSender().sendMessage("The channel you tried to join does not exist.");
			return;
		}
		channel = channel.toLowerCase();
		for (ClanChat clan : clans) {
			if (clan != null && clan.getName().toLowerCase().equals(channel)) {
				join(player, clan);
				break;
			}
		}
	}
	
	/**
	 * Updates the clan chat game frame tab
	 * interface with all {@link ClanChatMember#getPlayer()}
	 * user names and {@link Clan.Rank}s.
	 * @param clan	The {@link ClanChat} to update list for.
	 */
	public static void updateList(ClanChat clan) {
		for (ClanChatMember members : clan.getMembers()) {
			if (members != null && members.getPlayer() != null) {
				int childId = 18144;
				Player member = members.getPlayer();
				for (ClanChatMember others : clan.getMembers()) {
					if (others != null && others.getPlayer() != null) {
						Rank rank = clan.getRank(others.getPlayer());
						if (others.getPlayer().getRights() == PlayerRights.ADMINISTRATOR && rank != Rank.OWNER)
							rank = Rank.SERVER_ADMINISTRATOR;
						String prefix = rank != null ? ("<img=" + (rank.ordinal() + 3) +  "> ") : "    ";
						member.getPacketSender().sendString(childId, prefix + others.getPlayer().getCredentials().getUsername());
						childId++;
					}
				}
				for (int i = childId; i < 18244; i++) {
					member.getPacketSender().sendString(i, "");
				}
			}
		}
	}
	
	/**
	 * Handles the chat of a {@link ClanChatMember} in the
	 * {@link ClanChat}.
	 * @param player	The {@link org.niobe.world.Player} instance of the {@link ClanChatMember}.
	 * @param message	The chat message being sent by the {@link ClanChatMember}.
	 */
	public static void sendMessage(Player player, String message) {
		ClanChat clan = player.getFields().getClanChat();
		if (clan == null) {
			player.getPacketSender().sendMessage("You're not in a clan channel.");
			return;
		}
		Rank rank = clan.getRank(player);
		if (clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_TALK] != null && 
				rank.ordinal() < clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_TALK].ordinal()) {
			player.getPacketSender().sendMessage("You do not have the required rank to speak.");
			return;
		}
		for (ClanChatMember member : clan.getMembers()) {
			if (member != null && member.getPlayer() != null) {
				Player memberPlayer = member.getPlayer();
				String bracketColor = "<col=16777215>";
				String clanNameColor = "<col=255>";
				String nameColor = "<col=000000>";
				if (memberPlayer.getFields().getClientSize() != 0) {
					bracketColor = "<col=FFFFFF>";
					clanNameColor = "<col=11263>";
					nameColor = "<col=FFFFFF>";
				}
				memberPlayer.getPacketSender().sendMessage(bracketColor + "[" + clanNameColor + clan.getName() + bracketColor + "]" + nameColor + " " +
							NameUtil.capitalizeWords(player.getCredentials().getUsername()) +
							": <col=" + memberPlayer.getFields().getClanChatMessageColor().getRGB()[player.getFields().getClientSize()] + ">" +
							NameUtil.capitalize(message));
			}
		}
	}
	
	/**
	 * Sends a message to the {@link ClanChat#getMembers()}.
	 * @param clan		The {@link ClanChat} to send message to.
	 * @param message	The message to send to the {@link ClanChat#getMembers()}.
	 */
	public static void sendMessage(ClanChat clan, String message) {
		for (ClanChatMember member : clan.getMembers()) {
			if (member != null && member.getPlayer() != null) {
				member.getPlayer().getPacketSender().sendMessage(message);
			}
		}
	}
	
	/**
	 * Handles a player leaving their corresponding {@link ClanChat}.
	 * @param player	The {@link org.niobe.world.Player} leaving the {@link ClanChat}.
	 * @param kicked	If {@code true} the {@link ClanChatMember} was kicked from the {@link ClanChat}.
	 */
	public static void leave(Player player, boolean kicked) {
		ClanChat clan = player.getFields().getClanChat();
		if (clan == null) {
			player.getPacketSender().sendMessage("You are not in a clan channel.");
			return;
		}
		player.getPacketSender().sendInterfaceSpriteChange(18255, 239, -1);
		player.getPacketSender().sendString(18140, "Talking in: @whi@Not in chat");
		player.getPacketSender().sendString(18250, "Owner: None");
		player.getFields().setClanChat(null);
		clan.removeMember(player.getCredentials().getUsername());
		for (int i = 18144; i < 18244; i++) {
			player.getPacketSender().sendString(i, "");
		}
		updateList(clan);
		player.getPacketSender().sendMessage(kicked ? "You have been kicked from the channel." : "You have left the channel.");
	}
	
	/**
	 * Handles a {@link org.niobe.world.Player} joining the
	 * {@link ClanChat}.
	 * @param player	The {@link org.niobe.world.Player} attempting to join {@link ClanChat}.
	 * @param clan		The {@link ClanChat} being entered.
	 */
	private static void join(Player player, ClanChat clan) {
		if (clan.getOwnerName().equals(player.getCredentials().getUsername())) {
			if (clan.getOwner() == null) {
				clan.setOwner(player);
			}
			clan.getRankedNames().put(player.getCredentials().getUsername(), Rank.OWNER);
		}
		player.getPacketSender().sendMessage("Attempting to join channel...");
		if (clan.getTotalMembers() >= 100) {
			player.getPacketSender().sendMessage("This clan channel is currently full.");
			return;
		}
		Rank rank = clan.getRank(player);
		if (clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER] != null) {
			if (rank == null || clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER].ordinal() > rank.ordinal()) {
				player.getPacketSender().sendMessage("Your rank is not high enough to enter this channel.");
				return;
			}
		}
		player.getFields().setClanChat(clan);
		String clanName = NameUtil.capitalizeWords(clan.getName());
		ClanChatMember member = new ClanChatMember(player, clan, clan.getRank(player));
		clan.addMember(member);
		player.getPacketSender().sendInterfaceSpriteChange(18255, clan.getDropShareId(), -1);
		player.getPacketSender().sendString(18140, "Talking in: @whi@" + clanName);
		player.getPacketSender().sendString(18250, "Owner: " + NameUtil.capitalizeWords(clan.getOwnerName()));
		player.getPacketSender().sendMessage("Now talking in friends chat channel " + clanName);
		player.getPacketSender().sendMessage("To talk start each line of chat with the / symbol.");
		updateList(clan);
	}
	
	/**
	 * Handles the termination of a {@link ClanChat}
	 * owned by the {@link org.niobe.world.Player}.
	 * @param player	The {@link org.niobe.world.Player} shutting down their {@link ClanChat}.
	 */
	public static void shutdown(Player player) {
		ClanChat clan = getClanChat(player);
		File file = new File(FILE_DIRECTORY + player.getCredentials().getUsername());
		if (clan == null || !file.exists()) {
			player.getPacketSender().sendMessage("You need to have a public clan channel to do this.");
			return;
		}
		for (ClanChatMember member : clan.getMembers()) {
			if (member != null) {
				leave(member.getPlayer(), true);
			}
		}
		clans[clan.getIndex()] = null;
		file.delete();
	}
	
	/**
	 * Sets the {@link ClanChat#getName()} to a new name.
	 * @param player	The {@link org.niobe.world.Player} to get {@link ClanChat} instance from.
	 * @param newName	The new name for the {@link ClanChat}.
	 */
	public static void setName(Player player, String newName) {
		final ClanChat clan = getClanChat(player);
		if (clan == null) {
			player.getPacketSender().sendMessage("You need to have a public clan channel to do this.");
			return;
		}
		if (newName.length() > 12)
			newName = newName.substring(0, 11);
		final String name = NameUtil.capitalizeWords(newName);
		String green = MessageColor.GREEN.getRGB()[player.getFields().getClientSize()];
		player.getPacketSender().sendMessage("<col=" + green + ">Changes will take effect in 60 seconds.");
		clan.addAction(new ClanChatAction() {
			@Override
			public void execute() {
				clan.setName(name);
			}
		});
	}
	
	/**
	 * Handles the kick option in a clan chat
	 * game frame tab.
	 * @param player	The {@link org.niobe.world.Player} kicking another {@link ClanChatMember}.
	 * @param target	The {@link org.niobe.world.Player} being kicked.
	 */
	public static void kick(Player player, Player target) {
		ClanChat clan = player.getFields().getClanChat();
		if (target.getFields().getClanChat() != clan)
			return;
		if (clan == null) {
			player.getPacketSender().sendMessage("You're not in a clan channel.");
			return;
		}
		final Rank rank = clan.getRank(player);
		if (rank == null) {
			player.getPacketSender().sendMessage("You do not have the required rank to kick this player.");
			return;
		}
		if (clan.getRank(player).ordinal() < clan.getRank(target).ordinal()) {
			player.getPacketSender().sendMessage("You cannot kick this player!");
		} else {
			leave(target, true);
		}
	}
	
	/**
	 * Handles the {@link ClanChat#getOwner()} promoting a
	 * {@link ClanChatMember}.
	 * @param owner		The {@link org.niobe.world.Player} to get {@link ClanChat} instance from.
	 * @param target	The {@link org.niobe.world.Player} being promoted.
	 * @param rank		The {@link Clan.Rank} to promote or demote {@value target} to.
	 */
	public static void setRank(final Player owner, final Player target, final Rank rank) {
		final ClanChat clan = getClanChat(owner);
		if (clan != null) {
			String green = MessageColor.GREEN.getRGB()[owner.getFields().getClientSize()];
			owner.getPacketSender().sendMessage("<col=" + green + ">Changes will take effect in 60 seconds.");
			clan.addAction(new ClanChatAction() {
				@Override
				public void execute() {
					clan.giveRank(target, rank);
				}
			});
		} else {
			owner.getPacketSender().sendMessage("You need to have a public clan channel to do this.");
		}
	}
	
	/**
	 * Sets the {@link ClanChat.Rank} needed in order to enter
	 * the {@link org.niobe.world.Player}'s {@link ClanChat}.
	 * @param player	The {@link org.niobe.world.Player} to get {@link ClanChat} from.
	 * @param rank		The {@link ClanChat.Rank} required to enter.
	 */
	public static void setRankToEnter(final Player player, final Rank rank) {
		final ClanChat clan = getClanChat(player);
		if (clan != null) {
			String green = MessageColor.GREEN.getRGB()[player.getFields().getClientSize()];
			player.getPacketSender().sendMessage("<col=" + green + ">Changes will take effect in 60 seconds.");
			clan.addAction(new ClanChatAction() {
				@Override
				public void execute() {
					clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_ENTER, rank);
				}
			});
		} else {
			player.getPacketSender().sendMessage("You need to have a public clan channel to do this.");
		}
	}
	
	/**
	 * Sets the {@link ClanChat.Rank} needed in order to kick other
	 * {@link ClanChatMember}s in the {@link org.niobe.world.Player}'s {@link ClanChat}.
	 * @param player	The {@link org.niobe.world.Player} to get {@link ClanChat} from.
	 * @param rank		The {@link ClanChat.Rank} required to kick other {@link ClanChatMember}s.
	 */
	public static void setRankToKick(final Player player, final Rank rank) {
		final ClanChat clan = getClanChat(player);
		if (clan != null) {
			String green = MessageColor.GREEN.getRGB()[player.getFields().getClientSize()];
			player.getPacketSender().sendMessage("<col=" + green + ">Changes will take effect in 60 seconds.");
			clan.addAction(new ClanChatAction() {
				@Override
				public void execute() {
					clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_KICK, rank);
				}
			});
		}
	}
	
	/**
	 * Handles the {@link org.niobe.world.Mob} drops in
	 * the {@link ClanChat} if {@link !ClanChat#getDropShare()#equals(ClanChat.DropShare.NONE)}.
	 * @param player	The {@link org.niobe.world.Player} killing said {@link org.niobe.world.Mob}.
	 * @param mob		The {@link org.niobe.world.Mob} that was killed.
	 */
	public static void dropShareLoot(Player player, Mob mob) {
		ClanChat clan = player.getFields().getClanChat();
		if (clan != null) {
			List<Player> players = getPlayersWithinPosition(clan, mob.getPosition());
			String green = "<col=" + MessageColor.GREEN.getRGB()[player.getFields().getClientSize()] + ">";
			if (clan.getDropShare() == DropShare.LOOT_SHARE) {
				for (Item drop : mob.getDrops()) {
					Player rewarded = players.size() > 0 ? players.get(MathUtil.random(players.size() - 1)) : null;
					if (rewarded != null) {
						GroundItem groundItem = new GroundItem(drop, mob.getPosition(), rewarded);
						GameServer.getWorld().register(groundItem);
						rewarded.getPacketSender().sendMessage(green + "You have received " + drop.getAmount() + "x " + drop.getDefinition().getName() + ".");
					}
					//TODO loot share points?
				}
			} else if (clan.getDropShare() == DropShare.COIN_SHARE) {
				for (Item drop : mob.getDrops()) {
					if ((drop.getDefinition().getValue() * drop.getAmount()) < 50000) {
						GroundItem groundItem = new GroundItem(drop, mob.getPosition(), player);
						GameServer.getWorld().register(groundItem);
						continue;
					}
					int amount = (ItemDefinition.forId(drop.getId()).getValue() / players.size());
					Item split = new Item(995, amount);
					for (Player member : players) {
						GroundItem groundItem = new GroundItem(split.copy(), mob.getPosition(), member);
						GameServer.getWorld().register(groundItem);
						member.getPacketSender().sendMessage(green + "You have received " + amount + "x " + split.getDefinition().getName().toLowerCase() + " as part of a split drop.");
					}
				}
			}
		}
	}
	
	/**
	 * Handles the clicking of the drop share button
	 * located in the clan chat game frame tab interface.
	 * @param player	The {@link org.niobe.world.Player} clicking on the sprite.
	 */
	public static void toggleDropShare(Player player) {
		final ClanChat clan = player.getFields().getClanChat();
		if (clan == null) {
			player.getPacketSender().sendMessage("You're not in a clan channel.");
			return;
		}
		if (!clan.getOwner().equals(player)) {
			player.getPacketSender().sendMessage("Only the owner of the channel has the power to do this.");
			return;
		}
		String green = MessageColor.GREEN.getRGB()[player.getFields().getClientSize()];
		player.getPacketSender().sendMessage("<col=" + green + ">Changes will take effect in 60 seconds.");
		clan.setDropShareId(240);
		for (ClanChatMember member : clan.getMembers()) {
			if (member != null && member.getPlayer() != null) {
				member.getPlayer().getPacketSender().sendInterfaceSpriteChange(18255, 240, -1);
			}
		}
		clan.addAction(new ClanChatAction() {
			@Override
			public void execute() {
				DropShare dropShare = clan.isLootsharing() && clan.getDropShare() != DropShare.LOOT_SHARE ? DropShare.LOOT_SHARE :
					clan.isCoinsharing() && clan.getDropShare() != DropShare.COIN_SHARE? DropShare.COIN_SHARE : DropShare.NONE;
				int childConfig = dropShare == DropShare.LOOT_SHARE ? 2 : dropShare == DropShare.COIN_SHARE ? 3 : 0;
				String message = dropShare != DropShare.NONE ? NameUtil.capitalize(dropShare.toString().toLowerCase().replaceAll("_", "")) + " has been enabled by the clan leader." : "Lootshare has been disabled by the clan leader.";
				clan.setDropShare(dropShare);
				clan.setDropShareId(239 + childConfig);
				for (ClanChatMember member : clan.getMembers()) {
					if (member != null && member.getPlayer() != null) {
						member.getPlayer().getPacketSender().sendInterfaceSpriteChange(18255, 239 + childConfig, -1);
					}
				}
				sendMessage(clan, message);
			}
		});
	}
	
	/**
	 * Setting player's clan chat message color
	 * @param player	player setting clan chat message color
	 * @param color		color to set clan chat message color to
	 */
	public static void setMessageColor(Player player, MessageColor color) {
		player.getFields().setClanChatMessageColor(color);
	}
	
	/**
	 * Gets the {@link ClanChat} who's {@link ClanChat#getOwner()} is
	 * {@link org.niobe.world.Player#equals(Object)} to {@param player}.
	 * @param player	The {@link org.niobe.world.Player} to get {@link ClanChat} for.
	 * @return			The retrieved {@link ClanChat}.
	 */
	public static ClanChat getClanChat(Player player) {
		for (ClanChat clan : clans) {
			if (clan.getOwner().equals(player)) {
				return clan;
			}
		}
		return null;
	}
	
	/**
	 * Gets a {@link java.util.List} of all {@link org.niobe.world.Player}s
	 * within distance of said {@link org.niobe.model.Position}.
	 * @param clan		The {@link ClanChat} from where to get {@link ClanChatMember}s from.
	 * @param position	The {@link org.niobe.model.Position} to compare to members' position.
	 * @return			The list of all {@link ClanChatMember}s within distance of {@param position}.
	 */
	private static List<Player> getPlayersWithinPosition(ClanChat clan, Position position) {
		List<Player> players = new LinkedList<Player>();
		for (ClanChatMember member : clan.getMembers()) {
			if (member != null && member.getPlayer() != null && member.getPlayer().getPosition().isWithinDistance(position)) {
				players.add(member.getPlayer());
			}
		}
		return players;
	}
	
	/**
	 * Gets an empty index within the {@link #clans}
	 * array.
	 * @return	An empty index to occupy in the {@link #clans} array.
	 */
	private static int getFreeIndex() {
		for (int i = 0; i < clans.length; i++) {
			if (clans[i] == null) {
				return i;
			}
		}
		return -1;
	}
}
