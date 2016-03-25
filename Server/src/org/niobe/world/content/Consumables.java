package org.niobe.world.content;

import java.util.HashMap;
import java.util.Map;

import org.niobe.model.Animation;
import org.niobe.model.Item;
import org.niobe.world.Player;
import org.niobe.model.SkillManager.Skill;

/**
 * The consumables are foods/edible items in the game that
 * players can eat or drink.
 * 
 * @author relex lawl
 */
public final class Consumables {
	
	/**
	 * Checks if <code>item</code> is a valid consumable food type.
	 * @param player	The player clicking on <code>item</code>.
	 * @param item		The item being clicked upon.
	 * @param slot		The slot of the item.
	 * @return			If <code>true</code> player will proceed to eat said item.
	 */
	public static boolean isFood(Player player, Item item, int slot) {
		FoodType food = FoodType.types.get(item.getId());
		if (food != null) {
			eat(player, food, slot);
			return true;
		}
		return false;
	}
	
	/**
	 * Handles the player eating said food type.
	 * @param player	The player eating the consumable.
	 * @param food		The food type being consumed.
	 * @param slot		The slot of the food being eaten.
	 */
	private static void eat(Player player, FoodType food, int slot) {
		if (food != null && (System.currentTimeMillis() - player.getFields().getFoodDelay()) >= 1800) {
			if (player.getFields().getDialogue() != null) {
				player.getFields().getDialogue().finish(player);
			}
			player.getFields().getCombatAttributes().addAttackDelay(2);
			player.performAnimation(new Animation(829));
			player.getInventory().delete(food.item, slot);
			int heal = food.heal;
			if (food == FoodType.ROCKTAIL) {
				int max = (player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) + 100);
				if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) >= (player.getSkillManager().getMaxLevel(Skill.CONSTITUTION))) {
					heal = 100;
				}
				if (player.getConstitution() + heal > max) {
					heal = player.getConstitution() - max > 0 ? player.getConstitution() - max : max - player.getConstitution();
				}
			} else {
				if (heal + player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) > player.getSkillManager().getMaxLevel(Skill.CONSTITUTION)) {
					heal = player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) - player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION);
				}
			}
			player.getFields().setFoodDelay(System.currentTimeMillis());
			player.getPacketSender().sendMessage("You eat the " + food.name + ".");
			player.setConstitution(player.getConstitution() + heal);
		}
	}
	
	/**
	 * Represents a valid consumable item.
	 * 
	 * @author relex lawl
	 */
	private enum FoodType {
		/**
		 * Fish food types players can get by fishing
		 * or purchasing from other entities.
		 */
		ANCHOVIES(new Item(319), 10),
		SHRIMPS(new Item(315), 30),
		SARDINE(new Item(325), 40),
		COD(new Item(339), 70),
		TROUT(new Item(333), 70),
		PIKE(new Item(351), 80),
		SALMON(new Item(329), 90),
		TUNA(new Item(361), 100),
		LOBSTER(new Item(379), 120),
		SWORDFISH(new Item(373), 140),
		MONKFISH(new Item(7946), 160),
		SHARK(new Item(385), 200),
		SEA_TURTLE(new Item(397), 210),
		MANTA_RAY(new Item(391), 220),
		CAVEFISH(new Item(15266), 230),
		ROCKTAIL(new Item(15272), 230),
		
		/**
		 * Baked goods food types a player
		 * can make with the cooking skill.
		 */
		POTATO(new Item(1942), 10),
		BAKED_POTATO(new Item(6701), 40),
		POTATO_WITH_BUTTER(new Item(6703), 140),
		CHILLI_POTATO(new Item(7054), 140),
		EGG_POTATO(new Item(7056), 160),
		POTATO_WITH_CHEESE(new Item(6705), 160),
		MUSHROOM_POTATO(new Item(7058), 200),
		TUNA_POTATO(new Item(7060), 220),
		
		/**
		 * Fruit food types which a player can get
		 * by picking from certain trees or hand-making
		 * them (such as pineapple chunks/rings).
		 */
		BANANA(new Item(1963), 20),
		ORANGE(new Item(2108), 20),
		PINEAPPLE_CHUNKS(new Item(2116), 20),
		PINEAPPLE_RINGS(new Item(2118), 20),
		PEACH(new Item(6883), 80),
		
		/**
		 * Dungeoneering food types, which you can get
		 * in the Dungeoneering skill dungeons.
		 */
		HEIM_CRAB(new Item(18159), 20),
		RED_EYE(new Item(18161), 50),
		DUSK_EEL(new Item(18163), 70),
		GIANT__FLATFISH(new Item(18165), 100),
		SHORT__FINNED_EEL(new Item(18167), 120),
		WEB_SNIPPER(new Item(18169), 150),
		BOULDABASS(new Item(18171), 170),
		SALVE_EEL(new Item(18173), 200),
		BLUE_CRAB(new Item(18175), 220),
		
		/**
		 * Other food types.
		 */
		PURPLE_SWEETS(new Item(4561), 30),
		OKTOBERTFEST_PRETZEL(new Item(19778), 120);
		
		private FoodType(Item item, int heal) {
			this.item = item;
			this.heal = heal;
			this.name = (toString().toLowerCase().replaceAll("__", "-").replaceAll("_", " "));
		}
		
		private Item item;
		
		private int heal;
		
		private String name;
		
		private static Map<Integer, FoodType> types = new HashMap<Integer, FoodType>();
		
		static {
			for (FoodType type : FoodType.values()) {
				types.put(type.item.getId(), type);
			}
		}
	}
}
