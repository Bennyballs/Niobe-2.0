package org.niobe.world.content.dialogue;

import org.niobe.model.Animation;
import org.niobe.world.Player;

/**
 * Represents a dialogue that can be used in a
 * player received dialogue chat.
 * 
 * @author relex lawl
 */
public abstract class Dialogue {
	
	/**
	 * Gets the dialogue's type.
	 */
	public abstract DialogueType getType();
	
	/**
	 * Gets the animation to perform when type is an MOB or PLAYER STATEMENT.
	 */
	public abstract DialogueExpression getAnimation();
	
	/**
	 * Gets the dialogue's actual line dialogue strings.
	 */
	public abstract String[] getDialogues();
	
	/**
	 * Gets the next dialogue to show after 'click here to continue' has been clicked.
	 * @return	The next dialogue to display.
	 */
	public Dialogue getNextDialogue() {
		return null;
	}
	
	/**
	 * Gets the next dialogue id to show after 'click here to continue' has been clicked.
	 * @return	The next dialogue's id.
	 */
	public int getNextDialogueId() {
		return -1;
	}
	
	/**
	 * Gets this dialogue's id.
	 * @return	The default id gotten from dialogue manager.
	 */
	public int getId() {
		return DialogueManager.getDefaultId();
	}
	
	/**
	 * The id of the mob if type is MOB_STATEMENT.
	 * @return
	 */
	public int getMobId() {
		return -1;
	}
	
	/**
	 * An array containing the item configurations if type is ITEM_STATEMENT.
	 * First index contains the itemId, second index contains the itemZoom and
	 * the third index contains the dialogue's title.
	 * @return
	 */
	public String[] getItems() {
		return null;
	}
	
	/**
	 * Performs a special 'action' such as giving a player an item or
	 * teleporting after finishing said dialogue.
	 */
	public void specialAction() {
		
	}
	
	/**
	 * Called when a dialogue action removes
	 * the chat box interface.
	 */
	public void finish(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
	}
	
	/**
	 * Represents a dialogue head model's animation.
	 * 
	 * @author relex lawl
	 */
	public enum DialogueExpression {

		NO_EXPRESSION(9760),
		NO_EXPRESSION_TWO(9772),
		SAD(9764),
		SAD_TWO(9768),
		WHY(9776),
		SCARED(9780),
		MIDLY_ANGRY(9784),
		ANGRY(9788),
		VERY_ANGRY(9792),
		ANGRY_TWO(9796),
		MANIC_FACE(9800),
		JUST_LISTEN(9804),
		PLAIN_TALKING(9808),
		LOOK_DOWN(9812),
		CONFUSED(9816),
		CONFUSED_TWO(9820),
		WIDEN_EYES(9824),
		CROOKED_HEAD(9828),
		GLANCE_DOWN(9832),
		UNSURE(9836),
		LISTEN_LAUGH(9840),
		TALK_SWING(9844),
		NORMAL(9847),
		GOOFY_LAUGH(9851),
		NORMAL_STILL(9855),
		THINKING_STILL(9859),
		LOOKING_UP(9862);
		
		/**
		 * The DialogueExpression constructor.
		 * @param animationId	The id of the animation for said expression.
		 */
		private DialogueExpression(int animationId) {
			animation = new Animation(animationId);
		}
		
		/**
		 * The animation the dialogue head model will perform.
		 */
		private final Animation animation;
		
		/**
		 * Gets the animation for dialogue head model to perform.
		 * @return	animation.
		 */
		public Animation getAnimation() {
			return animation;
		}
	}
	
	/**
	 * Represents a type of dialogue.
	 * 
	 * @author relex lawl
	 */
	public enum DialogueType {

		/**
		 * Gives variable options for a player to choose.
		 */
		OPTIONS,
		
		/**
		 * Gives a statement.
		 */
		STATEMENT,
		
		/**
		 * Gives a dialogue said by a mob.
		 */
		MOB_STATEMENT,
		
		/**
		 * Gives a dialogue with an item model next to it.
		 */
		ITEM_STATEMENT,
		
		/**
		 * Gives a dialogue said by a player.
		 */
		PLAYER_STATEMENT;
	}
}
