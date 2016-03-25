package org.niobe.net.packet;

import org.niobe.model.Animation;
import org.niobe.model.Entity;
import org.niobe.model.Graphic;
import org.niobe.model.Item;
import org.niobe.model.MagicSpellBook;
import org.niobe.model.PlayerRights;
import org.niobe.model.Position;
import org.niobe.model.PrayerBook;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.container.ItemContainer;
import org.niobe.model.container.impl.Equipment;
import org.niobe.model.container.impl.Inventory;
import org.niobe.model.container.impl.Shop;
import org.niobe.model.weapon.WeaponSpecialBar;
import org.niobe.net.packet.GamePacket.PacketType;
import org.niobe.util.NameUtil;
import org.niobe.world.GameObject;
import org.niobe.world.GroundItem;
import org.niobe.world.Player;
import org.niobe.world.Projectile;
import org.niobe.world.content.BonusManager;
import org.niobe.world.content.PlayerSaving;
import org.niobe.world.content.clan.ClanChatManager;
import org.niobe.world.content.combat.CombatPrayers;
import org.niobe.world.region.RegionManager;
import org.niobe.world.util.GameConstants;

/**
 * Used to send packets from the server to 
 * player client(s).
 *
 * @author relex lawl
 */
public final class GamePacketSender {

	/**
	 * The GamePacketSender constructor.
	 * @param player	The associated player.
	 */
	public GamePacketSender(Player player) {
		this.player = player;
	}
	
	/**
	 * Sends the required packets and general data for a player's 
	 * successful login.
	 * @return	The GamePacketSender instance.
	 */
	public GamePacketSender sendLogin() {
		player.write(new GamePacketBuilder(249).writeByteA(1).writeLEShortA(player.getIndex()).toPacket());
		player.getFields().setRegionChange(true);
		sendLoginConfigurations();
		sendInteractionOption("Attack", 2, true);
		sendInteractionOption("Follow", 3, false);
		sendInteractionOption("Trade With", 4, false);
		sendItemContainer(player.getInventory(), Inventory.INTERFACE_ID);
		sendItemContainer(player.getEquipment(), Equipment.INVENTORY_INTERFACE_ID);
		player.getRelations().setPrivateMessageId(1).updateLists(true);
		sendMessage("Welcome to Niobe.");
		player.performGraphic(new Graphic(2000, 8));
		return this;
	}
	
	public GamePacketSender sendLoginConfigurations() {
		player.setRights(PlayerRights.ADMINISTRATOR);
		for (int i = 0; i < GameConstants.SIDEBAR_INTERFACES.length; i++)
			sendTabInterface(i, GameConstants.SIDEBAR_INTERFACES[i]);
		for (Skill skill : Skill.values()) {
			sendSkill(skill);
			player.getSkillManager().updateSkill(skill);
		}
		byte foundNotes = 0;
		for (String note : player.getFields().getNotes()) {
			if (note != null) {
				sendNote(note);
				foundNotes++;
			}
		}
		sendString(13800, foundNotes > 0 ? "" : "No notes");
		sendString(13801, "@lre@Notes (" + foundNotes + "/" + player.getFields().getNotes().length + ")");
		sendRunStatus();
		BonusManager.update(player);
		sendString(2426, player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() > 0 ? player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getDefinition().getName() : "None");
		sendTabInterface(GameConstants.PRAYER_TAB, player.getFields().getPrayerBook().equals(PrayerBook.NORMAL) ? PrayerBook.NORMAL.getInterfaceId() : PrayerBook.CURSES.getInterfaceId());
		int spellbook = player.getFields().getSpellbook().ordinal();
		sendTabInterface(GameConstants.MAGIC_TAB, spellbook == 0 ? MagicSpellBook.NORMAL.getInterfaceId() : spellbook == 1 ? MagicSpellBook.ANCIENT.getInterfaceId() : MagicSpellBook.LUNAR.getInterfaceId());
		sendRunEnergy(player.getFields().getRunEnergy());
		sendConfig(172, player.getFields().getCombatAttributes().isAutoRetaliation() ? 1 : 0);
		sendConfig(43, player.getFields().getCombatAttributes().getAttackStyle());
		/*QuickPrayers.login(player);
		QuickCurses.login(player);*/
		WeaponSpecialBar.update(player);
		return this;
	}
	
	/**
	 * Sends the removal of every player attribute that should
	 * be taken out when they are in movement.
	 * @return	The GamePacketSender instance.
	 */
	public GamePacketSender sendNonWalkableAttributeRemoval() {
		player.getFields().setInterfaceId(-1).setShop(null).setItemToSell(null);
		return this;
	}
	
