package src.interfaces;

import src.JagexArchive;
import src.RSFontSystem;

/**
 * This class manages all hard-coded interfaces, will use until I make my own
 * RSInterface packer/unpacker.
 * 
 * @author relex lawl
 */
public class Interfaces extends Extras {

	static RSFontSystem[] fonts;
	
	static JagexArchive archive;

	public static void loadInterfaces(JagexArchive archive, RSFontSystem[] fonts) {
		Interfaces.fonts = fonts;
		Interfaces.archive = archive;
		CachePackedInterfaces.init();
		equipmentScreen();
		//weaponTab();
		/*bonusXP();
		weaponTab();
		fightPits();
		squealOfFortune();
		configureLunar();
		constructLunar();
		equipmentScreen();
		equipmentTab();
		itemsOnDeath();
		itemsOnDeathDATA();*/
		/*friendsTab();
		ignoreTab();
		clanChatTab(archive);
	/*	// fistOfGuthixNotAllowed(fonts);
		// fistOfGuthixRank(fonts);
		// fistOfGuthixInGame(fonts);
		skillsTab();
		skillLevelUp();
		// summoningTab(fonts);
		// petSummoningTab(fonts);
		// barbarianAssaultAttackInformation(fonts);
		bobInterface();
		newTrade();*/
		//prayerTab();
		//cursesTab();
		//clanChatMessageColorTab();
	}

	@SuppressWarnings("unused")
	private static void newTrade() {
		RSInterface Interface = addInterface(3323);
		setChildren(19, Interface);
		addSprite(3324, 0, "trade_tab/trade", true);
		addHover(3442, 3, 0, 3325, 0, "trade_tab/exit", 17, 17, "Close Window");
		addHovered(3325, 1, "trade_tab/exit", 17, 17, 3326);
		addText(3417, "Trading With:", 0xFF981F, true, true, 52, fonts, 2);
		addText(3418, "Trader's Offer", 0xFF981F, false, true, 52, fonts, 1);
		addText(3419, "Your Offer", 0xFF981F, false, true, 52, fonts, 1);
		addText(3421, "Accept", 0x00C000, true, true, 52, fonts, 1);
		addText(3423, "Decline", 0xC00000, true, true, 52, fonts, 1);

		addText(3431, "Waiting For Other Player", 0xFFFFFF, true, true, 52,
				fonts, 1);
		addText(23504,
				"Wealth transfer: 2147,000,000 coins' worth to Zezimablud12",
				0xB9B855, true, true, -1, fonts, 0);
		addText(23505, "1 has\\n 28 free\\n inventory slots.", 0xFF981F, true,
				true, -1, fonts, 0);

		addText(23506,
				"Wealth transfer: 2147,000,000 coins' worth to Zezimablud12",
				0xB9B855, false, true, -1, fonts, 0);
		addText(23507, "Wealth transfer: 2147,000,000 coins' worth to me",
				0xB9B855, false, true, -1, fonts, 0);

		addHover(3420, 1, 0, 3327, 5, "trade_tab/trade", 65, 32, "Accept");
		addHovered(3327, 2, "trade_tab/trade", 65, 32, 3328);
		addHover(3422, 3, 0, 3329, 5, "trade_tab/trade", 65, 32, "Decline");
		addHovered(3329, 2, "trade_tab/trade", 65, 32, 3330);
		setBounds(3324, 0, 16, 0, Interface);
		setBounds(3442, 485, 24, 1, Interface);
		setBounds(3325, 485, 24, 2, Interface);
		setBounds(3417, 258, 25, 3, Interface);
		setBounds(3418, 355, 51, 4, Interface);
		setBounds(3419, 68, 51, 5, Interface);
		setBounds(3420, 223, 120, 6, Interface);
		setBounds(3327, 223, 120, 7, Interface);
		setBounds(3422, 223, 160, 8, Interface);
		setBounds(3329, 223, 160, 9, Interface);
		setBounds(3421, 256, 127, 10, Interface);
		setBounds(3423, 256, 167, 11, Interface);
		setBounds(3431, 256, 272, 12, Interface);
		setBounds(3415, 12, 64, 13, Interface);
		setBounds(3416, 321, 67, 14, Interface);

		setBounds(23505, 256, 67, 16, Interface);

		setBounds(23504, 255, 310, 15, Interface);
		setBounds(23506, 20, 310, 17, Interface);
		setBounds(23507, 380, 310, 18, Interface);

		Interface = addInterface(3443);
		setChildren(15, Interface);
		addSprite(3444, 1, "trade_tab/trade", true);
		addButton(3546, 2, "trade_tab/trade", 63, 24, "Accept", 1);
		addButton(3548, 2, "trade_tab/trade", 63, 24, "Decline", 3);
		addText(3547, "Accept", 0x00C000, true, true, 52, fonts, 1);
		addText(3549, "Decline", 0xC00000, true, true, 52, fonts, 1);
		addText(3450, "Trading With:", 0x00FFFF, true, true, 52, fonts, 2);
		addText(3451, "Yourself", 0x00FFFF, true, true, 52, fonts, 2);
		setBounds(3444, 12, 20, 0, Interface);
		setBounds(3442, 470, 32, 1, Interface);
		setBounds(3325, 470, 32, 2, Interface);
		setBounds(3535, 130, 28, 3, Interface);
		setBounds(3536, 105, 47, 4, Interface);
		setBounds(3546, 189, 295, 5, Interface);
		setBounds(3548, 258, 295, 6, Interface);
		setBounds(3547, 220, 299, 7, Interface);
		setBounds(3549, 288, 299, 8, Interface);
		setBounds(3557, 71, 87, 9, Interface);
		setBounds(3558, 315, 87, 10, Interface);
		setBounds(3533, 64, 70, 11, Interface);
		setBounds(3534, 297, 70, 12, Interface);
		setBounds(3450, 95, 289, 13, Interface);
		setBounds(3451, 95, 304, 14, Interface);
	}

	public static void addBobStorage(int index) {
		RSInterface rsi = interfaceCache[index] = new RSInterface();
		rsi.actions = new String[10];
		rsi.spritesX = new int[20];
		rsi.inventoryAmount = new int[30];
		rsi.inventory = new int[30];
		rsi.spritesY = new int[20];
		rsi.children = new int[0];
		rsi.childX = new int[0];
		rsi.childY = new int[0];
		rsi.actions[0] = "Take 1";
		rsi.actions[1] = "Take 5";
		rsi.actions[2] = "Take 10";
		rsi.actions[3] = "Take All";
		rsi.actions[4] = "Take X";
		rsi.centerText = true;
		rsi.aBoolean227 = false;
		rsi.aBoolean235 = false;
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.aBoolean259 = true;
		rsi.shadeText = false;
		rsi.invSpritePadX = 23;
		rsi.invSpritePadY = 22;
		rsi.height = 5;
		rsi.width = 6;
		rsi.parentId = 2702;
		rsi.id = 2700;
		rsi.type = 2;
	}

	@SuppressWarnings("unused")
	private static void bobInterface() {
		RSInterface rsi = addInterface(2700);
		addSprite(2701, 1, "summoning/bob/store", true);
		addBobStorage(2702);
		addHoverButton(2703, "summoning/bob/store", 3, 21, 21, "Close", 250,
				2704, 3);
		addHoveredButton(2704, "summoning/bob/store", 4, 21, 21, 2705);
		addHoverButton(2706, "summoning/bob/store", 2, 21, 21, "Take BoB", -1,
				2707, 1);
		rsi.totalChildren(5);
		rsi.child(0, 2701, 90, 14);
		rsi.child(1, 2702, 100, 56);
		rsi.child(2, 2703, 431, 23);
		rsi.child(3, 2704, 431, 23);
		rsi.child(4, 2706, 427, 285);
	}

	@SuppressWarnings("unused")
	private static void summoningTab() {
		final String dir = "summoning/tab/sprite";
		RSInterface rsi = addInterface(18017);
		addButton(18018, 0, dir, 143, 13, "Cast special", 1);
		addText(18019, "S P E C I A L  M O V E", fonts, 0, WHITE_TEXT, false,
				false);
		addSprite(18020, 1, dir, true);
		addFamiliarHead(18021, 75, 50, 875);
		addSprite(18022, 2, dir, true);
		addConfigButton(18023, 18017, 4, 3, dir, 30, 31, "Cast special", 0, 1,
				330);
		addText(18024, "0", fonts, 0, ORANGE_TEXT, false, true);
		addSprite(18025, 5, dir, true);
		addConfigButton(18026, 18017, 7, 6, dir, 29, 39, "Attack", 0, 1, 333);
		addSprite(18027, 8, dir, true);
		addText(18028, "None", fonts, 2, ORANGE_TEXT, true, false);
		addHoverButton(18029, dir, 9, 38, 38, "Withdraw BoB", -1, 18030, 1);
		addHoveredButton(18030, dir, 10, 38, 38, 18031);
		addHoverButton(18032, dir, 11, 38, 38, "Renew familiar", -1, 18033, 1);
		addHoveredButton(18033, dir, 12, 38, 38, 18034);
		addHoverButton(18035, dir, 13, 38, 38, "Call follower", -1, 18036, 1);
		addHoveredButton(18036, dir, 14, 38, 38, 18037);
		addHoverButton(18038, dir, 15, 38, 38, "Dismiss familiar", -1, 18039, 1);
		addHoveredButton(18039, dir, 16, 38, 38, 18040);
		addSprite(18041, 17, dir, true);
		addSprite(18042, 18, dir, true);
		addText(18043, "35.30", fonts, 0, GREY_TEXT, false, true);
		addSprite(18044, 19, dir, true);
		addText(18045, "63/69", fonts, 0, GREY_TEXT, false, true);
		setChildren(24, rsi);
		setBounds(18018, 23, 10, 0, rsi);
		setBounds(18019, 43, 12, 1, rsi);
		setBounds(18020, 10, 32, 2, rsi);
		setBounds(18021, 63, 60, 3, rsi);
		setBounds(18022, 11, 32, 4, rsi);
		setBounds(18023, 14, 35, 5, rsi);
		setBounds(18024, 25, 69, 6, rsi);
		setBounds(18025, 138, 32, 7, rsi);
		setBounds(18026, 143, 36, 8, rsi);
		setBounds(18027, 12, 144, 9, rsi);
		setBounds(18028, 93, 146, 10, rsi);
		setBounds(18029, 23, 168, 11, rsi);
		setBounds(18030, 23, 168, 12, rsi);
		setBounds(18032, 75, 168, 13, rsi);
		setBounds(18033, 75, 168, 14, rsi);
		setBounds(18035, 23, 213, 15, rsi);
		setBounds(18036, 23, 213, 16, rsi);
		setBounds(18038, 75, 213, 17, rsi);
		setBounds(18039, 75, 213, 18, rsi);
		setBounds(18041, 130, 168, 19, rsi);
		setBounds(18042, 153, 170, 20, rsi);
		setBounds(18043, 148, 198, 21, rsi);
		setBounds(18044, 149, 213, 22, rsi);
		setBounds(18045, 145, 241, 23, rsi);
	}

	@SuppressWarnings("unused")
	private static void petSummoningTab() {
		final String dir = "summoning/tab/sprite";
		RSInterface rsi = addInterface(19017);
		addSprite(19018, 1, dir, true);
		addFamiliarHead(19019, 75, 50, 900);
		addSprite(19020, 8, dir, true);
		addText(19021, "None", fonts, 2, ORANGE_TEXT, true, false);
		addHoverButton(19022, dir, 13, 38, 38, "Call follower", -1, 19023, 1);
		addHoveredButton(19023, dir, 14, 38, 38, 19024);
		addHoverButton(19025, dir, 15, 38, 38, "Dismiss familiar", -1, 19026, 1);
		addHoveredButton(19026, dir, 16, 38, 38, 19027);
		addSprite(19028, 17, dir, true);
		addSprite(19029, 20, dir, true);
		addText(19030, "0%", fonts, 0, GREY_TEXT, false, true);
		addSprite(19031, 21, dir, true);
		addText(19032, "0%", fonts, 0, WHITE_TEXT, false, true);
		setChildren(13, rsi);
		setBounds(19018, 10, 32, 0, rsi);
		setBounds(19019, 65, 65, 1, rsi);
		setBounds(19020, 12, 145, 2, rsi);
		setBounds(19021, 93, 147, 3, rsi);
		setBounds(19022, 23, 213, 4, rsi);
		setBounds(19023, 23, 213, 5, rsi);
		setBounds(19025, 75, 213, 6, rsi);
		setBounds(19026, 75, 213, 7, rsi);
		setBounds(19028, 130, 168, 8, rsi);
		setBounds(19029, 148, 170, 9, rsi);
		setBounds(19030, 152, 198, 10, rsi);
		setBounds(19031, 149, 220, 11, rsi);
		setBounds(19032, 155, 241, 12, rsi);
	}

	private static void addFamiliarHead(int interfaceID, int width, int height,
			int zoom) {
		RSInterface rsi = addInterface(interfaceID);
		rsi.type = 6;
		rsi.mediaType = 2;
		rsi.mediaId = 4000;
		rsi.modelZoom = zoom;
		rsi.modelRotation1 = 40; // up/down
		rsi.modelRotation2 = 1800; // right/left
		rsi.height = height;
		rsi.width = width;
	}

