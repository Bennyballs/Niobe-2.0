package src;

import java.util.Hashtable;

import src.constants.Constants;

public final class ObjectDefinition {
	
	public void readValues(Buffer buffer) {
		while (true) {
			int opcode = buffer.getUnsigned();
			if (opcode == 0)
				break;

			if (opcode == 1 || opcode == 5) {
				boolean lowMem_ = lowMem;
				if (opcode == 5 && lowMem_) {
					skip(buffer);
				}
				int count = buffer.getUnsigned();
				modelTypes = new int[count];
				modelIds = new int[count][];
				for (int i = 0; i != count; ++i) {
					modelTypes[i] = buffer.getUnsigned();
					int childCount = buffer.getUnsigned();
					modelIds[i] = new int[childCount];
					for (int i1 = 0; i1 != childCount; ++i1)
						modelIds[i][i1] = buffer.getUnsignedShort();
				}
				if (opcode == 5 && !lowMem_)
					skip(buffer);
			}
			else if (opcode == 2)
				name = buffer.getString();
			else if (opcode == 14)
				sizeX = buffer.getUnsigned();
			else if (opcode == 15)
				sizeY = buffer.getUnsigned();

			else if (opcode == 17) {
				aBoolean3034 = false;
				anInt3010 = 0;
				walkable = false;
			}
			else if (opcode == 18) {
				aBoolean3034 = false;
				aBoolean757 = false;
			}
			else if (opcode == 19)
				anInt3057 = buffer.getUnsigned();
			else if (opcode == 21)
			{
				aByte3027 = (byte) 1;
				adjustToTerrain = true;
			}
			else if (opcode == 22)
				delayShading = true;
			else if (opcode == 23)
				anInt2977 = 1;
			else if (opcode == 24)
			{
				int n = buffer.getUnsignedShort();
				if (n != 0xffff)
				{
					anIntArray3019 = new int[] { n };
					anInt781 = n;
				}
			}
			else if (opcode == 27)
				anInt3010 = 1;
			else if (opcode == 28)
				anInt775 = buffer.getUnsigned();
			else if (opcode == 29)
				lightAmbient = buffer.get();
			else if (opcode == 39)
				lightDifuse = 5 * buffer.get();

			else if (opcode >= 30 && opcode < 35)
			{
				if (actions == null)
					actions = new String[5];

				actions[opcode - 30] = buffer.getString();
			}
			else if (opcode == 40)
			{
				int count = buffer.getUnsigned();
				modifiedModelColors = new short[count];
				originalModelColors = new short[count];
				for (int i = 0; i != count; ++i)
				{
					modifiedModelColors[i] = (short) buffer.getUnsignedShort();
					originalModelColors[i] = (short) buffer.getUnsignedShort();
				}

			}
			else if (opcode == 41)
			{
				int count = buffer.getUnsigned();
				modifiedModelTextures = new short[count];
				originalModelTextures = new short[count];
				for (int i = 0; i != count; ++i)
				{
					modifiedModelTextures[i] = (short) buffer.getUnsignedShort();
					originalModelTextures[i] = (short) buffer.getUnsignedShort();
				}

			}
			else if (opcode == 42)
			{
				int count = buffer.getUnsigned();
				aByteArray2996 = new byte[count];
				for (int i = 0; i != count; ++i)
					aByteArray2996[i] = buffer.get();

			}
			else if (opcode == 62)
				rotated = true;
			else if (opcode == 64)
				aBoolean779 = false;
			else if (opcode == 65)
				modelSizeX = buffer.getUnsignedShort();
			else if (opcode == 66)
				modelSizeY = buffer.getUnsignedShort();
			else if (opcode == 67)
				modelSizeZ = buffer.getUnsignedShort();
			else if (opcode == 69)
				face = buffer.getUnsigned();
			else if (opcode == 70)
				translateX = buffer.getSignedShort();
			else if (opcode == 71)
				translateY = buffer.getSignedShort();
			else if (opcode == 72)
				translateZ = buffer.getSignedShort();
			else if (opcode == 73)
				aBoolean736 = true;
			else if (opcode == 74)
				aBoolean766 = true;
			else if (opcode == 75)
				solid = buffer.getUnsigned();

			else if (opcode == 77 || opcode == 92)
			{
				anInt774 = buffer.getUnsignedShort();
				if (anInt774 == 0xffff)
					anInt774 = -1;

				anInt749 = buffer.getUnsignedShort();
				if (anInt749 == 0xffff)
					anInt749 = -1;

				int ending = -1;
				if (opcode == 92)
				{
					ending = buffer.getUnsignedShort();
					if (ending == 0xffff)
						ending = -1;

				}
				int count = buffer.getUnsigned();
				childrenIDs = new int[count + 2];
				for (int i = 0; i <= count; ++i)
				{
					childrenIDs[i] = buffer.getUnsignedShort();
					if (childrenIDs[i] == 0xffff)
						childrenIDs[i] = -1;

				}

				childrenIDs[count + 1] = ending;
			}
			else if (opcode == 78)
			{
				anInt3015 = buffer.getUnsignedShort();
				anInt3012 = buffer.getUnsigned();
			}
			else if (opcode == 79)
			{
				anInt2989 = buffer.getUnsignedShort();
				anInt2971 = buffer.getUnsignedShort();
				anInt3012 = buffer.getUnsigned();
				int count = buffer.getUnsigned();
				anIntArray3036 = new int[count];
				for (int i = 0; i != count; ++i)
					anIntArray3036[i] = buffer.getUnsignedShort();

			}
			else if (opcode == 81)
			{
				aByte3027 = (byte) 2;
				anInt3023 = buffer.getUnsigned() * 256;
			}
			else if (opcode == 82)
				aBoolean2990 = true;
			else if (opcode == 88)
				aBoolean2972 = false;
			else if (opcode == 89)
				aBoolean3000 = false;
			else if (opcode == 91)
				aBoolean3002 = true;
			else if (opcode == 93)
			{
				aByte3027 = (byte) 3;
				anInt3023 = buffer.getUnsignedShort();
			}
			else if (opcode == 94)
				aByte3027 = (byte) 4;

			else if (opcode == 95)
			{
				aByte3027 = (byte) 5;
				anInt3023 = buffer.getSignedShort();
			}
			else if (opcode == 97)
				aBoolean3056 = true;
			else if (opcode == 98)
				aBoolean2998 = true;

			else if (opcode == 99)
			{
				anInt2987 = buffer.getUnsigned();
				anInt3008 = buffer.getUnsignedShort();
			}
			else if (opcode == 100)
			{
				anInt3038 = buffer.getUnsigned();
				anInt3013 = buffer.getUnsignedShort();
			}
			else if (opcode == 101)
				anInt2958 = buffer.getUnsigned();
			else if (opcode == 102)
				anInt3006 = buffer.getUnsignedShort();
			else if (opcode == 103)
				anInt2977 = 0;
			else if (opcode == 104)
				anInt3024 = buffer.getUnsigned();
			else if (opcode == 105)
				aBoolean3007 = true;

			else if (opcode == 106)
			{
				int count = buffer.getUnsigned();
				int total = 0;
				anIntArray3019 = new int[count];
				anIntArray2995 = new int[count];
				for (int i = 0; i != count; ++i)
				{
					anIntArray3019[i] = buffer.getUnsignedShort();
					if (anIntArray3019[i] == 0xffff)
						anIntArray3019[i] = -1;

					if (i == 0)
						anInt781 = anIntArray3019[i];

					total += anIntArray2995[i] = buffer.getUnsigned();
				}

				for (int i = 0; i != count; ++i)
					anIntArray2995[i] = anIntArray2995[i] * 0xffff / total;

			}
			else if (opcode == 107)
				anInt2994 = buffer.getUnsignedShort();

			else if (opcode >= 150 && opcode < 155)
			{
				if (actions == null)
					actions = new String[5];

				actions[opcode - 150] = buffer.getString();
				//if (!aClass112_3028.aBoolean1431)
				//	actions[opcode - 150] = null;

			}
			else if (opcode == 160)
			{
				int count = buffer.getUnsigned();
				anIntArray2981 = new int[count];
				for (int i = 0; i != count; ++i)
					anIntArray2981[i] = buffer.getUnsignedShort();

			}
			else if (opcode == 162)
			{
				aByte3027 = (byte) 3;
				anInt3023 = buffer.getInt();
			}
			else if (opcode == 163)
			{
				aByte2974 = buffer.get();
				aByte3045 = buffer.get();
				aByte3052 = buffer.get();
				aByte2960 = buffer.get();
			}
			else if (opcode == 164)
				anInt2964 = buffer.getSignedShort();
			else if (opcode == 165)
				anInt2963 = buffer.getSignedShort();
			else if (opcode == 166)
				anInt3018 = buffer.getSignedShort();
			else if (opcode == 167)
				anInt2983 = buffer.getUnsignedShort();
			else if (opcode == 168)
				aBoolean2961 = true;
			else if (opcode == 169)
				aBoolean2993 = true;
			else if (opcode == 170)
				anInt3032 = buffer.getUnsignedSmart();
			else if (opcode == 171)
				anInt2962 = buffer.getUnsignedSmart();

			else if (opcode == 173)
			{
				anInt3050 = buffer.getUnsignedShort();
				anInt3020 = buffer.getUnsignedShort();
			}
			else if (opcode == 177)
				aBoolean2992 = true;
			else if (opcode == 178)
				anInt2975 = buffer.getUnsigned();

			else if (opcode == 249)
			{
				int count = buffer.getUnsigned();
				if (aHashTable3014 == null)
					aHashTable3014 = new Hashtable<Integer, Object>();

				for (int i = 0; i != count; ++i)
				{
					boolean string = buffer.getUnsigned() == 1;
					int key = buffer.getMedium();
					Object value = string ? buffer.getString():buffer.getInt();
					aHashTable3014.put(new Integer(key), value);
				}

			}
			else
			{
				System.out.println("[ObjectDef] Unknown opcode: " + opcode);
				break;
			}
		}
		if (anInt3057 == -1)
		{
			anInt3057 = 0;
			if (modelTypes != null && modelTypes.length == 1 && modelTypes[0] == 10)
				anInt3057 = 1;

			if (anInt3057 == 0 && actions != null)
				for (int i = 0; i != 5; ++i)
					if (actions[i] != null)
					{
						anInt3057 = 1;
						break;
					}


		}
		if (solid == -1)
			solid = anInt3010 == 0 ? 0:1;

		if (anIntArray3019 != null || aBoolean2998 || childrenIDs != null)
			aBoolean2992 = true;

		hasActions = anInt3057 == 1;
	}

