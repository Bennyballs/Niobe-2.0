package org.niobe.world.content.combat.special;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.Damage.Hit;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.model.container.impl.Equipment;
import org.niobe.model.weapon.WeaponSpecialBar;
import org.niobe.task.Task;
import org.niobe.task.impl.PlayerSpecialAmountTask;
import org.niobe.world.Player;
import org.niobe.world.content.combat.CombatManager;

/**
 * An abstract class that handles special attacks
 * that can be used in game character combat.
 *
 * @author relex lawl
 */
public abstract class SpecialAttack {

	/**
	 * The animation performed when using this special.
	 */
	public abstract Animation getAnimation();
	
	/**
	 * The graphic performed when using this special.
	 */
	public abstract Graphic getGraphic();
	
	/**
	 * The amount of {@link org.niobe.world.util.GameCharacterFields #getCombatAttributes()#getSpecialAmount()}
	 * energy that will be deducted upon the usage of this special.
	 */
	public abstract int getSpecialAmount();
	
	/**
	 * The default strength multiplier.
	 * @return	The strength bonus multiplier.
	 */
	public double getMultiplier() {
		return 1;
	}
	
	/**
	 * The default accuracy used to see when a 
	 * {@link org.niobe.world.Player} will land
	 * a successful hit.
	 * @return	The accuracy.
	 */
	public double getAccuracy() {
		return 1;
	}
	
	/**
	 * The default extra damage that this special will cause.
	 * @param player	The player using the special.
	 * @param victim	The {@link org.niobe.model.GameCharacter} being hit.
	 * @return			The extra damage that will be done.
	 */
	public int getExtraDamage(Player player, GameCharacter victim) {
		return 0;
	}
	
	/**
	 * The default flag that checks if the special
	 * causes a double hit and flags the victim's
	 * {@link org.niobe.model.UpdateFlag.Flag#DOUBLE_HIT}.
	 * @return	{@code true} if the special causes a double hit.
	 */
	public boolean isDoubleHit() {
		return false;
	}
	
	/**
	 * The default flag that checks if the {@link SpecialAttack #getGraphic()}
	 * will be performed on the victim.
	 * @return	{@code true} if the graphic will be shown on the victim.
	 */
	public boolean victimGraphic() {
		return false;
	}

	/**
	 * The default flag that checks if the {@link SpecialAttack #execute(Player, GameCharacter)}
	 * will be called upon clicking the special attack bar.
	 * @param player	The {@link org.niobe.world.Player} using the special.
	 * @return			{@code true} if the special is immediately executed when special attack bar is clicked upon.
	 */
	public boolean isImmediate(Player player) {
		return false;
	}
	
	/**
	 * The default flag that checks if the special 
	 * will modify the {@link org.niobe.model.Damage} hit
	 * and will handle said damage through {@link #specialAction(Player, GameCharacter, Damage)}.
	 * @return	if {@code true} the {@link #execute(Player, GameCharacter)} method will not handle the damage being dealt.
	 */
	public boolean modifyDamage() {
		return false;
	}
	
	/**
	 * The default action that can be caused by the special, such as
	 * {@link org.niobe.world.content.combat.special.impl.DragonScimitarSpecialAttack}
	 * @param player	The player using the {@link SpecialAttack} implementation.
	 * @param victim	The {@link org.niobe.model.GameCharacter} being hit with the special.
	 * @param damage	The {@link org.niobe.model.Damage} being dealt in combat.
	 */
	public void specialAction(Player player, GameCharacter victim, Damage damage) {
		
	}
	
	/**
	 * The actual execution of the special attack.
	 * @param player	The player performing the special attack.
	 * @param victim	The {@link org.niobe.model.GameCharacter} being hit.
	 */
	public void execute(final Player player, final GameCharacter victim) {
		if (player.getFields().getCombatAttributes().getSpecialAttackAmount() < getSpecialAmount()) {
			player.getPacketSender().sendMessage("You don't have the required special energy to use this attack.");
			player.getFields().getCombatAttributes().setUsingSpecialAttack(false);
			WeaponSpecialBar.update(player);
			return;
		}
		int specialDecrease = getSpecialAmount();
		if (player.getEquipment().getItems()[Equipment.RING_SLOT].getDefinition().getName().toLowerCase().equals("ring of vigour")) {
			specialDecrease -= (specialDecrease / 10); //TODO check if this is correct?
		}
		if (victim != null) {
			victim.getFields().getCombatAttributes().setAttackedBy(player);
			player.getFields().getCombatAttributes().setAttacking(victim);
		}
		player.getFields().getCombatAttributes().setSpecialAttackAmount(player.getFields().getCombatAttributes().getSpecialAttackAmount() - specialDecrease);
		if (!player.getFields().getCombatAttributes().isRecoveringSpecialAttack()) {
			GameServer.getTaskManager().submit(new PlayerSpecialAmountTask(player));
		}
		player.setEntityInteraction(victim);
		player.performAnimation(getAnimation());
		if (getGraphic() != null) {
			if (victimGraphic() && victim != null) {
				victim.performGraphic(getGraphic());
			} else {
				player.performGraphic(getGraphic());
			}
		}
		Damage initialDamage = null;
		if (victim != null && !modifyDamage()) {
			//TODO max hit formula for ranged and magic
			final Hit hit = CombatManager.getHit(player, victim);
			if (!isDoubleHit()) {
				initialDamage = new Damage(hit);
			} else {
				initialDamage = new Damage(hit, CombatManager.getHit(player, victim));
			}
		}
		final Damage damage = initialDamage;
		specialAction(player, victim, damage);
		if (victim != null && damage != null) {
			CombatManager.getCombatAction(player).hit(player, victim, damage);
			if (victim.getFields().getCombatAttributes().getHitDelay() <= 0) {
				CombatManager.damage(player, victim, damage);
			} else {
				GameServer.getTaskManager().submit(new Task() {
					@Override
					public void execute() {
						if (victim.getFields().getCombatAttributes().getHitDelay() <= 0) {
							CombatManager.damage(player, victim, damage);
							stop();
						}
					}	
				});
			}
			victim.getFields().getCombatAttributes().setAttackedBy(player);
		}
		player.getFields().getCombatAttributes().setUsingSpecialAttack(false);
		WeaponSpecialBar.update(player);
	}
}
