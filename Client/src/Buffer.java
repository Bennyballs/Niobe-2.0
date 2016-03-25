package src;

public final class Buffer
{
	final int v(int i)
	{
		currentOffset += 3;
		return (0xff & buffer[currentOffset - 3] << 16) + (0xff & buffer[currentOffset - 2] << 8) + (0xff & buffer[currentOffset - 1]);
	}

	public static String toString(byte[] b)
	{
		return toString(b, 0, b.length);
	}

	public static String toString(byte[] b, int n, int n1)
	{
		char[] chr = new char[n1];
		for (int i = 0; i != n1; ++i)
			chr[i] = (char) (b[n + i] & 0xff);

		return new String(chr, 0, n1);
	}

	public static byte[] toBytes(String str, int escape)
	{
		int count = str.length();
		byte[] bytes = new byte[count];
		for (int i = 0; i != count; ++i)
		{
			char chr = str.charAt(i);
			if (chr == escape || chr > 255)
				chr = ' ';

			bytes[i] = (byte) chr;
		}

		return bytes;
	}

	public static byte[] toBytes(String str)
	{
		return toBytes(str, -1);
	}

	public void skip(int length)
	{
		currentOffset += length;
	}

	public Buffer(byte abyte0[])
	{
		buffer = abyte0;
		currentOffset = 0;
	}

	public void appendPacketSize()
	{
		buffer[packetOffset] = (byte) (currentOffset - packetOffset - 1);
	}

	public void writeByte(int i)
	{
		buffer[currentOffset++] = (byte) i;
	}

	public void writeShort(int i)
	{
		buffer[currentOffset++] = (byte) (i >>> 8);
		buffer[currentOffset++] = (byte) i;
	}

	public void writeLEShort(int i)
	{
		buffer[currentOffset++] = (byte) i;
		buffer[currentOffset++] = (byte) (i >>> 8);
	}

	public void writeMediumInt(int i)
	{
		buffer[currentOffset++] = (byte) (i >>> 16);
		buffer[currentOffset++] = (byte) (i >>> 8);
		buffer[currentOffset++] = (byte) i;
	}

	public void writeInt(int i)
	{
		buffer[currentOffset++] = (byte) (i >>> 24);
		buffer[currentOffset++] = (byte) (i >>> 16);
		buffer[currentOffset++] = (byte) (i >>> 8);
		buffer[currentOffset++] = (byte) i;
	}

	public void writeDWordLE(int j)
	{
		buffer[currentOffset++] = (byte) j;
		buffer[currentOffset++] = (byte) (j >>> 8);
		buffer[currentOffset++] = (byte) (j >>> 16);
		buffer[currentOffset++] = (byte) (j >>> 24);
	}

	public void writeLong(long l)
	{
		buffer[currentOffset++] = (byte) (int) (l >>> 56);
		buffer[currentOffset++] = (byte) (int) (l >>> 48);
		buffer[currentOffset++] = (byte) (int) (l >>> 40);
		buffer[currentOffset++] = (byte) (int) (l >>> 32);
		buffer[currentOffset++] = (byte) (int) (l >>> 24);
		buffer[currentOffset++] = (byte) (int) (l >>> 16);
		buffer[currentOffset++] = (byte) (int) (l >>> 8);
		buffer[currentOffset++] = (byte) (int) l;
	}

	public void writeLine(String s)
	{
		byte[] bb = toBytes(s, 10);
		System.arraycopy(bb, 0, buffer, currentOffset, bb.length);
		currentOffset += bb.length;
		buffer[currentOffset++] = 10;
	}

	public void writeString(String s)
	{
		byte[] bb = toBytes(s, 0);
		System.arraycopy(bb, 0, buffer, currentOffset, bb.length);
		currentOffset += bb.length;
		buffer[currentOffset++] = 0;
	}

	public void putEx(byte abyte0[], int i, int j)
	{
		for (int k = j; k < j + i; k++)
			buffer[currentOffset++] = abyte0[k];
	}

	public void appendPacketSize(int i)
	{
		appendPacketSize();
		//data[currentOffset - i - 1] = (byte) i;
	}

	public int getUnsigned()
	{
		return buffer[currentOffset++] & 0xff;
	}

