package src.interfaces;

import src.SpriteLoader;

public class CachePackedInterfaces extends Interfaces {

	private static final int CLOSE_BUTTON = 273;
	
	public static void init() {
		skillLevelUp();
		attackTab();
		prayerTab();
		skillsTab();
		bobInterface();
		friendsTab();
		ignoreTab();
		clanChatMessageColorTab();
		clanChatTab();
		notesTab();
		shop();
	}
	
	private static void shop() {
		RSInterface shop = addInterface(1200);
		InterfaceManager.addSpriteLoaderSprite(1201, 581, 100, 150);
		InterfaceManager.addSpriteLoaderHoverButton(1202, CLOSE_BUTTON, "Close", CLOSE_BUTTON + 1, new int[] {5, 3, 0});
		addShopTabs(1200, 1203);
		InterfaceManager.addText(1204, "Niobe Shop", YELLOW_TEXT, true, true, fonts[2]);
		shop.totalChildren(4);
		shop.child(0, 1201, 5, 10);
		shop.child(1, 1202, 480, 13);
		shop.child(2, 1203, 40, 80);
		shop.child(3, 1204, 245, 15);
	}
	
	private static void addShopTabs(int parentId, int id) {
		RSInterface shop = interfaceCache[id] = new RSInterface();
		shop.actions = new String[10];
		shop.spritesX = new int[20];
		shop.inventoryAmount = new int[30];
		shop.inventory = new int[30];
		shop.spritesY = new int[20];
		shop.children = new int[0];
		shop.childX = new int[0];
		shop.childY = new int[0];
		shop.actions[0] = "Value";
		shop.actions[1] = "Buy 1";
		shop.actions[2] = "Buy 5";
		shop.actions[3] = "Buy 10";
		shop.actions[4] = "Buy 20";
		shop.centerText = true;
		shop.aBoolean227 = false;
		shop.aBoolean235 = false;
		shop.usableItemInterface = false;
		shop.isInventoryInterface = false;
		shop.aBoolean259 = true;
		shop.invSpritePadX = 45;
		shop.invSpritePadY = 25; 
		shop.height = 5;
		shop.width = 6;
		shop.parentId = parentId;
		shop.id = id;
		shop.type = 2;
		shop.shopPrices = new int[30];
		shop.sprite1 = SpriteLoader.sprites[583];
		shop.hoverSprite1 = SpriteLoader.sprites[584];
		shop.shopCurrency = SpriteLoader.sprites[586]; //make this 585 and make other index the other sprites, later on;
		shop.shopInterface = true;
	}
	
	private static void notesTab() {
		RSInterface tab = addInterface(173);
		InterfaceManager.addSpriteLoaderSprite(17351, 280);
		InterfaceManager.addSpriteLoaderSprite(17356, 276);
		InterfaceManager.addSpriteLoaderSprite(17357, 276);
		InterfaceManager.addSpriteLoaderHoverButton(17352, 576, "Add note", 575);
		InterfaceManager.addSpriteLoaderHoverButton(17355, 579, "Delete All", 578);
		InterfaceManager.addText(13800, "", 0xffffff, true, false, fonts[0]);
		InterfaceManager.addText(13801, "@lre@Notes (8/30)", 0xfff, false, true, fonts[1]);
		tab.totalChildren(8);
		tab.child(0, 17351, 0, 30);
		tab.child(1, 17352, 15, 5);
		tab.child(2, 17355, 160, 235);
		tab.child(3, 13800, 85, 78);
		tab.child(4, 17356, 0, 28);
		tab.child(5, 17357, 0, 222);
		tab.child(6, 14000, 0, 30);
		tab.child(7, 13801, 70, 10);
		RSInterface.isTabInterface(tab, true);
		RSInterface list = addInterface(14000);
		list.totalChildren(30);
		for (int i = 14001; i < 14031; i++) {
			InterfaceManager.addMenu(14000, i, 0xffffff, fonts[0], new String[] {"Select", "Edit", "Colour", "Delete", "Cancel"});
			RSInterface.interfaceCache[i].isTabChild = true;
		}
		for (int id = 14001, i = 0; id < 14031 && i < 30; id++, i++) {
			list.children[i] = id;
			list.childX[i] = 5;
			for (int id2 = 14001, i2 = 1; id2 < 14031 && i2 < 30; id2++, i2++) {
				list.childY[0] = 2;
				list.childY[i2] = list.childY[i2 - 1] + 18;
			}
		}
		list.height = 192;
		list.width = 174;
		list.scrollMax = 550;
		RSInterface.isTabInterface(list, true);
	}
	
