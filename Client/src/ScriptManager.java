package src;

public class ScriptManager
{
	public ScriptManager(Client instance)
	  {
	    this.instance = instance;

	    /*this.mapX = 431;//209
	    this.mapY = 462;//709
		mapZ = 0;
		loop = false;
		int T = 0;
		//varrock: 6800, 10900
		actions = new int[] {
			6711, 5920, -1901, 2032, 383, 0, 6597, 7213, -665, 2034, 128, 0, 6454, 8791, -715, 1983, 133, 0, 6452, 9108,
			-1017, 1951, 208, 0,

			6131, 10220, -1420, 1794, 288, T, 6135, 11541, -1420, 1277, 288, T, 7432, 11543, -1420, 772, 288, T, 7440,
			10224, -1420, 256, 288, T,
		};*/
		mapX = 265;
		mapY = 489;
		mapZ = 0;
		loop = false;
		int T = 0;
		actions = new int[] {
			7963, 6612, -425, 500, 128, T,
			6431, 6612, -1073, 500, 128, T,
			5792, 6612, -1145, 500, 128, T,
			5792, 6612, -1145, 500, 128, T,
			4500, 6612, -950, 500, 128, T,
			2750, 6568, -1500, 556, 128, T,
			2750, 6568, -1500, 2900, 150, T(4),
		};
		/*mapX = 209;
		mapY = 709;
		mapZ = 0;
		loop = false;
		int T = 0;
		actions = new int[] {
			//7582, 9403, -759, 1033, 128, T,
			//7581, 7931, -580, 1034, 128, T,
			//6193, 6717, -675, 1033, 128, T,
			//6193, 5966, -727, 1033, 128, T, //-- cool ending 209/709

			5804, 6818, -983, 1130, 163, T,
			7262, 6806, -1153, 1418, 183, T,
			8317, 7593, -1388, 1683, 233, T,
			
			9258, 9134, -1765, 348, 311, T,
			
			7638, 9070, -1256, 475, 249, T,
			6660, 9037, -615, 459, 128, T,
			
			//6089, 9581, -698, 635, 158, T, //not so cool ending?
			//5578, 9715, -579, 650, 128, T,
		};
		/*actions = new int[] {
			6740, 7147, -851, 1124, 128, T,
			7862, 6396, -1003, 1739, 152, T,
			7779, 9294, -1002, 199, 169, T,
			6909, 10672, -1271, 509, 242, T,
			4921, 10681, -1400, 840, 287, T,
			6000, 9140, -919, 550, 208, T,
		};*/
		reset();
	}

	private static final int T(int n)
	{
		return ~n + 2;
	}

	//Unused - removed CameraActionBuilder.java
	/*private static int[] smooth(int[] n, int pre)
	{
		int t = n.length / 6;
		CameraActionBuilder c = new CameraActionBuilder(t);
		if (t != 0)
		{
			int xP = n[0];
			int yP = n[1];
			int zP = n[2];
			int xC = n[3] & 2047;
			int yC = n[4] & 2047;
			int T = n[5];
			int t1 = 6;
			c.append(xP, yP, zP, xC, yC, T);
			while (t != 1)
			{
				int xPN = n[t1];
				int yPN = n[1 + t1];
				int zPN = n[2 + t1];
				int xCN = n[3 + t1] & 2047;
				int yCN = n[4 + t1] & 2047;
				int TN = n[5 + t1];
				t1 += 6;
				--t;
			}
		}
		return c.toIntArray();
	}*/

	@SuppressWarnings("unused")
	private static int curve(int n, int n1)
	{
		int n2 = n - n1;
		int n3 = 2048 - n1 + n;
		int n4 = 2048 - n + n1;
		int n5 = Math.abs(n2);
		int n6 = Math.abs(n3);
		int n7 = Math.abs(n4);
		if (n5 < n6 && n5 < n7)
			return n2;

		if (n6 < n5 && n6 < n7)
			return n3;

		if (n7 < n5 && n7 < n6)
			return n4;

		return n2;
	}

	private static double curve(double d, double d1)
	{
		double d2 = d - d1;
		double d3 = 2048.0D - d1 + d;
		double d4 = 2048.0D - d + d1;
		double d5 = Math.abs(d2);
		double d6 = Math.abs(d3);
		double d7 = Math.abs(d4);
		if (d5 < d6 && d5 < d7)
			return d2;

		if (d6 < d5 && d6 < d7)
			return d3;

		if (d7 < d5 && d7 < d6)
			return d4;

		return d2;
	}

