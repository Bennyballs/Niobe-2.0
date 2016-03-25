package src;

import java.io.*;

final class Decompressor
{
	private int anInt2944;
	private RandomAccessFile data = null;
	private RandomAccessFile index = null;
	private static final byte[] aByteArray4057 = new byte[520];
	private int anInt2951 = 65000;

	public boolean insertIndex(int length, byte[] data, int id) {
		return put(id, data, length);
	}

	public int size() {
		try {
			return (int) (index.length() / 6L);
		} catch (IOException ex) {
		}
		return 0;
	}

	public boolean put(int n, byte[] b) {
		return put(n, b, b.length);
	}

	final boolean put(int paramInt2, byte[] paramArrayOfByte, int paramInt1) {
		synchronized (data) {
			if ((paramInt1 < 0) || (anInt2951 < paramInt1))
				throw new IllegalArgumentException();
			return put(paramInt2, paramArrayOfByte, paramInt1, true) || put(paramInt2, paramArrayOfByte, paramInt1, false);
		}
	}

	final byte[] get(int paramInt)
	{
		synchronized (data)
		{
			try
			{
				if (index.length() < 6 * paramInt + 6)
					return null;
				index.seek(6 * paramInt);
				index.readFully(aByteArray4057, 0, 6);
				int i = (0xFF & aByteArray4057[2]) + ((0xFF00 & aByteArray4057[1] << 8) + (0xFF0000 & aByteArray4057[0] << 16));
				if ((i > anInt2951))
					return null;
				int j = (0xFF & aByteArray4057[5]) + ((0xFF & aByteArray4057[3]) << 16) + ((aByteArray4057[4] & 0xFF) << 8);
				if ((j > data.length() / 520L))
					return null;
				byte[] arrayOfByte = new byte[i];
				int k = 0;
				int m = 0;
				while (k < i)
				{
					if (j == 0)
						return null;
					data.seek(j * 520);
					int n = i - k;
					if (n > 512)
						n = 512;
					data.readFully(aByteArray4057, 0, n + 8);
					int i1 = (0xFF00 & aByteArray4057[0] << 8) + (aByteArray4057[1] & 0xFF);
					int i2 = (aByteArray4057[2] << 8 & 0xFF00) + (aByteArray4057[3] & 0xFF);
					int i3 = ((aByteArray4057[5] & 0xFF) << 8) + (0xFF0000 & aByteArray4057[4] << 16) + (aByteArray4057[6] & 0xFF);
					int i4 = 0xFF & aByteArray4057[7];
					if ((paramInt != i1) || (m != i2) || (anInt2944 != i4))
						return null;
					if ((i3 < 0) || (i3 > data.length() / 520L))
						return null;
					if (j == i3)
						i3 = 0;
					j = i3;
					m++;
					System.arraycopy(aByteArray4057, 8, arrayOfByte, k, n);
					k += n;
				}
				return arrayOfByte;
			}
			catch (IOException localIOException)
			{
			}
			return null;
		}
	}

	private final boolean put(int paramInt2, byte[] paramArrayOfByte, int paramInt3, boolean paramBoolean)
	{
		synchronized (data)
		{
			try
			{
				int j;
				if (paramBoolean)
				{
					if (index.length() < paramInt2 * 6 + 6)
						return false;
					index.seek(paramInt2 * 6);
					index.readFully(aByteArray4057, 0, 6);
					j = (0xFF & aByteArray4057[5]) + ((0xFF & aByteArray4057[3]) << 16) + ((aByteArray4057[4] & 0xFF) << 8);
					if (j == 0 || (j > data.length() / 520L))
						return false;
				}
				else
				{
					j = (int) ((data.length() + 519L) / 520L);
					if (j == 0)
						j = 1;
				}
				aByteArray4057[0] = (byte) (paramInt3 >> 16);
				aByteArray4057[1] = (byte) (paramInt3 >> 8);
				aByteArray4057[2] = (byte) paramInt3;
				aByteArray4057[3] = (byte) (j >> 16);
				aByteArray4057[4] = (byte) (j >> 8);
				aByteArray4057[5] = (byte) j;
				index.seek(paramInt2 * 6);
				index.write(aByteArray4057, 0, 6);
				int k = 0;
				int m = 0;
				while (k < paramInt3)
				{
					int n = 0;
					if (paramBoolean)
					{
						data.seek(520 * j);
						data.readFully(aByteArray4057, 0, 8);
						int i1 = (0xFF & aByteArray4057[1]) + (0xFF00 & aByteArray4057[0] << 8);
						n = (0xFF00 & aByteArray4057[5] << 8) + (((aByteArray4057[4] & 0xFF) << 16) + (0xFF & aByteArray4057[6]));
						int i2 = (aByteArray4057[2] << 8 & 0xFF00) + (aByteArray4057[3] & 0xFF);
						int i3 = 0xFF & aByteArray4057[7];
						if ((paramInt2 != i1) || (i2 != m) || (i3 != anInt2944))
							return false;
						if ((n < 0) || (n > data.length() / 520L))
							return false;
						if (n == j)
							n = 0;
					}
					if (n == 0 && paramInt3 - k > 512)
					{
						paramBoolean = false;
						n = (int) ((data.length() + 519L) / 520L);
						if (n == 0)
							n++;
						if (n == j)
							n++;
					}
					aByteArray4057[0] = (byte) (paramInt2 >> 8);
					aByteArray4057[1] = (byte) paramInt2;
					aByteArray4057[2] = (byte) (m >> 8);
					aByteArray4057[3] = (byte) m;
					aByteArray4057[4] = (byte) (n >> 16);
					aByteArray4057[5] = (byte) (n >> 8);
					aByteArray4057[6] = (byte) n;
					aByteArray4057[7] = (byte) anInt2944;
					data.seek(520 * j);
					data.write(aByteArray4057, 0, 8);
					int i1 = paramInt3 - k;
					if (i1 > 512)
						i1 = 512;
					data.write(paramArrayOfByte, k, i1);
					m++;
					k += i1;
					j = n;
				}
				return true;
			}
			catch (IOException localIOException)
			{
			}
			return false;
		}
	}

	public final String toString()
	{
		return "Decompressor:" + anInt2944;
	}

	public Decompressor(RandomAccessFile paramRandomAccessFile1, RandomAccessFile paramRandomAccessFile2, int paramInt1, int paramInt2)
	{
		anInt2944 = paramInt1;
		data = paramRandomAccessFile1;
		index = paramRandomAccessFile2;
		anInt2951 = paramInt2;
	}
}