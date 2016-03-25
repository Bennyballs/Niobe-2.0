package org.niobe.world.region;

import java.util.LinkedList;
import java.util.List;

import org.niobe.world.GameObject;
import org.niobe.world.GroundItem;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.Projectile;

/**
 * Represents a "big" region in the world, which is made
 * up of the surrounding regions.
 *
 * @author relex lawl
 */
public final class Region {
	
	/**
	 * The Region constructor.
	 * @param position	The {@link RegionPosition} for this {@link Region}.
	 */
	public Region(RegionPosition position) {
		this.position = position;
		int index = 0;
		int regionX = this.position.x + 6;
		int regionY = this.position.y + 6;
		for (int x = (regionX - 6) / 8; x <= (regionX + 6) / 8; x++) {
			for (int y = (regionY - 6) / 8; y <= (regionY + 6) / 8; y++)
				index++;
		}
		boolean regionChange = (regionX / 8 == 48 || regionX / 8 == 49) && regionY / 8 == 48;
		if (regionX / 8 == 48 && regionY / 8 == 148)
			regionChange = true;
		mapLocation = new int[index];
		floorMap = new int[index];
		objectMap = new int[index];
		index = 0;
		for (int x = (regionX - 6) / 8; x <= (regionX + 6) / 8; x++) {
			for (int y = (regionY - 6) / 8; y <= (regionY + 6) / 8; y++) {
				mapLocation[index] = (x << 8) + y;
				if (regionChange && (y == 49 || y == 149 || y == 147 || x == 50 || x == 49
								&& y == 47)) {
					floorMap[index] = -1;
					objectMap[index] = -1;
					index++;
				} else {
					//TODO clipping here
					/*int floors = floorMap[index] = resourceProvider
							.getClipping(0, y, x);
					if (floors != -1)
						resourceProvider.loadMandatory(3, floors);
					int objects = objectMap[index] = resourceProvider
							.getClipping(1, y, x);
					if (objects != -1)
						resourceProvider.loadMandatory(3, objects);
					for (int j = 0; j < objectMap.length; j++) {
						if (objectMap[j] > 0) {
							System.out.println("objectMap[" + j + "]: " + objectMap[j]);
						}
					}*/
					//System.out.println("floors=" + floors + "; objects=" + objects);
					index++;
				}
			}
		}
	}
	
	/**
	 * The corresponding {@link RegionPosition}.
	 */
	private final RegionPosition position;
	
	/**
	 * TODO figure out what these arrays are exactly, 
	 * grabbed from client, has to do with clipping.
	 */
	
	/**
	 * The map locations.
	 */
	private int[] mapLocation;
	
	/**
	 * The floor map.
	 */
	private int[] floorMap;
	
	/**
	 * The object map.
	 */
	private int[] objectMap;

	/**
	 * This list contains local {@link org.niobe.world.Mob}s
	 * in this {@link Region}.
	 */
	private final List<Mob> mobs = new LinkedList<Mob>();
	
	/**
	 * This list contains local {@link org.niobe.world.Player}s
	 * in this {@link Region}.
	 */
	private final List<Player> players = new LinkedList<Player>();

	/**
	 * This list contains local {@link org.niobe.world.GroundItem}s
	 * in this {@link Region}.
	 */
	private final List<GroundItem> groundItem = new LinkedList<GroundItem>();
	
	/**
	 * This list contains local {@link org.niobe.world.GameObject}s
	 * in this {@link Region}.
	 */
	private final List<GameObject> gameObjects = new LinkedList<GameObject>();
	
	/**
	 * This list contains local {@link org.niobe.world.Projectile}s
	 * in this {@link Region}.
	 */
	private final List<Projectile> projectiles = new LinkedList<Projectile>();
	
	/**
	 * Gets the associated {@link RegionPosition}.
	 * @return	The regionPosition.
	 */
	public RegionPosition getPosition() {
		return position;
	}
		
	/**
	 * Gets the {@link Region}'s map location.
	 * @return	The {@link #mapLocation} array.
	 */
	public int[] getMapLocation() {
		return mapLocation;
	}

	/**
	 * Gets the {@link Region}'s floor map.
	 * @return	The {@link #floorMap} array.
	 */
	public int[] getFloorMap() {
		return floorMap;
	}

	/**
	 * Gets the {@link Region}'s object map.
	 * @return	The {@link #objectMap} array.
	 */
	public int[] getObjectMap() {
		return objectMap;
	}
	
	/**
	 * Gets the list of {@link org.niobe.world.Mob}s in
	 * this region.
	 * @return the mobs
	 */
	public List<Mob> getMobs() {
		return mobs;
	}
	
	/**
	 * Gets the list of {@link org.niobe.world.Player}s in
	 * this region.
	 * @return the players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Gets the list of {@link org.niobe.world.GroundItem}s in
	 * this region.
	 * @return the groundItem
	 */
	public List<GroundItem> getGroundItem() {
		return groundItem;
	}

	/**
	 * Gets the list of {@link org.niobe.world.GameObject}s in
	 * this region.
	 * @return the gameObjects
	 */
	public List<GameObject> getGameObjects() {
		return gameObjects;
	}

	/**
	 * Gets the list of {@link org.niobe.world.Projectile}s in
	 * this region.
	 * @return the projectiles.
	 */
	public List<Projectile> getProjectiles() {
		return projectiles;
	}
	
	/**
	 * Represents a {@link RegionPosition} which holds
	 * the coordinates, which are then shifted and altered
	 * to have the same value for the surrounding regions
	 * in which {@link org.niobe.model.Entity}s will be stored.
	 *
	 * @author relex lawl
	 */
	public static final class RegionPosition {
		
		/**
		 * The RegionPosition constructor.
		 * @param x	The x position coordinate.
		 * @param y	The y position coordinate.
		 * @param z	The height position.
		 */
		public RegionPosition(int x, int y, int z) {
			this.x = ((x >> 3) - 8) / 16;
			this.y = ((y >> 3)) / 16;
			this.z = z;
		}
		
		/**
		 * The region's x-coordinate, will be the same
		 * for the surrounding regions.
		 */
		private final int x;
		
		/**
		 * The region's y-coordinate, will be the same
		 * for the surrounding regions.
		 */
		private final int y;
		
		/**
		 * The region's height-coordinate, will be the same
		 * for the surrounding regions.
		 */
		private final int z;
		
		@Override
		public int hashCode() {
			int hash = x & y | z;
			return hash;
		}
		
		@Override
		public boolean equals(Object object) {
			if (object.getClass() != getClass()) {
				return false;
			}
			RegionPosition other = (RegionPosition) object;
			return other.x == x && other.y == y && other.z == z;
		}
		
		@Override
		public String toString() {
			return "Region position values: [" + x + "; " + y + "; " + z + "]";
		}
	}
}
