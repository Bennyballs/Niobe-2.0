package src;

public class Rasterizer extends DrawingArea
{
	public static void nullLoader()
	{
		shadowDecay = null;
		SINE = null;
		COSINE = null;
		lineOffsets = null;
		texelArrayPool = null;
		texelCache = null;
		textureLastUsed = null;
		hslToRgbLookupTable = null;
		brightness = 1.0F;
	}

	public static void nullLoaderSafe()
	{
		lineOffsets = null;
		texelArrayPool = null;
		texelCache = null;
		textureLastUsed = null;
		OFFSETS_512_334 = null;
		OFFSETS_765_503 = null;
		brightness = 1.0F;
	}

	public static void resetConstCache()
	{
		OFFSETS_512_334 = null;
		OFFSETS_765_503 = null;
	}

	public static void initTextures(int count)
	{
		loadedTextureCount = count;
		textureLastUsed = new int[count];
		texelCache = new int[count][];
	}

	private static int[] OFFSETS_512_334 = null;
	private static int[] OFFSETS_765_503 = null;

	public static int[] getOffsets(int j, int k)
	{
		if (j == 512 && k == 334 && OFFSETS_512_334 != null)
			return OFFSETS_512_334;

		if (j == 765 + 1 && k == 503 && OFFSETS_765_503 != null)
			return OFFSETS_765_503;

		int[] t = new int[k];
		for (int l = 0; l < k; l++)
			t[l] = j * l;

		if (j == 512 && k == 334)
			OFFSETS_512_334 = t;

		if (j == 765 + 1 && k == 503)
			OFFSETS_765_503 = t;

		return t;
	}

	public boolean isValid()
	{
		return texelArrayPool != null && texelCache != null;
	}

	public static void setDefaultBounds() {
		setBounds(width, height);
	}

	public static void setBounds(int width, int height) {
		lineOffsets = getOffsets(width, height);
		centerX = width / 2;
		centerY = height / 2;
	}

	public static void clearTextureCache() {
		texelArrayPool = null;
		for (int i = 0; i < loadedTextureCount; i++) {
			texelCache[i] = null;
		}

	}

	public static void resetTextures() {
		if (texelArrayPool == null) {
			textureTexelPoolPointer = 50;//was parameter
			texelArrayPool = new int[textureTexelPoolPointer][0x10000];
			for (int k = 0; k < loadedTextureCount; k++)
				texelCache[k] = null;


		}
	}

	public static void resetTexture(int textureId) {
		if (texelCache[textureId] == null) {
			return;
		}
		texelArrayPool[textureTexelPoolPointer++] = texelCache[textureId];
		texelCache[textureId] = null;
	}

	private static int[] getTexturePixels(int textureId) {
		Texture texture = Texture.get(textureId);
		if (texture == null)
			return null;

		textureLastUsed[textureId] = textureGetCount++;
		if (texelCache[textureId] != null)
			return texelCache[textureId];

		int texels[];
		//Start of mem management code
		if (textureTexelPoolPointer > 0) {	//Freed texture data arrays available
			texels = texelArrayPool[--textureTexelPoolPointer];
			texelArrayPool[textureTexelPoolPointer] = null;
		} else {   //No freed texture data arrays available, recycle least used texture's array
			int lastUsed = 0;
			int target = -1;
			for (int i = 0; i < loadedTextureCount; i++) {
				if (texelCache[i] != null && (textureLastUsed[i] < lastUsed || target == -1)) {
					lastUsed = textureLastUsed[i];
					target = i;
				}
			}

			texels = texelCache[target];
			texelCache[target] = null;
		}
		texelCache[textureId] = texels;
		//End of mem management code
		if (texture.width == 64)
			for (int y = 0; y < 128; y++)
				for (int x = 0; x < 128; x++)
					texels[x + (y << 7)] = texture.getPixel((x >> 1) + ((y >> 1) << 6));


		else
			for (int texelPtr = 0; texelPtr < 16384; texelPtr++)
				texels[texelPtr] = texture.getPixel(texelPtr);


		TextureDef def = textureId >= 0 && textureId < TextureDef.textures.length ? TextureDef.textures[textureId]:null;
		int blendType = def != null ? def.anInt1226 : 0;
		if (blendType != 1 && blendType != 2)
			blendType = 0;

		for (int texelPtr = 0; texelPtr < 16384; texelPtr++) {
			int texel = texels[texelPtr];
			int alpha;
			if (blendType == 2)
				alpha = texel >>> 24;
			else if (blendType == 1)
				alpha = texel != 0 ? 0xff:0;

			else
			{
				alpha = texel >>> 24;
				if (def != null && !def.aBoolean1223)
					alpha /= 5;

			}
			texel &= 0xffffff;
			texel = adjustBrightnessLinear(texel, 179);
			texel = adjustBrightness(texel, brightness);
			texel &= 0xf8f8ff;
			texels[texelPtr] = texel | (alpha << 24);
			texels[16384 + texelPtr] = ((texel - (texel >>> 3)) & 0xf8f8ff) | (alpha << 24);
			texels[32768 + texelPtr] = ((texel - (texel >>> 2)) & 0xf8f8ff) | (alpha << 24);
			texels[49152 + texelPtr] = ((texel - (texel >>> 2) - (texel >>> 3)) & 0xf8f8ff) | (alpha << 24);
		}

		return texels;
	}
 
	public static void calculatePalette(float brightness)
	{
		brightness += Math.random() * 0.03F - 0.015F;
		//if (Rasterizer.brightness == brightness)
		//	return;

		Rasterizer.brightness = brightness;
		int hsl = 0;
		for (int k = 0; k < 512; k++) {
			float d1 = (float) (k / 8) / 64F + 0.0078125F;
			float d2 = (float) (k & 7) / 8F + 0.0625F;
			for (int k1 = 0; k1 < 128; k1++) {
				float d3 = (float) k1 / 128F;
				float r = d3;
				float g = d3;
				float b = d3;
				if (d2 != 0.0F) {
					float d7;
					if (d3 < 0.5F) {
						d7 = d3 * (1.0F + d2);
					} else {
						d7 = (d3 + d2) - d3 * d2;
					}
					float d8 = 2F * d3 - d7;
					float d9 = d1 + 1F / 3F;
					if (d9 > 1.0F) {
						d9--;
					}
					float d10 = d1;
					float d11 = d1 - 1F / 3F;
					if (d11 < 0.0F) {
						d11++;
					}
					if (6F * d9 < 1.0F) {
						r = d8 + (d7 - d8) * 6F * d9;
					} else if (2F * d9 < 1.0F) {
						r = d7;
					} else if (3F * d9 < 2F) {
						r = d8 + (d7 - d8) * ((2F / 3F) - d9) * 6F;
					} else {
						r = d8;
					}
					if (6F * d10 < 1.0F) {
						g = d8 + (d7 - d8) * 6F * d10;
					} else if (2F * d10 < 1.0F) {
						g = d7;
					} else if (3F * d10 < 2F) {
						g = d8 + (d7 - d8) * ((2F / 3F) - d10) * 6F;
					} else {
						g = d8;
					}
					if (6F * d11 < 1.0F) {
						b = d8 + (d7 - d8) * 6F * d11;
					} else if (2F * d11 < 1.0F) {
						b = d7;
					} else if (3F * d11 < 2F) {
						b = d8 + (d7 - d8) * ((2F / 3F) - d11) * 6F;
					} else {
						b = d8;
					}
				}
				int rgb = ((int) ((float) Math.pow((double) r, (double) brightness) * 256F) << 16) + ((int) ((float) Math.pow((double) g, (double) brightness) * 256F) << 8) + (int) ((float) Math.pow((double) b, (double) brightness) * 256F);
				if (rgb == 0)
					rgb = 1;

				hslToRgbLookupTable[hsl++] = rgb;
			}

		}

		for (int textureId = 0; textureId != loadedTextureCount; ++textureId)
			resetTexture(textureId);

	}