	/**
	 * Sends the map region a player is located in and also
	 * sets the player's first step position of said region as their
	 * {@code lastKnownRegion}.
	 * @return	The GamePacketSender instance.
	 */
	public GamePacketSender sendMapRegion() {
		player.setLastKnownRegion(player.getPosition().copy());
		player.write(new GamePacketBuilder(73).writeShortA(player.getPosition().getRegionX() + 6).writeShort(player.getPosition().getRegionY() + 6).toPacket());
		RegionManager.register(player);
		return this;
	}
	
	/**
	 * Sends the logout packet for the player.
	 * @return	The GamePacketSender instance.
	 */
	public GamePacketSender sendLogout() {
		player.write(new GamePacketBuilder(109).toPacket());
		ClanChatManager.leave(player, false);
		CombatPrayers.deactivatePrayers(player);
		player.getRelations().updateLists(false);
		PlayerSaving.save(player);
		return this;
	}
	
	/**
	 * Sets the world's system update time, once timer is 0, everyone will be disconnected.
	 * @param time	The amount of seconds in which world will be updated in.
	 * @return		The GamePacketSender instance.
	 */
	public GamePacketSender sendSystemUpdate(int time) {
		player.write(new GamePacketBuilder(114).writeLEShort(time).toPacket());
		return this;
	}
	
	/**
	 * Sends a game message to a player in the server.
	 * @param message	The message they will receive in chat box.
	 * @return			The GamePacketSender instance.
	 */
	public GamePacketSender sendMessage(String message) {
		player.write(new GamePacketBuilder(253, PacketType.BYTE).writeString(message).toPacket());
		return this;
	}
	
	/**
	 * Sends skill information onto the client, to calculate things such as
	 * constitution, prayer and summoning orb and other configurations.
	 * @param skill		The skill being sent.
	 * @return			The GamePacketSender instance.
	 */
	public GamePacketSender sendSkill(Skill skill) {
		GamePacketBuilder bldr = new GamePacketBuilder(134);
		bldr.writeByte(skill.ordinal());
		bldr.writeSingleInt(player.getSkillManager().getExperience(skill));
		bldr.writeShort(player.getSkillManager().getCurrentLevel(skill));
		bldr.writeShort(player.getSkillManager().getMaxLevel(skill));
		player.write(bldr.toPacket());
		return this;
	}
	
	/**
	 * Sends a configuration button's state.
	 * @param configId	The id of the configuration button.
	 * @param state		The state to set it to.
	 * @return			The GamePacketSender instance.
	 */
	public GamePacketSender sendConfig(int configId, int state) {
		GamePacketBuilder builder = new GamePacketBuilder(36);
		builder.writeLEShort(configId);
		builder.writeByte(state);
		player.write(builder.toPacket());
		return this;
	}
	
	/**
	 * Sends a interface child's toggle. 
	 * @param id		The id of the child.
	 * @param state		The state to set it to.
	 * @return			The GamePacketSender instance.
	 */
	public GamePacketSender sendToggle(int id, int state) {
		player.write(new GamePacketBuilder(87).writeLEShort(id).writeSingleInt(state).toPacket());
		return this;
	}
	
	/**
	 * Sends the state in which the player has their chat options, such as public, private, friends only.
	 * @param publicChat	The state of their public chat.
	 * @param privateChat	The state of their private chat.
	 * @param tradeChat		The state of their trade chat.
	 * @return				The GamePacketSender instance.
	 */
	public GamePacketSender sendChatOptions(int publicChat, int privateChat, int tradeChat) {
		player.write(new GamePacketBuilder(206).writeByte(publicChat).writeByte(privateChat).writeByte(tradeChat).toPacket());
		return this;
	}
	
	public GamePacketSender sendSong(int id) {
		player.write(new GamePacketBuilder(74).writeLEShort(id).toPacket());
		return this;
	}
	
	public GamePacketSender sendSongConfigurations(int nextSong, int previousSong) {
		player.write(new GamePacketBuilder(121).writeLEShortA(nextSong).writeShortA(previousSong).toPacket());
		return this;
	}
	
	public GamePacketSender sendSound(int soundId, int volume, int delay) {
		player.write(new GamePacketBuilder(174).writeShort(soundId).writeByte(volume).writeShort(delay).toPacket());
		return this;
	}
	
