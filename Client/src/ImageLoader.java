package src;


import java.io.File;

import src.sign.signlink;

/**
 * The image loader class.
 * @author Daniel Ruess <mistermaggot@gmail.com>
 * @author Joshua Lipson (Galkon)
 */
public class ImageLoader {
 
    public ImageLoader(JagexArchive archive) {
		try {
			if (archive != null) {
				File index = new File(signlink.getCacheLocation() + "spritearchive.idx");
				File data = new File(signlink.getCacheLocation() + "spritearchive.dat");
				if (!index.exists() || index.length() <= 0) {
					DataUtils.writeFile(archive.getData("spritearchive.idx"), signlink.getCacheLocation() + "spritearchive.idx");
				}
				if (!data.exists() || data.length() <= 0) {
					DataUtils.writeFile(archive.getData("spritearchive.dat"), signlink.getCacheLocation() + "spritearchive.dat");
				}
			}
			cache = new ImageCache(new File(signlink.getCacheLocation()));
	        cache.loadIndices();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * The image cache instance.
     */
    public static ImageCache cache;

    /**
     * Returns the image cache instance.
     * @return
     */
    public ImageCache getCache() {
    	return cache;
    }
}