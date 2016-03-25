package org.niobe.world.content;

import java.util.HashMap;
import java.util.Map;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.Graphic;
import org.niobe.model.Item;
import org.niobe.model.MovementQueue.MovementFlag;
import org.niobe.model.SkillManager;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.container.impl.Equipment;
import org.niobe.task.Task;
import org.niobe.world.Player;

/**
 * Handles a player's skill cape emote.
 * 
 * @author relex lawl
 */
public final class SkillCapeEmotes {
	
	/**
	 * Does the emote for the associated player's said cape.
	 * @param player	The player attempting to use the emote.
	 */
	public static void doEmote(final Player player) {
		Item cape = player.getEquipment().getItems()[Equipment.CAPE_SLOT];
		Data data = Data.dataMap.get(cape.getId());
		if (data != null) {
			if (System.currentTimeMillis() - player.getFields().getGraphicDelay() < 2400) {
				player.getPacketSender().sendMessage("You are already performing an emote!");
				return;
			}
			if (data != Data.QUEST_POINT) {
				Skill skill = Skill.forId(data.ordinal());
				if (data == Data.DUNGEONEERING_MASTER)
					skill = Skill.DUNGEONEERING;
				int level = SkillManager.getMaxAchievingLevel(skill);
				if (player.getSkillManager().getMaxLevel(skill) < level) {
					player.getPacketSender().sendMessage("You need a " + skill.getName() + " level of " + level + " to do this!");
					return;
				}
			}
			player.getMovementQueue().stopMovement();
			player.getMovementQueue().setMovementFlag(MovementFlag.CANNOT_MOVE);
			if (data != Data.DUNGEONEERING && data != Data.DUNGEONEERING_MASTER) {
				player.performAnimation(data.animation);
				player.performGraphic(data.graphic);
				player.getFields().setGraphicDelay(System.currentTimeMillis());
				GameServer.getTaskManager().submit(new Task(data.delay) {
					@Override
					public void execute() {
						player.getMovementQueue().setMovementFlag(MovementFlag.NONE);
						stop();
					}
				});
			} else {
				dungeoneeringEmote(player, data);
			}
			return;
		}
		player.getPacketSender().sendMessage("You cannot perform this emote!");
	}
	
	/**
	 * Does the dungeoneering emotes.
	 * @param player	The player doing the emote.
	 * @param data		The skill cape data needed to specify if master emote or normal emote.
	 */
	private static void dungeoneeringEmote(Player player, Data data) {
		if (data == Data.DUNGEONEERING) {
			
		} else if (data == Data.DUNGEONEERING_MASTER) {
			
		}
	}
	
	/**
	 * Represents a skill capes configs.
	 * 
	 * @author relex lawl
	 */
	private enum Data {
		ATTACK(new int[] {9747, 9748, 10639},
				4959, 823, 7),
		DEFENCE(new int[] {9753, 9754, 10641},
				4961, 824, 10),
		STRENGTH(new int[] {9750, 9751, 10640},
				4981, 828, 25),
		CONSTITUTION(new int[] {9768, 9769, 10647},
				14252, 2745, 12),
		RANGED(new int[] {9756, 9757, 10642},
				4973, 832, 12),
		PRAYER(new int[] {9759, 9760, 10643},
				4979, 829, 15),
		MAGIC(new int[] {9762, 9763, 10644},
				4939, 813, 6),
		COOKING(new int[] {9801, 9802, 10658},
				4955, 821, 36),
		WOODCUTTING(new int[] {9807, 9808, 10660},
				4957, 822, 25),
		FLETCHING(new int[] {9783, 9784, 10652},
				4937, 812, 20),
		FISHING(new int[] {9798, 9799, 10657},
				4951, 819, 19),
		FIREMAKING(new int[] {9804, 9805, 10659},
				4975, 831, 14),
		CRAFTING(new int[] {9780, 9781, 10651},
				4949, 818, 15),
		SMITHING(new int[] {9795, 9796, 10656},
				4943, 815, 23),
		MINING(new int[] {9792, 9793, 10655},
				4941, 814, 8),
		HERBLORE(new int[] {9774, 9775, 10649},
				4969, 835, 16),
		AGILITY(new int[] {9771, 9772, 10648},
				4977, 830, 8),
		THIEVING(new int[] {9777, 9778, 10650},
				4965, 826, 16),
		SLAYER(new int[] {9786, 9787, 10653},
				4967, 1656, 8),
		FARMING(new int[] {9810, 9811, 10661},
				4963, 825, 16),
		RUNECRAFTING(new int[] {9765, 9766, 10645},
				4947, 817, 10),
		HUNTER(new int[] {9948, 9949, 10646},
				5158, 907, 14),
		CONSTRUCTION(new int[] {9789, 9790, 10654},
				4953, 820, 16),
		SUMMONING(new int[] {12169, 12170, 12524},
				8525, 1515, 10),
		DUNGEONEERING(new int[] {15706, 18508, 18509},
				-1, -1, -1),
		DUNGEONEERING_MASTER(new int[] {19709, 19710},
				-1, -1, -1),
		QUEST_POINT(new int[] {9813, 9814, 10662},
				4945, 816, 19);
		
		private Data(int[] itemId, int animationId, int graphicId, int delay) {
			item = new Item[itemId.length];
			for (int i = 0; i < itemId.length; i++) {
				item[i] = new Item(itemId[i]);
			}
			animation = new Animation(animationId);
			graphic = new Graphic(graphicId);
			this.delay = delay;
		}
		
		private final Item[] item;
		
		private final Animation animation;
		
		private final Graphic graphic;
		
		private final int delay;
		
		private static Map<Integer, Data> dataMap = new HashMap<Integer, Data>();
		
		static {
			for (Data data : Data.values()) {
				for (Item item : data.item) {
					dataMap.put(item.getId(), data);
				}
			}
		}
	}
}
