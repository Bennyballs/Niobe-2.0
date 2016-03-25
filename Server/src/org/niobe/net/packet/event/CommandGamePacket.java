package org.niobe.net.packet.event;

import java.io.IOException;

import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.Damage;
import org.niobe.model.Damage.CombatIcon;
import org.niobe.model.Damage.Hit;
import org.niobe.model.Damage.Hitmask;
import org.niobe.model.Direction;
import org.niobe.model.Graphic;
import org.niobe.model.Item;
import org.niobe.model.MovementQueue.MovementFlag;
import org.niobe.model.PlayerRights;
import org.niobe.model.Position;
import org.niobe.model.PrayerBook;
import org.niobe.model.SkillManager;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.UpdateFlag.Flag;
import org.niobe.model.container.impl.Equipment;
import org.niobe.model.definition.GameObjectDefinition;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.model.definition.MobDefinition;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.task.Task;
import org.niobe.util.FileUtil;
import org.niobe.world.GameObject;
import org.niobe.world.GroundItem;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.Projectile;
import org.niobe.world.content.ShopManager;
import org.niobe.world.content.clan.ClanChatManager;
import org.niobe.world.content.combat.CombatManager;
import org.niobe.world.content.combat.CombatPrayers;
import org.niobe.world.content.combat.formula.melee.MeleeHitFormula;
import org.niobe.world.util.GameConstants;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * that is called when a player writes in the command console.
 *
 * @author relex lawl
 */
public final class CommandGamePacket implements GamePacketEvent {

	@Override
	public void read(Player player, GamePacket packet) {
		String command = FileUtil.readString(packet.getBuffer());
		String[] parts = command.toLowerCase().split(" ");
		try {
			switch (player.getRights()) {
			case PLAYER:
				
				break;
			case DONATOR:
				
				break;
			case MODERATOR:
				
				break;
			case ADMINISTRATOR:
				administrator(player, parts, command);
				break;
			}
		} catch (Exception exception) {
			player.getPacketSender().sendConsoleMessage("Error while executing command!");
		}
	}

