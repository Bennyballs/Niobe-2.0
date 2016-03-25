package org.niobe.world.content.combat;

import java.util.HashMap;
import java.util.Map;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.Damage.CombatIcon;
import org.niobe.model.Damage.Hit;
import org.niobe.model.Damage.Hitmask;
import org.niobe.model.Graphic;
import org.niobe.model.SkillManager.Skill;
import org.niobe.task.Task;
import org.niobe.util.MathUtil;
import org.niobe.util.NameUtil;
import org.niobe.world.Player;
import org.niobe.world.content.BonusManager.OtherBonus;

public class CombatCurses {
	
	public static boolean isButton(Player player, int buttonId) {
		if (CurseData.buttons.containsKey(buttonId)) {
			CurseData curse = CurseData.buttons.get(buttonId);
			if (player.getFields().getPrayerActive()[curse.ordinal()])
				deactivateCurse(player, curse);
			else
				activateCurse(player, curse);
			return true;
		}
		return false;
	}
	
	public static void activateCurse(Player player, int curseId) {
		CurseData data = CurseData.ids.get(curseId);
		if (data != null)
			activateCurse(player, data);
	}
	
	public static void deactivateCurse(Player player, int curseId) {
		CurseData data = CurseData.ids.get(curseId);
		if (data != null)
			deactivateCurse(player, data);
	}
	
