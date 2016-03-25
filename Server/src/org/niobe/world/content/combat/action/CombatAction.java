package org.niobe.world.content.combat.action;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.AttackType;
import org.niobe.model.Damage;
import org.niobe.model.Graphic;
import org.niobe.model.PrayerBook;
import org.niobe.model.Damage.CombatIcon;
import org.niobe.model.Damage.Hit;
import org.niobe.model.Graphic.GraphicHeight;
import org.niobe.model.GameCharacter;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.container.impl.Equipment;
import org.niobe.util.MathUtil;
import org.niobe.world.Player;
import org.niobe.world.Projectile;
import org.niobe.world.content.combat.CombatCurses;
import org.niobe.world.content.combat.CombatManager;
import org.niobe.world.content.combat.curse.*;

/**
 * An abstract class that handles one of the
 * {@link org.niobe.model.AttackType} combat actions.
 *
 * @author relex lawl
 */
public abstract class CombatAction {
	
	/**
	 * Checks if the source {@link org.niobe.model.GameCharacter} can
	 * initiate combat for said {@link CombatAction}.
	 */
	public abstract boolean canInitiateCombat(GameCharacter source, GameCharacter victim);

	/**
	 * Gets the distance required for the source {@link org.niobe.model.GameCharacter}
	 * to attack the victim {@link org.niobe.model.GameCharacter}.
	 */
	public abstract int getDistanceRequirement(GameCharacter source, GameCharacter victim);
	
	/**
	 * Gets the delay for the victim {@link org.niobe.model.GameCharacter}
	 * to receive the actual damage and call {@link org.niobe.model.GameCharacter#setDamage(Damage)}. 
	 */
	public abstract int getHitDelay(GameCharacter source, GameCharacter victim);
	
	/**
	 * Handles actions that should be done as soon as the source 
	 * {@link org.niobe.model.GameCharacter} attacks.
	 */
	public abstract void hit(GameCharacter source, GameCharacter victim, Damage damage);
	
	/**
	 * Adds experience in specified {@link org.niobe.model.SkillManager.Skill}
	 * for the {@link org.niobe.world.Player}.
	 */
	public abstract void addExperience(Player player, Damage damage);
	
