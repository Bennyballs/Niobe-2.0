package org.niobe.world.content.combat.special.impl;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.model.Graphic.GraphicHeight;
import org.niobe.model.MovementQueue.MovementFlag;
import org.niobe.task.Task;
import org.niobe.world.Player;
import org.niobe.world.content.combat.CombatManager;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * An implementation of {@link org.niobe.world.content.combat.special.SpecialAttack}
 * that handles the special of a dragon spear.
 *
 * @author relex lawl
 */
public final class DragonSpearSpecialAttack extends SpecialAttack {

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
		return 25;
	}
	
	@Override
	public boolean victimGraphic() {
		return true;
	}
	
	@Override
	public boolean modifyDamage() {
		return true;
	}

	@Override
	public void specialAction(Player player, final GameCharacter victim, Damage damage) {
		if (victim.getSize() <= 1) {
			victim.getMovementQueue().stopMovement();
			int x = player.getPosition().getY() == victim.getPosition().getY() ? 
					player.getPosition().getX() > victim.getPosition().getX() ? -1 : 1 : 0;
			int y = player.getPosition().getX() == victim.getPosition().getX() ?
					player.getPosition().getY() > victim.getPosition().getY() ? -1 : 1 : 0;
			if (!victim.getMovementQueue().walk(x, y)) {
				victim.setPositionToFace(player.getPosition());
			}
			victim.getMovementQueue().setMovementFlag(MovementFlag.STUNNED);
			victim.setEntityInteraction(null);
			CombatManager.resetFlags(victim);
			GameServer.getTaskManager().submit(new Task(5) {
				@Override
				public void execute() {
					victim.getMovementQueue().setMovementFlag(MovementFlag.NONE);
					stop();
				}
			});
		}
	}
	
	/**
	 * The {@link org.niobe.model.Animation} used for this 
	 * special attack.
	 */
	private static final Animation ANIMATION = new Animation(12017);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(80, 5, GraphicHeight.HIGH);
}
