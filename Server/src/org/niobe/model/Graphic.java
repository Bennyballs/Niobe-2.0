package org.niobe.model;

/**
 * Represents a graphic that an {@link Entity}
 * can "perform".
 * 
 * @author relex lawl
 */
public final class Graphic {
	
	/**
	 * The graphic constructor for a character to perform.
	 * @param id		The graphic's id.
	 * @param delay		The delay to wait until performing the graphic.
	 */
	public Graphic(int id, int delay, GraphicHeight height) {
		this.id = id;
		this.delay = delay;
		this.height = height;
	}
	
	/**
	 * The graphic constructor for a character to perform.
	 * @param id	The graphic's id.
	 */
	public Graphic(int id, GraphicHeight height) {
		this(id, 0, height);
	}
	
	/**
	 * The graphic constructor for a character to perform.
	 * @param id		The graphic's id.
	 * @param delay		The delay to wait until performing the graphic.
	 */
	public Graphic(int id, int delay) {
		this(id, delay, GraphicHeight.LOW);
	}
	
	/**
	 * The graphic constructor for a character to perform.
	 * @param id	The graphic's id.
	 */
	public Graphic(int id) {
		this(id, 0);
	}
	
	/**
	 * The graphic's id.
	 */
	private final int id;
	
	/**
	 * The delay which the graphic must wait before being performed.
	 */
	private final int delay;
	
	/**
	 * The graphic's height level to display in.
	 */
	private final GraphicHeight height;
	
	/**
	 * Gets the graphic's id.
	 * @return	id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the graphic's wait delay.
	 * @return	delay.
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Gets the graphic's height level to be displayed in.
	 * @return	The height level.
	 */
	public GraphicHeight getHeight() {
		return height;
	}
	
	/**
	 * Represents a graphic's height.
	 * 
	 * @author relex lawl
	 */
	public enum GraphicHeight {
		/**
		 * Represents a low graphic, which is located
		 * on the floor.
		 */
		LOW,
		
		/**
		 * Represents a middle graphic, which is located
		 * around the waist of a {@link org.niobe.world.Player}.
		 */
		MIDDLE,
		
		/**
		 * Represents a high graphic, which is located
		 * around the neck of a {@link org.niobe.world.Player}.
		 */
		HIGH;
	}
}
