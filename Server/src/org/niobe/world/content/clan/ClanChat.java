package org.niobe.world.content.clan;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.niobe.GameServer;
import org.niobe.task.Task;
import org.niobe.world.Player;

/**
 * A clan chat is where players can chat and interact
 * and also take advantages such as loot and coin share.
 *
 * @author relex lawl
 */
public final class ClanChat {

	/**
	 * The ClanChat constructor.
	 * @param owner	The owner {@link org.niobe.world.Player} instance.
	 * @param name	The name of the clan chat.
	 * @param index	The clan chat index.
	 */
	public ClanChat(Player owner, String name, int index) {
		this.owner = owner;
		this.name = name;
		this.index = index;
		this.ownerName = owner.getCredentials().getUsername();
	}
	
	/**
	 * The ClanChat constructor.
	 * @param ownerName	The owner user name.
	 * @param name		The name of the clan chat.
	 * @param index		The clan chat index.
	 */
	public ClanChat(String ownerName, String name, int index) {
		this.owner = GameServer.getWorld().getPlayerForName(ownerName);
		this.ownerName = ownerName;
		this.name = name;
		this.index = index;
	}
	
	/**
	 * The name of the clan chat.
	 */
	private String name;
	
	/**
	 * The owner of the clan chat represented by
	 * a {@link org.niobe.world.Player}.
	 */
	private Player owner;
	
	/**
	 * The user name of {@link #owner}.
	 */
	private String ownerName;

	/**
	 * The unique id of the clan chat.
	 */
	private final int index;
	
	/**
	 * The amount of members in the clan chat.
	 */
	private int totalMembers;
	
	/**
	 * The flag that checks if a {@link ClanChatAction}
	 * is needs to be executed and avoids multiple {@link org.niobe.task.Task}s
	 * from being submitted to {@link org.niobe.task.TaskManager}.
	 */
	private boolean applyingChange;
	
	/**
	 * The flag that checks which {@link DropShare}
	 * to use.
	 */
	private boolean lootshare = true, coinshare;
	
	/**
	 * The id used to send a {@link ClanChatMember #getPlayer()} the
	 * {@link org.niobe.net.packet.GamePacketSender #sendInterfaceSpriteChange(int, int, int)}
	 * packet to their respective client.
	 */
	private int dropShareId;
	
	/**
	 * The {@link DropShare} being used by the clan chat.
	 */
	private DropShare dropShare = DropShare.NONE;

	/**
	 * This array contains all the {@link ClanChatMember}s in this 
	 * clan chat.
	 */
	private ClanChatMember[] members = new ClanChatMember[100];
	
	/**
	 * This array contains all the user names ranked in this
	 * clan chat.
	 */
	private final Map<String, Rank> rankedNames = new HashMap<String, Rank>();
	
	/**
	 * This array contains all the rank requirements
	 * needed to be met in order to perform certain
	 * actions in this clan chat; see {@link #RANK_REQUIRED_TO_ENTER},
	 * {@link #RANK_REQUIRED_TO_KICK} and {@link #RANK_REQUIRED_TO_TALK}. 
	 */
	private final Rank[] rankRequirement = new Rank[3];	
	
	/**
	 * This list contains all the {@link ClanChatAction}s that will be
	 * executed upon the completion of {@link #addAction(ClanChatAction)}'s 
	 * {@link org.niobe.task.Task}.
	 */
	private final List<ClanChatAction> actions = new LinkedList<ClanChatAction>();
	
	/**
	 * Adds a member to the {@link #members} array and
	 * adds onto the {@link #totalMembers}.
	 * @param member	The {@link ClanChatMember} being signed into the {@link ClanChat}.
	 * @return			The ClanChat instance.
	 */
	public ClanChat addMember(ClanChatMember member) {
		for (int i = 0; i < members.length; i++) {
			if (members[i] == null) {
				totalMembers++;
				members[i] = member;
				break;
			}
		}
		return this;
	}
	
