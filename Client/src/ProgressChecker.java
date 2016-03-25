package src;

import java.io.File;

class ProgressChecker extends RSApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8758190346542545915L;
	public static int per = 0;
	public static int per2 = 0;
	public String filetocheck;
	public long filelength;

	public ProgressChecker(String s, long l) {
		filetocheck = s;
		filelength = l;
		Thread thread = new Thread(this);
		thread.start();
	}

	public void run() {
		long l = 0L;
		per = 0;
		File file = new File(filetocheck);
		do {
			if (l > filelength) {
				break;
			}
			try {
				Thread.sleep(5L);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			l = file.length();
			per = (int) (((double) l / (double) filelength) * 100D);
			if (per > per2) {
				per2 = per;
				try {
					Client.instance.drawLoadingText(per,
							per != 100 ? "Downloading cache " + per + "%"
									: "Decompressing library");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} while (true);
	}

}