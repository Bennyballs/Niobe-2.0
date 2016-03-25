package src;

public final class IdentityKit
{
	public IdentityKit()
	{
	}

	public IdentityKit(Buffer buffer)
	{
		while (true)
		{
			int opcode = buffer.getUnsigned();
			if (opcode == 0)
				break;

			if (opcode == 1)
				anInt657 = buffer.getUnsigned();

			else if (opcode == 2)
			{
				int count = buffer.getUnsigned();
				anIntArray658 = new int[count];
				for (int i = 0; i != count; ++i)
					anIntArray658[i] = buffer.getUnsignedShort();

			}
			else if (opcode == 3)
			{
				aBoolean662 = true;
			}
			else if (opcode == 40)
			{
				int count = buffer.getUnsigned();
				anIntArray659 = new short[count];
				anIntArray660 = new short[count];
				for (int i = 0; i != count; ++i)
				{
					anIntArray659[i] = (short) buffer.getUnsignedShort();
					anIntArray660[i] = (short) buffer.getUnsignedShort();
				}

			}
			else if (opcode == 41)
			{
				int count = buffer.getUnsigned();
				aShortArray3356 = new short[count];
				aShortArray3353 = new short[count];
				for (int i = 0; i != count; ++i)
				{
					aShortArray3356[i] = (short) buffer.getUnsignedShort();
					aShortArray3353[i] = (short) buffer.getUnsignedShort();
				}

			}
			else if (opcode >= 60 && opcode < 70)
				anIntArray661[opcode - 60] = buffer.getUnsignedShort();

			else
			{
				System.out.println("[IdentityKit] Unknown opcode: " + opcode);
				break;
			}
		}
	}

	private short[] aShortArray3356;
	private short[] aShortArray3353;

	public static void unpackConfig(byte[] data)
	{
		Buffer buffer = new Buffer(data);
		int count = length = buffer.getUnsignedShort();
		cache = new IdentityKit[count];
		int pos = buffer.currentOffset;
		for (int i = 0; i != count; ++i)
		{
			buffer.currentOffset = pos;
			int size = buffer.getUnsignedShort();
			pos += 2 + size;
			cache[i] = size != 0 ? new IdentityKit(buffer):new IdentityKit();
		}

	}

	public boolean method537()
	{
		if (anIntArray658 == null)
			return true;
		boolean flag = true;
		for (int j = 0; j < anIntArray658.length; j++)
			if (!Model.method463(anIntArray658[j]))
				flag = false;

		return flag;
	}

	public Model method538()
	{
		if (anIntArray658 == null)
			return null;

		Model aclass30_sub2_sub4_sub6s[] = new Model[anIntArray658.length];
		for (int i = 0; i < anIntArray658.length; i++)
			aclass30_sub2_sub4_sub6s[i] = Model.getModel(anIntArray658[i]);

		Model model;
		if (aclass30_sub2_sub4_sub6s.length == 1)
			model = aclass30_sub2_sub4_sub6s[0];
		else
			model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);

		if (anIntArray659 != null)
			for (int l = 0; l < anIntArray659.length; l++)
				model.setColor(anIntArray659[l], anIntArray660[l]);


		if (aShortArray3356 != null)
			for (int l = 0; l < aShortArray3356.length; l++)
				model.setTexture(aShortArray3356[l], aShortArray3353[l]);


		return model;
	}

	public boolean method539()
	{
		boolean flag1 = true;
		for (int i = 0; i < 5; i++)
			if (anIntArray661[i] != -1 && !Model.method463(anIntArray661[i]))
				flag1 = false;

		return flag1;
	}

	public Model method540()
	{
		Model aclass30_sub2_sub4_sub6s[] = new Model[5];
		int j = 0;
		for (int k = 0; k < 5; k++)
			if (anIntArray661[k] != -1)
				aclass30_sub2_sub4_sub6s[j++] = Model.getModel(anIntArray661[k]);

		Model model = new Model(j, aclass30_sub2_sub4_sub6s);
		if (anIntArray659 != null)
			for (int l = 0; l < anIntArray659.length; l++)
				model.setColor(anIntArray659[l], anIntArray660[l]);


		if (aShortArray3356 != null)
			for (int l = 0; l < aShortArray3356.length; l++)
				model.setTexture(aShortArray3356[l], aShortArray3353[l]);


		//TODO: add support for texture modding
		return model;
	}

	public static void nullLoader()
	{
		cache = null;
	}

	public static int length;
	public static IdentityKit cache[];
	public int anInt657 = -1;
	private int[] anIntArray658;
	private short[] anIntArray659;
	private short[] anIntArray660;
	private final int[] anIntArray661 = {
		-1, -1, -1, -1, -1
	};
	public boolean aBoolean662;
}
