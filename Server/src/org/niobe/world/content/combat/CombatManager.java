package org.niobe.world.content.combat;

import java.util.Map.Entry;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.AttackType;
import org.niobe.model.Damage;
import org.niobe.model.Damage.Hit;
import org.niobe.model.Damage.CombatIcon;
import org.niobe.model.Damage.Hitmask;
import org.niobe.model.GameCharacter;
import org.niobe.model.MovementQueue.MovementFlag;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.action.GameAction;
import org.niobe.model.action.impl.CombatGameAction;
import org.niobe.model.weapon.Weapon;
import org.niobe.task.Task;
import org.niobe.task.impl.PoisonDamageTask;
import org.niobe.util.MathUtil;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.content.combat.action.*;
import org.niobe.world.content.combat.formula.melee.MeleeHitFormula;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * This class is used for {@link org.niobe.model.GameCharacter} versus
 * {@link org.niobe.model.GameCharacter} combat.
 *
 * @author relex lawl
 */
public final class CombatManager {
	
	/**
	 * Handles the engagement hook of a {@link org.niobe.model.GameCharacter}
	 * to another.
	 * @param source	The {@link org.niobe.model.GameCharacter} calling the hook.
	 * @param victim	The victim {@link org.niobe.model.GameCharacter}.
	 */
	public static void engageCombat(GameCharacter source, GameCharacter victim) {
		if (source.getFields().isDead() || victim.getFields().isDead() || source.getFields().getCombatAttributes().hasAttacked() ||
				source.getMovementQueue().getMovementFlag() == MovementFlag.STUNNED) {
			return;
		}
		source.setEntityInteraction(victim);
		source.getFields().getCombatAttributes().setAttackType(getAttackType(source));
		GameAction<GameCharacter> action = new CombatGameAction<GameCharacter>(source, victim, getDistanceRequired(source, victim));
		source.setAction(action);
	}
	
	/**
	 * Starts the actual combat of two {@link org.niobe.model.GameCharacter}s.
	 * @param source	The source {@link org.niobe.model.GameCharacter}.
	 * @param victim	The victim {@link org.niobe.model.GameCharacter}.
	 */
	public static void startCombat(final GameCharacter source, final GameCharacter victim) {
		if (source.getFields().isDead() || victim.getFields().isDead()
				|| !attackCheck(source, victim)) {
			return;
		}
		source.getFields().getCombatAttributes().setHasAttacked(true).setAttacking(victim);
		if (source.getFields().getCombatAttributes().getAttackDelay() <= 0) {
			doAttack(source, victim);
		}
		GameServer.getTaskManager().submit(new Task() {
			@Override
			public void execute() {
				if (canAttack(source, victim)) {
					if (source.getFields().getCombatAttributes().getAttackDelay() <= 0) {
						if (attackCheck(source, victim)) {
							doAttack(source, victim);
						} else {
							stop();
						}
					}
				} else {
					stop();
				}
			}	
		});
	}
	
	/**
	 * Handles the actual attack of one {@link org.niobe.model.GameCharacter}
	 * to another.
	 * @param source	The {@link org.niobe.model.GameCharacter} who initiated the attack.
	 * @param victim	The {@link org.niobe.model.GameCharacter} who was attacked.
	 */
	public static void doAttack(GameCharacter source, GameCharacter victim) {
		source.getFields().getCombatAttributes().setAttackType(getAttackType(source));
		source.getFields().getCombatAttributes().setHasAttacked(true).setAttacking(victim);
		victim.getFields().getCombatAttributes().setHitDelay(getCombatAction(source).getHitDelay(source, victim));
		if (!source.getFields().getCombatAttributes().isUsingSpecialAttack()) {
			configureAttack(source, victim);
		} else {
			specialAttack((Player)source, victim);
		}
		source.getFields().getCombatAttributes().addAttackDelay(source.getAttackDelay());
		if (victim.getFields().getCombatAttributes().getAttackedBy() == source
				&& !victim.getFields().getCombatAttributes().hasAttacked() &&
				(victim.getFields().getCombatAttributes().isAutoRetaliation() || victim.isMob())) {
			engageCombat(victim, source);
		}
	}

