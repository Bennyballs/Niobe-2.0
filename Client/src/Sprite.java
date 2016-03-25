package src;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;

import javax.swing.ImageIcon;

import src.sign.signlink;

public class Sprite extends DrawingArea {

	public static String CACHE_LOCATION = signlink.getCacheLocation() + "rsimg" + System.getProperty("file.separator");
	
	public Sprite(int width, int height) {
		myPixels = new int[width * height];
		myWidth = maxWidth = width;
		myHeight = maxHeight = height;
		drawOffsetX = drawOffsetY = 0;
	}
	
	public Sprite(int offsetX, int offsetY, int width, int height) {
		myPixels = new int[width * height];
		myWidth = maxWidth = width;
		myHeight = maxHeight = height;
		drawOffsetX = offsetX;
		drawOffsetY = offsetY;
	}
	
	public Sprite(byte abyte0[], Component component) {
		try {
			Image image = Toolkit.getDefaultToolkit().createImage(abyte0);
			MediaTracker mediatracker = new MediaTracker(component);
			mediatracker.addImage(image, 0);
			mediatracker.waitForAll();
			myWidth = image.getWidth(component);
			myHeight = image.getHeight(component);
			maxWidth = myWidth;
			maxHeight = myHeight;
			drawOffsetX = 0;
			drawOffsetY = 0;
			myPixels = new int[myWidth * myHeight];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, myWidth, myHeight, myPixels, 0, myWidth);
			pixelgrabber.grabPixels();
			setTransparency(255, 0, 255);
		} catch (Exception _ex) {
			System.out.println("Error converting jpg");
		}
	}
	
	public Sprite(String img, int width, int height) {
		try {
			String ext = ".png";
			Image image = Toolkit.getDefaultToolkit().createImage(FileOperations.ReadFile(CACHE_LOCATION + img + ext));
			myWidth = width;
			myHeight = height;
			maxWidth = myWidth;
			maxHeight = myHeight;
			drawOffsetX = 0;
			drawOffsetY = 0;
			myPixels = new int[myWidth * myHeight];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, myWidth, myHeight, myPixels, 0, myWidth);
			pixelgrabber.grabPixels();
			image = null;
			setTransparency(255, 0, 255);
		} catch (Exception _ex) {
			System.out.println(_ex);
		}
	}

	public Sprite(byte spriteData[]) {
		try {
			Image image = Toolkit.getDefaultToolkit().createImage(spriteData);
			ImageIcon sprite = new ImageIcon(image);
			myWidth = sprite.getIconWidth();
			myHeight = sprite.getIconHeight();
			maxWidth = myWidth;
			maxHeight = myHeight;
			drawOffsetX = 0;
			drawOffsetY = 0;
			myPixels = new int[myWidth * myHeight];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, myWidth, myHeight, myPixels, 0, myWidth);
			pixelgrabber.grabPixels();
			image = null;
			setTransparency(255, 0, 255);
			setTransparency(255, 255, 255);
		} catch (Exception _ex) {
			System.out.println(_ex);
		}
	}

	public Sprite(String img) {
		try {
			String ext = ".png";
			Image image = Toolkit.getDefaultToolkit().getImage(CACHE_LOCATION + img + ext);
			ImageIcon sprite = new ImageIcon(image);
			myWidth = sprite.getIconWidth();
			myHeight = sprite.getIconHeight();
			maxWidth = myWidth;
			maxHeight = myHeight;
			drawOffsetX = 0;
			drawOffsetY = 0;
			myPixels = new int[myWidth * myHeight];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, myWidth,
					myHeight, myPixels, 0, myWidth);
			pixelgrabber.grabPixels();
			image = null;
			setTransparency(255, 0, 255);
			setTransparency(255, 255, 255);
		} catch (Exception _ex) {
			System.out.println(_ex);
		}
	}
	
	public Sprite(JagexArchive streamLoader, String archive, int id) {
		JagexBuffer stream = new JagexBuffer(streamLoader.getData(archive + ".dat"));
		JagexBuffer stream_1 = new JagexBuffer(streamLoader.getData("index.dat"));
		stream_1.currentOffset = stream.readUnsignedShort();
		maxWidth = stream_1.readUnsignedShort();
		maxHeight = stream_1.readUnsignedShort();
		int j = stream_1.readUnsignedByte();
		int ai[] = new int[j];
		for (int k = 0; k < j - 1; k++) {
			ai[k + 1] = stream_1.readTripleBytes();
			if (ai[k + 1] == 0)
				ai[k + 1] = 1;
		}

		for (int l = 0; l < id; l++) {
			stream_1.currentOffset += 2;
			stream.currentOffset += stream_1.readUnsignedShort() * stream_1.readUnsignedShort();
			stream_1.currentOffset++;
		}

		drawOffsetX = stream_1.readUnsignedByte();
		drawOffsetY = stream_1.readUnsignedByte();
		myWidth = stream_1.readUnsignedShort();
		myHeight = stream_1.readUnsignedShort();
		int i1 = stream_1.readUnsignedByte();
		int j1 = myWidth * myHeight;
		if (j1 > maxWidth * maxHeight) {
			System.out.println("Possible java heap memory: sprite archive name: " + archive + ", index: " + id);
			System.out.println("myWidth:" + myWidth + "; myHeight:" + myHeight);
			return;
		}
		myPixels = new int[j1];
		if (i1 == 0) {
			for (int k1 = 0; k1 < j1; k1++) {
				if (k1 < myPixels.length) {
					int pixel = stream.readUnsignedByte();
					if (pixel < ai.length) {
						myPixels[k1] = ai[pixel];
					}
				}
			}
			setTransparency(255, 0, 255);
			return;
		}
		if (i1 == 1) {
			for (int l1 = 0; l1 < myWidth; l1++) {
				for (int i2 = 0; i2 < myHeight; i2++)
					myPixels[l1 + i2 * myWidth] = ai[stream.readUnsignedByte()];
			}

		}
		setTransparency(255, 0, 255);
	}

	public void setSprite(int width, int height) {
		myWidth = width;
		myHeight = height;
		maxWidth = myWidth;
		maxHeight = myHeight;
		myPixels = new int[myWidth * myHeight];
	}
	
	public void setTransparency(int transRed, int transGreen, int transBlue) {
		for (int index = 0; index < myPixels.length; index++)
			if (((myPixels[index] >> 16) & 255) == transRed && ((myPixels[index] >> 8) & 255) == transGreen && (myPixels[index] & 255) == transBlue)
				myPixels[index] = 0;
	}

	public void setAlphaTransparency(int alpha) {
		for (int pixel = 0; pixel < myPixels.length; pixel++){
			if (((myPixels[pixel] >> 24) & 255) == alpha)
				myPixels[pixel] = 0;
		}
	}

	public void mirrorHorizontal() {
		int abyte0[] = new int[myWidth * myHeight];
		int j = 0;
		for (int k = 0; k < myHeight; k++) {
			for (int l = myWidth - 1; l >= 0; l--)
				abyte0[j++] = myPixels[l + k * myWidth];
		}
		myPixels = abyte0;
		drawOffsetX = maxWidth - myWidth - drawOffsetX;
	}

	public void mirrorVertical() {
		int pixels[] = new int[myWidth * myHeight];
		int i = 0;
		for (int j = myHeight - 1; j >= 0; j--) {
			for (int k = 0; k < myWidth; k++)
				pixels[i++] = myPixels[k + j * myWidth];
		}
		myPixels = pixels;
		drawOffsetY = maxHeight - myHeight - drawOffsetY;
	}

	public void method343() {
		DrawingArea.initDrawingArea(myHeight, myWidth, myPixels);
	}

	public void method344(int i, int j, int k) {
		autoUpdate();
		for (int i1 = 0; i1 < myPixels.length; i1++) {
			int j1 = myPixels[i1];
			if (j1 != 0) {
				int k1 = j1 >> 16 & 0xff;
				k1 += i;
				if (k1 < 1)
					k1 = 1;
				else if (k1 > 255)
					k1 = 255;
				int l1 = j1 >> 8 & 0xff;
				l1 += j;
				if (l1 < 1)
					l1 = 1;
				else if (l1 > 255)
					l1 = 255;
				int i2 = j1 & 0xff;
				i2 += k;
				if (i2 < 1)
					i2 = 1;
				else if (i2 > 255)
					i2 = 255;
				myPixels[i1] = (k1 << 16) + (l1 << 8) + i2;
			}
		}

	}

	public void method345() {
		autoUpdate();
		int ai[] = new int[maxWidth * maxHeight];
		for (int j = 0; j < myHeight; j++) {
			System.arraycopy(myPixels, j * myWidth, ai, j + drawOffsetY
					* maxWidth + drawOffsetX, myWidth);
		}
		myPixels = ai;
		myWidth = maxWidth;
		myHeight = maxHeight;
		drawOffsetX = 0;
		drawOffsetY = 0;
	}

	public void method346(int i, int j) {
		autoUpdate();
		i += drawOffsetX;
		j += drawOffsetY;
		int l = i + j * DrawingArea.width;
		int i1 = 0;
		int j1 = myHeight;
		int k1 = myWidth;
		int l1 = DrawingArea.width - k1;
		int i2 = 0;
		if (j < DrawingArea.topY) {
			int j2 = DrawingArea.topY - j;
			j1 -= j2;
			j = DrawingArea.topY;
			i1 += j2 * k1;
			l += j2 * DrawingArea.width;
		}
		if (j + j1 > DrawingArea.bottomY)
			j1 -= (j + j1) - DrawingArea.bottomY;
		if (i < DrawingArea.topX) {
			int k2 = DrawingArea.topX - i;
			k1 -= k2;
			i = DrawingArea.topX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if (i + k1 > DrawingArea.bottomX) {
			int l2 = (i + k1) - DrawingArea.bottomX;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if (k1 <= 0 || j1 <= 0) {
		} else {
			method347(l, k1, j1, i2, i1, l1, myPixels, DrawingArea.pixels);
		}
	}

	public void method347(int i, int j, int k, int l, int i1, int k1, int ai[], int ai1[]) {
		autoUpdate();
		int l1 = -(j >> 2);
		j = -(j & 3);
		for (int i2 = -k; i2 < 0; i2++) {
			for (int j2 = l1; j2 < 0; j2++) {
				ai1[i++] = ai[i1++];
				ai1[i++] = ai[i1++];
				ai1[i++] = ai[i1++];
				ai1[i++] = ai[i1++];
			}

			for (int k2 = j; k2 < 0; k2++)
				ai1[i++] = ai[i1++];

			i += k1;
			i1 += l;
		}
	}

	public void drawARGBSprite(int xPos, int yPos) {
		drawARGBSprite(xPos, yPos, 256);
	}

	public void drawARGBSprite(int xPos, int yPos, int alpha) {
		int alphaValue = alpha;
		xPos += drawOffsetX;
		yPos += drawOffsetY;
		int i1 = xPos + yPos * DrawingArea.width;
		int j1 = 0;
		int spriteHeight = myHeight;
		int spriteWidth = myWidth;
		int i2 = DrawingArea.width - spriteWidth;
		int j2 = 0;
		if (yPos < DrawingArea.topY) {
			int k2 = DrawingArea.topY - yPos;
			spriteHeight -= k2;
			yPos = DrawingArea.topY;
			j1 += k2 * spriteWidth;
			i1 += k2 * DrawingArea.width;
		}
		if (yPos + spriteHeight > DrawingArea.bottomY)
			spriteHeight -= (yPos + spriteHeight) - DrawingArea.bottomY;
			if (xPos < DrawingArea.topX) {
			int l2 = DrawingArea.topX - xPos;
			spriteWidth -= l2;
			xPos = DrawingArea.topX;
			j1 += l2;
			i1 += l2;
			j2 += l2;
			i2 += l2;
		}
		if (xPos + spriteWidth > DrawingArea.bottomX) {
			int i3 = (xPos + spriteWidth) - DrawingArea.bottomX;
			spriteWidth -= i3;
			j2 += i3;
			i2 += i3;
		}
		if (!(spriteWidth <= 0 || spriteHeight <= 0)) {
			renderARGBPixels(spriteWidth, spriteHeight, myPixels, DrawingArea.pixels, i1, alphaValue, j1, j2, i2);
		}
	}

    private void renderARGBPixels(int spriteWidth, int spriteHeight, int spritePixels[], int renderAreaPixels[], int pixel, int alphaValue, int i, int l, int j1) {
    	int pixelColor;
    	int alphaLevel;
    	int alpha = alphaValue;
    	for (int height = -spriteHeight; height < 0; height++) {
    		for (int width = -spriteWidth; width < 0; width++) {
    			alphaValue = ((myPixels[i] >> 24) & (alpha - 1));
    			alphaLevel = 256 - alphaValue;
    			if (alphaLevel > 256) {
    				alphaValue = 0;
    			}
    			if (alpha == 0) {
    				alphaLevel = 256;
    				alphaValue = 0;
    			} 
    			pixelColor = spritePixels[i++];
    			if (pixelColor != 0) {
    				int pixelValue = renderAreaPixels[pixel];
    				renderAreaPixels[pixel++] = ((pixelColor & 0xff00ff) * alphaValue + (pixelValue & 0xff00ff) * alphaLevel & 0xff00ff00) + ((pixelColor & 0xff00) * alphaValue + (pixelValue & 0xff00) * alphaLevel & 0xff0000) >> 8;
    			} else {
    				pixel++;
    			}
    		}
    		pixel += j1;
    		i += l;
    	}
		/*int alpha = 256 - alphaValue;
		for (int k2 = -spriteHeight; k2 < 0; k2++) {
			for (int l2 = -spriteWidth; l2 < 0; l2++) {
				pixelLevel = spritePixels[i++];
				if (pixelLevel != 0) {
					int i3 = renderAreaPixels[pixel];
					renderAreaPixels[pixel++] = ((pixelLevel & 0xff00ff) * alphaValue + (i3 & 0xff00ff) * alpha & 0xff00ff00) + ((pixelLevel & 0xff00) * alphaValue + (i3 & 0xff00) * alpha & 0xff0000) >> 8;
				} else {
					pixel++;
				}
			}
			pixel += j1;
			i += l;
		}*/
    }

	public void drawSprite(int xPos, int yPos) {
		drawSprite(xPos, yPos, 256);
	}

	public void drawSprite(int xPos, int yPos, int alpha) {
		autoUpdate();
		int alphaValue = alpha;
		xPos += drawOffsetX;
		yPos += drawOffsetY;
		int i1 = xPos + yPos * DrawingArea.width;
		int j1 = 0;
		int height = myHeight;
		int width = myWidth;
		int i2 = DrawingArea.width - width;
		int j2 = 0;
		if (yPos < DrawingArea.topY) {
			int k2 = DrawingArea.topY - yPos;
			height -= k2;
			yPos = DrawingArea.topY;
			j1 += k2 * width;
			i1 += k2 * DrawingArea.width;
		}
		if (yPos + height > DrawingArea.bottomY)
			height -= (yPos + height) - DrawingArea.bottomY;
		if (xPos < DrawingArea.topX) {
			int l2 = DrawingArea.topX - xPos;
			width -= l2;
			xPos = DrawingArea.topX;
			j1 += l2;
			i1 += l2;
			j2 += l2;
			i2 += l2;
		}
		if (xPos + width > DrawingArea.bottomX) {
			int i3 = (xPos + width) - DrawingArea.bottomX;
			width -= i3;
			j2 += i3;
			i2 += i3;
		}
		if (!(width <= 0 || height <= 0)) {
			renderRGBPixels(j1, width, DrawingArea.pixels, myPixels, j2, height, i2, alphaValue, i1);
		}
	}

	public int[] cutPixels(int[] source, int oldWidth, int oldHeight, int newWidth, int newHeight) {
		if (newHeight > oldHeight)
			newHeight = oldHeight;
		if (newWidth > oldWidth)
			newWidth = oldWidth;
		int[] cut = new int[newWidth * newHeight];
		int wcount = 0;
		int index = 0;
		for (int height = 0; height < newHeight; height++) {
			while (wcount < newWidth) {
				cut[index++] = source[(height * oldWidth) + wcount];
				wcount++;
			}
			wcount = 0;
		}
		return cut;
	}

	public int[] setTransparency(int[] source, int alpha) {
		for (int i = 0; i < source.length; i++) {
			Color src = new Color(source[i]);
			source[i] = new Color(src.getRed(), src.getGreen(), src.getBlue(),
					(byte) ((double) (src.getAlpha() / 255.00) * alpha)).getRGB();
		}
		return source;
	}

	public int[] cutPixels(int[] source, int oldWidth, int oldHeight, int newWidth, int newHeight, int startX, int startY) {
		if (newHeight > oldHeight)
			newHeight = oldHeight;
		if (newWidth > oldWidth)
			newWidth = oldWidth;
		int[] cut = new int[newWidth * newHeight];
		int wcount = startX;
		int index = 0;
		for (int height = startY; height < newHeight + startY; height++) {
			while (wcount < startX + newWidth) {
				cut[index++] = source[(height * oldWidth) + wcount];
				wcount++;
			}
			wcount = startX;
		}
		return cut;
	}

	public void manipulateSprite(int xPos, int reduceWidth, int yPos, int reduceHeight) {
		int oldWidth = myWidth;
		int oldHeight = myHeight;
		//int[] oldPixels = myPixels;
		myWidth = oldWidth - reduceWidth;
		myHeight = oldHeight - reduceHeight;
		myPixels = new int[myWidth * myHeight];
		autoUpdate();
		int alphaValue = 256;
		xPos += drawOffsetX;
		yPos += drawOffsetY;
		int i1 = xPos + yPos * DrawingArea.width;
		int j1 = 0;
		int height = myHeight;
		int width = myWidth;
		int i2 = DrawingArea.width - width;
		int j2 = 0;
		if (yPos < DrawingArea.topY) {
			int k2 = DrawingArea.topY - yPos;
			height -= k2;
			yPos = DrawingArea.topY;
			j1 += k2 * width;
			i1 += k2 * DrawingArea.width;
		}
		if (yPos + height > DrawingArea.bottomY) {
			height -= (yPos + height) - DrawingArea.bottomY;
		}
		if (xPos < DrawingArea.topX) {
			int l2 = DrawingArea.topX - xPos;
			width -= l2;
			xPos = DrawingArea.topX;
			j1 += l2;
			i1 += l2;
			j2 += l2;
			i2 += l2;
		}
		if (xPos + width > DrawingArea.bottomX) {
			int i3 = (xPos + width) - DrawingArea.bottomX;
			width -= i3;
			j2 += i3;
			i2 += i3;
		}
		if (!(width <= 0 || height <= 0)) {
			renderRGBPixels(j1, width, DrawingArea.pixels, myPixels, j2, height, i2, alphaValue, i1);
		}
	}

	public void drawOutlinedSprite(int xPos, int yPos, int color) {
		autoUpdate();
		int tempWidth = myWidth + 2;
		int tempHeight = myHeight + 2;
		int[] tempArray = new int[tempWidth * tempHeight];
		for (int x = 0; x < myWidth; x++) {
			for (int y = 0; y < myHeight; y++) {
				if (myPixels[x + y * myWidth] != 0)
					tempArray[(x + 1) + (y + 1) * tempWidth] = myPixels[x + y
							* myWidth];
			}
		}
		for (int x = 0; x < tempWidth; x++) {
			for (int y = 0; y < tempHeight; y++) {
				if (tempArray[(x) + (y) * tempWidth] == 0) {
					if (x < tempWidth - 1
							&& tempArray[(x + 1) + ((y) * tempWidth)] > 0
							&& tempArray[(x + 1) + ((y) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
					if (x > 0
							&& tempArray[(x - 1) + ((y) * tempWidth)] > 0
							&& tempArray[(x - 1) + ((y) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
					if (y < tempHeight - 1
							&& tempArray[(x) + ((y + 1) * tempWidth)] > 0
							&& tempArray[(x) + ((y + 1) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
					if (y > 0
							&& tempArray[(x) + ((y - 1) * tempWidth)] > 0
							&& tempArray[(x) + ((y - 1) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
				}
			}
		}
		xPos--;
		yPos--;
		xPos += drawOffsetX;
		yPos += drawOffsetY;
		int l = xPos + yPos * DrawingArea.width;
		int i1 = 0;
		int j1 = tempHeight;
		int k1 = tempWidth;
		int l1 = DrawingArea.width - k1;
		int i2 = 0;
		if (yPos < DrawingArea.topY) {
			int j2 = DrawingArea.topY - yPos;
			j1 -= j2;
			yPos = DrawingArea.topY;
			i1 += j2 * k1;
			l += j2 * DrawingArea.width;
		}
		if (yPos + j1 > DrawingArea.bottomY) {
			j1 -= (yPos + j1) - DrawingArea.bottomY;
		}
		if (xPos < DrawingArea.topX) {
			int k2 = DrawingArea.topX - xPos;
			k1 -= k2;
			xPos = DrawingArea.topX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if (xPos + k1 > DrawingArea.bottomX) {
			int l2 = (xPos + k1) - DrawingArea.bottomX;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if (!(k1 <= 0 || j1 <= 0)) {
			method349(DrawingArea.pixels, tempArray, i1, l, k1, j1, l1, i2);
		}
	}

	public void method349(int ai[], int ai1[], int j, int k, int l, int i1,
			int j1, int k1) {
		autoUpdate();
		int i;// was parameter
		int l1 = -(l >> 2);
		l = -(l & 3);
		for (int i2 = -i1; i2 < 0; i2++) {
			for (int j2 = l1; j2 < 0; j2++) {
				i = ai1[j++];
				if (i != 0 && i != -1) {
					ai[k++] = i;
				} else {
					k++;
				}
				i = ai1[j++];
				if (i != 0 && i != -1) {
					ai[k++] = i;
				} else {
					k++;
				}
				i = ai1[j++];
				if (i != 0 && i != -1) {
					ai[k++] = i;
				} else {
					k++;
				}
				i = ai1[j++];
				if (i != 0 && i != -1) {
					ai[k++] = i;
				} else {
					k++;
				}
			}

			for (int k2 = l; k2 < 0; k2++) {
				i = ai1[j++];
				if (i != 0 && i != -1) {
					ai[k++] = i;
				} else {
					k++;
				}
			}
			k += j1;
			j += k1;
		}
	}

	public void renderRGBPixels(int i, int j, int ai[], int ai1[], int l, int i1, int j1, int k1, int l1) {
		autoUpdate();
		int k;// was parameter
		int j2 = 256 - k1;
		for (int k2 = -i1; k2 < 0; k2++) {
			for (int l2 = -j; l2 < 0; l2++) {
				k = ai1[i++];
				if (k != 0) {
					try {
						int i3 = ai[l1];
						ai[l1++] = ((k & 0xff00ff) * k1 + (i3 & 0xff00ff) * j2 & 0xff00ff00) + ((k & 0xff00) * k1 + (i3 & 0xff00) * j2 & 0xff0000) >> 8;
					} catch (ArrayIndexOutOfBoundsException e) {
						//e.printStackTrace();
						return;
					}
				} else {
					l1++;
				}
			}
			l1 += j1;
			i += l;
		}
	}

	public void method352(int i, int j, int ai[], int k, int ai1[], int i1,
			int j1, int k1, int l1, int i2) {
		autoUpdate();
		try {
			int j2 = -l1 / 2;
			int k2 = -i / 2;
			int l2 = (int) (Math.sin((double) j / 326.11000000000001D) * 65536D);
			int i3 = (int) (Math.cos((double) j / 326.11000000000001D) * 65536D);
			l2 = l2 * k >> 8;
			i3 = i3 * k >> 8;
			int j3 = (i2 << 16) + (k2 * l2 + j2 * i3);
			int k3 = (i1 << 16) + (k2 * i3 - j2 * l2);
			int l3 = k1 + j1 * DrawingArea.width;
			for (j1 = 0; j1 < i; j1++) {
				int i4 = ai1[j1];
				int j4 = l3 + i4;
				int k4 = j3 + i3 * i4;
				int l4 = k3 - l2 * i4;
				for (k1 = -ai[j1]; k1 < 0; k1++) {
					DrawingArea.pixels[j4++] = myPixels[(k4 >> 16) + (l4 >> 16)
							* myWidth];
					k4 += i3;
					l4 -= l2;
				}

				j3 += l2;
				k3 += i3;
				l3 += DrawingArea.width;
			}

		} catch (Exception _ex) {
		}
	}

	public void method353(int i, double d, int l1) {
		autoUpdate();
		// all of the following were parameters
		int j = 15;
		int k = 20;
		int l = 15;
		int j1 = 256;
		int k1 = 20;
		// all of the previous were parameters
		try {
			int i2 = -k / 2;
			int j2 = -k1 / 2;
			int k2 = (int) (Math.sin(d) * 65536D);
			int l2 = (int) (Math.cos(d) * 65536D);
			k2 = k2 * j1 >> 8;
			l2 = l2 * j1 >> 8;
			int i3 = (l << 16) + (j2 * k2 + i2 * l2);
			int j3 = (j << 16) + (j2 * l2 - i2 * k2);
			int k3 = l1 + i * DrawingArea.width;
			for (i = 0; i < k1; i++) {
				int l3 = k3;
				int i4 = i3;
				int j4 = j3;
				for (l1 = -k; l1 < 0; l1++) {
					int k4 = myPixels[(i4 >> 16) + (j4 >> 16) * myWidth];
					if (k4 != 0)
						DrawingArea.pixels[l3++] = k4;
					else
						l3++;
					i4 += l2;
					j4 -= k2;
				}

				i3 += k2;
				j3 += l2;
				k3 += DrawingArea.width;
			}

		} catch (Exception _ex) {
		}
	}

	public void drawIndexedSprite(IndexedImage index, int xPos, int yPos) {
		autoUpdate();
		xPos += drawOffsetX;
		yPos += drawOffsetY;
		int k = xPos + yPos * DrawingArea.width;
		int l = 0;
		int i1 = myHeight;
		int j1 = myWidth;
		int k1 = DrawingArea.width - j1;
		int l1 = 0;
		if (yPos < DrawingArea.topY) {
			int i2 = DrawingArea.topY - yPos;
			i1 -= i2;
			yPos = DrawingArea.topY;
			l += i2 * j1;
			k += i2 * DrawingArea.width;
		}
		if (yPos + i1 > DrawingArea.bottomY)
			i1 -= (yPos + i1) - DrawingArea.bottomY;
		if (xPos < DrawingArea.topX) {
			int j2 = DrawingArea.topX - xPos;
			j1 -= j2;
			xPos = DrawingArea.topX;
			l += j2;
			k += j2;
			l1 += j2;
			k1 += j2;
		}
		if (xPos + j1 > DrawingArea.bottomX) {
			int k2 = (xPos + j1) - DrawingArea.bottomX;
			j1 -= k2;
			l1 += k2;
			k1 += k2;
		}
		if (!(j1 <= 0 || i1 <= 0)) {
			method355(myPixels, j1, index.myPixels, i1,
					DrawingArea.pixels, 0, k1, k, l1, l);
		}
	}

	public void method355(int ai[], int i, byte abyte0[], int j, int ai1[],
			int k, int l, int i1, int j1, int k1) {
		autoUpdate();
		int l1 = -(i >> 2);
		i = -(i & 3);
		for (int j2 = -j; j2 < 0; j2++) {
			for (int k2 = l1; k2 < 0; k2++) {
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
			}

			for (int l2 = i; l2 < 0; l2++) {
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
			}

			i1 += l;
			k1 += j1;
		}

	}

	public Sprite() { }
	public void autoUpdate() { }

	public int myPixels[];
	public String myDirectory;
	public boolean spriteLoader;
	public int myWidth;
	public int myHeight;
	public int drawOffsetX;
	public int drawOffsetY;
	public int maxWidth;
	public int maxHeight;
}