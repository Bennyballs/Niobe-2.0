package org.niobe.world.content;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.niobe.model.Item;
import org.niobe.util.MathUtil;
import org.niobe.world.Mob;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This file is used to store an mob's drops that it will drop
 * upon death produced by a player.
 * 
 * @author relex lawl
 */
public final class MobDropTable {
	
	/**
	 * The directory which leads to the mob_drops xml file.
	 */
	private static final String FILE_DIRECTORY = "./data/mob_drops.xml";
	
	/**
	 * A map storing all the mobIds as keys and drops as their value.
	 */
	private static final Map<Integer, Drop[]> drops = new HashMap<Integer, Drop[]>();
	
	/**
	 * Loads all the mobs drop tables from the external file.
	 */
	public static void init() {
		long startup = System.currentTimeMillis();
		try {
			System.out.println("Loading mob drop tables...");
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(FILE_DIRECTORY);
			NodeList tables = doc.getElementsByTagName("mob");
			for (int i = 0; i < tables.getLength(); i++) {
				Element mob = (Element) tables.item(i);
				int mobId = Integer.parseInt(mob.getAttribute("mobId"));
				NodeList drops = mob.getElementsByTagName("drop");
				Drop[] dropArray = new Drop[drops.getLength()];
				for (int j = 0; j < drops.getLength(); j++) {
					Element drop = (Element) drops.item(j);
					int itemId = Integer.parseInt(drop.getAttribute("itemId"));
					int itemAmount = Integer.parseInt(drop.getAttribute("itemAmount"));
					int amountAddition = !drop.getAttribute("itemAmountAddition").equals("") ? Integer.parseInt(drop.getAttribute("itemAmountAddition")) : 0;
					Factor factor = Factor.valueOf(drop.getAttribute("factor"));
					int rolls = factor != Factor.ALWAYS ? Integer.parseInt(drop.getAttribute("rolls")) : 1;
					dropArray[dropArray.length - j - 1] = new Drop(new Item(itemId, itemAmount), factor, rolls, amountAddition);
				}
				MobDropTable.drops.put(mobId, dropArray);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		System.out.println("Loaded " + drops.size() + " mob drop table" + (drops.size() > 1 ? "s" : "") + " in " + (System.currentTimeMillis() - startup) + "ms");
	}
	
	/**
	 * Sets the mob's drops to a random generated drop list.
	 * @param mob	The mob to set drops for.
	 */
	public static void attachDrops(Mob mob) {
		mob.setDrops(generateNpcDrops(mob));
	}
	
	/**
	 * Generates random drops from the <code>mob</code>'s drop table.
	 * @param mob	The mob to grab random drop for.
	 * @return		Random drops from the mob's drop table.
	 */
	private static List<Item> generateNpcDrops(Mob mob) {
		Drop[] drops = MobDropTable.drops.get(mob.getDefinition().getId());
		List<Item> generated_drops = new LinkedList<Item>();
		if (drops == null)
			return generated_drops;
		for (Drop drop : drops) {
			if (drop != null && (drop.factor == Factor.ALWAYS || drop.success())) {
				Item item = drop.item.setAmount(drop.item.getAmount() + drop.itemAmountAddition);
				generated_drops.add(item);
			}
		}
		return generated_drops;
	}
	
	/**
	 * Represents the 'rarity' of a drop.
	 * 
	 * @author relex lawl
	 */
	private enum Factor {
		ALWAYS,
		COMMON,
		UNCOMMON,
		RARE,
		VERY_RARE;
	}
	
	/**
	 * Represents a 'drop' from an mob's drop table.
	 * 
	 * @author relex lawl
	 */
	private static class Drop {
		
		/**
		 * The Drop constructor.
		 * @param item		The item that will be dropped upon mob's death.
		 * @param factor	The rarity of the item.
		 * @param rolls		The amount of times the theoretical 'die' will be rolled.
		 */
		private Drop(Item item, Factor factor, int rolls) {
			this.item = item;
			this.factor = factor;
			this.rolls = rolls;
		}
		
		/**
		 * The Drop constructor.
		 * @param item					The item that will be dropped upon mob's death.
		 * @param factor				The rarity of the item.
		 * @param rolls					The amount of times the theoretical 'die' will be rolled.
		 * @param itemAmountAddition	The random amount that can be added to the item, used for drops such as coins, where amount can vary.
		 */
		private Drop(Item item, Factor factor, int rolls, int itemAmountAddition) {
			this(item.setAmount(item.getAmount()), factor, rolls);
			this.itemAmountAddition = MathUtil.random(itemAmountAddition);
		}
		
		/**
		 * The drop's 'item' that will be dropped.
		 */
		private Item item;
		
		/**
		 * The rarity of the drop.
		 */
		private Factor factor;
		
		/**
		 * The amount of times the 'die' will be rolled.
		 */
		private int rolls;
		
		/**
		 * The random amount an item can have when dropped.
		 */
		private int itemAmountAddition;
		
		/**
		 * This flag checks if this 'drop' will be included in 
		 * said mob's drop table.
		 * @return	If <code>true</code> the mob will have this 'drop'.
		 */
		private boolean success() {
			boolean success = true;
			while (rolls > 0) {
				int random = MathUtil.random(factor.ordinal());
				if (random != 0) {
					success = false;
					break;
				}
				rolls--;
			}
			return success;
		}
	}
}