	private static float brightness = 1.0F;

	private static int adjustBrightness(int rgb, float brightness) {
		return ((int) ((float) Math.pow((double) ((float) (rgb >>> 16) / 256.0F), (double) brightness) * 256.0F) << 16) | 
			((int) ((float) Math.pow((double) ((float) ((rgb >>> 8) & 0xff) / 256.0F), (double) brightness) * 256.0F) << 8) | 
			(int) ((float) Math.pow((double) ((float) (rgb & 0xff) / 256.0F), (double) brightness) * 256.0F);
	}


	private static int adjustBrightnessLinear(int rgb, int factor)
	{
		return ((((rgb >>> 16) * factor) & 0xff00) << 8) | 
			((((rgb >>> 8) & 0xff) * factor) & 0xff00) | 
			(((rgb & 0xff) * factor) >> 8);
	}

	public static void drawShadedTriangle(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int z_a, int z_b, int z_c) {
		++triangles;
		int x_a_off = 0;
		int z_a_off = 0;
		if (y_b != y_a) {
			x_a_off = (x_b - x_a << 16) / (y_b - y_a);
			z_a_off = (z_b - z_a << 15) / (y_b - y_a);
		}
		int x_b_off = 0;
		int z_b_off = 0;
		if (y_c != y_b) {
			x_b_off = (x_c - x_b << 16) / (y_c - y_b);
			z_b_off = (z_c - z_b << 15) / (y_c - y_b);
		}
		int x_c_off = 0;
		int z_c_off = 0;
		if (y_c != y_a) {
			x_c_off = (x_a - x_c << 16) / (y_a - y_c);
			z_c_off = (z_a - z_c << 15) / (y_a - y_c);
		}
		if (y_a <= y_b && y_a <= y_c) {
			if (y_a >= DrawingArea.bottomY) {
				return;
			}
			if (y_b > DrawingArea.bottomY) {
				y_b = DrawingArea.bottomY;
			}
			if (y_c > DrawingArea.bottomY) {
				y_c = DrawingArea.bottomY;
			}
			if (y_b < y_c) {
				x_c = x_a <<= 16;
				z_c = z_a <<= 15;
				if (y_a < 0) {
					x_c -= x_c_off * y_a;
					x_a -= x_a_off * y_a;
					z_c -= z_c_off * y_a;
					z_a -= z_a_off * y_a;
					y_a = 0;
				}
				x_b <<= 16;
				z_b <<= 15;
				if (y_b < 0) {
					x_b -= x_b_off * y_b;
					z_b -= z_b_off * y_b;
					y_b = 0;
				}
				if (y_a != y_b && x_c_off < x_a_off || y_a == y_b && x_c_off > x_b_off) {
					y_c -= y_b;
					y_b -= y_a;
					for (y_a = lineOffsets[y_a]; --y_b >= 0; y_a += width) {
						drawShadedLine(pixels, y_a, x_c >> 16, x_a >> 16, z_c >> 7, z_a >> 7);
						x_c += x_c_off;
						x_a += x_a_off;
						z_c += z_c_off;
						z_a += z_a_off;
					}

					while (--y_c >= 0) {
						drawShadedLine(pixels, y_a, x_c >> 16, x_b >> 16, z_c >> 7, z_b >> 7);
						x_c += x_c_off;
						x_b += x_b_off;
						z_c += z_c_off;
						z_b += z_b_off;
						y_a += width;
					}
					return;
				}
				y_c -= y_b;
				y_b -= y_a;
				for (y_a = lineOffsets[y_a]; --y_b >= 0; y_a += width) {
					drawShadedLine(pixels, y_a, x_a >> 16, x_c >> 16, z_a >> 7, z_c >> 7);
					x_c += x_c_off;
					x_a += x_a_off;
					z_c += z_c_off;
					z_a += z_a_off;
				}

				while (--y_c >= 0) {
					drawShadedLine(pixels, y_a, x_b >> 16, x_c >> 16, z_b >> 7, z_c >> 7);
					x_c += x_c_off;
					x_b += x_b_off;
					z_c += z_c_off;
					z_b += z_b_off;
					y_a += width;
				}
				return;
			}
			x_b = x_a <<= 16;
			z_b = z_a <<= 15;
			if (y_a < 0) {
				x_b -= x_c_off * y_a;
				x_a -= x_a_off * y_a;
				z_b -= z_c_off * y_a;
				z_a -= z_a_off * y_a;
				y_a = 0;
			}
			x_c <<= 16;
			z_c <<= 15;
			if (y_c < 0) {
				x_c -= x_b_off * y_c;
				z_c -= z_b_off * y_c;
				y_c = 0;
			}
			if (y_a != y_c && x_c_off < x_a_off || y_a == y_c && x_b_off > x_a_off) {
				y_b -= y_c;
				y_c -= y_a;
				for (y_a = lineOffsets[y_a]; --y_c >= 0; y_a += width) {
					drawShadedLine(pixels, y_a, x_b >> 16, x_a >> 16, z_b >> 7, z_a >> 7);
					x_b += x_c_off;
					x_a += x_a_off;
					z_b += z_c_off;
					z_a += z_a_off;
				}

				while (--y_b >= 0) {
					drawShadedLine(pixels, y_a, x_c >> 16, x_a >> 16, z_c >> 7, z_a >> 7);
					x_c += x_b_off;
					x_a += x_a_off;
					z_c += z_b_off;
					z_a += z_a_off;
					y_a += width;
				}
				return;
			}
			y_b -= y_c;
			y_c -= y_a;
			for (y_a = lineOffsets[y_a]; --y_c >= 0; y_a += width) {
				drawShadedLine(pixels, y_a, x_a >> 16, x_b >> 16, z_a >> 7, z_b >> 7);
				x_b += x_c_off;
				x_a += x_a_off;
				z_b += z_c_off;
				z_a += z_a_off;
			}

			while (--y_b >= 0) {
				drawShadedLine(pixels, y_a, x_a >> 16, x_c >> 16, z_a >> 7, z_c >> 7);
				x_c += x_b_off;
				x_a += x_a_off;
				z_c += z_b_off;
				z_a += z_a_off;
				y_a += width;
			}
			return;
		}
		if (y_b <= y_c) {
			if (y_b >= DrawingArea.bottomY) {
				return;
			}
			if (y_c > DrawingArea.bottomY) {
				y_c = DrawingArea.bottomY;
			}
			if (y_a > DrawingArea.bottomY) {
				y_a = DrawingArea.bottomY;
			}
			if (y_c < y_a) {
				x_a = x_b <<= 16;
				z_a = z_b <<= 15;
				if (y_b < 0) {
					x_a -= x_a_off * y_b;
					x_b -= x_b_off * y_b;
					z_a -= z_a_off * y_b;
					z_b -= z_b_off * y_b;
					y_b = 0;
				}
				x_c <<= 16;
				z_c <<= 15;
				if (y_c < 0) {
					x_c -= x_c_off * y_c;
					z_c -= z_c_off * y_c;
					y_c = 0;
				}
				if (y_b != y_c && x_a_off < x_b_off || y_b == y_c && x_a_off > x_c_off) {
					y_a -= y_c;
					y_c -= y_b;
					for (y_b = lineOffsets[y_b]; --y_c >= 0; y_b += width) {
						drawShadedLine(pixels, y_b, x_a >> 16, x_b >> 16, z_a >> 7, z_b >> 7);
						x_a += x_a_off;
						x_b += x_b_off;
						z_a += z_a_off;
						z_b += z_b_off;
					}

					while (--y_a >= 0) {
						drawShadedLine(pixels, y_b, x_a >> 16, x_c >> 16, z_a >> 7, z_c >> 7);
						x_a += x_a_off;
						x_c += x_c_off;
						z_a += z_a_off;
						z_c += z_c_off;
						y_b += width;
					}
					return;
				}
				y_a -= y_c;
				y_c -= y_b;
				for (y_b = lineOffsets[y_b]; --y_c >= 0; y_b += width) {
					drawShadedLine(pixels, y_b, x_b >> 16, x_a >> 16, z_b >> 7, z_a >> 7);
					x_a += x_a_off;
					x_b += x_b_off;
					z_a += z_a_off;
					z_b += z_b_off;
				}

				while (--y_a >= 0) {
					drawShadedLine(pixels, y_b, x_c >> 16, x_a >> 16, z_c >> 7, z_a >> 7);
					x_a += x_a_off;
					x_c += x_c_off;
					z_a += z_a_off;
					z_c += z_c_off;
					y_b += width;
				}
				return;
			}
			x_c = x_b <<= 16;
			z_c = z_b <<= 15;
			if (y_b < 0) {
				x_c -= x_a_off * y_b;
				x_b -= x_b_off * y_b;
				z_c -= z_a_off * y_b;
				z_b -= z_b_off * y_b;
				y_b = 0;
			}
			x_a <<= 16;
			z_a <<= 15;
			if (y_a < 0) {
				x_a -= x_c_off * y_a;
				z_a -= z_c_off * y_a;
				y_a = 0;
			}
			if (x_a_off < x_b_off) {
				y_c -= y_a;
				y_a -= y_b;
				for (y_b = lineOffsets[y_b]; --y_a >= 0; y_b += width) {
					drawShadedLine(pixels, y_b, x_c >> 16, x_b >> 16, z_c >> 7, z_b >> 7);
					x_c += x_a_off;
					x_b += x_b_off;
					z_c += z_a_off;
					z_b += z_b_off;
				}

				while (--y_c >= 0) {
					drawShadedLine(pixels, y_b, x_a >> 16, x_b >> 16, z_a >> 7, z_b >> 7);
					x_a += x_c_off;
					x_b += x_b_off;
					z_a += z_c_off;
					z_b += z_b_off;
					y_b += width;
				}
				return;
			}
			y_c -= y_a;
			y_a -= y_b;
			for (y_b = lineOffsets[y_b]; --y_a >= 0; y_b += width) {
				drawShadedLine(pixels, y_b, x_b >> 16, x_c >> 16, z_b >> 7, z_c >> 7);
				x_c += x_a_off;
				x_b += x_b_off;
				z_c += z_a_off;
				z_b += z_b_off;
			}

			while (--y_c >= 0) {
				drawShadedLine(pixels, y_b, x_b >> 16, x_a >> 16, z_b >> 7, z_a >> 7);
				x_a += x_c_off;
				x_b += x_b_off;
				z_a += z_c_off;
				z_b += z_b_off;
				y_b += width;
			}
			return;
		}
		if (y_c >= DrawingArea.bottomY) {
			return;
		}
		if (y_a > DrawingArea.bottomY) {
			y_a = DrawingArea.bottomY;
		}
		if (y_b > DrawingArea.bottomY) {
			y_b = DrawingArea.bottomY;
		}
		if (y_a < y_b) {
			x_b = x_c <<= 16;
			z_b = z_c <<= 15;
			if (y_c < 0) {
				x_b -= x_b_off * y_c;
				x_c -= x_c_off * y_c;
				z_b -= z_b_off * y_c;
				z_c -= z_c_off * y_c;
				y_c = 0;
			}
			x_a <<= 16;
			z_a <<= 15;
			if (y_a < 0) {
				x_a -= x_a_off * y_a;
				z_a -= z_a_off * y_a;
				y_a = 0;
			}
			if (x_b_off < x_c_off) {
				y_b -= y_a;
				y_a -= y_c;
				for (y_c = lineOffsets[y_c]; --y_a >= 0; y_c += width) {
					drawShadedLine(pixels, y_c, x_b >> 16, x_c >> 16, z_b >> 7, z_c >> 7);
					x_b += x_b_off;
					x_c += x_c_off;
					z_b += z_b_off;
					z_c += z_c_off;
				}

				while (--y_b >= 0) {
					drawShadedLine(pixels, y_c, x_b >> 16, x_a >> 16, z_b >> 7, z_a >> 7);
					x_b += x_b_off;
					x_a += x_a_off;
					z_b += z_b_off;
					z_a += z_a_off;
					y_c += width;
				}
				return;
			}
			y_b -= y_a;
			y_a -= y_c;
			for (y_c = lineOffsets[y_c]; --y_a >= 0; y_c += width) {
				drawShadedLine(pixels, y_c, x_c >> 16, x_b >> 16, z_c >> 7, z_b >> 7);
				x_b += x_b_off;
				x_c += x_c_off;
				z_b += z_b_off;
				z_c += z_c_off;
			}

			while (--y_b >= 0) {
				drawShadedLine(pixels, y_c, x_a >> 16, x_b >> 16, z_a >> 7, z_b >> 7);
				x_b += x_b_off;
				x_a += x_a_off;
				z_b += z_b_off;
				z_a += z_a_off;
				y_c += width;
			}
			return;
		}
		x_a = x_c <<= 16;
		z_a = z_c <<= 15;
		if (y_c < 0) {
			x_a -= x_b_off * y_c;
			x_c -= x_c_off * y_c;
			z_a -= z_b_off * y_c;
			z_c -= z_c_off * y_c;
			y_c = 0;
		}
		x_b <<= 16;
		z_b <<= 15;
		if (y_b < 0) {
			x_b -= x_a_off * y_b;
			z_b -= z_a_off * y_b;
			y_b = 0;
		}
		if (x_b_off < x_c_off) {
			y_a -= y_b;
			y_b -= y_c;
			for (y_c = lineOffsets[y_c]; --y_b >= 0; y_c += width) {
				drawShadedLine(pixels, y_c, x_a >> 16, x_c >> 16, z_a >> 7, z_c >> 7);
				x_a += x_b_off;
				x_c += x_c_off;
				z_a += z_b_off;
				z_c += z_c_off;
			}

			while (--y_a >= 0) {
				drawShadedLine(pixels, y_c, x_b >> 16, x_c >> 16, z_b >> 7, z_c >> 7);
				x_b += x_a_off;
				x_c += x_c_off;
				z_b += z_a_off;
				z_c += z_c_off;
				y_c += width;
			}
			return;
		}
		y_a -= y_b;
		y_b -= y_c;
		for (y_c = lineOffsets[y_c]; --y_b >= 0; y_c += width) {
			drawShadedLine(pixels, y_c, x_c >> 16, x_a >> 16, z_c >> 7, z_a >> 7);
			x_a += x_b_off;
			x_c += x_c_off;
			z_a += z_b_off;
			z_c += z_c_off;
		}

		while (--y_a >= 0) {
			drawShadedLine(pixels, y_c, x_c >> 16, x_b >> 16, z_c >> 7, z_b >> 7);
			x_b += x_a_off;
			x_c += x_c_off;
			z_b += z_a_off;
			z_c += z_c_off;
			y_c += width;
		}
	}