	private void skip(Buffer buffer)
	{
		int count = buffer.getUnsigned();
		for (int i = 0; i != count; ++i)
		{
			++buffer.currentOffset;
			int childCount = buffer.getUnsigned();
			buffer.currentOffset += childCount * 2;
		}

	}

	public boolean walkable;
	public byte[] description;
	public boolean hasActions;

	public int anInt746;
	public int mapSceneId;
	public int anInt781;
	public boolean adjustToTerrain;
	public boolean aBoolean764;
	public boolean aBoolean757;

	public void setDefaults()
	{
		walkable = true;
		aBoolean757 = true;
		hasActions = false;
		adjustToTerrain = false;
		aBoolean764 = false;
		anInt746 = -1;
		mapSceneId = -1;
		anInt781 = -1;

		anInt3030 = 0;
		aBoolean2961 = false;
		aByte2974 = 0;
		aByte3045 = 0;
		aByte3052 = 0;
		modelTypes = null;
		aByteArray2996 = null;
		originalModelTextures = null;
		originalModelColors = null;
		modifiedModelTextures = null;
		modifiedModelColors = null;
		anIntArray2981 = null;
		childrenIDs = null;
		anIntArray3036 = null;
		modelIds = null;
		actions = null;
		aHashTable3014 = null;

		aBoolean2972 = true;
		modelSizeY = 128;
		anInt2963 = 0;
		aByte2960 = (byte) 0;
		anInt2983 = 0;
		anInt2971 = 0;
		sizeY = 1;
		anInt2987 = -1;
		anInt2975 = 0;
		anInt2964 = 0;
		modelSizeX = 128;
		name = "null";
		translateZ = 0;
		aBoolean3002 = false;
		solid = -1;
		anInt2994 = -1;
		anInt2977 = -1;
		anInt3012 = 0;
		anInt3015 = -1;
		anInt774 = -1;
		aBoolean766 = false;
		aBoolean3007 = false;
		modelSizeZ = 128;
		anInt3018 = 0;
		anInt3024 = 0xff;
		anInt2958 = 0;
		anIntArray3019 = null;
		anInt3023 = -1;
		anInt2989 = 0;
		anInt3008 = -1;
		anInt3032 = 960;
		aBoolean3000 = true;
		anInt775 = 64;
		anIntArray2995 = null;
		aByte3027 = (byte) 0;
		anInt3020 = 256;
		anInt3038 = -1;
		translateX = 0;
		lightAmbient = 0;
		anInt3010 = 2;
		aBoolean2993 = false;
		aBoolean2998 = false;
		translateY = 0;
		face = 0;
		anInt3050 = 256;
		lightDifuse = 0;
		aBoolean736 = false;
		aBoolean2990 = false;
		aBoolean3034 = true;
		aBoolean2992 = false;
		delayShading = false;
		sizeX = 1;
		anInt2962 = 0;
		anInt3006 = -1;
		anInt749 = -1;
		anInt3013 = -1;
		aBoolean779 = true;
		aBoolean3056 = false;
		rotated = false;
		anInt3057 = -1;
	}

