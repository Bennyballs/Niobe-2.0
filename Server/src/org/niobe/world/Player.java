package org.niobe.world;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.Appearance;
import org.niobe.model.ChatMessage;
import org.niobe.model.GameCharacter;
import org.niobe.model.Item;
import org.niobe.model.PlayerRelations;
import org.niobe.model.PlayerRights;
import org.niobe.model.SkillManager;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.container.impl.Bank;
import org.niobe.model.container.impl.Equipment;
import org.niobe.model.container.impl.Inventory;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.model.weapon.Weapon;
import org.niobe.model.weapon.Weapon.AttackStyle;
import org.niobe.model.weapon.WeaponLoader;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketSender;
import org.niobe.net.security.credential.PlayerCredential;
import org.niobe.net.session.Session;
import org.niobe.task.impl.PlayerDeathTask;
import org.niobe.util.MathUtil;
import org.niobe.world.util.GameConstants;

/**
 * An implementation of {@link org.niobe.model.GameCharacter} that represents a
 * playable character.
 * 
 * @author relex lawl
 */
public final class Player extends GameCharacter {

	/**
	 * The Player constructor.
	 * 
	 * @param credentials
	 *            The credentials used for this player instance.
	 * @param channel
	 *            The channel used to connect to the game server.
	 */
	public Player(PlayerCredential credentials, Channel channel) {
		super(GameConstants.DEFAULT_POSITION.copy().add(MathUtil.random(5),
				MathUtil.random(5)));
		this.credentials = credentials;
		this.channel = channel;
	}

	/**
	 * The credentials used in this session.
	 */
	private final PlayerCredential credentials;

	/**
	 * The netty channel used to connect to the game server.
	 */
	private final Channel channel;

	/**
	 * The current {@link org.niobe.net.session.Session} instance.
	 */
	private Session session;

	/**
	 * The appearance other players will see this associated player as.
	 */
	private final Appearance appearance = new Appearance(this);

	/**
	 * The game packet sender used to send packets from this player session to
	 * the client.
	 */
	private final GamePacketSender packetSender = new GamePacketSender(this);

	/**
	 * An implementation of {@link orb.niobe.model.container.ItemContainer}
	 * which stores all of the players equipment items.
	 */
	private final Equipment equipment = new Equipment(this);

	/**
	 * An implementation of {@link orb.niobe.model.container.ItemContainer}
	 * which stores all of the players inventory items.
	 */
	private final Inventory inventory = new Inventory(this);

	/**
	 * An implementation of {@link orb.niobe.model.container.ItemContainer}
	 * which stores all of the players bank items.
	 */
	private final Bank bank = new Bank(this);

	/**
	 * The player-associated skills set.
	 */
	private final SkillManager skillManager = new SkillManager(this);

	/**
	 * The associated {@link org.niobe.model.PlayerRelations}.
	 */
	private final PlayerRelations relations = new PlayerRelations(this);

	/**
	 * The associated {@link org.niobe.model.ChatMessage}.
	 */
	private final ChatMessage chatMessage = new ChatMessage();

	/**
	 * The player's privilege rights.
	 */
	private PlayerRights rights = PlayerRights.PLAYER;

	/**
	 * Writes a packet on a player's connection channel.
	 * 
	 * @param packet
	 *            The packet to write on the channel.
	 * @return The Player instance.
	 */
	public ChannelFuture write(GamePacket packet) {
		if (channel.isConnected()) {
			return channel.write(packet);
		}
		return null;
	}

	/**
	 * Gets the player's credentials.
	 * 
	 * @return The credentials, such as username, password, etc.
	 */
	public PlayerCredential getCredentials() {
		return credentials;
	}

	/**
	 * Gets the channel used to connect to the game server.
	 * 
	 * @return The netty channel used to connect.
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * Gets the player's current {@link org.niobe.net.session.Session}.
	 * 
	 * @return The current session.
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * Sets the player's current session.
	 * 
	 * @param session
	 *            The new player session.
	 */
	public Player setSession(Session session) {
		this.session = session;
		return this;
	}

	/**
	 * Gets the appearance used for the associated player.
	 * 
	 * @return The appearance instance.
	 */
	public Appearance getAppearance() {
		return appearance;
	}

	/**
	 * The game packet sender used to send packets from the player session to
	 * the client.
	 * 
	 * @return The game packet sender.
	 */
	public GamePacketSender getPacketSender() {
		return packetSender;
	}

