package org.niobe.world.update;

import java.util.concurrent.Phaser;

/**
 * An implementation of {@link AbstractGameUpdate} which will
 * be submitted into the {@link ParallelGameUpdate}'s {@code executor}.
 *
 * @author relex lawl
 */
public final class PhasedGameUpdate extends AbstractGameUpdate {

	/**
	 * The PhasedGameUpdate constructor.
	 * @param phaser	The phaser used in the multi-core machine.
	 * @param update	The {@link AbstractGameUpdate} implementation to represent.
	 */
	public PhasedGameUpdate(Phaser phaser, AbstractGameUpdate update) {
		this.phaser = phaser;
		this.update = update;
	}
	
	/**
	 * The phaser used to synchronize the {@link AbstractGameUpdate}s.
	 */
	private final Phaser phaser;
	
	/**
	 * The updating procedure to represent.
	 */
	private final AbstractGameUpdate update;
	
	@Override
	public void run() {
		try {
			update.run();
		} finally {
			phaser.arriveAndDeregister();
		}
	}

}