	public int id;

	//private static final Model[] aModelArray741s = new Model[4];
	public static boolean lowMem;
	private static Buffer buffer;
	private static int[] streamIndices;
	public static Client clientInstance;
	private static int cacheIndex;
	public static Cache animatedModelNodes = new Cache(30);
	public static Cache mruNodes1 = new Cache(500);
	private static ObjectDefinition[] cache;
	public static int totalObjects;

	public int anInt2958;
	public int anInt2962;
	public int anInt2963;
	public int anInt2964;
	public int translateX;
	public int modelSizeX;
	public int solid;
	public int anInt2971;
	public int modelSizeY;
	public int anInt2975;
	public int anInt2977;
	public int anInt2983;
	public int translateZ;
	public int sizeY;
	public int anInt2987;
	public int anInt2989;
	public int anInt2994;
	public int anInt3006;
	public int anInt3008;
	public int modelSizeZ;
	public int anInt3010;
	public int anInt775;
	public int anInt3012;
	public int anInt3013;
	public int anInt3015;
	public int anInt774;
	public int anInt3018;
	public int anInt3020;
	public int anInt3023;
	public int anInt3024;
	public int lightDifuse;
	public int anInt3030;
	public int anInt3032;
	public int anInt749;
	public int translateY;
	public int anInt3038;
	public int face;
	public int lightAmbient;
	public int anInt3050;
	public int sizeX;
	public int anInt3057;
	public boolean aBoolean2961;
	public boolean aBoolean2972;
	public boolean aBoolean2990;
	public boolean aBoolean2992;
	public boolean aBoolean2993;
	public boolean aBoolean2998;
	public boolean aBoolean3000;
	public boolean aBoolean3002;
	public boolean aBoolean3007;
	public boolean aBoolean766;
	public boolean aBoolean779;
	public boolean aBoolean3034;
	public boolean rotated;
	public boolean aBoolean736;
	public boolean delayShading;
	public boolean aBoolean3056;
	public byte aByte2960;
	public byte aByte2974;
	public byte aByte3027;
	public byte aByte3045;
	public byte aByte3052;
	public int[] modelTypes;
	public byte[] aByteArray2996;
	public short[] originalModelTextures;
	public short[] originalModelColors;
	public short[] modifiedModelTextures;
	public short[] modifiedModelColors;
	public int[] anIntArray2981;
	public int[] childrenIDs;
	public int[] anIntArray2995;
	public int[] anIntArray3019;
	public int[] anIntArray3036;
	public int[][] modelIds;
	public String[] actions;
	public String name;
	public Hashtable<Integer, Object> aHashTable3014;

