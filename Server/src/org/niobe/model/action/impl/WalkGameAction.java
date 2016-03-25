package org.niobe.model.action.impl;

import org.niobe.model.GameCharacter;
import org.niobe.model.Position;
import org.niobe.model.MovementQueue.MovementFlag;
import org.niobe.model.action.GameAction;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.event.MovementGamePacket;
import org.niobe.world.content.combat.CombatManager;

/**
 * An implementation of {@link org.niobe.model.action.GameAction}
 * used for the movement of a {@link org.niobe.world.Player} and the 
 * alternation of some flags and mainly used to cancel out the current
 * {@link org.niobe.model.action.GameAction} that the {@link org.niobe.world.Player} is performing.
 *
 * @author relex lawl
 */
public final class WalkGameAction<T extends GameCharacter> extends GameAction<T> {

	/**
	 * The WalkGameAction constructor.
	 * @param character	The {@link org.niobe.model.GameCharacter} walking.
	 * @param packet	The {@link org.niobe.net.packet.GamePacket} received from the client movement.
	 */
	public WalkGameAction(T character, GamePacket packet) {
		super(0, character);
		int size = packet.getSize();
		if (packet.getOpcode() == MovementGamePacket.MINIMAP_MOVEMENT_OPCODE) {
			size -= 14;
		}
		if (packet.getOpcode() != MovementGamePacket.COMMAND_MOVEMENT_OPCODE) {
			CombatManager.resetFlags(character);
		}
		character.getMovementQueue().setMovementFlag(character.getFields().isRunning() ? MovementFlag.RUNNING : MovementFlag.WALKING);
		character.setEntityInteraction(null).setFollowEntity(null);
		character.getFields().setBanking(false);
		character.getMovementQueue().start();
		int steps = (size - 5) / 2;
		int[][] path = new int[steps][2];
		int stepX = packet.readLEShortA();
		for (int i = 0; i < steps; i++) {
			path[i][0] = packet.readByte();
			path[i][1] = packet.readByte();
		}
		int stepY = packet.readLEShort();
		packet.readByteC();
		character.getMovementQueue().addDestination(new Position(stepX, stepY));
		for (int i = 0; i < steps; i++) {
			path[i][0] += stepX;
			path[i][1] += stepY;
			character.getMovementQueue().addDestination(new Position(path[i][0], path[i][1]));
		}
		character.getMovementQueue().finish();
		character.getMovementQueue().setMovementFlag(MovementFlag.NONE);
	}
	
	@Override
	public void execute() {
		stop();
	}
}
