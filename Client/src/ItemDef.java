package src;

import java.util.*;

public final class ItemDef {
	private ItemDef() {
		id = -1;
	}

	public static void clearCache() {
		if (cache != null)
			for (int i = 0; i != 50; ++i)
				cache[i].id = -1;

	}

	public static void nullLoader() {
		mruNodes2 = null;
		mruNodes1 = null;
		streamIndices = null;
		cache = null;
		buffer = null;
	}

	public static void nullLoaderSafe() {
		mruNodes2.unlinkAll();
		mruNodes1.unlinkAll();
		streamIndices = null;
		cache = null;
		buffer = null;
	}

	private static final String[] wears = { "wield", "wear", "hold" };

	public static Sprite getFeedImage(int id, int size, int color, int zoom) {
		ItemDef item = forId(id);
		if (item.stackIDs == null) {
			size = -1;
		}
		if (size > 1) {
			int i1 = -1;
			for (int j1 = 0; j1 < 10; j1++) {
				if (size >= item.stackAmounts[j1] && item.stackAmounts[j1] != 0) {
					i1 = item.stackIDs[j1];
				}
			}
			if (i1 != -1) {
				item = forId(i1);
			}
		}
		Model model = item.getModelForAmount(1);
		if (model == null)
			return null;
		Sprite image = new Sprite(32, 32);
		int centerX = Rasterizer.centerX;
		int centerY = Rasterizer.centerY;
		int lineOffsets[] = Rasterizer.lineOffsets;
		int pixels[] = DrawingArea.pixels;
		int width = DrawingArea.width;
		int height = DrawingArea.height;
		int startX = DrawingArea.topX;
		int endX = DrawingArea.bottomX;
		int startY = DrawingArea.topY;
		int endY = DrawingArea.bottomY;
		Rasterizer.notTextured = false;
		DrawingArea.initDrawingArea(32, 32, image.myPixels);
		DrawingArea.drawPixels(32, 0, 0, 0, 32);
		Rasterizer.setDefaultBounds();
		int itemZoom = item.modelZoom * zoom - 500;
		int l3 = Rasterizer.SINE[item.modelRotation1] * itemZoom >> 16;
		int i4 = Rasterizer.COSINE[item.modelRotation1] * itemZoom >> 16;
		model.method482(item.modelRotation2, item.anInt204,
				item.modelRotation1, item.modelOffset1, l3 + model.modelHeight
						/ 2 + item.modelOffset2, i4 + item.modelOffset2);
		if (color == 0) {
			for (int index = 31; index >= 0; index--) {
				for (int index2 = 31; index2 >= 0; index2--)
					if (image.myPixels[index + index2 * 32] == 0
							&& index > 0
							&& index2 > 0
							&& image.myPixels[(index - 1) + (index2 - 1) * 32] > 0)
						image.myPixels[index + index2 * 32] = 0x302020;
			}
		}
		DrawingArea.initDrawingArea(height, width, pixels);
		DrawingArea.setDrawingArea(startX, startY, endX, endY);
		Rasterizer.centerX = centerX;
		Rasterizer.centerY = centerY;
		Rasterizer.lineOffsets = lineOffsets;
		Rasterizer.notTextured = true;
		if (item.stackable) {
			image.maxWidth = 33;
		} else {
			image.maxWidth = 32;
		}
		image.maxHeight = size;
		return image;
	}

	public boolean wearable() {
		if (actions != null) {
			for (int i = 0; i != actions.length; ++i) {
				String action = actions[i];
				if (action == null)
					continue;

				action = action.replace('_', ' ').trim().toLowerCase();
				for (int i1 = 0; i1 != wears.length; ++i1)
					if (action.indexOf(wears[i1]) != -1)
						return true;
			}
		}
		return false;
	}

	public boolean isValid(int anInt1702) {
		if (id < 0 || id >= totalItems)
			return false;

		if (modelID == 0)
			return false;

		if (name == null)
			return false;

		return true;
	}

	public boolean method192(int j) {
		int k = anInt175;
		int l = anInt166;
		if (j == 1) {
			k = anInt197;
			l = anInt173;
		}
		if (k == -1)
			return true;
		boolean flag = true;
		if (!Model.method463(k))
			flag = false;
		if (l != -1 && !Model.method463(l))
			flag = false;
		return flag;
	}

	public static void unpackConfig(byte[] dat, byte[] idx) {
		buffer = new Buffer(dat);
		totalItems = idx.length / 2;
		streamIndices = new int[totalItems];
		int index = 0;
		Buffer indexBuffer = new Buffer(idx);
		for (int i = 0; i != totalItems; ++i) {
			int size = indexBuffer.getUnsignedShort();
			streamIndices[i] = size != 0 ? index : -1;
			index += size;
		}

		cache = new ItemDef[50];
		for (int k = 0; k < 50; k++)
			cache[k] = new ItemDef();

		/*
		 * try { BufferedWriter writer = new BufferedWriter(new
		 * FileWriter("items.txt")); try { for (int i = 0; i != totalItems; ++i)
		 * { ItemDef def = forID(i); writer.write(i + "	" + def.name);
		 * writer.newLine(); }
		 * 
		 * } finally { writer.close(); } } catch (Exception ex) { }
		 */
	}

