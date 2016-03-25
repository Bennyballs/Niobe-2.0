package org.niobe.net.packet.event;

import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * that is called whenever a {@link org.niobe.world.Player} connection has 
 * been idle for an equivalent of {@link org.niobe.net.NetworkConstants#IDLE_TIME} minutes.
 *
 * @author relex lawl
 */
public final class IdleConnectionGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		if (player.getFields().isDead() || player.getFields().getCombatAttributes().getAttackedBy() != null)
			return;
		//World.deregister(player); //un-comment this if you want to enable idle logout
	}

	
}
