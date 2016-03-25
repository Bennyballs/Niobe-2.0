package org.niobe.net.packet.event;

import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * that is received once a {@link org.niobe.world.Player} enters a new region
 * and also when the "Loading - Please Wait" box is finished loading.
 *
 * @author relex lawl
 */
public final class RegionChangeGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		switch (packet.getOpcode()) {
		case FINALIZE_REGION_LOAD:
			finalize(player, packet);
			break;
		case ENTER_NEW_REGION:
			enter(player, packet);
			break;
		}
	}
	
	/**
	 * Called when the "Loading - Please Wait" box is finished
	 * loading.
	 * @param player	The {@link org.niobe.world.Player} entering the new region.
	 * @param packet	The {@link org.niobe.net.packet.GamePacket} received from the client.
	 */
	private static void finalize(Player player, GamePacket packet) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Called when a new region is being entered.
	 * @param player	The {@link org.niobe.world.Player} entering the new region.
	 * @param packet	The {@link org.niobe.net.packet.GamePacket} received from the client.
	 */
	private static void enter(Player player, GamePacket packet) {
		// TODO Auto-generated method stub
	}
	
	
	public static final int FINALIZE_REGION_LOAD = 121, ENTER_NEW_REGION = 210;
}
