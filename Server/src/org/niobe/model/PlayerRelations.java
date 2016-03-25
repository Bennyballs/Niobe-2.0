package org.niobe.model;

import java.util.ArrayList;
import java.util.List;

import org.niobe.GameServer;
import org.niobe.util.NameUtil;
import org.niobe.world.Player;

/**
 * This file represents a player's relation with other world entities,
 * this manages adding and removing friends who we can chat with and also
 * adding and removing ignored players who will not be able to message us or see us online.
 *
 * @author relex lawl
 */
public final class PlayerRelations {
	
	/**
	 * The player's current friend status, checks if others will be able to see them online.
	 */
	private Status status = Status.PUBLIC;
	
	/**
	 * The player's current private message index.
	 */
	private int privateMessageId = 1;
	
	/**
	 * This map contains the player's friends list.
	 */
	private final List<Long> friendList = new ArrayList<Long>(200);
	
	/**
	 * This map contains the player's ignore list.
	 */
	private final List<Long> ignoreList = new ArrayList<Long>(100);
	
	/**
	 * Gets the current private message index.
	 * @return	The current private message index + 1.
	 */
	public int getPrivateMessageId() {
		return privateMessageId++;
	}
	
	/**
	 * Sets the current private message index.
	 * @param privateMessageId	The new private message index value.	
	 * @return					The PlayerRelations instance.
	 */
	public PlayerRelations setPrivateMessageId(int privateMessageId) {
		this.privateMessageId = privateMessageId;
		return this;
	}
	
	/**
	 * Gets the player's friend list.
	 * @return	The player's friends.
	 */
	public List<Long> getFriendList() {
		return friendList;
	}
	
	/**
	 * Gets the player's ignore list.
	 * @return	The player's ignore list.
	 */
	public List<Long> getIgnoreList() {
		return ignoreList;
	}
	
	/**
	 * Updates the player's friend list.
	 * @param online	If <code>true</code>, the players who have this player added, will be sent the notification this player has logged in.
	 * @return			The PlayerRelations instance.
	 */
	public PlayerRelations updateLists(boolean online) {
		if (status == Status.PRIVATE)
			online = false;
		player.getPacketSender().sendFriendStatus(2);
		for (Player players : GameServer.getWorld().getPlayers()) {
			if (players != null) {
				boolean temporaryOnlineStatus = online;
				if (players.getRelations().friendList.contains(player.getCredentials().getLongUsername())) {
					if (status.equals(Status.FRIENDS_ONLY) && !friendList.contains(players.getCredentials().getLongUsername()) ||
							status.equals(Status.PRIVATE) || ignoreList.contains(players.getCredentials().getLongUsername())) {
						temporaryOnlineStatus = false;
					}
					players.getPacketSender().sendFriend(player.getCredentials().getLongUsername(), temporaryOnlineStatus ? 1 : 0);
				}
			}
		}
		return this;
	}
	
	/**
	 * Adds a player to the associated-player's friend list.
	 * @param username	The user name of the player to add to friend list.
	 */
	public void addFriend(Long username) {
		String name = NameUtil.longToString(username);
		if (friendList.size() >= 200) {
			player.getPacketSender().sendMessage("Your friend list is full!");
			return;
		}
		if (ignoreList.contains(username)) {
			player.getPacketSender().sendMessage("Please remove " + name + " from your ignore list first.");
			return;
		}
		if (friendList.contains(username)) {
			player.getPacketSender().sendMessage(name + " is already on your friends list!");
		} else {
			friendList.add(username);
			updateLists(true);
			Player friend = GameServer.getWorld().getPlayerForName(name);
			if (friend != null)
				friend.getRelations().updateLists(true);
		}
	}
	
	/**
	 * Deletes a friend from the associated-player's friends list.
	 * @param username	The user name of the friend to delete.
	 */
	public void deleteFriend(Long username) {
		if (friendList.contains(username)) {
			friendList.remove(username);
			if (!status.equals(Status.PUBLIC)) {	
				Player ignored = GameServer.getWorld().getPlayerForName(NameUtil.longToString(username));
				if (ignored != null)
					ignored.getRelations().updateLists(false);
				updateLists(false);
			}
		} else {
			player.getPacketSender().sendMessage("This player is not on your friends list!");
		}
	}
	
	/**
	 * Adds a player to the associated-player's ignore list.
	 * @param username	The user name of the player to add to ignore list.
	 */
	public void addIgnore(Long username) {
		String name = NameUtil.longToString(username);
		if (ignoreList.size() >= 100) {
			player.getPacketSender().sendMessage("Your ignore list is full!");
			return;
		}
		if (friendList.contains(username)) {
			player.getPacketSender().sendMessage("Please remove " + name + " from your friend list first.");
			return;
		}
		if (ignoreList.contains(username)) {
			player.getPacketSender().sendMessage(name + " is already on your ignore list!");
		} else {
			ignoreList.add(username);
			updateLists(false);
			Player ignored = GameServer.getWorld().getPlayerForName(name);
			if (ignored != null)
				ignored.getRelations().updateLists(false);
		}
	}
	
	/**
	 * Deletes an ignored player from the associated-player's ignore list.
	 * @param username	The user name of the ignored player to delete from ignore list.
	 */
	public void deleteIgnore(Long username) {
		if (ignoreList.contains(username)) {
			ignoreList.remove(username);
			updateLists(true);
			if (status.equals(Status.PUBLIC)) {
				Player ignored = GameServer.getWorld().getPlayerForName(NameUtil.longToString(username));
				if (ignored != null)
					ignored.getRelations().updateLists(true);
			}
		} else {
			player.getPacketSender().sendMessage("This player is not on your ignore list!");
		}
	}
	
	/**
	 * Sends a private message to {@code friend}.
	 * @param friend	The player to private message.
	 * @param message	The message being sent in bytes.
	 * @param size		The size of the message.
	 */
	public void message(Player friend, byte[] message, int size) {
		if (GameServer.getWorld().getPlayerForName(friend.getCredentials().getUsername()) == null || friend.getRelations().status.equals(Status.FRIENDS_ONLY) && !friend.getRelations().friendList.contains(player) || friend.getRelations().status.equals(Status.PRIVATE)) {
			this.player.getPacketSender().sendMessage("This player is currently offline.");
			return;
		}
		friend.getPacketSender().sendPrivateMessage(player.getCredentials().getLongUsername(), player.getRights(), message, size);
	}
	
	/**
	 * Represents a player's friends list status, whether
	 * others will be able to see them online or not.
	 * 
	 * @author relex lawl
	 */
	private enum Status {
		/**
		 * Anyone can see this player online and
		 * private message him.
		 */
		PUBLIC,
		
		/**
		 * Only friends can see this player online
		 * and private message him.
		 */
		FRIENDS_ONLY,
		
		/**
		 * No one can private message or see this
		 * player online.
		 */
		PRIVATE;
	}
	
	/**
	 * The PlayerRelations constructor.
	 * @param player	The associated-player.
	 */
	public PlayerRelations(Player player) {
		this.player = player;
	}
	
	/**
	 * The associated player.
	 */
	private Player player;
}
