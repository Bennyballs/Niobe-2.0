package org.niobe.world.region;

import java.util.HashMap;
import java.util.Map;

import org.niobe.world.*;
import org.niobe.model.Entity;
import org.niobe.model.Position;
import org.niobe.world.region.Region.RegionPosition;

/**
 * This class handles all the {@link Region}s in the
 * world.
 *
 * @author relex lawl
 */
public final class RegionManager {

	/**
	 * This map contains all the active {@link Region}s
	 * in the world.
	 */
	private static final Map<RegionPosition, Region> regions = new HashMap<RegionPosition, Region>();
		
	/**
	 * Registers an {@link org.niobe.model.Entity} to their
	 * corresponding {@link Region}.
	 * @param entity	The {@link org.niobe.model.Entity} to register.
	 * @return			The {@link Region} the entity is being registered to.
	 */
	public static void register(Entity entity) {
		if (entity.getRegion() != null) {
			unregister(entity);
		}
		Region region = forPosition(entity.getPosition());
		entity.setRegion(region);
		if (entity.isPlayer()) {
			Player player = (Player) entity;
			region.getPlayers().add(player);
			
			for (GameObject gameObject : region.getGameObjects()) {
				player.getPacketSender().sendGameObject(gameObject);
			}
			for (GroundItem groundItem : region.getGroundItem()) {
				if (groundItem.getOwner() != null && !groundItem.getOwnerName().equals(player.getCredentials().getUsername())) {
					continue;
				}
				player.getPacketSender().sendGroundItem(groundItem);
			}
		} else if (entity.isMob()) {
			Mob mob = (Mob) entity;
			region.getMobs().add(mob);
		} else if (entity.isGameObject()) {
			GameObject gameObject = (GameObject) entity;
			region.getGameObjects().add(gameObject);
		} else if (entity.isGroundItem()) {
			GroundItem groundItem = (GroundItem) entity;
			region.getGroundItem().add(groundItem);
		} else if (entity.isProjectile()) {
			Projectile projectile = (Projectile) entity;
			region.getProjectiles().add(projectile);
		}
	}
	
	/**
	 * Unregisters the {@link org.niobe.model.Entity} from their
	 * current {@link Region}.
	 * @param entity	The {@link org.niobe.model.Entity} to unregister.
	 */
	public static void unregister(Entity entity) {
		Region region = entity.getRegion();
		if (entity.isPlayer()) {
			Player player = (Player) entity;
			for (GameObject gameObject : region.getGameObjects()) {
				player.getPacketSender().sendGameObjectRemoval(gameObject);
			}
			for (GroundItem groundItem : region.getGroundItem()) {
				player.getPacketSender().sendGroundItemRemoval(groundItem);
			}
			region.getPlayers().remove(player);
		} else if (entity.isMob()) {
			Mob mob = (Mob) entity;
			region.getMobs().remove(mob);
		} else if (entity.isGameObject()) {
			GameObject gameObject = (GameObject) entity;
			region.getGameObjects().remove(gameObject);
		} else if (entity.isGroundItem()) {
			GroundItem groundItem = (GroundItem) entity;
			region.getGroundItem().remove(groundItem);
		} else if (entity.isProjectile()) {
			Projectile projectile = (Projectile) entity;
			region.getProjectiles().remove(projectile);
		}
	}
	
	/**
	 * Gets the corresponding {@link Region} for said
	 * position and checks if the {@link #regions} map contains
	 * the {@link Region} to avoid making new instances.
	 * @param position	The {@link org.niobe.model.Position} to get {@link Region} for.
	 * @return			The corresponding {@link Region}.
	 */
	public static Region forPosition(Position position) {
		RegionPosition regionPosition = new RegionPosition(position.getX(), position.getY(), position.getZ());
		Region region = regions.get(regionPosition);
		if (region != null) {
			return region;
		}
		region = new Region(regionPosition);
		regions.put(regionPosition, region);
		return region;
	}
}
