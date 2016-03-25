package src;


public final class TextureDef
{
	private TextureDef()
	{
	}

	public static void unpackConfig(byte[] data)
	{
		Buffer buffer = new Buffer(data);
		int count = buffer.getUnsignedShort();
		textures = new TextureDef[count];
		for (int i = 0; i != count; ++i)
			if (buffer.getUnsigned() == 1)
				textures[i] = new TextureDef();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1223 = buffer.getUnsigned() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1204 = buffer.getUnsigned() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1205 = buffer.getUnsigned() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aByte1217 = buffer.get();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aByte1225 = buffer.get();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aByte1214 = buffer.get();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aByte1213 = buffer.get();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aShort1221 = (short) buffer.getUnsignedShort();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aByte1211 = buffer.get();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aByte1203 = buffer.get();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1222 = buffer.getUnsigned() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1216 = buffer.getUnsigned() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aByte1207 = buffer.get();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1212 = buffer.getUnsigned() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1210 = buffer.getUnsigned() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1215 = buffer.getUnsigned() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].anInt1202 = buffer.getUnsigned();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].anInt1206 = buffer.getInt();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].anInt1226 = buffer.getUnsigned();


	}

	public static void nullLoader()
	{
		textures = null;
	}

	public boolean aBoolean1223;
	public boolean aBoolean1204;
	public boolean aBoolean1205;
	public byte aByte1217;
	public byte aByte1225;
	public byte aByte1214;
	public byte aByte1213;
	public short aShort1221;
	public byte aByte1211;
	public byte aByte1203;
	public boolean aBoolean1222;
	public boolean aBoolean1216;
	public byte aByte1207;
	public boolean aBoolean1212;
	public boolean aBoolean1210;
	public boolean aBoolean1215;
	public int anInt1202;
	public int anInt1206;
	public int anInt1226;
	public static TextureDef[] textures;
}
