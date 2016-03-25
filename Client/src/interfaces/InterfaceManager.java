package src.interfaces;

import src.JagexArchive;
import src.RSFontSystem;
import src.SpriteLoader;

public class InterfaceManager {
	
	public static void addSpriteLoaderButton(int childId, int spriteId, String tooltip) {
		RSInterface rsi = RSInterface.interfaceCache[childId] = new RSInterface();
		rsi.id = childId;
		rsi.parentId = childId;
		rsi.type = 5;
		rsi.actionType = 1;
		rsi.contentType = 0;
		rsi.opacity = 0;
		rsi.hoverId = 52;
		rsi.sprite1 = SpriteLoader.sprites[spriteId];
		rsi.sprite2 = SpriteLoader.sprites[spriteId];
		rsi.sprite1.spriteLoader = rsi.sprite2.spriteLoader = true;
		rsi.sprite1Id = rsi.sprite2Id = spriteId;
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite1.myHeight;
		rsi.tooltip = tooltip;
		rsi.isFalseTooltip = true;
	}
	
	public static void addSpriteLoaderButton(int childId, int spriteId, String tooltip, int actionType, int contentType) {
		RSInterface rsi = RSInterface.interfaceCache[childId] = new RSInterface();
		rsi.id = childId;
		rsi.parentId = childId;
		rsi.type = 5;
		rsi.actionType = actionType;
		rsi.contentType = contentType;
		rsi.opacity = 0;
		rsi.hoverId = 52;
		rsi.sprite1 = SpriteLoader.sprites[spriteId];
		rsi.sprite2 = SpriteLoader.sprites[spriteId];
		rsi.sprite1.spriteLoader = rsi.sprite2.spriteLoader = true;
		rsi.sprite1Id = rsi.sprite2Id = spriteId;
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite1.myHeight;
		rsi.tooltip = tooltip;
		rsi.isFalseTooltip = true;
	}
	
	public static void addSpriteLoaderHoverButton(int childId, int spriteId, String tooltip, int hoverSpriteId) {
		RSInterface rsi = RSInterface.interfaceCache[childId] = new RSInterface();
		rsi.id = childId;
		rsi.parentId = childId;
		rsi.type = 5;
		rsi.actionType = 1;
		rsi.contentType = 0;
		rsi.opacity = 0;
		rsi.hoverId = 52;
		rsi.sprite1 = SpriteLoader.sprites[spriteId];
		rsi.sprite2 = SpriteLoader.sprites[spriteId];
		rsi.sprite1.spriteLoader = rsi.sprite2.spriteLoader = true;
		rsi.hoverSprite1 = SpriteLoader.sprites[hoverSpriteId];
		rsi.hoverSprite2 = SpriteLoader.sprites[hoverSpriteId];
		rsi.hoverSprite1.spriteLoader = rsi.hoverSprite2.spriteLoader = true;
		rsi.sprite1Id = rsi.sprite2Id = spriteId;
		rsi.hoverSprite1Id = rsi.hoverSprite2Id = hoverSpriteId;
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite1.myHeight;
		rsi.tooltip = tooltip;
		rsi.isFalseTooltip = true;
	}
	
	public static void addSpriteLoaderHoverButton(int childId, int spriteId, String tooltip, int hoverSpriteId, int[] type) {
		if (type.length < 3)
			throw new IllegalArgumentException("Type array length must be equal to 3");
		RSInterface rsi = RSInterface.interfaceCache[childId] = new RSInterface();
		rsi.id = childId;
		rsi.parentId = childId;
		rsi.type = type[0];
		rsi.actionType = type[1];
		rsi.contentType = type[2];
		rsi.opacity = 0;
		rsi.hoverId = 52;
		rsi.sprite1 = SpriteLoader.sprites[spriteId];
		rsi.sprite2 = SpriteLoader.sprites[spriteId];
		rsi.sprite1.spriteLoader = rsi.sprite2.spriteLoader = true;
		rsi.hoverSprite1 = SpriteLoader.sprites[hoverSpriteId];
		rsi.hoverSprite2 = SpriteLoader.sprites[hoverSpriteId];
		rsi.hoverSprite1.spriteLoader = rsi.hoverSprite2.spriteLoader = true;
		rsi.sprite1Id = rsi.sprite2Id = spriteId;
		rsi.hoverSprite1Id = rsi.hoverSprite2Id = hoverSpriteId;
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite1.myHeight;
		rsi.tooltip = tooltip;
		rsi.isFalseTooltip = true;
	}
	
