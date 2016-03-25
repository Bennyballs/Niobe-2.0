package org.niobe.world.content.clan;

import org.niobe.world.Player;
import org.niobe.world.content.clan.ClanChat.Rank;

/**
 * Represents a {@link org.niobe.world.Player} categorized
 * as a member in a {@link ClanChat}.
 *
 * @author relex lawl
 */
public final class ClanChatMember {

	/**
	 * The ClanChatMember constructor.
	 * @param player	The {@link org.niobe.world.Player} instance.
	 * @param clan		The {@link ClanChat} in which member is in.
	 * @param rank		The {@link ClanChat.Rank} the member has within said {@link ClanChat}.
	 */
	public ClanChatMember(Player player, ClanChat clan, Rank rank) {
		this.player = player;
		this.clan = clan;
		this.rank = rank;
	}

	/**
	 * The rank the player occupies in the {@link ClanChat}.
	 */
	private Rank rank;
	
	/**
	 * The {@link ClanChat} this member is in.
	 */
	private final ClanChat clan;
	
	/**
	 * The {@link org.niobe.world.Player} instance being 
	 * represented as the member.
	 */
	private final Player player;
	
	/**
	 * Gets the member's clan rank.
	 * @return	The rank of the member.
	 */
	public Rank getRank() {
		return rank;
	}

	/**
	 * Sets the member's rank within the clan.
	 * @param rank	The new member rank.
	 * @return		The ClanChatMember instance.
	 */
	public ClanChatMember setRank(Rank rank) {
		this.rank = rank;
		return this;
	}
	
	/**
	 * Gets the associated player.
	 * @return	The associated player.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Gets the clan chat the member is in.
	 * @return	The associated clan.
	 */
	public ClanChat getClan() {
		return clan;
	}
}
