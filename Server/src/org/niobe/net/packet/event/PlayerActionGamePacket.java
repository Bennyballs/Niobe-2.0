package org.niobe.net.packet.event;

import org.niobe.GameServer;
import org.niobe.model.action.GameAction;
import org.niobe.model.action.impl.FollowPlayerGameAction;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Player;
import org.niobe.world.content.combat.CombatManager;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * that is called upon the interaction click on a {@link org.niobe.world.Player}.
 *
 * @author relex lawl
 */
public final class PlayerActionGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		if (player.getFields().isDead())
			return;
		switch(packet.getOpcode()) {
		case ATTACK_PLAYER:
			attack(player, packet);
			break;
		case OPTION_1:
			option1(player, packet);
			break;
		case FOLLOW_PLAYER:
			follow(player,  packet);
			break;
		case TRADE_PLAYER:
			trade(player, packet);
			break;
		}
	}
	
	private static void attack(Player player, GamePacket packet) {
		int index = packet.readLEShort();
		Player victim = GameServer.getWorld().getPlayers()[index];
		if (victim == null)
			return;
		CombatManager.engageCombat(player, victim);
	}

	/**
	 * Manages the first option click on a player option menu.
	 * @param player	The player clicking the other entity.
	 * @param packet	The packet to read values from.
	 */
	private static void option1(final Player player, GamePacket packet) {
		int id = packet.readShort();
		System.out.println("id=" + id);
		if(id < 0 || id >= GameServer.getWorld().getPlayers().length) {
			return;
		}
		Player victim = (Player) GameServer.getWorld().getPlayers()[id];
		if (victim == null)
			return;
		System.out.println("option1");
	}
	
	/**
	 * Manages the second option click on a player option menu.
	 * @param player	The player clicking the other entity.
	 * @param packet	The packet to read values from.
	 */
	private static void follow(Player player, GamePacket packet) {
		int id = packet.readLEShort();
		if(id < 0 || id >= GameServer.getWorld().getPlayers().length) {
			return;
		}
		Player victim = (Player) GameServer.getWorld().getPlayers()[id];
		if (victim == null)
			return;
		GameAction<Player> action = new FollowPlayerGameAction<Player>(player, victim);
		player.setAction(action);
	}
	
	/**
	 * Manages the third option click on a player option menu.
	 * @param player	The player clicking the other entity.
	 * @param packet	The packet to read values from.
	 */
	private static void trade(Player player, GamePacket packet) {
		int id = packet.readLEShort();
		if(id < 0 || id >= GameServer.getWorld().getPlayers().length) {
			return;
		}
		Player victim = (Player) GameServer.getWorld().getPlayers()[id];
		if (victim == null)
			return;
		//TODO trade
	}
	
	public static final int OPTION_1 = 128;
	
	public static final int ATTACK_PLAYER = 153;

	public static final int TRADE_PLAYER = 139;
	
	public static final int FOLLOW_PLAYER = 73;
}
