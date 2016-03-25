package org.niobe.world.content.combat.special.impl;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.GameCharacter;
import org.niobe.model.Graphic;
import org.niobe.model.MovementQueue.MovementFlag;
import org.niobe.model.definition.MobDefinition;
import org.niobe.task.Task;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.content.combat.special.SpecialAttack;

/**
 * An implementation of {@link org.niobe.world.content.combat.special.SpecialAttack}
 * that handles the special attack of a zamorak godsword.
 *
 * @author relex lawl
 */
public final class ZamorakGodswordSpecialAttack extends SpecialAttack {

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
		return 60;
	}
	
	@Override
	public void specialAction(Player player, final GameCharacter victim, Damage damage) {
		if (damage.getHits()[0].getDamage() > 0) {
			boolean showGraphic = true;
			if (victim.isMob()) {
				MobDefinition definition = ((Mob) victim).getDefinition();
				if (definition.getSize() > 1) {
					showGraphic = false;
				}
			}
			if (showGraphic) {
				victim.performGraphic(VICTIM_GRAPHIC);
			}
			victim.getMovementQueue().setMovementFlag(MovementFlag.FROZEN);
			GameServer.getTaskManager().submit(new Task(20) {//TODO 20 seconds, not ticks i guess
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
	private static final Animation ANIMATION = new Animation(7070);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for this 
	 * special attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(1221);
	
	/**
	 * The {@link org.niobe.model.Graphic} used for the victim 
	 * in this special attack.
	 */
	private static final Graphic VICTIM_GRAPHIC = new Graphic(2104);
}