	public static ObjectDefinition forId(int id) {
		for (int j = 0; j < 50; j++)
			if (cache[j].id == id)
				return cache[j];
		cacheIndex = (cacheIndex + 1) % 50;
		ObjectDefinition object = cache[cacheIndex];
		object.id = id;
		object.setDefaults();
		if (id >= 0 && id < totalObjects && streamIndices[id] != -1)
		{
			buffer.currentOffset = streamIndices[id];
			object.readValues(buffer);
		}
		/*switch (id) {
		case 28209:
			object.name = "Low-level orb";
			object.modelIds = new int[1][1];
			object.modelIds[0][0] = 30262;
			break;
		case 28210:
			object.name = "Mid-level orb";
			object.modelIds = new int[1][1];
			object.modelIds[0][0] = 30262;
			break;
		case 28211:
			object.name = "High-level orb";
			object.modelIds = new int[1][1];
			object.modelIds[0][0] = 30262;
			break;
		case 28119:
			object.name = "Low-level crater";
			object.modelIds = new int[1][1];
			object.modelIds[0][0] = 30147;
			break;
		case 28120:
			object.name = "Med-level crater";
			object.modelIds = new int[1][1];
			object.modelIds[0][0] = 30149;
			break;
		case 28121:
			object.name = "High-level crater";
			object.modelIds = new int[1][1];
			object.modelIds[0][0] = 30144;
			break;
		}
		if (id >= 30195 && id < 30205) {
			object.delayShading = false;
		}
		if(id >= 5000 && id <= 5500) {
			object.delayShading = false;
		}
		if(id >= 12314 && id <= 12541) {
			object.delayShading = false;
		}
		if(id == 21273 || id == 1278 || id == 1281 || id == 1308 || id == 1406 || id == 1276 || id == 1282) {
			object.adjustToTerrain = false;
		}
		if(id == 26420 || id == 26421 || id == 26422) {
			object.adjustToTerrain = true;
		}
		switch (id) {
		//case 15639:
		//case 15640:
		case 23268:
		case 26420:
		case 23271:
		case 23261:
		case 23262:
		case 23263:
		case 23264:
		case 23265:
		case 23266:
		case 23267:
		case 26341:
		case 26421:
		case 26422:
		case 30240:
		case 32683:
		case 33758:
		case 33759:
		case 33760:
		case 48407:
		case 48408: 
		case 11448:
		case 16039:
		case 16040:
		case 16041:
		case 41198:
		case 41202:
		case 41203:
		case 41519:
			object.delayShading = false;
			break;
		case 48284:
		case 48312:
			object.translateY = 60;
			break;
		case 61880://GE bridge
			object.translateY = 0;
			break;
		case 48340:
		case 48281:
		case 48341:
		case 48339:
		case 48286:
			object.translateY = 120;
			break;
		case 48342:
			object.translateY = 140;
			break;
		case 48343:
			object.translateY = 40;
			break;
		case 48282:
			object.translateY = 180;
			break;
		}*/
		if (Constants.debugObjects) {
			object.hasActions = true;
		}
		return object;
	}