	public Model method194(int j) {
		int k = anInt175;
		int l = anInt166;
		if (j == 1) {
			k = anInt197;
			l = anInt173;
		}
		if (k == -1)
			return null;
		Model model = Model.getModel(k);
		if (l != -1) {
			Model model_1 = Model.getModel(l);
			Model aclass30_sub2_sub4_sub6s[] = { model, model_1 };
			model = new Model(2, aclass30_sub2_sub4_sub6s);
		}
		if (modifiedModelColors != null) {
			for (int i1 = 0; i1 < originalModelColors.length; i1++)
				model.setColor(originalModelColors[i1], modifiedModelColors[i1]);
		}
		if (modifiedModelTextures != null) {
			for (int i1 = 0; i1 < modifiedModelTextures.length; i1++)
				model.setTexture(modifiedModelTextures[i1],
						originalModelTextures[i1]);
		}
		return model;
	}

	public boolean method195(int j) {
		int k = maleWearModel1;
		int l = femaleWearModel1;
		int i1 = colorEquip1;
		if (j == 1) {
			k = maleWearModel2;
			l = femaleWearModel2;
			i1 = colorEquip2;
		}
		if (k == -1)
			return true;
		boolean flag = true;
		if (!Model.method463(k))
			flag = false;
		if (l != -1 && !Model.method463(l))
			flag = false;
		if (i1 != -1 && !Model.method463(i1))
			flag = false;
		return flag;
	}

	public Model method196(int i) {
		int j = maleWearModel1;
		int k = femaleWearModel1;
		int l = colorEquip1;
		if (i == 1) {
			j = maleWearModel2;
			k = femaleWearModel2;
			l = colorEquip2;
		}
		if (j == -1)
			return null;
		Model model = Model.getModel(j);
		if (k != -1)
			if (l != -1) {
				Model model_1 = Model.getModel(k);
				Model model_3 = Model.getModel(l);
				Model aclass30_sub2_sub4_sub6_1s[] = { model, model_1, model_3 };
				model = new Model(3, aclass30_sub2_sub4_sub6_1s);
			} else {
				Model model_2 = Model.getModel(k);
				Model aclass30_sub2_sub4_sub6s[] = { model, model_2 };
				model = new Model(2, aclass30_sub2_sub4_sub6s);
			}
		if (i == 0 && aByte205 != 0)
			model.translate(0, aByte205, 0);
		if (i == 1 && aByte154 != 0)
			model.translate(0, aByte154, 0);
		if (modifiedModelColors != null) {
			for (int i1 = 0; i1 < originalModelColors.length; i1++)
				model.setColor(originalModelColors[i1], modifiedModelColors[i1]);
		}
		if (modifiedModelTextures != null) {
			for (int i1 = 0; i1 < modifiedModelTextures.length; i1++)
				model.setTexture(modifiedModelTextures[i1],
						originalModelTextures[i1]);
		}
		return model;
	}

