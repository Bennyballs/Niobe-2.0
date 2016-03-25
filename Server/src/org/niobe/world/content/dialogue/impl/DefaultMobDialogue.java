package org.niobe.world.content.dialogue.impl;

import org.niobe.util.MathUtil;
import org.niobe.world.Mob;
import org.niobe.world.content.dialogue.Dialogue;

/**
 * An implementation of {@link org.niobe.world.content.dialogue.Dialogue}
 * used as a default chat for mobs throughout the world.
 * 
 * @author relex lawl
 */
public final class DefaultMobDialogue extends Dialogue {

	/**
	 * The DefaultMobDialogue constructor.
	 * @param npc	The mob that will dialogue with the associated player.
	 */
	public DefaultMobDialogue(Mob mob) {
		this.mob = mob;
	}
	
	/**
	 * The mob that will dialogue with the associated player.
	 */
	private final Mob mob;

	@Override
	public DialogueType getType() {
		return DialogueType.MOB_STATEMENT;
	}

	@Override
	public DialogueExpression getAnimation() {
		return DialogueExpression.NORMAL;
	}

	@Override
	public String[] getDialogues() {
		return new String[] {
			RANDOM_DIALOGUE[MathUtil.random(RANDOM_DIALOGUE.length - 1)]
		};
	}
	
	@Override
	public int getMobId() {
		return mob.getDefinition().getId();
	}

	/**
	 * Random dialogues the mob can say.
	 */
	private static final String[] RANDOM_DIALOGUE = {
		"Hello, nice day for an adventure, huh?",
		"Wow! You're one of those adventurers right?",
		"Be careful out there, adventurer!"
	};
}
