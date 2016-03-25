package org.niobe.model.weapon;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.niobe.model.definition.ItemDefinition;
import org.niobe.model.weapon.impl.*;

/**
 * Manages the assignment of specific game items 
 * to their corresponding {@link Weapon}.
 * 
 * @author relex lawl
 */
public final class WeaponLoader {
	
	/**
	 * This array contains all the weapons in the world.
	 */
	private static final Weapon[] weapons = new Weapon[ItemDefinition.getMaxAmountOfItems()];
	
	/**
	 * This map contains all possible attack interface buttons
	 * along with their index in the attack style, animation and combat style arrays.
	 */
	private static final Map<Integer, Integer> buttons = new HashMap<Integer, Integer>();
	
	/**
	 * This list contains all possible special attack bar buttons.
	 */
	private static final List<Integer> specialAttackButtons = new LinkedList<Integer>();
	
	/**
	 * The default weapon (i.e "none"), used to stop unnecessary instances
	 * of {@link org.niobe.model.weapon.impl.DefaultWeapon}.
	 */
	private static final Weapon DEFAULT_WEAPON = new DefaultWeapon();
	
	/**
	 * Initializes the weapon definitions and sets the weapons array
	 * with their assigned 'weapon' instance.
	 */
	public static void init() {
		int amount = 0;
		long startup = System.currentTimeMillis();
		System.out.println("Loading weapon definitions...");
		for (ItemDefinition definition : ItemDefinition.getDefinitions()) {
			if (definition == null || !definition.isWeapon())
				continue;
			String name = definition.getName().toLowerCase();
			if (name.contains("whip")) {
				weapons[definition.getId()] = new AbyssalWhipWeapon(definition.getId());
				amount++;
			} else if (name.contains("granite maul")) {
				weapons[definition.getId()] = new GraniteMaulWeapon(definition.getId());
				amount++;
			} else if (name.contains("maul")) {
				weapons[definition.getId()] = new MaulWeapon(definition.getId());
				amount++;
			} else if (name.contains("dragon claws")) {
				weapons[definition.getId()] = new DragonClawWeapon(definition.getId());
				amount++;
			} else if (name.contains("claws")) {
				weapons[definition.getId()] = new ClawWeapon(definition.getId());
				amount++;
			} else if (name.contains("staff of light")) {
				weapons[definition.getId()] = new StaffOfLightWeapon(definition.getId());
				amount++;
			} else if (name.contains("staff")) {
				weapons[definition.getId()] = new StaffWeapon(definition.getId());
				amount++;
			} else if (name.contains("greataxe")) {
				weapons[definition.getId()] = new WarAxeWeapon(definition.getId());
				amount++;
			} else if (name.contains("korasi's sword")) {
				weapons[definition.getId()] = new KorasiSwordWeapon(definition.getId());
				amount++;
			} else if (name.contains("dragon scimitar")
						&& !name.contains("corrupt")) {
				weapons[definition.getId()] = new DragonScimitarWeapon(definition.getId());
				amount++;
			} else if (name.contains("scimitar")) {
				weapons[definition.getId()] = new ScimitarWeapon(definition.getId());
				amount++;
			} else if (name.contains("dragon longsword")) {
				weapons[definition.getId()] = new DragonLongswordWeapon(definition.getId());
				amount++;
			} else if (name.contains("longsword")) {
				weapons[definition.getId()] = new LongswordWeapon(definition.getId());
				amount++;
			} else if (name.contains("rapier")) {
				weapons[definition.getId()] = new RapierWeapon(definition.getId());
				amount++;
			} else if (name.contains("dragon dagger")
						&& !name.contains("corrupt")) {
				weapons[definition.getId()] = new DragonDaggerWeapon(definition.getId());
				amount++;
			} else if (name.contains("dagger")) {
				weapons[definition.getId()] = new DaggerWeapon(definition.getId());
				amount++;
			} else if (name.contains("mace")) {
				weapons[definition.getId()] = new MaceWeapon(definition.getId());
				amount++;
			} else if (name.contains("dragon spear")
						&& !name.contains("corrupt")) {
				weapons[definition.getId()] = new DragonSpearWeapon(definition.getId());
				amount++;
			} else if (name.contains("spear")) {
				weapons[definition.getId()] = new SpearWeapon(definition.getId());
				amount++;
			} else if (name.contains("battleaxe")) {
				weapons[definition.getId()] = new BattleAxeWeapon(definition.getId());
				amount++;
			} else if (name.contains("warhammer")) {
				weapons[definition.getId()] = new WarHammerWeapon(definition.getId());
				amount++;
			} else if (name.contains("armadyl godsword")) {
				weapons[definition.getId()] = new ArmadylGodswordWeapon(definition.getId());
				amount++;
			} else if (name.contains("bandos godsword")) {
				weapons[definition.getId()] = new BandosGodswordWeapon(definition.getId());
				amount++;
			} else if (name.contains("saradomin godsword")) {
				weapons[definition.getId()] = new SaradominGodswordWeapon(definition.getId());
				amount++;
			} else if (name.contains("zamorak godsword")) {
				weapons[definition.getId()] = new ZamorakGodswordWeapon(definition.getId());
				amount++;
			} else if (name.contains("saradomin sword")) {
				weapons[definition.getId()] = new SaradominSwordWeapon(definition.getId());
				amount++;
			} else if (name.contains("2h")) {
				weapons[definition.getId()] = new TwoHandedSwordWeapon(definition.getId());
				amount++;
			} else if (name.contains("halberd")) {
				weapons[definition.getId()] = new HalberdWeapon(definition.getId());
				amount++;
			} else if (name.contains("bronze sword") || name.contains("iron sword") || name.contains("white sword") ||
							name.contains("steel sword") || name.contains("adamant sword") || name.contains("rune sword")) {
				weapons[definition.getId()] = new ShortSwordWeapon(definition.getId());
				amount++;
			} else if (name.contains("dragon pickaxe")) {
				weapons[definition.getId()] = new DragonPickaxeWeapon(definition.getId());
				amount++;
			} else if (name.contains("pickaxe")) {
				weapons[definition.getId()] = new PickaxeWeapon(definition.getId());
				amount++;
			} else if (name.contains("magic shortbow")) {
				weapons[definition.getId()] = new MagicShortbowWeapon(definition.getId());
				amount++;
			} else if (name.contains("zamorak bow")) {
				//weapons[definition.getId()] = new ZamorakBowWeapon(definition.getId());
				//amount++;
			} else if (name.contains("saradomin bow")) {
				//weapons[definition.getId()] = new SaradominBowWeapon(definition.getId());
				//amount++;
			} else if (name.contains("guthix bow")) {
				//weapons[definition.getId()] = new GuthixBowWeapon(definition.getId());
				//amount++;
			} else if (name.contains("c'bow") || name.contains("crossbow")) {
				//weapons[definition.getId()] = new CrossbowWeapon(definition.getId());
				//amount++;
			} else if (name.contains("longbow")) {
				//weapons[definition.getId()] = new LongbowWeapon(definition.getId());
				//amount++;
			} else if (name.contains("shortbow")) {
				weapons[definition.getId()] = new BowWeapon(definition.getId());
				amount++;
			}
		}
		System.out.println("Loaded " + amount + " weapon definition" + (amount != 1 ? "s" : "") + " in " + (System.currentTimeMillis() - startup) + "ms");
	}
	
	/**
	 * Gets the 'weapon' for said item id.
	 * @param id	The id of the weapon's game item id.
	 * @return		The weapon with said index located in {@link weapons}.
	 */
	public static Weapon forId(int id) {
		return (id >= 0 && weapons[id] != null ? weapons[id] : DEFAULT_WEAPON);
	}
	
	/**
	 * Gets the valid attack interface buttons for weapons.
	 * @return	The map containing all buttons.
	 */
	public static Map<Integer, Integer> getButtons() {
		return buttons;
	}
	
	/**
	 * Gets the valid special attack bar buttons.
	 * @return	The list containing all valid buttons for attack bar child id.
	 */
	public static List<Integer> getSpecialAttackButtons() {
		return specialAttackButtons;
	}
	
	/**
	 * Gets the default weapon, which is called when a player is not wielding an item.
	 * @return	The DefaultWeapon instance.
	 */
	public static Weapon getDefaultWeapon() {
		return DEFAULT_WEAPON;
	}
}
