package org.niobe.net.packet.event;

import org.niobe.GameServer;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * that is used for {@link org.niobe.world.Player} to {@link org.niobe.world.Player}
 * trading.
 *
 * @author relex lawl
 */
public final class TradeAcceptanceGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		if (player.getFields().isDead())
			return;
		int index = packet.getOpcode() == TRADE_OPCODE ? (packet.readShort() & 0xFF) : packet.readLEShort();
		Player target = GameServer.getWorld().getPlayers()[index];
		if (target == null) 
			return;
		//TODO trading
	}
	
	/**
	 * This opcode is used when a player clicks
	 * on the "Trade with" in the player menu.
	 */
	public static final int TRADE_OPCODE = 128;
	
	/**
	 * This opcode is used when a player clicks
	 * on the trade request in their chat box.
	 */
	public static final int CHATBOX_TRADE_OPCODE = 139;

}
