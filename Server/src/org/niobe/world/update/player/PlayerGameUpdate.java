package org.niobe.world.update.player;

import java.util.Iterator;

import org.niobe.GameServer;
import org.niobe.model.Appearance;
import org.niobe.model.ChatMessage.Message;
import org.niobe.model.Entity;
import org.niobe.model.MovementQueue;
import org.niobe.model.Position;
import org.niobe.model.SkillManager;
import org.niobe.model.UpdateFlag;
import org.niobe.model.Appearance.Gender;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.UpdateFlag.Flag;
import org.niobe.model.container.impl.Equipment;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.model.weapon.Weapon;
import org.niobe.model.weapon.WeaponAnimation;
import org.niobe.model.weapon.WeaponLoader;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketBuilder;
import org.niobe.net.packet.GamePacket.PacketType;
import org.niobe.net.packet.GamePacketBuilder.AccessType;
import org.niobe.util.NameUtil;
import org.niobe.world.Player;
import org.niobe.world.update.AbstractGameUpdate;

/**
 * An implementation of {@link org.niobe.world.update.AbstractGameUpdater}
 * where new players can be added and player segment blocks are updated.
 *
 * @author relex lawl
 */
public final class PlayerGameUpdate extends AbstractGameUpdate {
	
	/**
	 * The PlayerGameUpdate constructor.
	 * @param player	The player to update for.
	 */
	public PlayerGameUpdate(Player player) {
		this.player = player;
	}
	
	/**
	 * The associated player to update.
	 */
	private final Player player;

	@Override
	public void run() {
		GamePacketBuilder update = new GamePacketBuilder();
		GamePacketBuilder packet = new GamePacketBuilder(81, PacketType.SHORT);
		packet.initializeAccess(AccessType.BIT);
		updateMovement(packet);
		appendUpdates(update, player, false, true);
		packet.writeBits(8, player.getFields().getLocalPlayers().size());
		for (Iterator<Player> playerIterator = player.getFields().getLocalPlayers().iterator(); playerIterator.hasNext();) {
			Player otherPlayer = playerIterator.next();
			if (GameServer.getWorld().getPlayers()[otherPlayer.getIndex()] != null && otherPlayer.getPosition().isWithinDistance(player.getPosition()) && !otherPlayer.getFields().isTeleporting()) {
				updateOtherPlayerMovement(packet, otherPlayer);
				if (otherPlayer.getUpdateFlag().isUpdateRequired()) {
					appendUpdates(update, otherPlayer, false, false);
				}
			} else {
				playerIterator.remove();
				packet.writeBits(1, 1);
				packet.writeBits(2, 3);
			}
		}
		for (Player otherPlayer : GameServer.getWorld().getPlayers()) {
			if (player.getRegion().getPlayers().size() >= 255) {
				break;
			}
			if (otherPlayer == null || otherPlayer == player || player.getFields().getLocalPlayers().contains(otherPlayer)
					|| !player.getPosition().isWithinDistance(otherPlayer.getPosition())) {
				continue;
			}
			player.getFields().getLocalPlayers().add(otherPlayer);
			addPlayer(otherPlayer, packet);
			appendUpdates(update, otherPlayer, true, false);
		}
		if (update.toPacket().getBuffer().writerIndex() > 0) {
			packet.writeBits(11, 2047);
			packet.initializeAccess(AccessType.BYTE);
			packet.writeBuffer(update.toPacket().getBuffer());
		} else {
			packet.initializeAccess(AccessType.BYTE);
		}
		player.write(packet.toPacket());
	}
	
	/**
	 * Adds a new player to the associated player's client.
	 * @param target	The player to add to the other player's client.
	 * @param builder	The packet builder to write information on.
	 */
	private void addPlayer(Player target, GamePacketBuilder builder) {
		builder.writeBits(11, target.getIndex());
		builder.writeBits(1, 1);
		builder.writeBits(1, 1);
		int yDiff = target.getPosition().getY() - player.getPosition().getY();
		int xDiff = target.getPosition().getX() - player.getPosition().getX();
		builder.writeBits(5, yDiff);
		builder.writeBits(5, xDiff);
	}
	
