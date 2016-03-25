package org.niobe.world;

import org.niobe.GameServer;
import org.niobe.model.Entity;
import org.niobe.model.Graphic;
import org.niobe.model.Position;

/**
 * Represents a projectile being shot from one position to specified
 * destination.
 * 
 * @author relex lawl
 */
public final class Projectile extends Entity {

	/**
	 * The Projectile constructor.
	 * @param position		The starting projectile position.
	 * @param destination	The projectile's destination.
	 * @param graphic		The graphic of the projectile.
	 * @param angle			The angle the projectile will have.
	 * @param speed			The velocity of the projectile.
	 */
	public Projectile(Position position, Position destination, Graphic graphic, Entity lockon, int delay, int angle, int speed) {
		super(position);
		this.destination = destination;
		this.graphic = graphic;
		this.lockon = lockon;
		this.delay = delay;
		this.angle = angle;
		this.speed = speed;
	}
	
	/**
	 * The Projectile constructor.
	 * @param position		The starting projectile position.
	 * @param destination	The projectile's destination.
	 * @param graphic		The graphic of the projectile.
	 * @param lockon		The entity lock-on for this projectile.
	 */
	public Projectile(Position position, Position destination, Graphic graphic, Entity lockon) {
		this(position, destination, graphic, lockon, 0, 5, 5);
	}
	
	/**
	 * The Projectile constructor.
	 * @param position		The starting projectile position.
	 * @param destination	The projectile's destination.
	 * @param graphic		The graphic of the projectile.
	 */
	public Projectile(Position position, Position destination, Graphic graphic) {
		this(position, destination, graphic, null);
	}
	
	/**
	 * The starting projectile's destination.
	 */
	private final Position destination;
	
	/**
	 * The graphic of the projectile.
	 */
	private final Graphic graphic;
	
	/**
	 * The delay before showing the projectile.
	 */
	private final int delay;
	
	/**
	 * The angle the projectile will have.
	 */
	private final int angle;
	
	/**
	 * The velocity of the projectile.
	 */
	private final int speed;
	
	/**
	 * The lock-on target entity.
	 */
	private final Entity lockon;
	
	/**
	 * Gets the destination/final pinpoint the projectile
	 * will reach.
	 * @return	The destination.
	 */
	public Position getDestination() {
		return destination;
	}
	
	/**
	 * Gets the graphic the projectile will be representing.
	 * @return	The graphic to show as a projectile.
	 */
	public Graphic getGraphic() {
		return graphic;
	}
	
	/**
	 * Gets the angle of the projectile.
	 * @return	The projectile's shown angle.
	 */
	public int getAngle() {
		return angle;
	}
	
	/**
	 * Gets the speed of the projectile.
	 * @return	The velocity of the projectile.
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Gets the delay of the projectile.
	 * @return	The projectile's display delay.
	 */
	public int getDelay() {
		return delay;
	}
	
	/**
	 * Gets the lock-on index entity.
	 * @return	The {@link #lockon} value.
	 */
	public Entity getLockon() {
		return lockon;
	}

	@Override
	public int getFreeIndex() {
		for (int i = 0; i < GameServer.getWorld().getProjectiles().length; i++) {
			if (GameServer.getWorld().getProjectiles()[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean isProjectile() {
		return true;
	}

	@Override
	public int getSize() {
		return 1;
	}
}
