package org.niobe.model;

import org.niobe.world.region.Region;

/**
 * Represents an entity in the world such as
 * players, mobs, ground items, game objects and projectiles.
 *
 * @author relex lawl
 */
public abstract class Entity {

	/**
	 * The Entity constructor.
	 * @param position	The position of the entity in the world.
	 */
	public Entity(Position position) {
		this.position = position.copy();
		this.index = getFreeIndex();
	}
		
	/**
	 * The entity's position in the game.
	 */
	private final Position position;
	
	/**
	 * The entity's {@link org.niobe.world.region.Region}
	 * in the game.
	 */
	private Region region;
	
	/**
	 * The entity's unique index.
	 */
	private final int index;
	
	/**
	 * Gets the entity's current position;
	 * @return	The position.
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Gets the entity's current {@link org.niobe.world.region.Region}
	 * in the world.
	 * @return	The current {@link org.niobe.world.region.Region}.
	 */
	public Region getRegion() {
		return region;
	}
	
	/**
	 * Sets the entity's current {@link org.niobe.world.region.Region}
	 * in the world.
	 * @param region	The new {@link org.niobe.world.region.Region}.
	 */
	public Entity setRegion(Region region) {
		this.region = region;
		return this;
	}
	
	/**
	 * Gets the entity's unique index.
	 * @return	The unique index.
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Performs an animation.
	 * @param animation	The animation to perform.
	 */
	public void performAnimation(Animation animation) {
		
	}
	
	/**
	 * Performs a graphic.
	 * @param graphic	The graphic to perform.
	 */
	public void performGraphic(Graphic graphic) {
		
	}
	
	/**
	 * Checks if entity is a {@link org.niobe.world.Player}.
	 * @return	If {@code true} this entity is a player.
	 */
	public boolean isPlayer() {
		return false;
	}
	
	/**
	 * Checks if entity is a {@link org.niobe.world.Mob}.
	 * @return	If {@code true} this entity is a mob.
	 */
	public boolean isMob() {
		return false;
	}
	
	/**
	 * Checks if entity is a {@link org.niobe.world.GroundItem}.
	 * @return	If {@code true} this entity is a ground item.
	 */
	public boolean isGroundItem() {
		return false;
	}
	
	/**
	 * Checks if entity is a {@link org.niobe.world.GameObject}.
	 * @return	If {@code true} this entity is a game object.
	 */
	public boolean isGameObject() {
		return false;
	}
	
	/**
	 * Checks if entity is a {@link org.niobe.world.Projectile}.
	 * @return	If {@code true} this entity is a projectile.
	 */
	public boolean isProjectile() {
		return false;
	}
	
	/**
	 * Used to get the entity's unique index, depending
	 * on its instance.
	 */
	public abstract int getFreeIndex();
	
	/**
	 * The entity's game tile size.
	 */
	public abstract int getSize();
}