	public GamePacketSender sendRunEnergy(int energy) {
		player.write(new GamePacketBuilder(110).writeByte(energy).toPacket());
		return this;
	}
	
	public GamePacketSender sendRunStatus() {
		player.write(new GamePacketBuilder(113).writeByte(player.getFields().isRunning() ? 1 : 0).toPacket());
		return this;
	}
	
	public GamePacketSender sendWeight(int weight) {
		player.write(new GamePacketBuilder(240).writeShort(weight).toPacket());
		return this;
	}
	
	public GamePacketSender sendInterface(int id) {
		player.write(new GamePacketBuilder(97).writeShort(id).toPacket());
		player.getFields().setInterfaceId(id);
		return this;
	}
	
	public GamePacketSender sendWalkableInterface(int interfaceId) {
		player.write(new GamePacketBuilder(208).writeShort(interfaceId).toPacket());
		return this;
	}
	
	public GamePacketSender sendFullScreenInterface(int backgroundInterfaceId, int interfaceId) {
		player.write(new GamePacketBuilder(69).writeShort(backgroundInterfaceId).writeShort(interfaceId).toPacket());
		return this;
	}
	
	public GamePacketSender sendInterfaceDisplayState(int interfaceId, boolean hide) {
		player.write(new GamePacketBuilder(171).writeByte(hide ? 1 : 0).writeShort(interfaceId).toPacket());
		return this;
	}
	
	public GamePacketSender sendInterfaceMediaState(int interfaceId, int state) {
		player.write(new GamePacketBuilder(8).writeLEShortA(interfaceId).writeShort(state).toPacket());
		return this;
	}
	
	public GamePacketSender sendInventoryOverlayInterface(int interfaceId) {
		player.write(new GamePacketBuilder(142).writeLEShort(interfaceId).toPacket());
		return this;
	}
	
	public GamePacketSender sendStringColour(int stringId, int colour) {
		player.write(new GamePacketBuilder(122).writeLEShortA(stringId).writeLEShortA(colour).toPacket());
		return this;
	}
	
	public GamePacketSender sendPlayerHeadOnInterface(int id) {
		player.write(new GamePacketBuilder(185).writeLEShortA(id).toPacket());
		return this;
	}
	
	public GamePacketSender sendNpcHeadOnInterface(int id, int interfaceId) {
		player.write(new GamePacketBuilder(75).writeLEShortA(id).writeLEShortA(interfaceId).toPacket());
		return this;
	}
	
	public GamePacketSender sendEnterAmountPrompt() {
		player.write(new GamePacketBuilder(27).toPacket());
		return this;
	}
	
	public GamePacketSender sendInterfaceReset() {
		player.write(new GamePacketBuilder(68).toPacket());
		return this;
	}
	
	public GamePacketSender sendInterfaceComponentMoval(int id, int x, int y) {
		player.write(new GamePacketBuilder(70).writeShort(x).writeShort(y).writeLEShort(id).toPacket());
		return this;
	}

	public GamePacketSender sendInterfaceColor(int id, String color) {
		long longColor = NameUtil.stringToLong(color);
		player.write(new GamePacketBuilder(141).writeShort(id).writeLong(longColor).toPacket());
		return this;
	}
	
	public GamePacketSender sendInterfaceEdit(int zoom, int id, int rotationX, int rotationY) {
		player.write(new GamePacketBuilder(230).writeShortA(zoom).writeShort(id).writeShort(rotationX).writeLEShortA(rotationY).toPacket());
		return this;
	}
	
	public GamePacketSender sendInterfaceAnimation(int interfaceId, Animation animation) {
		player.write(new GamePacketBuilder(200).writeShort(interfaceId).writeShort(animation.getId()).toPacket());
		return this;
	}
	
	public GamePacketSender sendInterfaceModel(int interfaceId, int itemId, int zoom) {
		player.write(new GamePacketBuilder(246).writeLEShort(interfaceId).writeShort(zoom).writeShort(itemId).toPacket());
		return this;
	}
	
	public GamePacketSender sendModelOnComponent(int interfaceId, int modelId) {
		player.write(new GamePacketBuilder(8).writeLEShortA(interfaceId).writeShort(modelId).toPacket());
		return this;
	}
	
	public GamePacketSender sendScrollbar(int childId, int location) {
		player.write(new GamePacketBuilder(79).writeLEShort(childId).writeShortA(location).toPacket());
		return this;
	}
	
	public GamePacketSender sendTabInterface(int tabId, int interfaceId) {
		player.write(new GamePacketBuilder(71).writeShort(interfaceId).writeByteA(tabId).toPacket());
		return this;
	}
	
