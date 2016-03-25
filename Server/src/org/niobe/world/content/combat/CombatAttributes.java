package org.niobe.world.content.combat;

import java.util.HashMap;
import java.util.Map;

import org.niobe.model.AttackType;
import org.niobe.model.GameCharacter;
import org.niobe.model.SkillManager.Skill;

/**
 * Contains all the attributes used in 
 * {@link org.niobe.model.GameCharacter} combat.
 *
 * @author relex lawl
 */
public final class CombatAttributes {

	/**
	 * The attack style chosen in the
	 * game frame attack tab.
	 */
	private int attackStyle = 0;
	
	/**
	 * Checks if character is has auto-retaliation on, which means
	 * they will automatically fight back when engaged in combat
	 * by another game character.
	 */
	private boolean autoRetaliation;
	
	/**
	 * Checks if player is going to use his special attack
	 * on their current combat target.
	 */
	private boolean usingSpecialAttack;
	
	/**
	 * Checks if player is currently recovering special attack amount.
	 */
	private boolean recoveringSpecialAttack;
	
	/**
	 * Checks if a character has attacked another character.
	 */
	private boolean hasAttacked;
	
	/**
	 * The character attacking the associated character.
	 */
	private GameCharacter attackedBy;
	
	/**
	 * The character this associated character is attacking.
	 */
	private GameCharacter attacking;
	
	/**
	 * The characters's combat attack delay, once this hits
	 * zero, they will attack their current target.
	 */
	private int attackDelay = 0;
	
	/**
	 * The player's magic spell id they are currently casting.
	 */
	private int magicSpellId;
	
	/**
	 * The player's current special attack amount.
	 */
	private int specialAttackAmount = 100;
	
	/**
	 * The amount to cut down received damaged by.
	 */
	private double invisibleDefenceMultiplier = 1;
	
	/**
	 * Checks if player has the staff of light defence effect.
	 */
	private boolean staffOfLightEffect;
	
	/**
	 * The character's current attack type.
	 */
	private AttackType attackType;
	
	/**
	 * The millisecond time where this {@link org.niobe.model.GameCharacter}
	 * was last attacked.
	 */
	private long damageDelay;
	
	/**
	 * The delay for the actual {@link org.niobe.model.Damage.Hit} to show
	 * up for the game character.
	 */
	private int hitDelay;
	
	/**
	 * This flag checks if the character has vengeance casted
	 * and will deflect damage to their opponent.
	 */
	private boolean vengeance;
	
	/**
	 * The amount of player-kill points this game character
	 * has, which is used for shop currency.
	 */
	private int pkPoints;
	
	/**
	 * This map contains the damage done by the key game character.
	 */
	private final Map<GameCharacter, Integer> damageMap = new HashMap<GameCharacter, Integer>();
	
	/**
	 * This integer array contains all the status that
	 * this game character's opponent has drained from them.
	 */
	private int[] curseDrain = new int[Skill.MAGIC.ordinal() + 1];
	
	/**
	 * This integer array contains all the status that
	 * this game character has drained from their opponent.
	 */
	private int[] curseGain = new int[Skill.MAGIC.ordinal() + 1];
	
	public int getAttackStyle() {
		return attackStyle;
	}
	
	public CombatAttributes setAttackStyle(int attackStyle) {
		this.attackStyle = attackStyle;
		return this;
	}
	
	public boolean isAutoRetaliation() {
		return autoRetaliation;
	}

	public CombatAttributes setAutoRetaliation(boolean autoRetaliation) {
		this.autoRetaliation = autoRetaliation;
		return this;
	}
	
	public boolean isUsingSpecialAttack() {
		return usingSpecialAttack;
	}

	public CombatAttributes setUsingSpecialAttack(boolean usingSpecialAttack) {
		this.usingSpecialAttack = usingSpecialAttack;
		return this;
	}
	
	public boolean isRecoveringSpecialAttack() {
		return recoveringSpecialAttack;
	}

	public CombatAttributes setRecoveringSpecialAttack(boolean recoveringSpecialAttack) {
		this.recoveringSpecialAttack = recoveringSpecialAttack;
		return this;
	}
	
