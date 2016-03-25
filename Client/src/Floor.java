package src;


import src.sign.signlink;

public final class Floor
{
	private Floor() {
		texture = -1;
		occlude = true;

		anInt3629 = -1;
		anInt3634 = 64;
		aBoolean3638 = true;
		anInt3633 = 8;
		anInt3641 = 127;
		textureId = -1;
		aBoolean3643 = false;
		anInt3646 = 1190717;
		anInt3631 = 0;
		anInt3629 = 0;
		anInt3632 = 512;
		aBoolean3636 = true;
	}

	public static void unpackConfig(byte[] data)
	{
		Buffer buffer = new Buffer(data);
		int count = buffer.getUnsignedShort();
		cache = new Floor[count];
		int index = buffer.currentOffset;
		for (int i = 0; i != count; ++i)
		{
			buffer.currentOffset = index;
			int size = buffer.getUnsignedShort();
			cache[i] = new Floor();
			if (size != 0)
				cache[i].readValues(buffer);

			index += size + 2;
		}
	}
	
	public static Floor overlays[];
	
	public static void unpackConfig(JagexArchive streamLoader)
	{
		//Stream stream = new Stream(streamLoader.getDataForName("floor_overlay.dat")); // rename the rest uhh sec PHONE isnt it JagexBuffer
		Buffer stream = new Buffer(FileOperations.ReadFile(signlink.getCacheLocation() + "floor_overlay.dat"));
		int overlaySize = stream.getUnsignedShort();
		if(overlays == null)
			overlays = new Floor[overlaySize];
		for(int j = 0; j < overlaySize; j++)
		{
			if(overlays[j] == null)
				overlays[j] = new Floor();
			overlays[j].readOverlays(stream);
				
		} //
	}

	public static int HSBtoRGB(float hue, float saturation, float brightness) {
		int r = 0, g = 0, b = 0;
		if (saturation == 0) {
			r = g = b = (int) (brightness * 255.0f + 0.5f);
		} else {
			float h = (hue - (float)Math.floor(hue)) * 6.0f;
			float f = h - (float)java.lang.Math.floor(h);
			float p = brightness * (1.0f - saturation);
			float q = brightness * (1.0f - saturation * f);
			float t = brightness * (1.0f - (saturation * (1.0f - f)));
			switch ((int) h) {
			case 0:
				r = (int) (brightness * 255.0f + 0.5f);
				g = (int) (t * 255.0f + 0.5f);
				b = (int) (p * 255.0f + 0.5f);
				break;
			case 1:
				r = (int) (q * 255.0f + 0.5f);
				g = (int) (brightness * 255.0f + 0.5f);
				b = (int) (p * 255.0f + 0.5f);
				break;
			case 2:
				r = (int) (p * 255.0f + 0.5f);
				g = (int) (brightness * 255.0f + 0.5f);
				b = (int) (t * 255.0f + 0.5f);
				break;
			case 3:
				r = (int) (p * 255.0f + 0.5f);
				g = (int) (q * 255.0f + 0.5f);
				b = (int) (brightness * 255.0f + 0.5f);
				break;
			case 4:
				r = (int) (t * 255.0f + 0.5f);
				g = (int) (p * 255.0f + 0.5f);
				b = (int) (brightness * 255.0f + 0.5f);
				break;
			case 5:
				r = (int) (brightness * 255.0f + 0.5f);
				g = (int) (p * 255.0f + 0.5f);
				b = (int) (q * 255.0f + 0.5f);
				break;
			}
		}
		return (r << 16) | (g << 8) | (b << 0);
	}

