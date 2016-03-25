package org.niobe.net.packet;

import org.niobe.net.packet.event.*;
import org.niobe.world.Player;

/**
 * Used for incoming packets received from a player
 * client.
 *
 * @author relex lawl
 */
public final class GamePacketManager {
		
	/**
	 * The array containing all packets that can be sent
	 * from a player's client.
	 */
	private static final GamePacketEvent[] packets = new GamePacketEvent[256];
	
	/**
	 * Receives the packet from a player's client and passes
	 * onto the respective packet's read method.
	 * @param player	The player setting off the packet.
	 * @param packet	The packet received from the player.
	 */
	public static void parse(Player player, GamePacket packet) {
		GamePacketEvent event = packets[packet.getOpcode()];
		if (event == null) {
			event = new DefaultGamePacket();
		}
		event.read(player, packet);
	}
	
	/**
	 * Populates the {@code packets} array with client to
	 * server game packets.
	 */
	static {
		packets[0] = packets[3] = packets[77] = packets[226] =
				packets[121] = packets[86] = packets[241] = packets[78] = new SilencedGamePacket();
		
		packets[4] = new ChatMessageGamePacket();
		
		packets[41] = new EquipItemGamePacket();
		
		packets[49] = new ScreenSizeGamePacket();
		
		packets[80] = new ClanChatMessageGamePacket();
		
		packets[87] = new DropItemGamePacket();
		
		packets[101] = new ConfirmAppearanceGamePacket();
		
		packets[103] = new CommandGamePacket();
		
		packets[130] = new CloseInterfaceGamePacket();
		
		packets[185] = new ButtonClickGamePacket();
		
		packets[202] = new IdleConnectionGamePacket();	
			
		packets[214] = new SwapItemSlotGamePacket();
		
		packets[236] = new GroundItemActionGamePacket();
		
		packets[DialogueActionGamePacket.CONTINUE_DIALOGUE_OPCODE] = new DialogueActionGamePacket();
		
		packets[NoteTabActionGamePacket.ADD_NOTE] = packets[NoteTabActionGamePacket.DELETE_ALL_NOTES] = new NoteTabActionGamePacket();
		
		packets[TradeAcceptanceGamePacket.CHATBOX_TRADE_OPCODE] = packets[TradeAcceptanceGamePacket.TRADE_OPCODE] = new TradeAcceptanceGamePacket();
		
		packets[RegionChangeGamePacket.ENTER_NEW_REGION] = packets[RegionChangeGamePacket.FINALIZE_REGION_LOAD] = new RegionChangeGamePacket();
		
		packets[PlayerActionGamePacket.ATTACK_PLAYER] = packets[PlayerActionGamePacket.FOLLOW_PLAYER] =
				packets[PlayerActionGamePacket.OPTION_1] = packets[PlayerActionGamePacket.TRADE_PLAYER] = new PlayerActionGamePacket();		

		packets[MobActionGamePacket.ATTACK_MOB_OPCODE] = packets[MobActionGamePacket.FIRST_CLICK_OPCODE] = 
				packets[MobActionGamePacket.SECOND_CLICK_OPCODE] = new MobActionGamePacket();
		
		packets[MovementGamePacket.COMMAND_MOVEMENT_OPCODE] = packets[MovementGamePacket.GAME_MOVEMENT_OPCODE] =
				packets[MovementGamePacket.MINIMAP_MOVEMENT_OPCODE] = new MovementGamePacket();
		
		packets[EnterInputGamePacket.ENTER_AMOUNT_OPCODE] = packets[EnterInputGamePacket.ENTER_CLAN_CHAT_OPCODE] = 
				packets[EnterInputGamePacket.SET_CLAN_CHAT_NAME_OPCODE] = new EnterInputGamePacket();
		
		packets[PlayerRelationGamePacket.ADD_FRIEND_OPCODE] = packets[PlayerRelationGamePacket.ADD_IGNORE_OPCODE] = 
				packets[PlayerRelationGamePacket.REMOVE_FRIEND_OPCODE] = packets[PlayerRelationGamePacket.REMOVE_IGNORE_OPCODE] = 
				packets[PlayerRelationGamePacket.SEND_MESSAGE_OPCODE] = new PlayerRelationGamePacket();
				
		packets[ItemOnEntityGamePacket.ITEM_ON_GROUND_ITEM] = packets[ItemOnEntityGamePacket.ITEM_ON_ITEM] = 
				packets[ItemOnEntityGamePacket.ITEM_ON_MOB] = packets[ItemOnEntityGamePacket.ITEM_ON_OBJECT] =
				packets[ItemOnEntityGamePacket.ITEM_ON_PLAYER] = new ItemOnEntityGamePacket();
		
		packets[GameObjectActionGamePacket.FIRST_CLICK] = packets[GameObjectActionGamePacket.SECOND_CLICK] =
				packets[GameObjectActionGamePacket.THIRD_CLICK] = packets[GameObjectActionGamePacket.FOURTH_CLICK] =
				packets[GameObjectActionGamePacket.FIFTH_CLICK] = new GameObjectActionGamePacket();
		
		packets[ItemContainerActionGamePacket.FIRST_ITEM_ACTION_OPCODE] = packets[ItemContainerActionGamePacket.SECOND_ITEM_ACTION_OPCODE] =
				packets[ItemContainerActionGamePacket.THIRD_ITEM_ACTION_OPCODE] = packets[ItemContainerActionGamePacket.FOURTH_ITEM_ACTION_OPCODE] =
				packets[ItemContainerActionGamePacket.FIFTH_ITEM_ACTION_OPCODE] = packets[ItemContainerActionGamePacket.FIRST_USE_ITEM_ACTION] =
				packets[ItemContainerActionGamePacket.SECOND_USE_ITEM_ACTION] = new ItemContainerActionGamePacket();
	}
}