	private static double p5(double d)
	{
		d = Math.abs(d);
		return d * d * d * d * d;
	}

	public void cycle()
	{
		boolean justReset = false;
		while (cycles < 1)
		{
			int ttl = actions.length / 6;
			if (pos < 0 || pos >= ttl)
			{
				if (loop && !justReset)
				{
					justReset = true;
					pos = 0;
					cycles = 0;
					//instance.resetMap1();
					instance.setNorth();
					if (instance.plane != mapZ)
						instance.plane = mapZ;

					if (instance.mapX != mapX || instance.mapY != mapY)
						instance.loadMap(mapX, mapY);

					instance.resetMap2();
				}
				return;
			}
			int idx = (pos << 2) + (pos << 1);
			++pos;
			if (actions[5 + idx] == 0x80000000)
			{
				//instance.resetMap1();
				instance.setNorth();
				if (instance.plane != actions[idx + 2])
					instance.plane = actions[idx + 2];

				if (instance.mapX != actions[idx] || instance.mapY != actions[idx + 1])
					instance.loadMap(actions[idx], actions[idx + 1]);

				instance.resetMap2();
				break;
			}
			double xPos = (double) actions[idx] - xCameraPos;
			double yPos = (double) actions[1 + idx] - yCameraPos;
			double zPos = (double) actions[2 + idx] - zCameraPos;
			double xCurve = curve((double) (actions[3 + idx] & 2047), xCameraCurve);
			double yCurve = curve((double) (actions[4 + idx] & 2047), yCameraCurve);
			cycles = actions[5 + idx];
			if (cycles < 1)
			{
				int mult = ~cycles + 2;
				double cyclesD = Math.pow(p5(xPos) + p5(yPos) + p5(zPos) + p5(xCurve) + p5(yCurve), 1.0D / 5.0D);
				cycles = round(cyclesD / 7.5D);
				if (cycles < 1)
					cycles = 1;
				cycles *= mult;
			}
			xCameraPosRate = xPos / (double) cycles;
			yCameraPosRate = yPos / (double) cycles;
			zCameraPosRate = zPos / (double) cycles;
			xCameraCurveRate = xCurve / (double) cycles;
			yCameraCurveRate = yCurve / (double) cycles;
		}
		if (cycles > 0)
		{
			xCameraPos += xCameraPosRate;
			yCameraPos += yCameraPosRate;
			zCameraPos += zCameraPosRate;
			xCameraCurve = sgn(xCameraCurve + xCameraCurveRate, 2048.0D);
			yCameraCurve = sgn(yCameraCurve + yCameraCurveRate, 2048.0D);
			//System.out.println(round(xCameraCurve));
			update();
			--cycles;
		}
	}

	private static double sgn(double d, double d1)
	{
		d %= d1;
		return d < 0.0D ? d + d1:d;
		//return d - Math.floor(d / d1) * d1;
	}

	private static int round(double d)
	{
		/*if (d < 0.0D)
			d -= 0.5D;
		else
			d += 0.5D;*/
		return (int) d;
	}

	public void update()
	{
		instance.xCameraPos = round(xCameraPos);
		instance.yCameraPos = round(yCameraPos);
		instance.zCameraPos = round(zCameraPos);
		instance.xCameraCurve = round(xCameraCurve) & 2047;
		instance.yCameraCurve = round(yCameraCurve) & 2047;
	}

	public void reset()
	{
		pos = 1;
		cycles = 0;
		if (actions.length < 6)
			return;

		xCameraPos = (double) actions[0];
		yCameraPos = (double) actions[1];
		zCameraPos = (double) actions[2];
		xCameraCurve = (double) (actions[3] & 2047);
		yCameraCurve = (double) (actions[4] & 2047);
		update();
	}

	public void stop()
	{
	}

	private boolean loop;
	private double xCameraPos;
	private double yCameraPos;
	private double zCameraPos;
	private double xCameraCurve;
	private double yCameraCurve;
	private int cycles;
	private double xCameraPosRate;
	private double yCameraPosRate;
	private double zCameraPosRate;
	private double xCameraCurveRate;
	private double yCameraCurveRate;
	private int pos;
	private Client instance;
	private int[] actions;
	public int mapX;
	public int mapY;
	public int mapZ;
}
		