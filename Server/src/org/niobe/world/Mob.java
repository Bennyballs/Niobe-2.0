package org.niobe.world;

import java.util.LinkedList;
import java.util.List;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.AttackType;
import org.niobe.model.Direction;
import org.niobe.model.GameCharacter;
import org.niobe.model.Item;
import org.niobe.model.Position;
import org.niobe.model.SkillManager;
import org.niobe.model.SkillManager.MobSkillManager;
import org.niobe.model.UpdateFlag.Flag;
import org.niobe.model.definition.MobDefinition;
import org.niobe.task.impl.MobDeathTask;
import org.niobe.world.content.MobDropTable;

/**
 * An implementation of {@link org.niobe.model.GameCharacter}
 * that represents a non-playable character also known as mob.
 *
 * @author relex lawl
 */
public class Mob extends GameCharacter {
	
	/**
	 * The Mob constructor.
	 * @param id			The id of the mob to register.
	 * @param position		The position the mob will have.
	 * @param direction		The mob's facing direction.
	 * @param respawnTimer	The timer for the mob to respawn.
	 */
	public Mob(int id, Position position, Direction direction, int respawnTimer) {
		super(position);
		this.id = id;
		this.startPosition = position;
		this.direction = direction;
		this.respawnTimer = respawnTimer;
		this.definition = MobDefinition.forId(id);
		this.constitution = definition.getLifePoints();
		
		MobDropTable.attachDrops(this);
	}
	
	/**
	 * The Mob constructor.
	 * @param id			The id of the mob to register.
	 * @param position		The position the mob will have.
	 * @param direction		The mob's facing direction.
	 */
	public Mob(int id, Position position, Direction direction) {
		this(id, position, direction, -1);
	}
	
	/**
	 * The Mob constructor.
	 * @param id		The id of the Mob to register.
	 * @param position	The position the Mob will have.
	 */
	public Mob(int id, Position position) {
		this(id, position, Direction.NORTH);
	}
	
	/**
	 * The mob's id.
	 */
	private final int id;
	
	/**
	 * The mob's definition, which contains the attributes.
	 */
	private MobDefinition definition;
	
	/**
	 * The mob's current constitution.
	 */
	private int constitution = 100;
	
	/**
	 * The mob's attack type.
	 */
	private AttackType attackType = AttackType.MELEE;
	
	/**
	 * The id of the mob this mob will 'transform' into.
	 */
	private int transformationId = -1;
	
	/**
	 * The max amount of tiles the mob can wander off in.
	 */
	private int walkDistance = 0;
	
	/**
	 * Checks if the mob will go through the updating process
	 * and players will be able to see it.
	 */
	private boolean visible = true;
	
	/**
	 * The mob's default rotation aka their face direction.
	 */
	private Direction direction;
	
	/**
	 * The mob's starting location, to check their in distance
	 * according to their walkDistance.
	 */
	private final Position startPosition;
	
	/**
	 * The mob's drop table, contains items that it will drop
	 * upon death.
	 */
	private List<Item> drops = new LinkedList<Item>();
	
	/**
	 * The skill manager instance for this mob.
	 */
	private final MobSkillManager skillManager = new MobSkillManager(this);
	
	/**
	 * This timer is used to check the timer 
	 * for a mob to reappear.
	 */
	private int respawnTimer = 5;
	
	/**
	 * Gets the mob's id.
	 * @return	The mob id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the mob's defined definition.
	 * 
	 * @return the definition.
	 */
	public MobDefinition getDefinition() {
		return definition;
	}

	/**
	 * Gets the mob's transformation id.
	 * @return	The id of the mob this mob will 'transform' into.
	 */
	public int getTransformationId() {
		return transformationId;
	}
	
	/**
	 * Sets this mob's transformation id.
	 * @param transformationId	The id of the mob this mob will 'transform' into.
	 * @return					The Mob instance.
	 */
	public Mob setTransformationId(int transformationId) {
		this.transformationId = transformationId;
		getUpdateFlag().flag(Flag.TRANSFORM);
		return this;
	}
	
	/**
	 * Sets the mob's attack type.
	 * @param attackType	The attack type mob is performing.
	 * @return				The Mob instance.
	 */
	public Mob setAttackType(AttackType attackType) {
		this.attackType = attackType;
		return this;
	}
	
	/**
	 * Gets the mob's current attack type.
	 * @return	The attack type being performed by the mob.
	 */
	public AttackType getAttackType() {
		return attackType;
	}
	
	/**
	 * Gets the mob's maximum walk distance
	 * they may wander off in.
	 * @return	The maximum amount of tiles that can be traversed.
	 */
	public int getWalkDistance() {
		return walkDistance;
	}
	
	/**
	 * Sets the mob's maximum walk distance
	 * @return	The Mob instance.
	 */
	public Mob setWalkDistance(int maximumWalkDistance) {
		this.walkDistance = maximumWalkDistance;
		return this;
	}
	
