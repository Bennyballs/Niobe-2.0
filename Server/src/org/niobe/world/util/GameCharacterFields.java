package org.niobe.world.util;

import java.util.ArrayList;
import java.util.List;

import org.niobe.model.Item;
import org.niobe.model.MagicSpellBook;
import org.niobe.model.Position;
import org.niobe.model.PrayerBook;
import org.niobe.model.container.impl.Shop;
import org.niobe.world.GameObject;
import org.niobe.world.Mob;
import org.niobe.world.Player;
import org.niobe.world.content.BonusManager;
import org.niobe.world.content.clan.ClanChat;
import org.niobe.world.content.clan.ClanChat.MessageColor;
import org.niobe.world.content.combat.CombatAttributes;
import org.niobe.world.content.dialogue.Dialogue;

/**
 * Stores all miscellaneous variables for {@link org.niobe.model.GameCharacter}s
 * in the world.
 *
 * @author relex lawl
 */
public final class GameCharacterFields {

	/**
	 * Checks if the character is currently dead, as in
	 * has 0 constitution points.
	 */
	private boolean dead;
	
	/**
	 * Checks if player is changing their current region
	 * and is in the 'Loading - Please wait' stage.
	 */
	private boolean changingRegion;
	
	/**
	 * Checks if the player is currently teleporting.
	 */
	private boolean teleporting;
	
	/**
	 * Checks if a player is currently banking to stop
	 * cheat-clients and such from opening the bank interface
	 * and banking.
	 */
	private boolean banking;
	
	/**
	 * Checks if a player is running or not.
	 */
	private boolean running;
	
	/**
	 * The player's run energy, this measures the amount of time/steps they
	 * are capable of running until they are 'tired'.
	 */
	private int runEnergy = 100;
	
	/**
	 * This flag checks if the player is recovering
	 * their run energy through {@link org.niobe.task.impl.PlayerRunEnergyTask}.
	 */
	private boolean recoveringRunEnergy;
	
	/**
	 * The interface id the player is currently viewing.
	 */
	private int interfaceId;
	
	/**
	 * The player's npc transformation id.
	 */
	private int npcTransformationId;
	
	/**
	 * The game character's current poison damage.
	 */
	private int poisonDamage;
	
	/**
	 * Checks if player is 'stunned' and cannot use overhead
	 * prayers.
	 */
	private boolean overheadPrayerCap;
	
	/**
	 * A boolean array containing whether a prayer has been selected.
	 */
	private boolean[] prayerActive = new boolean[30];
	
	/**
	 * A boolean array containing whether a prayer has been selected
	 * as a 'quick prayer'.
	 */
	private boolean[] quickPrayer =  new boolean[30];
	
	/**
	 * A boolean array containing whether a prayer has been selected
	 * as a 'quick curse'.
	 */
	private boolean[] quickCurse =  new boolean[20];
	
	/**
	 * Checks if player is currently using either
	 * quick prayers or quick curses.
	 */
	private boolean usingQuickPrayer;
	
	/**
	 * The player's current client window size (fixed, resizeable, fullscreen).
	 */
	private int clientSize = 0;
	
	/**
	 * The player's current shop id they are viewing.
	 */
	private Shop shop;
	
	/**
	 * The player's total experience on their experience counter.
	 */
	private int totalExperienceOnCounter;
	
	/**
	 * The player's graphic delay.
	 */
	private long graphicDelay;
	
	/**
	 * The player's delay between consuming foods.
	 */
	private long foodDelay;
	
	/**
	 * The character's message they are being forced to say
	 * through the forced_chat update mask.
	 */
	private String forcedChat;
	
	/**
	 * The position the character will teleport to.
	 */
	private Position teleportPosition;
	
	/**
	 * The player's saved notes for their notes tab.
	 */
	private String[] notes = new String[30];
	
	/**
	 * The player's magic spell book.
	 */
	private MagicSpellBook spellbook = MagicSpellBook.NORMAL;
	
	/**
	 * The player's prayer book.
	 */
	private PrayerBook prayerBook = PrayerBook.NORMAL;
	