	public static void RGBtoHSB(int r, int g, int b, float[] hsbvals) {
		float hue, saturation, brightness;
		int cmax = (r > g) ? r : g;
		if (b > cmax) cmax = b;
		int cmin = (r < g) ? r : g;
		if (b < cmin) cmin = b;

		brightness = ((float) cmax) / 255.0f;
		if (cmax != 0)
			saturation = ((float) (cmax - cmin)) / ((float) cmax);
		else
			saturation = 0;
		if (saturation == 0)
			hue = 0;
		else {
			float redc = ((float) (cmax - r)) / ((float) (cmax - cmin));
			float greenc = ((float) (cmax - g)) / ((float) (cmax - cmin));
			float bluec = ((float) (cmax - b)) / ((float) (cmax - cmin));
			if (r == cmax)
				hue = bluec - greenc;
			else if (g == cmax)
				hue = 2.0f + redc - bluec;
			else
				hue = 4.0f + greenc - redc;
			hue = hue / 6.0f;
			if (hue < 0)
				hue = hue + 1.0f;
		}
		hsbvals[0] = hue;
		hsbvals[1] = saturation;
		hsbvals[2] = brightness;
	}

	//private static final float[] HSB = new float[3];
	
	public void readOverlays(Buffer buffer) { 
		while(true) {
			int opcode = buffer.getUnsigned();// ?
			if(opcode == 0) {
				return;
			} else if(opcode == 1) {
				anInt3631 = method2582(buffer.getMedium());
			} else if(opcode == 2) {
				textureId = buffer.getUnsigned();
				if (textureId == 65535)
					textureId = -1;
			} else if(opcode == 3) {
				textureId = buffer.getSignedShort();
			} else if(opcode == 5) {
				aBoolean3636 = false;
			} else if(opcode == 7) {
				anInt3629 = method2582(buffer.getMedium());
			} else if(opcode == 8) {

			} else if(opcode == 9) {
				anInt3632 = buffer.getSignedShort() << 2;
			} else if(opcode == 10) {
				aBoolean3638 = false;
			} else if (opcode == 11) {
				anInt3633 = buffer.getUnsigned();
			} else if (opcode == 12) {
				aBoolean3643 = true;
			} else if (opcode == 13) {
				anInt3646 = buffer.getMedium();
			} else if (opcode == 14) {
				anInt3634 = buffer.getUnsigned() << 2;
			} else if (opcode == 16) {
				anInt3641 = buffer.getUnsigned();
			} else {
		    	System.out.println("[FloorDef: Overlay] Unknown opcode: " + opcode);
		    }
		}
	}
	
	private static int method2582(int color)
	{
		return color != 0xff00ff ? method1075(color) : -1;
	}
	
	private static int method1075(int color)
	{
		double r = (double) ((color >>> 16) & 0xff) / 256.0D;
		double g = (double) ((color >>> 8) & 0xff) / 256.0D;
		double b = (double) (color & 0xff) / 256.0D;
		double d_10_ = r;
		if (d_10_ > g)
			d_10_ = g;

		if (d_10_ > b)
			d_10_ = b;

		double d_11_ = r;
		if (g > d_11_)
			d_11_ = g;

		if (b > d_11_)
			d_11_ = b;

		double d_12_ = 0.0;
		double d_13_ = 0.0;
		double d_14_ = (d_10_ + d_11_) / 2.0D;
		if (d_11_ != d_10_)
		{
			if (d_14_ < 0.5D)
				d_13_ = (d_11_ - d_10_) / (d_10_ + d_11_);

			if (d_11_ != r)
			{
				if (d_11_ == g)
					d_12_ = (b - r) / (d_11_ - d_10_) + 2.0D;
				else if (d_11_ == b)
					d_12_ = (r - g) / (d_11_ - d_10_) + 4.0D;

			}
			else
				d_12_ = (g - b) / (d_11_ - d_10_);

			if (d_14_ >= 0.5D)
				d_13_ = (d_11_ - d_10_) / (2.0D - d_10_ - d_11_);

		}
		d_12_ /= 6.0D;
		int i_15_ = (int) (d_12_ * 256.0D);
		int i_16_ = (int) (d_13_ * 256.0D);
		int i_17_ = (int) (d_14_ * 256.0D);
		if (i_16_ < 0)
			i_16_ = 0;
		else if (i_16_ > 0xff)
			i_16_ = 0xff;

		if (i_17_ < 0)
			i_17_ = 0;
		else if (i_17_ > 0xff)
			i_17_ = 0xff;

		if (i_17_ > 242)
			i_16_ >>= 4;
		else if (i_17_ > 217)
			i_16_ >>= 3;
		else if (i_17_ > 192)
			i_16_ >>= 2;
		else if (i_17_ > 179)
			i_16_ >>= 1;

		return (i_17_ >> 1) + ((i_16_ >> 5 << 7) + ((i_15_ & 0xff) >> 2 << 10));
	}
	