	/**
	 * Updates the associated player's movement queue.
	 * @param builder	The packet builder to write information on.
	 */
	private void updateMovement(GamePacketBuilder builder) {
		if (player.getFields().isTeleporting() || player.getFields().isChangingRegion()) {
			builder.writeBits(1, 1);
			builder.writeBits(2, 3);
			builder.writeBits(2, player.getPosition().getZ());
			builder.writeBits(1, player.getFields().isTeleporting() ? 1 : 0);			
			builder.writeBits(1, player.getUpdateFlag().isUpdateRequired() ? 1 : 0);
			builder.writeBits(7, player.getPosition().getLocalY(player.getLastKnownRegion()));
			builder.writeBits(7, player.getPosition().getLocalX(player.getLastKnownRegion()));
		} else {
			if (player.getMovementQueue().getDirections()[MovementQueue.WALKING_DIRECTION] == -1) {
				if (player.getUpdateFlag().isUpdateRequired()) {
					builder.writeBits(1, 1);
					builder.writeBits(2, 0);
				} else {
					builder.writeBits(1, 0);
				}
			} else {
				if (player.getMovementQueue().getDirections()[MovementQueue.RUNNING_DIRECTION] == -1) {
					builder.writeBits(1, 1);
					builder.writeBits(2, 1);
					builder.writeBits(3, player.getMovementQueue().getDirections()[MovementQueue.WALKING_DIRECTION]);
					builder.writeBits(1, player.getUpdateFlag().isUpdateRequired() ? 1 : 0);
				} else {
					builder.writeBits(1, 1);
					builder.writeBits(2, 2);
					builder.writeBits(3, player.getMovementQueue().getDirections()[MovementQueue.WALKING_DIRECTION]);
					builder.writeBits(3, player.getMovementQueue().getDirections()[MovementQueue.RUNNING_DIRECTION]);
					builder.writeBits(1, player.getUpdateFlag().isUpdateRequired() ? 1 : 0);
				}
			}
		}
	}
	
	/**
	 * Updates another player's movement queue.
	 * @param builder			The packet builder to write information on.
	 * @param target			The player to update movement for.
	 */
	private void updateOtherPlayerMovement(GamePacketBuilder builder, Player target) {
		if (target.getMovementQueue().getDirections()[MovementQueue.WALKING_DIRECTION] == -1) {
			if (target.getUpdateFlag().isUpdateRequired()) {
				builder.writeBits(1, 1);
				builder.writeBits(2, 0);
			} else {
				builder.writeBits(1, 0);
			}
		} else if (target.getMovementQueue().getDirections()[MovementQueue.RUNNING_DIRECTION] == -1) {
			builder.writeBits(1, 1);
			builder.writeBits(2, 1);
			builder.writeBits(3, target.getMovementQueue().getDirections()[MovementQueue.WALKING_DIRECTION]);
			builder.writeBits(1, target.getUpdateFlag().isUpdateRequired() ? 1 : 0);
		} else {
			builder.writeBits(1, 1);
			builder.writeBits(2, 2);
			builder.writeBits(3, target.getMovementQueue().getDirections()[MovementQueue.WALKING_DIRECTION]);
			builder.writeBits(3, target.getMovementQueue().getDirections()[MovementQueue.RUNNING_DIRECTION]);
			builder.writeBits(1, target.getUpdateFlag().isUpdateRequired() ? 1 : 0);
		}
	}
	
