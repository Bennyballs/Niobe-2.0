package src;

import java.util.*;

public final class NPCDefinition
{
	public int anInt2803;
	public int anInt2804;
	public int anInt75; //anInt2806
	public int anInt2809;
	public int anInt2810;
	public int anInt2812;
	public Hashtable<Integer, Object> aHashTable2813;
	public int anInt2814;
	public int anInt2815;
	public byte aByte2816;
	public boolean aBoolean2817;
	public String name; //aString2821
	public boolean aBoolean93; //aBoolean2824
	public boolean aBoolean2825;
	public int anInt2826;
	public int anInt2828;
	public int anInt86; //anInt2830
	public int anInt2831;
	public int anInt2833;
	public byte aByte2836;
	public int animationIndex;
	public int combatLevel; //anInt2838
	public byte aByte2839;
	public boolean aBoolean2843;
	public int anInt2844;
	public int anInt85; //anInt2848
	public int anInt2849;
	public int anInt2852;
	public byte aByte2853;
	public boolean aBoolean84;
	public byte aByte2855;
	public int anInt2856;
	public byte aByte2857;
	public int anInt91; //anInt2858
	public int anInt2859;
	public int anInt2860;
	public int anInt2862;
	public short aShort2863;
	public int anInt2864;
	public int id; //anInt2867
	public byte aByte2868;
	public short aShort2871;
	public int anInt92; //anInt2872
	public byte aByte2873;
	public boolean aBoolean2875;
	public int turnSpeed; //anInt2876
	public byte aByte2877;
	public int anInt2878;
	public boolean aBoolean87; //aBoolean2879
	public int anInt57; //anInt2881
	public int anInt59; //anInt2882
	public boolean aBoolean2883;
	public int anInt2886;
	public byte[] aByteArray2820;
	public short[] anIntArray76; //aShortArray2823
	public int[] childrenIDs; //anIntArray2827
	public short[] anIntArray70; //aShortArray2829
	public int[] anIntArray2832;
	public final String[] actions = new String[10]; //aStringArray2834
	public short[] aShortArray2841;
	public int[][] anIntArrayArray2842;
	public int[] anIntArray73; //anIntArray2847
	public int[] anIntArray94; //anIntArray2865
	public short[] aShortArray2874;

	public static void clearCache()
	{
		if (definitions != null)
			for (int i = 0; i < 50; i++)
			{
				definitions[i].id = -1;
				definitions[i].setDefaults();
			}

	}

	public void setDefaults()
	{
		id = 0;
		description = null;
		turnRightAnimation = -1;
		reverseAnimation = -1;
		walkAnimation = -1;
		size = 1;
		standAnimation = -1;
		turnLeftAnimation = -1;
		headIconPK = -1;

		aHashTable2813 = null;
		aByte2836 = 0;
		aByte2853 = 0;
		aByte2857 = 0;
		aBoolean2883 = false;
		anIntArray73 = null;

		anInt2814 = -1;
		aByteArray2820 = null;
		anIntArray76 = null;
		childrenIDs = null;
		anIntArray70 = null;
		anIntArray2832 = null;
		for (int i = 0; i != 10; ++i)
			actions[i] = null;

		aShortArray2841 = null;
		anIntArrayArray2842 = null;
		anIntArray94 = null;
		aShortArray2874 = null;
		anInt2809 = -1;
		anInt2803 = -1;
		aBoolean2817 = true;
		anInt2804 = -1;
		aBoolean2843 = false;
		anInt86 = 128;
		anInt2833 = -1;
		aBoolean2825 = false;
		combatLevel = -1;
		anInt2826 = -1;
		aBoolean93 = false;
		aByte2855 = (byte) -1;
		aByte2816 = (byte) 0;
		aByte2839 = (byte) 0;
		anInt2812 = -1;
		anInt2844 = 256;
		aBoolean84 = true;
		name = "null";
		anInt91 = 128;
		anInt2810 = -1;
		anInt75 = -1;
		anInt2852 = 256;
		anInt2860 = -1;
		anInt2859 = -1;
		anInt2831 = 0;
		animationIndex = -1;
		anInt2862 = 0;
		aShort2863 = (short) 0;
		anInt2864 = 0;
		aByte2868 = (byte) -16;
		anInt85 = 0;
		anInt2849 = -1;
		anInt2856 = -1;
		aBoolean2875 = true;
		aShort2871 = (short) 0;
		anInt92 = 0;
		anInt2828 = 255;
		anInt57 = -1;
		aByte2877 = (byte) -96;
		aBoolean87 = true;
		anInt2878 = -1;
		aByte2873 = (byte) 4;
		anInt59 = -1;
		anInt2815 = -1;
		turnSpeed = 32;
		anInt2886 = -1;

		runAnimation = -1;
	}

