package org.niobe.world.content.dialogue.impl;

import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.content.dialogue.Dialogue;
import org.niobe.world.content.dialogue.DialogueManager;
import org.niobe.world.content.dialogue.OptionDialogue;
import org.niobe.world.util.GameConstants;

/**
 * An implementation of {@link org.niobe.world.content.dialogue.Dialogue}
 * used for bankers throughout the game.
 *
 * @author relex lawl
 */
public final class BankerDialogue extends Dialogue {

	/**
	 * The BankerDialogue constructor.
	 * @param mob	The banker mob.
	 */
	public BankerDialogue(Mob mob) {
		this.mob = mob;
	}
	
	/**
	 * The {@link org.niobe.world.Mob} representing a
	 * banker.
	 */
	private final Mob mob;
	
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
		return DialogueExpression.NORMAL;
	}

	@Override
	public String[] getDialogues() {
		return new String[] {
			"Good day, how may I help you?"
		};
	}

	@Override
	public Dialogue getNextDialogue() {
		return new OptionDialogue() {
			
			@Override
			public void firstOption(Player player) {
				player.getBank().open();
			}
			
			@Override
			public void secondOption(Player player) {
				//TODO pin
				finish(player);
			}
			
			@Override
			public void thirdOption(Player player) {
				//TODO collection box
				finish(player);
			}
			
			@Override
			public void fourthOption(Player player) {
				Dialogue dialogue = new Dialogue() {
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
						return DialogueExpression.NORMAL;
					}

					@Override
					public String[] getDialogues() {
						return new String[] {
							"This is a branch of the Bank of " + GameConstants.SERVER_NAME + ".",
							"We have branches in many towns."
						};
					}
					
					@Override
					public Dialogue getNextDialogue() {
						return new OptionDialogue() {

							@Override
							public void firstOption(Player player) {
								Dialogue dialogue = new Dialogue() {

									@Override
									public DialogueType getType() {
										return DialogueType.PLAYER_STATEMENT;
									}

									@Override
									public DialogueExpression getAnimation() {
										return DialogueExpression.CROOKED_HEAD;
									}

									@Override
									public String[] getDialogues() {
										return new String[] {
											"And what do you do?"
										};
									}
									
									@Override
									public Dialogue getNextDialogue() {
										Dialogue dialogue = new Dialogue() {
											
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
												return DialogueExpression.NORMAL;
											}

											@Override
											public String[] getDialogues() {
												return new String[] {
													"We will look after your items and money for you.",
													"Leave your valuables with us if you want to keep them",
													"safe."
												};
											}
											
										};
										return dialogue;
									}
								};
								DialogueManager.start(player, dialogue);
							}
							
							@Override
							public void secondOption(Player player) {
								Dialogue dialogue = new Dialogue() {

									@Override
									public DialogueType getType() {
										return DialogueType.PLAYER_STATEMENT;
									}

									@Override
									public DialogueExpression getAnimation() {
										return DialogueExpression.UNSURE;
									}

									@Override
									public String[] getDialogues() {
										return new String[] {
											"Didn't you used to be called the Bank of Varrock?"
										};
									}
									
									@Override
									public Dialogue getNextDialogue() {
										Dialogue dialogue = new Dialogue() {
											
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
												return DialogueExpression.NORMAL;
											}

											@Override
											public String[] getDialogues() {
												return new String[] {
													"Yes we did, but people kept telling us our",
													"signs were wrong. They acted as if we didn't know",
													"what town we were in or something."
												};
											}
											
										};
										return dialogue;
									}
								};
								DialogueManager.start(player, dialogue);
							}
							
							@Override
							public DialogueType getType() {
								return DialogueType.OPTIONS;
							}

							@Override
							public String[] getDialogues() {
								return new String[] {
									"And what do you do?",
									"Didn't you used to be called the Bank of Varrock?"
								};
							}
							
						};
					}
				};
				DialogueManager.start(player, dialogue);
			}
			
			@Override
			public DialogueType getType() {
				return DialogueType.OPTIONS;
			}
			
			@Override
			public String[] getDialogues() {
				return new String[] {
					"I'd like to access my bank account, please.",
					"I'd like to check my PIN settings.",
					"I'd like to see my collection box",
					"What is this place?"
				};
			}
		};
	}
}