	/**
	 * Handles special actions that should be executed upon the calling
	 * of the victim {@link org.niobe.model.GameCharacter#setDamage(Damage)} method. 
	 * @param source	The {@link org.niobe.model.GameCharacter} who initiated combat.
	 * @param victim	The {@link org.niobe.model.GameCharacter} who was attacked.
	 * @param damage	The {@link org.niobe.model.Damage} dealt to the victim.
	 */
	public void specialHitAction(GameCharacter source, final GameCharacter victim, Damage damage) {
		
		if (victim.isPlayer()) {
			Player player = (Player) victim;
			int deflect = 0;
			if (player.getEquipment().getItems()[Equipment.RING_SLOT].getDefinition().
					getName().toLowerCase().contains("ring of recoil")) {
				for (Hit hit : damage.getHits()) {
					deflect += Math.ceil(hit.getDamage() * .1);
				}
			}
			
			AttackType attackType = source.getFields().getCombatAttributes().getAttackType();
			if (player.getFields().getPrayerBook() == PrayerBook.CURSES) {
				int deflectId = (attackType == (AttackType.MAGIC) || attackType == AttackType.DRAGON_FIRE) ? CombatCurses.DEFLECT_MAGIC :
									attackType == AttackType.RANGED ? CombatCurses.DEFLECT_MISSILES : CombatCurses.DEFLECT_MELEE;
				int deflectGraphicId = deflectId == CombatCurses.DEFLECT_MISSILES ? 2229 : 
										deflectId == CombatCurses.DEFLECT_MAGIC ? 2228 : 2230;
				if (player.getFields().getPrayerActive()[deflectId]) {
					player.performAnimation(new Animation(12573));
					player.performGraphic(new Graphic(deflectGraphicId, GraphicHeight.LOW));
					if (MathUtil.random(3) == 0) {
						for (Hit hit : damage.getHits()) {
							deflect += (hit.getDamage() * .1);
							hit.setDamage((int) (hit.getDamage() * .35));
						}
					}
				}
			}
			
			if (player.getFields().getCombatAttributes().hasVengeance()) {
				for (Hit hit : damage.getHits()) {
					deflect += (hit.getDamage() * .75);
				}
			}
			
			if (deflect > 0) {
				Damage deflectDamage = new Damage(new Hit(deflect, CombatIcon.DEFLECT, CombatManager.getHitmask(source, deflect)));
				source.setDamage(deflectDamage);
			}
		}
		
		if (source.getFields().getPrayerBook() == PrayerBook.CURSES
				&& source.isPlayer()) { 
			Player player = (Player) source;
			boolean[] active = source.getFields().getPrayerActive();

			if (active[CombatCurses.SAP_WARRIOR]) {
				LEECH_CURSES[SAP_WARRIOR].execute(source, victim);
			} 

			if (active[CombatCurses.SAP_RANGER]) {
				LEECH_CURSES[SAP_RANGER].execute(source, victim);
			}

			if (active[CombatCurses.SAP_MAGE]) {
				LEECH_CURSES[SAP_MAGIC].execute(source, victim);
			}

			if (active[CombatCurses.SAP_SPIRIT]) {
				LEECH_CURSES[SAP_SPIRIT].execute(source, victim);
			} 

			if (active[CombatCurses.LEECH_ATTACK]) {
				LEECH_CURSES[LEECH_ATTACK].execute(source, victim);
			}

			if (active[CombatCurses.LEECH_RANGED]) {
				LEECH_CURSES[LEECH_RANGED].execute(source, victim);
			}

			if (active[CombatCurses.LEECH_MAGIC]) {
				LEECH_CURSES[LEECH_MAGIC].execute(source, victim);
			}

			if (active[CombatCurses.LEECH_DEFENCE]) {
				LEECH_CURSES[LEECH_DEFENCE].execute(source, victim);
			}

			if (active[CombatCurses.LEECH_STRENGTH]) {
				LEECH_CURSES[LEECH_STRENGTH].execute(source, victim);
			}

			if (active[CombatCurses.LEECH_ENERGY]) {
				LEECH_CURSES[LEECH_ENERGY].execute(source, victim);
			}

			if (active[CombatCurses.LEECH_SPECIAL_ATTACK]) {
				LEECH_CURSES[LEECH_SPECIAL].execute(source, victim);
			}

			if (active[CombatCurses.SOUL_SPLIT]) {
				source.performAnimation(new Animation(12575));
				for (Hit hit : damage.getHits()) {
					final int prayerGain = victim.isPlayer() ? (int) (hit.getDamage() * .2) : (int) (hit.getDamage() * .4);
					if (victim.isPlayer()) {
						Player target = ((Player) victim);
						double drain = hit.getDamage() * 1.2;
						int level = (int) (target.getSkillManager().getCurrentLevel(Skill.PRAYER) - drain);
						target.getSkillManager().setCurrentLevel(Skill.PRAYER, level > 0 ? level : 0);
					}
					final int totalPrayer = player.getSkillManager().getCurrentLevel(Skill.PRAYER) + prayerGain > player.getSkillManager().getMaxLevel(Skill.PRAYER) ?
												player.getSkillManager().getMaxLevel(Skill.PRAYER) : player.getSkillManager().getCurrentLevel(Skill.PRAYER) + prayerGain;
					player.getSkillManager().setCurrentLevel(Skill.PRAYER, totalPrayer);
					final int distance = source.getPosition().getDistance(victim.getPosition());
					Projectile projectile = new Projectile(source.getPosition(), victim.getPosition(), new Graphic(2263), victim, 40 + (distance * 5), 30, 30);
					GameServer.getWorld().register(projectile);
					final int distance2 = victim.getPosition().getDistance(source.getPosition());
					projectile = new Projectile(victim.getPosition(), source.getPosition(), new Graphic(2263), source, 20 + (distance2 * 5), 40 + (distance2 * 5), 30);
					GameServer.getWorld().register(projectile);
					victim.performGraphic(new Graphic(2264));
					int heal = (int) (hit.getDamage() * (hit.getDamage() > 200 ? .05 : .1));
					if (heal > 0) {
						heal = heal + player.getConstitution();
						if (heal > player.getSkillManager().getMaxLevel(Skill.CONSTITUTION)) {
							heal = player.getSkillManager().getMaxLevel(Skill.CONSTITUTION);
						}
						source.setConstitution(heal);
					}
				}
			}
		}
	}
	
	/**
	 * This {@link org.niobe.world.content.combat.curse.LeechCurse} array
	 * contains the available drain curses.
	 */
	private static final LeechCurse[] LEECH_CURSES = {
		new SapWarriorCurse(),
		new SapRangerCurse(),
		new SapMagicCurse(),
		new SapSpiritCurse(),
		
		new LeechAttackCurse(),
		new LeechRangedCurse(),
		new LeechMagicCurse(),
		new LeechDefenceCurse(),
		new LeechStrengthCurse(),
		new LeechSpecialCurse(),
		new LeechEnergyCurse(),
	};
	
	/**
	 * These constants contain the index for said curse in the
	 * {@link #LEECH_CURSES} array.
	 */
	private static final int SAP_WARRIOR = 0, SAP_RANGER = 1, SAP_MAGIC = 2, SAP_SPIRIT = 3,
							 LEECH_ATTACK = 4, LEECH_RANGED = 5, LEECH_MAGIC = 6, LEECH_DEFENCE = 7,
							 LEECH_STRENGTH = 8, LEECH_SPECIAL = 9, LEECH_ENERGY = 10;
}