	/**
	 * Configures all the variables once the combat has been
	 * executed.
	 * @param source	The {@link org.niobe.model.GameCharacter} who initiated the attack.
	 * @param victim	The {@link org.niobe.model.GameCharacter} who was attacked.
	 */
	private static void configureAttack(final GameCharacter source, final GameCharacter victim) {
		if (victim.getFields().isDead()) {
			return;
		}
		victim.getFields().getCombatAttributes().setAttackedBy(source);
		source.setEntityInteraction(victim);
		
		final Animation attackAnimation = source.getAttackAnimation();
		
		source.performAnimation(attackAnimation);
		
		final Damage damage = new Damage(getHit(source, victim));

		getCombatAction(source).hit(source, victim, damage);
		
		if (victim.getFields().getCombatAttributes().getHitDelay() <= 0) {
			damage(source, victim, damage);
		} else {
			GameServer.getTaskManager().submit(new Task() {
				@Override
				public void execute() {
					if (victim.getFields().getCombatAttributes().getHitDelay() <= 0) {
						damage(source, victim, damage);
						stop();
					}
				}	
			});
		}
	}
	
	/**
	 * Adds experience to the {@link org.niobe.world.Player}'s 
	 * {@link org.niobe.model.SkillManager.Skill} that is being used
	 * in combat.
	 * @param player	The {@link org.niobe.world.Player} gaining experience.
	 * @param damage	The {@link org.niobe.model.Damage} that player has hit.
	 */
	public static void addExperience(Player player, Damage damage) {
		getCombatAction(player).addExperience(player, damage);
	}
	
	/**
	 * Handles the usage of {@link org.niobe.world.content.combat.special.SpecialAttack}
	 * being used by the {@link org.niobe.world.Player}.
	 * @param player	The {@link org.niobe.world.Player} using the {@link org.niobe.world.content.combat.special.SpecialAttack}.
	 * @param victim	The {@link org.niobe.model.GameCharacter} being hit.
	 */
	public static void specialAttack(Player player, GameCharacter victim) {
		Weapon weapon = player.getWeapon();
		SpecialAttack special = weapon.getSpecialAttack();
		if (special != null) {
			special.execute(player, victim);
		}
	}

	/**
	 * Gets the {@link org.niobe.model.AttackType} the {@link org.niobe.model.GameCharacter}
	 * is using.
	 * @param gameCharacter	The {@link org.niobe.model.GameCharacter} to get {@link org.niobe.model.AttackType} for.
	 * @return				The {@link org.niobe.model.AttackType} being used.
	 */
	private static AttackType getAttackType(GameCharacter gameCharacter) {
		if (gameCharacter.isPlayer()) {
			Player player = (Player) gameCharacter;
			if (player.getFields().getCombatAttributes().getMagicSpellId() > 0) {
				return AttackType.MAGIC;
			} else if (player.getWeapon().isRanged()) {
				return AttackType.RANGED;
			}
		} else if (gameCharacter.isMob()){
			return ((Mob)gameCharacter).getAttackType();
		}
		return AttackType.MELEE;
	}
	
	/**
	 * Gets the {@link org.niobe.model.Hit} that is being dealt
	 * to the {@value victim}.
	 * @param source	The {@link org.niobe.model.GameCharacter} dealing damage.
	 * @param victim	The {@link org.niobe.model.GameCharacter} being hit.
	 * @return			The {@link org.niobe.model.Hit} retrieved.
	 */
	public static Hit getHit(GameCharacter source, GameCharacter victim) {
		int maxHit = 0;
		switch (source.getFields().getCombatAttributes().getAttackType()) {
		case MELEE:
			maxHit = MeleeHitFormula.getMaxHit(source, victim);
			break;
		case RANGED:
			maxHit = 200;//TODO ranged and magic max hit
			break;
		case MAGIC:
			maxHit = 150;
			break;
		case DRAGON_FIRE:
			maxHit = 450;
			break;
		}
		int randomHit = MathUtil.random(maxHit);
		return new Hit(randomHit, getCombatIcon(source), getHitmask(source, randomHit));
	}
	
	/**
	 * Gets the combat icon the {@param source}'s combat victim
	 * will have in their {@link org.niobe.model.Damage}.
	 * @param source	The {@link org.niobe.model.GameCharacter} doing the attack.
	 * @return			The {@link org.niobe.model.Damage.CombatIcon} the victim will have.
	 */
	public static CombatIcon getCombatIcon(GameCharacter source) {
		AttackType attackType = source.getFields().getCombatAttributes().getAttackType();
		CombatIcon icon = attackType == AttackType.RANGED ? CombatIcon.RANGED :
			(attackType == AttackType.MAGIC || attackType == AttackType.DRAGON_FIRE) ? CombatIcon.MAGIC : CombatIcon.MELEE;
		return icon;
	}
	