	protected int anInt3629;
	protected int anInt3634;
	protected boolean aBoolean3638;
	protected int anInt3633;
	protected int anInt3641;
	protected int textureId;
	protected boolean aBoolean3643;
	protected int anInt3646;
	protected int anInt3632;
	protected int anInt3631;
	protected boolean aBoolean3636;

	public void readValues(Buffer buffer) // underlays
	{
		while (true)
		{
			int opcode = buffer.getUnsigned();
			if (opcode == 0)
				break;

			if (opcode == 1)
			{
				color = buffer.getMedium();
				/*RGBtoHSB(color >>> 16, (color >>> 8) & 0xff, color & 0xff, HSB);
				float h = HSB[0];
				float s = HSB[1];
				float b = HSB[2];
				s *= 1.5F;
				if (h < 0.0F)
					h = 0.0F;
				else if (h > 1.0F)
					h = 1.0F;

				if (s < 0.0F)
					s = 0.0F;
				else if (s > 1.0F)
					s = 1.0F;

				if (b < 0.0F)
					b = 0.0F;
				else if (b > 1.0F)
					b = 1.0F;

				color = HSBtoRGB(h, s, b);*/
			}
			else if (opcode == 2)
			{
				texture = buffer.getUnsignedShort();
				if (texture == 0xffff)
					texture = -1;

			}
			else if (opcode == 3)
				anInt2894 = buffer.getUnsignedShort() << 2;
			else if (opcode == 4)
				aBoolean2896 = false;
			else if (opcode == 5)
				occlude = false;

			else
			{
				System.out.println("[Floor] Unknown opcode: " + opcode);
				break;
			}
		}
		//Looks WAY better....
		/*if (texture == 594)
		{
			texture = 1;//505
			color = 0x5d7397;//0x897b5f;
		}
		else if (texture == 512)
		{
			texture = 505;
			color = 0x897b5f;
		}*/
		method262(color);
	}

	private void method262(int i)
	{
		double d = (double) (i >> 16 & 0xff) / 256D;
		double d1 = (double) (i >> 8 & 0xff) / 256D;
		double d2 = (double) (i & 0xff) / 256D;

		double d3 = d;
		if (d1 < d3)
			d3 = d1;

		if (d2 < d3)
			d3 = d2;

		double d4 = d;
		if (d1 > d4)
			d4 = d1;

		if (d2 > d4)
			d4 = d2;

		double d5 = 0.0D;
		double d6 = 0.0D;
		double d7 = (d3 + d4) / 2D;
		if (d3 != d4)
		{
			if (d7 < 0.5D)
				d6 = (d4 - d3) / (d4 + d3);

			if (d7 >= 0.5D)
				d6 = (d4 - d3) / (2D - d4 - d3);

			if (d == d4)
				d5 = (d1 - d2) / (d4 - d3);
			else if (d1 == d4)
				d5 = 2D + (d2 - d) / (d4 - d3);
			else if (d2 == d4)
				d5 = 4D + (d - d1) / (d4 - d3);

		}
		d5 /= 6D;
		anInt394 = (int) (d5 * 256D);
		saturation = (int) (d6 * 256D);
		lightness = (int) (d7 * 256D);
		if (saturation < 0)
			saturation = 0;
		else if (saturation > 255)
			saturation = 255;

		if (lightness < 0)
			lightness = 0;
		else if (lightness > 255)
			lightness = 255;

		if (d7 > 0.5D)
			hueDivisor = (int) ((1.0D - d7) * d6 * 512D);
		else
			hueDivisor = (int) (d7 * d6 * 512D);

		if (hueDivisor < 1)
			hueDivisor = 1;

		hue = (int) (d5 * (double) hueDivisor);
		int k = anInt394;
		if (k < 0)
			k = 0;
		else if (k > 255)
			k = 255;

		int l = saturation;
		if (l < 0)
			l = 0;
		else if (l > 255)
			l = 255;

		int i1 = lightness;
		if (i1 < 0)
			i1 = 0;
		else if (i1 > 255)
			i1 = 255;

		anInt399 = method263(k, l, i1);
	}