	public int getAttackDelay() {
		return attackDelay;
	}

	public CombatAttributes setAttackDelay(int attackDelay) {
		this.attackDelay = attackDelay;
		return this;
	}

	public CombatAttributes addAttackDelay(int addition) {
		this.attackDelay += addition;
		return this;
	}
	
	public int getMagicSpellId() {
		return magicSpellId;
	}

	public CombatAttributes setMagicSpellId(int magicSpellId) {
		this.magicSpellId = magicSpellId;
		return this;
	}
	
	public boolean hasAttacked() {
		return hasAttacked;
	}

	public CombatAttributes setHasAttacked(boolean hasAttacked) {
		this.hasAttacked = hasAttacked;
		return this;
	}
	
	public int getSpecialAttackAmount() {
		return specialAttackAmount;
	}

	public CombatAttributes setSpecialAttackAmount(int specialAttackAmount) {
		this.specialAttackAmount = specialAttackAmount;
		return this;
	}

	public double getInvisibleDefenceMultiplier() {
		return invisibleDefenceMultiplier;
	}

	public CombatAttributes setInvisibleDefenceMultiplier(double invisibleDefenceMultiplier) {
		this.invisibleDefenceMultiplier = invisibleDefenceMultiplier;
		return this;
	}
	
	public CombatAttributes addInvisibleDefenceMultiplier(double invisibleDefenceMultiplier) {
		this.invisibleDefenceMultiplier += invisibleDefenceMultiplier;
		return this;
	}
	
	public CombatAttributes substractInvisibleDefenceMultiplier(double invisibleDefenceMultiplier) {
		this.invisibleDefenceMultiplier -= invisibleDefenceMultiplier;
		return this;
	}

	public boolean hasStaffOfLightEffect() {
		return staffOfLightEffect;
	}

	public CombatAttributes setStaffOfLightEffect(boolean staffOfLightEffect) {
		this.staffOfLightEffect = staffOfLightEffect;
		return this;
	}

	public AttackType getAttackType() {
		return attackType;
	}

	public CombatAttributes setAttackType(AttackType attackType) {
		this.attackType = attackType;
		return this;
	}

	public GameCharacter getAttackedBy() {
		return attackedBy;
	}

	public CombatAttributes setAttackedBy(GameCharacter attackedBy) {
		this.attackedBy = attackedBy;
		return this;
	}

	public GameCharacter getAttacking() {
		return attacking;
	}

	public CombatAttributes setAttacking(GameCharacter attacking) {
		this.attacking = attacking;
		return this;
	}

	public long getDamageDelay() {
		return damageDelay;
	}

	public CombatAttributes setDamageDelay(long damageDelay) {
		this.damageDelay = damageDelay;
		return this;
	}

	public int getHitDelay() {
		return hitDelay;
	}

	public CombatAttributes setHitDelay(int hitDelay) {
		this.hitDelay = hitDelay;
		return this;
	}

	public boolean hasVengeance() {
		return vengeance;
	}

	public CombatAttributes setVengeance(boolean vengeance) {
		this.vengeance = vengeance;
		return this;
	}

	public int getPkPoints() {
		return pkPoints;
	}

	public CombatAttributes setPkPoints(int pkPoints) {
		this.pkPoints = pkPoints;
		return this;
	}

	public Map<GameCharacter, Integer> getDamageMap() {
		return damageMap;
	}

	public int[] getCurseDrain() {
		return curseDrain;
	}
	
	public CombatAttributes setCurseDrain(int index, int drain) {
		curseDrain[index] = drain;
		return this;
	}
	
	public CombatAttributes setCurseDrain(int[] curseDrain) {
		this.curseDrain = curseDrain;
		return this;
	}

	public int[] getCurseGain() {
		return curseGain;
	}
	
	public CombatAttributes setCurseGain(int index, int gain) {
		curseGain[index] = gain;
		return this;
	}
	
	public CombatAttributes setCurseGain(int[] curseGain) {
		this.curseGain = curseGain;
		return this;
	}
}
