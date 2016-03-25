package src;

import java.io.*;

import src.sign.signlink;

public final class Animation
{
	private Animation()
	{
		anInt356 = -1;
		anInt365 = 2;
		anInt363 = -1;
		anInt362 = 99;
		anInt359 = 5;
		anInt361 = -1;
		anInt360 = -1;
		anInt364 = -1;
	}

	private Animation(Buffer buffer)
	{
		this();
		while (true)
		{
			int opcode = buffer.getUnsigned();
			if (opcode == 0)
				break;

			if (opcode == 1)
			{
				int count = anInt352 = buffer.getUnsignedShort();
				anIntArray355 = new int[count];
				anIntArray354 = new int[count];
				for (int i = 0; i != count; ++i)
				{
					anIntArray355[i] = buffer.getUnsignedShort();
					anIntArray354[i] = -1;
				}

				anIntArray353 = new int[count];
				for (int i = 0; i != count; ++i)
					anIntArray353[i] = buffer.getUnsignedShort();

				for (int i = 0; i != count; ++i)
					anIntArray353[i] |= buffer.getUnsignedShort() << 16;

			}
			else if (opcode == 2)
				anInt356 = buffer.getUnsignedShort();

			else if (opcode == 3)
			{
				/*aBooleanArray703 = new boolean[256];
				int count = stream.readUnsignedByte();
				for (int i = 0; i != count; ++i)
					aBooleanArray703[stream.readUnsignedByte()] = true;*/

				int count = buffer.getUnsigned();
				anIntArray357 = new int[count + 1];
				for (int i = 0; i != count; ++i)
					anIntArray357[i] = buffer.getUnsigned();

				anIntArray357[count] = 9999999;
			}
			else if (opcode == 5)
				anInt359 = buffer.getUnsigned();
			else if (opcode == 6)
				anInt360 = buffer.getUnsignedShort() + 512;
			else if (opcode == 7)
				anInt361 = buffer.getUnsignedShort() + 512;
			else if (opcode == 8)
				anInt362 = buffer.getUnsigned();
			else if (opcode == 9)
				anInt363 = buffer.getUnsigned();
			else if (opcode == 10)
				anInt364 = buffer.getUnsigned();
			else if (opcode == 11)
				anInt365 = buffer.getUnsigned();

			else if (opcode == 12)
			{
				int count = buffer.getUnsigned();
				anIntArray712 = new int[count];
				for (int i = 0; i != count; ++i)
					anIntArray712[i] = buffer.getUnsignedShort();

				for (int i = 0; i != count; ++i)
					anIntArray712[i] |= buffer.getUnsignedShort() << 16;

			}
			else if (opcode == 13)
			{
				int count = buffer.getUnsignedShort();
				anIntArrayArray700 = new int[count][];
				for (int i = 0; i != count; ++i)
				{
					int childCount = buffer.getUnsigned();
					if (childCount != 0)
					{
						anIntArrayArray700[i] = new int[childCount];
						anIntArrayArray700[i][0] = buffer.getMedium();
						for (int i1 = 1; i1 != childCount; ++i1)
							anIntArrayArray700[i][i1] = buffer.getUnsignedShort();

					}
				}

			}
			else if (opcode == 14)
				aBoolean358 = aBoolean691 = true;
			else if (opcode == 15)
				aBoolean714 = true;

			else if (opcode == 16)
			{
				//do nothing
			}
			else if (opcode == 18)
				aBoolean699 = true;

			else if (opcode == 19)
			{
				if (anIntArray701 == null)
				{
					int count = anIntArrayArray700.length;
					anIntArray701 = new int[count];
					for (int i = 0; i != count; ++i)
						anIntArray701[i] = 0xff;

				}
				anIntArray701[buffer.getUnsigned()] = buffer.getUnsigned();
			}
			else if (opcode == 20)
			{
				if (anIntArray690 == null || anIntArray692 == null)
				{
					int count = anIntArrayArray700.length;
					anIntArray690 = new int[count];
					anIntArray692 = new int[count];
					for (int i = 0; i != count; ++i)
						anIntArray690[i] = anIntArray692[i] = 0x100;

				}
				int i = buffer.getUnsigned();
				anIntArray690[i] = buffer.getUnsignedShort();
				anIntArray692[i] = buffer.getUnsignedShort();
			}
			else
			{
				System.out.println("[Animation] Unknown opcode: " + opcode);
				break;
			}
		}
		if (anInt352 == 0)
		{
			anInt352 = 1;
			anIntArray353 = new int[1];
			anIntArray353[0] = -1;
			anIntArray354 = new int[1];
			anIntArray354[0] = -1;
			anIntArray355 = new int[1];
			anIntArray355[0] = -1;
		}
		if (anInt363 == -1)
			if (anIntArray357 != null)
				anInt363 = 2;
			else
				anInt363 = 0;


		if (anInt364 == -1)
		{
			if (anIntArray357 != null)
			{
				anInt364 = 2;
				return;
			}
			anInt364 = 0;
		}
	}