	private int method263(int i, int j, int k)
	{
		if (k > 179)
			j >>= 1;
		if (k > 192)
			j >>= 1;
		if (k > 217)
			j >>= 1;
		if (k > 243)
			j >>= 1;
		return ((i >> 2) << 10) + (j / 32 << 7) + (k >> 1);
	}

	/*private final void method262(int i)
	{
		double d = (double) ((i >>> 16) & 0xff) / 256.0D;
		double d_6_ = (double) ((i >>> 8) & 0xff) / 256.0D;
		double d_7_ = (double) (i & 0xff) / 256.0D;
		double d_8_ = d;
		if (d_8_ > d_6_)
			d_8_ = d_6_;

		if (d_8_ > d_7_)
			d_8_ = d_7_;

		double d_9_ = d;
		if (d_6_ > d_9_)
			d_9_ = d_6_;

		if (d_7_ > d_9_)
			d_9_ = d_7_;

		double d_10_ = 0.0D;
		double d_11_ = 0.0D;
		double d_12_ = (d_8_ + d_9_) / 2.0D;
		if (d_8_ != d_9_)
		{
			if (d_12_ < 0.5D)
				d_11_ = (d_9_ - d_8_) / (d_8_ + d_9_);

			if (d == d_9_)
				d_10_ = (d_6_ - d_7_) / (d_9_ - d_8_);
			else if (d_9_ == d_6_)
				d_10_ = (d_7_ - d) / (d_9_ - d_8_) + 2.0D;
			else if (d_9_ == d_7_)
				d_10_ = 4.0D + (d - d_6_) / (d_9_ - d_8_);

			if (d_12_ >= 0.5D) 
				d_11_ = (d_9_ - d_8_) / (2.0D - d_9_ - d_8_);

		}
		saturation = (int) (256.0D * d_11_);
		lightness = (int) (256.0D * d_12_);
		d_10_ /= 6.0D;
		if (d_12_ <= 0.5D)
			hueDivisor = (int) (512.0D * ((1.0D - d_12_) * d_11_));
		else
			hueDivisor = (int) (512.0D * (d_12_ * d_11_));

		if (lightness < 0)
			lightness = 0;
		else if (lightness > 0xff)
			lightness = 0xff;

		if (saturation < 0)
			saturation = 0;
		else if (saturation > 0xff)
			saturation = 0xff;

		if (hueDivisor < 1)
			hueDivisor = 1;

		hue = (int) (d_10_ * (double) hueDivisor);
		anInt399 = method263(anInt394, saturation, lightness);
	}*/

	public static void nullLoader()
	{
		cache = null;
	}

	public int saturation;
	public int lightness;
	public int hueDivisor;

	public int color;
	public int texture;

	public static Floor cache[];
	public boolean occlude;
	public int anInt394;
	public int hue;
	public int anInt399;
	public int anInt2894;
	public boolean aBoolean2896;
}
