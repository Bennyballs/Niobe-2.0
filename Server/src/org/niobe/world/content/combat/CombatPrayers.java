package org.niobe.world.content.combat;

import java.util.HashMap;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.PrayerBook;
import org.niobe.model.SkillManager.Skill;
import org.niobe.task.Task;
import org.niobe.util.NameUtil;
import org.niobe.world.Player;
import org.niobe.world.util.GameConstants;
 
/**
 * This class is used to handle a player activating and deactivating
 * a prayer in their prayer tab.
 * 
 * @author relex lawl
 */

public class CombatPrayers {
	
	/**
	 * Represents a prayer's configurations, such as their
	 * level requirement, buttonId, configId and drain rate.
	 * 
	 * @author relex lawl
	 */
	private enum PrayerData {
		THICK_SKIN(1, 1, 5609, 83),
		BURST_OF_STRENGTH(4, 1, 5610, 84),
		CLARITY_OF_THOUGHT(7, 1, 5611, 85),
		SHARP_EYE(8, 1, 19812, 700),
		MYSTIC_WILL(9, 1, 19814, 701),
		ROCK_SKIN(10, 2, 5612, 86),
		SUPERHUMAN_STRENGTH(13, 2, 5613, 87),
		IMPROVED_REFLEXES(16, 2, 5614, 88),
		RAPID_RESTORE(19, .4, 5615, 89),
		RAPID_HEAL(22, .6, 5616, 90),
		PROTECT_ITEM(25, .6, 5617, 91),
		HAWK_EYE(26, 1.5, 19816, 702),
		MYSTIC_LORE(27, 2, 19818, 703),
		STEEL_SKIN(28, 4, 5618, 92),
		ULTIMATE_STRENGTH(31, 4, 5619, 93),
		INCREDIBLE_REFLEXES(34, 4, 5620, 94),
		PROTECT_FROM_SUMMONING(35, 4, 18022, 711, 3),
		PROTECT_FROM_MAGIC(37, 4, 5621, 95, 2),
		PROTECT_FROM_MISSILES(40, 4, 5622, 96, 1),
		PROTECT_FROM_MELEE(43, 4, 5623, 97, 0),
		EAGLE_EYE(44, 4, 19821, 704),
		MYSTIC_MIGHT(45, 4, 19823, 705),
		RETRIBUTION(46, 1, 683, 98, 4),
		REDEMPTION(49, 2, 684, 99, 5),
		SMITE(52, 6, 685, 100, 6),
		CHIVALRY(60, 8, 19825, 706),
		RAPID_RENEWAL(65, .6, 18014, 708),
		PIETY(70, 10, 19827, 707),
		RIGOUR(74, 10, 18018, 709),
		AUGURY(77, 10, 18020, 710);
		
		private PrayerData(int requirement, double drainRate, int buttonId, int configId, int... hint) {
			this.requirement = requirement / 10;
			this.drainRate = drainRate;
			this.buttonId = buttonId;
			this.configId = configId;
			if (hint.length > 0)
				this.hint = hint[0];
		}
		
		/**
		 * The prayer's level requirement for player to be able
		 * to activate it.
		 */
		private int requirement;
		
		/**
		 * The prayer's action button id in prayer tab.
		 */
		private int buttonId;
		
		/**
		 * The prayer's config id to switch their glow on/off by 
		 * sending the sendConfig packet.
		 */
		private int configId;
		
		/**
		 * The prayer's drain rate as which it will drain
		 * the associated player's prayer points.
		 */
		private double drainRate;
		
		/**
		 * The prayer's head icon hint index.
		 */
		private int hint = -1;
		
		/**
		 * The prayer's formatted name.
		 */
		private String name;
		
		/**
		 * Gets the prayer's formatted name.
		 * @return	The prayer's name
		 */
		private final String getPrayerName() {
			if (name == null)
				return NameUtil.capitalizeWords(toString().toLowerCase().replaceAll("_", " "));
			return name;
		}
		
