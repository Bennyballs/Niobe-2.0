package org.niobe.world;

import org.niobe.GamePulse;
import org.niobe.GameServer;
import org.niobe.model.Entity;
import org.niobe.model.definition.*;
import org.niobe.model.weapon.WeaponLoader;
import org.niobe.task.Task;
import org.niobe.task.impl.PlayerSkillsTask;
import org.niobe.task.impl.PlayerSpecialAmountTask;
import org.niobe.world.content.MobDropTable;
import org.niobe.world.content.MobSpawns;
import org.niobe.world.content.PlayerPunishment;
import org.niobe.world.content.ShopManager;
import org.niobe.world.content.clan.ClanChatManager;
import org.niobe.world.content.dialogue.DialogueManager;
import org.niobe.world.region.RegionManager;
import org.niobe.world.util.GameConstants;

/**
 * Represents the world of niobe including all
 * {@link org.niobe.model.Entity}s in it.
 *
 * @author relex lawl
 */
public final class World {
	
	/**
	 * This array contains all the controllable 
	 * entities in the world.
	 */
	private final Player[] players = new Player[GameConstants.MAX_PLAYERS];
	
	/**
	 * This array contains all the mobs in
	 * the world.
	 */
	private final Mob[] mobs = new Mob[GameConstants.MAX_MOBS];
	
	/**
	 * This array contains all the ground items
	 * in the world.
	 */
	private final GroundItem[] groundItems = new GroundItem[GameConstants.MAX_GROUND_ITEMS];
	
	/**
	 * This array contains all the game objects in
	 * the world.
	 */
	private final GameObject[] gameObjects = new GameObject[GameConstants.MAX_GAME_OBJECTS];
	
	/**
	 * This array contains all the projectiles in
	 * the world.
	 */
	private final Projectile[] projectiles = new Projectile[GameConstants.MAX_PROJECTILES];
	
	/**
	 * Initializes the World loader.
	 * @throws Exception 
	 */
	public void init() throws Exception {
		/*
		 * Loading entity definitions.
		 */
		MobDefinition.init();
		ItemDefinition.init();
		GameObjectDefinition.init();
		WeaponLoader.init();
		/*
		 * Loading other configurations.
		 */
		ShopManager.init();
		MobDropTable.init();
		MobSpawns.init();
		ClanChatManager.init();
		PlayerPunishment.init();
		DialogueManager.init();
		/*
		 * Loading game pulses.
		 */
		new GamePulse();
	}
	
	/**
	 * The pulse in the world - handled every 
	 * {@link org.niobe.world.util.GameConstants.TICK_DELAY} milliseconds.
	 */
	public void pulse() {
		GameServer.getTaskManager().pulse();
		
		for (GroundItem groundItem : groundItems) {
			if (groundItem == null) {
				continue;
			}
			groundItem.addTick();
			if (groundItem.getOwner() != null) {
				if (groundItem.getTicks() == (GameConstants.GROUND_ITEM_DELAY * 10)) {
					for (Player player : groundItem.getRegion().getPlayers()) {
						if (player != groundItem.getOwner()) {
							player.getPacketSender().sendGroundItem(groundItem);
						}
					}
					groundItems[groundItem.getIndex()].setOwner(null);
				}
			} else {
				if (groundItem.getTicks() == (GameConstants.GROUND_ITEM_DELAY * 20)) {
					for (Player player : groundItem.getRegion().getPlayers()) {
						player.getPacketSender().sendGroundItemRemoval(groundItem);
					}
					groundItems[groundItem.getIndex()] = null;
				}
			}
		}
	}
	
	/**
	 * Registers an entity into the world, 
	 * submitting tasks needed.
	 * @param entity	The entity being registered to the world.
	 */
	public void register(Entity entity) {
		synchronized (this) {
			if (!entity.isPlayer()) {
				RegionManager.register(entity);
			}
			if (entity.isPlayer()) {
				Player player = (Player) entity;
				
				player.getPacketSender().sendLogin();
				
				GameServer.getTaskManager().submit(new PlayerSkillsTask(player));
				GameServer.getTaskManager().submit(new PlayerSpecialAmountTask(player));
				
				players[player.getIndex()] = player;
			} else if (entity.isMob()) {
				Mob mob = (Mob) entity;
				mobs[mob.getIndex()] = mob;
			} else if (entity.isGroundItem()) {
				GroundItem groundItem = (GroundItem) entity;
				if (GroundItem.find(groundItem.getItem().getId(), groundItem.getPosition()) != null &&
						ItemDefinition.forId(groundItem.getItem().getId()).isStackable()) {
					if (groundItem.getOwner() == null) {
						for (Player player : groundItem.getRegion().getPlayers()) {
							GroundItem oldGroundItem = GroundItem.find(groundItem.getItem().getId(), groundItem.getPosition());
							int amount = oldGroundItem.getItem().getAmount() + groundItem.getItem().getAmount();
							oldGroundItem.getItem().setAmount(amount);
							player.getPacketSender().sendGroundItemAmount(groundItem.getPosition(), groundItem.getItem(), amount);
						}
					} else {
						GroundItem oldGroundItem = GroundItem.find(groundItem.getItem().getId(), groundItem.getPosition(), groundItem.getOwner());
						if (oldGroundItem != null) {
							int amount = oldGroundItem.getItem().getAmount() + groundItem.getItem().getAmount();
							oldGroundItem.getItem().setAmount(amount);
							groundItem.getOwner().getPacketSender().sendGroundItemAmount(groundItem.getPosition(), groundItem.getItem(), amount);
						} else {
							groundItem.getOwner().getPacketSender().sendGroundItem(groundItem);
						}
					}
				} else {
					if (groundItem.getOwner() == null) {
						for (Player player : groundItem.getRegion().getPlayers()) {
							player.getPacketSender().sendGroundItem(groundItem);
						}
					} else {
						groundItem.getOwner().getPacketSender().sendGroundItem(groundItem);
					}
				}
				groundItems[groundItem.getIndex()] = groundItem;
			} else if (entity.isGameObject()) {
				GameObject gameObject = (GameObject) entity;
								
				for (Player player : gameObject.getRegion().getPlayers()) {
					player.getPacketSender().sendGameObject(gameObject);
				}
				gameObjects[gameObject.getIndex()] = gameObject;
			} else if (entity.isProjectile()) {
				final Projectile projectile = (Projectile) entity;
				for (Player player : projectile.getRegion().getPlayers()) {
					player.getPacketSender().sendProjectile(projectile);
				}
				projectiles[projectile.getIndex()] = projectile;
				GameServer.getTaskManager().submit(new Task(5) {
					@Override
					public void execute() {
						unregister(projectile);
						stop();
					}	
				});
			}
		}
	}
	
