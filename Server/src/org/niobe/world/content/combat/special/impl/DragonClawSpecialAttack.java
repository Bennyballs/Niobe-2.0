package org.niobe.world.content.combat.special.impl;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.Damage.CombatIcon;
import org.niobe.model.Damage.Hit;
import org.niobe.model.Damage.Hitmask;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.task.Task;
import org.niobe.util.MathUtil;
import org.niobe.world.Player;
import org.niobe.world.content.combat.CombatManager;
import org.niobe.world.content.combat.formula.melee.MeleeHitFormula;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * An implementation of {@link org.niobe.world.content.combat.special.SpecialAttack}
 * that handles the special of a pair of dragon claws.
 *
 * @author relex lawl
 */
public final class DragonClawSpecialAttack extends SpecialAttack {

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
		return 50;
	}
	
	@Override
	public boolean victimGraphic() {
		return true;
	}

	@Override
	public boolean isDoubleHit() {
		return true;
	}
	
	@Override
	public double getAccuracy() {
		return 5;
	}
	
	@Override
	public boolean modifyDamage() {
		return true;
	}
	
	@Override
	public void specialAction(final Player player, final GameCharacter victim, Damage damage) {
		final int first = MeleeHitFormula.getRandomHit(player, victim);
		final int second = first <= 0 ? MeleeHitFormula.getRandomHit(player, victim) : (int) (first/2);
		final int third = first <= 0 && second > 0 ? (int) (second/2) :  first <= 0 && second <= 0 ? MeleeHitFormula.getRandomHit(player, victim) : MathUtil.random(second);
		final int fourth = first <= 0 && second <= 0 && third <= 0 ? (int) (MeleeHitFormula.getRandomHit(player, victim) * 1.47) + MathUtil.random(7) : first <= 0 && second <= 0 ?
							MeleeHitFormula.getRandomHit(player, victim) : third;
		damage = new Damage(new Hit(first, CombatIcon.MELEE, Hitmask.NORMAL), new Hit(second, CombatIcon.MELEE, Hitmask.NORMAL));
		CombatManager.damage(player, victim, damage);
		final Damage secondDamage = new Damage(new Hit(third, CombatIcon.MELEE, Hitmask.NORMAL), new Hit(fourth, CombatIcon.MELEE, Hitmask.NORMAL));
		GameServer.getTaskManager().submit(new Task(1) {
			@Override
			public void execute() {
				CombatManager.damage(player, victim, secondDamage);
				stop();
			}
		});
	}
	
	/**
	 * The {@link org.niobe.model.Animation} used for this 
	 * special attack.
	 */
	private static final Animation ANIMATION = new Animation(10961);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(1950);
}
