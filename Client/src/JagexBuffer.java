package src;


import java.math.BigInteger;

import src.sign.signlink;

public final class JagexBuffer extends NodeSub {

	public static JagexBuffer create() {
		synchronized (nodeList) {
			JagexBuffer stream = null;
			if (anInt1412 > 0) {
				anInt1412--;
				stream = (JagexBuffer) nodeList.popFront();
			}
			if (stream != null) {
				stream.currentOffset = 0;
				return stream;
			}
		}
		JagexBuffer stream_1 = new JagexBuffer();
		stream_1.currentOffset = 0;
		stream_1.buffer = new byte[5000];
		return stream_1;
	}

	public long getSmartLong() {
		int size = (buffer[currentOffset] & 0xc0) >>> 6;
		if (size == 0)
			return readUnsignedByte() - 1;

		if (size == 1)
			return readUnsignedShort() & 0x3fff;

		if (size == 2)
			return readInt() & 0x3fffffff;

		return readLong() & 0x3fffffffffffffffL;
	}


	final int v(int i) {
		currentOffset += 3;
		return (0xff & buffer[currentOffset - 3] << 16)
				+ (0xff & buffer[currentOffset - 2] << 8)
				+ (0xff & buffer[currentOffset - 1]);
	}

	private JagexBuffer() {
	}

	public JagexBuffer(byte abyte0[]) {
		buffer = abyte0;
		currentOffset = 0;
	}
	
	public final int getMediumInt() {
		currentOffset += 3;
		return (0xff & buffer[currentOffset - 1]) + ((buffer[currentOffset - 3] << 16 & 0xff0000) + (0xff00 & buffer[currentOffset - 2] << 8));
	}

	public void createFrame(int i) {
		buffer[currentOffset++] = (byte) (i + encryption.getNextKey());
	}
	
	public void writeByte(int i) {
		buffer[currentOffset++] = (byte) i;
	}

	public String readNewString() {
		int i = currentOffset;
		while (buffer[currentOffset++] != 0);
		return new String(buffer, i, currentOffset - i - 1);
	}

	public int readUSmart2() {
		int baseVal = 0;
		int lastVal = 0;
		while ((lastVal = getSmartB()) == 32767) {
			baseVal += 32767;
		}
		return baseVal + lastVal;
	}

	public void writeShortSpaceSaver(int i) {
		buffer[currentOffset++] = (byte) ((i >> 8) + 1);
		buffer[currentOffset++] = (byte) i;
	}