	/*
	 * private static void barbarianAssaultAttackInformation() { RSInterface rsi
	 * = addInterface(19909); setChildren(8, rsi); addText(19910, "Wave: 1",
	 * fonts, 2, 0xff9040, false, true); addSprite(19911, 0,
	 * "Interfaces/BarbarianAssault/SPRITE"); addText(19912,
	 * "  Controled/\\nBronze/Wind", fonts, 2, 0xff9040, false, true);
	 * addSprite(19913, 1, "Interfaces/BarbarianAssault/SPRITE"); addText(19914,
	 * "Green egg", fonts, 2, 0xff9040, false, true); addSprite(19915, 2,
	 * "Interfaces/BarbarianAssault/SPRITE"); addText(19916, "Collector", fonts,
	 * 2, 0xff9040, false, true); addSprite(19917, 3,
	 * "Interfaces/BarbarianAssault/SPRITE"); setBounds(19910, 430, 5, 0, rsi);
	 * //wave setBounds(19911, 492, 7, 1, rsi); //wave sprite setBounds(19912,
	 * 410, 25, 2, rsi); //attack style setBounds(19913, 495, 30, 3, rsi);
	 * //attack style sprite setBounds(19914, 425, 50, 4, rsi); //egg
	 * setBounds(19915, 495, 50, 5, rsi); //egg sprite setBounds(19916, 430, 65,
	 * 6, rsi); //role setBounds(19917, 493, 70, 7, rsi); //role sprite }
	 */

	public static void friendsTab() {
		RSInterface tab = addInterface(5065);
		RSInterface list = interfaceCache[5066];
		addSprite(16126, 4, "relations_tab/sprite", true);
		addSprite(16127, 1, "relations_tab/sprite", true);
		addText(5067, "Friends List", fonts, 1, 0xff9933, true, true);
		addText(5070, "World 1", fonts, 0, 0xff9933, false, true);
		addText(5071, "", fonts, 0, 0xff9933, false, true);
		addHoverButton(5068, "relations_tab/sprite", 6, 29, 29, "Add Name",
				201, 5072, 1);
		addHoveredButton(5072, "relations_tab/sprite", 7, 29, 29, 5073);
		addHoverButton(5069, "relations_tab/sprite", 9, 29, 29, "Delete Name",
				202, 5074, 1);
		addHoveredButton(5074, "relations_tab/sprite", 10, 29, 29, 5075);
		addHoverButton(5076, "relations_tab/sprite", 15, 215, 32,
				"Toggle Friends", 0, 5077, 1);
		addHoveredButton(5077, "relations_tab/sprite", 16, 215, 32, 5078);
		addHoverButton(5079, "relations_tab/sprite", 17, 215, 32,
				"Toggle Ignored", 0, 5080, 1);
		addHoveredButton(5080, "relations_tab/sprite", 18, 215, 32, 5081);
		tab.totalChildren(15);
		tab.child(0, 16127, 0, 40);
		tab.child(1, 5067, 92, 5);
		tab.child(2, 16126, 0, 40);
		tab.child(3, 5066, 0, 42);
		tab.child(4, 16126, 0, 231);
		tab.child(5, 5068, 5, 240);
		tab.child(6, 5072, 4, 240);
		tab.child(7, 5069, 25, 240);
		tab.child(8, 5074, 24, 240);
		tab.child(9, 5076, 150, 236);
		tab.child(10, 5077, 150, 237);
		tab.child(11, 5079, 170, 236);
		tab.child(12, 5080, 170, 237);
		tab.child(13, 5070, 70, 25);
		tab.child(14, 5071, 106, 237);
		list.height = 189;
		list.width = 174;
		list.scrollMax = 200;
		for (int id = 5092, i = 0; id <= 5191 && i <= 99; id++, i++) {
			list.children[i] = id;
			list.childX[i] = 3;
			list.childY[i] = list.childY[i] - 7;
		}
		for (int id = 5192, i = 100; id <= 5291 && i <= 199; id++, i++) {
			list.children[i] = id;
			list.childX[i] = 131;
			list.childY[i] = list.childY[i] - 7;
		}
	}

	public static void ignoreTab() {
		RSInterface tab = addInterface(5715);
		RSInterface list = interfaceCache[5716];
		addText(5717, "Ignore List", fonts, 1, 0xff9933, true, true);
		addText(5720, "World 1", fonts, 0, 0xff9933, false, true);
		addText(5721, "", fonts, 0, 0xff9933, false, true);
		addHoverButton(5718, "relations_tab/sprite", 11, 29, 29, "Add Name",
				501, 5722, 1);
		addHoveredButton(5722, "relations_tab/sprite", 12, 29, 29, 5723);
		addHoverButton(5719, "relations_tab/sprite", 13, 29, 29, "Delete Name",
				502, 5724, 1);
		addHoveredButton(5724, "relations_tab/sprite", 14, 29, 29, 5725);
		tab.totalChildren(15);
		tab.child(0, 5717, 92, 5);
		tab.child(1, 16127, 0, 40);
		tab.child(2, 16126, 0, 40);
		tab.child(3, 5716, 0, 42);
		tab.child(4, 16126, 0, 231);
		tab.child(5, 5718, 5, 240);
		tab.child(6, 5722, 4, 240);
		tab.child(7, 5719, 25, 240);
		tab.child(8, 5724, 24, 240);
		tab.child(9, 5076, 150, 236);
		tab.child(10, 5077, 150, 237);
		tab.child(11, 5079, 170, 236);
		tab.child(12, 5080, 170, 237);
		tab.child(13, 5720, 70, 25);
		tab.child(14, 5721, 108, 237);
		list.height = 189;
		list.width = 174;
		list.scrollMax = 200;
		for (int id = 5742, i = 0; id <= 5841 && i <= 99; id++, i++) {
			list.children[i] = id;
			list.childX[i] = 3;
			list.childY[i] = list.childY[i] - 7;
		}
	}

	@SuppressWarnings("unused")
	private static void skillsTab() {
		RSInterface Interface = addInterface(31110);
		setChildren(104, Interface);

		addButton(31111, 2, "skills_tab/skill", "View @or1@Attack @whi@guide",
				31112, 1);
		addTooltipBox(31112, "EXP: 14000000");
		addText(31114, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31115, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31116, 22, "skills_tab/skill",
				"View @or1@Strength @whi@guide", 31117, 1);
		addTooltipBox(31117, "EXP: 14000000");
		addText(31119, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31120, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31121, 6, "skills_tab/skill", "View @or1@Defence @whi@guide",
				31122, 1);
		addTooltipBox(31122, "EXP: 14000000");
		addText(31124, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31125, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31126, 18, "skills_tab/skill", "View @or1@Ranged @whi@guide",
				31127, 1);
		addTooltipBox(31127, "EXP: 14000000");
		addText(31129, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31130, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31131, 17, "skills_tab/skill", "View @or1@Prayer @whi@guide",
				31132, 1);
		addTooltipBox(31132, "EXP: 14000000");
		addText(31134, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31135, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31136, 15, "skills_tab/skill", "View @or1@Magic @whi@guide",
				31137, 1);
		addTooltipBox(31137, "EXP: 14000000");
		addText(31139, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31140, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31141, 19, "skills_tab/skill",
				"View @or1@Runecrafting @whi@guide", 31142, 1);
		addTooltipBox(31142, "EXP: 14000000");
		addText(31144, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31145, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31146, 3, "skills_tab/skill",
				"View @or1@Construction @whi@guide", 31147, 1);
		addTooltipBox(31147, "EXP: 14000000");
		addText(31149, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31150, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31151, 7, "skills_tab/skill",
				"View @or1@Dungeoneering @whi@guide", 31152, 1);
		addTooltipBox(31152, "EXP: 14000000");
		addText(31154, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31155, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31156, 13, "skills_tab/skill",
				"View @or1@Hitpoints @whi@guide", 31157, 1);
		addTooltipBox(31157, "EXP: 14000000");
		addText(31159, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31160, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31161, 1, "skills_tab/skill", "View @or1@Agility @whi@guide",
				31162, 1);
		addTooltipBox(31162, "EXP: 14000000");
		addText(31164, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31165, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31166, 12, "skills_tab/skill",
				"View @or1@Herblore @whi@guide", 31167, 1);
		addTooltipBox(31167, "EXP: 14000000");
		addText(31169, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31170, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31171, 24, "skills_tab/skill",
				"View @or1@Thieving @whi@guide", 31172, 1);
		addTooltipBox(31172, "EXP: 14000000");
		addText(31174, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31175, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31176, 5, "skills_tab/skill",
				"View @or1@Crafting @whi@guide", 31177, 1);
		addTooltipBox(31177, "EXP: 14000000");
		addText(31179, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31180, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31181, 11, "skills_tab/skill",
				"View @or1@Fletching @whi@guide", 31182, 1);
		addTooltipBox(31182, "EXP: 14000000");
		addText(31184, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31185, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31186, 20, "skills_tab/skill", "View @or1@Slayer @whi@guide",
				31187, 1);
		addTooltipBox(31187, "EXP: 14000000");
		addText(31189, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31190, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31191, 14, "skills_tab/skill", "View @or1@Hunter @whi@guide",
				31192, 1);
		addTooltipBox(31192, "EXP: 14000000");
		addText(31194, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31195, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31196, 0, "skills_tab/total",
				"View @or1@Total Level @whi@guide", 31197, 1);
		addTooltipBox(31197, "Total EXP: 350000000");
		addText(31199, "Total Level:", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31200, "2475", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31201, 16, "skills_tab/skill", "View @or1@Mining @whi@guide",
				31202, 1);
		addTooltipBox(31202, "EXP: 14000000");
		addText(31204, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31205, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31206, 21, "skills_tab/skill",
				"View @or1@Smithing @whi@guide", 31207, 1);
		addTooltipBox(31207, "EXP: 14000000");
		addText(31209, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31210, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31211, 10, "skills_tab/skill",
				"View @or1@Fishing @whi@guide", 31212, 1);
		addTooltipBox(31212, "EXP: 14000000");
		addText(31214, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31215, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31216, 4, "skills_tab/skill", "View @or1@Cooking @whi@guide",
				31217, 1);
		addTooltipBox(31217, "EXP: 14000000");
		addText(31219, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31220, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31221, 9, "skills_tab/skill",
				"View @or1@Firemaking @whi@guide", 31222, 1);
		addTooltipBox(31222, "EXP: 14000000");
		addText(31224, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31225, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31226, 0, "skills_tab/skill",
				"View @or1@Woodcutting @whi@guide", 31227, 1);
		addTooltipBox(31227, "EXP: 14000000");
		addText(31229, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31230, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31231, 8, "skills_tab/skill", "View @or1@Farming @whi@guide",
				31232, 1);
		addTooltipBox(31232, "EXP: 14000000");
		addText(31234, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31235, "99", 0xFFEE33, false, true, 52, fonts, 0);

		addButton(31236, 23, "skills_tab/skill",
				"View @or1@Summoning @whi@guide", 31237, 1);
		addTooltipBox(31237, "EXP: 14000000");
		addText(31239, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31240, "99", 0xFFEE33, false, true, 52, fonts, 0);

		setBounds(31111, 3, 3, 100, Interface);// Button
		setBounds(31114, 31, 6, 101, Interface);// Fake lvl
		setBounds(31115, 43, 18, 102, Interface);// Real lvl
		setBounds(31112, 3, 31, 103, Interface);// Yellow box

		setBounds(31116, 3, 31, 96, Interface);// Button
		setBounds(31119, 31, 34, 97, Interface);// Fake lvl
		setBounds(31120, 43, 46, 98, Interface);// Real lvl
		setBounds(31117, 3, 59, 99, Interface);// Yellow box

		setBounds(31121, 3, 59, 92, Interface);// Button
		setBounds(31124, 31, 62, 93, Interface);// Fake lvl
		setBounds(31125, 43, 74, 94, Interface);// Real lvl
		setBounds(31122, 3, 87, 95, Interface);// Yellow box

		setBounds(31126, 3, 87, 88, Interface);// Button
		setBounds(31129, 31, 90, 89, Interface);// Fake lvl
		setBounds(31130, 43, 102, 90, Interface);// Real lvl
		setBounds(31127, 3, 115, 91, Interface);// Yellow box

		setBounds(31131, 3, 115, 84, Interface);// Button
		setBounds(31134, 31, 118, 85, Interface);// Fake lvl
		setBounds(31135, 43, 130, 86, Interface);// Real lvl
		setBounds(31132, 3, 143, 87, Interface);// Yellow box

		setBounds(31136, 3, 143, 80, Interface);// Button
		setBounds(31139, 31, 146, 81, Interface);// Fake lvl
		setBounds(31140, 43, 158, 82, Interface);// Real lvl
		setBounds(31137, 3, 171, 83, Interface);// Yellow box

		setBounds(31141, 3, 171, 76, Interface);// Button
		setBounds(31144, 31, 174, 77, Interface);// Fake lvl
		setBounds(31145, 43, 186, 78, Interface);// Real lvl
		setBounds(31142, 3, 199, 79, Interface);// Yellow box

		setBounds(31146, 3, 199, 72, Interface);// Button
		setBounds(31149, 31, 202, 73, Interface);// Fake lvl
		setBounds(31150, 43, 214, 74, Interface);// Real lvl
		setBounds(31147, 3, 227, 75, Interface);// Yellow box

		setBounds(31151, 3, 227, 68, Interface);// Button
		setBounds(31154, 31, 230, 69, Interface);// Fake lvl
		setBounds(31155, 43, 242, 70, Interface);// Real lvl
		setBounds(31152, 3, 255, 71, Interface);// Yellow box

		setBounds(31156, 64, 3, 64, Interface);// Button
		setBounds(31159, 92, 6, 65, Interface);// Fake lvl
		setBounds(31160, 104, 18, 66, Interface);// Real lvl
		setBounds(31157, 64, 31, 67, Interface);// Yellow box

		setBounds(31161, 64, 31, 60, Interface);// Button
		setBounds(31164, 92, 34, 61, Interface);// Fake lvl
		setBounds(31165, 104, 46, 62, Interface);// Real lvl
		setBounds(31162, 64, 59, 63, Interface);// Yellow box

		setBounds(31166, 64, 59, 56, Interface);// Button
		setBounds(31169, 92, 62, 57, Interface);// Fake lvl
		setBounds(31170, 104, 74, 58, Interface);// Real lvl
		setBounds(31167, 64, 87, 59, Interface);// Yellow box

		setBounds(31171, 64, 87, 52, Interface);// Button
		setBounds(31174, 92, 90, 53, Interface);// Fake lvl
		setBounds(31175, 104, 102, 54, Interface);// Real lvl
		setBounds(31172, 64, 115, 55, Interface);// Yellow box

		setBounds(31176, 64, 115, 48, Interface);// Button
		setBounds(31179, 92, 118, 49, Interface);// Fake lvl
		setBounds(31180, 104, 130, 50, Interface);// Real lvl
		setBounds(31177, 64, 143, 51, Interface);// Yellow box

		setBounds(31181, 64, 143, 44, Interface);// Button
		setBounds(31184, 92, 146, 45, Interface);// Fake lvl
		setBounds(31185, 104, 158, 46, Interface);// Real lvl
		setBounds(31182, 64, 171, 47, Interface);// Yellow box

		setBounds(31186, 64, 171, 40, Interface);// Button
		setBounds(31189, 92, 174, 41, Interface);// Fake lvl
		setBounds(31190, 104, 186, 42, Interface);// Real lvl
		setBounds(31187, 64, 199, 43, Interface);// Yellow box

		setBounds(31191, 64, 199, 36, Interface);// Button
		setBounds(31194, 92, 202, 37, Interface);// Fake lvl
		setBounds(31195, 104, 214, 38, Interface);// Real lvl
		setBounds(31192, 64, 227, 39, Interface);// Yellow box

		setBounds(31201, 125, 3, 32, Interface);// Button
		setBounds(31204, 153, 6, 33, Interface);// Fake lvl
		setBounds(31205, 165, 18, 34, Interface);// Real lvl
		setBounds(31202, 125, 31, 35, Interface);// Yellow box

		setBounds(31206, 125, 31, 28, Interface);// Button
		setBounds(31209, 153, 34, 29, Interface);// Fake lvl
		setBounds(31210, 165, 46, 30, Interface);// Real lvl
		setBounds(31207, 125, 59, 31, Interface);// Yellow box

		setBounds(31211, 125, 59, 24, Interface);// Button
		setBounds(31214, 153, 62, 25, Interface);// Fake lvl
		setBounds(31215, 165, 74, 26, Interface);// Real lvl
		setBounds(31212, 125, 87, 27, Interface);// Yellow box

		setBounds(31216, 125, 87, 20, Interface);// Button
		setBounds(31219, 153, 90, 21, Interface);// Fake lvl
		setBounds(31220, 165, 102, 22, Interface);// Real lvl
		setBounds(31217, 125, 115, 23, Interface);// Yellow box

		setBounds(31221, 125, 115, 16, Interface);// Button
		setBounds(31224, 153, 118, 17, Interface);// Fake lvl
		setBounds(31225, 165, 130, 18, Interface);// Real lvl
		setBounds(31222, 125, 143, 19, Interface);// Yellow box

		setBounds(31226, 125, 143, 12, Interface);// Button
		setBounds(31229, 153, 146, 13, Interface);// Fake lvl
		setBounds(31230, 165, 158, 14, Interface);// Real lvl
		setBounds(31227, 125, 171, 15, Interface);// Yellow box

		setBounds(31231, 125, 171, 8, Interface);// Button
		setBounds(31234, 153, 174, 9, Interface);// Fake lvl
		setBounds(31235, 165, 186, 10, Interface);// Real lvl
		setBounds(31232, 125, 199, 11, Interface);// Yellow box

		setBounds(31236, 125, 199, 4, Interface);// Button
		setBounds(31239, 153, 202, 5, Interface);// Fake lvl
		setBounds(31240, 165, 214, 6, Interface);// Real lvl
		setBounds(31237, 125, 227, 7, Interface);// Yellow box

		setBounds(31196, 64, 227, 0, Interface);// Total level
		setBounds(31199, 105, 229, 1, Interface);// Text
		setBounds(31200, 116, 241, 2, Interface);// Lvl
		setBounds(31197, 64, 255, 3, Interface);// Yellow box
	}

