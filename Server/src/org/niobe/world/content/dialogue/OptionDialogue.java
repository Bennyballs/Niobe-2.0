package org.niobe.world.content.dialogue;

import org.niobe.world.Player;

/**
 * An abstract implementation of {@link Dialogue} used for
 * {@link Dialogues} that who's {@link Dialogue#getType()} equals
 * {@link Dialogue.DialogueType#OPTIONS}.
 *
 * @author relex lawl
 */
public abstract class OptionDialogue extends Dialogue {
	
	/**
	 * The first option action that will be performed upon the execution of
	 * {@link org.niobe.net.packet.event.ButtonClickGamePacket#read(Player, org.niobe.net.packet.GamePacket)}.
	 * @param player	The {@link org.niobe.world.Player} clicking the option.
	 */
	public void firstOption(Player player) {
		
	}
	
	/**
	 * The second option action that will be performed upon the execution of
	 * {@link org.niobe.net.packet.event.ButtonClickGamePacket#read(Player, org.niobe.net.packet.GamePacket)}.
	 * @param player	The {@link org.niobe.world.Player} clicking the option.
	 */
	public void secondOption(Player player) {
		
	}
	
	/**
	 * The third option action that will be performed upon the execution of
	 * {@link org.niobe.net.packet.event.ButtonClickGamePacket#read(Player, org.niobe.net.packet.GamePacket)}.
	 * @param player	The {@link org.niobe.world.Player} clicking the option.
	 */
	public void thirdOption(Player player) {
		
	}
	
	/**
	 * The fourth option action that will be performed upon the execution of
	 * {@link org.niobe.net.packet.event.ButtonClickGamePacket#read(Player, org.niobe.net.packet.GamePacket)}.
	 * @param player	The {@link org.niobe.world.Player} clicking the option.
	 */
	public void fourthOption(Player player) {
		
	}
	
	/**
	 * The fifth option action that will be performed upon the execution of
	 * {@link org.niobe.net.packet.event.ButtonClickGamePacket#read(Player, org.niobe.net.packet.GamePacket)}.
	 * @param player	The {@link org.niobe.world.Player} clicking the option.
	 */
	public void fifthOption(Player player) {
		
	}
	
	@Override
	public DialogueType getType() {
		return DialogueType.OPTIONS;
	}

	@Override
	public DialogueExpression getAnimation() {
		return null;
	}
}
