package org.niobe.model;

import org.niobe.GameServer;
import org.niobe.model.Damage.Hit;
import org.niobe.model.UpdateFlag.Flag;
import org.niobe.model.action.GameAction;
import org.niobe.world.util.GameCharacterFields;

/**
 * An implementation of {@link Entity}, which represents
 * both {@link org.niobe.world.Player}s and {@link org.niobe.world.Mob}s in the world.
 *
 * @author relex lawl
 */
public abstract class GameCharacter extends Entity {

	/**
	 * The GameCharacter constructor.
	 * @param position	The position the game character is located in.
	 */
	public GameCharacter(Position position) {
		super(position);
		lastKnownRegion = position;
	}
	
	/**
	 * The game character's last region position.
	 */
	private Position lastKnownRegion;
	
	/**
	 * The game character's updating flags used
	 * in the updating procedure.
	 */
	private final UpdateFlag updateFlag = new UpdateFlag();
	
	/**
	 * The movement queue for this entity, used for walking/moving.
	 */
	private final MovementQueue movementQueue = new MovementQueue(this);
	
	/**
	 * The game character fields in which to store
	 * miscellaneous variables.
	 */
	private final GameCharacterFields fields = new GameCharacterFields();
	
	/**
	 * The damage that has been dealt to this character.
	 */
	private Damage damage;
	
	/**
	 * The animation this entity will perform upon next tick.
	 */
	private Animation animation;
	
	/**
	 * The graphic this entity will perform upon next tick.
	 */
	private Graphic graphic;
	
	/**
	 * The current entity this character is interacting with.
	 */
	private Entity interactingEntity;
	
	/**
	 * The entity that this entity is following.
	 */
	private GameCharacter followEntity;
	
	/**
	 * The position this entity will face upon next tick.
	 */
	private Position positionToFace;
	
	/**
	 * The character's current {@link org.niobe.model.action.GameAction}
	 * being performed.
	 */
	private GameAction<?> action;
	
	/**
	 * Gets the character's current {@link org.niobe.model.action.GameAction}.
	 * @return	The game action being performed by the character.
	 */
	public GameAction<?> getAction() {
		return action;
	}
	
	/**
	 * Sets the {@link org.niobe.world.util.GameCharacter#getFields()#getAction()}
	 * and checks if the current action is already running to stop unnecessary instances.
 	 * @param action	The action the character is performing.
 	 * @return			The GameCharacter instance.
	 */
	public GameCharacter setAction(GameAction<?> action) {
		if (this.action != null) {
			this.action.stop();
			if (this.action.equals(action)) {
				return this;
			}
		}
		this.action = action;
		GameServer.getTaskManager().submit(action);
		return this;
	}
	
	/**
	 * Gets the last known region the character
	 * was located in.
	 * @return	The last known region instance.
	 */
	public Position getLastKnownRegion() {
		return lastKnownRegion;
	}
	
	/**
	 * Sets the character's last known region, used
	 * when the character moves to a different region.
	 * @param lastKnownRegion	The new lastKnownRegion assignment.
	 * @return					The GameCharacter instance.
	 */
	public GameCharacter setLastKnownRegion(Position lastKnownRegion) {
		this.lastKnownRegion = lastKnownRegion;
		return this;
	}
	
	/**
	 * Gets the associated movement queue.
	 * @return the movement queue
	 */
	public MovementQueue getMovementQueue() {
		return movementQueue;
	}
	
	/**
	 * Gets the game character's updating 
	 * flags.
	 * @return	The flags used in the updating procedure.
	 */
	public UpdateFlag getUpdateFlag() {
		return updateFlag;
	}
	
	/**
	 * Gets the character's miscellaneous fields 
	 * instance.
	 * @return	The game character fields instance.
	 */
	public GameCharacterFields getFields() {
		return fields;
	}
	
	/**
	 * Gets the damage dealt to the character.
	 * @return	The damage dealt.
	 */
	public Damage getDamage() {
		return damage;
	}