	public int method258(int i)
	{
		int j = anIntArray355[i];
		if (j == 0)
		{
			FrameReader skeleton = FrameReader.get(anIntArray353[i]);
			if (skeleton != null)
				j = anIntArray355[i] = skeleton.anInt636;

		}
		if (j == 0)
			j = 1;

		return j;
	}

	private static byte[] loadFile(File file)
	{
		if (!file.exists() || !file.isFile() || !file.canRead())
			return null;

		long length = file.length();
		if (length < 0L || length > 0x7fffffffL)
			return null;

		int size = (int) length;
		byte[] data = null;
		try
		{
			FileInputStream in = new FileInputStream(file);
			try
			{
				int offset = 0;
				data = new byte[size];
				for (int count; size != 0; size -= count)
				{
					count = in.read(data, offset, size);
					if (count < 0)
						break;

					if (count > size)
						count = size;

					offset += count;
				}

			}
			finally
			{
				in.close();
			}
		}
		catch (Exception ex) { }
		return size == 0 ? data:null;
	}

	public static void unpackConfig(byte[] dat)
	{
		dat = loadFile(new File(signlink.getCacheLocation() + "seq.dat"));
		Buffer buffer = new Buffer(dat);
		int count = buffer.getUnsignedShort();
		/*System.out.println("count: " + count + "/" + dat.length);
		int qsum = 0;
		for (int i = 0; i != dat.length; ++i)
			qsum = qsum * 31 + dat[i];
		System.out.println(Integer.toHexString(qsum));*/
		Animation[] anims_ = anims = new Animation[count];
		int pos = buffer.currentOffset;
		for (int i = 0; i != count; ++i)
		{
			buffer.currentOffset = pos;
			int size = buffer.getUnsignedShort();
			pos += 2 + size;
			anims_[i] = size != 0 ? new Animation(buffer):new Animation();
		}

	}

	public static Animation get(int id)
	{
		return id >= 0 && id < anims.length ? anims[id]:null;
	}

	public int getFrame(int id)
	{
		return id >= 0 && anIntArray353 != null && id < anIntArray353.length ? anIntArray353[id]:-1;
	}

	public int getFrame2(int id)
	{
		return id >= 0 && anIntArray354 != null && id < anIntArray354.length ? anIntArray354[id]:-1;
	}

	public static void nullLoader()
	{
		anims = null;
	}

	public static Animation[] anims;

	public int[] anIntArray690;
	public int[] anIntArray692;
	public int[] anIntArray701;
	public int[] anIntArray355;
	public int[] anIntArray353;
	public int[] anIntArray712;
	public int[][] anIntArrayArray700;
	public boolean[] aBooleanArray703;
	public boolean aBoolean714;
	public boolean aBoolean691;//358?
	public boolean aBoolean699;
	public int anInt360;
	public int anInt364;
	public int anInt361;
	public int anInt362;
	public int anInt359;
	public int anInt356;
	public int anInt363;
	public int anInt365;

	public int anInt352;
	public int[] anIntArray354;
	public int[] anIntArray357;

	public boolean aBoolean358;
}