	public GamePacketSender sendTab(int id) {
		player.write(new GamePacketBuilder(106).writeByteC(id).toPacket());
		return this;
	}
	
	public GamePacketSender sendFlashingSidebar(int id) {
		player.write(new GamePacketBuilder(24).writeByteS(id).toPacket());
		return this;
	}
	
	public GamePacketSender sendChatInterface(int id) {
		player.write(new GamePacketBuilder(218).writeLEShort(id).toPacket());
		return this;
	}
	
	public GamePacketSender sendChatboxInterface(int id) {
		player.write(new GamePacketBuilder(164).writeLEShort(id).toPacket());
		return this;
	}
	
	public GamePacketSender sendMapState(int state) {
		player.write(new GamePacketBuilder(99).writeByte(state).toPacket());
		return this;
	}
	
	public GamePacketSender sendCameraAngle(Position position, int speed, int angle) {
		player.write(new GamePacketBuilder(177).writeByte(position.getX()).writeByte(position.getY()).writeShort(position.getZ()).writeByte(speed).writeByte(angle).toPacket());
		return this;
	}
	
	public GamePacketSender sendCameraShake(int verticalAmount, int verticalSpeed, int horizontalAmount, int horizontalSpeed) {
		player.write(new GamePacketBuilder(35).writeByte(verticalAmount).writeByte(verticalSpeed).writeByte(horizontalAmount).writeByte(horizontalSpeed).toPacket());
		return this;
	}
	
	public GamePacketSender sendCameraSpin(Position position, int speed, int angle) {
		player.write(new GamePacketBuilder(166).writeByte(position.getX()).writeByte(position.getY()).writeShort(position.getZ()).writeByte(speed).writeByte(angle).toPacket());
		return this;
	}
	
	public GamePacketSender sendCameraNeutrality() {
		player.write(new GamePacketBuilder(107).toPacket());
		return this;
	}
	
	public GamePacketSender sendInterfaceRemoval() {
		player.getFields().setInterfaceId(-1).setShop(null).setItemToSell(null);
		player.write(new GamePacketBuilder(219).toPacket());
		return this;
	}
	
	public GamePacketSender sendInterfaceSet(int interfaceId, int sidebarInterfaceId) {
		GamePacketBuilder out = new GamePacketBuilder(248);
		out.writeShortA(interfaceId);
		out.writeShort(sidebarInterfaceId);
		player.write(out.toPacket());
		return this;
	}

	public GamePacketSender sendItemContainer(ItemContainer container, int interfaceId) {
		boolean shopContainer = container.getClass().getName().equals(Shop.class.getName());
		Shop shop = null;
		if (shopContainer) {
			shop = (Shop) container;
		}
		GamePacketBuilder builder = new GamePacketBuilder(53, PacketType.SHORT);
		builder.writeShort(interfaceId);
		builder.writeShort(container.capacity());
		if (shop != null) {
			builder.writeShort(shop.getCurrency().getSpriteId());
		}
		for (Item item : container.getItems()) {
			if (item.getAmount() > 254 && !shopContainer) {
				builder.writeByte(255);
				builder.writeDoubleInt(item.getAmount());
			} else {
				builder.writeByte(item.getAmount());
			}
			builder.writeLEShortA(item.getId() + 1);
			if (shop != null) {
				builder.writeInt(shop.getCurrency().getSellPrice(item));
			}
		}
		player.write(builder.toPacket());
		return this;
	}
	
	public GamePacketSender sendInteractionOption(String option, int slot, boolean top) {
		GamePacketBuilder bldr = new GamePacketBuilder(104, PacketType.BYTE);
		bldr.writeByteC(slot);
		bldr.writeByteA(top ? 1 : 0);
		bldr.writeString(option);
		player.write(bldr.toPacket());
		return this;
	}

	public GamePacketSender sendString(int id, String string) {
		GamePacketBuilder bldr = new GamePacketBuilder(126, PacketType.SHORT);
		bldr.writeString(string);
		bldr.writeShortA(id);
		player.write(bldr.toPacket());
		return this;
	}
	
	/**
	 * Sends a hint to specified position.
	 * @param position			The position to create the hint.
	 * @param tilePosition		The position on the square (middle = 2; west = 3; east = 4; south = 5; north = 6)
	 * @return					The Packet Sender instance.
	 */
	public GamePacketSender sendPositionalHint(Position position, int tilePosition) {
		player.write(new GamePacketBuilder(254).writeByte(tilePosition).writeShort(position.getX()).writeShort(position.getY()).writeByte(position.getZ()).toPacket());
		return this;
	}
	