	/**
	 * The commands a player with administrator rights can execute.
	 * @param player	The player executing the command.
	 * @param command	The command being executed.
	 */
	private static void administrator(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("update")) {
			/*int time = Integer.parseInt(command[1]);
			for (Player players : GameServer.getWorld().getPlayers()) {
				if (player != null) {
					players.getPacketSender().sendSystemUpdate(time);
				}
			}
			TaskManager.getInstance().submit(new Task(time) {
				@Override
				public void execute() {
					GameServer.getWorld().setServerUpdate(true);
					for (Player player : GameServer.getWorld().getPlayers()) {
						if (player != null) {
							player.getPacketSender().sendLogout();
							GameServer.getWorld().deregister(player);
						}
					}
					stop();
				}
			});
			TaskManager.getInstance().submit(new Task(500) {
				@Override
				public void execute() {
					GameServer.getWorld().setServerUpdate(false);
					stop();
				}
			});*/
		} else if (command[0].equals("master")) {
			for (Skill skill : Skill.values()) {
				int level = SkillManager.getMaxAchievingLevel(skill);
				player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill, SkillManager.getExperienceForLevel(level == 120 ? 120 : 99));
			}
			player.getPacketSender().sendConsoleMessage("You are now a master of all skills.");
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		} else if (command[0].equals("reset")) {
			for (Skill skill : Skill.values()) {
				int level = skill.equals(Skill.CONSTITUTION) ? 100 : skill.equals(Skill.PRAYER) ? 10 : 1;
				player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill, SkillManager.getExperienceForLevel(skill == Skill.CONSTITUTION ? 10 : 1));
			}
			player.getPacketSender().sendConsoleMessage("Your skill levels have now been reset.");
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		} else if (command[0].equals("setlevel")) {
			int skillId = Integer.parseInt(command[1]);
			int level = Integer.parseInt(command[2]);
			Skill skill = Skill.forId(skillId);
			player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill, SkillManager.getExperienceForLevel(level));
			player.getPacketSender().sendConsoleMessage("You have set your " + skill.getName() + " level to " + level);
		} else if (command[0].equals("item")) {
			int id = Integer.parseInt(command[1]);
			int amount = (command.length == 2 ? 1 : Integer.valueOf(command[2]));
			Item item = new Item(id, amount);
			player.getInventory().add(item);
			player.getInventory().refreshItems();
		} else if (command[0].equals("npc")) {
			Mob mob = new Mob(Integer.parseInt(command[1]), player.getPosition().copy().add(0, 1), Direction.WEST);
			GameServer.getWorld().register(mob);
			player.getPacketSender().sendConsoleMessage("Spawning npc: " + mob.getId());
		} else if (command[0].equals("multi-npc")) {
			for (int x = 0; x < 3; x++) {
				for (int y = 0; y < 3; y++) {
					Mob mob = new Mob(Integer.parseInt(command[1]), player.getPosition().copy().add(x, y + 1), Direction.WEST);
					GameServer.getWorld().register(mob);
				}
			}
			player.getPacketSender().sendConsoleMessage("Multi-npc spawn...");
		} else if (command[0].equals("object")) {
			GameObject object = new GameObject(Integer.parseInt(command[1]), player.getPosition());
			GameServer.getWorld().register(object);
			player.getPacketSender().sendConsoleMessage("Spawning Object: " + object.getId());
		} else if (command[0].equals("interface")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendInterface(id);
		}
		if (command[0].equals("anim")) {
			int id = Integer.parseInt(command[1]);
			player.performAnimation(new Animation(id));
			player.getPacketSender().sendConsoleMessage("Sending animation: " + id);
		} else if (command[0].equals("gfx")) {
			int id = Integer.parseInt(command[1]);
			player.performGraphic(new Graphic(id));
			player.getPacketSender().sendConsoleMessage("Sending graphic: " + id);
		} else if (command[0].equals("mypos")) {
			player.getPacketSender().sendConsoleMessage(player.getPosition().toString());
		} else if (command[0].equals("tele")) {
			int x = Integer.valueOf(command[1]), y = Integer.valueOf(command[2]);
			int z = player.getPosition().getZ();
			if (command.length > 3)
				z = Integer.valueOf(command[3]);
			Position position = new Position(x, y, z);
			player.moveTo(position);
			player.getPacketSender().sendConsoleMessage("Teleporting to " + position.toString());
		} else if (command[0].equals("rights")) {
			Player target = GameServer.getWorld().getPlayerForName(command[1]);
			int rankId = Integer.parseInt(command[2]);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("Player must be online to give them rights!");
			} else {
				target.setRights(PlayerRights.forId(rankId));
			}
		} else if (command[0].equals("staffzone")) {
			if (command.length > 1 && command[1].equals("all")) {
				for (Player players : GameServer.getWorld().getPlayers()) {
					if (players != null) {
						if (players.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR) {
							player.moveTo(new Position(2846, 5147));
						}
					}
				}
			} else {
				player.moveTo(new Position(2846, 5147));
			}
		} else if (command[0].equals("empty")) {
			if (command.length > 1) {
				String container = command[1];
				if (container.equals("bank"))
					player.getBank().resetItems().refreshItems();
			} else
				player.getInventory().resetItems().refreshItems();
		} else if (command[0].equals("bank")) {
			player.getBank().open();
		} else if (command[0].equals("child")) {
			int maxId = Integer.parseInt(command[1]);
			player.getPacketSender().sendConsoleMessage("Looping through " + maxId + " interface childs...");
			for (int i = 0; i < maxId; i++) {
				player.getPacketSender().sendString(i, Integer.toString(i));
			}
			player.getPacketSender().sendConsoleMessage("Done looping through child ids.");
		} else if (command[0].equals("gear")) {
			int[][] data = {
					{Equipment.HEAD_SLOT, 10828},
					{Equipment.CAPE_SLOT, 6570},
					{Equipment.AMULET_SLOT, 6585},
					{Equipment.WEAPON_SLOT, 4151},
					{Equipment.BODY_SLOT, 11724},
					{Equipment.SHIELD_SLOT, 20072},
					{Equipment.LEG_SLOT, 11726},
					{Equipment.HANDS_SLOT, 7462},
					{Equipment.FEET_SLOT, 11732}
			};
			for (int i = 0; i < data.length; i++) {
				int slot = data[i][0], id = data[i][1];
				player.getEquipment().setItem(slot, new Item(id));
			}
			player.getEquipment().refreshItems();
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		} else if (command[0].equals("find-item")) {
			String name = wholeCommand.substring(10).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendConsoleMessage("Finding item id for item - " + name);
			int amountFound = 0;
			for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendConsoleMessage("Found item with name [" + ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					amountFound++;
				}
			}
			if (amountFound == 0) {
				player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
			} else {
				player.getPacketSender().sendConsoleMessage("Found " + amountFound + " item results.");
			}
		} else if (command[0].equals("find-mob")) {
			String name = wholeCommand.substring(9).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendConsoleMessage("Finding mob id for mob - " + name);
			int amountFound = 0;
			for (MobDefinition definition : MobDefinition.getDefinitions()) {
				if (definition != null && definition.getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendConsoleMessage("Found mob with name [" + definition.getName().toLowerCase() + "] - id: " + definition.getId());
					amountFound++;
				}
			}
			if (amountFound == 0) {
				player.getPacketSender().sendConsoleMessage("No mob with name [" + name + "] has been found!");
			} else {
				player.getPacketSender().sendConsoleMessage("Found " + amountFound + " mob results.");
			}
		} else if (command[0].equals("find-object")) {
			String name = wholeCommand.substring(12).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendConsoleMessage("Finding object id for object - " + name);
			int amountFound = 0;
			for (GameObjectDefinition definition : GameObjectDefinition.getDefinitions()) {
				if (definition != null && definition.getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendConsoleMessage("Found object with name [" + definition.getName().toLowerCase() + "] - id: " + definition.getId());
					amountFound++;
				}
			}
			if (amountFound == 0) {
				player.getPacketSender().sendConsoleMessage("No object with name [" + name + "] has been found!");
			} else {
				player.getPacketSender().sendConsoleMessage("Found " + amountFound + " object results.");
			}
		} else if (command[0].equals("test")) {
			int stage = Integer.valueOf(command[1]);
			if (stage == 0)
				ClanChatManager.create(player);
			else if (stage == 1)
				player.getFields().getClanChat().setCoinshare(true);
			else if (stage == 2)
				CombatPrayers.switchPrayerbook(player, PrayerBook.forId(Integer.valueOf(command[2])));
			else if (stage == 3) {
				player.getPacketSender().sendTabInterface(GameConstants.OPTIONS_TAB, Integer.valueOf(command[2]));
				player.getPacketSender().sendTab(GameConstants.OPTIONS_TAB);
			} else if (stage == 4) {
				GameServer.getWorld().register(new GroundItem(new Item(4151), player.getPosition().copy()));
				//System.out.println(MeleeAccuracyFormula.calculate(player, GameServer.getWorld().getNpcs()[0]));
			} else if(stage == 5) {
				new Mob(Integer.valueOf(command[2]), player.getPosition().copy().add(0, 1), Direction.EAST);
			} else if (stage == 6) {
				GameServer.getTaskManager().submit(new Task(5) {
					int animationId = 2000;
					@Override
					public void execute() {
						if (player == null) {
							stop();
							return;
						}
						player.performAnimation(new Animation(animationId++));
						player.getPacketSender().sendMessage("Now performing animation: " + animationId);
					}
	
				});
			} else if (stage == 7) {
				player.getPacketSender().sendObjectTransformation(new GameObject(75, player.getPosition()));
			} else if (stage == 8) {
				Player other = GameServer.getWorld().getPlayerForName("relex");
				MeleeHitFormula.getRandomHit(player, other);
			} else if (stage == 9) {
				//Damage damage = new Damage(new Hit(100, CombatIcon.MELEE, Hitmask.NORMAL), new Hit(50, CombatIcon.MELEE, Hitmask.NORMAL));
				Damage damage = new Damage(new Hit((player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) - 1), CombatIcon.MELEE, Hitmask.NORMAL));
				player.setDamage(damage);
			} else if (stage == 10) {
				player.getFields().getCombatAttributes().setSpecialAttackAmount(100000);
			} else if (stage == 11) {
				int id = Integer.valueOf(command[2]);
				player.getFields().setMobTransformationId(id);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			} else if (stage == 12) {
				int id = Integer.valueOf(command[2]);
				System.out.println(ItemDefinition.forId(id).toString());
			} else if (stage == 13) {
				try {
					GameObjectDefinition.write("./data/object_definitions.dat");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (stage == 14) {
				int id = Integer.valueOf(command[2]);
				Mob target = GameServer.getWorld().getMobs()[0];
				Projectile projectile = new Projectile(player.getPosition(), target.getPosition(), new Graphic(id), target);
				GameServer.getWorld().register(projectile);
			} else if (stage == 15) {
				MeleeHitFormula.getRandomHit(player, GameServer.getWorld().getPlayers()[2]);
			} else if (stage == 16) {
				player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 100000);
			} else if (stage == 17) {
				player.performAnimation(new Animation(828));
				player.getMovementQueue().setMovementFlag(MovementFlag.CANNOT_MOVE);
				GameServer.getTaskManager().submit(new Task(1) {
					@Override
					public void execute() {
						Position AgilityBranch = new Position(2473, 3420, 2);
						player.moveTo(AgilityBranch);
						player.getMovementQueue().setMovementFlag(MovementFlag.NONE);
						stop();
					}
				});
			} else if (stage == 18) {
				int id = Integer.valueOf(command[2]);
				Mob mob = new Mob(id, player.getPosition().copy());
				System.out.println("mob=" + id);
				MeleeHitFormula.getMaxHit(mob, player);
			} else if (stage == 19) {
				GroundItem groundItem = new GroundItem(new Item(4151), player.getPosition().copy());
				player.getPacketSender().sendGroundItem(groundItem, player);
			} else if (stage == 20) {
				
			} else if (stage == 21) {
				int id = Integer.valueOf(command[2]);
				ShopManager.getShops().get(id).open(player);
			} else if (stage == 22) {
				//RegionClipping.stepAway(player);
			} else if (stage == 23) {
				player.getPacketSender().sendConsoleMessage("Local players:");
				for (Player players : player.getRegion().getPlayers()) {
					player.getPacketSender().sendConsoleMessage("Player: " + players.getCredentials().getUsername());
				}
				
				player.getPacketSender().sendConsoleMessage("Local mobs:");
				for (Mob mob : player.getRegion().getMobs()) {
					player.getPacketSender().sendConsoleMessage("Mob: " + mob.getId());
				}
				
				player.getPacketSender().sendConsoleMessage("Local ground items:");
				for (GroundItem groundItem : player.getRegion().getGroundItem()) {
					player.getPacketSender().sendConsoleMessage("GroundItem: " + groundItem.getItem().getId());
				}
			} else if (stage == 25) {
				//SlayerTask.getRandomTask(player, new NPC(8273, null));
				Mob mob = GameServer.getWorld().getMobs()[1];
				CombatManager.engageCombat(mob, GameServer.getWorld().getPlayers()[1]);
			} else if (stage == 26) {
				//CombatManager.resetFlags(player);
				player.getPacketSender().sendConsoleMessage("Players online: " + GameServer.getWorld().getPlayersOnline());
			} else if (stage == 30) {
				player.getFields().getCombatAttributes().setPkPoints(Integer.valueOf(command[2]));
				player.getPacketSender().sendConsoleMessage("You now have: " + player.getFields().getCombatAttributes().getPkPoints() + " pk points.");
			} else if (stage == 31) {
				GameObject gameObject = new GameObject(Integer.valueOf(command[2]), player.getPosition());
				GameServer.getWorld().register(gameObject);
				
				player.getPacketSender().sendGameObjectAnimation(gameObject, new Animation(303));
			}
		}
	}
}