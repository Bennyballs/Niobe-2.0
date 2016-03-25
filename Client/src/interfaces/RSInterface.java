package src.interfaces;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import src.Animation;
import src.Cache;
import src.Client;
import src.ItemDef;
import src.JagexArchive;
import src.JagexBuffer;
import src.Model;
import src.NPCDefinition;
import src.RSFontSystem;
import src.Sprite;
import src.SpriteLoader;
import src.TextClass;
import src.sign.signlink;

public class RSInterface {

	public void swapInventoryItems(int i, int j) {
		int k = inventory[i];
		inventory[i] = inventory[j];
		inventory[j] = k;
		k = inventoryAmount[i];
		inventoryAmount[i] = inventoryAmount[j];
		inventoryAmount[j] = k;
	}
	
	public static void pack2() {
		try {
			JagexBuffer buffer = new JagexBuffer(new byte[1297086]);
			buffer.writeShort(interfaceCache.length);
			for (RSInterface rsi : interfaceCache) {
				if (rsi == null) {
					continue;
				}
				int parent = rsi.parentId;
				if (rsi.parentId != -1) {
					buffer.writeShort(65535);
					buffer.writeShort(parent);
					buffer.writeShort(rsi.id);
				} else {
					buffer.writeShort(rsi.id);
				}
				buffer.writeByte(rsi.isTabChild ? 1 : 0);
				buffer.writeByte(rsi.type);
				buffer.writeByte(rsi.actionType);
				buffer.writeShort(rsi.contentType);
				buffer.writeShort(rsi.width);
				buffer.writeShort(rsi.height);
				buffer.writeByte(rsi.opacity);
				if (rsi.hoverId != -1) {
					buffer.writeShortSpaceSaver(rsi.hoverId);
				} else {
					buffer.writeByte(0);
				}
				int array245Count = 0;
				if (rsi.valueCompareType != null) {
					array245Count = rsi.valueCompareType.length;
				}
				buffer.writeByte(array245Count);
				if (array245Count > 0) {
					for (int i = 0; i < array245Count; i++) {
						buffer.writeByte(rsi.valueCompareType[i]);
						buffer.writeShort(rsi.requiredValue[i]);
					}
				}
				int valueLength = 0;
				if (rsi.valueIndexArray != null) {
					valueLength = rsi.valueIndexArray.length;
				}
				buffer.writeByte(valueLength);
				if (valueLength > 0) {
					for (int l1 = 0; l1 < valueLength; l1++) {
						int i3 = rsi.valueIndexArray[l1].length;
						buffer.writeShort(i3);
						for (int l4 = 0; l4 < i3; l4++) {
							buffer.writeShort(rsi.valueIndexArray[l1][l4]);
						}
					}
				}
				if (rsi.type == 0) {
					buffer.writeShort(rsi.scrollMax);
					buffer.writeByte(rsi.hidden ? 1 : 0);
					buffer.writeShort(rsi.children != null ? rsi.children.length : 0);
					if (rsi.children != null) {
						for (int i = 0; i < rsi.children.length; i++) {
							if (i >= rsi.childX.length || i >= rsi.childY.length)
								continue;
							buffer.writeShort(rsi.children[i]);
							buffer.writeShort(rsi.childX[i]);
							buffer.writeShort(rsi.childY[i]);
						}
					}
				}
				if (rsi.type == 1) {
					buffer.writeShort(0);
					buffer.writeByte(0);
				}
				if (rsi.type == 2) {

					buffer.writeByte(rsi.aBoolean259 ? 1 : 0);
					buffer.writeByte(rsi.isInventoryInterface ? 1 : 0);
					buffer.writeByte(rsi.usableItemInterface ? 1 : 0);
					buffer.writeByte(rsi.aBoolean235 ? 1 : 0);
					buffer.writeByte(rsi.invSpritePadX);
					buffer.writeByte(rsi.invSpritePadY);
					for (int i = 0; i < 20; i++) {
						int mediaType = rsi.sprites != null && rsi.sprites[i] != null ? 1 : 0;
						buffer.writeByte(mediaType);
						if (mediaType > 0) {
							buffer.writeShort(rsi.spritesX[i]);
							buffer.writeShort(rsi.spritesY[i]);
							buffer.writeString(rsi.sprites[i].myDirectory);
						}
					}
					for (int i = 0; i < 5; i++) {
						if (rsi.actions != null && rsi.actions[i] != null) {
							buffer.writeString(rsi.actions[i]);
						} else {
							buffer.writeString("");
						}
					}
				}
				if (rsi.type == 3) {
					buffer.writeByte(rsi.aBoolean227 ? 1 : 0);
				}
				if (rsi.type == 4 || rsi.type == 1) {
					buffer.writeByte(rsi.centerText ? 1 : 0);
					buffer.writeByte(rsi.fontId);
					buffer.writeByte(rsi.shadeText ? 1 : 0);
				}
				if (rsi.type == 4) {
					buffer.writeString(rsi.disabledText);
					if (rsi.enabledText != null)
						buffer.writeString(rsi.enabledText);
					else
						buffer.writeString("null");
				}
				if (rsi.type == 1 || rsi.type == 3 || rsi.type == 4)
					buffer.writeInt(rsi.textColor);
				if (rsi.type == 3 || rsi.type == 4) {
					buffer.writeInt(rsi.anInt219);
					buffer.writeInt(rsi.anInt216);
					buffer.writeInt(rsi.anInt239);
				}
				if (rsi.type == 5) {
					buffer.writeString(rsi.sprite1 != null ? "spriteloader" + rsi.sprite1Name + "," + rsi.sprite1Id : "");
					buffer.writeString(rsi.sprite2 != null ? "spriteloader" + rsi.sprite2Name + "," + rsi.sprite2Id : "");
					/*if (screen.hoverSprite1 != null) {
						// if(screen.hoverSprite1.myDirectory != null) {
						byteV.writeString(screen.hoverSprite1Name + ","
								+ screen.hoverSprite1ID);
						System.out.println(screen.hoverSprite1Name + ","
								+ screen.hoverSprite1ID);
						// } else {
						// byteV.writeString("");
						// }
					} else {
						byteV.writeString("");
					}
					if (screen.hoverSprite2 != null) {
						// if(screen.hoverSprite2.myDirectory != null) {
						byteV.writeString(screen.hoverSprite2Name + ","
								+ screen.hoverSprite2ID);
						System.out.println(screen.hoverSprite2Name + ","
								+ screen.hoverSprite2ID);
						// } else {
						// byteV.writeString("");
						// }
					} else {
						byteV.writeString("");
					}*/
				} else if (rsi.type == 6) {
					if (rsi.mediaType != -1 && rsi.mediaId > 0) {
						buffer.writeShortSpaceSaver(rsi.mediaId);
					} else {
						buffer.writeByte(0);
					}
					if (rsi.anInt256 > 0) {
						buffer.writeShortSpaceSaver(rsi.anInt256);
					} else {
						buffer.writeByte(0);
					}
					if (rsi.animationId > 0) {
						buffer.writeShortSpaceSaver(rsi.animationId);
					} else {
						buffer.writeByte(0);
					}
					if (rsi.anInt258 > 0) {
						buffer.writeShortSpaceSaver(rsi.anInt258);
					} else {
						buffer.writeByte(0);
					}
					buffer.writeShort(rsi.modelZoom);
					buffer.writeShort(rsi.modelRotation1);
					buffer.writeShort(rsi.modelRotation2);
				} else if (rsi.type == 7) {
					buffer.writeByte(rsi.centerText ? 1 : 0);
					buffer.writeByte(rsi.fontId);
					buffer.writeByte(rsi.shadeText ? 1 : 0);
					buffer.writeInt(rsi.textColor);
					buffer.writeShort(rsi.invSpritePadX);
					buffer.writeShort(rsi.invSpritePadY);
					buffer.writeByte(rsi.isInventoryInterface ? 1 : 0);
					for (int i = 0; i < 5; i++) {
						if (rsi.actions[i] != null) {
							buffer.writeString(rsi.actions[i]);
						} else {
							buffer.writeString("");
						}
					}
				}

				if (rsi.actionType == 2 || rsi.type == 2) {
					buffer.writeString(rsi.selectedActionName != null ? rsi.selectedActionName : "");
					buffer.writeString(rsi.spellName != null ? rsi.spellName : "");
					buffer.writeShort(rsi.spellUsableOn);
				}

				if (rsi.type == 8)
					buffer.writeString(rsi.disabledText);

				if (rsi.actionType == 1 || rsi.actionType == 4
						|| rsi.actionType == 5 || rsi.actionType == 6) {
					buffer.writeString(rsi.tooltip != null ? rsi.tooltip : "");
				}
				buffer.writeByte(rsi.fillWithColor ? 1 : 0);
				if (rsi.fillWithColor) {
					buffer.writeString(rsi.colorFill);
				}
			}
			System.out.println("buffer.length=" + buffer.buffer.length + "; offset=" + buffer.currentOffset);
			java.io.DataOutputStream dos = new java.io.DataOutputStream(
					new java.io.FileOutputStream(signlink.getCacheLocation() + "data.dat"));
			dos.write(buffer.buffer, 0, buffer.currentOffset);
			dos.close();
			System.out.println("Interfaces saved.");
		} catch (IOException e) {
			System.out.println("Failed to save interfaces.");
			e.printStackTrace();
		}
	}
	