	public static ItemDef forId(int i) {
		for (int j = 0; j < 50; j++)
			if (cache[j].id == i)
				return cache[j];

		cacheIndex = (cacheIndex + 1) % 50;
		ItemDef itemDef = cache[cacheIndex];
		itemDef.id = i;
		itemDef.setDefaults();
		int id = i;
		if (id >= 0 && id < totalItems && streamIndices[id] != -1) {
			buffer.currentOffset = streamIndices[id];
			itemDef.readValues(buffer);
		}
		if (itemDef.certTemplateID != -1)
			itemDef.toNote();

		if (itemDef.lendTemplateID != -1)
			itemDef.toLend();

		if (!isMembers && itemDef.membersObject) {
			itemDef.name = "Members Object";
			itemDef.description = Buffer
					.toBytes("Login to a members' server to use this object.");
			itemDef.groundActions = null;
			itemDef.actions = null;
			itemDef.team = 0;
		}
		switch (id) {
		case 19115:
			itemDef.name = "Ganodermic visor";
			itemDef.description = Buffer.toBytes("It's an Ganodermic visor");
			itemDef.actions = new String[] { null, "Wear", "Check", "Clean", "drop"};
			itemDef.groundActions = new String[] { null, null, "take", null, null};
			itemDef.modelID = 10935;
			itemDef.maleWearModel1 = 10373;
			itemDef.femaleWearModel1 = 10523;
			itemDef.modelZoom = 1118;
			itemDef.modelRotation1 = 215;
			itemDef.modelRotation2 = 175;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffset2 = -30;
		break;
		case 19114:
			itemDef.name = "Ganodermic poncho";
			itemDef.description = Buffer.toBytes("It's an Ganodermic poncho");
			itemDef.actions = new String[] { null, "Wear", "Check", "Clean", "drop"};
			itemDef.groundActions = new String[] { null, null, "take", null, null};
			itemDef.modelID = 10919;
			itemDef.maleWearModel1 = 10490;
			itemDef.femaleWearModel1 = 10664;
			itemDef.modelZoom = 1513;
			itemDef.modelRotation1 = 485;
			itemDef.modelRotation2 = 13;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffset2 = -3;
		break;
		case 19113:
			itemDef.name = "Ganodermic leggings";
			itemDef.description = Buffer.toBytes("It's an Ganodermic leggings");
			itemDef.actions = new String[] { null, "Wear", "Check", "Clean", "drop"};
			itemDef.groundActions = new String[] { null, null, "take", null, null};
			itemDef.modelID = 10942;
			itemDef.maleWearModel1 = 10486;
			itemDef.femaleWearModel1 = 10578;
			itemDef.modelZoom = 1513;
			itemDef.modelRotation1 = 498;
			itemDef.modelRotation2 = 0;
			itemDef.modelOffset1 = 8;
			itemDef.modelOffset2 = -18;
		break;
		case 19112:
			itemDef.name = "Polypore staff";
			itemDef.description = Buffer.toBytes("It's an Polypore staff");
			itemDef.actions = new String[] { null, "Wield", "Check", "Clean", "drop"};
			itemDef.groundActions = new String[] { null, null, "take", null, null};
			itemDef.modelID = 13426;
			itemDef.maleWearModel1 = 13417;
			itemDef.femaleWearModel1 = 13417;
			itemDef.modelZoom = 3750;
			itemDef.modelRotation1 = 1454;
			itemDef.modelRotation2 = 997;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffset2 = 8;
		break;
		case 19111:
			itemDef.name ="TokHaar-Kal";
			itemDef.value = 60000;
			itemDef.maleWearModel1 = 62575;
			itemDef.femaleWearModel1 = 62582;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.modelOffset1 = -4;
			itemDef.modelID = 62592;
			itemDef.stackable = false;
			itemDef.description = Buffer.toBytes("A cape made of ancient, enchanted rocks.");
			itemDef.modelZoom = 2086;
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelOffset2 = 0;
			itemDef.modelRotation1 = 533;
			itemDef.modelRotation2 = 333;
			break;
		}
		if (i == 689) {

			itemDef.modelID = 13436;
			itemDef.name = "Polypore Staff";
			itemDef.description = Buffer.toBytes("It's a Polypore Staff.");
			itemDef.modelZoom = 3250;
			itemDef.modelRotation1 = 550;
			itemDef.modelRotation2 = 1530;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffset2 = 0;
			itemDef.maleWearModel1 = 13416;
			if (forId(22494).name.toLowerCase().contains("polypore")) {
				itemDef.modelID = forId(22494).modelID;
				itemDef.maleWearModel1 = forId(22494).maleWearModel1;
				itemDef.modelRotation1 = forId(22494).modelRotation1;
				itemDef.modelRotation2 = forId(22494).modelRotation2;
				itemDef.modelZoom = forId(22494).modelZoom;
				itemDef.modelOffset1 = forId(22494).modelOffset1;
				itemDef.modelOffset2 = forId(22494).modelOffset2;
			}
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[0] = null;
			itemDef.actions[1] = "Wield";
			itemDef.actions[2] = null;
			itemDef.actions[3] = null;
			itemDef.actions[4] = "Drop";
		}
		if (i == 772) {

			itemDef.modelID = 13426;
			itemDef.name = "Polypore Staff";
			itemDef.description = Buffer.toBytes("It's a Polypore Staff.");
			itemDef.modelZoom = 3250;
			itemDef.modelRotation1 = 550;
			itemDef.modelRotation2 = 1530;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffset2 = 0;
			itemDef.maleWearModel1 = 13417;
			if (forId(22494).name.toLowerCase().contains("polypore")) {
				itemDef.modelID = forId(22494).modelID;
				itemDef.maleWearModel1 = forId(22494).maleWearModel1;
				itemDef.modelRotation1 = forId(22494).modelRotation1;
				itemDef.modelRotation2 = forId(22494).modelRotation2;
				itemDef.modelZoom = forId(22494).modelZoom;
				itemDef.modelOffset1 = forId(22494).modelOffset1;
				itemDef.modelOffset2 = forId(22494).modelOffset2;
			}
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[0] = null;
			itemDef.actions[1] = "Wield";
			itemDef.actions[2] = null;
			itemDef.actions[3] = null;
			itemDef.actions[4] = "Drop";
		}

		if (i == 919) {
			itemDef.femaleWearModel2 = 5409;
			itemDef.femaleWearModel1 = 5409;
			itemDef.modelID = 5412;
			itemDef.aByte154 = -5;
			itemDef.name = "Abyssal vine whip";
			itemDef.description = Buffer
					.toBytes("An abyssal whip wrapped with a vine.");
			itemDef.modelZoom = 848;
			itemDef.modelRotation1 = 324;
			itemDef.modelRotation2 = 1808;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffset2 = 9;
			itemDef.maleWearModel1 = 8725;
			itemDef.maleWearModel2 = 8725;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[0] = null;
			itemDef.groundActions[1] = null;
			itemDef.groundActions[2] = "Take";
			itemDef.groundActions[3] = null;
			itemDef.groundActions[4] = null;
			itemDef.actions = new String[5];
			itemDef.actions[0] = null;
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = null;
			itemDef.actions[3] = "Dismantle";
			itemDef.actions[4] = "Drop";
			itemDef.originalModelColors = new short[6];
			itemDef.modifiedModelColors = new short[6];
			itemDef.originalModelColors[0] = 6097;
			itemDef.originalModelColors[1] = 9158;
			itemDef.originalModelColors[2] = 9029;
			itemDef.originalModelColors[3] = 9215;
			itemDef.originalModelColors[4] = 9255;
			itemDef.originalModelColors[5] = 9221;
			itemDef.modifiedModelColors[0] = (short) 16745704;
			itemDef.modifiedModelColors[1] = (short) 31610065;
			itemDef.modifiedModelColors[2] = 16745;
			itemDef.modifiedModelColors[3] = (short) 34747;
			itemDef.modifiedModelColors[4] = (short) 34750;
			itemDef.modifiedModelColors[5] = (short) 57320;
		}
		switch (id) {
		case 11157:
			itemDef.femaleWearModel2 = -1;
			itemDef.femaleWearModel1 = -1;
			itemDef.name = "Ragefire boots";
			itemDef.modelID = 53897;
			itemDef.modelZoom = 900;
			itemDef.modelRotation1 = 165;
			itemDef.modelRotation2 = 99;
			itemDef.modelOffset1 = 3;
			itemDef.modelOffset2 = -7;
			itemDef.maleWearModel1 = 53330;
			itemDef.maleWearModel2 = 53330;
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.description = Buffer.toBytes("Some Ragefire boots.");

			break;
		case 11158:

			itemDef.femaleWearModel2 = -1;
			itemDef.femaleWearModel1 = -1;
			itemDef.name = "Steadfast Boots";
			itemDef.modelID = 53835;
			itemDef.modelZoom = 900;
			itemDef.modelRotation1 = 165;
			itemDef.modelRotation2 = 99;
			itemDef.modelOffset1 = 3;
			itemDef.modelOffset2 = -7;
			itemDef.maleWearModel1 = 53327;
			itemDef.maleWearModel2 = 53327;
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.description = Buffer.toBytes("Some Steadfast boots.");
			break;
		case 13362:
			itemDef.modelID = 62714;
			itemDef.name = "Torva full helm";
			itemDef.description = Buffer.toBytes("Torva full helm.");
			itemDef.modelZoom = 672;
			itemDef.modelRotation1 = 85;
			itemDef.modelRotation2 = 1867;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffset2 = -3;
			itemDef.maleWearModel1 = 62738;
			itemDef.maleWearModel2 = 62754;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";

			itemDef.femaleWearModel2 = -1;
			itemDef.femaleWearModel1 = -1;
			break;

		case 13360:
			itemDef.modelID = 62701;
			itemDef.name = "Torva platelegs";
			itemDef.description = Buffer.toBytes("Torva platelegs.");
			itemDef.modelZoom = 1740;
			itemDef.modelRotation1 = 474;
			itemDef.modelRotation2 = 2045;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffset2 = -5;
			itemDef.maleWearModel1 = 62743;
			itemDef.maleWearModel2 = 62760;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";

			itemDef.femaleWearModel2 = -1;
			itemDef.femaleWearModel1 = -1;
			break;

		case 13358:
			itemDef.modelID = 62699;
			itemDef.name = "Torva platebody";
			itemDef.description = Buffer.toBytes("Torva Platebody.");
			itemDef.modelZoom = 1506;
			itemDef.modelRotation1 = 473;
			itemDef.modelRotation2 = 2042;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffset2 = 0;
			itemDef.maleWearModel1 = 62746;
			itemDef.maleWearModel2 = 62762;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";

			itemDef.femaleWearModel2 = -1;
			itemDef.femaleWearModel1 = -1;
			break;
		case 13355:
			itemDef.modelID = 62693;
			itemDef.name = "Pernix cowl";
			itemDef.description = Buffer.toBytes("Pernix cowl");
			itemDef.modelZoom = 800;
			itemDef.modelRotation1 = 532;
			itemDef.modelRotation2 = 14;
			itemDef.modelOffset1 = -1;
			itemDef.modelOffset2 = 1;
			itemDef.maleWearModel1 = 62739;
			itemDef.maleWearModel2 = 62756;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			itemDef.anInt175 = 62731;
			itemDef.anInt197 = 62727;

			itemDef.femaleWearModel2 = -1;
			itemDef.femaleWearModel1 = -1;
			break;

		case 13354:
			itemDef.modelID = 62709;
			itemDef.name = "Pernix body";
			itemDef.description = Buffer.toBytes("Pernix body");
			itemDef.modelZoom = 1378;
			itemDef.modelRotation1 = 485;
			itemDef.modelRotation2 = 2042;
			itemDef.modelOffset1 = -1;
			itemDef.modelOffset2 = 7;
			itemDef.maleWearModel1 = 62744;
			itemDef.maleWearModel2 = 62765;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";

			itemDef.femaleWearModel2 = -1;
			itemDef.femaleWearModel1 = -1;
			break;

		case 13352:
			itemDef.modelID = 62695;
			itemDef.name = "Pernix chaps";
			itemDef.description = Buffer.toBytes("Pernix chaps");
			itemDef.modelZoom = 1740;
			itemDef.modelRotation1 = 504;
			itemDef.modelRotation2 = 0;
			itemDef.modelOffset1 = 4;
			itemDef.modelOffset2 = 3;
			itemDef.maleWearModel1 = 62741;
			itemDef.maleWearModel2 = 62757;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";

			itemDef.femaleWearModel2 = -1;
			itemDef.femaleWearModel1 = -1;
			break;

		case 13350:
			itemDef.modelID = 62710;
			itemDef.name = "Virtus mask";
			itemDef.description = Buffer.toBytes("Virtus mask");
			itemDef.modelZoom = 928;
			itemDef.modelRotation1 = 406;
			itemDef.modelRotation2 = 2041;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffset2 = -5;
			itemDef.maleWearModel1 = 62736;
			itemDef.maleWearModel2 = 62755;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			itemDef.anInt175 = 62728;
			itemDef.anInt197 = 62728;

			itemDef.femaleWearModel2 = -1;
			itemDef.femaleWearModel1 = -1;
			break;

		case 13348:
			itemDef.modelID = 62704;
			itemDef.name = "Virtus robe top";
			itemDef.description = Buffer.toBytes("Virtus robe top");
			itemDef.modelZoom = 1122;
			itemDef.modelRotation1 = 488;
			itemDef.modelRotation2 = 3;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffset2 = 0;
			itemDef.maleWearModel1 = 62748;
			itemDef.maleWearModel2 = 62764;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";

			itemDef.femaleWearModel2 = -1;
			itemDef.femaleWearModel1 = -1;
			break;
		case 13346:
			itemDef.modelID = 62700;
			itemDef.name = "Virtus robe legs";
			itemDef.description = Buffer.toBytes("Virtus robe legs");
			itemDef.modelZoom = 1740;
			itemDef.modelRotation1 = 498;
			itemDef.modelRotation2 = 2045;
			itemDef.modelOffset1 = -1;
			itemDef.modelOffset2 = 4;
			itemDef.maleWearModel1 = 62742;
			itemDef.maleWearModel2 = 62758;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";

			itemDef.femaleWearModel2 = -1;
			itemDef.femaleWearModel1 = -1;
			break;
		}
		return itemDef;
	}

