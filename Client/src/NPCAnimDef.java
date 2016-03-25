package src;


public final class NPCAnimDef
{
	private NPCAnimDef()
	{
		anInt3275 = -1;
		anInt3253 = -1;
		runAnimation = -1;
		anInt3282 = -1;
		anInt3287 = -1;
		walkAnimation = -1;
		anInt3260 = -1;
		anInt3286 = -1;
		aBoolean3267 = true;
		anInt3290 = -1;
		anInt3256 = -1;
		anInt3295 = -1;
		anInt3292 = -1;
		turnRightAnimation = -1;
		standAnimation = -1;
		anInt3299 = -1;
		anInt3298 = -1;
		anInt3270 = -1;
		anInt3274 = -1;
		turnLeftAnimation = -1;
		reverseAnimation = -1;
		anInt3301 = -1;
		anInt3293 = -1;
		anInt3305 = -1;
		anInt3303 = -1;
		anInt3304 = -1;
		anInt3271 = -1;
	}

	public int[][] anIntArrayArray3249;
	public int anInt3250;
	public int runAnimation;
	public int anInt3253;
	public int[] anIntArray3255;
	public int anInt3256;
	public int anInt3258;
	public int standAnimation;
	public int anInt3260;
	public int anInt3261;
	public int reverseAnimation;
	public int anInt3263;
	public int anInt3266;
	public boolean aBoolean3267;
	public int turnRightAnimation;
	public int anInt3270;
	public int anInt3271;
	public int anInt3272;
	public int[][] anIntArrayArray3273;
	public int anInt3274;
	public int anInt3275;
	public int[] anIntArray3276;
	public int walkAnimation;
	public int anInt3278;
	public int anInt3281;
	public int anInt3282;
	public int anInt3283;
	public int anInt3284;
	public int anInt3285;
	public int anInt3286;
	public int anInt3287;
	public int anInt3289;
	public int anInt3290;
	public int anInt3291;
	public int anInt3292;
	public int anInt3293;
	public int[] anIntArray3294;
	public int anInt3295;
	public int turnLeftAnimation;
	public int anInt3298;
	public int anInt3299;
	public int anInt3301;
	public int[] anIntArray3302;
	public int anInt3303;
	public int anInt3304;
	public int anInt3305;