	/**
	 * Removes a member from the {@link #members} array and 
	 * deducts from {@link #totalMembers}.
	 * @param name	The user name of the {@link ClanChatMember#getPlayer()}
	 * @return		The ClanChat instance.
	 */
	public ClanChat removeMember(String name) {
		for (int i = 0; i < getMembers().length; i++) {
			if (getMembers()[i] != null && getMembers()[i].getPlayer() != null && getMembers()[i].getPlayer().getCredentials().getUsername().equals(name)) {
				totalMembers--;
				this.members[i] = null;
				break;
			}
		}
		return this;
	}
	
	/**
	 * Assigns {@link Rank} the the {@link ClanChatMember} with said
	 * {@link org.niobe.world.Player} instance.
	 * @param player	The {@link org.niobe.world.Player} to give {@link Rank} to.
	 * @param rank		The {@link Rank} to assign.
	 * @return			The ClanChat instance.
	 */
	public ClanChat giveRank(Player player, Rank rank) {
		rankedNames.put(player.getCredentials().getUsername(), rank);
		for (ClanChatMember member : members) {
			if (member != null && member.getPlayer().getCredentials().getUsername().equals(player.getCredentials().getUsername())) {
				member.setRank(rank);
			}
		}
		return this;
	}
	
	/**
	 * Adds {@link ClanChatAction} onto the {@link #actions}
	 * list and submits a {@link org.niobe.task.Task} in which
	 * all pending {@link ClanChatAction}s will be executed upon
	 * 60 {@link org.niobe.world.util.GameConstants #GAME_TICK} ticks.
	 * @param action	The {@link ClanChatAction} to add to the list of pending actions.
	 */
	public void addAction(final ClanChatAction action) {
		actions.add(action);
		if (!applyingChange) {
			applyingChange = true;
			GameServer.getTaskManager().submit(new Task(60) {
				@Override
				public void execute() {
					for (ClanChatAction action : actions) {
						action.execute();
						actions.remove(actions);
					}
					applyingChange = false;
					stop();
				}
			});
		}
	}
	
	/**
	 * Gets the {@link org.niobe.world.Player} instance
	 * of the clan chat owner.
	 * @return	The owner of the clan chat.
	 */
	public Player getOwner() {
		return owner;
	}
	
	/**
	 * Sets the owner of the clan chat.
	 * @param owner	The {@link org.niobe.world.Player} representing the owner.
	 * @return		The ClanChat instance.
	 */
	public ClanChat setOwner(Player owner) {
		this.owner = owner;
		return this;
	}
	
	/**
	 * Gets the owner's user name, used for {@link ClanChat} who's
	 * owners are off line.
	 * @return	The owner user name.
	 */
	public String getOwnerName() {
		return ownerName;
	}
	