	public static void skillLevelUp() {
		RSInterface attack = interfaceCache[6247];
		RSInterface defence = interfaceCache[6253];
		RSInterface str = interfaceCache[6206];
		RSInterface hits = interfaceCache[6216];
		RSInterface rng = interfaceCache[4443];
		RSInterface pray = interfaceCache[6242];
		RSInterface mage = interfaceCache[6211];
		RSInterface cook = interfaceCache[6226];
		RSInterface wood = interfaceCache[4272];
		RSInterface flet = interfaceCache[6231];
		RSInterface fish = interfaceCache[6258];
		RSInterface fire = interfaceCache[4282];
		RSInterface craf = interfaceCache[6263];
		RSInterface smit = interfaceCache[6221];
		RSInterface mine = interfaceCache[4416];
		RSInterface herb = interfaceCache[6237];
		RSInterface agil = interfaceCache[4277];
		RSInterface thie = interfaceCache[4261];
		RSInterface slay = interfaceCache[12122];
		RSInterface farm = interfaceCache[5267];
		RSInterface rune = interfaceCache[4267];
		RSInterface cons = interfaceCache[7267];
		RSInterface hunt = interfaceCache[8267];
		RSInterface summ = addInterface(9267);
		RSInterface dung = addInterface(10267);
		addSprite(17878, 0, "skill_chat/skill", true);
		addSprite(17879, 1, "skill_chat/skill", true);
		addSprite(17880, 2, "skill_chat/skill", true);
		addSprite(17881, 3, "skill_chat/skill", true);
		addSprite(17882, 4, "skill_chat/skill", true);
		addSprite(17883, 5, "skill_chat/skill", true);
		addSprite(17884, 6, "skill_chat/skill", true);
		addSprite(17885, 7, "skill_chat/skill", true);
		addSprite(17886, 8, "skill_chat/skill", true);
		addSprite(17887, 9, "skill_chat/skill", true);
		addSprite(17888, 10, "skill_chat/skill", true);
		addSprite(17889, 11, "skill_chat/skill", true);
		addSprite(17890, 12, "skill_chat/skill", true);
		addSprite(17891, 13, "skill_chat/skill", true);
		addSprite(17892, 14, "skill_chat/skill", true);
		addSprite(17893, 15, "skill_chat/skill", true);
		addSprite(17894, 16, "skill_chat/skill", true);
		addSprite(17895, 17, "skill_chat/skill", true);
		addSprite(17896, 18, "skill_chat/skill", true);
		addSprite(11897, 19, "skill_chat/skill", true);
		addSprite(17898, 20, "skill_chat/skill", true);
		addSprite(17899, 21, "skill_chat/skill", true);
		addSprite(17900, 22, "skill_chat/skill", true);
		addSprite(17901, 23, "skill_chat/skill", true);
		addSprite(17902, 24, "skill_chat/skill", true);
		setChildren(4, attack);
		setBounds(17878, 20, 30, 0, attack);
		setBounds(4268, 80, 15, 1, attack);
		setBounds(4269, 80, 45, 2, attack);
		setBounds(358, 95, 75, 3, attack);
		setChildren(4, defence);
		setBounds(17879, 20, 30, 0, defence);
		setBounds(4268, 80, 15, 1, defence);
		setBounds(4269, 80, 45, 2, defence);
		setBounds(358, 95, 75, 3, defence);
		setChildren(4, str);
		setBounds(17880, 20, 30, 0, str);
		setBounds(4268, 80, 15, 1, str);
		setBounds(4269, 80, 45, 2, str);
		setBounds(358, 95, 75, 3, str);
		setChildren(4, hits);
		setBounds(17881, 20, 30, 0, hits);
		setBounds(4268, 80, 15, 1, hits);
		setBounds(4269, 80, 45, 2, hits);
		setBounds(358, 95, 75, 3, hits);
		setChildren(4, rng);
		setBounds(17882, 20, 30, 0, rng);
		setBounds(4268, 80, 15, 1, rng);
		setBounds(4269, 80, 45, 2, rng);
		setBounds(358, 95, 75, 3, rng);
		setChildren(4, pray);
		setBounds(17883, 20, 30, 0, pray);
		setBounds(4268, 80, 15, 1, pray);
		setBounds(4269, 80, 45, 2, pray);
		setBounds(358, 95, 75, 3, pray);
		setChildren(4, mage);
		setBounds(17884, 20, 30, 0, mage);
		setBounds(4268, 80, 15, 1, mage);
		setBounds(4269, 80, 45, 2, mage);
		setBounds(358, 95, 75, 3, mage);
		setChildren(4, cook);
		setBounds(17885, 20, 30, 0, cook);
		setBounds(4268, 80, 15, 1, cook);
		setBounds(4269, 80, 45, 2, cook);
		setBounds(358, 95, 75, 3, cook);
		setChildren(4, wood);
		setBounds(17886, 20, 30, 0, wood);
		setBounds(4268, 80, 15, 1, wood);
		setBounds(4269, 80, 45, 2, wood);
		setBounds(358, 95, 75, 3, wood);
		setChildren(4, flet);
		setBounds(17887, 20, 30, 0, flet);
		setBounds(4268, 80, 15, 1, flet);
		setBounds(4269, 80, 45, 2, flet);
		setBounds(358, 95, 75, 3, flet);
		setChildren(4, fish);
		setBounds(17888, 20, 30, 0, fish);
		setBounds(4268, 80, 15, 1, fish);
		setBounds(4269, 80, 45, 2, fish);
		setBounds(358, 95, 75, 3, fish);
		setChildren(4, fire);
		setBounds(17889, 20, 30, 0, fire);
		setBounds(4268, 80, 15, 1, fire);
		setBounds(4269, 80, 45, 2, fire);
		setBounds(358, 95, 75, 3, fire);
		setChildren(4, craf);
		setBounds(17890, 20, 30, 0, craf);
		setBounds(4268, 80, 15, 1, craf);
		setBounds(4269, 80, 45, 2, craf);
		setBounds(358, 95, 75, 3, craf);
		setChildren(4, smit);
		setBounds(17891, 20, 30, 0, smit);
		setBounds(4268, 80, 15, 1, smit);
		setBounds(4269, 80, 45, 2, smit);
		setBounds(358, 95, 75, 3, smit);
		setChildren(4, mine);
		setBounds(17892, 20, 30, 0, mine);
		setBounds(4268, 80, 15, 1, mine);
		setBounds(4269, 80, 45, 2, mine);
		setBounds(358, 95, 75, 3, mine);
		setChildren(4, herb);
		setBounds(17893, 20, 30, 0, herb);
		setBounds(4268, 80, 15, 1, herb);
		setBounds(4269, 80, 45, 2, herb);
		setBounds(358, 95, 75, 3, herb);
		setChildren(4, agil);
		setBounds(17894, 20, 30, 0, agil);
		setBounds(4268, 80, 15, 1, agil);
		setBounds(4269, 80, 45, 2, agil);
		setBounds(358, 95, 75, 3, agil);
		setChildren(4, thie);
		setBounds(17895, 20, 30, 0, thie);
		setBounds(4268, 80, 15, 1, thie);
		setBounds(4269, 80, 45, 2, thie);
		setBounds(358, 95, 75, 3, thie);
		setChildren(4, slay);
		setBounds(17896, 20, 30, 0, slay);
		setBounds(4268, 80, 15, 1, slay);
		setBounds(4269, 80, 45, 2, slay);
		setBounds(358, 95, 75, 3, slay);
		setChildren(3, farm);
		setBounds(4268, 80, 15, 0, farm);
		setBounds(4269, 80, 45, 1, farm);
		setBounds(358, 95, 75, 2, farm);
		setChildren(4, rune);
		setBounds(17898, 20, 30, 0, rune);
		setBounds(4268, 80, 15, 1, rune);
		setBounds(4269, 80, 45, 2, rune);
		setBounds(358, 95, 75, 3, rune);
		setChildren(3, cons);
		setBounds(4268, 80, 15, 0, cons);
		setBounds(4269, 80, 45, 1, cons);
		setBounds(358, 95, 75, 2, cons);
		setChildren(3, hunt);
		setBounds(4268, 80, 15, 0, hunt);
		setBounds(4269, 80, 45, 1, hunt);
		setBounds(358, 95, 75, 2, hunt);
		setChildren(4, summ);
		setBounds(17901, 20, 30, 0, summ);
		setBounds(4268, 80, 15, 1, summ);
		setBounds(4269, 80, 45, 2, summ);
		setBounds(358, 95, 75, 3, summ);
		setChildren(4, dung);
		setBounds(17902, 20, 30, 0, dung);
		setBounds(4268, 80, 15, 1, dung);
		setBounds(4269, 80, 45, 2, dung);
		setBounds(358, 95, 75, 3, dung);
	}

