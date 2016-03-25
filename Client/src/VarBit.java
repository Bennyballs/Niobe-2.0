package src;

//"Variable bit"?
public final class VarBit
{
	private VarBit(Buffer buffer)
	{
		while (true)
		{
			int opcode = buffer.getUnsigned();
			if (opcode == 0)
				break;

			if (opcode == 1)
			{
				anInt648 = buffer.getUnsignedShort();
				anInt649 = buffer.getUnsigned();
				anInt650 = buffer.getUnsigned();
			}
			else
			{
				System.out.println("[VariableBit] Unknown opcode: " + opcode);
				break;
			}
		}
	}

	public static void unpackConfig(byte[] data)
	{
		Buffer buffer = new Buffer(data);
		int count = buffer.getUnsignedShort();
		cache = new VarBit[count];
		int pos = buffer.currentOffset;
		for (int i = 0; i != count; ++i)
		{
			buffer.currentOffset = pos;
			int size = buffer.getUnsignedShort();
			pos += 2 + size;
			if (size != 0)
				cache[i] = new VarBit(buffer);

		}

	}

	public static void nullLoader()
	{
		cache = null;
	}

	public static VarBit[] cache;
	public int anInt648;
	public int anInt649;
	public int anInt650;
}