	public void writeShort(int i) {
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) i;
	}

	public void writeLEShort(int i) {
		buffer[currentOffset++] = (byte) i;
		buffer[currentOffset++] = (byte) (i >> 8);
	}

	public void writeTripleInt(int i) {
		buffer[currentOffset++] = (byte) (i >> 16);
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) i;
	}

	public void writeInt(int i) {
		buffer[currentOffset++] = (byte) (i >> 24);
		buffer[currentOffset++] = (byte) (i >> 16);
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) i;
	}

	public void method403(int j) {
		buffer[currentOffset++] = (byte) j;
		buffer[currentOffset++] = (byte) (j >> 8);
		buffer[currentOffset++] = (byte) (j >> 16);
		buffer[currentOffset++] = (byte) (j >> 24);
	}

	public void writeLong(long l) {
		try {
			buffer[currentOffset++] = (byte) (int) (l >> 56);
			buffer[currentOffset++] = (byte) (int) (l >> 48);
			buffer[currentOffset++] = (byte) (int) (l >> 40);
			buffer[currentOffset++] = (byte) (int) (l >> 32);
			buffer[currentOffset++] = (byte) (int) (l >> 24);
			buffer[currentOffset++] = (byte) (int) (l >> 16);
			buffer[currentOffset++] = (byte) (int) (l >> 8);
			buffer[currentOffset++] = (byte) (int) l;
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("14395, " + 5 + ", " + l + ", "
					+ runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public void writeString(String s) {
		// s.getBytes(0, s.length(), data, currentOffset); //deprecated
		System.arraycopy(s.getBytes(), 0, buffer, currentOffset, s.length());
		currentOffset += s.length();
		buffer[currentOffset++] = 10;
	}

	public void writeBytes(byte abyte0[], int i, int j) {
		for (int k = j; k < j + i; k++)
			buffer[currentOffset++] = abyte0[k];
	}

	public void writeBytes(int i) {
		buffer[currentOffset - i - 1] = (byte) i;
	}

	public int readUnsignedByte() {
		return buffer[currentOffset++] & 0xff;
	}

	public byte readByte() {
		return buffer[currentOffset++];
	}

	public int readUnsignedShort() {
		currentOffset += 2;
		return ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
	}

	public int readUnsignedShort2() {
		currentOffset += 2;
		int i = ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
		if(i  > 60000)
			i = -65536 + i;
		return i;
	}

	public int readSignedShort() {
		currentOffset += 2;
		int i = ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
		if (i > 32767)
			i -= 0x10000;
		return i;
	}

	public int readTripleBytes() {
		currentOffset += 3;
		return ((buffer[currentOffset - 3] & 0xff) << 16) + ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
	}

	public int readInt() {
		currentOffset += 4;
		return ((buffer[currentOffset - 4] & 0xff) << 24)
				+ ((buffer[currentOffset - 3] & 0xff) << 16)
				+ ((buffer[currentOffset - 2] & 0xff) << 8)
				+ (buffer[currentOffset - 1] & 0xff);
	}

	public long readLong() {
		long l = (long) readInt() & 0xffffffffL;
		long l1 = (long) readInt() & 0xffffffffL;
		return (l << 32) + l1;
	}

	public String readString() {
		int i = currentOffset;
		while (buffer[currentOffset++] != 10)
			;
		return new String(buffer, i, currentOffset - i - 1);
	}

	public byte[] readBytes() {
		int i = currentOffset;
		while (buffer[currentOffset++] != 10)
			;
		byte abyte0[] = new byte[currentOffset - i - 1];
		System.arraycopy(buffer, i, abyte0, i - i, currentOffset - 1 - i);
		return abyte0;
	}

	public void readBytes(int i, int j, byte abyte0[]) {
		for (int l = j; l < j + i; l++)
			abyte0[l] = buffer[currentOffset++];
	}

	public void initBitAccess() {
		bitPosition = currentOffset * 8;
	}

	public int readBits(int i) {
		int k = bitPosition >> 3;
		int l = 8 - (bitPosition & 7);
		int i1 = 0;
		bitPosition += i;
		for (; i > l; l = 8) {
			i1 += (buffer[k++] & anIntArray1409[l]) << i - l;
			i -= l;
		}
		if (i == l)
			i1 += buffer[k] & anIntArray1409[l];
		else
			i1 += buffer[k] >> l - i & anIntArray1409[i];
		return i1;
	}

	public void finishBitAccess() {
		currentOffset = (bitPosition + 7) / 8;
	}

	public int readSmart() {
		int i = buffer[currentOffset] & 0xff;
		if (i < 128)
			return readUnsignedByte() - 64;
		else
			return readUnsignedShort() - 49152;
	}

	public int getSmartB() {
		int i = buffer[currentOffset] & 0xff;
		if (i < 128)
			return readUnsignedByte();
		else
			return readUnsignedShort() - 32768;
	}

	public void doKeys() {
		int i = currentOffset;
		currentOffset = 0;
		byte abyte0[] = new byte[i];
		readBytes(i, 0, abyte0);
		BigInteger biginteger2 = new BigInteger(abyte0);
		BigInteger biginteger3 = biginteger2/* .modPow(biginteger, biginteger1) */;
		byte abyte1[] = biginteger3.toByteArray();
		currentOffset = 0;
		writeByte(abyte1.length);
		writeBytes(abyte1, abyte1.length, 0);
	}

	public void writeByteC(int i) {
		buffer[currentOffset++] = (byte) (-i);
	}

	public void method425(int j) {
		buffer[currentOffset++] = (byte) (128 - j);
	}

	public int readByteA() {
		return buffer[currentOffset++] - 128 & 0xff;
	}

	public int readByteC() {
		return -buffer[currentOffset++] & 0xff;
	}

	public int readByteS() {
		return 128 - buffer[currentOffset++] & 0xff;
	}

	public byte method429() {
		return (byte) (-buffer[currentOffset++]);
	}

	public byte method430() {
		return (byte) (128 - buffer[currentOffset++]);
	}

	public void writeShortA(int j) {
		buffer[currentOffset++] = (byte) (j >> 8);
		buffer[currentOffset++] = (byte) (j + 128);
	}

	public void writeLEShortA(int j) {
		buffer[currentOffset++] = (byte) (j + 128);
		buffer[currentOffset++] = (byte) (j >> 8);
	}

	public int readLEShort() {
		currentOffset += 2;
		return ((buffer[currentOffset - 1] & 0xff) << 8)
				+ (buffer[currentOffset - 2] & 0xff);
	}

	public int readShortA() {
		currentOffset += 2;
		return ((buffer[currentOffset - 2] & 0xff) << 8)
				+ (buffer[currentOffset - 1] - 128 & 0xff);
	}

	public int readLEShortA() {
		currentOffset += 2;
		return ((buffer[currentOffset - 1] & 0xff) << 8)
				+ (buffer[currentOffset - 2] - 128 & 0xff);
	}

	public int readShort() {
		currentOffset += 2;
		int j = ((buffer[currentOffset - 1] & 0xff) << 8)
				+ (buffer[currentOffset - 2] & 0xff);
		if (j > 32767)
			j -= 0x10000;
		return j;
	}

	public int method438() {
		currentOffset += 2;
		int j = ((buffer[currentOffset - 1] & 0xff) << 8)
				+ (buffer[currentOffset - 2] - 128 & 0xff);
		if (j > 32767)
			j -= 0x10000;
		return j;
	}

	public int readSingleInt() {
		currentOffset += 4;
		return ((buffer[currentOffset - 2] & 0xff) << 24)
				+ ((buffer[currentOffset - 1] & 0xff) << 16)
				+ ((buffer[currentOffset - 4] & 0xff) << 8)
				+ (buffer[currentOffset - 3] & 0xff);
	}

	public int method440() {
		currentOffset += 4;
		return ((buffer[currentOffset - 3] & 0xff) << 24)
				+ ((buffer[currentOffset - 4] & 0xff) << 16)
				+ ((buffer[currentOffset - 1] & 0xff) << 8)
				+ (buffer[currentOffset - 2] & 0xff);
	}

	public void method441(int i, byte abyte0[], int j) {
		for (int k = (i + j) - 1; k >= i; k--)
			buffer[currentOffset++] = (byte) (abyte0[k] + 128);

	}

	public void method442(int i, int j, byte abyte0[]) {
		for (int k = (j + i) - 1; k >= j; k--)
			abyte0[k] = buffer[currentOffset++];

	}

	public byte buffer[];
	public int currentOffset;
	public int bitPosition;
	private static final int[] anIntArray1409 = { 0, 1, 3, 7, 15, 31, 63, 127,
			255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff,
			0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff,
			0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff,
			0x7fffffff, -1 };
	public ISAACRandomGen encryption;
	private static int anInt1412;
	private static final Deque nodeList = new Deque();
}