	private void readValues(Buffer buffer)
	{
		while (true)
		{
			int opcode = buffer.getUnsigned();
			if (opcode == 0)
				break;

			if (opcode == 1)
			{
				int count = buffer.getUnsigned();
				anIntArray94 = new int[count];
				for (int i = 0; i != count; ++i)
				{
					anIntArray94[i] = buffer.getUnsignedShort();
					if (anIntArray94[i] == 65535)
						anIntArray94[i] = -1;

				}

			}
			else if (opcode == 2)
				name = buffer.getString();
			else if (opcode == 3)
				description = Buffer.toBytes(buffer.getString());
			else if (opcode == 12)
				size = buffer.get();
			else if (opcode >= 30 && opcode < 40)
				actions[opcode - 30] = buffer.getString();

			else if (opcode == 40)
			{
				int count = buffer.getUnsigned();
				anIntArray76 = new short[count];
				anIntArray70 = new short[count];
				for (int i = 0; i != count; ++i)
				{
					anIntArray76[i] = (short) buffer.getUnsignedShort();
					anIntArray70[i] = (short) buffer.getUnsignedShort();
				}

			}
			else if (opcode == 41)
			{
				int count = buffer.getUnsigned();
				aShortArray2841 = new short[count];
				aShortArray2874 = new short[count];
				for (int i = 0; i != count; ++i)
				{
					aShortArray2841[i] = (short) buffer.getUnsignedShort();
					aShortArray2874[i] = (short) buffer.getUnsignedShort();
				}

			}
			else if (opcode == 42)
			{
				int count = buffer.getUnsigned();
				aByteArray2820 = new byte[count];
				for (int i = 0; i != count; ++i)
					aByteArray2820[i] = (byte) count;

			}
			else if (opcode == 60)
			{
				int count = buffer.getUnsigned();
				anIntArray73 = new int[count];
				for (int i = 0; i != count; ++i)
					anIntArray73[i] = buffer.getUnsignedShort();

			}
			else if (opcode == 93)
				aBoolean87 = false;
			else if (opcode == 95)
				combatLevel = buffer.getUnsignedShort();
			else if (opcode == 97)
				anInt91 = buffer.getUnsignedShort();
			else if (opcode == 98)
				anInt86 = buffer.getUnsignedShort();
			else if (opcode == 99)
				aBoolean93 = true;
			else if (opcode == 100)
				anInt85 = buffer.get();
			else if (opcode == 101)
				anInt92 = 5 * buffer.get();
			else if (opcode == 102)
				anInt75 = buffer.getUnsignedShort();
			else if (opcode == 103)
				turnSpeed = buffer.getUnsignedShort();

			else if (opcode == 106 || opcode == 118)
			{
				anInt57 = buffer.getUnsignedShort();
				if (anInt57 == 65535)
					anInt57 = -1;

				anInt59 = buffer.getUnsignedShort();
				if (anInt59 == 65535)
					anInt59 = -1;

				int n1 = -1;
				if (opcode == 118)
				{
					n1 = buffer.getUnsignedShort();
					if (n1 == 65535)
						n1 = -1;

				}
				int n2 = buffer.getUnsigned();
				childrenIDs = new int[n2 + 2];
				for (int i = 0; i <= n2; ++i)
				{
					childrenIDs[i] = buffer.getUnsignedShort();
					if (childrenIDs[i] == 65535)
						childrenIDs[i] = -1;

				}

				childrenIDs[n2 + 1] = n1;
			}
			else if (opcode == 107)
				aBoolean84 = false;
			else if (opcode == 109)
				aBoolean2817 = false;
			else if (opcode == 111)
				aBoolean2875 = false;

			else if (opcode == 113)
			{
				aShort2863 = (short) buffer.getUnsignedShort();
				aShort2871 = (short) buffer.getUnsignedShort();
			}
			else if (opcode == 114)
			{
				aByte2877 = buffer.get();
				aByte2868 = buffer.get();
			}
			else if (opcode == 119)
				aByte2816 = buffer.get();

			else if (opcode == 121)
			{
				anIntArrayArray2842 = new int[anIntArray94.length][];
				int count = buffer.getUnsigned();
				for (int i = 0; i != count; ++i)
				{
					int index = buffer.getUnsigned();
					int[] out = anIntArrayArray2842[index] = new int[3];
					out[0] = buffer.getUnsigned();
					out[1] = buffer.getUnsigned();
					out[2] = buffer.getUnsigned();
				}

			}
			else if (opcode == 122)
				anInt2878 = buffer.getUnsignedShort();
			else if (opcode == 123)
				anInt2804 = buffer.getUnsignedShort();
			else if (opcode == 125)
				aByte2873 = buffer.get();
			else if (opcode == 127)//animationId index.
				animationIndex = buffer.getUnsignedShort();
			else if (opcode == 128)
				buffer.get();

			else if (opcode == 134)
			{
				anInt2812 = buffer.getUnsignedShort();
				if (anInt2812 == 65535)
					anInt2812 = -1;

				anInt2833 = buffer.getUnsignedShort();
				if (anInt2833 == 65535)
					anInt2833 = -1;

				anInt2809 = buffer.getUnsignedShort();
				if (anInt2809 == 65535)
					anInt2809 = -1;

				anInt2810 = buffer.getUnsignedShort();
				if (anInt2810 == 65535)
					anInt2810 = -1;

				anInt2864 = buffer.getUnsigned();
			}
			else if (opcode == 135)
			{
				anInt2815 = buffer.getUnsigned();
				anInt2859 = buffer.getUnsignedShort();
			}
			else if (opcode == 136)
			{
				anInt2856 = buffer.getUnsigned();
				anInt2886 = buffer.getUnsignedShort();
			}
			else if (opcode == 137)
				anInt2860 = buffer.getUnsignedShort();
			else if (opcode == 138)
				anInt2814 = buffer.getUnsignedShort();
			else if (opcode == 139)
				anInt2826 = buffer.getUnsignedShort();
			else if (opcode == 140)
				anInt2828 = buffer.getUnsigned();
			else if (opcode == 141)
				aBoolean2843 = true;
			else if (opcode == 142)
				anInt2849 = buffer.getUnsignedShort();
			else if (opcode == 143)
				aBoolean2825 = true;

			else if (opcode >= 150 && opcode < 155)
			{
				actions[opcode - 150] = buffer.getString();

			}
			else if (opcode == 155)
			{
				aByte2836 = buffer.get();
				aByte2853 = buffer.get();
				aByte2857 = buffer.get();
				aByte2839 = buffer.get();
			}
			else if (opcode == 158)
				aByte2855 = (byte) 1;
			else if (opcode == 159)
				aByte2855 = (byte) 0;

			else if (opcode == 160)
			{
				int count = buffer.getUnsigned();
				anIntArray2832 = new int[count];
				for (int i = 0; i != count; ++i)
					anIntArray2832[i] = buffer.getUnsignedShort();

			}
			else if (opcode == 162)
				aBoolean2883 = true;
			else if (opcode == 163)
				anInt2803 = buffer.getUnsigned();

			else if (opcode == 164)
			{
				anInt2844 = buffer.getUnsignedShort();
				anInt2852 = buffer.getUnsignedShort();
			}
			else if (opcode == 165)
				anInt2831 = buffer.getUnsigned();
			else if (opcode == 168)
				anInt2862 = buffer.getUnsigned();

			else if (opcode == 249)
			{
				int count = buffer.getUnsigned();
				if (aHashTable2813 == null)
					aHashTable2813 = new Hashtable<Integer, Object>();

				for (int i = 0; i != count; ++i)
				{
					boolean string = buffer.getUnsigned() == 1;
					int key = buffer.getMedium();
					Object value = string ? buffer.getString():Integer.valueOf(buffer.getInt());
					aHashTable2813.put(Integer.valueOf(key), value);
				}

			}
			else
			{
				System.out.println("[NPCDef] Unknown opcode: " + opcode);
				break;
			}
		}
		if (animationIndex >= 0 && animationIndex < NPCAnimDef.defs.length) {
			NPCAnimDef def = NPCAnimDef.defs[animationIndex];
			if (def != null) {
				standAnimation = def.standAnimation;
				walkAnimation = def.walkAnimation;
				reverseAnimation = def.reverseAnimation;
				turnLeftAnimation = def.turnLeftAnimation;
				turnRightAnimation = def.turnRightAnimation;
				runAnimation = def.runAnimation;
			}
		} else if (animationIndex >= 0) {
			System.out.println("Invalid animation definition for index: " + animationIndex);
		}

	}

