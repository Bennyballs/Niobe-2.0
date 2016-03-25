package src;


import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import src.sign.signlink;

public class MapDecompressor {

	public static List<Integer> mapIndices = null;
	public static Map<Integer, byte[]> mapBuffer = new HashMap<Integer, byte[]>();
	public static String location = signlink.getCacheLocation();

	public static byte[] grabMap(int id) throws IOException {
		if (mapIndices == null)
			loadIndex2();
		if (mapBuffer.get(id) == null) {
			RandomAccessFile raf_cache = new RandomAccessFile(location + "main_file_cache.dat2", "rw");
			RandomAccessFile raf_index = new RandomAccessFile(location + "main_file_cache.idx5", "rw");
			int pos = getIndexPosition(id);
			if (pos == -1)
				return null;
			raf_index.seek(pos * 12);
			raf_cache.seek(raf_index.readInt());
			byte[] b = new byte[raf_index.readInt()];
			raf_cache.readFully(b);
			b = inflate(b, raf_index.readInt());
			mapBuffer.put(id, b);
			return mapBuffer.get(id);
		}
		return mapBuffer.get(id);
	}

	public static int getTotalMaps() {
		return (int) new File(location + "main_file_cache.idx6").length();
	}

	public static void loadIndex2() throws IOException {
		mapIndices = new ArrayList<Integer>();
		DataInputStream dis = new DataInputStream(new FileInputStream(location + "main_file_cache.idx6"));
		for (int i = 0; i < (int) new File(location + "main_file_cache.idx6").length() / 2; i++)
			mapIndices.add((int) dis.readShort());
	}

	public static int getIndexPosition(int id) throws IOException {
		if (mapIndices.contains(id))
			for (int i = 0; i < mapIndices.size(); i++)
				if (mapIndices.get(i) == id)
					return i;
		return -1;
	}

	public static byte[] inflate(byte[] b, int l) throws IOException {
		byte[] buf = new byte[l];
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		DataInputStream dis = new DataInputStream(new GZIPInputStream(bais));
		dis.readFully(buf, 0, buf.length);
		dis.close();
		return buf;
	}
}