	private static void drawShadedLine(int dest[], int dest_off, int startX, int endX, int colorIndex, int grad)
	{
		//fixed the vertex blender
		int color;
		int loops;
		int off = 0;
		if(restrict_edges)
		{
			if(endX > DrawingArea.centerX)
				endX = DrawingArea.centerX;
			if(startX < 0)
			{
				colorIndex -= startX * off;
				startX = 0;
			}
		}
		if(startX >= endX)
			return;
		dest_off += startX;// - 1;//causes crashes if left as orig
		colorIndex += off * startX;
		int tmp;
			if(notTextured)
			{
				loops = endX - startX >> 2;
				//added code to get the grad working again
				if(loops > 0)
					off = (grad - colorIndex) * shadowDecay[loops] >> 15;
				else
					off = 0;

				if(alpha == 0)
				{
					if(loops > 0)
						do
						{
							color = hslToRgbLookupTable[colorIndex>>8];
							colorIndex += off;
							dest[dest_off++] = color;
							dest[dest_off++] = color;
							dest[dest_off++] = color;
							dest[dest_off++] = color;
						} while(--loops > 0);
					loops = endX - startX & 3;
					if(loops > 0)
					{
						color = hslToRgbLookupTable[colorIndex>>8];
						do
						{
							dest[dest_off++] = color;
						} while(--loops > 0);
						return;
					}
				} else
				{
					int src_alpha = alpha;
					int dest_alpha = 256 - alpha;
					if(loops > 0)
						do
						{
							color = hslToRgbLookupTable[colorIndex>>8];
							colorIndex += off;
							color = ((color & 0xff00ff) * dest_alpha >> 8 & 0xff00ff) + ((color & 0xff00) * dest_alpha >> 8 & 0xff00);
							tmp = dest[dest_off];
							dest[dest_off++] = color + ((tmp & 0xff00ff) * src_alpha >> 8 & 0xff00ff) + ((tmp & 0xff00) * src_alpha >> 8 & 0xff00);
							tmp = dest[dest_off];
							dest[dest_off++] = color + ((tmp & 0xff00ff) * src_alpha >> 8 & 0xff00ff) + ((tmp & 0xff00) * src_alpha >> 8 & 0xff00);
							tmp = dest[dest_off];
							dest[dest_off++] = color + ((tmp & 0xff00ff) * src_alpha >> 8 & 0xff00ff) + ((tmp & 0xff00) * src_alpha >> 8 & 0xff00);
							tmp = dest[dest_off];
							dest[dest_off++] = color + ((tmp & 0xff00ff) * src_alpha >> 8 & 0xff00ff) + ((tmp & 0xff00) * src_alpha >> 8 & 0xff00);

						} while(--loops > 0);
					loops = endX - startX & 3;
					if(loops > 0)
					{
						color = hslToRgbLookupTable[colorIndex>>8];
						color = ((color & 0xff00ff) * dest_alpha >> 8 & 0xff00ff) + ((color & 0xff00) * dest_alpha >> 8 & 0xff00);
						do
						{
							tmp = dest[dest_off];
							dest[dest_off++] = color + ((tmp & 0xff00ff) * src_alpha >> 8 & 0xff00ff) + ((tmp & 0xff00) * src_alpha >> 8 & 0xff00);

						} while(--loops > 0);
					}
				}
				return;
			}
			loops = endX - startX;
			if(alpha == 0)
			{
				do
				{

						dest[dest_off++] = hslToRgbLookupTable[colorIndex>>8];

					colorIndex += off;
				} while(--loops > 0);
				return;
			}
			int src_alpha = alpha;
			int dest_alpha = 256 - alpha;
			do
			{

					color = hslToRgbLookupTable[colorIndex>>8];
					color = ((color & 0xff00ff) * dest_alpha >> 8 & 0xff00ff) + ((color & 0xff00) * dest_alpha >> 8 & 0xff00);
					tmp = dest[dest_off];
					dest[dest_off++] = color + ((tmp & 0xff00ff) * src_alpha >> 8 & 0xff00ff) + ((tmp & 0xff00) * src_alpha >> 8 & 0xff00);


				colorIndex += off;
			} while(--loops > 0);
			return;
	}

