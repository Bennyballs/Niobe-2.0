package src;


public final class FrameReader
{
	private FrameReader(Buffer buffer, int size, SkinList skin)
	{
		load(buffer, size, skin);
	}

	private void load(Buffer buffer, int size, SkinList skin)
	{
		int start = buffer.currentOffset;
		int count = buffer.getUnsigned();
		int pos = 0;
		int vertexSkin1 = -1;
		int vertexSkin2 = -1;
		Buffer buffer1 = new Buffer(buffer.buffer);
		buffer1.currentOffset = buffer.currentOffset + count;
		for (int i = 0; i != count; ++i)
		{
			int vertexSkin = skin.anIntArray342[i];
			if (vertexSkin == 0)
				vertexSkin1 = i;

			int flags = buffer.getUnsigned();
			if (flags == 0)
				continue;

			if (vertexSkin == 0)
				vertexSkin2 = i;

			vertexIndicesBuffer[pos] = (short) i;
			short staticFrame = 0;
			if (vertexSkin == 3 || vertexSkin == 10)
				staticFrame = (short) 128;

			if ((flags & 0x1) != 0)
				vertexXBuffer[pos] = (short) buffer1.getSignedSmart();
			else
				vertexXBuffer[pos] = staticFrame;

			if ((flags & 0x2) != 0)
				vertexYBuffer[pos] = (short) buffer1.getSignedSmart();
			else
				vertexYBuffer[pos] = staticFrame;

			if ((flags & 0x4) != 0)
				vertexZBuffer[pos] = (short) buffer1.getSignedSmart();
			else
				vertexZBuffer[pos] = staticFrame;

			vertexFlagsBuffer[pos] = (byte) ((flags >>> 3) & 0x3);
			if (vertexSkin == 2 || vertexSkin == 9)
			{
				vertexXBuffer[pos] = (short) ((vertexXBuffer[pos] << 2) & 0x3fff);
				vertexYBuffer[pos] = (short) ((vertexYBuffer[pos] << 2) & 0x3fff);
				vertexZBuffer[pos] = (short) ((vertexZBuffer[pos] << 2) & 0x3fff);
			}
			vertexSkinsBuffer[pos] = (short) -1;
			if (vertexSkin == 1 || vertexSkin == 2 || vertexSkin == 3)
			{
				if (vertexSkin2 < vertexSkin1)
				{
					vertexSkinsBuffer[pos] = (short) vertexSkin1;
					vertexSkin2 = vertexSkin1;
				}
			}
			else if (vertexSkin == 5)
				aBoolean2640 = true;
			else if (vertexSkin == 7)
				aBoolean2637 = true;
			else if (vertexSkin == 9 || vertexSkin == 10 || vertexSkin == 8)
				aBoolean2646 = true;

			pos++;
		}

		if (buffer1.currentOffset - start != size)
			System.out.println("[FrameReader] Warning: size mismatch " + (buffer1.currentOffset - start) + "," + size);

		this.skin = skin;
		vertexCount = pos;//vertex count
		vertexIndices = new short[pos];//vertex indices
		vertexX = new short[pos];//vertex x
		vertexY = new short[pos];//vertex y
		vertexZ = new short[pos];//vertex z
		vertexSkins = new short[pos];//vertex skins
		vertexFlags = new byte[pos];//vertex clip
		for (int i = 0; i != pos; ++i)
		{
			vertexIndices[i] = vertexIndicesBuffer[i];
			vertexX[i] = vertexXBuffer[i];
			vertexY[i] = vertexYBuffer[i];
			vertexZ[i] = vertexZBuffer[i];
			vertexSkins[i] = vertexSkinsBuffer[i];
			vertexFlags[i] = vertexFlagsBuffer[i];
		}

	}

	public FrameReader(int skinId, byte[] data)
	{
		this.skinId = skinId;
		this.data = data;
	}

	private boolean load()
	{
		if (!SkinList.loaded(skinId))
			return false;

		byte[] tmp = data;
		data = null;
		SkinList skin = SkinList.get(skinId);
		if (skin == null)
		{
			System.out.println("[FrameReader] Null skin: " + skinId);
			throw new RuntimeException();
		}
		load(new Buffer(tmp), tmp.length, skin);
		return true;
	}

	private static final int BUFFER_SIZE = 500;
	private static final short[] vertexIndicesBuffer = new short[BUFFER_SIZE];
	private static final short[] vertexXBuffer = new short[BUFFER_SIZE];
	private static final short[] vertexYBuffer = new short[BUFFER_SIZE];
	private static final short[] vertexZBuffer = new short[BUFFER_SIZE];
	private static final short[] vertexSkinsBuffer = new short[BUFFER_SIZE];
	private static final byte[] vertexFlagsBuffer = new byte[BUFFER_SIZE];
	public short[] vertexSkins;
	public byte[] vertexFlags;
	public boolean aBoolean2640;
	public boolean aBoolean2637;
	public boolean aBoolean2646;
	public int anInt636;
	public int vertexCount;
	public short[] vertexIndices;
	public short[] vertexX;
	public short[] vertexY;
	public short[] vertexZ;
	public SkinList skin;
	private byte[] data;
	private int skinId;

	public static void load(int id, byte[] data)
	{
		loaded[id] = true;
		if (data != null)
		{
			Buffer buffer = new Buffer(data);
			int count = buffer.getUnsignedShort();
			FrameReader[] frames = new FrameReader[count];
			int pos = buffer.currentOffset;
			for (int i = 0; i != count; ++i)
			{
				buffer.currentOffset = pos;
				int size = buffer.getUnsignedShort();
				pos += 2 + size;
				if (size < 3)
					continue;

				size -= 3;
				++buffer.currentOffset;
				int skinId = buffer.getUnsignedShort();
				if (!SkinList.loaded(skinId))
				{
					byte[] tmp = new byte[size];
					buffer.get(tmp, 0, size);
					frames[i] = new FrameReader(skinId, tmp);
					//System.out.println("(w) loaded frame: " + i);
					continue;
				}
				SkinList skin = SkinList.get(skinId);
				if (skin != null)
					try
					{
						frames[i] = new FrameReader(buffer, size, skin);
						//System.out.println("loaded frame: " + i);
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				else
					System.out.println("[FrameReader] Null skin: " + skinId);

			}

			frameCache[id] = frames;
		}
	}

	public static void nullLoader()
	{
		updateManager = null;
		frameCache = null;
		loaded = null;
	}

	public static FrameReader get(int id)
	{
		if (id == -1)
			return null;

		int fileId = (id >>> 16) & 0xffff;
		int frameId = id & 0xffff;
		if (fileId >= frameCache.length)
			return null;

		FrameReader[] frames = frameCache[fileId];
		if (frames != null)
		{
			if (frameId >= frames.length)
				return null;

			FrameReader frame = frames[frameId];
			if (frame != null)
			{
				if (frame.data != null)
					try
					{
						if (!frame.load())
							frame = null;

					}
					catch (Exception ex)
					{
						ex.printStackTrace();
						frames[frameId] = null;
					}

				return frame;
			}
		}
		if (!loaded[fileId])
			updateManager.loadMandatory(1, fileId);//should this be important?

		return null;
	}

	public static void method528(int total)
	{
		frameCache = new FrameReader[total][];
		loaded = new boolean[total];
		updateManager = Client.resourceProvider;
	}

	private static boolean[] loaded;
	private static ResourceProvider updateManager;
	private static FrameReader[][] frameCache;
}
