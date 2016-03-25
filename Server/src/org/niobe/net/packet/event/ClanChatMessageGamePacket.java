package org.niobe.net.packet.event;

import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.util.FileUtil;
import org.niobe.world.Player;
import org.niobe.world.content.clan.ClanChatManager;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * used for chat messages received from {@link org.niobe.world.content.clan.ClanChat}s.
 *
 * @author relex lawl
 */
public final class ClanChatMessageGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		String message = FileUtil.readString(packet.getBuffer());
		ClanChatManager.sendMessage(player, message);
	}
}
