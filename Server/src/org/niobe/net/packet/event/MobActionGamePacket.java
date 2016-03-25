package org.niobe.net.packet.event;

import java.util.logging.Logger;

import org.niobe.GameServer;
import org.niobe.model.action.GameAction;
import org.niobe.model.action.DestinationListener;
import org.niobe.model.action.impl.MobClickGameAction;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.content.ShopManager;
import org.niobe.world.content.combat.CombatManager;
import org.niobe.world.content.dialogue.*;
import org.niobe.world.content.dialogue.impl.*;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * that is used for {@link org.niobe.world.Mob} interaction clicks done
 * by a {@link org.niobe.world.Player}.
 *
 * @author relex lawl
 */
public final class MobActionGamePacket implements GamePacketEvent {

	/**
	 * The logger for the {@link MobActionGamePacket} class.
	 */
	private static final Logger logger = Logger.getLogger(MobActionGamePacket.class.getName());
	
	@Override
	public void read(Player player, GamePacket packet) {
		switch (packet.getOpcode()) {
		case ATTACK_MOB_OPCODE:
			attackMob(player, packet);
			break;
		case FIRST_CLICK_OPCODE:
			firstClick(player, packet);
			break;
		case SECOND_CLICK_OPCODE:
			secondClick(player, packet);
			break;
		default:
			logger.info("Unhandled mob action packet - [opcode, size] : [" + packet.getOpcode() + ", " + packet.getSize() + "]");
			break;
		}
	}
	
	/**
	 * Handles the "Attack" {@link org.niobe.world.Mob} 
	 * interaction.
	 * @param player	The {@link org.niobe.world.Player} clicking.
	 * @param packet	The {@link org.niobe.net.packet.GamePacket} received from the client.
	 */
	private static void attackMob(final Player player, final GamePacket packet) {
		int index = packet.readShortA();
		final Mob mob = GameServer.getWorld().getMobs()[index];
		if (mob == null || mob.getFields().isDead())
			return;
		CombatManager.engageCombat(player, mob);
	}
	
	/**
	 * Handles the first click {@link org.niobe.world.Mob} 
	 * interaction.
	 * @param player	The {@link org.niobe.world.Player} clicking.
	 * @param packet	The {@link org.niobe.net.packet.GamePacket} received from the client.
	 */
	private static void firstClick(final Player player, final GamePacket packet) {
		int index = packet.readLEShort();
		final Mob mob = GameServer.getWorld().getMobs()[index];
		if (mob == null || mob.getFields().isDead())
			return;
		final DestinationListener reachDestination = new DestinationListener() {
			@Override
			public void reachDestination() {
				player.setEntityInteraction(mob);
				mob.setPositionToFace(player.getPosition());
				switch (mob.getId()) {
				case 1:
					DialogueManager.start(player, DialogueManager.getDialogues().get(0));
					break;
				case 649:
					DialogueManager.start(player, new ShopDialogue(mob, ShopManager.getShops().get(0))); //archer
					break;
				case 461:
					DialogueManager.start(player, new ShopDialogue(mob, ShopManager.getShops().get(2))); //magic
					break;
				case 559:
					DialogueManager.start(player, new ShopDialogue(mob, ShopManager.getShops().get(2))); //brian
					break;
				case 650:
					DialogueManager.start(player, new ShopDialogue(mob, ShopManager.getShops().get(1))); //warrior
					break;
				case 12377:
				case 13637:
				case 9687:
					DialogueManager.start(player, new ShopDialogue(mob, ShopManager.getShops().get(3))); //pkp store
					break;
				case 494:
				case 495:
				case 2759:
					DialogueManager.start(player, new BankerDialogue(mob));
					break;
				default:
					logger.info("Unhandled mob first click: " + mob.getId());
					break;
				}
			}
			
		};
		GameAction<Player> action = new MobClickGameAction<Player>(player, mob, reachDestination);
		player.setAction(action);
	}
	
	/**
	 * Handles the second click {@link org.niobe.world.Mob} 
	 * interaction.
	 * @param player	The {@link org.niobe.world.Player} clicking.
	 * @param packet	The {@link org.niobe.net.packet.GamePacket} received from the client.
	 */
	private static void secondClick(final Player player, final GamePacket packet) {
		int index = packet.readLEShortA();
		final Mob mob = GameServer.getWorld().getMobs()[index];
		if (mob == null || mob.getFields().isDead())
			return;
		final DestinationListener reachDestination = new DestinationListener() {
			@Override
			public void reachDestination() {
				player.setEntityInteraction(mob);
				mob.setPositionToFace(player.getPosition());
				switch (mob.getId()) {
				case 461:
					ShopManager.getShops().get(2).open(player); //magic
					break;
				case 559:
					ShopManager.getShops().get(2).open(player); //brian
					break;
				default:
					logger.info("Unhandled mob second click: " + mob.getId());
					break;
				}
			}
			
		};
		GameAction<Player> action = new MobClickGameAction<Player>(player, mob, reachDestination);
		player.setAction(action);
	}

	/**
	 * The constants for the mob interaction 
	 * {@link org.niobe.net.packet.GamePacket}s.
	 */
	public static final int ATTACK_MOB_OPCODE = 72, FIRST_CLICK_OPCODE = 155,
							SECOND_CLICK_OPCODE = 17;
}
