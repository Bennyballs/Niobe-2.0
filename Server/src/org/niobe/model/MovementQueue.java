package org.niobe.model;

import java.util.Deque;
import java.util.LinkedList;

import org.niobe.model.SkillManager.Skill;
import org.niobe.util.DirectionUtil;
import org.niobe.world.Player;

/**
 * Represents a {@link GameCharacter}'s movement prompt.
 * 
 * @author relex lawl
 */
public final class MovementQueue {
	
	/**
	 * A list containing a player's destination points.
	 */
	private Deque<WalkPoint> destinations = new LinkedList<WalkPoint>();

	/**
	 * Checks if an update is required for a gameCharacter's updating.
	 * @return	Walking direction or running direction is not -1.
	 */
	public boolean updateRequired() {
		return getDirections()[MovementQueue.WALKING_DIRECTION] != -1 || getDirections()[MovementQueue.RUNNING_DIRECTION] != -1;
	}
	
	/**
	 * Configures the settings needed to start the character's
	 * movement.
	 * @return	The MovementQueue instance.
	 */
	public MovementQueue start() {
		setDirections(-1, -1);
		destinations.clear();
		destinations.add(new WalkPoint(gameCharacter.getPosition(), -1));
		return this;
	}
	
	/**
	 * Finalizes the character's movement.
	 * @return	The MovementQueue instance.
	 */
	public MovementQueue finish() {
		destinations.removeFirst();
		return this;
	}
	
	/**
	 * Finalizes the character's current movement.
	 * @return	The MovementQueue instance.
	 */
	public MovementQueue stopMovement() {
		setDirections(-1, -1);
		destinations.clear();
		return this;
	}
	
	/**
	 * Starts a character's movement and walks the specified
	 * distance.
	 * @param x		The amount of tiles to move to the x-axis.
	 * @param y		The amount of tiles to move to the y-axis.
	 * @return		The MovementQueue instance.
	 */
	public boolean walk(int x, int y) {
		//TODO return false if cannot walk to area (tile is clipped)
		Position position = gameCharacter.getPosition();
		int finalX = (position.getX() + x);
		int finalY = (position.getY() + y);
		start();
		addDestination(new Position(finalX, finalY));
		finish();
		return true;
	}
	
