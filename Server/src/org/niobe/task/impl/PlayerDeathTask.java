package org.niobe.task.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.niobe.GameServer;
import org.niobe.model.Animation;
import org.niobe.model.GameCharacter;
import org.niobe.model.Item;
import org.niobe.model.PrayerBook;
import org.niobe.model.MovementQueue.MovementFlag;
import org.niobe.model.Position;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.UpdateFlag.Flag;
import org.niobe.model.weapon.WeaponSpecialBar;
import org.niobe.task.Task;
import org.niobe.world.GroundItem;
import org.niobe.world.Player;
import org.niobe.world.content.combat.CombatCurses;
import org.niobe.world.content.combat.CombatManager;
import org.niobe.world.content.combat.CombatPrayers;
import org.niobe.world.util.GameConstants;

/**
 * An implementation of {@link org.niobe.task.Task} submitted
 * when on the death hook of a {@link org.niobe.world.Player}.
 *
 * @author relex lawl
 */
public final class PlayerDeathTask extends Task {

	/**
	 * The PlayerDeathTask constructor.
	 * @param player	The player setting off the task.
	 */
	public PlayerDeathTask(Player player, boolean keepsItems) {
		this.player = player;
		this.keepItems = keepsItems;
		this.itemsKept = getMostValuedItems(player);
	}
	
	/**
	 * The player setting off the task.
	 */
	private Player player;
	
	/**
	 * This flag checks if a player will keep their items on death.
	 */
	private boolean keepItems;
	
	/**
	 * The amount of ticks on the task.
	 */
	private int ticks = 3;
	
	/**
	 * The items this player will keep once
	 * they die.
	 */
	private int[] itemsKept;
	
	@Override
	public void execute() {
		switch (ticks) {
		case 3:
			player.getFields().setDead(true);
			player.setEntityInteraction(null);
			player.getMovementQueue().setMovementFlag(MovementFlag.CANNOT_MOVE);
			player.performAnimation(new Animation(2261));
			player.getPacketSender().sendMessage("Oh dear, you are dead!");
			if (player.getFields().getPrayerBook() == PrayerBook.CURSES) {
				CombatCurses.deathHook(player);
			}
			break;
		case 1:
			if (!keepItems) {
				final GameCharacter character = CombatManager.getKillerByMostDamage(player);
				final Position position = player.getPosition().copy();
				for (int itemId : itemsKept) {
					if (itemId <= 0)
						continue;
					if (player.getInventory().contains(itemId)) {
						player.getInventory().delete(itemId, 1);
					} else if (player.getEquipment().contains(itemId)) {
						player.getEquipment().delete(itemId, 1);
					}
				}
				if (character != null && character.isPlayer()) {
					Player other = (Player) character;
					for (Item item : player.getInventory().getValidItems()) {
						GroundItem groundItem = new GroundItem(item, position, other);
						GameServer.getWorld().register(groundItem);
					}
					for (Item item : player.getEquipment().getValidItems()) {
						GroundItem groundItem = new GroundItem(item, position, other);
						GameServer.getWorld().register(groundItem);
					}
					other.getFields().getCombatAttributes().setPkPoints(other.getFields().getCombatAttributes().getPkPoints() + GameConstants.PK_POINTS_ADDITION);
					other.getPacketSender().sendMessage("You now have " + other.getFields().getCombatAttributes().getPkPoints() + " pk points.");
				} else {
					for (Item item : player.getInventory().getValidItems()) {
						GroundItem groundItem = new GroundItem(item, position, player);
						GameServer.getWorld().register(groundItem);
					}
					for (Item item : player.getEquipment().getValidItems()) {
						GroundItem groundItem = new GroundItem(item, position, player);
						GameServer.getWorld().register(groundItem);
					}
				}
				player.getInventory().resetItems().refreshItems();
				player.getEquipment().resetItems().refreshItems();
			}
			CombatManager.resetFlags(player);
			player.getFields().getCombatAttributes().getDamageMap().clear();
			for (int itemId : itemsKept) {
				if (itemId <= 0) 
					continue;
				player.getInventory().add(new Item(itemId));
			}
			break;
		case 0:
			player.getMovementQueue().setMovementFlag(MovementFlag.NONE);
			player.getFields().setDead(false);
			player.moveTo(GameConstants.DEFAULT_POSITION);
			player.getFields().setRunEnergy(100);
			for (Skill skill : Skill.values()) {
				player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill));
			}
			if (player.getFields().getPrayerBook() == PrayerBook.NORMAL) {
				CombatPrayers.deactivatePrayers(player);
			} else if (player.getFields().getPrayerBook() == PrayerBook.CURSES) {
				CombatCurses.deactivateCurses(player);
			}
			player.setAnimation(new Animation(65535));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getFields().getCombatAttributes().setUsingSpecialAttack(false);
			WeaponSpecialBar.update(player);
			stop();
			break;
		}
		ticks--;
	}
	
	public static int[] getMostValuedItems(Player player) {
		List<Item> items = new ArrayList<Item>();
		items.addAll(player.getInventory().getValidItems());
		items.addAll(player.getEquipment().getValidItems());
		Collections.sort(items, new Comparator<Item>() {
			@Override
			public int compare(Item o1, Item o2) {
				if(o1.getDefinition().getValue() < o2.getDefinition().getValue()) {
					o1.setAmount(1);
					o2.setAmount(1);
					return 1;
				}
				return 0;
			}
		});
		int first = items.size() > 0 ? items.get(0).getId() : -1;
		int second = items.size() > 1 ? items.get(1).getId() : -1;
		int third = items.size() > 2 ? items.get(2).getId() : -1;
		int fourth = items.size() > 3 ? items.get(3).getId() : -1;
		boolean keepsFourthItem = player.getFields().getPrayerBook() == PrayerBook.CURSES ? (player.getFields().getPrayerActive()[CombatCurses.PROTECT_ITEM]) :
									(player.getFields().getPrayerActive()[CombatPrayers.PROTECT_ITEM]);
		return !keepsFourthItem ? 
				new int[] {first, second, third} :
				new int[] {first, second, third, fourth};
	}
}