	/**
	 * Gets the player-associated inventory item container.
	 * 
	 * @return The player's inventory.
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Gets the player-associated equipment item container.
	 * 
	 * @return The player's equipment.
	 */
	public Equipment getEquipment() {
		return equipment;
	}

	/**
	 * Gets the player-associated bank item container.
	 * 
	 * @return The player's bank.
	 */
	public Bank getBank() {
		return bank;
	}

	/**
	 * Gets the player-associated {@link org.niobe.model.PlayerRelations}.
	 * 
	 * @return The player's relations.
	 */
	public PlayerRelations getRelations() {
		return relations;
	}

	/**
	 * Gets the player-associated {@link org.niobe.model.ChatMessage}.
	 * 
	 * @return The associated {@link org.niobe.model.ChatMessage}.
	 */
	public ChatMessage getChatMessage() {
		return chatMessage;
	}

	/**
	 * Gets the player's privilege rights.
	 * 
	 * @return rights.
	 */
	public PlayerRights getRights() {
		return rights;
	}

	/**
	 * Sets the player's privilege rights.
	 * 
	 * @param rights
	 *            The player's new privilege rights.
	 * @return The Player instance.
	 */
	public Player setRights(PlayerRights rights) {
		this.rights = rights;
		return this;
	}
	
	/**
	 * Gets the corresponding {@link org.niobe.model.weapon.Weapon}
	 * for the {@link #getEquipment().getItems()[Equipment.WEAPON_SLOT]}.
	 * @return	The weapon for this player.
	 */
	public Weapon getWeapon() {
		return WeaponLoader.forId(getEquipment().getItems()[Equipment.WEAPON_SLOT].getId());
	}
	
	/**
	 * Gets the total weight this player has.
	 * @return	The amount of weight this player has.
	 */
	public double getWeight() {
		double weight = 0;
		for (Item item : getEquipment().getItems()) {
			if (item != null) {
				weight += ItemDefinition.forId(item.getId()).getWeight();
			}
		}
		return weight;
	}

	@Override
	public int getFreeIndex() {
		for (int i = 1; i < GameServer.getWorld().getPlayers().length; i++) {
			if (GameServer.getWorld().getPlayers()[i] == null) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean isPlayer() {
		return true;
	}

	@Override
	public void appendDeath() {
		GameServer.getTaskManager().submit(
				new PlayerDeathTask(this, getFields().getCombatAttributes()
						.getAttackedBy() == null));
	}

	@Override
	public int getConstitution() {
		return getSkillManager().getCurrentLevel(Skill.CONSTITUTION);
	}

	@Override
	public GameCharacter setConstitution(int constitution) {
		getSkillManager().setCurrentLevel(Skill.CONSTITUTION, constitution);
		getPacketSender().sendSkill(Skill.CONSTITUTION);
		return this;
	}
	
	@Override
	public SkillManager getSkillManager() {
		return skillManager;
	}

	@Override
	public int getAttackDelay() {
		Weapon weapon = WeaponLoader
				.forId(getEquipment().getItems()[Equipment.WEAPON_SLOT].getId());
		int delay = weapon.getCombatDefinition().getSpeed().getDelay();
		if (weapon.getCombatDefinition().getAttackStyle()[getFields()
				.getCombatAttributes().getAttackStyle()] == AttackStyle.RAPID_RANGED) {
			delay -= 1;
		}
		return delay;
	}

	@Override
	public Animation getAttackAnimation() {
		Weapon weapon = WeaponLoader
				.forId(getEquipment().getItems()[Equipment.WEAPON_SLOT].getId());
		return weapon.getCombatDefinition().getAnimations()[getFields()
				.getCombatAttributes().getAttackStyle()];
	}

	@Override
	public Animation getBlockAnimation() {
		Weapon weapon = WeaponLoader
				.forId(getEquipment().getItems()[Equipment.WEAPON_SLOT].getId());
		return weapon.getCombatDefinition().getBlockAnimation();
	}
	
	@Override
	public void sendMessage(String message) {
		getPacketSender().sendMessage(message);
	}

	@Override
	public int getSize() {
		return 1;
	}
	
	@Override
	public boolean equals(Object object) {
		Player other = (Player) object;
		return other.getCredentials().getUsername()
				.equalsIgnoreCase(getCredentials().getUsername());
	}
}
