package org.niobe.world;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.Entity;
import org.niobe.model.Graphic;
import org.niobe.model.Position;
import org.niobe.model.definition.GameObjectDefinition;

/**
 * An implementation of {@link Entity} that represents
 * an object in the world.
 * 
 * @author relex lawl
 */
public class GameObject extends Entity {

	/**
	 * GameObject constructor to call upon a new game object.
	 * @param id		The new object's id.
	 * @param position	The new object's position on the globe.
	 */
	public GameObject(int id, Position position) {
		super(position);
		this.id = id;
		definition = GameObjectDefinition.forId(id);
	}
	
	/**
	 * The object's id.
	 */
	private int id;
	
	/**
	 * Gets the object's id.
	 * @return id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * The object's type (default=10).
	 */
	private int type = 10;
	
	/**
	 * Gets the object's type.
	 * @return	type.
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Sets the object's type.
	 * @param type	New type value to assign.
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * The object's current direction to face.
	 */
	private int face;
	
	/**
	 * Gets the object's current face direction.
	 * @return	face.
	 */
	public int getFace() {
		return face;
	}
	
	/**
	 * Sets the object's face direction.
	 * @param face	Face value to which object will face.
	 */
	public void setFace(int face) {
		this.face = face;
	}
	
	/**
	 * The object's definition.
	 */
	private GameObjectDefinition definition;
	
	/**
	 * Gets the object's definition.
	 * @return	definition.
	 */
	public GameObjectDefinition getDefinition() {
		if (definition != null) {
			return definition;
		}
		return (definition = GameObjectDefinition.forId(id));
	}

	@Override
	public void performAnimation(Animation animation) {
		for (Player player : getRegion().getPlayers()) {
			player.getPacketSender().sendGameObjectAnimation(this, animation);
		}
	}
	
	@Override
	public void performGraphic(Graphic graphic) {
		for (Player player : getRegion().getPlayers()) {
			player.getPacketSender().sendGraphic(graphic, getPosition());
		}
	}
	
	@Override
	public int getFreeIndex() {
		for (int i = 0; i < GameServer.getWorld().getGameObjects().length; i++) {
			if (GameServer.getWorld().getGameObjects()[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int getSize() {
		return definition.getSizeY(); //TODO get size
	}
	
	@Override
	public boolean isGameObject() {
		return true;
	}
}