	/**
	 * Sends a hint above an entity's head.
	 * @param entity	The target entity to draw hint for.
	 * @return			The GamePacketSender instance.
	 */
	public GamePacketSender sendEntityHint(Entity entity) {
		int type = entity instanceof Player ? 10 : 1;
		player.write(new GamePacketBuilder(254).writeByte(type).writeShort(entity.getIndex()).writeTripleInt(0).toPacket());
		return this;
	}
	
	public GamePacketSender sendMultiIcon(int value) {
		player.write(new GamePacketBuilder(61).writeByte(value).toPacket());
		return this;
	}
	
	public GamePacketSender sendPrivateMessage(long name, PlayerRights rights, byte[] message, int size) {
		//player.write(new GamePacketBuilder(196, PacketType.BYTE).writeLong(name).writeInt(player.getRelations().getPrivateMessageId()).writeByte(rights.ordinal()).writeByteArray(message, 0, size).toPacket());
		return this;
	}
	
	public GamePacketSender sendFriendStatus(int status) {
		try {
			player.write(new GamePacketBuilder(221).writeByte(status).toPacket());
		} catch (Exception exception) {}
		return this;
	}
	
	public GamePacketSender sendFriend(long name, int world) {
		world = world != 0 ? world + 9 : world;
		player.write(new GamePacketBuilder(50).writeLong(name).writeByte(world).toPacket());
		return this;
	}

	public GamePacketSender sendIgnoreListName() {
		player.write(new GamePacketBuilder(214, PacketType.SHORT).toPacket());
		return this;
	}
	
	public GamePacketSender sendObjectTransformation(GameObject object) {
		/**
		 * TODO: this packet turns a player into an object,
		 * could be used for things such as, ring of stone transformation, fight pits orb viewing, etc.
		 * Some information can be found here: http://www.rune-server.org/runescape-development/rs2-client/help/379637-whats-do-packet-147-a.html
		 */
		GamePacketBuilder builder = new GamePacketBuilder(147);
		int x = (player.getPosition().getLocalX() & 0x7) << 4;
		int offset = x + (player.getPosition().getLocalY() & 0x7);
		builder.writeByteS(offset)
				.writeShort(player.getIndex())
				.writeByteS(0)
				.writeLEShort(object.getId())
				.writeByteC(0)
				.writeShort(object.getId() + 1)
				.writeByteS((object.getType() << 2) + (object.getFace() & 3))
				.writeByte(0)
				.writeShort(object.getId())
				.writeByteC(0);
		player.write(builder.toPacket());
		return this;
	}
	
	public GamePacketSender sendProjectile(Projectile projectile) {
		final Position position = projectile.getPosition();
		final Position target = projectile.getDestination();
		int x = position.getX() - player.getLastKnownRegion().getRegionX() * 8;
		int y = position.getY() - player.getLastKnownRegion().getRegionY() * 8;
		player.write(new GamePacketBuilder(85).writeByteC(y).writeByteC(x).toPacket());
		int distance = position.getDistance(projectile.getDestination());
		int duration = projectile.getDelay() + projectile.getSpeed() + distance * 5;
		x = position.getX() - (position.getRegionX() << 4);
		y = position.getY() - (position.getRegionY() & 0x7);
		int offset = ((x & 0x7)) << 4 + (y & 0x7);
		player.write(new GamePacketBuilder(117).
			writeByte(offset).
			writeByte(target.getY() - position.getY()).
			writeByte(target.getX() - position.getX()).
			writeShort(projectile.getLockon() != null ? (projectile.getLockon().isPlayer() ? -(projectile.getLockon().getIndex() + 1) : projectile.getLockon().getIndex() + 1) : 0).
			writeShort(projectile.getGraphic().getId()).
			writeByte(position.getZ()).
			writeByte(target.getZ()).
			writeShort(projectile.getDelay()).
			writeShort(duration).
			writeByte(projectile.getAngle()).
			writeByte(1 * 64 + offset * 64).toPacket());
		return this;
	}
	
	public GamePacketSender sendAnimationReset() {
		player.write(new GamePacketBuilder(1).toPacket());
		return this;
	}
	
	public GamePacketSender sendGraphic(Graphic graphic, Position position) {
		sendPosition(position);
		player.write(new GamePacketBuilder(4).writeByte(0).writeShort(graphic.getId()).writeByte(position.getZ()).writeShort(graphic.getDelay()).toPacket());
		return this;
	}
	
