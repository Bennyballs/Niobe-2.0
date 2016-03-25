package src;

public final class SkinList
{
	private SkinList(Buffer buffer)
	{
		int count = buffer.getUnsigned();
		anIntArray342 = new int[count];
		aBooleanArray7502 = new boolean[count];
		anIntArray7510 = new int[count];
		anIntArrayArray343 = new int[count][];
		for (int i = 0; i != count; ++i)
		{
			anIntArray342[i] = buffer.getUnsigned();
			if (anIntArray342[i] == 6)
				anIntArray342[i] = 2;

		}

		for (int i = 0; i != count; ++i)
			aBooleanArray7502[i] = buffer.getUnsigned() == 1;

		for (int i = 0; i != count; ++i)
			anIntArray7510[i] = buffer.getUnsignedShort();

		for (int i = 0; i != count; ++i)
			anIntArrayArray343[i] = new int[buffer.getUnsigned()];

		for (int i = 0; i != count; ++i)
		{
			int childCount = anIntArrayArray343[i].length;
			for (int i1 = 0; i1 != childCount; ++i1)
				anIntArrayArray343[i][i1] = buffer.getUnsigned();

		}

	}

	private SkinList()
	{
		anIntArray342 = new int[0];
		aBooleanArray7502 = new boolean[0];
		anIntArray7510 = new int[0];
		anIntArrayArray343 = new int[0][];
	}

	public static boolean loaded(int id)
	{
		return true;
	}

	public static SkinList get(int id)
	{
		return id >= 0 && id < skins.length ? skins[id]:null;
	}

	public static void nullLoader()
	{
		skins = null;
	}

	public static void unpackConfig(byte[] data)
	{
		Buffer buffer = new Buffer(data);
		int count = buffer.getUnsignedShort() & 0xffff;
		skins = new SkinList[count];
		int pos = buffer.currentOffset;
		for (int i = 0; i != count; ++i)
		{
			buffer.currentOffset = pos;
			int size = buffer.getUnsignedShort() & 0xffff;
			pos += 2 + size;
			if (size != 0)
				skins[i] = new SkinList(buffer);

		}

	}

	public final int[] anIntArray342;
	public final boolean[] aBooleanArray7502;
	public final int[] anIntArray7510;
	public final int[][] anIntArrayArray343;
	private static SkinList[] skins;
	public static final SkinList NULL_SKIN = new SkinList();
}