	/**
	 * Appends a player's update mask blocks.
	 * @param builder				The packet builder to write information on.
	 * @param target				The player to update masks for.
	 * @param updateAppearance		Update the player's appearance without the flag being set?
	 * @param noChat				Do not allow player to chat?
	 */
	private void appendUpdates(GamePacketBuilder builder, Player target, boolean updateAppearance, boolean noChat) {
		if (!target.getUpdateFlag().isUpdateRequired() && !updateAppearance)
			return;
		final UpdateFlag flag = target.getUpdateFlag();
		int mask = 0;
		if (flag.flagged(Flag.GRAPHIC) && target.getGraphic() != null) {
			mask |= 0x100;
		}
		if (flag.flagged(Flag.ANIMATION) && target.getAnimation() != null) {
			mask |= 0x8;
		}
		if (flag.flagged(Flag.FORCED_CHAT) && target.getFields().getForcedChat().length() > 0) {
			mask |= 0x4;
		}
		if (flag.flagged(Flag.CHAT) && !noChat && !player.getRelations().getIgnoreList().contains(target.getCredentials().getLongUsername())) {
				mask |= 0x80;
		}
		if (flag.flagged(Flag.ENTITY_INTERACTION)) {
			mask |= 0x1;
		}
		if (flag.flagged(Flag.APPEARANCE) || updateAppearance) {
			mask |= 0x10;
		}
		if (flag.flagged(Flag.FACE_POSITION)) {
			mask |= 0x2;
		}
		if (flag.flagged(Flag.SINGLE_HIT) && target.getDamage().getHits().length >= 1) {
			mask |= 0x20;
		}
		if (flag.flagged(Flag.DOUBLE_HIT) && target.getDamage().getHits().length >= 2) {
			mask |= 0x200;
		}
		/*if (flag.flagged(Flag.FORCED_MOVEMENT) && target.getForceMovement() != null) {
				mask |= 0x400;
			}*/
		if (mask >= 0x100) {
			mask |= 0x40;			
			builder.writeByte((mask & 0xFF));
			builder.writeByte((mask >> 8));
		} else {
			builder.writeByte(mask);
		}
		if (flag.flagged(Flag.GRAPHIC) && target.getGraphic() != null) {
			updateGraphics(builder, target);
		}
		if (flag.flagged(Flag.ANIMATION) && target.getAnimation() != null) {
			updateAnimation(builder, target);
		}
		if (flag.flagged(Flag.FORCED_CHAT) && target.getFields().getForcedChat().length() > 0) {
			updateForcedChat(builder, target);
		}
		if (flag.flagged(Flag.CHAT) && !noChat && !player.getRelations().getIgnoreList().contains(target.getCredentials().getLongUsername())) {
			updateChat(builder, target);
		}
		if (flag.flagged(Flag.ENTITY_INTERACTION)) {
			updateEntityInteraction(builder, target);
		}
		if (flag.flagged(Flag.APPEARANCE) || updateAppearance) {
			updateAppearance(builder, target);
		}
		if (flag.flagged(Flag.FACE_POSITION)) {
			updateFacingPosition(builder, target);
		}
		if (flag.flagged(Flag.SINGLE_HIT) && target.getDamage().getHits().length >= 1) {
			updateSingleHit(builder, target);
		}
		if (flag.flagged(Flag.DOUBLE_HIT) && target.getDamage().getHits().length >= 2) {
			updateDoubleHit(builder, target);
		}
		/*if (flag.flagged(Flag.FORCED_MOVEMENT) && target.getForceMovement() != null) {
				updateForcedMovement(builder, target);
			}*/
	}
	
	/**
	 * This update block is used to update player chat.
	 * @param builder	The packet builder to write information on.
	 * @param target	The player to update chat for.
	 */
	private void updateChat(GamePacketBuilder builder, Player target) {
		Message message = target.getChatMessage().get();
		byte[] bytes = message.getText();
		builder.writeLEShort(((message.getColour() & 0xff) << 8) | (message.getEffects() & 0xff));
		builder.writeByte(target.getRights().ordinal());
		builder.writeByteC(bytes.length);
		builder.writeByteArray(bytes);
	}
	
	/**
	 * This update block is used to update forced player chat. 
	 * @param builder	The packet builder to write information on.
	 * @param target	The player to update forced chat for.
	 * @return			The PlayerUpdating instance.
	 */
	private void updateForcedChat(GamePacketBuilder builder, Player target) {
		builder.writeString(target.getFields().getForcedChat());
	}
	
	/**
	 * This update block is used to update forced player movement.
	 * @param builder	The packet builder to write information on.
	 * @param target	The player to update forced movement for.
	 */
	@SuppressWarnings("unused")
	private void updateForcedMovement(GamePacketBuilder builder, Player target) {
		/*Position position = target.getPosition();
		Position myPosition = player.getLastKnownRegion();
		builder.writeByteC((position.getLocalX(myPosition) + target.getForceMovement()[GameCharacter.FIRST_MOVEMENT_X]));
		builder.writeByteS((position.getLocalY(myPosition) + target.getForceMovement()[GameCharacter.FIRST_MOVEMENT_Y]));
		builder.writeByteS((position.getLocalX(myPosition) + target.getForceMovement()[GameCharacter.SECOND_MOVEMENT_X]));
		builder.writeByteC((position.getLocalX(myPosition) + target.getForceMovement()[GameCharacter.SECOND_MOVEMENT_Y]));
		builder.writeShort(target.getForceMovement()[GameCharacter.MOVEMENT_SPEED]);
		builder.writeShortA(target.getForceMovement()[GameCharacter.MOVEMENT_REVERSE_SPEED]);
		builder.writeByte(target.getForceMovement()[GameCharacter.MOVEMENT_DIRECTION]);*/
	}
	
	/**
	 * This update block is used to update a player's animation.
	 * @param builder	The packet builder to write information on.
	 * @param target	The player to update animations for.
	 * @return			The PlayerUpdating instance.
	 */
	private void updateAnimation(GamePacketBuilder builder, Player target) {
		builder.writeLEShort(target.getAnimation().getId());
		builder.writeByteC(target.getAnimation().getDelay());
	}
	
