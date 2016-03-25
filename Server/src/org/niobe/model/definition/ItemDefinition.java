package org.niobe.model.definition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.niobe.model.container.impl.Equipment;
import org.niobe.world.content.combat.action.RangedCombatAction.ArrowType;
import org.niobe.world.content.combat.action.RangedCombatAction.BowType;
import org.niobe.world.content.combat.action.RangedCombatAction.RangedWeaponType;

/**
 * This file manages every item definition, which includes
 * their name, description, value, skill requirements, etc.
 * 
 * @author relex lawl
 */
public final class ItemDefinition {
	
	/**
	 * The directory in which item definitions are found.
	 */
	private static final String FILE_DIRECTORY = "./data/cache/item_definitions.txt";

	/**
	 * The max amount of items that will be loaded.
	 */
	private static final int MAX_AMOUNT_OF_ITEMS = 20694;
	
	/**
	 * ItemDefinition array containing all items' definition values.
	 */
	private static final ItemDefinition[] definitions = new ItemDefinition[MAX_AMOUNT_OF_ITEMS];
	
	/**
	 * Reads the file located in {@code FILE_DIRECTORY} and
	 * populates the {@code definitions} array.
	 */
	public static void init() {
		long startup = System.currentTimeMillis();
		int amount = 0;
		ItemDefinition definition = definitions[0];
		try {
			System.out.println("Loading item definitions...");
			File file = new File(FILE_DIRECTORY);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("inish")) {
					definitions[definition.id] = definition;
					continue;
				}
				String[] args = line.split(": ");
				if (args.length <= 1)
					continue;
				String token = args[0], value = args[1];
				if (line.contains("Bonus[")) {
					String[] other = line.split("]");
					int index = Integer.valueOf(line.substring(6, other[0].length()));
					double bonus = Double.valueOf(value);
					definition.bonus[index] = bonus;
					continue;
				}
				if (line.contains("Requirement[")) {
					String[] other = line.split("]");
					int index = Integer.valueOf(line.substring(12, other[0].length()));
					int requirement = Integer.valueOf(value);
					definition.requirement[index] = requirement;
					continue;
				}
				switch (token.toLowerCase()) {
				case "item id":
					int id = Integer.valueOf(value);
					definition = new ItemDefinition();
					definition.id = id;
					amount++;
					break;
				case "name":
					definition.name = value;
					break;
				case "examine":
					definition.description = value;
					break;
				case "value":
					definition.value = Integer.valueOf(value);
					break;
				case "low alchemy":
					definition.lowAlchemy = Integer.valueOf(value);
					break;
				case "high alchemy":
					definition.highAlchemy = Integer.valueOf(value);
					break;
				case "weight":
					definition.weight = Double.valueOf(value);
					break;
				case "stackable":
					definition.stackable = Boolean.valueOf(value);
					break;
				case "noted":
					definition.noted = Boolean.valueOf(value);
					break;
				case "double-handed":
					definition.isTwoHanded = Boolean.valueOf(value);
					break;
				case "equipment type":
					definition.equipmentType = EquipmentType.valueOf(value);
					break;
				case "bow type":
					definition.bowType = BowType.valueOf(value);
					break;
				case "ranged weapon type":
					definition.rangedWeaponType = RangedWeaponType.valueOf(value);
					break;
				case "arrow type":
					definition.arrowType = ArrowType.valueOf(value);
					break;
				case "is weapon":
					definition.weapon = Boolean.valueOf(value);
					break;
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Loaded " + amount + " item definitions in " + (System.currentTimeMillis() - startup) + "ms");
	}
	
	/**
	 * Writes the current item definitions and replaces
	 * the current one.
	 * @throws IOException
	 */
	public static void writeDefinitions() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_DIRECTORY));
		for (ItemDefinition definition : ItemDefinition.definitions) {
			if (definition == null)
				continue;
			writer.write("Item Id: " + definition.getId());
			writer.newLine();
			writer.write("Name: " + definition.getName());
			writer.newLine();
			writer.write("Examine: " + definition.getDescription());
			writer.newLine();
			writer.write("Value: " + definition.getValue());
			writer.newLine();
			writer.write("Low alchemy: " + definition.getLowAlchemyValue());
			writer.newLine();
			writer.write("High alchemy: " + definition.getHighAlchemyValue());
			writer.newLine();
			writer.write("Weight: " + definition.getWeight());
			writer.newLine();
			writer.write("Stackable: " + definition.isStackable());
			writer.newLine();
			writer.write("Noted: " + definition.isNoted());
			writer.newLine();
			writer.write("Double-handed: " + definition.isTwoHanded());
			writer.newLine();
			writer.write("Equipment type: " + definition.getEquipmentType());
			writer.newLine();
			if (definition.bowType != null) {
				writer.write("Bow type: " + definition.bowType);
				writer.newLine();
			}
			if (definition.rangedWeaponType != null) {
				writer.write("Ranged weapon type: " + definition.rangedWeaponType);
				writer.newLine();
			}
			if (definition.arrowType != null) {
				writer.write("Arrow type: " + definition.arrowType);
				writer.newLine();
			}
			writer.write("Is Weapon: " + definition.isWeapon());
			writer.newLine();
			for (int i = 0; i < definition.bonus.length; i++) {
				if (definition.bonus[i] != 0) {
					writer.write("Bonus[" + i + "]: " + definition.bonus[i]);
					writer.newLine();
				}
			}
			for (int i = 0; i < definition.requirement.length; i++) {
				if (definition.requirement[i] > 0) {
					writer.write("Requirement[" + i + "]: " + definition.requirement[i]);
					writer.newLine();
				}
			}
			writer.write("finish");
			writer.newLine();
			writer.newLine();
		}
		writer.close();
	}
	
	/**
	 * Gets the item definitions available.
	 * @return	The definitions.
	 */
	public static ItemDefinition[] getDefinitions() {
		return definitions;
	}
	
	/**
	 * Gets the item definition correspondent to the id.
	 * 
	 * @param id	The id of the item to fetch definition for.
	 * @return		definitions[id].
	 */
	public static ItemDefinition forId(int id) {
		return (id < 0 || id > definitions.length || definitions[id] == null) ? new ItemDefinition() : definitions[id];
	}
	
	/**
	 * Gets the max amount of items that will be loaded
	 * in Niobe.
	 * @return	The maximum amount of item definitions loaded.
	 */
	public static int getMaxAmountOfItems() {
		return MAX_AMOUNT_OF_ITEMS;
	}
	
	/**
	 * The id of the item.
	 */
	private int id = 0;
	
	/**
	 * Gets the item's id.
	 * 
	 * @return id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * The name of the item.
	 */
	private String name = "None";
	
	/**
	 * Gets the item's name.
	 * 
	 * @return name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * The item's description.
	 */
	private String description = "Null";
	
	/**
	 * Gets the item's description.
	 * 
	 * @return	description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Flag to check if item is stackable.
	 */
	private boolean stackable;
	
	/**
	 * Checks if the item is stackable.
	 * 
	 * @return	stackable.
	 */
	public boolean isStackable() {
		return stackable;
	}
	
	/**
	 * The item's shop value.
	 */
	private int value;
	
	/**
	 * Gets the item's shop value.
	 * 
	 * @return	value.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * The item's low alchemy value.
	 */
	private int lowAlchemy;
	
	/**
	 * Gets the item's low alchemy value.
	 * @return lowAlchemy.
	 */
	public int getLowAlchemyValue() {
		return lowAlchemy;
	}
	
	/**
	 * The item's high alchemy value.
	 */
	private int highAlchemy;
	
	/**
	 * The item's weight.
	 */
	private double weight = 0.0;
	
	/**
	 * Gets the item's weight.
	 * @return	The weight.
	 */
	public double getWeight() {
		return weight;
	}
	
	/**
	 * Gets the item's high alchemy value.
	 * @return highAlchemy.
	 */
	public int getHighAlchemyValue() {
		return highAlchemy;
	}
	
	/**
	 * Gets the item's equipment slot index.
	 * 
	 * @return	equipmentSlot.
	 */
	public int getEquipmentSlot() {
		return equipmentType.slot;
	}
	
	/**
	 * Flag that checks if item is noted.
	 */
	private boolean noted;
	
	/**
	 * Checks if item is noted.
	 * 
	 * @return noted.
	 */
	public boolean isNoted() {
		return noted;
	}
		
	/**
	 * Flag that checks if weapon is two-handed.
	 */
	private boolean isTwoHanded;
	
	/**
	 * Checks if item is two-handed
	 */
	public boolean isTwoHanded() {
		return isTwoHanded;
	}
	
	/**
	 * Flag that checks if the item belongs
	 * in the {@link org.niobe.model.container.Equipment.WEAPON_SLOT},
	 * used because the default {@link EquipmentType} is {@link EquipmentType.WEAPON}.
	 */
	private boolean weapon;
	
	/**
	 * Checks if item is a weapon.
	 * @return	If {@code true}, the item goes to weapon slot.
	 */
	public boolean isWeapon() {
		return weapon;
	}
	
	/**
	 * The item's equipment slot.
	 */
	private EquipmentType equipmentType = EquipmentType.WEAPON;
	
	/**
	 * Gets the item's equipment type.
	 * @return	The equipment type.
	 */
	public EquipmentType getEquipmentType() {
		return equipmentType;
	}
	
	/**
	 * The {@link org.niobe.world.content.combat.action.RangedCombatAction.BowType}
	 * for this {@link ItemDefinition}.
	 */
	private BowType bowType;
	
	/**
	 * Gets the item definition's bow type, if any.
	 * @return	The {@link #bowType}.
	 */
	public BowType getBowType() {
		return bowType;
	}
	
	/**
	 * The {@link org.niobe.world.content.combat.action.RangedCombatAction.RangedWeaponType}
	 * for this {@link ItemDefinition}.
	 */
	private RangedWeaponType rangedWeaponType;
	
	/**
	 * Gets the item definition's bow type, if any.
	 * @return	The {@link #rangedWeaponType}.
	 */
	public RangedWeaponType getRangedWeaponType() {
		return rangedWeaponType;
	}
	
	/**
	 * The {@link org.niobe.world.content.combat.action.RangedCombatAction.ArrowType}
	 * for this {@link ItemDefinition}.
	 */
	private ArrowType arrowType;
	
	/**
	 * Gets the item definition's bow type, if any.
	 * @return	The {@link #arrowType}.
	 */
	public ArrowType getArrowType() {
		return arrowType;
	}
	
	/**
	 * Checks if item is full body.
	 */
	public boolean isFullBody() {
		return equipmentType.equals(EquipmentType.PLATEBODY);
	}
	
	/**
	 * Checks if item is full helm.
	 */
	public boolean isFullHelm() {
		return equipmentType.equals(EquipmentType.FULL_HELMET);
	}
	
	/**
	 * The item's equipment bonus.
	 */
	private double[] bonus = new double[18];
	
	/**
	 * Gets the bonus player will
	 * received upon wearing said item.
	 * @return	The bonus array.
	 */
	public double[] getBonus() {
		return bonus;
	}
	
	/**
	 * The skill requirements.
	 */
	private int[] requirement = new int[25];

	/**
	 * Gets the skill requirements the player
	 * will need to meet in order to wear said item.
	 * @return
	 */
	public int[] getRequirement() {
		return requirement;
	}
	
	/**
	 * Represents an equipment slot/type.
	 *
	 * @author relex lawl
	 */
	private enum EquipmentType {
		/**
		 * Item that belong in head slot.
		 */
		HAT(Equipment.HEAD_SLOT),
		
		/**
		 * Item that belong in cape slot.
		 */
		CAPE(Equipment.CAPE_SLOT),
		
		/**
		 * Item that belong in shield slot.
		 */
		SHIELD(Equipment.SHIELD_SLOT),
		
		/**
		 * Item that belong in gloves slot.
		 */
		GLOVES(Equipment.HANDS_SLOT),
		
		/**
		 * Item that belong in boots slot.
		 */
		BOOTS(Equipment.FEET_SLOT),
		
		/**
		 * Item that belong in amulet slot.
		 */
		AMULET(Equipment.AMULET_SLOT),
		
		/**
		 * Item that belong in ring slot.
		 */
		RING(Equipment.RING_SLOT),
		
		/**
		 * Item that belong in ammunition slot.
		 */
		ARROWS(Equipment.AMMUNITION_SLOT),
		
		/**
		 * Item that belong in head slot.
		 */
		FULL_MASK(Equipment.HEAD_SLOT),
		
		/**
		 * Item that belong in head slot.
		 */
		FULL_HELMET(Equipment.HEAD_SLOT),
		
		/**
		 * Item that belong in chest slot.
		 */
		BODY(Equipment.BODY_SLOT),
		
		/**
		 * Item that belong in chest slot.
		 */
		PLATEBODY(Equipment.BODY_SLOT),
		
		/**
		 * Item that belong in legs slot.
		 */
		LEGS(Equipment.LEG_SLOT),
		
		/**
		 * Item that belong in weapon slot.
		 */
		WEAPON(Equipment.WEAPON_SLOT);
		
		/**
		 * The EquipmentType constructor.
		 * @param slot	The slot the equipment type will have.
		 */
		private EquipmentType(int slot) {
			this.slot = slot;
		}
		
		/**
		 * The slot the equipment type will occupy.
		 */
		private final int slot;
	}
	
	@Override
	public String toString() {
		return "[ItemDefinition(" + id + ")] - Name: " + name + "; equipment slot: " + getEquipmentSlot() + "; value: "
				+ value + "; stackable ? " + Boolean.toString(stackable) + "; noted ? " + Boolean.toString(noted) + "; 2h ? " + isTwoHanded;
	}
}