	public static void pack() {
		try {
			JagexBuffer buffer = new JagexBuffer(new byte[1085790]);
			buffer.writeShort(interfaceCache.length);
			for (RSInterface rsi : interfaceCache) {
				if (rsi == null)
					continue;
				try {
					int parent = rsi.parentId;
					if (rsi.parentId != -1) {
						buffer.writeShort(65535);
						buffer.writeShort(parent);
						buffer.writeShort(rsi.id);
					} else {
						buffer.writeShort(rsi.id);
					}
					buffer.writeByte(rsi.isTabChild ? 1 : 0);
					buffer.writeByte(rsi.type);
					buffer.writeByte(rsi.actionType);
					buffer.writeShort(rsi.contentType);
					buffer.writeShort(rsi.width);
					buffer.writeShort(rsi.height);
					buffer.writeByte(rsi.opacity);
					buffer.writeByte(rsi.hoverId);
					if (rsi.hoverId != 0) {
						buffer.writeByte((rsi.hoverId >> 8) + 1);
					}
					buffer.writeByte(rsi.valueCompareType != null ? rsi.valueCompareType.length : 0);
					if (rsi.valueCompareType != null && rsi.valueCompareType.length > 0) {
						for (int i = 0; i < rsi.valueCompareType.length; i++) {
							buffer.writeByte(rsi.valueCompareType[i]);
							buffer.writeShort(rsi.requiredValue[i]);
						}
					}
					buffer.writeByte(rsi.valueIndexArray != null ? rsi.valueIndexArray.length : 0);
					if (rsi.valueIndexArray != null && rsi.valueIndexArray.length > 0) {
						for (int i = 0; i < rsi.valueIndexArray.length; i++) {
							buffer.writeByte(rsi.valueIndexArray[i].length);
							for (int j = 0; j < rsi.valueIndexArray[i].length; j++) {
								buffer.writeShort(rsi.valueIndexArray[i][j]);
								//System.out.println("rsi.valueIndexArray[" + i + "][" + j + "]=" + rsi.valueIndexArray[i][j]);
							}
						}
					}
					if (rsi.type == 0) {
						buffer.writeShort(rsi.scrollMax);
						buffer.writeByte(rsi.hidden ? 1 : 0);
						buffer.writeShort(rsi.children != null ? rsi.children.length : 0);
						if (rsi.children != null) {
							for (int i = 0; i < rsi.children.length; i++) {
								if (i >= rsi.childX.length || i >= rsi.childY.length) {
									//System.out.println("Revise childX and(or) childY length with children length for child: " + rsi.id);
									break;
								}
								buffer.writeShort(rsi.children[i]);
								buffer.writeShort(rsi.childX[i]);
								buffer.writeShort(rsi.childY[i]);
							}
						}
					}
					if (rsi.type == 1) {
						buffer.writeShort(0);
						buffer.writeByte(0);
					}
					if (rsi.type == 2) {
						buffer.writeByte(rsi.aBoolean259 ? 1 : 0);
						buffer.writeByte(rsi.isInventoryInterface ? 1 : 0);
						buffer.writeByte(rsi.usableItemInterface ? 1 : 0);
						buffer.writeByte(rsi.aBoolean235 ? 1 : 0);
						buffer.writeByte(rsi.invSpritePadX);
						buffer.writeByte(rsi.invSpritePadY);
						for (int i = 0; i < 20; i++) {
							buffer.writeByte(rsi.sprites != null && rsi.sprites[i] != null ? 1 : 0);
							if (rsi.sprites != null && rsi.sprites[i] != null) {
								buffer.writeShort(rsi.spritesX[i]);
								buffer.writeShort(rsi.spritesY[i]);
								buffer.writeString(rsi.sprites[i].myDirectory);
							}
						}
						for (int i = 0; i < 5; i++) {
							buffer.writeString(rsi.actions[i] != null ? rsi.actions[i] : "");
						}
					}
					if (rsi.type == 3) {
						buffer.writeByte(rsi.aBoolean227 ? 1 : 0);
					}
					if (rsi.type == 4 || rsi.type == 1) {
						buffer.writeByte(rsi.centerText ? 1 : 0);
						buffer.writeByte(rsi.fontId);
						buffer.writeByte(rsi.shadeText ? 1 : 0);
					}
					if (rsi.type == 4) {
						buffer.writeString(rsi.disabledText);
						buffer.writeString(rsi.enabledText);
					}
					if (rsi.type == 1 || rsi.type == 3 || rsi.type == 4) {
						buffer.writeInt(rsi.textColor);
					}
					if (rsi.type == 3 || rsi.type == 4) {
						buffer.writeInt(rsi.anInt219);
						buffer.writeInt(rsi.anInt216);
						buffer.writeInt(rsi.anInt239);
					}
					if (rsi.type == 5) {
						String sprite = rsi.sprite1 != null && rsi.sprite1Name != null ? rsi.sprite1Name : "";
						buffer.writeString(sprite);
						if (sprite.length() > 0) {
							buffer.writeByte(rsi.sprite1.spriteLoader ? 1 : 0);
						}
						sprite = rsi.sprite2 != null && rsi.sprite2Name != null ? rsi.sprite2Name : "";
						buffer.writeString(sprite);
						if (sprite.length() > 0) {
							buffer.writeByte(rsi.sprite2.spriteLoader ? 1 : 0);
						}
						sprite = rsi.hoverSprite1 != null && rsi.hoverSprite1Name != null ? rsi.hoverSprite1Name : "";
						buffer.writeString(sprite);
						if (sprite.length() > 0) {
							buffer.writeByte(rsi.hoverSprite1.spriteLoader ? 1 : 0);
						}
						sprite = rsi.hoverSprite2 != null && rsi.hoverSprite2Name != null ? rsi.hoverSprite2Name : "";
						buffer.writeString(sprite);
						if (sprite.length() > 0) {
							buffer.writeByte(rsi.hoverSprite2.spriteLoader ? 1 : 0);
						}
					}
					if (rsi.type == 6) {
						buffer.writeByte(rsi.mediaId > 0 ? 1 : 0);
						if (rsi.mediaId > 0) {
							buffer.writeByte((rsi.mediaId >> 8) + 1);
						}
						buffer.writeByte(rsi.anInt256 > 0 ? 1 : 0);
						if (rsi.anInt256 > 0) {
							buffer.writeByte((rsi.anInt256 >> 8) + 1);
						}
						buffer.writeByte(rsi.animationId > 0 ? 1 : 0);
						if (rsi.animationId > 0) {
							buffer.writeByte((rsi.animationId >> 8) + 1);
						}
						buffer.writeByte(rsi.anInt258 > 0 ? 1 : 0);
						if (rsi.anInt258 > 0) {
							buffer.writeByte((rsi.anInt258 >> 8) + 1);
						}
						buffer.writeShort(rsi.modelZoom);
						buffer.writeShort(rsi.modelRotation1);
						buffer.writeShort(rsi.modelRotation2);
					}
					if (rsi.type == 7) {
						buffer.writeByte(rsi.centerText ? 1 : 0);
						buffer.writeByte(rsi.fontId);
						buffer.writeByte(rsi.shadeText ? 1 : 0);
						buffer.writeInt(rsi.textColor);
						buffer.writeShort(rsi.invSpritePadX);
						buffer.writeShort(rsi.invSpritePadY);
						buffer.writeByte(rsi.isInventoryInterface ? 1 : 0);
						for (int i = 0; i < 5; i++) {
							buffer.writeString(rsi.actions[i] != null ? rsi.actions[i] : "");
						}
					}
					if (rsi.actionType == 2 || rsi.type == 2) {
						buffer.writeString(rsi.selectedActionName != null ? rsi.selectedActionName : "");
						buffer.writeString(rsi.spellName != null ? rsi.spellName : "");
						buffer.writeShort(rsi.spellUsableOn);
					}
					if (rsi.type == 8) {
						buffer.writeString(rsi.disabledText);
					}
					if (rsi.actionType == 1 || rsi.actionType >= 4 && rsi.actionType <= 6) {
						buffer.writeString(rsi.tooltip != null ? rsi.tooltip : "");
					}
					if (rsi.actionType == 7 && rsi.fillWithColor) {
						buffer.writeString(rsi.colorFill);
					}
				} catch (Exception exception) {
					exception.printStackTrace();
					System.out.println("Error packing childId: " + rsi.id);
				}
			}
			System.out.println("currentOffset=" + buffer.currentOffset);
			DataOutputStream output = new DataOutputStream(new FileOutputStream(signlink.getCacheLocation() + "data.dat"));
			output.write(buffer.buffer, 0, buffer.currentOffset);
			output.close();
			System.out.println("Successfully saved new data file in cache location.");
		} catch (IOException exception) {
			exception.printStackTrace();
			System.out.println("Error saving data file.");
		}
	}
	