	private NPCAnimDef(Buffer buffer)
	{
		this();
		while (true)
		{
			int opcode = buffer.getUnsigned();
			if (opcode == 0)
				break;

			if (opcode == 1)
			{
				standAnimation = buffer.getUnsignedShort();
				walkAnimation = buffer.getUnsignedShort();
				if (standAnimation == -1)
					standAnimation = -1;

				if (walkAnimation == -1)
					walkAnimation = -1;

			}
			else if (opcode == 2)
				reverseAnimation = buffer.getUnsignedShort();
			else if (opcode == 3)
				turnLeftAnimation = buffer.getUnsignedShort();
			else if (opcode == 4)
				turnRightAnimation = buffer.getUnsignedShort();
			else if (opcode == 5)
				anInt3304 = buffer.getUnsignedShort();
			else if (opcode == 6)
				runAnimation = buffer.getUnsignedShort();
			else if (opcode == 7)
				anInt3271 = buffer.getUnsignedShort();
			else if (opcode == 8)
				anInt3270 = buffer.getUnsignedShort();
			else if (opcode == 9)
				anInt3293 = buffer.getUnsignedShort();

			else if (opcode == 26)
			{
				anInt3261 = (short) (4 * buffer.getUnsigned());
				anInt3266 = (short) (4 * buffer.getUnsigned());
			}
			else if (opcode == 27)
			{
				if (anIntArrayArray3273 == null)
					anIntArrayArray3273 = new int[256][];

				int index = buffer.getUnsigned();
				anIntArrayArray3273[index] = new int[6];
				for (int i = 0; i != 6; ++i)
					anIntArrayArray3273[index][i] = buffer.getSignedShort();

			}
			else if (opcode == 28)
			{
				int count = buffer.getUnsignedShort();
				anIntArray3276 = new int[count];
				for (int i = 0; i != count; ++i)
				{
					anIntArray3276[i] = buffer.getUnsigned();
					if (anIntArray3276[i] == 0xff)
						anIntArray3276[i] = -1;

				}

			}
			else if (opcode == 29)
				anInt3258 = buffer.getUnsigned();
			else if (opcode == 30)
				anInt3283 = buffer.getUnsignedShort();
			else if (opcode == 31)
				anInt3278 = buffer.getUnsigned();
			else if (opcode == 32)
				anInt3284 = buffer.getUnsignedShort();
			else if (opcode == 33)
				anInt3250 = buffer.getSignedShort();
			else if (opcode == 35)
				anInt3289 = buffer.getUnsignedShort();
			else if (opcode == 36)
				anInt3285 = buffer.getSignedShort();
			else if (opcode == 37)
				anInt3256 = buffer.getUnsigned();
			else if (opcode == 38)
				anInt3299 = buffer.getUnsignedShort();
			else if (opcode == 39)
				anInt3274 = buffer.getUnsignedShort();
			else if (opcode == 40)
				anInt3286 = buffer.getUnsignedShort();
			else if (opcode == 41)
				anInt3301 = buffer.getUnsignedShort();
			else if (opcode == 42)
				anInt3287 = buffer.getUnsignedShort();
			else if (opcode == 43)
				anInt3290 = buffer.getUnsignedShort();
			else if (opcode == 44)
				anInt3292 = buffer.getUnsignedShort();
			else if (opcode == 45)
				anInt3303 = buffer.getUnsignedShort();
			else if (opcode == 46)
				anInt3275 = buffer.getUnsignedShort();
			else if (opcode == 47)
				anInt3260 = buffer.getUnsignedShort();
			else if (opcode == 48)
				anInt3282 = buffer.getUnsignedShort();
			else if (opcode == 49)
				anInt3253 = buffer.getUnsignedShort();
			else if (opcode == 50)
				anInt3298 = buffer.getUnsignedShort();
			else if (opcode == 51)
				anInt3305 = buffer.getUnsignedShort();

			else if (opcode == 52)
			{
				int count = buffer.getUnsigned();
				anIntArray3294 = new int[count];
				anIntArray3302 = new int[count];
				for (int i = 0; i != count; ++i)
				{
					anIntArray3294[i] = buffer.getUnsignedShort();
					int alpha = buffer.getUnsigned();
					anIntArray3302[i] = alpha;
					anInt3281 += alpha;
				}

			}
			else if (opcode == 53)
				aBoolean3267 = false;

			else if (opcode == 54)
			{
				anInt3263 = buffer.getUnsigned() << 6;
				anInt3291 = buffer.getUnsigned() << 6;
			}
			else if (opcode == 55)
			{
				if (anIntArray3255 == null)
					anIntArray3255 = new int[256];

				int index = buffer.getUnsigned();
				anIntArray3255[index] = buffer.getUnsignedShort();
			}
			else if (opcode == 56)
			{
				if (anIntArrayArray3249 == null)
					anIntArrayArray3249 = new int[256][];

				int index = buffer.getUnsigned();
				anIntArrayArray3249[index] = new int[3];
				for (int i = 0; i != 3; ++i)
					anIntArrayArray3249[index][i] = buffer.getSignedShort();

			}
			else
			{
				System.out.println("[NPCAnimDef] Unknown opcode: " + opcode);
				break;
			}
		}
	}

	public static void unpackConfig(byte[] data)
	{
		Buffer buffer = new Buffer(data);
		int count = buffer.getUnsignedShort();
		defs = new NPCAnimDef[count];
		int pos = buffer.currentOffset;
		for (int i = 0; i != count; ++i)
		{
			buffer.currentOffset = pos;
			int size = buffer.getUnsignedShort();
			pos += 2 + size;
			if (size != 0)
				defs[i] = new NPCAnimDef(buffer);

		}

	}

	public static void nullLoader()
	{
		defs = null;
	}

	public static NPCAnimDef[] defs;
}
