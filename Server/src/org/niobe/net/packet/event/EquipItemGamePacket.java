package org.niobe.net.packet.event;

import java.util.logging.Logger;

import org.niobe.model.Item;
import org.niobe.model.MovementQueue.MovementFlag;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.UpdateFlag.Flag;
import org.niobe.model.action.impl.ItemWieldGameAction;
import org.niobe.model.container.impl.Equipment;
import org.niobe.model.container.impl.Inventory;
import org.niobe.model.definition.ItemDefinition;
import org.niobe.model.weapon.Weapon;
import org.niobe.model.weapon.WeaponSpecialBar;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacketEvent;
import org.niobe.util.NameUtil;
import org.niobe.world.Player;
import org.niobe.world.content.BonusManager;

/**
 * An implementation of {@link org.niobe.net.packet.GamePacketEvent}
 * that handles a {@link org.niobe.world.Player} equipping {@link org.niobe.model.Item}s
 * into their {@link org.niobe.world.Player#getEquipment()} container.
 *
 * @author relex lawl
 */
public final class EquipItemGamePacket implements GamePacketEvent {
	
	/**
	 * The PacketListener logger to debug information and print out errors.
	 */
	private final Logger logger = Logger.getLogger(EquipItemGamePacket.class.getName());
	
	@Override
	public void read(Player player, GamePacket packet) {
		if (player.getFields().isDead() ||
				player.getMovementQueue().getMovementFlag() == MovementFlag.STUNNED)
			return;
		int id = packet.readShort();
		int slot = packet.readShortA();
		int interfaceId = packet.readShortA();
		switch (interfaceId) {
		case Inventory.INTERFACE_ID:
			/*
			 * Making sure slot is valid.
			 */
			if (slot >= 0 && slot <= 28) {
				Item item = player.getInventory().getItems()[slot].copy();
				/*
				 * Making sure item exists and that id is consistent.
				 */
				if (item != null && id == item.getId()) {
					player.setAction(new ItemWieldGameAction<Player>(player, item));
					for (Skill skill : Skill.values()) {
						if (item.getDefinition().getRequirement()[skill.ordinal()] > player.getSkillManager().getMaxLevel(skill)) {
							String name = NameUtil.getVowelFormat(skill.getName());
							String wearType = ItemDefinition.forId(item.getId()).isWeapon() ? "wield" : "wear";
							player.getPacketSender().sendMessage("You need " + name + " level of " + item.getDefinition().getRequirement()[skill.ordinal()] + " to " + wearType + " this!");
							return;
						}
					}
					int equipmentSlot = item.getDefinition().getEquipmentSlot();
					Item equipItem = player.getEquipment().forSlot(equipmentSlot).copy();
					if (player.getFields().getCombatAttributes().hasStaffOfLightEffect() 
							&& equipItem.getDefinition().getName().toLowerCase().contains("staff of light")) {
						player.getFields().getCombatAttributes().setStaffOfLightEffect(false);
						player.getPacketSender().sendMessage("You feel the spirit of the staff of light begin to fade away...");
					}
					if (equipItem.getDefinition().isStackable() && equipItem.getId() == item.getId()) {
						int amount = equipItem.getAmount() + item.getAmount() <= Integer.MAX_VALUE ? equipItem.getAmount() + item.getAmount() : Integer.MAX_VALUE;
						player.getInventory().delete(item);
						equipItem.setAmount(amount);
					} else {
						if (item.getDefinition().isTwoHanded() && item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT) {
							int slotsNeeded = 0;
							if (player.getEquipment().isSlotOccupied(Equipment.SHIELD_SLOT) && player.getEquipment().isSlotOccupied(Equipment.WEAPON_SLOT)) {
								slotsNeeded++;
							}
							if (player.getInventory().getFreeSlots() >= slotsNeeded) {
								Item shield = player.getEquipment().getItems()[Equipment.SHIELD_SLOT];
								player.getInventory().setItem(slot, equipItem);
								player.getInventory().add(shield);
								player.getEquipment().delete(shield);
								player.getEquipment().setItem(equipmentSlot, item);
							} else {
								player.getInventory().sendContainerFullMessage();
								return;
							}
						} else if (equipmentSlot == Equipment.SHIELD_SLOT && player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getDefinition().isTwoHanded()) {
							player.getInventory().setItem(slot, player.getEquipment().getItems()[Equipment.WEAPON_SLOT]);
							player.getEquipment().setItem(Equipment.WEAPON_SLOT, new Item(-1));
							player.getEquipment().setItem(Equipment.SHIELD_SLOT, item);
						} else {
							if (item.getDefinition().getEquipmentSlot() == equipItem.getDefinition().getEquipmentSlot() && equipItem.getId() != -1) {
								player.getInventory().setItem(slot, equipItem);
								player.getEquipment().setItem(equipmentSlot, item);
							} else {
								player.getInventory().setItem(slot, new Item(-1));
								player.getEquipment().setItem(item.getDefinition().getEquipmentSlot(), item);
							}
						}
					}
					BonusManager.update(player);
					Weapon weapon = player.getWeapon();
					if (player.getFields().getCombatAttributes().getAttackStyle() >= weapon.getCombatDefinition().getAnimations().length) {
						player.getFields().getCombatAttributes().setAttackStyle(weapon.getCombatDefinition().getAnimations().length - 1);
						player.getPacketSender().sendConfig(43, player.getFields().getCombatAttributes().getAttackStyle());
					}
					player.getInventory().refreshItems();
					player.getEquipment().refreshItems();
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					if (ItemDefinition.forId(item.getId()).getEquipmentSlot() == Equipment.WEAPON_SLOT) {
						player.getFields().getCombatAttributes().setUsingSpecialAttack(false);
						WeaponSpecialBar.update(player);
					}
				}
			}
			break;
		default:
			logger.info("Unhandled equip interface id: " + interfaceId);
			break;
		}
	}

}
