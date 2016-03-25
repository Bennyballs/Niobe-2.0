package org.niobe.net.packet.event;

import java.util.logging.Logger;

import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent} which
 * manages a non-set/nulled packet and logs its information. 
 * 
 * @author relex lawl
 */
public final class DefaultGamePacket implements GamePacketEvent {
	
	/**
	 * The  logger to debug information and print out errors.
	 */
	private static final Logger logger = Logger.getLogger(DefaultGamePacket.class.getName());

	@Override
	public void read(Player player, GamePacket packet) {
		logger.warning("Unhandled game packet - [opcode, size] : [" + packet.getOpcode() + ", " + packet.getSize() + "]");
	}
}