	public static int triangles = 0;


	public static void drawFlatTriangle(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int color) {
		++triangles;
		int x_a_off = 0;
		if (y_b != y_a) {
			x_a_off = (x_b - x_a << 16) / (y_b - y_a);
		}
		int x_b_off = 0;
		if (y_c != y_b) {
			x_b_off = (x_c - x_b << 16) / (y_c - y_b);
		}
		int x_c_off = 0;
		if (y_c != y_a) {
			x_c_off = (x_a - x_c << 16) / (y_a - y_c);
		}
		if (y_a <= y_b && y_a <= y_c) {
			if (y_a >= DrawingArea.bottomY) {
				return;
			}
			if (y_b > DrawingArea.bottomY) {
				y_b = DrawingArea.bottomY;
			}
			if (y_c > DrawingArea.bottomY) {
				y_c = DrawingArea.bottomY;
			}
			if (y_b < y_c) {
				x_c = x_a <<= 16;
				if (y_a < 0) {
					x_c -= x_c_off * y_a;
					x_a -= x_a_off * y_a;
					y_a = 0;
				}
				x_b <<= 16;
				if (y_b < 0) {
					x_b -= x_b_off * y_b;
					y_b = 0;
				}
				if (y_a != y_b && x_c_off < x_a_off || y_a == y_b && x_c_off > x_b_off) {
					y_c -= y_b;
					y_b -= y_a;
					for (y_a = lineOffsets[y_a]; --y_b >= 0; y_a += width) {
						drawScanLine(pixels, y_a, color, x_c >> 16, x_a >> 16);
						x_c += x_c_off;
						x_a += x_a_off;
					}

					while (--y_c >= 0) {
						drawScanLine(pixels, y_a, color, x_c >> 16, x_b >> 16);
						x_c += x_c_off;
						x_b += x_b_off;
						y_a += width;
					}
					return;
				}
				y_c -= y_b;
				y_b -= y_a;
				for (y_a = lineOffsets[y_a]; --y_b >= 0; y_a += width) {
					drawScanLine(pixels, y_a, color, x_a >> 16, x_c >> 16);
					x_c += x_c_off;
					x_a += x_a_off;
				}

				while (--y_c >= 0) {
					drawScanLine(pixels, y_a, color, x_b >> 16, x_c >> 16);
					x_c += x_c_off;
					x_b += x_b_off;
					y_a += width;
				}
				return;
			}
			x_b = x_a <<= 16;
			if (y_a < 0) {
				x_b -= x_c_off * y_a;
				x_a -= x_a_off * y_a;
				y_a = 0;
			}
			x_c <<= 16;
			if (y_c < 0) {
				x_c -= x_b_off * y_c;
				y_c = 0;
			}
			if (y_a != y_c && x_c_off < x_a_off || y_a == y_c && x_b_off > x_a_off) {
				y_b -= y_c;
				y_c -= y_a;
				for (y_a = lineOffsets[y_a]; --y_c >= 0; y_a += width) {
					drawScanLine(pixels, y_a, color, x_b >> 16, x_a >> 16);
					x_b += x_c_off;
					x_a += x_a_off;
				}

				while (--y_b >= 0) {
					drawScanLine(pixels, y_a, color, x_c >> 16, x_a >> 16);
					x_c += x_b_off;
					x_a += x_a_off;
					y_a += width;
				}
				return;
			}
			y_b -= y_c;
			y_c -= y_a;
			for (y_a = lineOffsets[y_a]; --y_c >= 0; y_a += width) {
				drawScanLine(pixels, y_a, color, x_a >> 16, x_b >> 16);
				x_b += x_c_off;
				x_a += x_a_off;
			}

			while (--y_b >= 0) {
				drawScanLine(pixels, y_a, color, x_a >> 16, x_c >> 16);
				x_c += x_b_off;
				x_a += x_a_off;
				y_a += width;
			}
			return;
		}
		if (y_b <= y_c) {
			if (y_b >= DrawingArea.bottomY) {
				return;
			}
			if (y_c > DrawingArea.bottomY) {
				y_c = DrawingArea.bottomY;
			}
			if (y_a > DrawingArea.bottomY) {
				y_a = DrawingArea.bottomY;
			}
			if (y_c < y_a) {
				x_a = x_b <<= 16;
				if (y_b < 0) {
					x_a -= x_a_off * y_b;
					x_b -= x_b_off * y_b;
					y_b = 0;
				}
				x_c <<= 16;
				if (y_c < 0) {
					x_c -= x_c_off * y_c;
					y_c = 0;
				}
				if (y_b != y_c && x_a_off < x_b_off || y_b == y_c && x_a_off > x_c_off) {
					y_a -= y_c;
					y_c -= y_b;
					for (y_b = lineOffsets[y_b]; --y_c >= 0; y_b += width) {
						drawScanLine(pixels, y_b, color, x_a >> 16, x_b >> 16);
						x_a += x_a_off;
						x_b += x_b_off;
					}

					while (--y_a >= 0) {
						drawScanLine(pixels, y_b, color, x_a >> 16, x_c >> 16);
						x_a += x_a_off;
						x_c += x_c_off;
						y_b += width;
					}
					return;
				}
				y_a -= y_c;
				y_c -= y_b;
				for (y_b = lineOffsets[y_b]; --y_c >= 0; y_b += width) {
					drawScanLine(pixels, y_b, color, x_b >> 16, x_a >> 16);
					x_a += x_a_off;
					x_b += x_b_off;
				}

				while (--y_a >= 0) {
					drawScanLine(pixels, y_b, color, x_c >> 16, x_a >> 16);
					x_a += x_a_off;
					x_c += x_c_off;
					y_b += width;
				}
				return;
			}
			x_c = x_b <<= 16;
			if (y_b < 0) {
				x_c -= x_a_off * y_b;
				x_b -= x_b_off * y_b;
				y_b = 0;
			}
			x_a <<= 16;
			if (y_a < 0) {
				x_a -= x_c_off * y_a;
				y_a = 0;
			}
			if (x_a_off < x_b_off) {
				y_c -= y_a;
				y_a -= y_b;
				for (y_b = lineOffsets[y_b]; --y_a >= 0; y_b += width) {
					drawScanLine(pixels, y_b, color, x_c >> 16, x_b >> 16);
					x_c += x_a_off;
					x_b += x_b_off;
				}

				while (--y_c >= 0) {
					drawScanLine(pixels, y_b, color, x_a >> 16, x_b >> 16);
					x_a += x_c_off;
					x_b += x_b_off;
					y_b += width;
				}
				return;
			}
			y_c -= y_a;
			y_a -= y_b;
			for (y_b = lineOffsets[y_b]; --y_a >= 0; y_b += width) {
				drawScanLine(pixels, y_b, color, x_b >> 16, x_c >> 16);
				x_c += x_a_off;
				x_b += x_b_off;
			}

			while (--y_c >= 0) {
				drawScanLine(pixels, y_b, color, x_b >> 16, x_a >> 16);
				x_a += x_c_off;
				x_b += x_b_off;
				y_b += width;
			}
			return;
		}
		if (y_c >= DrawingArea.bottomY) {
			return;
		}
		if (y_a > DrawingArea.bottomY) {
			y_a = DrawingArea.bottomY;
		}
		if (y_b > DrawingArea.bottomY) {
			y_b = DrawingArea.bottomY;
		}
		if (y_a < y_b) {
			x_b = x_c <<= 16;
			if (y_c < 0) {
				x_b -= x_b_off * y_c;
				x_c -= x_c_off * y_c;
				y_c = 0;
			}
			x_a <<= 16;
			if (y_a < 0) {
				x_a -= x_a_off * y_a;
				y_a = 0;
			}
			if (x_b_off < x_c_off) {
				y_b -= y_a;
				y_a -= y_c;
				for (y_c = lineOffsets[y_c]; --y_a >= 0; y_c += width) {
					drawScanLine(pixels, y_c, color, x_b >> 16, x_c >> 16);
					x_b += x_b_off;
					x_c += x_c_off;
				}

				while (--y_b >= 0) {
					drawScanLine(pixels, y_c, color, x_b >> 16, x_a >> 16);
					x_b += x_b_off;
					x_a += x_a_off;
					y_c += width;
				}
				return;
			}
			y_b -= y_a;
			y_a -= y_c;
			for (y_c = lineOffsets[y_c]; --y_a >= 0; y_c += width) {
				drawScanLine(pixels, y_c, color, x_c >> 16, x_b >> 16);
				x_b += x_b_off;
				x_c += x_c_off;
			}

			while (--y_b >= 0) {
				drawScanLine(pixels, y_c, color, x_a >> 16, x_b >> 16);
				x_b += x_b_off;
				x_a += x_a_off;
				y_c += width;
			}
			return;
		}
		x_a = x_c <<= 16;
		if (y_c < 0) {
			x_a -= x_b_off * y_c;
			x_c -= x_c_off * y_c;
			y_c = 0;
		}
		x_b <<= 16;
		if (y_b < 0) {
			x_b -= x_a_off * y_b;
			y_b = 0;
		}
		if (x_b_off < x_c_off) {
			y_a -= y_b;
			y_b -= y_c;
			for (y_c = lineOffsets[y_c]; --y_b >= 0; y_c += width) {
				drawScanLine(pixels, y_c, color, x_a >> 16, x_c >> 16);
				x_a += x_b_off;
				x_c += x_c_off;
			}

			while (--y_a >= 0) {
				drawScanLine(pixels, y_c, color, x_b >> 16, x_c >> 16);
				x_b += x_a_off;
				x_c += x_c_off;
				y_c += width;
			}
			return;
		}
		y_a -= y_b;
		y_b -= y_c;
		for (y_c = lineOffsets[y_c]; --y_b >= 0; y_c += width) {
			drawScanLine(pixels, y_c, color, x_c >> 16, x_a >> 16);
			x_a += x_b_off;
			x_c += x_c_off;
		}

		while (--y_a >= 0) {
			drawScanLine(pixels, y_c, color, x_c >> 16, x_b >> 16);
			x_b += x_a_off;
			x_c += x_c_off;
			y_c += width;
		}
	}