		/**
		 * Contains the PrayerData with their corresponding prayerId.
		 */
		private static HashMap <Integer, PrayerData> prayerData = new HashMap <Integer, PrayerData> ();
		
		/**
		 * Contains the PrayerData with their corresponding buttonId.
		 */
		private static HashMap <Integer, PrayerData> actionButton = new HashMap <Integer, PrayerData> ();

		/**
		 * Populates the prayerId and buttonId maps.
		 */
		static {
			for (PrayerData pd : PrayerData.values()) {
				prayerData.put(pd.ordinal(), pd);
				actionButton.put(pd.buttonId, pd);
			}
		}
	}
	
	/**
	 * Activates a prayer with specified <code>buttonId</code>.
	 * @param player	The player clicking on prayer button.
	 * @param buttonId	The button the player is clicking.
	 */
	public static void togglePrayerWithActionButton(Player player, final int buttonId) {
		for (PrayerData pd : PrayerData.values()) {
			if (buttonId == pd.buttonId) {
				if (!player.getFields().getPrayerActive()[pd.ordinal()])
					activatePrayer(player, pd.ordinal());
				else
					deactivatePrayer(player, pd.ordinal());
			}
		}
	}
	
	/**
	 * Activates said prayer with specified <code>prayerId</code> and de-activates
	 * all non-stackable prayers.
	 * @param player		The player activating prayer.
	 * @param prayerId		The id of the prayer being turned on, also known as the ordinal in the respective enum.
	 */
	public static void activatePrayer(Player player, final int prayerId) {
		if (player.getFields().getPrayerActive()[prayerId])
			return;
		PrayerData pd = PrayerData.prayerData.get(prayerId);
		if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) <= 0) {
			player.getPacketSender().sendConfig(pd.configId, 0);
			player.getPacketSender().sendMessage("You do not have enough prayer points. Recharge at an altar.");
			return;
		}
		if (player.getSkillManager().getMaxLevel(Skill.PRAYER) < (pd.requirement * 10)) {
			player.getPacketSender().sendConfig(pd.configId, 0);
			//DialogueManager.sendOneStringStatement(p, "               " + "You need a @blu@Prayer <col=0>level of " + pd.levelRequirement +" to use @blu@" + pd.getPrayerName());
			player.getPacketSender().sendMessage("You need a prayer level of " + pd.requirement + " to use " + pd.getPrayerName() + ".");
			return;
		}
		if (prayerId == CHIVALRY && player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 60) {
			player.getPacketSender().sendConfig(pd.configId, 0);
			player.getPacketSender().sendMessage("You need a defence level of 60 or above to use chivalry.");
			return;
		}
		if (prayerId == PIETY && player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 70) {
			player.getPacketSender().sendConfig(pd.configId, 0);
			player.getPacketSender().sendMessage("You need a defence level of 70 or above to use piety.");
			return;
		}
		switch (prayerId) {
			case THICK_SKIN:
			case ROCK_SKIN:
			case STEEL_SKIN:
				resetPrayers(player, DEFENCE_PRAYERS, prayerId);
				break;
			case BURST_OF_STRENGTH:
			case SUPERHUMAN_STRENGTH:
			case ULTIMATE_STRENGTH:
				resetPrayers(player, STRENGTH_PRAYERS, prayerId);
				resetPrayers(player, RANGED_PRAYERS, prayerId);
				resetPrayers(player, MAGIC_PRAYERS, prayerId);
				break;
			case CLARITY_OF_THOUGHT:
			case IMPROVED_REFLEXES:
			case INCREDIBLE_REFLEXES:
				resetPrayers(player, ATTACK_PRAYERS, prayerId);
				resetPrayers(player, RANGED_PRAYERS, prayerId);
				resetPrayers(player, MAGIC_PRAYERS, prayerId);
				break;
			case SHARP_EYE:
			case HAWK_EYE:
			case EAGLE_EYE:
			case MYSTIC_WILL:
			case MYSTIC_LORE:
			case MYSTIC_MIGHT:
				resetPrayers(player, STRENGTH_PRAYERS, prayerId);
				resetPrayers(player, ATTACK_PRAYERS, prayerId);
				resetPrayers(player, RANGED_PRAYERS, prayerId);
				resetPrayers(player, MAGIC_PRAYERS, prayerId);
				break;
			case CHIVALRY:
			case PIETY:
				resetPrayers(player, DEFENCE_PRAYERS, prayerId);
				resetPrayers(player, STRENGTH_PRAYERS, prayerId);
				resetPrayers(player, ATTACK_PRAYERS, prayerId);
				resetPrayers(player, RANGED_PRAYERS, prayerId);
				resetPrayers(player, MAGIC_PRAYERS, prayerId);
				break;
			case PROTECT_FROM_SUMMONING:
				resetPrayers(player, NON_PROTECTION_OVERHEAD_PRAYERS, prayerId);
				break;
			case PROTECT_FROM_MAGIC:
			case PROTECT_FROM_MISSILES:
			case PROTECT_FROM_MELEE:
				resetPrayers(player, OVERHEAD_PRAYERS, prayerId);
				break;
			case RETRIBUTION:
			case REDEMPTION:
			case SMITE:
				resetPrayers(player, OVERHEAD_PRAYERS, prayerId);
				deactivatePrayer(player, PROTECT_FROM_SUMMONING);
				break;
			case RIGOUR:
			case AUGURY:
				resetPrayers(player, STRENGTH_PRAYERS, prayerId);
				resetPrayers(player, ATTACK_PRAYERS, prayerId);
				resetPrayers(player, DEFENCE_PRAYERS, prayerId);
				resetPrayers(player, RANGED_PRAYERS, prayerId);
				resetPrayers(player, MAGIC_PRAYERS, prayerId);
				break;
		}
		if (player.getFields().isOverheadPrayerCap()) {
			if (prayerId == PROTECT_FROM_SUMMONING || prayerId == PROTECT_FROM_MAGIC ||
					prayerId == PROTECT_FROM_MISSILES || prayerId == PROTECT_FROM_MELEE) {
				player.getPacketSender().sendMessage("You have been injured and cannot use this prayer!");
				return;
			}
		}
		player.getFields().setPrayerActive(prayerId, true);
		player.getPacketSender().sendConfig(pd.configId, 1);
		if (hasNoPrayerOn(player, prayerId))
			startDrain(player);
		if (pd.hint != -1) {
			int hintId = getHeadHint(player);
			player.getAppearance().setHeadHint(hintId);
		}
		//if (player.prayerStatusOpened)
		//	PrayerBonus.togglePrayerStats(player);
	}
	
	/**
	 * Deactivates said prayer with specified <code>prayerId</code>.
	 * @param player		The player deactivating prayer.
	 * @param prayerId		The id of the prayer being deactivated.
	 */
	public static void deactivatePrayer(Player player, int prayerId) {
		if (!player.getFields().getPrayerActive()[prayerId])
			return;
		PrayerData pd = PrayerData.prayerData.get(prayerId);
		player.getFields().getPrayerActive()[prayerId] = false;
		player.getPacketSender().sendConfig(pd.configId, 0);
		if (pd.hint != -1) {
			int hintId = getHeadHint(player);
			player.getAppearance().setHeadHint(hintId);
		}
		//if (player.prayerStatusOpened)
		//	PrayerBonus.togglePrayerStats(player);
	}
	
	/**
	 * Deactivates every prayer in the player's prayer book.
	 * @param player	The player to deactivate prayers for.
	 */
	public static void deactivatePrayers(Player player) {
		for (int i = 0; i < player.getFields().getPrayerActive().length; i++) {
			if (player.getFields().getPrayerActive()[i]) {
				deactivatePrayer(player, i);
			}
		}
	}
	
	/**
	 * Switches the <code>player</code>'s current magic book.
	 * @param player		The player to switch magic books for.
	 * @param prayerBook	The prayer book the player will have.
	 */
	public static void switchPrayerbook(Player player, PrayerBook prayerBook) {
		if (prayerBook == null) {
			player.getPacketSender().sendTabInterface(GameConstants.PRAYER_TAB, -1);
		} else {
			CombatPrayers.deactivatePrayers(player);
			player.getFields().setPrayerBook(prayerBook);
			player.performAnimation(new Animation(645));
			CombatCurses.deactivateCurses(player);
			player.getPacketSender().sendTabInterface(GameConstants.PRAYER_TAB, prayerBook.getInterfaceId());
			if (prayerBook.getMessage() != null && prayerBook.getMessage().length() > 0)
				player.getPacketSender().sendMessage(prayerBook.getMessage());
		}
	}
	
	/**
	 * Gets the player's current head hint if they activate or deactivate
	 * a head prayer.
	 * @param player	The player to fetch head hint index for.
	 * @return			The player's current head hint index.
	 */
	private static int getHeadHint(Player player) {
		boolean[] prayers = player.getFields().getPrayerActive();
		if (prayers[PROTECT_FROM_SUMMONING] && prayers[PROTECT_FROM_MELEE])
			return 7;
		if (prayers[PROTECT_FROM_SUMMONING] && prayers[PROTECT_FROM_MISSILES])
			return 8;
		if (prayers[PROTECT_FROM_SUMMONING] && prayers[PROTECT_FROM_MAGIC])
			return 9;
		if (prayers[PROTECT_FROM_MELEE])
			return 0;
		if (prayers[PROTECT_FROM_MISSILES])
			return 1;
		if (prayers[PROTECT_FROM_MAGIC])
			return 2;
		if (prayers[RETRIBUTION])
			return 3;
		if (prayers[SMITE])
			return 4;
		if (prayers[REDEMPTION])
			return 5;
		if (prayers[PROTECT_FROM_SUMMONING])
			return 6;
		return -1;
	}
	
	/**
	 * Initializes the player's prayer drain once a first prayer
	 * has been selected.
	 * @param player	The player to start prayer drain for.
	 */
	private static void startDrain(final Player player) {
		if (getDrain(player) <= 0)
			return;
		GameServer.getTaskManager().submit(new Task() {
			@Override
			public void execute() {
				if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) <= 0) {
					for (int i = 0; i < player.getFields().getPrayerActive().length; i++) {
						if (player.getFields().getPrayerActive()[i])
							deactivatePrayer(player, i);
					}
					player.getPacketSender().sendMessage("You have run out of prayer points!");
					this.stop();
					return;
				}
				if (getDrain(player) <= 0) {
					this.stop();
					return;
				}
				int total = (int) (player.getSkillManager().getCurrentLevel(Skill.PRAYER) - getDrain(player));
				player.getSkillManager().setCurrentLevel(Skill.PRAYER, total, true);
			}
		});
	}
	
	/**
	 * Gets the amount of prayer to drain for <code>player</code>.
	 * @param player	The player to get drain amount for.
	 * @return			The amount of prayer that will be drained from the player.
	 */
	private static final double getDrain(Player player) {
		double toRemove = 0.0;
		for (int i = 0; i < player.getFields().getPrayerActive().length; i++) {
			if (player.getFields().getPrayerActive()[i]) {
				PrayerData prayerData = PrayerData.prayerData.get(i);
				toRemove += prayerData.drainRate / 10;
			}
		}
		//if (toRemove > 0) {
		//	toRemove /= (1 + (0.035 * c.playerBonus[11]));		
		//}
		return toRemove;
	}
	
	/**
	 * Checks if a player has no prayer on.
	 * @param player		The player to check prayer status for.
	 * @param exceptionId	The prayer id currently being turned on/activated.
	 * @return				if <code>true</code>, it means player has no prayer on besides <code>exceptionId</code>.
	 */
	private final static boolean hasNoPrayerOn(Player player, int exceptionId) {
		int prayersOn = 0;
		for (int i = 0; i < player.getFields().getPrayerActive().length; i++) {
			if (player.getFields().getPrayerActive()[i] && i != exceptionId)
				prayersOn++;
		}
		return prayersOn == 0;
	}

	/**
	 * Resets <code> prayers </code> with an exception for <code> prayerID </code>
	 * 
	 * @param prayers	The array of prayers to reset
	 * @param prayerID	The prayer ID to not turn off (exception)
	 */
	public static void resetPrayers(Player player, int[] prayers, int prayerID) {
		for (int i = 0; i < prayers.length; i++) {
			if (prayers[i] != prayerID)
				deactivatePrayer(player, prayers[i]);
		}
	}
	
	/**
	 * Checks if action button ID is a prayer button.
	 * 
	 * @param buttonId
	 * 						action button being hit.
	 */
	public static final boolean isButton(final int actionButtonID) {
		return PrayerData.actionButton.containsKey(actionButtonID);
	}
		
	public static final int THICK_SKIN = 0, BURST_OF_STRENGTH = 1, CLARITY_OF_THOUGHT = 2, SHARP_EYE = 3, MYSTIC_WILL = 4,
							ROCK_SKIN = 5, SUPERHUMAN_STRENGTH = 6, IMPROVED_REFLEXES = 7, RAPID_RESTORE = 8, RAPID_HEAL = 9, 
							PROTECT_ITEM = 10, HAWK_EYE = 11, MYSTIC_LORE = 12, STEEL_SKIN = 13, ULTIMATE_STRENGTH = 14,
							INCREDIBLE_REFLEXES = 15, PROTECT_FROM_SUMMONING = 16, PROTECT_FROM_MAGIC = 17, PROTECT_FROM_MISSILES = 18,
							PROTECT_FROM_MELEE = 19, EAGLE_EYE = 20, MYSTIC_MIGHT = 21, RETRIBUTION = 22, REDEMPTION = 23, SMITE = 24, CHIVALRY = 25,
							RAPID_RENEWAL = 26, PIETY = 27, RIGOUR = 28, AUGURY = 29;
	
	/**
	 * Contains every prayer that counts as a defense prayer.
	 */
	private static final int[] DEFENCE_PRAYERS = {THICK_SKIN, ROCK_SKIN, STEEL_SKIN, CHIVALRY, PIETY, RIGOUR, AUGURY};
	
	/**
	 * Contains every prayer that counts as a strength prayer.
	 */
	private static final int[] STRENGTH_PRAYERS = {BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, CHIVALRY, PIETY};
	
	/**
	 * Contains every prayer that counts as an attack prayer.
	 */
	private static final int[] ATTACK_PRAYERS = {CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, PIETY};
	
	/**
	 * Contains every prayer that counts as a ranged prayer.
	 */
	private static final int[] RANGED_PRAYERS = {SHARP_EYE, HAWK_EYE, EAGLE_EYE, RIGOUR};
	
	/**
	 * Contains every prayer that counts as a magic prayer.
	 */
	private static final int[] MAGIC_PRAYERS = {MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, AUGURY};
	
	/**
	 * Contains every prayer that counts as an overhead prayer, excluding protect from summoning.
	 */
	public static final int[] OVERHEAD_PRAYERS = {PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE, RETRIBUTION, REDEMPTION, SMITE};
	
	/**
	 * Contains every prayer that counts as an overhead prayer that isn't a 'protection' prayer.
	 */
	private static final int[] NON_PROTECTION_OVERHEAD_PRAYERS = {RETRIBUTION, REDEMPTION, SMITE};
}