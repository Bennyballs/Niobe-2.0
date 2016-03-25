package org.niobe.net.packet.event;

import java.util.logging.Logger;

import org.niobe.GameServer;
import org.niobe.model.Item;
import org.niobe.model.Position;
import org.niobe.model.action.DestinationListener;
import org.niobe.model.action.impl.MobClickGameAction;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.world.GameObject;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.content.dialogue.Dialogue;
import org.niobe.world.content.dialogue.DialogueManager;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * that is used when an {@link org.niobe.model.Item} is "used" on another
 * {@link org.niobe.model.Entity}.
 *
 * @author relex lawl
 */
public final class ItemOnEntityGamePacket implements GamePacketEvent {

	/**
	 * The {@link ItemOnEntityGamePacket} logger to debug information and print out errors.
	 */
	private final static Logger logger = Logger.getLogger(ItemOnEntityGamePacket.class.getName());

	/**
	 * Used when the {@link org.niobe.model.Item} is used on another
	 * {@link org.niobe.model.Item}.
	 * @param player	The {@link org.niobe.world.Player} using the {@link org.niobe.model.Item}.
	 * @param packet	The {@link org.niobe.net.packet.GamePacket} received from the client.
	 */
	private static void itemOnItem(Player player, GamePacket packet) {
		int usedWithSlot = packet.readUnsignedShort();
		int itemUsedSlot = packet.readUnsignedShortA();
		Item usedWith = player.getInventory().getItems()[usedWithSlot];
		Item itemUsedWith = player.getInventory().getItems()[itemUsedSlot];
		logger.info("Unhandled item on item - Item Used: " + itemUsedWith.getId() + " Slot: " + itemUsedSlot + " Used With: " + usedWith.getId() + " Slot: " + usedWithSlot);
	}

	/**
	 * Used when the {@link org.niobe.model.Item} is used on an
	 * {@link org.niobe.world.GameObject}.
	 * @param player	The {@link org.niobe.world.Player} using the {@link org.niobe.model.Item}.
	 * @param packet	The {@link org.niobe.net.packet.GamePacket} received from the client.
	 */
	private static void itemOnObject(Player player, GamePacket packet) {
		@SuppressWarnings("unused")
		int interfaceType = packet.readShort();
		int objectId = packet.readLEShort();
		int objectY = packet.readLEShortA();
		int itemSlot = packet.readLEShort();
		int objectX = packet.readLEShortA();
		int itemId = packet.readShort();
		Item item = player.getInventory().getItems()[itemSlot];
		if (item == null)
			return;
		GameObject gameObject = new GameObject(objectId, new Position(objectX, objectY, player.getPosition().getZ()));
		switch (gameObject.getDefinition().getId()) {
		default:
			logger.info("Unhandled item on object - [item used, object used on] : [" + itemId + ", " + gameObject.getDefinition().getId() + "]");
			break;
		}
	}
	
	/**
	 * Used when the {@link org.niobe.model.Item} is used on an
	 * {@link org.niobe.world.Mob}.
	 * @param player	The {@link org.niobe.world.Player} using the {@link org.niobe.model.Item}.
	 * @param packet	The {@link org.niobe.net.packet.GamePacket} received from the client.
	 */
	private static void itemOnMob(final Player player, GamePacket packet) {
		int id = packet.readShortA();
		int index = packet.readShortA();
		final int slot = packet.readLEShort();
		final Item item = player.getInventory().getItems()[slot];
		final Mob mob = GameServer.getWorld().getMobs()[index];
		if (item.getId() != id)
			return;
		player.setAction(new MobClickGameAction<Player>(player, mob, new DestinationListener() {
			@Override
			public void reachDestination() {
				mob.setPositionToFace(player.getPosition());
				switch (mob.getDefinition().getId()) {
				case 1:
					if (item.getId() == 995) {
						Dialogue dialogue = new Dialogue() {
							
							@Override
							public Dialogue getNextDialogue() {
								return new Dialogue() {
									@Override
									public DialogueType getType() {
										return DialogueType.MOB_STATEMENT;
									}

									@Override
									public DialogueExpression getAnimation() {
										return DialogueExpression.GOOFY_LAUGH;
									}

									@Override
									public String[] getDialogues() {
										return new String[] {
											"Well I am! So I'll take it!"
										};
									}
									
									@Override
									public int getMobId() {
										return mob.getDefinition().getId();
									}
									
									@Override
									public void specialAction() {
										player.getInventory().delete(new Item(995, 1), slot);
									}
								};
							}
							
							@Override
							public DialogueType getType() {
								return DialogueType.MOB_STATEMENT;
							}

							@Override
							public DialogueExpression getAnimation() {
								return DialogueExpression.ANGRY;
							}

							@Override
							public String[] getDialogues() {
								return new String[] {
									"Oh...You think I'm one of *those* guys...",
								};
							}
							
							@Override
							public int getMobId() {
								return mob.getId();
							}
						};
						DialogueManager.start(player, dialogue);
					}
					break;
					
					default:
						logger.info("Unhandled item on mob - [itemId, mobId] : [" + item.getId() + ", " + mob.getId() + "]");
						break;
				}
			}
		}));
	}
	
	/**
	 * Used when the {@link org.niobe.model.Item} is used on an
	 * {@link org.niobe.world.Player}.
	 * @param player	The {@link org.niobe.world.Player} using the {@link org.niobe.model.Item}.
	 * @param packet	The {@link org.niobe.net.packet.GamePacket} received from the client.
	 */
	private static void itemOnPlayer(Player player, GamePacket packet) {
		int interfaceId = packet.readUnsignedShortA();
		int targetIndex = packet.readUnsignedShort();
		int itemId = packet.readUnsignedShort();
		int slot = packet.readLEShort();
		switch (itemId) {
		default:
			logger.info("Unhandled item on player item action - [itemId, interfaceId, player index, inventory slot] : [" + itemId + ", " + interfaceId + ", " + targetIndex + ", " + slot + "].");
			break;
		}
	}
	
	@Override
	public void read(Player player, GamePacket packet) {
		if (player.getFields().isDead())
			return;
		switch (packet.getOpcode()) {
		case ITEM_ON_ITEM:
			itemOnItem(player, packet);
			break;
		case ITEM_ON_OBJECT:
			itemOnObject(player, packet);
			break;
		case ITEM_ON_GROUND_ITEM:
			//TODO
			break;
		case ITEM_ON_MOB:
			itemOnMob(player, packet);
			break;
		case ITEM_ON_PLAYER:
			itemOnPlayer(player, packet);
			break;
		}
	}
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode used
	 * when the {@link org.niobe.model.Item} is used on a {@link org.niobe.world.Mob}.
	 */
	public final static int ITEM_ON_MOB = 57;
		
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode used
	 * when the {@link org.niobe.model.Item} is used on an {@link org.niobe.model.Item}.
	 */
	public final static int ITEM_ON_ITEM = 53;

	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode used
	 * when the {@link org.niobe.model.Item} is used on a {@link org.niobe.world.GameObject}.
	 */
	public final static int ITEM_ON_OBJECT = 192;
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode used
	 * when the {@link org.niobe.model.Item} is used on a {@link org.niobe.world.GroundItem}.
	 */
	public final static int ITEM_ON_GROUND_ITEM = 25;
	
	/**
	 * The {@link org.niobe.net.packet.GamePacket} opcode used
	 * when the {@link org.niobe.model.Item} is used on a {@link org.niobe.world.Player}.
	 */
	public static final int ITEM_ON_PLAYER = 14;
}