	private static void drawScanLine(int dest[], int dest_off, int loops, int start_x, int end_x) {
		int rgb;//was parameter
		if (restrict_edges) {
			if (end_x > DrawingArea.centerX) {
				end_x = DrawingArea.centerX;
			}
			if (start_x < 0) {
				start_x = 0;
			}
		}
		if (start_x >= end_x) {
			return;
		}
		dest_off += start_x;
		rgb = end_x - start_x >> 2;
		if (alpha == 0) {
			while (--rgb >= 0) {
				dest[dest_off++] = loops;
				dest[dest_off++] = loops;
				dest[dest_off++] = loops;
				dest[dest_off++] = loops;
			}
			for (rgb = end_x - start_x & 3; --rgb >= 0; ) {
				dest[dest_off++] = loops;
			}

			return;
		}
		int dest_alpha = alpha;
		int src_alpha = 256 - alpha;
		loops = ((loops & 0xff00ff) * src_alpha >> 8 & 0xff00ff) + ((loops & 0xff00) * src_alpha >> 8 & 0xff00);
		  while (--rgb >= 0) {//alpha channel fix
				dest[dest_off] = loops + ((dest[dest_off] & 0xff00ff) * dest_alpha >> 8 & 0xff00ff) + ((dest[dest_off] & 0xff00) * dest_alpha >> 8 & 0xff00);
				dest_off++;
				dest[dest_off] = loops + ((dest[dest_off] & 0xff00ff) * dest_alpha >> 8 & 0xff00ff) + ((dest[dest_off] & 0xff00) * dest_alpha >> 8 & 0xff00);
				dest_off++;
				dest[dest_off] = loops + ((dest[dest_off] & 0xff00ff) * dest_alpha >> 8 & 0xff00ff) + ((dest[dest_off] & 0xff00) * dest_alpha >> 8 & 0xff00);
				dest_off++;
				dest[dest_off] = loops + ((dest[dest_off] & 0xff00ff) * dest_alpha >> 8 & 0xff00ff) + ((dest[dest_off] & 0xff00) * dest_alpha >> 8 & 0xff00);
				dest_off++;
		  }
			for (rgb = end_x - start_x & 3; --rgb >= 0; ) {
				dest[dest_off] = loops + ((dest[dest_off] & 0xff00ff) * dest_alpha >> 8 & 0xff00ff) + ((dest[dest_off] & 0xff00) * dest_alpha >> 8 & 0xff00);
				dest_off++;
			}

	}