	/**
	 * Unregisters an entity from the world.
	 * @param entity	The entity to be removed.
	 */
	public void unregister(Entity entity) {
		synchronized (this) {
			if (entity.isPlayer()) {
				Player player = (Player) entity;
				if ((System.currentTimeMillis() - player.getFields().getCombatAttributes().getDamageDelay()) < 5000) {
					player.getPacketSender().sendMessage("You cannot do this while being attacked!");
				} else {
					RegionManager.unregister(entity);
					player.getPacketSender().sendLogout();
					players[player.getIndex()] = null;
				}
			} else if (entity.isMob()) {
				Mob mob = (Mob) entity;
				RegionManager.unregister(entity);
				mobs[mob.getIndex()] = null;
			} else if (entity.isGroundItem()) {
				GroundItem groundItem = (GroundItem) entity;
				if (groundItems[groundItem.getIndex()] != null) {
					RegionManager.unregister(entity);
					if (groundItem.getOwner() == null) {
						for (Player player : groundItem.getRegion().getPlayers()) {
							player.getPacketSender().sendGroundItemRemoval(groundItem);
						}
					} else {
						Player player = getPlayerForName(groundItem.getOwnerName());
						player.getPacketSender().sendGroundItemRemoval(groundItem);
					}
					groundItems[groundItem.getIndex()] = null;
				}
			} else if (entity.isGameObject()) {
				GameObject gameObject = (GameObject) entity;
				for (Player player : gameObject.getRegion().getPlayers()) {
					player.getPacketSender().sendGameObjectRemoval(gameObject);
				}
				RegionManager.unregister(entity);
				gameObjects[gameObject.getIndex()] = null;
			} else if (entity.isProjectile()) {
				Projectile projectile = (Projectile) entity;
				RegionManager.unregister(entity);
				projectiles[projectile.getIndex()] = null;
			}
		}
	}
	
	/**
	 * Gets the player according to said name.
	 * @param name	The name of the player to search for.
	 * @return		The player who has the same name as said param.
	 */
	public Player getPlayerForName(String name) {
		for (Player player : players) {
			if (player != null && player.getCredentials().getUsername().equalsIgnoreCase(name)) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Checks if player with said username 
	 * is online in the world.
	 * @param username	The username to get player for.
	 * @return			If {@code true} the player is online.
	 */
	public boolean isPlayerOnline(String username) {
		for (Player player : getPlayers()) {
			if (player != null && player.getCredentials().getUsername().equalsIgnoreCase(username)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the total amount of players online
	 * in the world.
	 * @return	The total amount of players.
	 */
	public int getPlayersOnline() {
		int count = 0;
		for (Player player : players) {
			if (player != null) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Gets the total amount of registered
	 * {@link Mob}s in the world.
	 * @return	All the registered {@link Mob}s.
	 */
	public int getMobsRegistered() {
		int count = 0;
		for (Mob mob : mobs) {
			if (mob != null) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Gets the players in the world.
	 * @return	The players array.
	 */
	public Player[] getPlayers() {
		return players;
	}
	
	/**
	 * Gets the mobs in the world.
	 * @return	The mobs array.
	 */
	public Mob[] getMobs() {
		return mobs;
	}
	
	/**
	 * Gets the ground items in the world.
	 * @return	The ground items array.
	 */
	public GroundItem[] getGroundItems() {
		return groundItems;
	}
	
	/**
	 * Gets the game objects in the world.
	 * @return	The game objects array.
	 */
	public GameObject[] getGameObjects() {
		return gameObjects;
	}
	
	/**
	 * Gets the projectiles in the world.
	 * @return	The projectiles array.
	 */
	public Projectile[] getProjectiles() {
		return projectiles;
	}
}