	public static void clearCache()
	{
		if (cache != null)
			for (int i = 0; i != 50; ++i)
			{
				cache[i].id = -1;
				cache[i].setDefaults();
			}

	}

	public void requestModel(ResourceProvider updateManager)
	{
		if (modelIds == null)
			return;

		for (int i = 0; i != modelIds.length; ++i)
			for (int i1 = 0; i1 != modelIds[i].length; ++i1)
				Model.method463(modelIds[i][i1] & 0xffff);//updateManager.request(1, anIntArrayArray3031[i][i1] & 0xffff, true, true);//should this be important?


	}

	public static void nullLoader()
	{
		mruNodes1 = null;
		animatedModelNodes = null;
		modelBuffer1 = null;
		modelBuffer2 = null;
		streamIndices = null;
		cache = null;
		buffer = null;
	}

	public static void nullLoaderSafe()
	{
		mruNodes1.unlinkAll();
		animatedModelNodes.unlinkAll();
		for (int i = 0; i != 256; ++i)
		{
			modelBuffer1[i] = null;
			modelBuffer2[i] = null;
		}

		streamIndices = null;
		cache = null;
		buffer = null;
	}

	public static void unpackConfig(byte[] dat, byte[] idx)
	{
		buffer = new Buffer(dat);
		totalObjects = idx.length / 2;
		streamIndices = new int[totalObjects];
		int index = 0;
		Buffer indexBuffer = new Buffer(idx);
		for (int i = 0; i != totalObjects; ++i)
		{
			int size = indexBuffer.getUnsignedShort();
			streamIndices[i] = size != 0 ? index:-1;
			index += size;
		}
		cache = new ObjectDefinition[totalObjects];
		for (int k = 0; k < 50; k++)
			cache[k] = new ObjectDefinition();
		/*try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("objects.txt"));
			//writer.write("Total: " + ObjectDefinition.cache.length);
			//writer.newLine();
			//writer.newLine();
			for (int i = 0; i < ObjectDefinition.cache.length; i++) {
				System.out.println("Writing object definition: " + i);
				ObjectDefinition def = forId(i);
				writer.write("Id=" + i + "; walkable=" + def.walkable + "; solid=" + def.solid() + ";");
				writer.write("Name: " + def.name);
				writer.newLine();
				String desc = def.description != null ? new String(def.description) : "Null";
				writer.write("Description: " + desc);
				writer.newLine();
				writer.write("Rotation: " + def.face);
				writer.newLine();
				writer.write("Solid: " + def.solid);
				writer.newLine();
				writer.write("SizeX: " + def.sizeX);
				writer.newLine();
				writer.write("SizeY: " + def.sizeY);
				writer.newLine();
				if (def.actions != null) {
					for (int j = 0; j < def.actions.length; j++) {
						if (def.actions[j] == null || def.actions[j].length() <= 0)
							continue;
						writer.write("\tActions[" + j + "]: " + def.actions[j]);
						writer.newLine();
					}
				}
				//writer.write("Finish");
				//writer.newLine();
				writer.newLine();
			}
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}*/
	}

