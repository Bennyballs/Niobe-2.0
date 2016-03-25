package org.niobe.model.definition;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This file manages the parsing of object definitions, such as
 * the size, state, etc of the object.
 * 
 * @author relex lawl
 */
public final class GameObjectDefinition {

	/**
	 * Every object definition available.
	 */
	private static GameObjectDefinition[] definitions;
	
	/**
	 * The directory in which the object definition file is located.
	 */
	private static final String FILE_DIRECTORY = "./data/cache/object_definitions.dat";

	/**
	 * Initializes object definition parsing to set object flags.
	 */
	public static void init() {
		long startup = System.currentTimeMillis();
		int amount = 0;
		System.out.println("Loading game object definitions...");
		DataInputStream input = null;
		try {
			input = new DataInputStream(new FileInputStream(FILE_DIRECTORY));
			int total = input.readInt();
			definitions = new GameObjectDefinition[total];
			while (true) {
					GameObjectDefinition definition = new GameObjectDefinition();
					definition.id = input.readInt();
					definition.name = input.readUTF();
					definition.rotation = input.read();
					definition.solidState = input.read();
					definition.sizeX = input.read();
					definition.sizeY = input.read();
					definition.walkable = input.readBoolean();
					definition.solid = input.readBoolean();
					definition.actions = new String[input.read()];
					for (int i = 0; i < definition.actions.length; i++) {
						definition.actions[i] = input.readUTF();
						if (definition.actions[i].length() <= 0 || 
								definition.actions[i].toLowerCase().equals("null")) {
							definition.actions[i] = null;
						}
					}
					definitions[definition.id] = definition;
					amount++;
			}
		} catch (IOException exception) {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Loaded " + amount + " object definitions in " + (System.currentTimeMillis() - startup) + "ms");
	}

	/**
	 * Writes the game object definition binary file.
	 * @param directory	The location to store the newly written file.
	 */
	public static void write(String directory) throws IOException {
		if (directory.length() <= 0) {
			throw new IllegalArgumentException("Directory length must be greater than 0!");
		}
		DataOutputStream output = null;
		try {
			output = new DataOutputStream(new FileOutputStream(directory));
			output.writeInt(definitions.length);
			for (GameObjectDefinition definition : definitions) {
				if (definition == null)
					continue;
				output.writeInt(definition.id);
				output.writeUTF(definition.name);
				output.write(definition.rotation);
				output.write(definition.solidState);
				output.write(definition.sizeX);
				output.write(definition.sizeY);
				output.writeBoolean(definition.walkable);
				output.writeBoolean(definition.solid);
				output.write(definition.actions.length);
				for (int i = 0; i < definition.actions.length; i++) {
					if (definition.actions[i] == null)
						definition.actions[i] = "Null";
					output.writeUTF(definition.actions[i]);
					if (!definition.actions[i].equals("Null") && definition.actions[i].length() > 0) {
						definition.hasActions = true;
					}
				}
				System.out.println("Wrote definition for object with id: " + definition.id);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			output.close();
		}
		output.close();
		System.out.println("Finished adding object definition file.");
	}

	/**
	 * Gets objectDefinition for an object id.
	 * 
	 * @param id
	 *            Object id to fetch definition for.
	 */
	public static GameObjectDefinition forId(int id) {
		return (id < 0 || id > definitions.length || definitions[id] == null) ? new GameObjectDefinition() : definitions[id];
	}

	/**
	 * Gets the total amount of object definitions available.
	 * @return definitions.length.
	 */
	public static int getCount() {
		return definitions.length;
	}
	
	/**
	 * The object's id.
	 */
	private int id;
	
	/**
	 * The object's name.
	 */
	private String name;

	/**
	 * The object's description.
	 */
	private String description;
	
	/**
	 * The object's default rotation.
	 */
	private int rotation;
	
	/**
	 * The object's solid state.
	 */
	private int solidState;
	
	/**
	 * This flag checks if the game object is walkable.
	 */
	private boolean walkable;
	
	/**
	 * This flag checks if the game object is solid
	 */
	private boolean solid;

	/**
	 * The object's x size, can be used for clipping purposes.
	 */
	private int sizeX;

	/**
	 * The object's y size, can be used for clipping purposes.
	 */
	private int sizeY;

	/**
	 * If object has actions, action names.
	 */
	private String[] actions = new String[5];
	
	/**
	 * Checks if the object has any actions.
	 */
	private boolean hasActions;

	/**
	 * Gets the object's id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Fetches the object's name.
	 * 
	 * @return name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Fetches the object's description.
	 * 
	 * @return description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Fetches the object's x size.
	 * 
	 * @return sizeX.
	 */
	public int getSizeX() {
		return sizeX;
	}
	
	/**
	 * Fetches the object's y size.
	 * 
	 * @return sizeY.
	 */
	public int getSizeY() {
		return sizeY;
	}
	
	/**
	 * Fetches the object's face.
	 * @return rotation.
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * Fetches the action names if any.
	 * 
	 * @return actionNames.
	 */
	public String[] getActions() {
		return actions;
	}
	
	/**
	 * Checks if the game object has actions.
	 * @return	hasActions.
	 */
	public boolean hasActions() {
		return hasActions;
	}
	
	/**
	 * Gets the game object's solid state.
	 * @return	The solid state.
	 */
	public int getSolidState() {
		return solidState;
	}
	
	/**
	 * Checks if game object is walkable.
	 * @return	If <code>true</code> Game Object is walkable.
	 */
	public boolean isWalkable() {
		return walkable;
	}

	/**
	 * Checks if game object is solid.
	 * @return	If <code>true</code> Game Object is solid.
	 */
	public boolean isSolid() {
		return solid;
	}
	
	/**
	 * Gets the game object definitions populating
	 * the definitions array.
	 * @return	The {@link #definitions} array.
	 */
	public static GameObjectDefinition[] getDefinitions() {
		return definitions;
	}
	
	/**
	 * Overrides the java Object's toString method to return object values.
	 */
	@Override
	public String toString() {
		return "Name: " + name + ", description: " + description + ", sizeX: "
				+ sizeX + ", sizeY: " + sizeY;
	}
}