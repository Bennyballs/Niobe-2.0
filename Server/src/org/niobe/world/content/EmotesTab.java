package org.niobe.world.content;

import org.niobe.model.Animation;
import org.niobe.world.Player;

/**
 * A world content that is used for the performance
 * of emotes in the emote game frame tab.
 *
 * @author relex lawl
 */
public final class EmotesTab {
	
	/**
	 * This flag checks if the {@value buttonId} is a valid
	 * emote tab button.
	 * @param player	The {@link org.niobe.world.Player} clicking the button.
	 * @param buttonId	The button being clicked.
	 * @return			If {@code true} the player will proceed to perform the corresponding {@link org.niobe.model.Animation}.
	 */
	public static boolean isButton(Player player, int buttonId) {
		Emote emote = null;
		for (Emote emotes : Emote.values()) {
			if (emotes.buttonId == buttonId) {
				emote = emotes;
				break;
			}
		}
		if (emote == null) {
			return false;
		}
		player.getMovementQueue().stopMovement();
		player.performAnimation(emote.animation);
		return true;
	}

	/**
	 * Represents a single emote button in
	 * the tab.
	 *
	 * @author relex lawl
	 */
	private enum Emote {
		YES(168, new Animation(855)),
		NO(169, new Animation(856)),
		BOW(164, new Animation(858)),
		ANRGY(167, new Animation(859)),
		THINK(162, new Animation(857)),
		WAVE(163, new Animation(863)),
		SHRUG(13370, new Animation(2113)),
		CHEER(171, new Animation(862))
		;
		
		
		Emote(int buttonId, Animation animation) {
			this.buttonId = buttonId;
			this.animation = animation;
		}
		
		private final int buttonId;
		
		private final Animation animation;
	}
}
