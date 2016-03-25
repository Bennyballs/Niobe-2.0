package src;


public class Texture
{
	public Texture(int width, int height)
	{
		this.width = width;
		this.height = height;
		opaque = true;
	}

	public int getPixel(int i)
	{
		return 0xffffffff;
	}

	public static Texture getTest(int id)
	{
		if (id < 0 || id >= textures.length)
			return null;

		if (loaded[id])
			return textures[id];

		return null;
	}

	public static Texture get(int id)
	{
		if (id < 0 || id >= textures.length)
			return null;

		if (loaded[id])
			return textures[id];

		//System.out.println("Loading texture: " + id);
		//updateManager.request(7, id, true, true);//should this be important?

		updateManager.loadMandatory(4, id);
		return null;
	}

	public static void init(int count, ResourceProvider updateManager_)
	{
		textures = new Texture[count];
		loaded = new boolean[count];
		Rasterizer.initTextures(count);
		updateManager = updateManager_;
	}

	public static void load(int id, byte[] data)
	{
		loaded[id] = true;
		if (data != null && data.length >= 5)
		{
			Buffer buffer = new Buffer(data);
			int type = buffer.getUnsigned();
			int width = buffer.getUnsignedShort();
			int height = buffer.getUnsignedShort();
			if (type == 0)
				textures[id] = new PalettedTexture(width, height, buffer, false);
			else if (type == 1)
				textures[id] = new RGBTexture(width, height, buffer);
			else if (type == 2)
				textures[id] = new AlphaPalettedTexture(width, height, buffer);
			else if (type == 3)
				textures[id] = new ARGBTexture(width, height, buffer);

		}
	}

	public String toString()
	{
		return width + " X " + height + "	" + (opaque ? "+opaque":"-opaque") + "	" + (hasAlpha ? "+alpha":"-alpha");
	}

	public static void nullLoader()
	{
		loaded = null;
		textures = null;
		updateManager = null;
	}

	public static int getTotal()
	{
		return textures.length;
	}

	public boolean opaque;
	public boolean hasAlpha;
	public final int width;
	public final int height;
	private static boolean[] loaded;
	private static Texture[] textures;
	private static ResourceProvider updateManager;
	public static Texture NULL_TEXTURE = new Texture(64, 64);
}
