package src.util;

import java.util.HashSet;
import java.util.Set;

import src.RSImage;

public class Misc {

	/**
	 * Gets the average (most common) color of an image.
	 * @param image
	 * @return
	 */
	public static int getAverageColor(RSImage image) {
		int r = 0;
		int g = 0;
		int b = 0;
		int colorCount = image.myPixels.length;
		for (int index = 0; index < colorCount; index++) {
			r += image.myPixels[index] >> 16 & 0xff;
			g += image.myPixels[index] >> 8 & 0xff;
			b += image.myPixels[index] & 0xff;
		}
		int rgb = (r / colorCount << 16) + (g / colorCount << 8) + b / colorCount;
		if (rgb == 0) {
			rgb = 1;
		}
		return rgb;
	}

	/**
	 * Reverses the order of values in a given int array.
	 * @param data
	 */
	public static int[] reverse(int[] data) {
	    int left = 0;
	    int right = data.length - 1;
	    while(left < right) {
	        int temp = data[left];
	        data[left] = data[right];
	        data[right] = temp;
	        left++;
	        right--;
	    }
	    return data;
	}

	/**
	 * Returns whether or not an array contains duplicates.
	 * @param list
	 * @return
	 */
	public static boolean containsDuplicates(final int[] list) {
		Set<Integer> lump = new HashSet<Integer>();
		for (int i : list) {
			if (lump.contains(i)) {
				return true;
			}
			lump.add(i);
		}
		return false;
	}

}
