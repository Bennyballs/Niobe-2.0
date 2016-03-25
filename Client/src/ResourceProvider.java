package src;

import java.io.*;
import java.net.Socket;
import java.util.zip.*;

import src.sign.signlink;

public final class ResourceProvider extends OnDemandFetcherParent implements Runnable {

	public void dump() {
		/*byte[] data = new byte[4096];
		ByteArrayOutputStream builder = new ByteArrayOutputStream();
		File dir = new File("C:\\Users\\Alex\\Desktop\\cacheabc");
		for (int i = 1; i != 6; ++i) {
			System.out.println("Packing: " + i);
			File dirs = new File(dir, String.valueOf(i));
			for (File file : dirs.listFiles()) {
				if (!file.isFile() || !file.canRead())
					continue;

				String name = file.getName();
				if (!name.toLowerCase().endsWith(".dat"))
					continue;

				try {
					int id = Integer.parseInt(name.substring(0, name.length() - 4));
					if (id < 0 || id > 0xffff)
						System.out.println("Invalid id: " + id);

					else {
						FileInputStream in = new FileInputStream(file);
						try {
							builder.reset();
							GZIPOutputStream out = new GZIPOutputStream(builder);
							try {
								while (true) {
									int count = in.read(data, 0, data.length);
									if (count < 0)
										break;
	
									out.write(data, 0, count);
								}
							}
							finally {
								out.close();
							}
						}
						finally {
							in.close();
						}
						builder.write(0);
						builder.write(1);
						byte[] data = builder.toByteArray();
						System.out.println("Packing: " + i + "/" + id + "/" + data.length);
						if (!client.decompressors[i].put(id, data))
							System.out.println("Failed to pack: " + i + "/" + id + "/" + data.length);
						else {
							byte[] output = client.decompressors[i].get(id);
							if (output == null || output.length != data.length)
								System.out.println("MISMATCH!!!~!");
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}*/

		/*for (int i = 0; i != 5; ++i) {
			int index = 1 + i;
			int size = client.decompressors[index].size();
			System.out.println("dumping for: " + index + "/" + size);
			versionPack(index, size);
			crcPack(index, size);
		}*/

		/*try {
			Thread.sleep(5555555L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	private void handleResponse() {
		try {
			int available = in.available();
			if (expectedSize == 0 && available >= 6) {
				for (int index = 0; index < 6; index += in.read(inputBuffer, index, 6 - index));
				int type = inputBuffer[0] & 0xff;
				int id = ((inputBuffer[1] & 0xff) << 8) + (inputBuffer[2] & 0xff);
				int length = ((inputBuffer[3] & 0xff) << 8) + (inputBuffer[4] & 0xff);
				int blocknum = inputBuffer[5] & 0xff;
				current = null;
				for (Resource resource = (Resource) requested.head(); resource != null; resource = (Resource) requested.next()) {
					if (resource.type == type && resource.id == id) {
						current = resource;
					}
					if (current != null) {
						resource.requestAge = 0;
					}
				}
				if (current != null) {
					connectionIdleTicks = 0;
					if (length == 0) {
						signlink.reporterror("Rej: " + type + "," + id);
						current.data = null;
						if (current.incomplete)
							synchronized (completed) {
								completed.append(current);
							}
						else
							current.remove();
						current = null;
					} else {
						if (current.data == null && blocknum == 0) {
							current.data = new byte[length];
						}
						if (current.data == null && blocknum != 0) {
							throw new IOException("missing start of file");
						}
					}
				}
				completedSize = blocknum * 500;
				expectedSize = 500;
				if (expectedSize > length - blocknum * 500)
					expectedSize = length - blocknum * 500;
			}
			if (expectedSize > 0 && available >= expectedSize) {
				byte data[] = inputBuffer;
				int read = 0;
				if (current != null) {
					data = current.data;
					read = completedSize;
				}
				for (int index = 0; index < expectedSize; index += in.read(data, index + read, expectedSize - index));
				if (expectedSize + completedSize >= data.length && current != null) {
					if (client.mainCacheFile[0] != null) {
						client.mainCacheFile[current.type + 1].insertIndex(data.length, data, current.id);
					}
					if (!current.incomplete && current.type == 3) {
						current.incomplete = true;
						current.type = 93;
					}
					if (current.incomplete) {
						synchronized (completed) {
							completed.append(current);
						}
					} else {
						current.remove();
					}
				}
				expectedSize = 0;
			}
		} catch (IOException e) {
			try {
				socket.close();
			} catch (Exception ex) {
			}
			socket = null;
			in = null;
			out = null;
			expectedSize = 0;
		}
	}

	public int getRemaining() {
		synchronized (remainingMandatory) {
			return remainingMandatory.getNodeCount();
		}
	}

	public void disable() {
		running = false;
	}

	public void loadRegions(boolean members) {
		int total = mapRegionIds.length;
		if (members) {
			for (int index = 0; index < total; index++) {
				if (regionPreload[index] != 0) {
					setExtraPriority((byte) 2, 3, mapLandscapes[index]);
					setExtraPriority((byte) 2, 3, mapTerrains[index]);
				}
			}
		}
	}

	public int getModelCount() {
		return checksums[0].length;
	}

	private void closeRequest(Resource resource) {
		try {
			if (socket == null) {
				long l = System.currentTimeMillis();
				if (l - openSocketTime < 4000L) {
					return;
				}
				openSocketTime = l;
				socket = client.openSocket(43593);
				in = socket.getInputStream();
				out = socket.getOutputStream();
				out.write(15);
				for (int index = 0; index < 8; index++) {
					in.read();
				}
				connectionIdleTicks = 0;
			}
			inputBuffer[0] = (byte) resource.type;
			inputBuffer[1] = (byte) (resource.id >> 8);
			inputBuffer[2] = (byte) resource.id;
			if (resource.incomplete) {
				inputBuffer[3] = 2;
			} else if (!client.loggedIn) {
				inputBuffer[3] = 1;
			} else {
				inputBuffer[3] = 0;
			}
			out.write(inputBuffer, 0, 4);
			writeLoopCycle = 0;
			anInt1349 = -10000;
			return;
		} catch (IOException e) {
		}
		try {
			socket.close();
		} catch (Exception ex) {
		}
		socket = null;
		in = null;
		out = null;
		expectedSize = 0;
		anInt1349++;
	}

	public int getAnimCount() {
		return checksums[1].length;
	}

	public void loadMandatory(int type, int id) {
		if (type < 0 || id < 0) {
			return;
		}
		synchronized (remainingMandatory) {
			for (Resource resource = (Resource) remainingMandatory.reverseGetFirst(); resource != null; resource = (Resource) remainingMandatory.reverseGetNext())
				if (resource.type == type && resource.id == id)
					return;

			Resource resource = new Resource();
			resource.type = type;
			resource.id = id;
			resource.incomplete = true;
			synchronized (mandatory) {
				mandatory.append(resource);
			}
			remainingMandatory.insertHead(resource);
		}
	}

	public void start(JagexArchive archive, Client client) {
		this.client = client;
		dump();
		String ver[] = {
			"model_version", "anim_version", "midi_version", "map_version", "texture_version"
		};
		for(int type = 0; type < 5; type++) {
			byte data[] = archive.getData(ver[type]);
			int total = data.length / 2;
			JagexBuffer buffer = new JagexBuffer(data);
			versions[type] = new int[65536];
			priorities[type] = new byte[65536];
			for(int id = 0; id < total; id++) {
				versions[type][id] = buffer.readUnsignedShort();
			}
		}

		String crc[] = {
			"model_crc", "anim_crc", "midi_crc", "map_crc", "texture_crc"
		};
		for(int type = 0; type < 5; type++) {
			byte data[] = archive.getData(crc[type]);
			int total = data.length / 4;
			JagexBuffer crcStream = new JagexBuffer(data);
			checksums[type] = new int[65536];
			for(int id = 0; id < total; id++)
				checksums[type][id] = crcStream.readInt();
		}

		byte data[] = archive.getData("model_index");
		int total = versions[0].length;
		modelIndices = new byte[total];
		for(int id = 0; id < total; id++) {
			if(id < data.length) {
				modelIndices[id] = data[id];
			} else {
				modelIndices[id] = 0;
			}
		}
	
        data = archive.getData("map_index");
        JagexBuffer buffer = new JagexBuffer(data);
        /*try {
			DataOutputStream output = new DataOutputStream(new FileOutputStream(signlink.getCacheLocation() + "map_index.dat"));
			output.write(data, 0, data.length);
			output.close();
        } catch (IOException e) {
			e.printStackTrace();
		}*/
        int mapCount = buffer.readUnsignedShort();
        mapRegionIds = new int[mapCount];
        mapTerrains = new int[mapCount];
        mapLandscapes = new int[mapCount];
        for(int index = 0; index < mapCount; index++) {
            mapRegionIds[index] = buffer.readUnsignedShort();
            mapTerrains[index] = buffer.readUnsignedShort();
            mapLandscapes[index] = buffer.readUnsignedShort();
        }
		data = archive.getData("anim_index");
		buffer = new JagexBuffer(data);
		total = data.length / 2;
		animIndices = new int[total];
		for(int j2 = 0; j2 < total; j2++) {
			animIndices[j2] = buffer.readUnsignedShort();
		}
	
		data = archive.getData("midi_index");
		buffer = new JagexBuffer(data);
		total = data.length;
		midiIndices = new int[total];
		for(int index = 0; index < total; index++) {
			midiIndices[index] = buffer.readUnsignedByte();
		}
		running = true;
		this.client.startRunnable(this, 2);
	}