	public static void clanChatTab() {
		RSInterface tab = addInterface(18128);
		InterfaceManager.addSpriteLoaderHoverButton(18129, 278, "Join Clan", 279, new int[] {5, 1, 550});
		InterfaceManager.addSpriteLoaderHoverButton(18132, 281, "Leave Clan", 282);
		InterfaceManager.addSpriteLoaderHoverButton(18251, 573, "Clan Chat Settings", 574);
		InterfaceManager.addSpriteLoaderButton(18255, 239, "Toggle Lootshare");
		InterfaceManager.addSpriteLoaderSprite(18137, 280);
		InterfaceManager.addText(18138, "Clan Chat", 0xff9b00, true, true, fonts[1]);
		InterfaceManager.addText(18140, "Talking in: @whi@Not in chat", 0xff9b00, false, true, fonts[0]);
		InterfaceManager.addText(18250, "Owner: None", 0xff9b00, false, true, fonts[0]);
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
		isTabInterface(tab, true);
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
		RSInterface.isTabInterface(list, true);
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
		int spriteIndex = 291;
		InterfaceManager.addSpriteLoaderSprite(17878, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17879, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17880, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17881, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17882, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17883, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17884, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17885, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17886, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17887, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17888, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17889, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17890, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17891, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17892, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17893, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17894, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17895, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17896, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(11897, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17898, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17899, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17900, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17901, spriteIndex++);
		InterfaceManager.addSpriteLoaderSprite(17902, spriteIndex++);
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
	
	public static void attackTab() {
		Sidebar0d(328, 331, "Bash", "Pound", "Focus", 42, 75,
				125, 75, 42, 128, 44, 103, 40, 50, 127, 50);
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
		//InterfaceManager.addSpriteLoaderConfigButtonWithTooltipBox(19300, 150, 502, 503, -1, 172, 0, "Auto Retaliate", 19301, "When active your player\nWill automatically fight\nBack if attacked");
		rsi.totalChildren(3);
		rsi.child(0, 3983, 52, 25);
		rsi.child(1, 22845, 21, 153);
		rsi.child(2, 22885, 52, 161);
		rsi = interfaceCache[3983];
		rsi.centerText = true;
		rsi.textColor = 0xff981f;
		RSInterface auto = RSInterface.interfaceCache[19300];
		auto.isTabChild = true;
	}
	
	public static void Sidebar0a(int id, int id2, int id3, String text1,
			String text2, String text3, String text4, int str1x, int str1y,
			int str2x, int str2y, int str3x, int str3y, int str4x, int str4y,
			int img1x, int img1y, int img2x, int img2y, int img3x, int img3y,
			int img4x, int img4y) {
		RSInterface rsi = addInterface(id); // 2423
		addText(id2, "None", fonts, 3, 0xff981f, true, true); // 2426
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
			rsi.sprite1 = SpriteLoader.sprites[571];
			rsi.sprite2 = SpriteLoader.sprites[572];
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
		addText(id2, "None", fonts, 3, 0xff981f, true, true); // 2426
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
			rsi.sprite1 = SpriteLoader.sprites[571];
			rsi.sprite2 = SpriteLoader.sprites[572];
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0c(int id, int id2, int id3, String text1,
			String text2, String text3, int str1x, int str1y, int str2x,
			int str2y, int str3x, int str3y, int img1x, int img1y, int img2x,
			int img2y, int img3x, int img3y) {
		RSInterface rsi = addInterface(id); // 2423
		addText(id2, "None", fonts, 3, 0xff981f, true, true); // 2426
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
			rsi.sprite1 = SpriteLoader.sprites[571];
			rsi.sprite2 = SpriteLoader.sprites[572];
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0d(int id, int id2, String text1, String text2,
			String text3, int str1x, int str1y, int str2x, int str2y,
			int str3x, int str3y, int img1x, int img1y, int img2x, int img2y,
			int img3x, int img3y) {
		RSInterface rsi = addInterface(id); // 2423
		addText(id2, "None", fonts, 3, 0xff981f, true, true); // 2426
		addText(id2 + 9, text1, fonts, 0, 0xff981f, false, true);
		addText(id2 + 10, text2, fonts, 0, 0xff981f, false, true);
		addText(id2 + 11, text3, fonts, 0, 0xff981f, false, true);
		//rsi.specialBar(id2); // 7599
		newInterface(353);
		InterfaceManager.addSpriteLoaderSprite(337, 543);
		InterfaceManager.addSpriteLoaderSprite(338, 544);
		InterfaceManager.addSpriteLoaderSprite(339, 545); //pound
		newInterface(349);

		rsi.width = 190;
		rsi.height = 261;

		int last = 11;
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
		rsi.child(frame, id2 + 11, str3x, str3y); //pound
		frame++; // lunge
		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // special attack 7599
		for (int i = id2 + 3; i < id2 + 6; i++) {
			rsi = interfaceCache[i];
			rsi.sprite1 = SpriteLoader.sprites[571];
			rsi.sprite2 = SpriteLoader.sprites[572];
			rsi.width = 68;
			rsi.height = 44;
		}
	}
	
	public static void prayerTab() {
		RSInterface tab = interfaceCache[5608];
		addNewPrayer(18022, 711, 35, 0, 18033, "Protect from Summoning", 
						"Level 35\\nProtect from Summoning\\nProtection from attacks dealt\\nby familiars");
		addNewPrayer(18014, 708, 65, 1, 18041, "Rapid Renewal",
				"Level 65\\nRapid Renewal\\n5x restore rate for the Hitpoints\\nstat");
		addNewPrayer(18018, 709, 74, 2, 18045, "Rigour",
				"Level 74\\nRigour\\nIncreases your Ranged by 20%\\nand your Defence by 25%");
		addNewPrayer(18020, 710, 77, 3, 18047, "Augury",
				"Level 77\\nAugury\\nIncreases your Magic by 20%\\nand your Defence by 25%");
		setChildren(92, tab);
		int index = 0;
		tab.child(index++, 5651, 55, 242); // prayer icon
		tab.child(index++, 687, 75, 244); // text
		/* Buttons and sprites */
		tab.child(index++, 5609, 6, 4); // thick skin
		tab.child(index++, 5632, 9, 6);
		tab.child(index++, 5610, 42, 4); // burst of strength
		tab.child(index++, 5633, 46, 6);
		tab.child(index++, 5611, 78, 4); // clarity of thought
		tab.child(index++, 5634, 81, 9);
		tab.child(index++, 19812, 114, 4); // sharp eye
		tab.child(index++, 19813, 117, 8);
		tab.child(index++, 19814, 150, 4); // mystic will
		tab.child(index++, 19815, 153, 7);
		tab.child(index++, 5612, 6, 40); // rock skin
		tab.child(index++, 5635, 9, 42);
		tab.child(index++, 5613, 42, 40); // superhuman strength
		tab.child(index++, 5636, 46, 42);
		tab.child(index++, 5614, 78, 40); // improved reflexes
		tab.child(index++, 5637, 81, 45);
		tab.child(index++, 5615, 114, 40); // rapid restore
		tab.child(index++, 5638, 117, 45);
		tab.child(index++, 5616, 150, 40); // rapid heal
		tab.child(index++, 5639, 153, 45);
		tab.child(index++, 5617, 6, 76); // protect item
		tab.child(index++, 5640, 7, 79);
		tab.child(index++, 19816, 42, 76); // hawk eye
		tab.child(index++, 19817, 45, 80);
		tab.child(index++, 19818, 78, 76); // mystic lore
		tab.child(index++, 19820, 81, 79);
		tab.child(index++, 5618, 114, 76); // steel skin
		tab.child(index++, 5641, 117, 78);
		tab.child(index++, 5619, 150, 76); // ultimate strength
		tab.child(index++, 5642, 154, 79);
		tab.child(index++, 5620, 6, 113); // incredible reflexes
		tab.child(index++, 5643, 9, 118);
		tab.child(index++, 18022, 42, 110); // protect from summoning
		tab.child(index++, 18023, 45, 114);
		tab.child(index++, 5621, 81, 112); // protect from magic
		tab.child(index++, 5644, 83, 114);
		tab.child(index++, 5622, 111, 112); // protect from missiles
		tab.child(index++, 686, 116, 116);
		tab.child(index++, 5623, 150, 112); // protect from melee
		tab.child(index++, 5645, 152, 114);
		tab.child(index++, 19821, 4, 150); // eagle eye
		tab.child(index++, 19822, 8, 154);
		tab.child(index++, 19823, 39, 151); // mystic might
		tab.child(index++, 19824, 42, 154);
		tab.child(index++, 683, 76, 149); // retribution
		tab.child(index++, 5649, 77, 150);
		tab.child(index++, 684, 111, 148); // redemption
		tab.child(index++, 5647, 114, 153);
		tab.child(index++, 685, 147, 149); // smite
		tab.child(index++, 5648, 148, 150);
		tab.child(index++, 19825, 5, 187); // chivalry
		tab.child(index++, 19826, 12, 190);
		tab.child(index++, 18014, 38, 188); // rapid renewal
		tab.child(index++, 18015, 41, 193);
		tab.child(index++, 19827, 75, 190); // piety
		tab.child(index++, 19828, 77, 200);
		tab.child(index++, 18018, 112, 191); // rigour
		tab.child(index++, 18019, 115, 194);
		tab.child(index++, 18020, 147, 191); // augury
		tab.child(index++, 18021, 150, 194);
		/* Tooltips */
		tab.child(index++, 22638, 9, 6); // thick skin
		tab.child(index++, 22639, 46, 6); // burst of strength
		tab.child(index++, 22640, 81, 9); // clarity of thought
		tab.child(index++, 22641, 114, 4); // sharp eye
		tab.child(index++, 22642, 150, 4); // mystic will
		tab.child(index++, 20253, 9, 42); // rock skin
		tab.child(index++, 20254, 46, 42); // superhuman strength
		tab.child(index++, 20255, 81, 45); // improved reflexes
		tab.child(index++, 20256, 117, 45); // rapid restore
		tab.child(index++, 20257, 153, 45); // rapid heal
		tab.child(index++, 21141, 7, 79); // protect item
		tab.child(index++, 21142, 42, 76); // hawk eye
		tab.child(index++, 21143, 78, 76); // mystic lore
		tab.child(index++, 21144, 117, 78); // steel skin
		tab.child(index++, 21145, 154, 79); // ultimate strength
		tab.child(index++, 21146, 9, 118); // incredible reflexes
		tab.child(index++, 18033, 45, 114); // protect from summoning
		tab.child(index++, 21147, 83, 114); // protect from magic
		tab.child(index++, 21148, 116, 116); // protect from missiles
		tab.child(index++, 21149, 152, 114); // protect from melee
		tab.child(index++, 21150, 8, 154); // eagle eye
		tab.child(index++, 21135, 42, 154); // mystic might
		tab.child(index++, 21136, 77, 150); // retribution
		tab.child(index++, 21137, 114, 153); // redemption
		tab.child(index++, 21138, 148, 150); // smite
		tab.child(index++, 21139, 12, 190); // chivalry
		tab.child(index++, 18041, 41, 193); // rapid renewal
		tab.child(index++, 21140, 77, 200); // piety
		tab.child(index++, 18045, 115, 194); // rigour
		tab.child(index++, 18047, 150, 194); // augury
	}
	
	private static void clanChatMessageColorTab() {
		RSInterface rsi = addInterface(7850);
		String directory = "color_changer";
		InterfaceManager.addCacheHoverButton(7851, 0, "Previous", 1, archive, directory);
		InterfaceManager.addCacheHoverButton(7852, 2, "Next", 3, archive, directory);
		InterfaceManager.addCacheHoverButton(7853, 4, "Confirm color", 5, archive, directory);
		InterfaceManager.addText(7855, "Confirm", GREY_TEXT, false, true, fonts[0]);
		InterfaceManager.addCacheSprite(7856, 0, archive, "bank");
		InterfaceManager.addText(7857, "TestTestTestTestTestTest\\n\\nTestTestTestTestTestTestTest\\n\\nTestTestTestTestTestTestTest\\n\\nTestTestTestTestTestTestTest", YELLOW_TEXT, false, true, fonts[0]);
		InterfaceManager.addText(7858, "Clan Chat Message\\n    Color Changer", 0xFFFF00, false, true, fonts[2]);
		setChildren(7, rsi);
		int index = 0;
		rsi.child(index++, 7851, 45, 205); //previous
		rsi.child(index++, 7852, 115, 205); //next
		rsi.child(index++, 7853, 55, 230); //confirm color
		rsi.child(index++, 7856, 73, 200); //color background box
		rsi.child(index++, 7855, 77, 238); //confirm text
		rsi.child(index++, 7857, 20, 100); //top text
		rsi.child(index++, 7858, 35, 15); //title
		RSInterface color = RSInterface.interfaceCache[7856];
		color.fillWithColor = true;
		color.colorFill = "0000FF";
		color.actionType = 7;
	}
	
	public static void friendsTab() {
		RSInterface tab = addInterface(5065);
		RSInterface list = interfaceCache[5066];
		InterfaceManager.addSpriteLoaderSprite(16126, 276);
		InterfaceManager.addSpriteLoaderSprite(16127, 275);
		addText(5067, "Friends List", fonts, 1, 0xff9933, true, true);
		addText(5070, "World 1", fonts, 0, 0xff9933, false, true);
		addText(5071, "", fonts, 0, 0xff9933, false, true);
		InterfaceManager.addSpriteLoaderHoverButton(5068, 278, "Add Name", 279, new int[] {5, 1, 501});
		InterfaceManager.addSpriteLoaderHoverButton(5069, 281, "Delete Name", 282, new int[] {5, 1, 502});
		InterfaceManager.addSpriteLoaderHoverButton(5076, 288, "Toggle Friends", 287);
		InterfaceManager.addSpriteLoaderHoverButton(5079, 290, "Toggle Ignored", 289);
		tab.totalChildren(11);
		tab.child(0, 16127, 0, 40);
		tab.child(1, 5067, 92, 5);
		tab.child(2, 16126, 0, 40);
		tab.child(3, 5066, 0, 42);
		tab.child(4, 16126, 0, 231);
		tab.child(5, 5068, 5, 240);
		tab.child(6, 5069, 25, 240);
		tab.child(7, 5076, 150, 236);
		tab.child(8, 5079, 170, 236);
		tab.child(9, 5070, 70, 25);
		tab.child(10, 5071, 106, 237);
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
		RSInterface.isTabInterface(tab, true);
		RSInterface.isTabInterface(list, true);
	}

	public static void ignoreTab() {
		RSInterface tab = addInterface(5715);
		RSInterface list = interfaceCache[5716];
		addText(5717, "Ignore List", fonts, 1, 0xff9933, true, true);
		addText(5720, "World 1", fonts, 0, 0xff9933, false, true);
		addText(5721, "", fonts, 0, 0xff9933, false, true);
		InterfaceManager.addSpriteLoaderHoverButton(5724, 285, "i dunno", 286);
		tab.totalChildren(13);
		tab.child(0, 5717, 92, 5);
		tab.child(1, 16127, 0, 40);
		tab.child(2, 16126, 0, 40);
		tab.child(3, 5716, 0, 42);
		tab.child(4, 16126, 0, 231);
		tab.child(5, 5068, 5, 240);
		tab.child(6, 5069, 25, 240);
		tab.child(7, 5076, 150, 236);
		tab.child(8, 5077, 150, 237);
		tab.child(9, 5079, 170, 236);
		tab.child(10, 5080, 170, 237);
		tab.child(11, 5720, 70, 25);
		tab.child(12, 5721, 108, 237);
		list.height = 189;
		list.width = 174;
		list.scrollMax = 200;
		list.children = new int[200];
		for (int id = 5742, i = 0; id <= 5841 && i <= 99; id++, i++) {
			list.children[i] = id;
			list.childX[i] = 3;
			list.childY[i] = list.childY[i] - 7;
		}
		RSInterface.isTabInterface(tab, true);
		RSInterface.isTabInterface(list, true);
	}
	
	private static void bobInterface() {
		RSInterface rsi = addInterface(2700);
		InterfaceManager.addSpriteLoaderSprite(2701, 271);
		addBobStorage(2702);
		InterfaceManager.addSpriteLoaderHoverButton(2703, 273, "Close", 274);
		InterfaceManager.addSpriteLoaderButton(2704, 272, "Take BoB");
		rsi.totalChildren(4);
		rsi.child(0, 2701, 90, 14);
		rsi.child(1, 2702, 100, 56);
		rsi.child(2, 2703, 431, 23);
		rsi.child(3, 2704, 427, 285);
	}
	
	private static void skillsTab() {
		RSInterface rsi = addInterface(31110);
		setChildren(104, rsi);

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31111, 245, "View @or2@Attack @lre@guide", 268, 31112, "EXP: 14000000");
		addText(31114, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31115, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31112].contentType = 1004;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31116, 265, "View @or2@Strength @lre@guide", 268, 31117, "EXP: 14000000");
		addText(31119, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31120, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31117].contentType = 1007;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31121, 249, "View @or2@Defence @lre@guide", 268, 31122, "EXP: 14000000");
		addText(31124, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31125, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31122].contentType = 1010;
		
		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31126, 261, "View @or2@Ranged @lre@guide", 268, 31127, "EXP: 14000000");
		addText(31129, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31130, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31127].contentType = 1013;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31131, 260, "View @or2@Prayer @lre@guide", 268, 31132, "EXP: 14000000");
		addText(31134, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31135, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31132].contentType = 1016;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31136, 258, "View @or2@Magic @lre@guide", 268, 31137, "EXP: 14000000");
		addText(31139, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31140, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31137].contentType = 1019;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31141, 262, "View @or2@Runecrafting @lre@guide", 268, 31142, "EXP: 14000000");
		addText(31144, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31145, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31142].contentType = 1022;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31146, 246, "View @or2@Construction @lre@guide", 268, 31147, "EXP: 14000000");
		addText(31149, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31150, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31147].contentType = 1025;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31151, 250, "View @or2@Dungeoneering @lre@guide", 268, 31152, "EXP: 14000000");
		addText(31154, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31155, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31152].contentType = 1028;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31156, 256, "View @or2@Constitution @lre@guide", 268, 31157, "EXP: 14000000");
		addText(31159, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31160, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31157].contentType = 1005;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31161, 244, "View @or2@Agility @lre@guide", 268, 31162, "EXP: 14000000");
		addText(31164, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31165, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31162].contentType = 1008;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31166, 255, "View @or2@Herblore @lre@guide", 268, 31167, "EXP: 14000000");
		addText(31169, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31170, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31167].contentType = 1011;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31171, 267, "View @or2@Thieving @lre@guide", 268, 31172, "EXP: 14000000");
		addText(31174, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31175, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31172].contentType = 1014;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31176, 248, "View @or2@Crafting @lre@guide", 268, 31177, "EXP: 14000000");
		addText(31179, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31180, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31177].contentType = 1017;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31181, 254, "View @or2@Fletching @lre@guide", 268, 31182, "EXP: 14000000");
		addText(31184, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31185, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31182].contentType = 1020;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31186, 263, "View @or2@Slayer @lre@guide", 268, 31187, "EXP: 14000000");
		addText(31189, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31190, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31187].contentType = 1023;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31191, 257, "View @or2@Hunter @lre@guide", 268, 31192, "EXP: 14000000");
		addText(31194, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31195, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31192].contentType = 1026;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31196, 269, "View @or2@Total Level", 270, 31197, "EXP: 14000000");
		addText(31199, "Total Level:", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31200, "2475", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31197].contentType = 1029;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31201, 259, "View @or2@Mining @lre@guide", 268, 31202, "EXP: 14000000");
		addText(31204, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31205, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31202].contentType = 1006;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31206, 264, "View @or2@Smithing @lre@guide", 268, 31207, "EXP: 14000000");
		addText(31209, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31210, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31207].contentType = 1009;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31211, 253, "View @or2@Fishing @lre@guide", 268, 31212, "EXP: 14000000");
		addText(31214, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31215, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31212].contentType = 1012;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31216, 247, "View @or2@Cooking @lre@guide", 268, 31217, "EXP: 14000000");
		addText(31219, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31220, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31217].contentType = 1015;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31221, 252, "View @or2@Firemaking @lre@guide", 268, 31222, "EXP: 14000000");
		addText(31224, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31225, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31222].contentType = 1018;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31226, 243, "View @or2@Woodcutting @lre@guide", 268, 31227, "EXP: 14000000");
		addText(31229, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31230, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31227].contentType = 1021;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31231, 251, "View @or2@Farming @lre@guide", 268, 31232, "EXP: 14000000");
		addText(31234, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31235, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31232].contentType = 1024;

		InterfaceManager.addSpriteLoaderHoverButtonWithTooltipBox(31236, 266, "View @or2@Summoning @lre@guide", 268, 31237, "EXP: 14000000");
		addText(31239, "99", 0xFFEE33, false, true, 52, fonts, 0);
		addText(31240, "99", 0xFFEE33, false, true, 52, fonts, 0);
		RSInterface.interfaceCache[31237].contentType = 1027;
		int index = 0;
		rsi.child(index++, 31111, 3, 3);
		rsi.child(index++, 31114, 31, 6);
		rsi.child(index++, 31115, 43, 18);
		
		rsi.child(index++, 31116, 3, 31);
		rsi.child(index++, 31119, 31, 34);
		rsi.child(index++, 31120, 43, 46);

		rsi.child(index++, 31121, 3, 59);
		rsi.child(index++, 31124, 31, 62);
		rsi.child(index++, 31125, 43, 74);

		rsi.child(index++, 31126, 3, 87);
		rsi.child(index++, 31129, 31, 90);
		rsi.child(index++, 31130, 43, 102);

		rsi.child(index++, 31131, 3, 115);
		rsi.child(index++, 31134, 31, 118);
		rsi.child(index++, 31135, 43, 130);

		rsi.child(index++, 31136, 3, 143);
		rsi.child(index++, 31139, 31, 146);
		rsi.child(index++, 31140, 43, 158);

		rsi.child(index++, 31141, 3, 171);
		rsi.child(index++, 31144, 31, 174);
		rsi.child(index++, 31145, 43, 186);

		rsi.child(index++, 31146, 3, 199);
		rsi.child(index++, 31149, 31, 202);
		rsi.child(index++, 31150, 43, 214);

		rsi.child(index++, 31151, 3, 227);
		rsi.child(index++, 31154, 31, 230);
		rsi.child(index++, 31155, 43, 242);

		rsi.child(index++, 31156, 64, 3);
		rsi.child(index++, 31159, 92, 6);
		rsi.child(index++, 31160, 104, 18);

		rsi.child(index++, 31161, 64, 31);
		rsi.child(index++, 31164, 92, 34);
		rsi.child(index++, 31165, 104, 46);

		rsi.child(index++, 31166, 64, 59);
		rsi.child(index++, 31169, 92, 62);
		rsi.child(index++, 31170, 104, 74);

		rsi.child(index++, 31171, 64, 87);
		rsi.child(index++, 31174, 92, 90);
		rsi.child(index++, 31175, 104, 102);

		rsi.child(index++, 31176, 64, 115);
		rsi.child(index++, 31179, 92, 118);
		rsi.child(index++, 31180, 104, 130);

		rsi.child(index++, 31181, 64, 143);
		rsi.child(index++, 31184, 92, 146);
		rsi.child(index++, 31185, 104, 158);

		rsi.child(index++, 31186, 64, 171);
		rsi.child(index++, 31189, 92, 174);
		rsi.child(index++, 31190, 104, 186);

		rsi.child(index++, 31191, 64, 199);
		rsi.child(index++, 31194, 92, 202);
		rsi.child(index++, 31195, 104, 214);
		
		rsi.child(index++, 31201, 125, 3);
		rsi.child(index++, 31204, 153, 6);
		rsi.child(index++, 31205, 165, 18);

		rsi.child(index++, 31206, 125, 31);
		rsi.child(index++, 31209, 153, 34);
		rsi.child(index++, 31210, 165, 46);

		rsi.child(index++, 31211, 125, 59);
		rsi.child(index++, 31214, 153, 62);
		rsi.child(index++, 31215, 165, 74);

		rsi.child(index++, 31216, 125, 87);
		rsi.child(index++, 31219, 153, 90);
		rsi.child(index++, 31220, 165, 102);

		rsi.child(index++, 31221, 125, 115);
		rsi.child(index++, 31224, 153, 118);
		rsi.child(index++, 31225, 165, 130);

		rsi.child(index++, 31226, 125, 143);
		rsi.child(index++, 31229, 153, 146);
		rsi.child(index++, 31230, 165, 158);

		rsi.child(index++, 31231, 125, 171);
		rsi.child(index++, 31234, 153, 174);
		rsi.child(index++, 31235, 165, 186);

		rsi.child(index++, 31236, 125, 199);
		rsi.child(index++, 31239, 153, 202);
		rsi.child(index++, 31240, 165, 214);
		
		rsi.child(index++, 31196, 64, 227);
		rsi.child(index++, 31199, 105, 229);
		rsi.child(index++, 31200, 116, 241);
		
		rsi.child(index++, 31112, 3, 3);
		rsi.child(index++, 31117, 3, 31);
		rsi.child(index++, 31122, 3, 59);
		rsi.child(index++, 31127, 3, 87);
		rsi.child(index++, 31132, 3, 115);
		rsi.child(index++, 31137, 3, 143);
		rsi.child(index++, 31142, 3, 171);
		rsi.child(index++, 31147, 3, 199);
		rsi.child(index++, 31152, 3, 227);
		rsi.child(index++, 31157, 64, 3);
		rsi.child(index++, 31162, 64, 31);
		rsi.child(index++, 31167, 64, 59);
		rsi.child(index++, 31172, 64, 87);
		rsi.child(index++, 31177, 64, 115);
		rsi.child(index++, 31182, 64, 143);
		rsi.child(index++, 31187, 64, 171);
		rsi.child(index++, 31192, 64, 199);
		rsi.child(index++, 31202, 125, 3);
		rsi.child(index++, 31207, 125, 31);
		rsi.child(index++, 31212, 125, 59);
		rsi.child(index++, 31217, 125, 87);
		rsi.child(index++, 31222, 125, 115);
		rsi.child(index++, 31227, 125, 143);
		rsi.child(index++, 31232, 125, 171);
		rsi.child(index++, 31237, 125, 199);
		rsi.child(index++, 31197, 64, 227);
		
		RSInterface.isTabInterface(rsi, true);
	}
}
