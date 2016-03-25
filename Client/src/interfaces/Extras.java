package src.interfaces;

import src.JagexArchive;
import src.RSFontSystem;
import src.Sprite;
import src.TextClass;

public class Extras extends RSInterface {
	
	public static void addButton(int id, int sid, String spriteName, String tooltip, int w, int h) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentId = id;
		tab.type = 5;
		tab.actionType = 1;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.hoverId = 52;
		tab.sprite1 = imageLoader(sid, spriteName, true);
		tab.sprite2 = imageLoader(sid, spriteName, true);
		tab.width = w;
		tab.height = h;
		tab.tooltip = tooltip;
	}
	
	public static void addHoverButton(int i, String imageName, int j, int width, int height, String text, int contentType, int hoverOver, int aT) {//hoverable button
		RSInterface tab = addInterface(i);
		tab.id = i;
		tab.parentId = i;
		tab.type = 5;
		tab.actionType = aT;
		tab.contentType = contentType;
		tab.hoverId = hoverOver;
		tab.sprite1 = imageLoader(j, imageName, true);
		tab.sprite2 = imageLoader(j, imageName, true);
		tab.width = width;
		tab.height = height;
		tab.tooltip = text;
	}

	public static void addHoveredButton(int i, String imageName, int j, int w, int h, int IMAGEID) {//hoverable button
		RSInterface tab = addInterface(i);
		tab.parentId = i;
		tab.id = i;
		tab.type = 0;
		tab.actionType = 0;
		tab.width = w;
		tab.height = h;
		tab.opacity = 0;
		tab.hidden = true;
		tab.hoverId = -1;
		addHoverImage(IMAGEID, j, j, imageName);
		tab.totalChildren(1);
		tab.child(0, IMAGEID, 0, 0);
	}
	
	public static void addHoverImage(int i, int j, int k, String name) {
		RSInterface tab = addInterface(i);
		tab.id = i;
		tab.parentId = i;
		tab.type = 5;
		tab.actionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.opacity = 0;
		tab.hoverId = 52;
		tab.sprite1 = imageLoader(j, name, true);
		tab.sprite2 = imageLoader(k, name, true);
	}
	
	public static void addConfigButton(int id, int parentId, int spriteId1, int spriteId2,
			String spriteName, int width, int height, String tooltip, int configId, int actionType, int configFrame) {
        RSInterface Tab = addInterface(id);
        Tab.parentId = parentId;
        Tab.id = id;
        Tab.type = 5;
        Tab.actionType = actionType;
        Tab.contentType = 0;
        Tab.width = width;
        Tab.height = height;
        Tab.opacity = 0;
        Tab.hoverId = -1;
        Tab.valueCompareType = new int[1];
        Tab.requiredValue = new int[1];
        Tab.valueCompareType[0] = 1;
        Tab.requiredValue[0] = configId;
        Tab.valueIndexArray = new int[1][3];
        Tab.valueIndexArray[0][0] = 5;
        Tab.valueIndexArray[0][1] = configFrame;
        Tab.valueIndexArray[0][2] = 0;
        Tab.sprite1 = imageLoader(spriteId1, spriteName);
        Tab.sprite2 = imageLoader(spriteId2, spriteName);
        Tab.tooltip = tooltip;
    }
	
	public static Sprite grabSprite(int i, JagexArchive archive, String s) {
		long l = (TextClass.method585(s) << 8) + (long) i;
		Sprite sprite = (Sprite) spriteNodes.insertFromCache(l);
		if (sprite != null)
			return sprite;
		try {
			sprite = new Sprite(archive, s, i);
			spriteNodes.removeFromCache(sprite, l);
		} catch (Exception _ex) {
			return null;
		}
		return sprite;
	}

	public static void addNewPrayer(int id, int configId, int requiredValues, int spriteId, int hoverID, String prayerName, String tooltipBoxText) {
        RSInterface tab = addInterface(id);
        tab.id = id;
        tab.parentId = 5608;
        tab.type = 5;
        tab.actionType = 4;
        tab.contentType = 0;
        tab.sprite1 = getCacheSprite("prayerglow", 0);
        tab.sprite2 = null;
        tab.sprite1Name = "prayerglow";
        tab.sprite1Id = 0;
        tab.width = 34;
        tab.height = 34;
        tab.valueCompareType = new int[1];
        tab.requiredValue = new int[1];
        tab.valueCompareType[0] = 1;
        tab.requiredValue[0] = 0;
        tab.valueIndexArray = new int[1][3];
        tab.valueIndexArray[0][0] = 5;
        tab.valueIndexArray[0][1] = configId;
        tab.valueIndexArray[0][2] = 0;
        tab.tooltip = "Activate@or1@ " + prayerName;
        RSInterface tab2 = addInterface(id + 1);
        tab2.id = id + 1;
        tab2.parentId = 5608;
        tab2.type = 5;
        tab2.actionType = 0;
        tab2.contentType  = 0;
        tab2.hoverId = hoverID;
        tab2.sprite1 = getCacheSprite("newprayeron", spriteId);
        tab2.sprite2 = getCacheSprite("newprayeroff", spriteId);
        tab2.sprite1Name = "newprayeron";
        tab2.sprite2Name = "newprayeroff";
        tab2.sprite1Id = spriteId;
        tab2.sprite2Id = spriteId;
        tab2.width = 34;
        tab2.height = 34;
        tab2.valueCompareType = new int[1];
        tab2.requiredValue = new int[1];
        tab2.valueCompareType[0] = 2;
        tab2.requiredValue[0] = requiredValues;
        tab2.valueIndexArray = new int[1][3];
        tab2.valueIndexArray[0][0] = 2;
        tab2.valueIndexArray[0][1] = 5;
        tab2.valueIndexArray[0][2] = 0;
        RSInterface box = RSInterface.interfaceCache[hoverID] = new RSInterface();
		box.id = hoverID;
		box.parentId = 5608;
		box.type = 8;
		box.disabledText = tooltipBoxText;
		box.width = tab.sprite1.myWidth;
		box.height = tab.sprite1.myHeight;
    }
	
	public static void addCurse(int id, int configId, int requiredValues, int spriteId, int hoverId, String prayerName) {
        RSInterface tab = addInterface(id);
        tab.id = id;
        tab.parentId = 21000;
        tab.type = 5;
        tab.actionType = 4;
        tab.contentType = 0;
        tab.hoverId = hoverId;
        tab.sprite1 = grabSprite(0, interfaceArchive, "prayerglow");
        tab.sprite2 = grabSprite(1, interfaceArchive, "prayerglow");
        tab.width = 34;
        tab.height = 34;
        tab.valueCompareType = new int[1];
        tab.requiredValue = new int[1];
        tab.valueCompareType[0] = 1;
        tab.requiredValue[0] = 0;
        tab.valueIndexArray = new int[1][3];
        tab.valueIndexArray[0][0] = 5;
        tab.valueIndexArray[0][1] = configId;
        tab.valueIndexArray[0][2] = 0;
        tab.tooltip = "Activate@or1@ " + prayerName;
        RSInterface tab2 = addInterface(id + 1);
        tab2.id = id + 1;
        tab2.parentId = 21000;
        tab2.type = 5;
        tab2.actionType = 0;
        tab2.contentType  = 0;
        tab2.sprite1 = getCacheSprite("curseon", spriteId);
        tab2.sprite2 = getCacheSprite("curseoff", spriteId);
        tab2.width = 34;
        tab2.height = 34;
        tab2.valueCompareType = new int[1];
        tab2.requiredValue = new int[1];
        tab2.valueCompareType[0] = 2;
        tab2.requiredValue[0] = requiredValues;
        tab2.valueIndexArray = new int[1][3];
        tab2.valueIndexArray[0][0] = 2;
        tab2.valueIndexArray[0][1] = 5;
        tab2.valueIndexArray[0][2] = 0;
    }
	
	/**
	 * Returns a sprite gotten from the media archive file
	 * located inside the cache's main file.
	 * @param archiveName	The name of the archive inside the image archives -> media file.
	 * @param id			The id of the sprite inside the image archives -> media file.
	 */
	public static Sprite getCacheSprite(String archiveName, int id) {
		long l = (TextClass.method585(archiveName) << 8) + (long) id;
		Sprite sprite = (Sprite) spriteNodes.insertFromCache(l);
		if (sprite != null)
			return sprite;
		try {
			sprite = new Sprite(mediaArchive, archiveName, id);
			sprite.myDirectory = archiveName;
			spriteNodes.removeFromCache(sprite, l);
		} catch (Exception _ex) {
			return null;
		}
		return sprite;
	}
	
	/**
	 * The button that is going to be hovered
	 * @param id
	 * @param location
	 * @param sub
	 * @param index
	 * @param width
	 * @param height
	 * @param text
	 * @param contentType
	 * @param hoverOver
	 * @param actionType
	 */
	public static void addHoverButton(int id, String location, String sub, int index, int width, int height, String text, int contentType, int hoverOver, int actionType) {//hoverable button
        RSInterface tab = addInterface(id);
        tab.id = id;
        tab.parentId = id;
        tab.type = 5;
        tab.actionType = actionType;
        tab.contentType = contentType;
        tab.opacity = 0;
        tab.hoverId = hoverOver;
        tab.sprite1 = imageLoader(index, location, sub);
        tab.sprite2 = imageLoader(index, location, sub);
        tab.width = width;
        tab.height = height;
        tab.tooltip = text;
    }
    
    /**
     * What button it shows when it is hovered
     * @param i
     * @param imageName
     * @param sub
     * @param j
     * @param w
     * @param h
     * @param IMAGEID
     */
    public static void addHoveredButton(int i, String imageName, String sub, int j, int w, int h, int IMAGEID) {//hoverable button
        RSInterface tab = addInterface(i);
        tab.parentId = i;
        tab.id = i;
        tab.type = 0;
        tab.actionType = 0;
        tab.width = w;
        tab.height = h;
        tab.hidden = true;
        tab.opacity = 0;
        tab.hoverId = -1;
        tab.scrollMax = 0;
        addHoverImage(IMAGEID, j, j, imageName, sub);
        tab.totalChildren(1);
        tab.child(0, IMAGEID, 0, 0);
    }
    
    /**
     * Adding a sprite to a interface
     * @param id
     * @param spriteID
     * @param spriteName
     * @param subDir
     */
	public static void addSprite(int id, int spriteID, String spriteName, String subDir) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.id = id;
		rsi.parentId = id;
		rsi.type = 5;
		rsi.actionType = 0;
		rsi.contentType = 0;
		rsi.opacity = (byte)0;
		rsi.hoverId = 52;
		rsi.sprite1 = imageLoader(spriteID, spriteName, subDir);
		rsi.sprite2 = imageLoader(spriteID, spriteName, subDir); 
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite1.myHeight;
	}
	
	/**
	 * For equipment tab
	 * @param i
	 * @param j
	 * @param k
	 */
	public static void addSprite(int i, int j, int k) {
		RSInterface rsinterface = interfaceCache[i] = new RSInterface();
		rsinterface.id = i;
		rsinterface.parentId = i;
		rsinterface.type = 5;
		rsinterface.actionType = 1;
		rsinterface.contentType = 0;
		rsinterface.width = 20;
		rsinterface.height = 20;
		rsinterface.opacity = 0;
		rsinterface.hoverId = 52;
		rsinterface.sprite1 = imageLoader(j, "Equipment/SPRITE");
		rsinterface.sprite2 = imageLoader(k, "Equipment/SPRITE");
	}
	
	/**
	 * Adding a sprite to a interface that's transparent
	 * @param i
	 * @param j
	 * @param s
	 */
	public static void addTransparentSprite(int i, int j, String s)  {
        RSInterface rsinterface = interfaceCache[i] = new RSInterface();
        rsinterface.id = i;
        rsinterface.parentId = i;
        rsinterface.type = 5;
        rsinterface.actionType = 0;
        rsinterface.contentType = 0;
        rsinterface.opacity = 0;
        rsinterface.hoverId = 52;
        rsinterface.sprite1 = imageLoader(j, s);
        rsinterface.sprite2 = imageLoader(j, s);
        rsinterface.width = 512;
        rsinterface.height = 334;
        rsinterface.drawsTransparent = true;
    }
	
	/**
	 * Add Text to a interface
	 * @param id
	 * @param text
	 * @param tda
	 * @param idx
	 * @param color
	 * @param center
	 * @param shadow
	 */
	public static void addText(int id, String text, RSFontSystem[] tda, int idx, int color, boolean center, boolean shadow) {
		RSInterface rsi = addInterface(id);
		rsi.parentId = id;
		rsi.id = id;
		rsi.type = 4;
		rsi.actionType = 0;
		rsi.width = 0;
		rsi.height = 11;
		rsi.contentType = 0;
		rsi.opacity = 0;
		rsi.hoverId = -1;
		rsi.centerText = center;
		rsi.shadeText = shadow;
		rsi.textDrawingAreas = tda[idx];
		rsi.disabledText = text;
		rsi.enabledText = "";
		rsi.textColor = color;
	}
	
	public static void addText(int i, String s, int k, boolean l, boolean m, int a, RSFontSystem[] TDA, int j) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.parentId = i;
		RSInterface.id = i;
		RSInterface.type = 4;
		RSInterface.actionType = 0;
		RSInterface.width = 0;
		RSInterface.height = 0;
		RSInterface.contentType = 0;
		RSInterface.hoverId = a;
		RSInterface.centerText = l;
		RSInterface.shadeText = m;
		RSInterface.textDrawingAreas = TDA[j];
		RSInterface.disabledText = s;
		RSInterface.enabledText = "";
		RSInterface.textColor = k;
	}
	
	/**
	 * Adding a character to a interface
	 * @param id
	 */
	public static void addChar(int ID) { 
		RSInterface t = interfaceCache[ID] = new RSInterface(); 
		t.id = ID; 
		t.parentId = ID; 
		t.type = 6;
		t.actionType = 0; 
		t.contentType = 328; 
		t.width = 180; 
		t.height = 190; 
		t.opacity = 0;
		t.hoverId = 0;
		t.modelZoom = 560;
		t.modelRotation1 = 0;
		t.modelRotation2 = 0; 
		t.animationId = -1; 
		t.anInt258 = -1; 
	}
	
	/**
	 * Loads the lunar sprites
	 * @param i
	 * @param s
	 * @return
	 */
	public static Sprite loadLunarSprite(int i, String s) {
		Sprite sprite = imageLoader(i, s, "lunar");
		return sprite;
	}
	
	/**
	 * Lunar sprites
	 * @param i
	 * @param j
	 * @param name
	 */
	public static void addLunarSprite(int i, int j, String name) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parentId = i;
		RSInterface.type = 5;
		RSInterface.actionType = 0;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.hoverId = 52;
		RSInterface.sprite1 = loadLunarSprite(j, name);
		RSInterface.width = 500;
		RSInterface.height = 500;
		RSInterface.tooltip = "";
	}
	
	/**
	 * Draws the rune models for lunar interface
	 * @param i
	 * @param id
	 * @param runeName
	 */
	public static void drawRune(int i, int id, String runeName) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.type = 5;
		RSInterface.actionType = 0;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.hoverId = 52;
		RSInterface.sprite1 = loadLunarSprite(id, "RUNE");
		RSInterface.width = 500;
		RSInterface.height = 500;
	}
	
	/**
	 * Add text for the lunar interface
	 * @param i
	 * @param s
	 * @param k
	 * @param l
	 * @param m
	 * @param a
	 * @param j
	 * @param tda
	 */
	public static void addText(int i, String s, int k, boolean l, boolean m,
			int a, int j, RSFontSystem[] tda) {
		RSInterface rsinterface = addInterface(i);
		rsinterface.parentId = i;
		rsinterface.id = i;
		rsinterface.type = 4;
		rsinterface.actionType = 0;
		rsinterface.width = 0;
		rsinterface.height = 0;
		rsinterface.contentType = 0;
		rsinterface.opacity = 0;
		rsinterface.hoverId = a;
		rsinterface.centerText = l;
		rsinterface.shadeText = m;
		rsinterface.textDrawingAreas = tda[j];
		rsinterface.disabledText = s;
		rsinterface.textColor = k;
	}
}