	public static void activateCurse(Player player, CurseData curse) {
		if (player.getFields().getPrayerActive()[curse.ordinal()])
			return;
		if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) <= 0) {
			player.getPacketSender().sendConfig(curse.configId, 0);
			player.getPacketSender().sendMessage("You do not have enough prayer points. Recharge at an altar.");
			return;
		}
		if (player.getSkillManager().getMaxLevel(Skill.PRAYER) < (curse.requirement * 10)) {
			player.getPacketSender().sendConfig(curse.configId, 0);
			//DialogueManager.sendOneStringStatement(p, "               " + "You need a @blu@Prayer <col=0>level of " + pd.levelRequirement +" to use @blu@" + pd.getPrayerName());
			player.getPacketSender().sendMessage("You need a prayer level of " + curse.requirement + " to use " + curse.name + ".");
			return;
		}
		if (curse == CurseData.TURMOIL && player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 40) {
			player.getPacketSender().sendConfig(curse.configId, 0);
			player.getPacketSender().sendMessage("You need a defence level of 40 or above to use turmoil.");
			return;
		}
		switch (curse) {
		case SAP_WARRIOR:
		case LEECH_ATTACK:
			deactivateCurses(player, ACCURACY_CURSES);
			break;
		case SAP_RANGER:
		case LEECH_RANGED:
			deactivateCurses(player, RANGED_CURSES);
			break;
		case SAP_MAGE:
		case LEECH_MAGIC:
			deactivateCurses(player, MAGIC_CURSES);
			break;
		case SAP_SPIRIT:
		case LEECH_SPECIAL_ATTACK:
			deactivateCurses(player, SPECIAL_ATTACK_CURSES);
			break;
		case LEECH_DEFENCE:
			deactivateCurses(player, DEFENSE_CURSES);
			break;
		case LEECH_STRENGTH:
			deactivateCurses(player, STRENGTH_CURSES);
			break;
		case DEFLECT_SUMMONING:
			deactivateCurses(player, NON_DEFLECT_OVERHEAD_CURSES);
			break;
		case WRATH:
		case SOUL_SPLIT:
			deactivateCurses(player, OVERHEAD_CURSES);
			deactivateCurse(player, CurseData.DEFLECT_SUMMONING);
			break;
		case DEFLECT_MAGIC:
		case DEFLECT_MISSILES:
		case DEFLECT_MELEE:
			deactivateCurses(player, OVERHEAD_CURSES);
			break;
		case TURMOIL:
			deactivateCurses(player, COMBAT_CURSES);
			break;
		}
		if (player.getFields().isOverheadPrayerCap()) {
			if (curse == CurseData.DEFLECT_MAGIC || curse == CurseData.DEFLECT_MELEE ||
					curse == CurseData.DEFLECT_MISSILES || curse == CurseData.DEFLECT_SUMMONING) {
				player.getPacketSender().sendMessage("You have been injured and cannot use this prayer!");
				return;
			}
		}
		if (curse.prayerAnimation != null) {
			player.performAnimation(curse.prayerAnimation.animation);
			player.performGraphic(curse.prayerAnimation.graphic);
		}
		player.getFields().setPrayerActive(curse.ordinal(), true);
		player.getPacketSender().sendConfig(curse.configId, 1);
		int hintId = getHeadHint(player);
		if (hintId != -1) {
			player.getAppearance().setHeadHint(hintId);
		}
		if (noActiveCurse(player, curse))
			startDrain(player);
	}
	
	public static void deactivateCurse(Player player, CurseData curse) {
		if (!player.getFields().getPrayerActive()[curse.ordinal()])
			return;
		player.getPacketSender().sendConfig(curse.configId, 0);
		player.getFields().setPrayerActive(curse.ordinal(), false);
		player.getAppearance().setHeadHint(getHeadHint(player));
	}
	
	public static void deactivateCurses(Player player) {
		for (CurseData curse : CurseData.values()) {
			if (player.getFields().getPrayerActive()[curse.ordinal()]) {
				deactivateCurse(player, curse);
			}
		}
	}
	
	private static void deactivateCurses(Player player, CurseData[] curses) {
		for (CurseData curse : curses) {
			if (player.getFields().getPrayerActive()[curse.ordinal()]) {
				deactivateCurse(player, curse);
			}
		}
	}
	
	private static boolean noActiveCurse(Player player, CurseData exception) {
		for (CurseData data : CurseData.values()) {
			if (player.getFields().getPrayerActive()[data.ordinal()] && data != exception) {
				return false;
			}
		}
		return true;
	}
	
	private static int getHeadHint(Player player) {
		boolean[] active = player.getFields().getPrayerActive();
		if (active[CurseData.DEFLECT_SUMMONING.ordinal()]) {
			if (active[CurseData.DEFLECT_MELEE.ordinal()])
				return 14;
			if (active[CurseData.DEFLECT_MISSILES.ordinal()])
				return 15;
			if (active[CurseData.DEFLECT_MAGIC.ordinal()])
				return 16;
			return 13;
		}
		if (active[CurseData.DEFLECT_MELEE.ordinal()])
			return 10;
		if (active[CurseData.DEFLECT_MAGIC.ordinal()])
			return 11;
		if (active[CurseData.DEFLECT_MISSILES.ordinal()])
			return 12;
		if (active[CurseData.WRATH.ordinal()])
			return 17;
		if (active[CurseData.SOUL_SPLIT.ordinal()])
			return 18;
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
					for (CurseData curse : CurseData.values()) {
						if (player.getFields().getPrayerActive()[curse.ordinal()]) {
							deactivateCurse(player, curse);
						}
					}
					player.getPacketSender().sendMessage("You have run out of prayer points!");
					this.stop();
					return;
				}
				double drain = getDrain(player);
				if (drain <= 0) {
					this.stop();
					return;
				}
				int total = (int) (player.getSkillManager().getCurrentLevel(Skill.PRAYER) - drain);
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
		double toRemove = 0;
		for (CurseData curse : CurseData.values()) {
			if (player.getFields().getPrayerActive()[curse.ordinal()]) {
				toRemove += curse.drainRate / 10;
			}
		}
		if (toRemove > 0) {
			toRemove /= (1 + (0.035 * player.getFields().getBonusManager().getOtherBonus()[OtherBonus.PRAYER.ordinal()]));		
		}
		return toRemove;
	}
	
	/**
	 * Handles the death of a player, to handle things
	 * such as wrath, redemption, etc.
	 * @param player	The {@link org.niobe.world.Player} being killed.
	 */
	public static void deathHook(Player player) {
		boolean[] active = player.getFields().getPrayerActive();
		if (active[WRATH]) {
			player.performGraphic(new Graphic(2259));
			Graphic graphic = new Graphic(2260);
			for (Player target : GameServer.getWorld().getPlayers()) {
				if (target == null)
					continue;
				int distance = player.getPosition().getDistance(target.getPosition());
				if (distance <= 32 && distance != 0) {
					for (int x = 0; x < 5; x++) {
						for (int y = 0; y < 5; y++) {
							target.getPacketSender().sendGraphic(graphic, player.getPosition().copy().add(x - 2, y - 2));
						}
					}
				}
				if (target != player) {
					int damage = MathUtil.random((player.getSkillManager().getMaxLevel(Skill.PRAYER) / 10) * 3);
					if (CombatManager.inMultiCombatArea(target)) {
						if (distance <= 5) {
							target.setDamage(new Damage(new Hit(damage, CombatIcon.NONE, Hitmask.LOWEST_DAMAGE)));
						}
					} else {
						if (player.getFields().getCombatAttributes().getAttackedBy() == target) {
							target.setDamage(new Damage(new Hit(damage, CombatIcon.NONE, Hitmask.LOWEST_DAMAGE)));
						}
					}
				}
			}
		}
	}
	
	private enum CurseData {
		PROTECT_ITEM(50, .6, 21357, 910, new PrayerAnimation(new Animation(12567), new Graphic(2213))),
		SAP_WARRIOR(50, .9, 21359, 911),
		SAP_RANGER(52, .9, 21361, 912),
		SAP_MAGE(54, .9, 21363, 913),
		SAP_SPIRIT(56, .9, 21365, 914),
		BERSERKER(59, 1.2, 21367, 915, new PrayerAnimation(new Animation(12589), new Graphic(2266))),
		DEFLECT_SUMMONING(62, 1.25, 21369, 916),
		DEFLECT_MAGIC(65, 1.5, 21371, 917),
		DEFLECT_MISSILES(68, 1.5, 21373, 918),
		DEFLECT_MELEE(71, 1.5, 21375, 919),
		LEECH_ATTACK(74, 2.5, 21377, 920),
		LEECH_RANGED(76, 2.5, 21379, 921),
		LEECH_MAGIC(78, 2.5, 21381, 922),
		LEECH_DEFENCE(80, 2.5, 21383, 923),
		LEECH_STRENGTH(82, 2.5, 21385, 924),
		LEECH_ENERGY(84, 2.5, 21387, 925),
		LEECH_SPECIAL_ATTACK(86, 2.5, 21389, 926),
		WRATH(89, 2, 21391, 927),
		SOUL_SPLIT(92, 4, 21393, 928),
		TURMOIL(95, 10, 21395, 929, new PrayerAnimation(new Animation(12565), new Graphic(2226)));
		
		private CurseData(int requirement, double drainRate, int buttonId, int configId, PrayerAnimation...animations)  {
			this.requirement = requirement;
			this.drainRate = drainRate;
			this.buttonId = buttonId;
			this.configId = configId;
			this.prayerAnimation = animations.length > 0 ? animations[0] : null;
			this.name = NameUtil.capitalizeWords(toString().toLowerCase().replaceAll("_", " "));
		}
		
		private final int requirement;
		
		private final double drainRate;
		
		private final int buttonId;
		
		private final int configId;
		
		private final PrayerAnimation prayerAnimation;
				
		private final String name;
		
		private static final Map<Integer, CurseData> buttons = new HashMap<Integer, CurseData>();
		
		private static final Map<Integer, CurseData> ids = new HashMap<Integer, CurseData>();
			
		static {
			for (CurseData data : CurseData.values()) {
				buttons.put(data.buttonId, data);
				ids.put(data.ordinal(), data);
			}
		}
	}
	
	private static final CurseData[] ACCURACY_CURSES = {
		CurseData.SAP_WARRIOR,
		CurseData.LEECH_ATTACK,
		CurseData.TURMOIL
	};
	
	private static final CurseData[] RANGED_CURSES = {
		CurseData.SAP_RANGER,
		CurseData.LEECH_RANGED,
		CurseData.TURMOIL
	};
	
	private static final CurseData[] MAGIC_CURSES = {
		CurseData.SAP_MAGE,
		CurseData.LEECH_MAGIC,
		CurseData.TURMOIL
	};
	
	private static final CurseData[] SPECIAL_ATTACK_CURSES = {
		CurseData.SAP_SPIRIT,
		CurseData.LEECH_SPECIAL_ATTACK
	};
	
	private static final CurseData[] DEFENSE_CURSES = {
		CurseData.LEECH_DEFENCE,
		CurseData.TURMOIL
	};
	
	private static final CurseData[] STRENGTH_CURSES = {
		CurseData.LEECH_STRENGTH,
		CurseData.TURMOIL
	};
	
	private static final CurseData[] OVERHEAD_CURSES = {
		CurseData.DEFLECT_MAGIC,
		CurseData.DEFLECT_MISSILES,
		CurseData.DEFLECT_MELEE,
		CurseData.WRATH,
		CurseData.SOUL_SPLIT
	};
	
	private static final CurseData[] NON_DEFLECT_OVERHEAD_CURSES = {
		CurseData.WRATH,
		CurseData.SOUL_SPLIT
	};
	
	private static final CurseData[] COMBAT_CURSES = {
		CurseData.SAP_WARRIOR,
		CurseData.LEECH_ATTACK,
		CurseData.SAP_RANGER,
		CurseData.LEECH_RANGED,
		CurseData.SAP_MAGE,
		CurseData.LEECH_MAGIC,
		CurseData.LEECH_DEFENCE,
		CurseData.LEECH_STRENGTH
	};
	
	public static final int PROTECT_ITEM = 0, SAP_WARRIOR = 1, SAP_RANGER = 2, SAP_MAGE = 3, SAP_SPIRIT = 4,
			BERSERKER = 5, DEFLECT_SUMMONING = 6, DEFLECT_MAGIC = 7, DEFLECT_MISSILES = 8,
			DEFLECT_MELEE = 9, LEECH_ATTACK = 10, LEECH_RANGED = 11, LEECH_MAGIC = 12,
			LEECH_DEFENCE = 13, LEECH_STRENGTH = 14, LEECH_ENERGY = 15, LEECH_SPECIAL_ATTACK = 16,
			WRATH = 17, SOUL_SPLIT = 18, TURMOIL = 19;
	
	private static final class PrayerAnimation {
		
		private PrayerAnimation(Animation animation, Graphic graphic) {
			this.animation = animation;
			this.graphic = graphic;
		}
		
		private final Animation animation;
		
		private final Graphic graphic;
	}
}
