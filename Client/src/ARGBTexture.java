package src;

public final class ARGBTexture extends Texture
{
	public ARGBTexture(int width, int height, Buffer buffer)
	{
		super(width, height);
		int count = width * height;
		int[] pixels = this.pixels = new int[count];
		for (int i = 0; i != count; ++i)
		{
			int pixel = buffer.getInt();
			if ((pixel & 0xff000000) == 0)
				pixel = 0;

			int alpha = pixel & 0xff000000;
			if (alpha != 0xff000000)
			{
				opaque = false;
				if (alpha != 0)
					hasAlpha = true;

			}
			pixels[i] = pixel;
		}

	}

	public int getPixel(int i)
	{
		return pixels[i];
	}

	public String toString()
	{
		return "3	" + super.toString();
	}

	public final int[] pixels;
}