	/**
	 * Gets the hit mask the {@param source}'s combat victim
	 * will have in their {@link org.niobe.model.Damage}.
	 * @param source	The {@link org.niobe.model.GameCharacter} doing the attack.
	 * @return			The {@link org.niobe.model.Damage.Hitmask} the victim will have.
	 */
	public static Hitmask getHitmask(GameCharacter source, int hit) {
		//TODO fix this
		Hitmask hitmask = Hitmask.NORMAL;
		if (hit <= 150) {
			hitmask = Hitmask.LOWEST_DAMAGE; 
		} else {
			if (source.isPlayer()) {
				if (MathUtil.random(100 - ((Player)source).getSkillManager().getMaxLevel(Skill.ATTACK)) == 0) {
					hitmask = Hitmask.CRITICAL;
				}
			}
		}
		return hitmask;
	}
	
	/**
	 * Gets the required distance between specified
	 * {@link org.niobe.model.GameCharacter}s in order for
	 * the combat to be executed.
	 * @param source	The {@link org.niobe.model.GameCharacter} calling the hook.
	 * @param victim	The victim {@link org.niobe.model.GameCharacter}.
	 */
	public static int getDistanceRequired(GameCharacter source, GameCharacter victim) {
		if (source.isPlayer()) {
			return getCombatAction(source).getDistanceRequirement(source, victim);
		} else if (source.isMob()) {
			Mob mob = (Mob) source;
			return mob.getDefinition().getSize();
		}
		return victim.getSize();
	}
	