	/**
	 * The item that player is currently selling to shop,
	 * used for the sell x option.
	 */
	private Item itemToSell;
	
	/**
	 * The player's current dialogue.
	 */
	private Dialogue dialogue;
	
	/**
	 * The player's mob slayer assignment.
	 */
	private Mob slayerAssignment;
	
	/**
	 * The game object that is being interacted by the character.
	 */
	private GameObject interactingGameObject;
	
	/**
	 * The player's current clan.
	 */
	private ClanChat clan;
	
	/**
	 * The player's clan chat message color.
	 */
	private MessageColor clanChatMessageColor = MessageColor.BLUE;
	
	/**
	 * The player's bonus manager.
	 */
	private final BonusManager bonusManager = new BonusManager();
	
	/**
	 * The combat attributes used in PvP and PvM.
	 */
	private final CombatAttributes combatAttributes = new CombatAttributes();
	
	/**
	 * The local players in the game character's region.
	 */
	private final List<Player> localPlayers = new ArrayList<Player>();
	
	/**
	 * The local mobs in the game character's region.
	 */
	private final List<Mob> localMobs = new ArrayList<Mob>();
	
	public boolean isChangingRegion() {
		return changingRegion;
	}

	public GameCharacterFields setRegionChange(boolean changingRegion) {
		this.changingRegion = changingRegion;
		return this;
	}
	
	public boolean isDead() {
		return dead;
	}

	public GameCharacterFields setDead(boolean dead) {
		this.dead = dead;
		return this;
	}
	
	public boolean isTeleporting() {
		return teleporting;
	}

	public GameCharacterFields setTeleporting(boolean teleporting) {
		this.teleporting = teleporting;
		return this;
	}

	public int getRunEnergy() {
		return runEnergy;
	}

	public GameCharacterFields setRunEnergy(int runEnergy) {
		this.runEnergy = runEnergy;
		return this;
	}
	
	public String getForcedChat() {
		return forcedChat;
	}

	public GameCharacterFields setForcedChat(String forcedChat) {
		this.forcedChat = forcedChat;
		return this;
	}
	
	public boolean isUsingQuickPrayer() {
		return usingQuickPrayer;
	}

	public GameCharacterFields setUsingQuickPrayer(boolean usingQuickPrayer) {
		this.usingQuickPrayer = usingQuickPrayer;
		return this;
	}
	
	public boolean isOverheadPrayerCap() {
		return overheadPrayerCap;
	}

	public GameCharacterFields setOverheadPrayerCap(boolean overheadPrayerCap) {
		this.overheadPrayerCap = overheadPrayerCap;
		return this;
	}

	public String[] getNotes() {
		return notes;
	}

	public GameCharacterFields setNotes(String[] notes) {
		this.notes = notes;
		return this;
	}
	
	public GameCharacterFields setNote(int index, String note) {
		this.notes[index] = note;
		return this;
	}
	
	public Item getItemToSell() {
		return itemToSell;
	}

	public GameCharacterFields setItemToSell(Item itemToSell) {
		this.itemToSell = itemToSell;
		return this;
	}
	
	public Shop getShop() {
		return shop;
	}

	public GameCharacterFields setShop(Shop shop) {
		this.shop = shop;
		return this;
	}

	public int getTotalExperienceOnCounter() {
		return totalExperienceOnCounter;
	}

	public GameCharacterFields setTotalExperienceOnCounter(int totalExperienceOnCounter) {
		this.totalExperienceOnCounter = totalExperienceOnCounter;
		return this;
	}
	
	public GameObject getInteractingGameObject() {
		return interactingGameObject;
	}

	public GameCharacterFields setInteractingGameObject(GameObject interactingGameObject) {
		this.interactingGameObject = interactingGameObject;
		return this;
	}
	
	public MagicSpellBook getSpellbook() {
		return spellbook;
	}

	public GameCharacterFields setSpellbook(MagicSpellBook spellbook) {
		this.spellbook = spellbook;
		return this;
	}

	public PrayerBook getPrayerBook() {
		return prayerBook;
	}

