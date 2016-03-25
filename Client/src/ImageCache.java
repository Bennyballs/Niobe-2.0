package src;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Ruess <mistermaggot@gmail.com>
 */
public class ImageCache {
    
    private final Map<String, ImageCacheFile> files;
    private final RandomAccessFile dat;
    private final RandomAccessFile idx;
    
    public ImageCache(File dat, File idx) throws FileNotFoundException {
        files = new HashMap<String, ImageCacheFile>();
        this.dat = new RandomAccessFile(dat, "rw");
        this.idx = new RandomAccessFile(idx, "rw");
    }
    
    public ImageCache(String dat, String idx) throws FileNotFoundException {
        this(new File(dat), new File(idx));
    }
    
    public ImageCache(File rootDir) throws FileNotFoundException {
        this(new File(rootDir, "spritearchive.dat"), new File(rootDir, "spritearchive.idx"));
    }
    
    public ImageCache(String rootDir) throws FileNotFoundException {
        this(new File(rootDir));
    }
    
    public void loadIndices() throws IOException {
        //System.out.println(idx.length());
        int idxs = idx.readInt();
        //System.out.println("Found: " + idxs);
        for (int i = 0; i < idxs; ++i) {
            String name = "";
            char c;
            while ((c = (char) idx.read()) != '\n') {
                name += c;
            }
            long pos = idx.readLong();
            long len = idx.readLong();
            files.put(name, new ImageCacheFile(pos, len, dat));
            //System.out.println(name + ": " + pos + " " + len);
        }
    }

    public RandomAccessFile getDatFile() {
        return dat;
    }
   
    public int getFileCount() {
    	return files.size();
    }
   
    public byte[] getFile(String dir, String name) throws IOException {
    	dir = dir.toLowerCase();
    	name = name.toLowerCase();
    	ImageCacheFile f = files.get(dir + "\\" + name + ".png");
    	if (f == null) {
    		throw new IOException("The requested file was not found: " + dir + "/" + name);
    	}
    	return f.getData();
    }
    
    public byte[] getFile(String name) throws IOException {
    	name = name + ".png";
        name = name.replaceAll("/", "\\").toLowerCase();
        ImageCacheFile f = files.get(name);
        if (f == null) {
            throw new IOException("The requested file was not found: " + name);
        }
        return f.getData();
    }
}