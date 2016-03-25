package org.niobe.net.packet.event;

import org.niobe.model.UpdateFlag.Flag;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * used for the confirmation of a {@link org.niobe.world.Player}'s appearance
 * set in the appearance interface.
 *
 * @author relex lawl
 */
public final class ConfirmAppearanceGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		player.getAppearance().set(0, packet.readByte());
		player.getAppearance().set(1, packet.readByte());
		player.getAppearance().set(7, packet.readByte());
		player.getAppearance().set(2, packet.readByte());
		player.getAppearance().set(3, packet.readByte());
		player.getAppearance().set(4, packet.readByte());
		player.getAppearance().set(5, packet.readByte());
		player.getAppearance().set(6, packet.readByte());
		player.getAppearance().set(8, packet.readByte());
		player.getAppearance().set(9, packet.readByte());
		player.getAppearance().set(10, packet.readByte());
		player.getAppearance().set(11, packet.readByte());
		player.getAppearance().set(12, packet.readByte());
		player.getUpdateFlag().flag(Flag.APPEARANCE);
	}
}