	public byte get()
	{
		return buffer[currentOffset++];
	}

	public int getUnsignedShort()
	{
		currentOffset += 2;
		return ((buffer[currentOffset - 2] & 0xff) << 8) | (buffer[currentOffset - 1] & 0xff);
	}

	public int getSignedShort()
	{
		currentOffset += 2;
		int i = ((buffer[currentOffset - 2] & 0xff) << 8) | (buffer[currentOffset - 1] & 0xff);
		if (i > 32767)
			i -= 0x10000;
		return i;
	}

	public int getMedium()
	{
		currentOffset += 3;
		return ((buffer[currentOffset - 3] & 0xff) << 16) | ((buffer[currentOffset - 2] & 0xff) << 8) | (buffer[currentOffset - 1] & 0xff);
	}

	public int getLEMedium()
	{
		currentOffset += 3;
		return ((buffer[currentOffset - 1] & 0xff) << 16) | ((buffer[currentOffset - 2] & 0xff) << 8) | (buffer[currentOffset - 3] & 0xff);
	}

	public int getInt()
	{
		currentOffset += 4;
		return ((buffer[currentOffset - 4] & 0xff) << 24) | ((buffer[currentOffset - 3] & 0xff) << 16) | ((buffer[currentOffset - 2] & 0xff) << 8) | (buffer[currentOffset - 1] & 0xff);
	}

	public long getLong()
	{
		long l = (long) getInt() & 0xffffffffL;
		long l1 = (long) getInt() & 0xffffffffL;
		return (l << 32) | l1;
	}

	public String getLing()
	{
		int i = currentOffset;
		while (buffer[currentOffset++] != 10)
			;
		return toString(buffer, i, currentOffset - i - 1);
	}

	public String getString()
	{
		int i = currentOffset;
		while (buffer[currentOffset++] != 0)
			;
		return toString(buffer, i, currentOffset - i - 1);
	}

	public byte[] getLineData()
	{
		int i = currentOffset;
		while (buffer[currentOffset++] != 10)
			;
		byte abyte0[] = new byte[currentOffset - i - 1];
		System.arraycopy(buffer, i, abyte0, i - i, currentOffset - 1 - i);
		return abyte0;
	}

	public void getEx(int i, int j, byte abyte0[])
	{
		for (int l = j; l < j + i; l++)
			abyte0[l] = buffer[currentOffset++];
	}

	public void initBitAccess()
	{
		bitPosition = currentOffset << 3;
	}

	public int getBits(int i)
	{
		int k = bitPosition >>> 3;
		int l = 8 - (bitPosition & 7);
		int i1 = 0;
		bitPosition += i;
		for (; i > l; l = 8)
		{
			i1 += (buffer[k++] & anIntArray1409[l]) << i - l;
			i -= l;
		}
		if (i == l)
			i1 += buffer[k] & anIntArray1409[l];
		else
			i1 += buffer[k] >>> l - i & anIntArray1409[i];
		return i1;
	}

	public void finishBitAccess()
	{
		currentOffset = (bitPosition + 7) >>> 3;
	}

	public int getSignedSmart()
	{
		int i = buffer[currentOffset] & 0xff;
		if (i < 128)
			return getUnsigned() - 64;
		else
			return getUnsignedShort() - 49152;
	}

	public int getUnsignedSmart()
	{
		int i = buffer[currentOffset] & 0xff;
		if (i < 128)
			return getUnsigned();
		else
			return getUnsignedShort() - 32768;
	}

	public int getSmartInt()
	{
		int baseVal = 0;
		int lastVal = 0;
		while ((lastVal = getUnsignedSmart()) == 32767)
		{
			baseVal += 32767;
		}
		return baseVal + lastVal;
	}

	public void method424(int i)
	{
		buffer[currentOffset++] = (byte) (-i);
	}

	public void method425(int j)
	{
		buffer[currentOffset++] = (byte) (128 - j);
	}

	public int method426()
	{
		return buffer[currentOffset++] - 128 & 0xff;
	}

	public int readByteC()
	{
		return -buffer[currentOffset++] & 0xff;
	}

	public int readByteS()
	{
		return 128 - buffer[currentOffset++] & 0xff;
	}