	public static void addSpriteLoaderHoverButtonWithTooltipBox(int childId, int spriteId, String tooltip, int hoverSpriteId, int tooltipBoxChildId, String tooltipBoxText) {
		RSInterface rsi = RSInterface.interfaceCache[childId] = new RSInterface();
		rsi.id = childId;
		rsi.parentId = childId;
		rsi.type = 5;
		rsi.actionType = 1;
		rsi.contentType = 0;
		rsi.opacity = 0;
		rsi.hoverId = tooltipBoxChildId;
		rsi.sprite1 = SpriteLoader.sprites[spriteId];
		rsi.sprite2 = SpriteLoader.sprites[spriteId];
		rsi.sprite1.spriteLoader = rsi.sprite2.spriteLoader = true;
		rsi.hoverSprite1 = SpriteLoader.sprites[hoverSpriteId];
		rsi.hoverSprite2 = SpriteLoader.sprites[hoverSpriteId];
		rsi.hoverSprite1.spriteLoader = rsi.hoverSprite2.spriteLoader = true;
		rsi.sprite1Id = rsi.sprite2Id = spriteId;
		rsi.hoverSprite1Id = rsi.hoverSprite2Id = hoverSpriteId;
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite1.myHeight;
		rsi.tooltip = tooltip;
		rsi.isFalseTooltip = true;
		RSInterface box = RSInterface.interfaceCache[tooltipBoxChildId] = new RSInterface();
		box.id = tooltipBoxChildId;
		box.parentId = rsi.id;
		box.type = 8;
		box.disabledText = tooltipBoxText;
		box.width = rsi.sprite1.myWidth;
		box.height = rsi.sprite1.myHeight;
	}
	
	public static void addSpriteLoaderSprite(int childId, int spriteId) {
		RSInterface rsi = RSInterface.interfaceCache[childId] = new RSInterface();
		rsi.id = childId;
		rsi.parentId = childId;
		rsi.type = 5;
		rsi.actionType = 0;
		rsi.contentType = 0;
		rsi.opacity = 0;
		rsi.hoverId = 52;
		rsi.sprite1 = SpriteLoader.sprites[spriteId];
		rsi.sprite2 = SpriteLoader.sprites[spriteId];
		rsi.sprite1.spriteLoader = rsi.sprite2.spriteLoader = true;
		rsi.sprite1Id = rsi.sprite2Id = spriteId;
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite1.myHeight;
	}
	
	public static void addSpriteLoaderSprite(int childId, int spriteId, int width, int height) {
		RSInterface rsi = RSInterface.interfaceCache[childId] = new RSInterface();
		rsi.id = childId;
		rsi.parentId = childId;
		rsi.type = 5;
		rsi.actionType = 0;
		rsi.contentType = 0;
		rsi.opacity = 0;
		rsi.hoverId = 52;
		rsi.sprite1 = SpriteLoader.sprites[spriteId];
		rsi.sprite2 = SpriteLoader.sprites[spriteId];
		rsi.sprite1.spriteLoader = rsi.sprite2.spriteLoader = true;
		rsi.sprite1Id = rsi.sprite2Id = spriteId;
		rsi.width = width;
		rsi.height = height;
	}
	
	public static void addSpriteLoaderConfigButton(int parentId, int childId, int spriteId, int otherSpriteId, int actionType, int configId, int requiredValue, String tooltip) {
		RSInterface rsi = RSInterface.addInterface(childId);
		rsi.parentId = parentId;
		rsi.id = childId;
		rsi.type = 5;
		rsi.actionType = actionType;
		rsi.contentType = 0;
		rsi.opacity = 0;
		rsi.hoverId = -1;
		rsi.valueCompareType = new int[1];
		rsi.requiredValue = new int[1];
		rsi.valueCompareType[0] = 1;
		rsi.requiredValue[0] = requiredValue;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = configId;
		rsi.valueIndexArray[0][2] = 0;
		rsi.sprite1 = SpriteLoader.sprites[spriteId];
		rsi.sprite2 = SpriteLoader.sprites[otherSpriteId];
		rsi.sprite1.spriteLoader = rsi.sprite2.spriteLoader = true;
		rsi.sprite1Id = spriteId;
		rsi.sprite2Id = otherSpriteId;
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite1.myHeight;
		rsi.tooltip = tooltip;
	}
	