	public int runAnimation;
	public int headIconPK;
	public int turnRightAnimation;
	@SuppressWarnings("unused")
	private static int cacheIndex;
	public int reverseAnimation;
	private static Buffer buffer;
	//private final int anInt64;
	public int walkAnimation;
	public byte size;
	private static int[] streamIndices;
	public int standAnimation;
	private static NPCDefinition[] definitions;
	public static Client clientInstance;
	public int turnLeftAnimation;
	public byte description[];
	public static Cache nodeCache = new Cache(30);
	public static int totalEntities;

	public static void unpackConfig(byte[] dat, byte[] idx)
	{
		buffer = new Buffer(dat);
		totalEntities = idx.length / 2;
		streamIndices = new int[totalEntities];
		int index = 0;
		Buffer indexBuffer = new Buffer(idx);
		for (int i = 0; i < totalEntities; i++) {
			int size = indexBuffer.getUnsignedShort();
			streamIndices[i] = size != 0 ? index:-1;
			index += size;
		}
		definitions = new NPCDefinition[totalEntities];
	}
	
	public static NPCDefinition forId(int id) {
		if (id > definitions.length) {
			return new NPCDefinition();
		}
		if (definitions[id] != null) {
			return definitions[id];
		}
		cacheIndex = (id) % 50;
		NPCDefinition definition = definitions[id] = new NPCDefinition();
		definition.setDefaults();
		definition.id = id;
		if (id >= 0 && id < totalEntities && streamIndices[id] != -1) {
			buffer.currentOffset = streamIndices[id];
			definition.readValues(buffer);
		}
		definitions[id] = definition;
		return definition;
	}

