package org.niobe.world.update.mob;

import java.util.Iterator;

import org.niobe.GameServer;
import org.niobe.model.Entity;
import org.niobe.model.MovementQueue;
import org.niobe.model.Position;
import org.niobe.model.UpdateFlag;
import org.niobe.model.UpdateFlag.Flag;
import org.niobe.net.packet.GamePacket.PacketType;
import org.niobe.net.packet.GamePacketBuilder;
import org.niobe.net.packet.GamePacketBuilder.AccessType;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.update.AbstractGameUpdate;

/**
 * An implementation of {@link org.niobe.world.update.AbstractGameUpdate}
 * that handles all of the pre-mob updating procedures.
 *
 * @author relex lawl
 */
public final class MobGameUpdate extends AbstractGameUpdate {

	/**
	 * The PrePlayerUpdating constructor.
	 * @param player	The player to update mobs for.
	 */
	public MobGameUpdate(Player player) {
		this.player = player;
	}
	
	/**
	 * The associated {@link org.niobe.world.Player}
	 * to update local {@link org.niobe.world.Mob}s for.
	 */
	private final Player player;
	
	@Override
	public void run() {
		GamePacketBuilder update = new GamePacketBuilder();
		GamePacketBuilder packet = new GamePacketBuilder(65, PacketType.SHORT);
		packet.initializeAccess(AccessType.BIT);
		packet.writeBits(8, player.getFields().getLocalMobs().size());
		for (Iterator<Mob> mobIterator = player.getFields().getLocalMobs().iterator(); mobIterator.hasNext();) {
			Mob mob = mobIterator.next();
			if (GameServer.getWorld().getMobs()[mob.getIndex()] != null && mob.isVisible() && player.getPosition().isWithinDistance(mob.getPosition()) && !mob.getFields().isTeleporting()) {
				updateMovement(mob, packet);
				if (mob.getUpdateFlag().isUpdateRequired()) {
					appendUpdates(mob, update);
				}
			} else {
				mobIterator.remove();
				packet.writeBits(1, 1);
				packet.writeBits(2, 3);
			}
		}
		for (Mob mob : GameServer.getWorld().getMobs()) {
			if (player.getRegion().getMobs().size() >= 255) {
				break;
			}
			if (mob == null || player.getFields().getLocalMobs().contains(mob) || !mob.isVisible()) {
				continue;
			}
			if (mob.getPosition().isWithinDistance(player.getPosition())) {
				player.getFields().getLocalMobs().add(mob);
				addMob(mob, packet);
				if (mob.getUpdateFlag().isUpdateRequired()) {
					appendUpdates(mob, update);
				}
			}
		}
		if (update.toPacket().getBuffer().writerIndex() > 0) {
			packet.writeBits(14, 16383);
			packet.initializeAccess(AccessType.BYTE);
			packet.writeBuffer(update.toPacket().getBuffer());
		} else {
			packet.initializeAccess(AccessType.BYTE);
		}
		player.write(packet.toPacket());
	}

	/**
	 * Adds an mob to the associated player's client.
	 * @param mob		The mob to add.
	 * @param builder	The packet builder to write information on.
	 * @return			The void instance.
	 */
	private void addMob(Mob mob, GamePacketBuilder builder) {
		builder.writeBits(14, mob.getIndex());
		builder.writeBits(5, mob.getPosition().getY() - player.getPosition().getY());
		builder.writeBits(5, mob.getPosition().getX() - player.getPosition().getX());
		builder.writeBits(1, 0);
		builder.writeBits(16, mob.getId());
		builder.writeBits(1, mob.getUpdateFlag().isUpdateRequired() ? 1 : 0);
	}
	
	/**
	 * Updates the mob's movement queue.
	 * @param mob		The mob who's movement is updated.
	 * @param builder	The packet builder to write information on.
	 * @return			The void instance.
	 */
	private void updateMovement(Mob mob, GamePacketBuilder builder) {
		if (mob.getMovementQueue().getDirections()[MovementQueue.RUNNING_DIRECTION] == -1) {
			if (mob.getMovementQueue().getDirections()[MovementQueue.WALKING_DIRECTION] == -1) {
				if (mob.getUpdateFlag().isUpdateRequired()) {
					builder.writeBits(1, 1);
					builder.writeBits(2, 0);
				} else {
					builder.writeBits(1, 0);
				}
			} else {
				builder.writeBits(1, 1);
				builder.writeBits(2, 1);
				builder.writeBits(3, mob.getMovementQueue().getDirections()[MovementQueue.WALKING_DIRECTION]);
				builder.writeBits(1, mob.getUpdateFlag().isUpdateRequired() ? 1 : 0);
			}
		} else {
			builder.writeBits(1, 1);
			builder.writeBits(2, 2);
			builder.writeBits(3, mob.getMovementQueue().getDirections()[MovementQueue.WALKING_DIRECTION]);
			builder.writeBits(3, mob.getMovementQueue().getDirections()[MovementQueue.RUNNING_DIRECTION]);
			builder.writeBits(1, mob.getUpdateFlag().isUpdateRequired() ? 1 : 0);
		}
	}
	