	public static void addSpriteLoaderConfigButtonWithTooltipBox(int parentId, int childId, int spriteId, int otherSpriteId, int actionType, int configId, int requiredValue, String tooltip, int tooltipBoxChildId, String tooltipBoxText) {
		RSInterface rsi = RSInterface.addInterface(childId);
		rsi.parentId = parentId;
		rsi.id = childId;
		rsi.type = 5;
		rsi.actionType = actionType;
		rsi.contentType = 0;
		rsi.opacity = 0;
		rsi.hoverId = -1;
		rsi.valueCompareType = new int[1];
		rsi.requiredValue = new int[1];
		rsi.valueCompareType[0] = 1;
		rsi.requiredValue[0] = requiredValue;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = configId;
		rsi.valueIndexArray[0][2] = 0;
		rsi.sprite1 = SpriteLoader.sprites[spriteId];
		rsi.sprite2 = SpriteLoader.sprites[otherSpriteId];
		rsi.sprite1.spriteLoader = rsi.sprite2.spriteLoader = true;
		rsi.sprite1Id = spriteId;
		rsi.sprite2Id = otherSpriteId;
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite1.myHeight;
		rsi.tooltip = tooltip;
		RSInterface box = RSInterface.interfaceCache[tooltipBoxChildId] = new RSInterface();
		box.id = tooltipBoxChildId;
		box.parentId = rsi.id;
		box.type = 8;
		box.disabledText = tooltipBoxText;
		box.width = rsi.sprite1.myWidth;
		box.height = rsi.sprite1.myHeight;
	}
	
	public static void addCacheButton(int childId, int spriteId, String tooltip, JagexArchive archive, String cacheDirectory) {
		RSInterface rsi = RSInterface.interfaceCache[childId] = new RSInterface();
		rsi.id = childId;
		rsi.parentId = childId;
		rsi.type = 5;
		rsi.actionType = 1;
		rsi.contentType = 0;
		rsi.opacity = 0;
		rsi.hoverId = 52;
		rsi.sprite1 = RSInterface.loadCacheSprite(spriteId, archive, cacheDirectory);
		rsi.sprite2 = RSInterface.loadCacheSprite(spriteId, archive, cacheDirectory);
		rsi.sprite1Name = rsi.sprite1.myDirectory = cacheDirectory;
		rsi.sprite1Id = spriteId;
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite1.myHeight;
		rsi.tooltip = tooltip;
		rsi.isFalseTooltip = true;
	}
	
	public static void addCacheButton(int childId, int spriteId, String tooltip, JagexArchive archive, String cacheDirectory, int actionType, int contentType) {
		RSInterface rsi = RSInterface.interfaceCache[childId] = new RSInterface();
		rsi.id = childId;
		rsi.parentId = childId;
		rsi.type = 5;
		rsi.actionType = actionType;
		rsi.contentType = contentType;
		rsi.opacity = 0;
		rsi.hoverId = 52;
		rsi.sprite1 = RSInterface.loadCacheSprite(spriteId, archive, cacheDirectory);
		rsi.sprite2 = RSInterface.loadCacheSprite(spriteId, archive, cacheDirectory);
		rsi.sprite1Name = rsi.sprite1.myDirectory = cacheDirectory;
		rsi.sprite1Id = spriteId;
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite1.myHeight;
		rsi.tooltip = tooltip;
		rsi.isFalseTooltip = true;
	}
	
