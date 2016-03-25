package src;

//"Variable Parameter Type"?
public class Varp
{
	public static void unpackConfig(byte[] data)
	{
		Buffer buffer = new Buffer(data);//varp.dat
		anInt702 = 0;
		int cacheSize = buffer.getUnsignedShort();
		if (cache == null)
			cache = new Varp[cacheSize];

		if (anIntArray703 == null)
			anIntArray703 = new int[cacheSize];

		for (int j = 0; j < cacheSize; j++)
		{
			if (cache[j] == null)
				cache[j] = new Varp();

			cache[j].readValues(buffer, j);
		}
		if (buffer.currentOffset != buffer.buffer.length)
			System.out.println("varptype load mismatch");

	}

	public static void nullLoader()
	{
		cache = null;
	}

	private void readValues(Buffer buffer, int i)
	{
		do
		{
			int j = buffer.getUnsigned();
			if (j == 0)
				return;
			//int dummy;
			if (j == 1)
				buffer.getUnsigned();
			else if (j == 2)
				buffer.getUnsigned();
			else if (j == 3)
				anIntArray703[anInt702++] = i;
			//else if (j == 4)
			//dummy = 2;
			else if (j == 5)
				anInt709 = buffer.getUnsignedShort();
			//else if (j == 6)
			//	dummy = 2;
			else if (j == 7)
				buffer.getInt();
			else if (j == 8)
				aBoolean713 = true;
			else if (j == 10)
				buffer.getLing();
			else if (j == 11)
				aBoolean713 = true;
			else if (j == 12)
				buffer.getInt();
			//else if (j == 13)
			//	dummy = 2;
			else
				System.out.println("Error unrecognised config code: " + j);
		}
		while (true);
	}

	private Varp()
	{
		aBoolean713 = false;
	}

	public static Varp cache[];
	private static int anInt702;
	private static int[] anIntArray703;
	public int anInt709;
	public boolean aBoolean713;
}