	public byte method429()
	{
		return (byte) (-buffer[currentOffset++]);
	}

	public byte method430()
	{
		return (byte) (128 - buffer[currentOffset++]);
	}

	public void method431(int i)
	{
		buffer[currentOffset++] = (byte) i;
		buffer[currentOffset++] = (byte) (i >>> 8);
	}

	public void method432(int j)
	{
		buffer[currentOffset++] = (byte) (j >>> 8);
		buffer[currentOffset++] = (byte) (j + 128);
	}

	public void method433(int j)
	{
		buffer[currentOffset++] = (byte) (j + 128);
		buffer[currentOffset++] = (byte) (j >>> 8);
	}

	public int method434()
	{
		currentOffset += 2;
		return ((buffer[currentOffset - 1] & 0xff) << 8) | (buffer[currentOffset - 2] & 0xff);
	}

	public int method435()
	{
		currentOffset += 2;
		return ((buffer[currentOffset - 2] & 0xff) << 8) | (buffer[currentOffset - 1] - 128 & 0xff);
	}

	public int method436()
	{
		currentOffset += 2;
		return ((buffer[currentOffset - 1] & 0xff) << 8) | (buffer[currentOffset - 2] - 128 & 0xff);
	}

	public int method437()
	{
		currentOffset += 2;
		int j = ((buffer[currentOffset - 1] & 0xff) << 8) | (buffer[currentOffset - 2] & 0xff);
		if (j > 32767)
			j -= 0x10000;
		return j;
	}

	public int method438()
	{
		currentOffset += 2;
		int j = ((buffer[currentOffset - 1] & 0xff) << 8) | (buffer[currentOffset - 2] - 128 & 0xff);
		if (j > 32767)
			j -= 0x10000;
		return j;
	}

	public int method439()
	{
		currentOffset += 4;
		return ((buffer[currentOffset - 2] & 0xff) << 24) | ((buffer[currentOffset - 1] & 0xff) << 16) | ((buffer[currentOffset - 4] & 0xff) << 8) | (buffer[currentOffset - 3] & 0xff);
	}

	public int method440()
	{
		currentOffset += 4;
		return ((buffer[currentOffset - 3] & 0xff) << 24) | ((buffer[currentOffset - 4] & 0xff) << 16) | ((buffer[currentOffset - 1] & 0xff) << 8) | (buffer[currentOffset - 2] & 0xff);
	}

	public void method441(int i, byte abyte0[], int j)
	{
		for (int k = (i + j) - 1; k >= i; k--)
			buffer[currentOffset++] = (byte) (abyte0[k] + 128);
	}

	public void method442(int i, int j, byte abyte0[])
	{
		for (int k = (j + i) - 1; k >= j; k--)
			abyte0[k] = buffer[currentOffset++];
	}

	public void get(byte[] array, int offset, int length)
	{
		System.arraycopy(buffer, currentOffset, array, offset, length);
		currentOffset += length;
	}

	public void putSmartLong(long n)
	{
		if (n < 0L)
			n = -1L;

		if (n < 0x3fL)
			writeByte((int) n + 1);
		else if (n < 0x3fffL)
			writeShort((int) n | 0x4000);
		else if (n < 0x3fffffffL)
			writeInt((int) n | 0x80000000);

		else
		{
			if (n > 0x3fffffffffffffffL)
				n = 0x3fffffffffffffffL;

			writeLong(n | 0xc000000000000000L);
		}
	}

	public int getSizeInt()
	{
		int val = getUnsigned();
		if (val != 0xff)
			return val;

		return getInt();
	}

	public long getSmartLong()
	{
		int size = (buffer[currentOffset] & 0xc0) >>> 6;
		if (size == 0)
			return getUnsigned() - 1;

		if (size == 1)
			return getUnsignedShort() & 0x3fff;

		if (size == 2)
			return getInt() & 0x3fffffff;

		return getLong() & 0x3fffffffffffffffL;
	}

	private int packetOffset;
	public byte buffer[];
	public int currentOffset;
	public int bitPosition;
	private static final int[] anIntArray1409 = {
		0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff, 0x3ffff, 0x7ffff,
		0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff,
		0x3fffffff, 0x7fffffff, -1
	};
}