	/**
	 * Checks if combat can be executed between the two
	 * {@link org.niobe.model.GameCharacter}s.
	 * @param source	The {@link org.niobe.model.GameCharacter} who initiated the attack.
	 * @param victim	The {@link org.niobe.model.GameCharacter} who was attacked.
	 * @return			{@code true} if combat can be continued normally.
	 */
	private static boolean canAttack(GameCharacter source, GameCharacter victim) {
		if (source.getFields().isDead() || victim.getFields().isDead()) {
			resetFlags(source);
			resetFlags(victim);
			return false;
		}
		if (source.getFields().getCombatAttributes().getAttacking() == null) {
			return false;
		}
		if (!source.getPosition().isWithinDistance(victim.getPosition(), getDistanceRequired(source, victim))) {
			if (source.getFields().getCombatAttributes().getAttacking() == victim
					&& source.getFields().getCombatAttributes().hasAttacked()) {
				source.getFields().getCombatAttributes().setHasAttacked(false);
				engageCombat(source, victim);
			} else {
				resetFlags(source);
			}
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the interaction between {@param source} and {@param victim}
	 * can be successfully completed.
	 * @param source	The {@link org.niobe.model.GameCharacter} initiating the attack.
	 * @param victim	The {@link org.niobe.model.GameCharacter} being attacked.
	 * @return			If {@code true} the {@param source} can successfully attack.
	 */
	private static boolean attackCheck(GameCharacter source, GameCharacter victim) {
		if (victim.getFields().getCombatAttributes().getAttackedBy() != null
		 		&& victim.getFields().getCombatAttributes().getAttackedBy() != source && !inMultiCombatArea(victim)) {
			if (source.isPlayer()) {
				Player player = (Player) source;
				String name = victim.isPlayer() ? "player" : "npc";
				player.getPacketSender().sendMessage("That " + name + " is already in combat!");
			}
			source.getFields().getCombatAttributes().setHasAttacked(false).setAttacking(null);
			return false;
		} else if (source.getFields().getCombatAttributes().getAttackedBy() != null
				&& source.getFields().getCombatAttributes().getAttackedBy() != victim && !inMultiCombatArea(source)) {
			if (source.isPlayer()) {
				Player player = (Player) source;
				player.getPacketSender().sendMessage("You are already under attack!");
			}
			source.getFields().getCombatAttributes().setHasAttacked(false).setAttacking(null);
			return false;
		}
		return getCombatAction(source).canInitiateCombat(source, victim);
	}
	
	/**
	 * Resets the {@link org.niobe.model.GameCharacter}'s flags
	 * used in combat.
	 * @param gameCharacter	The {@link org.niobe.model.GameCharacter} to reset flags for.
	 */
	public static void resetFlags(GameCharacter gameCharacter) {
		gameCharacter.setEntityInteraction(null);
		gameCharacter.getFields().getCombatAttributes().setAttacking(null).setHasAttacked(false);
	}
	
	/**
	 * Checks if {@link org.niobe.model.GameCharacter} is in a
	 * multi-combat area.
	 * @param gameCharacter	The {@link org.niobe.model.GameCharacter} to check for.
	 */
	public static boolean inMultiCombatArea(GameCharacter gameCharacter) {
		//TODO get multi combat areas
		return false;
	}
	
	/**
	 * Adds and starts the poison damage task for
	 * the {@link org.niobe.model.GameCharacter}
	 * @param gameCharacter	The character to deal poison damage to.
	 * @param poison		The initial poison damage.
	 */
	public static void poison(GameCharacter gameCharacter, int poison) {
		if (gameCharacter.getFields().getPoisonDamage() > 0)
			return;
		gameCharacter.getFields().setPoisonDamage(poison);
		GameServer.getTaskManager().submit(new PoisonDamageTask(gameCharacter));
		if (gameCharacter.isPlayer()) {
			Player player = (Player) gameCharacter;
			player.getPacketSender().sendConstitutionOrbStatus(true);
			player.getPacketSender().sendMessage("You have been poisoned!");
		}
	}
	
	/**
	 * Deals damage to the {@param victim}.
	 * @param source	The source of the combat - the one who initiated the attack.
	 * @param victim	The victim - the one who was attacked and who will be damaged.
	 * @param damage	The {@link org.niobe.model.Damage} dealt to victim.
	 */
	public static void damage(final GameCharacter source, final GameCharacter victim, final Damage damage) {
		victim.setDamage(damage);
		victim.performAnimation(victim.getBlockAnimation());
		victim.getFields().getCombatAttributes().setDamageDelay(System.currentTimeMillis());
		int totalDamage = damage.getHits()[0].getDamage() + (damage.getHits().length > 1 ? damage.getHits()[1].getDamage() : 0) 
							+ (victim.getFields().getCombatAttributes().getDamageMap().containsKey(source) ? victim.getFields().getCombatAttributes().getDamageMap().get(source) : 0);
		victim.getFields().getCombatAttributes().getDamageMap().put(source, totalDamage);
		GameServer.getTaskManager().submit(new Task(0) {
			@Override
			public void execute() {
				getCombatAction(source).specialHitAction(source, victim, damage);
				stop();
			}
		});
		if (source.isPlayer()) {
			CombatManager.addExperience(((Player)source), damage);
		}
	}
	
	/**
	 * Gets the {@link org.niobe.world.content.combat.action.CombatAction} for
	 * said {@link org.niobe.model.GameCharacter}.
	 * @return	The {@link org.niobe.world.content.combat.action.CombatAction} correspondent
	 * 			to the game character's attack type.
	 */
	public static CombatAction getCombatAction(GameCharacter gameCharacter) {
		switch (gameCharacter.getFields().getCombatAttributes().getAttackType()) {
		case RANGED:
			return RANGED_COMBAT_ACTION;
		case MAGIC:
		case DRAGON_FIRE:
			return MAGIC_COMBAT_ACTION;
		default:
			return MELEE_COMBAT_ACTION;
		}
	}
	
	/**
	 * Gets the {@link org.niobe.model.GameCharacter} that has done
	 * the most damage to the {@link victim}.
	 * @param victim	The {@link org.niobe.model.GameCharacter} that was killed.
	 * @return			The {@link org.niobe.model.GameCharacter} that did the most damage to the {@link victim}.
	 */
	public static GameCharacter getKillerByMostDamage(GameCharacter victim) {
		Entry<GameCharacter, Integer> maxEntry = null;
		for(Entry<GameCharacter, Integer> entry : victim.getFields().getCombatAttributes().getDamageMap().entrySet()) {
		    if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
		        maxEntry = entry;
		    }
		}
		return maxEntry != null ? maxEntry.getKey() : null;
	}
	
	/**
	 * The Melee {@link org.niobe.world.content.combat.action.CombatAction} instance.
	 */
	private static final CombatAction MELEE_COMBAT_ACTION = new MeleeCombatAction();
	
	/**
	 * The Magic {@link org.niobe.world.content.combat.action.CombatAction} instance.
	 */
	private static final CombatAction MAGIC_COMBAT_ACTION = new MagicCombatAction();
	
	/**
	 * The Ranged {@link org.niobe.world.content.combat.action.CombatAction} instance.
	 */
	private static final CombatAction RANGED_COMBAT_ACTION = new RangedCombatAction();
}