	public int getVersionCount(int type) {
		return versions[type].length;
	}

	public void run() {
		try {
			while (running) {
				resourceCycle++;
				int i = 20;
				if (maxPriority == 0 && client.mainCacheFile[0] != null)
					i = 50;
				try {
					Thread.sleep(i);
				} catch (Exception _ex) {
				}
				for (int j = 0; j < 100; j++) {
					checkReceived();
					handleFailed();
					if (incompletedCount == 0 && j >= 5)
						break;
					loadExtras();
					if (in != null) {
						handleResponse();
					}
				}
				boolean flag = false;
				for (Resource resource = (Resource) requested.head(); resource != null; resource = (Resource) requested.next())
					if (resource.incomplete) {
						flag = true;
						resource.requestAge++;
						if (resource.requestAge > 50) {
							resource.requestAge = 0;
							closeRequest(resource);
						}
					}

				if (!flag) {
					for (Resource resource = (Resource) requested.head(); resource != null; resource = (Resource) requested.next()) {
						flag = true;
						resource.requestAge++;
						if (resource.requestAge > 50) {
							resource.requestAge = 0;
							closeRequest(resource);
						}
					}
				}
				if (flag) {
					connectionIdleTicks++;
					if (connectionIdleTicks > 750) {
						try {
							socket.close();
						} catch (Exception _ex) {
						}
						socket = null;
						in = null;
						out = null;
						expectedSize = 0;
					}
				} else {
					connectionIdleTicks = 0;
					statusString = "";
				}
				if (client.loggedIn && socket != null && out != null && (maxPriority > 0 || client.mainCacheFile[0] == null)) {
					writeLoopCycle++;
					if (writeLoopCycle > 500) {
						writeLoopCycle = 0;
						inputBuffer[0] = 0;
						inputBuffer[1] = 0;
						inputBuffer[2] = 0;
						inputBuffer[3] = 10;
						try {
							out.write(inputBuffer, 0, 4);
						} catch (IOException _ex) {
							connectionIdleTicks = 5000;
						}
					}
				}
			}
		} catch (Exception e) {
			signlink.reporterror("od_ex " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void method560(int id, int type) {
		if (client.mainCacheFile[0] == null) {
			return;
		}
		if (maxPriority == 0) {
			return;
		}
		Resource resource = new Resource();
		resource.type = type;
		resource.id = id;
		resource.incomplete = false;
		synchronized (extras) {
			extras.append(resource);
		}
	}

	int i1 = 0;

	public Resource getNextNode() {
		Resource resource;
		synchronized (completed) {
			resource = (Resource) completed.popFront();
		}
		if (resource == null)
			return null;
		synchronized (remainingMandatory) {
			resource.unlinkSub();
		}
		if (resource.data == null)
			return resource;
		try {
			gzipBuffer.reset();
			GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(resource.data, 0, resource.data.length - 2));
			try {
				while (true) {
					int count = in.read(gzipInputBuffer, 0, gzipInputBuffer.length);
					if (count < 0)
						break;
				
					gzipBuffer.write(gzipInputBuffer, 0, count);
				}
			}
			finally {
				in.close();
			}
			resource.data = gzipBuffer.toByteArray();
		} catch (IOException ex) {
			resource.data = null;
		}
		return resource;
	}
	
	private ByteArrayOutputStream gzipBuffer;

	public int getClipping(int type, int y, int x) {
		int regionId = (x << 8) + y;
		//System.out.println("regionId=" + regionId + "; i=" + i);
		for (int index = 0; index < mapRegionIds.length; index++) {
			if (mapRegionIds[index] == regionId) {
				if (type == 0)
					return mapTerrains[index];
				else
					return mapLandscapes[index];
			}
		}
		return -1;
	}

	public void loadModel(int id) {
		loadMandatory(0, id);
	}

	public void setExtraPriority(byte priority, int type, int id) {
		if (client.mainCacheFile[0] == null) {
			return;
		}
		priorities[type][id] = priority;
		if (priority > maxPriority) {
			maxPriority = priority;
		}
		totalFiles++;
	}

	public boolean method564(int i) {
		for (int index = 0; index < mapRegionIds.length; index++) {
			if (mapLandscapes[index] == i) {
				return true;
			}
		}
		return false;
	}

	private void handleFailed() {
		incompletedCount = 0;
		completedCount = 0;
		for (Resource resource = (Resource) requested.head(); resource != null; resource = (Resource) requested.next())
			if (resource.incomplete)
				incompletedCount++;
			else
				completedCount++;

		while (incompletedCount < 10) {
			Resource resource = (Resource) toRequest.popFront();
			if (resource == null)
				break;
			if (priorities[resource.type][resource.id] != 0) {
				filesLoaded++;
			}
			priorities[resource.type][resource.id] = 0;
			requested.append(resource);
			incompletedCount++;
			closeRequest(resource);
		}
	}

	public void ignoreExtras() {
		synchronized (extras) {
			extras.clear();
		}
	}

	private void checkReceived() {
		Resource resource;
		synchronized (mandatory) {
			resource = (Resource) mandatory.popFront();
		}
		while (resource != null) {
			byte abyte0[] = null;
			if (client.mainCacheFile[0] != null)
				abyte0 = client.mainCacheFile[resource.type + 1].get(resource.id);
			synchronized (mandatory) {
				if (abyte0 == null) {
					toRequest.append(resource);
				} else {
					resource.data = abyte0;
					synchronized (completed) {
						completed.append(resource);
					}
				}
				resource = (Resource) mandatory.popFront();
			}
		}
	}

	private void loadExtras() {
		while (incompletedCount == 0 && completedCount < 10) {
			if (maxPriority == 0)
				break;
			Resource resource;
			synchronized (extras) {
				resource = (Resource) extras.popFront();
			}
			while (resource != null) {
				if (priorities[resource.type][resource.id] != 0) {
					priorities[resource.type][resource.id] = 0;
					requested.append(resource);
					closeRequest(resource);
					if (filesLoaded < totalFiles)
						filesLoaded++;
					statusString = "Loading extra files - "
							+ (filesLoaded * 100) / totalFiles + "%";
					completedCount++;
					if (completedCount == 10)
						return;
				}
				synchronized (extras) {
					resource = (Resource) extras.popFront();
				}
			}
			for (int j = 0; j < 5; j++) {
				byte abyte0[] = priorities[j];
				int k = abyte0.length;
				for (int l = 0; l < k; l++)
					if (abyte0[l] == maxPriority) {
						abyte0[l] = 0;
						Resource onDemandData_1 = new Resource();
						onDemandData_1.type = j;
						onDemandData_1.id = l;
						onDemandData_1.incomplete = false;
						requested.append(onDemandData_1);
						closeRequest(onDemandData_1);
						if (filesLoaded < totalFiles)
							filesLoaded++;
						statusString = "Loading extra files - "
								+ (filesLoaded * 100) / totalFiles + "%";
						completedCount++;
						if (completedCount == 10)
							return;
					}

			}

			maxPriority--;
		}
	}

	public boolean method569(int i) {
		return midiIndices[i] == 1;
	}

	public ResourceProvider()
	{
		requested = new Deque();
		statusString = "";
		//crc32 = new CRC32();
		inputBuffer = new byte[500];
		priorities = new byte[5][];
		extras = new Deque();
		running = true;
		//waiting = false;
		completed = new Deque();
		gzipInputBuffer = new byte[4096];
		remainingMandatory = new Queue();
		versions = new int[5][];
		checksums = new int[5][];
		toRequest = new Deque();
		mandatory = new Deque();
		gzipBuffer = new ByteArrayOutputStream(8 * 4096);
	}

	private int totalFiles;
	private final Deque requested;
	private int maxPriority;
	public String statusString;
	private int writeLoopCycle;
	private long openSocketTime;
	private int[] mapLandscapes;
	//private final CRC32 crc32;
	private final byte[] inputBuffer;
	public int resourceCycle;
	private final byte[][] priorities;
	private Client client;
	private final Deque extras;
	private int completedSize;
	private int expectedSize;
	private int[] midiIndices;
	public int anInt1349;
	private int[] mapTerrains;
	private int filesLoaded;
	private boolean running;
	private OutputStream out;
	private int[] regionPreload;
	//private boolean waiting;
	private final Deque completed;
	private final byte[] gzipInputBuffer;
	private int[] animIndices;
	private final Queue remainingMandatory;
	private InputStream in;
	private Socket socket;
	private final int[][] versions;
	private final int[][] checksums;
	private int incompletedCount;
	private int completedCount;
	private final Deque toRequest;
	private Resource current;
	private final Deque mandatory;
	private int[] mapRegionIds;
	private byte[] modelIndices;
	private int connectionIdleTicks;
}