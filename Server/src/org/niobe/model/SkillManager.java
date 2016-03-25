package org.niobe.model;

import org.niobe.model.UpdateFlag.Flag;
import org.niobe.model.container.impl.Equipment;
import org.niobe.model.definition.MobDefinition;
import org.niobe.model.weapon.Weapon;
import org.niobe.model.weapon.WeaponLoader;
import org.niobe.util.NameUtil;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.util.GameConstants;

/**
 * Manages the {@link org.niobe.world.Player}'s
 * {@link Skill}s.
 * 
 * @author relex lawl
 */
public class SkillManager {
	
	/**
	 * The Skills constructor, also sets a player's constitution
	 * and prayer level to 100 and 10 respectively.
	 * @param player	The player's who skill set is being represented.
	 */
	public SkillManager(Player player) {
		this.player = player;
		for (int i = 0; i < MAX_SKILLS; i++) {
			level[i] = maxLevel[i] = 1;
			experience[i] = 0;
		}
		level[Skill.CONSTITUTION.ordinal()] = maxLevel[Skill.CONSTITUTION.ordinal()] = 100;
		experience[Skill.CONSTITUTION.ordinal()] = 1184;
		level[Skill.PRAYER.ordinal()] = maxLevel[Skill.PRAYER.ordinal()] = 10;
	}
	