	/**
	 * Sets this entity's current damage.
	 * @param damage	The damage entity will receive.
	 * @return			The GameCharacter instance.
	 */
	public GameCharacter setDamage(Damage damage) {
		if (getConstitution() <= 0)
			return this;
		this.damage = damage;
		if (damage.getHits().length >= 1) {
			Hit hit = damage.getHits()[0];
			int outcome = getConstitution() - hit.getDamage();
			if (hit.getAbsorption() > 0)
				outcome += hit.getAbsorption();
			if (outcome < 0) {
				hit.setDamage(getConstitution());
				outcome = 0;
			}
			setConstitution(outcome);
			getUpdateFlag().flag(Flag.SINGLE_HIT);
		}
		if (damage.getHits().length >= 2) {
			Hit hit = damage.getHits()[1];
			int outcome = getConstitution() - hit.getDamage();
			if (hit.getAbsorption() > 0)
				outcome += hit.getAbsorption();
			if (outcome < 0) {
				hit.setDamage(getConstitution());
				outcome = 0;
			}
			setConstitution(outcome);
			getUpdateFlag().flag(Flag.DOUBLE_HIT);
		}
		if (getConstitution() <= 0)
			appendDeath();
		return this;
	}
	
	/**
	 * Gets this entity's current graphic.
	 * @return	The entity's graphic.
	 */
	public Graphic getGraphic() {
		return graphic;
	}
	
	/**
	 * Sets this entity's graphic.
	 * @param graphic	The graphic for this entity to perform.
	 * @return			The GameCharacter instance.
	 */
	public GameCharacter setGraphic(Graphic graphic) {
		this.graphic = graphic;
		return this;
	}
	
	/**
	 * Gets this entity's current animation.
	 * @return	The entity's animation.
	 */
	public Animation getAnimation() {
		return animation;
	}
	
	/**
	 * Sets this entity's animation.
	 * @param animation	The animation for this entity to perform.
	 * @return			The GameCharacter instance.
	 */
	public GameCharacter setAnimation(Animation animation) {
		this.animation = animation;
		return this;
	}
	
	/**
	 * Gets the position this entity will face upon next tick.
	 * @return	The position that the entity will face.
	 */
	public Position getPositionToFace() {
		return positionToFace;
	}
	
	/**
	 * Sets the position for this entity to face upon next tick.
	 * @param positionToFace	The position for entity to face.
	 * @return					The GameCharacter instance.
	 */
	public GameCharacter setPositionToFace(Position positionToFace) {
		this.positionToFace = positionToFace;
		getUpdateFlag().flag(Flag.FACE_POSITION);
		return this;
	}
	
	/**
	 * Sets the entity's teleport target attribute.
	 * @param teleportTarget	The position player's teleport attribute will be set to.
	 * @return					The GameCharacter instance.
	 */
	public GameCharacter moveTo(Position teleportTarget) {
		getFields().setTeleportPosition(teleportTarget);
		return this;
	}
	
	/**
	 * Gets the entity's following entity (the entity which they are following)
	 * @return followEntity.
	 */
	public GameCharacter getFollowEntity() {
		return followEntity;
	}
	
	/**
	 * Sets the entity's following entity.
	 * @param followEntity	The entity to which they will now follow.
	 * @return
	 */
	public GameCharacter setFollowEntity(GameCharacter followEntity) {
		this.followEntity = followEntity;
		return this;
	}
	
	/**
	 * Sets the entity interaction and flags
	 * the corresponding {@link org.niobe.model.UpdateFlag.Flag}.
	 * @param entity
	 * @return
	 */
	public GameCharacter setEntityInteraction(Entity entity) {
		this.interactingEntity = entity;
		getUpdateFlag().flag(Flag.ENTITY_INTERACTION);
		return this;
	}
	
	/**
	 * Gets the character's interacting entity.
	 * @return	The entity this character is interacting with.
	 */
	public Entity getInteractingEntity() {
		return interactingEntity;
	}
	
	/**
	 * Appends a character's death.
	 */
	public abstract void appendDeath();
	
	/**
	 * Gets the character's current constitution.
	 */
	public abstract int getConstitution();
	
	/**
	 * Gets the character's attack delay.
	 */
	public abstract int getAttackDelay();
	
	/**
	 * Gets the character's attack animation.
	 */
	public abstract Animation getAttackAnimation();
	
	/**
	 * Gets the character's block animation.
	 */
	public abstract Animation getBlockAnimation();
	
	/**
	 * Sets the character's current constitution.
	 */
	public abstract GameCharacter setConstitution(int constitution);
	
	/**
	 * Gets the {@link SkillManager} for said {@link GameCharacter}.
	 */
	public abstract SkillManager getSkillManager();
	
	/**
	 * Sends the message, used to stop down-casting just for
	 * this simple action.
	 */
	public abstract void sendMessage(String message);
	
	@Override
	public void performAnimation(Animation animation) {
		setAnimation(animation);
		getUpdateFlag().flag(Flag.ANIMATION);
	}

	@Override
	public void performGraphic(Graphic graphic) {
		setGraphic(graphic);
		getUpdateFlag().flag(Flag.GRAPHIC);
	}
}