	/**
	 * Gets the unique id for this clan chat.
	 * @return	The index.
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Gets the {@link ClanChat}'s name.
	 * @return	The name of the clan chat.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the {@link ClanChat}.
	 * @param name	The new name of the clan chat.
	 * @return		The ClanChat instance.
	 */
	public ClanChat setName(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * Gets the {@link #members} array that are in
	 * this {@link ClanChat}.
	 * @return	The {@link #members} array.
	 */
	public ClanChatMember[] getMembers() {
		return members;
	}
	
	/**
	 * Gets the total amount of members in the
	 * {@link ClanChat}.
	 * @return	The {@link #totalMembers} value.
	 */
	public int getTotalMembers() {
		return totalMembers;
	}
	
	/**
	 * Gets the {@link org.niobe.world.Player}'s {@link Rank}
	 * within the {@link ClanChat}.
	 * @param player	The {@link org.niobe.world.Player} to get {@link Rank} for.
	 * @return			The {@link Rank} of said member.
	 */
	public Rank getRank(Player player) {
		Rank rank = rankedNames.get(player.getCredentials().getUsername());
		return rank;
	}
	
	/**
	 * Gets all the ranked user names within the
	 * {@link #rankedNames} map.
	 * @return	The {@link #rankedNames} map.
	 */
	public Map<String, Rank> getRankedNames() {
		return rankedNames;
	}
	
	/**
	 * Gets the array of {@link Rank} requirements
	 * in order to do certain actions in the {@link ClanChat}.
	 * @return	The {@link #rankRequirement} array.
	 */
	public Rank[] getRankRequirement() {
		return rankRequirement;
	}

	/**
	 * Sets the designated {@link #rankRequirement} with said
	 * index.
	 * @param index				The index of the rank requirement to edit.
	 * @param rankRequirement	The new {@link Rank} value.
	 * @return					The ClanChat instance.
	 */
	public ClanChat setRankRequirements(int index, Rank rankRequirement) {
		this.rankRequirement[index] = rankRequirement;
		return this;
	}

	/**
	 * Gets the {@link DropShare} for the {@link ClanChat}.
	 * @return	The current {@link DropShare}.
	 */
	public DropShare getDropShare() {
		return dropShare;
	}

	/**
	 * Sets the {@link DropShare} to said value.
	 * @param dropShare	The {@link DropShare} to set for {@link ClanChat}.
	 * @return			The ClanChat instance.
	 */
	public ClanChat setDropShare(DropShare dropShare) {
		this.dropShare = dropShare;
		return this;
	}

	/**
	 * Checks if the {@link ClanChat} loot share option
	 * in the clan setup interface is active.
	 * @return	{@code true} if the {@link ClanChat} is loot sharing.
	 */
	public boolean isLootsharing() {
		return lootshare;
	}

	/**
	 * Sets the {@link ClanChat} loot share option.
	 * @param lootshare	If {@code true} the loot share option will take effect.
	 * @return			The ClanChat instance.
	 */
	public ClanChat setLootshare(boolean lootshare) {
		this.lootshare = lootshare;
		this.coinshare = !lootshare;
		return this;
	}

	/**
	 * Checks if the {@link ClanChat} coin share option
	 * in the clan setup interface is active.
	 * @return	{@code true} if the {@link ClanChat} is coin sharing.
	 */
	public boolean isCoinsharing() {
		return coinshare;
	}

	/**
	 * Sets the {@link ClanChat} coin share option.
	 * @param coinshare	If {@code true} the coin share option will take effect.
	 * @return			The ClanChat instance.
	 */
	public ClanChat setCoinshare(boolean coinshare) {
		this.coinshare = coinshare;
		this.lootshare = !coinshare;
		return this;
	}
	
	/**
	 * Gets the drop share id to send 
	 * {@link org.niobe.net.packet.GamePacketSender#sendInterfaceSpriteChange(int, int, int)}
	 * to all {@link ClanChatMember}s in the {@link #members} array.
	 * @return	The drop share id.
	 */
	public int getDropShareId() {
		return dropShareId;
	}

	/**
	 * Sets the drop share id when a drop share option
	 * is chosen.
	 * @param dropShareId	The drop share id.
	 * @return				The ClanChat instance.
	 */
	public ClanChat setDropShareId(int dropShareId) {
		this.dropShareId = dropShareId;
		return this;
	}

	/**
	 * Represents a rank in the clan chat
	 * game frame tab interface.
	 *
	 * @author relex lawl
	 */
	public enum Rank {
		/**
		 * The friend rank, given to anyone in
		 * the {@link ClanChat#getOwner()}'s friends list.
		 */
		FRIEND,
		
		/**
		 * The recruit rank.
		 */
		RECRUIT,
		
		/**
		 * The corporal rank.
		 */
		CORPORAL,
		
		/**
		 * The sergeant rank.
		 */
		SERGEANT,
		
		/**
		 * The lieutenant rank.
		 */
		LIEUTENANT,
		
		/**
		 * The captain rank.
		 */
		CAPTAIN,
		
		/**
		 * The general rank.
		 */
		GENERAL,
		
		/**
		 * The owner rank, only given to
		 * {@link ClanChat#getOwner()}
		 */
		OWNER,
		
		/**
		 * The server administrator rank given
		 * to any {@link org.niobe.world.Player}
		 * with a {@link org.niobe.model.PlayerRights} equal to
		 * {@link org.niobe.model.PlayerRights#ADMINISTRATOR}.
		 */
		SERVER_ADMINISTRATOR;
		
		/**
		 * Gets the {@link Rank} with an {@link #ordinal()}
		 * value equal to {@param id}.
		 * @param id	The ordinal value to get {@link Rank} with.
		 * @return		The Rank retrieved.
		 */
		public static Rank forId(int id) {
			for (Rank rank : Rank.values()) {
				if (rank.ordinal() == id) {
					return rank;
				}
			}
			return null;
		}
	}
	
	/**
	 * Represents a drop share value from
	 * the {@link ClanChat}, value set will depend on
	 * whether the {@link ClanChat#isCoinsharing()} is {@code true}
	 * or {@link ClanChat#isLootsharing()} is {@code true}.
	 *
	 * @author relex lawl
	 */
	public enum DropShare {
		/**
		 * The clan chat is not sharing
		 * the loot.
		 */
		NONE,
		
		/**
		 * The clan chat is given a random
		 * chance of a rare drop being given
		 * to a random {@link ClanChatMember}.
		 */
		LOOT_SHARE,
		
		/**
		 * The clan chat is splitting the value
		 * of any rare drop to all {@link ClanChatMember}s
		 * populating the {@link ClanChat#getMembers()} array.
		 */
		COIN_SHARE
	}

	/**
	 * Represents the message color red-green-blue
	 * code, which depends on the client's screen size.
	 *
	 * @author relex lawl
	 */
	public enum MessageColor {
		/**
		 * The blue rgb's.
		 */
		BLUE("000080", "0000FF", "0000FF"),
		
		/**
		 * The red rgb's.
		 */
		RED("8B0000", "FF0000", "FF0000"),
		
		/**
		 * The green rgb's.
		 */
		GREEN("00D807", "00FF09", "00FF09"),
		
		/**
		 * The purple rgb's.
		 */
		PURPLE("9100FF", "BC00FF", "BC00FF"),
		
		/**
		 * The pink rgb's.
		 */
		PINK("D10099", "FF00BC", "FF00BC"),
		
		/**
		 * The yellow rgb's.
		 */
		YELLOW("F2EA00", "F7FF00", "F7FF00"),
		
		/**
		 * The cyan rgb's.
		 */
		CYAN("008B8B", "00FFFF", "00FFFF"),
		
		/**
		 * The teal rgb's.
		 */
		TEAL("00FFEF", "00C3B6", "00C3B6"),
		
		/**
		 * The white rgb's.
		 */
		WHITE("FFFFFF", "FFFFFF", "FFFFFF"),
		
		/**
		 * The black rgb's.
		 */
		BLACK("000000", "000000", "000000"),
		
		/**
		 * The grey rgb's.
		 */
		GREY("708090", "778899", "778899");
		
		/**
		 * The MessageColor constructor.
		 * @param rgb	The red-green-blue code values in an array,
		 * 				correspondent to the client size.
		 */
		private MessageColor(String... rgb) {
			if (rgb.length > 3)
				throw new IllegalArgumentException("Clan chat message colors can only hold 3 decimal values.");
			this.rgb = rgb;
		}
		
		/**
		 * This array contains the red-green-blue values.
		 */
		private final String[] rgb;
		
		/**
		 * Gets the red-green-blue code values.
		 * @return	The {@link #rgb} array.
		 */
		public String[] getRGB() {
			return rgb;
		}
		
		/**
		 * Gets the {@link MessageColor} correspondent to
		 * the id (from the {@link #ordinal()} value).
		 * @param id	The ordinal value to get {@link MessageColor} with.
		 * @return		The retrieved {@link MessageColor}.
		 */
		public static MessageColor forId(int id) {
			for (MessageColor color : MessageColor.values()) {
				if (color.ordinal() == id) {
					return color;
				}
			}
			return null;
		}
	}

	/**
	 * The rank requirement constants to retrieve from
	 * the {@link ClanChat#rankRequirement} array.
	 */
	public static final int RANK_REQUIRED_TO_ENTER = 0, RANK_REQUIRED_TO_KICK = 1, RANK_REQUIRED_TO_TALK = 2;
}