	public static boolean drawTexturedTriangle(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int grad_a, int grad_b, int grad_c, int Px, int Mx, int Nx, int Pz, int Mz, int Nz, int Py, int My, int Ny, int t_id, int color, boolean force) {
		if (t_id < 0 || t_id >= TextureDef.textures.length) {
			drawShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c);
			return true;
		}
		TextureDef def = TextureDef.textures[t_id];
		if (def == null || (!force && !def.aBoolean1223 && lowMem)) {
			drawShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c);
			return true;
		}
		int texture[] = getTexturePixels(t_id);
		if (texture == null) {
			drawShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c);
			return false;
		}
		if (color >= 0xffff) {
			color = -1;
		}

		if (color >= 0) {
			color = hslToRgbLookupTable[color];
		}

		++triangles;
		Mx = Px - Mx;
		Mz = Pz - Mz;
		My = Py - My;
		Nx -= Px;
		Nz -= Pz;
		Ny -= Py;
		int Oa = Nx * Pz - Nz * Px << 14;
		int Ha = Nz * Py - Ny * Pz << 8;
		int Va = Ny * Px - Nx * Py << 5;
		int Ob = Mx * Pz - Mz * Px << 14;
		int Hb = Mz * Py - My * Pz << 8;
		int Vb = My * Px - Mx * Py << 5;
		int Oc = Mz * Nx - Mx * Nz << 14;
		int Hc = My * Nz - Mz * Ny << 8;
		int Vc = Mx * Ny - My * Nx << 5;
		int x_a_off = 0;
		int grad_a_off = 0;
		if (y_b != y_a) {
			x_a_off = (x_b - x_a << 16) / (y_b - y_a);
			grad_a_off = (grad_b - grad_a << 16) / (y_b - y_a);
		}
		int x_b_off = 0;
		int grad_b_off = 0;
		if (y_c != y_b) {
			x_b_off = (x_c - x_b << 16) / (y_c - y_b);
			grad_b_off = (grad_c - grad_b << 16) / (y_c - y_b);
		}
		int x_c_off = 0;
		int grad_c_off = 0;
		if (y_c != y_a) {
			x_c_off = (x_a - x_c << 16) / (y_a - y_c);
			grad_c_off = (grad_a - grad_c << 16) / (y_a - y_c);
		}
		if (y_a <= y_b && y_a <= y_c) {
			if (y_a >= DrawingArea.bottomY) {
				return true;
			}
			if (y_b > DrawingArea.bottomY) {
				y_b = DrawingArea.bottomY;
			}
			if (y_c > DrawingArea.bottomY) {
				y_c = DrawingArea.bottomY;
			}
			if (y_b < y_c) {
				x_c = x_a <<= 16;
				grad_c = grad_a <<= 16;
				if (y_a < 0) {
					x_c -= x_c_off * y_a;
					x_a -= x_a_off * y_a;
					grad_c -= grad_c_off * y_a;
					grad_a -= grad_a_off * y_a;
					y_a = 0;
				}
				x_b <<= 16;
				grad_b <<= 16;
				if (y_b < 0) {
					x_b -= x_b_off * y_b;
					grad_b -= grad_b_off * y_b;
					y_b = 0;
				}
				int jA = y_a - centerY;
				Oa += Va * jA;
				Ob += Vb * jA;
				Oc += Vc * jA;
				/*if (y_a != y_b && x_c_off < x_a_off || y_a == y_b && x_c_off > x_b_off) {
					y_c -= y_b;
					y_b -= y_a;
					y_a = lineOffsets[y_a];
					while (--y_b >= 0) {
						drawTexturedLine(pixels, texture, y_a, x_c >> 16, x_a >> 16, grad_c >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_c += x_c_off;
						x_a += x_a_off;
						grad_c += grad_c_off;
						grad_a += grad_a_off;
						y_a += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					while (--y_c >= 0) {
						drawTexturedLine(pixels, texture, y_a, x_c >> 16, x_b >> 16, grad_c >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_c += x_c_off;
						x_b += x_b_off;
						grad_c += grad_c_off;
						grad_b += grad_b_off;
						y_a += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					return true;
				}*/
				y_c -= y_b;
				y_b -= y_a;
				y_a = lineOffsets[y_a];
				while (--y_b >= 0) {
					drawTexturedLine(pixels, texture, y_a, x_a >> 16, x_c >> 16, grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_c += x_c_off;
					x_a += x_a_off;
					grad_c += grad_c_off;
					grad_a += grad_a_off;
					y_a += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_c >= 0) {
					drawTexturedLine(pixels, texture, y_a, x_b >> 16, x_c >> 16, grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_c += x_c_off;
					x_b += x_b_off;
					grad_c += grad_c_off;
					grad_b += grad_b_off;
					y_a += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				return true;
			}
			x_b = x_a <<= 16;
			grad_b = grad_a <<= 16;
			if (y_a < 0) {
				x_b -= x_c_off * y_a;
				x_a -= x_a_off * y_a;
				grad_b -= grad_c_off * y_a;
				grad_a -= grad_a_off * y_a;
				y_a = 0;
			}
			x_c <<= 16;
			grad_c <<= 16;
			if (y_c < 0) {
				x_c -= x_b_off * y_c;
				grad_c -= grad_b_off * y_c;
				y_c = 0;
			}
			int l8 = y_a - centerY;
			Oa += Va * l8;
			Ob += Vb * l8;
			Oc += Vc * l8;
			/*if (y_a != y_c && x_c_off < x_a_off || y_a == y_c && x_b_off > x_a_off) {
				y_b -= y_c;
				y_c -= y_a;
				y_a = lineOffsets[y_a];
				while (--y_c >= 0) {
					drawTexturedLine(pixels, texture, y_a, x_b >> 16, x_a >> 16, grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_b += x_c_off;
					x_a += x_a_off;
					grad_b += grad_c_off;
					grad_a += grad_a_off;
					y_a += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_b >= 0) {
					drawTexturedLine(pixels, texture, y_a, x_c >> 16, x_a >> 16, grad_c >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_c += x_b_off;
					x_a += x_a_off;
					grad_c += grad_b_off;
					grad_a += grad_a_off;
					y_a += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				return true;
			}*/
			y_b -= y_c;
			y_c -= y_a;
			y_a = lineOffsets[y_a];
			while (--y_c >= 0) {
				drawTexturedLine(pixels, texture, y_a, x_a >> 16, x_b >> 16, grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_b += x_c_off;
				x_a += x_a_off;
				grad_b += grad_c_off;
				grad_a += grad_a_off;
				y_a += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			while (--y_b >= 0) {
				drawTexturedLine(pixels, texture, y_a, x_a >> 16, x_c >> 16, grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_c += x_b_off;
				x_a += x_a_off;
				grad_c += grad_b_off;
				grad_a += grad_a_off;
				y_a += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			return true;
		}
		if (y_b <= y_c) {
			if (y_b >= DrawingArea.bottomY) {
				return true;
			}
			if (y_c > DrawingArea.bottomY) {
				y_c = DrawingArea.bottomY;
			}
			if (y_a > DrawingArea.bottomY) {
				y_a = DrawingArea.bottomY;
			}
			if (y_c < y_a) {
				x_a = x_b <<= 16;
				grad_a = grad_b <<= 16;
				if (y_b < 0) {
					x_a -= x_a_off * y_b;
					x_b -= x_b_off * y_b;
					grad_a -= grad_a_off * y_b;
					grad_b -= grad_b_off * y_b;
					y_b = 0;
				}
				x_c <<= 16;
				grad_c <<= 16;
				if (y_c < 0) {
					x_c -= x_c_off * y_c;
					grad_c -= grad_c_off * y_c;
					y_c = 0;
				}
				int i9 = y_b - centerY;
				Oa += Va * i9;
				Ob += Vb * i9;
				Oc += Vc * i9;
				/*if (y_b != y_c && x_a_off < x_b_off || y_b == y_c && x_a_off > x_c_off) {
					y_a -= y_c;
					y_c -= y_b;
					y_b = lineOffsets[y_b];
					while (--y_c >= 0) {
						drawTexturedLine(pixels, texture, y_b, x_a >> 16, x_b >> 16, grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_a += x_a_off;
						x_b += x_b_off;
						grad_a += grad_a_off;
						grad_b += grad_b_off;
						y_b += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					while (--y_a >= 0) {
						drawTexturedLine(pixels, texture, y_b, x_a >> 16, x_c >> 16, grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_a += x_a_off;
						x_c += x_c_off;
						grad_a += grad_a_off;
						grad_c += grad_c_off;
						y_b += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					return true;
				}*/
				y_a -= y_c;
				y_c -= y_b;
				y_b = lineOffsets[y_b];
				//not these
				while (--y_c >= 0) {
					drawTexturedLine(pixels, texture, y_b, x_b >> 16, x_a >> 16, grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_a += x_a_off;
					x_b += x_b_off;
					grad_a += grad_a_off;
					grad_b += grad_b_off;
					y_b += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_a >= 0) {
					drawTexturedLine(pixels, texture, y_b, x_c >> 16, x_a >> 16, grad_c >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_a += x_a_off;
					x_c += x_c_off;
					grad_a += grad_a_off;
					grad_c += grad_c_off;
					y_b += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				return true;
			}
			x_c = x_b <<= 16;
			grad_c = grad_b <<= 16;
			if (y_b < 0) {
				x_c -= x_a_off * y_b;
				x_b -= x_b_off * y_b;
				grad_c -= grad_a_off * y_b;
				grad_b -= grad_b_off * y_b;
				y_b = 0;
			}
			x_a <<= 16;
			grad_a <<= 16;
			if (y_a < 0) {
				x_a -= x_c_off * y_a;
				grad_a -= grad_c_off * y_a;
				y_a = 0;
			}
			int j9 = y_b - centerY;
			Oa += Va * j9;
			Ob += Vb * j9;
			Oc += Vc * j9;
			/*if (x_a_off < x_b_off) {
				y_c -= y_a;
				y_a -= y_b;
				y_b = lineOffsets[y_b];
				while (--y_a >= 0) {
					drawTexturedLine(pixels, texture, y_b, x_c >> 16, x_b >> 16, grad_c >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_c += x_a_off;
					x_b += x_b_off;
					grad_c += grad_a_off;
					grad_b += grad_b_off;
					y_b += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_c >= 0) {
					drawTexturedLine(pixels, texture, y_b, x_a >> 16, x_b >> 16, grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_a += x_c_off;
					x_b += x_b_off;
					grad_a += grad_c_off;
					grad_b += grad_b_off;
					y_b += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				return true;
			}*/
			y_c -= y_a;
			y_a -= y_b;
			y_b = lineOffsets[y_b];
			//not these
			while (--y_a >= 0) {
				drawTexturedLine(pixels, texture, y_b, x_b >> 16, x_c >> 16, grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_c += x_a_off;
				x_b += x_b_off;
				grad_c += grad_a_off;
				grad_b += grad_b_off;
				y_b += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			while (--y_c >= 0) {
				drawTexturedLine(pixels, texture, y_b, x_b >> 16, x_a >> 16, grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_a += x_c_off;
				x_b += x_b_off;
				grad_a += grad_c_off;
				grad_b += grad_b_off;
				y_b += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			return true;
		}
		if (y_c >= DrawingArea.bottomY) {
			return true;
		}
		if (y_a > DrawingArea.bottomY) {
			y_a = DrawingArea.bottomY;
		}
		if (y_b > DrawingArea.bottomY) {
			y_b = DrawingArea.bottomY;
		}
		if (y_a < y_b) {
			x_b = x_c <<= 16;
			grad_b = grad_c <<= 16;
			if (y_c < 0) {
				x_b -= x_b_off * y_c;
				x_c -= x_c_off * y_c;
				grad_b -= grad_b_off * y_c;
				grad_c -= grad_c_off * y_c;
				y_c = 0;
			}
			x_a <<= 16;
			grad_a <<= 16;
			if (y_a < 0) {
				x_a -= x_a_off * y_a;
				grad_a -= grad_a_off * y_a;
				y_a = 0;
			}
			int k9 = y_c - centerY;
			Oa += Va * k9;
			Ob += Vb * k9;
			Oc += Vc * k9;
			/*if (x_b_off < x_c_off) {
				y_b -= y_a;
				y_a -= y_c;
				y_c = lineOffsets[y_c];
				while (--y_a >= 0) {
					drawTexturedLine(pixels, texture, y_c, x_b >> 16, x_c >> 16, grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_b += x_b_off;
					x_c += x_c_off;
					grad_b += grad_b_off;
					grad_c += grad_c_off;
					y_c += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_b >= 0) {
					drawTexturedLine(pixels, texture, y_c, x_b >> 16, x_a >> 16, grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_b += x_b_off;
					x_a += x_a_off;
					grad_b += grad_b_off;
					grad_a += grad_a_off;
					y_c += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				return true;
			}*/
			y_b -= y_a;
			y_a -= y_c;
			y_c = lineOffsets[y_c];
			//not these
			while (--y_a >= 0) {
				drawTexturedLine(pixels, texture, y_c, x_c >> 16, x_b >> 16, grad_c >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_b += x_b_off;
				x_c += x_c_off;
				grad_b += grad_b_off;
				grad_c += grad_c_off;
				y_c += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			while (--y_b >= 0) {
				drawTexturedLine(pixels, texture, y_c, x_a >> 16, x_b >> 16, grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_b += x_b_off;
				x_a += x_a_off;
				grad_b += grad_b_off;
				grad_a += grad_a_off;
				y_c += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			return true;
		}
		x_a = x_c <<= 16;
		grad_a = grad_c <<= 16;
		if (y_c < 0) {
			x_a -= x_b_off * y_c;
			x_c -= x_c_off * y_c;
			grad_a -= grad_b_off * y_c;
			grad_c -= grad_c_off * y_c;
			y_c = 0;
		}
		x_b <<= 16;
		grad_b <<= 16;
		if (y_b < 0) {
			x_b -= x_a_off * y_b;
			grad_b -= grad_a_off * y_b;
			y_b = 0;
		}
		int l9 = y_c - centerY;
		Oa += Va * l9;
		Ob += Vb * l9;
		Oc += Vc * l9;
		if (x_b_off < x_c_off) {
			y_a -= y_b;
			y_b -= y_c;
			y_c = lineOffsets[y_c];
			/*while (--y_b >= 0) {
				drawTexturedLine(pixels, texture, y_c, x_a >> 16, x_c >> 16, grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_a += x_b_off;
				x_c += x_c_off;
				grad_a += grad_b_off;
				grad_c += grad_c_off;
				y_c += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			while (--y_a >= 0) {
				drawTexturedLine(pixels, texture, y_c, x_b >> 16, x_c >> 16, grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_b += x_a_off;
				x_c += x_c_off;
				grad_b += grad_a_off;
				grad_c += grad_c_off;
				y_c += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}*/
			return true;
		}
		y_a -= y_b;
		y_b -= y_c;
		y_c = lineOffsets[y_c];
		//not these
		while (--y_b >= 0) {
			drawTexturedLine(pixels, texture, y_c, x_c >> 16, x_a >> 16, grad_c >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
			x_a += x_b_off;
			x_c += x_c_off;
			grad_a += grad_b_off;
			grad_c += grad_c_off;
			y_c += width;
			Oa += Va;
			Ob += Vb;
			Oc += Vc;
		}
		while (--y_a >= 0) {
			drawTexturedLine(pixels, texture, y_c, x_c >> 16, x_b >> 16, grad_c >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
			x_b += x_a_off;
			x_c += x_c_off;
			grad_b += grad_a_off;
			grad_c += grad_c_off;
			y_c += width;
			Oa += Va;
			Ob += Vb;
			Oc += Vc;
		}
		return true;
	}

	private static void drawTexturedLine(int dest[], int texture[], int dest_off, int start_x, int end_x, int shadeValue, int gradient, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, int color, boolean force) {
		//shadeValue = 500;//lol makes textures ultra bright and makes triangles visible - slightly wrong name.. meh
		int rgb = 0;
		int loops = 0;
		if (start_x >= end_x) {
			return;
		}
		int j3;
		int k3;
		if (restrict_edges) {
			j3 = (gradient - shadeValue) / (end_x - start_x);
			if (end_x > DrawingArea.centerX) {
				end_x = DrawingArea.centerX;
			}
			if (start_x < 0) {
				shadeValue -= start_x * j3;
				start_x = 0;
			}
			if (start_x >= end_x) {
				return;
			}
			k3 = end_x - start_x >> 3;
			j3 <<= 12;
			shadeValue <<= 9;
		} else {
			if (end_x - start_x > 7) {
				k3 = end_x - start_x >> 3;
				j3 = (gradient - shadeValue) * shadowDecay[k3] >> 6;
			} else {
				k3 = 0;
				j3 = 0;
			}
			shadeValue <<= 9;
		}
		dest_off += start_x;
		int j4 = 0;
		int l4 = 0;
		int l6 = start_x - centerX;
		arg7 += (arg10 >> 3) * l6;
		arg8 += (arg11 >> 3) * l6;
		arg9 += (arg12 >> 3) * l6;
		int l5 = arg9 >> 14;
		if (l5 != 0) {
			rgb = arg7 / l5;
			loops = arg8 / l5;
			if (rgb < 0) {
				rgb = 0;
			} else if (rgb > 16256) {
				rgb = 16256;
			}
		}
		arg7 += arg10;
		arg8 += arg11;
		arg9 += arg12;
		l5 = arg9 >> 14;
		if (l5 != 0) {
			j4 = arg7 / l5;
			l4 = arg8 / l5;
			if (j4 < 7) {
				j4 = 7;
			} else if (j4 > 16256) {
				j4 = 16256;
			}
		}
		int j7 = j4 - rgb >> 3;
		int l7 = l4 - loops >> 3;
		rgb += shadeValue & 0x600000;
		int glb_alpha = alpha;
		if (glb_alpha < 0 || glb_alpha >= 0xff)
			glb_alpha = 0;

		int src;
		int src_alpha;
		int src_delta;
		int dst;
		while (k3-- > 0)
		{
			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

				if (src_alpha != 0)
				{
					if (src_alpha == 0xff)
						dest[dest_off] = src & 0xffffff;

					else
					{
						dst = dest[dest_off];
						src_delta = 0xff - src_alpha;
						dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
					}
				}
			}
			dest_off++;
			rgb += j7;
			loops += l7;

			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

				if (src_alpha != 0)
				{
					if (src_alpha == 0xff)
						dest[dest_off] = src & 0xffffff;

					else
					{
						dst = dest[dest_off];
						src_delta = 0xff - src_alpha;
						dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
					}
				}
			}
			dest_off++;
			rgb += j7;
			loops += l7;

			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

				if (src_alpha != 0)
				{
					if (src_alpha == 0xff)
						dest[dest_off] = src & 0xffffff;

					else
					{
						dst = dest[dest_off];
						src_delta = 0xff - src_alpha;
						dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
					}
				}
			}
			dest_off++;
			rgb += j7;
			loops += l7;

			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

				if (src_alpha != 0)
				{
					if (src_alpha == 0xff)
						dest[dest_off] = src & 0xffffff;

					else
					{
						dst = dest[dest_off];
						src_delta = 0xff - src_alpha;
						dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
					}
				}
			}
			dest_off++;
			rgb += j7;
			loops += l7;

			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

				if (src_alpha != 0)
				{
					if (src_alpha == 0xff)
						dest[dest_off] = src & 0xffffff;

					else
					{
						dst = dest[dest_off];
						src_delta = 0xff - src_alpha;
						dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
					}
				}
			}
			dest_off++;
			rgb += j7;
			loops += l7;

			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

				if (src_alpha != 0)
				{
					if (src_alpha == 0xff)
						dest[dest_off] = src & 0xffffff;

					else
					{
						dst = dest[dest_off];
						src_delta = 0xff - src_alpha;
						dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
					}
				}
			}
			dest_off++;
			rgb += j7;
			loops += l7;

			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

				if (src_alpha != 0)
				{
					if (src_alpha == 0xff)
						dest[dest_off] = src & 0xffffff;

					else
					{
						dst = dest[dest_off];
						src_delta = 0xff - src_alpha;
						dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
					}
				}
			}
			dest_off++;
			rgb += j7;
			loops += l7;

			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

				if (src_alpha != 0)
				{
					if (src_alpha == 0xff)
						dest[dest_off] = src & 0xffffff;

					else
					{
						dst = dest[dest_off];
						src_delta = 0xff - src_alpha;
						dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
					}
				}
			}
			dest_off++;
			//rgb += j7;
			//loops += l7;

			rgb = j4;
			loops = l4;
			arg7 += arg10;
			arg8 += arg11;
			arg9 += arg12;
			int i6 = arg9 >> 14;
			if (i6 != 0) {
				j4 = arg7 / i6;
				l4 = arg8 / i6;
				if (j4 < 7) {
					j4 = 7;
				} else if (j4 > 16256) {
					j4 = 16256;
				}
			}
			j7 = j4 - rgb >> 3;
			l7 = l4 - loops >> 3;
			shadeValue += j3;
			rgb += shadeValue & 0x600000;
		}
		for (k3 = end_x - start_x & 7; k3-- > 0; ) {
			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

				if (src_alpha != 0)
				{
					if (src_alpha == 0xff)
						dest[dest_off] = src & 0xffffff;

					else
					{
						dst = dest[dest_off];
						src_delta = 0xff - src_alpha;
						dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
					}
				}
			}
			dest_off++;
			rgb += j7;
			loops += l7;
		}

	}


	//public static final int anInt1459 = -477; dummy never used
	public static boolean lowMem = true;
	static boolean restrict_edges;
	public static boolean notTextured = true;
	public static int alpha;
	public static int centerX;
	public static int centerY;
	private static int[] shadowDecay;
	public static final int[] lightDecay;
	public static int SINE[];
	public static int COSINE[];
	public static int lineOffsets[];
	private static int loadedTextureCount;
	private static int textureTexelPoolPointer;
	private static int[][] texelArrayPool;
	private static int[][] texelCache;
	public static int[] textureLastUsed;
	public static int textureGetCount;
	public static int hslToRgbLookupTable[] = new int[0x10000];
 
	static {
		shadowDecay = new int[512];
		lightDecay = new int[2048];
		SINE = new int[2048];
		COSINE = new int[2048];
		for (int i = 1; i < 512; i++) {
			shadowDecay[i] = 32768 / i;//decay rate for shadows or some *profound* - super_
		}

		for (int i = 1; i < 2048; i++) {
			lightDecay[i] = 0x10000 / i;
		}

		for (int i = 0; i < 2048; i++) {
			SINE[i] = (int) (65536D * Math.sin((double) i * (Math.PI / 1024.0D)));
			COSINE[i] = (int) (65536D * Math.cos((double) i * (Math.PI / 1024.0D)));
		}

	}
}