	/**
	 * This update block is used to update a player's graphics.
	 * @param builder	The packet builder to write information on.
	 * @param target	The player to update graphics for.
	 * @return			The PlayerUpdating instance.
	 */
	private void updateGraphics(GamePacketBuilder builder, Player target) {
		builder.writeLEShort(target.getGraphic().getId());
		builder.writeInt(((target.getGraphic().getHeight().ordinal() * 50) << 16) + (target.getGraphic().getDelay() & 0xffff));
	}
	
	/**
	 * This update block is used to update a player's single hit.
	 * @param builder	The packet builder used to write information on.
	 * @param target	The player to update the single hit for.
	 * @return			The PlayerUpdating instance.
	 */
	private void updateSingleHit(GamePacketBuilder builder, Player target) {
		builder.writeLEShort(target.getDamage().getHits()[0].getDamage());
		builder.writeLEShortA(target.getDamage().getHits()[0].getAbsorption());
		builder.writeByte(target.getDamage().getHits()[0].getCombatIcon().getId());
		builder.writeString(player.getCredentials().getUsername());
		builder.writeString(target.getCredentials().getUsername());
		builder.writeByteS(target.getDamage().getHits()[0].getHitmask().ordinal());
		builder.writeShort(target.getConstitution());
		builder.writeShort(SkillManager.getLevelForExperience(target.getSkillManager().getExperience(Skill.CONSTITUTION)) * 10);
	}
	
	/**
	 * This update block is used to update a player's double hit.
	 * @param builder	The packet builder used to write information on.
	 * @param target	The player to update the double hit for.
	 */
	private void updateDoubleHit(GamePacketBuilder builder, Player target) {
		builder.writeLEShort(target.getDamage().getHits()[1].getDamage());
		builder.writeLEShortA(target.getDamage().getHits()[1].getAbsorption());
		builder.writeByte(target.getDamage().getHits()[1].getCombatIcon().getId());
		builder.writeString(player.getCredentials().getUsername());
		builder.writeString(target.getCredentials().getUsername());
		builder.writeByteS(target.getDamage().getHits()[1].getHitmask().ordinal());
		builder.writeShort(target.getConstitution());
		builder.writeShort(SkillManager.getLevelForExperience(target.getSkillManager().getExperience(Skill.CONSTITUTION)) * 10);
	}
	
	/**
	 * This update block is used to update a player's face position.
	 * @param builder	The packet builder to write information on.
	 * @param target	The player to update face position for.
	 */
	private void updateFacingPosition(GamePacketBuilder builder, Player target) {
		final Position position = target.getPositionToFace();
		int x = position == null ? 0 : position.getX(); 
		int y = position == null ? 0 : position.getY();
		builder.writeLEShortA(x * 2 + 1);
		builder.writeLEShort(y * 2 + 1);
	}
	
	/**
	 * This update block is used to update a player's entity interaction.
	 * @param builder	The packet builder to write information on.
	 * @param target	The player to update entity interaction for.
	 */
	private void updateEntityInteraction(GamePacketBuilder builder, Player target) {
		Entity entity = target.getInteractingEntity();
		if (entity != null) {
			int index = entity.getIndex();
			if (entity.isPlayer())
				index += + 32768;
			builder.writeLEShort(index);
		} else {
			builder.writeLEShort(-1);
		}
	}
	