	public static void addCacheHoverButton(int childId, int spriteId, String tooltip, int hoverSpriteId, JagexArchive archive, String cacheDirectory) {
		RSInterface rsi = RSInterface.interfaceCache[childId] = new RSInterface();
		rsi.id = childId;
		rsi.parentId = childId;
		rsi.type = 5;
		rsi.actionType = 1;
		rsi.contentType = 0;
		rsi.opacity = 0;
		rsi.hoverId = 52;
		rsi.sprite1 = RSInterface.loadCacheSprite(spriteId, archive, cacheDirectory);
		rsi.sprite2 = RSInterface.loadCacheSprite(spriteId, archive, cacheDirectory);
		rsi.hoverSprite1 = RSInterface.loadCacheSprite(hoverSpriteId, archive, cacheDirectory);
		rsi.hoverSprite2 = RSInterface.loadCacheSprite(hoverSpriteId, archive, cacheDirectory);
		rsi.sprite1Name = rsi.sprite1.myDirectory = cacheDirectory;
		rsi.sprite1Id = spriteId;
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite1.myHeight;
		rsi.tooltip = tooltip;
		rsi.isFalseTooltip = true;
	}
	
	public static void addCacheSprite(int childId, int spriteId, JagexArchive archive, String cacheDirectory) {
		RSInterface rsi = RSInterface.interfaceCache[childId] = new RSInterface();
		rsi.id = childId;
		rsi.parentId = childId;
		rsi.type = 5;
		rsi.actionType = 0;
		rsi.contentType = 0;
		rsi.opacity = 0;
		rsi.hoverId = 52;
		rsi.sprite1 = RSInterface.loadCacheSprite(spriteId, archive, cacheDirectory);
		rsi.sprite2 = RSInterface.loadCacheSprite(spriteId, archive, cacheDirectory);
		rsi.sprite1Name = cacheDirectory;
		rsi.sprite1Id = spriteId;
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite1.myHeight;
	}
	
	public static void addCacheConfigButton(int parentId, int childId, int spriteId, int otherSpriteId, int actionType, int configId, int requiredValue, String tooltip, JagexArchive archive, String cacheDirectory) {
		RSInterface rsi = RSInterface.addInterface(childId);
		rsi.parentId = parentId;
		rsi.id = childId;
		rsi.type = 5;
		rsi.actionType = actionType;
		rsi.contentType = 0;
		rsi.opacity = 0;
		rsi.hoverId = -1;
		rsi.valueCompareType = new int[1];
		rsi.requiredValue = new int[1];
		rsi.valueCompareType[0] = 1;
		rsi.requiredValue[0] = requiredValue;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = configId;
		rsi.valueIndexArray[0][2] = 0;
		rsi.sprite1 = RSInterface.loadCacheSprite(spriteId, archive, cacheDirectory);
		rsi.sprite2 = RSInterface.loadCacheSprite(otherSpriteId, archive, cacheDirectory);
		rsi.sprite1Name = rsi.sprite1.myDirectory = cacheDirectory;
		rsi.sprite1Id = spriteId;
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite1.myHeight;
		rsi.tooltip = tooltip;
	}
	
	public static void addText(int childId, String text, int color, boolean center, boolean shadow, RSFontSystem rsFont) {
		RSInterface rsi = RSInterface.addInterface(childId);
		rsi.parentId = childId;
		rsi.id = childId;
		rsi.type = 4;
		rsi.actionType = 0;
		rsi.width = 0;
		rsi.height = 11;
		rsi.contentType = 0;
		rsi.opacity = 0;
		rsi.hoverId = -1;
		rsi.centerText = center;
		rsi.shadeText = shadow;
		rsi.textDrawingAreas = rsFont;
		rsi.disabledText = text;
		rsi.enabledText = "";
		rsi.textColor = color;
	}
	
	public static void addMenu(int parentId, int childId, int colour, RSFontSystem font, String[] text) {
		RSInterface rsi = RSInterface.addInterface(childId);
		rsi.parentId = parentId;
		rsi.id = childId;
		rsi.type = 4;
		rsi.actionType = 11;
		rsi.width = 19;
		rsi.height = 11;
		rsi.contentType = 0;
		rsi.opacity = 0;
		rsi.hoverId = -1;
		rsi.textDrawingAreas = font;
		rsi.disabledText = "";
		rsi.textColor = colour;
		rsi.menuActions = text;
		rsi.menuId = childId;
	}
}