	public static void Pestpanel() {
		RSInterface RSinterface = addInterface(21119);
		addText(21120, "What", 0x999999, false, true, 52, fonts, 1);
		addText(21121, "What", 0x33cc00, false, true, 52, fonts, 1);
		addText(21122, "(Need 5 to 25 players)", 0xFFcc33, false, true, 52,
				fonts, 1);
		addText(21123, "Points", 0x33ccff, false, true, 52, fonts, 1);

		int last = 4;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21120, 15, 12, 0, RSinterface);
		setBounds(21121, 15, 30, 1, RSinterface);
		setBounds(21122, 15, 48, 2, RSinterface);
		setBounds(21123, 15, 66, 3, RSinterface);
	}

	public static void Pestpanel2() {
		RSInterface RSinterface = addInterface(21100);
		addSprite(21101, 0, "PEST1", "Pest Control");
		addSprite(21102, 1, "PEST1", "Pest Control");
		addSprite(21103, 2, "PEST1", "Pest Control");
		addSprite(21104, 3, "PEST1", "Pest Control");
		addSprite(21105, 4, "PEST1", "Pest Control");
		addSprite(21106, 5, "PEST1", "Pest Control");
		addText(21107, "", 0xCC00CC, false, true, 52, fonts, 1);
		addText(21108, "", 0x0000FF, false, true, 52, fonts, 1);
		addText(21109, "", 0xFFFF44, false, true, 52, fonts, 1);
		addText(21110, "", 0xCC0000, false, true, 52, fonts, 1);
		addText(21111, "250", 0x99FF33, false, true, 52, fonts, 1);// w purp
		addText(21112, "250", 0x99FF33, false, true, 52, fonts, 1);// e blue
		addText(21113, "250", 0x99FF33, false, true, 52, fonts, 1);// se yel
		addText(21114, "250", 0x99FF33, false, true, 52, fonts, 1);// sw red
		addText(21115, "200", 0x99FF33, false, true, 52, fonts, 1);// attacks
		addText(21116, "0", 0x99FF33, false, true, 52, fonts, 1);// knights hp
		addText(21117, "Time Remaining:", 0xFFFFFF, false, true, 52, fonts, 0);
		addText(21118, "", 0xFFFFFF, false, true, 52, fonts, 0);
		int last = 18;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21101, 361, 26, 0, RSinterface);
		setBounds(21102, 396, 26, 1, RSinterface);
		setBounds(21103, 436, 26, 2, RSinterface);
		setBounds(21104, 474, 26, 3, RSinterface);
		setBounds(21105, 3, 21, 4, RSinterface);
		setBounds(21106, 3, 50, 5, RSinterface);
		setBounds(21107, 371, 60, 6, RSinterface);
		setBounds(21108, 409, 60, 7, RSinterface);
		setBounds(21109, 443, 60, 8, RSinterface);
		setBounds(21110, 479, 60, 9, RSinterface);
		setBounds(21111, 362, 10, 10, RSinterface);
		setBounds(21112, 398, 10, 11, RSinterface);
		setBounds(21113, 436, 10, 12, RSinterface);
		setBounds(21114, 475, 10, 13, RSinterface);
		setBounds(21115, 32, 32, 14, RSinterface);
		setBounds(21116, 32, 62, 15, RSinterface);
		setBounds(21117, 8, 88, 16, RSinterface);
		setBounds(21118, 87, 88, 17, RSinterface);
	}

	/**
	 * Requesting a battle with someone
	 * 
	 * @param fonts
	 */
	public static void clanBattleRequest() {
		int id = 9000;
		RSInterface rsi = addInterface(id);
		addSprite(id + 1, 0, "CLAN", "clan");
		addText(id + 2, "PlayerName is inviting your clan to a battle!", fonts,
				3, 0xff9b00, false, true);
		addHoverButton(id + 3, "CLAN", "clan", 6, 72, 32, "Accept", -1, id + 4,
				1);
		addHoveredButton(id + 4, "CLAN", "clan", 7, 72, 32, id + 5);
		addText(id + 6, "Accept", fonts, 1, 0xff9b00, false, true);
		addHoverButton(id + 7, "CLAN", "clan", 6, 72, 32, "Decline", -1,
				id + 8, 1);
		addHoveredButton(id + 8, "CLAN", "clan", 7, 72, 32, id + 9);
		addText(id + 10, "Decline", fonts, 1, 0xff9b00, false, true);

		int[][] children = { { id + 1, 16, 254 }, { id + 2, 90, 254 },
				{ id + 3, 145, 283 }, { id + 4, 145, 283 },
				{ id + 6, 164, 290 }, { id + 7, 250, 283 },
				{ id + 8, 250, 283 }, { id + 10, 269, 290 } };

		rsi.totalChildren(children.length);
		for (int i = 0; i < children.length; i++) {
			rsi.child(i, children[i][0], children[i][1], children[i][2]);
		}
	}

	/**
	 * In Game Countdown interface
	 * 
	 * @param fonts
	 */
	public static void clanGameWait() {
		int id = 3500;
		RSInterface rsi = addInterface(id);
		addSprite(id + 1, 0, "CLANW", "ClanWars");
		addText(id + 2, "Keldagrim:", fonts, 1, 0xff9b00, false, true);
		addText(id + 3, "Opposing:", fonts, 1, 0xff9b00, false, true);
		addText(id + 4, "Countdown to Battle:", fonts, 1, 0xff9b00, false, true);
		addText(id + 5, "Players: 55", fonts, 1, 0xff9b00, false, true);
		addText(id + 6, "Players: 48", fonts, 1, 0xff9b00, false, true);
		addText(id + 7, "2:00", fonts, 1, 0xff9b00, false, true);

		int[][] children = { { id + 1, 341, 3 }, { id + 2, 350, 8 },
				{ id + 3, 436, 8 }, { id + 4, 370, 47 }, { id + 5, 350, 22 },
				{ id + 6, 436, 22 }, { id + 7, 410, 60 } };

		rsi.totalChildren(children.length);
		for (int i = 0; i < children.length; i++) {
			rsi.child(i, children[i][0], children[i][1], children[i][2]);
		}
	}

	/**
	 * In Game Interface
	 * 
	 * @param fonts
	 */
	public static void clanGame() {
		int id = 3400;
		RSInterface rsi = addInterface(id);
		addTransparentSprite(id + 1, 1, "ClanWars/CLANW");
		addText(id + 2, "Keldagrim:", fonts, 1, 0xff9b00, false, true);
		addText(id + 3, "Opposing:", fonts, 1, 0xff9b00, false, true);
		addText(id + 4, "Kills: 5", fonts, 1, 0xff9b00, false, true);
		addText(id + 5, "Players: 55", fonts, 1, 0xff9b00, false, true);
		addText(id + 6, "Players: 48", fonts, 1, 0xff9b00, false, true);
		addText(id + 7, "Kills: 30", fonts, 1, 0xff9b00, false, true);

		int[][] children = { { id + 1, 341, 3 }, { id + 2, 350, 8 },
				{ id + 3, 436, 8 }, { id + 4, 350, 36 }, { id + 5, 350, 22 },
				{ id + 6, 436, 22 }, { id + 7, 436, 36 } };

		rsi.totalChildren(children.length);
		for (int i = 0; i < children.length; i++) {
			rsi.child(i, children[i][0], children[i][1], children[i][2]);
		}
	}

	/**
	 * BonusXp Interface
	 * 
	 * @param fonts
	 */
	public static void bonusXP() {
		int id = 10000;
		RSInterface rsi = addInterface(id);
		addText(id + 2, "Time Left:", fonts, 0, 0xff9b00, true, true);
		addText(id + 3, "Bonus:", fonts, 0, 0xff9b00, false, true);
		addText(id + 4, "XP Gain:", fonts, 0, 0xff9b00, false, true);
		addHoverButton(id + 5, "BonusXP", "BonusXP", 0, 167, 83, "Hide", -1,
				id + 6, 1);
		addHoveredButton(id + 6, "BonusXP", "BonusXP", 1, 167, 83, id + 7);

		int[][] children = { { id + 5, 0, 0 }, { id + 6, 0, 0 },
				{ id + 2, 50, 30 }, { id + 3, 10, 47 }, { id + 4, 10, 64 } };

		rsi.totalChildren(children.length);
		for (int i = 0; i < children.length; i++) {
			rsi.child(i, children[i][0], children[i][1], children[i][2]);
		}
	}

	/**
	 * Main Clan Chat interface
	 * 
	 * @param fonts
	 */
	@SuppressWarnings("unused")
	public static void clanChatTab(JagexArchive archive) {
		RSInterface tab = addInterface(18128);
		String directory = "clan_chat/clan";
		/*InterfaceManager.addHardcodedHoverButton(18129, 0, "Join Clan", 1, directory, 5, 1, 550);
		InterfaceManager.addHardcodedHoverButton(18132, 2, "Leave Clan", 3, directory, 5, 1, -1);
		InterfaceManager.addHardcodedHoverButton(18251, 4, "Clan Chat Settings", 5, directory, 5, 1, -1);
		//InterfaceManager.addHardcodedSprite(18137, 6, directory);
		InterfaceManager.addText(18138, "Clan Chat", 0xff9b00, true, true, fonts[1]);
		InterfaceManager.addText(18139, "", 0xffb00, false, true, fonts[0]);
		InterfaceManager.addText(18140, "Talking in: ", 0xff9b00, false, true, fonts[0]);
		InterfaceManager.addText(18250, "Owner: ", 0xff9b00, false, true, fonts[0]);
		InterfaceManager.addHardcodedButton(18255, 7, "Toggle Lootshare", directory);*/
		addSprite(16126, 4, "SPRITE", "clan");
		addSprite(18137, 6, "chat", "clan");
		tab.totalChildren(11);
		tab.child(0, 16126, 0, 236);
		tab.child(1, 16126, 0, 62);
		tab.child(2, 18137, 0, 62);
		tab.child(3, 18143, 0, 62);
		tab.child(4, 18129, 8, 239);
		tab.child(5, 18132, 25, 239);
		tab.child(6, 18138, 95, 1);
		tab.child(7, 18140, 10, 23);
		tab.child(8, 18250, 10, 36);
		tab.child(9, 18251, 42, 239);
		tab.child(10, 18255, 150, 23);
		/* Text area */
		RSInterface list = addInterface(18143);
		list.totalChildren(100);
		for (int i = 18144; i <= 18244; i++) {
			addText(i, "", fonts, 0, 0xffffff, false, true);
		}
		for (int id = 18144, i = 0; id <= 18243 && i <= 99; id++, i++) {
			list.children[i] = id;
			list.childX[i] = 5;
			for (int id2 = 18144, i2 = 1; id2 <= 18243 && i2 <= 99; id2++, i2++) {
				list.childY[0] = 2;
				list.childY[i2] = list.childY[i2 - 1] + 14;
			}
		}
		list.height = 174;
		list.width = 174;
		list.scrollMax = 1405;
	}

	/**
	 * Clan Chat settings(Owner settings)
	 * 
	 * @param fonts
	 */
	public static void clanChatSettings() {
		int id = 6000;
		RSInterface rsi = addInterface(id);
		addSprite(id + 1, 7, "chat", "clan");
		addHoverButton(id + 2, "chat", "clan", 8, 150, 25, "Promote/Demote",
				-1, id + 3, 1);
		addHoveredButton(id + 3, "chat", "clan", 9, 150, 25, id + 4);
		addText(id + 5, "Promote/Demote", fonts, 2, 0xff9b00, true, false);
		addHoverButton(id + 6, "chat", "clan", 8, 150, 25, "Ban/Unban", -1,
				id + 7, 1);
		addHoveredButton(id + 7, "chat", "clan", 9, 150, 25, id + 8);
		addText(id + 9, "Ban/Unban", fonts, 2, 0xff9b00, true, false);
		addText(id + 10, "Clan Settings", fonts, 3, 0xff9b00, true, true);
		addHoverButton(id + 11, "chat", "clan", 8, 150, 25, "Kick", -1,
				id + 12, 1);
		addHoveredButton(id + 12, "chat", "clan", 9, 150, 25, id + 13);
		addText(id + 14, "Kick", fonts, 2, 0xff9b00, true, false);
		addHoverButton(id + 15, "chat", "clan", 8, 150, 25, "Invite", -1,
				id + 16, 1);
		addHoveredButton(id + 16, "chat", "clan", 9, 150, 25, id + 17);
		addText(id + 18, "Invite", fonts, 2, 0xff9b00, true, false);
		addHoverButton(id + 19, "chat", "clan", 8, 150, 25, "Invite", -1,
				id + 20, 1);
		addHoveredButton(id + 20, "chat", "clan", 9, 150, 25, id + 21);
		addText(id + 22, "Lock Clan", fonts, 2, 0xff9b00, true, false);
		addHoverButton(id + 23, "chat", "clan", 8, 150, 25, "Invite", -1,
				id + 24, 1);
		addHoveredButton(id + 24, "chat", "clan", 9, 150, 25, id + 25);
		addText(id + 26, "Lock Chat", fonts, 2, 0xff9b00, true, false);
		addHoverButton(id + 27, "chat", "clan", 8, 150, 25, "Invite", -1,
				id + 28, 1);
		addHoveredButton(id + 28, "chat", "clan", 9, 150, 25, id + 29);
		addText(id + 30, "Change Name", fonts, 2, 0xff9b00, true, false);

		int[][] children = { { id + 1, 0, 0 }, { id + 2, 18, 180 },
				{ id + 3, 18, 180 }, { id + 5, 95, 185 }, { id + 6, 18, 210 },
				{ id + 7, 18, 210 }, { id + 9, 95, 215 }, { id + 10, 95, 1 },
				{ id + 11, 18, 150 }, { id + 12, 18, 150 },
				{ id + 14, 95, 155 }, { id + 15, 18, 120 },
				{ id + 16, 18, 12 }, { id + 18, 95, 125 }, { id + 19, 18, 90 },
				{ id + 20, 18, 90 }, { id + 22, 95, 95 }, { id + 23, 18, 60 },
				{ id + 24, 18, 60 }, { id + 26, 95, 65 }, { id + 27, 18, 30 },
				{ id + 28, 18, 30 }, { id + 30, 95, 35 } };

		rsi.totalChildren(children.length);
		for (int i = 0; i < children.length; i++) {
			rsi.child(i, children[i][0], children[i][1], children[i][2]);
		}
	}

	/**
	 * Clan Moderator Settings interface
	 * 
	 * @param fonts
	 */
	public static void clanModeratorSettings() {
		int id = 5000;
		RSInterface rsi = addInterface(id);
		addSprite(id + 1, 7, "chat", "clan");
		addHoverButton(id + 2, "chat", "clan", 8, 150, 25, "Kick", -1, id + 3,
				1);
		addHoveredButton(id + 3, "chat", "clan", 9, 150, 25, id + 4);
		addText(id + 5, "Kick", fonts, 2, 0xff9b00, true, false);
		addHoverButton(id + 6, "chat", "clan", 8, 150, 25, "Ban/Unban", -1,
				id + 7, 1);
		addHoveredButton(id + 7, "chat", "clan", 9, 150, 25, id + 8);
		addText(id + 9, "Ban/Unban", fonts, 2, 0xff9b00, true, false);
		addText(id + 10, "Moderator Panel", fonts, 3, 0xff9b00, true, true);
		addText(id + 11, "Because you are a", fonts, 3, 0xff9b00, true, true);
		addText(id + 12, "clan moderator you", fonts, 3, 0xff9b00, true, true);
		addText(id + 13, "have the ability to", fonts, 3, 0xff9b00, true, true);
		addText(id + 14, "ban, kick and invite", fonts, 3, 0xff9b00, true, true);
		addText(id + 15, "members in the chat.", fonts, 3, 0xff9b00, true, true);
		addHoverButton(id + 16, "chat", "clan", 8, 150, 25, "Invite", -1,
				id + 17, 1);
		addHoveredButton(id + 17, "chat", "clan", 9, 150, 25, id + 18);
		addText(id + 19, "Invite", fonts, 2, 0xff9b00, true, false);

		int[][] children = { { id + 1, 0, 0 }, { id + 2, 18, 180 },
				{ id + 3, 18, 180 }, { id + 5, 95, 185 }, { id + 6, 18, 210 },
				{ id + 7, 18, 210 }, { id + 9, 95, 215 }, { id + 10, 95, 1 },
				{ id + 11, 95, 53 }, { id + 12, 95, 68 }, { id + 14, 95, 98 },
				{ id + 15, 95, 113 }, { id + 16, 18, 150 },
				{ id + 17, 18, 150 }, { id + 19, 95, 155 } };

		rsi.totalChildren(children.length);
		for (int i = 0; i < children.length; i++) {
			rsi.child(i, children[i][0], children[i][1], children[i][2]);
		}
	}

	/**
	 * Equipment sidebar tab
	 * 
	 * @param wid
	 */
	public static void equipmentTab() {
		RSInterface Interface = interfaceCache[1644];
		newInterface(21338);
		newInterface(21344);
		newInterface(21342);
		newInterface(21341);
		newInterface(21340);
		setBounds(27650, 0, 0, 26, Interface);
		Interface = addInterface(27650);
		addHoverButton(27651, "CUSTOM", "Equipment", 1, 40, 40,
				"Show Equipment Screen", 0, 27652, 1);
		addHoveredButton(27652, "CUSTOM", "Equipment", 5, 40, 40, 27653);
		addHoverButton(27654, "CUSTOM", "Equipment", 3, 40, 40,
				"Price Checker", 0, 27655, 1);
		addHoveredButton(27655, "CUSTOM", "Equipment", 6, 40, 40, 27656);
		addHoverButton(27657, "CUSTOM", "Equipment", 2, 40, 40,
				"Items Kept on Death", 0, 27658, 1);
		addHoveredButton(27658, "CUSTOM", "Equipment", 4, 40, 40, 27659);
		addHoverButton(27667, "CUSTOM", "Equipment", 9, 40, 40,
				"Experience Toggle", 0, 27668, 1);
		addHoveredButton(27668, "CUSTOM", "Equipment", 10, 40, 40, 27669);
		setChildren(8, Interface);
		setBounds(27651, 5, 210, 0, Interface);
		setBounds(27652, 5, 210, 1, Interface);
		setBounds(27654, 52, 210, 2, Interface);
		setBounds(27655, 52, 210, 3, Interface);
		setBounds(27657, 99, 210, 4, Interface);
		setBounds(27658, 99, 210, 5, Interface);
		setBounds(27667, 146, 210, 6, Interface);
		setBounds(27668, 146, 210, 7, Interface);
	}

	/**
	 * The actual items on death interface
	 * 
	 * @param wid
	 */
	public static void itemsOnDeath() {
		RSInterface rsinterface = addInterface(17100);
		addSprite(17101, 2, 2);
		addHover(17102, 3, 0, 10601, 1, "Equipment/SPRITE", 17, 17,
				"Close Window");
		addHovered(10601, 3, "Equipment/SPRITE", 17, 17, 10602);
		addText(17103, "Items kept on death", fonts, 2, 0xff981f, false, false);
		addText(17104, "Items I will keep...", fonts, 1, 0xff981f, false, false);
		addText(17105, "Items I will lose...", fonts, 1, 0xff981f, false, false);
		addText(17106, "Info", fonts, 1, 0xff981f, false, false);
		addText(17107, "RoyalePS", fonts, 1, 0xffcc33, false, false);
		addText(17108, "", fonts, 1, 0xffcc33, false, false);
		rsinterface.scrollMax = 0;
		rsinterface.hidden = false;
		rsinterface.children = new int[12];
		rsinterface.childX = new int[12];
		rsinterface.childY = new int[12];

		rsinterface.children[0] = 17101;
		rsinterface.childX[0] = 7;
		rsinterface.childY[0] = 8;
		rsinterface.children[1] = 17102;
		rsinterface.childX[1] = 480;
		rsinterface.childY[1] = 17;
		rsinterface.children[2] = 17103;
		rsinterface.childX[2] = 185;
		rsinterface.childY[2] = 18;
		rsinterface.children[3] = 17104;
		rsinterface.childX[3] = 22;
		rsinterface.childY[3] = 50;
		rsinterface.children[4] = 17105;
		rsinterface.childX[4] = 22;
		rsinterface.childY[4] = 110;
		rsinterface.children[5] = 17106;
		rsinterface.childX[5] = 347;
		rsinterface.childY[5] = 47;
		rsinterface.children[6] = 17107;
		rsinterface.childX[6] = 349;
		rsinterface.childY[6] = 270;
		rsinterface.children[7] = 17108;
		rsinterface.childX[7] = 398;
		rsinterface.childY[7] = 298;
		rsinterface.children[8] = 17115;
		rsinterface.childX[8] = 348;
		rsinterface.childY[8] = 64;
		rsinterface.children[9] = 10494;
		rsinterface.childX[9] = 26;
		rsinterface.childY[9] = 74;
		rsinterface.children[10] = 10600;
		rsinterface.childX[10] = 26;
		rsinterface.childY[10] = 133;
		rsinterface.children[11] = 10601;
		rsinterface.childX[11] = 480;
		rsinterface.childY[11] = 17;
	}

	/**
	 * Data for the items on death interface
	 * 
	 * @param wid
	 */
	public static void itemsOnDeathDATA() {
		RSInterface rsinterface = addInterface(17115);
		addText(17109, "a", fonts, 0, 0xff981f, false, false);
		addText(17110, "b", fonts, 0, 0xff981f, false, false);
		addText(17111, "c", fonts, 0, 0xff981f, false, false);
		addText(17112, "d", fonts, 0, 0xff981f, false, false);
		addText(17113, "e", fonts, 0, 0xff981f, false, false);
		addText(17114, "f", fonts, 0, 0xff981f, false, false);
		addText(17117, "g", fonts, 0, 0xff981f, false, false);
		addText(17118, "h", fonts, 0, 0xff981f, false, false);
		addText(17119, "i", fonts, 0, 0xff981f, false, false);
		addText(17120, "j", fonts, 0, 0xff981f, false, false);
		addText(17121, "k", fonts, 0, 0xff981f, false, false);
		addText(17122, "l", fonts, 0, 0xff981f, false, false);
		addText(17123, "m", fonts, 0, 0xff981f, false, false);
		addText(17124, "n", fonts, 0, 0xff981f, false, false);
		addText(17125, "o", fonts, 0, 0xff981f, false, false);
		addText(17126, "p", fonts, 0, 0xff981f, false, false);
		addText(17127, "q", fonts, 0, 0xff981f, false, false);
		addText(17128, "r", fonts, 0, 0xff981f, false, false);
		addText(17129, "s", fonts, 0, 0xff981f, false, false);
		addText(17130, "t", fonts, 0, 0xff981f, false, false);
		rsinterface.parentId = 17115;
		rsinterface.id = 17115;
		rsinterface.type = 0;
		rsinterface.actionType = 0;
		rsinterface.contentType = 0;
		rsinterface.width = 130;
		rsinterface.height = 197;
		rsinterface.opacity = 0;
		rsinterface.hoverId = -1;
		rsinterface.scrollMax = 280;
		rsinterface.children = new int[20];
		rsinterface.childX = new int[20];
		rsinterface.childY = new int[20];
		rsinterface.children[0] = 17109;
		rsinterface.childX[0] = 0;
		rsinterface.childY[0] = 0;
		rsinterface.children[1] = 17110;
		rsinterface.childX[1] = 0;
		rsinterface.childY[1] = 12;
		rsinterface.children[2] = 17111;
		rsinterface.childX[2] = 0;
		rsinterface.childY[2] = 24;
		rsinterface.children[3] = 17112;
		rsinterface.childX[3] = 0;
		rsinterface.childY[3] = 36;
		rsinterface.children[4] = 17113;
		rsinterface.childX[4] = 0;
		rsinterface.childY[4] = 48;
		rsinterface.children[5] = 17114;
		rsinterface.childX[5] = 0;
		rsinterface.childY[5] = 60;
		rsinterface.children[6] = 17117;
		rsinterface.childX[6] = 0;
		rsinterface.childY[6] = 72;
		rsinterface.children[7] = 17118;
		rsinterface.childX[7] = 0;
		rsinterface.childY[7] = 84;
		rsinterface.children[8] = 17119;
		rsinterface.childX[8] = 0;
		rsinterface.childY[8] = 96;
		rsinterface.children[9] = 17120;
		rsinterface.childX[9] = 0;
		rsinterface.childY[9] = 108;
		rsinterface.children[10] = 17121;
		rsinterface.childX[10] = 0;
		rsinterface.childY[10] = 120;
		rsinterface.children[11] = 17122;
		rsinterface.childX[11] = 0;
		rsinterface.childY[11] = 132;
		rsinterface.children[12] = 17123;
		rsinterface.childX[12] = 0;
		rsinterface.childY[12] = 144;
		rsinterface.children[13] = 17124;
		rsinterface.childX[13] = 0;
		rsinterface.childY[13] = 156;
		rsinterface.children[14] = 17125;
		rsinterface.childX[14] = 0;
		rsinterface.childY[14] = 168;
		rsinterface.children[15] = 17126;
		rsinterface.childX[15] = 0;
		rsinterface.childY[15] = 180;
		rsinterface.children[16] = 17127;
		rsinterface.childX[16] = 0;
		rsinterface.childY[16] = 192;
		rsinterface.children[17] = 17128;
		rsinterface.childX[17] = 0;
		rsinterface.childY[17] = 204;
		rsinterface.children[18] = 17129;
		rsinterface.childX[18] = 0;
		rsinterface.childY[18] = 216;
		rsinterface.children[19] = 17130;
		rsinterface.childX[19] = 0;
		rsinterface.childY[19] = 228;
	}

	/**
	 * Equipment screen telling you your bonuses
	 */
	public static void equipmentScreen() {
		RSInterface tab = addInterface(21172);
		addSprite(21173, 1, "bg", "Equipment");
		addHover(15210, 3, 0, 15211, 1, "Equipment/SPRITE", 17, 17,
				"Close Window");
		addHovered(15211, 3, "Equipment/SPRITE", 17, 17, 15212);
		addText(15111, "", fonts, 2, 0xe4a146, false, true);
		int rofl = 3;
		addText(15112, "Attack bonuses", fonts, 2, 0xFF8900, false, true);
		addText(15113, "Defence bonuses", fonts, 2, 0xFF8900, false, true);
		addText(15114, "Other bonuses", fonts, 2, 0xFF8900, false, true);
		addText(16522, "Summoning: +0", fonts, 1, 0xFF8900, false, true);// 19148
		addText(16523, "Absorb Melee: +0%", fonts, 1, 0xFF9200, false, true);// 19149
		addText(16524, "Absorb Magic: +0%", fonts, 1, 0xFF9200, false, true);// 19150
		addText(16525, "Absorb Ranged: +0%", fonts, 1, 0xFF9200, false, true);// 19151
		addText(16526, "Ranged Strength: +0", fonts, 1, 0xFF9200, false, true);// 19152
		addText(16527, "Magic Damage: +0%", fonts, 1, 0xFF9200, false, true);// 19153
		for (int i = 1675; i <= 1684; i++) {
			textSize(i, fonts, 1);
		}
		textSize(1686, fonts, 1);
		textSize(1687, fonts, 1);
		addChar(15125);
		tab.totalChildren(50);
		tab.child(0, 21173, 15, 5);
		tab.child(1, 15210, 476, 8);
		tab.child(2, 15211, 476, 8);
		tab.child(3, 15111, 14, 30);
		int Child = 4;
		int Y = 45;
		tab.child(16, 15112, 24, 30 - rofl);
		for (int i = 1675; i <= 1679; i++) {
			tab.child(Child, i, 29, Y - rofl);
			Child++;
			Y += 14;
		}
		int edit = 7 + rofl;
		tab.child(18, 15113, 24, 122 - edit);
		tab.child(9, 1680, 29, 137 - edit - 2);
		tab.child(10, 1681, 29, 153 - edit - 3);
		tab.child(11, 1682, 29, 168 - edit - 3);
		tab.child(12, 1683, 29, 183 - edit - 3);
		tab.child(13, 1684, 29, 197 - edit - 3);
		tab.child(44, 16522, 29, 211 - edit - 3);
		tab.child(45, 16523, 29, 225 - edit - 3);
		tab.child(46, 16524, 29, 239 - edit - 3);
		tab.child(47, 16525, 29, 253 - edit - 3);
		/* bottom */
		int edit2 = 33 - rofl, edit3 = 2;
		tab.child(19, 15114, 24, 223 + edit2);
		tab.child(14, 1686, 29, 262 - 24 + edit2 - edit3);
		tab.child(17, 16526, 29, 276 - 24 + edit2 - edit3);
		tab.child(48, 1687, 29, 290 - 24 + edit2 - edit3);
		tab.child(49, 16527, 29, 304 - 24 + edit2 - edit3);
		tab.child(15, 15125, 170, 200);
		tab.child(20, 1645, 104 + 295, 149 - 52);
		tab.child(21, 1646, 399, 163);
		tab.child(22, 1647, 399, 163);
		tab.child(23, 1648, 399, 58 + 146);
		tab.child(24, 1649, 26 + 22 + 297 - 2, 110 - 44 + 118 - 13 + 5);
		tab.child(25, 1650, 321 + 22, 58 + 154);
		tab.child(26, 1651, 321 + 134, 58 + 118);
		tab.child(27, 1652, 321 + 134, 58 + 154);
		tab.child(28, 1653, 321 + 48, 58 + 81);
		tab.child(29, 1654, 321 + 107, 58 + 81);
		tab.child(30, 1655, 321 + 58, 58 + 42);
		tab.child(31, 1656, 321 + 112, 58 + 41);
		tab.child(32, 1657, 321 + 78, 58 + 4);
		tab.child(33, 1658, 321 + 37, 58 + 43);
		tab.child(34, 1659, 321 + 78, 58 + 43);
		tab.child(35, 1660, 321 + 119, 58 + 43);
		tab.child(36, 1661, 321 + 22, 58 + 82);
		tab.child(37, 1662, 321 + 78, 58 + 82);
		tab.child(38, 1663, 321 + 134, 58 + 82);
		tab.child(39, 1664, 321 + 78, 58 + 122);
		tab.child(40, 1665, 321 + 78, 58 + 162);
		tab.child(41, 1666, 321 + 22, 58 + 162);
		tab.child(42, 1667, 321 + 134, 58 + 162);
		tab.child(43, 1688, 50 + 297 - 2, 110 - 13 + 5);
		for (int i = 1675; i <= 1684; i++) {
			RSInterface rsi = interfaceCache[i];
			rsi.textColor = 0xFF9200;
			rsi.centerText = false;
		}
		for (int i = 1686; i <= 1687; i++) {
			RSInterface rsi = interfaceCache[i];
			rsi.textColor = 0xFF9200;
			rsi.centerText = false;
		}
	}

	/**
	 * Fight pits
	 * 
	 * @param fonts
	 */
	public static void fightPits() {
		int id = 3900;
		RSInterface rsi = addInterface(id);
		addSprite(id + 1, 2, "FistOfGuthix/SPRITE");
		addText(id + 2, "Game starts in: 1min", fonts, 1, 0xff9b00, false, true);
		addText(id + 3, "Players: 3", fonts, 1, 0xff9b00, false, true);

		int[][] children = { { id + 1, 335, 10 }, { id + 2, 345, 20 },
				{ id + 3, 345, 45 } };

		rsi.totalChildren(children.length);
		for (int i = 0; i < children.length; i++) {
			rsi.child(i, children[i][0], children[i][1], children[i][2]);
		}
	}

	/**
	 * Squeal of fortune main interface
	 * 
	 * @param fonts
	 */
	public static void squealOfFortune() {
		int id = 26800;
		RSInterface rsi = addInterface(id);
		addSprite(id + 1, 0, "squeal", "Squeal");
		addSprite(id + 2, 1, "squeal", "Squeal");
		addInv(id + 3, id, 2, 2);
		addInv(id + 4, id, 2, 2);
		addInv(id + 5, id, 2, 2);
		addInv(id + 6, id, 2, 2);
		addInv(id + 7, id, 2, 2);
		addInv(id + 8, id, 2, 2);
		addInv(id + 9, id, 2, 2);
		addInv(id + 10, id, 2, 2);
		addText(id + 11, "Available Spins", fonts, 3, 0xff9933, false, true);
		addText(id + 12, "0", fonts, 3, 0xff9933, false, true);
		addHoverText(id + 13, "Lets play now!", "Play", fonts, 3, 0xff9933,
				false, true, 100);
		addText(id + 14, "Premium Benefits", fonts, 3, 0xff9933, false, true);
		addText(id + 15, "Did you know that premium members", fonts, 1,
				0xff9933, false, true);
		addText(id + 16, "get two spins a day? You also have", fonts, 1,
				0xff9933, false, true);
		addHoverText(id + 17, "Click here to buy spins!.", "Buy spins", fonts,
				0, 0xff9933, false, false, 115);
		addHoverText(id + 18, "Click here to learn about premium!",
				"Premium member", fonts, 0, 0xff9933, false, false, 180);
		addText(id + 19, "the ability to buy spins for $.", fonts, 1, 0xff9933,
				false, true);
		addText(id + 20, "Squeal of Fortune", fonts, 3, 0xff9933, false, true);
		addSprite(id + 21, 2, "squeal", "Squeal");
		addText(id + 22, "Claim your prize!", fonts, 3, 0xff9933, false, true);
		addHoverText(id + 23, "Claim Item", "claim item", fonts, 1, 0xff9933,
				false, true, 60);
		addHoverText(id + 24, "Discard Item", "discard item", fonts, 1,
				0xff9933, false, true, 60);
		addInv(id + 25, id, 2, 2);

		int[][] children = { { id + 1, 6, 15 }, { id + 2, 66, 239 },
				{ id + 3, 32, 270 }, { id + 4, 82, 270 }, { id + 5, 132, 270 },
				{ id + 6, 182, 270 }, { id + 7, 232, 270 },
				{ id + 8, 282, 270 }, { id + 9, 332, 270 },
				{ id + 10, 382, 270 }, { id + 11, 30, 62 },
				{ id + 12, 67, 82 }, { id + 13, 363, 193 },
				{ id + 14, 318, 65 }, { id + 15, 278, 81 },
				{ id + 16, 278, 94 }, { id + 17, 278, 122 },
				{ id + 18, 278, 134 }, { id + 19, 278, 108 },
				{ id + 20, 198, 25 }, { id + 21, 42, 122 },
				{ id + 22, 80, 124 }, { id + 23, 128, 166 },
				{ id + 24, 128, 190 }, { id + 25, 47, 160 }, };

		rsi.totalChildren(children.length);
		for (int i = 0; i < children.length; i++) {
			rsi.child(i, children[i][0], children[i][1], children[i][2]);
		}
	}

	/**
	 * Items not allowed in FOG interface
	 * 
	 * @author Relex Lawl
	 * @param fonts
	 */
	public static void fistOfGuthixNotAllowed() {
		RSInterface rsi = addInterface(11607);
		setChildren(3, rsi);
		addSprite(11608, 0, "FistOfGuthix/SPRITE");
		addText(11609, "The following item is not allowed in the arena:",
				fonts, 1, 0xff9040, false, true);
		addText(11610, "Ectophial", fonts, 1, 0xff9040, true, true);
		setBounds(11608, 50, 290, 0, rsi);
		setBounds(11609, 140, 298, 1, rsi);
		setBounds(11610, 250, 312, 2, rsi);
	}

	/**
	 * Rating interface
	 * 
	 * @author Relex Lawl
	 * @param fonts
	 */
	public static void fistOfGuthixRank() {
		RSInterface rsi = addInterface(11694);
		setChildren(2, rsi);
		addSprite(11695, 1, "FistOfGuthix/SPRITE");
		addText(11696, "Rating: 300", fonts, 1, 0xff9040, false, true);
		setBounds(11695, 10, 30, 0, rsi);
		setBounds(11696, 40, 40, 1, rsi);
	}

	/**
	 * In game interface for FOG
	 * 
	 * @author Relex Lawl
	 * @param fonts
	 */
	public static void fistOfGuthixInGame() {
		RSInterface rsi = addInterface(11710);
		setChildren(3, rsi);
		addSprite(11711, 2, "FistOfGuthix/SPRITE");
		addText(11712, "Charges: 5000", fonts, 1, 0xff9040, false, true);
		addText(11713, "Hunted by:", fonts, 1, 0xff9040, false, true);
		setBounds(11711, 335, 10, 0, rsi);
		setBounds(11712, 345, 17, 1, rsi);
		setBounds(11713, 345, 45, 2, rsi);
	}

	public static void addRuneText(int ID, int runeAmount, int RuneID,
			RSFontSystem[] font) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentId = 1151;
		rsInterface.type = 4;
		rsInterface.actionType = 0;
		rsInterface.contentType = 0;
		rsInterface.width = 0;
		rsInterface.height = 14;
		rsInterface.opacity = 0;
		rsInterface.hoverId = -1;
		rsInterface.valueCompareType = new int[1];
		rsInterface.requiredValue = new int[1];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValue[0] = runeAmount;
		rsInterface.valueIndexArray = new int[1][4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = RuneID;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.centerText = true;
		rsInterface.textDrawingAreas = font[0];
		rsInterface.shadeText = true;
		rsInterface.disabledText = "%1/" + runeAmount + "";
		rsInterface.enabledText = "";
		rsInterface.textColor = 12582912;
		rsInterface.anInt219 = 49152;
	}

	public static void homeTeleport() {
		RSInterface RSInterface = addInterface(30000);
		RSInterface.tooltip = "Cast @gre@Lunar Home Teleport";
		RSInterface.id = 30000;
		RSInterface.parentId = 30000;
		RSInterface.type = 5;
		RSInterface.actionType = 5;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.hoverId = 30001;
		RSInterface.sprite1 = loadLunarSprite(1, "HOME");
		RSInterface.width = 20;
		RSInterface.height = 20;
		RSInterface hover = addInterface(30001);
		hover.hoverId = -1;
		hover.hidden = true;
		setChildren(1, hover);
		addLunarSprite(30002, 0, "SPRITE");
		setBounds(30002, 0, 0, 0, hover);
	}

	public static void addLunar2RunesSmallBox(int ID, int r1, int r2, int ra1,
			int ra2, int rune1, int lvl, String name, String descr, int sid,
			int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentId = 1151;
		rsInterface.type = 5;
		rsInterface.actionType = type;
		rsInterface.contentType = 0;
		rsInterface.hoverId = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast On";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.valueCompareType = new int[3];
		rsInterface.requiredValue = new int[3];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValue[0] = ra1;
		rsInterface.valueCompareType[1] = 3;
		rsInterface.requiredValue[1] = ra2;
		rsInterface.valueCompareType[2] = 3;
		rsInterface.requiredValue[2] = lvl;
		rsInterface.valueIndexArray = new int[3][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[3];
		rsInterface.valueIndexArray[2][0] = 1;
		rsInterface.valueIndexArray[2][1] = 6;
		rsInterface.valueIndexArray[2][2] = 0;
		rsInterface.sprite2 = loadLunarSprite(sid, "LUNARON");
		rsInterface.sprite1 = loadLunarSprite(sid, "LUNAROFF");
		RSInterface hover = addInterface(ID + 1);
		hover.hoverId = -1;
		hover.hidden = true;
		setChildren(7, hover);
		addLunarSprite(ID + 2, 0, "BOX");
		setBounds(ID + 2, 0, 0, 0, hover);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true,
				true, 52, 1, fonts);
		setBounds(ID + 3, 90, 4, 1, hover);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, 0, fonts);
		setBounds(ID + 4, 90, 19, 2, hover);
		setBounds(30016, 37, 35, 3, hover);// Rune
		setBounds(rune1, 112, 35, 4, hover);// Rune
		addRuneText(ID + 5, ra1 + 1, r1, fonts);
		setBounds(ID + 5, 50, 66, 5, hover);
		addRuneText(ID + 6, ra2 + 1, r2, fonts);
		setBounds(ID + 6, 123, 66, 6, hover);

	}

	public static void addLunar3RunesSmallBox(int ID, int r1, int r2, int r3,
			int ra1, int ra2, int ra3, int rune1, int rune2, int lvl,
			String name, String descr, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentId = 1151;
		rsInterface.type = 5;
		rsInterface.actionType = type;
		rsInterface.contentType = 0;
		rsInterface.hoverId = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.valueCompareType = new int[4];
		rsInterface.requiredValue = new int[4];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValue[0] = ra1;
		rsInterface.valueCompareType[1] = 3;
		rsInterface.requiredValue[1] = ra2;
		rsInterface.valueCompareType[2] = 3;
		rsInterface.requiredValue[2] = ra3;
		rsInterface.valueCompareType[3] = 3;
		rsInterface.requiredValue[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = loadLunarSprite(sid, "LUNARON");
		rsInterface.sprite1 = loadLunarSprite(sid, "LUNAROFF");
		RSInterface hover = addInterface(ID + 1);
		hover.hoverId = -1;
		hover.hidden = true;
		setChildren(9, hover);
		addLunarSprite(ID + 2, 0, "BOX");
		setBounds(ID + 2, 0, 0, 0, hover);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true,
				true, 52, 1, fonts);
		setBounds(ID + 3, 90, 4, 1, hover);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, 0, fonts);
		setBounds(ID + 4, 90, 19, 2, hover);
		setBounds(30016, 14, 35, 3, hover);
		setBounds(rune1, 74, 35, 4, hover);
		setBounds(rune2, 130, 35, 5, hover);
		addRuneText(ID + 5, ra1 + 1, r1, fonts);
		setBounds(ID + 5, 26, 66, 6, hover);
		addRuneText(ID + 6, ra2 + 1, r2, fonts);
		setBounds(ID + 6, 87, 66, 7, hover);
		addRuneText(ID + 7, ra3 + 1, r3, fonts);
		setBounds(ID + 7, 142, 66, 8, hover);
	}

	public static void addLunar3RunesBigBox(int ID, int r1, int r2, int r3,
			int ra1, int ra2, int ra3, int rune1, int rune2, int lvl,
			String name, String descr, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentId = 1151;
		rsInterface.type = 5;
		rsInterface.actionType = type;
		rsInterface.contentType = 0;
		rsInterface.hoverId = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.valueCompareType = new int[4];
		rsInterface.requiredValue = new int[4];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValue[0] = ra1;
		rsInterface.valueCompareType[1] = 3;
		rsInterface.requiredValue[1] = ra2;
		rsInterface.valueCompareType[2] = 3;
		rsInterface.requiredValue[2] = ra3;
		rsInterface.valueCompareType[3] = 3;
		rsInterface.requiredValue[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = loadLunarSprite(sid, "LUNARON");
		rsInterface.sprite1 = loadLunarSprite(sid, "LUNAROFF");
		RSInterface hover = addInterface(ID + 1);
		hover.hoverId = -1;
		hover.hidden = true;
		setChildren(9, hover);
		addLunarSprite(ID + 2, 1, "BOX");
		setBounds(ID + 2, 0, 0, 0, hover);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true,
				true, 52, 1, fonts);
		setBounds(ID + 3, 90, 4, 1, hover);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, 0, fonts);
		setBounds(ID + 4, 90, 21, 2, hover);
		setBounds(30016, 14, 48, 3, hover);
		setBounds(rune1, 74, 48, 4, hover);
		setBounds(rune2, 130, 48, 5, hover);
		addRuneText(ID + 5, ra1 + 1, r1, fonts);
		setBounds(ID + 5, 26, 79, 6, hover);
		addRuneText(ID + 6, ra2 + 1, r2, fonts);
		setBounds(ID + 6, 87, 79, 7, hover);
		addRuneText(ID + 7, ra3 + 1, r3, fonts);
		setBounds(ID + 7, 142, 79, 8, hover);
	}

	public static void addLunar3RunesLargeBox(int ID, int r1, int r2, int r3,
			int ra1, int ra2, int ra3, int rune1, int rune2, int lvl,
			String name, String descr, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentId = 1151;
		rsInterface.type = 5;
		rsInterface.actionType = type;
		rsInterface.contentType = 0;
		rsInterface.hoverId = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.valueCompareType = new int[4];
		rsInterface.requiredValue = new int[4];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValue[0] = ra1;
		rsInterface.valueCompareType[1] = 3;
		rsInterface.requiredValue[1] = ra2;
		rsInterface.valueCompareType[2] = 3;
		rsInterface.requiredValue[2] = ra3;
		rsInterface.valueCompareType[3] = 3;
		rsInterface.requiredValue[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = loadLunarSprite(sid, "LUNARON");
		rsInterface.sprite1 = loadLunarSprite(sid, "LUNAROFF");
		RSInterface hover = addInterface(ID + 1);
		hover.hidden = true;
		hover.hoverId = -1;
		setChildren(9, hover);
		addLunarSprite(ID + 2, 2, "BOX");
		setBounds(ID + 2, 0, 0, 0, hover);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true,
				true, 52, 1, fonts);
		setBounds(ID + 3, 90, 4, 1, hover);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, 0, fonts);
		setBounds(ID + 4, 90, 34, 2, hover);
		setBounds(30016, 14, 61, 3, hover);
		setBounds(rune1, 74, 61, 4, hover);
		setBounds(rune2, 130, 61, 5, hover);
		addRuneText(ID + 5, ra1 + 1, r1, fonts);
		setBounds(ID + 5, 26, 92, 6, hover);
		addRuneText(ID + 6, ra2 + 1, r2, fonts);
		setBounds(ID + 6, 87, 92, 7, hover);
		addRuneText(ID + 7, ra3 + 1, r3, fonts);
		setBounds(ID + 7, 142, 92, 8, hover);
	}

	/**
	 * Lunar rune info
	 * 
	 * @param fonts
	 */
	public static void configureLunar() {
		homeTeleport();
		drawRune(30003, 1, "Fire");
		drawRune(30004, 2, "Water");
		drawRune(30005, 3, "Air");
		drawRune(30006, 4, "Earth");
		drawRune(30007, 5, "Mind");
		drawRune(30008, 6, "Body");
		drawRune(30009, 7, "Death");
		drawRune(30010, 8, "Nature");
		drawRune(30011, 9, "Chaos");
		drawRune(30012, 10, "Law");
		drawRune(30013, 11, "Cosmic");
		drawRune(30014, 12, "Blood");
		drawRune(30015, 13, "Soul");
		drawRune(30016, 14, "Astral");
		addLunar3RunesSmallBox(30017, 9075, 554, 555, 0, 4, 3, 30003, 30004,
				64, "Bake Pie", "Bake pies without a stove", 0, 16, 2);
		addLunar2RunesSmallBox(30025, 9075, 557, 0, 7, 30006, 65, "Cure Plant",
				"Cure disease on farming patch", 1, 4, 2);
		addLunar3RunesBigBox(30032, 9075, 564, 558, 0, 0, 0, 30013, 30007, 65,
				"Monster Examine",
				"Detect the combat statistics of a\\nmonster", 2, 2, 2);
		addLunar3RunesSmallBox(30040, 9075, 564, 556, 0, 0, 1, 30013, 30005,
				66, "NPC Contact", "Speak with varied NPCs", 3, 0, 2);
		addLunar3RunesSmallBox(30048, 9075, 563, 557, 0, 0, 9, 30012, 30006,
				67, "Cure Other", "Cure poisoned players", 4, 8, 2);
		addLunar3RunesSmallBox(30056, 9075, 555, 554, 0, 2, 0, 30004, 30003,
				67, "Humidify", "fills certain vessels with water", 5, 0, 5);
		addLunar3RunesSmallBox(30064, 9075, 563, 557, 1, 0, 1, 30012, 30006,
				68, "Moonclan Teleport", "Teleports you to moonclan island", 6,
				0, 5);
		addLunar3RunesBigBox(30075, 9075, 563, 557, 1, 0, 3, 30012, 30006, 69,
				"Tele Group Moonclan",
				"Teleports players to Moonclan\\nisland", 7, 0, 5);
		addLunar3RunesSmallBox(30083, 9075, 563, 557, 1, 0, 5, 30012, 30006,
				70, "Ourania Teleport", "Teleports you to ourania rune altar",
				8, 0, 5);
		addLunar3RunesSmallBox(30091, 9075, 564, 563, 1, 1, 0, 30013, 30012,
				70, "Cure Me", "Cures Poison", 9, 0, 5);
		addLunar2RunesSmallBox(30099, 9075, 557, 1, 1, 30006, 70, "Hunter Kit",
				"Get a kit of hunting gear", 10, 0, 5);
		addLunar3RunesSmallBox(30106, 9075, 563, 555, 1, 0, 0, 30012, 30004,
				71, "Waterbirth Teleport",
				"Teleports you to Waterbirth island", 11, 0, 5);
		addLunar3RunesBigBox(30114, 9075, 563, 555, 1, 0, 4, 30012, 30004, 72,
				"Tele Group Waterbirth",
				"Teleports players to Waterbirth\\nisland", 12, 0, 5);
		addLunar3RunesSmallBox(30122, 9075, 564, 563, 1, 1, 1, 30013, 30012,
				73, "Cure Group", "Cures Poison on players", 13, 0, 5);
		addLunar3RunesBigBox(30130, 9075, 564, 559, 1, 1, 4, 30013, 30008, 74,
				"Stat Spy",
				"Cast on another player to see their\\nskill levels", 14, 8, 2);
		addLunar3RunesBigBox(30138, 9075, 563, 554, 1, 1, 2, 30012, 30003, 74,
				"Barbarian Teleport",
				"Teleports you to the Barbarian\\noutpost", 15, 0, 5);
		addLunar3RunesBigBox(30146, 9075, 563, 554, 1, 1, 5, 30012, 30003, 75,
				"Tele Group Barbarian",
				"Teleports players to the Barbarian\\noutpost", 16, 0, 5);
		addLunar3RunesSmallBox(30154, 9075, 554, 556, 1, 5, 9, 30003, 30005,
				76, "Superglass Make", "Make glass without a furnace", 17, 16,
				2);
		addLunar3RunesSmallBox(30162, 9075, 563, 555, 1, 1, 3, 30012, 30004,
				77, "Khazard Teleport", "Teleports you to Port khazard", 18, 0,
				5);
		addLunar3RunesSmallBox(30170, 9075, 563, 555, 1, 1, 7, 30012, 30004,
				78, "Tele Group Khazard", "Teleports players to Port khazard",
				19, 0, 5);
		addLunar3RunesBigBox(30178, 9075, 564, 559, 1, 0, 4, 30013, 30008, 78,
				"Dream", "Take a rest and restore hitpoints 3\\n times faster",
				20, 0, 5);
		addLunar3RunesSmallBox(30186, 9075, 557, 555, 1, 9, 4, 30006, 30004,
				79, "String Jewellery", "String amulets without wool", 21, 0, 5);
		addLunar3RunesLargeBox(30194, 9075, 557, 555, 1, 9, 9, 30006, 30004,
				80, "Stat Restore Pot\\nShare",
				"Share a potion with up to 4 nearby\\nplayers", 22, 0, 5);
		addLunar3RunesSmallBox(30202, 9075, 554, 555, 1, 6, 6, 30003, 30004,
				81, "Magic Imbue", "Combine runes without a talisman", 23, 0, 5);
		addLunar3RunesBigBox(30210, 9075, 561, 557, 2, 1, 14, 30010, 30006, 82,
				"Fertile Soil",
				"Fertilise a farming patch with super\\ncompost", 24, 4, 2);
		addLunar3RunesBigBox(30218, 9075, 557, 555, 2, 11, 9, 30006, 30004, 83,
				"Boost Potion Share",
				"Shares a potion with up to 4 nearby\\nplayers", 25, 0, 5);
		addLunar3RunesSmallBox(30226, 9075, 563, 555, 2, 2, 9, 30012, 30004,
				84, "Fishing Guild Teleport",
				"Teleports you to the fishing guild", 26, 0, 5);
		addLunar3RunesLargeBox(30234, 9075, 563, 555, 1, 2, 13, 30012, 30004,
				85, "Tele Group Fishing\\nGuild",
				"Teleports players to the Fishing\\nGuild", 27, 0, 5);
		addLunar3RunesSmallBox(30242, 9075, 557, 561, 2, 14, 0, 30006, 30010,
				85, "Plank Make", "Turn Logs into planks", 28, 16, 5);
		/******** Cut Off Limit **********/
		addLunar3RunesSmallBox(30250, 9075, 563, 555, 2, 2, 9, 30012, 30004,
				86, "Catherby Teleport", "Teleports you to Catherby", 29, 0, 5);
		addLunar3RunesSmallBox(30258, 9075, 563, 555, 2, 2, 14, 30012, 30004,
				87, "Tele Group Catherby", "Teleports players to Catherby", 30,
				0, 5);
		addLunar3RunesSmallBox(30266, 9075, 563, 555, 2, 2, 7, 30012, 30004,
				88, "Ice Plateau Teleport", "Teleports you to Ice Plateau", 31,
				0, 5);
		addLunar3RunesBigBox(30274, 9075, 563, 555, 2, 2, 15, 30012, 30004, 89,
				"Tele Group Ice\\n Plateau",
				"Teleports players to Ice Plateau", 32, 0, 5);
		addLunar3RunesBigBox(
				30282,
				9075,
				563,
				561,
				2,
				1,
				0,
				30012,
				30010,
				90,
				"Energy Transfer",
				"Spend hitpoints and SA Energy to\\n give another player hitpoints and run energy",
				33, 8, 2);
		addLunar3RunesBigBox(30290, 9075, 563, 565, 2, 2, 0, 30012, 30014, 91,
				"Heal Other",
				"Transfer up to 75% of hitpoints\\n to another player", 34, 8,
				2);
		addLunar3RunesBigBox(30298, 9075, 560, 557, 2, 1, 9, 30009, 30006, 92,
				"Vengeance Other",
				"Allows another player to rebound\\ndamage to an opponent", 35,
				8, 2);
		addLunar3RunesSmallBox(30306, 9075, 560, 557, 3, 1, 9, 30009, 30006,
				93, "Vengeance", "Rebound damage to an opponent", 36, 0, 5);
		addLunar3RunesBigBox(30314, 9075, 565, 563, 3, 2, 5, 30014, 30012, 94,
				"Heal Group", "Transfer up to 75% of hitpoints to a group", 37,
				0, 5);
		addLunar3RunesBigBox(30322, 9075, 564, 563, 2, 1, 0, 30013, 30012, 95,
				"Spellbook Swap",
				"Change to another spellbook for 1\\nspell cast", 38, 0, 5);
	}

	/**
	 * The main lunar interface
	 */
	public static void constructLunar() {
		RSInterface Interface = addInterface(29999);
		setChildren(80, Interface);
		setBounds(30000, 11, 10, 0, Interface);
		setBounds(30017, 40, 9, 1, Interface);
		setBounds(30025, 71, 12, 2, Interface);
		setBounds(30032, 103, 10, 3, Interface);
		setBounds(30040, 135, 12, 4, Interface);
		setBounds(30048, 165, 10, 5, Interface);
		setBounds(30056, 8, 38, 6, Interface);
		setBounds(30064, 39, 39, 7, Interface);
		setBounds(30075, 71, 39, 8, Interface);
		setBounds(30083, 103, 39, 9, Interface);
		setBounds(30091, 135, 39, 10, Interface);
		setBounds(30099, 165, 37, 11, Interface);
		setBounds(30106, 12, 68, 12, Interface);
		setBounds(30114, 42, 68, 13, Interface);
		setBounds(30122, 71, 68, 14, Interface);
		setBounds(30130, 103, 68, 15, Interface);
		setBounds(30138, 135, 68, 16, Interface);
		setBounds(30146, 165, 68, 17, Interface);
		setBounds(30154, 14, 97, 18, Interface);
		setBounds(30162, 42, 97, 19, Interface);
		setBounds(30170, 71, 97, 20, Interface);
		setBounds(30178, 101, 97, 21, Interface);
		setBounds(30186, 135, 98, 22, Interface);
		setBounds(30194, 168, 98, 23, Interface);
		setBounds(30202, 11, 125, 24, Interface);
		setBounds(30210, 42, 124, 25, Interface);
		setBounds(30218, 74, 125, 26, Interface);
		setBounds(30226, 103, 125, 27, Interface);
		setBounds(30234, 135, 125, 28, Interface);
		setBounds(30242, 164, 126, 29, Interface);
		setBounds(30250, 10, 155, 30, Interface);
		setBounds(30258, 42, 155, 31, Interface);
		setBounds(30266, 71, 155, 32, Interface);
		setBounds(30274, 103, 155, 33, Interface);
		setBounds(30282, 136, 155, 34, Interface);
		setBounds(30290, 165, 155, 35, Interface);
		setBounds(30298, 13, 185, 36, Interface);
		setBounds(30306, 42, 185, 37, Interface);
		setBounds(30314, 71, 184, 38, Interface);
		setBounds(30322, 104, 184, 39, Interface);
		setBounds(30001, 6, 184, 40, Interface);// hover
		setBounds(30018, 5, 176, 41, Interface);// hover
		setBounds(30026, 5, 176, 42, Interface);// hover
		setBounds(30033, 5, 163, 43, Interface);// hover
		setBounds(30041, 5, 176, 44, Interface);// hover
		setBounds(30049, 5, 176, 45, Interface);// hover
		setBounds(30057, 5, 176, 46, Interface);// hover
		setBounds(30065, 5, 176, 47, Interface);// hover
		setBounds(30076, 5, 163, 48, Interface);// hover
		setBounds(30084, 5, 176, 49, Interface);// hover
		setBounds(30092, 5, 176, 50, Interface);// hover
		setBounds(30100, 5, 176, 51, Interface);// hover
		setBounds(30107, 5, 176, 52, Interface);// hover
		setBounds(30115, 5, 163, 53, Interface);// hover
		setBounds(30123, 5, 176, 54, Interface);// hover
		setBounds(30131, 5, 163, 55, Interface);// hover
		setBounds(30139, 5, 163, 56, Interface);// hover
		setBounds(30147, 5, 163, 57, Interface);// hover
		setBounds(30155, 5, 176, 58, Interface);// hover
		setBounds(30163, 5, 176, 59, Interface);// hover
		setBounds(30171, 5, 176, 60, Interface);// hover
		setBounds(30179, 5, 163, 61, Interface);// hover
		setBounds(30187, 5, 176, 62, Interface);// hover
		setBounds(30195, 5, 149, 63, Interface);// hover
		setBounds(30203, 5, 176, 64, Interface);// hover
		setBounds(30211, 5, 163, 65, Interface);// hover
		setBounds(30219, 5, 163, 66, Interface);// hover
		setBounds(30227, 5, 176, 67, Interface);// hover
		setBounds(30235, 5, 149, 68, Interface);// hover
		setBounds(30243, 5, 176, 69, Interface);// hover
		setBounds(30251, 5, 5, 70, Interface);// hover
		setBounds(30259, 5, 5, 71, Interface);// hover
		setBounds(30267, 5, 5, 72, Interface);// hover
		setBounds(30275, 5, 5, 73, Interface);// hover
		setBounds(30283, 5, 5, 74, Interface);// hover
		setBounds(30291, 5, 5, 75, Interface);// hover
		setBounds(30299, 5, 5, 76, Interface);// hover
		setBounds(30307, 5, 5, 77, Interface);// hover
		setBounds(30323, 5, 5, 78, Interface);// hover
		setBounds(30315, 5, 5, 79, Interface);// hover
	}

	/**
	 * Weapon tab interface TODO: Optimize code
	 * 
	 * @param fonts
	 */
	public static void weaponTab() {
		Sidebar0d(328, 331, "Bash", "Pound", "Focus", 42, 66, 39, 101, 41, 136,
				40, 120, 40, 50, 40, 85);
		Sidebar0a(1698, 1701, 7499, "Chop", "Hack", "Smash", "Block", 42, 75,
				127, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103);
		Sidebar0a(3796, 3799, 7624, "Pound", "Pummel", "Spike", "Block", 39,
				75, 121, 75, 41, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40,
				103);
		Sidebar0c(425, 428, 7474, "Pound", "Pummel", "Block", 39, 75, 121, 75,
				42, 128, 40, 103, 40, 50, 122, 50);
		Sidebar0c(1749, 1752, 7524, "Accurate", "Rapid", "Longrange", 33, 75,
				125, 75, 29, 128, 40, 103, 40, 50, 122, 50);
		Sidebar0c(1764, 1767, 7549, "Accurate", "Rapid", "Longrange", 33, 75,
				125, 75, 29, 128, 40, 103, 40, 50, 122, 50);
		Sidebar0c(4446, 4449, 7649, "Accurate", "Rapid", "Longrange", 33, 75,
				125, 75, 29, 128, 40, 103, 40, 50, 122, 50);
		RSInterface rsi = addInterface(19300);
		textSize(3983, fonts, 0);
		addToggleButton(150, 150, 172, 150, 44, "Auto Retaliate");
		rsi.totalChildren(2);
		rsi.child(0, 3983, 52, 25); // combat level
		rsi.child(1, 150, 21, 153); // auto retaliate
		rsi = interfaceCache[3983];
		rsi.centerText = true;
		rsi.textColor = 0xff981f;
	}

	public static void Sidebar0a(int id, int id2, int id3, String text1,
			String text2, String text3, String text4, int str1x, int str1y,
			int str2x, int str2y, int str3x, int str3y, int str4x, int str4y,
			int img1x, int img1y, int img2x, int img2y, int img3x, int img3y,
			int img4x, int img4y) {
		RSInterface rsi = addInterface(id); // 2423
		addText(id2, "-2", fonts, 3, 0xff981f, true, true); // 2426
		addText(id2 + 11, text1, fonts, 0, 0xff981f, false, true);
		addText(id2 + 12, text2, fonts, 0, 0xff981f, false, true);
		addText(id2 + 13, text3, fonts, 0, 0xff981f, false, true);
		addText(id2 + 14, text4, fonts, 0, 0xff981f, false, true);
		rsi.specialBar(id3); // 7599

		rsi.width = 190;
		rsi.height = 261;

		int last = 15;
		int frame = 0;
		rsi.totalChildren(last);
		rsi.child(frame, id2 + 3, 21, 46);
		frame++; // 2429
		rsi.child(frame, id2 + 4, 104, 99);
		frame++; // 2430
		rsi.child(frame, id2 + 5, 21, 99);
		frame++; // 2431
		rsi.child(frame, id2 + 6, 105, 46);
		frame++; // 2432
		rsi.child(frame, id2 + 7, img1x, img1y);
		frame++; // bottomright 2433
		rsi.child(frame, id2 + 8, img2x, img2y);
		frame++; // topleft 2434
		rsi.child(frame, id2 + 9, img3x, img3y);
		frame++; // bottomleft 2435
		rsi.child(frame, id2 + 10, img4x, img4y);
		frame++; // topright 2436
		rsi.child(frame, id2 + 11, str1x, str1y);
		frame++; // chop 2437
		rsi.child(frame, id2 + 12, str2x, str2y);
		frame++; // slash 2438
		rsi.child(frame, id2 + 13, str3x, str3y);
		frame++; // lunge 2439
		rsi.child(frame, id2 + 14, str4x, str4y);
		frame++; // block 2440
		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon 2426
		rsi.child(frame, id3, 21, 205);
		frame++; // special attack 7599
		for (int i = id2 + 3; i < id2 + 7; i++) { // 2429 - 2433
			rsi = interfaceCache[i];
			rsi.sprite1 = CustomSpriteLoader(19301, "");
			rsi.sprite2 = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0b(int id, int id2, String text1, String text2,
			String text3, String text4, int str1x, int str1y, int str2x,
			int str2y, int str3x, int str3y, int str4x, int str4y, int img1x,
			int img1y, int img2x, int img2y, int img3x, int img3y, int img4x,
			int img4y) {
		RSInterface rsi = addInterface(id); // 2423
		addText(id2, "-2", fonts, 3, 0xff981f, true, true); // 2426
		addText(id2 + 11, text1, fonts, 0, 0xff981f, false, true);
		addText(id2 + 12, text2, fonts, 0, 0xff981f, false, true);
		addText(id2 + 13, text3, fonts, 0, 0xff981f, false, true);
		addText(id2 + 14, text4, fonts, 0, 0xff981f, false, true);

		rsi.width = 190;
		rsi.height = 261;

		int last = 14;
		int frame = 0;
		rsi.totalChildren(last);
		rsi.child(frame, id2 + 3, 21, 46);
		frame++; // 2429
		rsi.child(frame, id2 + 4, 104, 99);
		frame++; // 2430
		rsi.child(frame, id2 + 5, 21, 99);
		frame++; // 2431
		rsi.child(frame, id2 + 6, 105, 46);
		frame++; // 2432
		rsi.child(frame, id2 + 7, img1x, img1y);
		frame++; // bottomright 2433
		rsi.child(frame, id2 + 8, img2x, img2y);
		frame++; // topleft 2434
		rsi.child(frame, id2 + 9, img3x, img3y);
		frame++; // bottomleft 2435
		rsi.child(frame, id2 + 10, img4x, img4y);
		frame++; // topright 2436
		rsi.child(frame, id2 + 11, str1x, str1y);
		frame++; // chop 2437
		rsi.child(frame, id2 + 12, str2x, str2y);
		frame++; // slash 2438
		rsi.child(frame, id2 + 13, str3x, str3y);
		frame++; // lunge 2439
		rsi.child(frame, id2 + 14, str4x, str4y);
		frame++; // block 2440
		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon 2426
		for (int i = id2 + 3; i < id2 + 7; i++) { // 2429 - 2433
			rsi = interfaceCache[i];
			rsi.sprite1 = CustomSpriteLoader(19301, "");
			rsi.sprite2 = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0c(int id, int id2, int id3, String text1,
			String text2, String text3, int str1x, int str1y, int str2x,
			int str2y, int str3x, int str3y, int img1x, int img1y, int img2x,
			int img2y, int img3x, int img3y) {
		RSInterface rsi = addInterface(id); // 2423
		addText(id2, "-2", fonts, 3, 0xff981f, true, true); // 2426
		addText(id2 + 9, text1, fonts, 0, 0xff981f, false, true);
		addText(id2 + 10, text2, fonts, 0, 0xff981f, false, true);
		addText(id2 + 11, text3, fonts, 0, 0xff981f, false, true);
		rsi.specialBar(id3); // 7599

		rsi.width = 190;
		rsi.height = 261;

		int last = 12;
		int frame = 0;
		rsi.totalChildren(last);
		rsi.child(frame, id2 + 3, 21, 99);
		frame++;
		rsi.child(frame, id2 + 4, 105, 46);
		frame++;
		rsi.child(frame, id2 + 5, 21, 46);
		frame++;
		rsi.child(frame, id2 + 6, img1x, img1y);
		frame++; // topleft
		rsi.child(frame, id2 + 7, img2x, img2y);
		frame++; // bottomleft
		rsi.child(frame, id2 + 8, img3x, img3y);
		frame++; // topright
		rsi.child(frame, id2 + 9, str1x, str1y);
		frame++; // chop
		rsi.child(frame, id2 + 10, str2x, str2y);
		frame++; // slash
		rsi.child(frame, id2 + 11, str3x, str3y);
		frame++; // lunge
		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon
		rsi.child(frame, id3, 21, 205);
		frame++; // special attack 7599
		for (int i = id2 + 3; i < id2 + 6; i++) {
			rsi = interfaceCache[i];
			rsi.sprite1 = CustomSpriteLoader(19301, "");
			rsi.sprite2 = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0d(int id, int id2, String text1, String text2,
			String text3, int str1x, int str1y, int str2x, int str2y,
			int str3x, int str3y, int img1x, int img1y, int img2x, int img2y,
			int img3x, int img3y) {
		RSInterface rsi = addInterface(id); // 2423
		addText(id2, "-2", fonts, 3, 0xff981f, true, true); // 2426
		addText(id2 + 9, text1, fonts, 0, 0xff981f, false, true);
		addText(id2 + 10, text2, fonts, 0, 0xff981f, false, true);
		addText(id2 + 11, text3, fonts, 0, 0xff981f, false, true);
		newInterface(353);
		addText(354, "Spell", fonts, 0, 0xff981f, false, true);
		addCacheSprite(337, 19, 0, "combaticons");
		addCacheSprite(338, 13, 0, "combaticons2");
		addCacheSprite(339, 14, 0, "combaticons2");
		newInterface(349);
		addToggleButton(350, 350, 108, 68, 44, "Select");

		rsi.width = 190;
		rsi.height = 261;

		int last = 15;
		int frame = 0;
		rsi.totalChildren(last);
		rsi.child(frame, id2 + 3, 20, 115);
		frame++;
		rsi.child(frame, id2 + 4, 20, 80);
		frame++;
		rsi.child(frame, id2 + 5, 20, 45);
		frame++;
		rsi.child(frame, id2 + 6, img1x, img1y);
		frame++; // topleft
		rsi.child(frame, id2 + 7, img2x, img2y);
		frame++; // bottomleft
		rsi.child(frame, id2 + 8, img3x, img3y);
		frame++; // topright
		rsi.child(frame, id2 + 9, str1x, str1y);
		frame++; // bash
		rsi.child(frame, id2 + 10, str2x, str2y);
		frame++; // pound
		rsi.child(frame, id2 + 11, str3x, str3y);
		frame++; // focus
		rsi.child(frame, 349, 105, 46);
		frame++; // spell1
		rsi.child(frame, 350, 104, 106);
		frame++; // spell2
		rsi.child(frame, 353, 125, 74);
		frame++; // spell
		rsi.child(frame, 354, 110, 134);
		frame++; // spell
		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon
	}

	@SuppressWarnings("unused")
	private static void addStandardMagicSpell(int childId, int spellIndex,
			int configId, int magicLevel, int usableOn, int[][] runes,
			int[] requiredValues, String name) {
		RSInterface child = interfaceCache[childId];
		child.id = childId;
		child.parentId = 1151;
		child.type = 5;
		child.actionType = 0;
		child.contentType = 0;
		child.hoverId = childId + 1;
		child.sprite1 = getCacheSprite("magic_on", spellIndex);
		child.sprite2 = getCacheSprite("magic_off", spellIndex);
		child.spellUsableOn = usableOn;
		child.selectedActionName = "Cast on";
		child.width = child.height = 20;
		child.tooltip = "Cast @gre@" + name;
		child.spellName = name;
		child.valueCompareType = child.requiredValue = new int[4];
		child.valueCompareType[0] = child.valueCompareType[1] = child.valueCompareType[2] = 3;
		// TODO
	}

	private static final int WHITE_TEXT = 0xFFFFFF;
	protected static final int YELLOW_TEXT = 0xff9040;
	protected static final int GREY_TEXT = 0xB9B855;
	private static final int ORANGE_TEXT = 0xFF981F;
}