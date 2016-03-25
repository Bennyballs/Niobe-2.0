package org.niobe.net.packet.event;

import java.util.logging.Logger;

import org.niobe.model.Position;
import org.niobe.model.action.DestinationListener;
import org.niobe.model.action.impl.GameObjectClickGameAction;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.GameObject;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.content.dialogue.DialogueManager;
import org.niobe.world.content.dialogue.impl.BankerDialogue;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * where {@link org.niobe.world.GameObject} interaction is handled.
 *
 * @author relex lawl
 */
public final class GameObjectActionGamePacket implements GamePacketEvent {

	/**
	 * The PacketListener logger to debug information and print out errors.
	 */
	private final static Logger logger = Logger.getLogger(GameObjectActionGamePacket.class.getName());

	@Override
	public void read(Player player, GamePacket packet) {
		switch (packet.getOpcode()) {
		case FIRST_CLICK:
			firstClick(player, packet);
			break;
		case SECOND_CLICK:
			secondClick(player, packet);
			break;
		case THIRD_CLICK:
			//TODO
			break;
		case FOURTH_CLICK:
			//TODO
			break;
		case FIFTH_CLICK:
			//TODO
			break;
		}
	}
	
	/**
	 * Handles a first-click option on the {@link org.niobe.world.GameObject}.
	 * @param player	The player clicking the {@link org.niobe.world.GameObject}.
	 * @param packet	The {@link org.niobe.net.packet.GamePacket} received from client.
	 */
	private static void firstClick(final Player player, GamePacket packet) {
		int x = packet.readLEShortA();
		final int id = packet.readUnsignedShort();
		int y = packet.readUnsignedShortA();
		final Position position = new Position(x, y, player.getPosition().getZ());
		final GameObject gameObject = new GameObject(id, position);
		final DestinationListener action = new DestinationListener() {
			@Override
			public void reachDestination() {
				if (gameObject.getDefinition().getActions() != null) {
					for (String action : gameObject.getDefinition().getActions()) {
						if (action != null && (action.equalsIgnoreCase("use-quickly") || action.equalsIgnoreCase("collect"))) {
							DialogueManager.start(player, new BankerDialogue(new Mob(2759, position)));
							return;
						}
					}
				}
				switch (id) {
				default:
					logger.info("Unhandled first click object id; [id, position] : [" + id + ", " + position.toString() + "]");
					break;
				}
			}
		};
		player.setAction(new GameObjectClickGameAction<Player>(player, gameObject, action));
	}
	
	/**
	 * Handles a second-click option on the {@link org.niobe.world.GameObject}.
	 * @param player	The player clicking the {@link org.niobe.world.GameObject}.
	 * @param packet	The {@link org.niobe.net.packet.GamePacket} received from client.
	 */
	private static void secondClick(final Player player, GamePacket packet) {
		final int id = packet.readLEShortA();
		int y = packet.readLEShort();
		int x = packet.readUnsignedShortA();
		final Position position = new Position(x, y, player.getPosition().getZ());
		final GameObject gameObject = new GameObject(id, position);
		final DestinationListener action = new DestinationListener() {
			@Override
			public void reachDestination() {
				if (gameObject.getDefinition().getActions() != null) {
					for (String action : gameObject.getDefinition().getActions()) {
						if (action != null && (action.equalsIgnoreCase("use-quickly") || action.equalsIgnoreCase("collect"))) {
							player.getBank().open();
							return;
						}
					}
				}
				switch (id) {
				default:
					logger.info("Unhandled second click object id; [id, position] : [" + id + ", " + position.toString() + "]");
					break;
				}
			}
		};
		player.setAction(new GameObjectClickGameAction<Player>(player, gameObject, action));
	}

	/**
	 * The opcode constants for {@link org.niobe.world.GameObject} 
	 * interaction.
	 */
	public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70, FOURTH_CLICK = 234, FIFTH_CLICK = 228;
}
