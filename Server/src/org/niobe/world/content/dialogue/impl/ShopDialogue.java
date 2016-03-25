package org.niobe.world.content.dialogue.impl;

import org.niobe.model.container.impl.Shop;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.content.dialogue.Dialogue;
import org.niobe.world.content.dialogue.DialogueManager;
import org.niobe.world.content.dialogue.OptionDialogue;

/**
 * An implementation of {@link org.niobe.world.content.dialogue.Dialogue}
 * used for {@link org.niobe.model.container.impl.Shop} dialogues that will be
 * used for {@link org.niobe.world.Mob} {@link org.niobe.world.content.dialogue.Dialogue.DialogueType#OPTIONS}s.
 *
 * @author relex lawl
 */
public final class ShopDialogue extends Dialogue {
	
	/**
	 * The ShopDialogue constructor.
	 * @param mob	The {@link org.niobe.world.Mob} starting the chat.
	 * @param shop	The {@link org.niobe.model.container.impl.Shop} that will be opened.
	 */
	public ShopDialogue(Mob mob, Shop shop) {
		this.mob = mob;
		this.options = new Options(mob, shop);
	}

	/**
	 * The {@link org.niobe.world.Mob} starting the chat.
	 */
	private final Mob mob;
	
	/**
	 * The {@link Options} that will be used on the calling of
	 * {@link org.niobe.world.content.dialogue.Dialogue#getNextDialogue()}.
	 */
	private final Options options;
	
	@Override
	public int getMobId() {
		return mob.getId();
	}
	
	@Override
	public Dialogue getNextDialogue() {
		return options;
	}
	
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
		return DIALOGUE;
	}
	
	/**
	 * The dialogues given in this {@link org.niobe.world.content.dialogue.Dialogue}.
	 */
	private static final String[] DIALOGUE = new String[] {
		"Hello, would you like to purchase some items?"
	};
	
	/**
	 * An implementation of {@link org.niobe.world.content.dialogue.OptionDialogue} used
	 * for the {@link ShopDialogue#getNextDialogue()} method.
	 *
	 * @author relex lawl
	 */
	private static final class Options extends OptionDialogue {
		
		/**
		 * The Options constructor.
		 * @param mob	The {@link org.niobe.world.Mob} chatting.
		 * @param shop	The {@link org.niobe.model.container.impl.Shop} that will be opened
		 * 				if {@link #firstOption(Player)} is chosen.
		 */
		private Options(final Mob mob, Shop shop) {
			this.shop = shop;
			this.rejectDialogue = new Dialogue() {
				@Override
				public int getMobId() {
					return mob.getId();
				}

				@Override
				public DialogueType getType() {
					return DialogueType.MOB_STATEMENT;
				}

				@Override
				public DialogueExpression getAnimation() {
					return DialogueExpression.SAD;
				}

				@Override
				public String[] getDialogues() {
					return new String[] {
						"Perhaps another time then."	
					};
				}
			};
		}
		
		/**
		 * The {@link org.niobe.model.container.impl.Shop} that will be opened
		 * 	if {@link #firstOption(Player)} is chosen.
		 */
		private final Shop shop;
		
		/**
		 * The {@link org.niobe.world.content.dialogue.Dialogue} given
		 * when the {@link org.niobe.world.Player} calls the {@link #secondOption(Player)}.
		 */
		private final Dialogue rejectDialogue;
		
		@Override
		public void firstOption(Player player) {
			shop.open(player);
		}

		@Override
		public void secondOption(Player player) {
			DialogueManager.start(player, rejectDialogue);
		}

		@Override
		public DialogueType getType() {
			return DialogueType.OPTIONS;
		}

		@Override
		public DialogueExpression getAnimation() {
			return null;
		}

		@Override
		public String[] getDialogues() {
			return DIALOGUE;
		}	
		
		/**
		 * The options given in this {@link org.niobe.world.content.dialogue.OptionDialogue}.
		 */
		private static final String[] DIALOGUE = new String[] {
			"Sure.",
			"No, thanks."
		};	
	}
}
