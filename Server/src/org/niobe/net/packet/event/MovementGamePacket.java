package org.niobe.net.packet.event;

import org.niobe.model.MovementQueue.MovementFlag;
import org.niobe.model.action.impl.WalkGameAction;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * where a {@link org.niobe.world.Player}'s movement queue is managed.
 *
 * @author relex lawl
 */
public final class MovementGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		final MovementFlag flag = player.getMovementQueue().getMovementFlag();
		if (player.getFields().isDead() || flag == MovementFlag.CANNOT_MOVE) {
			return;
		}
		if (flag == MovementFlag.STUNNED || flag == MovementFlag.FROZEN) {
			player.getPacketSender().sendMessage("A magical force stops you from moving.");
			return;
		}
		player.getPacketSender().sendInterfaceRemoval();
		player.getPacketSender().sendNonWalkableAttributeRemoval();
		player.setAction(new WalkGameAction<Player>(player, packet));
	}
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcodes for a 
	 * {@link org.niobe.world.Player} movement queue.
	 */
	public static final int COMMAND_MOVEMENT_OPCODE = 98, GAME_MOVEMENT_OPCODE = 164, MINIMAP_MOVEMENT_OPCODE = 248;
}