	public boolean method577(int type)
	{
		if (modelTypes == null)
		{
			if (modelIds == null)
				return true;

			if (type != 10)
				return true;

			boolean flag = true;
			int count = modelIds.length;
			for (int i = 0; i != count; ++i)
			{
				int childCount = modelIds[i].length;
				for (int i1 = 0; i1 != childCount; ++i1)
					flag &= Model.method463(modelIds[i][i1] & 0xffff);

			}

			return flag;
		}
		boolean flag = true;
		int count = modelTypes.length;
		for (int i = 0; i != count; ++i)
			if (modelTypes[i] == type)
			{
				int childCount = modelIds[i].length;
				for (int i1 = 0; i1 != childCount; ++i1)
					flag &= Model.method463(modelIds[i][i1] & 0xffff);

				break;
			}

		return flag;
	}

	public Model getModelFromPosition(int type, int face, int k, int l, int i1, int j1, int k1, Animation animation) {
		Model model = getAnimatedModel(type, k1, face, animation);
		if (model == null)
			return null;
		if (adjustToTerrain || delayShading)
			model = new Model(adjustToTerrain, delayShading, model);
		if (adjustToTerrain) {
			int l1 = (k + l + i1 + j1) >> 2;
			for(int i2 = 0; i2 < model.numVertices; i2++) {
				int j2 = model.verticesX[i2] >> 2;
				int k2 = model.verticesZ[i2] >> 2;
				int l2 = k + (((l - k) * (j2 + 64)) >> 7);
				int i3 = j1 + (((i1 - j1) * (j2 + 64)) >> 7);
				int j3 = l2 + (((i3 - l2) * (k2 + 64)) >> 7);
				model.verticesY[i2] += (j3 - l1) << 2;
			}
			model.normalize();
		}
		return model;
	}
	
