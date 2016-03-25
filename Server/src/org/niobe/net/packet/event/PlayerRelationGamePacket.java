package org.niobe.net.packet.event;

import org.niobe.GameServer;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.util.NameUtil;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * used for {@link org.niobe.model.PlayerRelation} related actions.
 *
 * @author relex lawl
 */
public final class PlayerRelationGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		long username = packet.readLong();
		switch (packet.getOpcode()) {
		case ADD_FRIEND_OPCODE:
			player.getRelations().addFriend(username);
			break;
		case ADD_IGNORE_OPCODE:
			player.getRelations().addIgnore(username);
			break;
		case REMOVE_FRIEND_OPCODE:
			player.getRelations().deleteFriend(username);
			break;
		case REMOVE_IGNORE_OPCODE:
			player.getRelations().deleteIgnore(username);
			break;
		case SEND_MESSAGE_OPCODE:
			Player friend = GameServer.getWorld().getPlayerForName(NameUtil.longToString(username));
			int size = packet.getSize();
			byte[] message = packet.readBytes(size);
			player.getRelations().message(friend, message, size);
			break;
		}
	}

	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode
	 * used when adding a friend.
	 */
	public static final int ADD_FRIEND_OPCODE = 188;
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode
	 * used when removing a friend.
	 */
	public static final int REMOVE_FRIEND_OPCODE = 215;
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode
	 * used when adding a player to the ignore list.
	 */
	public static final int ADD_IGNORE_OPCODE = 133;
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode
	 * used when removing a player from the ignore list.
	 */
	public static final int REMOVE_IGNORE_OPCODE = 74;
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode
	 * used when sending a private message to a friend.
	 */
	public static final int SEND_MESSAGE_OPCODE = 126;
}
