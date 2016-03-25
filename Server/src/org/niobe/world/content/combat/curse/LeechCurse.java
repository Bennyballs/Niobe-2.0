package org.niobe.world.content.combat.curse;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.weapon.WeaponSpecialBar;
import org.niobe.task.Task;
import org.niobe.task.impl.PlayerRunEnergyTask;
import org.niobe.task.impl.PlayerSpecialAmountTask;
import org.niobe.util.MathUtil;
import org.niobe.world.Player;
import org.niobe.world.Projectile;

/**
 * An abstract class that handles all the
 * leech and sap curses for {@link org.niobe.model.PrayerBook#CURSES}
 *
 * @author relex lawl
 */
public abstract class LeechCurse {

	/**
	 * Executes the effect of the leech and sap curses.
	 * @param source	The {@link org.niobe.model.GameCharacter} who initiated the combat.
	 * @param victim	The {@link org.niobe.model.GameCharacter} who was attacked.
	 */
	public void execute(final GameCharacter source, final GameCharacter victim) {
		final LeechType leechType = getLeechType();
		int random = leechType == LeechType.SAP ? 10 : 15;
		if (MathUtil.random(random) != 0) {
			return;
		}
		if (!leechSpecial() && !leechRun()) {
			boolean overTimeEffect = victim.getFields().getCombatAttributes().getDamageMap().get(source) > 1000;
			int drain = leechType == LeechType.SAP ? (overTimeEffect ? 3 : 9) : (overTimeEffect ? 6 : 12);
			victim.getFields().getCombatAttributes().setCurseDrain(getLeechedSkill().ordinal(), drain);
			if (leechType == LeechType.LEECH) {
				drain = (overTimeEffect ? 2 : 8);
				source.getFields().getCombatAttributes().setCurseGain(getLeechedSkill().ordinal(), drain);
			}
		} else if (leechSpecial()) {
			if (victim.isPlayer() && victim.getFields().getCombatAttributes().getSpecialAttackAmount() > 0) {
				Player target = (Player) victim;
				int totalSpecial = victim.getFields().getCombatAttributes().getSpecialAttackAmount() - 10;
				if (totalSpecial < 0) {
					totalSpecial = 0;
				}
				victim.getFields().getCombatAttributes().setSpecialAttackAmount(totalSpecial);
				WeaponSpecialBar.update(target);
				
				if (!victim.getFields().getCombatAttributes().isRecoveringSpecialAttack()) {
					GameServer.getTaskManager().submit(new PlayerSpecialAmountTask(target));
				}
				
				if (leechType == LeechType.LEECH && source.isPlayer()) {
					totalSpecial = source.getFields().getCombatAttributes().getSpecialAttackAmount() + 10;
					if (totalSpecial > 100) {
						totalSpecial = 100;
					}
					source.getFields().getCombatAttributes().setSpecialAttackAmount(totalSpecial);
					WeaponSpecialBar.update((Player)source);
				}
			}	
		} else if (leechRun()) {
			if (victim.isPlayer() && victim.getFields().getRunEnergy() > 0) {
				Player target = (Player) victim;
				int totalRun = victim.getFields().getRunEnergy() - 10;
				if (totalRun < 0) {
					totalRun = 0;
				}
				victim.getFields().setRunEnergy(totalRun);
				target.getPacketSender().sendRunEnergy(totalRun);
				
				if (!victim.getFields().isRecoveringRunEnergy()) {
					GameServer.getTaskManager().submit(new PlayerRunEnergyTask(target));
				}
				
				if (leechType == LeechType.LEECH && source.isPlayer()) {
					totalRun = source.getFields().getRunEnergy() + 10;
					if (totalRun > 100) {
						totalRun = 100;
					}
					source.getFields().setRunEnergy(totalRun);
					((Player)source).getPacketSender().sendRunEnergy(totalRun);
				}
			}
		}
		source.performAnimation(leechType == LeechType.SAP ? SAP_ANIMATION : LEECH_ANIMATION);
		if (getGraphic()[0].getId() != -1) {
			source.performGraphic(getGraphic()[0]);
		}
		
		Projectile projectile = new Projectile(source.getPosition(), victim.getPosition(), getGraphic()[1], victim);
		GameServer.getWorld().register(projectile);
		
		GameServer.getTaskManager().submit(new Task(source.getFields().getCombatAttributes().getHitDelay()) {
			@Override
			public void execute() {
				victim.performGraphic(getGraphic()[2]);
				stop();
			}
		});
		
		victim.sendMessage("Your " + getName() + " has been drained.");
		if (leechType == LeechType.LEECH) {
			source.sendMessage("Your " + getName() + " has been boosted.");
		}
	}
	
	/**
	 * Gets the name of the entity being drained.
	 */
	public abstract String getName();
	
	/**
	 * Gets the {@link LeechType} - whether it's a sap or a leech
	 * curse.
	 */
	public abstract LeechType getLeechType();
		
	/**
	 * Gets the {@link org.niobe.model.SkillManager.Skill} being
	 * drained - if any.
	 */
	public abstract Skill getLeechedSkill();
	
	/**
	 * Gets the {@link org.niobe.model.Graphic} that will
	 * be performed throughout the curse effect.
	 */
	public abstract Graphic[] getGraphic();
	
	/**
	 * The default flag that checks if the curse
	 * drains the victim's special attack energy.
	 * @return	If {@code true} the curse drains the victim's special attack energy.
	 */
	public boolean leechSpecial() {
		return false;
	}
	
	/**
	 * The default flag that checks if the curse
	 * drains the victim's run energy.
	 * @return	If {@code true} the curse drains the victim's run energy.
	 */
	public boolean leechRun() {
		return false;
	}
	
	/**
	 * Represents a type of leech, whether
	 * it will be a sap curse or a leech curse.
	 *
	 * @author relex lawl
	 */
	public enum LeechType {
		/**
		 * The sap curse type.
		 */
		SAP,
		
		/**
		 * The leech curse type.
		 */
		LEECH;
	}
	
	/**
	 * The {@link org.niobe.model.Animation} performed if {@link #getLeechType()}
	 * is equal to {@link LeechType#SAP}.
	 */
	private static final Animation SAP_ANIMATION = new Animation(12569);
	
	/**
	 * The {@link org.niobe.model.Animation} performed if {@link #getLeechType()}
	 * is equal to {@link LeechType#LEECH}.
	 */
	private static final Animation LEECH_ANIMATION = new Animation(12575);
}