	/*public final Model getModelAt(int type, int face, int k, int l, int i1, int j1, int k1, int animationId) {)
		Model model = getAnimatedModel(type, animationId, face);
		if (model == null)
			return null;
		if (adjustToTerrain || delayShading)
			model = new Model(adjustToTerrain, delayShading, model);
		if (adjustToTerrain) {
			int l1 = (k + l + i1 + j1) >> 2;
			for(int i2 = 0; i2 < model.numVertices; i2++) {
				int j2 = model.verticesX[i2] >> 2;
				int k2 = model.verticesZ[i2] >> 2;
				int l2 = k + (((l - k) * (j2 + 64)) >> 7);
				int i3 = j1 + (((i1 - j1) * (j2 + 64)) >> 7);
				int j3 = l2 + (((i3 - l2) * (k2 + 64)) >> 7);
				model.verticesY[i2] += (j3 - l1) << 2;
			}
			model.normalize();
		}
		return model;
	}*/
	
	/*public final Model getAnimatedModel(int type, int animationId, int face) {
		
	}*/

	public boolean method579()
	{
		if (modelIds == null)
			return true;

		boolean flag = true;
		int count = modelIds.length;
		for (int i = 0; i != count; ++i)
		{
			int childCount = modelIds[i].length;
			for (int i1 = 0; i1 != childCount; ++i1)
				flag &= Model.method463(modelIds[i][i1] & 0xffff);

		}

		return flag;
	}

	public ObjectDefinition method580()
	{
		int i = -1;
		if (anInt774 != -1)
		{
			VarBit variableBit = VarBit.cache[anInt774];
			int j = variableBit.anInt648;
			int k = variableBit.anInt649;
			int l = variableBit.anInt650;
			int i1 = Client.anIntArray1232[l - k];
			i = clientInstance.variousSettings[j] >> k & i1;
		}
		else if (anInt749 != -1)
			i = clientInstance.variousSettings[anInt749];
		if (i < 0 || i >= childrenIDs.length || childrenIDs[i] == -1)
			return null;
		else
			return forId(childrenIDs[i]);
	}

	private static Model[] modelBuffer1 = new Model[256];
	private static Model[] modelBuffer2 = new Model[256];