	public static void unpack2(JagexArchive interfaceArchive, RSFontSystem[] fonts, JagexArchive mediaArchive) {
		spriteNodes = new Cache(50000);
		JagexBuffer buffer = new JagexBuffer(interfaceArchive.getData("data"));
		int parentId = -1;
		int length = buffer.readUnsignedShort();
		interfaceCache = new RSInterface[length + 30000];
		while (buffer.currentOffset < buffer.buffer.length) {
			try {
				int childId = buffer.readUnsignedShort();
				System.out.println("id=" + childId + "; parentId=" + parentId);
				if (childId == 65535) {
					parentId = buffer.readUnsignedShort();
					childId = buffer.readUnsignedShort();
				}
				RSInterface rsi = interfaceCache[childId] = new RSInterface();
				try{
				rsi.id = childId;
				rsi.parentId = parentId;
				rsi.isTabChild = buffer.readUnsignedByte() == 1;
				rsi.type = buffer.readUnsignedByte();
				rsi.actionType = buffer.readUnsignedByte();
				rsi.contentType = buffer.readUnsignedShort();
				rsi.width = buffer.readUnsignedShort();
				rsi.height = buffer.readUnsignedShort();
				rsi.opacity = (byte) buffer.readUnsignedByte();
				int hoverId = buffer.readUnsignedByte();
				if (hoverId != 0) {
					rsi.hoverId = hoverId - 1 << 8;
				}
				int valueLength = buffer.readUnsignedByte();
				if (valueLength > 0) {
					rsi.valueCompareType = new int[valueLength];
					rsi.requiredValue = new int[valueLength];
					for (int i = 0; i < valueLength; i++) {
						rsi.valueCompareType[i] = buffer.readUnsignedByte();
						rsi.requiredValue[i] = buffer.readUnsignedShort();
					}
				}
				int indexLength = buffer.readUnsignedByte();
				if (indexLength > 0) {
					rsi.valueIndexArray = new int[indexLength][];
					for (int i = 0; i < indexLength; i++) {
						int arrayLength = buffer.readUnsignedByte();
						rsi.valueIndexArray[i] = new int[arrayLength];
						for (int j = 0; j < arrayLength; j++) {
							rsi.valueIndexArray[i][j] = buffer.readUnsignedShort();
						}
					}
				}
				if (rsi.type == 0) {
					rsi.drawsTransparent = false;
					rsi.scrollMax = buffer.readUnsignedShort();
					rsi.hidden = buffer.readUnsignedByte() == 1;
					int childLength = buffer.readUnsignedShort();
					rsi.children = rsi.childX = rsi.childY = new int[childLength];
					for (int i = 0; i < rsi.children.length; i++) {
						rsi.children[i] = buffer.readUnsignedShort();
						rsi.childX[i] = buffer.readUnsignedShort();
						rsi.childY[i] = buffer.readUnsignedShort();
					}
				}
				if (rsi.type == 1) {
					buffer.readUnsignedShort();
					buffer.readUnsignedByte();
				}
				if (rsi.type == 2) {
					rsi.inventory = new int[rsi.width * rsi.height];
					rsi.inventoryAmount = new int[rsi.width * rsi.height];
					rsi.aBoolean259 = buffer.readUnsignedByte() == 1;
					rsi.isInventoryInterface = buffer.readUnsignedByte() == 1;
					rsi.usableItemInterface = buffer.readUnsignedByte() == 1;
					rsi.aBoolean235 = buffer.readUnsignedByte() == 1;
					rsi.invSpritePadX = buffer.readUnsignedByte();
					rsi.invSpritePadY = buffer.readUnsignedByte();
					rsi.spritesX = new int[20];
					rsi.spritesY = new int[20];
					rsi.sprites = new Sprite[20];
					for (int i = 0; i < 20; i++) {
						boolean read = buffer.readUnsignedByte() == 1;
						if (read) {
							rsi.spritesX[i] = buffer.readUnsignedShort();
							rsi.spritesY[i] = buffer.readUnsignedShort();
							rsi.sprites[i].myDirectory = buffer.readString();
							String directory = rsi.sprites[i].myDirectory;
							if (directory.length() > 0) {
								int i5 = directory.lastIndexOf(",");
								rsi.sprites[i] = loadCacheSprite(Integer.parseInt(directory.substring(i5 + 1)), mediaArchive, directory.substring(0, i5));
							}
						}
					}
					rsi.actions = new String[5];
					for (int i = 0; i < 5; i++) {
						rsi.actions[i] = buffer.readString();
						if (rsi.actions[i].length() == 0) {
							rsi.actions[i] = null;
						} 
						if (rsi.parentId == 1644) {
							rsi.actions[2] = "Operate";
						}
						if (rsi.parentId == 3822) {
							rsi.actions[4] = "Sell X";
						}
						if (rsi.parentId == 3824) {
							rsi.actions[4] = "Buy X";
						}
					}
				}
				if (rsi.type == 3) {
					rsi.aBoolean227 = buffer.readUnsignedByte() == 1;
				}
				if (rsi.type == 4 || rsi.type == 1) {
					rsi.centerText = buffer.readUnsignedByte() == 1;
					rsi.fontId = buffer.readUnsignedByte();
					rsi.shadeText = buffer.readUnsignedByte() == 1;
				}
				if (rsi.type == 4) {
					rsi.disabledText = buffer.readString().replaceAll("RuneScape", Client.CLIENT_NAME);
					rsi.enabledText = buffer.readString().replaceAll("RuneScape", Client.CLIENT_NAME);
				}
				if (rsi.type == 1 || rsi.type == 3 || rsi.type == 4) {
					rsi.textColor = buffer.readInt();
				}
				if (rsi.type == 3 || rsi.type == 4) {
					rsi.anInt219 = buffer.readInt();
					rsi.anInt216 = buffer.readInt();
					rsi.anInt239 = buffer.readInt();
				}
				if (rsi.type == 5) {
					rsi.drawsTransparent = false;
					String sprite = buffer.readString();
					boolean spriteLoader = false;
					if (sprite.length() > 0) {
						spriteLoader = buffer.readUnsignedByte() == 1;
						int name = sprite.lastIndexOf(",");
						rsi.sprite1Id = Integer.parseInt(sprite.substring(name + 1));
						rsi.sprite1Name = sprite.substring(0, name);
						rsi.sprite1 = spriteLoader ? SpriteLoader.sprites[rsi.sprite1Id] : loadCacheSprite(rsi.sprite1Id, mediaArchive, rsi.sprite1Name);
					}
					sprite = buffer.readString();
					if (sprite.length() > 0) {
						spriteLoader = buffer.readUnsignedByte() == 1;
						int name = sprite.lastIndexOf(",");
						rsi.sprite2Id = Integer.parseInt(sprite.substring(name + 1));
						rsi.sprite2Name = sprite.substring(0, name);
						rsi.sprite2 = spriteLoader ? SpriteLoader.sprites[rsi.sprite2Id] : loadCacheSprite(rsi.sprite2Id, mediaArchive, rsi.sprite2Name);
					}
					sprite = buffer.readString();
					if (sprite.length() > 0) {
						spriteLoader = buffer.readUnsignedByte() == 1;
						int name = sprite.lastIndexOf(",");
						rsi.hoverSprite1Id = Integer.parseInt(sprite.substring(name + 1));
						rsi.hoverSprite1Name = sprite.substring(0, name);
						rsi.hoverSprite1 = spriteLoader ? SpriteLoader.sprites[rsi.hoverSprite1Id] : loadCacheSprite(rsi.hoverSprite1Id, mediaArchive, rsi.hoverSprite1Name);
					}
					sprite = buffer.readString();
					if (sprite.length() > 0) {
						spriteLoader = buffer.readUnsignedByte() == 1;
						int name = sprite.lastIndexOf(",");
						rsi.hoverSprite2Id = Integer.parseInt(sprite.substring(name + 1));
						rsi.hoverSprite2Name = sprite.substring(0, name);
						rsi.hoverSprite2 = spriteLoader ? SpriteLoader.sprites[rsi.hoverSprite2Id] : loadCacheSprite(rsi.hoverSprite2Id, mediaArchive, rsi.hoverSprite2Name);
					}
				}
				if (rsi.type == 6) {
					int read = buffer.readUnsignedByte();
					if (read != 0) {
						rsi.mediaType = 1;
						rsi.mediaId = (read - 1 << 8) + buffer.readUnsignedByte();
					}
					read = buffer.readUnsignedByte();
					if (read != 0) {
						rsi.anInt255 = 1;
						rsi.anInt256 = (read - 1 << 8) + buffer.readUnsignedByte();
					}
					read = buffer.readUnsignedByte();
					if (read != 0) {
						rsi.animationId = (read - 1 << 8) + buffer.readUnsignedByte();
					} else {
						rsi.animationId = -1;
					}
					read = buffer.readUnsignedByte();
					if (read != 0) {
						rsi.anInt258 = (read - 1 << 8) + buffer.readUnsignedByte();
					} else {
						rsi.animationId = -1;
					}
					rsi.modelZoom = buffer.readUnsignedShort();
					rsi.modelRotation1 = buffer.readUnsignedShort();
					rsi.modelRotation2 = buffer.readUnsignedShort();
				}
				if (rsi.type == 7) {
					rsi.inventory = rsi.inventoryAmount = new int[rsi.width * rsi.height];
					rsi.centerText = buffer.readUnsignedByte() == 1;
					rsi.fontId = buffer.readUnsignedByte();
					if (fonts != null && rsi.fontId < fonts.length) {
						rsi.textDrawingAreas = fonts[rsi.fontId];
					}
					rsi.shadeText = buffer.readUnsignedByte() == 1;
					rsi.textColor = buffer.readInt();
					rsi.invSpritePadX = buffer.readUnsignedShort();
					rsi.invSpritePadY = buffer.readUnsignedShort();
					rsi.isInventoryInterface = buffer.readUnsignedByte() == 1;
					rsi.actions = new String[5];
					for (int i = 0; i < 5; i++) {
						rsi.actions[i] = buffer.readString();
						if (rsi.actions[i].length() <= 0) {
							rsi.actions[i] = null;
						}
					}
				}
				if (rsi.actionType == 2 || rsi.type == 2) {
					rsi.selectedActionName = buffer.readString();
					rsi.spellName = buffer.readString();
					rsi.spellUsableOn = buffer.readUnsignedShort();
				}
				if (rsi.type == 8) {
					rsi.disabledText = buffer.readString();
				}
				if (rsi.actionType == 1 || rsi.actionType >= 4 && rsi.actionType <= 6) {
					rsi.tooltip = buffer.readString();
					if (rsi.tooltip.length() == 0) {
						if (rsi.actionType == 1) {
							rsi.tooltip = "Ok";
						}
						if (rsi.actionType == 4 || rsi.actionType == 5) {
							rsi.tooltip = "Select";
						}
						if (rsi.actionType == 6) {
							rsi.tooltip = "Continue";
						}
					}
				}
				if (rsi.actionType == 7) {
					rsi.colorFill = buffer.readString();
				}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error with childId: " + childId + "; parentId=" + parentId);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		RSInterface.interfaceArchive = interfaceArchive;
		RSInterface.mediaArchive = mediaArchive;
		Interfaces.loadInterfaces(mediaArchive, fonts);
		hoverSprites(mediaArchive);
		spriteNodes = null;
	}
	
	public static void unpack(JagexArchive streamLoader, RSFontSystem[] fonts, JagexArchive streamLoader_1) {
		spriteNodes = new Cache(50000);
		JagexBuffer buffer = new JagexBuffer(streamLoader.getData("data"));
		int parentId = -1;
		int length = buffer.readUnsignedShort();
		interfaceCache = new RSInterface[length + 30000];
		newInterfaceCache = new RSInterface[1][2];
		while (buffer.currentOffset < buffer.buffer.length) {
			int childId = buffer.readUnsignedShort();
			if (childId == 65535) {
				parentId = buffer.readUnsignedShort();
				childId = buffer.readUnsignedShort();
			}
			RSInterface rsi = interfaceCache[childId] = new RSInterface();
			rsi.id = childId;
			rsi.parentId = parentId;
			//int tab = buffer.readUnsignedByte();
			//rsi.isTabChild = tab == 1;
			rsi.type = buffer.readUnsignedByte();
			rsi.actionType = buffer.readUnsignedByte();
			rsi.contentType = buffer.readUnsignedShort();
			rsi.width = buffer.readUnsignedShort();
			rsi.height = buffer.readUnsignedShort();
			rsi.opacity = (byte) buffer.readUnsignedByte();
			rsi.hoverId = buffer.readUnsignedByte();
			if (rsi.hoverId != 0)
				rsi.hoverId = (rsi.hoverId - 1 << 8)
						+ buffer.readUnsignedByte();
			else
				rsi.hoverId = -1;
			int requirementLength = buffer.readUnsignedByte();
			if (requirementLength > 0) {
				rsi.valueCompareType = new int[requirementLength];
				rsi.requiredValue = new int[requirementLength];
				for (int i = 0; i < requirementLength; i++) {
					rsi.valueCompareType[i] = buffer.readUnsignedByte();
					rsi.requiredValue[i] = buffer.readUnsignedShort();
				}
			}
			int valueLength = buffer.readUnsignedByte();
			if (valueLength > 0) {
				rsi.valueIndexArray = new int[valueLength][];
				for (int i = 0; i < valueLength; i++) {
					int indexLength = buffer.readUnsignedShort();
					rsi.valueIndexArray[i] = new int[indexLength];
					for (int j = 0; j < indexLength; j++) {
						rsi.valueIndexArray[i][j] = buffer.readUnsignedShort();
					}
				}
			}
			if (rsi.type == 0) {
				rsi.drawsTransparent = false;
				rsi.scrollMax = buffer.readUnsignedShort();
				rsi.hidden = buffer.readUnsignedByte() == 1;
				int childLength = buffer.readUnsignedShort();
				rsi.children = new int[childLength];
				rsi.childX = new int[childLength];
				rsi.childY = new int[childLength];
				for (int i = 0; i < childLength; i++) {
					rsi.children[i] = buffer.readUnsignedShort();
					rsi.childX[i] = buffer.readSignedShort();
					rsi.childY[i] = buffer.readSignedShort();
				}
			}
			if (rsi.type == 1) {
				buffer.readUnsignedShort();
				buffer.readUnsignedByte();
			}
			if (rsi.type == 2) {
				rsi.inventory = new int[rsi.width
						* rsi.height];
				rsi.inventoryAmount = new int[rsi.width
						* rsi.height];
				rsi.aBoolean259 = buffer.readUnsignedByte() == 1;
				rsi.isInventoryInterface = buffer.readUnsignedByte() == 1;
				rsi.usableItemInterface = buffer.readUnsignedByte() == 1;
				rsi.aBoolean235 = buffer.readUnsignedByte() == 1;
				rsi.invSpritePadX = buffer.readUnsignedByte();
				rsi.invSpritePadY = buffer.readUnsignedByte();
				rsi.spritesX = new int[20];
				rsi.spritesY = new int[20];
				rsi.sprites = new Sprite[20];
				for (int j2 = 0; j2 < 20; j2++) {
					int k3 = buffer.readUnsignedByte();
					if (k3 == 1) {
						rsi.spritesX[j2] = buffer.readSignedShort();
						rsi.spritesY[j2] = buffer.readSignedShort();
						String s1 = buffer.readString();
						if (streamLoader_1 != null && s1.length() > 0) {
							int i5 = s1.lastIndexOf(",");
							rsi.sprites[j2] = loadCacheSprite(
									Integer.parseInt(s1.substring(i5 + 1)),
									streamLoader_1, s1.substring(0, i5));
						}
					}
				}
				rsi.actions = new String[5];
				for (int l3 = 0; l3 < 5; l3++) {
					rsi.actions[l3] = buffer.readString();
					if (rsi.actions[l3].length() == 0)
						rsi.actions[l3] = null;
					if (rsi.parentId == 1644)
						rsi.actions[2] = "Operate";
					if (rsi.parentId == 3822)
						rsi.actions[4] = "Sell X";
					if (rsi.parentId == 3824)
						rsi.actions[4] = "Buy X";
				}
			}
			if (rsi.type == 3)
				rsi.aBoolean227 = buffer.readUnsignedByte() == 1;
			if (rsi.type == 4 || rsi.type == 1) {
				rsi.centerText = buffer.readUnsignedByte() == 1;
				rsi.fontId = buffer.readUnsignedByte();
				if (fonts != null)
					rsi.textDrawingAreas = fonts[rsi.fontId];
				rsi.shadeText = buffer.readUnsignedByte() == 1;
			}
			if (rsi.type == 4) {
				rsi.disabledText = buffer.readString()/*+ "\\nid: " + rsi.id*/;
				rsi.disabledText = rsi.disabledText.replaceAll("RuneScape", Client.CLIENT_NAME);
				rsi.enabledText = buffer.readString()/* + "\\nid: " + rsi.id*/;
			}
			if (rsi.type == 1 || rsi.type == 3
					|| rsi.type == 4)
				rsi.textColor = buffer.readInt();
			if (rsi.type == 3 || rsi.type == 4) {
				rsi.anInt219 = buffer.readInt();
				rsi.anInt216 = buffer.readInt();
				rsi.anInt239 = buffer.readInt();
			}
			if (rsi.type == 5) {
				rsi.drawsTransparent = false;
				String spriteName = buffer.readString();
				boolean spriteLoader = spriteName.startsWith("spriteloader");
				if (spriteLoader)
					spriteName = spriteName.substring(12);
				if (streamLoader_1 != null && spriteName.length() > 0) {
					int name = spriteName.lastIndexOf(",");
					rsi.sprite1Id = Integer.parseInt(spriteName.substring(name + 1));
					rsi.sprite1Name = spriteName.substring(0, name);
					rsi.sprite1 = spriteLoader ? SpriteLoader.sprites[rsi.sprite1Id] : loadCacheSprite(rsi.sprite1Id, streamLoader_1, rsi.sprite1Name);
				}
				spriteName = buffer.readString();
				spriteLoader = spriteName.startsWith("spriteloader");
				if (spriteLoader)
					spriteName = spriteName.substring(12);
				if (streamLoader_1 != null && spriteName.length() > 0) {
					int name = spriteName.lastIndexOf(",");
					rsi.sprite2Id = Integer.parseInt(spriteName.substring(name + 1));
					rsi.sprite2Name = spriteName.substring(0, name);
					rsi.sprite2 = spriteLoader ? SpriteLoader.sprites[rsi.sprite2Id] : loadCacheSprite(rsi.sprite2Id, streamLoader_1, rsi.sprite2Name);
				}
			}
			if (rsi.type == 6) {
				int l = buffer.readUnsignedByte();
				if (l != 0) {
					rsi.mediaType = 1;
					rsi.mediaId = (l - 1 << 8) + buffer.readUnsignedByte();
				}
				l = buffer.readUnsignedByte();
				if (l != 0) {
					rsi.anInt255 = 1;
					rsi.anInt256 = (l - 1 << 8) + buffer.readUnsignedByte();
				}
				l = buffer.readUnsignedByte();
				if (l != 0)
					rsi.animationId = (l - 1 << 8) + buffer.readUnsignedByte();
				else
					rsi.animationId = -1;
				l = buffer.readUnsignedByte();
				if (l != 0)
					rsi.anInt258 = (l - 1 << 8) + buffer.readUnsignedByte();
				else
					rsi.anInt258 = -1;
				rsi.modelZoom = buffer.readUnsignedShort();
				rsi.modelRotation1 = buffer.readUnsignedShort();
				rsi.modelRotation2 = buffer.readUnsignedShort();
			}
			if (rsi.type == 7) {
				rsi.inventory = new int[rsi.width
						* rsi.height];
				rsi.inventoryAmount = new int[rsi.width
						* rsi.height];
				rsi.centerText = buffer.readUnsignedByte() == 1;
				rsi.fontId = buffer.readUnsignedByte();
				if (fonts != null)
					rsi.textDrawingAreas = fonts[rsi.fontId];
				rsi.shadeText = buffer.readUnsignedByte() == 1;
				rsi.textColor = buffer.readInt();
				rsi.invSpritePadX = buffer.readSignedShort();
				rsi.invSpritePadY = buffer.readSignedShort();
				rsi.isInventoryInterface = buffer.readUnsignedByte() == 1;
				rsi.actions = new String[5];
				for (int k4 = 0; k4 < 5; k4++) {
					rsi.actions[k4] = buffer.readString();
					if (rsi.actions[k4].length() == 0)
						rsi.actions[k4] = null;
				}

			}
			
			if (rsi.actionType == 2 || rsi.type == 2) {
				rsi.selectedActionName = buffer.readString();
				rsi.spellName = buffer.readString();
				rsi.spellUsableOn = buffer.readUnsignedShort();
			}

			if (rsi.type == 8)
				rsi.disabledText = buffer.readString()/* + "\\nid: " + rsi.id*/;

			if (rsi.actionType == 1 || rsi.actionType == 4
					|| rsi.actionType == 5
					|| rsi.actionType == 6) {
				rsi.tooltip = buffer.readString();
				if (rsi.tooltip.length() == 0) {
					if (rsi.actionType == 1)
						rsi.tooltip = "Ok";
					if (rsi.actionType == 4)
						rsi.tooltip = "Select";
					if (rsi.actionType == 5)
						rsi.tooltip = "Select";
					if (rsi.actionType == 6)
						rsi.tooltip = "Continue";
				}
			}
			boolean fillWithColor = buffer.readUnsignedByte() == 1;
			if (fillWithColor) {
				rsi.fillWithColor = true;
				rsi.colorFill = buffer.readString();
			}
		}
		interfaceArchive = streamLoader;
		mediaArchive = streamLoader_1;
		Interfaces.loadInterfaces(streamLoader_1, fonts);
		hoverSprites(streamLoader_1);
		spriteNodes = null;
	}
	
	public static void addCacheSprite(int id, int disabledSprite, int enabledSprite, String sprites) {
        RSInterface rsi = interfaceCache[id] = new RSInterface();
        rsi.sprite1 = loadCacheSprite(disabledSprite, interfaceArchive, sprites);
        rsi.sprite1 = loadCacheSprite(enabledSprite, interfaceArchive, sprites);
        rsi.parentId = id;
        rsi.id = id;
        rsi.type = 5;
    }
	
	public static void unpackCommand() {
		unpack(interfaceArchive, Interfaces.fonts, mediaArchive);
	}
	
	public static void textSize(int id, RSFontSystem tda[], int idx) {
		RSInterface rsi = interfaceCache[id];
		rsi.textDrawingAreas = tda[idx];
	}
	
	public static void isTabInterface(RSInterface rsi, boolean isTabInterface) {
		if (rsi.children == null)
			return;
		for (int childId : rsi.children) {
			RSInterface child = RSInterface.interfaceCache[childId];
			if (child == null)
				continue;
			child.isTabChild = true;
		}
	}

	public static void hoverSprites(JagexArchive archive) {
		/*int[] tabParentIds = { 31110 , 7850, 5065, 5715 };
		for (int id : tabParentIds) {
			RSInterface rsi = interfaceCache[id];
			if (rsi == null)
				continue;
			for (int childId : rsi.children) {
				RSInterface child = interfaceCache[childId];
				if (child == null)
					continue;
				child.isTabChild = true;
			}
		}*/
		int[] bankButtons = { 8130, 5387, 23097, 5386, 5390, 5388 };
		for (int id : bankButtons) {
			RSInterface rsi = interfaceCache[id];
			if (rsi == null)
				continue;
			rsi.hoverSprite1 = loadCacheSprite(1, archive, "bank");
			rsi.hoverSprite2 = loadCacheSprite(13, archive, "bank");
		}
		int[] smallClose = { 5384, 23090 };
		for (int id : smallClose) {
			RSInterface rsi = interfaceCache[id];
			if (rsi == null)
				continue;
			if (id == 23090) {
				rsi.hoverRegion = 1;
			}
			rsi.hoverSprite1 = loadCacheSprite(11, archive, "miscgraphics2");
		}
		int[] confirmPrayer = { 22990, 23048 };
		for (int id : confirmPrayer) {
			RSInterface rsi = interfaceCache[id];
			if (rsi == null)
				continue;
			rsi.hoverRegion = 1;
			rsi.hoverSprite1 = loadCacheSprite(15, archive, "miscgraphics3");
		}
		int[] screenModes = { 23058, 23059, 23060 };
		for (int id : screenModes) {
			RSInterface rsi = interfaceCache[id];
			if (rsi == null)
				continue;
			rsi.hoverRegion = 1;
			rsi.hoverSprite1 = rsi.sprite2;
		}
	}
    public static void addButton(int i, int j, String name, int W, int H, String S, int AT) {
        RSInterface RSInterface = addInterface(i);
        RSInterface.id = i;
        RSInterface.parentId = i;
        RSInterface.type = 5;
        RSInterface.actionType = AT;
        RSInterface.contentType = 0;
        RSInterface.opacity = (byte)0;
        RSInterface.hoverId = 52;
        RSInterface.sprite1 = imageLoader(j,name);
        RSInterface.sprite2 = imageLoader(j,name);
        RSInterface.width = W;
        RSInterface.height = H;
        RSInterface.tooltip = S;
}
	public static void AddInterfaceButton(int id, int sid, String spriteName, String tooltip, int mOver, int atAction, int width, int height) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentId = id;
		tab.type = 5;
		tab.actionType = atAction;
		tab.contentType = 0;
		tab.opacity = 0;
		tab.hoverId = mOver;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		tab.width = width;
		tab.height = height;
		tab.tooltip = tooltip;
		tab.isFalseTooltip = true;
	}

	public static void addButton(int id, int sid, String spriteName, String tooltip, int mOver, int atAction, int width, int height) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentId = id;
		tab.type = 5;
		tab.actionType = atAction;
		tab.contentType = 0;
		tab.opacity = (byte)0;
		tab.hoverId = mOver;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		tab.width = width;
		tab.height = height;
		tab.tooltip = tooltip;
		tab.isFalseTooltip = true;
	}
	
	public static void addCacheButton(int id, int sid, String spriteName, String tooltip, JagexArchive archive) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentId = id;
		tab.type = 5;
		tab.actionType = 1;
		tab.contentType = 0;
		tab.opacity = (byte)0;
		tab.hoverId = 52;
		tab.sprite1 = loadCacheSprite(sid, archive, spriteName);
		tab.sprite2 = loadCacheSprite(sid, archive, spriteName);
		tab.width = tab.sprite1.myWidth;
		tab.height = tab.sprite1.myHeight;
		tab.tooltip = tooltip;
		tab.isFalseTooltip = true;
	}
	