	/**
	 * This update block is used to update a player's appearance, this includes 
	 * their equipment, clothing, combat level, gender, head icons, user name and animations.
	 * @param builder	The packet builder to write information on.
	 * @param target	The player to update appearance for.
	 */
	private void updateAppearance(GamePacketBuilder builder, Player target) {	
		Appearance appearance = target.getAppearance();
		Equipment equipment = target.getEquipment();
		GamePacketBuilder properties = new GamePacketBuilder();
		properties.writeByte(appearance.getGender().ordinal());
		properties.writeByte(appearance.getHeadHint());
		properties.writeByte(appearance.getSkullHint());
		if (target.getFields().getNpcTransformationId() <= 0) {
			int[] equip = new int[equipment.capacity()];
			for (int i = 0; i < equipment.capacity(); i++) {
				equip[i] = equipment.getItems()[i].getId();
			}
			if (equip[Equipment.HEAD_SLOT] > -1) {
				properties.writeShort(0x200 + equip[Equipment.HEAD_SLOT]);
			} else {
				properties.writeByte(0);
			}
			if (equip[Equipment.CAPE_SLOT] > -1) {
				properties.writeShort(0x200 + equip[Equipment.CAPE_SLOT]);
			} else {
				properties.writeByte(0);
			}
			if (equip[Equipment.AMULET_SLOT] > -1) {
				properties.writeShort(0x200 + equip[Equipment.AMULET_SLOT]);
			} else {
				properties.writeByte(0);
			}
			if (equip[Equipment.WEAPON_SLOT] > -1) {
				properties.writeShort(0x200 + equip[Equipment.WEAPON_SLOT]);
			} else {
				properties.writeByte(0);
			}
			if (equip[Equipment.BODY_SLOT] > -1) {
				properties.writeShort(0x200 + equip[Equipment.BODY_SLOT]);
			} else {
				properties.writeShort(0x100 + appearance.getLook()[Appearance.CHEST]);
			}
			if (equip[Equipment.SHIELD_SLOT] > -1) {
				properties.writeShort(0x200 + equip[Equipment.SHIELD_SLOT]);
			} else {
				properties.writeByte(0);
			}
			if (ItemDefinition.forId(equip[Equipment.BODY_SLOT]).isFullBody()) {
				properties.writeByte(0);
			} else {
				properties.writeShort(0x100 + appearance.getLook()[Appearance.ARMS]);
			}
			if (equip[Equipment.LEG_SLOT] > -1) {
				properties.writeShort(0x200 + equip[Equipment.LEG_SLOT]);
			} else {
				properties.writeShort(0x100 + appearance.getLook()[Appearance.LEGS]);
			}
			if (ItemDefinition.forId(equip[Equipment.HEAD_SLOT]).isFullHelm()) {
				properties.writeByte(0);
			} else {
				properties.writeShort(0x100 + appearance.getLook()[Appearance.HEAD]);
			}
			if (equip[Equipment.HANDS_SLOT] > -1) {
				properties.writeShort(0x200 + equip[Equipment.HANDS_SLOT]);
			} else {
				properties.writeShort(0x100 + appearance.getLook()[Appearance.HANDS]);
			}
			if (equip[Equipment.FEET_SLOT] > -1) {
				properties.writeShort(0x200 + equip[Equipment.FEET_SLOT]);
			} else {
				properties.writeShort(0x100 + appearance.getLook()[Appearance.FEET]);
			}
			if (appearance.getLook()[Appearance.BEARD] <= 0 || appearance.getGender().equals(Gender.FEMALE)) {
				properties.writeByte(0);
			} else {
				properties.writeShort(0x100 + appearance.getLook()[Appearance.BEARD]);
			}
		} else {
			properties.writeShort(-1);
			properties.writeShort(target.getFields().getNpcTransformationId());
		}
		properties.writeByte(appearance.getLook()[Appearance.HAIR_COLOUR]);
		properties.writeByte(appearance.getLook()[Appearance.TORSO_COLOUR]);
		properties.writeByte(appearance.getLook()[Appearance.LEG_COLOUR]);
		properties.writeByte(appearance.getLook()[Appearance.FEET_COLOUR]);
		properties.writeByte(appearance.getLook()[Appearance.SKIN_COLOUR]);
		Weapon weapon = target.getWeapon();
		WeaponAnimation animations = DEFAULT_ANIMATION;
		if (weapon != WeaponLoader.getDefaultWeapon()) {
			animations = weapon.getAnimations();
		} else {
			animations = DEFAULT_ANIMATION;
		}
		properties.writeShort(animations.getStand().getId());
		properties.writeShort(animations.getTurn().getId());
		properties.writeShort(animations.getWalk().getId());
		properties.writeShort(animations.getCompleteTurn().getId());
		properties.writeShort(animations.getLeftTurn().getId());
		properties.writeShort(animations.getRightTurn().getId());
		properties.writeShort(animations.getRun().getId());
		properties.writeLong(NameUtil.stringToLong(target.getCredentials().getUsername()));	
		properties.writeByte(target.getSkillManager().getCombatLevel());
		properties.writeShort(0);
		GamePacket packet = properties.toPacket();
		builder.writeByteC(packet.getBuffer().writerIndex());
		builder.writeBuffer(packet.getBuffer());
	}
	
	/**
	 * The default {@link org.niobe.model.weapon.WeaponAnimation} for players.
	 */
	private static final WeaponAnimation DEFAULT_ANIMATION = new WeaponAnimation(808, 819, 824, 823, 821, 822,820);
}
