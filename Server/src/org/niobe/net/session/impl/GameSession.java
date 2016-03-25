package org.niobe.net.session.impl;

import org.jboss.netty.channel.Channel;
import org.niobe.GameServer;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketManager;
import org.niobe.net.session.Session;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.session.Session}
 * in which a player will receive packets sent from their clients
 * over to the game server.
 *
 * @author relex lawl
 */
public final class GameSession extends Session {

	/**
	 * The GameSession constructor.
	 * @param channel	The channel to receive information from.
	 * @param player	The player using the channel.
	 */
	public GameSession(Channel channel, Player player) {
		super(channel);
		this.player = player;
	}
	
	/**
	 * The player connected with associated channel.
	 */
	private final Player player;

	@Override
	public void receiveMessage(Object message) throws Exception {
		if (message.getClass() == GamePacket.class) {
			GamePacket packet = (GamePacket) message;
			GamePacketManager.parse(player, packet);
		}
	}

	@Override
	public void finalize() throws Exception {
		GameServer.getWorld().unregister(player);
	}
}