	/**
	 * Adds a step to a destination queue player already has.
	 * @param position	The position to set destination to.
	 */
	public void addDestination(Position position) {
		WalkPoint lastPosition = destinations.peekLast();
		int diffX = position.getX() - lastPosition.position.getX();
		int diffY = position.getY() - lastPosition.position.getY();
		int amountOfSteps = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int i = 0; i < amountOfSteps; i++) {
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++;
			} else if (diffY > 0) {
				diffY--;
			}
			addStep(position.getX() - diffX, position.getY() - diffY);
		}
	}
	
	/**
	 * Adds a single step to the queue internally without counting gaps.
	 * This method is unsafe if used incorrectly so it is private to protect
	 * the queue.
	 * @param x The x coordinate of the step.
	 * @param y The y coordinate of the step.
	 */
	private void addStep(int x, int y) {
		WalkPoint currentPosition = destinations.peekLast();
		int diffX = x - currentPosition.position.getX();
		int diffY = y - currentPosition.position.getY();
		int direction = DirectionUtil.getDirection(diffX, diffY);
		if (direction > -1) {
			destinations.add(new WalkPoint(new Position(x, y), direction));
		}
	}
	
	/**
	 * Checks if game character is moving.
	 * @return	If {@code true} the character is either running or walking.
	 */
	public boolean isMoving() {
		MovementFlag flag = gameCharacter.getMovementQueue().getMovementFlag();
		if (flag == MovementFlag.WALKING || flag == MovementFlag.RUNNING) {
			return true;
		}
		return false;
	}
	
	/**
	 * Movement queue pulse that is initialized upon
	 * the call of {@link org.niobe.world.World #init()}.
	 */
	public void pulse() {
		boolean teleporting = gameCharacter.getFields().getTeleportPosition() != null;
		WalkPoint walkPoint = null, runPoint = null;
		if (teleporting) {
			stopMovement();
			gameCharacter.getFields().setTeleporting(true);
			gameCharacter.getPosition().set(gameCharacter.getFields().getTeleportPosition());
			gameCharacter.getFields().setTeleportPosition(null);
		} else {
			walkPoint = getNextWalkPoint();
			if (gameCharacter.getFields().isRunning())
				runPoint = getNextWalkPoint();
			int walkDirection = walkPoint == null ? -1 : walkPoint.direction;
			int runDirection = runPoint == null ? -1 : runPoint.direction;
			setDirections(walkDirection, runDirection);
		}
		int diffX = gameCharacter.getPosition().getX() - gameCharacter.getLastKnownRegion().getRegionX() * 8;
		int diffY = gameCharacter.getPosition().getY() - gameCharacter.getLastKnownRegion().getRegionY() * 8;
		boolean regionChange = false;
		if(diffX < 16) {
			regionChange = true;
		} else if(diffX >= 88) {
			regionChange = true;
		}
		if(diffY < 16) {
			regionChange = true;
		} else if(diffY >= 88) {
			regionChange = true;
		}
		if (regionChange) {
			gameCharacter.getFields().setRegionChange(true);
		}
	}
	
	/**
	 * Gets the next point of movement.
	 * @return The next point.
	 */
	private WalkPoint getNextWalkPoint() {
		WalkPoint next = destinations.poll();
		if (next == null || next.direction == -1) {
			return null;
		}
		int diffX = DirectionUtil.DIRECTION_DELTA_X[next.direction];
		int diffY = DirectionUtil.DIRECTION_DELTA_Y[next.direction];
		int direction = DirectionUtil.getDirection(diffX, diffY);
		gameCharacter.getPosition().add(DirectionUtil.DIRECTION_DELTA_X[direction], DirectionUtil.DIRECTION_DELTA_Y[direction]);
		return next;
	}
	
	/**
	 * The walking and running direction player has.
	 */
	private int[] direction = new int[2];
	
	/**
	 * Gets the walking and running directions.
	 * @return	The directions player has.
	 */
	public int[] getDirections() {
		return direction;
	}
	
	/**
	 * Sets the walking and running direction.
	 * @param walkingDirection	The player's new walking direction.
	 * @param runningDirection	The player's new running direction.
	 * @return					The MovementQueue instance.
	 */
	public MovementQueue setDirections(int walkingDirection, int runningDirection) {
		direction[WALKING_DIRECTION] = walkingDirection;
		direction[RUNNING_DIRECTION] = runningDirection;
		return this;
	}
	
	/**
	 * These constants are used for the direction array as their respective index.
	 */
	public static int WALKING_DIRECTION = 0, RUNNING_DIRECTION = 1;
	
	/**
	 * The movement flag, checks if player is standing still, walking or running.
	 */
	private MovementFlag movementFlag = MovementFlag.NONE;
	
	/**
	 * Gets the player's movement flag.
	 * @return	The movement flag object.
	 */
	public MovementFlag getMovementFlag() {
		return movementFlag;
	}
	
	/**
	 * Sets the player's movement flag.
	 * @param movementFlag	The new movement flag instance.
	 */
	public void setMovementFlag(MovementFlag movementFlag) {
		this.movementFlag = movementFlag;
		if (gameCharacter.isPlayer()) {
			Player player = (Player) gameCharacter;
			if (movementFlag == MovementFlag.FROZEN) {
				player.getPacketSender().sendMessage("You have been frozen!");
			} else if (movementFlag == MovementFlag.STUNNED) {
				player.getPacketSender().sendMessage("You have been stunned!");
			}
		}
	}

	/**
	 * The MovementQueue constructor.
	 * @param gameCharacter	The gameCharacter associated with this MovementQueue.
	 */
	public MovementQueue(GameCharacter gameCharacter) {
		this.gameCharacter = gameCharacter;
	}

	/**
	 * The gameCharacter associated with this MovementQUeue.
	 */
	private GameCharacter gameCharacter;
	
	/**
	 * Gets the amount of ticks used to determine
	 * the time for the run energy to recover.
	 * @param player	The {@link org.niobe.world.Player} to get recovery rate for.
	 * @return			The recovery rate for the player.
	 */
	public static double getRecoveryRate(Player player) {
		final double basePerTick = 0.096;
		return basePerTick * Math.pow(Math.E, 0.0162569486104454583293005993255170468638949631744294 * player.getSkillManager().getCurrentLevel(Skill.AGILITY));
	}
	
	/**
	 * Gets the amount of energy that will be used
	 * per tile ran.
	 * @return	The amount of energy to reduce from {@link player}.
	 */
	public static double getEnergyDeplecation(Player player) { 
		final double energy_used_per_tile = 0.318;
		return energy_used_per_tile*3 * Math.pow(Math.E, 0.0027725887222397812376689284858327062723020005374410 * player.getWeight());
	}
	
	/**
	 * Represents a character's movement status, whether they are standing still,
	 * moving, frozen or stunned.
	 * 
	 * @author relex lawl
	 */
	public enum MovementFlag {
		/**
		 * The character is standing still.
		 */
		NONE,
		
		/**
		 * The character is walking around.
		 */
		WALKING,
		
		/**
		 * The character is running.
		 */
		RUNNING,
		
		/**
		 * The character has been frozen by a
		 * magical effect.
		 */
		FROZEN,
		
		/**
		 * The character has been stunt.
		 */
		STUNNED,
		
		/**
		 * The character cannot move.
		 */
		CANNOT_MOVE;
	}
	
	/**
	 * Represents a 'point' to walk to.
	 * 
	 * @author relex lawl
	 */
	private static final class WalkPoint {
		
		/**
		 * The WalkPoint constructor.
		 * @param position		The destination of the walk point.
		 * @param direction		The direction the character is moving in.
		 */
		private WalkPoint(Position position, int direction) {
			this.position = position;
			this.direction = direction;
		}
		
		/**
		 * The destination of the walk point.
		 */
		private Position position;
		
		/**
		 * The direction the character is moving in.
		 */
		private int direction;
	}

}
