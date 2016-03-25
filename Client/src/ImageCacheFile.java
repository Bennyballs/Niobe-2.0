package src;


import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Daniel Ruess <mistermaggot@gmail.com>
 */
public class ImageCacheFile {
    private final long pos;
    private final long len;
    private final RandomAccessFile dat;
    
    public ImageCacheFile(long pos, long len, RandomAccessFile dat) {
        this.pos = pos;
        this.len = len;
        this.dat = dat;
    }
    
    public byte[] getData() throws IOException {
        byte[] data = new byte[(int) this.len];
        synchronized (dat) {
            dat.seek(pos);
            dat.read(data);
        }
        return data;
    }

}