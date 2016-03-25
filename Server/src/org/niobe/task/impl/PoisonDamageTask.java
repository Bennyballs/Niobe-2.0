package org.niobe.task.impl;

import org.niobe.model.Damage;
import org.niobe.model.Damage.Hit;
import org.niobe.model.Damage.Hitmask;
import org.niobe.model.GameCharacter;
import org.niobe.model.Damage.CombatIcon;
import org.niobe.task.Task;
import org.niobe.world.Player;

/**
 * An implementation of {@link org.niobe.task.Task}
 * used for the poison damage dealt to {@link org.niobe.model.GameCharacter}s
 * over time.
 *
 * @author relex lawl
 */
public final class PoisonDamageTask extends Task {

	/**
	 * The PoisonDamageTask constructor.
	 * @param gameCharacter	The {@link org.niobe.model.GameCharacter} to deal poison damage to.
	 */
	public PoisonDamageTask(GameCharacter gameCharacter) {
		super(30);
		this.gameCharacter = gameCharacter;
	}
	
	/**
	 * The {@link org.niobe.model.GameCharacter} deal damage to.
	 */
	private final GameCharacter gameCharacter;
	
	@Override
	public void execute() {
		int poison = gameCharacter.getFields().getPoisonDamage();
		if (poison > 0) {
			Damage damage = new Damage(new Hit(poison, CombatIcon.NONE, Hitmask.LOW_POISON_DAMAGE));
			gameCharacter.setDamage(damage);
			gameCharacter.getFields().setPoisonDamage(poison - 10);
		} else {
			stop();
			if (gameCharacter.isPlayer()) {
				((Player)gameCharacter).getPacketSender().sendConstitutionOrbStatus(false);
			}
		}
	}
}
