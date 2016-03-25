package org.niobe.model;

/**
 * Represents a "position" in the game world,
 * made up of the x, y and z (height) coordinates.
 * 
 * @author relex lawl
 */
public final class Position {
	
	/**
	 * The max level of heights you can make a position have.
	 */
	private static final int MAX_HEIGHT_LEVEL = 4;

	/**
	 * The Position constructor.
	 * @param x		The x-type coordinate of the position.
	 * @param y		The y-type coordinate of the position.
	 * @param z		The height of the position.
	 */
	public Position(int x, int y, int z) {
		if (z > MAX_HEIGHT_LEVEL)
			throw new IllegalArgumentException("If attempting to make custom areas for players, use construct region packet instead.");
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * The Position constructor.
	 * @param x		The x-type coordinate of the position.
	 * @param y		The y-type coordinate of the position.
	 */
	public Position(int x, int y) {
		this(x, y, 0);
	}
	
	/**
	 * The x coordinate of the position.
	 */
	private int x;
	
	/**
	 * Gets the x coordinate of this position.
	 * @return	The associated x coordinate.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Sets the x coordinate of this position.
	 * @return The Position instance.
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * The y coordinate of the position.
	 */
	private int y;
	
	/**
	 * Gets the y coordinate of this position.
	 * @return	The associated y coordinate.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Sets the y coordinate of this position.
	 * @return The Position instance.
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * The height level of the position.
	 */
	private int z;
	
	/**
	 * Gets the height level of this position.
	 * @return	The associated height level.
	 */
	public int getZ() {
		return z;
	}
	
	/**
	 * Sets the height level of this position.
	 * @return The Position instance.
	 */
	public void setZ(int z) {
		this.z = z;
	}
	
	/**
	 * Sets the player's associated Position values.
	 * @param x	The new x coordinate.
	 * @param y	The new y coordinate.
	 * @param z The new height level.
	 */
	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Sets the player's associated Position values.
	 * @param x	The new x coordinate.
	 * @param y	The new y coordinate.
	 */
	public void set(int x, int y) {
		set(x, y, z);
	}
	
	/**
	 * Sets the player's associated Position values.
	 * @param position	The position value to give to this instance.
	 */
	public void set(Position position) {
		set(position.getX(), position.getY(), position.getZ());
	}
	
	/**
	 * Gets the local x coordinate relative to a specific region.
	 * @param position	The region the coordinate will be relative to.
	 * @return 			The local x coordinate.
	 */
	public int getLocalX(Position position) {
		return x - 8 * position.getRegionX();
	}
	
	/**
	 * Gets the local y coordinate relative to a specific region.
	 * @param position 	The region the coordinate will be relative to.
	 * @return 			The local y coordinate.
	 */
	public int getLocalY(Position position) {
		return y - 8 * position.getRegionY();
	}
	
	/**
	 * Gets the local x coordinate relative to a specific region.
	 * @return 			The local x coordinate.
	 */
	public int getLocalX() {
		return x - 8 * getRegionX();
	}
	
	/**
	 * Gets the local y coordinate relative to a specific region.
	 * @return 			The local y coordinate.
	 */
	public int getLocalY() {
		return y - 8 * getRegionY();
	}
	
	/**
	 * Gets the region x coordinate.
	 * @return The region x coordinate.
	 */
	public int getRegionX() {
		return (x >> 3) - 6;
	}
	
	/**
	 * Gets the region y coordinate.
	 * @return The region y coordinate.
	 */
	public int getRegionY() {
		return (y >> 3) - 6;
	}
	
	/**
	 * Adds steps/coordinates to this position.
	 */
	public Position add(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	/**
	 * Adds steps/coordinates to this position.
	 */
	public Position add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	/**
	 * Checks if this location is within range of another.
	 * @param other The other location.
	 * @return <code>true</code> if the location is in range,
	 * <code>false</code> if not.
	 */
	public boolean isWithinDistance(Position other) {
		if(z != other.z)
			return false;
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
	}
	
	/**
	 * Checks if the position is within distance of another.
	 * @param other The other position.
	 * @param distance The distance.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isWithinDistance(Position other, int distance) {
		int deltaX = Math.abs(x - other.x);
		int deltaY = Math.abs(y - other.y);
		return deltaX <= distance && deltaY <= distance;
	}
	
	/**
	 * Checks if this location is within interaction range of another.
	 * @param other The other location.
	 * @return <code>true</code> if the location is in range,
	 * <code>false</code> if not.
	 */
	public boolean isWithinInteractionDistance(Position other) {
		if(z != other.z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 2 && deltaX >= -3 && deltaY <= 2 && deltaY >= -3;
	}
	
	/**
	 * Gets the distance between this position and another position. Only X and
	 * Y are considered (i.e. 2 dimensions).
	 * @param other The other position.
	 * @return The distance.
	 */
	public int getDistance(Position other) {
		int deltaX = x - other.x;
		int deltaY = y - other.y;
		return (int) Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY));
	}
	
	/**
	 * Checks if {@code position} has the same values as this position.
	 * @param position	The position to check.
	 * @return			The values of {@code position} are the same as this position's.
	 */
	public boolean sameAs(Position position) {
		return position.x == x && position.y == y && position.z == z;
	}
	
	/**
	 * Creates a new {@link Position} instance
	 * with the same values as this {@link Position}.
	 * @return	The new {@link Position} instance.
	 */
	public Position copy() {
		return new Position(x, y, z);
	}
	
	@Override
	public String toString() {
		return "Position values: [x, y, z] - [" + x + ", " + y + ", " + z + "]";
	}
	
	@Override
	public int hashCode() {
		return z << 30 | x << 15 | y;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Position)) {
			return false;
		}
		Position position = (Position) other;
		return position.sameAs(this);
	}
}