package org.niobe.net.packet.event;

import org.niobe.model.ChatMessage.Message;
import org.niobe.model.UpdateFlag.Flag;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * used for updating {@link org.niobe.model.ChatMessage}.
 *
 * @author relex lawl
 */
public final class ChatMessageGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		int effects = packet.readUnsignedByteS();
		int color = packet.readUnsignedByteS();
		int size = packet.getSize();
		byte[] text = packet.readBytesA(size);
		player.getChatMessage().set(new Message(color, effects, text));
		player.getUpdateFlag().flag(Flag.CHAT);
	}
}