	public GamePacketSender sendGameObject(GameObject object) {
		sendPosition(object.getPosition());
		player.write(new GamePacketBuilder(151).writeByteA(0).writeLEShort(object.getDefinition().getId()).writeByteS((byte) ((object.getType() << 2) + (object.getFace() & 3))).toPacket());
		return this;
	}
	
	public GamePacketSender sendGameObjectRemoval(GameObject object) {
		sendPosition(object.getPosition());
		player.write(new GamePacketBuilder(101).writeByteC((object.getType() << 2) + (object.getFace() & 3)).writeByte((byte)0).toPacket());
		return this;
	}
	
	public GamePacketSender sendGameObjectAnimation(GameObject object, Animation animation) {
		sendPosition(object.getPosition());
		player.write(new GamePacketBuilder(160).writeByteS(0).writeByteS((object.getType() << 2) + (object.getFace() & 3)).writeShortA(animation.getId()).toPacket());
		return this;
	}
	
	public GamePacketSender sendGroundItem(GroundItem groundItem) {
		sendPosition(groundItem.getPosition().copy());
		player.write(new GamePacketBuilder(44).writeLEShortA(groundItem.getItem().getId()).writeShort(groundItem.getItem().getAmount()).writeByte(0).toPacket());
		return this;
	}
	
	public GamePacketSender sendGroundItem(GroundItem groundItem, Player player) {
		sendPosition(groundItem.getPosition());
		player.write(new GamePacketBuilder(215).writeShortA(groundItem.getItem().getId()).writeByteS(0).writeShortA(player.getIndex()).writeShort(groundItem.getItem().getAmount()).toPacket());
		return this;
	}
	
	public GamePacketSender sendGroundItemAmount(Position position, Item item, int amount) {
		sendPosition(position);
		player.write(new GamePacketBuilder(84).writeByte(0).writeShort(item.getId()).writeShort(item.getAmount()).writeShort(amount).toPacket());
		return this;
	}

	public GamePacketSender sendGroundItemRemoval(GroundItem groundItem) {
		sendPosition(groundItem.getPosition().copy());
		player.write(new GamePacketBuilder(156).writeByteA(0).writeShort(groundItem.getItem().getId()).toPacket());
		return this;
	}
	
	public GamePacketSender sendRegion(Position position) {
		player.write(new GamePacketBuilder(64).writeByteC(position.getLocalY()).writeByteS(position.getLocalX()).toPacket());
		return this;
	}
	
	public GamePacketSender sendPosition(final Position position) {
		final Position other = player.getLastKnownRegion();
		player.write(new GamePacketBuilder(85).writeByteC(position.getY() - 8 * other.getRegionY()).writeByteC(position.getX() - 8 * other.getRegionX()).toPacket());
		return this;
	}

	public GamePacketSender sendConsoleMessage(String message) {
		player.write(new GamePacketBuilder(123, PacketType.BYTE).writeString(message).toPacket());
		return this;
	}
	
	public GamePacketSender sendQuickPrayerStatus(boolean usingQuickPrayer) {
		player.write(new GamePacketBuilder(88).writeByte(usingQuickPrayer ? 1 : 0).toPacket());
		return this;
	}
	
	public GamePacketSender sendConstitutionOrbStatus(boolean poison) {
		//player.write(new GamePacketBuilder(89).writeByte(poison ? 1 : 0).toPacket());
		//TODO fix this client-sided
		return this;
	}
	
	public GamePacketSender sendInterfaceSpriteChange(int childId, int firstSprite, int secondSprite) {
		player.write(new GamePacketBuilder(140).writeShort(childId).writeByte((firstSprite << 0) + (secondSprite & 0x0)).toPacket());
		return this;
	}
	
	public GamePacketSender sendNote(String note) {
		player.write(new GamePacketBuilder(130, PacketType.BYTE).writeString(note).toPacket());
		return this;
	}
	
	/**
	 * Gets the position's offset that is used in client's
	 * region system.
	 * @param position	The position to get offset for.
	 * @return			The GamePacketSender instance.
	 */
	public int getRegionOffset(Position position) {
		int x = position.getX() - (position.getRegionX() << 4);
		int y = position.getY() - (position.getRegionY() & 0x7);
		int offset = ((x & 0x7)) << 4 + (y & 0x7);
		return offset;
	}
	
	private final Player player;
}
