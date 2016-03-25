package org.niobe.net.packet.event;

import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.util.NameUtil;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * used for note game frame tab interface actions that will be saved
 * upon the call of {@link org.niobe.world.content.PlayerSaving#save(Player)}.
 *
 * @author relex lawl
 */
public final class NoteTabActionGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		switch (packet.getOpcode()) {
		case ADD_NOTE:
			int id = packet.readByte();
			String note = NameUtil.longToString(packet.readLong());
			player.getFields().setNote(id, note);
			break;
		case DELETE_ALL_NOTES:
			player.getFields().setNotes(new String[30]);
			break;
		}
	}

	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode
	 * that signifies the client action of adding a new note.
	 */
	public static final int ADD_NOTE = 30;
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode
	 * that signifies the client action of deleting all notes.
	 */
	public static final int DELETE_ALL_NOTES = 31;
}
