package org.niobe.world.content.combat.special.impl;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.AttackType;
import org.niobe.model.Damage;
import org.niobe.model.Damage.CombatIcon;
import org.niobe.model.Damage.Hit;
import org.niobe.model.Damage.Hitmask;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.task.Task;
import org.niobe.world.Player;
import org.niobe.world.content.combat.CombatManager;
import org.niobe.world.content.combat.formula.melee.MeleeHitFormula;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * An implementation of {@link org.niobe.world.content.combat.special.SpecialAttack}
 * that handles the special of a korasi's sword.
 *
 * @author relex lawl
 */
public final class KorasiSwordSpecialAttack extends SpecialAttack {

	@Override
	public Animation getAnimation() {
		return ANIMATION;
	}

	@Override
	public Graphic getGraphic() {
		return GRAPHIC;
	}

	@Override
	public int getSpecialAmount() {
		return 55;
	}
	
	@Override
	public double getAccuracy() {
		return 5;
	}
	
	@Override
	public double getMultiplier() {
		return 1.5;
	}
	
	@Override
	public boolean modifyDamage() {
		return true;
	}

	@Override
	public void specialAction(final Player player, final GameCharacter victim, Damage damage) {
		player.getFields().getCombatAttributes().setAttackType(AttackType.MAGIC);
		final Damage victimDamage = new Damage(new Hit(MeleeHitFormula.getRandomHit(player, victim), CombatIcon.MAGIC, Hitmask.NORMAL));
		GameServer.getTaskManager().submit(new Task(2) {
			@Override
			public void execute() {
				victim.performGraphic(VICTIM_GRAPHIC);
				CombatManager.damage(player, victim, victimDamage);
				stop();
			}
		});
	}
	
	/**
	 * The {@link org.niobe.model.Animation} used for this 
	 * special attack.
	 */
	private static final Animation ANIMATION = new Animation(14788);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(1729);	
	
	/**
	 * The {@link org.niobe.model.Graphic} used for the victim
	 * in this special attack.
	 */
	private static final Graphic VICTIM_GRAPHIC = new Graphic(1730);
}
