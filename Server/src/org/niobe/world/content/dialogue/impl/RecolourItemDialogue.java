package org.niobe.world.content.dialogue.impl;

import org.niobe.model.Item;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.world.content.dialogue.Dialogue;

/**
 * An implementation of {@link org.niobe.world.content.dialogue.Dialogue}
 * that represents a re-color item dialogue, used for barbarian assault
 * for items such as abyssal whip, dark bows and staves of light.
 * 
 * @author relex lawl
 */
public class RecolourItemDialogue extends Dialogue {
	
	/**
	 * The RecolourItemDialogue constructor.
	 * @param item		The item to show in the dialogue.
	 * @param zoom		The zoom of the item.
	 * @param title		The title of the dialogue.
	 */
	public RecolourItemDialogue(Item item, int zoom, String title) {
		this.item = item;
		this.zoom = zoom;
		this.title = title;
	}
	
	/**
	 * The item to show in the dialogue.
	 */
	private final Item item;
	
	/**
	 * The zoom of the item.
	 */
	private final int zoom;
	
	/**
	 * The title of the dialogue.
	 */
	private final String title;

	@Override
	public DialogueType getType() {
		return DialogueType.ITEM_STATEMENT;
	}

	@Override
	public DialogueExpression getAnimation() {
		return null;
	}

	@Override
	public String[] getDialogues() {
		return new String[] {
			"You have recoloured your " + ItemDefinition.forId(item.getId()).getName() + "."
		};
	}
	
	@Override
	public String[] getItems() {
		return new String[] {
			String.valueOf(item.getId()),
			String.valueOf(zoom),
			title
		};
	}
}