	/**
	 * Appends a mask update for {@code mob}.
	 * @param mob		The mob to update masks for.
	 * @param builder	The packet builder to write information on.
	 * @return			The void instance.
	 */
	private void appendUpdates(Mob mob, GamePacketBuilder builder) {
		int mask = 0;
		UpdateFlag flag = mob.getUpdateFlag();
		if (flag.flagged(Flag.ANIMATION) && mob.getAnimation() != null) {
			mask |= 0x10;
		}
		if (flag.flagged(Flag.GRAPHIC) && mob.getGraphic() != null) {
			mask |= 0x80;
		}
		if (flag.flagged(Flag.SINGLE_HIT) && mob.getDamage().getHits().length >= 1) {
			mask |= 0x8;
		}
		if (flag.flagged(Flag.ENTITY_INTERACTION)) {
			mask |= 0x20;
		}
		if (flag.flagged(Flag.FORCED_CHAT) && mob.getFields().getForcedChat().length() > 0) {
			mask |= 0x1;
		}
		if (flag.flagged(Flag.DOUBLE_HIT) && mob.getDamage().getHits().length >= 2) {
			mask |= 0x40;
		}
		if (flag.flagged(Flag.TRANSFORM) && mob.getTransformationId() != -1) {
			mask |= 0x2;
		}
		if (flag.flagged(Flag.FACE_POSITION) && mob.getPositionToFace() != null) {
			mask |= 0x4;
		}
		builder.writeByte(mask);
		if (flag.flagged(Flag.ANIMATION) && mob.getAnimation() != null) {
			updateAnimation(builder, mob);
		}
		if (flag.flagged(Flag.SINGLE_HIT) && mob.getDamage().getHits().length >= 1) {
			updateSingleHit(builder, mob);
		}
		if (flag.flagged(Flag.GRAPHIC) && mob.getGraphic() != null) {
			updateGraphics(builder, mob);
		}
		if (flag.flagged(Flag.ENTITY_INTERACTION)) {
			Entity entity = mob.getInteractingEntity();
			builder.writeShort(entity == null ? -1 : entity.getIndex() + (entity instanceof Player ? 32768 : 0));
		}
		if (flag.flagged(Flag.FORCED_CHAT) && mob.getFields().getForcedChat().length() > 0) {
			builder.writeString(mob.getFields().getForcedChat());
		}
		if (flag.flagged(Flag.DOUBLE_HIT) && mob.getDamage().getHits().length >= 2) {
			updateDoubleHit(builder, mob);
		}
		if (flag.flagged(Flag.TRANSFORM) && mob.getTransformationId() != -1) {
			builder.writeLEShortA(mob.getTransformationId());
		}
		if (flag.flagged(Flag.FACE_POSITION) && mob.getPositionToFace() != null) {
			final Position position = mob.getPositionToFace();
			int x = position == null ? 0 : position.getX(); 
			int y = position == null ? 0 : position.getY();
			builder.writeLEShort(x * 2 + 1);
			builder.writeLEShort(y * 2 + 1);
		}
	}
	
	/**
	 * Updates {@code mob}'s current animation and displays it for all local players.
	 * @param builder	The packet builder to write information on.
	 * @param mob		The mob to update animation for.
	 * @return			The void instance.
	 */
	private void updateAnimation(GamePacketBuilder builder, Mob mob) {
		builder.writeLEShort(mob.getAnimation().getId());
		builder.writeByte(mob.getAnimation().getDelay());
	}
	
	/**
	 * Updates {@code mob}'s current graphics and displays it for all local players.
	 * @param builder	The packet builder to write information on.
	 * @param mob		The mob to update graphics for.
	 * @return			The void instance.
	 */
	private void updateGraphics(GamePacketBuilder builder, Mob mob) {
		builder.writeShort(mob.getGraphic().getId());
		builder.writeInt(((mob.getGraphic().getHeight().ordinal() * 50) << 16) + (mob.getGraphic().getDelay() & 0xffff));
	}
	
	/**
	 * Updates the mob's single hit.
	 * @param builder	The packet builder to write information on.
	 * @param mob		The mob to update the single hit for.
	 * @return			The void instance.
	 */
	private void updateSingleHit(GamePacketBuilder builder, Mob mob) {
		builder.writeLEShortA(mob.getDamage().getHits()[0].getDamage());
		builder.writeLEShortA(mob.getDamage().getHits()[0].getAbsorption());
		builder.writeByte(mob.getDamage().getHits()[0].getCombatIcon().getId());
		builder.writeString("");
		builder.writeByte(mob.getDamage().getHits()[0].getHitmask().ordinal());
		builder.writeLEShortA(mob.getConstitution());
		builder.writeLEShortA(mob.getDefinition().getLifePoints());
	}
	
	/**
	 * Updates the mob's double hit.
	 * @param builder	The packet builder to write information on.
	 * @param mob		The mob to update the double hit for.
	 * @return			The void instance.
	 */
	private void updateDoubleHit(GamePacketBuilder builder, Mob mob) {
		builder.writeShort(mob.getDamage().getHits()[1].getDamage());
		builder.writeShort(mob.getDamage().getHits()[1].getAbsorption());
		builder.writeByte(mob.getDamage().getHits()[1].getCombatIcon().getId());
		builder.writeString("");
		builder.writeByte(mob.getDamage().getHits()[1].getHitmask().ordinal());
		builder.writeShort(mob.getConstitution());
		builder.writeShort(mob.getDefinition().getLifePoints());
	}
}