	public GameCharacterFields setPrayerBook(PrayerBook prayerBook) {
		this.prayerBook = prayerBook;
		return this;
	}

	public boolean[] getPrayerActive() {
		return prayerActive;
	}

	public GameCharacterFields setPrayerActive(boolean[] prayerActive) {
		this.prayerActive = prayerActive;
		return this;
	}
	
	public GameCharacterFields setPrayerActive(int id, boolean prayerActive) {
		this.prayerActive[id] = prayerActive;
		return this;
	}
	
	public GameCharacterFields setQuickPrayer(int index, boolean quickPrayer) {
		this.quickPrayer[index] = quickPrayer;
		return this;
	}
	
	public GameCharacterFields setQuickPrayer(boolean[] quickPrayer) {
		this.quickPrayer = quickPrayer;
		return this;
	}
	
	public boolean[] getQuickCurse() {
		return quickCurse;
	}
	
	public GameCharacterFields setQuickCurse(int index, boolean quickCurse) {
		this.quickCurse[index] = quickCurse;
		return this;
	}
	
	public GameCharacterFields setQuickCurse(boolean[] quickCurse) {
		this.quickCurse = quickCurse;
		return this;
	}
	
	public Position getTeleportPosition() {
		return teleportPosition;
	}

	public GameCharacterFields setTeleportPosition(Position teleportPosition) {
		this.teleportPosition = teleportPosition;
		return this;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public GameCharacterFields setInterfaceId(int interfaceId) {
		this.interfaceId = interfaceId;
		return this;
	}
	
	public Dialogue getDialogue() {
		return dialogue;
	}
	
	public GameCharacterFields setDialogue(Dialogue dialogue) {
		this.dialogue = dialogue;
		return this;
	}
	
	public long getFoodDelay() {
		return foodDelay;
	}

	public GameCharacterFields setFoodDelay(long foodDelay) {
		this.foodDelay = foodDelay;
		return this;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public long getGraphicDelay() {
		return graphicDelay;
	}

	public GameCharacterFields setGraphicDelay(long graphicDelay) {
		this.graphicDelay = graphicDelay;
		return this;
	}

	public boolean isBanking() {
		return banking;
	}
	
	public GameCharacterFields setBanking(boolean banking) {
		this.banking = banking;
		return this;
	}
	
	public int getClientSize() {
		return clientSize;
	}

	public GameCharacterFields setClientSize(int clientSize) {
		this.clientSize = clientSize;
		return this;
	}
	
	public int getNpcTransformationId() {
		return npcTransformationId;
	}

	public GameCharacterFields setMobTransformationId(int npcTransformationId) {
		this.npcTransformationId = npcTransformationId;
		return this;
	}
	
	public Mob getSlayerAssignment() {
		return slayerAssignment;
	}
	
	public GameCharacterFields setSlayerAssignment(Mob slayerAssignment) {
		this.slayerAssignment = slayerAssignment;
		return this;
	}

	public BonusManager getBonusManager() {
		return bonusManager;
	}
	
	public CombatAttributes getCombatAttributes() {
		return combatAttributes;
	}
	
	public ClanChat getClanChat() {
		return clan;
	}
	
	public GameCharacterFields setClanChat(ClanChat clan) {
		this.clan = clan;
		return this;
	}
	
	public MessageColor getClanChatMessageColor() {
		return clanChatMessageColor;
	}
	
	public GameCharacterFields setClanChatMessageColor(MessageColor clanChatMessageColor) {
		this.clanChatMessageColor = clanChatMessageColor;
		return this;
	}

	public List<Player> getLocalPlayers() {
		return localPlayers;
	}

	public List<Mob> getLocalMobs() {
		return localMobs;
	}

	public int getPoisonDamage() {
		return poisonDamage;
	}

	public GameCharacterFields setPoisonDamage(int poisonDamage) {
		this.poisonDamage = poisonDamage;
		return this;
	}

	public boolean isRecoveringRunEnergy() {
		return recoveringRunEnergy;
	}

	public void setRecoveringRunEnergy(boolean recoveringRunEnergy) {
		this.recoveringRunEnergy = recoveringRunEnergy;
	}
}
