package org.niobe.model.action;

import org.niobe.model.GameCharacter;
import org.niobe.task.Task;

/**
 * An abstract implementation of {@link org.niobe.task.Task} 
 * that handles a game action which can be used for map clicking,
 * object clicking, etc.
 *
 * When adding a {@link GameAction} to a {@link org.niobe.model.GameCharacter}
 * the action will call {@link #equals(Object)} to see if their current {@link GameAction}
 * is the same as the new one being added to avoid problems such as spam-clicking.
 * 
 * @author relex lawl
 */
public abstract class GameAction<T extends GameCharacter> extends Task {

	/**
	 * The GameAction constructor.
	 * @param ticks		The amount of ticks before the action is executed.
	 * @param character	The {@link org.niobe.model.GameCharacter} executing the action.	
	 */
	public GameAction(int ticks, T character) {
		super(ticks);
		this.character = character;
	}
	
	/**
	 * The {@link org.niobe.model.GameCharacter} that will
	 * perform the event.
	 */
	private final T character;
	
	/**
	 * Gets the character that will perform the task.
	 * @return	The game character.
	 */
	public T getCharacter() {
		return character;
	}
}