	public Model method160()
	{
		if (childrenIDs != null)
		{
			NPCDefinition def = method161();
			if (def == null)
				return null;
			else
				return def.method160();
		}
		if (anIntArray73 == null)
			return null;
		boolean flag1 = false;
		for (int i = 0; i < anIntArray73.length; i++)
			if (!Model.method463(anIntArray73[i]))
				flag1 = true;

		if (flag1)
			return null;
		Model aclass30_sub2_sub4_sub6s[] = new Model[anIntArray73.length];
		for (int j = 0; j < anIntArray73.length; j++)
			aclass30_sub2_sub4_sub6s[j] = Model.getModel(anIntArray73[j]);

		Model model;
		if (aclass30_sub2_sub4_sub6s.length == 1)
			model = aclass30_sub2_sub4_sub6s[0];
		else
			model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);
		if (anIntArray76 != null)
		{
			for (int k = 0; k < anIntArray76.length; k++)
				model.setColor(anIntArray76[k], anIntArray70[k]);
		}
		if (aShortArray2841 != null)
			for (int i = 0; i != aShortArray2841.length; ++i)
				model.setTexture(aShortArray2841[i], aShortArray2874[i]);


		return model;
	}

	public NPCDefinition method161()
	{
		try
		{
			int j = -1;
			if (anInt57 != -1)
			{
				VarBit variableBit = VarBit.cache[anInt57];
				int k = variableBit.anInt648;
				int l = variableBit.anInt649;
				int i1 = variableBit.anInt650;
				int j1 = Client.anIntArray1232[i1 - l];
				j = clientInstance.variousSettings[k] >> l & j1;
			}
			else if (anInt59 != -1)
				j = clientInstance.variousSettings[anInt59];
			if (j < 0 || j >= childrenIDs.length || childrenIDs[j] == -1)
				return null;
			else
				return forId(childrenIDs[j]);
		}
		catch (Exception e)
		{
			/* Empty */
		}
		return null;
	}

	public static void nullLoader()
	{
		nodeCache = null;
		streamIndices = null;
		definitions = null;
		buffer = null;
	}

	public static void nullLoaderSafe()
	{
		nodeCache.unlinkAll();
		streamIndices = null;
		definitions = null;
		buffer = null;
	}

	public Model method164(int j, int k, Animation def, boolean carry)
	{
		if (childrenIDs != null)
		{
			NPCDefinition npcdef = method161();
			if (npcdef == null)
				return null;
			else
				return npcdef.method164(j, k, def, carry);
		}
		Model model = (Model) nodeCache.insertFromCache(id);
		if (model == null)
		{
			boolean flag = false;
			for (int i1 = 0; i1 < anIntArray94.length; i1++)
				if (!Model.method463(anIntArray94[i1]))
					flag = true;

			if (flag)
				return null;
			Model aclass30_sub2_sub4_sub6s[] = new Model[anIntArray94.length];
			for (int j1 = 0; j1 < anIntArray94.length; j1++)
				aclass30_sub2_sub4_sub6s[j1] = Model.getModel(anIntArray94[j1]);

			if (aclass30_sub2_sub4_sub6s.length == 1)
				model = aclass30_sub2_sub4_sub6s[0];
			else
				model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);
			if (anIntArray76 != null)
				for (int k1 = 0; k1 < anIntArray76.length; k1++)
					model.setColor(anIntArray76[k1], anIntArray70[k1]);


			if (aShortArray2841 != null)
				for (int i = 0; i != aShortArray2841.length; ++i)
					model.setTexture(aShortArray2841[i], aShortArray2874[i]);


			model.createBones();
			model.setLighting(64 + anInt85, 850 + anInt92, -30, -50, -30, true, true);
			nodeCache.removeFromCache(model, id);
		}
		Model model_1 = Model.aModel_1621;
		boolean rotate = anInt91 != 128 || anInt86 != 128;
		int flags = 0;
		if (rotate)
			flags |= 0x4;

		j = -1; //NO SUPPORT FOR DOUBLE ANIMS!!!
		if (k != -1 && j != -1)
			flags |= Model.getFlags(def, j, k, carry);
		else if (k != -1)
			flags |= Model.getFlags(k, def);

		model_1.method464(model, flags);
		if (k != -1 && j != -1)
			model_1.method471(def, j, k, carry);
		else if (k != -1)
			model_1.applyTransformation(k, def);

		model_1.triangleSkin = null;
		model_1.vectorSkin = null;
		model_1.method466();
		if (rotate)
			model_1.scale(anInt91, anInt91, anInt86);

		if (size == 1)
			model_1.aBoolean1659 = true;

		return model_1;
	}

	private NPCDefinition()
	{
		id = -1;
		//anInt64 = 1834;
	}
}