	private Model getAnimatedModel(int type, int animationId, int face, Animation animDef)
	{
		Model subModel = null;
		long hash;
		if (modelTypes == null)
		{
			if (type != 10) {
				return null;
			}
			hash = (id << 6) + face + ((long) (animationId + 1) << 32);
			Model cachedModel = (Model) animatedModelNodes.insertFromCache(hash);
			if (cachedModel != null)
				return cachedModel;
			if (modelIds == null)
				return null;

			boolean mirror = rotated ^ (face > 3);
			int modelCount = modelIds.length;
			for (int i = 0; i != modelCount; ++i)
			{
				int subModelId = modelIds[i].length;
				for (int i1 = 0; i1 != subModelId; ++i1)
				{
					int id = modelIds[i][i1];
					if (mirror)
						id += 0x10000;
					subModel = (Model) mruNodes1.insertFromCache(id);
					if (subModel == null)
					{
						subModel = Model.getModel(subModelId & 0xffff);
						if (subModel == null)
							return null;

						if (mirror)
							subModel.method477();

						mruNodes1.removeFromCache(subModel, id);
					}
					if (subModelId > 1)
						modelBuffer2[i1] = subModel;

				}

				if (subModelId > 1)
					subModel = new Model(subModelId, modelBuffer2);

				if (modelCount > 1)
					modelBuffer1[i] = subModel;

			}

			if (modelCount > 1)
				subModel = new Model(modelCount, modelBuffer1);

		}
		else
		{
			int i1 = -1;
			int count = modelTypes.length;
			for (int j1 = 0; j1 < count; j1++)
			{
				if (modelTypes[j1] != type)
					continue;

				i1 = j1;
				break;
			}

			if (i1 == -1)
				return null;

			hash = (long) ((id << 6) + (i1 << 3) + face) + ((long) (animationId + 1) << 32);
			//model = (Model) mruNodes2.insertFromCache(l1);
			subModel = null;
			if (modelIds == null)
				return null;

			boolean flag = rotated ^ (face > 3);
			int childCount = modelIds[i1].length;
			for (int i = 0; i != childCount; ++i)
			{
				int id = modelIds[i1][i];
				if (flag)
					id += 0x10000;

				//model = (Model) mruNodes1.insertFromCache(id);
				subModel = null;
				if (subModel == null)
				{
					subModel = Model.getModel(id & 0xffff);
					if (subModel == null)
						return null;

					if (flag)
						subModel.method477();

					mruNodes1.removeFromCache(subModel, id);
				}
				if (childCount > 1)
					modelBuffer1[i] = subModel;

			}

			if (childCount > 1)
				subModel = new Model(childCount, modelBuffer1);

		}
		boolean scale = modelSizeX != 128 || modelSizeY != 128 || modelSizeZ != 128;
		boolean needsTranslation = translateX != 0 || translateY != 0 || translateZ != 0;
		int flags = 0;
		if (animationId != -1)
			flags |= Model.getFlags(animationId, animDef);

		if (needsTranslation || scale)
			flags |= 0x4;

		if (modifiedModelColors != null)
			flags |= 0x2;

		if (modifiedModelTextures != null)
			flags |= 0x8;

		if (face > 0)
			flags |= 0x4;

		Model animatedModel = new Model(flags, subModel);//modifiedModelColors == null && modifiedModelTextures == null, Skeleton.method532(k), l == 0 && k == -1 && !flag && !flag2
		if (animationId != -1)
		{
			animatedModel.createBones();
			animatedModel.applyTransformation(animationId, animDef);
			animatedModel.triangleSkin = null;
			animatedModel.vectorSkin = null;
		}
		while (face-- > 0)
			animatedModel.rotate90Degrees();

		if (modifiedModelColors != null)
			for (int k2 = 0; k2 < modifiedModelColors.length; k2++)
				animatedModel.setColor(modifiedModelColors[k2], originalModelColors[k2]);


		if (modifiedModelTextures != null)
			for (int k2 = 0; k2 < modifiedModelTextures.length; k2++)
				animatedModel.setTexture(modifiedModelTextures[k2], originalModelTextures[k2]);


		if (scale)
			animatedModel.scale(modelSizeX, modelSizeZ, modelSizeY);

		if (needsTranslation)
			animatedModel.translate(translateX, translateY, translateZ);

		animatedModel.setLighting(64 + lightAmbient, 768 + lightDifuse, -50, -10, -50, !delayShading, false);
		if (solid == 1)
			animatedModel.anInt1654 = animatedModel.modelHeight;

		animatedModelNodes.removeFromCache(animatedModel, hash);
		return animatedModel;
	} 

	private ObjectDefinition()
	{
		id = -1;
	}

	public boolean hasActions()
	{
		return hasActions;
	}

	public boolean solid()
	{
		return aBoolean757;
	}

	public int xLength()
	{
		return sizeX;
	}

	public int yLength()
	{
		return sizeY;
	}
}
