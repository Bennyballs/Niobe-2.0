package org.niobe.model.definition;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.niobe.model.Animation;

/**
 * This file manages the parsing of mob definitions, such as their
 * combat level, size, name, descriptions, etc.
 * 
 * @author relex lawl
 */
public final class MobDefinition {
	
	/**
	 * MobDefinition array containing every npc definition instance.
	 */
	private static MobDefinition[] definitions;
	
	/**
	 * The directory in which the npc definitions file is located.
	 */
	private static final String FILE_DIRECTORY = "./data/cache/mob_definitions.dat";
	
	/**
	 * Initializes the parsing of the mob file.
	 */
	public static void init() {
		long startup = System.currentTimeMillis();
		int amount = 0;
		System.out.println("Loading mob definitions...");
		DataInputStream input = null;
		try {
			input = new DataInputStream(new FileInputStream(FILE_DIRECTORY));
			int total = input.readShort();
			definitions = new MobDefinition[total];
			while (true) {
					MobDefinition definition = new MobDefinition();
					definition.id = input.readShort();
					definition.name = input.readUTF();
					definition.description = input.readUTF();
					definition.size = input.readByte();
					definition.level = input.readShort();
					definition.members = input.readBoolean();
					definition.lifePoints = input.readShort();
					definition.attackable = input.readBoolean();
					definition.aggressive = input.readBoolean();
					definition.poisonous = input.readBoolean();
					definition.attackAnimation = new Animation(input.readShort());
					definition.rangeAnimation = new Animation(input.readShort());
					definition.deathAnimation = new Animation(input.readShort());
					definition.attackLevel = input.readByte();
					definition.rangedLevel = input.readByte();
					definition.magicLevel = input.readByte();
					definition.defenceLevel = input.readByte();
					definition.weakness = input.readUTF();
					definition.slayerKey = input.readUTF();
					definition.slayerRequirement = input.readByte();
					definition.areas = new String[input.readByte()];
					for (int i = 0; i < definition.areas.length; i++) {
						definition.areas[i] = input.readUTF();
						if (definition.areas[i].length() <= 0)
							definition.areas[i] = null;
					}
					if (definition.name.toLowerCase().contains("banker")) {
						definition.size = 2;
					}
					definitions[definition.id] = definition;
					amount++;
			}
		} catch (IOException exception) {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Loaded " + amount + " mob definitions in " + (System.currentTimeMillis() - startup) + "ms");
	}
	
	/**
	 * Used to rewrite the mob definitions.
	 * You will first need to edit the mob definition before writing it 
	 * of course.
	 * @param directory		The location to store the newly written file.
	 */
	public static void write(String directory) throws IOException {
		if (directory.length() <= 0) {
			throw new IllegalArgumentException("Directory length must be greater than 0!");
		}
		DataOutputStream output = new DataOutputStream(new FileOutputStream(directory));
		for (MobDefinition definition : definitions) {
			if (definition == null)
				continue;
			output.writeShort(definition.id);
			output.writeUTF(definition.name);
			output.writeUTF(definition.description);
			output.writeByte(definition.size);
			output.writeShort(definition.level);
			output.writeBoolean(definition.members);
			output.writeShort(definition.lifePoints);
			output.writeBoolean(definition.attackable);
			output.writeBoolean(definition.aggressive);
			output.writeBoolean(definition.poisonous);
			output.writeShort(definition.attackAnimation.getId());
			output.writeShort(definition.rangeAnimation.getId());
			output.writeShort(definition.deathAnimation.getId());
			output.writeByte(definition.attackLevel);
			output.writeByte(definition.rangedLevel);
			output.writeByte(definition.magicLevel);
			output.writeByte(definition.defenceLevel);
			output.writeUTF(definition.weakness.toUpperCase());
			output.writeUTF(definition.slayerKey.toUpperCase());
			output.writeByte(definition.slayerRequirement);
			output.writeByte(definition.areas.length);
			for (int i = 0; i < definition.areas.length; i++) {
				output.writeUTF(definition.areas[i].toUpperCase());
			}
		}
	}

	/*public static void main(String[] args) throws IOException {
		init();
		write("./data/mob_definitions.txt");
		List<String> keys = new ArrayList<String>();
		BufferedWriter writer = new BufferedWriter(new FileWriter("./data/weakness.txt"));
		for (MobDefinition definition : definitions) {
			if (definition == null || definition.weakness == null ||
					keys.contains(definition.weakness))
				continue;
			String key = definition.weakness + ",";
			writer.write(key.replaceAll(" ", "_"));
			writer.newLine();
			keys.add(definition.weakness);
		}
		writer.close();
	}*/
	
	/**
	 * Returns NPCDefinition instance for a specified NPC Id.
	 * @param id	NPC Id to fetch definition for.
	 * @return		definitions[id].
	 */
	public static MobDefinition forId(int id) {
		return (id < 0 || id > definitions.length || definitions[id] == null) ? new MobDefinition() : definitions[id];
	}
	
	/**
	 * The mob id.
	 */
	private int id;
	
	/**
	 * The name of the mob.
	 */
	private String name = "Null";
	
	/**
	 * The examine information received from the mob.
	 */
	private String description = "Null";
	
	/**
	 * The tile size of the mob.
	 */
	private int size = 1;
	
	/**
	 * The mob level.
	 */
	private int level = 1;
	
	/**
	 * The flag that checks if only members are
	 * able to fight the mob.
	 */
	private boolean members;
	
	/**
	 * The amount of constitution/life points the 
	 * mob has.
	 */
	private int lifePoints = 100;
	
	/**
	 * The flag that checks if players are able
	 * to attack the mob.
	 */
	private boolean attackable;
	
	/**
	 * The flag that checks if the mob will 
	 * automatically attack certain near-by players.
	 */
	private boolean aggressive;
	
	/**
	 * The flag that checks if the mob will
	 * cause poisonous damage to the interacting player.
	 */
	private boolean poisonous;
	
	/**
	 * The {@link org.niobe.model.Animation} the mob
	 * will use upon using melee attacks.
	 */
	private Animation attackAnimation;
	
	/**
	 * The {@link org.niobe.model.Animation} the mob
	 * will use upon using ranged attacks.
	 */
	private Animation rangeAnimation;
	
	/**
	 * The {@link org.niobe.model.Animation} the mob
	 * will use upon death.
	 */
	private Animation deathAnimation;
	
	/**
	 * The attack level the mob has.
	 */
	private int attackLevel;
	
	/**
	 * The ranged level the mob has.
	 */
	private int rangedLevel;
	
	/**
	 * The magic level the mob has.
	 */
	private int magicLevel;
	
	/**
	 * The defense level the mob has.
	 */
	private int defenceLevel;
	
	/**
	 * The mob's weakness.
	 */
	private String weakness;
	
	/**
	 * The slayer key used to categorize the mob in the
	 * {@link org.niobe.model.SkillManager.Skill.SLAYER} skill.
	 */
	private String slayerKey = "Null";
	
	/**
	 * The slayer level requirement a player will
	 * need to meet in order to deal damage to said mob.
	 */
	private int slayerRequirement;
	
	/**
	 * The areas in the world in which you can find
	 * the mob.
	 */
	private String[] areas;
	
	/**
	 * Gets the mob id.
	 * @return	The id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the mob name.
	 * @return	The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the information the player
	 * will receive upon examining the mob.
	 * @return	The description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Gets the amount of world tiles the
	 * mob takes up.
	 * @return	The size of the mob.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Gets the mob's combat level.
	 * @return	The combat level.
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Checks if the mob can only be 
	 * fought by members.
	 * @return	If {@code true} only members can fight said mob.
	 */
	public boolean isMembers() {
		return members;
	}
	
	/**
	 * Gets the {@link org.niobe.world.Mob} life points.
	 * @return	The {@link #lifePoints}.
	 */
	public int getLifePoints() {
		return lifePoints;
	}
	
	/**
	 * Checks if {@link org.niobe.world.Mob} is
	 * attackable by {@link org.niobe.world.Player}s.
	 * @return	{@code true} if the {@link org.niobe.world.Mob} is attackable.
	 */
	public boolean isAttackable() {
		return attackable;
	}
	
	/**
	 * Checks if {@link org.niobe.world.Mob} is
	 * aggressive and will attack {@link org.niobe.world.Player}s
	 * near-by.
	 * @return	{@code true} if {@link org.niobe.world.Mob} is aggressive.
	 */
	public boolean isAggressive() {
		return aggressive;
	}
	
	/**
	 * Checks if {@link org.niobe.world.Mob} is poisonous
	 * and has a change of poisoning a {@link org.niobe.world.Player}
	 * while engaged in combat. 
	 * @return	{@code true} if {@link org.niobe.world.Mob} is poisonous.
	 */
	public boolean isPoisonous() {
		return poisonous;
	}
	
	/**
	 * Gets the {@link org.niobe.world.Mob}'s attack 
	 * {@link org.niobe.model.Animation}.
	 * @return	The attack {{@link org.niobe.model.Animation}.
	 */
	public Animation getAttackAnimation() {
		return attackAnimation;
	}
	
	/**
	 * Gets the {@link org.niobe.world.Mob}'s ranged 
	 * {@link org.niobe.model.Animation}.
	 * @return	The ranged {{@link org.niobe.model.Animation}.
	 */
	public Animation getRangeAnimation() {
		return rangeAnimation;
	}
	
	/**
	 * Gets the {@link org.niobe.world.Mob}'s death 
	 * {@link org.niobe.model.Animation}.
	 * @return	The death {{@link org.niobe.model.Animation}.
	 */
	public Animation getDeathAnimation() {
		return deathAnimation;
	}
	
	/**
	 * Gets the {@link org.niobe.world.Mob}'s attack
	 * level.
	 * @return	The attack level used for calculating damage.
	 */
	public int getAttackLevel() {
		return attackLevel;
	}
	
	/**
	 * Sets the {@link org.niobe.world.Mob}'s attack
	 * level.
	 * @return	The MobDefinition instance.
	 */
	public MobDefinition setAttackLevel(int attackLevel) {
		this.attackLevel = attackLevel;
		return this;
	}
	
	/**
	 * Gets the {@link org.niobe.world.Mob}'s ranged
	 * level.
	 * @return	The ranged level used for calculating damage.
	 */
	public int getRangedLevel() {
		return rangedLevel;
	}
	
	/**
	 * Sets the {@link org.niobe.world.Mob}'s ranged
	 * level.
	 * @return	The MobDefinition instance.
	 */
	public MobDefinition setRangedLevel(int rangedLevel) {
		this.rangedLevel = rangedLevel;
		return this;
	}
	
	/**
	 * Gets the {@link org.niobe.world.Mob}'s magic
	 * level.
	 * @return	The magic level used for calculating damage.
	 */
	public int getMagicLevel() {
		return magicLevel;
	}
	
	/**
	 * Sets the {@link org.niobe.world.Mob}'s magic
	 * level.
	 * @return	The MobDefinition instance.
	 */
	public MobDefinition setMagicLevel(int magicLevel) {
		this.magicLevel = magicLevel;
		return this;
	}
	
	/**
	 * Gets the {@link org.niobe.world.Mob}'s defense
	 * level.
	 * @return	The defense level used for deducting the
	 * 			damage dealt to the {@link org.niobe.world.Mob}.
	 */
	public int getDefenceLevel() {
		return defenceLevel;
	}
	
	/**
	 * Sets the {@link org.niobe.world.Mob}'s defense
	 * level.
	 * @return	The MobDefinition instance.
	 */
	public MobDefinition setDefenceLevel(int defenceLevel) {
		this.defenceLevel = defenceLevel;
		return this;
	}
	
	/**
	 * Gets the {@link org.niobe.world.Mob}'s 
	 * {@link org.niobe.world.Mob.Weakness}.
	 * @return	The {@link org.niobe.world.Mob.Weakness} string.
	 */
	public String getWeakness() {
		return weakness;
	}
	
	/**
	 * Gets the {@link org.niobe.world.Mob}'s 
	 * slayer key.
	 * @return	The {@link org.niobe.world.Mob} slayer key.
	 */
	public String getSlayerKey() {
		return slayerKey;
	}
	
	/**
	 * Gets the {@link org.niobe.world.Mob}'s slayer level
	 * requirement in order to be damaged.
	 * @return	The slayer level requirement.
	 */
	public int getSlayerRequirement() {
		return slayerRequirement;
	}
	
	/**
	 * Gets the areas throughout the game where
	 * the {@link org.niobe.world.Mob} can be found.
	 * @return	The {@link #areas} array.
	 */
	public String[] getAreas() {
		return areas;
	}
	
	/**
	 * Gets the available mob definitions.
	 * @return	The {@link #definitions} array.
	 */
	public static MobDefinition[] getDefinitions() {
		return definitions;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Name=" + name + "; Description=" + description + "Level=" + level + ";\nSize=" + size + "; MembersOnly=" + members + "; isAttackable=" + attackable + "; Weakness=" + weakness + "; isPoisonous=" + poisonous + "\nAttackAnimation=" + attackAnimation.getId() + "; RangedAnimation=" + rangeAnimation.getId() + "; DeathAnimation=" + deathAnimation.getId());
		return builder.toString();
	}
}