	private void toNote() {
		ItemDef itemDef = forId(certTemplateID);
		modelID = itemDef.modelID;
		modelZoom = itemDef.modelZoom;
		modelRotation1 = itemDef.modelRotation1;
		modelRotation2 = itemDef.modelRotation2;
		anInt204 = itemDef.anInt204;
		modelOffset1 = itemDef.modelOffset1;
		modelOffset2 = itemDef.modelOffset2;
		originalModelColors = itemDef.originalModelColors;
		modifiedModelColors = itemDef.modifiedModelColors;
		originalModelTextures = itemDef.originalModelTextures;
		modifiedModelTextures = itemDef.modifiedModelTextures;
		ItemDef itemDef_1 = forId(certID);
		name = itemDef_1.name;
		membersObject = itemDef_1.membersObject;
		value = itemDef_1.value;
		String article = "a";
		char c = itemDef_1.name.charAt(0);
		if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
			article = "an";
		}
		description = Buffer.toBytes("Swap this note at any bank for "
				+ article + " " + itemDef_1.name + ".");
		stackable = true;
	}

	private static Object clone(int[] object) {
		return object != null ? object.clone() : null;
	}

	private static Object clone(byte[] object) {
		return object != null ? object.clone() : null;
	}

	private static Object clone(short[] object) {
		return object != null ? object.clone() : null;
	}

	private static Object clone(String[] object) {
		return object != null ? object.clone() : null;
	}

	private void toLend() {
		ItemDef itemDef = forId(lendTemplateID);
		modelID = itemDef.modelID;
		modelRotation1 = itemDef.modelRotation1;
		modelRotation2 = itemDef.modelRotation2;
		modelOffset1 = itemDef.modelOffset1;
		modelOffset2 = itemDef.modelOffset2;
		modelZoom = itemDef.modelZoom;
		anInt204 = itemDef.anInt204;
		itemDef = forId(lendID);
		name = itemDef.name;
		membersObject = itemDef.membersObject;
		value = 0;
		maleWearModel1 = itemDef.maleWearModel1;
		maleWearModel2 = itemDef.maleWearModel2;
		femaleWearModel1 = itemDef.femaleWearModel1;
		femaleWearModel2 = itemDef.femaleWearModel2;
		colorEquip1 = itemDef.colorEquip1;
		colorEquip2 = itemDef.colorEquip2;
		team = itemDef.team;
		actions = (String[]) clone(itemDef.actions);
		groundActions = (String[]) clone(itemDef.groundActions);
		stackable = itemDef.stackable;
		stackAmounts = (int[]) clone(itemDef.stackAmounts);
		stackIDs = (int[]) clone(itemDef.stackIDs);
		originalModelColors = (short[]) clone(itemDef.originalModelColors);
		modifiedModelColors = (short[]) clone(itemDef.modifiedModelColors);
		originalModelTextures = (short[]) clone(itemDef.originalModelTextures);
		modifiedModelTextures = (short[]) clone(itemDef.modifiedModelTextures);
		description = (byte[]) clone(itemDef.description); // TODO: figure out
															// what the examine
															// for these are.
		if (actions == null)
			actions = new String[5];

		actions[4] = "Discard";
	}

	public static Sprite getSprite(int id, int amount, int color) {
		if (color == 0) {
			Sprite sprite = (Sprite) mruNodes1.insertFromCache(id);
			if (sprite != null && sprite.maxHeight != amount
					&& sprite.maxHeight != -1) {
				sprite.remove();
				sprite = null;
			}
			if (sprite != null) {
				return sprite;
			}
		}
		ItemDef def = forId(id);
		if (def.stackIDs == null) {
			amount = -1;
		}
		if (amount > 1) {
			int stackId = -1;
			for (int index = 0; index < 10; index++) {
				if (amount >= def.stackAmounts[index]
						&& def.stackAmounts[index] != 0) {
					stackId = def.stackIDs[index];
				}
			}
			if (stackId != -1) {
				def = forId(stackId);
			}
		}
		Model model = def.getModelForAmount(amount);
		if (model == null) {
			return null;
		}
		Sprite sprite1 = null;
		if (def.certTemplateID != -1) {
			sprite1 = getSprite(def.certID, 10, -1);
			if (sprite1 == null) {
				return null;
			}
		}
		if (def.lendTemplateID != -1) {
			sprite1 = getSprite(def.lendID, 50, 0);
			if (sprite1 == null) {
				return null;
			}
		}
		Sprite sprite2 = new Sprite(32, 32);
		int centerX = Rasterizer.centerX;
		int centerY = Rasterizer.centerY;
		int lineOffsets[] = Rasterizer.lineOffsets;
		int pixels[] = DrawingArea.pixels;
		int width = DrawingArea.width;
		int height = DrawingArea.height;
		int startX = DrawingArea.topX;
		int endX = DrawingArea.bottomX;
		int startY = DrawingArea.topY;
		int endY = DrawingArea.bottomY;
		DrawingArea.initDrawingArea(32, 32, sprite2.myPixels);
		DrawingArea.drawPixels(32, 0, 0, 0, 32);
		Rasterizer.setDefaultBounds();
		int zoom = def.modelZoom;
		if (color == -1) {
			zoom = (int) ((double) zoom * 1.5D);
		}
		if (color > 0) {
			zoom = (int) ((double) zoom * 1.04D);
		}
		int sine = Rasterizer.SINE[def.modelRotation1] * zoom >> 16;
		int cosine = Rasterizer.COSINE[def.modelRotation1] * zoom >> 16;
		boolean empty = !model.method482(def.modelRotation2, def.anInt204,
				def.modelRotation1, def.modelOffset1, sine + model.modelHeight
						/ 2 + def.modelOffset2, cosine + def.modelOffset2);
		for (int i4 = 31; i4 >= 0; i4--) {
			for (int j5 = 31; j5 >= 0; j5--) {
				if (sprite2.myPixels[i4 + j5 * 32] != 0)
					continue;
				if (i4 > 0 && sprite2.myPixels[(i4 - 1) + j5 * 32] > 1) {
					sprite2.myPixels[i4 + j5 * 32] = 1;
					continue;
				}
				if (j5 > 0 && sprite2.myPixels[i4 + (j5 - 1) * 32] > 1) {
					sprite2.myPixels[i4 + j5 * 32] = 1;
					continue;
				}
				if (i4 < 31 && sprite2.myPixels[i4 + 1 + j5 * 32] > 1) {
					sprite2.myPixels[i4 + j5 * 32] = 1;
					continue;
				}
				if (j5 < 31 && sprite2.myPixels[i4 + (j5 + 1) * 32] > 1) {
					sprite2.myPixels[i4 + j5 * 32] = 1;
				}
			}
		}
		if (color > 0) {
			for (int j4 = 31; j4 >= 0; j4--) {
				for (int k5 = 31; k5 >= 0; k5--) {
					if (sprite2.myPixels[j4 + k5 * 32] != 0) {
						continue;
					}
					if (j4 > 0 && sprite2.myPixels[(j4 - 1) + k5 * 32] == 1) {
						sprite2.myPixels[j4 + k5 * 32] = color;
						continue;
					}
					if (k5 > 0 && sprite2.myPixels[j4 + (k5 - 1) * 32] == 1) {
						sprite2.myPixels[j4 + k5 * 32] = color;
						continue;
					}
					if (j4 < 31 && sprite2.myPixels[j4 + 1 + k5 * 32] == 1) {
						sprite2.myPixels[j4 + k5 * 32] = color;
						continue;
					}
					if (k5 < 31 && sprite2.myPixels[j4 + (k5 + 1) * 32] == 1) {
						sprite2.myPixels[j4 + k5 * 32] = color;
					}
				}
			}
		} else if (color == 0) {
			for (int k4 = 31; k4 >= 0; k4--) {
				for (int l5 = 31; l5 >= 0; l5--) {
					if (sprite2.myPixels[k4 + l5 * 32] == 0 && k4 > 0 && l5 > 0
							&& sprite2.myPixels[(k4 - 1) + (l5 - 1) * 32] > 0) {
						sprite2.myPixels[k4 + l5 * 32] = 0x302020;
					}
				}
			}
		}
		if (def.certTemplateID != -1) {
			int maxWidth = sprite1.maxWidth;
			int maxHeight = sprite1.maxHeight;
			sprite1.maxWidth = 32;
			sprite1.maxHeight = 32;
			sprite1.drawSprite(0, 0);
			sprite1.maxWidth = maxWidth;
			sprite1.maxHeight = maxHeight;
		}
		if (def.lendTemplateID != -1) {
			int maxWidth = sprite1.maxWidth;
			int maxHeight = sprite1.maxHeight;
			sprite1.maxWidth = 32;
			sprite1.maxHeight = 32;
			sprite1.drawSprite(0, 0);
			sprite1.maxWidth = maxWidth;
			sprite1.maxHeight = maxHeight;
		}
		if (color == 0 && !empty) {
			mruNodes1.removeFromCache(sprite2, id);
		}
		DrawingArea.initDrawingArea(height, width, pixels);
		DrawingArea.setDrawingArea(startX, startY, endX, endY);
		Rasterizer.centerX = centerX;
		Rasterizer.centerY = centerY;
		Rasterizer.lineOffsets = lineOffsets;
		if (def.stackable) {
			sprite2.maxHeight = 33;
		} else {
			sprite2.maxHeight = 32;
		}
		sprite2.maxHeight = amount;
		return sprite2;
	}

	public Model getModelForAmount(int amount) {
		if (stackIDs != null && amount > 1) {
			int stackId = -1;
			for (int index = 0; index < 10; index++) {
				if (amount >= stackAmounts[index] && stackAmounts[index] != 0) {
					stackId = stackIDs[index];
				}
			}
			if (stackId != -1) {
				return forId(stackId).getModelForAmount(1);
			}
		}
		Model model = (Model) mruNodes2.insertFromCache(id);
		if (model != null) {
			return model;
		}
		model = Model.getModel(modelID);
		if (model == null)
			return null;
		if (anInt167 != 128 || anInt192 != 128 || anInt191 != 128)
			model.scale(anInt167, anInt191, anInt192);
		if (modifiedModelColors != null) {
			for (int l = 0; l < originalModelColors.length; l++) {
				model.setColor(originalModelColors[l], modifiedModelColors[l]);
			}
		}
		if (originalModelTextures != null) {
			for (int i1 = 0; i1 < originalModelTextures.length; i1++) {
				model.setTexture(originalModelTextures[i1],
						modifiedModelTextures[i1]);
			}
		}
		model.setLighting(64 + anInt196, 768 + anInt184, -50, -10, -50, true, true);
		model.aBoolean1659 = true;
		mruNodes2.removeFromCache(model, id);
		return model;
	}

	public Model method202(int amount) {
		if (stackIDs != null && amount > 1) {
			int stackId = -1;
			for (int index = 0; index < 10; index++) {
				if (amount >= stackAmounts[index] && stackAmounts[index] != 0) {
					stackId = stackIDs[index];
				}
			}
			if (stackId != -1) {
				return forId(stackId).method202(1);
			}
		}
		Model model = Model.getModel(modelID);
		if (model == null) {
			return null;
		}
		if (modifiedModelColors != null) {
			for (int color = 0; color < originalModelColors.length; color++) {
				model.setColor(originalModelColors[color],
						modifiedModelColors[color]);
			}
		}
		if (originalModelTextures != null) {
			for (int texture = 0; texture < originalModelTextures.length; texture++) {
				model.setTexture(originalModelTextures[texture],
						modifiedModelTextures[texture]);
			}
		}
		return model;
	}

	public int anInt1849;
	public int anInt1851;
	public int lendTemplateID;
	public int maleWearModel1;
	public int certTemplateID;
	public int anInt1859;
	public int anInt1862;
	public int anInt197;
	public int anInt1864;
	public int anInt204;
	public int colorEquip1;
	public int modelRotation2;
	public int modelZoom;
	public int modelOffset2;
	public int anInt196;
	public int anInt1877;
	public int femaleWearModel1;
	public int anInt1879;
	public int maleWearModel2;
	public int femaleWearModel2;
	public int modelRotation1;
	public int anInt184;
	public int certID;
	public int anInt166;
	public int anInt1819;
	public int anInt1890;
	public int anInt1891;
	public boolean stackable;
	public int anInt1893;
	public int anInt1895;
	public int anInt191;
	public int anInt173;
	public int modelID;
	public int team;
	public int anInt1900;
	public int anInt167;
	public int modelOffset1;
	public int anInt1908;
	public int anInt192;
	public int anInt175;
	public int id;
	public int colorEquip2;
	public int anInt1916;
	public int anInt1919;
	public int value;
	public int lendID;
	public int anInt1928;
	public int anInt1930;
	public int anInt1931;
	public String name;
	public boolean membersObject;
	public boolean aBoolean1933;
	public byte[] aByteArray1882;
	public short[] modifiedModelTextures;
	public short[] modifiedModelColors;
	public short[] originalModelColors;
	public short[] originalModelTextures;
	public int[] stackAmounts;
	public int[] stackIDs;
	public int[] anIntArray1926;
	public String[] actions;
	public String[] groundActions;
	public Hashtable<Integer, Object> aHashTable1873;

	public void setDefaults() {
		aByte205 = 0;
		aByte154 = 0;
		description = null;
		aByteArray1882 = null;
		modifiedModelTextures = null;
		modifiedModelColors = null;
		originalModelColors = null;
		originalModelTextures = null;
		stackAmounts = null;
		stackIDs = null;
		anIntArray1926 = null;
		actions = null;
		groundActions = null;
		aHashTable1873 = null;
		modelID = 0;
		anInt1851 = -1;
		anInt1849 = -1;
		maleWearModel1 = -1;
		anInt1864 = -1;
		femaleWearModel1 = -1;
		anInt204 = 0;
		lendTemplateID = -1;
		anInt1862 = -1;
		anInt1891 = -1;
		colorEquip1 = -1;
		anInt196 = 0;
		anInt184 = 0;
		anInt197 = -1;
		modelZoom = 2000;
		anInt1879 = -1;
		team = 0;
		membersObject = false;
		anInt192 = 128;
		modelOffset1 = 0;
		name = "null";
		anInt1859 = -1;
		anInt167 = 128;
		anInt166 = -1;
		anInt173 = -1;
		anInt1908 = -1;
		modelRotation2 = 0;
		anInt1895 = 0;
		anInt1819 = -1;
		anInt1900 = -1;
		anInt1893 = 0;
		femaleWearModel2 = -1;
		modelOffset2 = 0;
		anInt1890 = 0;
		anInt175 = -1;
		anInt1919 = 0;
		stackable = false;
		anInt191 = 128;
		colorEquip2 = -1;
		certTemplateID = -1;
		certID = -1;
		value = 1;
		anInt1877 = 0;
		anInt1928 = -1;
		modelRotation1 = 0;
		lendID = -1;
		maleWearModel2 = -1;
		anInt1930 = 0;
		anInt1916 = 0;
		anInt1931 = 0;
		aBoolean1933 = false;
	}

	public void readValues(Buffer buffer) {
		while (true) {
			int opcode = buffer.getUnsigned();
			if (opcode == 0)
				break;

			if (opcode == 1)
				modelID = buffer.getUnsignedShort();
			else if (opcode == 2)
				name = buffer.getString();
			else if (opcode == 4)
				modelZoom = buffer.getUnsignedShort();
			else if (opcode == 5)
				modelRotation1 = buffer.getUnsignedShort();
			else if (opcode == 6)
				modelRotation2 = buffer.getUnsignedShort();
			else if (opcode == 7)
				modelOffset1 = buffer.getSignedShort();
			else if (opcode == 8)
				modelOffset2 = buffer.getSignedShort();
			else if (opcode == 11)
				stackable = true;
			else if (opcode == 12)
				value = buffer.getInt();
			else if (opcode == 16)
				membersObject = true;
			else if (opcode == 18)
				anInt1879 = buffer.getUnsignedShort();
			else if (opcode == 23)
				maleWearModel1 = buffer.getUnsignedShort();
			else if (opcode == 24)
				femaleWearModel1 = buffer.getUnsignedShort();
			else if (opcode == 25)
				maleWearModel2 = buffer.getUnsignedShort();
			else if (opcode == 26)
				femaleWearModel2 = buffer.getUnsignedShort();

			else if (opcode >= 30 && opcode < 35) {
				if (groundActions == null)
					groundActions = new String[5];

				groundActions[opcode - 30] = buffer.getString();
			} else if (opcode >= 35 && opcode < 40) {
				if (actions == null)
					actions = new String[5];

				actions[opcode - 35] = buffer.getString();
			} else if (opcode == 40) {
				int count = buffer.getUnsigned();
				originalModelColors = new short[count];
				modifiedModelColors = new short[count];
				for (int i = 0; i != count; ++i) {
					originalModelColors[i] = (short) buffer.getUnsignedShort();
					modifiedModelColors[i] = (short) buffer.getUnsignedShort();
				}
			} else if (opcode == 41) {
				int count = buffer.getUnsigned();
				originalModelTextures = new short[count];
				modifiedModelTextures = new short[count];
				for (int i = 0; i != count; ++i) {
					originalModelTextures[i] = (short) buffer
							.getUnsignedShort();
					modifiedModelTextures[i] = (short) buffer
							.getUnsignedShort();
				}

			} else if (opcode == 42) {
				int count = buffer.getUnsigned();
				aByteArray1882 = new byte[count];
				for (int i = 0; i != count; ++i)
					aByteArray1882[i] = buffer.get();

			} else if (opcode == 65)
				aBoolean1933 = true;
			else if (opcode == 78)
				colorEquip1 = buffer.getUnsignedShort();
			else if (opcode == 79)
				colorEquip2 = buffer.getUnsignedShort();
			else if (opcode == 90)
				anInt175 = buffer.getUnsignedShort();
			else if (opcode == 91)
				anInt197 = buffer.getUnsignedShort();
			else if (opcode == 92)
				anInt166 = buffer.getUnsignedShort();
			else if (opcode == 93)
				anInt173 = buffer.getUnsignedShort();
			else if (opcode == 95)
				anInt204 = buffer.getUnsignedShort();
			else if (opcode == 96)
				anInt1877 = buffer.getUnsigned();
			else if (opcode == 97)
				certID = buffer.getUnsignedShort();
			else if (opcode == 98)
				certTemplateID = buffer.getUnsignedShort();

			else if (opcode >= 100 && opcode < 110) {
				if (stackIDs == null) {
					stackIDs = new int[10];
					stackAmounts = new int[10];
				}
				stackIDs[opcode - 100] = buffer.getUnsignedShort();
				stackAmounts[opcode - 100] = buffer.getUnsignedShort();
			} else if (opcode == 110)
				anInt167 = buffer.getUnsignedShort();
			else if (opcode == 111)
				anInt192 = buffer.getUnsignedShort();
			else if (opcode == 112)
				anInt191 = buffer.getUnsignedShort();
			else if (opcode == 113)
				anInt196 = buffer.get();
			else if (opcode == 114)
				anInt184 = buffer.get() * 5;
			else if (opcode == 115)
				team = buffer.getUnsigned();
			else if (opcode == 121)
				lendID = buffer.getUnsignedShort();
			else if (opcode == 122)
				lendTemplateID = buffer.getUnsignedShort();

			else if (opcode == 125) {
				anInt1931 = buffer.get();
				anInt1930 = buffer.get();
				anInt1895 = buffer.get();
			} else if (opcode == 126) {
				anInt1890 = buffer.get();
				anInt1893 = buffer.get();
				anInt1916 = buffer.get();
			} else if (opcode == 127) {
				anInt1908 = buffer.getUnsigned();
				anInt1819 = buffer.getUnsignedShort();
			} else if (opcode == 128) {
				anInt1849 = buffer.getUnsigned();
				anInt1851 = buffer.getUnsignedShort();
			} else if (opcode == 129) {
				anInt1900 = buffer.getUnsigned();
				anInt1859 = buffer.getUnsignedShort();
			} else if (opcode == 130) {
				anInt1864 = buffer.getUnsigned();
				anInt1862 = buffer.getUnsignedShort();
			} else if (opcode == 132) {
				int count = buffer.getUnsigned();
				anIntArray1926 = new int[count];
				for (int i = 0; i != count; ++i)
					anIntArray1926[i] = buffer.getUnsignedShort();

			} else if (opcode == 134)
				anInt1919 = buffer.getUnsigned();
			else if (opcode == 139)
				anInt1891 = buffer.getUnsignedShort();
			else if (opcode == 140)
				anInt1928 = buffer.getUnsignedShort();

			else if (opcode == 249) {
				int count = buffer.getUnsigned();
				if (aHashTable1873 == null)
					aHashTable1873 = new Hashtable<Integer, Object>();

				for (int i = 0; i != count; ++i) {
					boolean string = buffer.getUnsigned() == 1;
					int key = buffer.getMedium();
					Object value = string ? buffer.getString() : Integer
							.valueOf(buffer.getInt());
					aHashTable1873.put(Integer.valueOf(key), value);
				}

			} else {
				System.out.println("[ItemDef] Unknown opcode: " + opcode);
				break;
			}
		}
	}

	private byte aByte154;
	static Cache mruNodes1 = new Cache(100);
	public static Cache mruNodes2 = new Cache(50);
	private static ItemDef[] cache;
	public byte description[];
	private static int cacheIndex;
	public static boolean isMembers = true;
	private static Buffer buffer;
	private static int[] streamIndices;
	public static int totalItems;
	private byte aByte205;
}
