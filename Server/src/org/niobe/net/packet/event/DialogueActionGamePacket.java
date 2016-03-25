package org.niobe.net.packet.event;

import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Player;
import org.niobe.world.content.dialogue.DialogueManager;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * used for {@link org.niobe.world.content.dialogue.Dialogue} actions
 * executed by a {@link org.niobe.world.Player}.
 *
 * @author relex lawl
 */
public final class DialogueActionGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		switch (packet.getOpcode()) {
		case CONTINUE_DIALOGUE_OPCODE:
			DialogueManager.next(player);
			break;
		}
	}
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode
	 * used when a dialogue's "Click here to continue" button
	 * is clicked.
	 */
	public static final int CONTINUE_DIALOGUE_OPCODE = 40;
}