	/**
	 * Adds experience to {@code skill} by the {@code experience} amount.
	 * @param skill			The skill to add experience to.
	 * @param experience	The amount of experience to add to the skill.
	 * @return				The Skills instance.
	 */
	public SkillManager addExperience(Skill skill, int experience) {
		/*
		 * If the experience in the skill is already greater or equal to
		 * {@code MAX_EXPERIENCE} then stop.
		 */
		if (this.experience[skill.ordinal()] >= MAX_EXPERIENCE)
			return this;
		/*
		 * The skill's level before adding experience.
		 */
		int startingLevel = isNewSkill(skill) ? (int) (maxLevel[skill.ordinal()]/10) : maxLevel[skill.ordinal()];
		/*
		 * Adds the experience to the skill's experience.
		 */
		this.experience[skill.ordinal()] = this.experience[skill.ordinal()] + experience > MAX_EXPERIENCE ? MAX_EXPERIENCE : this.experience[skill.ordinal()] + experience;
		/*
		 * The skill's level after adding the experience.
		 */
		int newLevel = getLevelForExperience(this.experience[skill.ordinal()]);
		/*
		 * If the starting level less than the new level, level up.
		 */
		if (newLevel > startingLevel) {
			int level = newLevel - startingLevel;
			String skillName = NameUtil.getVowelFormat(skill.getName());
			maxLevel[skill.ordinal()] += isNewSkill(skill) ? level * 10 : level;
			/*
			 * If the skill is not constitution, prayer or summoning, then set the current level
			 * to the max level.
			 */
			if (!isNewSkill(skill) && !skill.equals(Skill.SUMMONING))
				setCurrentLevel(skill, maxLevel[skill.ordinal()]);
			player.getPacketSender().sendFlashingSidebar(GameConstants.SKILLS_TAB);
			player.getPacketSender().sendString(4268, "Congratulations! You have achieved " + skillName + " level!");
			player.getPacketSender().sendString(4269, "Well done. You are now level " + newLevel + ".");
			player.getPacketSender().sendString(358, "Click here to continue.");
			player.getPacketSender().sendChatboxInterface(skill.updateStrings[3]);
			player.getPacketSender().sendMessage("You've just advanced " + skillName + " level! You have reached level " + newLevel);
			if (maxLevel[skill.ordinal()] == getMaxAchievingLevel(skill)) {
				player.getPacketSender().sendMessage("Well done! You've achieved the highest possible level in this skill!");
			}
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		/*
		 * The total experience on the player's experience counter.
		 */
		int totalXP = player.getFields().getTotalExperienceOnCounter();
		/*
		 * If total experience is greater than 214m or negative, then set total experience
		 * to 214m (maximum amount).
		 */
		totalXP += experience;
		if (totalXP > 214748364 || totalXP < 0)
			totalXP = 214748364;
		player.getFields().setTotalExperienceOnCounter(totalXP);
		updateSkill(skill);
		return this;
	}
	
	/**
	 * Updates the skill strings, for skill tab and orb updating.
	 * @param skill	The skill who's strings to update.
	 * @return		The Skills instance.
	 */
	public SkillManager updateSkill(Skill skill) {
		int maxLevel = getMaxLevel(skill), currentLevel = getCurrentLevel(skill);
		if (isNewSkill(skill)) {
			maxLevel = (maxLevel / 10);
			currentLevel = (currentLevel / 10);
		}
		if (skill == Skill.PRAYER)
			player.getPacketSender().sendString(687, currentLevel + "/" + maxLevel);
		player.getPacketSender().sendString(skill.updateStrings[0], Integer.toString(currentLevel));
		player.getPacketSender().sendString(skill.updateStrings[1], Integer.toString(maxLevel));
		player.getPacketSender().sendString(skill.updateStrings[2], Integer.toString(getExperience(skill)));
		player.getPacketSender().sendString(31200, "" + getTotalLevel());
		Weapon weapon = WeaponLoader.forId(player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId());
		player.getPacketSender().sendString(weapon.getInterfaceId()[Weapon.COMBAT_LEVEL_INDEX], "Combat level: " + getCombatLevel());
		player.getPacketSender().sendSkill(skill);
		return this;
	}
	
	/**
	 * Gets the minimum experience in said level.
	 * @param level		The level to get minimum experience for.
	 * @return			The least amount of experience needed to achieve said level.
	 */
	public static int getExperienceForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int)Math.floor(points / 4);
		}
		return 0;
	}
	
	/**
	 * Gets the level from said experience.
	 * @param experience	The experience to get level for.
	 * @return				The level you obtain when you have specified experience.
	 */
	public static int getLevelForExperience(int experience) {
		int points = 0, output = 0;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= experience) {
				return lvl;
			}
		}
		return 99;
	}

	/**
	 * Calculates the player's combat level.
	 * @return	The average of the player's combat skills.
	 */
	public int getCombatLevel() {
		final int attack = maxLevel[Skill.ATTACK.ordinal()];
		final int defence = maxLevel[Skill.DEFENCE.ordinal()];
		final int strength = maxLevel[Skill.STRENGTH.ordinal()];
		final int hp = (maxLevel[Skill.CONSTITUTION.ordinal()] / 10);
		final int prayer = (maxLevel[Skill.PRAYER.ordinal()] / 10);
		final int ranged = maxLevel[Skill.RANGED.ordinal()];
		final int magic = maxLevel[Skill.MAGIC.ordinal()];
		final int summoning = maxLevel[Skill.SUMMONING.ordinal()];
		int combatLevel = 3;	
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.2535) + 1;
		final double melee = (attack + strength) * 0.325;
		final double ranger = Math.floor(ranged * 1.5) * 0.325;
		final double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		combatLevel += summoning * 0.125;
		if (combatLevel > 138)
			return 138;
		if (combatLevel < 3)
			return 3;
		return combatLevel;
	}
	
	/**
	 * Gets the player's total level.
	 * @return	The value of every skill summed up.
	 */
	public int getTotalLevel() {
		int total = 0;
		for (Skill skill : Skill.values()) {
			/*
			 * If the skill is not equal to constitution or prayer, total can 
			 * be summed up with the maxLevel.
			 */
			if (!isNewSkill(skill))
				total += maxLevel[skill.ordinal()];
			/*
			 * Other-wise add the maxLevel / 10, used for 'constitution' and prayer * 10.
			 */
			else
				total += maxLevel[skill.ordinal()] / 10;
		}
		return total;
	}
	
	/**
	 * Checks if the skill is a x10 skill.
	 * @param skill		The skill to check.
	 * @return			The skill is a x10 skill.
	 */
	public static boolean isNewSkill(Skill skill) {
		return skill.equals(Skill.CONSTITUTION) || skill.equals(Skill.PRAYER);
	}
	
	/**
	 * Gets the max level for <code>skill</code>
	 * @param skill		The skill to get max level for.
	 * @return			The max level that can be achieved in said skill.
	 */
	public static int getMaxAchievingLevel(Skill skill) {
		int level = 99;
		if (isNewSkill(skill))
			level = 990;
		if (skill == Skill.DUNGEONEERING)
			level = 120;
		return level;
	}
	
	/**
	 * Gets the current level for said skill.
	 * @param skill		The skill to get current/temporary level for.
	 * @return			The skill's level.
	 */
	public int getCurrentLevel(Skill skill) {
		return level[skill.ordinal()];
	}
	
	/**
	 * Gets the max level for said skill.
	 * @param skill		The skill to get max level for.
	 * @return			The skill's maximum level.
	 */
	public int getMaxLevel(Skill skill) {
		return maxLevel[skill.ordinal()];
	}
	
	/**
	 * Gets the experience for said skill.
	 * @param skill		The skill to get experience for.
	 * @return			The experience in said skill.
	 */
	public int getExperience(Skill skill) {
		return experience[skill.ordinal()];
	}
	
	/**
	 * Sets the current level of said skill.
	 * @param skill		The skill to set current/temporary level for.
	 * @param level		The level to set the skill to.
	 * @param refresh	If <code>true</code>, the skill's strings will be updated.
	 * @return			The Skills instance.
	 */
	public SkillManager setCurrentLevel(Skill skill, int level, boolean refresh) {
		this.level[skill.ordinal()] = level;
		if (refresh)
			updateSkill(skill);
		return this;
	}
	
	/**
	 * Sets the maximum level of said skill.
	 * @param skill		The skill to set maximum level for.
	 * @param level		The level to set skill to.
	 * @param refresh	If <code>true</code>, the skill's strings will be updated.
	 * @return			The Skills instance.
	 */
	public SkillManager setMaxLevel(Skill skill, int level, boolean refresh) {
		maxLevel[skill.ordinal()] = level;
		if (refresh)
			updateSkill(skill);
		return this;
	}
	
	/**
	 * Sets the experience of said skill.
	 * @param skill			The skill to set experience for.
	 * @param experience	The amount of experience to set said skill to.
	 * @param refresh		If <code>true</code>, the skill's strings will be updated.
	 * @return				The Skills instance.
	 */
	public SkillManager setExperience(Skill skill, int experience, boolean refresh) {
		this.experience[skill.ordinal()] = experience;
		if (refresh)
			updateSkill(skill);
		return this;
	}
	
	/**
	 * Sets the current level of said skill.
	 * @param skill		The skill to set current/temporary level for.
	 * @param level		The level to set the skill to.
	 * @return			The Skills instance.
	 */
	public SkillManager setCurrentLevel(Skill skill, int level) {
		setCurrentLevel(skill, level, true);
		return this;
	}
	
	/**
	 * Sets the maximum level of said skill.
	 * @param skill		The skill to set maximum level for.
	 * @param level		The level to set skill to.
	 * @return			The Skills instance.
	 */
	public SkillManager setMaxLevel(Skill skill, int level) {
		setMaxLevel(skill, level, true);
		return this;
	}
	
	/**
	 * Sets the experience of said skill.
	 * @param skill			The skill to set experience for.
	 * @param experience	The amount of experience to set said skill to.
	 * @return				The Skills instance.
	 */
	public SkillManager setExperience(Skill skill, int experience) {
		setExperience(skill, experience, true);
		return this;
	}
	
	/**
	 * Increases the {@link #level} by said {@link amount}.
	 * @param skill		The {@link Skill} to increase.
	 * @param amount	The amount to add to said skill.
	 * @param bounds	If {@code true} the {@link #newLevel} will be compared to {@link #getMaxLevel(Skill)}
	 * 						and will be set to that value if it goes over it.
	 * @param refresh	If {@code true} update the skill strings.
	 * @return			The {@link SkillManager} instance.
	 */
	public SkillManager increaseCurrentLevelNoBounds(Skill skill, int amount, boolean bounds, boolean refresh) {
		int newLevel = getCurrentLevel(skill) + amount;
		if (bounds && newLevel > getMaxLevel(skill)) {
			newLevel = getMaxLevel(skill);
		}
		setCurrentLevel(skill, newLevel);
		return this;
	}
	
	/**
	 * Decreases the {@link #level} by said {@link amount}.
	 * @param skill		The {@link Skill} to decrease.
	 * @param amount	The amount to decrease for said skill.
	 * @param refresh	If {@code true} update the skill strings.
	 * @return			The {@link SkillManager} instance.
	 */
	public SkillManager detractCurrentLevel(Skill skill, int amount, boolean refresh) {
		int newLevel = getCurrentLevel(skill) - amount;
		if (newLevel < 1) {
			newLevel = 1;
		}
		setCurrentLevel(skill, newLevel, refresh);
		return this;
	}
	
	/**
	 * This enum contains data used as constants for skill configurations
	 * such as experience rates, string id's for interface updating.
	 * 
	 * @author relex lawl
	 */
	public enum Skill {
		ATTACK(75, 
				new int[] {31114, 31115, 31113, 6247}),
		DEFENCE(75, 
				new int[] {31124, 31125, 31123, 6253}),
		STRENGTH(75, 
				new int[] {31119, 31120, 31118, 6206}),
		CONSTITUTION(50, 
				new int[] {31159, 31160, 31158, 6216}),
		RANGED(75, 
				new int[] {31129, 31130, 31128, 4443}),
		PRAYER(50, 
				new int[] {31134, 31135, 31133, 6242}),
		MAGIC(75, 
				new int[] {31139, 31140, 31138, 6211}),
		COOKING(150, 
				new int[] {31219, 31220, 31218, 6226}),
		WOODCUTTING(150, 
				new int[] {31229, 31230, 31228, 4272}),
		FLETCHING(175, 
				new int[] {31184, 31185, 31183, 6231}),
		FISHING(150, 
				new int[] {31214, 31215, 31213, 6258}),
		FIREMAKING(200, 
				new int[] {31224, 31225, 31123, 4282}),
		CRAFTING(250, 
				new int[] {31179, 31180, 31178, 6263}),
		SMITHING(300, 
				new int[] {31209, 31210, 31208, 6221}),
		MINING(150, 
				new int[] {31204, 31205, 31203, 4416}),
		HERBLORE(250, 
				new int[] {31169, 31170, 31168, 6237}),
		AGILITY(300,
				new int[] {31164, 31165, 31163, 4277}),
		THIEVING(300, 
				new int[] {31174, 31175, 31173, 4261}),
		SLAYER(400,
				new int[] {31189, 31190, 31188, 12122}),
		FARMING(400, 
				new int[] {31234, 31235, 31233, 5267}),
		RUNECRAFTING(400,
				new int[] {31144, 31145, 31143, 4267}),
		CONSTRUCTION(175, 
				new int[] {31149, 31150, 31148, 7267}),
		HUNTER(150, 
				new int[] {31194, 31195, 31193, 8267}),
		SUMMONING(200, 
				new int[] {31239, 31240, 31238, 9267}),
		DUNGEONEERING(200, 
				new int[] {31154, 31155, 31153, 10267});
		
		private Skill(int multiplier, int[] updateStrings) {
			this.multiplier = multiplier;
			this.updateStrings = updateStrings;
		}
		
		/**
		 * The amount the experience will be multiplied by
		 * when being added to the player's skills.
		 */
		private int multiplier;
		
		/**
		 * The string child id's on the skills tab to update
		 * upon login and level up.
		 */
		int[] updateStrings;
		
		/**
		 * Gets the Skills experience multiplier.
		 * @return multiplier.
		 */
		public int getExperienceMultiplier() {
			return multiplier;
		}

		/**
		 * Gets the Skill's name.
		 * @return	The skill's name in a lower case format.
		 */
		public String getName() {
			return toString().toLowerCase();
		}
		
		/**
		 * Gets the Skill value which ordinal() matches {@code id}.
		 * @param id	The index of the skill to fetch Skill instance for.
		 * @return		The Skill instance.
		 */
		public static Skill forId(int id) {
			for (Skill skill : Skill.values()) {
				if (skill.ordinal() == id) {
					return skill;
				}
			}
			return null;
		}
	}
	
	/**
	 * The player associated with this Skills instance.
	 */
	private Player player;
	
	/**
	 * The current/temporary levels of the player's skills.
	 */
	private int[] level = new int[MAX_SKILLS];
	
	/**
	 * The maximum levels of the player's skills.
	 */
	private int[] maxLevel = new int[MAX_SKILLS];
	
	/**
	 * The experience of the player's skills.
	 */
	private int[] experience = new int[MAX_SKILLS];
	
	/**
	 * The maximum amount of skills in the server.
	 */
	public static final int MAX_SKILLS = 25;
	
	/**
	 * The maximum amount of experience you can
	 * achieve in a skill.
	 */
	public static final int MAX_EXPERIENCE = 200000000;
	
	/**
	 * The default SkillManager constructor.
	 * Used for the {@link MobSkillManager} implementation.
	 */
	public SkillManager() {
		
	}
	
	/**
	 * An implementation of {@link SkillManager} used to manage
	 * {@link org.niobe.world.Mob} skills.
	 *
	 * @author relex lawl
	 */
	public static final class MobSkillManager extends SkillManager {

		/**
		 * The MobSkillManager constructor.
		 * @param mob	The {@link org.niobe.world.Mob} being represented.
		 */
		public MobSkillManager(Mob mob) {
			this.definition = MobDefinition.forId(mob.getId());
			setLevels();
		}
		
		/**
		 * The {@link org.niobe.model.definition.MobDefinition} for this
		 * skill manager.
		 */
		private final MobDefinition definition;
		
		/**
		 * Sets the mob's skill levels to predetermined value stored in
		 * their {@link #definition}.
		 */
		private void setLevels() {
			setCurrentLevel(Skill.ATTACK, definition.getAttackLevel(), false);
			setCurrentLevel(Skill.RANGED, definition.getRangedLevel(), false);
			setCurrentLevel(Skill.MAGIC, definition.getMagicLevel(), false);
			setCurrentLevel(Skill.DEFENCE, definition.getDefenceLevel(), false);
			
			setMaxLevel(Skill.ATTACK, definition.getAttackLevel(), false);
			setMaxLevel(Skill.RANGED, definition.getRangedLevel(), false);
			setMaxLevel(Skill.MAGIC, definition.getMagicLevel(), false);
			setMaxLevel(Skill.DEFENCE, definition.getDefenceLevel(), false);
		}
	}
}