	/**
	 * Gets the mob's facing direction.
	 * @return	direction.
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Sets the mob's direction.
	 * @param direction	The newly face direction.
	 */
	public Mob setDirection(Direction direction) {
		this.direction = direction;
		Position face = getPosition().copy();
		int x = 0, y = 0;
		if (direction == Direction.NORTH) 
			y = 1;
		else if (direction == Direction.EAST)
			x = 1;
		else if (direction == Direction.SOUTH)
			y = -1;
		else if (direction == Direction.WEST)
			x = -1;
		face = face.add(x, y);
		setPositionToFace(face);
		return this;
	}
	
	/**
	 * Gets the mob's starting position.
	 * @return	The position the mob was spawned in.
	 */
	public Position getStartPosition() {
		return startPosition;
	}
	
	/**
	 * Gets a collection of the mob's pre-configured drops.
	 * @return	The drop(s) mob will have.
	 */
	public List<Item> getDrops() {
		return drops;
	}
	
	/**
	 * Sets the mob's drops.
	 * @param drops		A Colleciton of the items the mob will drop upon death.
	 * @return			The Mob instance.
	 */
	public Mob setDrops(List<Item> drops) {
		this.drops = drops;
		return this;
	}
	
	/**
	 * Checks if the mob is 'visible', if <code>false</code> they will not
	 * go through players' mob updating and therefore will not be 'visible'.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets the mob's visibility.
	 * @param visible	If <code>false</code> player's will not be able to see the mob.
	 * @return			The Mob instance.
	 */
	public Mob setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}
	
	/**
	 * Gets the timer for the mob to be
	 * re-registered to the world.
	 * @return	The {@link #respawnTimer} value.
	 */
	public int getRespawnTimer() {
		return respawnTimer;
	}

	/**
	 * Sets the respawn timer for the mob.
	 * @param respawnTimer	The new {@link #respawnTimer} value.
	 */
	public Mob setRespawnTimer(int respawnTimer) {
		this.respawnTimer = respawnTimer;
		return this;
	}
	
	/**
	 * Handles all the content that will be 
	 * called upon in the {@link org.niobe.world.update.mobl.PreMobGameUpdate}.
	 */
	public void pulse() {
		if (getFields().getCombatAttributes().getAttackedBy() != null
				&& (System.currentTimeMillis() - getFields().getCombatAttributes().getDamageDelay() >= 5000)) {
			getFields().getCombatAttributes().setAttackedBy(null);
		}
		if (getFields().getCombatAttributes().getAttackDelay() > 0) {
			getFields().getCombatAttributes().setAttackDelay(getFields().getCombatAttributes().getAttackDelay() - 1);
		}
		if (getFields().getCombatAttributes().getHitDelay() > 0) {
			getFields().getCombatAttributes().setHitDelay(getFields().getCombatAttributes().getHitDelay() - 1);
		}
	}
	
	/**
	 * Represents a {@link Mob}'s combat-oriented
	 * weakness.
	 *
	 * @author relex lawl
	 */
	public enum Weakness {
		/**
		 * Has no weakness.
		 */
		NONE,
		
		/**
		 * Weak to {@link org.niobe.model.weapon.Weapon.CombatStyle#SLASH} attacks.
		 */
		SLASHING,
		
		/**
		 * Weak to {@link org.niobe.model.weapon.Weapon.CombatStyle#STAB} attacks.
		 */
		STABBING,
		
		/**
		 * Weak to {@link org.niobe.model.weapon.Weapon.CombatStyle#CRUSH} attacks.
		 */
		CRUSHING,
		
		/**
		 * Weak to fire-based attacks.
		 */
		FIRE,
		
		/**
		 * Weak to water-based attacks.
		 */
		WATER,
		
		/**
		 * Weak to earth-based attacks.
		 */
		EARTH,
		
		/**
		 * Weak to air-based attacks.
		 */
		AIR,
		
		/**
		 * Weak to ranged bow-based attacks.
		 */
		ARROW,

		/**
		 * Weak to ranged bolt-based attacks.
		 */
		BOLT,
		
		/**
		 * Weak to ranged thrown-based attacks.
		 */
		THROWN,
	}
	
	@Override
	public int getFreeIndex() {
		for (int i = 0; i < GameServer.getWorld().getMobs().length; i++) {
			if (GameServer.getWorld().getMobs()[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean isMob() {
		return true;
	}
	
	@Override
	public int getSize() {
		return definition.getSize();
	}
	
	@Override
	public void appendDeath() {
		GameServer.getTaskManager().submit(new MobDeathTask(this));
	}

	@Override
	public int getConstitution() {
		return constitution;
	}
	
	@Override
	public Mob setConstitution(int constitution) {
		this.constitution = constitution;
		return this;
	}

	@Override
	public int getAttackDelay() {
		return 2 + definition.getSize();
	}

	@Override
	public Animation getAttackAnimation() {
		return definition.getAttackAnimation();
	}

	@Override
	public Animation getBlockAnimation() {
		return new Animation(definition.getAttackAnimation().getId() + 2);
	}

	@Override
	public SkillManager getSkillManager() {
		return skillManager;
	}
	
	@Override
	public void sendMessage(String message) {
		
	}
	
	@Override
	public boolean equals(Object other) {
		return ((Mob)other).getIndex() == getIndex();
	}
}