	public static void addButton(int id, int spriteId, String spriteName, String subDir, String tooltip) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentId = id;
		tab.type = 5;
		tab.actionType = 1;
		tab.contentType = 0;
		tab.opacity = (byte)0;
		tab.hoverId = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName, subDir);
		tab.sprite2 = imageLoader(spriteId, spriteName, subDir);
		tab.width = tab.sprite1.myWidth;
		tab.height = tab.sprite1.myHeight;
		tab.tooltip = tooltip;
	}
	
	
	public static void addConfigButton(int ID, int pID, int bID, int bID2, String bName, String subDir, int width, int height, String tT, int configID, int aT, int configFrame) {
		RSInterface Tab = addInterface(ID);
		Tab.parentId = pID;
		Tab.id = ID;
		Tab.type = 5;
		Tab.actionType = aT;
		Tab.contentType = 0;
		Tab.width = width;
		Tab.height = height;
		Tab.opacity = 0;
		Tab.hoverId = -1;
		Tab.valueCompareType = new int[1];
		Tab.requiredValue = new int[1];
		Tab.valueCompareType[0] = 1;
		Tab.requiredValue[0] = configID;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.sprite1 = imageLoader(bID, bName, subDir);
		Tab.sprite2 = imageLoader(bID2, bName, subDir);
		Tab.tooltip = tT;
	}

    public static void addModel(int ID, int parentId, int mId, int modelZ, int modelRT, int modelRT2) {
        RSInterface Tab = addInterface(ID);
        Tab.id = ID;
        Tab.parentId = parentId;
        Tab.type = 6;
        Tab.actionType = 0;
        Tab.anInt256 = 1;
        Tab.contentType = 0;
        Tab.width = 512;
        Tab.height = 334;
        Tab.opacity = 0;
        Tab.hoverId = -1;
        Tab.mediaId = mId;
        Tab.modelZoom = modelZ;
        Tab.modelRotation1 = modelRT;
        Tab.modelRotation2 = modelRT2;
    } 
	
	public static void addInv(int id, int pID, int h, int w) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.inventory = new int[h * w];
		rsi.inventoryAmount = new int[h * w];
		for(int i1 = 0; i1 < h * w; i1++) {
			rsi.inventory[i1] = 0;
			rsi.inventoryAmount[i1] = 0;
		}
		rsi.spritesX = new int[20];
		rsi.spritesY = new int[20];
		for(int i2 = 0; i2 < 20; i2++) {
			rsi.spritesX[i2] = 0;
			rsi.spritesY[i2] = 0;
		}
		rsi.type = 2;
		rsi.height = h;
		rsi.width = w;
		rsi.hoverId = -1;
		rsi.scrollMax = 0;
		rsi.id = id;
		rsi.parentId = pID;
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
		// rsi.aBoolean251 = false;
		rsi.aBoolean259 = true;
		// rsi.interfaceShown = false;
		// rsi.textShadow = false;
		// rsi.hoverType = -1;
		rsi.invSpritePadX = 22;
		rsi.invSpritePadY = 21; 
		rsi.height = 5;
		rsi.width = 6;
		rsi.parentId = 2702;
		rsi.id = 2700;
		rsi.type = 2;
	}
	
	public static void drawTooltip(int id, String text) {
		RSInterface rsinterface = addInterface(id);
		rsinterface.id = id;
		rsinterface.type = 0;
		rsinterface.hidden = true;
		rsinterface.hoverId = -1;
		addTooltipBox(id + 1, text);
		rsinterface.totalChildren(1);
		rsinterface.child(0, id + 1, 0, 0);
	}
	
	public static void addTooltipBox(int id, String text) {
		RSInterface rsi = addInterface(id);
		rsi.id = id;
		rsi.parentId = id;
		rsi.type = 8;
		rsi.disabledText = text;
	}
	
	public static void addToggleButton(int id, int sprite, int setconfig, int width, int height, String tooltip, int mOver) {
        RSInterface rsi = addInterface(id);
        rsi.sprite1 = CustomSpriteLoader(sprite, "");
        rsi.sprite2 = CustomSpriteLoader(sprite, "a");
        rsi.requiredValue = new int[1];
        rsi.requiredValue[0] = 1;
        rsi.valueCompareType = new int[1];
        rsi.valueCompareType[0] = 1;
        rsi.valueIndexArray = new int[1][3];
        rsi.valueIndexArray[0][0] = 5;
        rsi.valueIndexArray[0][1] = setconfig;
        rsi.valueIndexArray[0][2] = 0;
        rsi.actionType = 4;
        rsi.width = width;
        rsi.hoverId = mOver;
        rsi.parentId = id;
        rsi.id = id;
        rsi.type = 5;
        rsi.height = height;
        rsi.tooltip = tooltip;
    }

	public static void addToggleButton(int id, int sprite, int setconfig, int width, int height, String s) {
        RSInterface rsi = addInterface(id);
        rsi.sprite1 = CustomSpriteLoader(sprite, "");
        rsi.sprite2 = CustomSpriteLoader(sprite, "a");
        rsi.requiredValue = new int[1];
        rsi.requiredValue[0] = 1;
        rsi.valueCompareType = new int[1];
        rsi.valueCompareType[0] = 1;
        rsi.valueIndexArray = new int[1][3];
        rsi.valueIndexArray[0][0] = 5;
        rsi.valueIndexArray[0][1] = setconfig;
        rsi.valueIndexArray[0][2] = 0;
        rsi.actionType = 4;
        rsi.width = width;
        rsi.hoverId = -1;
        rsi.parentId = id;
        rsi.id = id;
        rsi.type = 5;
        rsi.height = height;
        rsi.tooltip = s;
    }
	
	public static void addActionButton(int id, int sprite, int sprite2, int width, int height, String s) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.sprite1 = CustomSpriteLoader(sprite, "");
		if (sprite2 == sprite) {
			rsi.sprite2 = CustomSpriteLoader(sprite, "a");
		} else {
			rsi.sprite2 = CustomSpriteLoader(sprite2, "");
		}
		rsi.tooltip = s;
		rsi.contentType = 0;
		rsi.actionType = 1;
		rsi.width = width;
		rsi.hoverId = 52;
		rsi.parentId = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
	}

	public static void addTooltip(int id, String text) {
		RSInterface rsi = addInterface(id);
		rsi.id = id;
		rsi.type = 0;
		rsi.hidden = true;
		rsi.hoverId = -1;
		addTooltipBox(id + 1, text);
		rsi.totalChildren(1);
		rsi.child(0, id + 1, 0, 0);
	}
	
	public static void sprite1(int id, int sprite) {
		RSInterface class9 = interfaceCache[id];
		class9.sprite1 = CustomSpriteLoader(sprite, "");
	}
	
	public void totalChildren(int id, int x, int y) {
		children = new int[id];
		childX = new int[x];
		childY = new int[y];
	}
	
	public static void addHoverText(int id, String text, String tooltip, RSFontSystem[] tda, int idx, int color, boolean center, boolean textShadow, int width) {
		RSInterface rsinterface = addInterface(id);
		rsinterface.id = id;
		rsinterface.parentId = id;
		rsinterface.type = 4;
		rsinterface.actionType = 1;
		rsinterface.width = width;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.opacity = 0;
		rsinterface.hoverId = -1;
		rsinterface.centerText = center;
		rsinterface.shadeText = textShadow;
		rsinterface.textDrawingAreas = tda[idx];
		rsinterface.disabledText = text;
		rsinterface.enabledText = "";
		rsinterface.textColor = color;
		rsinterface.anInt219 = 0;
		rsinterface.anInt216 = 0xffffff;
		rsinterface.anInt239 = 0;
		rsinterface.tooltip = tooltip;
	}
	public static void addHover(int i, int aT, int cT, int hoverid,int sId, String NAME, String sub, int W, int H, String tip) { 
		RSInterface rsinterfaceHover = addInterface(i);
		rsinterfaceHover.id = i;
		rsinterfaceHover.parentId = i;
		rsinterfaceHover.type = 5;
		rsinterfaceHover.actionType = aT;
		rsinterfaceHover.contentType = cT;
		rsinterfaceHover.hoverId = hoverid;
		rsinterfaceHover.sprite1 = imageLoader(sId, NAME, sub);
		rsinterfaceHover.sprite2 = imageLoader(sId, NAME, sub);
		rsinterfaceHover.width = W;
		rsinterfaceHover.height = H;
		rsinterfaceHover.tooltip = tip;
	}
	public static void addHovered(int i, int j, String imageName, String sub, int w, int h, int IMAGEID) {
		RSInterface rsinterfaceHover = addInterface(i);
		rsinterfaceHover.parentId = i;
		rsinterfaceHover.id = i;
		rsinterfaceHover.type = 0;
		rsinterfaceHover.actionType = 0;
		rsinterfaceHover.width = w;
		rsinterfaceHover.height = h;
		rsinterfaceHover.hidden = true;
		rsinterfaceHover.hoverId = -1;
		addHoverImage(IMAGEID, j, j, imageName, sub);
		rsinterfaceHover.totalChildren(1);
		rsinterfaceHover.child(0, IMAGEID, 0, 0);
	}

    public static void addHover(int i, int aT, int cT, int hoverid,int sId, String NAME, int W, int H, String tip) { 
		RSInterface rsinterfaceHover = addInterface(i);
		rsinterfaceHover.id = i;
		rsinterfaceHover.parentId = i;
		rsinterfaceHover.type = 5;
		rsinterfaceHover.actionType = aT;
		rsinterfaceHover.contentType = cT;
		rsinterfaceHover.hoverId = hoverid;
		rsinterfaceHover.sprite1 = imageLoader(sId, NAME);
		rsinterfaceHover.sprite2 = imageLoader(sId, NAME);
		rsinterfaceHover.width = W;
		rsinterfaceHover.height = H;
		rsinterfaceHover.tooltip = tip;
	}
	public static void addHovered(int i, int j, String imageName, int w, int h, int IMAGEID) {
		RSInterface rsinterfaceHover = addInterface(i);
		rsinterfaceHover.parentId = i;
		rsinterfaceHover.id = i;
		rsinterfaceHover.type = 0;
		rsinterfaceHover.actionType = 0;
		rsinterfaceHover.width = w;
		rsinterfaceHover.height = h;
		rsinterfaceHover.hidden = true;
		rsinterfaceHover.hoverId = -1;
		addSprite(IMAGEID, j, imageName);
		setChildren(1, rsinterfaceHover);
		setBounds(IMAGEID, 0, 0, 0, rsinterfaceHover);
	}
	public static void setBounds(int ID, int X, int Y, int frame, RSInterface RSinterface){
		RSinterface.children[frame] = ID;
		RSinterface.childX[frame] = X;
		RSinterface.childY[frame] = Y;
	}

	public static void setChildren(int total, RSInterface rsinterface) {
		rsinterface.children = new int[total];
		rsinterface.childX = new int[total];
		rsinterface.childY = new int[total];
	}

    public static void addHoverImage(int i, int j, int k, String name, String sub) {
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
        tab.sprite1 = imageLoader(j, name, sub);
        tab.sprite2 = imageLoader(k, name, sub);
    }
    
    public static void addSprite(int id, int spriteId, String spriteName) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentId = id;
		tab.type = 5;
		tab.actionType = 0;
		tab.contentType = 0;
		tab.opacity = (byte)0;
		tab.hoverId = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.sprite2 = imageLoader(spriteId, spriteName); 
		tab.width = 512;
		tab.height = 334;
	}
    
    public static void addSprite(int id, int spriteId, String spriteName, boolean cacheDirectory) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentId = id;
		tab.type = 5;
		tab.actionType = 0;
		tab.contentType = 0;
		tab.opacity = (byte)0;
		tab.hoverId = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName, true);
		tab.sprite2 = imageLoader(spriteId, spriteName, true); 
		tab.width = 512;
		tab.height = 334;
	}

	/*public void child(int id, int interID, int x, int y) {
		children[id] = interID;
		childX[id] = x;
		childY[id] = y;
	}*/
	
	public void child(int id, int interID, int x, int y) {
		children[id] = interID;
		childX[id] = x;
		childY[id] = y;
	}

	public void totalChildren(int t) {
		children = new int[t];
		childX = new int[t];
		childY = new int[t];
	}
	public static String getData(int id) {
		RSInterface rsi = interfaceCache[id];
		String data = "";
		data += rsi.mediaType;
		data += ", ";
		data += rsi.mediaId;
		data += ", ";
		data += rsi.anInt255;
		data += ", ";
		data += rsi.anInt256;
		data += ", ";
		data += rsi.sprite1;
		data += ", ";
		data += rsi.sprite2;
		data += ", ";
		data += rsi.sprite1Name;
		data += ", ";
		data += rsi.sprite2Name;
		data += ", ";
		return data;
	}
	public void specialBar(int id) // 7599
	{
		try {
		/*
		 * addActionButton(id, SpriteOFF, SpriteON, Width, Height,
		 * "SpriteText");
		 */
		addActionButton(id - 12, 7587, -1, 150, 26, "Use @gre@Special Attack");
		/* removeSomething(id); */
		for (int i = id - 11; i < id; i++) {
			newInterface(i);
		}

		RSInterface rsi = interfaceCache[id - 12];
		rsi.width = 150;
		rsi.height = 26;

		rsi = interfaceCache[id];
		rsi.width = 150;
		rsi.height = 26;
		if (rsi.children.length < 24) {
			rsi.totalChildren(24);
		}
		rsi.child(0, id - 12, 0, 0);
		rsi.child(12, id + 1, 3, 7);
		rsi.child(23, id + 12, 16, 8);

		for (int i = 13; i < 23; i++) {
			rsi.childY[i] -= 1;
		}

		rsi = interfaceCache[id + 1];
		rsi.type = 5;
		rsi.sprite1 = CustomSpriteLoader(7600, "");

		for (int i = id + 2; i < id + 12; i++) {
			rsi = interfaceCache[i];
			rsi.type = 5;
		}
		sprite1(id + 2, 7601);
		sprite1(id + 3, 7602);
		sprite1(id + 4, 7603);
		sprite1(id + 5, 7604);
		sprite1(id + 6, 7605);
		sprite1(id + 7, 7606);
		sprite1(id + 8, 7607);
		sprite1(id + 9, 7608);
		sprite1(id + 10, 7609);
		sprite1(id + 11, 7610);
		} catch(Exception e) {
			//e.printStackTrace();
		}
	}

	public static RSInterface addInterface(int id) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.id = id;
		rsi.parentId = id;
		rsi.type = 0;
		rsi.actionType = 0;
		rsi.contentType = 0;
		rsi.width = 765;
		rsi.height = 503;
		rsi.opacity = (byte)0;
		rsi.hoverId = -1;
		return rsi;
	}
	
	protected static Sprite imageLoader(int i, String s) {
		long l = (TextClass.method585(s) << 8) + (long)i;
		Sprite sprite = (Sprite) spriteNodes.insertFromCache(l);
		if(sprite != null)
			return sprite;
		try {
			sprite = new Sprite(Client.getImageLoader().getCache().getFile(s.toLowerCase() + " " + i));
			spriteNodes.removeFromCache(sprite, l);
		} catch(Exception exception) {
			return null;
		}
		return sprite;
	}

	protected static Sprite imageLoader(int i, String s, String subDir) {
		long l = (TextClass.method585(s) << 8) + (long) i;
		Sprite sprite = (Sprite) spriteNodes.insertFromCache(l);
		if (sprite != null)
			return sprite;
		try {
			if (!subDir.equals(""))
				subDir = subDir + "";
			if (i == -1)
				sprite = new Sprite(Client.getImageLoader().getCache().getFile(subDir.toLowerCase(), s.toLowerCase()));
			else
				sprite = new Sprite(Client.getImageLoader().getCache().getFile(subDir.toLowerCase(), s.toLowerCase() + " " + i));
			spriteNodes.removeFromCache(sprite, l);
		} catch (Exception exception) {
			return null;
		}
		return sprite;
	}
	
	public static Sprite imageLoader(int i, String s, boolean spriteDirectory) {
		long l = (TextClass.method585(s) << 8) + (long) i;
		Sprite sprite = (Sprite) spriteNodes.insertFromCache(l);
		if (sprite != null)
			return sprite;
		try {
			sprite = new Sprite(s + " " + i);
			spriteNodes.removeFromCache(sprite, l);
		} catch (Exception exception) {
			return null;
		}
		return sprite;
	}
	
	public static Sprite CustomSpriteLoader(int id, String s) {
		String name = "Attack" + System.getProperty("file.separator") + id + s;
		Sprite sprite = new Sprite(name);
		//public boolean exists = false;
		if (new File(Sprite.CACHE_LOCATION + name + ".jpg").exists()) {
			//exists = true;
		}
		//System.out.println(sprite.location + name + ".jpg" +  ", " + sprite.myPixels.length + ", " + exists);
		return sprite;
        /*long l = (TextClass.method585(s) << 8) + (long)id;
        Sprite sprite = (Sprite)spriteNodes.insertFromCache(l);
        if(sprite != null) {
        	return sprite;
        }
        try {
            sprite = new Sprite("/Attack/"+id + s);
            spriteNodes.removeFromCache(sprite, l);
        } catch(Exception exception) {
			return null;
		}
        System.out.println(sprite.myPixels.length + ", " + sprite.myWidth + ", " + sprite.myHeight);
        return sprite;*/
    }
	
	public static void newInterface(int id) {
		interfaceCache[id] = new RSInterface();
	}

	private Model method206(int i, int j) {
		Model model = (Model) cache.insertFromCache((i << 16) + j);
		if (model != null)
			return model;
		if (i == 1)
			model = Model.getModel(j);
		if (i == 2)
			model = NPCDefinition.forId(j).method160();
		if (i == 3)
			model = Client.myPlayer.method453();
		if (i == 4)
			model = ItemDef.forId(j).method202(50);
		if (i == 5)
			model = null;
		if (model != null)
			cache.removeFromCache(model, (i << 16) + j);
		return model;
	}

	protected static Sprite loadCacheSprite(int i, JagexArchive streamLoader, String s) {
		long l = (TextClass.method585(s) << 8) + (long) i;
		Sprite sprite = (Sprite) spriteNodes.insertFromCache(l);
		if (sprite != null)
			return sprite;
		try {
			sprite = new Sprite(streamLoader, s, i);
			sprite.myDirectory = s + "," + i;
			spriteNodes.removeFromCache(sprite, l);
		} catch (Exception _ex) {
			return null;
		}
		return sprite;
	}

	public static void method208(boolean flag, Model model) {
		int i = 0;// was parameter
		int j = 5;// was parameter
		if (flag)
			return;
		cache.unlinkAll();
		if (model != null && j != 4)
			cache.removeFromCache(model, (j << 16) + i);
	}

	public Model method209(int j, int k, boolean flag, Animation animDef)
	{
		Model model;
		if (flag)
			model = method206(anInt255, anInt256);
		else
			model = method206(mediaType, mediaId);
		if (model == null) {
			return null;
		}
		if (k == -1 && j == -1 && model.colorValues == null)
			return model;
		Model model_1 = new Model(Model.getFlags(k, animDef) | Model.getFlags(j, animDef), model);
		if (k != -1 || j != -1)
			model_1.createBones();
		if (k != -1)
			model_1.applyTransformation(k, animDef);
		if (j != -1)
			model_1.applyTransformation(j, animDef);
		model_1.setLighting(64, 768, -50, -10, -50, true, false);
		return model_1;
	}
	public static void addTextFieldB(int i, String s, RSFontSystem[] tda, int j, int k, boolean flag, boolean flag1, int cT)
    {
        RSInterface rsinterface = addInterface(i);
        rsinterface.parentId = i;
        rsinterface.id = i;
        rsinterface.type = 4;
        rsinterface.actionType = 1;
        rsinterface.width = 58;
        rsinterface.height = 14;
        rsinterface.contentType = cT;
        rsinterface.opacity = 0;
        rsinterface.hoverId = -1;
        rsinterface.centerText = flag;
        rsinterface.shadeText = flag1;
		rsinterface.textDrawingAreas = tda[j];
        rsinterface.disabledText = s;
        rsinterface.textColor = k;
        rsinterface.anInt219 = 0;
        rsinterface.anInt216 = 0;
        rsinterface.anInt239 = 0;
		rsinterface.tooltip = "Select";
    }
	protected static void addHead2(int id, int w, int h, int zoom) {//tewst
        RSInterface rsinterface = addInterface(id);
        rsinterface.type = 6;
		rsinterface.mediaType = 2;
		rsinterface.mediaId = 4000;//
        rsinterface.modelZoom = zoom;
        rsinterface.modelRotation1 = 40;
        rsinterface.modelRotation2 = 1900;
        rsinterface.height = h;
        rsinterface.width = w;
    }
	
	public static int getConfigID(int id) {
		RSInterface rsi = interfaceCache[id];
		if (rsi.valueIndexArray[0][1] > 0) {
			return rsi.valueIndexArray[0][1];
		}
		return 0;
	}

	public RSInterface() {
	}

	public static RSInterface newInterfaceCache[][];
	public static JagexArchive interfaceArchive;
	public static JagexArchive mediaArchive;
	public boolean drawsTransparent;
	public Sprite sprite1;
	public boolean isFalseTooltip;
	public boolean isTabChild;
	public boolean fillWithColor;
	public String colorFill;
	public boolean shopInterface;
	public Sprite shopCurrency;
	public int[] shopPrices = new int[30];
	
	public int anInt208;
	public Sprite sprites[];
	public static RSInterface interfaceCache[];
	public int requiredValue[];
	public int contentType;// contentType
	public int spritesX[];
	public int anInt216;
	public int actionType;
	public String spellName;
	public int anInt219;
	public int width;
	public String tooltip;
	public String selectedActionName;
	public  boolean centerText;
	public int scrollPosition;
	public String actions[];
	public int valueIndexArray[][];
	public boolean aBoolean227;
	public String enabledText;
	public int hoverId;
	public int invSpritePadX;
	public int textColor;
	public int mediaType;
	public int mediaId;
	public boolean aBoolean235;
	public int parentId;
	public int spellUsableOn;
	protected static Cache spriteNodes;
	public int anInt239;
	public int children[];
	public int childX[];
	public boolean usableItemInterface;
	public RSFontSystem textDrawingAreas;
	public int invSpritePadY;
	public int valueCompareType[];
	public int anInt246;
	public int spritesY[];
	public String disabledText;
	public boolean isInventoryInterface;
	public int id;
	public int inventoryAmount[];
	public int inventory[];
	public byte opacity;
	private int anInt255;
	private int anInt256;
	public int animationId;
	public int anInt258;
	public boolean aBoolean259;
	public Sprite sprite2;
	public int scrollMax;
	public int type;
	public int drawOffsetX;
	private static final Cache cache = new Cache(30);
	public int drawOffsetY;
	public boolean hidden;
	public int height;
	public boolean shadeText;
	public int modelZoom;
	public int modelRotation1;
	public int modelRotation2;
	public int childY[];
	public boolean invHover;
	public String[] menuActions;
	public int menuId;
	public String[] dropDownItems;
	public int[] dropDownItemActions;
	public int sprite1Id;
	public int sprite2Id;
	public int spriteID[];
	public String sprite1Name;
	public String sprite2Name;
	public String spriteName[];
	public Sprite hoverSprite1;
	public Sprite hoverSprite2;
	public String hoverSprite1Name;
	public String hoverSprite2Name;
	public int hoverSprite1Id;
	public int hoverSprite2Id;
	public int fontId;
	public int hoverRegion;
	public int delayTime;
	public int buttonAlpha;
}