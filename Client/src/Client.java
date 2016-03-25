package src;

import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.zip.CRC32;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

import src.accounts.Accounts;
import src.accounts.Create;
import src.accounts.Recover;
import src.constants.Constants;
import src.constants.SizeConstants;
import src.constants.SkillConstants;
import src.interfaces.RSInterface;
import src.sign.signlink;
import src.util.TextUtils;

public class Client extends RSApplet {

	public static final long serialVersionUID = 1L;
	public static final String CLIENT_NAME = "Niobe";
	public static boolean demo = false;
	public boolean enableServerIP = false;
	
	public Desktop SearchWeb = Desktop.getDesktop();

	public Calendar calendar = new GregorianCalendar();
	
	/**
	 * Cuts a string into more than one line if it exceeds the specified max width.
	 * @param newRegularFont2
	 * @param string
	 * @param maxWidth
	 * @return
	 */
	public static String[] splitString(RSFontSystem newRegularFont2, String prefix, String string, int maxWidth, boolean ranked) {
		maxWidth -= newRegularFont2.getTextWidth(prefix) + (ranked ? 14 : 0);
		if (newRegularFont2.getTextWidth(prefix + string) + (ranked ? 14 : 0) <= maxWidth) {
			return new String[]{ string };
		}
		String line = "";
		String[] cut = new String[2];
		boolean split = false;
		char[] characters = string.toCharArray();
		int space = -1;
		for (int index = 0; index < characters.length; index++) {
			char c = characters[index];
			line += c;
			if (c == ' ') {
				space = index;
			}
			if (!split) {
				if (newRegularFont2.getTextWidth(line) + 10 > maxWidth) {
					if (space != -1 && characters[index - 1] != ' ') {
						cut[0] = line.substring(0, space);
						line = line.substring(space);
					} else {
						cut[0] = line;
						line = "";
					}
					split = true;
				}
			}
		}
		if (line.length() > 0) {
			cut[1] = line;
		}
		return cut;
	}
	
	public String getMeridiem() {
		if (calendar.get(Calendar.AM_PM) == 0)
			return "AM";
		else
			return "PM";
	}

	public int getHour() {
		return calendar.get(Calendar.HOUR);
	}

	public int getMinute() {
		return calendar.get(Calendar.MINUTE);
	}

	public static int clientAction;

	public static void doClientAction(int action) {
		switch (action) {

		default:
			break;
		}
	}

	public static int REGULAR_WIDTH = 765, REGULAR_HEIGHT = 503,
			RESIZABLE_WIDTH = 800, RESIZABLE_HEIGHT = 600;

	public void drawUnfixedGame() {
		if (!isFixed()) {
			drawChatArea();
			drawTabArea();
			drawMinimap();
		}
	}

	public final String formatAmount(long amount) {
		String format = "Too high!";
		if (amount >= 0 && amount < 100000) {
			format = String.valueOf(amount);
		} else if (amount >= 100000 && amount < 10000000) {
			format = amount / 1000 + "K";
		} else if (amount >= 10000000 && amount < 10000000000L) {
			format = amount / 1000000 + "M";
		} else if (amount >= 10000000000L && amount < 10000000000000L) {
			format = amount / 1000000000 + "B";
		} else if (amount >= 10000000000000L && amount < 10000000000000000L) {
			format = amount / 1000000000000L + "T";
		} else if (amount >= 10000000000000000L && amount < 1000000000000000000L) {
			format = amount / 1000000000000000L + "QD";
		} else if (amount >= 1000000000000000000L && amount < Long.MAX_VALUE) {
			format = amount / 1000000000000000000L + "QT";
		}
		return format;
	}

	public int getAmountColor(long amount) {
		if (amount >= 10000000000L)
			return 0x00FFFF;
		if (amount >= 10000000)
			return 0x00FF80;
		if (amount >= 100000)
			return 0xFFFFFF;
		if (amount >= 1)
			return 0xFFFF00;
		return 0xFFFFFF;
	}

	public boolean showXP;
	public boolean showBonus;
	public static int totalSettings = 15;
	public static boolean settingsEnabled = true;
	public static boolean rememberMe,
			menuAnimations, timeStamp, regionLighting = true;

	public void applySettings() {
		setConfig(780, 2);
		setConfig(800, clientSize + 1);
		setConfig(801, rememberMe ? 1 : 0);
		setConfig(804, menuAnimations ? 1 : 0);
		setConfig(805, timeStamp ? 1 : 0);
	}

	public void readSettings() {
		File file = new File(signlink.getCacheLocation() + "settings.dat");
		if (!file.exists()) {
			return;
		}
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(
					signlink.getCacheLocation() + "settings.dat"));
			int totalSettings = in.readShort();
			for (int index = 0; index < totalSettings; index++) {
				readSettingsValues(in);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readSettingsValues(DataInputStream in) {
		try {
			do {
				int opCode = in.readByte();
				if (opCode == 1) {
					rememberMe = true;
				}
				if (opCode == 2) {
					in.readUTF();
				}
				if (opCode == 3) {
					in.readUTF();
				}
				if (opCode == 6) {
					canGainXP = true;
				}
				if (opCode == 7) {
					totalXP = in.readInt();
				}
				if (opCode == 9) {
					menuAnimations = true;
				}
				if (opCode == 10) {
					timeStamp = true;
				}
				if (opCode == 13) {
					regionLighting = true;
				}
				if (opCode == 14) {
					leftClick = in.readInt();
				}
			} while (true);
		} catch (IOException e) {
		}
	}

	public void writeSettings() {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					signlink.getCacheLocation() + "settings.dat"));
			out.writeShort(totalSettings);
			for (int index = 0; index < totalSettings; index++) {
				if (rememberMe) {
					out.writeByte(1);
				}
				if (rememberMe) {
					out.writeByte(2);
					out.writeUTF(myUsername);
				}
				if (rememberMe) {
					out.writeByte(3);
					out.writeUTF(myPassword);
				}
				if (canGainXP) {
					out.writeByte(6);
				}
				if (totalXP > 0) {
					out.writeByte(7);
					out.writeInt(totalXP);
				}
				if (menuAnimations) {
					out.writeByte(9);
				}
				if (timeStamp) {
					out.writeByte(10);
				}
				if (regionLighting) {
					out.writeByte(13);
				}
				if (leftClick != -1 && leftClick < 7) {
					out.writeByte(14);
					out.writeInt(leftClick);
				}
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dumpSprite(Sprite image) {
		File directory = new File(signlink.getCacheLocation() + "rsimg/dump/");
		if (!directory.exists()) {
			directory.mkdir();
		}
		BufferedImage bufferedimage = new BufferedImage(image.myWidth,
				image.myHeight, 1);
		bufferedimage.setRGB(0, 0, image.myWidth, image.myHeight,
				image.myPixels, 0, image.myWidth);
		System.out.println(bufferedimage.getAlphaRaster());
		Graphics2D graphics2d = bufferedimage.createGraphics();
		graphics2d.dispose();
		try {
			File file1 = new File(signlink.getCacheLocation() + "rsimg/dump/"
					+ (directory.listFiles().length + 1) + ".png");
			ImageIO.write(bufferedimage, "png", file1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getDataForItem(int itemId, int part) {
		ItemDef itemDef = ItemDef.forId(itemId);
		String partOne = "modelID: "
				+ itemDef.modelID
				+ ", "
				+ (itemDef.maleWearModel2 != -1 ? "maleModel1: "
						+ itemDef.maleWearModel1 + ", femaleModel1: "
						+ itemDef.femaleWearModel1 : "");
		String partTwo = itemDef.maleWearModel1 != -1 ? ", maleModel2: "
				+ itemDef.maleWearModel2 + ", femaleModel2: "
				+ itemDef.femaleWearModel2 : "";
		String partThree = itemDef.modelZoom + ", " + itemDef.modelRotation1
				+ ", " + itemDef.modelRotation2 + ", " + itemDef.modelOffset1
				+ ", " + itemDef.modelOffset2;
		return part == 1 ? partOne + partTwo : partThree;
	}

	public static int totalRead = 0;

	public void preloadModels() {
		File file = new File(signlink.getCacheLocation() + "rsmdl" + System.getProperty("file.separator"));
		File[] fileArray = file.listFiles();
		for (int y = 0; y < fileArray.length; y++) {
			String s = fileArray[y].getName();
			byte[] buffer = readFile(signlink.getCacheLocation() + "rsmdl" + System.getProperty("file.separator") + s);
			Model.method460(buffer, Integer.parseInt(getFileNameWithoutExtension(s)));
		}
	}

	public int mapX, mapY;

	public void requestObject(int x, int y, int objectId, int face, int type,
			int height) {
		int mX = mapX - 6;
		int mY = mapY - 6;
		int x2 = x - (mX * 8);
		int y2 = y - (mY * 8);
		int i15 = 40 >> 2;
		int l17 = objectClassType[i15];
		if (y2 > 0 && y2 < 103 && x2 > 0 && x2 < 103) {
			requestGameObjectSpawn(-1, objectId, face, l17, y2, type, height, x2, 0);
		}
	}

	public static final byte[] readFile(String s) {
		try {
			byte abyte0[];
			File file = new File(s);
			int i = (int) file.length();
			abyte0 = new byte[i];
			DataInputStream datainputstream = new DataInputStream(
					new BufferedInputStream(new FileInputStream(s)));
			datainputstream.readFully(abyte0, 0, i);
			datainputstream.close();
			totalRead++;
			return abyte0;
		} catch (Exception e) {
			System.out.println((new StringBuilder()).append("Read error: ")
					.append(s).toString());
			return null;
		}
	}

	public static Client getClient() {
		return instance;
	}

	public int positions[] = new int[2000];
	public int landScapes[] = new int[2000];
	public int objects[] = new int[2000];

	public void readMapPositions() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					signlink.getCacheLocation() + "rsmap/maplocation.txt"));
			String s;
			int i = 0;
			while ((s = in.readLine()) != null) {
				positions[i] = Integer.parseInt(s.substring(s.indexOf("=") + 1,
						s.indexOf("(")));
				landScapes[i] = Integer.parseInt(s.substring(
						s.indexOf("(") + 1, s.indexOf(")")));
				objects[i] = Integer.parseInt(s.substring(s.indexOf("[") + 1,
						s.indexOf("]")));
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Place a String on the clipboard, and make this class the owner of the
	 * Clipboard's contents.
	 */
	public static void setClipboardContents(String aString) {
		StringSelection stringSelection = new StringSelection(aString);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}

	/**
	 * Get the String residing on the clipboard.
	 * 
	 * @return any text found on the Clipboard; if none found, return an empty
	 *         String.
	 */
	public static String getClipboardContents() {
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// odd: the Object param of getContents is not currently used
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null)
				&& contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents
						.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException ex) {
				// highly unlikely since we are using a standard DataFlavor
				System.out.println(ex);
				ex.printStackTrace();
			} catch (IOException ex) {
				System.out.println(ex);
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String capitalize(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)),
						s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1),
							Character.toUpperCase(s.charAt(i + 1)),
							s.substring(i + 2));
				}
			}
		}
		return s;
	}

	public int MapX, MapY;
	public static int spellID = 0;

	public static String intToKOrMilLongName(int i) {
		String s = String.valueOf(i);
		for (int k = s.length() - 3; k > 0; k -= 3)
			s = s.substring(0, k) + "," + s.substring(k);
		if (s.length() > 8)
			s = "@gre@" + s.substring(0, s.length() - 8) + " million @whi@("
					+ s + ")";
		else if (s.length() > 4)
			s = "@cya@" + s.substring(0, s.length() - 4) + "K @whi@(" + s + ")";
		return " " + s;
	}

	public final String methodR(int j) {
		if (j >= 0 && j < 10000)
			return String.valueOf(j);
		if (j >= 10000 && j < 10000000)
			return j / 1000 + "K";
		if (j >= 10000000 && j < 999999999)
			return j / 1000000 + "M";
		if (j >= 999999999)
			return "*";
		else
			return "?";
	}

	public void stopMidi() {
		signlink.midifade = 0;
		signlink.midi = "stop";
	}

	public boolean menuHasAddFriend(int j) {
		if (j < 0)
			return false;
		int k = menuActionID[j];
		if (k >= 2000)
			k -= 2000;
		return k == 337;
	}

	public NumberFormat format = NumberFormat.getInstance();
	public static int totalXP = 0;

	public LinkedList<XPGain> gains = new LinkedList<XPGain>();

	public void addXP(int skillID, int xp) {
		if (xp != 0 && canGainXP) {
			totalXP += xp;
			gains.add(new XPGain(skillID, xp));
			writeSettings();
		}
	}

	public class XPGain {
		/**
		 * The skill which gained the xp
		 */
		public int skill;

		/**
		 * The XP Gained
		 */
		public int xp;
		public int y;
		public int alpha = 0;

		public XPGain(int skill, int xp) {
			this.skill = skill;
			this.xp = xp;
		}

		public void increaseY() {
			y++;
		}

		public int getSkill() {
			return skill;
		}

		public int getXP() {
			return xp;
		}

		public int getY() {
			return y;
		}

		public int getAlpha() {
			return alpha;
		}

		public void increaseAlpha() {
			alpha += alpha < 256 ? 25 : 0;
			alpha = alpha > 256 ? 256 : alpha;
		}

		public void decreaseAlpha() {
			alpha -= alpha > 0 ? 25 : 0;
			alpha = alpha > 256 ? 256 : alpha;
		}
	}

	public int gainedExpY = 0;
	public static boolean xpGained = false, canGainXP = true;

	public void displayXPCounter() {
		int x = clientSize == 0 ? 419 : clientWidth - 310;
		int y = clientSize == 0 ? 0 : -36;
		int currentIndex = 0;
		int offsetY = 0;
		int stop = 70;
		cacheSprite[40].drawSprite(x, clientSize == 0 ? 50 : 48 + y);
		normalFont.method389(true, x + 4, 0xffffff,
				"XP: " + format.format(totalXP), (clientSize == 0 ? 63 : 61)
						+ y);
		if (!gains.isEmpty()) {
			Iterator<XPGain> it$ = gains.iterator();
			while (it$.hasNext()) {
				XPGain gain = it$.next();
				if (gain.getY() < stop) {
					if (gain.getY() <= 10) {
						gain.increaseAlpha();
					}
					if (gain.getY() >= stop - 10) {
						gain.decreaseAlpha();
					}
					gain.increaseY();
				} else if (gain.getY() == stop) {
					it$.remove();
				}
				Sprite sprite = cacheSprite[gain.getSkill() + 41];
				if (gains.size() > 1) {
					offsetY = (clientSize == 0 ? 0 : -20) + (currentIndex * 28);
				}
				if (gain.getY() < stop) {
					sprite.drawSprite((x + 15) - (sprite.myWidth / 2),
							gain.getY() + offsetY + 66 - (sprite.myHeight / 2),
							gain.getAlpha());
					newSmallFont.drawBasicString("<trans=" + gain.getAlpha()
							+ ">+" + format.format(gain.getXP()) + "xp",
							x + 30, gain.getY() + offsetY + 70, 0xCC6600, 0);
				}
				currentIndex++;
			}
		}
	}

	public static int consoleAlpha = 0;

	public void drawConsole() {
		if (consoleOpen) {
			consoleAlpha += consoleAlpha < 100 ? 10 : 0;
			int height = isFixed() ? 334
					: clientHeight / 2;
			DrawingArea474.drawAlphaFilledPixels(0, 0, clientWidth, height,
					5320850, consoleAlpha < 101 ? consoleAlpha : 100);
			DrawingArea.drawPixels(1, height - 19, 0, 16777215, clientWidth);
			newBoldFont.drawBasicString("-->", 11, height - 6, 16777215, 0);
			if (loopCycle % 20 < 10) {
				newBoldFont.drawBasicString(consoleInput + "|", 38, height - 6,
						16777215, 0);
				return;
			} else {
				newBoldFont.drawBasicString(consoleInput, 38, height - 6,
						16777215, 0);
				return;
			}
		} else {
			consoleAlpha -= consoleAlpha > 0 ? 10 : 0;
			consoleAlpha = consoleAlpha < 0 ? 0 : consoleAlpha;
			if (consoleAlpha > 0) {
				int height = isFixed() ? 334 : clientHeight / 2;
				DrawingArea474.drawAlphaFilledPixels(0, 0, clientWidth, height,
						5320850, consoleAlpha);
			}
		}
	}

	public void drawConsoleArea() {
		if (consoleOpen) {
			for (int index = 0, positionY = isFixed() ? 308 : (clientHeight / 2) - 26; index < 17; index++, positionY -= 18) {
				if (consoleMessages[index] != null) {
					newRegularFont.drawBasicString(consoleMessages[index], 9,
							positionY, 16777215, 0);
				}
			}
			if (consoleMessages[0].length() <= 0) {
				sendConsoleMessage(
						"Type 'commands' for a list of commands, 'cls' to clear the console, and 'info' for more "
								+ (clientWidth > 765 ? "information." : ""),
						false);
				if (clientWidth <= 765) {
					sendConsoleMessage("information.", false);
				}
			}
		}
	}

	public void sendConsoleMessage(String s, boolean response) {
		if (backDialogID == -1)
			inputTaken = true;
		for (int index = 16; index > 0; index--) {
			consoleMessages[index] = consoleMessages[index - 1];
		}
		if (response) {
			consoleMessages[0] = "--> " + s;
		} else {
			consoleMessages[0] = s;
		}
	}
	
	String twitter = "";
	public void getTwitterStatus() {
		try {
		URL url = new URL("http://search.twitter.com/search.atom?q=from:Battle Royalersps&rpp=1");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String results = reader.readLine();
			reader.close();
			if (results != null) {
				if (results.length() > 0) {
					int i = results.lastIndexOf("<title>"); int i2 = results.lastIndexOf("</title>");
					String s = results.substring(i, i2);
					twitter = s;
				}
			}
		} catch (MalformedURLException e) {
				System.out.println("Malformed URL Exception packet getTwitterStatus()");
			} catch (IOException e) {
				System.out.println("IO Exception");
			}
	}
	
	public void getConsoleCommands() {
		sendConsoleMessage("'cls' - clear the console.", false);
		sendConsoleMessage("'data' - toggles data display (fps, memory usage, etc).", false);
		if (myRank > 1) {
			sendConsoleMessage("'config #' - displays the configs for specified child id.", false);
			sendConsoleMessage("'unpack_interfaces' - unpacks all interfaces.", false);
			sendConsoleMessage("'pos' - prints out your position values.", false);
			sendConsoleMessage("'reset' - resets your current skill levels.", false);
			sendConsoleMessage("'master' - raises all your levels to their maximum level.", false);
			sendConsoleMessage("'setlevel $ #' - raises your skill ($) to said amount (#).", false);
			sendConsoleMessage("'anim #' - performs the specified animation (#).", false);
			sendConsoleMessage("'gfx #' - performs the specified graphic (#).", false);
			sendConsoleMessage("'rights (name) (rank)' - gives (rank) to player with the username as (name).", false);
			sendConsoleMessage("'interface #' - displays specified interface id (#).", false);
			sendConsoleMessage("'update #' - sets a system update for said seconds (#).", false);
			sendConsoleMessage("'gear' - equips you with a default equipment array.", false);
			sendConsoleMessage("'item $' or 'item $ #' - spawns an item id ($) by a certain amount (# or 1 by default).", false);
			sendConsoleMessage("'npc #' - spawns an npc (#) or 'multi-npc #' - spawns multiple (3x3) npcs.", false);
			sendConsoleMessage("'object #' - spawns an object (#).", false);
			sendConsoleMessage("'find #' - finds the id for said item name.", false);
		}
	}
	
	public void sendConsoleCommand(String command) {
		String[] args = command.split(" ");
		String commandStart = args[0].toLowerCase();
		switch (commandStart) {
		case "cls":
			for (int index = 0; index < 17; index++) {
				consoleMessages[index] = null;
			}
			sendConsoleMessage(
					"Type 'commands' for a list of commands, 'cls' to clear the console, and 'info' for more "
							+ (clientWidth > 765 ? "information." : ""), false);
			if (clientWidth <= 765) {
				sendConsoleMessage("information.", false);
			}
			break;
		case "commands":
			getConsoleCommands();
			break;
		case "data":
			clientData = !clientData;
			break;
		case "config":
			if (myRank > 1) {
				if (args.length < 1) {
					sendConsoleMessage("Please enter a valid child id...", false);
					return;
				}
				int childId = Integer.valueOf(args[1]) != null ? Integer.valueOf(args[1]) : -1;
				if (childId == -1) {
					sendConsoleMessage("Please enter a valid child id...", false);
					return;
				}
				RSInterface rsi = RSInterface.interfaceCache[childId];
				if (rsi == null) {
					sendConsoleMessage("This child id has not been initialized and is currently null.", false);
					return;
				}
				boolean writeChildConfigs = args.length > 1;
				File file = new File("child_configs.txt");
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
					writer.write("ParentId: " + rsi.parentId);
					writer.newLine();
					writer.write("ChildId: " + rsi.id);
					writer.newLine();
					writer.write("Type: " + rsi.type);
					writer.newLine();
					writer.write("ActionType: " + rsi.actionType);
					writer.newLine();
					writer.write("HoverId: " + rsi.hoverId);
					writer.newLine();
					writer.write("ConfigId: " + (rsi.valueIndexArray != null ? rsi.valueIndexArray[0][1] : "null"));
					writer.newLine();
					writer.write("RequiredValue: " + (rsi.requiredValue != null ? rsi.requiredValue[0] : "null"));
					writer.newLine();
					if (rsi.children != null) {
						for (int i = 0; i < rsi.children.length; i++) {
							writer.write("Children[" + i + "]=" + rsi.children[i]);
							if (writeChildConfigs) {
								RSInterface child = RSInterface.interfaceCache[i];
								if (child == null)
									continue;
								writer.newLine();
								writer.write("\tType: " + child.type);
								writer.newLine();
								writer.write("\tActionType: " + child.actionType);
								writer.newLine();
								writer.write("\tHoverId: " + child.hoverId);
								writer.newLine();
								writer.write("\tConfigId: " + (child.valueIndexArray != null ? child.valueIndexArray[0][1] : "null"));
								writer.newLine();
								writer.write("\tRequiredValue: " + (child.requiredValue != null ? child.requiredValue[0] : "null"));
							}
							writer.newLine();
						}
					}
					writer.newLine();
					writer.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
				sendConsoleMessage("Config has been written in ./bin/ directory.", false);
			}
			break;
		case "pack_interfaces":
			if (myRank == 3) {
				RSInterface.pack();
			}
			break;
		case "unpack_interfaces":
			if (myRank > 1) {
				RSInterface.unpackCommand();
			}
			break;
		case "dump_interfaces":
			boolean[] parents = new boolean[RSInterface.interfaceCache.length];
			try {
				File file = new File("./interfaces.txt");
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				for (RSInterface rsi : RSInterface.interfaceCache) {
					if (rsi == null || parents[rsi.parentId])
						continue;
					writer.write("Valid interface id=" + rsi.parentId);
					writer.newLine();
					parents[rsi.parentId] = true;
				}
				System.out.println("Wrote valid interfaces.");
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "lol":
			try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("./weapons.txt"));
			for (int i = 0; i < ItemDef.totalItems; i++) {
				ItemDef def = ItemDef.forId(i);
				if (def != null && def.actions != null) {
					for (String action : def.actions) {
						if (action != null) {
							String name = action.toLowerCase();
							if (name.contains("wield")) {
								writer.write("itemId=" + def.id);
								writer.newLine();
								break;
							}
						}
					}
				}
			}
			writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			stream.createFrame(103);
			stream.writeByte(command.length() + 1);
			stream.writeString(command);
			break;
		}
	}

	public void drawChannelButtons(int xPosOffset, int yPosOffset) {
		cacheSprite[5].drawSprite(5 + xPosOffset, 142 + yPosOffset);
		String text[] = { "On", "Friends", "Off", "Hide" };
		int textColor[] = { 65280, 0xffff00, 0xff0000, getChatColor() };
		int[] x = { 5, 62, 119, 176, 233, 290, 347, 404 };
		switch (cButtonCPos) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			cacheSprite[2].drawSprite(x[cButtonCPos] + xPosOffset,
					142 + yPosOffset);
			break;
		}
		if (cButtonHPos == cButtonCPos) {
			switch (cButtonHPos) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				cacheSprite[3].drawSprite(x[cButtonHPos] + xPosOffset,
						142 + yPosOffset);
				break;
			case 7:
				cacheSprite[4].drawSprite(x[cButtonHPos] + xPosOffset,
						142 + yPosOffset);
				break;
			}
		} else {
			switch (cButtonHPos) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				cacheSprite[1].drawSprite(x[cButtonHPos] + xPosOffset,
						142 + yPosOffset);
				break;
			case 7:
				cacheSprite[4].drawSprite(x[cButtonHPos] + xPosOffset,
						142 + yPosOffset);
				break;
			}
		}
		smallText.method382(0xffffff, x[0] + 28 + xPosOffset, "All",
				157 + yPosOffset, true);
		smallText.method382(0xffffff, x[1] + 28 + xPosOffset, "Game",
				157 + yPosOffset, true);
		smallText.method382(0xffffff, x[2] + 28 + xPosOffset, "Public",
				152 + yPosOffset, true);
		smallText.method382(0xffffff, x[3] + 28 + xPosOffset, "Private",
				152 + yPosOffset, true);
		smallText.method382(0xffffff, x[4] + 28 + xPosOffset, "Clan",
				152 + yPosOffset, true);
		smallText.method382(0xffffff, x[5] + 28 + xPosOffset, "Trade",
				152 + yPosOffset, true);
		smallText.method382(0xffffff, x[6] + 28 + xPosOffset, "Asisstance",
				152 + yPosOffset, true);
		smallText.method382(0xffffff, x[7] + 55 + xPosOffset, "Report Abuse",
				157 + yPosOffset, true);
		smallText.method382(textColor[publicChatMode], x[2] + 28 + xPosOffset,
				text[publicChatMode], 163 + yPosOffset, true);
		smallText.method382(textColor[publicChatMode], x[3] + 28 + xPosOffset,
				text[publicChatMode], 163 + yPosOffset, true);
		smallText.method382(textColor[clanChatMode], x[4] + 28 + xPosOffset,
				text[clanChatMode], 163 + yPosOffset, true);
		smallText.method382(textColor[tradeMode], x[5] + 28 + xPosOffset,
				text[tradeMode], 163 + yPosOffset, true);
		smallText.method382(textColor[yellMode], x[6] + 28 + xPosOffset,
				text[yellMode], 163 + yPosOffset, true);
	}

	/**
	 * quickChat: is quick chat open? canTalk: can player submit text(type packet
	 * the chatbox)? quickHoverY: hover position of the green box.
	 **/
	public boolean quickChat = false, canTalk = true, divideSelections = false,
			divideSelectedSelections = false;
	public int quickSelY = -1, quickSelY2 = -1, quickHoverY = -1,
			quickHoverY2 = -1, shownSelection = -1,
			shownSelectedSelection = -1;
	public String quickChatDir = "Quick Chat";

	public void openQuickChat() {
		resetQuickChat();
		quickChat = true;
		canTalk = false;
		inputTaken = true;
	}

	public void resetQuickChat() {
		divideSelections = false;
		divideSelectedSelections = false;
		shownSelection = -1;
		shownSelectedSelection = -1;
		quickSelY = -1;
		quickSelY2 = -1;
		quickHoverY = -1;
		quickHoverY2 = -1;
	}

	/**
	 * Draws the quick chat interface.
	 **/
	public void displayQuickChat(int x, int y) {
		String[] shortcutKey = { "G", "T", "S", "E", "C", "M", "Enter" };
		String[] name = { "General", "Trade/Items", "Skills", "Group Events",
				"Clans", "Inter-game", "I'm muted." };
		cacheSprite[65].drawSprite(7 + x, 7 + y);
		if (cButtonHPos == 8) {
			cacheSprite[66].drawSprite(7 + x, 7 + y);
		}
		DrawingArea.drawPixels(2, 23 + y, 7 + x, 0x847963, 506);
		if (divideSelections) {
			DrawingArea.drawPixels(111, 25 + y, 116 + x, 0x847963, 2);
		}
		if (divideSelectedSelections) {
			DrawingArea.drawPixels(111, 25 + y, 116 + 158 + x, 0x847963, 2);
		}
		normalFont.method389(false, 45 + x, 255, quickChatDir, 20 + y);
		if (quickHoverY != -1 && shownSelection == -1 && quickHOffsetX == 0) {
			DrawingArea.drawPixels(14, quickHoverY + y, 7 + x, 0x577E45, 109);
		} else if (quickHoverY != -1 && shownSelection != -1
				&& quickHOffsetX == 0) {
			DrawingArea.drawPixels(14, quickHoverY + y, 7 + x, 0x969777, 109);
		}
		/**
		 * Hovering over text on selected->selections.
		 **/
		if (quickHoverY2 != -1 && shownSelectedSelection == -1
				&& quickHOffsetX == 0) {
			DrawingArea.drawPixels(14, quickHoverY2 + y, 118 + 159 + x,
					0x577E45, 109);
		} else if (quickHoverY2 != -1 && shownSelectedSelection != -1
				&& quickHOffsetX == 0) {
			DrawingArea.drawPixels(14, quickHoverY2 + y, 118 + 159 + x,
					0x969777, 109);
		}
		if (quickSelY != -1) {
			DrawingArea.drawPixels(14, quickSelY + y, 7 + x, 0x969777, 109);
		}
		if (quickSelY2 != -1) {
			DrawingArea.drawPixels(14, quickSelY2 + y, 118 + x, 0x969777, 156);
		}
		for (int i1 = 0, y2 = 36; i1 < name.length; i1++, y2 += 14) {
			normalFont.method389(false, 10 + x, 0x555555,
					shortcutKey[i1] + ".", y + y2);
			if (i1 == name.length - 1)
				normalFont
						.method389(
								false,
								12
										+ x
										+ normalFont
												.getTextWidth(shortcutKey[i1]
														+ "."), 0, name[i1], y
										+ y2);
			else
				normalFont
						.method389(
								false,
								12
										+ x
										+ normalFont
												.getTextWidth(shortcutKey[i1]
														+ "."), 0, name[i1]
										+ " ->", y + y2);
		}
		if (shownSelection != -1) {
			showSelections(shownSelection, x, y);
		}
		if (shownSelectedSelection != -1) {
			showSelectedSelections(shownSelectedSelection, x, y);
		}
	}

	public void showSelections(int selections, int x, int y) {
		switch (selections) {
		case 0:
			String[] keys1 = { "R", "H", "G", "C", "S", "M", "B", "P" };
			String[] names1 = { "Responses", "Hello", "Goodbye", "Comments",
					"Smilies", "Mood", "Banter", "Player vs Player" };
			if (quickHoverY != -1 && quickHOffsetX == 110)
				DrawingArea.drawPixels(14, quickHoverY + y, 118 + x, 0x577E45,
						156);
			for (int i1 = 0, y2 = 36; i1 < names1.length; i1++, y2 += 14) {
				normalFont.method389(false, 118 + x, 0x555555, keys1[i1] + ".",
						y + y2);
				normalFont.method389(false,
						120 + x + normalFont.getTextWidth(keys1[i1] + "."), 0,
						names1[i1] + " ->", y + y2);
			}
			break;
		case 1:
			String[] keys2 = { "T", "I" };
			String[] names2 = { "Trade", "Items" };
			if (quickHoverY != -1 && quickHoverY < 53 && quickHOffsetX == 110)
				DrawingArea.drawPixels(14, quickHoverY + y, 118 + x, 0x577E45,
						101);
			for (int i2 = 0, y2 = 36; i2 < names2.length; i2++, y2 += 14) {
				normalFont.method389(false, 118 + x, 0x555555, keys2[i2] + ".",
						y + y2);
				normalFont.method389(false,
						120 + x + normalFont.getTextWidth(keys2[i2] + "."), 0,
						names2[i2] + " ->", y + y2);
			}
			break;

		default:
			break;
		}
	}

	public void showSelectedSelections(int selections, int x, int y) {
		switch (selections) {
		case 0:
			String[] keys1 = { "1", "2", "3", "4", "5" };
			String[] names1 = { "Hi.", "Hey!", "Sup?", "Hello.", "Yo dawg." };
			if (quickHoverY2 != -1 && quickHOffsetX == 269)
				DrawingArea.drawPixels(14, quickHoverY2 + y, 118 + 158 + x,
						0x577E45, 156);
			for (int i1 = 0, y2 = 36; i1 < names1.length; i1++, y2 += 14) {
				normalFont.method389(false, 118 + 159 + x, 0x555555, keys1[i1]
						+ ".", y + y2);
				normalFont.method389(
						false,
						120 + 159 + x
								+ normalFont.getTextWidth(keys1[i1] + "."), 0,
						names1[i1], y + y2);
			}
			break;

		default:
			break;
		}
	}

	public boolean filterMessages = false;
	public String[] filteredMessages = { "You catch a",
			"You successfully cook the", "You accidentally burn the",
			"You manage to get" };

	public String mutedBy = "";
	public String muteReason = "";

	public int chatColor = 0;
	public int chatEffect = 0;

	public int totalItemResults;
	public String itemResultNames[] = new String[100];
	public int itemResultIDs[] = new int[100];
	public int itemResultScrollPos;

	public void itemSearch(String itemName) {
		if (itemName == null || itemName.length() == 0) {
			totalItemResults = 0;
			return;
		}
		String searchName = itemName;
		String searchParts[] = new String[100];
		int totalResults = 0;
		do {
			int regex = searchName.indexOf(" ");
			if (regex == -1) {
				break;
			}
			String part = searchName.substring(0, regex).trim();
			if (part.length() > 0) {
				searchParts[totalResults++] = part.toLowerCase();
			}
			searchName = searchName.substring(regex + 1);
		} while (true);
		searchName = searchName.trim();
		if (searchName.length() > 0) {
			searchParts[totalResults++] = searchName.toLowerCase();
		}
		totalItemResults = 0;
		label0: for (int id = 0; id < ItemDef.totalItems; id++) {
			ItemDef item = ItemDef.forId(id);
			if (item.certTemplateID != -1 || item.name == null) {
				continue;
			}
			String resultName = item.name.toLowerCase();
			for (int index = 0; index < totalResults; index++) {
				if (resultName.indexOf(searchParts[index]) == -1) {
					continue label0;
				}
			}
			itemResultNames[totalItemResults] = resultName;
			itemResultIDs[totalItemResults] = id;
			totalItemResults++;
			if (totalItemResults >= itemResultNames.length) {
				return;
			}
		}
	}

	public String getPrefix(int rights) {
		String prefix = "cr";
		if (rights > 9) {
			prefix = "c";
		}
		return "@" + prefix + rights + "@";
	}

	public int getPrefixRights(String prefix) {
		int rights = 0;
		int start = 3;
		if (!prefix.contains("cr")) {
			start = 2;
		}
		try {
			rights = Integer.parseInt(prefix.substring(start, 4));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rights;
	}

	public void drawChatArea() {
		int offsetX = 0;
		int offsetY = clientSize != 0 ? clientHeight - 165 : 0;
		if (isFixed()) {
			chatAreaIP.initDrawingArea();
		}
		Rasterizer.lineOffsets = anIntArray1180;
		RSFont textDrawingArea = normalFont;
		if (showChat) {
			if (clientSize == 0) {
				cacheSprite[0].drawSprite(0 + offsetX, 0 + offsetY);
			} else {
				cacheSprite[88].drawARGBSprite(7 + offsetX, 7 + offsetY);
			}
		}
		drawChannelButtons(offsetX, offsetY);
		if (!demo) {
			if (showInput) {
				cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
				newBoldFont.drawCenteredString(promptMessage, 259 + offsetX, 60 + offsetY, 0, -1);
				newBoldFont.drawCenteredString(promptInput + "*", 259 + offsetX, 80 + offsetY, 128, -1);
			} else if (inputDialogState == 1) {
				cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
				newBoldFont.drawCenteredString("Enter amount:", 259 + offsetX, 60 + offsetY, 0, -1);
				newBoldFont.drawCenteredString(amountOrNameInput + "*", 259 + offsetX, 80 + offsetY, 128, -1);
			} else if (inputDialogState == 2) {
				cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
				newBoldFont.drawCenteredString("Enter name:", 259 + offsetX, 60 + offsetY, 0, -1);
				newBoldFont.drawCenteredString(amountOrNameInput + "*", 259 + offsetX, 80 + offsetY, 128, -1);
			} else if (inputDialogState == 3) {
				if (amountOrNameInput != "") {
					itemSearch(amountOrNameInput);
				}
				DrawingArea.setDrawingArea(8 + offsetX, 7 + offsetY, 512 + offsetX, 121 + offsetY);
				for (int j = 0; j < totalItemResults; j++) {
					final int yPos = 18 + j * 14 - itemResultScrollPos;
					if (yPos > 0 && yPos < 132) {
						newRegularFont.drawBasicString(itemResultNames[j] + " - " + itemResultIDs[j], 10 + offsetX, yPos + offsetY, 0, -1);
					}
				}
				DrawingArea.defaultDrawingAreaSize();
				if (totalItemResults > 8) {
					drawScrollbar(114, itemResultScrollPos, 7 + offsetY, 496 + offsetX, totalItemResults * 14 + 7, false, clientSize == 0 ? false : true);
				}
				if (amountOrNameInput.length() == 0) {
					newBoldFont.drawCenteredString("Enter item name", 259 + offsetX, 70 + offsetY, 0, -1);
				} else if (totalItemResults == 0) {
					newBoldFont.drawCenteredString("No matching items found", 259 + offsetX, 70 + offsetY, 0, -1);
				}
				newRegularFont.drawBasicString(amountOrNameInput + "*", 10 + offsetX, 132 + offsetY, 0xffffff, 0);
				DrawingArea.drawHorizontalLine(121 + offsetY, clientSize == 0 ? 0x807660 : 0xffffff, 505, 7);
			} else if (inputDialogState == 5) {
				cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
				newBoldFont.drawCenteredString("Search for item:", 259 + offsetX, 60 + offsetY, 0, -1);
				newBoldFont.drawCenteredString(amountOrNameInput + "*", 259 + offsetX, 80 + offsetY, 128, -1);
			} else if (inputDialogState == 6) {
				cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
				newBoldFont.drawCenteredString("Enter your note:", 259 + offsetX, 60 + offsetY, 0, -1);
				newBoldFont.drawCenteredString(amountOrNameInput + "*", 259 + offsetX, 80 + offsetY, 128, -1);
			} else if (aString844 != null)  {
				cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
				newBoldFont.drawCenteredString(aString844, 259 + offsetX, 60 + offsetY, 0, -1);
				newBoldFont.drawCenteredString("Click to continue", 259 + offsetX, 80 + offsetY, 128, -1);
			} else if (backDialogID != -1) {
				cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
				drawInterface(20 + offsetX, 20 + offsetY, 0, RSInterface.interfaceCache[backDialogID]);
			} else if (dialogID != -1) {
				cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
				drawInterface(20 + offsetX, 20 + offsetY, 0, RSInterface.interfaceCache[dialogID]);
			} else if (!quickChat && showChat) {
				int messageY = -3;
				int scrollPosition = 0;
				DrawingArea.setDrawingArea(8 + offsetX, 8 + offsetY, 497 + offsetX, 121 + offsetY);
				for (int index = 0; index < 500; index++)
					if (chatMessages[index] != null) {
						int chatType = chatTypes[index];
						int positionY = (70 - messageY * 14) + anInt1089 + 6;
						String name = chatNames[index];
						String prefixName = name;
						final String time = "[" + /*chatTimes[index]*/"" + "]";
						int playerRights = 0;
						if (name != null && name.indexOf("@") == 0) {
							name = name.substring(5);
							playerRights = getPrefixRights(prefixName.substring(0, prefixName.indexOf(name)));
						}
						if (chatType == 0) {
							if (chatTypeView == 5 || chatTypeView == 0) {
								if (positionY > 0 && positionY < 210) {
									int xPos = 11;
									newRegularFont.drawBasicString(chatMessages[index], xPos + offsetX, positionY + offsetY, clientSize == 0 ? 0 : 0xffffff, clientSize == 0 ? -1 : 0);
								}
								scrollPosition++;
								messageY++;
							}
						}
						if ((chatType == 1 || chatType == 2) && (chatType == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(name))) {
							if (chatTypeView == 1 || chatTypeView == 0 || (playerRights > 0 && playerRights <= 4)) {
								if (positionY > 0 && positionY < 210) {
									int xPos = 11;
									if (timeStamp) {
										newRegularFont.drawBasicString(time, xPos + offsetX, positionY + offsetY, clientSize == 0 ? 0 : 0xffffff, clientSize == 0 ? -1 : 0);
										xPos += newRegularFont.getTextWidth(time);
									}
									if (playerRights != 0) {
										modIcons[playerRights - 1].drawSprite(xPos + 1 + offsetX, positionY - 11 + offsetY);
										xPos += 14;
									}
									newRegularFont.drawBasicString(name + ":", xPos + offsetX, positionY + offsetY, clientSize == 0 ? 0 : 0xffffff, clientSize == 0 ? -1 : 0);
									xPos += newRegularFont.getTextWidth(name) + 7;
									newRegularFont.drawBasicString(chatMessages[index], xPos + offsetX, positionY + offsetY, clientSize == 0 ? 255 : 0x7FA9FF, clientSize == 0 ? -1 : 0);
								}
								scrollPosition++;
								messageY++;
							}
						}
						if ((chatType == 3 || chatType == 7) && (splitpublicChat == 0 || chatTypeView == 2) && (chatType == 7 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(name))) {
							if (chatTypeView == 2 || chatTypeView == 0) {
								if (positionY > 0 && positionY < 210) {
									int xPos = 11;
									if (timeStamp) {
										newRegularFont.drawBasicString(time, xPos + offsetX, positionY + offsetY, clientSize == 0 ? 0 : 0xffffff, clientSize == 0 ? -1 : 0);
										xPos += newRegularFont.getTextWidth(time);
									}
									newRegularFont.drawBasicString("From", xPos + offsetX, positionY + offsetY, clientSize == 0 ? 0 : 0xffffff, clientSize == 0 ? -1 : 0);
									xPos += newRegularFont.getTextWidth("From ");
									if (playerRights != 0) {
										modIcons[playerRights - 1].drawSprite(xPos + 1 + offsetX, positionY - 11 + offsetY);
										xPos += 14;
									}
									newRegularFont.drawBasicString(name + ":", xPos + offsetX, positionY + offsetY, clientSize == 0 ? 0 : 0xffffff, clientSize == 0 ? -1 : 0);
									xPos += newRegularFont.getTextWidth(name) + 8;
									newRegularFont.drawBasicString(chatMessages[index], xPos + offsetX, positionY + offsetY, clientSize == 0 ? 0x800000 : 0xFF5256, clientSize == 0 ? -1 : 0);
								}
								scrollPosition++;
								messageY++;
							}
						}
						if (chatType == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(name))) {
							if (chatTypeView == 3 || chatTypeView == 0) {
								if (positionY > 0 && positionY < 210) {
									newRegularFont.drawBasicString(name + " " + chatMessages[index], 11 + offsetX, positionY + offsetY, clientSize == 0 ? 0x800080 : 0xFF00D4, clientSize == 0 ? -1 : 0);
								}
								scrollPosition++;
								messageY++;
							}
						}
						if (chatType == 5 && splitpublicChat == 0 && publicChatMode < 2) {
							if (chatTypeView == 2 || chatTypeView == 0) {
								if (positionY > 0 && positionY < 210)
									newRegularFont.drawBasicString(chatMessages[index], 11 + offsetX, positionY + offsetY, clientSize == 0 ? 0x800000 : 0xFF5256, clientSize == 0 ? -1 : 0);
								scrollPosition++;
								messageY++;
							}
						}
						if (chatType == 6 && (splitpublicChat == 0 || chatTypeView == 2) && publicChatMode < 2) {
							if (chatTypeView == 2 || chatTypeView == 0) {
								if (positionY > 0 && positionY < 210) {
									newRegularFont.drawBasicString("To " + name + ":", 11 + offsetX, positionY + offsetY, clientSize == 0 ? 0 : 0xffffff, clientSize == 0 ? -1 : 0);
									newRegularFont.drawBasicString(chatMessages[index], 15 + newRegularFont.getTextWidth("To :" + name) + offsetX + offsetX, positionY + offsetY, clientSize == 0 ? 0x800000 : 0xFF5256, clientSize == 0 ? -1 : 0);
								}
								scrollPosition++;
								messageY++;
							}
						}
						if (chatType == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(name))) {
							if (chatTypeView == 3 || chatTypeView == 0) {
								if (positionY > 0 && positionY < 210)
									textDrawingArea.method385(0x7e3200, name + " " + chatMessages[index], positionY + offsetY, 11 + offsetX);
								scrollPosition++;
								messageY++;
							}
						}
						if (chatType == 12 && (clanChatMode == 0 || clanChatMode == 1 && isFriendOrSelf(name))) {
							if (chatTypeView == 11 || chatTypeView == 0) {
								if (positionY > 0 && positionY < 210) {
									int positionX = 11;
									String title =  (isFixed() ? "<col=0000FF>" : "<col=7FA9FF>") + clanname + "</col>";
									String username = (chatRights[index] > 0 ? "<img=" + (chatRights[index]) + ">" : "") + capitalize(chatNames[index]);
									String message = (isFixed() ? "<col=800000>" : "<col=FF5256>") + chatMessages[index] + "</col>";
									newRegularFont.drawBasicString("[" + title + "] " + username + ": " + message, positionX, positionY + offsetY, isFixed() ? 0 : 0xffffff, isFixed() ? -1 : 0);
								}
								scrollPosition++;
								messageY++;
							}
						}
						if (chatType == 13) {
							if (chatTypeView == 6 || chatTypeView == 0) {
								if (yellMode == 0 || yellMode == 1 && isFriendOrSelf(name)) {
									if (positionY > 0 && positionY < 210) {
										int xPos = 11;
										if (timeStamp) {
											normalFont.method389(clientSize == 0 ? false : true, xPos + offsetX, clientSize == 0 ? 0 : 0xffffff, time, positionY + offsetY);
											xPos += normalFont.getTextWidth(time);
										}
										String name2 = "[@red@";
										textDrawingArea.method389(clientSize == 0 ? false : true, xPos + offsetX, clientSize == 0 ? 0 : 0xffffff, name2, positionY + offsetY);
										xPos += normalFont.getTextWidth(name2);
										String name3 = clientSize == 0 ? chatNames[index] + "@bla@]: " : chatNames[index] + "@whi@]: ";
										if (chatRights[index] != 0) {
											modIcons[chatRights[index] - 1].drawSprite(xPos + offsetX, positionY - 11 + offsetY);
											xPos += 14;
										}
										textDrawingArea.method389(clientSize == 0 ? false : true, xPos + offsetX, clientSize == 0 ? 0 : 0xFFFFFF, name3, positionY + offsetY);
										textDrawingArea.method389(clientSize == 0 ? false : true, xPos + offsetX + normalFont.getTextWidth(name3), clientSize == 0 ? 0x800000 : 0xFF5256, chatMessages[index], positionY + offsetY);
									}
									scrollPosition++;
									messageY++;
								}
							}
						}
					}
				DrawingArea.defaultDrawingAreaSize();
				anInt1211 = scrollPosition * 14 + 7 + 5;
				if (anInt1211 < 111) {
					anInt1211 = 111;
				}
				drawScrollbar(114, anInt1211 - anInt1089 - 113, 7 + offsetY, 496 + offsetX, anInt1211, false, clientSize != 0);
				String name;
				if (myPlayer != null && myPlayer.name != null) {
					name = myPlayer.name;
				} else {
					name = TextUtils.fixName(myUsername);
				}
				if (myRank - 1 >= 0) {
					modIcons[myRank - 1].drawSprite(12 + offsetX, 122 + offsetY);
					offsetX += 14;
				}
				if (muteReason.length() > 0) {
					textDrawingArea.method389(clientSize == 0 ? false : true, 11 + offsetX, clientSize == 0 ? 0 : 0xffffff, "You are currently muted. Reason: " + muteReason + ".", 133 + offsetY);
				} else {
					textDrawingArea.method389(clientSize == 0 ? false : true, 11 + offsetX, clientSize == 0 ? 0 : 0xffffff, name, 133 + offsetY);
					cacheSprite[14].drawSprite(textDrawingArea.getTextWidth(name) + 11 + offsetX, 123 + offsetY);
					textDrawingArea.method389(clientSize == 0 ? false : true, textDrawingArea.getTextWidth(name) + 24 + offsetX, clientSize == 0 ? 0 : 0xffffff, ": ", 133 + offsetY);
					//newRegularFont.drawRAString(inputString + "*", 24 + newRegularFont.getTextWidth(s + ": ") + xPosOffset, 133 + yPosOffset, clientSize == 0 ? 255 : 0x7FA9FF, clientSize == 0 ? -1 : 0);
					newRegularFont.drawBasicString(inputString + "*", 24 + newRegularFont.getTextWidth(name + ": ") + offsetX, 133 + offsetY, clientSize == 0 ? 255 : 0x7FA9FF, clientSize == 0 ? -1 : 0);
				}
				if (clientSize == 0)
					DrawingArea.drawHorizontalLine(121 + offsetY, clientSize == 0 ? 0x807660 : 0xffffff, 505, 7);
			} else if (quickChat) {
				displayQuickChat(offsetX, offsetY);
			}
		} else {
			newRegularFont.drawCenteredString("You are currently on a demo account. Please <col=255><u=255>register</u></col> to play.", 260 + offsetX, 75 + offsetY, 0, isFixed() ? -1 : 0);
		}
		if (menuOpen && menuScreenArea == 2) {
			drawMenu();
		}
		if (clientSize == 0) {
			chatAreaIP.drawGraphics(0, 338, super.graphics);
		}
		gameScreenIP.initDrawingArea();
		Rasterizer.lineOffsets = anIntArray1182;
	}

	public void init() {
		try {
			nodeID = 10;
			portOff = 0;
			setHighMemory();
			isMembers = true;
			signlink.startpriv(InetAddress.getByName(Constants.HOST_ADDRESS));
			instance = this;
			clientSize = 0;
			clientWidth = 765;
			clientHeight = 503;
			gameAreaWidth = 512;
			gameAreaHeight = 334;
			System.out.println("[CLIENT]: Applet dimensions: " + appletWidth + "x" + appletHeight + "...");
			initClientFrame(appletWidth, appletHeight);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startRunnable(Runnable runnable, int i) {
		if (i > 10)
			i = 10;
		if (signlink.mainapp != null) {
			signlink.startthread(runnable, i);
		} else {
			super.startRunnable(runnable, i);

		}
	}

	public Socket openSocket(int port) throws IOException {
		if(signlink.mainapp != null)
			return signlink.openSocket(port);
		else
			return new Socket(Constants.HOST_ADDRESS, port);
	}

	public boolean processMenuClick() {
		if (activeInterfaceType != 0)
			return false;
		int j = super.clickMode3;
		if (spellSelected == 1 && super.saveClickX >= 516
				&& super.saveClickY >= 160 && super.saveClickX <= 765
				&& super.saveClickY <= 205)
			j = 0;
		if (menuOpen) {
			if (j != 1) {
				int k = super.mouseX;
				int j1 = super.mouseY;
				if (menuScreenArea == 0) {
					k -= clientSize == 0 ? 4 : 0;
					j1 -= clientSize == 0 ? 4 : 0;
				}
				if (menuScreenArea == 1) {
					k -= 519;
					j1 -= 168;
				}
				if (menuScreenArea == 2) {
					k -= 17;
					j1 -= 338;
				}
				if (menuScreenArea == 3) {
					k -= 519;
					j1 -= 0;
				}
				if (k < menuOffsetX - 10 || k > menuOffsetX + menuWidth + 10
						|| j1 < menuOffsetY - 10
						|| j1 > menuOffsetY + menuHeight + 10) {
					menuOpen = false;
					contextWidth = 0;
					contextHeight = 0;
					if (menuScreenArea == 1)
						needDrawTabArea = true;
					if (menuScreenArea == 2)
						inputTaken = true;
				}
			}
			if (j == 1) {
				int l = menuOffsetX;
				int k1 = menuOffsetY;
				int i2 = menuWidth;
				int k2 = super.saveClickX;
				int l2 = super.saveClickY;
				if (menuScreenArea == 0) {
					k2 -= clientSize == 0 ? 4 : 0;
					l2 -= clientSize == 0 ? 4 : 0;
				}
				if (menuScreenArea == 1) {
					k2 -= 519;
					l2 -= 168;
				}
				if (menuScreenArea == 2) {
					k2 -= 17;
					l2 -= 338;
				}
				if (menuScreenArea == 3) {
					k2 -= 519;
					l2 -= 0;
				}
				int i3 = -1;
				for (int j3 = 0; j3 < menuActionRow; j3++) {
					int k3 = k1 + 31 + (menuActionRow - 1 - j3) * 15;
					if (k2 > l && k2 < l + i2 && l2 > k3 - 13 && l2 < k3 + 3)
						i3 = j3;
				}
				// System.out.println(i3);
				if (i3 != -1)
					doAction(i3);
				menuOpen = false;
				contextWidth = 0;
				contextHeight = 0;
				if (menuScreenArea == 1)
					needDrawTabArea = true;
				if (menuScreenArea == 2) {
					inputTaken = true;
				}
			}
			return true;
		} else {
			if (j == 1 && menuActionRow > 0) {
				int i1 = menuActionID[menuActionRow - 1];
				if (i1 == 632 || i1 == 78 || i1 == 867 || i1 == 431 || i1 == 53
						|| i1 == 74 || i1 == 454 || i1 == 539 || i1 == 493
						|| i1 == 847 || i1 == 447 || i1 == 1125) {
					int l1 = menuActionCmd2[menuActionRow - 1];
					int j2 = menuActionCmd3[menuActionRow - 1];
					RSInterface rsi = RSInterface.interfaceCache[j2];
					if (rsi.aBoolean259 || rsi.aBoolean235) {
						aBoolean1242 = false;
						anInt989 = 0;
						anInt1084 = j2;
						anInt1085 = l1;
						activeInterfaceType = 2;
						anInt1087 = super.saveClickX;
						anInt1088 = super.saveClickY;
						if (RSInterface.interfaceCache[j2].parentId == openInterfaceID)
							activeInterfaceType = 1;
						if (RSInterface.interfaceCache[j2].parentId == backDialogID)
							activeInterfaceType = 3;
						return true;
					}
				}
			}
			if (j == 1
					&& (anInt1253 == 1 || menuHasAddFriend(menuActionRow - 1))
					&& menuActionRow > 2)
				j = 2;
			if (j == 1 && menuActionRow > 0)
				doAction(menuActionRow - 1);
			if (j == 2 && menuActionRow > 0)
				determineMenuSize();
			return false;
		}
	}

	public static String getFileNameWithoutExtension(String fileName) {
		File tmpFile = new File(fileName);
		tmpFile.getName();
		int whereDot = tmpFile.getName().lastIndexOf('.');
		if (0 < whereDot && whereDot <= tmpFile.getName().length() - 2) {
			return tmpFile.getName().substring(0, whereDot);
		}
		return "";
	}

	public void saveMidi(boolean flag, byte abyte0[]) {
		signlink.midifade = flag ? 1 : 0;
		signlink.midisave(abyte0, abyte0.length);
	}

	public static void writeFile(byte[] data, String fileName)
			throws IOException {
		OutputStream out = new FileOutputStream(fileName);
		out.write(data);
		out.close();
	}

	public final void method22() {
		try {
			anInt985 = -1;
			aClass19_1056.clear();
			aClass19_1013.clear();
			// Rasterizer.method366();
			unlinkMRUNodes();
			landscapeScene.initToNull();
			System.gc();
			for (int i = 0; i < 4; i++)
				clippingPlanes[i].reset();
			for (int l = 0; l < 4; l++) {
				for (int k1 = 0; k1 < 104; k1++) {
					for (int j2 = 0; j2 < 104; j2++)
						byteGroundArray[l][k1][j2] = 0;
				}
			}
			SceneGraph sceneGraph = new SceneGraph(byteGroundArray,
					intGroundArray);
			int k2 = aByteArrayArray1183.length;
			int k18 = 62;
			for (int A = 0; A < k2; A++) {
				for (int B = 0; B < 2000; B++) {
					if (mapLocation[A] == positions[B]) {
						floorMap[A] = landScapes[B];
						objectMap[A] = objects[B];
					}
				}
			}
			if (loggedIn)
				stream.createFrame(0);
			if (!aBoolean1159) {
				for (int i3 = 0; i3 < k2; i3++) {
					int i4 = (mapLocation[i3] >> 8) * 64 - regionAbsBaseX;
					int k5 = (mapLocation[i3] & 0xff) * 64 - regionAbsBaseY;
					byte floorData[] = aByteArrayArray1183[i3];
					File floor = new File(signlink.getCacheLocation() + "rsmap" + System.getProperty("file.separator") + floorMap[i3] + ".dat");
					if (floor.exists()) {
						floorData = DataUtils.readFile(floor.getAbsolutePath());
					}
					if (floorData != null) {
						sceneGraph.method180(floorData, k5, i4,
								(currentRegionX - 6) * 8, (currentRegionY - 6) * 8,
								clippingPlanes);
						//pushMessage("floorMap: " + floorMap[i3], 0, "");
					}

				}
				for (int j4 = 0; j4 < k2; j4++) {
					int l5 = (mapLocation[j4] >> 8) * k18 - regionAbsBaseX;
					int k7 = (mapLocation[j4] & 0xff) * k18 - regionAbsBaseY;
					byte abyte2[] = aByteArrayArray1183[j4];
					if (abyte2 == null && currentRegionY < 800)
						sceneGraph.loadVertexHeights(k7, 64, 64, l5);
				}
				anInt1097++;
				if (anInt1097 > 160) {
					anInt1097 = 0;
					stream.createFrame(238);
					stream.writeByte(96);
				}
				if (loggedIn)
					stream.createFrame(0);
				for (int i6 = 0; i6 < k2; i6++) {
					try {
						byte objectData[] = aByteArrayArray1247[i6];
						File object = new File(signlink.getCacheLocation() + "rsmap" + System.getProperty("file.separator") + objectMap[i6] + ".dat");
						if (object.exists()) {
							objectData = DataUtils.readFile(object.getAbsolutePath());
						}
						if (objectData != null) {
							int l8 = (mapLocation[i6] >> 8) * 64 - regionAbsBaseX;
							int k9 = (mapLocation[i6] & 0xff) * 64 - regionAbsBaseY;
							sceneGraph.method190(l8, clippingPlanes, k9,
									landscapeScene, objectData);
							// pushMessage("objectMap: " + objectMap[i6], 0,
							// "");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (aBoolean1159) {
				for (int j3 = 0; j3 < 4; j3++) {
					for (int k4 = 0; k4 < 13; k4++) {
						for (int j6 = 0; j6 < 13; j6++) {
							int l7 = anIntArrayArrayArray1129[j3][k4][j6];
							if (l7 != -1) {
								int i9 = l7 >> 24 & 3;
								int l9 = l7 >> 1 & 3;
								int j10 = l7 >> 14 & 0x3ff;
								int l10 = l7 >> 3 & 0x7ff;
								int j11 = (j10 / 8 << 8) + l10 / 8;
								for (int l11 = 0; l11 < mapLocation.length; l11++) {
									if (mapLocation[l11] != j11
											|| aByteArrayArray1183[l11] == null)
										continue;
									sceneGraph.method179(i9, l9,
											clippingPlanes, k4 * 8,
											(j10 & 7) * 8,
											aByteArrayArray1183[l11],
											(l10 & 7) * 8, j3, j6 * 8);
									break;
								}

							}
						}

					}

				}
				for (int l4 = 0; l4 < 13; l4++) {
					for (int k6 = 0; k6 < 13; k6++) {
						int i8 = anIntArrayArrayArray1129[0][l4][k6];
						if (i8 == -1)
							sceneGraph.loadVertexHeights(k6 * 8, 8, 8, l4 * 8);
					}
				}
				if (loggedIn)
					stream.createFrame(0);
				for (int l6 = 0; l6 < 4; l6++) {
					for (int j8 = 0; j8 < 13; j8++) {
						for (int j9 = 0; j9 < 13; j9++) {
							int i10 = anIntArrayArrayArray1129[l6][j8][j9];
							if (i10 != -1) {
								int k10 = i10 >> 24 & 3;
								int i11 = i10 >> 1 & 3;
								int k11 = i10 >> 14 & 0x3ff;
								int i12 = i10 >> 3 & 0x7ff;
								int j12 = (k11 / 8 << 8) + i12 / 8;
								for (int k12 = 0; k12 < mapLocation.length; k12++) {
									if (mapLocation[k12] != j12
											|| aByteArrayArray1247[k12] == null)
										continue;
									sceneGraph.method183(clippingPlanes,
											landscapeScene, k10, j8 * 8,
											(i12 & 7) * 8, l6,
											aByteArrayArray1247[k12],
											(k11 & 7) * 8, i11, j9 * 8);
									break;
								}
							}
						}
					}
				}
			}
			if (loggedIn)
				stream.createFrame(0);
			sceneGraph.createScene(clippingPlanes, landscapeScene);
			gameScreenIP.initDrawingArea();
			if (loggedIn)
				stream.createFrame(0);
			int k3 = SceneGraph.lowestPlane;
			if (k3 > plane)
				k3 = plane;
			if (k3 < plane - 1)
				k3 = plane - 1;
			if (lowMemory)
				landscapeScene.method275(SceneGraph.lowestPlane);
			else
				landscapeScene.method275(0);
			for (int i5 = 0; i5 < 104; i5++) {
				for (int i7 = 0; i7 < 104; i7++)
					refreshGroundItems(i5, i7);
			}
			anInt1051++;
			if (anInt1051 > 98) {
				anInt1051 = 0;
				if (loggedIn)
					stream.createFrame(150);
			}
			method63();
		} catch (Exception exception) {
		}
		ObjectDefinition.mruNodes1.unlinkAll();
		if (super.mainFrame != null && loggedIn) {
			stream.createFrame(210);
			stream.writeInt(0x3f008edd);
		}
		System.gc();
		// Rasterizer.method367();
		resourceProvider.ignoreExtras();
		int k = (currentRegionX - 6) / 8 - 1;
		int j1 = (currentRegionX + 6) / 8 + 1;
		int i2 = (currentRegionY - 6) / 8 - 1;
		int l2 = (currentRegionY + 6) / 8 + 1;
		if (aBoolean1141) {
			k = 49;
			j1 = 50;
			i2 = 49;
			l2 = 50;
		}
		for (int l3 = k; l3 <= j1; l3++) {
			for (int j5 = i2; j5 <= l2; j5++)
				if (l3 == k || l3 == j1 || j5 == i2 || j5 == l2) {
					int j7 = resourceProvider.getClipping(0, j5, l3);
					if (j7 != -1)
						resourceProvider.method560(j7, 3);
					int k8 = resourceProvider.getClipping(1, j5, l3);
					if (k8 != -1)
						resourceProvider.method560(k8, 3);
				}
		}
	}

	public void unlinkMRUNodes() {
		ObjectDefinition.mruNodes1.unlinkAll();
		ObjectDefinition.animatedModelNodes.unlinkAll();
		NPCDefinition.nodeCache.unlinkAll();
		ItemDef.mruNodes2.unlinkAll();
		ItemDef.mruNodes1.unlinkAll();
		Player.mruNodes.unlinkAll();
		StillGraphics.aMRUNodes_415.unlinkAll();
	}

	public void method24(int i) {
		int ai[] = miniMap.myPixels;
		int j = ai.length;
		for (int k = 0; k < j; k++)
			ai[k] = 0;

		for (int l = 1; l < 103; l++) {
			int i1 = 24628 + (103 - l) * 512 * 4;
			for (int k1 = 1; k1 < 103; k1++) {

				if ((byteGroundArray[i][k1][l] & 0x18) == 0)
					landscapeScene.method309(ai, i1, i, k1, l);
				if (i < 3 && (byteGroundArray[i + 1][k1][l] & 8) != 0)
					landscapeScene.method309(ai, i1, i + 1, k1, l);
				i1 += 4;
			}

		}

		int j1 = ((238 + (int) (Math.random() * 20D)) - 10 << 16)
				+ ((238 + (int) (Math.random() * 20D)) - 10 << 8)
				+ ((238 + (int) (Math.random() * 20D)) - 10);
		int l1 = (238 + (int) (Math.random() * 20D)) - 10 << 16;
		if (loggedIn)
			miniMap.method343();
		for (int i2 = 1; i2 < 103; i2++) {
			for (int j2 = 1; j2 < 103; j2++) {
				if ((byteGroundArray[i][j2][i2] & 0x18) == 0)
					method50(i2, j1, j2, l1, i);
				if (i < 3 && (byteGroundArray[i + 1][j2][i2] & 8) != 0)
					method50(i2, j1, j2, l1, i + 1);
			}

		}

		if (loggedIn)
			gameScreenIP.initDrawingArea();
		anInt1071 = 0;
		for (int k2 = 0; k2 < 104; k2++) {
			for (int l2 = 0; l2 < 104; l2++) {
				int i3 = landscapeScene.method303(plane, k2, l2);
				if (i3 != 0) {
					i3 = i3 >> 14 & 0x7fff;
					int j3 = ObjectDefinition.forId(i3).anInt746;
					if (j3 >= 0) {
						int k3 = k2;
						int l3 = l2;
						if (j3 != 22 && j3 != 29 && j3 != 34 && j3 != 36
								&& j3 != 46 && j3 != 47 && j3 != 48) {
							byte byte0 = 104;
							byte byte1 = 104;
							int ai1[][] = clippingPlanes[plane].clip;
							for (int i4 = 0; i4 < 10; i4++) {
								int j4 = (int) (Math.random() * 4D);
								if (j4 == 0 && k3 > 0 && k3 > k2 - 3
										&& (ai1[k3 - 1][l3] & 0x1280108) == 0)
									k3--;
								if (j4 == 1 && k3 < byte0 - 1 && k3 < k2 + 3
										&& (ai1[k3 + 1][l3] & 0x1280180) == 0)
									k3++;
								if (j4 == 2 && l3 > 0 && l3 > l2 - 3
										&& (ai1[k3][l3 - 1] & 0x1280102) == 0)
									l3--;
								if (j4 == 3 && l3 < byte1 - 1 && l3 < l2 + 3
										&& (ai1[k3][l3 + 1] & 0x1280120) == 0)
									l3++;
							}

						}
						aClass30_Sub2_Sub1_Sub1Array1140[anInt1071] = mapFunctions[j3];
						anIntArray1072[anInt1071] = k3;
						anIntArray1073[anInt1071] = l3;
						anInt1071++;
					}
				}
			}

		}

	}

	public void refreshGroundItems(int x, int y) {
		Deque class19 = groundEntity[plane][x][y];
		if (class19 == null) {
			landscapeScene.method295(plane, x, y);
			return;
		}
		int defaultValue = 0xfa0a1f01;
		Object obj = null;
		for (Item item = (Item) class19.head(); item != null; item = (Item) class19
				.next()) {
			ItemDef definition = ItemDef.forId(item.id);
			int value = definition.value;
			if (definition.stackable)
				value *= item.amount + 1;
			// notifyItemSpawn(item, i + regionAbsBaseX, j + regionAbsBaseY);
			if (value > defaultValue) {
				defaultValue = value;
				obj = item;
			}
		}
		class19.insertTail(((Node) (obj)));
		Object obj1 = null;
		Object obj2 = null;
		for (Item class30_sub2_sub4_sub2_1 = (Item) class19.head(); class30_sub2_sub4_sub2_1 != null; class30_sub2_sub4_sub2_1 = (Item) class19
				.next()) {
			if (class30_sub2_sub4_sub2_1.id != ((Item) (obj)).id
					&& obj1 == null)
				obj1 = class30_sub2_sub4_sub2_1;
			if (class30_sub2_sub4_sub2_1.id != ((Item) (obj)).id
					&& class30_sub2_sub4_sub2_1.id != ((Item) (obj1)).id
					&& obj2 == null)
				obj2 = class30_sub2_sub4_sub2_1;
		}

		int i1 = x + (y << 7) + 0x60000000;
		landscapeScene.method281(x, i1, ((Animable) (obj1)),
				getHeight(plane, y * 128 + 64, x * 128 + 64),
				((Animable) (obj2)), ((Animable) (obj)), plane, y);
	}

	public void method26(boolean flag) {
		for (int j = 0; j < npcCount; j++) {
			NPC npc = npcArray[npcIndices[j]];
			int k = 0x20000000 + (npcIndices[j] << 14);
			if (npc == null || !npc.isVisible()
					|| npc.getDefinition().aBoolean93 != flag)
				continue;
			int l = npc.x >> 7;
			int i1 = npc.y >> 7;
			if (l < 0 || l >= 104 || i1 < 0 || i1 >= 104)
				continue;
			if (npc.size == 1 && (npc.x & 0x7f) == 64
					&& (npc.y & 0x7f) == 64) {
				if (anIntArrayArray929[l][i1] == anInt1265)
					continue;
				anIntArrayArray929[l][i1] = anInt1265;
			}
			if (!npc.getDefinition().aBoolean84)
				k += 0x80000000;
			landscapeScene
					.method285(plane, npc.anInt1552,
							getHeight(plane, npc.y, npc.x), k, npc.y,
							(npc.size - 1) * 64 + 60, npc.x, npc,
							npc.aBoolean1541);
		}
	}

	public boolean replayWave() {
		return signlink.wavereplay();
	}

	public void loadError() {
		String s = "ondemand";// was a constant parameter
		System.out.println(s);
		try {
			getAppletContext().showDocument(
					new URL(getCodeBase(), "loaderror_" + s + ".html"));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		do
			try {
				Thread.sleep(1000L);
			} catch (Exception _ex) {
			}
		while (true);
	}

	public void drawHoverBox(int xPos, int yPos, String text) {
		String[] results = text.split("\n");
		int height = (results.length * 16) + 6;
		int width;
		width = boldFont.getTextWidth(results[0]) + 6;
		for (int i = 1; i < results.length; i++)
			if (width <= boldFont.getTextWidth(results[i]) + 6)
				width = boldFont.getTextWidth(results[i]) + 6;
		DrawingArea.drawPixels(height, yPos, xPos, 0xFFFFA0, width);
		DrawingArea.fillPixels(xPos, width, height, 0, yPos);
		yPos += 14;
		for (int i = 0; i < results.length; i++) {
			boldFont.method389(false, xPos + 3, 0, results[i], yPos);
			yPos += 16;
		}
	}

	public void buildInterfaceMenu(int positionX, int positionY, int mouseX,
			int mouseY, int paddingY, RSInterface rsi) {
		try {
		if (rsi.type != 0 || rsi.children == null || rsi.hidden) {
			return;
		}
		if (mouseX < positionX || mouseY < positionY
				|| mouseX > positionX + rsi.width
				|| mouseY > positionY + rsi.height) {
			return;
		}
		int childrenLength = rsi.children.length;
		for (int index = 0; index < childrenLength; index++) {
			int xPos = rsi.childX[index] + positionX;
			int yPos = (rsi.childY[index] + positionY) - paddingY;
			RSInterface child = RSInterface.interfaceCache[rsi.children[index]];
			xPos += child.drawOffsetX;
			yPos += child.drawOffsetY;
			if ((child.hoverId >= 0 || child.anInt216 != 0)
					&& mouseX >= xPos && mouseY >= yPos
					&& mouseX < xPos + child.width
					&& mouseY < yPos + child.height)
				if (child.hoverId >= 0) {
					anInt886 = child.hoverId;
				} else {
					anInt886 = child.id;
				}
			if (child.type == 8 && mouseX >= xPos && mouseY >= yPos
					&& mouseX < xPos + child.width
					&& mouseY < yPos + child.height) {
				anInt1315 = child.id;
			}
			if (child.type == 0) {
				buildInterfaceMenu(xPos, yPos, mouseX, mouseY,
						child.scrollPosition, child);
				if (child.scrollMax > child.height) {
					method65(xPos + child.width, child.height, mouseX, mouseY, child, yPos, true, child.scrollMax);
				}
			} else {
				if (child.actionType == 1 && mouseX >= xPos && mouseY >= yPos
						&& mouseX < xPos + child.width
						&& mouseY < yPos + child.height) {
					boolean flag = false;
					if (child.contentType != 0)
						flag = buildFriendsListMenu(child);
					if (!flag) {
						menuActionName[menuActionRow] = "@lre@" + child.tooltip
								+ ", " + child.id;
						// menuActionName[menuActionRow] = rsi_1.tooltip;
						menuActionID[menuActionRow] = 315;
						menuActionCmd3[menuActionRow] = child.id;
						menuActionRow++;
					}
				}

				if (child.actionType == 2 && spellSelected == 0 && mouseX >= xPos && mouseY >= yPos && mouseX < xPos + child.width && mouseY < yPos + child.height) {
					String s = child.selectedActionName;
					if(s.indexOf(" ") != -1)
						s = s.substring(0, s.indexOf(" "));
					if (child.spellName.endsWith("Rush") || child.spellName.endsWith("Burst") || child.spellName.endsWith("Blitz") || child.spellName.endsWith("Barrage") || child.spellName.endsWith("strike") || child.spellName.endsWith("bolt") || child.spellName.equals("Crumble undead") || child.spellName.endsWith("blast") || child.spellName.endsWith("wave") || child.spellName.equals("Claws of Guthix") || child.spellName.equals("Flames of Zamorak") || child.spellName.equals("Magic Dart")) {
						menuActionName[menuActionRow] = "Autocast @gre@" + child.spellName;
						menuActionID[menuActionRow] = 104;
						menuActionCmd3[menuActionRow] = child.id;
						menuActionRow++;
					}
					menuActionName[menuActionRow] = s + " @gre@" + child.spellName;
					menuActionID[menuActionRow] = 626;
					menuActionCmd3[menuActionRow] = child.id;
					menuActionRow++;
					/**String s = child.selectedActionName;
					if (s.indexOf(" ") != -1) s = s.substring(0, s.indexOf(" "));
					menuActionName[menuActionRow] = "Autocast" + "@gre@ " + child.spellName;
               					menuActionID[menuActionRow] = 104;// autocast
                    				menuActionCmd3[menuActionRow] = child.id;
                    				menuActionRow++;
					menuActionName[menuActionRow] = s + " @gre@" + child.spellName;
					menuActionID[menuActionRow] = 626;
					menuActionCmd3[menuActionRow] = child.id;
					menuActionRow++;*/
				}
				if (child.actionType == 3 && mouseX >= xPos && mouseY >= yPos && mouseX < xPos + child.width && mouseY < yPos + child.height) {
					menuActionName[menuActionRow] = "Close";
					menuActionID[menuActionRow] = 200;
					menuActionCmd3[menuActionRow] = child.id;
					menuActionRow++;
				}
				if (child.actionType == 4 && mouseX >= xPos && mouseY >= yPos
						&& mouseX < xPos + child.width
						&& mouseY < yPos + child.height) {
					menuActionName[menuActionRow] = "@lre@" + child.tooltip
							+ ", " + child.id + ", "
							+ RSInterface.getConfigID(child.id);
					// menuActionName[menuActionRow] = rsi_1.tooltip;
					menuActionID[menuActionRow] = 169;
					menuActionCmd3[menuActionRow] = child.id;
					menuActionRow++;
				}
				if (child.actionType == 5 && mouseX >= xPos && mouseY >= yPos
						&& mouseX < xPos + child.width
						&& mouseY < yPos + child.height) {
					menuActionName[menuActionRow] = "@lre@" + child.tooltip
							+ ", " + child.id;
					// menuActionName[menuActionRow] = rsi_1.tooltip;
					menuActionID[menuActionRow] = 646;
					menuActionCmd3[menuActionRow] = child.id;
					menuActionRow++;
				}
				if (child.actionType == 6 && !aBoolean1149 && mouseX >= xPos
						&& mouseY >= yPos && mouseX < xPos + child.width
						&& mouseY < yPos + child.height) {
					menuActionName[menuActionRow] = "@lre@" + child.tooltip
							+ ", " + child.id;
					// menuActionName[menuActionRow] = rsi_1.tooltip;
					menuActionID[menuActionRow] = 679;
					menuActionCmd3[menuActionRow] = child.id;
					menuActionRow++;
				}
				if (child.actionType == 11) {
					if (super.clickMode2 == 2 && child.menuActions != null &&
							child.menuActions.length > 0) {
						for (int i = child.menuActions.length - 1; i >= 0; i--) {
							String action = child.menuActions[i];
							if (action != null) {
								menuActionName[menuActionRow] = child.actions[i];
								if (i == 0)
									menuActionID[menuActionRow] = 800;
								if (i == 1)
									menuActionID[menuActionRow] = 801;
								if (i == 2)
									menuActionID[menuActionRow] = 802;
								if (i == 3)
									menuActionID[menuActionRow] = 803;
								if (i == 4)
									menuActionID[menuActionRow] = 53;
								menuActionCmd1[menuActionRow] = 0;
								menuActionCmd2[menuActionRow] = 0;
								menuActionCmd3[menuActionRow] = child.id;
								menuActionRow++;
								System.out.println("klol");
							}
						}
					}
				}
				if (child.type == 2) {
					int k2 = 0;
					for (int l2 = 0; l2 < child.height; l2++) {
						for (int i3 = 0; i3 < child.width; i3++) {
							int x = xPos + i3 * (32 + child.invSpritePadX);
							int y = yPos + l2 * (32 + child.invSpritePadY);
							if (k2 < 20) {
								x += child.spritesX[k2];
								y += child.spritesY[k2];
							}
							if (mouseX >= x && mouseY >= y
									&& mouseX < x + 32 && mouseY < y + 32) {
								mouseInvInterfaceIndex = k2;
								lastActiveInvInterface = child.id;
								if (child.inventory[k2] > 0) {
									ItemDef itemDef = ItemDef
											.forId(child.inventory[k2] - 1);
									if (itemSelected == 1
											&& child.isInventoryInterface) {
										if (child.id != useEntityInterface
												|| k2 != useEntitySlot) {
											menuActionName[menuActionRow] = "Use "
													+ selectedItemName
													+ " with @lre@"
													+ itemDef.name;
											menuActionID[menuActionRow] = 870;
											menuActionCmd1[menuActionRow] = itemDef.id;
											menuActionCmd2[menuActionRow] = k2;
											menuActionCmd3[menuActionRow] = child.id;
											menuActionRow++;
										}
									} else if (spellSelected == 1
											&& child.isInventoryInterface) {
										if ((spellUsableOn & 0x10) == 16) {
											menuActionName[menuActionRow] = spellTooltip
													+ " @lre@" + itemDef.name;
											menuActionID[menuActionRow] = 543;
											menuActionCmd1[menuActionRow] = itemDef.id;
											menuActionCmd2[menuActionRow] = k2;
											menuActionCmd3[menuActionRow] = child.id;
											menuActionRow++;
										}
									} else {
										if (child.isInventoryInterface) {
											for (int l3 = 4; l3 >= 3; l3--)
												if (itemDef.actions != null
														&& itemDef.actions[l3] != null) {
													menuActionName[menuActionRow] = itemDef.actions[l3]
															+ " @lre@"
															+ itemDef.name;
													if (l3 == 3)
														menuActionID[menuActionRow] = 493;
													if (l3 == 4)
														menuActionID[menuActionRow] = 847;
													menuActionCmd1[menuActionRow] = itemDef.id;
													menuActionCmd2[menuActionRow] = k2;
													menuActionCmd3[menuActionRow] = child.id;
													menuActionRow++;
												} else if (l3 == 4) {
													menuActionName[menuActionRow] = "Drop @lre@"
															+ itemDef.name;
													menuActionID[menuActionRow] = 847;
													menuActionCmd1[menuActionRow] = itemDef.id;
													menuActionCmd2[menuActionRow] = k2;
													menuActionCmd3[menuActionRow] = child.id;
													menuActionRow++;
												}
										}
										if (child.usableItemInterface) {
											menuActionName[menuActionRow] = "Use @lre@"
													+ itemDef.name;
											menuActionID[menuActionRow] = 447;
											menuActionCmd1[menuActionRow] = itemDef.id;
											menuActionCmd2[menuActionRow] = k2;
											menuActionCmd3[menuActionRow] = child.id;
											menuActionRow++;
										}
										if (child.isInventoryInterface
												&& itemDef.actions != null) {
											for (int i4 = 2; i4 >= 0; i4--)
												if (itemDef.actions[i4] != null) {
													menuActionName[menuActionRow] = itemDef.actions[i4]
															+ " @lre@"
															+ itemDef.name;
													if (i4 == 0)
														menuActionID[menuActionRow] = 74;
													if (i4 == 1)
														menuActionID[menuActionRow] = 454;
													if (i4 == 2)
														menuActionID[menuActionRow] = 539;
													menuActionCmd1[menuActionRow] = itemDef.id;
													menuActionCmd2[menuActionRow] = k2;
													menuActionCmd3[menuActionRow] = child.id;
													menuActionRow++;
												}
										}
										if (child.actions != null) {
											for (int j4 = 4; j4 >= 0; j4--)
												if (child.actions[j4] != null) {
													menuActionName[menuActionRow] = child.actions[j4]
															+ " @lre@"
															+ itemDef.name;
													if (j4 == 0)
														menuActionID[menuActionRow] = 632;
													if (j4 == 1)
														menuActionID[menuActionRow] = 78;
													if (j4 == 2)
														menuActionID[menuActionRow] = 867;
													if (j4 == 3)
														menuActionID[menuActionRow] = 431;
													if (j4 == 4)
														menuActionID[menuActionRow] = 53;
													menuActionCmd1[menuActionRow] = itemDef.id;
													menuActionCmd2[menuActionRow] = k2;
													menuActionCmd3[menuActionRow] = child.id;
													menuActionRow++;
												}

										}
										menuActionName[menuActionRow] = "Examine @lre@"
												+ itemDef.name
												+ " ("
												+ (child.inventory[k2] - 1) + ")";
										// menuActionName[menuActionRow] =
										// "Examine @lre@" + itemDef.name;
										menuActionID[menuActionRow] = 1125;
										menuActionCmd1[menuActionRow] = itemDef.id;
										menuActionCmd2[menuActionRow] = k2;
										menuActionCmd3[menuActionRow] = child.id;
										menuActionRow++;
									}
								}
							}
							k2++;
						}
					}
				}
			}
		}
		} catch (NullPointerException e) {
			//e.printStackTrace();
		}
	}

	public void drawScrollbar(int barHeight, int scrollPos, int yPos,
			int xPos, int contentHeight, boolean newScroller,
			boolean isTransparent) {
		int backingAmount = (barHeight - 32) / (isTransparent ? 2 : 5);
		int scrollPartHeight = ((barHeight - 32) * barHeight) / contentHeight;
		int scrollerID;
		if (newScroller) {
			scrollerID = 4;
		} else if (isTransparent) {
			scrollerID = 8;
		} else {
			scrollerID = 0;
		}
		if (scrollPartHeight < 10)
			scrollPartHeight = 10;
		int scrollPartAmount = (scrollPartHeight / (isTransparent ? 1 : 5)) - 2;
		int scrollPartPos = ((barHeight - 32 - scrollPartHeight) * scrollPos)
				/ (contentHeight - barHeight) + 16 + yPos;
		/* Bar fill */
		for (int i = 0, yyPos = yPos + 16; i <= (isTransparent ? backingAmount / 2 + 6
				: backingAmount); i++, yyPos += (isTransparent ? 3 : 5)) {
			if (isTransparent) {
				cacheSprite[84].drawARGBSprite(xPos, yyPos);
			} else {
				scrollPart[scrollerID + 1].drawSprite(xPos, yyPos);
			}
		}
		/* Top of bar */
		if (isTransparent) {
			cacheSprite[85].drawARGBSprite(xPos, scrollPartPos);
		} else
			scrollPart[scrollerID + 2].drawSprite(xPos, scrollPartPos);
		scrollPartPos += 5;
		/* Middle of bar */
		for (int i = 0; i <= (isTransparent ? scrollPartAmount - 9
				: scrollPartAmount); i++) {
			if (isTransparent) {
				cacheSprite[86].drawARGBSprite(xPos, scrollPartPos);
			} else
				scrollPart[scrollerID + 3].drawSprite(xPos, scrollPartPos);
			scrollPartPos += isTransparent ? 1 : 5;
		}
		scrollPartPos = ((barHeight - 32 - scrollPartHeight) * scrollPos)
				/ (contentHeight - barHeight) + 16 + yPos
				+ (scrollPartHeight - 5);
		/* Bottom of bar */
		if (isTransparent) {
			cacheSprite[87].drawARGBSprite(xPos, scrollPartPos);
		} else
			scrollPart[scrollerID].drawSprite(xPos, scrollPartPos);
		/* Arrows */
		if (newScroller) {
			scrollBar[2].drawSprite(xPos, yPos);
			scrollBar[3].drawSprite(xPos, (yPos + barHeight) - 16);
		} else if (isTransparent) {
			cacheSprite[82].drawARGBSprite(xPos, yPos);
			cacheSprite[83].drawARGBSprite(xPos, (yPos + barHeight) - 16);
		} else {
			scrollBar[0].drawSprite(xPos, yPos);
			scrollBar[1].drawSprite(xPos, (yPos + barHeight) - 16);
		}
	}

	public void updateNpcs(JagexBuffer stream, int i) {
		anInt839 = 0;
		anInt893 = 0;
		method139(stream);
		addNewNpc(i, stream);
		updateNpcBlocks(stream);
		for (int k = 0; k < anInt839; k++) {
			int l = anIntArray840[k];
			if (npcArray[l].anInt1537 != loopCycle) {
				npcArray[l].id_2 = -1;
				npcArray[l] = null;
			}
		}

		if (stream.currentOffset != i) {
			signlink.reporterror(myUsername
					+ " size mismatch packet getnpcpos - pos:"
					+ stream.currentOffset + " psize:" + i);
			throw new RuntimeException("eek");
		}
		for (int i1 = 0; i1 < npcCount; i1++)
			if (npcArray[npcIndices[i1]] == null) {
				signlink.reporterror(myUsername
						+ " null entry packet npc list - pos:" + i1 + " size:"
						+ npcCount);
				throw new RuntimeException("eek");
			}

	}

	public int quickHOffsetX = shownSelection != -1 ? 110 : 0;

	public void processQuickChatArea() {
		int y = clientHeight - 503;
		if (super.mouseX < 117 && super.mouseY > 363) {
			quickHOffsetX = 0;
			quickHoverY2 = -1;
		} else if (super.mouseX > 117 && super.mouseX < 117 + 158
				&& super.mouseY > 363) {
			quickHOffsetX = 110;
			quickHoverY2 = -1;
		} else {
			quickHOffsetX = 269;
			quickHoverY2 = quickHoverY;
		}
		if (super.mouseX >= 7 && super.mouseX <= 23 && super.mouseY >= y + 345
				&& super.mouseY <= y + 361) {
			cButtonHPos = 8;
			aBoolean1233 = true;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 364 && super.mouseY <= y + 377) {
			quickHoverY = 25;
			aBoolean1233 = true;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 378 && super.mouseY <= y + 391) {
			quickHoverY = 39;
			aBoolean1233 = true;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 392 && super.mouseY <= y + 405) {
			quickHoverY = 53;
			aBoolean1233 = true;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 406 && super.mouseY <= y + 419) {
			quickHoverY = 67;
			aBoolean1233 = true;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 420 && super.mouseY <= y + 433) {
			quickHoverY = 81;
			aBoolean1233 = true;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 434 && super.mouseY <= y + 447) {
			quickHoverY = 95;
			aBoolean1233 = true;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 448 && super.mouseY <= y + 461) {
			quickHoverY = 109;
			aBoolean1233 = true;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 462 && super.mouseY <= y + 474
				&& shownSelection == 0) {
			quickHoverY = 123;
			aBoolean1233 = true;
			inputTaken = true;
		} else {
			quickHoverY = -1;
			quickHoverY2 = -1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (super.clickMode3 == 1) {
			if (super.saveClickX >= 8 && super.saveClickX <= 117
					&& super.saveClickY >= y + 364
					&& super.saveClickY <= y + 377) {
				setSelection(25, "Quick Chat @bla@-> @blu@General", 0);
			} else if (super.saveClickX >= 8 && super.saveClickX <= 117
					&& super.saveClickY >= y + 378
					&& super.saveClickY <= y + 391) {
				setSelection(39, "Quick Chat @bla@-> @blu@Trade/Items", 1);
			} else if (clickInRegion(118, clientHeight - 126, 118 + 156,
					clientHeight - 113)) {
				if (shownSelection == 0) {
					setSelectedSelection(
							25,
							39,
							"Quick Chat @bla@-> @blu@General @bla@-> @blu@Hello",
							0);
				}
			} else if (clickInRegion(277, clientHeight - 140, 277 + 156,
					clientHeight - 126)) {
				if (shownSelectedSelection == 0) {
					quickSay("Hi.");
				}
			} else if (clickInRegion(277, clientHeight - 126, 277 + 156,
					clientHeight - 112)) {
				if (shownSelectedSelection == 0) {
					quickSay("Hey!");
				}
			} else if (clickInRegion(277, clientHeight - 112, 277 + 156,
					clientHeight - 98)) {
				if (shownSelectedSelection == 0) {
					quickSay("Sup?");
				}
			} else if (clickInRegion(277, clientHeight - 98, 277 + 156,
					clientHeight - 84)) {
				if (shownSelectedSelection == 0) {
					quickSay("Hello.");
				}
			} else if (clickInRegion(277, clientHeight - 84, 277 + 156,
					clientHeight - 70)) {
				if (shownSelectedSelection == 0) {
					quickSay("Yo dawg.");
				}
			} else if (clickInRegion(7, clientHeight - 56, 116,
					clientHeight - 42)) {
				quickSay("I'm muted and I can only use quick chat.");
			} else {
				aBoolean1233 = true;
				inputTaken = true;
			}
		}
	}

	public void setSelection(int y, String directory, int selection) {
		quickSelY = y;
		quickSelY2 = -1;
		divideSelections = true;
		divideSelectedSelections = false;
		quickChatDir = directory;
		shownSelection = selection;
		shownSelectedSelection = -1;
		aBoolean1233 = true;
		inputTaken = true;
	}

	public void setSelectedSelection(int y1, int y2, String directory,
			int selectedSelection) {
		divideSelections = true;
		divideSelectedSelections = true;
		quickSelY = y1;
		quickSelY2 = y2;
		quickChatDir = directory;
		shownSelectedSelection = selectedSelection;
		aBoolean1233 = true;
		inputTaken = true;
	}

	public void quickSay(String text) {
		playerChat(text, true);
		isQuickChat = true;
		resetQuickChat();
		quickChat = false;
		canTalk = true;
		inputTaken = true;
	}

	public int hoverPos;

	public void processMapAreaMouse() {
		if (mouseInRegion(clientWidth - (clientSize == 0 ? 249 : 217),
				clientSize == 0 ? 46 : 3, clientWidth
						- (clientSize == 0 ? 249 : 217) + 34,
				(clientSize == 0 ? 46 : 3) + 34)) {
			hoverPos = 0;// xp counter
		} else if (mouseInRegion(clientSize == 0 ? clientWidth - 58
				: getOrbX(1), getOrbY(1), (clientSize == 0 ? clientWidth - 58
				: getOrbX(1)) + 57, getOrbY(1) + 34)) {
			hoverPos = 1;// prayer
		} else if (mouseInRegion(clientSize == 0 ? clientWidth - 58
				: getOrbX(2), getOrbY(2), (clientSize == 0 ? clientWidth - 58
				: getOrbX(2)) + 57, getOrbY(2) + 34)) {
			hoverPos = 2;// run
		} else if (mouseInRegion(clientSize == 0 ? clientWidth - 74
				: getOrbX(3), getOrbY(3), (clientSize == 0 ? clientWidth - 74
				: getOrbX(3)) + 57, getOrbY(3) + 34)) {
			hoverPos = 3;// summoning
		} else {
			hoverPos = -1;
		}
	}

	public int cButtonHPos;
	public int cButtonHCPos;
	public int cButtonCPos;
	public boolean showChat = true;
	public int channel;

	public void processChatModeClick() {
		int[] x = { 5, 62, 119, 176, 233, 290, 347, 404 };
		if (super.mouseX >= x[0] && super.mouseX <= x[0] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 0;
			aBoolean1233 = true;
			inputTaken = true;
		} else if (super.mouseX >= x[1] && super.mouseX <= x[1] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 1;
			aBoolean1233 = true;
			inputTaken = true;
		} else if (super.mouseX >= x[2] && super.mouseX <= x[2] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 2;
			aBoolean1233 = true;
			inputTaken = true;
		} else if (super.mouseX >= x[3] && super.mouseX <= x[3] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 3;
			aBoolean1233 = true;
			inputTaken = true;
		} else if (super.mouseX >= x[4] && super.mouseX <= x[4] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 4;
			aBoolean1233 = true;
			inputTaken = true;
		} else if (super.mouseX >= x[5] && super.mouseX <= x[5] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 5;
			aBoolean1233 = true;
			inputTaken = true;
		} else if (super.mouseX >= x[6] && super.mouseX <= x[6] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 6;
			aBoolean1233 = true;
			inputTaken = true;
		} else if (super.mouseX >= x[7] && super.mouseX <= x[7] + 111
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 7;
			aBoolean1233 = true;
			inputTaken = true;
		} else {
			cButtonHPos = -1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (super.clickMode3 == 1) {
			if (super.saveClickX >= x[0] && super.saveClickX <= x[0] + 56
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				if (clientSize != 0) {
					if (channel != 0) {
						cButtonCPos = 0;
						chatTypeView = 0;
						aBoolean1233 = true;
						inputTaken = true;
						channel = 0;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 0;
					chatTypeView = 0;
					aBoolean1233 = true;
					inputTaken = true;
					channel = 0;
				}
				stream.createFrame(95);
				stream.writeByte(publicChatMode);
				stream.writeByte(publicChatMode);
				stream.writeByte(tradeMode);
			} else if (super.saveClickX >= x[1]
					&& super.saveClickX <= x[1] + 56
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				if (clientSize != 0) {
					if (channel != 1 && clientSize != 0) {
						cButtonCPos = 1;
						chatTypeView = 5;
						aBoolean1233 = true;
						inputTaken = true;
						channel = 1;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 1;
					chatTypeView = 5;
					aBoolean1233 = true;
					inputTaken = true;
					channel = 1;
				}
				stream.createFrame(95);
				stream.writeByte(publicChatMode);
				stream.writeByte(publicChatMode);
				stream.writeByte(tradeMode);
			} else if (super.saveClickX >= x[2]
					&& super.saveClickX <= x[2] + 56
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				if (clientSize != 0) {
					if (channel != 2 && clientSize != 0) {
						cButtonCPos = 2;
						chatTypeView = 1;
						aBoolean1233 = true;
						inputTaken = true;
						channel = 2;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 2;
					chatTypeView = 1;
					aBoolean1233 = true;
					inputTaken = true;
					channel = 2;
				}
				stream.createFrame(95);
				stream.writeByte(publicChatMode);
				stream.writeByte(publicChatMode);
				stream.writeByte(tradeMode);
			} else if (super.saveClickX >= x[3]
					&& super.saveClickX <= x[3] + 56
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				if (clientSize != 0) {
					if (channel != 3 && clientSize != 0) {
						cButtonCPos = 3;
						chatTypeView = 2;
						aBoolean1233 = true;
						inputTaken = true;
						channel = 3;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 3;
					chatTypeView = 2;
					aBoolean1233 = true;
					inputTaken = true;
					channel = 3;
				}
				stream.createFrame(95);
				stream.writeByte(publicChatMode);
				stream.writeByte(publicChatMode);
				stream.writeByte(tradeMode);
			} else if (super.saveClickX >= x[4]
					&& super.saveClickX <= x[4] + 56
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				if (clientSize != 0) {
					if (channel != 4 && clientSize != 0) {
						cButtonCPos = 4;
						chatTypeView = 11;
						aBoolean1233 = true;
						inputTaken = true;
						channel = 4;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 4;
					chatTypeView = 11;
					aBoolean1233 = true;
					inputTaken = true;
					channel = 4;
				}
				stream.createFrame(95);
				stream.writeByte(publicChatMode);
				stream.writeByte(publicChatMode);
				stream.writeByte(tradeMode);
			} else if (super.saveClickX >= x[5]
					&& super.saveClickX <= x[5] + 56
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				if (clientSize != 0) {
					if (channel != 5 && clientSize != 0) {
						cButtonCPos = 5;
						chatTypeView = 3;
						aBoolean1233 = true;
						inputTaken = true;
						channel = 5;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 5;
					chatTypeView = 3;
					aBoolean1233 = true;
					inputTaken = true;
					channel = 5;
				}
				stream.createFrame(95);
				stream.writeByte(publicChatMode);
				stream.writeByte(publicChatMode);
				stream.writeByte(tradeMode);
			} else if (super.saveClickX >= x[6]
					&& super.saveClickX <= x[6] + 56
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				if (clientSize != 0) {
					if (channel != 6 && clientSize != 0) {
						cButtonCPos = 6;
						chatTypeView = 6;
						aBoolean1233 = true;
						inputTaken = true;
						channel = 6;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 6;
					chatTypeView = 6;
					aBoolean1233 = true;
					inputTaken = true;
					channel = 6;
				}
				stream.createFrame(95);
				stream.writeByte(publicChatMode);
				stream.writeByte(publicChatMode);
				stream.writeByte(tradeMode);
			} else if (super.saveClickX >= 404 && super.saveClickX <= 515
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				if (openInterfaceID == -1) {
					clearTopInterfaces();
					CustomUserInput.input = "";
					reportAbuseInput = "";
					canMute = false;
					for (int i = 0; i < RSInterface.interfaceCache.length; i++) {
						if (RSInterface.interfaceCache[i] == null
								|| RSInterface.interfaceCache[i].contentType != 600)
							continue;
						reportAbuseInterfaceID = openInterfaceID = RSInterface.interfaceCache[i].parentId;
						break;
					}
				} else {
					pushMessage(
							"",
							"Please close the interface you have open before using 'report abuse'", 0);
				}
			}
			if (!showChat) {
				cButtonCPos = -1;
			}
		}
	}

	public void toggleInterface(int configID) {
		int action = Varp.cache[configID].anInt709;
		if (action == 0)
			return;
		int config = variousSettings[configID];
		if (action == 1) {
			if (config == 1)
				Rasterizer.calculatePalette(0.9F);
			if (config == 2)
				Rasterizer.calculatePalette(0.8F);
			if (config == 3)
				Rasterizer.calculatePalette(0.7F);
			if (config == 4)
				Rasterizer.calculatePalette(0.6F);
			ItemDef.mruNodes1.unlinkAll();
			welcomeScreenRaised = true;
		}
		if (action == 3) {
			boolean flag1 = musicEnabled;
			if (config == 0) {
				adjustVolume(musicEnabled, 0);
				musicEnabled = true;
			}
			if (config == 1) {
				adjustVolume(musicEnabled, -400);
				musicEnabled = true;
			}
			if (config == 2) {
				adjustVolume(musicEnabled, -800);
				musicEnabled = true;
			}
			if (config == 3) {
				adjustVolume(musicEnabled, -1200);
				musicEnabled = true;
			}
			if (config == 4)
				musicEnabled = false;
			if (musicEnabled != flag1 && !lowMemory) {
				if (musicEnabled) {
					nextSong = currentSong;
					songChanging = true;
					resourceProvider.loadMandatory(2, nextSong);
				} else {
					stopMidi();
				}
				previousSong = 0;
			}
		}
		if (action == 4) {
			if (config == 0) {
				aBoolean848 = true;
				setWaveVolume(0);
			}
			if (config == 1) {
				aBoolean848 = true;
				setWaveVolume(-400);
			}
			if (config == 2) {
				aBoolean848 = true;
				setWaveVolume(-800);
			}
			if (config == 3) {
				aBoolean848 = true;
				setWaveVolume(-1200);
			}
			if (config == 4)
				aBoolean848 = false;
		}
		if (action == 5) {
			anInt1253 = config;
		}
		if (action == 6) {
			anInt1249 = config;
		}
		if (action == 8) {
			splitpublicChat = config;
			inputTaken = true;
		}
		if (action == 9) {
			anInt913 = config;
		}
		switch (action) {
		case 10:
			cameraZoom = config + 1;
			break;
		case 11:
			toggleSize(config - 1);
			break;
		case 12:
			rememberMe = config == 1;
			break;
		case 13:
			//constitution = config == 1;
			break;
		case 14:
			//newContext = config == 1;
			break;
		case 15:
			menuAnimations = config == 1;
			break;
		case 16:
			timeStamp = config == 1;
			break;
		}
	}
	
	/**
	 * Used to set a sprite to another sprite. An example
	 * is the clan chat's lootshare sprite, which has 4
	 * different sprites.
	 * @param childId	The id of the interface child.
	 * @param spriteId	The new sprite id located in sprites.dat.
	 */
	private void changeSprite(int childId, int... spriteId) {
		RSInterface rsi = RSInterface.interfaceCache[childId];
		if (rsi != null) {
			if (cacheSprite[spriteId[0]] != null && spriteId[0] > 0)
				rsi.sprite1 = cacheSprite[spriteId[0]];
			if (cacheSprite[spriteId[1]] != null && spriteId[1] > 0)
				rsi.sprite2 = cacheSprite[spriteId[1]];
		}
	}

	public void buildHitMarkOrig(Entity e, int cycle, int x, int y, int type, int mark, String dmg) {
		int damage = Integer.valueOf(dmg);
		if (cycle > loopCycle) {
			y -= 20;
			int trans = e.hitMarkAlpha;
			int shadetrans = (trans / 3) >= 0 ? (trans / 3) : 0;
			int difference = (cycle - loopCycle);
			int height = (int) Math.round(difference / 5);
			if (height < 6) {
				e.hitMarkAlpha -= e.hitMarkAlpha <= 256 && e.hitMarkAlpha > 0 ? 16
						: 0;
				e.hitMarkAlpha = e.hitMarkAlpha <= 0 ? 0 : e.hitMarkAlpha;
			}
			if (mark != 0) {
				int width = 13;
				int spriteIndex = e.hitMarkMax ? 4 : 1;
				if (damage >= 10) {
					width = 22;
				}
				if (damage >= 100) {
					width = 30;
				}
				x -= (width / 2);
				x += 7;
				hitStyle[e.combatIcon].drawSprite(x
						- hitStyle[e.combatIcon].myWidth - 1, (y - 8 + height),
						trans);
				hitMark[spriteIndex].drawSprite(x, (y - 8 + height), trans);
				hitShadow[0].drawSprite(x - 1, (y - 9 + height), shadetrans);
				x += 3;
				hitMark[spriteIndex + 1].drawSprite(x, (y - 8 + height), trans);
				hitShadow[1].drawSprite(x, (y - 9 + height), shadetrans);
				x += 4;
				hitMark[spriteIndex + 1].drawSprite(x, (y - 8 + height), trans);
				hitShadow[1].drawSprite(x, (y - 9 + height), shadetrans);
				x += 4;
				if (damage >= 10) {
					hitMark[spriteIndex + 1].drawSprite(x, (y - 8 + height),
							trans);
					hitShadow[1].drawSprite(x, (y - 9 + height), shadetrans);
					x += 4;
					hitMark[spriteIndex + 1].drawSprite(x, (y - 8 + height),
							trans);
					hitShadow[1].drawSprite(x, (y - 9 + height), shadetrans);
					x += 4;
				}
				if (damage >= 100) {
					hitMark[spriteIndex + 1].drawSprite(x, (y - 8 + height),
							trans);
					hitShadow[1].drawSprite(x, (y - 9 + height), shadetrans);
					x += 4;
					hitMark[spriteIndex + 1].drawSprite(x, (y - 8 + height),
							trans);
					hitShadow[1].drawSprite(x, (y - 9 + height), shadetrans);
					x += 4;
				}
				hitMark[spriteIndex + 2].drawSprite(x, (y - 8 + height), trans);
				hitShadow[2].drawSprite(x, (y - 9 + height), shadetrans);
				RSFontSystem font = e.hitMarkMax ? bigHitFont : regularHitFont;
				if (font.equals(regularHitFont)) {
					y += 29;
				}
				font.drawCenteredString("<trans=" + trans + ">" + dmg,
						spriteDrawX + 5, (y + 5 + height), 0xffffff, -1);
			} else {
				hitMark[0].drawSprite(spriteDrawX - (hitMark[0].myWidth / 2), y
						- 15 + height, trans);
			}
		}
	}
	
	public Sprite getStyleSprite(int icon) {
		switch (icon) {
		case 3:
		case 4:
			icon += 1;
			break;
		}
		if (icon >= 0 && icon <= 5) {
			return cacheSprite[icon + 107];
		}
		return null;
	}
	
	public void drawHitmark(Entity entity, int x, int y, int hit, String dmg, int type, int hitmask, int cycle) {
		int damage = Integer.valueOf(dmg);
		if (cycle > loopCycle) {
			y -= 20;
			int difference = (cycle - loopCycle);
			int height = (int) Math.round(difference / 5);
			if (height < 6) {
				entity.hitAlpha[hit] -= entity.hitAlpha[hit] <= 256 && entity.hitAlpha[hit] > 0 ? 16 : 0;
				entity.hitAlpha[hit] = entity.hitAlpha[hit] <= 0 ? 0 : entity.hitAlpha[hit];
			}
			int trans = entity.hitAlpha[hit];
			int soak = entity.hitSoak[hit];
			int offsetX = entity.hitIcon[hit] == -1 ? 20 : 27;
			if (soak > 0 && soak != 65535) {
				int middle = damage < 100 ? 147 : 144;
				x -= offsetX;
				spriteDrawX -= offsetX;
				cacheSprite[110].drawSprite(x + offsetX - 5, (y - 8 + height), trans);
				cacheSprite[middle - 1].drawARGBSprite(x + offsetX + 10, (y - 9 + height), trans);//start
				cacheSprite[middle].drawARGBSprite(x + offsetX + 14, (y - 9 + height), trans);//middle
				cacheSprite[middle].drawARGBSprite(x + offsetX + 18, (y - 9 + height), trans);
				cacheSprite[middle].drawARGBSprite(x + offsetX + 22, (y - 9 + height), trans);
				cacheSprite[middle].drawARGBSprite(x + offsetX + 26, (y - 9 + height), trans);
				cacheSprite[middle + 1].drawARGBSprite(x + offsetX + 30, (y - 9 + height), trans);//end
				regularHitFont.drawCenteredString("<trans=" + (trans) + ">" + Integer.toString(soak), x + offsetX + 22, y + 35 + height, 0xffffff, -1);
			}
			if (damage != 0) {
				int width = 13;
				if (damage >= 10) {
					width = 22;
				}
				if (damage >= 100) {
					width = 30;
				}
				x -= (width / 2);
				x += 7;
				if (getStyleSprite(entity.hitIcon[hit]) != null) {
					getStyleSprite(entity.hitIcon[hit]).drawARGBSprite(x - getStyleSprite(entity.hitIcon[hit]).myWidth - 2, (y - 8 + height), trans);
				}
				if (entity.hitIcon[hit] == -1) {
					x -= 7;
				}
				int hitmaskId = 113;
				hitmaskId += hitmask == 0 ? 1 : hitmask == 1 ? 4 : hitmask == 2 ? 7 : 
					hitmask == 3 ? 10 : hitmask == 4 ? 13 : hitmask == 5 ? 16 :
					hitmask == 6 ? 19 : hitmask == 7 ? 22 : hitmask == 8 ? 25 :
					hitmask == 9 ? 28 : 31;
				Sprite startHit = cacheSprite[hitmaskId - 1];
				Sprite mainHit = cacheSprite[hitmaskId];
				Sprite endHit = cacheSprite[hitmaskId + 1];
				startHit.drawARGBSprite(x, (y - 9  + height), trans);
				x += 4;
				mainHit.drawARGBSprite(x, (y - 9  + height), trans);
				x += 4;
				if (damage >= 10) {
					mainHit.drawARGBSprite(x, (y - 9  + height), trans);
					x += 4;
					mainHit.drawARGBSprite(x, (y - 9  + height), trans);
					x += 4;
					spriteDrawX += 1;
				}
				if (damage >= 100) {
					mainHit.drawARGBSprite(x, (y - 9  + height), trans);
					x += 4;
					mainHit.drawARGBSprite(x, (y - 9  + height), trans);
					x += 4;
				}
				endHit.drawARGBSprite(x, (y - 9  + height), trans);
				RSFontSystem font = hitmask == 1 ? bigHitFont : regularHitFont;
				if (font.equals(regularHitFont)) {
					y += 30;
				}
				font.drawCenteredString("<trans=" + trans + ">" + dmg, spriteDrawX + (entity.hitIcon[hit] == -1 ? -2 : 5) + (damage < 10 ? 2 : 0), (y + 5 + height), 0xffffff, -1);
			} else {
				hitMark[0].drawSprite(spriteDrawX - (hitMark[0].myWidth / 2), y - 15 + height, trans);
			}
		}
	}

	public void updateEntities() {
		try {
			int anInt974 = 0;
			for (int j = -1; j < playerCount + npcCount; j++) {
				Object obj;
				if (j == -1)
					obj = myPlayer;
				else if (j < playerCount)
					obj = playerArray[playerIndices[j]];
				else
					obj = npcArray[npcIndices[j - playerCount]];
				if (obj == null || !((Entity) (obj)).isVisible())
					continue;
				if (obj instanceof NPC) {
					NPCDefinition entityDef = ((NPC) obj).getDefinition();
					if (entityDef.childrenIDs != null)
						entityDef = entityDef.method161();
					if (entityDef == null)
						continue;
				}
				if (j < playerCount) {
					int l = 30;
					Player player = (Player) obj;
					if (player.prayerId >= 0 || player.skullId >= 0) {
						npcScreenPos(((Entity) (obj)),
								((Entity) (obj)).height + 15);
						if (spriteDrawX > -1) {
							if (player.skullId < 2 && player.skullId != -1) {
								skullIcons[player.skullId].drawSprite(spriteDrawX - 12, spriteDrawY - l);
								l += 25;
							}
							if (player.prayerId < 19 && player.prayerId != -1) {
								headIcons[player.prayerId].drawSprite(spriteDrawX - 12, spriteDrawY - l);
								l += 18;
							}
						}
					}
					if (j >= 0 && headIconType == 10
							&& otherPlayerIndex == playerIndices[j]) {
						npcScreenPos(((Entity) (obj)),
								((Entity) (obj)).height + 15);
						if (spriteDrawX > -1)
							headIconsHint[player.hintIcon].drawSprite(
									spriteDrawX - 12, spriteDrawY - l);
					}
				} else {
					NPCDefinition entityDef_1 = ((NPC) obj).getDefinition();
					if (entityDef_1.anInt75 >= 0
							&& entityDef_1.anInt75 < headIcons.length) {
						npcScreenPos(((Entity) (obj)),
								((Entity) (obj)).height + 15);
						if (spriteDrawX > -1)
							headIcons[entityDef_1.anInt75].drawSprite(
									spriteDrawX - 12, spriteDrawY - 30);
					}
					if (headIconType == 1
							&& anInt1222 == npcIndices[j - playerCount]
							&& loopCycle % 20 < 10) {
						npcScreenPos(((Entity) (obj)),
								((Entity) (obj)).height + 15);
						if (spriteDrawX > -1)
							headIconsHint[0].drawSprite(spriteDrawX - 12,
									spriteDrawY - 28);
					}
				}
				if (((Entity) (obj)).textSpoken != null
						&& (j >= playerCount || publicChatMode == 0
								|| publicChatMode == 3 || publicChatMode == 1
								&& isFriendOrSelf(((Player) obj).name))) {
					npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height);
					if (spriteDrawX > -1 && anInt974 < anInt975) {
						anIntArray979[anInt974] = boldFont
								.method384(((Entity) (obj)).textSpoken) / 2;
						anIntArray978[anInt974] = boldFont.anInt1497;
						anIntArray976[anInt974] = spriteDrawX;
						anIntArray977[anInt974] = spriteDrawY;
						anIntArray980[anInt974] = ((Entity) (obj)).textColour;
						anIntArray981[anInt974] = ((Entity) (obj)).textEffect;
						anIntArray982[anInt974] = ((Entity) (obj)).textCycle;
						aStringArray983[anInt974++] = ((Entity) (obj)).textSpoken;
						if (anInt1249 == 0 && ((Entity) (obj)).textEffect >= 1
								&& ((Entity) (obj)).textEffect <= 3) {
							anIntArray978[anInt974] += 10;
							anIntArray977[anInt974] += 5;
						}
						if (anInt1249 == 0 && ((Entity) (obj)).textEffect == 4)
							anIntArray979[anInt974] = 60;
						if (anInt1249 == 0 && ((Entity) (obj)).textEffect == 5)
							anIntArray978[anInt974] += 5;
					}
				}
				if (((Entity) (obj)).loopCycleStatus > loopCycle) {
					try {
						npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
						if (spriteDrawX > -1) {
							int width = 56;
							Entity e = ((Entity) (obj));
							//System.out.println(e.currentHealth + ", " + e.maxHealth);
							int barWidth = (e.currentHealth * width) / e.maxHealth;
							if (barWidth > width) {
								barWidth = width;
							}
							hitBar[0].drawSprite(spriteDrawX - (width / 2), spriteDrawY - 7);
							full.myPixels = hitBar[1].cutPixels(hitBar[1].myPixels, hitBar[1].myWidth, hitBar[1].myHeight, barWidth, 7);
							full.myWidth = barWidth;
							full.drawSprite(spriteDrawX - (width / 2), spriteDrawY - 7);
						}
					} catch (Exception e) {
					}
				}
				for (int hit = 0; hit < 4; hit++) {
					Entity e = (Entity) obj;
					if (e.hitsLoopCycle[hit] > loopCycle) {
						npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height / 2);
						if (spriteDrawX > -1) {
							spriteDrawY += hit * 22;
							drawHitmark(e, spriteDrawX, spriteDrawY, hit, String.valueOf(e.hitArray[hit]), 0, e.hitMarkTypes[hit], e.hitsLoopCycle[hit]);
						}
					}
				}
			}
			for (int k = 0; k < anInt974; k++) {
				int k1 = anIntArray976[k];
				int l1 = anIntArray977[k];
				int j2 = anIntArray979[k];
				int k2 = anIntArray978[k];
				boolean flag = true;
				while (flag) {
					flag = false;
					for (int l2 = 0; l2 < k; l2++)
						if (l1 + 2 > anIntArray977[l2] - anIntArray978[l2]
								&& l1 - k2 < anIntArray977[l2] + 2
								&& k1 - j2 < anIntArray976[l2]
										+ anIntArray979[l2]
								&& k1 + j2 > anIntArray976[l2]
										- anIntArray979[l2]
								&& anIntArray977[l2] - anIntArray978[l2] < l1) {
							l1 = anIntArray977[l2] - anIntArray978[l2];
							flag = true;
						}

				}
				spriteDrawX = anIntArray976[k];
				spriteDrawY = anIntArray977[k] = l1;
				String s = aStringArray983[k];
				if (anInt1249 == 0) {
					int i3 = 0xffff00;
					if (anIntArray980[k] < 6)
						i3 = anIntArray965[anIntArray980[k]];
					if (anIntArray980[k] == 6)
						i3 = anInt1265 % 20 >= 10 ? 0xffff00 : 0xff0000;
					if (anIntArray980[k] == 7)
						i3 = anInt1265 % 20 >= 10 ? 65535 : 255;
					if (anIntArray980[k] == 8)
						i3 = anInt1265 % 20 >= 10 ? 0x80ff80 : 45056;
					if (anIntArray980[k] == 9) {
						int j3 = 150 - anIntArray982[k];
						if (j3 < 50)
							i3 = 0xff0000 + 1280 * j3;
						else if (j3 < 100)
							i3 = 0xffff00 - 0x50000 * (j3 - 50);
						else if (j3 < 150)
							i3 = 65280 + 5 * (j3 - 100);
					}
					if (anIntArray980[k] == 10) {
						int k3 = 150 - anIntArray982[k];
						if (k3 < 50)
							i3 = 0xff0000 + 5 * k3;
						else if (k3 < 100)
							i3 = 0xff00ff - 0x50000 * (k3 - 50);
						else if (k3 < 150)
							i3 = (255 + 0x50000 * (k3 - 100)) - 5 * (k3 - 100);
					}
					if (anIntArray980[k] == 11) {
						int l3 = 150 - anIntArray982[k];
						if (l3 < 50)
							i3 = 0xffffff - 0x50005 * l3;
						else if (l3 < 100)
							i3 = 65280 + 0x50005 * (l3 - 50);
						else if (l3 < 150)
							i3 = 0xffffff - 0x50000 * (l3 - 100);
					}
					if (anIntArray981[k] == 0) {
						boldFont.drawText(0, s, spriteDrawY + 1, spriteDrawX);
						boldFont.drawText(i3, s, spriteDrawY, spriteDrawX);
					}
					if (anIntArray981[k] == 1) {
						boldFont.method386(0, s, spriteDrawX, anInt1265,
								spriteDrawY + 1);
						boldFont.method386(i3, s, spriteDrawX, anInt1265,
								spriteDrawY);
					}
					if (anIntArray981[k] == 2) {
						boldFont.method387(spriteDrawX, s, anInt1265,
								spriteDrawY + 1, 0);
						boldFont.method387(spriteDrawX, s, anInt1265,
								spriteDrawY, i3);
					}
					if (anIntArray981[k] == 3) {
						boldFont.method388(150 - anIntArray982[k], s,
								anInt1265, spriteDrawY + 1, spriteDrawX, 0);
						boldFont.method388(150 - anIntArray982[k], s,
								anInt1265, spriteDrawY, spriteDrawX, i3);
					}
					if (anIntArray981[k] == 4) {
						int i4 = boldFont.method384(s);
						int k4 = ((150 - anIntArray982[k]) * (i4 + 100)) / 150;
						DrawingArea.setDrawingArea(spriteDrawX - 50, 0,
								spriteDrawX + 50, 334);
						boldFont.method385(0, s, spriteDrawY + 1,
								(spriteDrawX + 50) - k4);
						boldFont.method385(i3, s, spriteDrawY,
								(spriteDrawX + 50) - k4);
						DrawingArea.defaultDrawingAreaSize();
					}
					if (anIntArray981[k] == 5) {
						int j4 = 150 - anIntArray982[k];
						int l4 = 0;
						if (j4 < 25)
							l4 = j4 - 25;
						else if (j4 > 125)
							l4 = j4 - 125;
						DrawingArea.setDrawingArea(0, spriteDrawY
								- boldFont.anInt1497 - 1, 512, spriteDrawY + 5);
						boldFont.drawText(0, s, spriteDrawY + 1 + l4,
								spriteDrawX);
						boldFont.drawText(i3, s, spriteDrawY + l4, spriteDrawX);
						DrawingArea.defaultDrawingAreaSize();
					}
				} else {
					boldFont.drawText(0, s, spriteDrawY + 1, spriteDrawX);
					boldFont.drawText(0xffff00, s, spriteDrawY, spriteDrawX);
				}
			}
		} catch (Exception e) {
		}
	}

	public void delFriend(long l) {
		try {
			if (l == 0L)
				return;
			for (int i = 0; i < friendsCount; i++) {
				if (friendsListNames[i] != l)
					continue;
				friendsCount--;
				needDrawTabArea = true;
				for (int j = i; j < friendsCount; j++) {
					friendsList[j] = friendsList[j + 1];
					friendsNodeIDs[j] = friendsNodeIDs[j + 1];
					friendsListNames[j] = friendsListNames[j + 1];
				}
				stream.createFrame(215);
				stream.writeLong(l);
				break;
			}
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("18622, " + false + ", " + l + ", "
					+ runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public void drawTabHover(boolean fixed) {
		if (fixed) {
			if (tabHover != -1) {
				if (tabInterfaceIDs[tabHover] != -1) {
					int[] positionX = { 0, 30, 60, 90, 120, 150, 180, 210, 0, 30, 60, 90, 120, 150, 180, 210 };
					int[] positionY = { 0, 0, 0, 0, 0, 0, 0, 0, 298, 298, 298, 298, 298, 298, 298, 298, 298 };
					cacheSprite[16].drawSprite(3 + positionX[tabHover], positionY[tabHover]);
				}
			}
		} else {
			if (tabHover != -1) {
				int[] tab = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
						14, 15 };
				int[] positionX = { 0, 30, 60, 90, 120, 150, 180, 210, 0, 30,
						60, 90, 120, 150, 180, 210 };
				int offsetX = 0;
				for (int index = 0; index < tab.length; index++) {
					if (tabInterfaceIDs[tab[index]] != -1) {
						if (tabHover == tab[index]) {
							offsetX = index > 7 && clientWidth >= 900 ? 240 : 0;
							cacheSprite[16].drawARGBSprite(
									(clientWidth - (clientWidth >= 900 ? 480
											: 240))
											+ positionX[index]
											+ offsetX, clientHeight
											- (clientWidth >= 900 ? 37
													: (index < 8 ? 74 : 37)));
						}
					}
				}
			}
		}
	}

	public void drawLightTab(boolean fixed) {
		if (fixed) {
			int xPos = 2;
			int yPos = 0;
			if (tabId < tabInterfaceIDs.length && tabInterfaceIDs[tabId] != -1) {
				switch (tabId) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
					xPos += tabId * 30 - 4;
					yPos = 0;
					break;	
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
					xPos += (tabId * 30) - 244;
					yPos = 299;
					break;
				}
				if (tabId != 16)
					cacheSprite[17].drawARGBSprite(xPos, yPos);
			}
		} else {
			int[] tab = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
			int[] positionX = { 0, 30, 60, 90, 120, 150, 180, 210, 0, 30, 60,
					90, 120, 150, 180, 210 };
			for (int index = 0; index < tab.length; index++) {
				int offsetX = clientWidth >= 900 ? 481 : 241;
				if (offsetX == 481 && index > 7) {
					offsetX -= 240;
				}
				int offsetY = clientWidth >= 900 ? 37 : (index > 7 ? 37 : 74);
				if (tabId == tab[index] && tabInterfaceIDs[tab[index]] != -1 && tabId != 16) {
					cacheSprite[17].drawARGBSprite((clientWidth - offsetX - 4)
							+ positionX[index], (clientHeight - offsetY) + 0);
				}
			}
		}
	}

	public void drawSideIcons(boolean fixed) {
		if (fixed) {
			int[] id = { 
					20, 89, 21, 22, 23, 24, 25, 26,
					95, 149, 150, 27, 31, 32, 33, 90 
			};
			int[] tab = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
			int[] positionX = {
					8, 37, 67, 97, 127, 159, 187, 217, 
					7, 38, 67, 97, 127, 157, 187, 217 
			};
			int[] positionY = { 9, 9, 8, 8, 8, 8, 8, 8, 307, 306, 306, 307,
					306, 306, 306, 308 };
			for (int index = 0; index < tab.length; index++) {
				if (tabInterfaceIDs[tab[index]] != -1) {
					if (id[index] != -1) {
						cacheSprite[id[index]].drawSprite(positionX[index], positionY[index]);
					}
				}
			}
		} else {
			int[] id = { 
					20, 89, 21, 22, 23, 24, 25, 26,
					95, 149, 150, 27, 31, 32, 33, 90 
			};
			int[] tab = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
			int[] positionX = {
					8, 37, 67, 97, 127, 159, 187, 217,
					7, 38, 67, 97, 127, 157, 187, 217 
			};
			int[] positionY = { 9, 9, 8, 8, 8, 8, 8, 8, /* second row */8, 8,
					8, 9, 8, 8, 8, 9 };
			for (int index = 0; index < tab.length; index++) {
				int offsetX = clientWidth >= 900 ? 482 : 242;
				if (offsetX == 482 && index > 7) {
					offsetX -= 240;
				}
				int offsetY = clientWidth >= 900 ? 37 : (index > 7 ? 37 : 74);
				if (tabInterfaceIDs[tab[index]] != -1) {
					if (id[index] != -1) {
						cacheSprite[id[index]].drawSprite(
								(clientWidth - offsetX - 1) + positionX[index],
								(clientHeight - offsetY) + positionY[index]);
					}
				}
			}
		}
	}

	public void handleTabArea(boolean fixed) {
		if (fixed) {
			cacheSprite[13].drawSprite(0, 0);
		} else {
			if (clientWidth >= 900) {
				for (int positionX = clientWidth - 480, positionY = clientHeight - 37, index = 0; positionX <= clientWidth - 30
						&& index < 16; positionX += 30, index++) {
					cacheSprite[15].drawSprite(positionX, positionY);
				}
				if (showTab) {
					cacheSprite[18].drawSprite(clientWidth - 197,
							clientHeight - 37 - 267, 150);
					cacheSprite[19].drawSprite(clientWidth - 204,
							clientHeight - 37 - 274);
				}
			} else {
				for (int positionX = clientWidth - 240, positionY = clientHeight - 74, index = 0; positionX <= clientWidth - 30
						&& index < 8; positionX += 30, index++) {
					cacheSprite[15].drawSprite(positionX, positionY);
				}
				for (int positionX = clientWidth - 240, positionY = clientHeight - 37, index = 0; positionX <= clientWidth - 30
						&& index < 8; positionX += 30, index++) {
					cacheSprite[15].drawSprite(positionX, positionY);
				}
				if (showTab) {
					cacheSprite[18].drawSprite(clientWidth - 197,
							clientHeight - 74 - 267, 150);
					cacheSprite[19].drawSprite(clientWidth - 204,
							clientHeight - 74 - 274);
				}
			}
		}
		if (invOverlayInterfaceID == -1) {
			drawTabHover(fixed);
			if (showTab) {
				drawLightTab(fixed);
			}
			drawSideIcons(fixed);
		}
	}

	public void drawTabArea() {
		if (isFixed()) {
			tabAreaIP.initDrawingArea();
		}
		Rasterizer.lineOffsets = anIntArray1181;
		handleTabArea(isFixed());
		int y = clientWidth >= 900 ? 37 : 74;
		if (showTab) {
			if (invOverlayInterfaceID != -1) {
				drawInterface((clientSize == 0 ? 28 : clientWidth - 197),
						(clientSize == 0 ? 37 : clientHeight - y - 267), 0,
						RSInterface.interfaceCache[invOverlayInterfaceID]);
			} else if (tabInterfaceIDs[tabId] != -1) {
				drawInterface((clientSize == 0 ? 28 : clientWidth - 197),
						(clientSize == 0 ? 37 : clientHeight - y - 267), 0,
						RSInterface.interfaceCache[tabInterfaceIDs[tabId]]);
			}
			if (menuOpen && menuScreenArea == 1) {
				drawMenu();
			}
		}
		if (clientSize == 0)
			tabAreaIP.drawGraphics(519, 168, super.graphics);
		gameScreenIP.initDrawingArea();
		Rasterizer.lineOffsets = anIntArray1182;
	}

	public void method37(int j) {
		/*
		 * if (!lowMemory) { if (Rasterizer.anIntArray1480[17] >= j) {
		 * IndexedImage background = Rasterizer.aBackgroundArray1474s[17]; int k =
		 * background.anInt1452 * background.anInt1453 - 1; // fire cape
		 * apparently? int j1 = background.anInt1452 * anInt945 * 2; byte
		 * abyte0[] = background.aByteArray1450; byte abyte3[] = aByteArray912;
		 * for (int i2 = 0; i2 <= k; i2++) abyte3[i2] = abyte0[i2 - j1 & k];
		 * 
		 * background.aByteArray1450 = abyte3; aByteArray912 = abyte0;
		 * Rasterizer.method370(17); anInt854++; if (anInt854 > 1235) { anInt854 =
		 * 0; stream.createFrame(226); stream.writeByte(0); int l2 =
		 * stream.currentOffset; stream.writeWord(58722); stream.writeByte(240);
		 * stream.writeWord((int) (Math.random() * 65536D));
		 * stream.writeByte((int) (Math.random() * 256D)); if ((int)
		 * (Math.random() * 2D) == 0) stream.writeWord(51825);
		 * stream.writeByte((int) (Math.random() * 256D));
		 * stream.writeWord((int) (Math.random() * 65536D));
		 * stream.writeWord(7130); stream.writeWord((int) (Math.random() *
		 * 65536D)); stream.writeWord(61657);
		 * stream.writeBytes(stream.currentOffset - l2); } } if
		 * (Rasterizer.anIntArray1480[24] >= j) { IndexedImage background_1 =
		 * Rasterizer.aBackgroundArray1474s[24]; int l = background_1.anInt1452 *
		 * background_1.anInt1453 - 1; int k1 = background_1.anInt1452 *
		 * anInt945 * 2; byte abyte1[] = background_1.aByteArray1450; byte
		 * abyte4[] = aByteArray912; for (int j2 = 0; j2 <= l; j2++) abyte4[j2]
		 * = abyte1[j2 - k1 & l];
		 * 
		 * background_1.aByteArray1450 = abyte4; aByteArray912 = abyte1;
		 * Rasterizer.method370(24); } if (Rasterizer.anIntArray1480[34] >= j) {
		 * IndexedImage background_2 = Rasterizer.aBackgroundArray1474s[34]; int
		 * i1 = background_2.anInt1452 * background_2.anInt1453 - 1; int l1 =
		 * background_2.anInt1452 * anInt945 * 2; byte abyte2[] =
		 * background_2.aByteArray1450; byte abyte5[] = aByteArray912; for (int
		 * k2 = 0; k2 <= i1; k2++) abyte5[k2] = abyte2[k2 - l1 & i1];
		 * 
		 * background_2.aByteArray1450 = abyte5; aByteArray912 = abyte2;
		 * Rasterizer.method370(34); } if (Rasterizer.anIntArray1480[40] >= j) {
		 * IndexedImage background_2 = Rasterizer.aBackgroundArray1474s[40]; int
		 * i1 = background_2.anInt1452 * background_2.anInt1453 - 1; int l1 =
		 * background_2.anInt1452 * anInt945 * 2; byte abyte2[] =
		 * background_2.aByteArray1450; byte abyte5[] = aByteArray912; for (int
		 * k2 = 0; k2 <= i1; k2++) abyte5[k2] = abyte2[k2 - l1 & i1];
		 * background_2.aByteArray1450 = abyte5; aByteArray912 = abyte2;
		 * Rasterizer.method370(40);
		 * 
		 * } }
		 */
	}

	public void method38() {
		for (int i = -1; i < playerCount; i++) {
			int j;
			if (i == -1)
				j = myPlayerIndex;
			else
				j = playerIndices[i];
			Player player = playerArray[j];
			if (player != null && player.textCycle > 0) {
				player.textCycle--;
				if (player.textCycle == 0)
					player.textSpoken = null;
			}
		}
		for (int k = 0; k < npcCount; k++) {
			int l = npcIndices[k];
			NPC npc = npcArray[l];
			if (npc != null && npc.textCycle > 0) {
				npc.textCycle--;
				if (npc.textCycle == 0)
					npc.textSpoken = null;
			}
		}
	}

	public void calcCameraPos() {
		int i = anInt1098 * 128 + 64;
		int j = anInt1099 * 128 + 64;
		int k = getHeight(plane, j, i) - anInt1100;
		if (xCameraPos < i) {
			xCameraPos += anInt1101 + ((i - xCameraPos) * anInt1102) / 1000;
			if (xCameraPos > i)
				xCameraPos = i;
		}
		if (xCameraPos > i) {
			xCameraPos -= anInt1101 + ((xCameraPos - i) * anInt1102) / 1000;
			if (xCameraPos < i)
				xCameraPos = i;
		}
		if (zCameraPos < k) {
			zCameraPos += anInt1101 + ((k - zCameraPos) * anInt1102) / 1000;
			if (zCameraPos > k)
				zCameraPos = k;
		}
		if (zCameraPos > k) {
			zCameraPos -= anInt1101 + ((zCameraPos - k) * anInt1102) / 1000;
			if (zCameraPos < k)
				zCameraPos = k;
		}
		if (yCameraPos < j) {
			yCameraPos += anInt1101 + ((j - yCameraPos) * anInt1102) / 1000;
			if (yCameraPos > j)
				yCameraPos = j;
		}
		if (yCameraPos > j) {
			yCameraPos -= anInt1101 + ((yCameraPos - j) * anInt1102) / 1000;
			if (yCameraPos < j)
				yCameraPos = j;
		}
		i = cameraPositionX * 128 + 64;
		j = cameraPositionY * 128 + 64;
		k = getHeight(plane, j, i) - cameraPositionZ;
		int l = i - xCameraPos;
		int i1 = k - zCameraPos;
		int j1 = j - yCameraPos;
		int k1 = (int) Math.sqrt(l * l + j1 * j1);
		int l1 = (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
		int i2 = (int) (Math.atan2(l, j1) * -325.94900000000001D) & 0x7ff;
		if (l1 < 128)
			l1 = 128;
		if (l1 > 383)
			l1 = 383;
		if (yCameraCurve < l1) {
			yCameraCurve += cameraMovementSpeed + ((l1 - yCameraCurve) * cameraAngle) / 1000;
			if (yCameraCurve > l1)
				yCameraCurve = l1;
		}
		if (yCameraCurve > l1) {
			yCameraCurve -= cameraMovementSpeed + ((yCameraCurve - l1) * cameraAngle) / 1000;
			if (yCameraCurve < l1)
				yCameraCurve = l1;
		}
		int j2 = i2 - xCameraCurve;
		if (j2 > 1024)
			j2 -= 2048;
		if (j2 < -1024)
			j2 += 2048;
		if (j2 > 0) {
			xCameraCurve += cameraMovementSpeed + (j2 * cameraAngle) / 1000;
			xCameraCurve &= 0x7ff;
		}
		if (j2 < 0) {
			xCameraCurve -= cameraMovementSpeed + (-j2 * cameraAngle) / 1000;
			xCameraCurve &= 0x7ff;
		}
		int k2 = i2 - xCameraCurve;
		if (k2 > 1024)
			k2 -= 2048;
		if (k2 < -1024)
			k2 += 2048;
		if (k2 < 0 && j2 > 0 || k2 > 0 && j2 < 0)
			xCameraCurve = i2;
	}

	public int contextWidth;
	public int contextHeight;

	public void drawMenu() {
		//super.setCursor(0);
		int x = menuOffsetX;
		int j = menuOffsetY;
		if (menuAnimations) {
			if (contextWidth < menuWidth) {
				contextWidth += 20;
			} else if (contextWidth > menuWidth) {
				contextWidth -= 1;
			}
			if (contextHeight < menuHeight) {
				contextHeight += 20;
				;
			} else if (contextHeight > menuHeight) {
				contextHeight -= 1;
			}
		} else {
			contextWidth = menuWidth;
			contextHeight = menuHeight;
		}
		int i1 = 0x5d5447;
		DrawingArea.method338(j + 2, contextHeight - 4, 250, 0x706a5e, contextWidth, x);
		DrawingArea.method338(j + 1, contextHeight - 2, 250, 0x706a5e, contextWidth - 2, x + 1);
		DrawingArea.method338(j, contextHeight, 200, 0x706a5e, contextWidth - 4, x + 2);
		DrawingArea.method338(j + 1, contextHeight - 2, 250, 0x2d2822, contextWidth - 6, x + 3);
		DrawingArea.method338(j + 2, contextHeight - 4, 250, 0x2d2822, contextWidth - 4, x + 2);
		DrawingArea.method338(j + 3, contextHeight - 6, 250, 0x2d2822, contextWidth - 2, x + 1);
		DrawingArea.method338(j + 19, contextHeight - 22, 250, 0x524a3d, contextWidth - 4, x + 2);
		DrawingArea.method338(j + 20, contextHeight - 22, 250, 0x524a3d, contextWidth - 6, x + 3);
		DrawingArea.method335(0x112329, j + 20, contextWidth - 6, contextHeight - 23, 180, x + 3); 
		DrawingArea.fillPixels(x + 3, contextWidth - 6, 1, 0x2a291b, j + 2);
		DrawingArea.fillPixels(x + 2, contextWidth - 4, 1, 0x2a261b, j + 3);
		DrawingArea.fillPixels(x + 2, contextWidth - 4, 1, 0x252116, j + 4);
		DrawingArea.fillPixels(x + 2, contextWidth - 4, 1, 0x211e15, j + 5);
		DrawingArea.fillPixels(x + 2, contextWidth - 4, 1, 0x1e1b12, j + 6);
		DrawingArea.fillPixels(x + 2, contextWidth - 4, 1, 0x1a170e, j + 7);
		DrawingArea.fillPixels(x + 2, contextWidth - 4, 2, 0x15120b, j + 8);
		DrawingArea.fillPixels(x + 2, contextWidth - 4, 1, 0x100d08, j + 10);
		DrawingArea.fillPixels(x + 2, contextWidth - 4, 1, 0x090a04, j + 11);
		DrawingArea.fillPixels(x + 2, contextWidth - 4, 1, 0x080703, j + 12);
		DrawingArea.fillPixels(x + 2, contextWidth - 4, 1, 0x090a04, j + 13);
		DrawingArea.fillPixels(x + 2, contextWidth - 4, 1, 0x070802, j + 14);
		DrawingArea.fillPixels(x + 2, contextWidth - 4, 1, 0x090a04, j + 15);
		DrawingArea.fillPixels(x + 2, contextWidth - 4, 1, 0x070802, j + 16);
		DrawingArea.fillPixels(x + 2, contextWidth - 4, 1, 0x090a04, j + 17);
		DrawingArea.fillPixels(x + 2, contextWidth - 4, 1, 0x2a291b, j + 18);
		DrawingArea.fillPixels(x + 3, contextWidth - 6, 1, 0x564943, j + 19);
		boldFont.method385(i1, "Choose Option", j + 14, x + 3);
		boldFont.method385(0xc6b895, "Choose Option", j + 14, x + 3);	
		int j1 = super.mouseX;
		int k1 = super.mouseY;
		if (menuScreenArea == 0) {
			j1 -= clientSize == 0 ? 4 : 0;
			k1 -= clientSize == 0 ? 4 : 0;
		}
		if (menuScreenArea == 1) {
			j1 -= 519;
			k1 -= 168;
		}
		if (menuScreenArea == 2) {
			j1 -= 17;
			k1 -= 338;
		}
		if (menuScreenArea == 3) {
			j1 -= 519;
			k1 -= 0;
		}
		if (contextWidth == menuWidth && contextHeight == menuHeight) {
			for (int index = 0; index < menuActionRow; index++) {
				int y = j + 31 + (menuActionRow - 1 - index) * 15;
				int color;
				color = 0xc6b895;
				if (j1 > x && j1 < x + contextWidth && k1 > y - 13
						&& k1 < y + 3) {
					DrawingArea.drawPixels(15, y - 11, x + 3, 0x6f695d,
							menuWidth - 6);
					color = 0xeee5c6;
					setMouseCursor(menuActionName[index]);
				}
				boldFont.method389(true, x + 4, color, menuActionName[index], y + 1);
			}
		}
	}

	public void addFriend(long l) {
		try {
			if (l == 0L)
				return;
			if (friendsCount >= 100 && memberStatus != 1) {
				pushMessage(
						"",
						"Your friendlist is full. Max of 100 for free users, and 200 for members.", 0);
				return;
			}
			if (friendsCount >= 200) {
				pushMessage(
						"",
						"Your friendlist is full. Max of 100 for free users, and 200 for members.", 0);
				return;
			}
			String s = TextClass.fixName(TextClass.nameForLong(l));
			for (int i = 0; i < friendsCount; i++)
				if (friendsListNames[i] == l) {
					pushMessage("", s + " is already on your friend list!", 0);
					return;
				}
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListNames[j] == l) {
					pushMessage("", "Please remove " + s
									+ " from your ignore list first.", 0);
					return;
				}

			if (s.equals(myPlayer.name)) {
				return;
			} else {
				friendsList[friendsCount] = s;
				friendsListNames[friendsCount] = l;
				friendsNodeIDs[friendsCount] = 0;
				friendsCount++;
				needDrawTabArea = true;
				stream.createFrame(188);
				stream.writeLong(l);
				return;
			}
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("15283, " + (byte) 68 + ", " + l + ", "
					+ runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	public int getHeight(int height, int x, int y) {
		int l = y >> 7;
		int i1 = x >> 7;
		if (l < 0 || i1 < 0 || l > 103 || i1 > 103)
			return 0;
		int j1 = height;
		if (j1 < 3 && (byteGroundArray[1][l][i1] & 2) == 2)
			j1++;
		int k1 = y & 0x7f;
		int l1 = x & 0x7f;
		int i2 = intGroundArray[j1][l][i1] * (128 - k1)
				+ intGroundArray[j1][l + 1][i1] * k1 >> 7;
		int j2 = intGroundArray[j1][l][i1 + 1] * (128 - k1)
				+ intGroundArray[j1][l + 1][i1 + 1] * k1 >> 7;
		return i2 * (128 - l1) + j2 * l1 >> 7;
	}

	public static String intToKOrMil(int j) {
		if (j < 0x186a0)
			return String.valueOf(j);
		if (j < 0x989680)
			return j / 1000 + "K";
		else
			return j / 0xf4240 + "M";
	}

	public void resetLogout() {
		try {
			if (socketStream != null)
				socketStream.close();
		} catch (Exception _ex) {
		}
		alertHandler.alert = null;
		socketStream = null;
		loggedIn = false;
		loginScreenState = LOGIN;
		loginMessage1 = "";
		loginMessage2 = "";
		writeSettings();
		unlinkMRUNodes();
		landscapeScene.initToNull();
		for (int i = 0; i < 4; i++)
			clippingPlanes[i].reset();
		System.gc();
		stopMidi();
		currentSong = -1;
		nextSong = -1;
		previousSong = 0;
		notes.notes = new String[30];
		// modMenu.setEnabled(false);
		// adminMenu.setEnabled(false);
	}

	public void method45() {

		aBoolean1031 = true;
		for (int j = 0; j < 7; j++) {
			anIntArray1065[j] = -1;
			for (int k = 0; k < IdentityKit.cache.length; k++) {
				if (IdentityKit.cache[k].aBoolean662
						|| IdentityKit.cache[k].anInt657 != j
								+ (aBoolean1047 ? 0 : 7))
					continue;
				anIntArray1065[j] = k;
				break;
			}
		}
	}

	public void addNewNpc(int i, JagexBuffer stream) {
		while (stream.bitPosition + 21 < i * 8) {
			int index = stream.readBits(14);
			if (index == 16383)
				break;
			if (npcArray[index] == null)
				npcArray[index] = new NPC();
			NPC npc = npcArray[index];
			npcIndices[npcCount++] = index;
			npc.anInt1537 = loopCycle;
			int regionY = stream.readBits(5);
			if (regionY > 15)
				regionY -= 32;
			int regionX = stream.readBits(5);
			if (regionX > 15)
				regionX -= 32;
			int j1 = stream.readBits(1);
			int id = stream.readBits(16);
			npc.id_2 = id;
			int updateRequired = stream.readBits(1);
			if (updateRequired == 1)
				anIntArray894[anInt893++] = index;
			NPCDefinition def = NPCDefinition.forId(id);
			npc.size = def.size;
			npc.turnSpeed = def.turnSpeed;
			npc.walkAnimation = def.walkAnimation;
			npc.reverseAnimation = def.reverseAnimation;
			npc.turnLeftAnimation = def.turnLeftAnimation;
			npc.turnRightAnimation = def.turnRightAnimation;
			npc.standAnimation = def.standAnimation;
			npc.runAnimation = def.runAnimation;
			npc.setPosition(myPlayer.smallX[0] + regionX, myPlayer.smallY[0] + regionY, j1 == 1);
		}
		stream.finishBitAccess();
	}

	public void processGameLoop() {
		if (rsAlreadyLoaded || loadingError || genericLoadingError)
			return;
		loopCycle++;
		if (!loggedIn)
			processLoginScreenInput();
		else
			mainGameProcessor();
		processOnDemandQueue();
	}

	public void method47(boolean flag) {
		if (myPlayer.x >> 7 == destX && myPlayer.y >> 7 == destY)
			destX = 0;
		int j = playerCount;
		if (flag)
			j = 1;
		for (int l = 0; l < j; l++) {
			Player player;
			int i1;
			if (flag) {
				player = myPlayer;
				i1 = myPlayerIndex << 14;
			} else {
				player = playerArray[playerIndices[l]];
				i1 = playerIndices[l] << 14;
			}
			if (player == null || !player.isVisible())
				continue;
			player.aBoolean1699 = (lowMemory && playerCount > 50 || playerCount > 200)
					&& !flag && player.anInt1517 == player.standAnimation;
			int j1 = player.x >> 7;
			int k1 = player.y >> 7;
			if (j1 < 0 || j1 >= 104 || k1 < 0 || k1 >= 104)
				continue;
			if (player.objectTransformatiomModel != null && loopCycle >= player.secondaryModel
					&& loopCycle < player.primaryModel) {
				player.aBoolean1699 = false;
				player.anInt1709 = getHeight(plane, player.y, player.x);
				landscapeScene.method286(plane, player.y, player,
						player.anInt1552, player.anInt1722, player.x,
						player.anInt1709, player.anInt1719, player.anInt1721,
						i1, player.anInt1720);
				continue;
			}
			if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
				if (anIntArrayArray929[j1][k1] == anInt1265)
					continue;
				anIntArrayArray929[j1][k1] = anInt1265;
			}
			player.anInt1709 = getHeight(plane, player.y, player.x);
			landscapeScene.method285(plane, player.anInt1552,
					player.anInt1709, i1, player.y, 60, player.x, player,
					player.aBoolean1541);
		}
	}

	public boolean promptUserForInput(RSInterface rsi) {
		int contentType = rsi.contentType;
		if (friendsListServerStatus == 2) {
			if (contentType == 201) {
				inputTaken = true;
				inputDialogState = 0;
				showInput = true;
				promptInput = "";
				friendsListAction = 1;
				promptMessage = "Enter name of friend to add to list";
			}
			if (contentType == 202) {
				inputTaken = true;
				inputDialogState = 0;
				showInput = true;
				promptInput = "";
				friendsListAction = 2;
				promptMessage = "Enter name of friend to delete from list";
			}
		}
		if (contentType == 205) {
			anInt1011 = 250;
			return true;
		}
		if (contentType == 501) {
			inputTaken = true;
			inputDialogState = 0;
			showInput = true;
			promptInput = "";
			friendsListAction = 4;
			promptMessage = "Enter name of player to add to list";
		}
		if (contentType == 502) {
			inputTaken = true;
			inputDialogState = 0;
			showInput = true;
			promptInput = "";
			friendsListAction = 5;
			promptMessage = "Enter name of player to delete from list";
		}
		
		if (contentType == 550) { 
			inputTaken = true;
			inputDialogState = 0;
			showInput = true;
			promptInput = "";
			friendsListAction = 6;
			promptMessage = "Enter the name of the chat you wish to join";
		}
		if (contentType >= 300 && contentType <= 313) {
			int k = (contentType - 300) / 2;
			int j1 = contentType & 1;
			int i2 = anIntArray1065[k];
			if (i2 != -1) {
				do {
					if (j1 == 0 && --i2 < 0)
						i2 = IdentityKit.length - 1;
					if (j1 == 1 && ++i2 >= IdentityKit.length)
						i2 = 0;
				} while (IdentityKit.cache[i2].aBoolean662
						|| IdentityKit.cache[i2].anInt657 != k
								+ (aBoolean1047 ? 0 : 7));
				anIntArray1065[k] = i2;
				aBoolean1031 = true;
			}
		}
		if (contentType >= 314 && contentType <= 323) {
			int l = (contentType - 314) / 2;
			int k1 = contentType & 1;
			int j2 = anIntArray990[l];
			if (k1 == 0 && --j2 < 0)
				j2 = VALID_CLOTHE_COLOUR[l].length - 1;
			if (k1 == 1 && ++j2 >= VALID_CLOTHE_COLOUR[l].length)
				j2 = 0;
			anIntArray990[l] = j2;
			aBoolean1031 = true;
		}
		if (contentType == 324 && !aBoolean1047) {
			aBoolean1047 = true;
			method45();
		}
		if (contentType == 325 && aBoolean1047) {
			aBoolean1047 = false;
			method45();
		}
		if (contentType == 326) {
			stream.createFrame(101);
			stream.writeByte(aBoolean1047 ? 0 : 1);
			for (int i1 = 0; i1 < 7; i1++)
				stream.writeByte(anIntArray1065[i1]);

			for (int l1 = 0; l1 < 5; l1++)
				stream.writeByte(anIntArray990[l1]);

			return true;
		}
		if (contentType == 613)
			canMute = !canMute;
		if (contentType >= 601 && contentType <= 612) {
			clearTopInterfaces();
			if (reportAbuseInput.length() > 0) {
				stream.createFrame(218);
				stream.writeLong(TextClass.longForName(reportAbuseInput));
				stream.writeByte(contentType - 601);
				stream.writeByte(canMute ? 1 : 0);
			}
		}
		return false;
	}

	public void method49(JagexBuffer stream) {
		for (int j = 0; j < anInt893; j++) {
			int k = anIntArray894[j];
			Player player = playerArray[k];
			int mask = stream.readUnsignedByte();
			if ((mask & 0x40) != 0)
				mask += stream.readUnsignedByte() << 8;
			updatePlayerMasks(mask, k, stream, player);
		}
	}

	public void method50(int i, int k, int l, int i1, int j1) {
		int k1 = landscapeScene.method300(j1, l, i);
		if (k1 != 0) {
			int l1 = landscapeScene.getObject(j1, l, i, k1);
			int k2 = l1 >> 6 & 3;
			int i3 = l1 & 0x1f;
			int k3 = k;
			if (k1 > 0)
				k3 = i1;
			int ai[] = miniMap.myPixels;
			int k4 = 24624 + l * 4 + (103 - i) * 512 * 4;
			int i5 = k1 >> 14 & 0x7fff;
			ObjectDefinition def = ObjectDefinition.forId(i5);
			if (def.mapSceneId != -1) {
				Sprite mapScene = mapScenes[def.mapSceneId];
				if (mapScene != null) {
					int i6 = (def.sizeX * 4 - mapScene.myWidth) / 2;
					int j6 = (def.sizeY * 4 - mapScene.myHeight) / 2;
					mapScene.drawSprite(48 + l * 4 + i6, 48 + (104 - i - def.sizeY) * 4 + j6);
				}
			} else {
				if (i3 == 0 || i3 == 2)
					if (k2 == 0) {
						ai[k4] = k3;
						ai[k4 + 512] = k3;
						ai[k4 + 1024] = k3;
						ai[k4 + 1536] = k3;
					} else if (k2 == 1) {
						ai[k4] = k3;
						ai[k4 + 1] = k3;
						ai[k4 + 2] = k3;
						ai[k4 + 3] = k3;
					} else if (k2 == 2) {
						ai[k4 + 3] = k3;
						ai[k4 + 3 + 512] = k3;
						ai[k4 + 3 + 1024] = k3;
						ai[k4 + 3 + 1536] = k3;
					} else if (k2 == 3) {
						ai[k4 + 1536] = k3;
						ai[k4 + 1536 + 1] = k3;
						ai[k4 + 1536 + 2] = k3;
						ai[k4 + 1536 + 3] = k3;
					}
				if (i3 == 3)
					if (k2 == 0)
						ai[k4] = k3;
					else if (k2 == 1)
						ai[k4 + 3] = k3;
					else if (k2 == 2)
						ai[k4 + 3 + 1536] = k3;
					else if (k2 == 3)
						ai[k4 + 1536] = k3;
				if (i3 == 2)
					if (k2 == 3) {
						ai[k4] = k3;
						ai[k4 + 512] = k3;
						ai[k4 + 1024] = k3;
						ai[k4 + 1536] = k3;
					} else if (k2 == 0) {
						ai[k4] = k3;
						ai[k4 + 1] = k3;
						ai[k4 + 2] = k3;
						ai[k4 + 3] = k3;
					} else if (k2 == 1) {
						ai[k4 + 3] = k3;
						ai[k4 + 3 + 512] = k3;
						ai[k4 + 3 + 1024] = k3;
						ai[k4 + 3 + 1536] = k3;
					} else if (k2 == 2) {
						ai[k4 + 1536] = k3;
						ai[k4 + 1536 + 1] = k3;
						ai[k4 + 1536 + 2] = k3;
						ai[k4 + 1536 + 3] = k3;
					}
			}
		}
		k1 = landscapeScene.method302(j1, l, i);
		if (k1 != 0) {
			int i2 = landscapeScene.getObject(j1, l, i, k1);
			int l2 = i2 >> 6 & 3;
			int j3 = i2 & 0x1f;
			int l3 = k1 >> 14 & 0x7fff;
			ObjectDefinition def = ObjectDefinition.forId(l3);
			if (def.mapSceneId != -1) {
				int sceneId = def.mapSceneId;
				Sprite mapScene = mapScenes[sceneId >= 0 && sceneId < 100 ? sceneId : 0];
				if (mapScene != null) {
					int j5 = (def.sizeX * 4 - mapScene.myWidth) / 2;
					int k5 = (def.sizeY * 4 - mapScene.myHeight) / 2;
					mapScene.drawSprite(48 + l * 4 + j5, 48 + (104 - i - def.sizeY) * 4 + k5);
				}
			} else if (j3 == 9) {
				int l4 = 0xeeeeee;
				if (k1 > 0)
					l4 = 0xee0000;
				int ai1[] = miniMap.myPixels;
				int l5 = 24624 + l * 4 + (103 - i) * 512 * 4;
				if (l2 == 0 || l2 == 2) {
					ai1[l5 + 1536] = l4;
					ai1[l5 + 1024 + 1] = l4;
					ai1[l5 + 512 + 2] = l4;
					ai1[l5 + 3] = l4;
				} else {
					ai1[l5] = l4;
					ai1[l5 + 512 + 1] = l4;
					ai1[l5 + 1024 + 2] = l4;
					ai1[l5 + 1536 + 3] = l4;
				}
			}
		}
		k1 = landscapeScene.method303(j1, l, i);
		if (k1 != 0) {
			int j2 = k1 >> 14 & 0x7fff;
			ObjectDefinition class46 = ObjectDefinition.forId(j2);
			if (class46.mapSceneId != -1) {
				Sprite mapScene = mapScenes[0];
				if (mapScene != null) {
					int i4 = (class46.sizeX * 4 - mapScene.myWidth) / 2;
					int j4 = (class46.sizeY * 4 - mapScene.myHeight) / 2;
					mapScene.drawSprite(48 + l * 4 + i4, 48
							+ (104 - i - class46.sizeY) * 4 + j4);
				}
			}
		}
	}
	public Sprite WorldSelect;
	public void LoginLobbyNull() {
		WorldSelect = null;
	}
	public void readLoginLobbySprites() {
		WorldSelect = new Sprite("LoginLobby" + System.getProperty("file.separator")+ "WorldSelect");
	}
	public void loadTitleScreen() {
		// title = new Sprite(titleArchive, "maintitle", 0);
		titleBox = new Sprite(titleArchive, "titlebox", 0);
		fbHover = new Sprite(titleArchive, "social", 0);
		readLoginLobbySprites();
	//	backgroundFix = new Sprite("background");

		//for (int i = 0; i < 1; i++) {
		//	socialBox[i] = new Sprite(titleArchive, "socialbox", i);
	//	}

		/**
		 * Add new Sprites here
		 */
		// for (int i = 0; i < 9; i++) {
		// optionBG = new Sprite(titleArchive, "option", 0);
		// }

		// exitButton = new Sprite(signlink.cacheLocation() +
		// "rsimg/option/exit.png");
		/*for (int i = 0; i < 1; i++) {
			titleText[i] = new Sprite(titleArchive, "titletext", i);
		}*/
		for (int i = 0; i < 4; i++) {
			optionSelect[i] = new Sprite(titleArchive, "optionselect", i);
		}

		for (int i = 0; i < 9; i++) {
			worldSelect[i] = new Sprite(titleArchive, "worldselect", i);
		}
		for (int i = 0; i < 10; i++) {
			titleButton[i] = new Sprite(titleArchive, "titlebutton", i);
		}
		for (int i = 0; i < 19; i++) {
			border[i] = new Sprite(titleArchive, "border", i);
		}
		aBackgroundArray1152s = new IndexedImage[12];
		int j = 0;
		try {
			j = Integer.parseInt(getParameter("fl_icon"));
		} catch (Exception _ex) {
		}
		if (j == 0) {
			for (int k = 0; k < 12; k++)
				aBackgroundArray1152s[k] = new IndexedImage(titleArchive,
						"runes", k);

		} else {
			for (int l = 0; l < 12; l++)
				aBackgroundArray1152s[l] = new IndexedImage(titleArchive,
						"runes", 12 + (l & 3));

		}
		aClass30_Sub2_Sub1_Sub1_1201 = new Sprite(128, 265);
		aClass30_Sub2_Sub1_Sub1_1202 = new Sprite(128, 265);
		//System.arraycopy(aRSImageProducer_1110.pixels, 0,
		//		aClass30_Sub2_Sub1_Sub1_1201.myPixels, 0, 33920);

		//System.arraycopy(aRSImageProducer_1111.pixels, 0,
		//		aClass30_Sub2_Sub1_Sub1_1202.myPixels, 0, 33920);

		anIntArray851 = new int[256];
		for (int k1 = 0; k1 < 64; k1++)
			anIntArray851[k1] = k1 * 0x40000;

		for (int l1 = 0; l1 < 64; l1++)
			anIntArray851[l1 + 64] = 0xff0000 + 1024 * l1;

		for (int i2 = 0; i2 < 64; i2++)
			anIntArray851[i2 + 128] = 0xffff00 + 4 * i2;

		for (int j2 = 0; j2 < 64; j2++)
			anIntArray851[j2 + 192] = 0xffffff;

		anIntArray852 = new int[256];
		for (int k2 = 0; k2 < 64; k2++)
			anIntArray852[k2] = k2 * 1024;

		for (int l2 = 0; l2 < 64; l2++)
			anIntArray852[l2 + 64] = 65280 + 4 * l2;

		for (int i3 = 0; i3 < 64; i3++)
			anIntArray852[i3 + 128] = 65535 + 0x40000 * i3;

		for (int j3 = 0; j3 < 64; j3++)
			anIntArray852[j3 + 192] = 0xffffff;

		anIntArray853 = new int[256];
		for (int k3 = 0; k3 < 64; k3++)
			anIntArray853[k3] = k3 * 4;

		for (int l3 = 0; l3 < 64; l3++)
			anIntArray853[l3 + 64] = 255 + 0x40000 * l3;

		for (int i4 = 0; i4 < 64; i4++)
			anIntArray853[i4 + 128] = 0xff00ff + 1024 * i4;

		for (int j4 = 0; j4 < 64; j4++)
			anIntArray853[j4 + 192] = 0xffffff;

		anIntArray850 = new int[256];
		anIntArray1190 = new int[32768];
		anIntArray1191 = new int[32768];
		randomizeBackground(null);
		anIntArray828 = new int[32768];
		anIntArray829 = new int[32768];
		if (!aBoolean831) {
			drawFlames = true;
			aBoolean831 = true;
			startRunnable(this, 2);
		}
	}

	public static void setHighMemory() {
		LandscapeScene.lowMem = false;
		Rasterizer.lowMem = true;
		lowMemory = false;
		SceneGraph.lowMemory = false;
		ObjectDefinition.lowMem = false;
	}

	public static void setLowMemory(boolean lowmem) {
		if (lowmem) {
			setLowMem();
		} else {
			setHighMemory();
		}
	}

	public static void deleteFile(File path) {
		if (path.exists()) {
			path.delete();
		}
	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public static void main(String args[]) {
		try {
			setHighMemory();
			nodeID = 10;
			portOff = 0;
			isMembers = true;
			signlink.storeid = 32;
			src.sign.signlink.startpriv(InetAddress.getByName(Constants.HOST_ADDRESS));
			instance = new Client();
			instance.createClientFrame(765, 503);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Client instance;

	public void loadingStages() {
		if (lowMemory && loadingStage == 2
				&& SceneGraph.onBuildTimePlane != plane) {
			gameScreenIP.initDrawingArea();
			drawLoadingMessage(1);
			/*normalFont.drawText(0, "Loading - please wait.", 151, 257);
			normalFont.drawText(0xffffff, "Loading - please wait.", 150, 256);*/
			gameScreenIP.drawGraphics(clientSize == 0 ? 4 : 0, clientSize == 0 ? 4 : 0,
					super.graphics);
			loadingStage = 1;
			aLong824 = System.currentTimeMillis();
		}
		if (loadingStage == 1) {
			int j = method54();
			if (j != 0 && System.currentTimeMillis() - aLong824 > 0x57e40L) {
				signlink.reporterror(myUsername + " glcfb " + aLong1215 + ","
						+ j + "," + lowMemory + "," + mainCacheFile[0] + ","
						+ resourceProvider.getRemaining() + "," + plane
						+ "," + currentRegionX + "," + currentRegionY);
				aLong824 = System.currentTimeMillis();
			}
		}
		if (loadingStage == 2 && plane != anInt985) {
			anInt985 = plane;
			method24(plane);
		}
	}
	
	private void drawLoadingMessage(int stage) {
		Sprite sprite = stage == 1 ? SpriteLoader.sprites[609] : stage == 2 ? SpriteLoader.sprites[610] : null;
		if (sprite != null) {
			sprite.drawSprite(8, 9);
		}
	}

	public int method54() {
		for (int i = 0; i < aByteArrayArray1183.length; i++) {
			if (aByteArrayArray1183[i] == null && floorMap[i] != -1) {
				//System.out.println("mapId:" + floorMap[i]);
				return -1;
			}
			if (aByteArrayArray1247[i] == null && objectMap[i] != -1) {
				//System.out.println("landscapeId:" + objectMap[i]);
				return -2;
			}
		}
		boolean flag = true;
		for (int j = 0; j < aByteArrayArray1183.length; j++) {
			byte abyte0[] = aByteArrayArray1247[j];
			if (abyte0 != null) {
				int k = (mapLocation[j] >> 8) * 64 - regionAbsBaseX;
				int l = (mapLocation[j] & 0xff) * 64 - regionAbsBaseY;
				if (aBoolean1159) {
					k = 10;
					l = 10;
				}
				flag &= SceneGraph.method189(k, abyte0, l);
			}
		}
		if (!flag) {
			return -3;// couldn't parse all landscapes
		}
		if (aBoolean1080) {
			return -4;
		} else {
			loadingStage = 2;
			SceneGraph.onBuildTimePlane = plane;
			method22();
			if (loggedIn)
				stream.createFrame(121);
			return 0;
		}
	}

	public void method55() {
		for (ProjectileModel class30_sub2_sub4_sub4 = (ProjectileModel) aClass19_1013
				.head(); class30_sub2_sub4_sub4 != null; class30_sub2_sub4_sub4 = (ProjectileModel) aClass19_1013
				.next())
			if (class30_sub2_sub4_sub4.anInt1597 != plane
					|| loopCycle > class30_sub2_sub4_sub4.anInt1572)
				class30_sub2_sub4_sub4.remove();
			else if (loopCycle >= class30_sub2_sub4_sub4.anInt1571) {
				if (class30_sub2_sub4_sub4.anInt1590 > 0) {
					NPC npc = npcArray[class30_sub2_sub4_sub4.anInt1590 - 1];
					if (npc != null && npc.x >= 0 && npc.x < 13312
							&& npc.y >= 0 && npc.y < 13312)
						class30_sub2_sub4_sub4.method455(
								loopCycle,
								npc.y,
								getHeight(class30_sub2_sub4_sub4.anInt1597,
										npc.y, npc.x)
										- class30_sub2_sub4_sub4.anInt1583,
								npc.x);
				}
				if (class30_sub2_sub4_sub4.anInt1590 < 0) {
					int j = -class30_sub2_sub4_sub4.anInt1590 - 1;
					Player player;
					if (j == playerId)
						player = myPlayer;
					else
						player = playerArray[j];
					if (player != null && player.x >= 0 && player.x < 13312
							&& player.y >= 0 && player.y < 13312)
						class30_sub2_sub4_sub4.method455(
								loopCycle,
								player.y,
								getHeight(class30_sub2_sub4_sub4.anInt1597,
										player.y, player.x)
										- class30_sub2_sub4_sub4.anInt1583,
								player.x);
				}
				class30_sub2_sub4_sub4.method456(anInt945);
				landscapeScene.method285(plane,
						class30_sub2_sub4_sub4.anInt1595,
						(int) class30_sub2_sub4_sub4.aDouble1587, -1,
						(int) class30_sub2_sub4_sub4.aDouble1586, 60,
						(int) class30_sub2_sub4_sub4.aDouble1585,
						class30_sub2_sub4_sub4, false);
			}

	}

	public AppletContext getAppletContext() {
		if (signlink.mainapp != null)
			return signlink.mainapp.getAppletContext();
		else
			return super.getAppletContext();
	}

	public void processOnDemandQueue() {
		do {
			Resource resource;
			do {
				resource = resourceProvider.getNextNode();
				if (resource == null)
					return;
				if (resource.type == resource.MODELS) {
					Model.method460(resource.data, resource.id);
					needDrawTabArea = true;
					if (backDialogID != -1)
						inputTaken = true;
				}
				if (resource.type == resource.ANIMATIONS) {
					FrameReader.load(resource.id, resource.data);
				}
				if (resource.type == resource.MIDI_SEQUENCE
						&& resource.id == nextSong
						&& resource.data != null) {
					saveMidi(songChanging, resource.data);
				}
				if (resource.type == resource.MAPS
						&& loadingStage == 1) {
					for (int i = 0; i < aByteArrayArray1183.length; i++) {
						if (floorMap[i] == resource.id) {
							aByteArrayArray1183[i] = resource.data;
							if (resource.data == null) {
								floorMap[i] = -1;
							}
							break;
						}
						if (objectMap[i] != resource.id)
							continue;
						aByteArrayArray1247[i] = resource.data;
						if (resource.data == null) {
							objectMap[i] = -1;
						}
						break;
					}
				}
				if (resource.type == resource.TEXTURES) {
					Texture.load(resource.id, resource.data);
				}
			} while (resource.type != 93
					|| !resourceProvider.method564(resource.id));
			SceneGraph.requestGameObjectModel(new JagexBuffer(resource.data),
					resourceProvider);
		} while (true);
	}

	public boolean saveWave(byte abyte0[], int i) {
		return abyte0 == null || signlink.wavesave(abyte0, i);
	}

	public void displayInterface(int i) {
		RSInterface rsi = RSInterface.interfaceCache[i];
		if (rsi != null && rsi.children != null) {
			for (int j = 0; j < rsi.children.length; j++) {
				if (rsi.children[j] == -1)
					break;
				RSInterface rsi_1 = RSInterface.interfaceCache[rsi.children[j]];
				if (rsi_1.type == 1)
					displayInterface(rsi_1.id);
				rsi_1.anInt246 = 0;
				rsi_1.anInt208 = 0;
			}
		}
	}

	public void drawHintIcon() {
		if (headIconType != 2)
			return;
		calcEntityScreenPos((anInt934 - regionAbsBaseX << 7) + anInt937, anInt936 * 2, (anInt935 - regionAbsBaseY << 7) + anInt938);
		if (spriteDrawX > -1 && loopCycle % 20 < 10) {
			headIconsHint[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28);
		}
	}

	public void mainGameProcessor() {
		checkSize();
		if (systemUpdateTimer > 1)
			systemUpdateTimer--;
		if (anInt1011 > 0)
			anInt1011--;
		for (int j = 0; j < 5; j++)
			if (!parsePacket())
				break;

		if (!loggedIn)
			return;
		synchronized (mouseDetection.syncObject) {
			if (flagged) {
				if (super.clickMode3 != 0 || mouseDetection.coordsIndex >= 40) {
					stream.createFrame(45);
					stream.writeByte(0);
					int j2 = stream.currentOffset;
					int j3 = 0;
					for (int j4 = 0; j4 < mouseDetection.coordsIndex; j4++) {
						if (j2 - stream.currentOffset >= 240)
							break;
						j3++;
						int l4 = mouseDetection.coordsY[j4];
						if (l4 < 0)
							l4 = 0;
						else if (l4 > 502)
							l4 = 502;
						int k5 = mouseDetection.coordsX[j4];
						if (k5 < 0)
							k5 = 0;
						else if (k5 > 764)
							k5 = 764;
						int i6 = l4 * 765 + k5;
						if (mouseDetection.coordsY[j4] == -1
								&& mouseDetection.coordsX[j4] == -1) {
							k5 = -1;
							l4 = -1;
							i6 = 0x7ffff;
						}
						if (k5 == anInt1237 && l4 == anInt1238) {
							if (anInt1022 < 2047)
								anInt1022++;
						} else {
							int j6 = k5 - anInt1237;
							anInt1237 = k5;
							int k6 = l4 - anInt1238;
							anInt1238 = l4;
							if (anInt1022 < 8 && j6 >= -32 && j6 <= 31
									&& k6 >= -32 && k6 <= 31) {
								j6 += 32;
								k6 += 32;
								stream.writeShort((anInt1022 << 12) + (j6 << 6)
										+ k6);
								anInt1022 = 0;
							} else if (anInt1022 < 8) {
								stream.writeTripleInt(0x800000
										+ (anInt1022 << 19) + i6);
								anInt1022 = 0;
							} else {
								stream.writeInt(0xc0000000 + (anInt1022 << 19)
										+ i6);
								anInt1022 = 0;
							}
						}
					}

					stream.writeBytes(stream.currentOffset - j2);
					if (j3 >= mouseDetection.coordsIndex) {
						mouseDetection.coordsIndex = 0;
					} else {
						mouseDetection.coordsIndex -= j3;
						for (int i5 = 0; i5 < mouseDetection.coordsIndex; i5++) {
							mouseDetection.coordsX[i5] = mouseDetection.coordsX[i5
									+ j3];
							mouseDetection.coordsY[i5] = mouseDetection.coordsY[i5
									+ j3];
						}

					}
				}
			} else {
				mouseDetection.coordsIndex = 0;
			}
		}
		if (super.clickMode3 != 0) {
			long l = (super.aLong29 - aLong1220) / 50L;
			if (l > 4095L)
				l = 4095L;
			aLong1220 = super.aLong29;
			int k2 = super.saveClickY;
			if (k2 < 0)
				k2 = 0;
			else if (k2 > 502)
				k2 = 502;
			int k3 = super.saveClickX;
			if (k3 < 0)
				k3 = 0;
			else if (k3 > 764)
				k3 = 764;
			int k4 = k2 * 765 + k3;
			int j5 = 0;
			if (super.clickMode3 == 2)
				j5 = 1;
			int l5 = (int) l;
			stream.createFrame(241);
			stream.writeInt((l5 << 20) + (j5 << 19) + k4);
		}
		if (anInt1016 > 0)
			anInt1016--;
		if (super.keyArray[1] == 1 || super.keyArray[2] == 1
				|| super.keyArray[3] == 1 || super.keyArray[4] == 1)
			aBoolean1017 = true;
		if (aBoolean1017 && anInt1016 <= 0) {
			anInt1016 = 20;
			aBoolean1017 = false;
			stream.createFrame(86);
			stream.writeShort(anInt1184);
			stream.writeShortA(viewRotation);
		}
		if (super.awtFocus && !aBoolean954) {
			aBoolean954 = true;
			stream.createFrame(3);
			stream.writeByte(1);
		}
		if (!super.awtFocus && aBoolean954) {
			aBoolean954 = false;
			stream.createFrame(3);
			stream.writeByte(0);
		}
		loadingStages();
		method115();
		method90();
		anInt1009++;
		if (anInt1009 > 750)
			dropClient();
		method114();
		method95();
		method38();
		anInt945++;
		if (crossType != 0) {
			crossIndex += 20;
			if (crossIndex >= 400)
				crossType = 0;
		}
		if (atInventoryInterfaceType != 0) {
			atInventoryLoopCycle++;
			if (atInventoryLoopCycle >= 15) {
				if (atInventoryInterfaceType == 2)
					needDrawTabArea = true;
				if (atInventoryInterfaceType == 3)
					inputTaken = true;
				atInventoryInterfaceType = 0;
			}
		}
		if (activeInterfaceType != 0 || walkableInterface == 10000) {
			anInt989++;
			if (super.mouseX > anInt1087 + 5 || super.mouseX < anInt1087 - 5
					|| super.mouseY > anInt1088 + 5
					|| super.mouseY < anInt1088 - 5)
				aBoolean1242 = true;
			if (super.clickMode2 == 0) {
				if (activeInterfaceType == 2)
					needDrawTabArea = true;
				if (activeInterfaceType == 3)
					inputTaken = true;
				activeInterfaceType = 0;
				if (aBoolean1242 && anInt989 >= 5) {
					lastActiveInvInterface = -1;
					processRightClick();
					if (lastActiveInvInterface == anInt1084
							&& mouseInvInterfaceIndex != anInt1085) {
						RSInterface rsi = RSInterface.interfaceCache[anInt1084];
						int j1 = 0;
						if (anInt913 == 1 && rsi.contentType == 206)
							j1 = 1;
						if (rsi.inventory[mouseInvInterfaceIndex] <= 0)
							j1 = 0;
						if (rsi.aBoolean235) {
							int l2 = anInt1085;
							int l3 = mouseInvInterfaceIndex;
							rsi.inventory[l3] = rsi.inventory[l2];
							rsi.inventoryAmount[l3] = rsi.inventoryAmount[l2];
							rsi.inventory[l2] = -1;
							rsi.inventoryAmount[l2] = 0;
						} else if (j1 == 1) {
							int i3 = anInt1085;
							for (int i4 = mouseInvInterfaceIndex; i3 != i4;)
								if (i3 > i4) {
									rsi.swapInventoryItems(i3, i3 - 1);
									i3--;
								} else if (i3 < i4) {
									rsi.swapInventoryItems(i3, i3 + 1);
									i3++;
								}

						} else {
							rsi.swapInventoryItems(anInt1085,
									mouseInvInterfaceIndex);
						}
						stream.createFrame(214);
						stream.writeLEShortA(anInt1084);
						stream.writeByteC(j1);
						stream.writeLEShortA(anInt1085);
						stream.writeLEShort(mouseInvInterfaceIndex);
					}
				} else if ((anInt1253 == 1 || menuHasAddFriend(menuActionRow - 1))
						&& menuActionRow > 2)
					determineMenuSize();
				else if (menuActionRow > 0)
					doAction(menuActionRow - 1);
				atInventoryLoopCycle = 10;
				super.clickMode3 = 0;
			}
		}
		if (LandscapeScene.localX != -1) {
			int localX = LandscapeScene.localX;
			int localY = LandscapeScene.localY;
			boolean flag = calculatePath(0, 0, 0, 0, myPlayer.smallY[0], 0, 0, localY,
					myPlayer.smallX[0], true, localX);
			LandscapeScene.localX = -1;
			if (flag) {
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 1;
				crossIndex = 0;
			}
		}
		if (super.clickMode3 == 1 && aString844 != null) {
			aString844 = null;
			inputTaken = true;
			super.clickMode3 = 0;
		}
		if (!processMenuClick()) {
			processMainScreenClick();
			if (clientSize == 0)
				processTabArea();
			else
				processNewTabArea();
			processChatModeClick();
			if (quickChat)
				processQuickChatArea();
			processMapAreaMouse();
		}
		if (super.clickMode2 == 1 || super.clickMode3 == 1)
			anInt1213++;
		if (anInt1500 != 0 || anInt1044 != 0 || anInt1129 != 0) {
			if (anInt1501 < 50 && !menuOpen) {
				anInt1501++;
				if (anInt1501 == 50) {
					if (anInt1500 != 0) {
						inputTaken = true;
					}
					if (anInt1044 != 0) {
						needDrawTabArea = true;
					}
				}
			}
		} else if (anInt1501 > 0) {
			anInt1501--;
		} else if (anInt1501 > 0) {
			anInt1501--;
		}
		if (loadingStage == 2)
			method108();
		if (loadingStage == 2 && aBoolean1160)
			calcCameraPos();
		for (int i1 = 0; i1 < 5; i1++)
			anIntArray1030[i1]++;

		method73();
		super.idleTime++;
		if (super.idleTime > 4500) {
			anInt1011 = 250;
			super.idleTime -= 500;
			stream.createFrame(202);
		}
		anInt1010++;
		if (anInt1010 > 50)
			stream.createFrame(0);
		try {
			if (socketStream != null && stream.currentOffset > 0) {
				socketStream.queueBytes(stream.currentOffset, stream.buffer);
				stream.currentOffset = 0;
				anInt1010 = 0;
			}
		} catch (IOException _ex) {
			dropClient();
		} catch (Exception exception) {
			resetLogout();
		}
	}

	public void method63() {
		ObjectSpawnNode class30_sub1 = (ObjectSpawnNode) aClass19_1179
				.head();
		for (; class30_sub1 != null; class30_sub1 = (ObjectSpawnNode) aClass19_1179
				.next())
			if (class30_sub1.anInt1294 == -1) {
				class30_sub1.anInt1302 = 0;
				method89(class30_sub1);
			} else {
				class30_sub1.remove();
			}

	}

	public void resetImageProducers()
	{
		if (aRSImageProducer_1107 != null)
			return;
		super.fullGameScreen = null;
		chatAreaIP = null;
		mapAreaIP = null;
		tabAreaIP = null;
		gameScreenIP = null;
		aRSImageProducer_1123 = null;
		aRSImageProducer_1124 = null;
		aRSImageProducer_1125 = null;
		aRSImageProducer_1110 = new RSImageProducer(128, 265, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1111 = new RSImageProducer(128, 265, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1107 = new RSImageProducer(509, 171, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1108 = new RSImageProducer(360, 132, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		title = new RSImageProducer(getClientWidth(), getClientHeight(), getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1112 = new RSImageProducer(202, 238, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1113 = new RSImageProducer(203, 238, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1114 = new RSImageProducer(74, 94, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1115 = new RSImageProducer(75, 94, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		if(titleArchive != null)
		{
			//drawLogo();
			loadTitleScreen();
		}
		welcomeScreenRaised = true;
	}

	public float progress;
	public void updateProgress(String string, int percent) {
        for(float f = progress; f < (float)percent; f = (float)((double)f + 0.29999999999999999D)) {
            displayProgress(string, (int)f);
        }
        progress = percent;
    }

	public void displayProgress(String string, int percent) {
		int width = 360;
		int height = 80;
		int x = (getClientWidth() / 2) - (width / 2);
		int y = (getClientHeight() / 2) - (height / 2);
		anInt1079 = percent;
		aString1049 = string;
		resetImageProducers();
		if(titleArchive == null) {
			super.displayProgress(percent, string);
			return;
		}
		title.initDrawingArea();
		if (loadedImages || (new File(signlink.getCacheLocation() + "spritearchive.dat").exists() && new File(signlink.getCacheLocation() + "spritearchive.idx").exists())) {
			if (background != null && background[0] != null && background[1] != null && background[2] != null && background[3] != null) {
				background[0].drawImage((getClientWidth() / 2) - 382, (getClientHeight() / 2) - 251);
				background[1].drawImage(getClientWidth() / 2 + 1, (getClientHeight() / 2) - 251);
				background[2].drawImage((getClientWidth() / 2) - 382, getClientHeight() / 2 + 1);
				background[3].drawImage(getClientWidth() / 2 + 1, getClientHeight() / 2 + 1);
			}
		} else {
			DrawingArea474.drawFilledPixels(0, 0, getClientWidth(), getClientHeight(), 0x1e1e1e);
		}
		if (bar != null && bar[0] != null && bar[1] != null) {
			int barWidth = bar[0].myWidth;
			int barHeight = bar[0].myHeight;
			int fillWidth = (barWidth * percent) / 100;
			fill.myPixels = RSImage.cutPixels(bar[1].myPixels, barWidth, barHeight, fillWidth, barHeight);
			fill.myWidth = fillWidth;
			DrawingArea474.drawRoundedRectangle(x, y, width, height, 0, 150, true, false);
			arial[1].drawStringCenter(CLIENT_NAME + " is loading...", getClientWidth() / 2, (getClientHeight() / 2) - (barHeight) + 20, 0xffffff, true);
			bar[0].drawARGBImage((getClientWidth() / 2) - (barWidth / 2), (getClientHeight() / 2) - (barHeight / 2));
			fill.drawARGBImage((getClientWidth() / 2) - (barWidth / 2), (getClientHeight() / 2) - (barHeight / 2));
			arial[1].drawString(string, (getClientWidth() / 2) - (barWidth / 2) + 4, (getClientHeight() / 2) + (barHeight / 2) + 11, 0xffffff, true);
			arial[1].drawStringRight(percent + "%", (getClientWidth() / 2) + (barWidth / 2) - 2, (getClientHeight() / 2) + (barHeight / 2) + 11, 0xffffff, true);
		}
		title.drawGraphics(0, 0, super.graphics);
		if(welcomeScreenRaised) {
			welcomeScreenRaised = false;
		}
	}

	public void showPercentLoaded(int i, String s) {
		for (float f = percentLoaded; f < (float) i; f = (float) ((double) f + 0.29999999999999999D))
			drawLoadingText((int) f, s);
		percentLoaded = i;
	}
	
	public void resetImage() {
		DrawingArea.setAllPixelsToZero();
	}

	void drawLoadingText(int i, String s)
    {
			
			anInt1079 = i;
			aString1049 = s;
			resetImageProducers();
			if (titleArchive == null) {
				super.displayProgress(i, s);
				return;
			}
			title.initDrawingArea();
			loadingBarEmpty = new Sprite("Configuration/empty");
			loadingBarFull = new Sprite("Configuration/full");
			loadingBarEmpty.drawSprite(220, 9);
			loadingBarFull.drawSprite(286, 31);
			DrawingArea.drawPixels(13, 31, (286 + i), 0x302e2c, (194 - i));
			if (i == 194) {
				smallText.drawText(0xffffff, s + " - 100%", 28, 385);
			} else {
				smallText.drawText(0xffffff, s + " - " + (i/2) + "%", 28, 385);
			}
			title.drawGraphics(0, 0, super.graphics);
			if (welcomeScreenRaised)
				welcomeScreenRaised = false;

		}

	public void method65(int i, int j, int k, int l, RSInterface rsi, int i1,
			boolean flag, int j1) {
		int anInt992;
		if (aBoolean972)
			anInt992 = 32;
		else
			anInt992 = 0;
		aBoolean972 = false;
		if (k >= i && k < i + 16 && l >= i1 && l < i1 + 16) {
			rsi.scrollPosition -= anInt1213 * 4;
			if (flag) {
				needDrawTabArea = true;
			}
		} else if (k >= i && k < i + 16 && l >= (i1 + j) - 16 && l < i1 + j) {
			rsi.scrollPosition += anInt1213 * 4;
			if (flag) {
				needDrawTabArea = true;
			}
		} else if (k >= i - anInt992 && k < i + 16 + anInt992 && l >= i1 + 16
				&& l < (i1 + j) - 16 && anInt1213 > 0) {
			int l1 = ((j - 32) * j) / j1;
			if (l1 < 8)
				l1 = 8;
			int i2 = l - i1 - 16 - l1 / 2;
			int j2 = j - 32 - l1;
			rsi.scrollPosition = ((j1 - j) * i2) / j2;
			if (flag)
				needDrawTabArea = true;
			aBoolean972 = true;
		}
	}

	public boolean findGameObjectPath(int uid, int y, int x) {
		return findGameObjectPath(uid, y, x, -1);
	}
	
	public boolean findGameObjectPath(int uid, int y, int x, int id) {
		int i1 = id == -1 ? uid >> 14 & 0x7fff : id;
		int j1 = landscapeScene.getObject(plane, x, y, uid);
		if (j1 == -1)
			return false;
		int objectType = j1 & 0x1f;
		int objectRotation = j1 >> 6 & 3;
		crossX = super.saveClickX;
		crossY = super.saveClickY;
		crossType = 2;
		crossIndex = 0;
		if (objectType == 10 || objectType == 11 || objectType == 22) {
			ObjectDefinition def = ObjectDefinition.forId(i1);
			int objectSizeX;
			int objectSizeY;
			if (objectRotation == 0 || objectRotation == 2) {
				objectSizeX = def.sizeX;
				objectSizeY = def.sizeY;
			} else {
				objectSizeX = def.sizeY;
				objectSizeY = def.sizeX;
			}
			int objectFace = def.face;
			if (objectRotation != 0)
				objectFace = (objectFace << objectRotation & 0xf) + (objectFace >> 4 - objectRotation);
			if (!calculatePath(2, 0, objectSizeY, 0, myPlayer.smallY[0], objectSizeX, objectFace, y, myPlayer.smallX[0], false, x))
				return false;
		} else {
			if (!calculatePath(2, objectRotation, 0, objectType + 1, myPlayer.smallY[0], 0, 0, y, myPlayer.smallX[0], false, x))
				return false;
		}
		return true;
	}

	public final CRC32 aCRC32_930 = new CRC32();

	public JagexArchive streamLoaderForName(int i, String s, String s1, int j,
			int k) {
		byte abyte0[] = null;
		int l = 5;
		try {
			if (mainCacheFile[0] != null)
				abyte0 = mainCacheFile[0].get(i);
		} catch (Exception _ex) {
		}
		/*
		 * if (abyte0 != null) { aCRC32_930.reset(); aCRC32_930.update(abyte0);
		 * int i1 = (int)aCRC32_930.getValue(); if(i1 != j) abyte0 = null; }
		 */
		if (abyte0 != null)
			return new JagexArchive(abyte0);
		int j1 = 0;
		while (abyte0 == null) {
			String s2 = "Unknown error";
			updateProgress("Requesting " + s, k);
			try {
				int k1 = 0;
				DataInputStream datainputstream = openJagGrabInputStream(s1 + j);
				byte abyte1[] = new byte[6];
				datainputstream.readFully(abyte1, 0, 6);
				JagexBuffer stream = new JagexBuffer(abyte1);
				stream.currentOffset = 3;
				int i2 = stream.readTripleBytes() + 6;
				int j2 = 6;
				abyte0 = new byte[i2];
				System.arraycopy(abyte1, 0, abyte0, 0, 6);
				while (j2 < i2) {
					int l2 = i2 - j2;
					if (l2 > 1000)
						l2 = 1000;
					int j3 = datainputstream.read(abyte0, j2, l2);
					if (j3 < 0) {
						s2 = "Length error: " + j2 + "/" + i2;
						throw new IOException("EOF");
					}
					j2 += j3;
					int k3 = (j2 * 100) / i2;
					if (k3 != k1)
						updateProgress("Loading " + s + " - " + k3 + "%", k);
					k1 = k3;
				}
				datainputstream.close();
				try {
					if (mainCacheFile[0] != null)
						mainCacheFile[0].insertIndex(abyte0.length, abyte0, i);
				} catch (Exception _ex) {
					mainCacheFile[0] = null;
				}
				/*
				 * if(abyte0 != null) { aCRC32_930.reset();
				 * aCRC32_930.update(abyte0); int i3 =
				 * (int)aCRC32_930.getValue(); if(i3 != j) { abyte0 = null;
				 * j1++; s2 = "Checksum error: " + i3; } }
				 */

			} catch (IOException ioexception) {
				if (s2.equals("Unknown error"))
					s2 = "Connection error";
				abyte0 = null;
			} catch (NullPointerException _ex) {
				s2 = "Null error";
				abyte0 = null;
				if (!signlink.reporterror)
					return null;
			} catch (ArrayIndexOutOfBoundsException _ex) {
				s2 = "Bounds error";
				abyte0 = null;
				if (!signlink.reporterror)
					return null;
			} catch (Exception _ex) {
				s2 = "Unexpected error";
				abyte0 = null;
				if (!signlink.reporterror)
					return null;
			}
			if (abyte0 == null) {
				for (int l1 = l; l1 > 0; l1--) {
					if (j1 >= 3) {
						updateProgress("Game updated - please reload page", k);
						l1 = 10;
					} else {
						updateProgress(s2 + " - Retrying packet " + l1, k);
					}
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}
				l *= 2;
				if (l > 60)
					l = 60;
				aBoolean872 = !aBoolean872;
			}
		}
		return new JagexArchive(abyte0);
	}

	public void dropClient() {
		if (anInt1011 > 0) {
			resetLogout();
			return;
		}
		gameScreenIP.initDrawingArea();
		//normalFont.drawText(0, "Connection lost", 144, 257);
		//normalFont.drawText(0xffffff, "Connection lost", 143, 256);
		//normalFont.drawText(0, "Please wait - attempting to reestablish", 159,
		//		257);
		//normalFont.drawText(0xffffff,
		//		"Please wait - attempting to reestablish", 158, 256);
		drawLoadingMessage(2);
		gameScreenIP.drawGraphics(clientSize == 0 ? 4 : 0, clientSize == 0 ? 4 : 0,
				super.graphics);
		minimapState = 0;
		destX = 0;
		RSSocket rsSocket = socketStream;
		loggedIn = false;
		loginFailures = 0;
		login(getUsername(), myPassword, true, false);
		if (!loggedIn)
			resetLogout();
		try {
			rsSocket.close();
		} catch (Exception _ex) {
		}
	}

	public void doAction(int i) {
		if (i < 0)
			return;
		if (inputDialogState != 0 && inputDialogState != 3) {
			inputDialogState = 0;
			inputTaken = true;
		}
		int inventorySlot = menuActionCmd2[i];
		int childId = menuActionCmd3[i];
		int clickType = menuActionID[i];
		int entityId = menuActionCmd1[i];
		if (clickType >= 2000)
			clickType -= 2000;
		//if (myRank == 3) //TODO remove when testing is done
		//	System.out.println("inventorySlot=" + inventorySlot + "; childId=" + childId + "; click_type=" + clickType + "; entityId=" + entityId);
		if (childId >= 14001 && childId <= 14030) {
			
		}
		switch (childId) {
		case 17355:
			notes.deleteAll();
			break;
		case 17352:
			amountOrNameInput = "";
			inputDialogState = 6;
			break;
		case 18129:
			amountOrNameInput = "";
			inputDialogState = 5;
			break;
		case 23097:
			amountOrNameInput = "";
			inputDialogState = 5;
			break;
		}
		switch (clickType) {
		case 1015:
		case 1016:
		case 1017:
			stream.createFrame(185);
			stream.writeShort(clickType);
			break;
		}
		if (clickType == 476) {
			alertHandler.close();
		}
		if (clickType == 582) {
			NPC npc = npcArray[entityId];
			if (npc != null) {
				calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, npc.smallY[0],
						myPlayer.smallX[0], false, npc.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(57);
				stream.writeShortA(useEntityId);
				stream.writeShortA(entityId);
				stream.writeLEShort(useEntitySlot);
				stream.writeShortA(useEntityInterface);
			}
		}
		if (clickType == 234) {
			if (!calculatePath(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, childId,
					myPlayer.smallX[0], false, inventorySlot))
			calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, childId,
					myPlayer.smallX[0], false, inventorySlot);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(236);
			stream.writeLEShort(childId + regionAbsBaseY);
			stream.writeShort(entityId);
			stream.writeLEShort(inventorySlot + regionAbsBaseX);
		}
		if (clickType == 62) {
			int x = entityId & 0x7f;
			int y = entityId >> 7 & 0x7f;
			if (findGameObjectPath(entityId, y, x, inventorySlot)) {
				stream.createFrame(192);
				stream.writeShort(useEntityInterface);
				stream.writeLEShort(inventorySlot);
				stream.writeLEShortA(y + regionAbsBaseY);
				stream.writeLEShort(useEntitySlot);
				stream.writeLEShortA(x + regionAbsBaseX);
				stream.writeShort(useEntityId);
			}
		}
		if (clickType == 511) {
			if (!calculatePath(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, childId,
					myPlayer.smallX[0], false, inventorySlot))
			calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, childId,
					myPlayer.smallX[0], false, inventorySlot);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(25);
			stream.writeLEShort(useEntityInterface);
			stream.writeShortA(useEntityId);
			stream.writeShort(entityId);
			stream.writeShortA(childId + regionAbsBaseY);
			stream.writeLEShortA(useEntitySlot);
			stream.writeShort(inventorySlot + regionAbsBaseX);
		}
		if (clickType == 74) {
			stream.createFrame(122);
			stream.writeLEShortA(childId);
			stream.writeShortA(inventorySlot);
			stream.writeLEShort(entityId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = childId;
			atInventoryIndex = inventorySlot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[childId].parentId == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[childId].parentId == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (clickType == 315) {
			RSInterface rsi = RSInterface.interfaceCache[childId];
			boolean flag8 = true;
			if (rsi.contentType > 0)
				flag8 = promptUserForInput(rsi);
			if (flag8) {
				switch (childId) {
				default:
					stream.createFrame(185);
					stream.writeShort(childId);
					break;
				}
			}
		}
		if (clickType == 561) {
			Player player = playerArray[entityId];
			if (player != null) {
				calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
						player.smallY[0], myPlayer.smallX[0], false,
						player.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1188 += entityId;
				if (anInt1188 >= 90) {
					stream.createFrame(136);
					anInt1188 = 0;
				}
				stream.createFrame(128);
				stream.writeShort(entityId);
			}
		}
		if (clickType == 20) {
			NPC class30_sub2_sub4_sub1_sub1_1 = npcArray[entityId];
			if (class30_sub2_sub4_sub1_sub1_1 != null) {
				calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
						class30_sub2_sub4_sub1_sub1_1.smallY[0],
						myPlayer.smallX[0], false,
						class30_sub2_sub4_sub1_sub1_1.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(155);
				stream.writeLEShort(entityId);
			}
		}
		if (clickType == 779) {
			Player class30_sub2_sub4_sub1_sub2_1 = playerArray[entityId];
			if (class30_sub2_sub4_sub1_sub2_1 != null) {
				calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
						class30_sub2_sub4_sub1_sub2_1.smallY[0],
						myPlayer.smallX[0], false,
						class30_sub2_sub4_sub1_sub2_1.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(153);
				stream.writeLEShort(entityId);
			}
		}
		if (clickType == 516)
			if (!menuOpen)
				landscapeScene.method312(super.saveClickY - 4,
						super.saveClickX - 4);
			else
				landscapeScene.method312(childId - 4, inventorySlot - 4);
		if (clickType == 1062) {
			anInt924 += regionAbsBaseX;
			if (anInt924 >= 113) {
				stream.createFrame(183);
				stream.writeTripleInt(0xe63271);
				anInt924 = 0;
			}
			int x = entityId & 0x7f;
			int y = entityId >> 7 & 0x7f;
			findGameObjectPath(entityId, y, x, inventorySlot);
			stream.createFrame(228);
			stream.writeShortA(inventorySlot);
			stream.writeShortA(y + regionAbsBaseY);
			stream.writeShort(x + regionAbsBaseX);
		}
		if (clickType == 679 && !aBoolean1149) {
			stream.createFrame(40);
			stream.writeShort(childId);
			aBoolean1149 = true;
		}
		if (clickType == 431) {
			stream.createFrame(129);
			stream.writeShortA(inventorySlot);
			stream.writeShort(childId);
			stream.writeShortA(entityId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = childId;
			atInventoryIndex = inventorySlot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[childId].parentId == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[childId].parentId == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (clickType == 337 || clickType == 42 || clickType == 792 || clickType == 322) {
			String s = menuActionName[i];
			int k1 = s.indexOf("@whi@");
			if (k1 != -1) {
				long l3 = TextClass.longForName(s.substring(k1 + 5).trim());
				if (clickType == 337)
					addFriend(l3);
				if (clickType == 42)
					addIgnore(l3);
				if (clickType == 792)
					delFriend(l3);
				if (clickType == 322)
					delIgnore(l3);
			}
		}
		if(clickType == 104) {
			RSInterface class9_1 = RSInterface.interfaceCache[childId];
			spellID = class9_1.id;
				if(!Autocast) {
					Autocast = true;
					autocastId = class9_1.id;
					stream.createFrame(185);
					stream.writeShort(class9_1.id);
				} else
				if(autocastId == class9_1.id) {
					Autocast = false;
					autocastId = 0;
					stream.createFrame(185);
					stream.writeShort(class9_1.id);
				} else
				if(autocastId != class9_1.id) {
					Autocast = true;
					autocastId = class9_1.id;
					stream.createFrame(185);
					stream.writeShort(class9_1.id);
				}
		}
		if (clickType == 53) {
			stream.createFrame(135);
			stream.writeLEShort(inventorySlot);
			stream.writeShortA(childId);
			stream.writeLEShort(entityId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = childId;
			atInventoryIndex = inventorySlot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[childId].parentId == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[childId].parentId == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (clickType == 539) {
			stream.createFrame(16);
			stream.writeShortA(entityId);
			stream.writeLEShortA(inventorySlot);
			stream.writeLEShortA(childId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = childId;
			atInventoryIndex = inventorySlot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[childId].parentId == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[childId].parentId == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (clickType == 484 || clickType == 6) {
			String s1 = menuActionName[i];
			int l1 = s1.indexOf("@whi@");
			if (l1 != -1) {
				s1 = s1.substring(l1 + 5).trim();
				String s7 = TextClass.fixName(TextClass.nameForLong(TextClass
						.longForName(s1)));
				boolean flag9 = false;
				for (int j3 = 0; j3 < playerCount; j3++) {
					Player class30_sub2_sub4_sub1_sub2_7 = playerArray[playerIndices[j3]];
					if (class30_sub2_sub4_sub1_sub2_7 == null
							|| class30_sub2_sub4_sub1_sub2_7.name == null
							|| !class30_sub2_sub4_sub1_sub2_7.name
									.equalsIgnoreCase(s7))
						continue;
					calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
							class30_sub2_sub4_sub1_sub2_7.smallY[0],
							myPlayer.smallX[0], false,
							class30_sub2_sub4_sub1_sub2_7.smallX[0]);
					if (clickType == 484) {
						stream.createFrame(139);
						stream.writeLEShort(playerIndices[j3]);
					}
					if (clickType == 6) {
						anInt1188 += entityId;
						if (anInt1188 >= 90) {
							stream.createFrame(136);
							anInt1188 = 0;
						}
						stream.createFrame(128);
						stream.writeShort(playerIndices[j3]);
					}
					flag9 = true;
					break;
				}

				if (!flag9)
					pushMessage("", "Unable to find " + s7, 0);
			}
		}
		if (clickType == 870) {
			stream.createFrame(53);
			stream.writeShort(inventorySlot);
			stream.writeShortA(useEntitySlot);
			stream.writeLEShortA(entityId);
			stream.writeShort(useEntityInterface);
			stream.writeLEShort(useEntityId);
			stream.writeShort(childId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = childId;
			atInventoryIndex = inventorySlot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[childId].parentId == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[childId].parentId == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (clickType == 847) {
			stream.createFrame(87);
			stream.writeShortA(entityId);
			stream.writeShort(childId);
			stream.writeShortA(inventorySlot);
			atInventoryLoopCycle = 0;
			atInventoryInterface = childId;
			atInventoryIndex = inventorySlot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[childId].parentId == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[childId].parentId == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (clickType == 626) {
			RSInterface rsi_1 = RSInterface.interfaceCache[childId];
			spellSelected = 1;
			spellID = rsi_1.id;
			anInt1137 = childId;
			spellUsableOn = rsi_1.spellUsableOn;
			itemSelected = 0;
			needDrawTabArea = true;
			spellID = rsi_1.id;
			String s4 = rsi_1.selectedActionName;
			if (s4.indexOf(" ") != -1)
				s4 = s4.substring(0, s4.indexOf(" "));
			String s8 = rsi_1.selectedActionName;
			if (s8.indexOf(" ") != -1)
				s8 = s8.substring(s8.indexOf(" ") + 1);
			spellTooltip = s4 + " " + rsi_1.spellName + " " + s8;
			// rsi_1.sprite1.drawSprite(rsi_1.anInt263, rsi_1.anInt265,
			// 0xffffff);
			// rsi_1.sprite1.drawSprite(200,200);
			// System.out.println("Sprite: " + rsi_1.sprite1.toString());
			if (spellUsableOn == 16) {
				needDrawTabArea = true;
				tabId = 4;
				tabAreaAltered = true;
			}
			return;
		}
		if (clickType == 78) {
			stream.createFrame(117);
			stream.writeLEShortA(childId);
			stream.writeLEShortA(entityId);
			stream.writeLEShort(inventorySlot);
			atInventoryLoopCycle = 0;
			atInventoryInterface = childId;
			atInventoryIndex = inventorySlot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[childId].parentId == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[childId].parentId == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (clickType == 27) {
			Player class30_sub2_sub4_sub1_sub2_2 = playerArray[entityId];
			if (class30_sub2_sub4_sub1_sub2_2 != null) {
				calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
						class30_sub2_sub4_sub1_sub2_2.smallY[0],
						myPlayer.smallX[0], false,
						class30_sub2_sub4_sub1_sub2_2.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt986 += entityId;
				if (anInt986 >= 54) {
					stream.createFrame(189);
					stream.writeByte(234);
					anInt986 = 0;
				}
				stream.createFrame(73);
				stream.writeLEShort(entityId);
			}
		}
		if (clickType == 213) {
			if (!calculatePath(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, childId,
					myPlayer.smallX[0], false, inventorySlot))
			calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, childId,
					myPlayer.smallX[0], false, inventorySlot);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(79);
			stream.writeLEShort(childId + regionAbsBaseY);
			stream.writeShort(entityId);
			stream.writeShortA(inventorySlot + regionAbsBaseX);
		}
		if (clickType == 632) {
			stream.createFrame(145);
			stream.writeShortA(childId);
			stream.writeShortA(inventorySlot);
			stream.writeShortA(entityId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = childId;
			atInventoryIndex = inventorySlot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[childId].parentId == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[childId].parentId == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (clickType == 1026) {// Cast Special Attack
			if (choosingLeftClick) {
				leftClick = 7;
				choosingLeftClick = false;
			}
		}
		if (clickType == 1025) {
			if (choosingLeftClick) {
				leftClick = -1;
				choosingLeftClick = false;
			} else {
				leftClick = -1;
				choosingLeftClick = true;
			}
		}
		if (clickType == 1024) {// Follower Details
			if (choosingLeftClick) {
				leftClick = 6;
				choosingLeftClick = false;
			}
		}
		if (clickType == 1023) {// Attack
			if (choosingLeftClick) {
				leftClick = 5;
				choosingLeftClick = false;
			}
		}
		if (clickType == 1022) {// Interact
			if (choosingLeftClick) {
				leftClick = 4;
				choosingLeftClick = false;
			}
		}
		if (clickType == 1021) {// Renew Familiar
			if (choosingLeftClick) {
				leftClick = 3;
				choosingLeftClick = false;
			}
		}
		if (clickType == 1020) {// Tale BoB
			if (choosingLeftClick) {
				leftClick = 2;
				choosingLeftClick = false;
			}
		}
		if (clickType == 1019) {// Dismiss
			if (choosingLeftClick) {
				leftClick = 1;
				choosingLeftClick = false;
			} else {
			}
		}
		if (clickType == 1018) {// Call Follower
			if (choosingLeftClick) {
				leftClick = 0;
				choosingLeftClick = false;
			}
		}
		if (clickType >= 1018 && clickType <= 1025) {
			writeSettings();
		}
		if (clickType == 1014) {
			sendPacket185(19158);
		}
		if (clickType == 1013) {
			totalXP = 0;
			writeSettings();
		}
		if (clickType == 1012) {
			yellMode = 3;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 1011) {
			yellMode = 2;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 1010) {
			yellMode = 1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 1009) {
			yellMode = 0;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 1008) {
			cButtonCPos = 6;
			chatTypeView = 6;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 1007) {
			canGainXP = canGainXP ? false : true;
		}
		if (clickType == 1006 && !showBonus) {
			if (!gains.isEmpty()) {
				gains.removeAll(gains);
			}
			showXP = showXP ? false : true;
		}
		if (clickType == 1030 && !showXP) {
			showBonus = showBonus ? false : true;
		}
		if (clickType == 1005) {
			openQuickChat();
			aBoolean1233 = true;
		}
		if (clickType == 1004) {
			quickChat = false;
			canTalk = true;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 1003) {
			clanChatMode = 2;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 1002) {
			clanChatMode = 1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 1001) {
			clanChatMode = 0;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 1000) {
			cButtonCPos = 4;
			chatTypeView = 11;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 999) {
			cButtonCPos = 0;
			chatTypeView = 0;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 998) {
			cButtonCPos = 1;
			chatTypeView = 5;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 997) {
			publicChatMode = 3;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 996) {
			publicChatMode = 2;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 995) {
			publicChatMode = 1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 994) {
			publicChatMode = 0;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 993) {
			cButtonCPos = 2;
			chatTypeView = 1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 992) {
			publicChatMode = 2;
			aBoolean1233 = true;
			inputTaken = true;
			stream.createFrame(95);
			stream.writeByte(publicChatMode);
			stream.writeByte(publicChatMode);
			stream.writeByte(tradeMode);
		}
		if (clickType == 991) {
			publicChatMode = 1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 990) {
			publicChatMode = 0;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 989) {
			cButtonCPos = 3;
			chatTypeView = 2;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 987) {
			tradeMode = 2;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 986) {
			tradeMode = 1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 985) {
			tradeMode = 0;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 984) {
			cButtonCPos = 5;
			chatTypeView = 3;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 983) {
			duelMode = 2;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 982) {
			duelMode = 1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 981) {
			duelMode = 0;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 980) {
			cButtonCPos = 6;
			chatTypeView = 4;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if (clickType == 493) {
			stream.createFrame(75);
			stream.writeLEShortA(childId);
			stream.writeLEShort(inventorySlot);
			stream.writeShortA(entityId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = childId;
			atInventoryIndex = inventorySlot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[childId].parentId == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[childId].parentId == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (clickType == 652) {
			if (!calculatePath(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, childId,
					myPlayer.smallX[0], false, inventorySlot))
			calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, childId,
					myPlayer.smallX[0], false, inventorySlot);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(156);
			stream.writeShortA(inventorySlot + regionAbsBaseX);
			stream.writeLEShort(childId + regionAbsBaseY);
			stream.writeLEShortA(entityId);
		}
		if (clickType == 94) {
			if (!calculatePath(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, childId,
					myPlayer.smallX[0], false, inventorySlot))
			calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, childId,
					myPlayer.smallX[0], false, inventorySlot);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(181);
			stream.writeLEShort(childId + regionAbsBaseY);
			stream.writeShort(entityId);
			stream.writeLEShort(inventorySlot + regionAbsBaseX);
			stream.writeShortA(anInt1137);
		}
		if (clickType == 646) {
			stream.createFrame(185);
			stream.writeShort(childId);
			RSInterface class9_2 = RSInterface.interfaceCache[childId];
			if (class9_2.valueIndexArray != null
					&& class9_2.valueIndexArray[0][0] == 5) {
				int i2 = class9_2.valueIndexArray[0][1];
				if (variousSettings[i2] != class9_2.requiredValue[0]) {
					variousSettings[i2] = class9_2.requiredValue[0];
					toggleInterface(i2);
					needDrawTabArea = true;
				}
			}
			switch (childId) {
			case 19156:
				tabInterfaceIDs[12] = 23050;
				break;
			case 23090:
				tabInterfaceIDs[12] = 904;
				writeSettings();
				break;
			}
		}
		if (clickType == 225) {
			NPC class30_sub2_sub4_sub1_sub1_2 = npcArray[entityId];
			if (class30_sub2_sub4_sub1_sub1_2 != null) {
				calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
						class30_sub2_sub4_sub1_sub1_2.smallY[0],
						myPlayer.smallX[0], false,
						class30_sub2_sub4_sub1_sub1_2.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1226 += entityId;
				if (anInt1226 >= 85) {
					stream.createFrame(230);
					stream.writeByte(239);
					anInt1226 = 0;
				}
				stream.createFrame(17);
				stream.writeLEShortA(entityId);
			}
		}
		if (clickType == 965) {
			NPC class30_sub2_sub4_sub1_sub1_3 = npcArray[entityId];
			if (class30_sub2_sub4_sub1_sub1_3 != null) {
				calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
						class30_sub2_sub4_sub1_sub1_3.smallY[0],
						myPlayer.smallX[0], false,
						class30_sub2_sub4_sub1_sub1_3.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1134++;
				if (anInt1134 >= 96) {
					stream.createFrame(152);
					stream.writeByte(88);
					anInt1134 = 0;
				}
				stream.createFrame(21);
				stream.writeShort(entityId);
			}
		}
		if (clickType == 413) {
			NPC class30_sub2_sub4_sub1_sub1_4 = npcArray[entityId];
			if (class30_sub2_sub4_sub1_sub1_4 != null) {
				calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
						class30_sub2_sub4_sub1_sub1_4.smallY[0],
						myPlayer.smallX[0], false,
						class30_sub2_sub4_sub1_sub1_4.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(131);
				stream.writeLEShortA(entityId);
				stream.writeShortA(anInt1137);
			}
		}
		if (clickType == 200)
			clearTopInterfaces();
		if (clickType == 1025) {
			NPC class30_sub2_sub4_sub1_sub1_5 = npcArray[entityId];
			if (class30_sub2_sub4_sub1_sub1_5 != null) {
				NPCDefinition entityDef = class30_sub2_sub4_sub1_sub1_5.getDefinition();
				if (entityDef.childrenIDs != null)
					entityDef = entityDef.method161();
				if (entityDef != null) {
					String s9;
					if (entityDef.description != null)
						s9 = new String(entityDef.description);
					else
						s9 = "It's a " + entityDef.name + ".";
					pushMessage("", s9, 0);
				}
			}
		}
		if (clickType == 900) {
			int x = entityId & 0x7f;
			int y = entityId >> 7 & 0x7f;
			findGameObjectPath(entityId, y, x, inventorySlot);
			stream.createFrame(252);
			stream.writeLEShortA(inventorySlot);
			stream.writeLEShort(y + regionAbsBaseY);
			stream.writeShortA(x + regionAbsBaseX);
		}
		if (clickType == 412) {
			NPC class30_sub2_sub4_sub1_sub1_6 = npcArray[entityId];
			if (class30_sub2_sub4_sub1_sub1_6 != null) {
				calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
						class30_sub2_sub4_sub1_sub1_6.smallY[0],
						myPlayer.smallX[0], false,
						class30_sub2_sub4_sub1_sub1_6.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(72);
				stream.writeShortA(entityId);
			}
		}
		if (clickType == 365) {
			Player class30_sub2_sub4_sub1_sub2_3 = playerArray[entityId];
			if (class30_sub2_sub4_sub1_sub2_3 != null) {
				calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
						class30_sub2_sub4_sub1_sub2_3.smallY[0],
						myPlayer.smallX[0], false,
						class30_sub2_sub4_sub1_sub2_3.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(249);
				stream.writeShortA(entityId);
				stream.writeLEShort(anInt1137);
			}
		}
		if (clickType == 729) {
			Player class30_sub2_sub4_sub1_sub2_4 = playerArray[entityId];
			if (class30_sub2_sub4_sub1_sub2_4 != null) {
				calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
						class30_sub2_sub4_sub1_sub2_4.smallY[0],
						myPlayer.smallX[0], false,
						class30_sub2_sub4_sub1_sub2_4.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(39);
				stream.writeLEShort(entityId);
			}
		}
		if (clickType == 577) {
			Player class30_sub2_sub4_sub1_sub2_5 = playerArray[entityId];
			if (class30_sub2_sub4_sub1_sub2_5 != null) {
				calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
						class30_sub2_sub4_sub1_sub2_5.smallY[0],
						myPlayer.smallX[0], false,
						class30_sub2_sub4_sub1_sub2_5.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(139);
				stream.writeLEShort(entityId);
			}
		}
		if (clickType == 956) {
			int x = entityId & 0x7f;
			int y = entityId >> 7 & 0x7f;
			findGameObjectPath(entityId, y, x, inventorySlot);
			stream.createFrame(35);
			stream.writeLEShort(x + regionAbsBaseX);
			stream.writeShortA(anInt1137);
			stream.writeShortA(y + regionAbsBaseY);
			stream.writeLEShort(inventorySlot);
		}
		if (clickType == 567) {
			if (!calculatePath(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, childId,
					myPlayer.smallX[0], false, inventorySlot))
			calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, childId,
						myPlayer.smallX[0], false, inventorySlot);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(23);
			stream.writeLEShort(childId + regionAbsBaseY);
			stream.writeLEShort(entityId);
			stream.writeLEShort(inventorySlot + regionAbsBaseX);
		}
		if (clickType == 867) {
			if ((entityId & 3) == 0)
				anInt1175++;
			if (anInt1175 >= 59) {
				stream.createFrame(200);
				stream.writeShort(25501);
				anInt1175 = 0;
			}
			stream.createFrame(43);
			stream.writeLEShort(childId);
			stream.writeShortA(entityId);
			stream.writeShortA(inventorySlot);
			atInventoryLoopCycle = 0;
			atInventoryInterface = childId;
			atInventoryIndex = inventorySlot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[childId].parentId == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[childId].parentId == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (clickType == 543) {
			stream.createFrame(237);
			stream.writeShort(inventorySlot);
			stream.writeShortA(entityId);
			stream.writeShort(childId);
			stream.writeShortA(anInt1137);
			atInventoryLoopCycle = 0;
			atInventoryInterface = childId;
			atInventoryIndex = inventorySlot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[childId].parentId == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[childId].parentId == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (clickType == 606) {
			String s2 = menuActionName[i];
			int j2 = s2.indexOf("@whi@");
			if (j2 != -1)
				if (openInterfaceID == -1) {
					clearTopInterfaces();
					reportAbuseInput = s2.substring(j2 + 5).trim();
					canMute = false;
					for (int i3 = 0; i3 < RSInterface.interfaceCache.length; i3++) {
						if (RSInterface.interfaceCache[i3] == null
								|| RSInterface.interfaceCache[i3].contentType != 600)
							continue;
						reportAbuseInterfaceID = openInterfaceID = RSInterface.interfaceCache[i3].parentId;
						break;
					}

				} else {
					pushMessage(
							"",
							"Please close the interface you have open before using 'report abuse'", 0);
				}
		}
		if (clickType == 491) {
			Player class30_sub2_sub4_sub1_sub2_6 = playerArray[entityId];
			if (class30_sub2_sub4_sub1_sub2_6 != null) {
				calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
						class30_sub2_sub4_sub1_sub2_6.smallY[0],
						myPlayer.smallX[0], false,
						class30_sub2_sub4_sub1_sub2_6.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(14);
				stream.writeShortA(useEntityInterface);
				stream.writeShort(entityId);
				stream.writeShort(useEntityId);
				stream.writeLEShort(useEntitySlot);
			}
		}
		if (clickType == 639) {
			String s3 = menuActionName[i];
			int k2 = s3.indexOf("@whi@");
			if (k2 != -1) {
				long l4 = TextClass.longForName(s3.substring(k2 + 5).trim());
				int k3 = -1;
				for (int i4 = 0; i4 < friendsCount; i4++) {
					if (friendsListNames[i4] != l4)
						continue;
					k3 = i4;
					break;
				}
				if (k3 != -1 && friendsNodeIDs[k3] > 0) {
					inputTaken = true;
					inputDialogState = 0;
					showInput = true;
					promptInput = "";
					friendsListAction = 3;
					aLong953 = friendsListNames[k3];
					promptMessage = "Enter message to send to "
							+ friendsList[k3];
				}
			}
		}
		if (clickType == 454) {
			stream.createFrame(41);
			stream.writeShort(entityId);
			stream.writeShortA(inventorySlot);
			stream.writeShortA(childId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = childId;
			atInventoryIndex = inventorySlot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[childId].parentId == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[childId].parentId == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (clickType == 478) {
			NPC class30_sub2_sub4_sub1_sub1_7 = npcArray[entityId];
			if (class30_sub2_sub4_sub1_sub1_7 != null) {
				calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
						class30_sub2_sub4_sub1_sub1_7.smallY[0],
						myPlayer.smallX[0], false,
						class30_sub2_sub4_sub1_sub1_7.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				if ((entityId & 3) == 0)
					anInt1155++;
				if (anInt1155 >= 53) {
					stream.createFrame(85);
					stream.writeByte(66);
					anInt1155 = 0;
				}
				stream.createFrame(18);
				stream.writeLEShort(entityId);
			}
		}
		if (clickType == 113) {
			int x = entityId & 0x7f;
			int y = entityId >> 7 & 0x7f;
			if (findGameObjectPath(entityId, y, x, inventorySlot)) {
				stream.createFrame(70);
				stream.writeLEShort(x + regionAbsBaseX);
				stream.writeShort(y + regionAbsBaseY);
				stream.writeLEShortA(inventorySlot);
			}
		}
		if (clickType == 872) {
			int x = entityId & 0x7f;
			int y = entityId >> 7 & 0x7f;
			if (findGameObjectPath(entityId, y, x, inventorySlot)) {
				stream.createFrame(234);
				stream.writeLEShortA(x + regionAbsBaseX);
				stream.writeShortA(inventorySlot);
				stream.writeLEShortA(y + regionAbsBaseY);
			}
		}
		if (clickType == 502) {
			int x = entityId & 0x7f;
			int y = entityId >> 7 & 0x7f;
			if (findGameObjectPath(entityId, y, x, inventorySlot)) {
				stream.createFrame(132);
				stream.writeLEShortA(x + regionAbsBaseX);
				stream.writeShort(inventorySlot);
				stream.writeShortA(y + regionAbsBaseY);
			}
		}
		if (clickType == 1125) {
			ItemDef itemDef = ItemDef.forId(entityId);
			RSInterface class9_4 = RSInterface.interfaceCache[childId];
			String s5;
			if (class9_4 != null && class9_4.inventoryAmount[inventorySlot] >= 0x186a0)
				s5 = class9_4.inventoryAmount[inventorySlot] + " x " + itemDef.name;
			else if (itemDef.description != null)
				s5 = new String(itemDef.description);
			else
				s5 = "It's a " + itemDef.name + ".";
			pushMessage("", s5, 0);
		}
		if (clickType == 169) {
			if (loggedIn) {
				stream.createFrame(185);
				stream.writeShort(childId);
			}
			RSInterface class9_3 = RSInterface.interfaceCache[childId];
			if (class9_3.valueIndexArray != null
					&& class9_3.valueIndexArray[0][0] == 5) {
				int l2 = class9_3.valueIndexArray[0][1];
				variousSettings[l2] = 1 - variousSettings[l2];
				toggleInterface(l2);
				needDrawTabArea = true;
			}
		}
		if (clickType == 447) {
			itemSelected = 1;
			useEntitySlot = inventorySlot;
			useEntityInterface = childId;
			useEntityId = entityId;
			selectedItemName = ItemDef.forId(entityId).name;
			spellSelected = 0;
			needDrawTabArea = true;
			return;
		}
		if (clickType == 1226) {
			//int id = cmd1 >> 14 & 0x7fff;
			int id = inventorySlot;
			ObjectDefinition object = ObjectDefinition.forId(id);
			String examine;
			if (object.description != null)
				examine = new String(object.description);
			else
				examine = "It's a " + object.name + ".";
			pushMessage("", examine, 0);
			if (Constants.debugObjects) {
				String models = "";
				if (object.modelIds != null) {
					for (int index = 0; index < object.modelIds.length; index++) {
						for (int pointer = 0; pointer < object.modelIds[index].length; pointer++) {
							models += "[" + object.modelIds[index][pointer] + "]";
						}
					}
					String[] split = splitString(newRegularFont, "", models, 489, false);
					pushMessage("", id + ": " + split[0], 0);
					if (split.length > 1) {
						pushMessage("", split[1], 0);
					}
					pushMessage("", "offsetZ: " + object.translateY, 0);
				}
			}
		}
		if (clickType == 244) {
			if (!calculatePath(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, childId,
					myPlayer.smallX[0], false, inventorySlot))
			calculatePath(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, childId,
						myPlayer.smallX[0], false, inventorySlot);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(253);
			stream.writeLEShort(inventorySlot + regionAbsBaseX);
			stream.writeLEShortA(childId + regionAbsBaseY);
			stream.writeShortA(entityId);
		}
		if (clickType == 1448) {
			ItemDef itemDef_1 = ItemDef.forId(entityId);
			String s6;
			if (itemDef_1.description != null)
				s6 = new String(itemDef_1.description);
			else
				s6 = "It's a " + itemDef_1.name + ".";
			pushMessage("", s6, 0);
		}
		itemSelected = 0;
		spellSelected = 0;
		needDrawTabArea = true;

	}

	public void method70() {
		anInt1251 = 0;
		int j = (myPlayer.x >> 7) + regionAbsBaseX;
		int k = (myPlayer.y >> 7) + regionAbsBaseY;
		if (j >= 3053 && j <= 3156 && k >= 3056 && k <= 3136)
			anInt1251 = 1;
		if (j >= 3072 && j <= 3118 && k >= 9492 && k <= 9535)
			anInt1251 = 1;
		if (anInt1251 == 1 && j >= 3139 && j <= 3199 && k >= 3008 && k <= 3062)
			anInt1251 = 0;
	}

	public void run() {

		if (drawFlames) {
			drawFlames();
		} else {
			super.run();
		}
	}

	public void build3dScreenMenu() {
		if (itemSelected == 0 && spellSelected == 0) {
			menuActionName[menuActionRow] = "Walk here";
			menuActionID[menuActionRow] = 516;
			menuActionCmd2[menuActionRow] = super.mouseX;
			menuActionCmd3[menuActionRow] = super.mouseY;
			menuActionRow++;
		}
		int j = -1;
		for (int index = 0; index < Model.anInt1687; index++) {
			int data = Model.anIntArray1688[index];
			int x = data & 0x7f;
			int y = data >> 7 & 0x7f;
			int face = data >> 29 & 3;
			int id = data >> 14 & 0x7fff;
			if (data == j)
				continue;
			j = data;
			if (face == 2 && landscapeScene.getObject(plane, x, y, data) >= 0) {
				id = Model.mapObjectIds[index];
				ObjectDefinition object = ObjectDefinition.forId(id);
				if (object.childrenIDs != null)
					object = object.method580();
				if (object == null)
					continue;
				if (itemSelected == 1) {
					menuActionName[menuActionRow] = "Use " + selectedItemName + " with @cya@" + object.name;
					menuActionID[menuActionRow] = 62;
					menuActionCmd1[menuActionRow] = data;
					menuActionCmd2[menuActionRow] = id;//x;
					//menuActionCmd3[menuActionRow] = y;
					menuActionRow++;
				} else if (spellSelected == 1) {
					if ((spellUsableOn & 4) == 4) {
						menuActionName[menuActionRow] = spellTooltip + " @cya@"
								+ object.name;
						menuActionID[menuActionRow] = 956;
						menuActionCmd1[menuActionRow] = data;
						menuActionCmd2[menuActionRow] = id;//x;
						//menuActionCmd3[menuActionRow] = y;
						menuActionRow++;
					}
				} else {
					if (object.actions != null) {
						for (int i2 = 4; i2 >= 0; i2--)
							if (object.actions[i2] != null) {
								menuActionName[menuActionRow] = object.actions[i2]
										+ " @cya@" + object.name;
								if (i2 == 0)
									menuActionID[menuActionRow] = 502;
								if (i2 == 1)
									menuActionID[menuActionRow] = 900;
								if (i2 == 2)
									menuActionID[menuActionRow] = 113;
								if (i2 == 3)
									menuActionID[menuActionRow] = 872;
								if (i2 == 4)
									menuActionID[menuActionRow] = 1062;
								menuActionCmd1[menuActionRow] = data;
								menuActionCmd2[menuActionRow] = id;//x;
								//menuActionCmd3[menuActionRow] = y;
								menuActionRow++;
								// System.out.println("l1: " + l1 + ", k1: " +
								// k1 + ", j1: " + j1 + ", i1: " + i1);
							}

					}
					menuActionName[menuActionRow] = "Examine @cya@" + object.name;
					menuActionID[menuActionRow] = 1226;
					menuActionCmd1[menuActionRow] = data;
					menuActionCmd2[menuActionRow] = id;
					//menuActionCmd3[menuActionRow] = y;
					menuActionRow++;
				}
			}
			if (face == 1) {
				NPC npc = npcArray[id];
				if (npc.getDefinition().size == 1 && (npc.x & 0x7f) == 64
						&& (npc.y & 0x7f) == 64) {
					for (int j2 = 0; j2 < npcCount; j2++) {
						NPC npc2 = npcArray[npcIndices[j2]];
						if (npc2 != null && npc2 != npc
								&& npc2.getDefinition().size == 1 && npc2.x == npc.x
								&& npc2.y == npc.y)
							buildAtNPCMenu(npc2.getDefinition(), npcIndices[j2], y, x);
					}

					for (int l2 = 0; l2 < playerCount; l2++) {
						Player player = playerArray[playerIndices[l2]];
						if (player != null && player.x == npc.x
								&& player.y == npc.y)
							buildAtPlayerMenu(x, playerIndices[l2], player, y);
					}

				}
				buildAtNPCMenu(npc.getDefinition(), id, y, x);
			}
			if (face == 0) {
				Player player = playerArray[id];
				if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
					for (int k2 = 0; k2 < npcCount; k2++) {
						NPC class30_sub2_sub4_sub1_sub1_2 = npcArray[npcIndices[k2]];
						if (class30_sub2_sub4_sub1_sub1_2 != null
								&& class30_sub2_sub4_sub1_sub1_2.getDefinition().size == 1
								&& class30_sub2_sub4_sub1_sub1_2.x == player.x
								&& class30_sub2_sub4_sub1_sub1_2.y == player.y)
							buildAtNPCMenu(
									class30_sub2_sub4_sub1_sub1_2.getDefinition(),
									npcIndices[k2], y, x);
					}

					for (int i3 = 0; i3 < playerCount; i3++) {
						Player class30_sub2_sub4_sub1_sub2_2 = playerArray[playerIndices[i3]];
						if (class30_sub2_sub4_sub1_sub2_2 != null
								&& class30_sub2_sub4_sub1_sub2_2 != player
								&& class30_sub2_sub4_sub1_sub2_2.x == player.x
								&& class30_sub2_sub4_sub1_sub2_2.y == player.y)
							buildAtPlayerMenu(x, playerIndices[i3],
									class30_sub2_sub4_sub1_sub2_2, y);
					}

				}
				buildAtPlayerMenu(x, id, player, y);
			}
			if (face == 3) {
				Deque class19 = groundEntity[plane][x][y];
				if (class19 != null) {
					for (Item item = (Item) class19.getFirst(); item != null; item = (Item) class19
							.getNext()) {
						ItemDef itemDef = ItemDef.forId(item.id);
						if (itemSelected == 1) {
							menuActionName[menuActionRow] = "Use "
									+ selectedItemName + " with @lre@"
									+ itemDef.name;
							menuActionID[menuActionRow] = 511;
							menuActionCmd1[menuActionRow] = item.id;
							menuActionCmd2[menuActionRow] = x;
							menuActionCmd3[menuActionRow] = y;
							menuActionRow++;
						} else if (spellSelected == 1) {
							if ((spellUsableOn & 1) == 1) {
								menuActionName[menuActionRow] = spellTooltip
										+ " @lre@" + itemDef.name;
								menuActionID[menuActionRow] = 94;
								menuActionCmd1[menuActionRow] = item.id;
								menuActionCmd2[menuActionRow] = x;
								menuActionCmd3[menuActionRow] = y;
								menuActionRow++;
							}
						} else {
							for (int j3 = 4; j3 >= 0; j3--)
								if (itemDef.groundActions != null
										&& itemDef.groundActions[j3] != null) {
									menuActionName[menuActionRow] = itemDef.groundActions[j3]
											+ " @lre@" + itemDef.name;
									if (j3 == 0)
										menuActionID[menuActionRow] = 652;
									if (j3 == 1)
										menuActionID[menuActionRow] = 567;
									if (j3 == 2)
										menuActionID[menuActionRow] = 234;
									if (j3 == 3)
										menuActionID[menuActionRow] = 244;
									if (j3 == 4)
										menuActionID[menuActionRow] = 213;
									menuActionCmd1[menuActionRow] = item.id;
									menuActionCmd2[menuActionRow] = x;
									menuActionCmd3[menuActionRow] = y;
									menuActionRow++;
								} else if (j3 == 2) {
									menuActionName[menuActionRow] = "Take @lre@"
											+ itemDef.name;
									menuActionID[menuActionRow] = 234;
									menuActionCmd1[menuActionRow] = item.id;
									menuActionCmd2[menuActionRow] = x;
									menuActionCmd3[menuActionRow] = y;
									menuActionRow++;
								}

							// menuActionName[menuActionRow] = "Examine @lre@" +
							// itemDef.name + " @gre@(@whi@" + item.ID +
							// "@gre@)";
							menuActionName[menuActionRow] = "Examine @lre@"
									+ itemDef.name;
							menuActionID[menuActionRow] = 1448;
							menuActionCmd1[menuActionRow] = item.id;
							menuActionCmd2[menuActionRow] = x;
							menuActionCmd3[menuActionRow] = y;
							menuActionRow++;
						}
					}

				}
			}
		}
	}

	public void cleanUpForQuit() {
		signlink.reporterror = false;
		try {
			if (socketStream != null)
				socketStream.close();
		} catch (Exception _ex) {
		}
		socketStream = null;
		stopMidi();
		if (mouseDetection != null)
			mouseDetection.running = false;
		mouseDetection = null;
		try {
			resourceProvider.disable();
		} catch (Exception exception) {}
		resourceProvider = null;
		aStream_834 = null;
		stream = null;
		aStream_847 = null;
		packet = null;
		mapLocation = null;
		aByteArrayArray1183 = null;
		aByteArrayArray1247 = null;
		floorMap = null;
		objectMap = null;
		intGroundArray = null;
		byteGroundArray = null;
		landscapeScene = null;
		clippingPlanes = null;
		wayPoints = null;
		distanceValues = null;
		walkingQueueX = null;
		walkingQueueY = null;
		aByteArray912 = null;
		tabAreaIP = null;
		mapEdgeIP = null;
		leftFrame = null;
		topFrame = null;
		rightFrame = null;
		mapAreaIP = null;
		gameScreenIP = null;
		chatAreaIP = null;
		aRSImageProducer_1123 = null;
		aRSImageProducer_1124 = null;
		aRSImageProducer_1125 = null;
		/* Null pointers for custom sprites */
		border = null;
		//titleText = null;
		backgroundFix = null;
		loadingBarFull = null;
		loadingBarEmpty = null;

		titleBox = null;
		//socialBox = null;
		titleButton = null;
		logo = null;
		worldSelect = null;
		optionSelect = null;
		LoginLobbyNull();
		fbHover = null;
		/**/
		mapBack = null;
		sideIcons = null;
		redStones = null;
		orbs = null;
		hitBar = null;
		full = null;
		compass = null;
		hitMark = null;
		headIcons = null;
		skullIcons = null;
		headIconsHint = null;
		crosses = null;
		mapDotItem = null;
		mapDotNPC = null;
		mapDotPlayer = null;
		mapDotFriend = null;
		mapDotTeam = null;
		mapScenes = null;
		mapFunctions = null;
		anIntArrayArray929 = null;
		playerArray = null;
		playerIndices = null;
		anIntArray894 = null;
		aStreamArray895s = null;
		anIntArray840 = null;
		npcArray = null;
		npcIndices = null;
		groundEntity = null;
		aClass19_1179 = null;
		aClass19_1013 = null;
		aClass19_1056 = null;
		menuActionCmd2 = null;
		menuActionCmd3 = null;
		menuActionID = null;
		menuActionCmd1 = null;
		menuActionName = null;
		variousSettings = null;
		anIntArray1072 = null;
		anIntArray1073 = null;
		aClass30_Sub2_Sub1_Sub1Array1140 = null;
		miniMap = null;
		friendsList = null;
		friendsListNames = null;
		friendsNodeIDs = null;
		titleScreen = null;
		aRSImageProducer_1110 = null;
		aRSImageProducer_1111 = null;
		aRSImageProducer_1107 = null;
		aRSImageProducer_1108 = null;
		title = null;
		aRSImageProducer_1112 = null;
		aRSImageProducer_1113 = null;
		aRSImageProducer_1114 = null;
		aRSImageProducer_1115 = null;
		multiOverlay = null;
		nullLoader();
		ObjectDefinition.nullLoader();
		NPCDefinition.nullLoader();
		ItemDef.nullLoader();
		Floor.cache = null;
		IdentityKit.cache = null;
		RSInterface.interfaceCache = null;
		DummyClass.cache = null;
		Animation.anims = null;
		StillGraphics.cache = null;
		StillGraphics.aMRUNodes_415 = null;
		Varp.cache = null;
		super.fullGameScreen = null;
		Player.mruNodes = null;
		Rasterizer.nullLoader();
		LandscapeScene.nullLoader();
		Model.nullLoader();
		FrameReader.nullLoader();
		notes.notes = new String[30];
		System.gc();
	}

	public void printDebug() {
		System.out.println("============");
		System.out.println("flame-cycle:" + anInt1208);
		if (resourceProvider != null)
			System.out.println("Od-cycle:" + resourceProvider.resourceCycle);
		System.out.println("loop-cycle:" + loopCycle);
		System.out.println("draw-cycle:" + anInt1061);
		System.out.println("ptype:" + packetId);
		System.out.println("psize:" + packetSize);
		if (socketStream != null)
			socketStream.printDebug();
		super.shouldDebug = true;
	}

	Component getGameComponent() {
		if (signlink.mainapp != null)
			return signlink.mainapp;
		if (super.mainFrame != null)
			return super.mainFrame;
		else
			return this;
	}

	public String[] error = { "comon", "yse", "mabe", "mabey", "sumtimes",
			"cna", "mispell", "cehck", "chekc" };
	public String[] fixed = { "common", "yes", "maybe", "maybe", "sometimes",
			"can", "misspell", "check", "check" };
	public static long stringToLong(String s)
    {
        long l = 0L;
        for(int i = 0; i < s.length() && i < 12; i++)
        {
            char c = s.charAt(i);
            l *= 37L;
            if(c >= 'A' && c <= 'Z')
            {
                l += (1 + c) - 65;
                continue;
            }
            if(c >= 'a' && c <= 'z')
            {
                l += (1 + c) - 97;
                continue;
            }
            if(c >= '0' && c <= '9')
                l += (27 + c) - 48;
        }

        for(; l % 37L == 0L && l != 0L; l /= 37L);
        return l;
    }
	public void method73() {
		do {
			int pressedKey = readCharacter();
			if (pressedKey == -1)
				break;
			if (pressedKey == 96)
				break;
			if (consoleOpen) {
				if (pressedKey == 8 && consoleInput.length() > 0)
					consoleInput = consoleInput.substring(0,
							consoleInput.length() - 1);
				if (pressedKey >= 32 && pressedKey <= 122
						&& consoleInput.length() < 80)
					consoleInput += (char) pressedKey;

				if ((pressedKey == 13 || pressedKey == 10)
						&& consoleInput.length() > 0) {
					sendConsoleMessage(consoleInput, true);
					sendConsoleCommand(consoleInput);
					consoleInput = "";
					inputTaken = true;
				}
			} else if (openInterfaceID != -1 && openInterfaceID == reportAbuseInterfaceID) {
				if (pressedKey == 8 && reportAbuseInput.length() > 0)
					reportAbuseInput = reportAbuseInput.substring(0, reportAbuseInput.length() - 1);
				if ((pressedKey >= 97 && pressedKey <= 122 || pressedKey >= 65
						&& pressedKey <= 90 || pressedKey >= 48
						&& pressedKey <= 57 || pressedKey == 32)
						&& reportAbuseInput.length() < 12)
					reportAbuseInput += (char) pressedKey;
			} else if(CustomUserInput.id != -1 && inputDialogState == 0) {
				if(pressedKey == 8 && CustomUserInput.input.length() > 0) {
					CustomUserInput.input = CustomUserInput.input.substring(0, CustomUserInput.input.length() - 1);
				}
				if((pressedKey >= 97 && pressedKey <= 122 || pressedKey >= 65 && pressedKey <= 90 || pressedKey >= 48 && pressedKey <= 57 || pressedKey == 32) && CustomUserInput.input.length() < 13) {
					CustomUserInput.input += (char)pressedKey;
				}
				if((pressedKey == 13 || pressedKey == 10) && CustomUserInput.input.length() > 0) {
					stream.createFrame(5);
					stream.writeShort(CustomUserInput.id);
					stream.writeLong(stringToLong(CustomUserInput.input));
					CustomUserInput.reset();
				}
			} else if (showInput) {
				if (pressedKey >= 32 && pressedKey <= 122
						&& promptInput.length() < 80) {
					promptInput += (char) pressedKey;
					inputTaken = true;
				}
				if (pressedKey == 8 && promptInput.length() > 0) {
					promptInput = promptInput.substring(0,
							promptInput.length() - 1);
					inputTaken = true;
				}
				if (pressedKey == 13 || pressedKey == 10) {
					showInput = false;
					inputTaken = true;
					if (friendsListAction == 1) {
						long l = TextClass.longForName(promptInput);
						addFriend(l);
					}
					if (friendsListAction == 2 && friendsCount > 0) {
						long l1 = TextClass.longForName(promptInput);
						delFriend(l1);
					}
					if (friendsListAction == 3 && promptInput.length() > 0) {
						stream.createFrame(126);
						stream.writeByte(0);
						int k = stream.currentOffset;
						stream.writeLong(aLong953);
						TextInput.method526(promptInput, stream);
						stream.writeBytes(stream.currentOffset - k);
						promptInput = TextInput.processText(promptInput);
						// promptInput = Censor.doCensor(promptInput);
						pushMessage(TextClass.fixName(TextClass.nameForLong(aLong953)), promptInput, 6);
						if (publicChatMode == 2) {
							publicChatMode = 1;
							aBoolean1233 = true;
							stream.createFrame(95);
							stream.writeByte(publicChatMode);
							stream.writeByte(publicChatMode);
							stream.writeByte(tradeMode);
						}
					}
					if (friendsListAction == 4 && ignoreCount < 100) {
						long l2 = TextClass.longForName(promptInput);
						addIgnore(l2);
					}
					if (friendsListAction == 5 && ignoreCount > 0) {
						long l3 = TextClass.longForName(promptInput);
						delIgnore(l3);
					}
					if (friendsListAction == 6) {
						long l3 = TextClass.longForName(promptInput);
						chatJoin(l3);
					}
				}
			} else if (inputDialogState == 1) {
				if (pressedKey >= 48 && pressedKey <= 57
						&& amountOrNameInput.length() < 10) {
					amountOrNameInput += (char) pressedKey;
					inputTaken = true;
				}
				if (pressedKey == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0,
							amountOrNameInput.length() - 1);
					inputTaken = true;
				}
				if (pressedKey == 13 || pressedKey == 10) {
					if (amountOrNameInput.length() > 0) {
						int i1 = 0;
						try {
							i1 = Integer.parseInt(amountOrNameInput);
						} catch (Exception _ex) {
						}
						stream.createFrame(208);
						stream.writeInt(i1);
						// pushMessage("Client: " + i1, 0, "");
					}
					inputDialogState = 0;
					inputTaken = true;
				}
			} else if (inputDialogState == 2) {
				if (pressedKey >= 32 && pressedKey <= 122
						&& amountOrNameInput.length() < 12) {
					amountOrNameInput += (char) pressedKey;
					inputTaken = true;
				}
				if (pressedKey == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0,
							amountOrNameInput.length() - 1);
					inputTaken = true;
				}
				if (pressedKey == 13 || pressedKey == 10) {
					if (amountOrNameInput.length() > 0) {
						stream.createFrame(60);
						stream.writeLong(TextClass
								.longForName(amountOrNameInput));
					}
					inputDialogState = 0;
					inputTaken = true;
				}
			} else if (inputDialogState == 3) {
				if (pressedKey == 10) {
					inputDialogState = 0;
					inputTaken = true;
				}
				if (pressedKey >= 32 && pressedKey <= 122
						&& amountOrNameInput.length() < 40) {
					amountOrNameInput += (char) pressedKey;
					inputTaken = true;
				}
				if (pressedKey == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0,
							amountOrNameInput.length() - 1);
					inputTaken = true;
				}
			} else if (inputDialogState == 5) {
				if (pressedKey == 10) {
					amountOrNameInput = "";
					inputDialogState = 0;
					inputTaken = true;
				}
				if (pressedKey >= 32 && pressedKey <= 122
						&& amountOrNameInput.length() < 40) {
					amountOrNameInput += (char) pressedKey;
					inputTaken = true;
					searchBankItem(amountOrNameInput);
				}
				if (pressedKey == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0,
							amountOrNameInput.length() - 1);
					inputTaken = true;
					searchBankItem(amountOrNameInput);
				}
			} else if (inputDialogState == 6) {
				if (pressedKey == 10 || pressedKey == 13) {
					if (amountOrNameInput.length() > 0) {
						notes.add(amountOrNameInput);
						stream.createFrame(30);
						stream.writeByte(notes.totalNotes);
						stream.writeLong(TextUtils.longForName(amountOrNameInput));
					}
					amountOrNameInput = "";
					inputDialogState = 0;
					inputTaken = true;
				}
				if (pressedKey >= 32 && pressedKey <= 122
						&& amountOrNameInput.length() < 40) {
					amountOrNameInput += (char) pressedKey;
					inputTaken = true;
				}
				if (pressedKey == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0,
							amountOrNameInput.length() - 1);
					inputTaken = true;
				}
			} else if (backDialogID == -1 && canTalk
					&& muteReason.length() == 0 && showChat) {
				if (pressedKey >= 32
						&& pressedKey <= 122
						&& inputString.length() < (inputString
								.contains("<u=ff0000>") ? 200 : 80)) {
					inputString += (char) pressedKey;
					inputTaken = true;
				}
				if (pressedKey == 8 && inputString.length() > 0) {
					inputString = inputString.substring(0,
							inputString.length() - 1);
					inputTaken = true;
				}
				if ((pressedKey == 13 || pressedKey == 10)
						&& inputString.length() > 0) {
					if (inputString.contains("<u=ff0000>")) {
						inputString = inputString.replaceAll("<u=ff0000>", "");
						inputString = inputString.replaceAll("</u>", "");
					}
					if (inputString.startsWith("/")) {
						stream.createFrame(80);
						stream.writeByte(inputString.length());
						stream.writeString(inputString.substring(1));
					} else if (inputString.startsWith("::")) {
						/*stream.createFrame(103);
						stream.writeByte(inputString.length() - 1);
						stream.writeString(inputString.substring(2));*/
						pushMessage("", "Please use the console for commands (by pressing the \"`\" key located next to the \"1\" key).", 5);
						inputString = "";
					} else {
						String s = inputString.toLowerCase();
						if (s.startsWith("yellow:")) {
							chatColor = 0;
							inputString = inputString.substring(7);
						} else if (s.startsWith("red:")) {
							chatColor = 1;
							inputString = inputString.substring(4);
						} else if (s.startsWith("green:")) {
							chatColor = 2;
							inputString = inputString.substring(6);
						} else if (s.startsWith("cyan:")) {
							chatColor = 3;
							inputString = inputString.substring(5);
						} else if (s.startsWith("purple:")) {
							chatColor = 4;
							inputString = inputString.substring(7);
						} else if (s.startsWith("white:")) {
							chatColor = 5;
							inputString = inputString.substring(6);
						} else if (s.startsWith("flash1:")) {
							chatColor = 6;
							inputString = inputString.substring(7);
						} else if (s.startsWith("flash2:")) {
							chatColor = 7;
							inputString = inputString.substring(7);
						} else if (s.startsWith("flash3:")) {
							chatColor = 8;
							inputString = inputString.substring(7);
						} else if (s.startsWith("glow1:")) {
							chatColor = 9;
							inputString = inputString.substring(6);
						} else if (s.startsWith("glow2:")) {
							chatColor = 10;
							inputString = inputString.substring(6);
						} else if (s.startsWith("glow3:")) {
							chatColor = 11;
							inputString = inputString.substring(6);
						}
						s = inputString.toLowerCase();
						if (s.startsWith("wave:")) {
							chatEffect = 1;
							inputString = inputString.substring(5);
						} else if (s.startsWith("wave2:")) {
							chatEffect = 2;
							inputString = inputString.substring(6);
						} else if (s.startsWith("shake:")) {
							chatEffect = 3;
							inputString = inputString.substring(6);
						} else if (s.startsWith("scroll:")) {
							chatEffect = 4;
							inputString = inputString.substring(7);
						} else if (s.startsWith("slide:")) {
							chatEffect = 5;
							inputString = inputString.substring(6);
						}
						stream.createFrame(4);
						stream.writeByte(0);
						int j3 = stream.currentOffset;
						stream.method425(chatEffect);
						stream.method425(chatColor);
						aStream_834.currentOffset = 0;
						TextInput.method526(inputString, aStream_834);
						stream.method441(0, aStream_834.buffer,
								aStream_834.currentOffset);
						stream.writeBytes(stream.currentOffset - j3);
						inputString = TextInput.processText(inputString);
						myPlayer.textSpoken = inputString;
						myPlayer.textColour = chatColor;
						myPlayer.textEffect = chatEffect;
						myPlayer.textCycle = 150;
						pushMessage(getPrefix(myRank) + myPlayer.name, myPlayer.textSpoken, 2);
						if (publicChatMode == 2) {
							publicChatMode = 3;
							aBoolean1233 = true;
							stream.createFrame(95);
							stream.writeByte(publicChatMode);
							stream.writeByte(publicChatMode);
							stream.writeByte(tradeMode);
						}
					}
					inputString = "";
					inputTaken = true;
				}
			}
		} while (true);
	}
	
	private void searchBankItem(String name) {
		RSInterface bank = RSInterface.interfaceCache[5382];
		for (int i = 0; i < (bank.inventory.length - 1); i++) {
			if (bank.inventory[i] <= 0) {
				bank.inventory[i + 1] = bank.inventory[i];
				bank.inventoryAmount[i + 1] = bank.inventoryAmount[i];
				continue;
			}
			if (ItemDef.forId(bank.inventory[i] - 1).name.toLowerCase().contains(name.toLowerCase()))
				continue;
			bank.inventory[i] = 0;
			bank.inventoryAmount[i] = 0;
		}
		stream.createFrame(33);
		stream.writeShort(bank.inventory.length);
		for (int i = 0; i < bank.inventory.length; i++) {
			stream.writeShort(bank.inventory[i] + 1);
			stream.writeShort(bank.inventoryAmount[i]);
		}
	}
	
	private static final String CURSOR_INFO[] = {
		"Walk-to", "Take", "Use", "Talk-to", "Open", "Net", "Bait", "Cage", "Harpoon", //8
		"Chop", "Bury", "Pray-at", "Mine", "Eat", "Drink", "Wield", "Wear", "Remove", //17
		"Attack", "Enter", "Exit", "Climb-up", "Climb-down", "Search", "Steal", "Smith", //25
		"Clean", "Back", "Deposit Bank", "Pointless", "Inspect", "Pick", "Zoom", "Configure", "Pointless", //34
		"Pointless", "Accept", "Decline", //37
		"Cast Ice Barrage on", "Cast Blood Barrage on", "Cast Shadow Barrage on", "Cast Smoke Barrage on",  "Cast Ice Blitz on", "Cast Blood Blitz on", "Cast Shadow Blitz on",
		"Cast Smoke Blitz on", "Cast Ice Burst on", "Cast Blood Burst on", "Cast Shadow Burst on", "Cast Smoke Burst on", "Cast Ice Rush on", "Cast Blood Rush on", 
		"Cast Shadow Rush on", "Cast Smoke Rush on",
		"Link", "Split Private", "Graphics", "Audio", "Pointless", "Pray", "Click", "Information", "Cast High level alchemy on", "Cast Low level alchemy on", "Setting",
		"Select Starter", "Craft-rune", "View world map", "Withdraw", "Slash", "Pull",
		"Smelt", "Pickpocket"
	};

	public boolean isQuickChat = false;

	public void playerChat(String text, boolean isQuickSay) {
		isQuickChat = true;
		stream.createFrame(4);
		stream.writeByte(0);
		int j3 = stream.currentOffset;
		stream.method425(chatEffect);
		stream.method425(chatColor);
		aStream_834.currentOffset = 0;
		TextInput.method526(text, aStream_834);
		stream.method441(0, aStream_834.buffer, aStream_834.currentOffset);
		stream.writeBytes(stream.currentOffset - j3);
		text = TextInput.processText(text);
		myPlayer.textSpoken = text;
		myPlayer.textColour = chatColor;
		myPlayer.textEffect = chatEffect;
		myPlayer.textCycle = 150;
		pushMessage(getPrefix(myRank) + myPlayer.name, myPlayer.textSpoken, 2);
		if (publicChatMode == 2) {
			publicChatMode = 3;
			aBoolean1233 = true;
			stream.createFrame(95);
			stream.writeByte(publicChatMode);
			stream.writeByte(publicChatMode);
			stream.writeByte(tradeMode);
		}
	}

	public void buildPublicChat(int j) {
		int l = 0;
		for (int index = 0; index < 500; index++) {
			if (chatMessages[index] == null)
				continue;
			if (chatTypeView != 1)
				continue;
			int type = chatTypes[index];
			String name = chatNames[index];
			//String message = chatMessages[index];
			int positionY = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if (positionY < -23)
				break;
			if (name != null && name.indexOf("@") == 0) {
				name = name.substring(5);
			}
			if ((type == 1 || type == 2)
					&& (type == 1 || publicChatMode == 0 || publicChatMode == 1
							&& isFriendOrSelf(name))) {
				if (j > positionY - 14 && j <= positionY && !name.equals(myPlayer.name)) {
					if (myRank >= 1) {
						menuActionName[menuActionRow] = "Report abuse @whi@"
								+ name;
						menuActionID[menuActionRow] = 606;
						menuActionRow++;
					}
					if (!isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Add ignore @whi@" + name;
						menuActionID[menuActionRow] = 42;
						menuActionRow++;
						menuActionName[menuActionRow] = "Add friend @whi@" + name;
						menuActionID[menuActionRow] = 337;
						menuActionRow++;
					}
					if (isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Message @whi@" + name;
						menuActionID[menuActionRow] = 639;
						menuActionRow++;
					}
				}
				l++;
			}
		}
	}

	public void buildFriendChat(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			if (chatTypeView != 2)
				continue;
			int type = chatTypes[i1];
			String name = chatNames[i1];
			//String message = chatMessages[i1];
			int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if (k1 < -23)
				break;
			if (name != null && name.indexOf("@") == 0) {
				name = name.substring(5);
			}
			if ((type == 5 || type == 6)
					&& (splitpublicChat == 0 || chatTypeView == 2)
					&& (type == 6 || publicChatMode == 0 || publicChatMode == 1
							&& isFriendOrSelf(name)))
				l++;
			if ((type == 3 || type == 7)
					&& (splitpublicChat == 0 || chatTypeView == 2)
					&& (type == 7 || publicChatMode == 0 || publicChatMode == 1
							&& isFriendOrSelf(name))) {
				if (j > k1 - 14 && j <= k1) {
					if (myRank >= 1) {
						menuActionName[menuActionRow] = "Report abuse @whi@"
								+ name;
						menuActionID[menuActionRow] = 606;
						menuActionRow++;
					}
					if (!isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Add ignore @whi@" + name;
						menuActionID[menuActionRow] = 42;
						menuActionRow++;
						menuActionName[menuActionRow] = "Add friend @whi@" + name;
						menuActionID[menuActionRow] = 337;
						menuActionRow++;
					}
					if (isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Message @whi@" + name;
						menuActionID[menuActionRow] = 639;
						menuActionRow++;
					}
				}
				l++;
			}
		}
	}

	public void buildDuelorTrade(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			if (chatTypeView != 3 && chatTypeView != 4)
				continue;
			int j1 = chatTypes[i1];
			String name = chatNames[i1];
			int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if (k1 < -23)
				break;
			if (name != null && name.indexOf("@") == 0) {
				name = name.substring(5);
			}
			if (chatTypeView == 3 && j1 == 4
					&& (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(name))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept trade @whi@" + name;
					menuActionID[menuActionRow] = 484;
					menuActionRow++;
				}
				l++;
			}
			if (chatTypeView == 4 && j1 == 8
					&& (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(name))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept challenge @whi@"
							+ name;
					menuActionID[menuActionRow] = 6;
					menuActionRow++;
				}
				l++;
			}
			if (j1 == 12) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Go-to @blu@" + name;
					menuActionID[menuActionRow] = 915;
					menuActionRow++;
				}
				l++;
			}
		}
	}

	public void buildChatAreaMenu(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			int j1 = chatTypes[i1];
			int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if (k1 < -23)
				break;
			String name = chatNames[i1];
			if (chatTypeView == 1) {
				buildPublicChat(j);
				break;
			}
			if (chatTypeView == 2) {
				buildFriendChat(j);
				break;
			}
			if (chatTypeView == 3 || chatTypeView == 4) {
				buildDuelorTrade(j);
				break;
			}
			if (chatTypeView == 5) {
				break;
			}
			if (name != null && name.indexOf("@") == 0) {
				name = name.substring(5);
			}
			if (j1 == 0)
				l++;
			if ((j1 == 1 || j1 == 2)
					&& (j1 == 1 || publicChatMode == 0 || publicChatMode == 1
							&& isFriendOrSelf(name))) {
				if (j > k1 - 14 && j <= k1 && !name.equals(myPlayer.name)) {
					if (myRank >= 1) {
						menuActionName[menuActionRow] = "Report abuse @whi@"
								+ name;
						menuActionID[menuActionRow] = 606;
						menuActionRow++;
					}
					if (!isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Add ignore @whi@" + name;
						menuActionID[menuActionRow] = 42;
						menuActionRow++;
						menuActionName[menuActionRow] = "Add friend @whi@" + name;
						menuActionID[menuActionRow] = 337;
						menuActionRow++;
					}
					if (isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Message @whi@" + name;
						menuActionID[menuActionRow] = 639;
						menuActionRow++;
					}
				}
				l++;
			}
			if ((j1 == 3 || j1 == 7)
					&& splitpublicChat == 0
					&& (j1 == 7 || publicChatMode == 0 || publicChatMode == 1
							&& isFriendOrSelf(name))) {
				if (j > k1 - 14 && j <= k1) {
					if (myRank >= 1) {
						menuActionName[menuActionRow] = "Report abuse @whi@"
								+ name;
						menuActionID[menuActionRow] = 606;
						menuActionRow++;
					}
					if (!isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Add ignore @whi@" + name;
						menuActionID[menuActionRow] = 42;
						menuActionRow++;
						menuActionName[menuActionRow] = "Reply to @whi@" + name;
						menuActionID[menuActionRow] = 639;
						menuActionRow++;
						menuActionName[menuActionRow] = "Add friend @whi@" + name;
						menuActionID[menuActionRow] = 337;
						menuActionRow++;
					}
					if (isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Message @whi@" + name;
						menuActionID[menuActionRow] = 639;
						menuActionRow++;
					}
				}
				l++;
			}
			if (j1 == 4
					&& (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(name))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept trade @whi@" + name;
					menuActionID[menuActionRow] = 484;
					menuActionRow++;
				}
				l++;
			}
			if ((j1 == 5 || j1 == 6) && splitpublicChat == 0
					&& publicChatMode < 2)
				l++;
			if (j1 == 8
					&& (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(name))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept challenge @whi@"
							+ name;
					menuActionID[menuActionRow] = 6;
					menuActionRow++;
				}
				l++;
			}
		}
	}

	public void drawFriendsListOrWelcomeScreen(RSInterface rsi) {
		int contentType = rsi.contentType;
		if (contentType >= 1 && contentType <= 100 || contentType >= 701 && contentType <= 800) {
			if (contentType == 1 && friendsListServerStatus == 0) {
				rsi.disabledText = "Loading friend list";
				rsi.actionType = 0;
				return;
			}
			if (contentType == 1 && friendsListServerStatus == 1) {
				rsi.disabledText = "Connecting to friendserver";
				rsi.actionType = 0;
				return;
			}
			if (contentType == 2 && friendsListServerStatus != 2) {
				rsi.disabledText = "Please wait...";
				rsi.actionType = 0;
				return;
			}
			int k = friendsCount;
			if (friendsListServerStatus != 2)
				k = 0;
			if (contentType > 700)
				contentType -= 601;
			else
				contentType--;
			if (contentType >= k) {
				rsi.disabledText = "";
				rsi.actionType = 0;
				return;
			}
		if(contentType == 901) {
				rsi.disabledText = friendsCount + " / 200";
				return;
			}
		if(contentType == 902) {
				rsi.disabledText = ignoreCount + " / 100";
				return;
			} else {
				rsi.disabledText = friendsList[contentType];
				rsi.actionType = 1;
				return;
			}
		}
		if (contentType >= 101 && contentType <= 200 || contentType >= 801 && contentType <= 900) {
			int l = friendsCount;
			if (friendsListServerStatus != 2)
				l = 0;
			if (contentType > 800)
				contentType -= 701;
			else
				contentType -= 101;
			if (contentType >= l) {
				rsi.disabledText = "";
				rsi.actionType = 0;
				return;
			}
			if (friendsNodeIDs[contentType] == 0)
				rsi.disabledText = "<col=FF0000>Offline";
			else if (friendsNodeIDs[contentType] == nodeID)
				rsi.disabledText = "<col=00FF00>Online"/*
														 * + (friendsNodeIDs[j]
														 * - 9)
														 */;
			else
				rsi.disabledText = "<col=FF0000>Offline"/*
														 * + (friendsNodeIDs[j]
														 * - 9)
														 */;
			rsi.actionType = 1;
			return;
		}
		if (contentType == 203) {
			int i1 = friendsCount;
			if (friendsListServerStatus != 2)
				i1 = 0;
			rsi.scrollMax = i1 * 15 + 20;
			if (rsi.scrollMax <= rsi.height)
				rsi.scrollMax = rsi.height + 1;
			return;
		}
		if (contentType >= 401 && contentType <= 500) {
			if ((contentType -= 401) == 0 && friendsListServerStatus == 0) {
				rsi.disabledText = "Loading ignore list";
				rsi.actionType = 0;
				return;
			}
			if (contentType == 1 && friendsListServerStatus == 0) {
				rsi.disabledText = "Please wait...";
				rsi.actionType = 0;
				return;
			}
			int j1 = ignoreCount;
			if (friendsListServerStatus == 0)
				j1 = 0;
			if (contentType >= j1) {
				rsi.disabledText = "";
				rsi.actionType = 0;
				return;
			} else {
				rsi.disabledText = TextClass.fixName(TextClass
						.nameForLong(ignoreListNames[contentType]));
				rsi.actionType = 1;
				return;
			}
		}
		if (contentType == 503) {
			rsi.scrollMax = ignoreCount * 15 + 20;
			if (rsi.scrollMax <= rsi.height)
				rsi.scrollMax = rsi.height + 1;
			return;
		}
		if (contentType == 327) {
			rsi.modelRotation1 = 150;
			rsi.modelRotation2 = (int) (Math.sin((double) loopCycle / 40D) * 256D) & 0x7ff;
			if (aBoolean1031) {
				for (int k1 = 0; k1 < 7; k1++) {
					int l1 = anIntArray1065[k1];
					if (l1 >= 0 && !IdentityKit.cache[l1].method537())
						return;
				}

				aBoolean1031 = false;
				Model aclass30_sub2_sub4_sub6s[] = new Model[7];
				int i2 = 0;
				for (int j2 = 0; j2 < 7; j2++) {
					int k2 = anIntArray1065[j2];
					if (k2 >= 0)
						aclass30_sub2_sub4_sub6s[i2++] = IdentityKit.cache[k2]
								.method538();
				}

				Model model = new Model(i2, aclass30_sub2_sub4_sub6s);
				for (int l2 = 0; l2 < 5; l2++)
					if (anIntArray990[l2] != 0) {
						model.setColor(
								(short) VALID_CLOTHE_COLOUR[l2][0],
								(short) VALID_CLOTHE_COLOUR[l2][anIntArray990[l2]]);
						if (l2 == 1)
							model.setColor((short) anIntArray1204[0],
									(short) anIntArray1204[anIntArray990[l2]]);
					}

				model.createBones();
				if (myPlayer.standAnimation >= 0) {
					Animation animDef = Animation.get(myPlayer.standAnimation);
					if (animDef != null)
						model.applyTransformation(animDef.getFrame(0), animDef);

				}
				model.setLighting(64, 850, -30, -50, -30, true, true);
				rsi.mediaType = 5;
				rsi.mediaId = 0;
				RSInterface.method208(aBoolean994, model);
			}
			return;
		}
		if (contentType == 328) {
			RSInterface rsInterface = rsi;
			int verticleTilt = 150;
			int anInt1519 = (int) (Math.sin((double) loopCycle / 40D) * 256D) & 0x7ff;
			rsInterface.modelRotation1 = verticleTilt;
			rsInterface.modelRotation2 = anInt1519;
			if (aBoolean1031) {
				Model characterDisplay = myPlayer.method452();
				for (int l2 = 0; l2 < 5; l2++)
					if (anIntArray990[l2] != 0) {
						characterDisplay
								.setColor(
										(short) VALID_CLOTHE_COLOUR[l2][0],
										(short) VALID_CLOTHE_COLOUR[l2][anIntArray990[l2]]);
						if (l2 == 1)
							characterDisplay.setColor(
									(short) anIntArray1204[0],
									(short) anIntArray1204[anIntArray990[l2]]);
					}
				int staticFrame = myPlayer.standAnimation;
				// characterDisplay.method469();
				if (staticFrame >= 0) {
					Animation animDef = Animation.get(staticFrame);
					if (animDef != null)
						characterDisplay
								.applyTransformation(animDef.getFrame(0), animDef);

				}
				rsInterface.mediaType = 5;
				rsInterface.mediaId = 0;
				RSInterface.method208(aBoolean994, characterDisplay);
			}
			return;
		}
		if (contentType == 324) {
			if (aClass30_Sub2_Sub1_Sub1_931 == null) {
				aClass30_Sub2_Sub1_Sub1_931 = rsi.sprite1;
				aClass30_Sub2_Sub1_Sub1_932 = rsi.sprite2;
			}
			if (aBoolean1047) {
				rsi.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
				return;
			} else {
				rsi.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
				return;
			}
		}
		if (contentType == 325) {
			if (aClass30_Sub2_Sub1_Sub1_931 == null) {
				aClass30_Sub2_Sub1_Sub1_931 = rsi.sprite1;
				aClass30_Sub2_Sub1_Sub1_932 = rsi.sprite2;
			}
			if (aBoolean1047) {
				rsi.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
				return;
			} else {
				rsi.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
				return;
			}
		}
		if (contentType == 600) {
			rsi.disabledText = reportAbuseInput;
			if (loopCycle % 20 < 10) {
				rsi.disabledText += "|";
				return;
			} else {
				rsi.disabledText += " ";
				return;
			}
		}
		if (contentType == 9960 || contentType == 9961) {
			rsi.disabledText = CustomUserInput.input;
			CustomUserInput.id = rsi.id;
			if (loopCycle % 20 < 10) {
				rsi.disabledText += "|";
				return;
			} else {
				rsi.disabledText += "";
				return;
			}
		}
		if (contentType == 613)
			if (myRank >= 1) {
				if (canMute) {
					rsi.textColor = 0xff0000;
					rsi.disabledText = "Moderator option: Mute player for 48 hours: <ON>";
				} else {
					rsi.textColor = 0xffffff;
					rsi.disabledText = "Moderator option: Mute player for 48 hours: <OFF>";
				}
			} else {
				rsi.disabledText = "";
			}
		if (contentType == 650 || contentType == 655)
			if (anInt1193 != 0) {
				String s;
				if (daysSinceLastLogin == 0)
					s = "earlier today";
				else if (daysSinceLastLogin == 1)
					s = "yesterday";
				else
					s = daysSinceLastLogin + " days ago";
				rsi.disabledText = "You last logged packet " + s + " from: "
						+ signlink.dns;
			} else {
				rsi.disabledText = "";
			}
		if (contentType == 651) {
			if (unreadMessages == 0) {
				rsi.disabledText = "0 unread messages";
				rsi.textColor = 0xffff00;
			}
			if (unreadMessages == 1) {
				rsi.disabledText = "1 unread message";
				rsi.textColor = 65280;
			}
			if (unreadMessages > 1) {
				rsi.disabledText = unreadMessages + " unread messages";
				rsi.textColor = 65280;
			}
		}
		if (contentType == 652)
			if (daysSinceRecovChange == 201) {
				if (membersInt == 1)
					rsi.disabledText = "@yel@This is a non-members world: @whi@Since you are a member we";
				else
					rsi.disabledText = "";
			} else if (daysSinceRecovChange == 200) {
				rsi.disabledText = "You have not yet set any password recovery questions.";
			} else {
				String s1;
				if (daysSinceRecovChange == 0)
					s1 = "Earlier today";
				else if (daysSinceRecovChange == 1)
					s1 = "Yesterday";
				else
					s1 = daysSinceRecovChange + " days ago";
				rsi.disabledText = s1 + " you changed your recovery questions";
			}
		if (contentType == 653)
			if (daysSinceRecovChange == 201) {
				if (membersInt == 1)
					rsi.disabledText = "@whi@recommend you use a members world instead. You may use";
				else
					rsi.disabledText = "";
			} else if (daysSinceRecovChange == 200)
				rsi.disabledText = "We strongly recommend you do so now to secure your account.";
			else
				rsi.disabledText = "If you do not remember making this change then cancel it immediately";
		if (contentType == 654) {
			if (daysSinceRecovChange == 201)
				if (membersInt == 1) {
					rsi.disabledText = "@whi@this world but member benefits are unavailable whilst here.";
					return;
				} else {
					rsi.disabledText = "";
					return;
				}
			if (daysSinceRecovChange == 200) {
				rsi.disabledText = "Do this from the 'account management' area on our front webpage";
				return;
			}
			rsi.disabledText = "Do this from the 'account management' area on our front webpage";
		}
		if (contentType == 1004) {
			if (maxLevel[0] < 99) {
				rsi.disabledText = "Attack: " + currentLevel[0] + "/"
						+ maxLevel[0] + "\\nCurrent XP:\\r" + currentExp[0]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[0] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[0] + 1) - currentExp[0]);
				return;
			} else {
				rsi.disabledText = "Attack: " + currentLevel[0] + "/"
						+ maxLevel[0] + "\\nCurrent XP:\\r" + currentExp[0]
						+ "";
				return;
			}
		}
		if (contentType == 1005) {
			int max = (maxLevel[3] / 10), current = (currentLevel[3] / 10);
			if (max < 99) {
				rsi.disabledText = "Constitution: " + current + "/"
						+ max + "\\nCurrent XP:\\r" + currentExp[3]
						+ "\\nNext level:\\r" + getXPForLevel(max + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(max + 1) - currentExp[3]);
				return;
			} else {
				rsi.disabledText = "Constitution: " + current + "/"
						+ max + "\\nCurrent XP:\\r" + currentExp[3];
				return;
			}
		}
		if (contentType == 1006) {
			if (maxLevel[14] < 99) {
				rsi.disabledText = "Mining: " + currentLevel[14] + "/"
						+ maxLevel[14] + "\\nCurrent XP:\\r" + currentExp[14]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[14] + 1)
						+ "\\nRemainder: \\r"
						+ (getXPForLevel(maxLevel[14] + 1) - currentExp[14]);
				return;
			} else {
				rsi.disabledText = "Mining: " + currentLevel[14] + "/"
						+ maxLevel[14] + "\\nCurrent XP:\\r" + currentExp[14];
				return;
			}
		}
		if (contentType == 1007) {
			if (maxLevel[2] < 99) {
				rsi.disabledText = "Strength: " + currentLevel[2] + "/"
						+ maxLevel[2] + "\\nCurrent XP:\\r" + currentExp[2]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[2] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[2] + 1) - currentExp[2]);
				return;
			} else {
				rsi.disabledText = "Strength: " + currentLevel[2] + "/"
						+ maxLevel[2] + "\\nCurrent XP:\\r" + currentExp[2];
				return;
			}
		}
		if (contentType == 1008) {
			if (maxLevel[16] < 99) {
				rsi.disabledText = "Agility: " + currentLevel[16] + "/"
						+ maxLevel[16] + "\\nCurrent XP:\\r" + currentExp[16]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[16] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[16] + 1) - currentExp[16]);
				return;
			} else {
				rsi.disabledText = "Agility: " + currentLevel[16] + "/"
						+ maxLevel[16] + "\\nCurrent XP:\\r" + currentExp[16];
				return;
			}
		}
		if (contentType == 1009) {
			if (maxLevel[13] < 99) {
				rsi.disabledText = "Smithing: " + currentLevel[13] + "/"
						+ maxLevel[13] + "\\nCurrent XP:\\r" + currentExp[13]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[13] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[13] + 1) - currentExp[13]);
				return;
			} else {
				rsi.disabledText = "Smithing: " + currentLevel[13] + "/"
						+ maxLevel[13] + "\\nCurrent XP:\\r" + currentExp[13];
				return;
			}
		}
		if (contentType == 1010) {
			if (maxLevel[1] < 99) {
				rsi.disabledText = "Defence: " + currentLevel[1] + "/"
						+ maxLevel[1] + "\\nCurrent XP:\\r" + currentExp[1]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[1] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[1] + 1) - currentExp[1]);
				return;
			} else {
				rsi.disabledText = "Defence: " + currentLevel[1] + "/"
						+ maxLevel[1] + "\\nCurrent XP:\\r" + currentExp[1];
				return;
			}
		}
		if (contentType == 1011) {
			if (maxLevel[15] < 99) {
				rsi.disabledText = "Herblore: " + currentLevel[15] + "/"
						+ maxLevel[15] + "\\nCurrent XP:\\r" + currentExp[15]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[15] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[15] + 1) - currentExp[15]);
				return;
			} else {
				rsi.disabledText = "Herblore: " + currentLevel[15] + "/"
						+ maxLevel[15] + "\\nCurrent XP:\\r" + currentExp[15];
				return;
			}
		}
		if (contentType == 1012) {
			if (maxLevel[10] < 99) {
				rsi.disabledText = "Fishing: " + currentLevel[10] + "/"
						+ maxLevel[10] + "\\nCurrent XP:\\r" + currentExp[10]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[10] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[10] + 1) - currentExp[10]);
				return;
			} else {
				rsi.disabledText = "Fishing: " + currentLevel[10] + "/"
						+ maxLevel[10] + "\\nCurrent XP:\\r" + currentExp[10];
				return;
			}
		}
		if (contentType == 1013) {
			if (maxLevel[4] < 99) {
				rsi.disabledText = "Ranged: " + currentLevel[4] + "/"
						+ maxLevel[4] + "\\nCurrent XP:\\r" + currentExp[4]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[4] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[4] + 1) - currentExp[4]);
				return;
			} else {
				rsi.disabledText = "Ranged: " + currentLevel[4] + "/"
						+ maxLevel[4] + "\\nCurrent XP:\\r" + currentExp[4];
				return;
			}
		}
		if (contentType == 1014) {
			if (maxLevel[17] < 99) {
				rsi.disabledText = "Thieving: " + currentLevel[17] + "/"
						+ maxLevel[17] + "\\nCurrent XP:\\r" + currentExp[17]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[17] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[17] + 1) - currentExp[17]);
				return;
			} else {
				rsi.disabledText = "Thieving: " + currentLevel[17] + "/"
						+ maxLevel[17] + "\\nCurrent XP:\\r" + currentExp[17];
				return;
			}
		}
		if (contentType == 1015) {
			if (maxLevel[7] < 99) {
				rsi.disabledText = "Cooking: " + currentLevel[7] + "/"
						+ maxLevel[7] + "\\nCurrent XP:\\r" + currentExp[7]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[7] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[7] + 1) - currentExp[7]);
				return;
			} else {
				rsi.disabledText = "Cooking: " + currentLevel[7] + "/"
						+ maxLevel[7] + "\\nCurrent XP:\\r" + currentExp[7];
				return;
			}
		}
		if (contentType == 1016) {
			int max = (maxLevel[5] / 10), current = (currentLevel[5] / 10);
			if (max < 99) {
				rsi.disabledText = "Prayer: " + current + "/"
						+ max + "\\nCurrent XP:\\r" + currentExp[5]
						+ "\\nNext level:\\r" + getXPForLevel(max + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(max + 1) - currentExp[5]);
				return;
			} else {
				rsi.disabledText = "Prayer: " + current + "/"
						+ max + "\\nCurrent XP:\\r" + currentExp[5];
				return;
			}
		}
		if (contentType == 1017) {
			if (maxLevel[12] < 99) {
				rsi.disabledText = "Crafting: " + currentLevel[12] + "/"
						+ maxLevel[12] + "\\nCurrent XP:\\r" + currentExp[12]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[12] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[12] + 1) - currentExp[12]);
				return;
			} else {
				rsi.disabledText = "Crafting: " + currentLevel[12] + "/"
						+ maxLevel[12] + "\\nCurrent XP:\\r" + currentExp[12];
				return;
			}
		}
		if (contentType == 1018) {
			if (maxLevel[11] < 99) {
				rsi.disabledText = "Firemaking: " + currentLevel[11] + "/"
						+ maxLevel[11] + " \\nCurrent XP: \\r" + currentExp[11]
						+ "\\nNext level: \\r"
						+ getXPForLevel(maxLevel[11] + 1) + "\\nRemainder: \\r"
						+ (getXPForLevel(maxLevel[11] + 1) - currentExp[11]);
				return;
			} else {
				rsi.disabledText = "Firemaking:\\r" + currentLevel[11] + "/"
						+ maxLevel[11] + " \\nCurrent XP: \\r" + currentExp[11];
				return;
			}
		}
		if (contentType == 1019) {
			if (maxLevel[6] < 99) {
				rsi.disabledText = "Magic: " + currentLevel[6] + "/"
						+ maxLevel[6] + "\\nCurrent XP:\\r" + currentExp[6]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[6] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[6] + 1) - currentExp[6]);
				return;
			} else {
				rsi.disabledText = "Magic: " + currentLevel[6] + "/"
						+ maxLevel[6] + "\\nCurrent XP:\\r" + currentExp[6];
				return;
			}
		}
		if (contentType == 1020) {
			if (maxLevel[9] < 99) {
				rsi.disabledText = "Fletching: " + currentLevel[9] + "/"
						+ maxLevel[9] + "\\nCurrent XP:\\r" + currentExp[9]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[9] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[9] + 1) - currentExp[9]);
				return;
			} else {
				rsi.disabledText = "Fletching: " + currentLevel[6] + "/"
						+ maxLevel[6] + "\\nCurrent XP:\\r" + currentExp[9];
				return;
			}
		}
		if (contentType == 1021) {
			if (maxLevel[8] < 99) {
				rsi.disabledText = "Woodcutting: " + currentLevel[8] + "/"
						+ maxLevel[8] + " \\nCurrent XP: \\r" + currentExp[8]
						+ "\\nNext level: \\r" + getXPForLevel(maxLevel[8] + 1)
						+ "\\nRemainder: \\r"
						+ (getXPForLevel(maxLevel[8] + 1) - currentExp[8]);
				return;
			} else {
				rsi.disabledText = "Woodcutting: " + currentLevel[8] + "/"
						+ maxLevel[8] + " \\nCurrent XP: \\r" + currentExp[8];
				return;
			}
		}
		if (contentType == 1022) {
			if (maxLevel[20] < 99) {
				rsi.disabledText = "Runecrafting: " + currentLevel[20] + "/"
						+ maxLevel[20] + " \\nCurrent XP: \\r" + currentExp[20]
						+ "\\nNext level: \\r"
						+ getXPForLevel(maxLevel[20] + 1) + "\\nRemainder: \\r"
						+ (getXPForLevel(maxLevel[20] + 1) - currentExp[20]);
				return;
			} else {
				rsi.disabledText = "Runecrafting: " + currentLevel[20] + "/"
						+ maxLevel[20] + " \\nCurrent XP: \\r" + currentExp[20];
				return;
			}
		}
		if (contentType == 1023) {
			if (maxLevel[18] < 99) {
				rsi.disabledText = "Slayer: " + currentLevel[18] + "/"
						+ maxLevel[18] + "\\nCurrent XP:\\r" + currentExp[18]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[18] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[18] + 1) - currentExp[18]);
				return;
			} else {
				rsi.disabledText = "Slayer: " + currentLevel[18] + "/"
						+ maxLevel[18] + "\\nCurrent XP:\\r" + currentExp[18];
				return;
			}
		}
		if (contentType == 1024) {
			if (maxLevel[19] < 99) {
				rsi.disabledText = "Farming: " + currentLevel[19] + "/"
						+ maxLevel[19] + "\\nCurrent XP:\\r" + currentExp[19]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[19] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[19] + 1) - currentExp[19]);
				return;
			} else {
				rsi.disabledText = "Farming: " + currentLevel[19] + "/"
						+ maxLevel[19] + "\\nCurrent XP:\\r" + currentExp[19];
				return;
			}
		}
		if (contentType == 1025) {
			if (maxLevel[21] < 99) {
				rsi.disabledText = "Construction: " + currentLevel[21] + "/"
						+ maxLevel[21] + "\\nCurrent XP: \\r" + currentExp[21]
						+ "\\nNext level: \\r"
						+ getXPForLevel(maxLevel[21] + 1) + "\\nRemainder: \\r"
						+ (getXPForLevel(maxLevel[21] + 1) - currentExp[21]);
				return;
			} else {
				rsi.disabledText = "Construction: " + currentLevel[21] + "/"
						+ maxLevel[21] + " \\nCurrent XP: \\r" + currentExp[21];
				return;
			}
		}
		if (contentType == 1026) {
			if (maxLevel[22] < 99) {
				rsi.disabledText = "Hunter: " + currentLevel[22] + "/"
						+ maxLevel[22] + "\\nCurrent XP:\\r" + currentExp[22]
						+ "\\nNext level:\\r" + getXPForLevel(maxLevel[22] + 1)
						+ "\\nRemainder:\\r"
						+ (getXPForLevel(maxLevel[22] + 1) - currentExp[22]);
				return;
			} else {
				rsi.disabledText = "Hunter: " + currentLevel[22] + "/"
						+ maxLevel[22] + "\\nCurrent XP:\\r" + currentExp[22];
				return;
			}
		}
		if (contentType == 1027) {
			if (maxLevel[23] < 99) {
				rsi.disabledText = "Summoning: " + currentLevel[23] + "/"
						+ maxLevel[23] + " \\nCurrent XP: \\r" + currentExp[23]
						+ "\\nNext level: \\r"
						+ getXPForLevel(maxLevel[23] + 1) + "\\nRemainder: \\r"
						+ (getXPForLevel(maxLevel[23] + 1) - currentExp[23]);
				return;
			} else {
				rsi.disabledText = "Summoning: " + currentLevel[23] + "/"
						+ maxLevel[23] + " \\nCurrent XP: \\r" + currentExp[23];
				return;
			}
		}
		if (contentType == 1028) {
			if (maxLevel[24] < 120) {
				rsi.disabledText = "Dungeoneering: " + currentLevel[24] + "/"
						+ maxLevel[23] + " \\nCurrent XP: \\r" + currentExp[24]
						+ "\\nNext level: \\r"
						+ getXPForLevel(maxLevel[24] + 1) + "\\nRemainder: \\r"
						+ (getXPForLevel(maxLevel[24] + 1) - currentExp[24]);
				return;
			} else {
				rsi.disabledText = "Dungeoneering: " + currentLevel[24] + "/"
						+ maxLevel[24] + " \\nCurrent XP: \\r" + currentExp[24];
				return;
			}
		}
		if (contentType == 1029) {
			int totalLevel = 0, totalExperience = 0;
			for (int i = 0; i < maxLevel.length; i++) {
				int max = (i == 3 || i == 5) ? maxLevel[i] / 10 : maxLevel[i];
				totalLevel += max;
				totalExperience += currentExp[i];
			}
			rsi.disabledText = "Total Level: " + totalLevel + "\\nTotal XP: " + totalExperience;
		}
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor((double) lvl + 300.0
					* Math.pow(2.0, (double) lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public void replyToLastPM() {
		String name = null;
		for (int k = 0; k < 500; k++) {
			if (chatMessages[k] == null) {
				continue;
			}
			int l = chatTypes[k];
			if (l == 3 || l == 7) {
				name = chatNames[k];
				break;
			}
		}
		if (name == null || name.length() <= 0) {
			pushMessage(
					"",
					"You haven't received any messages to which you can reply.", 0);
			return;
		}
		if (name.contains("@")) {
			name = name.substring(5);
		}
		long nameAsLong = TextClass.longForName(name.trim());
		int k3 = -1;
		for (int i4 = 0; i4 < friendsCount; i4++) {
			if (friendsListNames[i4] != nameAsLong)
				continue;
			k3 = i4;
			break;
		}
		if (k3 != -1) {
			if (friendsNodeIDs[k3] > 0) {
				inputTaken = true;
				inputDialogState = 0;
				showInput = true;
				promptInput = "";
				friendsListAction = 3;
				aLong953 = friendsListNames[k3];
				promptMessage = "Enter message to send to " + friendsList[k3];
			} else {
				pushMessage("", "That player is currently offline.", 0);
			}
		}
	}

	public void drawSplitpublicChat() {
		if (splitpublicChat == 0)
			return;
		RSFont textDrawingArea = normalFont;
		int i = 0;
		if (systemUpdateTimer != 0)
			i = 1;
		for (int j = 0; j < 100; j++)
			if (chatMessages[j] != null) {
				int type = chatTypes[j];
				String name = chatNames[j];
				String prefixName = name;
				int rights = 0;
				if (name != null && name.indexOf("@") == 0) {
					name = name.substring(5);
					rights = getPrefixRights(prefixName.substring(0, prefixName.indexOf(name)));
				}
				if ((type == 3 || type == 7)
						&& (type == 7 || publicChatMode == 0 || publicChatMode == 1
								&& isFriendOrSelf(name))) {
					int l = (clientHeight - 174) - i * 13;
					int k1 = 4;
					textDrawingArea.method385(0, "From", l, k1);
					textDrawingArea.method385(getChatColor(), "From", l - 1, k1);
					k1 += textDrawingArea.getTextWidth("From ");
					modIcons[rights - 1].drawSprite(k1, l - 12);
					k1 += 12;
					textDrawingArea.method385(0, name + ": " + chatMessages[j], l,
							k1);
					textDrawingArea.method385(65535,
							name + ": " + chatMessages[j], l - 1, k1);
					if (++i >= 5)
						return;
				}
				if (type == 5 && publicChatMode < 2) {
					int i1 = (clientHeight - 174) - i * 13;
					textDrawingArea.method385(0, chatMessages[j], i1, 4);
					textDrawingArea
							.method385(65535, chatMessages[j], i1 - 1, 4);
					if (++i >= 5)
						return;
				}
				if (type == 6 && publicChatMode < 2) {
					int j1 = (clientHeight - 174) - i * 13;
					textDrawingArea.method385(0, "To " + name + ": "
							+ chatMessages[j], j1, 4);
					textDrawingArea.method385(getChatColor(), "To " + name + ": "
							+ chatMessages[j], j1 - 1, 4);
					if (++i >= 5)
						return;
				}
			}
	}

	public void sendMessage(String message) {
		pushMessage("@cr3@[Client]", message, 1);
	}

	public void pushMessage(String prefix, String message, int type) {
		if (type == 0 && dialogID != -1) {
			aString844 = message;
			super.clickMode3 = 0;
		}
		if (backDialogID == -1)
			inputTaken = true;
		for (int j = 499; j > 0; j--) {
			chatTypes[j] = chatTypes[j - 1];
			chatNames[j] = chatNames[j - 1];
			chatMessages[j] = chatMessages[j - 1];
			chatRights[j] = chatRights[j - 1];
		}
		chatTypes[0] = type;
		chatNames[0] = prefix;
		chatMessages[0] = message;
		chatRights[0] = rights;
	}

	public void setTab(int id) {
		needDrawTabArea = true;
		tabId = id;
		tabAreaAltered = true;
	}

	public int tabHover = -1;
	public boolean showTab = true;

	public void processNewTabArea() {
		int[] positionX = { 0, 30, 60, 90, 120, 150, 180, 210, 0, 30, 60, 90,
				120, 150, 180, 210 };
		int[] tab = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
		int offsetX = (clientWidth >= 900 ? clientWidth - 480
				: clientWidth - 240);
		int positionY = (clientWidth >= 900 ? clientHeight - 37
				: clientHeight - 74);
		int secondPositionY = clientHeight - 37;
		int secondOffsetX = clientWidth >= 900 ? 240 : 0;
		if (mouseInRegion(positionX[0] + offsetX, positionY,
				positionX[0] + offsetX + 30, positionY + 37)) {
			tabHover = tab[0];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX[1] + offsetX, positionY,
				positionX[1] + offsetX + 30, positionY + 37)) {
			tabHover = tab[1];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX[2] + offsetX, positionY,
				positionX[2] + offsetX + 30, positionY + 37)) {
			tabHover = tab[2];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX[3] + offsetX, positionY,
				positionX[3] + offsetX + 30, positionY + 37)) {
			tabHover = tab[3];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX[4] + offsetX, positionY,
				positionX[4] + offsetX + 30, positionY + 37)) {
			tabHover = tab[4];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX[5] + offsetX, positionY,
				positionX[5] + offsetX + 30, positionY + 37)) {
			tabHover = tab[5];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX[6] + offsetX, positionY,
				positionX[6] + offsetX + 30, positionY + 37)) {
			tabHover = tab[6];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX[7] + offsetX, positionY,
				positionX[7] + offsetX + 30, positionY + 37)) {
			tabHover = tab[7];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX[8] + offsetX + secondOffsetX,
				secondPositionY, positionX[8] + offsetX + secondOffsetX + 30,
				secondPositionY + 37)) {
			tabHover = tab[8];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX[9] + offsetX + secondOffsetX,
				secondPositionY, positionX[9] + offsetX + secondOffsetX + 30,
				secondPositionY + 37)) {
			tabHover = tab[9];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX[10] + offsetX + secondOffsetX,
				secondPositionY, positionX[10] + offsetX + secondOffsetX + 30,
				secondPositionY + 37)) {
			tabHover = tab[10];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX[11] + offsetX + secondOffsetX,
				secondPositionY, positionX[11] + offsetX + secondOffsetX + 30,
				secondPositionY + 37)) {
			tabHover = tab[11];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX[12] + offsetX + secondOffsetX,
				secondPositionY, positionX[12] + offsetX + secondOffsetX + 30,
				secondPositionY + 37)) {
			tabHover = tab[12];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX[13] + offsetX + secondOffsetX,
				secondPositionY, positionX[13] + offsetX + secondOffsetX + 30,
				secondPositionY + 37)) {
			tabHover = tab[13];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX[14] + offsetX + secondOffsetX,
				secondPositionY, positionX[14] + offsetX + secondOffsetX + 30,
				secondPositionY + 37)) {
			tabHover = tab[14];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX[15] + offsetX + secondOffsetX,
				secondPositionY, positionX[15] + offsetX + secondOffsetX + 30,
				secondPositionY + 37)) {
			tabHover = tab[15];
			needDrawTabArea = true;
			tabAreaAltered = true;
		} else {
			tabHover = -1;
			needDrawTabArea = true;
			tabAreaAltered = true;
		}
		if (super.clickMode3 == 1) {
			if (clickInRegion(positionX[0] + offsetX, positionY, positionX[0]
					+ offsetX + 30, positionY + 37)
					&& tabInterfaceIDs[tab[0]] != -1) {
				if (tabId == tab[0]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[0];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(positionX[1] + offsetX, positionY,
					positionX[1] + offsetX + 30, positionY + 37)
					&& tabInterfaceIDs[tab[1]] != -1) {
				if (tabId == tab[1]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[1];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(positionX[2] + offsetX, positionY,
					positionX[2] + offsetX + 30, positionY + 37)
					&& tabInterfaceIDs[tab[2]] != -1) {
				if (tabId == tab[2]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[2];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(positionX[3] + offsetX, positionY,
					positionX[3] + offsetX + 30, positionY + 37)
					&& tabInterfaceIDs[tab[3]] != -1) {
				if (tabId == tab[3]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[3];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(positionX[4] + offsetX, positionY,
					positionX[4] + offsetX + 30, positionY + 37)
					&& tabInterfaceIDs[tab[4]] != -1) {
				if (tabId == tab[4]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[4];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(positionX[5] + offsetX, positionY,
					positionX[5] + offsetX + 30, positionY + 37)
					&& tabInterfaceIDs[tab[5]] != -1) {
				if (tabId == tab[5]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[5];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(positionX[6] + offsetX, positionY,
					positionX[6] + offsetX + 30, positionY + 37)
					&& tabInterfaceIDs[tab[6]] != -1) {
				if (tabId == tab[6]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[6];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(positionX[7] + offsetX, positionY,
					positionX[7] + offsetX + 30, positionY + 37)
					&& tabInterfaceIDs[tab[7]] != -1) {
				if (tabId == tab[7]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[7];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(positionX[8] + offsetX + secondOffsetX,
					secondPositionY, positionX[8] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				if (tabId == tab[8]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[8];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(positionX[9] + offsetX + secondOffsetX,
					secondPositionY, positionX[9] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				if (tabId == tab[9]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[9];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(positionX[10] + offsetX + secondOffsetX,
					secondPositionY, positionX[10] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				if (tabId == tab[10]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[10];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(positionX[11] + offsetX + secondOffsetX,
					secondPositionY, positionX[11] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				if (tabId == tab[11]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[11];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(positionX[12] + offsetX + secondOffsetX,
					secondPositionY, positionX[12] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				if (tabId == tab[12]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[12];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(positionX[13] + offsetX + secondOffsetX,
					secondPositionY, positionX[13] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				if (tabId == tab[13]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[13];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(positionX[14] + offsetX + secondOffsetX,
					secondPositionY, positionX[14] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				if (tabId == tab[14]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[14];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(positionX[15] + offsetX + secondOffsetX,
					secondPositionY, positionX[15] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				if (tabId == tab[15]) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = tab[15];
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (clickInRegion(clientWidth - 21, 0, clientWidth, 21)) {
				if (tabId == 16) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
				tabId = 16;
				needDrawTabArea = true;
				tabAreaAltered = true;
			}
		}
	}

	public void processTabArea() {
		int positionX = clientWidth - 244;
		int positionY = 169, positionY2 = clientHeight - 36;
		if (mouseInRegion(positionX, positionY, positionX + 30,
				positionY + 36)) {
			needDrawTabArea = true;
			tabHover = 0;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX + 30, positionY, positionX + 60,
				positionY + 36)) {
			needDrawTabArea = true;
			tabHover = 1;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX + 60, positionY, positionX + 90,
				positionY + 36)) {
			needDrawTabArea = true;
			tabHover = 2;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX + 90, positionY, positionX + 120,
				positionY + 36)) {
			needDrawTabArea = true;
			tabHover = 3;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX + 120, positionY, positionX + 150,
				positionY + 36)) {
			needDrawTabArea = true;
			tabHover = 4;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX + 150, positionY, positionX + 180,
				positionY + 36)) {
			needDrawTabArea = true;
			tabHover = 5;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX + 180, positionY, positionX + 210,
				positionY + 36)) {
			needDrawTabArea = true;
			tabHover = 6;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX + 210, positionY, positionX + 240,
				positionY + 36)) {
			needDrawTabArea = true;
			tabHover = 7;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX, positionY2, positionX + 30,
				positionY2 + 36)) {
			needDrawTabArea = true;
			tabHover = 8;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX + 30, positionY2, positionX + 60,
				positionY2 + 36)) {
			needDrawTabArea = true;
			tabHover = 9;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX + 60, positionY2, positionX + 90,
				positionY2 + 36)) {
			needDrawTabArea = true;
			tabHover = 10;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX + 90, positionY2, positionX + 120,
				positionY2 + 36)) {
			needDrawTabArea = true;
			tabHover = 11;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX + 120, positionY2, positionX + 150,
				positionY2 + 36)) {
			needDrawTabArea = true;
			tabHover = 12;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX + 150, positionY2, positionX + 180,
				positionY2 + 36)) {
			needDrawTabArea = true;
			tabHover = 13;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX + 180, positionY2, positionX + 210,
				positionY2 + 36)) {
			needDrawTabArea = true;
			tabHover = 14;
			tabAreaAltered = true;
		} else if (mouseInRegion(positionX + 210, positionY2, positionX + 240,
				positionY2 + 36)) {
			needDrawTabArea = true;
			tabHover = 15;
			tabAreaAltered = true;
		} else {
			needDrawTabArea = true;
			tabHover = -1;
			tabAreaAltered = true;
		}
		positionX = clientWidth - 244;
		positionY = 169;
		if (super.clickMode3 == 1) {
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[0] != -1) {
				needDrawTabArea = true;
				tabId = 0;
				tabAreaAltered = true;
			}
			positionX += 30;
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[1] != -1) {
				needDrawTabArea = true;
				tabId = 1;
				tabAreaAltered = true;
			}
			positionX += 30;
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[2] != -1) {
				needDrawTabArea = true;
				tabId = 2;
				tabAreaAltered = true;
			}
			positionX += 30;
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[3] != -1) {
				needDrawTabArea = true;
				tabId = 3;
				tabAreaAltered = true;
			}
			positionX += 30;
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[4] != -1) {
				needDrawTabArea = true;
				tabId = 4;
				tabAreaAltered = true;
			}
			positionX += 30;
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[5] != -1) {
				needDrawTabArea = true;
				tabId = 5;
				tabAreaAltered = true;
			}
			positionX += 30;
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[6] != -1) {
				needDrawTabArea = true;
				tabId = 6;
				tabAreaAltered = true;
			}
			positionX += 30;
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[7] != -1) {
				needDrawTabArea = true;
				tabId = 7;
				tabAreaAltered = true;
			}
			positionX = clientWidth - 244;
			positionY = clientHeight - 36;
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[8] != -1) {
				needDrawTabArea = true;
				tabId = 8;
				tabAreaAltered = true;
			}
			positionX += 30;
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[9] != -1) {
				needDrawTabArea = true;
				tabId = 9;
				tabAreaAltered = true;
			}
			positionX += 30;
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[10] != -1) {
				needDrawTabArea = true;
				tabId = 10;
				tabAreaAltered = true;
			}
			positionX += 30;
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[11] != -1) {
				needDrawTabArea = true;
				tabId = 11;
				tabAreaAltered = true;
			}
			positionX += 30;
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[12] != -1) {
				needDrawTabArea = true;
				tabId = 12;
				tabAreaAltered = true;
			}
			positionX += 30;
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[13] != -1) {
				needDrawTabArea = true;
				tabId = 13;
				tabAreaAltered = true;
			}
			positionX += 30;
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[14] != -1) {
				needDrawTabArea = true;
				tabId = 14;
				tabAreaAltered = true;
			}
			positionX += 30;
			if (clickInRegion(positionX, positionY, positionX + 30,
					positionY + 36) && tabInterfaceIDs[15] != -1) {
				needDrawTabArea = true;
				tabId = 15;
				tabAreaAltered = true;
			}
		}
	}

	public void resetImageProducers2() {
		if (chatAreaIP != null)
			return;
		nullLoader();
		super.fullGameScreen = null;
		aRSImageProducer_1107 = null;
		aRSImageProducer_1108 = null;
		title = null;
		titleScreen = null;
		aRSImageProducer_1110 = null;
		aRSImageProducer_1111 = null;
		aRSImageProducer_1112 = null;
		aRSImageProducer_1113 = null;
		aRSImageProducer_1114 = null;
		aRSImageProducer_1115 = null;
		chatAreaIP = new RSImageProducer(519, 165, getGameComponent());
		mapAreaIP = new RSImageProducer(246, 168, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		cacheSprite[6].drawSprite(0, 0);
		tabAreaIP = new RSImageProducer(246, 335, getGameComponent());
		gameScreenIP = new RSImageProducer(clientSize == 0 ? 512 : clientWidth,
				clientSize == 0 ? 334 : clientHeight, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1123 = new RSImageProducer(496, 50, getGameComponent());
		aRSImageProducer_1124 = new RSImageProducer(269, 37, getGameComponent());
		aRSImageProducer_1125 = new RSImageProducer(249, 45, getGameComponent());
		welcomeScreenRaised = true;
	}

	public String getDocumentBaseHost() {
		if (signlink.mainapp != null) {
			return signlink.mainapp.getDocumentBase().getHost().toLowerCase();
		}
		return null;
	}

	public void drawScreenSprite(Sprite sprite, int j, int k) {
		int l = k * k + j * j;
		if (l > 4225 && l < 0x15f90) {
			int i1 = viewRotation + minimapRotation & 0x7ff;
			int j1 = Rasterizer.SINE[i1];
			int k1 = Rasterizer.COSINE[i1];
			j1 = (j1 * 256) / (minimapZoom + 256);
			k1 = (k1 * 256) / (minimapZoom + 256);
			int l1 = j * j1 + k * k1 >> 16;
			int i2 = j * k1 - k * j1 >> 16;
			double d = Math.atan2(l1, i2);
			int j2 = (int) (Math.sin(d) * 63D);
			int k2 = (int) (Math.cos(d) * 57D);
			mapEdge.method353(83 - k2 - 20, d, (94 + j2 + 4) - 10);
		} else {
			markMinimap(sprite, k, j);
		}
	}

	public boolean choosingLeftClick;
	public int leftClick;
	public String[] leftClickNames = { "Call Follower", "Dismiss", "Take BoB",
			"Renew Familiar", "Interact", "Attack", "Follower Details", "Cast"
	// "Follower Details", "Attack", "Interact", "Renew Familiar", "Take BoB",
	// "Dismiss", "Call Follower"
	};
	public int[] leftClickActions = { 1018, 1019, 1020, 1021, 1022, 1023, 1024,
			1026 };
	private boolean quickPrayers;

	public void rightClickMapArea() {
		if (mouseInRegion(clientWidth - (clientSize == 0 ? 249 : 217),
				clientSize == 0 ? 46 : 3, clientWidth
						- (clientSize == 0 ? 249 : 217) + 34,
				(clientSize == 0 ? 46 : 3) + 34)) {
			menuActionName[1] = "Reset counter";
			menuActionID[1] = 1013;
			menuActionName[2] = canGainXP ? "Pause counter" : "Resume counter";
			menuActionID[2] = 1007;
			menuActionName[3] = showBonus ? "Hide Bonus" : "Show Bonus";
			menuActionID[3] = 1030;
			menuActionName[4] = showXP ? "Hide counter" : "Show counter";
			menuActionID[4] = 1006;
			menuActionRow = 5;
		}
		if (mouseInRegion(clientSize == 0 ? clientWidth - 58 : getOrbX(1),
				getOrbY(1),
				(clientSize == 0 ? clientWidth - 58 : getOrbX(1)) + 57,
				getOrbY(1) + 34)) {
			String type = (tabInterfaceIDs[6] == 5608 || tabInterfaceIDs[6] == 21000) ? "prayers"
					: "curses";
			menuActionName[1] = "Reset quick " + type;
			menuActionID[1] = 1017;
			menuActionName[2] = "Select quick " + type;
			menuActionID[2] = 1015;
			menuActionName[3] = quickPrayers ? "Turn " + type + " off" : "Turn quick " + type + " on";
			menuActionID[3] = 1016;
			menuActionRow = 4;
		}
		if (mouseInRegion(clientSize == 0 ? clientWidth - 58 : getOrbX(2),
				getOrbY(2),
				(clientSize == 0 ? clientWidth - 58 : getOrbX(2)) + 57,
				getOrbY(2) + 34)) {
			menuActionName[2] = running ? "Turn run off" : "Turn run on";
			menuActionID[2] = 1014;
			menuActionName[1] = "Rest";
			menuActionID[1] = 1015;
			menuActionRow = 3;
		}
		if (mouseInRegion(clientSize == 0 ? clientWidth - 74 : getOrbX(3),
				getOrbY(3),
				(clientSize == 0 ? clientWidth - 74 : getOrbX(3)) + 57,
				getOrbY(3) + 34)) {
			if (leftClick != -1 && leftClick < 8) {
				menuActionName[1] = "Select left-click option";
				menuActionID[1] = 1027;
				menuActionName[2] = leftClickNames[leftClick].equals("Cast") ? leftClickNames[leftClick]
						+ " @gre@" + getFamiliar().getSpecialAttack()
						: leftClickNames[leftClick];
				menuActionID[2] = leftClickActions[leftClick];
				menuActionRow = 3;
			} else if (choosingLeftClick) {
				menuActionName[1] = "Select left-click option";
				menuActionID[1] = 1027;
				menuActionName[2] = "Call "
						+ (getFamiliar().isActive() ? getFamiliar().getName()
								: "Follower");
				menuActionID[2] = 1018;
				menuActionName[3] = "Dismiss";
				menuActionID[3] = 1019;
				menuActionName[4] = "Take BoB";
				menuActionID[4] = 1020;
				menuActionName[5] = "Renew Familiar";
				menuActionID[5] = 1021;
				menuActionName[6] = "Interact";
				menuActionID[6] = 1022;
				menuActionName[7] = "Attack";
				menuActionID[7] = 1023;
				if (getFamiliar().isActive()
						&& getFamiliar().getSpecialAttack().length() > 0) {
					menuActionName[8] = "Cast @gre@"
							+ getFamiliar().getSpecialAttack();
					menuActionID[8] = 1026;
					menuActionName[9] = "Follower Details";
					menuActionID[9] = 1024;
					menuActionRow = 10;
				} else {
					menuActionName[8] = "Follower Details";
					menuActionID[8] = 1024;
					menuActionRow = 9;
				}
			} else {
				menuActionName[1] = "Select left-click option";
				menuActionID[1] = 1027;
				menuActionRow = 2;
			}
		}
	}

	public void rightClickChatButtons() {
		int y = clientHeight - 503;
		int[] x = { 5, 62, 119, 176, 233, 290, 347, 404 };
		if (super.mouseX >= 7 && super.mouseX <= 23 && super.mouseY >= y + 345
				&& super.mouseY <= y + 361) {
			if (quickChat) {
				menuActionName[1] = "Close";
				menuActionID[1] = 1004;
				menuActionRow = 2;
			}
		} else if (super.mouseX >= 7
				&& super.mouseX <= normalFont.getTextWidth(myUsername) + 24
				&& super.mouseY >= clientHeight - 43
				&& super.mouseY <= clientHeight - 31) {
			if (!quickChat) {
				menuActionName[1] = "Open quickchat";
				menuActionID[1] = 1005;
				menuActionRow = 2;
			}
		} else if (super.mouseX >= x[0] && super.mouseX <= x[0] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			menuActionName[1] = "View All";
			menuActionID[1] = 999;
			menuActionRow = 2;
		} else if (super.mouseX >= x[1] && super.mouseX <= x[1] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			menuActionName[1] = "View Game";
			menuActionID[1] = 998;
			menuActionRow = 2;
		} else if (super.mouseX >= x[2] && super.mouseX <= x[2] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			menuActionName[1] = "Hide Public";
			menuActionID[1] = 997;
			menuActionName[2] = "Off Public";
			menuActionID[2] = 996;
			menuActionName[3] = "Friends Public";
			menuActionID[3] = 995;
			menuActionName[4] = "On Public";
			menuActionID[4] = 994;
			menuActionName[5] = "View Public";
			menuActionID[5] = 993;
			menuActionRow = 6;
		} else if (super.mouseX >= x[3] && super.mouseX <= x[3] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			menuActionName[1] = "Off public";
			menuActionID[1] = 992;
			menuActionName[2] = "Friends public";
			menuActionID[2] = 991;
			menuActionName[3] = "On public";
			menuActionID[3] = 990;
			menuActionName[4] = "View public";
			menuActionID[4] = 989;
			menuActionRow = 5;
		} else if (super.mouseX >= x[4] && super.mouseX <= x[4] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			menuActionName[1] = "Off Clan chat";
			menuActionID[1] = 1003;
			menuActionName[2] = "Friends Clan chat";
			menuActionID[2] = 1002;
			menuActionName[3] = "On Clan chat";
			menuActionID[3] = 1001;
			menuActionName[4] = "View Clan chat";
			menuActionID[4] = 1000;
			menuActionRow = 5;
		} else if (super.mouseX >= x[5] && super.mouseX <= x[5] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			menuActionName[1] = "Off Trade";
			menuActionID[1] = 987;
			menuActionName[2] = "Friends Trade";
			menuActionID[2] = 986;
			menuActionName[3] = "On Trade";
			menuActionID[3] = 985;
			menuActionName[4] = "View Trade";
			menuActionID[4] = 984;
			menuActionRow = 5;
		} else if (super.mouseX >= x[6] && super.mouseX <= x[6] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			menuActionName[1] = "Hide Assistance";
			menuActionID[1] = 1012;
			menuActionName[2] = "Off Assistance";
			menuActionID[2] = 1011;
			menuActionName[3] = "Friends Assistance";
			menuActionID[3] = 1010;
			menuActionName[4] = "On Assistance";
			menuActionID[4] = 1009;
			menuActionName[5] = "View Assistance";
			menuActionID[5] = 1008;
			menuActionRow = 6;
		}
	}

	public boolean canClick() {
		if (mouseInRegion(clientWidth - (clientWidth < 900 ? 240 : 480), clientHeight - (clientWidth < 900 ? 74 : 37), clientWidth, clientHeight)) {
			return false;
		}
		if (showChat) {
			if (super.mouseX > 0 && super.mouseX < 519
					&& super.mouseY > clientHeight - 165
					&& super.mouseY < clientHeight
					|| super.mouseX > clientWidth - 220
					&& super.mouseX < clientWidth && super.mouseY > 0
					&& super.mouseY < 165) {
				return false;
			}
		}
		if (mouseInRegion2(clientWidth - 216, clientWidth, 0, 172)) {
			return false;
		}
		if (showTab) {
			if (clientWidth >= 900) {
				if (super.mouseX >= clientWidth - 420
						&& super.mouseX <= clientWidth
						&& super.mouseY >= clientHeight - 37
						&& super.mouseY <= clientHeight
						|| super.mouseX > clientWidth - 204
						&& super.mouseX < clientWidth
						&& super.mouseY > clientHeight - 37 - 274
						&& super.mouseY < clientHeight)
					return false;
			} else {
				if (super.mouseX >= clientWidth - 210
						&& super.mouseX <= clientWidth
						&& super.mouseY >= clientHeight - 74
						&& super.mouseY <= clientHeight
						|| super.mouseX > clientWidth - 204
						&& super.mouseX < clientWidth
						&& super.mouseY > clientHeight - 74 - 274
						&& super.mouseY < clientHeight)
					return false;
			}
		}
		return true;
	}

	public void processRightClick() {
		if (activeInterfaceType != 0) {
			return;
		}
		menuActionName[0] = "Cancel";
		menuActionID[0] = 1107;
		menuActionRow = 1;
		if (fullscreenInterfaceID != -1) {
			anInt886 = 0;
			anInt1315 = 0;
			buildInterfaceMenu(8, 8, super.mouseX, super.mouseY, 0,
					RSInterface.interfaceCache[fullscreenInterfaceID]);
			if (anInt886 != anInt1026) {
				anInt1026 = anInt886;
			}
			if (anInt1315 != anInt1129) {
				anInt1129 = anInt1315;
			}
			return;
		}
		buildSplitpublicChatMenu();
		anInt886 = 0;
		anInt1315 = 0;
		if (!displayPreferences) {
			if (clientSize == 0) {
				if (super.mouseX > 4 && super.mouseY > 4 && super.mouseX < 516
						&& super.mouseY < 338) {
					if (openInterfaceID != -1) {
						buildInterfaceMenu(4, 4, super.mouseX, super.mouseY, 0,
								RSInterface.interfaceCache[openInterfaceID]);
					} else {
						build3dScreenMenu();
					}
				}
			} else if (!isFixed()) {
				if (canClick()) {
					if (super.mouseX > (clientWidth / 2) - 256
							&& super.mouseY > (clientHeight / 2) - 167
							&& super.mouseX < ((clientWidth / 2) + 256)
							&& super.mouseY < (clientHeight / 2) + 167
							&& openInterfaceID != -1) {
						buildInterfaceMenu((clientWidth / 2) - 256,
								(clientHeight / 2) - 167, super.mouseX,
								super.mouseY, 0,
								RSInterface.interfaceCache[openInterfaceID]);
					} else {
						build3dScreenMenu();
					}
				}
			}
		}
		if (anInt886 != anInt1026) {
			anInt1026 = anInt886;
		}
		if (anInt1315 != anInt1129) {
			anInt1129 = anInt1315;
		}
		anInt886 = 0;
		anInt1315 = 0;
		if (clientSize == 0) {
			if (super.mouseX > clientWidth - 218
					&& super.mouseY > clientHeight - 298
					&& super.mouseX < clientWidth - 25
					&& super.mouseY < clientHeight - 35) {
				if (invOverlayInterfaceID != -1) {
					buildInterfaceMenu(clientWidth - 218, clientHeight - 298,
							super.mouseX, super.mouseY, 0,
							RSInterface.interfaceCache[invOverlayInterfaceID]);
				} else if (tabInterfaceIDs[tabId] != -1) {
					buildInterfaceMenu(clientWidth - 218, clientHeight - 298,
							super.mouseX, super.mouseY, 0,
							RSInterface.interfaceCache[tabInterfaceIDs[tabId]]);
				}
			}
		} else {
			int y = clientWidth >= 900 ? 37 : 74;
			if (super.mouseX > clientWidth - 197
					&& super.mouseY > clientHeight - y - 267
					&& super.mouseX < clientWidth - 7
					&& super.mouseY < clientHeight - y - 7 && showTab) {
				if (invOverlayInterfaceID != -1) {
					buildInterfaceMenu(clientWidth - 197, clientHeight - y
							- 267, super.mouseX, super.mouseY, 0,
							RSInterface.interfaceCache[invOverlayInterfaceID]);
				} else if (tabInterfaceIDs[tabId] != -1) {
					buildInterfaceMenu(clientWidth - 197, clientHeight - y
							- 267, super.mouseX, super.mouseY, 0,
							RSInterface.interfaceCache[tabInterfaceIDs[tabId]]);
				}
			}
		}
		if (anInt886 != anInt1048) {
			needDrawTabArea = true;
			tabAreaAltered = true;
			anInt1048 = anInt886;
		}
		if (anInt1315 != anInt1044) {
			needDrawTabArea = true;
			tabAreaAltered = true;
			anInt1044 = anInt1315;
		}
		anInt886 = 0;
		anInt1315 = 0;
		if (super.mouseX > 0
				&& super.mouseY > (clientSize == 0 ? 338 : clientHeight - 165)
				&& super.mouseX < 490
				&& super.mouseY < (clientSize == 0 ? 463 : clientHeight - 40)
				&& showChat && inputDialogState != 3) {
			if (backDialogID != -1) {
				buildInterfaceMenu(20, (clientSize == 0 ? 358
						: clientHeight - 145), super.mouseX, super.mouseY, 0,
						RSInterface.interfaceCache[backDialogID]);
			} else if (super.mouseY < (clientSize == 0 ? 463
					: clientHeight - 40) && super.mouseX < 490) {
				buildChatAreaMenu(super.mouseY
						- (clientSize == 0 ? 338 : clientHeight - 165));
			}
		}
		if (backDialogID != -1 && anInt886 != anInt1039) {
			inputTaken = true;
			anInt1039 = anInt886;
		}
		if (backDialogID != -1 && anInt1315 != anInt1500) {
			inputTaken = true;
			anInt1500 = anInt1315;
		}
		alertHandler.processMouse(super.mouseX, super.mouseY);
		if (super.mouseX > 0 && super.mouseY > clientHeight - 165
				&& super.mouseX < 519 && super.mouseY < clientHeight)
			rightClickChatButtons();
		else if (super.mouseX > clientWidth - 249 && super.mouseY < 168)
			rightClickMapArea();
		boolean flag = false;
		while (!flag) {
			flag = true;
			for (int j = 0; j < menuActionRow - 1; j++) {
				if (menuActionID[j] < 1000 && menuActionID[j + 1] > 1000) {
					String name = menuActionName[j];
					menuActionName[j] = menuActionName[j + 1];
					menuActionName[j + 1] = name;
					int k = menuActionID[j];
					menuActionID[j] = menuActionID[j + 1];
					menuActionID[j + 1] = k;
					k = menuActionCmd2[j];
					menuActionCmd2[j] = menuActionCmd2[j + 1];
					menuActionCmd2[j + 1] = k;
					k = menuActionCmd3[j];
					menuActionCmd3[j] = menuActionCmd3[j + 1];
					menuActionCmd3[j + 1] = k;
					k = menuActionCmd1[j];
					menuActionCmd1[j] = menuActionCmd1[j + 1];
					menuActionCmd1[j + 1] = k;
					flag = false;
				}
			}
		}
	}

	public int method83(int i, int j, int k) {
		int l = 256 - k;
		return ((i & 0xff00ff) * l + (j & 0xff00ff) * k & 0xff00ff00)
				+ ((i & 0xff00) * l + (j & 0xff00) * k & 0xff0000) >> 8;
	}

	public void setNorth() {
		cameraOffsetX = 0;
		cameraOffsetY = 0;
		viewRotationOffset = 0;
		viewRotation = 0;
		minimapRotation = 0;
		minimapZoom = 0;
	}

	public int loginCode;

	public int messageState = 0;
	public int USERNAME = 1;
	public int PASSWORD = 2;
	public int BOTH = 3;

	/**
	 * Returns your formatted username.
	 * @return
	 */
	public String getUsername() {
		return TextUtils.fixName(myUsername);
	}

	/**
	 * Returns the hash for the specified password.
	 * @param password
	 * @return
	 */
	public String getPasswordHash(String password) {
		//return MD5.getHash(MD5.getHash(password));
		return password;
	}

	/**
	 * Sets the username to the formatted string.
	 * @param username
	 */
	public void setUsername(String username) {
		myUsername = TextUtils.fixName(username);
	}

	public void login(String username, String password, boolean flag, boolean saved) {
		username = TextUtils.fixName(username);
		signlink.errorname = username;
		if (username.length() == 0 && password.length() == 0 && !getCreate().available && !getRecovery().recovering) {
			loginMessage1 = "Please enter valid login details.";
			loginMessage2 = "";
			return;
		}
		if (username.length() == 0 && password.length() > 0 && !getCreate().available && !getRecovery().recovering) {
			String message = "Please enter a valid username.";
			if (loginMessage1.length() == 0 || messageState == USERNAME) {
				loginMessage1 = message;
				loginMessage2 = "";
				messageState = USERNAME;
			} else {
				loginMessage2 = message;
			}
			return;
		}
		if (password.length() == 0 && username.length() > 0 && !getCreate().available && !getRecovery().recovering) {
			String message = "Please enter a valid password.";
			if (loginMessage1.length() == 0 || messageState == PASSWORD) {
				loginMessage1 = message;
				loginMessage2 = "";
				messageState = PASSWORD;
			} else {
				loginMessage2 = message;
			}
			return;
		}
		if (password.length() < 5 && !saved && !getCreate().available && !getRecovery().recovering) {
			String message = "Your password is too short!";
			if (loginMessage1.length() == 0 || messageState == PASSWORD) {
				loginMessage1 = message;
				loginMessage2 = "";
				messageState = PASSWORD;
			} else {
				loginMessage2 = message;
			}
			return;
		}
		if (password.length() > 20 && !saved && !getCreate().available && !getRecovery().recovering) {
			String message = "Your password is too long!";
			if (loginMessage1.length() == 0 || messageState == PASSWORD) {
				loginMessage1 = message;
				loginMessage2 = "";
				messageState = PASSWORD;
			} else {
				loginMessage2 = message;
			}
			return;
		}
		try {
			if (!flag) {
				loginMessage1 = "";
				loginMessage2 = "";
				displayTitleScreen(true);
			}
			socketStream = new RSSocket(this, openSocket(Constants.HOST_PORT));
			long l = TextClass.longForName(username);
			int dummy = (int) (l >> 16 & 31L);
			stream.currentOffset = 0;
			stream.writeByte(14);
			stream.writeByte(dummy);
			socketStream.queueBytes(2, stream.buffer);
			for (int j = 0; j < 8; j++)
				socketStream.read();
			loginCode = socketStream.read();
			int i1 = loginCode;
			if (loginCode == 0) {
				socketStream.flushInputStream(packet.buffer, 8);
				packet.currentOffset = 0;
				aLong1215 = packet.readLong();
				int ai[] = new int[4];
				ai[0] = (int) (Math.random() * 99999999D);
				ai[1] = (int) (Math.random() * 99999999D);
				ai[2] = (int) (aLong1215 >> 32);
				ai[3] = (int) aLong1215;
				stream.currentOffset = 0;
				stream.writeByte(10);
				stream.writeInt(ai[0]);
				stream.writeInt(ai[1]);
				stream.writeInt(ai[2]);
				stream.writeInt(ai[3]);
				// stream.writeInt(signlink.uid);
				stream.writeInt(81597329);
				stream.writeString(username);
				stream.writeString(password);
				stream.writeByte(getCreate().created && loginScreenState == CREATE ? 1 : (getRecovery().recovering && loginScreenState == RECOVER ? 2 : 0));
				if (getCreate().created && loginScreenState == CREATE) {
					stream.writeString(getCreate().getEmail());
					stream.writeString(getCreate().getReferrer());
				}
				if (getRecovery().recovering && loginScreenState == RECOVER) {
					stream.writeString(getRecovery().getEmail());
				}
				stream.doKeys();
				aStream_847.currentOffset = 0;
				if (flag)
					aStream_847.writeByte(18);
				else
					aStream_847.writeByte(16);
				aStream_847.writeByte(stream.currentOffset + 36 + 1 + 1 + 2);
				aStream_847.writeByte(255);
				aStream_847.writeShort(317);
				aStream_847.writeByte(lowMemory ? 1 : 0);
				for (int l1 = 0; l1 < 9; l1++)
					aStream_847.writeInt(expectedCRCs[l1]);

				aStream_847.writeBytes(stream.buffer, stream.currentOffset, 0);
				stream.encryption = new ISAACRandomGen(ai);
				for (int j2 = 0; j2 < 4; j2++)
					ai[j2] += 50;

				encryption = new ISAACRandomGen(ai);
				socketStream.queueBytes(aStream_847.currentOffset,
						aStream_847.buffer);
				loginCode = socketStream.read();
			}
			if (loginCode != 23) {
				 getCreate().available = false;
			}
			if (loginCode == 26) {
				getRecovery().recovering = false;
			}
			if (loginCode == 1) {
				try {
					Thread.sleep(2000L);
				} catch (Exception _ex) {
				}
				login(username, password, flag, saved);
				return;
			}
			if (loginCode == 2) {
				Accounts.add(username, getPasswordHash(password), 1);
				Accounts.write();
				myUsername = username;
				myPassword = saved ? "" : password;
				myRank = socketStream.read();
				flagged = socketStream.read() == 1;
				scriptManager = null;
				titleScreenOffsets = null;
				aLong1220 = 0L;
				anInt1022 = 0;
				mouseDetection.coordsIndex = 0;
				super.awtFocus = true;
				aBoolean954 = true;
				loggedIn = true;
				stream.currentOffset = 0;
				packet.currentOffset = 0;
				packetId = -1;
				anInt841 = -1;
				anInt842 = -1;
				anInt843 = -1;
				packetSize = 0;
				anInt1009 = 0;
				systemUpdateTimer = 0;
				anInt1011 = 0;
				headIconType = 0;
				menuActionRow = 0;
				menuOpen = false;
				contextWidth = 0;
				contextHeight = 0;
				super.idleTime = 0;
				for (int j1 = 0; j1 < 500; j1++) {
					chatMessages[j1] = null;
				}
				consoleOpen = false;
				for (int index = 0; index < 17; index++) {
					consoleMessages[index] = "";
				}
				itemSelected = 0;
				spellSelected = 0;
				loadingStage = 0;
				currentSound = 0;
				setNorth();
				minimapState = 0;
				anInt985 = -1;
				destX = 0;
				destY = 0;
				playerCount = 0;
				npcCount = 0;
				for (int i2 = 0; i2 < maxPlayers; i2++) {
					playerArray[i2] = null;
					aStreamArray895s[i2] = null;
				}
				for (int k2 = 0; k2 < 16384; k2++)
					npcArray[k2] = null;
				myPlayer = playerArray[myPlayerIndex] = new Player();
				aClass19_1013.clear();
				aClass19_1056.clear();
				for (int l2 = 0; l2 < 4; l2++) {
					for (int i3 = 0; i3 < 104; i3++) {
						for (int k3 = 0; k3 < 104; k3++)
							groundEntity[l2][i3][k3] = null;
					}
				}
				aClass19_1179 = new Deque();
				fullscreenInterfaceID = -1;
				friendsListServerStatus = 0;
				friendsCount = 0;
				dialogID = -1;
				backDialogID = -1;
				openInterfaceID = -1;
				invOverlayInterfaceID = -1;
				walkableInterface = -1;
				aBoolean1149 = false;
				tabId = 4;
				inputDialogState = 0;
				menuOpen = false;
				contextWidth = 0;
				contextHeight = 0;
				showInput = false;
				aString844 = null;
				screenMultiIconId = 0;
				flashingSidebar = -1;
				aBoolean1047 = true;
				method45();
				for (int j3 = 0; j3 < 5; j3++)
					anIntArray990[j3] = 0;
				for (int l3 = 0; l3 < 5; l3++) {
					playerAction[l3] = null;
					atPlayerArray[l3] = false;
				}
				anInt1175 = 0;
				anInt1134 = 0;
				anInt986 = 0;
				currentWalkingQueueSize = 0;
				anInt924 = 0;
				anInt1188 = 0;
				anInt1155 = 0;
				anInt1226 = 0;
				applySettings();
				resetImageProducers2();
				// mainFrame.setTitle(CLIENT_NAME + " - " +
				// capitalize(myUsername));
				stream.createFrame(49);
				stream.writeByte(clientSize);
				return;
			}
			if (loginCode == 3) {
				loginMessage1 = "";
				loginMessage2 = "Invalid username or password.";
				return;
			}
			if (loginCode == 4) {
				loginMessage1 = "Your account has been disabled.";
				loginMessage2 = "Please check your message-center for details.";
				return;
			}
			if (loginCode == 5) {
				loginMessage1 = "Your account is already logged in.";
				loginMessage2 = "Try again in 60 seconds...";
				return;
			}
			if (loginCode == 6) {
				loginMessage1 = CLIENT_NAME + " has been updated!";
				loginMessage2 = "Please reload this page.";
				return;
			}
			if (loginCode == 7) {
				loginMessage1 = "This world is full.";
				loginMessage2 = "Please use a different world.";
				return;
			}
			if (loginCode == 8) {
				loginMessage1 = "Unable to connect.";
				loginMessage2 = "Login server offline.";
				return;
			}
			if (loginCode == 9) {
				loginMessage1 = "Login limit exceeded.";
				loginMessage2 = "Too many connections from your address.";
				return;
			}
			if (loginCode == 10) {
				loginMessage1 = "Unable to connect.";
				loginMessage2 = "Bad session id.";
				return;
			}
			if (loginCode == 11) {
				loginMessage1 = "Login server rejected session.";
				loginMessage2 = "Please try again.";
				return;
			}
			if (loginCode == 12) {
				loginMessage1 = "You need a members account to login to this world.";
				loginMessage2 = "Please subscribe, or use a different world.";
				return;
			}
			if (loginCode == 13) {
				loginMessage1 = "Could not complete login.";
				loginMessage2 = "Please try using a different world.";
				return;
			}
			if (loginCode == 14) {
				loginMessage1 = "The server is being updated.";
				loginMessage2 = "Please wait 1 minute and try again.";
				return;
			}
			if (loginCode == 15) {
				loggedIn = true;
				stream.currentOffset = 0;
				packet.currentOffset = 0;
				packetId = -1;
				anInt841 = -1;
				anInt842 = -1;
				anInt843 = -1;
				packetSize = 0;
				anInt1009 = 0;
				systemUpdateTimer = 0;
				menuActionRow = 0;
				menuOpen = false;
				contextWidth = 0;
				contextHeight = 0;
				aLong824 = System.currentTimeMillis();
				return;
			}
			if (loginCode == 16) {
				loginMessage1 = "Login attempts exceeded.";
				loginMessage2 = "Please wait 1 minute and try again.";
				return;
			}
			if (loginCode == 17) {
				loginMessage1 = "You are standing packet a members-only area.";
				loginMessage2 = "To play on this world move to a free area first";
				return;
			}
			if (loginCode == 20) {
				loginMessage1 = "Invalid loginserver requested";
				loginMessage2 = "Please try using a different world.";
				return;
			}
			if (loginCode == 21) {
				for (int k1 = socketStream.read(); k1 >= 0; k1--) {
					loginMessage1 = "You have only just left another world";
					loginMessage2 = "Your profile will be transferred packet: "
							+ k1 + " seconds";
					displayTitleScreen(true);
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}

				login(username, password, flag, saved);
				return;
			}
			if (loginCode == 22) {
				loginMessage1 = "Unabled to connect to the server.";
				loginMessage2 = "Please use your forum username & password.";
				return;
			}
			if(loginCode == 22) {
				getCreate().verified[0] = false;
				getCreate().usernameError = new String[]{ "This username is not available." };
				return;
			}
			if(loginCode == 23) {
				getCreate().available = true;
				loginMessage1 = "This username is available!";
				loginMessage2 = "Click here to create your account.";
				return;
			}
			if(loginCode == 24) {
				getRecovery().verified[0] = false;
				getRecovery().usernameError = new String[]{ "The account you're trying to recover", "does not exist!" };
				loginMessage1 = "This account does not exist!";
				loginMessage2 = "";
				return;
			}
			if(loginCode == 25) {
				getRecovery().verified[1] = false;
				getRecovery().emailError = new String[]{ "The email entered does not match", "the recovery address on file." };
				loginMessage1 = "The email you entered is not correct.";
				loginMessage2 = "Please try again.";
				return;
			}
			if(loginCode == 26) {
				loginMessage1 = "An email has been dispatched to the";
				loginMessage2 = "recovery address, please check your email.";
				return;
			}
			if (loginCode == -1) {
				if (i1 == 0) {
					if (loginFailures < 2) {
						try {
							Thread.sleep(2000L);
						} catch (Exception _ex) {
						}
						loginFailures++;
						login(username, password, flag, saved);
						return;
					} else {
						loginMessage1 = "Unabled to connect to the server.";
						loginMessage2 = "Please use your forum username & password.";
						return;
					}
				} else {
					loginMessage1 = "Unabled to connect to the server.";
					loginMessage2 = "Please use your forum username & password.";
					return;
				}
			} else {
				System.out.println("response:" + loginCode);
				loginMessage1 = "Unexpected server response";
				loginMessage2 = "Please try using a different world.";
				return;
			}
		} catch (IOException _ex) {
			loginMessage1 = "Unable to connect to the server.";
			loginMessage2 = "Please visit the forums for help.";
		}
	}

	/**
	 * Refactored breadth first search algorithm path-finding.
	 * All credits to <url>http://www.rune-server.org/members/Maxi</url>.
	 */
	public boolean calculatePath(int click_type, int object_rotation, int object_size_y, int object_type, int from_local_y, int object_size_x,
			int object_face, int to_local_y, int from_local_x, boolean arbitrary_destination, int to_local_x) {
		byte map_size_x = 104;
		byte map_size_y = 104;
		for (int x = 0; x < map_size_x; x++) {
			for (int y = 0; y < map_size_y; y++) {
				wayPoints[x][y] = 0;
				distanceValues[x][y] = 0x5f5e0ff;
			}
		}
		int current_x = from_local_x;
		int current_y = from_local_y;
		wayPoints[from_local_x][from_local_y] = 99;
		distanceValues[from_local_x][from_local_y] = 0;
		int next_index = 0;
		int current_index = 0;
		walkingQueueX[next_index] = from_local_x;
		walkingQueueY[next_index++] = from_local_y;
		boolean found_destination = false;
		int max_path_size = walkingQueueX.length;
		int clipping_flags[][] = clippingPlanes[plane].clip;
		while (current_index != next_index) {
			current_x = walkingQueueX[current_index];
			current_y = walkingQueueY[current_index];
			current_index = (current_index + 1) % max_path_size;
			if (current_x == to_local_x && current_y == to_local_y) {
				found_destination = true;
				break;
			}
			if (object_type != 0) {
				if ((object_type < 5 || object_type == 10) 
						&& clippingPlanes[plane].reachedWall(to_local_x, current_x, current_y, object_rotation, object_type - 1, to_local_y)) {
					found_destination = true;
					break;
				}
				if (object_type < 10 
						&& clippingPlanes[plane].reachedDecoration(to_local_x, to_local_y, current_y, object_type - 1, object_rotation, current_x)) {
					found_destination = true;
					break;
				}
			}
			if (object_size_x != 0 && object_size_y != 0 
					&& clippingPlanes[plane].reachedFacingObject(to_local_y, to_local_x, current_x, object_size_y, object_face, object_size_x, current_y)) {
				found_destination = true;
				break;
			}
			int new_distance_value = distanceValues[current_x][current_y] + 1;
			if (current_x > 0 && wayPoints[current_x - 1][current_y] == 0 
					&& (clipping_flags[current_x - 1][current_y] & 0x1280108) == 0) {
				walkingQueueX[next_index] = current_x - 1;
				walkingQueueY[next_index] = current_y;
				next_index = (next_index + 1) % max_path_size;
				wayPoints[current_x - 1][current_y] = 2;
				distanceValues[current_x - 1][current_y] = new_distance_value;
			}
			if (current_x < map_size_x - 1 && wayPoints[current_x + 1][current_y] == 0 
					&& (clipping_flags[current_x + 1][current_y] & 0x1280180) == 0) {
				walkingQueueX[next_index] = current_x + 1;
				walkingQueueY[next_index] = current_y;
				next_index = (next_index + 1) % max_path_size;
				wayPoints[current_x + 1][current_y] = 8;
				distanceValues[current_x + 1][current_y] = new_distance_value;
			}
			if (current_y > 0 && wayPoints[current_x][current_y - 1] == 0 
					&& (clipping_flags[current_x][current_y - 1] & 0x1280102) == 0) {
				walkingQueueX[next_index] = current_x;
				walkingQueueY[next_index] = current_y - 1;
				next_index = (next_index + 1) % max_path_size;
				wayPoints[current_x][current_y - 1] = 1;
				distanceValues[current_x][current_y - 1] = new_distance_value;
			}
			if (current_y < map_size_y - 1 && wayPoints[current_x][current_y + 1] == 0 
					&& (clipping_flags[current_x][current_y + 1] & 0x1280120) == 0) {
				walkingQueueX[next_index] = current_x;
				walkingQueueY[next_index] = current_y + 1;
				next_index = (next_index + 1) % max_path_size;
				wayPoints[current_x][current_y + 1] = 4;
				distanceValues[current_x][current_y + 1] = new_distance_value;
			}
			if (current_x > 0 && current_y > 0 && wayPoints[current_x - 1][current_y - 1] == 0 
					&& (clipping_flags[current_x - 1][current_y - 1] & 0x128010e) == 0 
					&& (clipping_flags[current_x - 1][current_y] & 0x1280108) == 0 
					&& (clipping_flags[current_x][current_y - 1] & 0x1280102) == 0) {
				walkingQueueX[next_index] = current_x - 1;
				walkingQueueY[next_index] = current_y - 1;
				next_index = (next_index + 1) % max_path_size;
				wayPoints[current_x - 1][current_y - 1] = 3;
				distanceValues[current_x - 1][current_y - 1] = new_distance_value;
			}
			if (current_x < map_size_x - 1 && current_y > 0 && wayPoints[current_x + 1][current_y - 1] == 0 
					&& (clipping_flags[current_x + 1][current_y - 1] & 0x1280183) == 0 
					&& (clipping_flags[current_x + 1][current_y] & 0x1280180) == 0 
					&& (clipping_flags[current_x][current_y - 1] & 0x1280102) == 0) {
				walkingQueueX[next_index] = current_x + 1;
				walkingQueueY[next_index] = current_y - 1;
				next_index = (next_index + 1) % max_path_size;
				wayPoints[current_x + 1][current_y - 1] = 9;
				distanceValues[current_x + 1][current_y - 1] = new_distance_value;
			}
			if (current_x > 0 && current_y < map_size_y - 1 && wayPoints[current_x - 1][current_y + 1] == 0 
					&& (clipping_flags[current_x - 1][current_y + 1] & 0x1280138) == 0 
					&& (clipping_flags[current_x - 1][current_y] & 0x1280108) == 0 
					&& (clipping_flags[current_x][current_y + 1] & 0x1280120) == 0) {
				walkingQueueX[next_index] = current_x - 1;
				walkingQueueY[next_index] = current_y + 1;
				next_index = (next_index + 1) % max_path_size;
				wayPoints[current_x - 1][current_y + 1] = 6;
				distanceValues[current_x - 1][current_y + 1] = new_distance_value;
			}
			if (current_x < map_size_x - 1 && current_y < map_size_y - 1 && wayPoints[current_x + 1][current_y + 1] == 0 
					&& (clipping_flags[current_x + 1][current_y + 1] & 0x12801e0) == 0 
					&& (clipping_flags[current_x + 1][current_y] & 0x1280180) == 0 
					&& (clipping_flags[current_x][current_y + 1] & 0x1280120) == 0) {
				walkingQueueX[next_index] = current_x + 1;
				walkingQueueY[next_index] = current_y + 1;
				next_index = (next_index + 1) % max_path_size;
				wayPoints[current_x + 1][current_y + 1] = 12;
				distanceValues[current_x + 1][current_y + 1] = new_distance_value;
			}
		}
		arbitraryDestination = 0;
		if (!found_destination) {
			if (arbitrary_destination) {
				int max_steps_non_inclusive = 100;
				for (int deviation = 1; deviation < 2; deviation++) {
					for (int x_deviation = to_local_x - deviation; x_deviation <= to_local_x + deviation; x_deviation++) {
						for (int y_deviation = to_local_y - deviation; y_deviation <= to_local_y + deviation; y_deviation++)
							if (x_deviation >= 0 && y_deviation >= 0 && x_deviation < 104 && y_deviation < 104 
							&& distanceValues[x_deviation][y_deviation] < max_steps_non_inclusive) {
								max_steps_non_inclusive = distanceValues[x_deviation][y_deviation];
								current_x = x_deviation;
								current_y = y_deviation;
								arbitraryDestination = 1;
								found_destination = true;
							}
					}
					if (found_destination)
						break;
				}
			}
			if (!found_destination)
				return false;
		}
		current_index = 0;
		walkingQueueX[current_index] = current_x;
		walkingQueueY[current_index++] = current_y;
		int initial_skip_check;
		for (int waypoint = initial_skip_check = wayPoints[current_x][current_y]; 
				current_x != from_local_x || current_y != from_local_y; waypoint = wayPoints[current_x][current_y]) {
			if (waypoint != initial_skip_check) {
				initial_skip_check = waypoint;
				walkingQueueX[current_index] = current_x;
				walkingQueueY[current_index++] = current_y;
			}
			if ((waypoint & 2) != 0)
				current_x++;
			else if ((waypoint & 8) != 0)
				current_x--;
			if ((waypoint & 1) != 0)
				current_y++;
			else if ((waypoint & 4) != 0)
				current_y--;
		}
		if (current_index > 0) {
			int traverse_index = current_index;
			if (traverse_index > 25)
				traverse_index = 25;
			current_index--;
			int local_x = walkingQueueX[current_index];
			int local_y = walkingQueueY[current_index];
			currentWalkingQueueSize += traverse_index;
			if (currentWalkingQueueSize >= 92) {
				stream.createFrame(36);
				stream.writeInt(0);
				currentWalkingQueueSize = 0;
			}
			if (click_type == 0) { // minimap click
				stream.createFrame(164);
				stream.writeByte(traverse_index + traverse_index + 3);
			}
			if (click_type == 1) { // main screen click
				stream.createFrame(248);
				stream.writeByte(traverse_index + traverse_index + 3 + 14);
			}
			if (click_type == 2) { // object click
				stream.createFrame(98);
				stream.writeByte(traverse_index + traverse_index + 3);
			}
			stream.writeLEShortA(local_x + regionAbsBaseX);
			destX = walkingQueueX[0];
			destY = walkingQueueY[0];
			for (int j7 = 1; j7 < traverse_index; j7++) {
				current_index--;
				stream.writeByte(walkingQueueX[current_index] - local_x);
				stream.writeByte(walkingQueueY[current_index] - local_y);
			}
			stream.writeLEShort(local_y + regionAbsBaseY);
			stream.writeByteC(super.keyArray[5] != 1 ? 0 : 1);
			return true;
		}
		return click_type != 1;
	}

	public void updateNpcBlocks(JagexBuffer stream) {
		for (int j = 0; j < anInt893; j++) {
			int k = anIntArray894[j];
			NPC npc = npcArray[k];
			int l = stream.readUnsignedByte();
			if ((l & 0x10) != 0) {
				int i1 = stream.readLEShort();
				if (i1 == 65535)
					i1 = -1;
				int i2 = stream.readUnsignedByte();
				if (i1 == npc.animationId && i1 != -1) {
					int l2 = Animation.anims[i1].anInt365;
					if (l2 == 1) {
						npc.anInt1527 = 0;
						npc.anInt1528 = 0;
						npc.animationDelay = i2;
						npc.anInt1530 = 0;
					}
					if (l2 == 2)
						npc.anInt1530 = 0;
				} else if (i1 == -1
						|| npc.animationId == -1
						|| Animation.anims[i1].anInt359 >= Animation.anims[npc.animationId].anInt359) {
					npc.animationId = i1;
					npc.anInt1527 = 0;
					npc.anInt1528 = 0;
					npc.animationDelay = i2;
					npc.anInt1530 = 0;
					npc.anInt1542 = npc.smallXYIndex;
				}
			}
			if ((l & 8) != 0) { //single hit
				int damage = stream.readLEShortA();
				stream.readLEShortA();
				int icon = -1;
				String attacker = null;
				boolean critical = false;
				icon = stream.readByte();
				attacker = stream.readString();
				int type = stream.readByte();
				npc.updateHitData(type, icon, damage, 0, critical, attacker, null, loopCycle);
				npc.loopCycleStatus = loopCycle + 300;
				npc.currentHealth = stream.readLEShortA();
				npc.maxHealth = stream.readLEShortA();
			}
			if ((l & 0x80) != 0) {
				npc.graphicId = stream.readUnsignedShort();
				int delay = stream.readInt();
				npc.graphicHeight = delay >> 16;
				npc.graphicDelay = loopCycle + (delay & 0xffff);
				npc.anInt1521 = 0;
				npc.anInt1522 = 0;
				if (npc.graphicDelay > loopCycle)
					npc.anInt1521 = -1;
				if (npc.graphicId == 65535)
					npc.graphicId = -1;
			}
			if ((l & 0x20) != 0) {
				npc.interactingEntity = stream.readUnsignedShort();
				if (npc.interactingEntity == 65535)
					npc.interactingEntity = -1;
			}
			if ((l & 1) != 0) {
				npc.textSpoken = stream.readString();
				npc.textCycle = 100;
			}
			if ((l & 0x40) != 0) {
				int damage = stream.readUnsignedShort();
				stream.readUnsignedShort();
				boolean max = false;
				int icon = -1;
				String attacker = null;
				max = false;
				icon = stream.readByte();
				attacker = stream.readString();
				int type = stream.readByte();
				npc.updateHitData(type, icon, damage, 0, max, attacker, null, loopCycle);
				npc.loopCycleStatus = loopCycle + 300;
				npc.currentHealth = stream.readUnsignedShort();
				npc.maxHealth = stream.readUnsignedShort();
			}
			if ((l & 2) != 0) {
				npc.id_2 = stream.readLEShortA();
				NPCDefinition def = npc.getDefinition();
				npc.size = def.size;
				npc.turnSpeed = def.turnSpeed;
				npc.walkAnimation = def.walkAnimation;
				npc.reverseAnimation = def.reverseAnimation;
				npc.turnLeftAnimation = def.turnLeftAnimation;
				npc.turnRightAnimation = def.turnRightAnimation;
				npc.standAnimation = def.standAnimation;
				npc.runAnimation = def.runAnimation;
			}
			if ((l & 4) != 0) {
				npc.facePositionX = stream.readLEShort();
				npc.facePositionY = stream.readLEShort();
			}
		}
	}

	public void buildAtNPCMenu(NPCDefinition entityDef, int i, int j, int k) {
		if (menuActionRow >= 400)
			return;
		if (entityDef.childrenIDs != null)
			entityDef = entityDef.method161();
		if (entityDef == null)
			return;
		if (!entityDef.aBoolean84)
			return;
		String s = entityDef.name;
		if (entityDef.combatLevel != 0)
			s = s
					+ combatDiffColor(myPlayer.combatLevel,
							entityDef.combatLevel) + " (level-"
					+ entityDef.combatLevel + ")";
		if (itemSelected == 1) {
			menuActionName[menuActionRow] = "Use " + selectedItemName
					+ " with @yel@" + s;
			menuActionID[menuActionRow] = 582;
			menuActionCmd1[menuActionRow] = i;
			menuActionCmd2[menuActionRow] = k;
			menuActionCmd3[menuActionRow] = j;
			menuActionRow++;
			return;
		}
		if (spellSelected == 1) {
			if ((spellUsableOn & 2) == 2) {
				menuActionName[menuActionRow] = spellTooltip + " @yel@" + s;
				menuActionID[menuActionRow] = 413;
				menuActionCmd1[menuActionRow] = i;
				menuActionCmd2[menuActionRow] = k;
				menuActionCmd3[menuActionRow] = j;
				menuActionRow++;
			}
		} else {
			if (entityDef.actions != null) {
				for (int l = 4; l >= 0; l--)
					if (entityDef.actions[l] != null
							&& !entityDef.actions[l].equalsIgnoreCase("attack")) {
						menuActionName[menuActionRow] = entityDef.actions[l]
								+ " @yel@" + s;
						if (l == 0)
							menuActionID[menuActionRow] = 20;
						if (l == 1)
							menuActionID[menuActionRow] = 412;
						if (l == 2)
							menuActionID[menuActionRow] = 225;
						if (l == 3)
							menuActionID[menuActionRow] = 965;
						if (l == 4)
							menuActionID[menuActionRow] = 478;
						menuActionCmd1[menuActionRow] = i;
						menuActionCmd2[menuActionRow] = k;
						menuActionCmd3[menuActionRow] = j;
						menuActionRow++;
					}

			}
			if (entityDef.actions != null) {
				for (int i1 = 4; i1 >= 0; i1--)
					if (entityDef.actions[i1] != null
							&& entityDef.actions[i1].equalsIgnoreCase("attack")) {
						char c = '\0';
						if (entityDef.combatLevel > myPlayer.combatLevel)
							c = '\u07D0';
						menuActionName[menuActionRow] = entityDef.actions[i1]
								+ " @yel@" + s;
						if (i1 == 0)
							menuActionID[menuActionRow] = 20 + c;
						if (i1 == 1)
							menuActionID[menuActionRow] = 412 + c;
						if (i1 == 2)
							menuActionID[menuActionRow] = 225 + c;
						if (i1 == 3)
							menuActionID[menuActionRow] = 965 + c;
						if (i1 == 4)
							menuActionID[menuActionRow] = 478 + c;
						menuActionCmd1[menuActionRow] = i;
						menuActionCmd2[menuActionRow] = k;
						menuActionCmd3[menuActionRow] = j;
						menuActionRow++;
					}

			}
			// menuActionName[menuActionRow] = "Examine @yel@" + s +
			// " @gre@(@whi@" + entityDef.type + "@gre@)";
			menuActionName[menuActionRow] = "Examine @yel@" + s;
			menuActionID[menuActionRow] = 1025;
			menuActionCmd1[menuActionRow] = i;
			menuActionCmd2[menuActionRow] = k;
			menuActionCmd3[menuActionRow] = j;
			menuActionRow++;
		}
	}
	public String getRank(int i){
		String s = "";
		switch(i){
			case 1:
			return "Lord";
			case 2:
			return "Sir";
			case 3:
			return "Lionheart";
			case 4:
			return "Desperado";
			case 5:
			return "Bandito";
			case 6:
			return "King";
			case 7:
			return "Pking Master";
			case 8:
			return "Wunderkind";
			case 9:
			return "Crusader";
			case 10:
			return "Overlord";
			case 11:
			return "Bigwig";
			case 12:
			return "Count";
			case 13:
			return "Warrior";
			case 14:
			return "Hell Raiser";
			case 15:
			return "Baron";
			case 16:
			return "Duke";
			case 17:
			return "Lady";
			case 18:
			return "Dame";
			case 19:
			return "Fucking Pro";
			case 20:
			return "Boss";
			case 21:
			return "Countess";
			case 22:
			return "Pker";
			case 23:
			return "Duchess";
			case 24:
			return "Queen";
		}
		return s;
	}

	public void buildAtPlayerMenu(int i, int j, Player player, int k) {
		if (player == myPlayer)
			return;
		if (menuActionRow >= 400)
			return;
		String s;
		if (player.title == 0) {
			s = player.name + combatDiffColor(myPlayer.combatLevel, player.combatLevel) + " (level-" + player.combatLevel + ")";
		} else {
			s = "@or2@" + getRank(player.title) + "@whi@ " + player.name + combatDiffColor(myPlayer.combatLevel, player.combatLevel) + " (level-" + player.combatLevel + ")";
		}
		//System.out.println(getRank(player.skill));
		if (player.rights == 0) {
			s = "@cr1@" + s;
		} else if (player.rights == 2) {
			s = "@cr2@" + s;
		} else if (player.rights == 3) {
			s = "@cr3@" + s;
		}
		if (itemSelected == 1) {
			menuActionName[menuActionRow] = "Use " + selectedItemName
					+ " with @whi@" + s;
			menuActionID[menuActionRow] = 491;
			menuActionCmd1[menuActionRow] = j;
			menuActionCmd2[menuActionRow] = i;
			menuActionCmd3[menuActionRow] = k;
			menuActionRow++;
		} else if (spellSelected == 1) {
			if ((spellUsableOn & 8) == 8) {
				menuActionName[menuActionRow] = spellTooltip + " @whi@" + s;
				menuActionID[menuActionRow] = 365;
				menuActionCmd1[menuActionRow] = j;
				menuActionCmd2[menuActionRow] = i;
				menuActionCmd3[menuActionRow] = k;
				menuActionRow++;
			}
		} else {
			for (int l = 4; l >= 0; l--)
				if (playerAction[l] != null) {
					menuActionName[menuActionRow] = playerAction[l]
							+ " @whi@" + s;
					char c = '\0';
					if (playerAction[l].equalsIgnoreCase("attack")) {
						if (player.combatLevel > myPlayer.combatLevel)
							c = '\u07D0';
						if (myPlayer.team != 0 && player.team != 0)
							if (myPlayer.team == player.team)
								c = '\u07D0';
							else
								c = '\0';
					} else if (atPlayerArray[l])
						c = '\u07D0';
					if (l == 0)
						menuActionID[menuActionRow] = 561 + c;
					if (l == 1)
						menuActionID[menuActionRow] = 779 + c;
					if (l == 2)
						menuActionID[menuActionRow] = 27 + c;
					if (l == 3)
						menuActionID[menuActionRow] = 577 + c;
					if (l == 4)
						menuActionID[menuActionRow] = 729 + c;
					menuActionCmd1[menuActionRow] = j;
					menuActionCmd2[menuActionRow] = i;
					menuActionCmd3[menuActionRow] = k;
					menuActionRow++;
				}
		}
		for (int i1 = 0; i1 < menuActionRow; i1++) {
			if (menuActionID[i1] == 516) {
				menuActionName[i1] = "Walk here @whi@" + s;
				return;
			}
		}
	}

	public void method89(ObjectSpawnNode class30_sub1) {
		int i = 0;
		int j = -1;
		int k = 0;
		int l = 0;
		if (class30_sub1.type == 0)
			i = landscapeScene.method300(class30_sub1.height,
					class30_sub1.x, class30_sub1.y);
		if (class30_sub1.type == 1)
			i = landscapeScene.method301(class30_sub1.height,
					class30_sub1.x, class30_sub1.y);
		if (class30_sub1.type == 2)
			i = landscapeScene.method302(class30_sub1.height,
					class30_sub1.x, class30_sub1.y);
		if (class30_sub1.type == 3)
			i = landscapeScene.method303(class30_sub1.height,
					class30_sub1.x, class30_sub1.y);
		if (i != 0) {
			int i1 = landscapeScene.getObject(class30_sub1.height,
					class30_sub1.x, class30_sub1.y, i);
			j = i >> 14 & 0x7fff;
			k = i1 & 0x1f;
			l = i1 >> 6;
		}
		class30_sub1.anInt1299 = j;
		class30_sub1.anInt1301 = k;
		class30_sub1.anInt1300 = l;
	}

	public final void method90() {
		for (int i = 0; i < currentSound; i++)
			if (anIntArray1250[i] <= 0) {
				boolean flag1 = false;
				try {
					JagexBuffer stream = Sounds.method241(anIntArray1241[i],
							anIntArray1207[i]);
					new SoundPlayer((InputStream) new ByteArrayInputStream(
							stream.buffer, 0, stream.currentOffset));
					if (System.currentTimeMillis()
							+ (long) (stream.currentOffset / 22) > aLong1172
							+ (long) (anInt1257 / 22)) {
						anInt1257 = stream.currentOffset;
						aLong1172 = System.currentTimeMillis();
					}
				} catch (Exception exception) {
					// exception.printStackTrace();
				}
				if (!flag1 || anIntArray1250[i] == -5) {
					currentSound--;
					for (int j = i; j < currentSound; j++) {
						anIntArray1207[j] = anIntArray1207[j + 1];
						anIntArray1241[j] = anIntArray1241[j + 1];
						anIntArray1250[j] = anIntArray1250[j + 1];
					}
					i--;
				} else {
					anIntArray1250[i] = -5;
				}
			} else {
				anIntArray1250[i]--;
			}

		if (previousSong > 0) {
			previousSong -= 20;
			if (previousSong < 0)
				previousSong = 0;
			if (previousSong == 0 && musicEnabled && !lowMemory) {
				nextSong = currentSong;
				songChanging = true;
				resourceProvider.loadMandatory(2, nextSong);
			}
		}
	}
	
	public void demandModel(int modelId) {
		do {
			Resource resource;
			do {
				resource = resourceProvider.getNextNode();
				if (resource == null)
					return;
				Model.method460(resource.data, modelId);
			} while (!resourceProvider.method564(modelId));
		} while (true);
	}

	public void loadTitleImages() {
		banner = new RSImage(getImage("title", "banner"));
		header = new RSImage(getImage("title", "header"));
		box = new RSImage(getImage("title", "box"));
		input = new RSImage(getImage("title", "input"));
		input_hover = new RSImage(getImage("title", "input_hover"));
		play = new RSImage(getImage("title", "play"));
		play_hover = new RSImage(getImage("title", "play_hover"));
		check = new RSImage[]{ new RSImage(getImage("title", "check")), new RSImage(getImage("title", "check_hover")) };
		cross = new RSImage[]{ new RSImage(getImage("title", "cross")), new RSImage(getImage("title", "cross_hover")) };
		info = new RSImage[]{ new RSImage(getImage("title", "info")), new RSImage(getImage("title", "info_hover")) };
		back = new RSImage[]{ new RSImage(getImage("title", "back")), new RSImage(getImage("title", "back_hover")) };
		formbox = new RSImage(getImage("title", "formbox"));
		status = new RSImage[]{ cross[0], check[0] };
		status_hover = new RSImage[]{ cross[1], check[1] };
		close = new RSImage[]{ new RSImage(getImage("title", "close")), new RSImage(getImage("title", "close_hover")) };
		screen_dull = new RSImage[]{ new RSImage(getImage("title", "fixed_dull")), new RSImage(getImage("title", "resize_dull")), new RSImage(getImage("title", "full_dull")) };
		screen_selected = new RSImage[]{ new RSImage(getImage("title", "fixed_selected")), new RSImage(getImage("title", "resize_selected")), new RSImage(getImage("title", "full_selected")) };
		detail_dull = new RSImage[]{ new RSImage(getImage("title", "sd_dull")), new RSImage(getImage("title", "hd_dull")) };
		detail_hover = new RSImage[]{ new RSImage(getImage("title", "sd_hover")), new RSImage(getImage("title", "hd_hover")) };
		detail_selected = new RSImage[]{ new RSImage(getImage("title", "sd_selected")), new RSImage(getImage("title", "hd_selected")) };
		header_glow = new RSImage(getImage("title", "header_glow"));
	}

	void startUp() {
		//new UpdateCache(this).run(); //TODO uncomment for cache updating
		updateProgress("Starting up", 0);
		if (signlink.sunjava)
			super.minDelay = 5;
		aBoolean993 = true;
		getDocumentBaseHost();
		if (signlink.cache_dat != null) {
			for (int i = 0; i < 6; i++) {
				mainCacheFile[i] = new Decompressor(signlink.cache_dat,
						signlink.cache_idx[i], i + 1, 0xffffff); //this unpacks the main_cache_files
			}
		}
		try {
			// connectServer();
			titleArchive = streamLoaderForName(1, "title screen", "title", expectedCRCs[1], 25);
			smallText = new RSFont(false, "p11_full", titleArchive);
			normalFont = new RSFont(false, "p12_full", titleArchive);
			boldFont = new RSFont(false, "b12_full", titleArchive);
			fancyText = new RSFont(true, "q8_full", titleArchive);
			newSmallFont = new RSFontSystem(false, "p11_full", titleArchive);
			newRegularFont = new RSFontSystem(false, "p12_full", titleArchive);
			newBoldFont = new RSFontSystem(false, "b12_full", titleArchive);
			newFancyFont = new RSFontSystem(true, "q8_full", titleArchive);
			regularHitFont = new RSFontSystem(false, "regularhit", titleArchive);
			bigHitFont = new RSFontSystem(true, "bighit", titleArchive);
			// drawLogo();
			loadTitleScreen();
			JagexArchive configArchive = streamLoaderForName(2, "config", "config", expectedCRCs[2], 30);
			JagexArchive interfaceArchive = streamLoaderForName(3, "interface", "interface", expectedCRCs[3], 35);
			JagexArchive mediaArchive = streamLoaderForName(4, "2d graphics", "media", expectedCRCs[4], 40);
			streamLoaderForName(6, "textures", "textures", expectedCRCs[6], 45);
			JagexArchive chatArchive = streamLoaderForName(7, "chat system", "wordenc", expectedCRCs[7], 50);
			streamLoaderForName(8, "sound effects", "sounds", expectedCRCs[8], 55);
			imageLoader = new ImageLoader(configArchive);
			loadedImages = true;
			bar[0] = new RSImage(getImage("title", "empty"));
			bar[1] = new RSImage(getImage("title", "fill"));
			fill = new RSImage(getImage("title", "fill"));
			magicAuto = new Sprite("magicAuto");
			background[0] = new RSImage(getImage("title", "background"), 0, 0, 383, 252);
			background[1] = new RSImage(getImage("title", "background"), 383, 0, 382, 252);
			background[2] = new RSImage(getImage("title", "background"), 0, 252, 383, 251);
			background[3] = new RSImage(getImage("title", "background"), 383, 252, 382, 251);
			background[4] = new RSImage(getImage("title", "background"));
			byteGroundArray = new byte[4][104][104];
			intGroundArray = new int[4][105][105];
			landscapeScene = new LandscapeScene(intGroundArray);
			for (int j = 0; j < 4; j++) {
				clippingPlanes[j] = new LandscapeClippingPlane();
			}
			miniMap = new Sprite(512, 512);
			JagexArchive streamLoader_6 = streamLoaderForName(5, "update list", "versionlist", expectedCRCs[5], 60);
			updateProgress("Connecting to update server", 60);
			resourceProvider = new ResourceProvider();
			resourceProvider.start(streamLoader_6, this);
			FrameReader.method528(resourceProvider.getAnimCount());
			Model.method459(resourceProvider.getVersionCount(0), resourceProvider);
			Texture.init(resourceProvider.getVersionCount(4), resourceProvider);
			preloadModels();
			updateProgress("Unpacking media", 80);
			loadTitleImages();
			try {
				SpriteLoader.loadSprites(configArchive);
				cacheSprite = SpriteLoader.sprites;
				try {
					for (int index = 0; index < 20; index++) {
						if (index < 17) {
							orbs[index] = new Sprite(mediaArchive, "orbs",
									index);
						} else {
							orbs[index] = new Sprite(mediaArchive, "orbs", 1);
						}
					}
				} catch (Exception e) {
				}
				try {
					for (int index = 0; index < 2; index++) {
						hitBar[index] = new Sprite(mediaArchive, "hitmarks",
								index + 5);
						full = new Sprite(mediaArchive, "hitmarks", 6);
					}
				} catch (Exception e) {
				}
				multiOverlay = new Sprite(mediaArchive, "overlay_multiway", 0);
				for (int index = 0; index < 12; index++) {
					scrollPart[index] = new Sprite(mediaArchive, "scrollpart",
							index);
				}
				for (int index = 0; index < 6; index++) {
					scrollBar[index] = new Sprite(mediaArchive, "scrollbar",
							index);
				}
				/**/
				mapBack = new IndexedImage(mediaArchive, "mapback", 0);
				for (int i = 0; i < 13; i++) {
					sideIcons[i] = new Sprite(mediaArchive, "sideicons", i);
				}
				compass = new Sprite(mediaArchive, "compass", 0);
				mapEdge = new Sprite(mediaArchive, "mapedge", 0);
				mapEdge.method345();
				try {
					for (int mapSceneIndex = 0; mapSceneIndex < 80; mapSceneIndex++) {
						mapScenes[mapSceneIndex] = new Sprite(mediaArchive, "mapscene", mapSceneIndex);
					}
				} catch (Exception e) {
				}
				try {
					for (int mapFunctionIndex = 0; mapFunctionIndex < 76; mapFunctionIndex++) {
						mapFunctions[mapFunctionIndex] = new Sprite(mediaArchive, "mapfunction", mapFunctionIndex);
					}
				} catch (Exception e) {
					//e.printStackTrace();
				}
				try {
					for (int hitMarkIndex = 72; hitMarkIndex < 79; hitMarkIndex++) {
						hitMark[hitMarkIndex - 72] = cacheSprite[hitMarkIndex];
					}
					for (int hitMarkIndex = 79; hitMarkIndex <= 81; hitMarkIndex++) {
						hitShadow[hitMarkIndex - 79] = cacheSprite[hitMarkIndex];
					}
					for (int hitStyleIndex = 69; hitStyleIndex <= 71; hitStyleIndex++) {
						hitStyle[hitStyleIndex - 69] = cacheSprite[hitStyleIndex];
					}
				} catch (Exception e) {
				}
				try {
					for (int hintIconIndex = 0; hintIconIndex < 2; hintIconIndex++) {
						headIconsHint[hintIconIndex] = new Sprite(mediaArchive,
								"headicons_hint", hintIconIndex);
					}
					for (int prayerIconIndex = 0; prayerIconIndex < 19; prayerIconIndex++) {
						headIcons[prayerIconIndex] = new Sprite(mediaArchive, "headicons_prayer", prayerIconIndex);
					}
					for (int skullIconIndex = 0; skullIconIndex < 8; skullIconIndex++) {
						skullIcons[skullIconIndex] = new Sprite(mediaArchive,
								"headicons_pk", skullIconIndex);
					}
				} catch (Exception _ex) {
				}
				mapFlag = new Sprite(mediaArchive, "mapmarker", 0);
				mapMarker = new Sprite(mediaArchive, "mapmarker", 1);
				for (int k4 = 0; k4 < 8; k4++)
					crosses[k4] = new Sprite(mediaArchive, "cross", k4);
				mapDotItem = new Sprite(mediaArchive, "mapdots", 0);
				mapDotNPC = new Sprite(mediaArchive, "mapdots", 1);
				mapDotPlayer = new Sprite(mediaArchive, "mapdots", 2);
				mapDotFriend = new Sprite(mediaArchive, "mapdots", 3);
				mapDotTeam = new Sprite(mediaArchive, "mapdots", 4);
				mapDotClan = new Sprite(mediaArchive, "mapdots", 5);
				for (int index = 0; index < 12; index++) {
					modIcons[index] = cacheSprite[227 + index];
				}	
				Sprite sprite = new Sprite(mediaArchive, "frame", 0);
				leftFrame = new RSImageProducer(sprite.myWidth,
						sprite.myHeight, getGameComponent());
				sprite.method346(0, 0);
				sprite = new Sprite(mediaArchive, "frame", 1);
				topFrame = new RSImageProducer(sprite.myWidth, sprite.myHeight,
						getGameComponent());
				sprite.method346(0, 0);
				sprite = new Sprite(mediaArchive, "frame", 2);
				rightFrame = new RSImageProducer(sprite.myWidth,
						sprite.myHeight, getGameComponent());
				sprite.method346(0, 0);

				/*int i5 = (int) (Math.random() * 21D) - 10;
				int j5 = (int) (Math.random() * 21D) - 10;
				int k5 = (int) (Math.random() * 21D) - 10;
				int l5 = (int) (Math.random() * 41D) - 20;
				for (int i6 = 0; i6 < 100; i6++) {
					if (mapFunctions[i6] != null)
						mapFunctions[i6].method344(i5 + l5, j5 + l5, k5 + l5);
					if (mapScenes[i6] != null)
						mapScenes[i6].method360(i5 + l5, j5 + l5, k5 + l5);
				}*/
			} catch (Exception e) {
				e.printStackTrace();
			}
			updateProgress("Unpacking textures", 83);
			// Rasterizer.unpack(textureArchive);
			Rasterizer.calculatePalette(0.8F);
			Rasterizer.resetTextures();
			updateProgress("Unpacking config", 86);
			try {
				Varp.unpackConfig(configArchive.getData("varp.dat"));
				VarBit.unpackConfig(configArchive.getData("varbit.dat"));
				TextureDef.unpackConfig(configArchive.getData("texture.dat"));
				Floor.unpackConfig(configArchive.getData("flo.dat"));
				SkinList.unpackConfig(configArchive.getData("skin.dat"));
				Animation.unpackConfig(configArchive.getData("seq.dat"));
				StillGraphics.unpackConfig(configArchive.getData("gfx.dat"));
				IdentityKit.unpackConfig(configArchive.getData("idk.dat"));
				NPCAnimDef.unpackConfig(configArchive.getData("npcanim.dat"));
				ObjectDefinition.unpackConfig(configArchive.getData("object.dat"),
						configArchive.getData("object.idx"));
				ItemDef.unpackConfig(configArchive.getData("item.dat"),
						configArchive.getData("item.idx"));
				Floor.unpackConfig(configArchive);
				NPCDefinition.unpackConfig(configArchive.getData("npc.dat"),
						configArchive.getData("npc.idx"));
				ItemDef.isMembers = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			updateProgress("Unpacking interfaces", 95);
			RSFontSystem[] fonts = { newSmallFont, newRegularFont, newBoldFont,
					newFancyFont };
			RSInterface.unpack(interfaceArchive, fonts, mediaArchive);
			//RSInterface.pack();
			updateProgress("Preparing game engine", 100);
			for (int j6 = 0; j6 < 33; j6++) {
				int k6 = 999;
				int i7 = 0;
				for (int k7 = 0; k7 < 34; k7++) {
					if (mapBack.myPixels[k7 + j6 * mapBack.anInt1452] == 0) {
						if (k6 == 999)
							k6 = k7;
						continue;
					}
					if (k6 == 999)
						continue;
					i7 = k7;
					break;
				}

				anIntArray968[j6] = k6;
				anIntArray1057[j6] = i7 - k6;
			}
			for (int l6 = 1; l6 < 153; l6++) {
				int j7 = 999;
				int l7 = 0;
				for (int j8 = 24; j8 < 177; j8++) {
					if (mapBack.myPixels[j8 + l6 * mapBack.anInt1452] == 0
							&& (j8 > 34 || l6 > 34)) {
						if (j7 == 999) {
							j7 = j8;
						}
						continue;
					}
					if (j7 == 999) {
						continue;
					}
					l7 = j8;
					break;
				}

				anIntArray1052[l6 - 1] = j7 - 24;
				anIntArray1229[l6 - 1] = l7 - j7;
			}
			Rasterizer.setBounds(765, 503);
			fullScreenTextureArray = Rasterizer.lineOffsets;
			Rasterizer.setBounds(519, 165);
			anIntArray1180 = Rasterizer.lineOffsets;
			Rasterizer.setBounds(246, 335);
			anIntArray1181 = Rasterizer.lineOffsets;
			Rasterizer.setBounds(512, 334);
			anIntArray1182 = Rasterizer.lineOffsets;
			int ai[] = new int[9];
			for (int i8 = 0; i8 < 9; i8++) {
				int k8 = 128 + i8 * 32 + 15;
				int l8 = 600 + k8 * 3;
				int i9 = Rasterizer.SINE[k8];
				ai[i8] = l8 * i9 >> 16;
			}
			LandscapeScene.method310(500, 800, 700, 700, ai);
			Censor.loadConfig(chatArchive);
			mouseDetection = new MouseDetection(this);
			startRunnable(mouseDetection, 10);
			GameObject.clientInstance = this;
			ObjectDefinition.clientInstance = this;
			NPCDefinition.clientInstance = this;
			// readMapPositions();
			//readSettings();
			RSFontSystem.unpackImages(modIcons, modIcons);
			Accounts.read();
			//extractMapFiles(signlink.getCacheLocation() + "/maps/");
			/*DataOutputStream output = new DataOutputStream(new FileOutputStream(signlink.getCacheLocation() + "map_index.dat"));
			output.write(mainCacheFile[0].get(4));
			output.close();*/
			return;
		} catch (Exception exception) {
			exception.printStackTrace();
			signlink.reporterror("loaderror " + aString1049 + " " + anInt1079);
		}
		loadingError = true;
	}

	public void addNewPlayer(JagexBuffer stream, int i) {
		while (stream.bitPosition + 10 < i * 8) {
			int j = stream.readBits(11);
			if (j == 2047)
				break;
			if (playerArray[j] == null) {
				playerArray[j] = new Player();
				if (aStreamArray895s[j] != null)
					playerArray[j].updatePlayer(aStreamArray895s[j]);
			}
			playerIndices[playerCount++] = j;
			Player player = playerArray[j];
			player.anInt1537 = loopCycle;
			int update = stream.readBits(1);
			if (update == 1)
				anIntArray894[anInt893++] = j;
			int movement = stream.readBits(1);
			int regionY = stream.readBits(5);
			if (regionY > 15)
				regionY -= 32;
			int regionX = stream.readBits(5);
			if (regionX > 15)
				regionX -= 32;
			player.setPosition(myPlayer.smallX[0] + regionX, myPlayer.smallY[0] + regionY,
					movement == 1);
		}
		stream.finishBitAccess();
	}

	public boolean inCircle(int circleX, int circleY, int clickX, int clickY,
			int radius) {
		return Math.pow((circleX + radius - clickX), 2)
				+ Math.pow((circleY + radius - clickY), 2) < Math
				.pow(radius, 2);
	}

	public boolean canClickMap() {
		if (super.mouseX >= clientWidth - 21 && super.mouseX <= clientWidth
				&& super.mouseY >= 0 && super.mouseY <= 21)
			return false;
		return true;
	}

	public void processMainScreenClick() {
		if (minimapState != 0) {
			return;
		}
		if (super.clickMode3 == 1) {
			int clickX = super.saveClickX - 3
					- (clientSize == 0 ? clientWidth - 214 : clientWidth - 163);
			int clickY = super.saveClickY - (clientSize == 0 ? 9 : 6);
			// if (i >= 0 && j >= 0 && i < 152 && j < 152 && canClickMap()) {
			if (inCircle(0, 0, clickX, clickY, 76)) {
				clickX -= 73;
				clickY -= 75;
				int k = viewRotation + minimapRotation & 0x7ff;
				int i1 = Rasterizer.SINE[k];
				int j1 = Rasterizer.COSINE[k];
				i1 = i1 * (minimapZoom + 256) >> 8;
				j1 = j1 * (minimapZoom + 256) >> 8;
				int k1 = clickY * i1 + clickX * j1 >> 11;
				int l1 = clickY * j1 - clickX * i1 >> 11;
				int toLocalX = myPlayer.x + k1 >> 7;
				int toLocalY = myPlayer.y - l1 >> 7;
				boolean flag1 = calculatePath(1, 0, 0, 0, myPlayer.smallY[0], 0, 0,
						toLocalY, myPlayer.smallX[0], true, toLocalX);
				if (flag1) {
					stream.writeByte(clickX);
					stream.writeByte(clickY);
					stream.writeShort(viewRotation);
					stream.writeByte(57);
					stream.writeByte(minimapRotation);
					stream.writeByte(minimapZoom);
					stream.writeByte(89);
					stream.writeShort(myPlayer.x);
					stream.writeShort(myPlayer.y);
					stream.writeByte(arbitraryDestination);
					stream.writeByte(63);
				}
			}
			anInt1117++;
			if (anInt1117 > 1151) {
				anInt1117 = 0;
				stream.createFrame(246);
				stream.writeByte(0);
				int l = stream.currentOffset;
				if ((int) (Math.random() * 2D) == 0)
					stream.writeByte(101);
				stream.writeByte(197);
				stream.writeShort((int) (Math.random() * 65536D));
				stream.writeByte((int) (Math.random() * 256D));
				stream.writeByte(67);
				stream.writeShort(14214);
				if ((int) (Math.random() * 2D) == 0)
					stream.writeShort(29487);
				stream.writeShort((int) (Math.random() * 65536D));
				if ((int) (Math.random() * 2D) == 0)
					stream.writeByte(220);
				stream.writeByte(180);
				stream.writeBytes(stream.currentOffset - l);
			}
		}
	}

	public String interfaceIntToString(int j) {
		if (j < 0x3b9ac9ff)
			return String.valueOf(j);
		else
			return "*";
	}

	public void showErrorScreen() {
		Graphics g = getGameComponent().getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 765, 503);
		method4(1);
		if (loadingError) {
			aBoolean831 = false;
			g.setFont(new Font("Helvetica", 1, 16));
			g.setColor(Color.yellow);
			int k = 35;
			g.drawString(
					"Sorry, an error has occured whilst loading Battle Royale.",
					30, k);
			k += 50;
			g.setColor(Color.white);
			g.drawString("To fix this try the following (packet order):", 30, k);
			k += 50;
			g.setColor(Color.white);
			g.setFont(new Font("Helvetica", 1, 12));
			g.drawString(
					"1: Try closing ALL open web-browser windows, and reloading",
					30, k);
			k += 30;
			g.drawString(
					"2: Try clearing your web-browsers cache from tools->internet options",
					30, k);
			k += 30;
			g.drawString("3: Try using a different game-world", 30, k);
			k += 30;
			g.drawString("4: Try rebooting your computer", 30, k);
			k += 30;
			g.drawString(
					"5: Try selecting a different version of Java from the play-game menu",
					30, k);
		}
		if (genericLoadingError) {
			aBoolean831 = false;
			g.setFont(new Font("Helvetica", 1, 20));
			g.setColor(Color.white);
			g.drawString("Error - unable to load game!", 50, 50);
			g.drawString("To play Battle Royale make sure you play from", 50, 100);
			g.drawString("http://www.royaleps.com", 50, 150);
		}
		if (rsAlreadyLoaded) {
			aBoolean831 = false;
			g.setColor(Color.yellow);
			int l = 35;
			g.drawString(
					"Error a copy of Battle Royale already appears to be loaded",
					30, l);
			l += 50;
			g.setColor(Color.white);
			g.drawString("To fix this try the following (packet order):", 30, l);
			l += 50;
			g.setColor(Color.white);
			g.setFont(new Font("Helvetica", 1, 12));
			g.drawString(
					"1: Try closing ALL open web-browser windows, and reloading",
					30, l);
			l += 30;
			g.drawString("2: Try rebooting your computer, and reloading", 30, l);
			l += 30;
		}
	}

	public URL getCodeBase() {
		try {
			return new URL(Constants.HOST_ADDRESS + ":" + (80 + portOff));
		} catch (Exception _ex) {
		}
		return null;
	}

	public void method95() {
		for (int j = 0; j < npcCount; j++) {
			int k = npcIndices[j];
			NPC npc = npcArray[k];
			if (npc != null)
				method96(npc);
		}
	}

	public void method96(Entity entity) {
		if (entity.x < 128 || entity.y < 128 || entity.x >= 13184
				|| entity.y >= 13184) {
			entity.animationId = -1;
			entity.graphicId = -1;
			entity.anInt1547 = 0;
			entity.anInt1548 = 0;
			entity.x = entity.smallX[0] * 128 + entity.size * 64;
			entity.y = entity.smallY[0] * 128 + entity.size * 64;
			entity.resetRegionPosition();
		}
		if (entity == myPlayer
				&& (entity.x < 1536 || entity.y < 1536 || entity.x >= 11776 || entity.y >= 11776)) {
			entity.animationId = -1;
			entity.graphicId = -1;
			entity.anInt1547 = 0;
			entity.anInt1548 = 0;
			entity.x = entity.smallX[0] * 128 + entity.size * 64;
			entity.y = entity.smallY[0] * 128 + entity.size * 64;
			entity.resetRegionPosition();
		}
		if (entity.anInt1547 > loopCycle)
			method97(entity);
		else if (entity.anInt1548 >= loopCycle)
			method98(entity);
		else
			method99(entity);
		method100(entity);
		handleAnimableData(entity);
	}

	public void method97(Entity entity) {
		int i = entity.anInt1547 - loopCycle;
		int j = entity.anInt1543 * 128 + entity.size * 64;
		int k = entity.anInt1545 * 128 + entity.size * 64;
		entity.x += (j - entity.x) / i;
		entity.y += (k - entity.y) / i;
		entity.anInt1503 = 0;
		if (entity.anInt1549 == 0)
			entity.turnDirection = 1024;
		if (entity.anInt1549 == 1)
			entity.turnDirection = 1536;
		if (entity.anInt1549 == 2)
			entity.turnDirection = 0;
		if (entity.anInt1549 == 3)
			entity.turnDirection = 512;
	}

	public void method98(Entity entity) {
		if (entity.anInt1548 == loopCycle
				|| entity.animationId == -1
				|| entity.animationDelay != 0
				|| entity.anInt1528 + 1 > Animation.anims[entity.animationId]
						.method258(entity.anInt1527)) {
			int i = entity.anInt1548 - entity.anInt1547;
			int j = loopCycle - entity.anInt1547;
			int k = entity.anInt1543 * 128 + entity.size * 64;
			int l = entity.anInt1545 * 128 + entity.size * 64;
			int i1 = entity.anInt1544 * 128 + entity.size * 64;
			int j1 = entity.anInt1546 * 128 + entity.size * 64;
			if (i != 0) {
				entity.x = (k * (i - j) + i1 * j) / i;
				entity.y = (l * (i - j) + j1 * j) / i;
			}
		}
		entity.anInt1503 = 0;
		if (entity.anInt1549 == 0)
			entity.turnDirection = 1024;
		if (entity.anInt1549 == 1)
			entity.turnDirection = 1536;
		if (entity.anInt1549 == 2)
			entity.turnDirection = 0;
		if (entity.anInt1549 == 3)
			entity.turnDirection = 512;
		entity.anInt1552 = entity.turnDirection;
	}

	public void method99(Entity entity) {
		entity.anInt1517 = entity.standAnimation;
		if (entity.smallXYIndex == 0) {
			entity.anInt1503 = 0;
			return;
		}
		if (entity.animationId != -1 && entity.animationDelay == 0) {
			Animation animation = Animation.anims[entity.animationId];
			if (entity.anInt1542 > 0 && animation.anInt363 == 0) {
				entity.anInt1503++;
				return;
			}
			if (entity.anInt1542 <= 0 && animation.anInt364 == 0) {
				entity.anInt1503++;
				return;
			}
		}
		int i = entity.x;
		int j = entity.y;
		int k = entity.smallX[entity.smallXYIndex - 1] * 128 + entity.size
				* 64;
		int l = entity.smallY[entity.smallXYIndex - 1] * 128 + entity.size
				* 64;
		if (k - i > 256 || k - i < -256 || l - j > 256 || l - j < -256) {
			entity.x = k;
			entity.y = l;
			return;
		}
		if (i < k) {
			if (j < l)
				entity.turnDirection = 1280;
			else if (j > l)
				entity.turnDirection = 1792;
			else
				entity.turnDirection = 1536;
		} else if (i > k) {
			if (j < l)
				entity.turnDirection = 768;
			else if (j > l)
				entity.turnDirection = 256;
			else
				entity.turnDirection = 512;
		} else if (j < l)
			entity.turnDirection = 1024;
		else
			entity.turnDirection = 0;
		int i1 = entity.turnDirection - entity.anInt1552 & 0x7ff;
		if (i1 > 1024)
			i1 -= 2048;
		int j1 = entity.reverseAnimation;
		if (i1 >= -256 && i1 <= 256)
			j1 = entity.walkAnimation;
		else if (i1 >= 256 && i1 < 768)
			j1 = entity.turnRightAnimation;
		else if (i1 >= -768 && i1 <= -256)
			j1 = entity.turnLeftAnimation;
		if (j1 == -1)
			j1 = entity.walkAnimation;
		entity.anInt1517 = j1;
		int k1 = 4;
		if (entity.anInt1552 != entity.turnDirection
				&& entity.interactingEntity == -1 && entity.turnSpeed != 0)
			k1 = 2;
		if (entity.smallXYIndex > 2)
			k1 = 6;
		if (entity.smallXYIndex > 3)
			k1 = 8;
		if (entity.anInt1503 > 0 && entity.smallXYIndex > 1) {
			k1 = 8;
			entity.anInt1503--;
		}
		if (entity.aBooleanArray1553[entity.smallXYIndex - 1])
			k1 <<= 1;
		if (k1 >= 8 && entity.anInt1517 == entity.walkAnimation
				&& entity.runAnimation != -1)
			entity.anInt1517 = entity.runAnimation;
		if (i < k) {
			entity.x += k1;
			if (entity.x > k)
				entity.x = k;
		} else if (i > k) {
			entity.x -= k1;
			if (entity.x < k)
				entity.x = k;
		}
		if (j < l) {
			entity.y += k1;
			if (entity.y > l)
				entity.y = l;
		} else if (j > l) {
			entity.y -= k1;
			if (entity.y < l)
				entity.y = l;
		}
		if (entity.x == k && entity.y == l) {
			entity.smallXYIndex--;
			if (entity.anInt1542 > 0)
				entity.anInt1542--;
		}
	}

	public void method100(Entity entity) {
		try {
		if (entity.turnSpeed == 0)
			return;
		if (entity.interactingEntity != -1 && entity.interactingEntity < 32768) {
			NPC npc = npcArray[entity.interactingEntity];
			if (npc != null) {
				int i1 = entity.x - npc.x;
				int k1 = entity.y - npc.y;
				if (i1 != 0 || k1 != 0)
					entity.turnDirection = (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
				}
		}
		if (entity.interactingEntity >= 32768) {
			int j = entity.interactingEntity - 32768;
			if (j == playerId)
				j = myPlayerIndex;
			Player player = playerArray[j];
			if (player != null) {
				int l1 = entity.x - player.x;
				int i2 = entity.y - player.y;
				if (l1 != 0 || i2 != 0)
					entity.turnDirection = (int) (Math.atan2(l1, i2) * 325.94900000000001D) & 0x7ff;
			}
		}
		if ((entity.facePositionX != 0 || entity.facePositionY != 0)
				&& (entity.smallXYIndex == 0 || entity.anInt1503 > 0)) {
			int k = entity.x - (entity.facePositionX - regionAbsBaseX - regionAbsBaseX) * 64;
			int j1 = entity.y - (entity.facePositionY - regionAbsBaseY - regionAbsBaseY) * 64;
			if (k != 0 || j1 != 0)
				entity.turnDirection = (int) (Math.atan2(k, j1) * 325.94900000000001D) & 0x7ff;
			entity.facePositionX = 0;
			entity.facePositionY = 0;
		}
		int l = entity.turnDirection - entity.anInt1552 & 0x7ff;
		if (l != 0) {
			if (l < entity.turnSpeed || l > 2048 - entity.turnSpeed)
				entity.anInt1552 = entity.turnDirection;
			else if (l > 1024)
				entity.anInt1552 -= entity.turnSpeed;
			else
				entity.anInt1552 += entity.turnSpeed;
			entity.anInt1552 &= 0x7ff;
			if (entity.anInt1517 == entity.standAnimation
					&& entity.anInt1552 != entity.turnDirection) {
				if (entity.spinAnimation != -1) {
					entity.anInt1517 = entity.spinAnimation;
					return;
				}
				entity.anInt1517 = entity.walkAnimation;
			}
		}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void handleAnimableData(Entity e) {
		e.aBoolean1541 = false;
		if (e.anInt1517 != -1 && e.anInt1517 != 65535 && e.anInt1517 < Animation.anims.length) {
			Animation animation = Animation.anims[e.anInt1517];
			e.anInt1519++;
			if (e.anInt1518 < animation.anInt352
					&& e.anInt1519 > animation.method258(e.anInt1518)) {
				e.anInt1519 = 1;
				e.anInt1518++;
			}
			if (e.anInt1518 >= animation.anInt352) {
				e.anInt1519 = 1;
				e.anInt1518 = 0;
			}
		}
		if (e.graphicId != -1 && loopCycle >= e.graphicDelay) {
			if (e.anInt1521 < 0)
				e.anInt1521 = 0;
			if (e.graphicId >= StillGraphics.cache.length)// graphics
															// anti-freeze
				e.graphicId = 0;
			Animation animation_1 = StillGraphics.cache[e.graphicId].animDef;
			if (animation_1 != null) {
				for (e.anInt1522++; e.anInt1521 < animation_1.anInt352
						&& e.anInt1522 > animation_1.method258(e.anInt1521); e.anInt1521++)
					e.anInt1522 -= animation_1.method258(e.anInt1521);
				if (e.anInt1521 >= animation_1.anInt352
						&& (e.anInt1521 < 0 || e.anInt1521 >= animation_1.anInt352))
					e.graphicId = -1;
			}
		}
		if (e.animationId != -1 && e.animationDelay <= 1) {
			if (e.animationId >= Animation.anims.length)// animationId anti-freeze
				e.animationId = 0x328;
			Animation animation_2 = Animation.anims[e.animationId];
			if (animation_2.anInt363 == 1 && e.anInt1542 > 0
					&& e.anInt1547 <= loopCycle && e.anInt1548 < loopCycle) {
				e.animationDelay = 1;
				return;
			}
		}
		if (e.animationId != -1 && e.animationDelay == 0) {
			Animation animation_3 = Animation.anims[e.animationId];
			for (e.anInt1528++; e.anInt1527 < animation_3.anInt352
					&& e.anInt1528 > animation_3.method258(e.anInt1527); e.anInt1527++)
				e.anInt1528 -= animation_3.method258(e.anInt1527);

			if (e.anInt1527 >= animation_3.anInt352) {
				e.anInt1527 -= animation_3.anInt356;
				e.anInt1530++;
				if (e.anInt1530 >= animation_3.anInt362)
					e.animationId = -1;
				if (e.anInt1527 < 0 || e.anInt1527 >= animation_3.anInt352)
					e.animationId = -1;
			}
			e.aBoolean1541 = animation_3.aBoolean358;
		}
		if (e.animationDelay > 0)
			e.animationDelay--;
	}

	public void drawGameScreen() {
		if (fullscreenInterfaceID != -1
				&& (loadingStage == 2 || super.fullGameScreen != null)) {
			if (loadingStage == 2) {
				method119(anInt945, fullscreenInterfaceID);
				if (openInterfaceID != -1) {
					method119(anInt945, openInterfaceID);
				}
				anInt945 = 0;
				resetAllImageProducers();
				super.fullGameScreen.initDrawingArea();
				Rasterizer.lineOffsets = fullScreenTextureArray;
				DrawingArea.setAllPixelsToZero();
				welcomeScreenRaised = true;
				if (openInterfaceID != -1) {
					RSInterface rsInterface_1 = RSInterface.interfaceCache[openInterfaceID];
					if (rsInterface_1.width == 512
							&& rsInterface_1.height == 334
							&& rsInterface_1.type == 0) {
						rsInterface_1.width = 765;
						rsInterface_1.height = 503;
					}
					drawInterface(0, 8, 0, rsInterface_1);
				}
				RSInterface rsInterface = RSInterface.interfaceCache[fullscreenInterfaceID];
				if (rsInterface.width == 512 && rsInterface.height == 334
						&& rsInterface.type == 0) {
					rsInterface.width = 765;
					rsInterface.height = 503;
				}
				drawInterface(0, 8, 0, rsInterface);

				if (!menuOpen) {
					processRightClick();
					drawTooltip();
				} else {
					drawMenu();
				}
			}
			drawCount++;
			super.fullGameScreen.drawGraphics(0, 0, super.graphics);
			return;
		} else {
			if (drawCount != 0) {
				resetImageProducers2();
			}
		}
		if (welcomeScreenRaised) {
			welcomeScreenRaised = false;
			if (clientSize == 0) {
				leftFrame.drawGraphics(0, 0, super.graphics);
				topFrame.drawGraphics(4, 0, super.graphics);
				rightFrame.drawGraphics(516, 0, super.graphics);
			}
			needDrawTabArea = true;
			inputTaken = true;
			tabAreaAltered = true;
			aBoolean1233 = true;
			if (loadingStage != 2) {
				gameScreenIP.drawGraphics(clientSize == 0 ? 4 : 0,
						clientSize == 0 ? 4 : 0, super.graphics);
				if (clientSize == 0)
					mapAreaIP.drawGraphics(765 - 246, 0, super.graphics);
			}
		}
		if (menuOpen && menuScreenArea == 1)
			needDrawTabArea = true;
		if (invOverlayInterfaceID != -1) {
			boolean flag1 = method119(anInt945, invOverlayInterfaceID);
			if (flag1)
				needDrawTabArea = true;
		}
		if (atInventoryInterfaceType == 2)
			needDrawTabArea = true;
		if (activeInterfaceType == 2)
			needDrawTabArea = true;
		if (needDrawTabArea) {
			if (isFixed()) {
				drawTabArea();
			}
			needDrawTabArea = false;
		}
		if (backDialogID == -1 && inputDialogState != 3) {
			rsi.scrollPosition = anInt1211 - anInt1089 - 110;
			if (super.mouseX > 478 && super.mouseX < 580
					&& super.mouseY > (clientHeight - 161))
				method65(494, 110, super.mouseX - 0, super.mouseY
						- (clientHeight - 155), rsi, 0, false, anInt1211);
			int i = anInt1211 - 110 - rsi.scrollPosition;
			if (i < 0)
				i = 0;
			if (i > anInt1211 - 110)
				i = anInt1211 - 110;
			if (anInt1089 != i) {
				anInt1089 = i;
				inputTaken = true;
			}
		}
		if (backDialogID == -1 && inputDialogState == 3) {
			int position = totalItemResults * 14 + 7;
			rsi.scrollPosition = itemResultScrollPos;
			if (super.mouseX > 478 && super.mouseX < 580
					&& super.mouseY > (clientHeight - 161)) {
				method65(494, 110, super.mouseX - 0, super.mouseY
						- (clientHeight - 155), rsi, 0, false, totalItemResults);
			}
			int scrollPosition = rsi.scrollPosition;
			if (scrollPosition < 0) {
				scrollPosition = 0;
			}
			if (scrollPosition > position - 110) {
				scrollPosition = position - 110;
			}
			if (itemResultScrollPos != scrollPosition) {
				itemResultScrollPos = scrollPosition;
				inputTaken = true;
			}
		}
		if (backDialogID != -1) {
			boolean flag2 = method119(anInt945, backDialogID);
			if (flag2)
				inputTaken = true;
		}
		if (atInventoryInterfaceType == 3)
			inputTaken = true;
		if (activeInterfaceType == 3)
			inputTaken = true;
		if (aString844 != null)
			inputTaken = true;
		if (menuOpen && menuScreenArea == 2)
			inputTaken = true;
		if (inputTaken) {
			if (isFixed()) {
				drawChatArea();
				gameScreenIP.initDrawingArea();
			}
			drawConsoleArea();
			inputTaken = false;
		}
		if (loadingStage == 2)
			method146();
		if (loadingStage == 2) {
			if (clientSize == 0) {
				drawMinimap();
				mapAreaIP.drawGraphics(765 - 246, 0, super.graphics);
			}
		}
		if (flashingSidebar != -1)
			tabAreaAltered = true;
		if (tabAreaAltered) {
			if (flashingSidebar != -1 && flashingSidebar == tabId) {
				flashingSidebar = -1;
				stream.createFrame(120);
				stream.writeByte(tabId);
			}
			tabAreaAltered = false;
			aRSImageProducer_1125.initDrawingArea();
			gameScreenIP.initDrawingArea();
		}
		anInt945 = 0;
	}

	public boolean buildFriendsListMenu(RSInterface rsi) {
		int contentType = rsi.contentType;
		if (contentType >= 1 && contentType <= 200 || contentType >= 701 && contentType <= 900) {
			if (contentType >= 801)
				contentType -= 701;
			else if (contentType >= 701)
				contentType -= 601;
			else if (contentType >= 101)
				contentType -= 101;
			else
				contentType--;
			menuActionName[menuActionRow] = "Remove @whi@" + friendsList[contentType];
			menuActionID[menuActionRow] = 792;
			menuActionRow++;
			menuActionName[menuActionRow] = "Message @whi@" + friendsList[contentType];
			menuActionID[menuActionRow] = 639;
			menuActionRow++;
			return true;
		}
		if (contentType >= 401 && contentType <= 500) {
			menuActionName[menuActionRow] = "Remove @whi@" + rsi.disabledText;
			menuActionID[menuActionRow] = 322;
			menuActionRow++;
			return true;
		} else {
			return false;
		}
	}

	public void method104() {
		GraphicModel class30_sub2_sub4_sub3 = (GraphicModel) aClass19_1056
				.head();
		for (; class30_sub2_sub4_sub3 != null; class30_sub2_sub4_sub3 = (GraphicModel) aClass19_1056
				.next())
			if (class30_sub2_sub4_sub3.anInt1560 != plane
					|| class30_sub2_sub4_sub3.aBoolean1567)
				class30_sub2_sub4_sub3.remove();
			else if (loopCycle >= class30_sub2_sub4_sub3.anInt1564) {
				class30_sub2_sub4_sub3.method454(anInt945);
				if (class30_sub2_sub4_sub3.aBoolean1567)
					class30_sub2_sub4_sub3.remove();
				else
					landscapeScene.method285(class30_sub2_sub4_sub3.anInt1560,
							0, class30_sub2_sub4_sub3.anInt1563, -1,
							class30_sub2_sub4_sub3.anInt1562, 60,
							class30_sub2_sub4_sub3.anInt1561,
							class30_sub2_sub4_sub3, false);
			}

	}

	public void drawBlackBox(int xPos, int yPos) {
		DrawingArea.drawPixels(71, yPos - 1, xPos - 2, 0x726451, 1);
		DrawingArea.drawPixels(69, yPos, xPos + 174, 0x726451, 1);
		DrawingArea.drawPixels(1, yPos - 2, xPos - 2, 0x726451, 178);
		DrawingArea.drawPixels(1, yPos + 68, xPos, 0x726451, 174);
		DrawingArea.drawPixels(71, yPos - 1, xPos - 1, 0x2E2B23, 1);
		DrawingArea.drawPixels(71, yPos - 1, xPos + 175, 0x2E2B23, 1);
		DrawingArea.drawPixels(1, yPos - 1, xPos, 0x2E2B23, 175);
		DrawingArea.drawPixels(1, yPos + 69, xPos, 0x2E2B23, 175);
		DrawingArea.method335(0, yPos, 174, 68, 220, xPos);
	}

	public void drawInterface(int xPosition, int yPosition, int paddingY,
			RSInterface rsi) {
		try {
		if (rsi == null) {
			return;
		}
		if (rsi.type != 0 || rsi.children == null) {
			return;
		}
		if (rsi.hidden && anInt1026 != rsi.id
				&& anInt1048 != rsi.id && anInt1039 != rsi.id) {
			return;
		}
		int i1 = DrawingArea.topX;
		int j1 = DrawingArea.topY;
		int k1 = DrawingArea.bottomX;
		int l1 = DrawingArea.bottomY;
		DrawingArea.setDrawingArea(xPosition, yPosition, xPosition + rsi.width,
				yPosition + rsi.height);
		int i2 = rsi.children.length;
		for (int j2 = 0; j2 < i2; j2++) {
			int offsetX = rsi.childX[j2] + xPosition;
			int offsetY = (rsi.childY[j2] + yPosition) - paddingY;
			int positionX = offsetX;
			int positionY = offsetY;
			RSInterface child = RSInterface.interfaceCache[rsi.children[j2]];
			/*if (child == null) {
				System.out.println("childId is null: " + j2 + "; parentId: " + rsi.id);
				return;
			}*/
			offsetX += child.drawOffsetX;
			offsetY += child.drawOffsetY;
			if (child.contentType > 0)
				drawFriendsListOrWelcomeScreen(child);
			if (child.type == 0) {
				if (child.scrollPosition > child.scrollMax - child.height)
					child.scrollPosition = child.scrollMax - child.height;
				if (child.scrollPosition < 0)
					child.scrollPosition = 0;
				drawInterface(offsetX, offsetY, child.scrollPosition, child);
				if (child.scrollMax > child.height)
					drawScrollbar(child.height, child.scrollPosition, offsetY,
							offsetX + child.width, child.scrollMax, false,
							false);
			} else if (child.type != 1)
				if (child.type == 2) {
					int index = 0;
					for (int childY = 0; childY < child.height; childY++) {
						for (int childX = 0; childX < child.width; childX++) {
							int x = positionX + childX * (32 + child.invSpritePadX);
							int y = positionY + childY * (32 + child.invSpritePadY);
							if (index < 20) {
								x += child.spritesX[index];
								y += child.spritesY[index];
							}
							if (child.inventory[index] > 0) {
								int offX = 0;
								int offY = 0;
								int itemId = (int) child.inventory[index] - 1;
								int value = child.shopPrices[index];
								String formatPrice = formatAmount(value);
								if (x > DrawingArea.topX - 32 && x < DrawingArea.bottomX && y > DrawingArea.topY - 32 && y < DrawingArea.bottomY || activeInterfaceType != 0 && anInt1085 == index) {
									int outlineColor = 0;
									if (itemSelected == 1 && useEntitySlot == index && useEntityInterface == child.id) {
										outlineColor = 0xffffff;
									}
									Sprite itemSprite = ItemDef.getSprite(itemId, child.inventoryAmount[index], outlineColor);
									if (itemSprite != null) {
										if (child.sprite1 != null) {
											Sprite sprite = child.sprite1;
											if (mouseInRegion(x, y, x + sprite.maxWidth, y + sprite.maxHeight) 
													&& child.hoverSprite1 != null) {
												sprite = child.hoverSprite1;
											}
											if (child.shopInterface) {
												sprite.drawSprite(x - 5, y - 5);
												newSmallFont.drawBasicString(formatPrice, (x + formatPrice.length() / 2), (y + 43 + offY), getAmountColor(value), 0);
												child.shopCurrency.drawSprite(x + offX + 32, y + 33 + offY);
											}
										}
										if (activeInterfaceType != 0 && anInt1085 == index && anInt1084 == child.id) {
											offX = super.mouseX - anInt1087;
											offY = super.mouseY - anInt1088;
											if (offX < 5 && offX > -5) {
												offX = 0;
											}
											if (offY < 5 && offY > -5) {
												offY = 0;
											}
											if (anInt989 < 5) {
												offX = 0;
												offY = 0;
											}
											if (itemSprite != null) {
												itemSprite.drawSprite(x + offX, y + offY, 128);
											}
											/*if (y + offY < DrawingArea.topY && rsi.scrollPosition > 0) {
												int scrollOffsetY = (anInt945 * (DrawingArea.topY - y - offY)) / 3;
												if (scrollOffsetY > anInt945 * 10) {
													scrollOffsetY = anInt945 * 10;
												}
												if (scrollOffsetY > rsi.scrollPosition) {
													scrollOffsetY = rsi.scrollPosition;
												}
												rsi.scrollPosition -= scrollOffsetY;
												anInt1088 += scrollOffsetY;
											}
											if (y + offY + 32 > DrawingArea.bottomY && rsi.scrollPosition < rsi.scrollMax - rsi.height) {
												int scrollOffsetY = (anInt945 * ((y + offY + 32) - DrawingArea.bottomY)) / 3;
												if (scrollOffsetY > anInt945 * 10) {
													scrollOffsetY = anInt945 * 10;
												}
												if (scrollOffsetY > rsi.scrollMax - rsi.height - rsi.scrollPosition) {
													scrollOffsetY = rsi.scrollMax - rsi.height - rsi.scrollPosition;
												}
												rsi.scrollPosition += scrollOffsetY;
												anInt1088 -= scrollOffsetY;
											}*/
										} else if (atInventoryInterfaceType != 0 && atInventoryIndex == index && atInventoryInterface == child.id) {
											if (itemSprite != null) {
												itemSprite.drawSprite(x, y, 128);
											}
										} else {
											if (itemSprite != null) {
												itemSprite.drawSprite(x, y);
											}
										}
										if (child.inventoryAmount != null && index < child.inventoryAmount.length && itemSprite.maxWidth == 33 || child.inventoryAmount[index] != 1) {
											long amount = child.inventoryAmount[index];
											newSmallFont.drawBasicString(formatAmount(amount), x + offX, y + 9 + offY, getAmountColor(amount), 0);
										}
									}
								}
							} else if (child.sprites != null && index < 20) {
								Sprite sprite = child.sprites[index];
								if (sprite != null) {
									sprite.drawSprite(x, y);
								}
							}
							index++;
						}
					}
				} else if (child.type == 3) {
					boolean flag = false;
					if (anInt1039 == child.id || anInt1048 == child.id
							|| anInt1026 == child.id)
						flag = true;
					int j3;
					if (interfaceIsSelected(child)) {
						j3 = child.anInt219;
						if (flag && child.anInt239 != 0)
							j3 = child.anInt239;
					} else {
						j3 = child.textColor;
						if (flag && child.anInt216 != 0)
							j3 = child.anInt216;
					}
					if (child.opacity == 0) {
						if (child.aBoolean227)
							DrawingArea.drawPixels(child.height, offsetY,
									offsetX, j3, child.width);
						else
							DrawingArea.fillPixels(offsetX, child.width,
									child.height, j3, offsetY);
					} else if (child.aBoolean227)
						DrawingArea.method335(j3, offsetY, child.width,
								child.height, 256 - (child.opacity & 0xff),
								offsetX);
					else
						DrawingArea.method338(offsetY, child.height,
								256 - (child.opacity & 0xff), j3, child.width,
								offsetX);
				} else if (child.type == 4) {
					RSFontSystem font = child.textDrawingAreas;
					String s = child.disabledText;
					boolean flag1 = false;
					if (anInt1039 == child.id || anInt1048 == child.id
							|| anInt1026 == child.id)
						flag1 = true;
					int i4;
					if (interfaceIsSelected(child)) {
						i4 = child.anInt219;
						if (flag1 && child.anInt239 != 0)
							i4 = child.anInt239;
						if (child.enabledText.length() > 0)
							s = child.enabledText;
					} else {
						i4 = child.textColor;
						if (flag1 && child.anInt216 != 0)
							i4 = child.anInt216;
					}
					if (child.actionType == 6 && aBoolean1149) {
						s = "Please wait...";
						i4 = child.textColor;
					}
					if (DrawingArea.width == chatAreaIP.width || rsi.id == backDialogID) {
						if (i4 == 0xffff00)
							i4 = 255;
						if (i4 == 49152)
							i4 = 0xffffff;
					}
					if ((child.parentId == 1151) || (child.parentId == 12855)) {
						switch (i4) {
						case 16773120:
							i4 = 0xFE981F;
							break;
						case 7040819:
							i4 = 0xAF6A1A;
							break;
						}
					}
					for (int l6 = offsetY + font.baseCharacterHeight; s
							.length() > 0; l6 += font.baseCharacterHeight) {
						if (s.indexOf("%") != -1) {
							do {
								int k7 = s.indexOf("%1");
								if (k7 == -1)
									break;
								// if(rsi_1.id < 4000 || rsi_1.id > 5000 &&
								// rsi_1.id != 13921 && rsi_1.id != 13922 &&
								// rsi_1.id != 12171 && rsi_1.id != 12172)
								// s = s.substring(0, k7) +
								// methodR(extractInterfaceValues(rsi_1, 0)) +
								// s.substring(k7 + 2);
								// else
								s = s.substring(0, k7)
										+ interfaceIntToString(extractInterfaceValues(
												child, 0))
										+ s.substring(k7 + 2);
							} while (true);
							do {
								int l7 = s.indexOf("%2");
								if (l7 == -1)
									break;
								s = s.substring(0, l7)
										+ interfaceIntToString(extractInterfaceValues(
												child, 1))
										+ s.substring(l7 + 2);
							} while (true);
							do {
								int i8 = s.indexOf("%3");
								if (i8 == -1)
									break;
								s = s.substring(0, i8)
										+ interfaceIntToString(extractInterfaceValues(
												child, 2))
										+ s.substring(i8 + 2);
							} while (true);
							do {
								int j8 = s.indexOf("%4");
								if (j8 == -1)
									break;
								s = s.substring(0, j8)
										+ interfaceIntToString(extractInterfaceValues(
												child, 3))
										+ s.substring(j8 + 2);
							} while (true);
							do {
								int k8 = s.indexOf("%5");
								if (k8 == -1)
									break;
								s = s.substring(0, k8)
										+ interfaceIntToString(extractInterfaceValues(
												child, 4))
										+ s.substring(k8 + 2);
							} while (true);
						}
						int l8 = s.indexOf("\\n");
						String s1;
						if (l8 != -1) {
							s1 = s.substring(0, l8);
							s = s.substring(l8 + 2);
						} else {
							s1 = s;
							s = "";
						}
						if (child.centerText) {
							font.drawCenteredString(s1, offsetX + child.width / 2, l6, i4, child.shadeText ? 0 : -1);
						} else {
							font.drawBasicString(s1, offsetX, l6, i4, child.shadeText ? 0 : -1);
						}
					}
				} else if (child.type == 5) {
					Sprite sprite;
					Sprite hover;
					if (interfaceIsSelected(child)) {
						sprite = child.sprite2;
						hover = child.hoverSprite2;
					} else {
						sprite = child.sprite1;
						hover = child.hoverSprite1;
					}
					if (spellSelected == 1 && child.id == spellID && spellID != 0 && sprite != null) {
						sprite.drawOutlinedSprite(offsetX, offsetY, 0xffffff);
					} else {
						if(Autocast && child.id == autocastId)
							magicAuto.drawSprite(offsetX-3, offsetY-3);
						if (sprite != null) {
							sprite.drawSprite(offsetX, offsetY);
							if (child.delayTime != 0) {
								hover.drawSprite(offsetX, offsetY,child.buttonAlpha);
							}
						}
					}
					if (sprite != null) {
						if (child.drawsTransparent) {
							sprite.drawSprite(offsetX, offsetY);
						}
					}
					if (hover != null) {
						int offX = clientSize == 0 ? 4 : 0;
						int offY = clientSize == 0 ? 4 : 0;
						if (child.hoverRegion == 1 || child.isTabChild) {
							offX = 491;
							offY = 131;
						}
						int x = (clientSize == 0 ? xPosition + offX : 0)
								+ offsetX;
						int y = (clientSize == 0 ? yPosition + offY : 0)
								+ offsetY;
						if (hover != null) {
							if (super.mouseX >= x
									&& super.mouseX <= x + hover.myWidth
									&& super.mouseY >= y
									&& super.mouseY <= y + hover.myHeight) {
								if (child.delayTime != 0) {
									child.buttonAlpha += child.buttonAlpha < 250 ? child.delayTime
											: 0;
									child.buttonAlpha -= child.buttonAlpha > 250 ? 1
											: 0;
									if (child.buttonAlpha < 250) {
										hover.drawSprite(offsetX, offsetY,
												child.buttonAlpha);
									} else {
										hover.drawSprite(offsetX, offsetY);
									}
								} else {
									hover.drawSprite(offsetX, offsetY);
								}
							} else {
								if (child.delayTime != 0) {
									child.buttonAlpha -= child.buttonAlpha > 0 ? child.delayTime
											: 0;
								}
							}
						}
					}
				} else if (child.type == 6) {
					int k3 = Rasterizer.centerX;
					int j4 = Rasterizer.centerY;
					Rasterizer.centerX = offsetX + child.width / 2;
					Rasterizer.centerY = offsetY + child.height / 2;
					int i5 = Rasterizer.SINE[child.modelRotation1]
							* child.modelZoom >> 16;
					int l5 = Rasterizer.COSINE[child.modelRotation1]
							* child.modelZoom >> 16;
					boolean flag2 = interfaceIsSelected(child);
					int i7;
					if (flag2)
						i7 = child.anInt258;
					else
						i7 = child.animationId;
					Model model;
					if (i7 == -1) {
						model = child.method209(-1, -1, flag2, null);
					} else {
						Animation animDef = Animation.get(i7);
						if (animDef != null)
							model = child.method209(
									animDef != null ? animDef
											.getFrame2(child.anInt246) : -1,
									animDef != null ? animDef
											.getFrame(child.anInt246) : -1,
									flag2, animDef);
						else
							model = null;

					}
					if (model != null)
						model.method482(child.modelRotation2, 0,
								child.modelRotation1, 0, i5, l5);
					Rasterizer.centerX = k3;
					Rasterizer.centerY = j4;
				} else if (child.type == 7) {
					RSFontSystem font = child.textDrawingAreas;
					int k4 = 0;
					for (int j5 = 0; j5 < child.height; j5++) {
						for (int i6 = 0; i6 < child.width; i6++) {
							if (child.inventory[k4] > 0) {
								ItemDef itemDef = ItemDef
										.forId(child.inventory[k4] - 1);
								String s2 = itemDef.name;
								if (itemDef.stackable
										|| child.inventoryAmount[k4] != 1)
									s2 = s2
											+ " x"
											+ intToKOrMilLongName(child.inventoryAmount[k4]);
								int i9 = offsetX + i6
										* (115 + child.invSpritePadX);
								int k9 = offsetY + j5
										* (12 + child.invSpritePadY);
								if (child.centerText) {
									font.drawCenteredString(s2, i9
											+ child.width / 2, k9,
											child.textColor,
											child.shadeText ? 0 : -1);
								} else {
									font.drawCenteredString(s2, i9, k9,
											child.textColor,
											child.shadeText ? 0 : -1);
								}
							}
							k4++;
						}
					}
				} else if (child.type == 8
						&& (anInt1500 == child.id || anInt1044 == child.id || anInt1129 == child.id)
						&& anInt1501 == 50 && !menuOpen) {
					int boxWidth = 0;
					int boxHeight = 0;
					RSFont font = normalFont;
					for (String s1 = child.disabledText; s1.length() > 0;) {
						if (s1.indexOf("%") != -1) {
							do {
								int k7 = s1.indexOf("%1");
								if (k7 == -1)
									break;
								s1 = s1.substring(0, k7)
										+ interfaceIntToString(extractInterfaceValues(
												child, 0))
										+ s1.substring(k7 + 2);
							} while (true);
							do {
								int l7 = s1.indexOf("%2");
								if (l7 == -1)
									break;
								s1 = s1.substring(0, l7)
										+ interfaceIntToString(extractInterfaceValues(
												child, 1))
										+ s1.substring(l7 + 2);
							} while (true);
							do {
								int i8 = s1.indexOf("%3");
								if (i8 == -1)
									break;
								s1 = s1.substring(0, i8)
										+ interfaceIntToString(extractInterfaceValues(
												child, 2))
										+ s1.substring(i8 + 2);
							} while (true);
							do {
								int j8 = s1.indexOf("%4");
								if (j8 == -1)
									break;
								s1 = s1.substring(0, j8)
										+ interfaceIntToString(extractInterfaceValues(
												child, 3))
										+ s1.substring(j8 + 2);
							} while (true);
							do {
								int k8 = s1.indexOf("%5");
								if (k8 == -1)
									break;
								s1 = s1.substring(0, k8)
										+ interfaceIntToString(extractInterfaceValues(
												child, 4))
										+ s1.substring(k8 + 2);
							} while (true);
						}
						int l7 = s1.indexOf("\\n");
						String text;
						if (l7 != -1) {
							text = s1.substring(0, l7);
							s1 = s1.substring(l7 + 2);
						} else {
							text = s1;
							s1 = "";
						}
						int textLength = font.getTextWidth(text);
						if (textLength > boxWidth) {
							boxWidth = textLength;
						}
						boxHeight += font.anInt1497 + 1;
					}
					boxWidth += 6;
					boxHeight += 7;
					int xPos = (offsetX + child.width) - boxWidth;
					int yPos = (offsetY + child.height);
					if (xPos <= 0) {
						xPos += (boxWidth - offsetX);
					}
					/*if (boxHeight - child.height) {
						yPos -= (boxHeight + offsetY);
					}*/
					//System.out.println(xPos + "; " + yPos + " : " + boxWidth + "; " + boxHeight 
					//		+ " : " + offsetX + "; " + offsetY + " : " + child.width + "; " + child.height);
					DrawingArea.drawPixels(boxHeight, yPos, xPos, 0xFFFFA0,
							boxWidth);
					DrawingArea.fillPixels(xPos, boxWidth, boxHeight, 0, yPos);
					String s2 = child.disabledText;
					for (int j11 = yPos + font.anInt1497 + 2; s2.length() > 0; j11 += font.anInt1497 + 1) {// anInt1497
						if (s2.indexOf("%") != -1) {
							do {
								int k7 = s2.indexOf("%1");
								if (k7 == -1)
									break;
								s2 = s2.substring(0, k7)
										+ interfaceIntToString(extractInterfaceValues(
												child, 0))
										+ s2.substring(k7 + 2);
							} while (true);
							do {
								int l7 = s2.indexOf("%2");
								if (l7 == -1)
									break;
								s2 = s2.substring(0, l7)
										+ interfaceIntToString(extractInterfaceValues(
												child, 1))
										+ s2.substring(l7 + 2);
							} while (true);
							do {
								int i8 = s2.indexOf("%3");
								if (i8 == -1)
									break;
								s2 = s2.substring(0, i8)
										+ interfaceIntToString(extractInterfaceValues(
												child, 2))
										+ s2.substring(i8 + 2);
							} while (true);
							do {
								int j8 = s2.indexOf("%4");
								if (j8 == -1)
									break;
								s2 = s2.substring(0, j8)
										+ interfaceIntToString(extractInterfaceValues(
												child, 3))
										+ s2.substring(j8 + 2);
							} while (true);
							do {
								int k8 = s2.indexOf("%5");
								if (k8 == -1)
									break;
								s2 = s2.substring(0, k8)
										+ interfaceIntToString(extractInterfaceValues(
												child, 4))
										+ s2.substring(k8 + 2);
							} while (true);
						}
						int l11 = s2.indexOf("\\n");
						String s5;
						if (l11 != -1) {
							s5 = s2.substring(0, l11);
							s2 = s2.substring(l11 + 2);
						} else {
							s5 = s2;
							s2 = "";
						}
						if (child.centerText) {
							font.method382(yPos, xPos + child.width / 2, s5,
									j11, false);
						} else {
							if (s5.contains("\\r")) {
								String text = s5
										.substring(0, s5.indexOf("\\r"));
								String text2 = s5
										.substring(s5.indexOf("\\r") + 2);
								font.method389(false, xPos + 3, 0, text, j11);
								int rightX = boxWidth + xPos
										- font.getTextWidth(text2) - 2;
								font.method389(false, rightX, 0, text2, j11);
							} else
								font.method389(false, xPos + 3, 0, s5, j11);
						}
					}
				} else if (child.type == 9) {
					int xOffset = clientSize == 0 ? 4 : 0;
					int yOffset = clientSize == 0 ? 4 : 0;
					Sprite sprite = child.sprite1;
					Sprite sprite2 = child.sprite2;
					if (sprite != null)
						sprite.drawSprite(positionX, positionY);
					if (sprite2 != null
							&& super.mouseX >= xOffset + positionX
							&& super.mouseX <= xOffset + positionX
									+ child.width
							&& super.mouseY >= yOffset + positionY
							&& super.mouseY <= yOffset + positionY
									+ child.height)
						sprite2.drawSprite(positionX, positionY);
				} else if (child.type == 10) {
					if (child.dropDownItems.length > 0 && child.disabledText != null &&
							child.disabledText.length() > 0) {
						boolean listDropped = false;
						RSFont listText = smallText;
						// Sprite up = new Sprite("arrow 0");
						// Sprite down = new Sprite("arrow 1");
						// down.drawSprite(positionX, positionY);
						int listHeight = child.dropDownItems.length * 14;
						int listWidth = 40;
						for (int list1 = 0; list1 < child.dropDownItems.length; list1++) {
							int listLength = listText
									.getTextWidth(child.dropDownItems[list1]);
							int previousLength = list1 - 1 > 0 ? listText
									.getTextWidth(child.dropDownItems[list1 - 1])
									: 0;
							int nextLength = list1 + 1 < child.dropDownItems.length ? listText
									.getTextWidth(child.dropDownItems[list1 + 1])
									: 0;
							if (listLength > previousLength
									&& listLength > nextLength) {
								listWidth = listLength;
							}
						}
						/*DrawingArea474
								.drawUnfilledPixels(
										positionX + 16,
										positionY,
										listText.getTextWidth(child.dropDownItems[0]) + 4,
										16, 0);
						DrawingArea474
								.drawFilledPixels(
										positionX + 16,
										positionY + 1,
										listText.getTextWidth(child.dropDownItems[0]) + 4 - 1,
										14, 0x514941);*/
						listText.method385(0xffffff, child.disabledText,
								positionY + 13, positionX + 18);
						if (super.saveClickX >= (clientSize == 0 ? 4 : 0)
								+ positionX
								&& super.saveClickX <= (clientSize == 0 ? 4 : 0)
										+ positionX + listWidth
								&& super.saveClickY >= (clientSize == 0 ? 4 : 0)
										+ positionY
								&& super.saveClickY <= (clientSize == 0 ? 4 : 0)
										+ positionY + 16) {
							listDropped = true;
						} else {
							if (super.saveClickX >= (clientSize == 0 ? 4 : 0)
									+ positionX
									&& super.saveClickX <= (clientSize == 0 ? 4
											: 0) + positionX + listWidth
									&& super.saveClickY >= (clientSize == 0 ? 4
											: 0) + positionY
									&& super.saveClickY <= (clientSize == 0 ? 4
											: 0) + positionY + 16 + listHeight)
								listDropped = true;
							else
								listDropped = false;
						}
						if (listDropped) {
							// up.drawSprite(positionX, positionY);
							String itemName = child.dropDownItems[child.dropDownItems.length - 1];
							if (itemName.contains("g")
									|| itemName.contains("y")
									|| itemName.contains("q")
									|| itemName.contains("j")
									|| itemName.contains("p"))
								listHeight = (child.dropDownItems.length * 14) + 2;
							DrawingArea474.drawFilledPixels(positionX,
									positionY + 15, listWidth + 4, listHeight,
									0x514941);
							DrawingArea474.drawUnfilledPixels(positionX,
									positionY + 15, listWidth + 4, listHeight,
									0);
							for (int list2 = 0; list2 < child.dropDownItems.length; list2++) {
								int color = 0;
								int startX = (clientSize == 0 ? 4 : 0)
										+ positionX + 2;
								int endX = startX + listWidth - 2;
								int startY = (clientSize == 0 ? 4 : 0)
										+ positionY + 16 + (list2 * 14);
								int endY = startY + 11;
								if (super.mouseX >= startX
										&& super.mouseX <= endX
										&& super.mouseY >= startY
										&& super.mouseY <= endY) {
									color = 0xffffff;
								} else {
									color = child.textColor;
								}
								if (super.clickMode3 == 1
										&& super.saveClickX >= startX
										&& super.saveClickX <= endX
										&& super.saveClickY >= startY
										&& super.saveClickY <= endY) {
									clientAction = child.dropDownItemActions[list2];
									System.out.println("Client action=" + clientAction);
								}
								listText.method385(color,
										child.dropDownItems[list2], positionY
												+ 27 + (list2 * 14),
										positionX + 2);
							}
						}
					}
				} else if (child.type == 11) {
					/*smallText.method385(0xffffff, child.disabledText,
							positionY + 13, positionX + 18);
					int listHeight = child.menuActions.length * 14;
					int listWidth = 40;
					boolean listDropped = false;
					if (super.saveClickX >= (clientSize == 0 ? 4 : 0)
							+ positionX
							&& super.saveClickX <= (clientSize == 0 ? 4 : 0)
									+ positionX + listWidth
							&& super.saveClickY >= (clientSize == 0 ? 4 : 0)
									+ positionY
							&& super.saveClickY <= (clientSize == 0 ? 4 : 0)
									+ positionY + 16) {
						listDropped = true;
					} else {
						if (super.saveClickX >= (clientSize == 0 ? 4 : 0)
								+ positionX
								&& super.saveClickX <= (clientSize == 0 ? 4
										: 0) + positionX + listWidth
								&& super.saveClickY >= (clientSize == 0 ? 4
										: 0) + positionY
								&& super.saveClickY <= (clientSize == 0 ? 4
										: 0) + positionY + 16 + listHeight)
							listDropped = true;
						else
							listDropped = false;
					}
					if (listDropped) {
						for (int i = 0; i < child.menuActions.length; i++) {
							String action = child.menuActions[i];
							if (action != null) {
								menuActionName[menuActionRow] = action;
								menuActionID[menuActionRow] = 560;
								menuActionCmd2[menuActionRow] = child.menuId;
								menuActionRow++;
							}
						}
						drawMenu();
					}*/
				}
			if (child.fillWithColor) {
				int offX = clientSize == 0 ? 4 : 0;
				int offY = clientSize == 0 ? 4 : 0;
				if (child.hoverRegion == 1 || child.isTabChild) {
					offX = 491;
					offY = 131;
				}
				int x = (clientSize == 0 ? xPosition + offX : 32)
						+ offsetX;
				int y = (clientSize == 0 ? yPosition + offY : 41)
						+ offsetY;
				DrawingArea.drawPixels(child.sprite1.myHeight - 10, y - 36, x - 27, hexToColor(child.colorFill), child.sprite1.myWidth - 10);
			}
		}
		DrawingArea.setDrawingArea(i1, j1, k1, l1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Sprite magicAuto;
	public boolean Autocast = false;
	public int autocastId = 0;

	public void randomizeBackground(IndexedImage background) {
		int j = 256;
		for (int k = 0; k < anIntArray1190.length; k++)
			anIntArray1190[k] = 0;

		for (int l = 0; l < 5000; l++) {
			int i1 = (int) (Math.random() * 128D * (double) j);
			anIntArray1190[i1] = (int) (Math.random() * 256D);
		}
		for (int j1 = 0; j1 < 20; j1++) {
			for (int k1 = 1; k1 < j - 1; k1++) {
				for (int i2 = 1; i2 < 127; i2++) {
					int k2 = i2 + (k1 << 7);
					anIntArray1191[k2] = (anIntArray1190[k2 - 1]
							+ anIntArray1190[k2 + 1] + anIntArray1190[k2 - 128] + anIntArray1190[k2 + 128]) / 4;
				}

			}
			int ai[] = anIntArray1190;
			anIntArray1190 = anIntArray1191;
			anIntArray1191 = ai;
		}
		if (background != null) {
			int l1 = 0;
			for (int j2 = 0; j2 < background.anInt1453; j2++) {
				for (int l2 = 0; l2 < background.anInt1452; l2++)
					if (background.myPixels[l1++] != 0) {
						int i3 = l2 + 16 + background.anInt1454;
						int j3 = j2 + 16 + background.anInt1455;
						int k3 = i3 + (j3 << 7);
						anIntArray1190[k3] = 0;
					}
			}
		}
	}

	public void updatePlayerMasks(int mask, int index, JagexBuffer stream, Player player) {
		if ((mask & 0x400) != 0) { //forced movement, TODO: write server sided- it's writing wrong values.
			player.anInt1543 = stream.readByteS();
			player.anInt1545 = stream.readByteS();
			player.anInt1544 = stream.readByteS();
			player.anInt1546 = stream.readByteS();
			player.anInt1547 = stream.readLEShortA() + loopCycle;
			player.anInt1548 = stream.readShortA() + loopCycle;
			player.anInt1549 = stream.readByteS();
			player.resetRegionPosition();
		}
		if ((mask & 0x100) != 0) { //graphic
			player.graphicId = stream.readLEShort();
			int height = stream.readInt();
			player.graphicHeight = height >> 16;
			player.graphicDelay = loopCycle + (height & 0xffff);
			player.anInt1521 = 0;
			player.anInt1522 = 0;
			if (player.graphicDelay > loopCycle)
				player.anInt1521 = -1;
			if (player.graphicId == 65535)
				player.graphicId = -1;
		}
		if ((mask & 8) != 0) { //animation
			int animationId = stream.readLEShort();
			if (animationId == 65535)
				animationId = -1;
			int delay = stream.readByteC();
			if (animationId == player.animationId && animationId != -1) {
				int i3 = Animation.anims[animationId].anInt365;
				if (i3 == 1) {
					player.anInt1527 = 0;
					player.anInt1528 = 0;
					player.animationDelay = delay;
					player.anInt1530 = 0;
				}
				if (i3 == 2)
					player.anInt1530 = 0;
			} else if (animationId == -1
					|| player.animationId == -1 || animationId > Animation.anims.length
					|| Animation.anims[animationId].anInt359 >= Animation.anims[player.animationId].anInt359) {
				player.animationId = animationId;
				player.anInt1527 = 0;
				player.anInt1528 = 0;
				player.animationDelay = delay;
				player.anInt1530 = 0;
				player.anInt1542 = player.smallXYIndex;
			}
		}
		if ((mask & 4) != 0) { //force chat
			player.textSpoken = stream.readString();
			if (player.textSpoken.charAt(0) == '`') {
				player.textSpoken = player.textSpoken.substring(1);
				pushMessage(player.name, player.textSpoken, 2);
			} else if (player == myPlayer) {
				pushMessage(player.name, player.textSpoken, 2);
			}
			player.textColour = 0;
			player.textEffect = 0;
			player.textCycle = 150;
		}
		if ((mask & 0x80) != 0) { //chat
			int colourAndEffects = packet.readLEShort();
			int playerRights = packet.readUnsignedByte();
			int textBytes = packet.readByteC();
			int textByteArray = packet.currentOffset;
			if (player.name != null && player.visible) {
				long name = TextUtils.longForName(player.name);
				boolean flag = false;
				if (playerRights <= 1) {
					for (int i4 = 0; i4 < ignoreCount; i4++) {
						if (ignoreListNames[i4] != name)
							continue;
						flag = true;
						break;
					}

				}
				if (!flag && anInt1251 == 0)
					try {
						aStream_834.currentOffset = 0;
						packet.method442(textBytes, 0, aStream_834.buffer);
						aStream_834.currentOffset = 0;
						String message = TextInput.method525(textBytes, aStream_834);
						player.textSpoken = message;
						player.textColour = colourAndEffects >> 8;
						player.rights = playerRights;
						player.textEffect = colourAndEffects & 0xff;
						player.textCycle = 150;
						pushMessage(getPrefix(playerRights) + player.name, message, 2);
					} catch (Exception exception) {
						signlink.reporterror("chat decoding 2");
					}
			}
			packet.currentOffset = textByteArray + textBytes;
		}
		if ((mask & 1) != 0) { //interacting entity
			player.interactingEntity = stream.readLEShort();
			if (player.interactingEntity == 65535)
				player.interactingEntity = -1;
		}
		if ((mask & 0x10) != 0) { //appearance
			int packetBufferWriterIndex = stream.readByteC();
			byte packetBuffer[] = new byte[packetBufferWriterIndex];
			JagexBuffer buffer = new JagexBuffer(packetBuffer);
			stream.readBytes(packetBufferWriterIndex, 0, packetBuffer);
			aStreamArray895s[index] = buffer;
			player.updatePlayer(buffer);
		}
		if ((mask & 2) != 0) { //face position
			player.facePositionX = stream.readLEShortA();
			player.facePositionY = stream.readLEShort();
		}
		if ((mask & 0x20) != 0) { //single hit
			int damage = packet.readLEShort();
			int absorbed = packet.readLEShortA();
			boolean maxHit = false;
			int combatStyle = -1;
			String attacker = null;
			String victim = null;
			maxHit = false;
			combatStyle = packet.readByte();
			attacker = packet.readString();
			victim = packet.readString();
			int hitMark = packet.readByteS();
			player.updateHitData(hitMark, combatStyle, damage, absorbed, maxHit, attacker, victim, loopCycle);
			player.loopCycleStatus = loopCycle + 300;
			player.currentHealth = stream.readUnsignedShort();
			player.maxHealth = stream.readUnsignedShort();
		}
		if ((mask & 0x200) != 0) { //double hit
			int damage = packet.readLEShort();
			int absorbed = packet.readLEShortA();
			boolean maxHit = false;
			int combatStyle = -1;
			String attacker = null;
			String victim = null;
			maxHit = false;
			combatStyle = packet.readByte();
			attacker = packet.readString();
			victim = packet.readString();
			int hitMark = packet.readByteS();
			player.updateHitData(hitMark, combatStyle, damage, absorbed, maxHit, attacker, victim, loopCycle);
			player.loopCycleStatus = loopCycle + 300;
			player.currentHealth = stream.readUnsignedShort();
			player.maxHealth = stream.readUnsignedShort();
		}
	}

	public void method108() {
		try {
			int j = myPlayer.x + cameraOffsetX;
			int k = myPlayer.y + cameraOffsetY;
			if (anInt1014 - j < -500 || anInt1014 - j > 500
					|| anInt1015 - k < -500 || anInt1015 - k > 500) {
				anInt1014 = j;
				anInt1015 = k;
			}
			if (anInt1014 != j)
				anInt1014 += (j - anInt1014) / 16;
			if (anInt1015 != k)
				anInt1015 += (k - anInt1015) / 16;
			if (super.keyArray[1] == 1)
				anInt1186 += (-24 - anInt1186) / 2;
			else if (super.keyArray[2] == 1)
				anInt1186 += (24 - anInt1186) / 2;
			else
				anInt1186 /= 2;
			if (super.keyArray[3] == 1)
				anInt1187 += (12 - anInt1187) / 2;
			else if (super.keyArray[4] == 1)
				anInt1187 += (-12 - anInt1187) / 2;
			else
				anInt1187 /= 2;
			viewRotation = viewRotation + anInt1186 / 2 & 0x7ff;
			anInt1184 += anInt1187 / 2;
			if (anInt1184 < 128)
				anInt1184 = 128;
			if (anInt1184 > 383)
				anInt1184 = 383;
			int l = anInt1014 >> 7;
			int i1 = anInt1015 >> 7;
			int j1 = getHeight(plane, anInt1015, anInt1014);
			int k1 = 0;
			if (l > 3 && i1 > 3 && l < 100 && i1 < 100) {
				for (int l1 = l - 4; l1 <= l + 4; l1++) {
					for (int k2 = i1 - 4; k2 <= i1 + 4; k2++) {
						int l2 = plane;
						if (l2 < 3 && (byteGroundArray[1][l1][k2] & 2) == 2)
							l2++;
						int i3 = j1 - intGroundArray[l2][l1][k2];
						if (i3 > k1)
							k1 = i3;
					}

				}

			}
			anInt1005++;
			if (anInt1005 > 1512) {
				anInt1005 = 0;
				stream.createFrame(77);
				stream.writeByte(0);
				int i2 = stream.currentOffset;
				stream.writeByte((int) (Math.random() * 256D));
				stream.writeByte(101);
				stream.writeByte(233);
				stream.writeShort(45092);
				if ((int) (Math.random() * 2D) == 0)
					stream.writeShort(35784);
				stream.writeByte((int) (Math.random() * 256D));
				stream.writeByte(64);
				stream.writeByte(38);
				stream.writeShort((int) (Math.random() * 65536D));
				stream.writeShort((int) (Math.random() * 65536D));
				stream.writeBytes(stream.currentOffset - i2);
			}
			int j2 = k1 * 192;
			if (j2 > 0x17f00)
				j2 = 0x17f00;
			if (j2 < 32768)
				j2 = 32768;
			if (j2 > anInt984) {
				anInt984 += (j2 - anInt984) / 24;
				return;
			}
			if (j2 < anInt984) {
				anInt984 += (j2 - anInt984) / 80;
			}
		} catch (Exception _ex) {
			signlink.reporterror("glfc_ex " + myPlayer.x + "," + myPlayer.y
					+ "," + anInt1014 + "," + anInt1015 + "," + currentRegionX + ","
					+ currentRegionY + "," + regionAbsBaseX + "," + regionAbsBaseY);
			throw new RuntimeException("eek");
		}
	}

	public void processDrawing() {
		if (rsAlreadyLoaded || loadingError || genericLoadingError) {
			showErrorScreen();
			return;
		}
		anInt1061++;
		if (!loggedIn) {
			displayTitleScreen(false);
		} else {
			drawGameScreen();
		}
		anInt1213 = 0;
	}

	public boolean isFriendOrSelf(String s) {
		if (s == null)
			return false;
		for (int i = 0; i < friendsCount; i++)
			if (s.equalsIgnoreCase(friendsList[i]))
				return true;
		return s.equalsIgnoreCase(myPlayer.name);
	}

	public static String combatDiffColor(int i, int j) {
		int k = i - j;
		if (k < -9)
			return "@red@";
		if (k < -6)
			return "@or3@";
		if (k < -3)
			return "@or2@";
		if (k < 0)
			return "@or1@";
		if (k > 9)
			return "@gre@";
		if (k > 6)
			return "@gr3@";
		if (k > 3)
			return "@gr2@";
		if (k > 0)
			return "@gr1@";
		else
			return "@yel@";
	}

	public void setWaveVolume(int i) {
		signlink.wavevol = i;
	}

	public boolean displayPreferences = false;

	public void drawPreferences() {
		int centerX = clientSize == 0 ? 256 + 4 : clientWidth / 2;
		int centerY = clientSize == 0 ? 167 + 4 : clientHeight / 2;
		drawTitleBox(centerX - (225 / 2), centerY - (settingHeight / 2), 225,
				settingHeight, 1);
		titleButton[3].drawSprite(centerX - (225 / 2) + 206, centerY
				- (settingHeight / 2) + 3);
		if (mouseInRegion(centerX - (225 / 2) + 206, centerY
				- (settingHeight / 2) + 3, centerX - (225 / 2) + 222, centerY
				- (settingHeight / 2) + 19))
			titleButton[7].drawSprite(centerX - (225 / 2) + 206, centerY
					- (settingHeight / 2) + 3);
		normalFont.method382(0xffffff, centerX, "Client Settings", centerY
				- (settingHeight / 2) + 16, true);
		/*
		 * if (!settingsEnabled) { smallText.method382(0xff9933, centerX,
		 * "There are no settings", centerY - (settingHeight / 2) + 40, true);
		 * smallText.method382(0xff9933, centerX, "that have been enabled.",
		 * centerY - (settingHeight / 2) + 50, true); } else { for (int i1 = 0,
		 * yPos = centerY - (settingHeight / 2) + 40; i1 < settingNames.length;
		 * i1++, yPos += 16) { smallText.method389(true, centerX - 85, 0xffffff,
		 * settingNames[i1], yPos); if (settingEnabled[i1]) {
		 * titleButton[9].drawSprite(centerX - 102, yPos - 13); } else {
		 * titleButton[8].drawSprite(centerX - 102, yPos - 13); } } }
		 */
	}

	public void draw3dScreen() {
		drawSplitpublicChat();
		alertHandler.processAlerts();
		if (crossType == 1) {
			crosses[crossIndex / 100]
					.drawSprite(crossX - 8 - 4, crossY - 8 - 4);
			anInt1142++;
			if (anInt1142 > 67) {
				anInt1142 = 0;
				stream.createFrame(78);
			}
		}
		if (crossType == 2) {
			crosses[4 + crossIndex / 100].drawSprite(crossX - 8 - 4, crossY - 8 - 4);
		}
		if (walkableInterface != -1) {
			method119(anInt945, walkableInterface);
			drawInterface(clientSize == 0 ? 0 : (clientWidth / 2) - 256, clientSize == 0 ? 0 : (clientHeight / 2) - 167, 0, RSInterface.interfaceCache[walkableInterface]);
		}
		if (openInterfaceID != -1) {
			method119(anInt945, openInterfaceID);
			drawInterface(clientSize == 0 ? 0 : (clientWidth / 2) - 256, clientSize == 0 ? 0 : (clientHeight / 2) - 167, 0, RSInterface.interfaceCache[openInterfaceID]);
		}
		method70();
		if (!menuOpen) {
			processRightClick();
			drawTooltip();
		} else if (menuScreenArea == 0)
			drawMenu();
		if (screenMultiIconId == 1) {
			multiOverlay.drawSprite(472, 296);
		}
		if (fpsOn) {
			char c = '\u01FB';
			int k = 20;
			int i1 = 0xffff00;
			if (super.fps < 15)
				i1 = 0xff0000;
			normalFont.method380("Fps:" + super.fps, c, i1, k);
			k += 15;
			Runtime runtime = Runtime.getRuntime();
			int j1 = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			i1 = 0xffff00;
			if (j1 > 0x2000000 && lowMemory) {
				i1 = 0xff0000;
			}
			normalFont.method380("Mem:" + j1 + "k", c, 0xffff00, k);
			k += 15;
		}
		playerX = regionAbsBaseX + (myPlayer.x - 6 >> 7);
		playerY = regionAbsBaseY + (myPlayer.y - 6 >> 7);
		if (clientData) {
			if (super.fps < 15) {
			}
			normalFont.method385(0xffff00, "Fps: " + super.fps, 285, 5);
			Runtime runtime = Runtime.getRuntime();
			int j1 = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			if (j1 > 0x2000000 && lowMemory) {
			}
			// normalFont.method385(0xffff00, "Interface: " + openInterfaceID +
			// "", 283, 5);
			normalFont.method385(0xffff00, "Memory usage: " + j1 + "k", 299, 5);
			normalFont.method385(0xffff00, "Mouse X: " + super.mouseX
					+ " , Mouse Y: " + super.mouseY, 314, 5);
			normalFont.method385(0xffff00, "Position: " + playerX + ", "
					+ playerY, 329, 5);
		}
		if (systemUpdateTimer != 0) {
			int seconds = systemUpdateTimer / 50;
			int minutes = seconds / 60;
			seconds %= 60;
			if (seconds < 10)
				normalFont.method385(0xffff00, "System update packet: " + minutes
						+ ":0" + seconds, 329, 4);
			else
				normalFont.method385(0xffff00, "System update packet: " + minutes
						+ ":" + seconds, 329, 4);
			anInt849++;
			if (anInt849 > 75) {
				anInt849 = 0;
				stream.createFrame(148);
			}
		}
	}

	public void addIgnore(long l) {
		try {
			if (l == 0L)
				return;
			if (ignoreCount >= 100) {
				pushMessage(
						"", "Your ignore list has reached it's maximum of 100.",
						0);
				return;
			}
			String s = TextClass.fixName(TextClass.nameForLong(l));
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListNames[j] == l) {
					pushMessage("", s + " is already on your ignore list!", 0);
					return;
				}
			for (int k = 0; k < friendsCount; k++)
				if (friendsListNames[k] == l) {
					pushMessage("", "Please remove " + s
									+ " from your friend list first.", 0);
					return;
				}

			ignoreListNames[ignoreCount++] = l;
			needDrawTabArea = true;
			stream.createFrame(133);
			stream.writeLong(l);
			return;
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("45688, " + l + ", " + 4 + ", "
					+ runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	public void method114() {
		for (int i = -1; i < playerCount; i++) {
			int j;
			if (i == -1)
				j = myPlayerIndex;
			else
				j = playerIndices[i];
			Player player = playerArray[j];
			if (player != null)
				method96(player);
		}

	}

	public void method115() {
		if (loadingStage == 2) {
			for (ObjectSpawnNode class30_sub1 = (ObjectSpawnNode) aClass19_1179
					.head(); class30_sub1 != null; class30_sub1 = (ObjectSpawnNode) aClass19_1179
					.next()) {
				if (class30_sub1.anInt1294 > 0)
					class30_sub1.anInt1294--;
				if (class30_sub1.anInt1294 == 0) {
					if (class30_sub1.anInt1299 < 0
							|| SceneGraph.method178(class30_sub1.anInt1299,
									class30_sub1.anInt1301)) {
						method142(class30_sub1.y,
								class30_sub1.height, class30_sub1.anInt1300,
								class30_sub1.anInt1301, class30_sub1.x,
								class30_sub1.type, class30_sub1.anInt1299);
						class30_sub1.remove();
					}
				} else {
					if (class30_sub1.anInt1302 > 0)
						class30_sub1.anInt1302--;
					if (class30_sub1.anInt1302 == 0
							&& class30_sub1.x >= 1
							&& class30_sub1.y >= 1
							&& class30_sub1.x <= 102
							&& class30_sub1.y <= 102
							&& (class30_sub1.anInt1291 < 0 || SceneGraph
									.method178(class30_sub1.anInt1291,
											class30_sub1.anInt1293))) {
						method142(class30_sub1.y,
								class30_sub1.height, class30_sub1.anInt1292,
								class30_sub1.anInt1293, class30_sub1.x,
								class30_sub1.type, class30_sub1.anInt1291);
						class30_sub1.anInt1302 = -1;
						if (class30_sub1.anInt1291 == class30_sub1.anInt1299
								&& class30_sub1.anInt1299 == -1)
							class30_sub1.remove();
						else if (class30_sub1.anInt1291 == class30_sub1.anInt1299
								&& class30_sub1.anInt1292 == class30_sub1.anInt1300
								&& class30_sub1.anInt1293 == class30_sub1.anInt1301)
							class30_sub1.remove();
					}
				}
			}

		}
	}

	public void determineMenuSize() {
		int i = boldFont.getTextWidth("Choose Option");
		for (int j = 0; j < menuActionRow; j++) {
			int k = boldFont.getTextWidth(menuActionName[j]);
			if (k > i)
				i = k;
		}
		i += 8;
		int l = 15 * menuActionRow + 21;
		if (clientSize == 0) {
			if (super.saveClickX > 4 && super.saveClickY > 4
					&& super.saveClickX < 516 && super.saveClickY < 338) {
				int i1 = super.saveClickX - 4 - i / 2;
				if (i1 + i > 512)
					i1 = 512 - i;
				if (i1 < 0)
					i1 = 0;
				int l1 = super.saveClickY - 4;
				if (l1 + l > 334)
					l1 = 334 - l;
				if (l1 < 0)
					l1 = 0;
				menuOpen = true;
				menuScreenArea = 0;
				menuOffsetX = i1;
				menuOffsetY = l1;
				menuWidth = i;
				menuHeight = 15 * menuActionRow + 22;
			}
			if (super.saveClickX > 519 && super.saveClickY > 168
					&& super.saveClickX < 765 && super.saveClickY < 503) {
				int j1 = super.saveClickX - 519 - i / 2;
				if (j1 < 0)
					j1 = 0;
				else if (j1 + i > 245)
					j1 = 245 - i;
				int i2 = super.saveClickY - 168;
				if (i2 < 0)
					i2 = 0;
				else if (i2 + l > 333)
					i2 = 333 - l;
				menuOpen = true;
				menuScreenArea = 1;
				menuOffsetX = j1;
				menuOffsetY = i2;
				menuWidth = i;
				menuHeight = 15 * menuActionRow + 22;
			}
			if (super.saveClickX > 0 && super.saveClickY > 338
					&& super.saveClickX < 516 && super.saveClickY < 503) {
				int k1 = super.saveClickX - 0 - i / 2;
				if (k1 < 0)
					k1 = 0;
				else if (k1 + i > 516)
					k1 = 516 - i;
				int j2 = super.saveClickY - 338;
				if (j2 < 0)
					j2 = 0;
				else if (j2 + l > 165)
					j2 = 165 - l;
				menuOpen = true;
				menuScreenArea = 2;
				menuOffsetX = k1;
				menuOffsetY = j2;
				menuWidth = i;
				menuHeight = 15 * menuActionRow + 22;
			}
			// if(super.saveClickX > 0 && super.saveClickY > 338 &&
			// super.saveClickX < 516 && super.saveClickY < 503) {
			if (super.saveClickX > 519 && super.saveClickY > 0
					&& super.saveClickX < 765 && super.saveClickY < 168) {
				int j1 = super.saveClickX - 519 - i / 2;
				if (j1 < 0)
					j1 = 0;
				else if (j1 + i > 245)
					j1 = 245 - i;
				int i2 = super.saveClickY - 0;
				if (i2 < 0)
					i2 = 0;
				else if (i2 + l > 168)
					i2 = 168 - l;
				menuOpen = true;
				menuScreenArea = 3;
				menuOffsetX = j1;
				menuOffsetY = i2;
				menuWidth = i;
				menuHeight = 15 * menuActionRow + 22;
			}
		} else {
			if (super.saveClickX > 0 && super.saveClickY > 0
					&& super.saveClickX < clientWidth
					&& super.saveClickY < clientHeight) {
				int i1 = super.saveClickX - 0 - i / 2;
				if (i1 + i > clientWidth)
					i1 = clientWidth - i;
				if (i1 < 0)
					i1 = 0;
				int l1 = super.saveClickY - 0;
				if (l1 + l > clientHeight)
					l1 = clientHeight - l;
				if (l1 < 0)
					l1 = 0;
				menuOpen = true;
				menuScreenArea = 0;
				menuOffsetX = i1;
				menuOffsetY = l1;
				menuWidth = i;
				menuHeight = 15 * menuActionRow + 22;
			}
		}
	}

	public void updateMovement(JagexBuffer stream) {
		stream.initBitAccess();
		int firstBit = stream.readBits(1);
		if (firstBit == 0)
			return;
		int secondBit = stream.readBits(2);
		if (secondBit == 0) {
			anIntArray894[anInt893++] = myPlayerIndex;
			return;
		}
		if (secondBit == 1) {
			int l = stream.readBits(3);
			myPlayer.moveInDir(false, l);
			int k1 = stream.readBits(1);
			if (k1 == 1)
				anIntArray894[anInt893++] = myPlayerIndex;
			return;
		}
		if (secondBit == 2) {
			int i1 = stream.readBits(3);
			myPlayer.moveInDir(true, i1);
			int l1 = stream.readBits(3);
			myPlayer.moveInDir(true, l1);
			int j2 = stream.readBits(1);
			if (j2 == 1)
				anIntArray894[anInt893++] = myPlayerIndex;
			return;
		}
		if (secondBit == 3) {
			plane = stream.readBits(2);
			int teleport = stream.readBits(1);
			int update = stream.readBits(1);
			if (update == 1)
				anIntArray894[anInt893++] = myPlayerIndex;
			int localY = stream.readBits(7);
			int localX = stream.readBits(7);
			myPlayer.setPosition(localX, localY, teleport == 1);
		}
	}

	public void nullLoader() {
		aBoolean831 = false;
		while (drawingFlames) {
			aBoolean831 = false;
			try {
				Thread.sleep(50L);
			} catch (Exception _ex) {
			}
		}
		//title = null;
		aBackgroundArray1152s = null;
		anIntArray850 = null;
		anIntArray851 = null;
		anIntArray852 = null;
		anIntArray853 = null;
		anIntArray1190 = null;
		anIntArray1191 = null;
		anIntArray828 = null;
		anIntArray829 = null;
		aClass30_Sub2_Sub1_Sub1_1201 = null;
		aClass30_Sub2_Sub1_Sub1_1202 = null;
	}

	public boolean method119(int i, int j) {
		boolean flag1 = false;
		RSInterface rsi = RSInterface.interfaceCache[j];
		if (rsi != null && rsi.children != null) {
			for (int index = 0; index < rsi.children.length; index++) {
				if (rsi.children[index] == -1)
					break;
				RSInterface child = RSInterface.interfaceCache[rsi.children[index]];
				if (child.type == 1)
					flag1 |= method119(i, child.id);
				if (child.type == 6 && (child.animationId != -1 || child.anInt258 != -1)) {
					boolean flag2 = interfaceIsSelected(child);
					int l;
					if (flag2)
						l = child.anInt258;
					else
						l = child.animationId;
					if (l != -1) {
						Animation animation = Animation.anims[l];
						for (child.anInt208 += i; child.anInt208 > animation.method258(child.anInt246);) {
							child.anInt208 -= animation.method258(child.anInt246) + 1;
							child.anInt246++;
							if (child.anInt246 >= animation.anInt352) {
								child.anInt246 -= animation.anInt356;
								if (child.anInt246 < 0 || child.anInt246 >= animation.anInt352)
									child.anInt246 = 0;
							}
							flag1 = true;
						}
					}
				}
			}
		}
		return flag1;
	}

	public int method120() {
		int j = 3;
		if (yCameraCurve < 310) {
			int k = xCameraPos >> 7;
			int l = yCameraPos >> 7;
			int i1 = myPlayer.x >> 7;
			int j1 = myPlayer.y >> 7;
			if ((byteGroundArray[plane][k][l] & 4) != 0)
				j = plane;
			int k1;
			if (i1 > k)
				k1 = i1 - k;
			else
				k1 = k - i1;
			int l1;
			if (j1 > l)
				l1 = j1 - l;
			else
				l1 = l - j1;
			if (k1 > l1) {
				int i2 = (l1 * 0x10000) / k1;
				int k2 = 32768;
				while (k != i1) {
					if (k < i1)
						k++;
					else if (k > i1)
						k--;
					if ((byteGroundArray[plane][k][l] & 4) != 0)
						j = plane;
					k2 += i2;
					if (k2 >= 0x10000) {
						k2 -= 0x10000;
						if (l < j1)
							l++;
						else if (l > j1)
							l--;
						if ((byteGroundArray[plane][k][l] & 4) != 0)
							j = plane;
					}
				}
			} else {
				int j2 = (k1 * 0x10000) / l1;
				int l2 = 32768;
				while (l != j1) {
					if (l < j1)
						l++;
					else if (l > j1)
						l--;
					if ((byteGroundArray[plane][k][l] & 4) != 0)
						j = plane;
					l2 += j2;
					if (l2 >= 0x10000) {
						l2 -= 0x10000;
						if (k < i1)
							k++;
						else if (k > i1)
							k--;
						if ((byteGroundArray[plane][k][l] & 4) != 0)
							j = plane;
					}
				}
			}
		}
		if ((byteGroundArray[plane][myPlayer.x >> 7][myPlayer.y >> 7] & 4) != 0)
			j = plane;
		return j;
	}

	public int method121() {
		int j = getHeight(plane, yCameraPos, xCameraPos);
		if (j - zCameraPos < 800
				&& (byteGroundArray[plane][xCameraPos >> 7][yCameraPos >> 7] & 4) != 0)
			return plane;
		else
			return 3;
	}

	public void delIgnore(long l) {
		try {
			if (l == 0L)
				return;
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListNames[j] == l) {
					ignoreCount--;
					needDrawTabArea = true;
					System.arraycopy(ignoreListNames, j + 1,
							ignoreListNames, j, ignoreCount - j);

					stream.createFrame(74);
					stream.writeLong(l);
					return;
				}

			return;
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("47229, " + 3 + ", " + l + ", "
					+ runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	public void chatJoin(long l) {
		try {
			if (l == 0L)
				return;
			stream.createFrame(60);
			stream.writeLong(l);
			return;
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("47229, " + 3 + ", " + l + ", "
					+ runtimeexception.toString());
		}
		throw new RuntimeException();

	}

	public String getParameter(String s) {
		if (signlink.mainapp != null)
			return signlink.mainapp.getParameter(s);
		else
			return super.getParameter(s);
	}

	public void adjustVolume(boolean flag, int i) {
		signlink.midivol = i;
		if (flag)
			signlink.midi = "voladjust";
	}

	public int extractInterfaceValues(RSInterface rsi, int value) {
		if (rsi.valueIndexArray == null || value >= rsi.valueIndexArray.length)
			return -2;
		try {
			int valueArray[] = rsi.valueIndexArray[value];
			int k = 0;
			int l = 0;
			int i1 = 0;
			do {
				int type = valueArray[l++];
				int k1 = 0;
				byte byte0 = 0;
				if (type == 0)
					return k;
				if (type == 1)
					k1 = currentLevel[valueArray[l++]];
				if (type == 2) {
					int index = l++;
					int level = maxLevel[valueArray[index]];
					if (valueArray[index] == 3 || valueArray[index] == 5)
						level = (int) (level / 10);
					k1 = level;
				}
				if (type == 3)
					k1 = currentExp[valueArray[l++]];
				if (type == 4) {
					RSInterface rsi_1 = RSInterface.interfaceCache[valueArray[l++]];
					int k2 = valueArray[l++];
					if (k2 >= 0 && k2 < ItemDef.totalItems
							&& (!ItemDef.forId(k2).membersObject || isMembers)) {
						for (int j3 = 0; j3 < rsi_1.inventory.length; j3++)
							if (rsi_1.inventory[j3] == k2 + 1)
								k1 += rsi_1.inventoryAmount[j3];

					}
				}
				if (type == 5)
					k1 = variousSettings[valueArray[l++]];
				if (type == 6)
					k1 = anIntArray1019[maxLevel[valueArray[l++]] - 1];
				if (type == 7)
					k1 = (variousSettings[valueArray[l++]] * 100) / 46875;
				if (type == 8)
					k1 = myPlayer.combatLevel;
				if (type == 9) {
					for (int skillId = 0; skillId < SkillConstants.skillsCount; skillId++) {
						if (SkillConstants.skillEnabled[skillId]) {
							k1 += maxLevel[skillId];
						}
					}
				}
				if (type == 10) {
					RSInterface class9_2 = RSInterface.interfaceCache[valueArray[l++]];
					int l2 = valueArray[l++] + 1;
					if (l2 >= 0 && l2 < ItemDef.totalItems
							&& (!ItemDef.forId(l2).membersObject || isMembers)) {
						for (int k3 = 0; k3 < class9_2.inventory.length; k3++) {
							if (class9_2.inventory[k3] != l2)
								continue;
							k1 = 0x3b9ac9ff;
							break;
						}

					}
				}
				if (type == 11)
					k1 = runEnergy;
				if (type == 12)
					k1 = weight;
				if (type == 13) {
					int i2 = variousSettings[valueArray[l++]];
					int i3 = valueArray[l++];
					k1 = (i2 & 1 << i3) == 0 ? 0 : 1;
				}
				if (type == 14) {
					int j2 = valueArray[l++];
					VarBit varBit = VarBit.cache[j2];
					int l3 = varBit.anInt648;
					int i4 = varBit.anInt649;
					int j4 = varBit.anInt650;
					int k4 = anIntArray1232[j4 - i4];
					k1 = variousSettings[l3] >> i4 & k4;
				}
				if (type == 15)
					byte0 = 1;
				if (type == 16)
					byte0 = 2;
				if (type == 17)
					byte0 = 3;
				if (type == 18)
					k1 = (myPlayer.x >> 7) + regionAbsBaseX;
				if (type == 19)
					k1 = (myPlayer.y >> 7) + regionAbsBaseY;
				if (type == 20)
					k1 = valueArray[l++];
				if (byte0 == 0) {
					if (i1 == 0)
						k += k1;
					if (i1 == 1)
						k -= k1;
					if (i1 == 2 && k1 != 0)
						k /= k1;
					if (i1 == 3)
						k *= k1;
					i1 = 0;
				} else {
					i1 = byte0;
				}
			} while (true);
		} catch (Exception _ex) {
			return -1;
		}
	}

	private void drawTooltip() {
		String s;
		if(menuActionRow < 2 && itemSelected == 0 && spellSelected == 0) {
			return;
		}
		if(itemSelected == 1 && menuActionRow < 2)
			s = "Use " + selectedItemName + "-> ";
		else if(spellSelected == 1 && menuActionRow < 2)
			s = spellTooltip + "...";
		else
			s = menuActionName[menuActionRow - 1];
		if(menuActionRow > 2)
			s = s + "@whi@ / " + (menuActionRow - 2) + " more options"; 
		boldFont.method390(4, 0xffffff, s, loopCycle / 1000, 15);
		setMouseCursor(menuActionName[menuActionRow - 1]);
	}
	
	private void setMouseCursor(String tooltip) {
		boolean hasFoundCursor = false;
		for (int i = 0; i < CURSOR_INFO.length; i++) {
			if (tooltip.startsWith(CURSOR_INFO[i])) {
				super.setCursor(i);
				hasFoundCursor = true;
				break;
			} 
		}
		if (!hasFoundCursor)
			super.setCursor(0); 
	}
	
	public static String chatColorHex = "00FFFF";
	
	public static int getChatColor() {
		int convertHexCode = Integer.parseInt(chatColorHex, 16);
		return convertHexCode;
	}
	
	public static int hexToColor(String hex) {
		int convertHexCode = Integer.parseInt(hex, 16);
		return convertHexCode;
	}

	public int getOrbX(int orb) {
		switch (orb) {
		case 0:
			return !isFixed() ? clientWidth - 212 : 172;
		case 1:
			return !isFixed() ? clientWidth - 215 : 188;
		case 2:
			return !isFixed() ? clientWidth - 203 : 188;
		case 3:
			return !isFixed() ? clientWidth - 180 : 172;
		}
		return 0;
	}

	public int getOrbY(int orb) {
		switch (orb) {
		case 0:
			return !isFixed() ? 39 : 15;
		case 1:
			return !isFixed() ? 73 : 54;
		case 2:
			return !isFixed() ? 107 : 93;
		case 3:
			return !isFixed() ? 141 : 128;
		}
		return 0;
	}

	public double fillHP;
	private boolean poisoned = false;

	public void drawHPOrb() {
		int health = (int) (((double) currentLevel[3] / (double) maxLevel[3]) * 100D);
		int x = getOrbX(0);
		int y = getOrbY(0);
		int orbColor = poisoned ? 17 : 2;
		orbs[clientSize == 0 ? 0 : 11].drawSprite(x, y);
		if (health >= 75) {
			newSmallFont.drawCenteredString(Integer.toString(currentLevel[3]),
					x + (clientSize == 0 ? 42 : 15), y + 26, 65280, 0);
		} else if (health <= 74 && health >= 50) {
			newSmallFont.drawCenteredString(Integer.toString(currentLevel[3]),
					x + (clientSize == 0 ? 42 : 15), y + 26, 0xffff00, 0);
		} else if (health <= 49 && health >= 25) {
			newSmallFont.drawCenteredString(Integer.toString(currentLevel[3]),
					x + (clientSize == 0 ? 42 : 15), y + 26, 0xfca607, 0);
		} else if (health <= 24 && health >= 0) {
			newSmallFont.drawCenteredString(Integer.toString(currentLevel[3]),
					x + (clientSize == 0 ? 42 : 15), y + 26, 0xf50d0d, 0);
		}
		orbs[orbColor].drawSprite(x + (clientSize == 0 ? 3 : 27), y + 3);
		double percent = (health / 100D);
		fillHP = 27 * percent;
		int depleteFill = 27 - (int) fillHP;
		orbs[1].myHeight = depleteFill;
		DrawingArea.height = depleteFill;
		orbs[1].drawSprite(x + (clientSize == 0 ? 3 : 27), y + 3);
		orbs[3].drawSprite(x + (clientSize == 0 ? 9 : 33), y + 11);
	}

	public double fillPrayer;

	public void drawPrayerOrb() {
		int prayer = (int) (((double) currentLevel[5] / (double) maxLevel[5]) * 100D);
		int x = getOrbX(1);
		int y = getOrbY(1);
		orbs[clientSize == 0 ? (hoverPos == 1 ? 12 : 0) : (hoverPos == 1 ? 13
				: 11)].drawSprite(x, y);
		if (prayer <= 100 && prayer >= 75) {
			newSmallFont.drawCenteredString(Integer.toString(currentLevel[5]),
					x + (clientSize == 0 ? 42 : 15), y + 26, 65280, 0);
		} else if (prayer <= 74 && prayer >= 50) {
			newSmallFont.drawCenteredString(Integer.toString(currentLevel[5]),
					x + (clientSize == 0 ? 42 : 15), y + 26, 0xffff00, 0);
		} else if (prayer <= 49 && prayer >= 25) {
			newSmallFont.drawCenteredString(Integer.toString(currentLevel[5]),
					x + (clientSize == 0 ? 42 : 15), y + 26, 0xfca607, 0);
		} else if (prayer <= 24 && prayer >= 0) {
			newSmallFont.drawCenteredString(Integer.toString(currentLevel[5]),
					x + (clientSize == 0 ? 42 : 15), y + 26, 0xf50d0d, 0);
		}
		orbs[quickPrayers ? 10
				: 4].drawSprite(x + (clientSize == 0 ? 3 : 27), y + 3);
		double percent = (prayer / 100D);
		fillPrayer = 27 * percent;
		int depleteFill = 27 - (int) fillPrayer;
		orbs[17].myHeight = depleteFill;
		DrawingArea.height = depleteFill;
		orbs[17].drawSprite(x + (clientSize == 0 ? 3 : 27), y + 3);
		orbs[5].drawSprite(x + (clientSize == 0 ? 7 : 31), y + 7);
	}

	public double fillRun;
	public boolean running;

	public void drawRunOrb() {
		int run = (int) (((double) runEnergy / (double) 100) * 100D);
		int x = getOrbX(2);
		int y = getOrbY(2);
		orbs[clientSize == 0 ? (hoverPos == 2 ? 12 : 0) : (hoverPos == 2 ? 13
				: 11)].drawSprite(x, y);
		if (run <= 100 && run >= 75) {
			newSmallFont.drawCenteredString(Integer.toString(runEnergy), x
					+ (clientSize == 0 ? 42 : 15), y + 26, 65280, 0);
		} else if (run <= 74 && run >= 50) {
			newSmallFont.drawCenteredString(Integer.toString(runEnergy), x
					+ (clientSize == 0 ? 42 : 15), y + 26, 0xffff00, 0);
		} else if (run <= 49 && run >= 25) {
			newSmallFont.drawCenteredString(Integer.toString(runEnergy), x
					+ (clientSize == 0 ? 42 : 15), y + 26, 0xfca607, 0);
		} else if (run <= 24 && run >= 0) {
			newSmallFont.drawCenteredString(Integer.toString(runEnergy), x
					+ (clientSize == 0 ? 42 : 15), y + 26, 0xf50d0d, 0);
		}
		orbs[!running ? 6 : 8]
				.drawSprite(x + (clientSize == 0 ? 3 : 27), y + 3);
		double percent = (run / 100D);
		fillRun = 27 * percent;
		int depleteFill = 27 - (int) fillRun;
		orbs[18].myHeight = depleteFill;
		DrawingArea.height = depleteFill;
		orbs[18].drawSprite(x + (clientSize == 0 ? 3 : 27), y + 3);
		orbs[!running ? 7 : 9].drawSprite(x + (clientSize == 0 ? 10 : 34),
				y + 7);
	}

	public double fillSummoning;

	public void drawSummoningOrb() {
		int summoning = (int) (((double) currentLevel[23] / (double) maxLevel[23]) * 100D);
		int x = getOrbX(3);
		int y = getOrbY(3);
		orbs[clientSize == 0 ? (hoverPos == 3 ? 12 : 0) : (hoverPos == 3 ? 13
				: 11)].drawSprite(x, y);
		if (summoning <= 100 && summoning >= 75) {
			newSmallFont.drawCenteredString(Integer.toString(currentLevel[23]),
					x + (clientSize == 0 ? 42 : 15), y + 26, 65280, 0);
		} else if (summoning <= 74 && summoning >= 50) {
			newSmallFont.drawCenteredString(Integer.toString(currentLevel[23]),
					x + (clientSize == 0 ? 42 : 15), y + 26, 0xffff00, 0);
		} else if (summoning <= 49 && summoning >= 25) {
			newSmallFont.drawCenteredString(Integer.toString(currentLevel[23]),
					x + (clientSize == 0 ? 42 : 15), y + 26, 0xfca607, 0);
		} else if (summoning <= 24 && summoning >= 0) {
			newSmallFont.drawCenteredString(Integer.toString(currentLevel[23]),
					x + (clientSize == 0 ? 42 : 15), y + 26, 0xf50d0d, 0);
		}
		orbs[getFamiliar().isActive() ? 16 : 14].drawSprite(x
				+ (clientSize == 0 ? 3 : 27), y + 3);
		double percent = (summoning / 100D);
		fillSummoning = 27 * percent;
		int depleteFill = 27 - (int) fillSummoning;
		orbs[19].myHeight = depleteFill;
		DrawingArea.height = depleteFill;
		orbs[19].drawSprite(x + (clientSize == 0 ? 3 : 27), y + 3);
		orbs[15].drawSprite(x + (clientSize == 0 ? 9 : 33), y + 9);
	}

	public void drawMinimap() {
		int xPosOffset = clientSize == 0 ? 0 : clientWidth - 246;
		if (clientSize == 0)
			mapAreaIP.initDrawingArea();
		if (minimapState == 2) {
			cacheSprite[67].drawSprite((clientSize == 0 ? 32
					: clientWidth - 162), (clientSize == 0 ? 9 : 5));
			if (clientSize == 0) {
				cacheSprite[6].drawSprite(0 + xPosOffset, 0);
			} else {
				cacheSprite[36].drawSprite(clientWidth - 167, 0);
				cacheSprite[37].drawSprite(clientWidth - 172, 0);
			}
			cacheSprite[38].drawSprite(
					clientSize == 0 ? -3 : clientWidth - 188,
					clientSize == 0 ? 46 : 40);
			if (hoverPos == 0) {
				cacheSprite[39].drawSprite(clientSize == 0 ? -3
						: clientWidth - 188, clientSize == 0 ? 46 : 40);
			}
			cacheSprite[30].drawSprite(
					(clientSize == 0 ? 246 : clientWidth) - 21, 0);
			if (mouseInRegion(clientWidth - 21, 0, clientWidth, 21)) {
				if (super.clickMode2 == 1) {
					cacheSprite[35].drawSprite((clientSize == 0 ? 246 : clientWidth) - 21, 0);
					tabId = 16;
				} else {
					cacheSprite[34].drawSprite((clientSize == 0 ? 246 : clientWidth) - 21, 0);
				}
			}
			drawHPOrb();
			drawPrayerOrb();
			drawRunOrb();
			drawSummoningOrb();
			compass.method352(33, viewRotation, anIntArray1057, 256,
					anIntArray968, 25, (clientSize == 0 ? 8 : 5),
					(clientSize == 0 ? 8 + xPosOffset : clientWidth - 167), 33,
					25);
			gameScreenIP.initDrawingArea();
			return;
		}
		int i = viewRotation + minimapRotation & 0x7ff;
		int j = 48 + myPlayer.x / 32;
		int l2 = 464 - myPlayer.y / 32;
		miniMap.method352(152, i, anIntArray1229, 256 + minimapZoom,
				anIntArray1052, l2, (clientSize == 0 ? 9 : 5),
				(clientSize == 0 ? 32 : clientWidth - 162), 152, j);
		for (int j5 = 0; j5 < anInt1071; j5++) {
			int k = (anIntArray1072[j5] * 4 + 2) - myPlayer.x / 32;
			int i3 = (anIntArray1073[j5] * 4 + 2) - myPlayer.y / 32;
			try {
				markMinimap(aClass30_Sub2_Sub1_Sub1Array1140[j5], k, i3);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (int k5 = 0; k5 < 104; k5++) {
			for (int l5 = 0; l5 < 104; l5++) {
				Deque class19 = groundEntity[plane][k5][l5];
				if (class19 != null) {
					int l = (k5 * 4 + 2) - myPlayer.x / 32;
					int j3 = (l5 * 4 + 2) - myPlayer.y / 32;
					markMinimap(mapDotItem, l, j3);
				}
			}
		}
		for (int i6 = 0; i6 < npcCount; i6++) {
			NPC npc = npcArray[npcIndices[i6]];
			if (npc != null && npc.isVisible()) {
				NPCDefinition entityDef = npc.getDefinition();
				if (entityDef.childrenIDs != null)
					entityDef = entityDef.method161();
				if (entityDef != null && entityDef.aBoolean87
						&& entityDef.aBoolean84) {
					int i1 = npc.x / 32 - myPlayer.x / 32;
					int k3 = npc.y / 32 - myPlayer.y / 32;
					markMinimap(mapDotNPC, i1, k3);
				}
			}
		}
		for (int j6 = 0; j6 < playerCount; j6++) {
			Player player = playerArray[playerIndices[j6]];
			if (player != null && player.isVisible()) {
				int j1 = player.x / 32 - myPlayer.x / 32;
				int l3 = player.y / 32 - myPlayer.y / 32;
				boolean flag1 = false;
				long l6 = TextClass.longForName(player.name);
				for (int k6 = 0; k6 < friendsCount; k6++) {
					if (l6 != friendsListNames[k6] || friendsNodeIDs[k6] == 0)
						continue;
					flag1 = true;
					break;
				}
				boolean flag2 = false;
				if (myPlayer.team != 0 && player.team != 0
						&& myPlayer.team == player.team)
					flag2 = true;
				if (flag1)
					markMinimap(mapDotFriend, j1, l3);
				else if (flag2)
					markMinimap(mapDotTeam, j1, l3);
				else
					markMinimap(mapDotPlayer, j1, l3);
			}
		}
		if (headIconType != 0 && loopCycle % 20 < 10) {
			if (headIconType == 1 && anInt1222 >= 0 && anInt1222 < npcArray.length) {
				NPC class30_sub2_sub4_sub1_sub1_1 = npcArray[anInt1222];
				if (class30_sub2_sub4_sub1_sub1_1 != null) {
					int k1 = class30_sub2_sub4_sub1_sub1_1.x / 32 - myPlayer.x
							/ 32;
					int i4 = class30_sub2_sub4_sub1_sub1_1.y / 32 - myPlayer.y
							/ 32;
					drawScreenSprite(mapMarker, i4, k1);
				}
			}
			if (headIconType == 2) {
				int l1 = ((anInt934 - regionAbsBaseX) * 4 + 2) - myPlayer.x / 32;
				int j4 = ((anInt935 - regionAbsBaseY) * 4 + 2) - myPlayer.y / 32;
				drawScreenSprite(mapMarker, j4, l1);
			}
			if (headIconType == 10 && otherPlayerIndex >= 0
					&& otherPlayerIndex < playerArray.length) {
				Player class30_sub2_sub4_sub1_sub2_1 = playerArray[otherPlayerIndex];
				if (class30_sub2_sub4_sub1_sub2_1 != null) {
					int i2 = class30_sub2_sub4_sub1_sub2_1.x / 32 - myPlayer.x
							/ 32;
					int k4 = class30_sub2_sub4_sub1_sub2_1.y / 32 - myPlayer.y
							/ 32;
					drawScreenSprite(mapMarker, k4, i2);
				}
			}
		}
		if (destX != 0) {
			int j2 = (destX * 4 + 2) - myPlayer.x / 32;
			int l4 = (destY * 4 + 2) - myPlayer.y / 32;
			markMinimap(mapFlag, j2, l4);
		}
		DrawingArea.drawPixels(3, (clientSize == 0 ? 84 : 80),
				(clientSize == 0 ? 107 + xPosOffset : clientWidth - 88),
				0xffffff, 3);
		if (clientSize == 0) {
			cacheSprite[6].drawSprite(0 + xPosOffset, 0);
		} else {
			cacheSprite[36].drawSprite(clientWidth - 167, 0);
			cacheSprite[37].drawSprite(clientWidth - 172, 0);
		}
		cacheSprite[38].drawSprite(clientSize == 0 ? -3 : clientWidth - 217,
				clientSize == 0 ? 46 : 3);
		if (hoverPos == 0) {
			cacheSprite[39].drawSprite(
					clientSize == 0 ? -3 : clientWidth - 217,
					clientSize == 0 ? 46 : 3);
		}
		cacheSprite[30].drawSprite((clientSize == 0 ? 246 : clientWidth) - 21,
				0);
		if (mouseInRegion(clientWidth - 21, 0, clientWidth, 21)) {
			if (super.clickMode2 == 1) {
				cacheSprite[35].drawSprite((clientSize == 0 ? 246 : clientWidth) - 21, 0);
				tabInterfaceIDs[16] = 2449;
				tabId = 16;
			} else {
				cacheSprite[34].drawSprite((clientSize == 0 ? 246 : clientWidth) - 21, 0);
			}
		}
		drawHPOrb();
		drawPrayerOrb();
		drawRunOrb();
		drawSummoningOrb();
		compass.method352(33, viewRotation, anIntArray1057, 256, anIntArray968,
				25, (clientSize == 0 ? 8 : 5),
				(clientSize == 0 ? 8 + xPosOffset : clientWidth - 167), 33, 25);
		if (menuOpen && menuScreenArea == 3)
			drawMenu();
		gameScreenIP.initDrawingArea();
	}

	public void npcScreenPos(Entity entity, int i) {
		calcEntityScreenPos(entity.x, i, entity.y);
	}

	public void calcEntityScreenPos(int i, int j, int l) {
		if (i < 128 || l < 128 || i > 13056 || l > 13056) {
			spriteDrawX = -1;
			spriteDrawY = -1;
			return;
		}
		int i1 = getHeight(plane, l, i) - j;
		i -= xCameraPos;
		i1 -= zCameraPos;
		l -= yCameraPos;
		int j1 = Rasterizer.SINE[yCameraCurve];
		int k1 = Rasterizer.COSINE[yCameraCurve];
		int l1 = Rasterizer.SINE[xCameraCurve];
		int i2 = Rasterizer.COSINE[xCameraCurve];
		int j2 = l * l1 + i * i2 >> 16;
		l = l * i2 - i * l1 >> 16;
		i = j2;
		j2 = i1 * k1 - l * j1 >> 16;
		l = i1 * j1 + l * k1 >> 16;
		i1 = j2;
		if (l >= 50) {
			spriteDrawX = Rasterizer.centerX + (i << 9) / l;
			spriteDrawY = Rasterizer.centerY + (i1 << 9) / l;
		} else {
			spriteDrawX = -1;
			spriteDrawY = -1;
		}
	}

	public void buildSplitpublicChatMenu() {
		if (splitpublicChat == 0)
			return;
		int i = 0;
		if (systemUpdateTimer != 0)
			i = 1;
		for (int j = 0; j < 100; j++)
			if (chatMessages[j] != null) {
				int k = chatTypes[j];
				String name = chatNames[j];
				if (name != null && name.indexOf("@") == 0) {
					name = name.substring(5);
				}
				if ((k == 3 || k == 7)
						&& (k == 7 || publicChatMode == 0 || publicChatMode == 1
								&& isFriendOrSelf(name))) {
					int l = (clientHeight - 174) - i * 13;
					if (super.mouseX > (clientSize == 0 ? 4 : 0)
							&& super.mouseY - (clientSize == 0 ? 4 : 0) > l - 10
							&& super.mouseY - (clientSize == 0 ? 4 : 0) <= l + 3) {
						int i1 = normalFont.getTextWidth("From:  " + name
								+ chatMessages[j]) + 25;
						if (i1 > 450)
							i1 = 450;
						if (super.mouseX < (clientSize == 0 ? 4 : 0) + i1) {
							if (myRank >= 1) {
								menuActionName[menuActionRow] = "Report abuse @whi@"
										+ name;
								menuActionID[menuActionRow] = 2606;
								menuActionRow++;
							}
							if (!isFriendOrSelf(name)) {
								menuActionName[menuActionRow] = "Add ignore @whi@"
										+ name;
								menuActionID[menuActionRow] = 2042;
								menuActionRow++;
								menuActionName[menuActionRow] = "Reply to @whi@" + name;
								menuActionID[menuActionRow] = 2639;
								menuActionRow++;
								menuActionName[menuActionRow] = "Add friend @whi@"
										+ name;
								menuActionID[menuActionRow] = 2337;
								menuActionRow++;
							}
							if (isFriendOrSelf(name)) {
								menuActionName[menuActionRow] = "Message @whi@"
										+ name;
								menuActionID[menuActionRow] = 2639;
								menuActionRow++;
							}
						}
					}
					if (++i >= 5)
						return;
				}
				if ((k == 5 || k == 6) && publicChatMode < 2 && ++i >= 5)
					return;
			}

	}

	public void requestGameObjectSpawn(int priority1, int k, int l, int type, int y, int k1, int height,
			int x, int priority2) {
		ObjectSpawnNode objectSpawn = null;
		for (ObjectSpawnNode class30_sub1_1 = (ObjectSpawnNode) aClass19_1179
				.head(); class30_sub1_1 != null; class30_sub1_1 = (ObjectSpawnNode) aClass19_1179
				.next()) {
			if (class30_sub1_1.height != height
					|| class30_sub1_1.x != x
					|| class30_sub1_1.y != y
					|| class30_sub1_1.type != type)
				continue;
			objectSpawn = class30_sub1_1;
			break;
		}

		if (objectSpawn == null) {
			objectSpawn = new ObjectSpawnNode();
			objectSpawn.height = height;
			objectSpawn.type = type;
			objectSpawn.x = x;
			objectSpawn.y = y;
			method89(objectSpawn);
			aClass19_1179.append(objectSpawn);
		}
		objectSpawn.anInt1291 = k;
		objectSpawn.anInt1293 = k1;
		objectSpawn.anInt1292 = l;
		objectSpawn.anInt1302 = priority2;
		objectSpawn.anInt1294 = priority1;
	}

	public boolean interfaceIsSelected(RSInterface rsi) {
		if (rsi.valueCompareType == null)
			return false;
		for (int i = 0; i < rsi.valueCompareType.length; i++) {
			int j = extractInterfaceValues(rsi, i);
			int k = rsi.requiredValue[i];
			if (rsi.valueCompareType[i] == 2) {
				if (j >= k)
					return false;
			} else if (rsi.valueCompareType[i] == 3) {
				if (j <= k)
					return false;
			} else if (rsi.valueCompareType[i] == 4) {
				if (j == k)
					return false;
			} else if (j != k)
				return false;
		}

		return true;
	}

	public void connectServer() {
		int j = 5;
		expectedCRCs[8] = 0;
		int k = 0;
		while (expectedCRCs[8] == 0) {
			String s = "Unknown problem";
			updateProgress("Connecting to web server", 20);
			try {
				DataInputStream datainputstream = openJagGrabInputStream("crc"
						+ (int) (Math.random() * 99999999D) + "-" + 317);
				JagexBuffer stream = new JagexBuffer(new byte[40]);
				datainputstream.readFully(stream.buffer, 0, 40);
				datainputstream.close();
				for (int i1 = 0; i1 < 9; i1++)
					expectedCRCs[i1] = stream.readInt();

				int j1 = stream.readInt();
				int k1 = 1234;
				for (int l1 = 0; l1 < 9; l1++)
					k1 = (k1 << 1) + expectedCRCs[l1];

				if (j1 != k1) {
					s = "checksum problem";
					expectedCRCs[8] = 0;
				}
			} catch (EOFException _ex) {
				s = "EOF problem";
				expectedCRCs[8] = 0;
			} catch (IOException _ex) {
				s = "connection problem";
				expectedCRCs[8] = 0;
			} catch (Exception _ex) {
				s = "logic problem";
				expectedCRCs[8] = 0;
				if (!signlink.reporterror)
					return;
			}
			if (expectedCRCs[8] == 0) {
				k++;
				for (int l = j; l > 0; l--) {
					if (k >= 10) {
						updateProgress("Game updated - please reload page", 10);
						l = 10;
					} else {
						updateProgress(s + " - Will retry packet " + l
								+ " secs.", 10);
					}
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}

				j *= 2;
				if (j > 60)
					j = 60;
				aBoolean872 = !aBoolean872;
			}
		}
	}

	public DataInputStream openJagGrabInputStream(String s) throws IOException {
		if (aSocket832 != null) {
			try {
				aSocket832.close();
			} catch (Exception _ex) {
			}
			aSocket832 = null;
		}
		aSocket832 = openSocket(43595);
		aSocket832.setSoTimeout(10000);
		java.io.InputStream inputstream = aSocket832.getInputStream();
		OutputStream outputstream = aSocket832.getOutputStream();
		outputstream.write(("JAGGRAB /" + s + "\n\n").getBytes());
		return new DataInputStream(inputstream);
	}

	public void method134(JagexBuffer stream) {
		int localPlayers = stream.readBits(8);
		if (localPlayers < playerCount) {
			for (int k = localPlayers; k < playerCount; k++)
				anIntArray840[anInt839++] = playerIndices[k];

		}
		if (localPlayers > playerCount) {
			throw new RuntimeException("Local players read from server is higher than Client's playerCount.");
		}
		playerCount = 0;
		for (int l = 0; l < localPlayers; l++) {
			int index = playerIndices[l];
			Player player = playerArray[index];
			int discardPlayer = stream.readBits(1);
			if (discardPlayer == 0) {
				playerIndices[playerCount++] = index;
				player.anInt1537 = loopCycle;
			} else {
				int k1 = stream.readBits(2);
				if (k1 == 0) {
					playerIndices[playerCount++] = index;
					player.anInt1537 = loopCycle;
					anIntArray894[anInt893++] = index;
				} else if (k1 == 1) {
					playerIndices[playerCount++] = index;
					player.anInt1537 = loopCycle;
					int direction = stream.readBits(3);
					player.moveInDir(false, direction);
					int j2 = stream.readBits(1);
					if (j2 == 1)
						anIntArray894[anInt893++] = index;
				} else if (k1 == 2) {
					playerIndices[playerCount++] = index;
					player.anInt1537 = loopCycle;
					int walkDirection = stream.readBits(3);
					player.moveInDir(true, walkDirection);
					int k2 = stream.readBits(3);
					player.moveInDir(true, k2);
					int l2 = stream.readBits(1);
					if (l2 == 1)
						anIntArray894[anInt893++] = index;
				} else if (k1 == 3)
					anIntArray840[anInt839++] = index;
			}
		}
	}

	public int optionsWidth = 0;

	public int getClickMode() {
		return super.clickMode3;
	}

	public boolean mouseInRegion2(int x1, int x2, int y1, int y2) {
		if (super.mouseX >= x1 && super.mouseX <= x2 && super.mouseY >= y1 && super.mouseY <= y2) {
			return true;
		}
		return false;
	}

	public boolean clickInRegion2(int x1, int x2, int y1, int y2) {
		if (super.saveClickX >= x1 && super.saveClickX <= x2 && super.saveClickY >= y1 && super.saveClickY <= y2) {
			return true;
		}
		return false;
	}

	public boolean mouseInRegion(int x1, int y1, int x2, int y2) {
		if (super.mouseX >= x1 && super.mouseX <= x2 && super.mouseY >= y1
				&& super.mouseY <= y2)
			return true;
		return false;
	}

	public boolean clickInRegion(int x1, int y1, int x2, int y2) {
		if (super.saveClickX >= x1 && super.saveClickX <= x2
				&& super.saveClickY >= y1 && super.saveClickY <= y2)
			return true;
		return false;
	}

	public void drawBorder(int x, int y, int width, int height, int type) {
		border[type == 0 ? 0 : 6].drawSprite(x, y);
		if (type == 0)
			border[8].drawSprite(x + width - 16, y);
		else
			border[18].drawSprite(x + width - 16, y);
		for (int x2 = 16; x2 < width - 16; x2 += 16) {
			x2 = x2 >= width - 20 ? width - 20 : x2;
			border[type == 0 ? 1 : 7].drawSprite(x + x2, y);
			border[16].drawSprite(x + x2, y + height - 7);
		}
		for (int y2 = 16; y2 < height - 16; y2 += 16) {
			y2 = y2 >= height - 20 ? height - 20 : y2;
			if (y2 > 16) {
				border[5].drawSprite(x, y + y2);
				border[17].drawSprite(x + width - 7, y + y2);
			} else {
				border[5].drawSprite(x, y + y2);
				border[15].drawSprite(x + width - 7, y + y2);
			}
		}
		border[12].drawSprite(x, y + height - 16);
		border[13].drawSprite(x + width - 16, y + height - 16);
	}

	public void drawTitleBox(int x, int y, int width, int height, int type) {
		DrawingArea.drawGradient(x + 5, y + 5, width - 10, height - 10, 0x595043, 0xA8A8A8);
		drawBorder(x, y, width, height, type);
	}

	public int settingHeight = 200;
	public int titleAlpha = 0;
	public int loginHover = -1;
	public int selectedWorld = -1;
	public int worldHover = -1;
	public Sprite loading;

	public void loadMap(int x, int y) {
		mapX = x;
		mapY = y;
		aBoolean1159 = false;
		if (currentRegionX == x && currentRegionY == y && loadingStage == 2) {
			return;
		}
		currentRegionX = x;
		currentRegionY = y;
		regionAbsBaseX = (currentRegionX - 6) * 8;
		regionAbsBaseY = (currentRegionY - 6) * 8;
		aBoolean1141 = (currentRegionX / 8 == 48 || currentRegionX / 8 == 49) && currentRegionY / 8 == 48;
		if (currentRegionX / 8 == 48 && currentRegionY / 8 == 148) {
			aBoolean1141 = true;
		}
		loadingStage = 1;
		aLong824 = System.currentTimeMillis();
		int length = 0;
		for (int i21 = (currentRegionX - 6) / 8; i21 <= (currentRegionX + 6) / 8; i21++) {
			for (int k23 = (currentRegionY - 6) / 8; k23 <= (currentRegionY + 6) / 8; k23++) {
				length++;
			}
		}
		aByteArrayArray1183 = new byte[length][];
		aByteArrayArray1247 = new byte[length][];
		mapLocation = new int[length];
		floorMap = new int[length];
		objectMap = new int[length];
		length = 0;
		for (int xLoop = (currentRegionX - 6) / 8; xLoop <= (currentRegionX + 6) / 8; xLoop++) {
			for (int yLoop = (currentRegionY - 6) / 8; yLoop <= (currentRegionY + 6) / 8; yLoop++) {
				mapLocation[length] = (xLoop << 8) + yLoop;
				if (aBoolean1141 && (yLoop == 49 || yLoop == 149 || yLoop == 147 || xLoop == 50 || xLoop == 49 && yLoop == 47)) {
					floorMap[length] = -1;
					objectMap[length] = -1;
					length++;
				} else {
					int k28 = floorMap[length] = resourceProvider.getClipping(0, yLoop, xLoop);
					if (k28 != -1) {
						resourceProvider.loadMandatory(3, k28);
					}
					int j30 = objectMap[length] = resourceProvider.getClipping(1, yLoop, xLoop);
					if (j30 != -1) {
						resourceProvider.loadMandatory(3, j30);
					}
					length++;
				}
			}
		}
		int i17 = regionAbsBaseX - anInt1036;
		int j21 = regionAbsBaseY - anInt1037;
		anInt1036 = regionAbsBaseX;
		anInt1037 = regionAbsBaseY;
		for (int j24 = 0; j24 < 16384; j24++) {
			NPC npc = npcArray[j24];
			if (npc != null) {
				for (int j29 = 0; j29 < 10; j29++) {
					npc.smallX[j29] -= i17;
					npc.smallY[j29] -= j21;
				}
				npc.x -= i17 * 128;
				npc.y -= j21 * 128;
			}
		}
		for (int i27 = 0; i27 < maxPlayers; i27++) {
			Player player = playerArray[i27];
			if (player != null) {
				for (int i31 = 0; i31 < 10; i31++) {
					player.smallX[i31] -= i17;
					player.smallY[i31] -= j21;
				}
				player.x -= i17 * 128;
				player.y -= j21 * 128;
			}
		}
		aBoolean1080 = true;
		byte byte1 = 0;
		byte byte2 = 104;
		byte byte3 = 1;
		if (i17 < 0) {
			byte1 = 103;
			byte2 = -1;
			byte3 = -1;
		}
		byte byte4 = 0;
		byte byte5 = 104;
		byte byte6 = 1;
		if (j21 < 0) {
			byte4 = 103;
			byte5 = -1;
			byte6 = -1;
		}
		for (int k33 = byte1; k33 != byte2; k33 += byte3) {
			for (int l33 = byte4; l33 != byte5; l33 += byte6) {
				int i34 = k33 + i17;
				int j34 = l33 + j21;
				for (int k34 = 0; k34 < 4; k34++)
					if (i34 >= 0 && j34 >= 0 && i34 < 104 && j34 < 104)
						groundEntity[k34][k33][l33] = groundEntity[k34][i34][j34];
					else
						groundEntity[k34][k33][l33] = null;
			}
		}
		for (ObjectSpawnNode class30_sub1_1 = (ObjectSpawnNode) aClass19_1179
				.head(); class30_sub1_1 != null; class30_sub1_1 = (ObjectSpawnNode) aClass19_1179
				.next()) {
			class30_sub1_1.x -= i17;
			class30_sub1_1.y -= j21;
			if (class30_sub1_1.x < 0 || class30_sub1_1.y < 0 || class30_sub1_1.x >= 104 || class30_sub1_1.y >= 104)
				class30_sub1_1.remove();
		}
		if (destX != 0) {
			destX -= i17;
			destY -= j21;
		}
		aBoolean1160 = false;
	}

	public void resetMap2() {
		aBoolean1080 = false;
	}

	public ScriptManager scriptManager;

	public void resetMap1() {
		currentSound = 0;
		cameraOffsetX = (int) (Math.random() * 100D) - 50;
		cameraOffsetY = (int) (Math.random() * 110D) - 55;
		viewRotationOffset = (int) (Math.random() * 80D) - 40;
		minimapRotation = (int) (Math.random() * 120D) - 60;
		minimapZoom = (int) (Math.random() * 30D) - 20;
		viewRotation = (int) (Math.random() * 20D) - 10 & 0x7ff;
		minimapState = 0;
		loadingStage = 1;
	}

	public void loginScreenBG(boolean b) {
		xCameraPos = 6100;
		yCameraPos = 6867;
		zCameraPos = -750;
		xCameraCurve = 2040;
		yCameraCurve = 383;
		resetMap1();
		if (b || scriptManager == null) {
			scriptManager = new ScriptManager(this);
		} else {
			scriptManager.update();
		}
		plane = scriptManager.mapZ;
		loadMap(scriptManager.mapX, scriptManager.mapY);
		resetMap2();
	}

	public int[] titleScreenOffsets = null;
	public int titleWidth = -1;
	public int titleHeight = -1;

	public void drawLoginScreen(boolean flag) {
		super.setCursor(0);
		resetImageProducers();
		title.initDrawingArea();
		DrawingArea474.drawFilledPixels(0, 0, clientWidth, clientHeight, 0x040404);
		int centerX = clientWidth / 2;
		int centerY = clientHeight / 2;
		titleAlpha += titleAlpha < 250 ? 10 : 0;
		if (Constants.DISPLAY_WORLD_ANIMATION) {
			if (scriptManager == null) {
				loginScreenBG(true);
			}
			int canvasCenterX = Rasterizer.centerX;
			int canvasCenterY = Rasterizer.centerY;
			int canvasPixels[] = Rasterizer.lineOffsets;
			if (titleScreenOffsets != null && (titleWidth != clientWidth || titleHeight != clientHeight)) {
				titleScreenOffsets = null;
			}
			if (titleScreenOffsets == null) {
				titleWidth = clientWidth;
				titleHeight = clientHeight;
				titleScreenOffsets = Rasterizer.getOffsets(titleWidth, titleHeight);
			}
			Rasterizer.centerX = centerX;
			Rasterizer.centerY = centerY;
			Rasterizer.lineOffsets = titleScreenOffsets;
			if (loadingStage == 2 && SceneGraph.onBuildTimePlane != plane)
				loadingStage = 1;
	
			if (!loggedIn && loadingStage == 1) {
				method54();
			}
			if (!loggedIn && loadingStage == 2 && plane != anInt985) {
				anInt985 = plane;
				method24(plane);
			}
			if (loadingStage == 2) {
				Rasterizer.triangles = 0;
				try {
					landscapeScene.method313(xCameraPos, yCameraPos, xCameraCurve, zCameraPos, method121(), yCameraCurve);
					landscapeScene.clearObj5Cache();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (scriptManager != null && loadingStage == 2 && plane == anInt985 && !loggedIn) {
				scriptManager.cycle();
			}
			Rasterizer.centerX = canvasCenterX;
			Rasterizer.centerY = canvasCenterY;
			Rasterizer.lineOffsets = canvasPixels;
		}
		if (titleAlpha >= 250) {
			if (loginScreenState == 0) {
				titleBox.drawSprite(centerX - (266 / 2), centerY - (305 / 2));
				if (loginHover == 10) {
					optionSelect[2].drawSprite(clientWidth - 454, clientHeight - 223);//309 279
				}
				if (loginHover == 11) {
					optionSelect[3].drawSprite(clientWidth - 445, clientHeight - 127);
				}
				if (loginHover == 12) {
					fbHover.drawSprite(clientWidth - 441, clientHeight - 189);
				}
				//socialBox[1].drawSprite(clientWidth - 445, clientHeight - 189);
				DrawingArea474.drawHorizontalLine(centerX - (216 / 2), centerY - 116, 216, 0x161513);
				/* Username */
				titleButton[1].drawSprite(centerX - (titleButton[1].myWidth / 2), centerY - 93);
				if (loginHover == 2) {
					//titleHover.drawSprite(centerX - (titleButton[1].myWidth / 2), centerY - 93);
				}
				/* Password */
				titleButton[1].drawSprite(centerX - (titleButton[1].myWidth / 2), centerY + (!enableServerIP ? -47 : -67));
				if (loginHover == 3) {
					//titleHover.drawSprite(centerX - (titleButton[1].myWidth / 2), centerY + (!enableServerIP ? -47 : -67));
				}
				if (enableServerIP) {
					/* Server IP */
					titleButton[1].drawSprite(centerX - (titleButton[1].myWidth / 2), centerY + 11);
					if (loginHover == 4) {
						titleButton[5].drawSprite(centerX - (titleButton[1].myWidth / 2), centerY + 11);
					}
				}
				DrawingArea474.drawHorizontalLine(centerX - (216 / 2), centerY + (!enableServerIP ? -10 : 20), 216, 0x161513);
				/* Log In */
				titleButton[0].drawSprite(centerX - (titleButton[0].myWidth / 2), centerY + (!enableServerIP ? -2 : 28));
				if (loginHover == 5) {
					titleButton[3].drawSprite(centerX - (titleButton[0].myWidth / 2), centerY + (!enableServerIP ? -2 : 28));
				}
				//boldFont.method382(0xffff00, centerX, "Mouse X: " + super.mouseX + " , Mouse Y: " + super.mouseY, centerY, true);
				normalFont.method389(true, centerX - (titleButton[1].myWidth / 2) + 5, 0xEBE0BC, capitalize(myUsername) + ((loginScreenCursorPos == 0) & (loopCycle % 40 < 20) ? "@whi@|" : ""), centerY - 75);
				normalFont.method389(true, centerX - (titleButton[1].myWidth / 2) + 5, 0xEBE0BC, TextClass.passwordField(myPassword) + ((loginScreenCursorPos == 1) & (loopCycle % 40 < 20) ? "@whi@|" : ""), centerY + (!enableServerIP ? -28 : -38));
				if (enableServerIP) {
					normalFont.method389(true, centerX - (titleButton[1].myWidth / 2), 0xEBE0BC, "Server IP:", centerY + 38);
					normalFont.method389(true, centerX - (titleButton[1].myWidth / 2) + 5, 0xEBE0BC, Constants.HOST_ADDRESS + ((loginScreenCursorPos == 2) & (loopCycle % 40 < 20) ? "@yel@|" : ""), centerY + 61);
				}
			}
			if (loginScreenState == 2) {
				DrawingArea474.drawAlphaFilledPixels(0, 0, clientWidth, clientHeight, 0, 150);
				drawTitleBox(centerX - (225 / 2), centerY - (settingHeight / 2), 225, settingHeight, 1);
				titleButton[3].drawSprite(centerX - (225 / 2) + 206, centerY - (settingHeight / 2) + 3);
				if (loginHover == 7) {
					titleButton[7].drawSprite(centerX - (225 / 2) + 206, centerY - (settingHeight / 2) + 3);
				}
				normalFont.method382(0xffffff, centerX, "Client Settings", centerY - (settingHeight / 2) + 16, true);
			}
			/**
			 * Login Lobby(Sprites position ect..)
			 */
			if (loginScreenState == 3) {
				DrawingArea474.drawAlphaFilledPixels(0, 0, clientWidth, clientHeight, 0, 150);
				optionSelect[1].drawSprite(125, 54);
				normalFont.drawText(0, "Mouse X: "+ super.mouseX + " , Mouse Y: " + super.mouseY, 151, 257);
				if (loginHover == 9) {
					optionSelect[0].drawSprite(clientWidth - 153, 64);
				}
			}
			if (loginMessage1.length() > 0 || loginMessage2.length() > 0) {
				DrawingArea474.drawAlphaFilledPixels(0, 0, clientWidth, clientHeight, 0, 200);
				int messageWidth = 270;
				int messageHeight = 100;
				int x1 = centerX - (messageWidth / 2);
				int y1 = centerY - (messageHeight / 2);
				drawTitleBox(x1, y1, messageWidth, messageHeight, 0);
				titleButton[3].drawSprite(x1 + messageWidth - 19, y1 + 4);
				if (loginHover == 6) {
					titleButton[7].drawSprite(x1 + messageWidth - 19, y1 + 4);
				}
				if (loginMessage1.length() > 0) {
					normalFont.method382(0xffffff, centerX, loginMessage1, centerY - 7, true);
					normalFont.method382(0xffffff, centerX, loginMessage2, centerY + 7, true);
				} else {
					normalFont.method382(0xffffff, centerX, loginMessage2,
							centerY, true);
				}
			}
		}
		title.drawGraphics(0, 0, super.graphics);
		if (welcomeScreenRaised) {
			welcomeScreenRaised = false;
		}
	}

	public int[] worlds = { 1, 2, 3 };
	public int[] worldType = { 2, 1, 1 };
	public String[] worldDescription = {
			"RuneCreation BETA. Daily updates, hundreds of improvements, this is the big cheese!",
			"Old RuneCreation. Outdated and not updated, but get on if you wish.",
			"RuneCreation PK. Spawn your items, set your levels, and PK all you want!" };

	public void addWorld(int x, int y, int color, int world, int type,
			String description) {
		if (selectedWorld == world) {
			DrawingArea474.drawFilledPixels(x, y, clientWidth - 94, 20,
					0xB24D00);
		} else if (worldHover == world) {
			DrawingArea474.drawFilledPixels(x, y, clientWidth - 94, 20,
					0x606060);
		} else {
			DrawingArea474.drawFilledPixels(x, y, clientWidth - 94, 20,
					color == 0 ? 0x201911 : 0x292016);
		}
		worldSelect[type == 1 ? 5 : 6].drawSprite(x + 1, y + 1);
		smallText.method389(true, x + 24, type == 1 ? 0xFCFCFC : 0xFCFC64,
				Integer.toString(world), y + 15);
		DrawingArea474.drawFilledPixels(x + 45, y, 2, 20, 0x453718);
		smallText.method389(true, x + 50, type == 1 ? 0xFCFCFC : 0xFCFC64,
				description, y + 15);
	}

	public final int LOGIN = 0;
	public final int CREATE = 1;
	public final int RECOVER = 2;

	public int[] alpha = { 0, 0, 0, 0, 0, 0 };
	public boolean[] increasing = { false, false, false, false, false, false };
	public int optionsHeight = 25;
	public int[] optionsAlpha = { 0, 0, 0, 0, 0 };
	public int[] linkColor = { 0xffffff, 0xffff00 };
	public int accountsX = 0;
	public int messageX = 0;
	public int messageY = 0;
	public int messageWidth = 0;
	public int messageHeight = 0;
	public int messageAlpha = 0;
	public int titleHover = -1;
	public int screenHover = -1;
	public int detailHover = -1;
	public int accountHover = -1;
	public int editHover = -1;
	public boolean editAccounts = false;
	public int bannerAlpha = 0;

	/**
	 * Handles the "glowing" of images on the title screen.
	 * @param position
	 */
	public void handleGlow(int position) {
		int x = (getClientWidth() / 2) - (box.myWidth / 2);
		int y = (getClientHeight() / 2) - (box.myHeight / 2);
		int[] rate = new int[]{ 24, 8 };
		if (position == 0) {
			if (mouseInRegion2(x + 308, x + 308 + play.myWidth, y + 62, y + 62 + play.myHeight)) {
				alpha[0] += alpha[0] < 256 ? rate[0] : 0;
			} else {
				alpha[0] -= alpha[0] > 0 ? rate[0] : 0;
			}
			play_hover.drawARGBImage(x + 308, y + 62, alpha[0]);
		} else if (position == 1) {
			if (alpha[loginScreenCursorPos + 2] < 256 && increasing[loginScreenCursorPos + 2]) {
				alpha[loginScreenCursorPos + 2] += rate[1];
			}
			if (alpha[loginScreenCursorPos + 2] == 256) {
				increasing[loginScreenCursorPos + 2] = false;
			}
			if (alpha[loginScreenCursorPos + 2] > 0 && !increasing[loginScreenCursorPos + 2]) {
				alpha[loginScreenCursorPos + 2] -= rate[1];
			}
			if (alpha[loginScreenCursorPos + 2] <= 0) {
				increasing[loginScreenCursorPos + 2] = true;
				alpha[loginScreenCursorPos + 2] = 0;
			}
			input_hover.drawImage(x + 29, y + 63 + (loginScreenCursorPos == 0 ? 0 : 52), alpha[loginScreenCursorPos + 2]);
		} else if (position == 2) {
			int index = 4;
			if (alpha[index] < 256 && increasing[index]) {
				alpha[index] += rate[1];
			}
			if (alpha[index] == 256) {
				increasing[index] = false;
			}
			if (alpha[index] > 0 && !increasing[index]) {
				alpha[index] -= rate[1];
			}
			if (alpha[index] <= 0) {
				increasing[index] = true;
				alpha[index] = 0;
			}
			int formY = (getClientHeight() / 2) - (359 / 2);
			if (getCreate().canCreate()) {
				header_glow.drawARGBImage(x + 69, formY, alpha[index]);
				arial[0].drawStringCenter("(click here)", x + 150, formY + 33, 0xffffff, true);
			}
		} else if (position == 3) {
			int index = 5;
			if (alpha[index] < 256 && increasing[index]) {
				alpha[index] += rate[1];
			}
			if (alpha[index] == 256) {
				increasing[index] = false;
			}
			if (alpha[index] > 0 && !increasing[index]) {
				alpha[index] -= rate[1];
			}
			if (alpha[index] <= 0) {
				increasing[index] = true;
				alpha[index] = 0;
			}
			if (getRecovery().canRecover()) {
				header_glow.drawARGBImage(x + 69, y, alpha[index]);
				arial[0].drawStringCenter("(click here)", x + 150, y + 33, 0xffffff, true);
			}
		}
	}

	/**
	 * Displays the Client options tab and the Client options.
	 */
	public void displayOptions() {
		int x = (getClientWidth() / 2) - (box.myWidth / 2);
		int y = (getClientHeight() / 2) - (box.myHeight / 2);
		int min = 25;
		int max = 150;
		if (titleHover == 1) {
			if (optionsHeight < max) {
				optionsHeight += 7;
			}
			if (optionsHeight > max) {
				optionsHeight = max;
			}
		} else {
			if (optionsHeight > min) {
				optionsHeight -= 7;
			}
			if (optionsHeight < min) {
				optionsHeight = min;
			}
		}
		DrawingArea474.drawRoundedRectangle(x + 20, y + 180, 322, optionsHeight, 0x242424, 256, true, false);
		DrawingArea474.drawRoundedRectangle(x + 20, y + 180, 322, optionsHeight, 0x2C2C2C, 256, false, false);
		arial[2].drawStringCenter("Client Options", x + 20 + (322 / 2), y + 197, 0xffffff, true);
		if (optionsHeight == max) {
			int[] screen_x = { x + (322 / 2) - 45, x + 20 + (322 / 2), x + 85 + (322 / 2) };
			if (screenHover != -1) {
				optionsAlpha[screenHover] += optionsAlpha[screenHover] < 256 ? 24 : 0;
			} else {
				optionsAlpha[0] -= optionsAlpha[0] > 0 ? 24 : 0;
				optionsAlpha[1] -= optionsAlpha[1] > 0 ? 24 : 0;
				optionsAlpha[2] -= optionsAlpha[2] > 0 ? 24 : 0;
			}
			for (int index = 0; index <= 2; index++) {
				screen_dull[index].drawCenteredImage(screen_x[index], y + 226);
				screen_selected[index].drawCenteredImage(screen_x[index], y + 226, optionsAlpha[index]);
			}
			screen_selected[clientSize].drawCenteredImage(screen_x[clientSize], y + 226);
			int[] detail_x = { x + 85, x + 200 };
			if (detailHover != -1) {
				optionsAlpha[detailHover + 3] += optionsAlpha[detailHover + 3] < 256 ? 24 : 0;
			} else {
				optionsAlpha[3] -= optionsAlpha[3] > 0 ? 24 : 0;
				optionsAlpha[4] -= optionsAlpha[4] > 0 ? 24 : 0;
			}
			detail_dull[0].drawARGBImage(detail_x[0], y + 260);
			detail_dull[1].drawARGBImage(detail_x[1], y + 260);
			detail_hover[0].drawARGBImage(detail_x[0], y + 260, optionsAlpha[3]);
			detail_hover[1].drawARGBImage(detail_x[1], y + 260, optionsAlpha[4]);
			int detail = lowMemory ? 0 : 1;
			detail_selected[detail].drawARGBImage(detail_x[detail], y + 260);
		}
	}

	/**
	 * Displays the accounts tab and the list of accounts.
	 */
	public void displayAccounts() {
		int x = (getClientWidth() / 2) - (box.myWidth / 2) + 5;
		int y = (getClientHeight() / 2) - (box.myHeight / 2) + 25;
		int min = 0;
		int max = 125;
		if (titleHover == 2) {
			if (accountsX < max) {
				accountsX += 7;
			}
			if (accountsX > max) {
				accountsX = max;
			}
		} else {
			if (accountsX > min) {
				accountsX -= 7;
			}
			if (accountsX < min) {
				accountsX = min;
			}
		}
		DrawingArea474.drawRoundedRectangle(x - accountsX, y, 250, 140, 0x242424, 256, true, false);
		DrawingArea474.drawRoundedRectangle(x - accountsX, y, 250, 140, 0x2C2C2C, 256, false, false);
		arial[1].drawString("Accounts (" + Accounts.accounts.length + " / 6) " + (editAccounts ? "[finish]" : "[edit]"), x - accountsX + 5, y + 15, editHover == 0 ? 0xffff00 : 0xffffff, true);
		int text_x = x - accountsX + 72;
		int text_y = y + 35;
		if (Accounts.accounts != null) {
			for (int index = 0; index < Accounts.accounts.length; index++, text_y += 17) {
				if (Accounts.getAccount(Accounts.sortNamesByUsage()[index]) != null) {
					arial[1].drawStringCenter(Accounts.sortNamesByUsage()[index] + " (" + Accounts.getAccount(Accounts.sortNamesByUsage()[index]).uses + " use" + (Accounts.getAccount(Accounts.sortNamesByUsage()[index]).uses > 1 ? "s" : "") + ")" + (editAccounts ? " [x]" : ""), text_x, text_y, accountHover == index ? (editAccounts ? 0xff0000 : 0xffff00) : 0xffffff, true);
				}
			}
		}
	}


	/**
	 * Displays the login messages (errors, problems, missing info, etc).
	 */
	public void displayMessages() {
		int width = 300;
		int height = 50;
		int x = (getClientWidth() / 2) - (width / 2);
		int y = (getClientHeight() / 2) - (height / 2);
		int center_x = (getClientWidth() / 2);
		int center_y = (getClientHeight() / 2);
		int alpha_rate = 10;
		int alpha_max = 200;
		int x_max = (width / 2);
		int y_max = (height / 2);
		DrawingArea474.drawAlphaFilledPixels(0, 0, getClientWidth(), getClientHeight(), 0, messageAlpha);
		DrawingArea474.drawRoundedRectangle(center_x - messageX, center_y - messageY, messageWidth, messageHeight, 0x242424, 256, true, false);
		DrawingArea474.drawRoundedRectangle(center_x - messageX, center_y - messageY, messageWidth, messageHeight, 0x2C2C2C, 256, false, false);
		if (loginMessage1.length() > 0 || loginMessage2.length() > 0) {
			if (messageAlpha < alpha_max) {
				messageAlpha += alpha_rate;
			}
			if (messageAlpha > alpha_max) {
				messageAlpha = alpha_max;
			}
			if (messageX < x_max) {
				messageX += 10;
			}
			if (messageY < y_max) {
				messageY += 5;
			}
			if (messageWidth < width) {
				messageWidth += 20;
			}
			if (messageHeight < height) {
				messageHeight += 5;
			}
			if (messageAlpha == alpha_max) {
				close[titleHover == 3 ? 1 : 0].drawImage(x + width - 16, y);
				if (loginMessage2.length() > 0) {
					arial[2].drawStringCenter(loginMessage1, center_x, center_y - 3, 0xffffff, true);
					arial[2].drawStringCenter(loginMessage2, center_x, center_y + 12, 0xffffff, true);
				} else {
					arial[2].drawStringCenter(loginMessage1, center_x, center_y + 5, 0xffffff, true);
				}
			}
		} else {
			if (messageAlpha > 0) {
				messageAlpha -= alpha_rate;
			}
			if (messageAlpha < 0) {
				messageAlpha = 0;
			}
			if (messageX > 0) {
				messageX -= 10;
			}
			if (messageY > 0) {
				messageY -= 5;
			}
			if (messageWidth > 0) {
				messageWidth -= 20;
			}
			if (messageHeight > 0) {
				messageHeight -= 5;
			}
		}
	}

	public RealFont[] arial = { new RealFont(this, "Arial", 0, 10, true), new RealFont(this, "Arial", 0, 12, true), new RealFont(this, "Arial", 0, 14, true) };

	/**
	 * Displays the title screen.
	 * @param hideButtons
	 */
	public void displayTitleScreen(boolean hideButtons) {
		//drawLoginScreen(hideButtons);
		//return;
		resetImageProducers();
		title.initDrawingArea();
		DrawingArea474.drawFilledPixels(0, 0, getClientWidth(), getClientHeight(), 0x010101);

		if (Constants.DISPLAY_WORLD_ANIMATION) {
			int centerX = clientWidth / 2;
			int centerY = clientHeight / 2;
			titleAlpha += titleAlpha < 250 ? 10 : 0;
			if (scriptManager == null) {
				loginScreenBG(true);
			}
			int canvasCenterX = Rasterizer.centerX;
			int canvasCenterY = Rasterizer.centerY;
			int canvasPixels[] = Rasterizer.lineOffsets;
			if (titleScreenOffsets != null && (titleWidth != clientWidth || titleHeight != clientHeight)) {
				titleScreenOffsets = null;
			}
			if (titleScreenOffsets == null) {
				titleWidth = clientWidth;
				titleHeight = clientHeight;
				titleScreenOffsets = Rasterizer.getOffsets(titleWidth, titleHeight);
			}
			Rasterizer.centerX = centerX;
			Rasterizer.centerY = centerY;
			Rasterizer.lineOffsets = titleScreenOffsets;
			if (loadingStage == 2 && SceneGraph.onBuildTimePlane != plane)
				loadingStage = 1;
	
			if (!loggedIn && loadingStage == 1) {
				method54();
			}
			if (!loggedIn && loadingStage == 2 && plane != anInt985) {
				anInt985 = plane;
				method24(plane);
			}
			if (loadingStage == 2) {
				Rasterizer.triangles = 0;
				try {
					landscapeScene.method313(xCameraPos, yCameraPos, xCameraCurve, zCameraPos, method121(), yCameraCurve);
					landscapeScene.clearObj5Cache();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (scriptManager != null && loadingStage == 2 && plane == anInt985 && !loggedIn) {
				scriptManager.cycle();
			}
			Rasterizer.centerX = canvasCenterX;
			Rasterizer.centerY = canvasCenterY;
			Rasterizer.lineOffsets = canvasPixels;
		}
		int x = (getClientWidth() / 2) - (box.myWidth / 2);
		int y = (getClientHeight() / 2) - (box.myHeight / 2);
//		if (loadedImages || (new File(signlink.cacheLocation() + "spritearchive.dat").exists() && new File(signlink.cacheLocation() + "spritearchive.idx").exists())) {
//			if (isFixed()) {
//				if (background != null && background[0] != null && background[1] != null && background[2] != null && background[3] != null) {
//					background[0].drawImage((getClientWidth() / 2) - 382, (getClientHeight() / 2) - 251);
//					background[1].drawImage(getClientWidth() / 2 + 1, (getClientHeight() / 2) - 251);
//					background[2].drawImage((getClientWidth() / 2) - 382, getClientHeight() / 2 + 1);
//					background[3].drawImage(getClientWidth() / 2 + 1, getClientHeight() / 2 + 1);
//				}
//			} else {
//				background[4].drawImage((getClientWidth() / 2) - (background[4].myWidth / 2), (getClientHeight() / 2) - (background[4].myHeight / 2));
//			}
//		} else {
//			DrawingArea474.drawFilledPixels(0, 0, getClientWidth(), getClientHeight(), 0x1e1e1e);
//		}
		if (bannerAlpha < 256) {
			bannerAlpha += 16;
		}
		if (bannerAlpha > 256) {
			bannerAlpha = 256;
		}
		banner.drawARGBImage((getClientWidth() / 2) - (banner.myWidth / 2), (y / 2) - (banner.myHeight / 2), bannerAlpha);
		if (loginScreenState == LOGIN) {
			if (Accounts.accounts != null) {
				displayAccounts();
			}
			box.drawARGBImage(x, y);
			header.drawARGBImage(x + 23, y);
			arial[2].drawStringCenter("Login", x + 120, y + 27, 0xffffff, true);
			arial[1].drawString("Username:", x + 29, y + 60, 0xffffff, true);
			input.drawImage(x + 29, y + 63);
			arial[1].drawString("Password:", x + 29, y + 112, 0xffffff, true);
			input.drawImage(x + 29, y + 115);
			handleGlow(1);
			arial[1].drawString(capitalize(myUsername) + ((loginScreenCursorPos == 0) & (loopCycle % 40 < 20) ? "|" : ""), x + 36, y + 83, 0xffffff, true);
			arial[1].drawString(TextClass.passwordField(myPassword) + ((loginScreenCursorPos == 1) & (loopCycle % 40 < 20) ? "|" : ""), x + 36, y + 135, 0xffffff, true);
			arial[0].drawString("Forgotten Password?", x + 29, y + 162, linkColor[titleHover == 5 ? 1 : 0], true);
			DrawingArea474.drawHorizontalLine(x + 29 + 2, y + 164, 95, 0);
			DrawingArea474.drawHorizontalLine(x + 29 + 1, y + 163, 95, linkColor[titleHover == 5 ? 1 : 0]);
			if (!hideButtons) {
				play.drawARGBImage(x + 308, y + 62);
				if (loginMessage1.length() == 0 && loginMessage2.length() == 0) {
					handleGlow(0);
				}
			}
			displayOptions();
			displayMessages();
		} else if (loginScreenState == CREATE) {
			DrawingArea474.drawAlphaFilledPixels(0, 0, getClientWidth(), getClientHeight(), 0, 200);
			int formY = (getClientHeight() / 2) - (formbox.myHeight / 2);
			formbox.drawARGBImage(x, formY);
			header.drawARGBImage(x + 23, formY);
			handleGlow(2);
			arial[2].drawStringCenter("Create Account", x + 150, formY + (getCreate().canCreate() ? 24 : 27), 0xffffff, true);
			back[titleHover == 4 ? 1 : 0].drawARGBImage(x + 23, formY);
			arial[2].drawStringCenter("Login", x + 55, formY + 27, 0xffffff, true);
			String[] field = { "Username*:", "Password*:", "Confirm password*:", "Email address*:", "Referrer (optional):" };
			String[] text = { getCreate().getName(), getCreate().getPassword(), getCreate().confirm, getCreate().getEmail(), getCreate().getReferrer() };
			for (int index = 0, inputY = formY + 65; index < field.length; index++, inputY += 55) {
				arial[1].drawString(field[index], x + 30, inputY - 2, 0xffffff, true);
				input.drawImage(x + 30, inputY);
				info[0].drawImage(x + input.myWidth + 35, inputY + 5);
				if (text[index].length() > 0) {
					status[getCreate().verified[index] ? 1 : 0].drawImage(x + input.myWidth + 40 + info[0].myWidth, inputY + 5);
					if (mouseInRegion2(x + input.myWidth + 40 + info[0].myWidth, x + input.myWidth + 40 + info[0].myWidth + status[0].myWidth, inputY + 5, inputY + 5 + info[0].myHeight)) {
						status_hover[getCreate().verified[index] ? 1 : 0].drawImage(x + input.myWidth + 40 + info[0].myWidth, inputY + 5);
						drawTooltip(index, x + input.myWidth + info[0].myWidth + status[0].myWidth + 42, inputY + 5, true);
					}
				}
				if (mouseInRegion2(x + input.myWidth + 35, x + input.myWidth + 35 + info[0].myWidth, inputY + 5, inputY + 5 + info[0].myHeight)) {
					info[1].drawImage(x + input.myWidth + 35, inputY + 5);
					drawTooltip(index, x + input.myWidth + info[0].myWidth + 37, inputY + 5, false);
				}
				arial[1].drawString((index == 1 || index == 2 ? TextClass.passwordField(text[index]) : text[index]) + ((getCreate().getCursorPos() == index) & (loopCycle % 40 < 20) ? "|" : ""), x + 37, inputY + 20, 0xffffff, true);
			}
			arial[0].drawStringCenter("* fields marked with an asterik are required.", getClientWidth() / 2, y + 246, 0xffffff, true);
		} else if (loginScreenState == RECOVER) {
			DrawingArea474.drawAlphaFilledPixels(0, 0, getClientWidth(), getClientHeight(), 0, 200);
			box.drawARGBImage(x, y);
			header.drawARGBImage(x + 23, y);
			handleGlow(3);
			arial[2].drawStringCenter("Recover Account", x + 150, y + (getRecovery().canRecover() ? 24 : 27), 0xffffff, true);
			back[titleHover == 4 ? 1 : 0].drawARGBImage(x + 23, y);
			arial[2].drawStringCenter("Login", x + 55, y + 27, 0xffffff, true);
			String[] field = { "Username:", "Email address:" };
			String[] text = { getRecovery().getName(), getRecovery().getEmail() };
			for (int index = 0, inputY = y + 65; index < field.length; index++, inputY += 55) {
				arial[1].drawString(field[index], x + 30, inputY - 2, 0xffffff, true);
				input.drawImage(x + 30, inputY);
				info[0].drawImage(x + input.myWidth + 35, inputY + 5);
				if (text[index].length() > 0) {
					status[getRecovery().verified[index] ? 1 : 0].drawImage(x + input.myWidth + 40 + info[0].myWidth, inputY + 5);
					if (mouseInRegion2(x + input.myWidth + 40 + info[0].myWidth, x + input.myWidth + 40 + info[0].myWidth + status[0].myWidth, inputY + 5, inputY + 5 + info[0].myHeight)) {
						status_hover[getRecovery().verified[index] ? 1 : 0].drawImage(x + input.myWidth + 40 + info[0].myWidth, inputY + 5);
						drawTooltip(index, x + input.myWidth + info[0].myWidth + status[0].myWidth + 42, inputY + 5, true);
					}
				}
				if (mouseInRegion2(x + input.myWidth + 35, x + input.myWidth + 35 + info[0].myWidth, inputY + 5, inputY + 5 + info[0].myHeight)) {
					info[1].drawImage(x + input.myWidth + 35, inputY + 5);
					drawTooltip(index, x + input.myWidth + info[0].myWidth + 37, inputY + 5, false);
				}
				arial[1].drawString(text[index] + ((getRecovery().getCursorPos() == index) & (loopCycle % 40 < 20) ? "|" : ""), x + 37, inputY + 20, 0xffffff, true);
			}
			displayMessages();
		}
		title.drawGraphics(0, 0, super.graphics);
	}

	public void drawTooltip(int info, int x, int y, boolean error) {
		String[] text = null;
		switch (info) {
			case 0:
				text = error ? (loginScreenState == CREATE ? getCreate().usernameError : getRecovery().usernameError) : (loginScreenState == CREATE ? getCreate().usernameInfo : getRecovery().usernameInfo);
				break;
			case 1:
				text = error ? (loginScreenState == CREATE ? getCreate().passwordError : getRecovery().emailError) : (loginScreenState == CREATE ? getCreate().passwordInfo : getRecovery().emailInfo);
				break;
			case 2:
				text = error ? getCreate().confirmError : getCreate().confirmInfo;
				break;
			case 3:
				text = error ? getCreate().emailError : getCreate().emailInfo;
				break;
			case 4:
				text = error ? getCreate().referrerError : getCreate().referrerInfo;
				break;
		}
		boolean verified = (loginScreenState == CREATE ? getCreate().verified[info] : getRecovery().verified[info]);
		if (text != null || (error && verified)) {
			if (error && verified) {
				text = new String[]{ "This field is valid." };
			}
			int width = 0;
			for (int index = 0; index < text.length; index++) {
				if (newSmallFont.getTextWidth(text[index]) > width) {
					width = newSmallFont.getTextWidth(text[index]) + 10;
				}
			}
			int height = (text.length * 15) + 8;
			DrawingArea474.drawRoundedRectangle(x, y, width, height, 0, 150, true, false);
			for (int index = 0; index < text.length; index++) {
				newSmallFont.drawBasicString(text[index], x + 5, y + 15 + (15 * index), 0xffffff, 0);
			}
		}
	}

	public void drawFlames() {
		drawingFlames = true;
		try {
			long l = System.currentTimeMillis();
			int i = 0;
			int j = 20;
			while (aBoolean831) {
				anInt1208++;
				if (++i > 10) {
					long l1 = System.currentTimeMillis();
					int k = (int) (l1 - l) / 10 - j;
					j = 40 - k;
					if (j < 5)
						j = 5;
					i = 0;
					l = l1;
				}
				try {
					Thread.sleep(j);
				} catch (Exception _ex) {
				}
			}
		} catch (Exception _ex) {
		}
		drawingFlames = false;
	}

	public void raiseWelcomeScreen() {
		welcomeScreenRaised = true;
	}

	public void updateEntity(JagexBuffer stream, int packetId) {
		if (packetId == 84) { //send ground item amount
			int offset = stream.readUnsignedByte();
			int x = bigRegionX + (offset >> 4 & 7);
			int y = bigRegionY + (offset & 7);
			int itemId = stream.readUnsignedShort();
			int oldAmount = stream.readUnsignedShort();
			int newAmount = stream.readUnsignedShort();
			if (x >= 0 && y >= 0 && x < 104 && y < 104) {
				Deque class19_1 = groundEntity[plane][x][y];
				if (class19_1 != null) {
					for (Item item = (Item) class19_1
							.head(); item != null; item = (Item) class19_1
							.next()) {
						if (item.id != (itemId & 0x7fff)
								|| item.amount != oldAmount)
							continue;
						item.amount = newAmount;
						break;
					}
					refreshGroundItems(x, y);
				}
			}
			return;
		}
		if (packetId == 105) { //send sound
			int firstOffset = stream.readUnsignedByte();
			int firstX = bigRegionX + (firstOffset >> 4 & 7);
			int firstY = bigRegionY + (firstOffset & 7);
			int soundId = stream.readUnsignedShort();
			int secondOffset = stream.readUnsignedByte();
			int secondX = secondOffset >> 4 & 0xf;
			int secondY = secondOffset & 7;
			if (myPlayer.smallX[0] >= firstX - secondX
					&& myPlayer.smallX[0] <= firstX + secondX
					&& myPlayer.smallY[0] >= firstY - secondX
					&& myPlayer.smallY[0] <= firstY + secondX && aBoolean848
					&& !lowMemory && currentSound < 50) {
				anIntArray1207[currentSound] = soundId;
				anIntArray1241[currentSound] = secondY;
				anIntArray1250[currentSound] = Sounds.sound[soundId];
				currentSound++;
			}
		}
		if (packetId == 215) { //not sure
			int id = stream.readShortA();
			int offset = stream.readByteS();
			int x = bigRegionX + (offset >> 4 & 7);
			int y = bigRegionY + (offset & 7);
			int playerIndex = stream.readShortA();
			int amount = stream.readUnsignedShort();
			if (x >= 0 && y >= 0 && x < 104 && y < 104
					&& playerIndex != playerId) {
				Item item = new Item();
				item.id = id;
				item.amount = amount;
				if (groundEntity[plane][x][y] == null)
					groundEntity[plane][x][y] = new Deque();
				groundEntity[plane][x][y].append(item);
				refreshGroundItems(x, y);
			}
			return;
		}
		if (packetId == 156) { //send ground item removal
			int offset = stream.readByteA();
			int x = bigRegionX + (offset >> 4 & 7);
			int y = bigRegionY + (offset & 7);
			int itemId = stream.readUnsignedShort();
			if (x >= 0 && y >= 0 && x < 104 && y < 104) {
				Deque class19 = groundEntity[plane][x][y];
				if (class19 != null) {
					for (Item item = (Item) class19.head(); item != null; item = (Item) class19
							.next()) {
						if (item.id != (itemId & 0x7fff))
							continue;
						item.remove();
						break;
					}
					if (class19.head() == null)
						groundEntity[plane][x][y] = null;
					refreshGroundItems(x, y);
				}
			}
			return;
		}
		if (packetId == 160) { //send object animation
			int offset = stream.readByteS();
			int x = bigRegionX + (offset >> 4 & 7);
			int y = bigRegionY + (offset & 7);
			int objectAttributes = stream.readByteS();
			int objectType = objectAttributes >> 2;
			int objectRotation = objectAttributes & 3;
			int j16 = objectClassType[objectType];
			int animationId = stream.readShortA();
			if (x >= 0 && y >= 0 && x < 103 && y < 103) {
				int j18 = intGroundArray[plane][x][y];
				int i19 = intGroundArray[plane][x + 1][y];
				int l19 = intGroundArray[plane][x + 1][y + 1];
				int k20 = intGroundArray[plane][x][y + 1];
				if (j16 == 0) {
					Object1 class10 = landscapeScene.method296(plane,
							x, y);
					if (class10 != null) {
						int k21 = class10.uid >> 14 & 0x7fff;
						if (objectType == 2) {
							class10.aClass30_Sub2_Sub4_278 = new GameObject(
									k21, 4 + objectRotation, 2, i19, l19, j18, k20, animationId,
									false);
							class10.aClass30_Sub2_Sub4_279 = new GameObject(
									k21, objectRotation + 1 & 3, 2, i19, l19, j18, k20,
									animationId, false);
						} else {
							class10.aClass30_Sub2_Sub4_278 = new GameObject(
									k21, objectRotation, objectType, i19, l19, j18, k20, animationId,
									false);
						}
					}
				}
				if (j16 == 1) {
					Object2 class26 = landscapeScene.method297(x, y,
							plane);
					if (class26 != null)
						class26.aClass30_Sub2_Sub4_504 = new GameObject(
								class26.uid >> 14 & 0x7fff, 0, 4, i19, l19,
								j18, k20, animationId, false);
				}
				if (j16 == 2) {
					Object5 class28 = landscapeScene.method298(x, y,
							plane);
					if (objectType == 11)
						objectType = 10;
					if (class28 != null)
						class28.aClass30_Sub2_Sub4_521 = new GameObject(
								class28.uid >> 14 & 0x7fff, objectRotation, objectType, i19, l19,
								j18, k20, animationId, false);
				}
				if (j16 == 3) {
					Object3 class49 = landscapeScene.method299(y, x,
							plane);
					if (class49 != null)
						class49.aClass30_Sub2_Sub4_814 = new GameObject(
								class49.uid >> 14 & 0x7fff, objectRotation, 22, i19, l19,
								j18, k20, animationId, false);
				}
			}
			return;
		}
		if (packetId == 147) { //transforms player into object
			int offset = stream.readByteS();
			int regionX = bigRegionX + (offset >> 4 & 7);
			int regionY = bigRegionY + (offset & 7);
			int playerId = stream.readUnsignedShort();
			byte firstByteOffsetX = stream.method430();
			int secondaryModelPriority = stream.readLEShort();
			byte secondaryByteOffsetY = stream.method429();
			int primaryModelPriority = stream.readUnsignedShort();
			int objectAttributes = stream.readByteS();
			int objectType = objectAttributes >> 2;
			int objectRotation = objectAttributes & 3;
			int object = objectClassType[objectType];
			byte secondaryByteOffsetX = stream.readByte();
			int objectId = stream.readUnsignedShort();
			byte firstByteOffsetY = stream.method429();				
			Player player;
			if (playerId == this.playerId)
				player = myPlayer;
			else
				player = playerArray[playerId];
			if (player != null) {
				ObjectDefinition definition = ObjectDefinition.forId(objectId);
				System.out.println("objectId=" + definition.id);
				int tile = intGroundArray[plane][regionX][regionY];
				int tileX = intGroundArray[plane][regionX + 1][regionY];
				int tileYX = intGroundArray[plane][regionX + 1][regionY + 1];
				int tileY = intGroundArray[plane][regionX][regionY + 1];
				Model model = definition.getModelFromPosition(objectType, objectRotation, tile, tileX, tileYX, tileY,
						-1, null);
				if (model != null) {
					requestGameObjectSpawn(primaryModelPriority + 1, -1, 0, object, regionY, 0, plane, regionX,
							secondaryModelPriority + 1);
					player.secondaryModel = secondaryModelPriority + loopCycle;
					player.primaryModel = primaryModelPriority + loopCycle;
					player.objectTransformatiomModel = model;
					int objectSizeX = definition.sizeX;
					int objectSizeY = definition.sizeY;
					if (objectRotation == 1 || objectRotation == 3) {
						objectSizeX = definition.sizeY;
						objectSizeY = definition.sizeX;
					}
					player.modelOffsetX = regionX * 128 + objectSizeX * 64;
					player.modelOffsetY = regionY * 128 + objectSizeY * 64;
					player.modelOffsetZ = getHeight(plane, player.modelOffsetY,
							player.modelOffsetX);
					if (secondaryByteOffsetX > firstByteOffsetX) {
						byte byte4 = secondaryByteOffsetX;
						secondaryByteOffsetX = firstByteOffsetX;
						firstByteOffsetX = byte4;
					}
					if (firstByteOffsetY > secondaryByteOffsetY) {
						byte byte5 = firstByteOffsetY;
						firstByteOffsetY = secondaryByteOffsetY;
						secondaryByteOffsetY = byte5;
					}
					player.anInt1719 = regionX + secondaryByteOffsetX;
					player.anInt1721 = regionX + firstByteOffsetX;
					player.anInt1720 = regionY + firstByteOffsetY;
					player.anInt1722 = regionY + secondaryByteOffsetY;
				}
			}
		}
		if (packetId == 151) { //sends an object
			int offset = stream.readByteA();
			int regionX = bigRegionX + (offset >> 4 & 7);
			int regionY = bigRegionY + (offset & 7);
			int objectId = stream.readLEShort();
			int objectAttributes = stream.readByteS();
			int objectType = objectAttributes >> 2;
			int objectRotation = objectAttributes & 3;
			int l17 = objectClassType[objectType];
			if (regionX >= 0 && regionY >= 0 && regionX < 104 && regionY < 104)
				requestGameObjectSpawn(-1, objectId, objectRotation, l17, regionY, objectType, plane, regionX, 0);
			return;
		}
		if (packetId == 4) { //sends graphic
			int offset = stream.readUnsignedByte();
			int regionX = bigRegionX + (offset >> 4 & 7);
			int regionY = bigRegionY + (offset & 7);
			int graphicId = stream.readUnsignedShort();
			int height = stream.readUnsignedByte();
			int delay = stream.readUnsignedShort();
			if (regionX >= 0 && regionY >= 0 && regionX < 104 && regionY < 104) {
				regionX = regionX * 128 + 64;
				regionY = regionY * 128 + 64;
				GraphicModel model = new GraphicModel(
										plane, loopCycle, delay, graphicId, getHeight(plane,
										regionY, regionX) - height, regionY, regionX);
				aClass19_1056.append(model);
			}
			return;
		}
		if (packetId == 44) { //sends ground item
			int itemId = stream.readLEShortA();
			if (itemId == -1 || itemId == 65535)
				return;
			int itemAmount = stream.readUnsignedShort();
			int offset = stream.readUnsignedByte();
			int regionX = bigRegionX + (offset >> 4 & 7);
			int regionY = bigRegionY + (offset & 7);
			if (regionX >= 0 && regionY >= 0 && regionX < 104 && regionY < 104) {
				Item item = new Item();
				item.id = itemId;
				item.amount = itemAmount;
				if (groundEntity[plane][regionX][regionY] == null)
					groundEntity[plane][regionX][regionY] = new Deque();
				groundEntity[plane][regionX][regionY].append(item);
				refreshGroundItems(regionX, regionY);
			}
			return;
		}
		if (packetId == 101) { //send object removal
			int attributes = stream.readByteC();
			int type = attributes >> 2; //TODO type = index?
			int face = attributes & 3;
			int i11 = objectClassType[type];
			int offset = stream.readUnsignedByte();
			int regionX = bigRegionX + (offset >> 4 & 7);
			int regionY = bigRegionY + (offset & 7);
			if (regionX >= 0 && regionY >= 0 && regionX < 104 && regionY < 104)
				requestGameObjectSpawn(-1, -1, face, i11, regionY, type, plane, regionX, 0);
			return;
		}
		if (packetId == 117) { //send projectile
			int offset = stream.readUnsignedByte();
			int regionX = bigRegionX + (offset >> 4 & 7);
			int regionY = bigRegionY + (offset & 7);
			int offsetY = regionX + stream.readByte();
			int offsetX = regionY + stream.readByte();
			int lockon = stream.readSignedShort();
			int id = stream.readUnsignedShort();
			int startingZ = stream.readUnsignedByte() * 4;
			int endingZ = stream.readUnsignedByte() * 4;
			int delay = stream.readUnsignedShort();
			int speed = stream.readUnsignedShort();
			int slope = stream.readUnsignedByte();
			int radius = stream.readUnsignedByte();
			if (regionX >= 0 && regionY >= 0 && regionX < 104 && regionY < 104 && offsetY >= 0 && offsetX >= 0 && offsetY < 104 && offsetX < 104 && id != 65535) {
				regionX = regionX * 128 + 64;
				regionY = regionY * 128 + 64;
				offsetY = offsetY * 128 + 64;
				offsetX = offsetX * 128 + 64;
				ProjectileModel projectile = new ProjectileModel(slope,
						endingZ, delay + loopCycle, speed + loopCycle, radius, plane,
						getHeight(plane, regionY, regionX) - startingZ, regionY, regionX, lockon, id);
				projectile.method455(delay + loopCycle, offsetX,
						getHeight(plane, offsetX, offsetY) - endingZ, offsetY);
				aClass19_1013.append(projectile);
			}
		}
	}

	public static void setLowMem() {
		LandscapeScene.lowMem = true;
		Rasterizer.lowMem = true;
		lowMemory = true;
		SceneGraph.lowMemory = true;
		ObjectDefinition.lowMem = true;
	}

	public void method139(JagexBuffer stream) {
		stream.initBitAccess();
		int countReceived = stream.readBits(8);
		//System.out.println("countReceived=" + countReceived + "; npcCount=" + npcCount);
		if (countReceived < npcCount) {
			for (int l = countReceived; l < npcCount; l++)
				anIntArray840[anInt839++] = npcIndices[l];

		}
		if (countReceived > npcCount) {
			signlink.reporterror(myUsername + ": too many npcs");
			throw new RuntimeException("eek");
		}
		npcCount = 0;
		for (int i1 = 0; i1 < countReceived; i1++) {
			int j1 = npcIndices[i1];
			NPC npc = npcArray[j1];
			int k1 = stream.readBits(1);
			if (k1 == 0) {
				npcIndices[npcCount++] = j1;
				npc.anInt1537 = loopCycle;
			} else {
				int l1 = stream.readBits(2);
				if (l1 == 0) {
					npcIndices[npcCount++] = j1;
					npc.anInt1537 = loopCycle;
					anIntArray894[anInt893++] = j1;
				} else if (l1 == 1) {
					npcIndices[npcCount++] = j1;
					npc.anInt1537 = loopCycle;
					int i2 = stream.readBits(3);
					npc.moveInDir(false, i2);
					int k2 = stream.readBits(1);
					if (k2 == 1)
						anIntArray894[anInt893++] = j1;
				} else if (l1 == 2) {
					npcIndices[npcCount++] = j1;
					npc.anInt1537 = loopCycle;
					int j2 = stream.readBits(3);
					npc.moveInDir(true, j2);
					int l2 = stream.readBits(3);
					npc.moveInDir(true, l2);
					int i3 = stream.readBits(1);
					if (i3 == 1)
						anIntArray894[anInt893++] = j1;
				} else if (l1 == 3)
					anIntArray840[anInt839++] = j1;
			}
		}

	}

	public void processLoginScreenInput() {
		checkSize();
		if (loginMessage1.length() > 0 || loginMessage2.length() > 0) {
			int width = 300;
			int height = 50;
			int x = (getClientWidth() / 2) - (width / 2);
			int y = (getClientHeight() / 2) - (height / 2);
			//create account
			if (!mouseInRegion2(x + width - 16, x + width, y, y + 16) && getCreate().available) {
				if (super.clickMode3 == 1 && clickInRegion2(x, x + width, y, y + height)) {
					getCreate().username = myUsername;
					getCreate().password = myPassword;
					getCreate().verified[0] = getCreate().checkUsername();
					getCreate().verified[1] = getCreate().checkPassword();
					loginScreenState = CREATE;
					loginMessage1 = "";
					loginMessage2 = "";
					messageAlpha = 0;
					messageX = 0;
					messageY = 0;
					messageWidth = 0;
					messageHeight = 0;
				}
			}
			//close button
			if (super.clickMode3 == 1 && clickInRegion2(x + width - 16, x + width, y, y + 16)) {
				loginMessage1 = "";
				loginMessage2 = "";
			}
			if (mouseInRegion2(x + width - 16, x + width, y, y + 16)) {
				titleHover = 3;
			} else {
				titleHover = -1;
			}
			return;
		}// else {
			if (loginScreenState == LOGIN && loginMessage1.length() == 0 && loginMessage2.length() == 0) {
				int x = (getClientWidth() / 2) - (box.myWidth / 2);
				int y = (getClientHeight() / 2) - (box.myHeight / 2);
				int[] screen_x = { x + (322 / 2) - 45 - 27, x + 20 + (322 / 2) - 27, x + 85 + (322 / 2) - 27 };
				int[] detail_x = { x + 85, x + 200 };
				//options
				if (mouseInRegion2(x + 20, x + 342, y + 180, y + 180 + optionsHeight)) {
					titleHover = 1;
				//accounts
				} else if (mouseInRegion2(x + 5 - accountsX, x + 20, y + 25, y + 165)) {
					titleHover = 2;
				} else if (mouseInRegion2(x + 29, x + 125, y + 153, y + 165)) {
					titleHover = 5;
				} else {
					titleHover = -1;
				}
				//recover
				if (super.clickMode3 == 1 && clickInRegion2(x + 29, x + 125, y + 153, y + 165)) {
					loginScreenState = RECOVER;
				}
				if (titleHover == 1) {
					//screen mode
					if (mouseInRegion2(screen_x[0], screen_x[0] + 54, y + 205, y + 248)) {
						screenHover = 0;
					} else if (mouseInRegion2(screen_x[1], screen_x[1] + 54, y + 205, y + 248)) {
						screenHover = 1;
					} else if (mouseInRegion2(screen_x[2], screen_x[2] + 54, y + 205, y + 248)) {
						screenHover = 2;
					} else {
						screenHover = -1;
					}
					if (super.clickMode3 == 1 && clickInRegion2(screen_x[0], screen_x[0] + 54, y + 205, y + 248)) {
						toggleSize(0);
					} else if (super.clickMode3 == 1 && clickInRegion2(screen_x[1], screen_x[1] + 54, y + 205, y + 248)) {
						toggleSize(1);
					} else if (super.clickMode3 == 1 && clickInRegion2(screen_x[2], screen_x[2] + 54, y + 205, y + 248)) {
						toggleSize(2);
					}
					//detail
					if (mouseInRegion2(detail_x[0], detail_x[0] + 77, y + 260, y + 313)) {
						detailHover = 0;
					} else if (mouseInRegion2(detail_x[1], detail_x[1] + 77, y + 260, y + 313)) {
						detailHover = 1;
					} else {
						detailHover = -1;
					}
					if (super.clickMode3 == 1 && clickInRegion2(detail_x[0], detail_x[0] + 77, y + 260, y + 313)) {
						setLowMem();
					} else if (super.clickMode3 == 1 && clickInRegion2(detail_x[1], detail_x[1] + 77, y + 260, y + 313)) {
						setHighMemory();
					}
				}
				//edit accounts
				if (super.clickMode3 == 1 && clickInRegion2(x - accountsX + 5, x, y + 28, y + 42) && titleHover == 2) {
					editAccounts = !editAccounts;
					Accounts.write();
				}
				if (mouseInRegion2(x - accountsX + 5, x, y + 28, y + 42) && titleHover == 2) {
					editHover = 0;
				} else {
					editHover = -1;
				}
				/* Accounts */
				if (titleHover == 2) {
					int text_x = x - accountsX + 5;
					int text_y = y + 62;
					if (Accounts.accounts != null) {
						accountHover = -1;
						for (int index = 0; index < Accounts.accounts.length; index++, text_y += 17) {
							if (mouseInRegion2(text_x, x + 10, text_y - 12, text_y)) {
								accountHover = index;
							}
							if (super.clickMode3 == 1 && clickInRegion2(text_x, x + 10, text_y - 12, text_y)) {
								if (editAccounts) {
									Accounts.remove(Accounts.getAccount(Accounts.sortNamesByUsage()[index]));
								} else {
									getCreate().created = false;
									loginFailures = 0;
									login(Accounts.getAccount(Accounts.sortNamesByUsage()[index]).name, Accounts.getAccount(Accounts.sortNamesByUsage()[index]).password, false, true);
									if(loggedIn) {
										return;
									}
								}
							}
						}
					}
				}
				/**/
				if (super.clickMode3 == 1 && clickInRegion2(x + 29, x + 29 + input.myWidth, y + 63, y + 63 + input.myHeight)) {
					loginScreenCursorPos = 0;
				}
				if (super.clickMode3 == 1 && clickInRegion2(x + 29, x + 29 + input.myWidth, y + 115, y + 115 + input.myHeight)) {
					loginScreenCursorPos = 1;
				}
				if (super.clickMode3 == 1 && clickInRegion2(x + 308, x + 308 + play.myWidth, y + 62, y + 62 + play.myHeight)) {
					getCreate().created = false;
					loginFailures = 0;
					login(myUsername, myPassword, false, false);
					if(loggedIn)
						return;
				}
				do {
					int key = readCharacter();
					if(key == -1) {
						break;
					}
					boolean validKey = false;
					for(int index = 0; index < validUserPassChars.length(); index++) {
						if(key != validUserPassChars.charAt(index)) {
							continue;
						}
						validKey = true;
						break;
					}
					if(loginScreenCursorPos == 0) {
						if(key == 8 && myUsername.length() > 0) {
							myUsername = myUsername.substring(0, myUsername.length() - 1);
						}
						if(key == 9 || key == 10 || key == 13) {
							loginScreenCursorPos = 1;
						}
						if(validKey) {
							myUsername = myUsername + (char)key;
						}
						if(myUsername.length() > 12) {
							myUsername = myUsername.substring(0, 12);
						}
					} else if(loginScreenCursorPos == 1) {
						if(key == 8 && myPassword.length() > 0) {
							myPassword = myPassword.substring(0, myPassword.length() - 1);
						}
						if (key == 9) {
							loginScreenCursorPos = 0;
						}
						if(key == 10 || key == 13) {
							getCreate().created = false;
							loginFailures = 0;
							login(myUsername, myPassword, false, false);
							if(loggedIn)
								return;
						}
						if(validKey) {
							myPassword += (char)key;
						}
						if(myPassword.length() > 20) {
							myPassword = myPassword.substring(0, 20);
						}
					}
				} while(true);
				return;
			}
			if (loginScreenState == CREATE) {
				getCreate().processInput();
			}
			if (loginScreenState == RECOVER) {
				getRecovery().processInput();
			}
		//}
	}

	public void processLoginScreenInputOLD() {
		/*
		 * if (clientWidth != super.getSize().width || clientHeight !=
		 * super.getSize().height) { clientWidth = super.getSize().width;
		 * clientHeight = super.getSize().height; if (frame != null)
		 * frame.setSize(clientWidth + (os.contains("7") ? 16 : 10),
		 * clientHeight + (os.contains("7") ? 38 : 56)); titleScreen = new
		 * RSImageProducer(clientWidth, clientHeight, getGameComponent()); }
		 */
		int centerX = clientWidth / 2, centerY = clientHeight / 2;
		int messageWidth = 270;
		int messageHeight = 100;
		int x1 = centerX - (messageWidth / 2);
		int y1 = centerY - (messageHeight / 2);
		if (loginMessage1.length() > 0 || loginMessage2.length() > 0) {
			if (super.clickMode3 == 1
					&& clickInRegion(x1 + messageWidth - 19, y1 + 4, x1
							+ messageWidth - 3, y1 + 20)) {
				loginMessage1 = "";
				loginMessage2 = "";
			}
		}
		/*
		 * if (loginScreenState != 3) { if (super.clickMode3 == 1 &&
		 * clickInRegion(clientWidth - 37, clientHeight - 36, clientWidth - 5,
		 * clientHeight - 5)) { loginScreenState = 2; } }
		 */
		if (loginScreenState != 2) {
			if (mouseInRegion(clientWidth - 37, clientHeight - 36,
					clientWidth - 5, clientHeight - 5)) {
				loginHover = 0;// options
			} else if (mouseInRegion(35, clientHeight - 40, 76,
					clientHeight - 10)) {
				loginHover = 1;// world select
			} else if (mouseInRegion(centerX - (titleButton[1].myWidth / 2),
					centerY - 93, centerX + (titleButton[1].myWidth / 2),
					centerY - 67)) {
				loginHover = 2;// username field
			} else if (mouseInRegion(centerX - (titleButton[1].myWidth / 2),
					centerY + (!enableServerIP ? -47 : -67), centerX
							+ (titleButton[1].myWidth / 2), centerY
							+ (!enableServerIP ? -21 : -41))) {
				loginHover = 3;// password field
			} else if (mouseInRegion(centerX - (titleButton[0].myWidth / 2),
					centerY + (!enableServerIP ? -2 : 28), centerX
							+ (titleButton[0].myWidth / 2), centerY
							+ (!enableServerIP ? 24 : 54))) {
				loginHover = 5;// login button
			} else if (mouseInRegion(x1 + messageWidth - 19, y1 + 4, x1
					+ messageWidth - 3, y1 + 20)) {
				loginHover = 6;
			} else if (enableServerIP) {
				if (mouseInRegion(centerX - (titleButton[1].myWidth / 2),
						centerY + 11, centerX + (titleButton[1].myWidth / 2),
						centerY + 37)) {
					loginHover = 4;// server ip field
				}
			} else {
				loginHover = -1;
			}
		}
		if (loginScreenState == 0) {
			/**
			 * Recover your password
			 */
			String passwordRecovery = "http://Battle Royale.net/Home/login.php?do=lostpw";
			if (mouseInRegion(clientWidth - 456, clientHeight - 224, clientWidth - 314, clientHeight - 214)) {
				loginHover = 10;
			} 
			if (super.clickMode3 == 1 && clickInRegion(clientWidth - 456, clientHeight - 224, clientWidth - 314, clientHeight - 214)) {
				try {
				java.net.URI WebsiteInput = new java.net.URI(passwordRecovery);
				SearchWeb.browse(WebsiteInput);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
			/**
			 * Create an Account
			 */
			String createAccount = "http://Battle Royale.net/Home/register.php";
			if (mouseInRegion(clientWidth - 446, clientHeight - 129, clientWidth - 323, clientHeight - 120)) {
				loginHover = 11;
			}
			if (super.clickMode3 == 1 && clickInRegion(clientWidth - 446, clientHeight - 129, clientWidth - 323, clientHeight - 120)) {
				try {
				java.net.URI WebsiteInput = new java.net.URI(createAccount);
				SearchWeb.browse(WebsiteInput);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
			/**
			 * FaceBook Page
			 */
			String facebook  = "http://www.facebook.com/pages/Battle Royale/363867143631781";
			if (mouseInRegion(clientWidth - 442, clientHeight - 190, clientWidth - 413, clientHeight - 161)) {
				loginHover = 12;
			}
			if (super.clickMode3 == 1 && clickInRegion(clientWidth - 442, clientHeight - 190, clientWidth - 413, clientHeight - 161)) {
				try {
				java.net.URI WebsiteInput = new java.net.URI(facebook);
				SearchWeb.browse(WebsiteInput);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
			/* Username */
			if (super.clickMode3 == 1
					&& clickInRegion(centerX - (titleButton[1].myWidth / 2),
							centerY - 93, centerX
									+ (titleButton[1].myWidth / 2),
							centerY - 67)) {
				loginScreenCursorPos = 0;
			}
			/* Password */
			if (super.clickMode3 == 1
					&& clickInRegion(centerX - (titleButton[1].myWidth / 2),
							centerY + (!enableServerIP ? -47 : -67), centerX
									+ (titleButton[1].myWidth / 2), centerY
									+ (!enableServerIP ? -21 : -41))) {
				loginScreenCursorPos = 1;
			}
			/* Server IP */
			if (enableServerIP) {
				if (super.clickMode3 == 1
						&& clickInRegion(
								centerX - (titleButton[1].myWidth / 2),
								centerY + 11, centerX
										+ (titleButton[1].myWidth / 2),
								centerY + 37)) {
					loginScreenCursorPos = 2;
				}
			}
			/* Login button */
			if (super.clickMode3 == 1
					&& clickInRegion(centerX - (titleButton[0].myWidth / 2),
							centerY + (!enableServerIP ? -2 : 28), centerX
									+ (titleButton[0].myWidth / 2), centerY
									+ (!enableServerIP ? 24 : 54))) {
				if (myUsername.length() > 0 && myPassword.length() > 0) {
					writeSettings();
					loginFailures = 0;
					login(capitalize(myUsername), myPassword, false, false);
					//loginScreenState = 3;
					if (loggedIn) {
						return;
					}
				} else {
					loginMessage2 = "Please enter a username and password.";
				}
			}
			/* Remember me */
			if (super.clickMode3 == 1
					&& clickInRegion(centerX
							- (titleButton[rememberMe ? 9 : 8].myWidth / 2)
							- 41, centerY + 27, centerX
							+ (titleButton[rememberMe ? 9 : 8].myWidth / 2)
							+ 41, centerY + 45)) {
				rememberMe = !rememberMe;
			}
			do {
				int keyPressed = readCharacter();
				if (keyPressed == -1)
					break;
				boolean flag1 = false;
				for (int i2 = 0; i2 < validUserPassChars.length(); i2++) {
					if (keyPressed != validUserPassChars.charAt(i2))
						continue;
					flag1 = true;
					break;
				}
				if (loginScreenCursorPos == 0) {
					if (keyPressed == 8 && myUsername.length() > 0)
						myUsername = capitalize(myUsername.substring(0,
								myUsername.length() - 1));
					if (keyPressed == 9 || keyPressed == 10 || keyPressed == 13)
						loginScreenCursorPos = 1;
					if (flag1) {
						myUsername += (char) keyPressed;
					}
					if (myUsername.length() > 12) {
						myUsername = capitalize(myUsername.substring(0, 12));
					}
				} else if (loginScreenCursorPos == 1) {
					if (keyPressed == 8 && myPassword.length() > 0)
						myPassword = myPassword.substring(0,
								myPassword.length() - 1);
					if (keyPressed == 9 || keyPressed == 10 || keyPressed == 13)
						loginScreenCursorPos = enableServerIP ? 2 : 0;
					if (flag1)
						myPassword += (char) keyPressed;
					if (myPassword.length() > 20)
						myPassword = myPassword.substring(0, 20);
				} /*else if (loginScreenCursorPos == 2) {
					if (keyPressed == 8 && Constants.HOST_ADDRESS.length() > 0)
						Constants.HOST_ADDRESS = Constants.HOST_ADDRESS.substring(0, Constants.HOST_ADDRESS.length() - 1);
					if (keyPressed == 9 || keyPressed == 10 || keyPressed == 13)
						loginScreenCursorPos = 0;
					if (flag1)
						Constants.HOST_ADDRESS += (char) keyPressed;
					if (myPassword.length() > 20)
						Constants.HOST_ADDRESS = Constants.HOST_ADDRESS.substring(0, 20);
				}*/
			} while (true);
			return;
			/**
			 * Login Lobby
			 */
		} else if (loginScreenState == 3) {
			if (super.clickMode3 == 1
					&& clickInRegion(clientWidth - 154, 63, clientWidth - 139,
							76)) {
				loginScreenState = 0;
			}
			if (mouseInRegion(clientWidth - 154, 63, clientWidth - 139, 76)) {
				loginHover = 9;
			} else if (mouseInRegion(x1 + messageWidth - 19, y1 + 4, x1
					+ messageWidth - 3, y1 + 20)) {
				loginHover = 6;
			} else {
				loginHover = -1;
			}
		}
	}

	public void markMinimap(Sprite sprite, int x, int y) {
		try {
			int offX = clientSize == 0 ? 0 : clientWidth - 249;
			int k = viewRotation + minimapRotation & 0x7ff;
			int l = x * x + y * y;
			if (l > 6400) {
				return;
			}
			int i1 = Rasterizer.SINE[k];
			int j1 = Rasterizer.COSINE[k];
			i1 = (i1 * 256) / (minimapZoom + 256);
			j1 = (j1 * 256) / (minimapZoom + 256);
			int k1 = y * i1 + x * j1 >> 16;
			int l1 = y * j1 - x * i1 >> 16;
			if (clientSize == 0)
				sprite.drawSprite(((105 + k1) - sprite.maxWidth / 2) + 4
						+ offX, 88 - l1 - sprite.maxHeight / 2 - 4);
			else
				sprite.drawSprite(((77 + k1) - sprite.maxWidth / 2) + 4
						+ (clientWidth - 167), 85 - l1 - sprite.maxHeight / 2
						- 4);
		} catch (Exception e) {

		}
	}

	public void method142(int i, int j, int k, int l, int i1, int j1, int k1) {
		if (i1 >= 1 && i >= 1 && i1 <= 102 && i <= 102) {
			if (lowMemory && j != plane)
				return;
			int i2 = 0;
			if (j1 == 0)
				i2 = landscapeScene.method300(j, i1, i);
			if (j1 == 1)
				i2 = landscapeScene.method301(j, i1, i);
			if (j1 == 2)
				i2 = landscapeScene.method302(j, i1, i);
			if (j1 == 3)
				i2 = landscapeScene.method303(j, i1, i);
			if (i2 != 0) {
				int i3 = landscapeScene.getObject(j, i1, i, i2);
				int j2 = i2 >> 14 & 0x7fff;
				int k2 = i3 & 0x1f;
				int l2 = i3 >> 6;
				if (j1 == 0) {
					landscapeScene.method291(i1, j, i, (byte) -119);
					ObjectDefinition class46 = ObjectDefinition.forId(j2);
					if (class46.walkable)
						clippingPlanes[j].unmarkWall(l2, k2,
								class46.aBoolean757, i1, i);
				}
				if (j1 == 1)
					landscapeScene.method292(i, j, i1);
				if (j1 == 2) {
					landscapeScene.method293(j, i1, i);
					ObjectDefinition class46_1 = ObjectDefinition.forId(j2);
					if (i1 + class46_1.sizeX > 103
							|| i + class46_1.sizeX > 103
							|| i1 + class46_1.sizeY > 103
							|| i + class46_1.sizeY > 103)
						return;
					if (class46_1.walkable)
						clippingPlanes[j].unmarkSolidClip(l2, class46_1.sizeX, i1,
								i, class46_1.sizeY, class46_1.aBoolean757);
				}
				if (j1 == 3) {
					landscapeScene.method294(j, i, i1);
					ObjectDefinition class46_2 = ObjectDefinition.forId(j2);
					if (class46_2.walkable && class46_2.hasActions)
						clippingPlanes[j].unmarkConcealed(i, i1);
				}
			}
			if (k1 >= 0) {
				int j3 = j;
				if (j3 < 3 && (byteGroundArray[1][i1][i] & 2) == 2)
					j3++;
				SceneGraph.method188(landscapeScene, k, i, l, j3,
						clippingPlanes[j], intGroundArray, i1, k1, j);
			}
		}
	}
	
	public void extractMapFiles(String directory) throws IOException {
		File folder = new File(directory);
		if (!folder.isDirectory()) {
			System.out.println("Directory not found...Enter a valid directory.");
			return;
		}
		for (int i = 0; i < mainCacheFile[4].size(); i++) {
			System.out.println("Extracting map: " + i);
			byte[] map = mainCacheFile[4].get(i);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			DeflaterOutputStream os = new GZIPOutputStream(bout);
			try {
				os.write(map);
				os.finish();
				DataOutputStream output = new DataOutputStream(new FileOutputStream(directory + i + ".gz"));
				output.write(bout.toByteArray());
			} finally {
				os.close();
			}
		}
	}

	public void maps() {
		for (int MapIndex = 0; MapIndex < 3536; MapIndex++) {
			byte[] abyte0 = GetMap(MapIndex);
			if (abyte0 != null && abyte0.length > 0) {
				mainCacheFile[4].insertIndex(abyte0.length, abyte0, MapIndex);
			}
		}
	}

	public byte[] GetMap(int Index) {
		try {
			// File Map = new File("./Cache/index4/"+Index+".gz");
			File Map = new File(signlink.getCacheLocation() + "index4/" + Index
					+ ".gz");
			// File Map = new File(System.getProperty("user.home") +
			// System.getProperty("file.separator") + name +
			// System.getProperty("file.separator")+ /index4/+"+Index+".gz");
			byte[] aByte = new byte[(int) Map.length()];
			FileInputStream Fis = new FileInputStream(Map);
			Fis.read(aByte);
			System.out.println("" + Index + " aByte = [" + aByte.toString() + "]!");
			Fis.close();
			return aByte;
		} catch (Exception e) {
			return null;
		}
	}

	public void updatePlayers(int i, JagexBuffer stream) {
		anInt839 = 0;
		anInt893 = 0;
		updateMovement(stream);
		method134(stream);
		addNewPlayer(stream, i);
		method49(stream);
		for (int k = 0; k < anInt839; k++) {
			int l = anIntArray840[k];
			if (playerArray[l].anInt1537 != loopCycle)
				playerArray[l] = null;
		}

		if (stream.currentOffset != i) {
			//throw new RuntimeException("Error reading player updating packet - [size, expectedSize] : [" + i + ", " + stream.currentOffset + "]");
			System.out.println("Error reading player updating packet - [size, expectedSize] : [" + i + ", " + stream.currentOffset + "]");
			return;
		}
		for (int i1 = 0; i1 < playerCount; i1++)
			if (playerArray[playerIndices[i1]] == null) {
				signlink.reporterror(myUsername
						+ " null entry packet pl list - pos:" + i1 + " size:"
						+ playerCount);
				throw new RuntimeException("eek");
			}

	}

	public void setCameraPos(int j, int k, int l, int i1, int j1, int k1) {
		int l1 = 2048 - k & 0x7ff;
		int i2 = 2048 - j1 & 0x7ff;
		int j2 = 0;
		int k2 = 0;
		int l2 = j;
		if (l1 != 0) {
			int i3 = Rasterizer.SINE[l1];
			int k3 = Rasterizer.COSINE[l1];
			int i4 = k2 * k3 - l2 * i3 >> 16;
			l2 = k2 * i3 + l2 * k3 >> 16;
			k2 = i4;
		}
		if (i2 != 0) {
			int j3 = Rasterizer.SINE[i2];
			int l3 = Rasterizer.COSINE[i2];
			int j4 = l2 * j3 + j2 * l3 >> 16;
			l2 = l2 * l3 - j2 * j3 >> 16;
			j2 = j4;
		}
		xCameraPos = l - j2;
		zCameraPos = i1 - k2;
		yCameraPos = k1 - l2;
		yCameraCurve = k;
		xCameraCurve = j1;
	}

	public void sendFrame126(String str, int i) {
		RSInterface.interfaceCache[i].disabledText = str;
		if (RSInterface.interfaceCache[i].parentId == tabInterfaceIDs[tabId])
			needDrawTabArea = true;
	}

	public final int SEND_TEXT = 126, PLAYER_UPDATING = 81;

	public void sendPacket185(int buttonID) {
		stream.createFrame(185);
		stream.writeShort(buttonID);
		RSInterface rsi = RSInterface.interfaceCache[buttonID];
		if (rsi.valueIndexArray != null && rsi.valueIndexArray[0][0] == 5) {
			int configID = rsi.valueIndexArray[0][1];
			variousSettings[configID] = 1 - variousSettings[configID];
			toggleInterface(configID);
			needDrawTabArea = true;
		}
	}

	public void setConfig(int id, int value) {
		anIntArray1045[id] = value;
		if (variousSettings[id] != value) {
			variousSettings[id] = value;
			needDrawTabArea = true;
			if (dialogID != -1) {
				inputTaken = true;
			}
		}
	}

	private Notes notes = new Notes(this);
	
	public int getConfig(int id) {
		return variousSettings[RSInterface.getConfigID(id)];
	}

	public boolean parsePacket() {
		if (socketStream == null)
			return false;
		try {
			int i = socketStream.available();
			if (i == 0)
				return false;
			if (packetId == -1) {
				socketStream.flushInputStream(packet.buffer, 1);
				packetId = packet.buffer[0] & 0xff;
				if (encryption != null)
					packetId = packetId - encryption.getNextKey() & 0xff;
				packetSize = SizeConstants.PACKET_SIZES[packetId];
				i--;
			}
			if (packetSize == -1)
				if (i > 0) {
					socketStream.flushInputStream(packet.buffer, 1);
					packetSize = packet.buffer[0] & 0xff;
					i--;
				} else {
					return false;
				}
			if (packetSize == -2)
				if (i > 1) {
					socketStream.flushInputStream(packet.buffer, 2);
					packet.currentOffset = 0;
					packetSize = packet.readUnsignedShort();
					i -= 2;
				} else {
					return false;
				}
			if (i < packetSize)
				return false;
			packet.currentOffset = 0;
			socketStream.flushInputStream(packet.buffer, packetSize);
			anInt1009 = 0;
			anInt843 = anInt842;
			anInt842 = anInt841;
			anInt841 = packetId;
			switch (packetId) {
			case 130:
				String note = packet.readString();
				notes.add(note);
				packetId = -1;
				return true;
			
			case 141:
				int childId = packet.readUnsignedShort();
				long longHex = packet.readLong();
				String colourHex = TextClass.nameForLong(longHex);
				RSInterface.interfaceCache[childId].colorFill = colourHex;
				packetId = -1;
				return true;
			
			case 140:
				childId = packet.readUnsignedShort();
				int attributes = packet.readUnsignedByte();
				int firstSprite = attributes >> 0;
				int secondSprite = attributes & 0;
				int[] newSprites = new int[] {firstSprite, secondSprite};
				changeSprite(childId, newSprites);
				packetId = -1;
				return true;
			
			case 88:
				quickPrayers = packet.readByte() > 0;
				packetId = -1;
				return true;
				
			case 89:
				poisoned = packet.readByte() > 0;
				packetId = -1;
				return true;
			
			case 123:
				sendConsoleMessage(packet.readString(), false);
				packetId = -1;
				return true;

			case 124:
				int skillID = packet.readUnsignedShort();
				int gainedXP = packet.readUnsignedShort();
				addXP(skillID, gainedXP);
				packetId = -1;
				return true;

			case 172:
				try {
					boolean active = packet.readByte() == 1;
					String name = packet.readString();
					String special = packet.readString();
					getFamiliar().setActive(active);
					getFamiliar().setFamiliar(name, special);
					System.out.println(getFamiliar().isActive() + ", "
							+ getFamiliar().getName() + ", "
							+ getFamiliar().getSpecialAttack());
				} catch (Exception e) {
					e.printStackTrace();
				}
				packetId = -1;
				return true;
				
			case PLAYER_UPDATING:
				updatePlayers(packetSize, packet);
				aBoolean1080 = false;
				packetId = -1;
				return true;

			case 176:
				daysSinceRecovChange = packet.readByteC();
				unreadMessages = packet.readShortA();
				membersInt = packet.readUnsignedByte();
				anInt1193 = packet.method440();
				daysSinceLastLogin = packet.readUnsignedShort();
				if (anInt1193 != 0 && openInterfaceID == -1) {
					signlink.dnslookup(TextClass.method586(anInt1193));
					clearTopInterfaces();
					char c = '\u028A';
					if (daysSinceRecovChange != 201 || membersInt == 1)
						c = '\u028F';
					reportAbuseInput = "";
					canMute = false;
					for (int k9 = 0; k9 < RSInterface.interfaceCache.length; k9++) {
						if (RSInterface.interfaceCache[k9] == null
								|| RSInterface.interfaceCache[k9].contentType != c)
							continue;
						openInterfaceID = RSInterface.interfaceCache[k9].parentId;

					}
				}
				packetId = -1;
				return true;

			case 64:
				bigRegionX = packet.readByteC();
				bigRegionY = packet.readByteS();
				for (int j = bigRegionX; j < bigRegionX + 8; j++) {
					for (int l9 = bigRegionY; l9 < bigRegionY + 8; l9++)
						if (groundEntity[plane][j][l9] != null) {
							groundEntity[plane][j][l9] = null;
							refreshGroundItems(j, l9);
						}
				}
				for (ObjectSpawnNode class30_sub1 = (ObjectSpawnNode) aClass19_1179
						.head(); class30_sub1 != null; class30_sub1 = (ObjectSpawnNode) aClass19_1179
						.next())
					if (class30_sub1.x >= bigRegionX
							&& class30_sub1.x < bigRegionX + 8
							&& class30_sub1.y >= bigRegionY
							&& class30_sub1.y < bigRegionY + 8
							&& class30_sub1.height == plane)
						class30_sub1.anInt1294 = 0;
				packetId = -1;
				return true;

			case 185:
				int k = packet.readLEShortA();
				RSInterface.interfaceCache[k].mediaType = 3;
				if (myPlayer.desc == -1)
					RSInterface.interfaceCache[k].mediaId = (myPlayer.clotheColour[0] << 25)
							+ (myPlayer.clotheColour[4] << 20)
							+ (myPlayer.equipment[0] << 15)
							+ (myPlayer.equipment[8] << 10)
							+ (myPlayer.equipment[11] << 5)
							+ myPlayer.equipment[1];
				else
					RSInterface.interfaceCache[k].mediaId = (int) (0x12345678L + myPlayer.desc);
				packetId = -1;
				return true;

			/* Clan chat packet */
			case 217 :
				try {
					name = packet.readString();
					message = packet.readString();
					clanname = packet.readString();
					rights = packet.readUnsignedShort();
					pushMessage(name, message, 12);
				} catch (Exception e) {
					e.printStackTrace();
				}
				packetId = -1;
				return true;

				/* Yell channel packet */
			case 222:
				try {
					name = packet.readString();
					message = packet.readString();
					message = TextInput.processText(message);
					rights = packet.readUnsignedShort();
					pushMessage(name, message, 13);
				} catch (Exception e) {
					e.printStackTrace();
				}
				packetId = -1;
				return true;

				/* Muted reason packet */
			case 223:
				try {
					mutedBy = packet.readString();
					muteReason = packet.readString();
				} catch (Exception e) {
					e.printStackTrace();
				}
				packetId = -1;
				return true;

			case 107:
				aBoolean1160 = false;
				for (int l = 0; l < 5; l++)
					aBooleanArray876[l] = false;
				packetId = -1;
				return true;

			case 72:
				int i1 = packet.readLEShort();
				RSInterface rsi = RSInterface.interfaceCache[i1];
				for (int k15 = 0; k15 < rsi.inventory.length; k15++) {
					rsi.inventory[k15] = -1;
					rsi.inventory[k15] = 0;
				}
				packetId = -1;
				return true;

			case 214:
				ignoreCount = packetSize / 8;
				for (int j1 = 0; j1 < ignoreCount; j1++)
					ignoreListNames[j1] = packet.readLong();
				packetId = -1;
				return true;

			case 166:
				aBoolean1160 = true;
				anInt1098 = packet.readUnsignedByte();
				anInt1099 = packet.readUnsignedByte();
				anInt1100 = packet.readUnsignedShort();
				anInt1101 = packet.readUnsignedByte();
				anInt1102 = packet.readUnsignedByte();
				if (anInt1102 >= 100) {
					xCameraPos = anInt1098 * 128 + 64;
					yCameraPos = anInt1099 * 128 + 64;
					zCameraPos = getHeight(plane, yCameraPos, xCameraPos)
							- anInt1100;
				}
				packetId = -1;
				return true;

			case 134:
				needDrawTabArea = true;
				int id = packet.readUnsignedByte();
				int exp = packet.readSingleInt();
				int level = packet.readUnsignedShort();
				currentExp[id] = exp;
				currentLevel[id] = level;
				maxLevel[id] = packet.readUnsignedShort();
				packetId = -1;
				return true;

			case 71:
				int interfaceChildId = packet.readUnsignedShort();
				int tabIndex = packet.readByteA();
				if (interfaceChildId == 65535)
					interfaceChildId = -1;
				tabInterfaceIDs[tabIndex] = interfaceChildId;
				needDrawTabArea = true;
				tabAreaAltered = true;
				packetId = -1;
				return true;

			case 74:
				int songId = packet.readLEShort();
				if (songId == 65535) {
					songId = -1;
				}
				if (songId != currentSong && musicEnabled && !lowMemory
						&& previousSong == 0) {
					nextSong = songId;
					songChanging = true;
					resourceProvider.loadMandatory(2, nextSong);
				}
				currentSong = songId;
				packetId = -1;
				return true;

			case 121:
				int j2 = packet.readLEShortA();
				int k10 = packet.readShortA();
				if (musicEnabled && !lowMemory) {
					nextSong = j2;
					songChanging = false;
					resourceProvider.loadMandatory(2, nextSong);
					previousSong = k10;
				}
				packetId = -1;
				return true;

			case 109:
				resetLogout();
				packetId = -1;
				return false;

			case 70:
				int offsetX = packet.readSignedShort();
				int offsetY = packet.readShort();
				childId = packet.readLEShort();
				rsi = RSInterface.interfaceCache[childId];
				if (rsi != null) {
					rsi.drawOffsetX = offsetX;
					rsi.drawOffsetY = offsetY;
				}
				packetId = -1;
				return true;

			case 73: //loads map region
			case 241: //construct map region 8*8
				int regionX = currentRegionX;
				int regionY = currentRegionY;
				if (packetId == 73) {
					regionX = mapX = packet.readShortA();
					regionY = mapY = packet.readUnsignedShort();
					aBoolean1159 = false;
				}
				if (packetId == 241) {
					regionY = packet.readShortA();
					packet.initBitAccess();
					for (int j16 = 0; j16 < 4; j16++) {
						for (int l20 = 0; l20 < 13; l20++) {
							for (int j23 = 0; j23 < 13; j23++) {
								int i26 = packet.readBits(1);
								if (i26 == 1)
									anIntArrayArrayArray1129[j16][l20][j23] = packet
											.readBits(26);
								else
									anIntArrayArrayArray1129[j16][l20][j23] = -1;
							}
						}
					}
					packet.finishBitAccess();
					regionX = packet.readUnsignedShort();
					aBoolean1159 = true;
				}
				if (currentRegionX == regionX && currentRegionY == regionY && loadingStage == 2) {//run
					packetId = -1;
					return true;
				}
				currentRegionX = regionX;
				currentRegionY = regionY;
				regionAbsBaseX = (currentRegionX - 6) * 8;
				regionAbsBaseY = (currentRegionY - 6) * 8;
				aBoolean1141 = (currentRegionX / 8 == 48 || currentRegionX / 8 == 49) && currentRegionY / 8 == 48;
				if (currentRegionX / 8 == 48 && currentRegionY / 8 == 148)
					aBoolean1141 = true;
				loadingStage = 1;
				aLong824 = System.currentTimeMillis();
				gameScreenIP.initDrawingArea();
				drawLoadingMessage(1);
				/*normalFont.drawText(0, "Loading - please wait.", 151, 257);
				normalFont.drawText(0xffffff, "Loading - please wait.", 150, 256);*/
				gameScreenIP.drawGraphics(clientSize == 0 ? 4 : 0,
						clientSize == 0 ? 4 : 0, super.graphics);
				if (packetId == 73) {
					int index = 0;
					for (int i21 = (currentRegionX - 6) / 8; i21 <= (currentRegionX + 6) / 8; i21++) {
						for (int k23 = (currentRegionY - 6) / 8; k23 <= (currentRegionY + 6) / 8; k23++)
							index++;
					}
					aByteArrayArray1183 = new byte[index][];
					aByteArrayArray1247 = new byte[index][];
					mapLocation = new int[index];
					floorMap = new int[index];
					objectMap = new int[index];
					index = 0;
					for (int x = (currentRegionX - 6) / 8; x <= (currentRegionX + 6) / 8; x++) {
						for (int y = (currentRegionY - 6) / 8; y <= (currentRegionY + 6) / 8; y++) {
							mapLocation[index] = (x << 8) + y;
							if (aBoolean1141
									&& (y == 49 || y == 149 || y == 147
											|| x == 50 || x == 49
											&& y == 47)) {
								floorMap[index] = -1;
								objectMap[index] = -1;
								index++;
							} else {
								int floors = floorMap[index] = resourceProvider
										.getClipping(0, y, x);
								if (floors != -1)
									resourceProvider.loadMandatory(3, floors);
								int objects = objectMap[index] = resourceProvider
										.getClipping(1, y, x);
								if (objects != -1)
									resourceProvider.loadMandatory(3, objects);
								/*for (int j = 0; j < objectMap.length; j++) {
									if (objectMap[j] > 0) {
										System.out.println("objectMap[" + j + "]: " + objectMap[j]);
									}
								}*/
								//System.out.println("floors=" + floors + "; objects=" + objects);
								index++;
							}
						}
					}
				}
				if (packetId == 241) {
					int l16 = 0;
					int ai[] = new int[676];
					for (int i24 = 0; i24 < 4; i24++) {
						for (int k26 = 0; k26 < 13; k26++) {
							for (int l28 = 0; l28 < 13; l28++) {
								int k30 = anIntArrayArrayArray1129[i24][k26][l28];
								if (k30 != -1) {
									int k31 = k30 >> 14 & 0x3ff;
									int i32 = k30 >> 3 & 0x7ff;
									int k32 = (k31 / 8 << 8) + i32 / 8;
									for (int j33 = 0; j33 < l16; j33++) {
										if (ai[j33] != k32)
											continue;
										k32 = -1;

									}
									if (k32 != -1)
										ai[l16++] = k32;
								}
							}
						}
					}
					aByteArrayArray1183 = new byte[l16][];
					aByteArrayArray1247 = new byte[l16][];
					mapLocation = new int[l16];
					floorMap = new int[l16];
					objectMap = new int[l16];
					for (int l26 = 0; l26 < l16; l26++) {
						int i29 = mapLocation[l26] = ai[l26];
						int l30 = i29 >> 8 & 0xff;
						int l31 = i29 & 0xff;
						int j32 = floorMap[l26] = resourceProvider.getClipping(0,
								l31, l30);
						if (j32 != -1)
							resourceProvider.loadMandatory(3, j32);
						int i33 = objectMap[l26] = resourceProvider.getClipping(1,
								l31, l30);
						if (i33 != -1)
							resourceProvider.loadMandatory(3, i33);
					}
				}
				int i17 = regionAbsBaseX - anInt1036;
				int j21 = regionAbsBaseY - anInt1037;
				anInt1036 = regionAbsBaseX;
				anInt1037 = regionAbsBaseY;
				for (int j24 = 0; j24 < 16384; j24++) {
					NPC npc = npcArray[j24];
					if (npc != null) {
						for (int j29 = 0; j29 < 10; j29++) {
							npc.smallX[j29] -= i17;
							npc.smallY[j29] -= j21;
						}
						npc.x -= i17 * 128;
						npc.y -= j21 * 128;
					}
				}
				for (int i27 = 0; i27 < maxPlayers; i27++) {
					Player player = playerArray[i27];
					if (player != null) {
						for (int i31 = 0; i31 < 10; i31++) {
							player.smallX[i31] -= i17;
							player.smallY[i31] -= j21;
						}
						player.x -= i17 * 128;
						player.y -= j21 * 128;
					}
				}
				aBoolean1080 = true;
				byte byte1 = 0;
				byte byte2 = 104;
				byte byte3 = 1;
				if (i17 < 0) {
					byte1 = 103;
					byte2 = -1;
					byte3 = -1;
				}
				byte byte4 = 0;
				byte byte5 = 104;
				byte byte6 = 1;
				if (j21 < 0) {
					byte4 = 103;
					byte5 = -1;
					byte6 = -1;
				}
				for (int k33 = byte1; k33 != byte2; k33 += byte3) {
					for (int l33 = byte4; l33 != byte5; l33 += byte6) {
						int i34 = k33 + i17;
						int j34 = l33 + j21;
						for (int k34 = 0; k34 < 4; k34++)
							if (i34 >= 0 && j34 >= 0 && i34 < 104 && j34 < 104)
								groundEntity[k34][k33][l33] = groundEntity[k34][i34][j34];
							else
								groundEntity[k34][k33][l33] = null;
					}
				}
				for (ObjectSpawnNode class30_sub1_1 = (ObjectSpawnNode) aClass19_1179
						.head(); class30_sub1_1 != null; class30_sub1_1 = (ObjectSpawnNode) aClass19_1179
						.next()) {
					class30_sub1_1.x -= i17;
					class30_sub1_1.y -= j21;
					if (class30_sub1_1.x < 0
							|| class30_sub1_1.y < 0
							|| class30_sub1_1.x >= 104
							|| class30_sub1_1.y >= 104)
						class30_sub1_1.remove();
				}
				if (destX != 0) {
					destX -= i17;
					destY -= j21;
				}
				aBoolean1160 = false;
				packetId = -1;
				return true;

			case 208:
				try {
					int interfaceId = packet.readShort();
					if (interfaceId >= 0)
						displayInterface(interfaceId);
					walkableInterface = interfaceId;
				} catch (Exception e) {
					e.printStackTrace();
				}
				packetId = -1;
				return true;

			case 99:
				minimapState = packet.readUnsignedByte();
				packetId = -1;
				return true;

			case 75:
				int npcId = packet.readLEShortA();
				int interfaceId = packet.readLEShortA();
				RSInterface.interfaceCache[interfaceId].mediaType = 2;
				RSInterface.interfaceCache[interfaceId].mediaId = npcId;
				packetId = -1;
				return true;

			case 114:
				systemUpdateTimer = packet.readLEShort() * 30;
				packetId = -1;
				return true;

			case 60:
				bigRegionY = packet.readUnsignedByte();
				bigRegionX = packet.readByteC();
				while (packet.currentOffset < packetSize) {
					int packetOpcode = packet.readUnsignedByte();
					updateEntity(packet, packetOpcode);
				}
				packetId = -1;
				return true;

			case 35:
				int l3 = packet.readUnsignedByte();
				int k11 = packet.readUnsignedByte();
				int j17 = packet.readUnsignedByte();
				int k21 = packet.readUnsignedByte();
				aBooleanArray876[l3] = true;
				anIntArray873[l3] = k11;
				anIntArray1203[l3] = j17;
				anIntArray928[l3] = k21;
				anIntArray1030[l3] = 0;
				packetId = -1;
				return true;

			case 174:
				int soundId = packet.readUnsignedShort();
				int volume = packet.readUnsignedByte();
				int delay = packet.readUnsignedShort();
				// if (aBoolean848 && !lowMemory && currentSound < 50) {
				anIntArray1207[currentSound] = soundId;
				anIntArray1241[currentSound] = volume;
				anIntArray1250[currentSound] = delay
						+ Sounds.sound[soundId];
				currentSound++;
				// }
				// sendMessage("Received: " + soundId + ", " + volume + ", " +
				// delay);
				packetId = -1;
				return true;

			case 104:
				int slot = packet.readByteC();
				int isTopSlot = packet.readByteA();
				String actionName = packet.readString();
				if (slot >= 1 && slot <= 5) {
					if (actionName.equalsIgnoreCase("null"))
						actionName = null;
					playerAction[slot - 1] = actionName;
					atPlayerArray[slot - 1] = isTopSlot == 0;
				}
				packetId = -1;
				return true;

			case 78:
				destX = 0;
				packetId = -1;
				return true;

			case 250:
				String request = packet.readString();
				String name = request.substring(0, request.indexOf(">"));
				long nameAsLong = TextClass.longForName(name);
				boolean ignored = false;
				for (int j27 = 0; j27 < ignoreCount; j27++) {
					if (ignoreListNames[j27] != nameAsLong)
						continue;
					ignored = true;
				}
				if (!ignored && anInt1251 == 0) {
					if (request.endsWith("<trade>")) {
						pushMessage(name, "wishes to trade with you.", 4);
					} else if (request.endsWith("<duel>")) {
						pushMessage(name, "wishes to duel with you.", 8);
					} else if (request.endsWith("<chal>")) { //TODO what is this for?
						String message = request.substring(request.indexOf(">") + 1, request.length() - 6);
						pushMessage(name, message, 8);
					} else if (name.startsWith("alert#")) {
						String[] args = name.split("#");
						if (args.length == 3) {
							alertHandler.alert = new Alert("Notification", args[1],
									args[2]);
						} else if (args.length == 4) {
							alertHandler.alert = new Alert(args[1], args[2],
									args[3]);
						}
						packetId = -1;
						return true;
					}
				}
				packetId = -1;
				return true;
				
			case 253:
				String s = packet.readString();
				pushMessage("", s, 0);
				packetId = -1;
				return true;

			case 1:
				for (int k4 = 0; k4 < playerArray.length; k4++)
					if (playerArray[k4] != null)
						playerArray[k4].animationId = -1;
				for (int j12 = 0; j12 < npcArray.length; j12++)
					if (npcArray[j12] != null)
						npcArray[j12].animationId = -1;
				packetId = -1;
				return true;

			case 50:
				long longName = packet.readLong();
				int world = packet.readUnsignedByte();
				String playerName = TextClass.fixName(TextClass.nameForLong(longName));
				for (int index = 0; index < friendsCount; index++) {
					if (longName != friendsListNames[index])
						continue;
					if (friendsNodeIDs[index] != world) {
						friendsNodeIDs[index] = world;
						needDrawTabArea = true;
						if (world >= 2) {
							pushMessage("", playerName + " has logged in.", 5);
						}
						if (world <= 1) {
							pushMessage("", playerName + " has logged out.", 5);
						}
					}
					playerName = null;

				}
				if (playerName != null && friendsCount < 200) {
					friendsListNames[friendsCount] = longName;
					friendsList[friendsCount] = playerName;
					friendsNodeIDs[friendsCount] = world;
					friendsCount++;
					needDrawTabArea = true;
				}
				for (boolean flag6 = false; !flag6;) {
					flag6 = true;
					for (int k29 = 0; k29 < friendsCount - 1; k29++)
						if (friendsNodeIDs[k29] != nodeID
								&& friendsNodeIDs[k29 + 1] == nodeID
								|| friendsNodeIDs[k29] == 0
								&& friendsNodeIDs[k29 + 1] != 0) {
							int j31 = friendsNodeIDs[k29];
							friendsNodeIDs[k29] = friendsNodeIDs[k29 + 1];
							friendsNodeIDs[k29 + 1] = j31;
							String s10 = friendsList[k29];
							friendsList[k29] = friendsList[k29 + 1];
							friendsList[k29 + 1] = s10;
							long l32 = friendsListNames[k29];
							friendsListNames[k29] = friendsListNames[k29 + 1];
							friendsListNames[k29 + 1] = l32;
							needDrawTabArea = true;
							flag6 = false;
						}
				}
				packetId = -1;
				return true;

			case 110:
				if (tabId == 12)
					needDrawTabArea = true;
				runEnergy = packet.readUnsignedByte();
				packetId = -1;
				return true;
				
			case 113:
				running = packet.readUnsignedByte() > 0;
				packetId = -1;
				return true;

			case 254:
				headIconType = packet.readUnsignedByte();
				if (headIconType == 1)
					anInt1222 = packet.readUnsignedShort();
				if (headIconType >= 2 && headIconType <= 6) {
					if (headIconType == 2) {
						anInt937 = 64;
						anInt938 = 64;
					}
					if (headIconType == 3) {
						anInt937 = 0;
						anInt938 = 64;
					}
					if (headIconType == 4) {
						anInt937 = 128;
						anInt938 = 64;
					}
					if (headIconType == 5) {
						anInt937 = 64;
						anInt938 = 0;
					}
					if (headIconType == 6) {
						anInt937 = 64;
						anInt938 = 128;
					}
					headIconType = 2;
					anInt934 = packet.readUnsignedShort();
					anInt935 = packet.readUnsignedShort();
					anInt936 = packet.readUnsignedByte();
				}
				if (headIconType == 10)
					otherPlayerIndex = packet.readUnsignedShort();
				packetId = -1;
				return true;

			case 248:
				interfaceId = packet.readShortA();
				int overlayInterfaceId = packet.readUnsignedShort();
				if (backDialogID != -1) {
					backDialogID = -1;
					inputTaken = true;
				}
				if (inputDialogState != 0) {
					inputDialogState = 0;
					inputTaken = true;
				}
				openInterfaceID = interfaceId;
				invOverlayInterfaceID = overlayInterfaceId;
				needDrawTabArea = true;
				tabAreaAltered = true;
				aBoolean1149 = false;
				packetId = -1;
				return true;

			case 79:
				interfaceId = packet.readLEShort();
				int scrollPosition = packet.readShortA();
				RSInterface class9_3 = RSInterface.interfaceCache[interfaceId];
				if (class9_3 != null && class9_3.type == 0) {
					if (scrollPosition < 0)
						scrollPosition = 0;
					if (scrollPosition > class9_3.scrollMax - class9_3.height)
						scrollPosition = class9_3.scrollMax - class9_3.height;
					class9_3.scrollPosition = scrollPosition;
				}
				packetId = -1;
				return true;

			case 68:
				for (int k5 = 0; k5 < variousSettings.length; k5++) {
					if (variousSettings[k5] != anIntArray1045[k5]) {
						variousSettings[k5] = anIntArray1045[k5];
						toggleInterface(k5);
						needDrawTabArea = true;
					}
				}
				packetId = -1;
				return true;

			case 196:
				longName = packet.readLong();
				id = packet.readInt();
				int rankId = packet.readUnsignedByte();
				boolean successfullySendMessage = false;
				if (rankId <= 1) {
					for (int index = 0; index < ignoreCount; index++) {
						if (ignoreListNames[index] == longName) {
							successfullySendMessage = false;
							break;
						}
					}
				}
				if (successfullySendMessage && anInt1251 == 0)
					try {
						String message = TextInput.method525(packetSize - 13, packet);
						if (rankId != 0) {
							pushMessage(getPrefix(rankId) + TextUtils.fixName(TextUtils.nameForLong(longName)), message, 7);
						} else {
							pushMessage(TextUtils.fixName(TextUtils.nameForLong(longName)), message, 3);
						}
					} catch(Exception exception1) {
						signlink.reporterror("cde1");
					}
				packetId = -1;
				return true;

			case 85:
				bigRegionY = packet.readByteC();
				bigRegionX = packet.readByteC();
				packetId = -1;
				return true;

			case 24:
				flashingSidebar = packet.readByteS();
				if (flashingSidebar == tabId) {
					flashingSidebar = -1;
					needDrawTabArea = true;
				}
				packetId = -1;
				return true;

			case 246:
				interfaceId = packet.readLEShort();
				int zoom = packet.readUnsignedShort();
				int itemId = packet.readUnsignedShort();
				if (itemId == 65535) {
					RSInterface.interfaceCache[interfaceId].mediaType = 0;
					packetId = -1;
					return true;
				} else {
					ItemDef itemDef = ItemDef.forId(itemId);
					RSInterface.interfaceCache[interfaceId].mediaType = 4;
					RSInterface.interfaceCache[interfaceId].mediaId = itemId;
					RSInterface.interfaceCache[interfaceId].modelRotation1 = itemDef.modelRotation1;
					RSInterface.interfaceCache[interfaceId].modelRotation2 = itemDef.modelRotation2;
					RSInterface.interfaceCache[interfaceId].modelZoom = (itemDef.modelZoom * 100)
							/ zoom;
					packetId = -1;
					return true;
				}

			case 171:
				boolean hide = packet.readUnsignedByte() == 1;
				childId = packet.readUnsignedShort();
				RSInterface.interfaceCache[childId].hidden = hide;
				packetId = -1;
				return true;

			case 142:
				interfaceId = packet.readLEShort();
				displayInterface(interfaceId);
				if (backDialogID != -1) {
					backDialogID = -1;
					inputTaken = true;
				}
				if (inputDialogState != 0) {
					inputDialogState = 0;
					inputTaken = true;
				}
				invOverlayInterfaceID = interfaceId;
				needDrawTabArea = true;
				tabAreaAltered = true;
				openInterfaceID = -1;
				aBoolean1149 = false;
				packetId = -1;
				return true;

			case 126:
				String text = packet.readString();
				int frame = packet.readShortA();
				try {
					sendFrame126(text, frame);
					needDrawTabArea = true;
					tabAreaAltered = true;
				} catch (Exception e) {
					//System.out.println("Invalid text frame: " + frame);
				}
				packetId = -1;
				return true;

			case 206:
				publicChatMode = packet.readUnsignedByte();
				publicChatMode = packet.readUnsignedByte();
				tradeMode = packet.readUnsignedByte();
				aBoolean1233 = true;
				inputTaken = true;
				packetId = -1;
				return true;

			case 240:
				if (tabId == 12)
					needDrawTabArea = true;
				weight = packet.readSignedShort();
				packetId = -1;
				return true;

			case 8:
				childId = packet.readLEShortA();
				int modelId = packet.readUnsignedShort();
				RSInterface.interfaceCache[childId].mediaType = 1;
				RSInterface.interfaceCache[childId].mediaId = modelId;
				packetId = -1;
				return true;

			case 122:
				interfaceId = packet.readLEShortA();
				int colour = packet.readLEShortA();
				int i19 = colour >> 10 & 0x1f;
				int i22 = colour >> 5 & 0x1f;
				int l24 = colour & 0x1f;
				RSInterface.interfaceCache[interfaceId].textColor = (i19 << 19)
						+ (i22 << 11) + (l24 << 3);
				packetId = -1;
				return true;

			case 53:
				needDrawTabArea = true;
				id = packet.readUnsignedShort();
				rsi = RSInterface.interfaceCache[id];
				int total = packet.readUnsignedShort();
				boolean shopContainer = id == 1203;
				if (shopContainer) {
					int spriteId = packet.readUnsignedShort();
					rsi.shopCurrency = SpriteLoader.sprites[spriteId];
				}
				for(int index = 0; index < total; index++) {
					long amount = 1;
					boolean newLimit = false;
					if (newLimit) {
						amount = packet.getSmartLong();
					} else {
						amount = packet.readUnsignedByte();
					}
					if (amount == 255) {
						amount = packet.method440();
					}
					rsi.inventory[index] = packet.readLEShortA();
					rsi.inventoryAmount[index] = (int) amount;
					if (shopContainer) { //TODO better way to do this..maybe custom packet but meh...
						rsi.shopPrices[index] = packet.readInt();
					}
				}
				for(int index = total; index < rsi.inventory.length; index++) {
					rsi.inventory[index] = 0;
					rsi.inventoryAmount[index] = 0;
				}
				for(int index = total; index < rsi.shopPrices.length; index++) {
					rsi.shopPrices[index] = 0;
				}
				packetId = -1;
				return true;

			case 230:
				zoom = packet.readShortA();
				childId = packet.readUnsignedShort();
				int firstRotation = packet.readUnsignedShort();
				int secondRotation = packet.readLEShortA();
				RSInterface.interfaceCache[childId].modelRotation1 = firstRotation;
				RSInterface.interfaceCache[childId].modelRotation2 = secondRotation;
				RSInterface.interfaceCache[childId].modelZoom = zoom;
				packetId = -1;
				return true;

			case 221:
				friendsListServerStatus = packet.readUnsignedByte();
				needDrawTabArea = true;
				packetId = -1;
				return true;

			case 177:
				aBoolean1160 = true;
				cameraPositionX = packet.readUnsignedByte();
				cameraPositionY = packet.readUnsignedByte();
				cameraPositionZ = packet.readUnsignedShort();
				cameraMovementSpeed = packet.readUnsignedByte();
				cameraAngle = packet.readUnsignedByte();
				if (cameraAngle >= 100) {
					int k7 = cameraPositionX * 128 + 64;
					int k14 = cameraPositionY * 128 + 64;
					int i20 = getHeight(plane, k14, k7) - cameraPositionZ;
					int l22 = k7 - xCameraPos;
					int k25 = i20 - zCameraPos;
					int j28 = k14 - yCameraPos;
					int i30 = (int) Math.sqrt(l22 * l22 + j28 * j28);
					yCameraCurve = (int) (Math.atan2(k25, i30) * 325.94900000000001D) & 0x7ff;
					xCameraCurve = (int) (Math.atan2(l22, j28) * -325.94900000000001D) & 0x7ff;
					if (yCameraCurve < 128)
						yCameraCurve = 128;
					if (yCameraCurve > 383)
						yCameraCurve = 383;
				}
				packetId = -1;
				return true;

			case 249:
				memberStatus = packet.readByteA();
				playerId = packet.readLEShortA();
				packetId = -1;
				return true;

			case 65:
				updateNpcs(packet, packetSize);
				packetId = -1;
				return true;

			case 27:
				showInput = false;
				inputDialogState = 1;
				amountOrNameInput = "";
				inputTaken = true;
				packetId = -1;
				return true;

			case 187:
				showInput = false;
				inputDialogState = 2;
				amountOrNameInput = "";
				inputTaken = true;
				packetId = -1;
				return true;

			case 97:
				interfaceId = packet.readUnsignedShort();
				displayInterface(interfaceId);
				if (invOverlayInterfaceID != -1) {
					invOverlayInterfaceID = -1;
					needDrawTabArea = true;
					tabAreaAltered = true;
				}
				if (backDialogID != -1) {
					backDialogID = -1;
					inputTaken = true;
				}
				if (inputDialogState != 0) {
					inputDialogState = 0;
					inputTaken = true;
				}
				openInterfaceID = interfaceId;
				aBoolean1149 = false;
				packetId = -1;
				return true;

			case 218:
				int i8 = packet.method438();
				dialogID = i8;
				inputTaken = true;
				packetId = -1;
				return true;

			case 87:
				childId = packet.readLEShort();
				int state = packet.readSingleInt();
				anIntArray1045[childId] = state;
				if (variousSettings[childId] != state) {
					variousSettings[childId] = state;
					toggleInterface(childId);
					needDrawTabArea = true;
					if (dialogID != -1)
						inputTaken = true;
				}
				packetId = -1;
				return true;

			case 36:
				childId = packet.readLEShort();
				byte configState = packet.readByte();
				anIntArray1045[childId] = configState;
				if (variousSettings[childId] != configState) {
					variousSettings[childId] = configState;
					toggleInterface(childId);
					needDrawTabArea = true;
					if (dialogID != -1)
						inputTaken = true;
				}
				packetId = -1;
				return true;

			case 61:
				screenMultiIconId = packet.readUnsignedByte();
				packetId = -1;
				return true;

			case 200:
				childId = packet.readUnsignedShort();
				int animationId = packet.readSignedShort();
				RSInterface class9_4 = RSInterface.interfaceCache[childId];
				class9_4.animationId = animationId;
				class9_4.modelZoom = 2000;
				if (animationId == -1) {
					class9_4.anInt246 = 0;
					class9_4.anInt208 = 0;
				}
				packetId = -1;
				return true;

			case 219:
				if (invOverlayInterfaceID != -1) {
					invOverlayInterfaceID = -1;
					needDrawTabArea = true;
					tabAreaAltered = true;
				}
				if (backDialogID != -1) {
					backDialogID = -1;
					inputTaken = true;
				}
				if (inputDialogState != 0) {
					inputDialogState = 0;
					inputTaken = true;
				}
				openInterfaceID = -1;
				aBoolean1149 = false;
				packetId = -1;
				return true;

			case 34:
				needDrawTabArea = true;
				id = packet.readUnsignedShort();
				rsi = RSInterface.interfaceCache[id];
				while(packet.currentOffset < packetSize) {
					int inventorySlot = packet.getSmartB();
					int item_id = packet.readUnsignedShort();
					long amount = 1;
					boolean newLimit = false; //new limit means amount can go over 255
					if (newLimit) {
						amount = packet.getSmartLong();
					} else {
						amount = packet.readUnsignedByte();
					}
					if (amount == 255) {
						amount = packet.readInt();
					}
					if(inventorySlot >= 0 && inventorySlot < rsi.inventory.length) {
						rsi.inventory[inventorySlot] = item_id;
						rsi.inventoryAmount[inventorySlot] = (int) amount;
					}
				}
				packetId = -1;
				return true;

			case 4:
			case 44:
			case 84:
			case 101:
			case 105:
			case 117:
			case 147:
			case 151:
			case 156:
			case 160:
			case 215:
				updateEntity(packet, packetId);
				packetId = -1;
				return true;

			case 106:
				tabId = packet.readByteC();
				needDrawTabArea = true;
				tabAreaAltered = true;
				packetId = -1;
				return true;

			case 164:
				interfaceId = packet.readLEShort();
				displayInterface(interfaceId);
				if (invOverlayInterfaceID != -1) {
					invOverlayInterfaceID = -1;
					needDrawTabArea = true;
					tabAreaAltered = true;
				}
				backDialogID = interfaceId;
				inputTaken = true;
				openInterfaceID = -1;
				aBoolean1149 = false;
				packetId = -1;
				return true;

			}
			signlink.reporterror("T1 - " + packetId + "," + packetSize + " - "
					+ anInt842 + "," + anInt843);
			// resetLogout();
		} catch (IOException _ex) {
			dropClient();
		} catch (Exception exception) {
			String error = "Incoming packet - [packetId, exceptedSize, receivedsize] : [" + packetId + ", " + SizeConstants.PACKET_SIZES[packetId] + ", " + packetSize + "]";
			String s2 = "T2 - " + packetId + "," + anInt842 + "," + anInt843
					+ " - " + packetSize + "," + (regionAbsBaseX + myPlayer.smallX[0])
					+ "," + (regionAbsBaseY + myPlayer.smallY[0]) + " - ";
			for (int j15 = 0; j15 < packetSize && j15 < 50; j15++)
				s2 = s2 + packet.buffer[j15] + ",";
			signlink.reporterror(error);
			exception.printStackTrace();
			// resetLogout();
		}
		packetId = -1;
		return true;
	}

	public int cameraZoom = 3;

	public void displayRegionLighting() {
		if (regionLighting) {
			if (playerY <= 3500) {
				// DrawingArea474.drawAlphaFilledPixels(0, 0, clientWidth,
				// clientHeight, 0, 3487 - playerY);
			}
		}
	}

	public void method146() {
		anInt1265++;
		method47(true);
		method26(true);
		method47(false);
		method26(false);
		method55();
		method104();
		if (!aBoolean1160) {
			int i = anInt1184;
			if (anInt984 / 256 > i)
				i = anInt984 / 256;
			if (aBooleanArray876[4] && anIntArray1203[4] + 128 > i)
				i = anIntArray1203[4] + 128;
			int k = viewRotation + viewRotationOffset & 0x7ff;
			setCameraPos(600 + i * cameraZoom, i, anInt1014,
					getHeight(plane, myPlayer.y, myPlayer.x) - 50, k,
					anInt1015);
		}
		int j;
		if (!aBoolean1160)
			j = method120();
		else
			j = method121();
		int l = xCameraPos;
		int i1 = zCameraPos;
		int j1 = yCameraPos;
		int k1 = yCameraCurve;
		int l1 = xCameraCurve;
		for (int i2 = 0; i2 < 5; i2++)
			if (aBooleanArray876[i2]) {
				int j2 = (int) ((Math.random()
						* (double) (anIntArray873[i2] * 2 + 1) - (double) anIntArray873[i2]) + Math
						.sin((double) anIntArray1030[i2]
								* ((double) anIntArray928[i2] / 100D))
						* (double) anIntArray1203[i2]);
				if (i2 == 0)
					xCameraPos += j2;
				if (i2 == 1)
					zCameraPos += j2;
				if (i2 == 2)
					yCameraPos += j2;
				if (i2 == 3)
					xCameraCurve = xCameraCurve + j2 & 0x7ff;
				if (i2 == 4) {
					yCameraCurve += j2;
					if (yCameraCurve < 128)
						yCameraCurve = 128;
					if (yCameraCurve > 383)
						yCameraCurve = 383;
				}
			}
		int k2 = Rasterizer.textureGetCount;
		Model.aBoolean1684 = true;
		Model.anInt1687 = 0;
		Model.anInt1685 = super.mouseX - 4;
		Model.anInt1686 = super.mouseY - 4;
		DrawingArea.method336(clientHeight, 0, 0, 0xC8C0A8, clientWidth);
		landscapeScene.method313(xCameraPos, yCameraPos, xCameraCurve,
				zCameraPos, j, yCameraCurve);
		DrawingArea474.drawPixels(0, 0, clientWidth, clientHeight, 0xC8C0A8);
		landscapeScene.clearObj5Cache();
		updateEntities();
		drawHintIcon();
		method37(k2);
		displayRegionLighting();
		drawUnfixedGame();
		draw3dScreen();
		drawConsole();
		drawConsoleArea();
		if (showXP) {
			displayXPCounter();
		}
		if (showBonus) {
			int x = clientSize == 0 ? clientWidth - 425 : clientWidth - 405;
			drawInterface(x, 1, 0, RSInterface.interfaceCache[10000]);
		}
		gameScreenIP.drawGraphics(clientSize == 0 ? 4 : 0, clientSize == 0 ? 4 : 0,
				super.graphics);
		xCameraPos = l;
		zCameraPos = i1;
		yCameraPos = j1;
		yCameraCurve = k1;
		xCameraCurve = l1;
	}

	public void clearTopInterfaces() {
		stream.createFrame(130);
		if (invOverlayInterfaceID != -1) {
			invOverlayInterfaceID = -1;
			needDrawTabArea = true;
			aBoolean1149 = false;
			tabAreaAltered = true;
		}
		if (backDialogID != -1) {
			backDialogID = -1;
			inputTaken = true;
			aBoolean1149 = false;
		}
		openInterfaceID = -1;
		fullscreenInterfaceID = -1;
	}

	public Create create;
	public Create getCreate() {
		if (create != null) {
			return create;
		}
		return new Create(this);
	}

	public Recover recover;
	public Recover getRecovery() {
		if (recover != null) {
			return recover;
		}
		return new Recover(this);
	}

	public Client() {
		progress = 0.0F;
		create = new Create(this);
		create.setDefaults();
		recover = new Recover(this);
		recover.setDefaults();
		CustomUserInput.input = "";
		familiarHandler = new FamiliarHandler();
		choosingLeftClick = false;
		leftClick = -1;
		alertHandler = new AlertHandler(this);
		consoleInput = "";
		consoleOpen = false;
		consoleMessages = new String[17];
		world = 1;
		percentLoaded = 0.0F;
		fullscreenInterfaceID = -1;
		chatRights = new int[500];
		chatTypeView = 0;
		clanChatMode = 0;
		cButtonHPos = -1;
		cButtonHCPos = -1;
		cButtonCPos = 0;
		distanceValues = new int[104][104];
		friendsNodeIDs = new int[200];
		groundEntity = new Deque[4][104][104];
		aBoolean831 = false;
		aStream_834 = new JagexBuffer(new byte[5000]);
		npcArray = new NPC[16384];
		npcIndices = new int[16384];
		anIntArray840 = new int[1000];
		aStream_847 = JagexBuffer.create();
		aBoolean848 = true;
		openInterfaceID = -1;
		currentExp = new int[SkillConstants.skillsCount];
		aBoolean872 = false;
		anIntArray873 = new int[5];
		anInt874 = -1;
		aBooleanArray876 = new boolean[5];
		drawFlames = false;
		reportAbuseInput = "";
		playerId = -1;
		menuOpen = false;
		contextWidth = 0;
		contextHeight = 0;
		inputString = "";
		maxPlayers = 2048;
		myPlayerIndex = 2047;
		playerArray = new Player[maxPlayers];
		playerIndices = new int[maxPlayers];
		anIntArray894 = new int[maxPlayers];
		aStreamArray895s = new JagexBuffer[maxPlayers];
		anInt897 = 1;
		wayPoints = new int[104][104];
		anInt902 = 0x766654;
		aByteArray912 = new byte[16384];
		currentLevel = new int[SkillConstants.skillsCount];
		ignoreListNames = new long[100];
		loadingError = false;
		anInt927 = 0x332d25;
		anIntArray928 = new int[5];
		anIntArrayArray929 = new int[104][104];
		chatTypes = new int[500];
		chatNames = new String[500];
		chatMessages = new String[500];
		quickChatMessage = new boolean[500];
		sideIcons = new Sprite[14];
		scrollPart = new Sprite[12];
		scrollBar = new Sprite[6];
		orbs = new Sprite[20];
		
		hitBar = new Sprite[2];
		titleButton = new Sprite[15];
		worldSelect = new Sprite[9];
		optionSelect = new Sprite[4];
		border = new Sprite[19];
		aBoolean954 = true;
		friendsListNames = new long[200];
		currentSong = -1;
		drawingFlames = false;
		spriteDrawX = -1;
		spriteDrawY = -1;
		anIntArray968 = new int[33];
		anIntArray969 = new int[256];
		mainCacheFile = new Decompressor[6];
		variousSettings = new int[2000];
		aBoolean972 = false;
		anInt975 = 50;
		anIntArray976 = new int[anInt975];
		anIntArray977 = new int[anInt975];
		anIntArray978 = new int[anInt975];
		anIntArray979 = new int[anInt975];
		anIntArray980 = new int[anInt975];
		anIntArray981 = new int[anInt975];
		anIntArray982 = new int[anInt975];
		aStringArray983 = new String[anInt975];
		anInt985 = -1;
		hitMark = new Sprite[20];
		hitStyle = new Sprite[5];
		hitShadow = new Sprite[6];
		anIntArray990 = new int[5];
		aBoolean994 = false;
		anInt1002 = 0x23201b;
		amountOrNameInput = "";
		aClass19_1013 = new Deque();
		aBoolean1017 = false;
		walkableInterface = -1;
		anIntArray1030 = new int[5];
		aBoolean1031 = false;
		mapFunctions = new Sprite[100];
		dialogID = -1;
		maxLevel = new int[SkillConstants.skillsCount];
		anIntArray1045 = new int[2000];
		aBoolean1047 = true;
		anIntArray1052 = new int[152];
		flashingSidebar = -1;
		aClass19_1056 = new Deque();
		anIntArray1057 = new int[33];
		rsi = new RSInterface();
		mapScenes = new Sprite[200];
		barFillColor = 0x4d4233;
		anIntArray1065 = new int[7];
		anIntArray1072 = new int[1000];
		anIntArray1073 = new int[1000];
		aBoolean1080 = false;
		friendsList = new String[200];
		packet = JagexBuffer.create();
		expectedCRCs = new int[9];
		menuActionCmd2 = new int[500];
		menuActionCmd3 = new int[500];
		menuActionID = new int[500];
		menuActionCmd1 = new int[500];
		headIcons = new Sprite[20];
		background = new RSImage[5];
		bar = new RSImage[2];
		skullIcons = new Sprite[20];
		headIconsHint = new Sprite[20];
		tabAreaAltered = false;
		promptMessage = "";
		playerAction = new String[5];
		atPlayerArray = new boolean[5];
		anIntArrayArrayArray1129 = new int[4][13][13];
		anInt1132 = 2;
		aClass30_Sub2_Sub1_Sub1Array1140 = new Sprite[1000];
		aBoolean1141 = false;
		aBoolean1149 = false;
		crosses = new Sprite[8];
		musicEnabled = true;
		needDrawTabArea = false;
		loggedIn = false;
		canMute = false;
		aBoolean1159 = false;
		aBoolean1160 = false;
		anInt1171 = 1;
		myUsername = myUsername != null ? myUsername : "";
		myPassword = myPassword != null ? myPassword : "";
		genericLoadingError = false;
		reportAbuseInterfaceID = -1;
		aClass19_1179 = new Deque();
		anInt1184 = 128;
		invOverlayInterfaceID = -1;
		stream = JagexBuffer.create();
		menuActionName = new String[500];
		anIntArray1203 = new int[5];
		anIntArray1207 = new int[50];
		anInt1210 = 2;
		anInt1211 = 78;
		promptInput = "";
		modIcons = new Sprite[12];
		tabId = 4;
		inputTaken = false;
		songChanging = true;
		anIntArray1229 = new int[152];
		clippingPlanes = new LandscapeClippingPlane[4];
		aBoolean1233 = false;
		anIntArray1240 = new int[100];
		anIntArray1241 = new int[50];
		aBoolean1242 = false;
		anIntArray1250 = new int[50];
		rsAlreadyLoaded = false;
		welcomeScreenRaised = false;
		showInput = false;
		loginMessage1 = "";
		loginMessage2 = "";
		loginScreenState = LOGIN;
		backDialogID = -1;
		anInt1279 = 2;
		walkingQueueX = new int[4000];
		walkingQueueY = new int[4000];
		anInt1289 = -1;
	}

	public Sprite[] hitBar;
	public Sprite full;
	public int playerX;
	public int playerY;
	public int world;
	public Sprite[] orbs;
	public Sprite[] cacheSprite;
	public float percentLoaded;
	public int yellMode;
	public Sprite quickChatSprite;
	public int rights;
	/**
	 * Add new sprite ints here
	 */
	public Sprite[] optionBG;
	public Sprite backgroundFix;
	public Sprite loadingBarFull;
	public Sprite loadingBarEmpty;

	public String name;
	public String message;
	public String clanname;
	public final int[] chatRights;
	public int chatTypeView;
	public int clanChatMode;
	public int duelMode;
	public RSImageProducer leftFrame;
	public RSImageProducer topFrame;
	public RSImageProducer rightFrame;
	public int ignoreCount;
	public long aLong824;
	public int[][] distanceValues;
	public int[] friendsNodeIDs;
	public Deque[][][] groundEntity;
	public int[] anIntArray828;
	public int[] anIntArray829;
	public volatile boolean aBoolean831;
	public Socket aSocket832;
	public int loginScreenState;
	public JagexBuffer aStream_834;
	public NPC[] npcArray;
	public int npcCount;
	public int[] npcIndices;
	public int anInt839;
	public int[] anIntArray840;
	public int anInt841;
	public int anInt842;
	public int anInt843;
	public String aString844;
	public JagexBuffer aStream_847;
	public boolean aBoolean848;
	public static int anInt849;
	public int[] anIntArray850;
	public int[] anIntArray851;
	public int[] anIntArray852;
	public int[] anIntArray853;
	public static int anInt854;
	public int headIconType;
	public static int openInterfaceID;
	public int xCameraPos;
	public int zCameraPos;
	public int yCameraPos;
	public int yCameraCurve;
	public int xCameraCurve;
	public int myRank;
	public final int[] currentExp;
	public Sprite[] redStones;
	public Sprite mapFlag;
	public Sprite mapMarker;
	public boolean aBoolean872;
	public final int[] anIntArray873;
	public int anInt874;
	public final boolean[] aBooleanArray876;
	public int weight;
	public MouseDetection mouseDetection;
	public volatile boolean drawFlames;
	public String reportAbuseInput;
	public int playerId;
	public boolean menuOpen;
	public int anInt886;
	static String inputString;
	public final int maxPlayers;
	public final int myPlayerIndex;
	public Player[] playerArray;
	public int playerCount;
	public int[] playerIndices;
	public int anInt893;
	public int[] anIntArray894;
	public JagexBuffer[] aStreamArray895s;
	public int viewRotationOffset;
	public int anInt897;
	public int friendsCount;
	public int friendsListServerStatus;
	public int[][] wayPoints;
	public final int anInt902;
	public byte[] aByteArray912;
	public int anInt913;
	public int crossX;
	public int crossY;
	public int crossIndex;
	public int crossType;
	public int plane;
	final int[] currentLevel;
	public static int anInt924;
	public final long[] ignoreListNames;
	public boolean loadingError;
	public final int anInt927;
	public final int[] anIntArray928;
	public int[][] anIntArrayArray929;
	public Sprite aClass30_Sub2_Sub1_Sub1_931;
	public Sprite aClass30_Sub2_Sub1_Sub1_932;
	public int otherPlayerIndex;
	public int anInt934;
	public int anInt935;
	public int anInt936;
	public int anInt937;
	public int anInt938;
	public static int anInt940;
	public final int[] chatTypes;
	public final String[] chatNames;
	public final String[] chatMessages;
	public final boolean[] quickChatMessage;
	public int anInt945;
	public LandscapeScene landscapeScene;
	public Sprite[] sideIcons;
	public int menuScreenArea;
	public int menuOffsetX;
	public int menuOffsetY;
	public int menuWidth;
	public int menuHeight;
	public long aLong953;
	public boolean aBoolean954;
	public long[] friendsListNames;
	public String[] clanList = new String[100];
	public int currentSong;
	public static int nodeID = 10;
	static int portOff;
	static boolean clientData;
	public static boolean isMembers = true;
	public static boolean lowMemory;
	public volatile boolean drawingFlames;
	public int spriteDrawX;
	public int spriteDrawY;
	public final int[] anIntArray965 = { 0xffff00, 0xff0000, 65280, 65535,
			0xff00ff, 0xffffff };
	//public Sprite title;
	//public Sprite[] titleText;
	public Sprite titleBox;
	//public Sprite[] socialBox;
	public Sprite exitButton;
	public Sprite logo;
	public Sprite[] border;
	public Sprite[] worldSelect;
	public Sprite fbHover;
	public Sprite[] optionSelect;
	public Sprite[] titleButton;
	public Sprite[] scrollBar;
	public Sprite[] scrollPart;
	public final int[] anIntArray968;
	public final int[] anIntArray969;
	final Decompressor[] mainCacheFile;
	public int variousSettings[];
	public boolean aBoolean972;
	public final int anInt975;
	public final int[] anIntArray976;
	public final int[] anIntArray977;
	public final int[] anIntArray978;
	public final int[] anIntArray979;
	public final int[] anIntArray980;
	public final int[] anIntArray981;
	public final int[] anIntArray982;
	public final String[] aStringArray983;
	public int anInt984;
	public int anInt985;
	public static int anInt986;
	public Sprite[] hitMark;
	public Sprite[] hitStyle;
	public Sprite[] hitShadow;
	public int anInt988;
	public int anInt989;
	public final int[] anIntArray990;
	public static boolean aBoolean993;
	public final boolean aBoolean994;
	public int cameraPositionX;
	public int cameraPositionY;
	public int cameraPositionZ;
	public int cameraMovementSpeed;
	public int cameraAngle;
	public ISAACRandomGen encryption;
	public Sprite mapEdge;
	public Sprite multiOverlay;
	public final int anInt1002;
	static final int[][] VALID_CLOTHE_COLOUR = {
			{ 6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983,
					54193 },
			{ 8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153,
					56621, 4783, 1341, 16578, 35003, 25239 },
			{ 25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094,
					10153, 56621, 4783, 1341, 16578, 35003 },
			{ 4626, 11146, 6439, 12, 4758, 10270 },
			{ 4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574 } };
	public String amountOrNameInput;
	public static int anInt1005;
	public int daysSinceLastLogin;
	public int packetSize;
	public int packetId;
	public int anInt1009;
	public int anInt1010;
	public int anInt1011;
	public Deque aClass19_1013;
	public int anInt1014;
	public int anInt1015;
	public int anInt1016;
	public boolean aBoolean1017;
	public int walkableInterface;
	public static final int[] anIntArray1019;
	public int minimapState;
	public int anInt1022;
	public int loadingStage;
	public Sprite scrollBar1;
	public Sprite scrollBar2;
	public int anInt1026;
	public IndexedImage backBase1;
	public IndexedImage backBase2;
	public IndexedImage backHmid1;
	public final int[] anIntArray1030;
	public boolean aBoolean1031;
	public Sprite[] mapFunctions;
	public int regionAbsBaseX;
	public int regionAbsBaseY;
	public int anInt1036;
	public int anInt1037;
	public int loginFailures;
	public int anInt1039;
	public int anInt1040;
	public int anInt1041;
	public int dialogID;
	final int[] maxLevel;
	public final int[] anIntArray1045;
	public int memberStatus;
	public boolean aBoolean1047;
	public int anInt1048;
	public String aString1049;
	public static int anInt1051;
	public final int[] anIntArray1052;
	public JagexArchive titleArchive;
	public int flashingSidebar;
	public int screenMultiIconId;
	public Deque aClass19_1056;
	public final int[] anIntArray1057;
	public final RSInterface rsi;
	public Sprite[] mapScenes;
	public static int anInt1061;
	public int currentSound;
	public final int barFillColor;
	public int friendsListAction;
	public final int[] anIntArray1065;
	public int mouseInvInterfaceIndex;
	public int lastActiveInvInterface;
	public static ResourceProvider resourceProvider;
	public int currentRegionX;
	public int currentRegionY;
	public int anInt1071;
	public int[] anIntArray1072;
	public int[] anIntArray1073;
	public Sprite mapDotItem;
	public Sprite mapDotNPC;
	public Sprite mapDotPlayer;
	public Sprite mapDotFriend;
	public Sprite mapDotTeam;
	public Sprite mapDotClan;
	public int anInt1079;
	public boolean aBoolean1080;
	public String[] friendsList;
	public JagexBuffer packet;
	public int anInt1084;
	public int anInt1085;
	public int activeInterfaceType;
	public int anInt1087;
	public int anInt1088;
	public static int anInt1089;
	public final int[] expectedCRCs;
	static int[] menuActionCmd2;
	public int[] menuActionCmd3;
	int[] menuActionID;
	static int[] menuActionCmd1;
	public Sprite[] headIcons;
	public Sprite[] skullIcons;
	public Sprite[] headIconsHint;
	public static int anInt1097;
	public int anInt1098;
	public int anInt1099;
	public int anInt1100;
	public int anInt1101;
	public int anInt1102;
	public static boolean tabAreaAltered;
	public int systemUpdateTimer;
	public RSImageProducer titleScreen;
	public RSImageProducer aRSImageProducer_1107;
	public RSImageProducer aRSImageProducer_1108;
	public RSImageProducer title;
	public RSImageProducer aRSImageProducer_1110;
	public RSImageProducer aRSImageProducer_1111;
	public RSImageProducer aRSImageProducer_1112;
	public RSImageProducer aRSImageProducer_1113;
	public RSImageProducer aRSImageProducer_1114;
	public RSImageProducer aRSImageProducer_1115;
	public static int anInt1117;
	public int membersInt;
	public String promptMessage;
	public Sprite compass;
	public RSImageProducer aRSImageProducer_1123;
	public RSImageProducer aRSImageProducer_1124;
	public RSImageProducer aRSImageProducer_1125;
	public static Player myPlayer;
	public final String[] playerAction;
	public final boolean[] atPlayerArray;
	public final int[][][] anIntArrayArrayArray1129;
	public final int[] tabInterfaceIDs = { 
			-1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1
	};
	public int cameraOffsetY;
	public int anInt1132;
	int menuActionRow;
	public static int anInt1134;
	public int spellSelected;
	public int anInt1137;
	public int spellUsableOn;
	public String spellTooltip;
	public Sprite[] aClass30_Sub2_Sub1_Sub1Array1140;
	public boolean aBoolean1141;
	public static int anInt1142;
	public int runEnergy;
	public boolean aBoolean1149;
	public Sprite[] crosses;
	public boolean musicEnabled;
	public IndexedImage[] aBackgroundArray1152s;
	static boolean needDrawTabArea;
	public int unreadMessages;
	public static int anInt1155;
	public static boolean fpsOn;
	public boolean loggedIn;
	public boolean canMute;
	public boolean aBoolean1159;
	public boolean aBoolean1160;
	static int loopCycle;
	public final String validUserPassChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
	public RSImageProducer tabAreaIP;
	public RSImageProducer mapEdgeIP;
	public RSImageProducer mapAreaIP;
	public RSImageProducer gameScreenIP;
	public RSImageProducer chatAreaIP;
	public int daysSinceRecovChange;
	public RSSocket socketStream;
	public int anInt1169;
	public int minimapZoom;
	public int anInt1171;
	public long aLong1172;
	public static String myUsername;
	public static String myPassword;
	public static int anInt1175;
	public boolean genericLoadingError;
	public final int[] objectClassType = { 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 };
	public int reportAbuseInterfaceID;
	public Deque aClass19_1179;
	public int[] anIntArray1180;
	public int[] anIntArray1181;
	public int[] anIntArray1182;
	public byte[][] aByteArrayArray1183;
	public int anInt1184;
	public int viewRotation;
	public int anInt1186;
	public int anInt1187;
	public static int anInt1188;
	int invOverlayInterfaceID;
	public int[] anIntArray1190;
	public int[] anIntArray1191;
	public JagexBuffer stream;
	public int anInt1193;
	public int splitpublicChat;
	public IndexedImage mapBack;
	String[] menuActionName;
	public Sprite aClass30_Sub2_Sub1_Sub1_1201;
	public Sprite aClass30_Sub2_Sub1_Sub1_1202;
	public final int[] anIntArray1203;
	static final int[] anIntArray1204 = { 9104, 10275, 7595, 3610, 7975, 8526,
			918, 38802, 24466, 10145, 58654, 5027, 1457, 16565, 34991, 25486 };
	public static boolean flagged;
	public final int[] anIntArray1207;
	public int anInt1208;
	public int minimapRotation;
	public int anInt1210;
	static int anInt1211;
	public String promptInput;
	public int anInt1213;
	public int[][][] intGroundArray;
	public long aLong1215;
	public int loginScreenCursorPos;
	public final Sprite[] modIcons;
	public long aLong1220;
	public int tabId;
	public int anInt1222;
	public static boolean inputTaken;
	public int inputDialogState;
	public static int anInt1226;
	public int nextSong;
	public boolean songChanging;
	public final int[] anIntArray1229;
	public LandscapeClippingPlane[] clippingPlanes;
	public static int anIntArray1232[];
	public boolean aBoolean1233;
	public int[] mapLocation;
	public int[] floorMap;
	public int[] objectMap;
	public int anInt1237;
	public int anInt1238;
	public final int anInt1239 = 100;
	public final int[] anIntArray1240;
	public final int[] anIntArray1241;
	public boolean aBoolean1242;
	public int atInventoryLoopCycle;
	public int atInventoryInterface;
	public int atInventoryIndex;
	public int atInventoryInterfaceType;
	public byte[][] aByteArrayArray1247;
	public int tradeMode;
	public int anInt1249;
	public final int[] anIntArray1250;
	public int anInt1251;
	public boolean rsAlreadyLoaded;
	public int anInt1253;
	public int anInt1254;
	public boolean welcomeScreenRaised;
	public boolean showInput;
	public int anInt1257;
	public byte[][][] byteGroundArray;
	public int previousSong;
	public int destX;
	public int destY;
	public Sprite miniMap;// aClass30_Sub2_Sub1_Sub1_1263
	public int arbitraryDestination;
	public int anInt1265;
	public String loginMessage1;
	public String loginMessage2;
	public int bigRegionX;
	public int bigRegionY;
	public RSFontSystem newSmallFont, newRegularFont, newBoldFont,
			newFancyFont, regularHitFont, bigHitFont;
	public static RSFont smallText;
	public static RSFont normalFont;
	public static RSFont boldFont;
	public static RSFont fancyText;
	public int anInt1275;
	public int backDialogID;
	public int cameraOffsetX;
	public int anInt1279;
	public int[] walkingQueueX;
	public int[] walkingQueueY;
	public int itemSelected;
	public int useEntitySlot;
	public int useEntityInterface;
	public int useEntityId;
	public String selectedItemName;
	public int publicChatMode;
	public static int currentWalkingQueueSize;
	public int anInt1289;
	public static int anInt1290;
	public int drawCount;
	public int fullscreenInterfaceID;
	public int anInt1044;// 377
	public int anInt1129;// 377
	public int anInt1315;// 377
	public int anInt1500;// 377
	public int anInt1501;// 377
	public int[] fullScreenTextureArray;
	public String consoleInput;
	public static boolean consoleOpen;
	public final String[] consoleMessages;
	public AlertHandler alertHandler;
	public static FamiliarHandler familiarHandler;
	public boolean loadedImages = false;
	public static ImageLoader imageLoader;
	//title screen sprites
	public RSImage header;
	public RSImage box;
	public RSImage input;
	public RSImage input_hover;
	public RSImage play;
	public RSImage play_hover;
	public RSImage[] back;
	public RSImage formbox;
	public RSImage[] info;
	public RSImage[] check;
	public RSImage[] cross;
	public RSImage[] status;
	public RSImage[] status_hover;
	public RSImage[] close;
	public RSImage[] screen_dull;
	public RSImage[] screen_selected;
	public RSImage[] detail_dull;
	public RSImage[] detail_hover;
	public RSImage[] detail_selected;
	public RSImage header_glow;
	public RSImage[] bar;
	public RSImage fill;
	public RSImage banner;
	public RSImage[] background;

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	public byte[] getImage(String name) {
		try {
			return getImageLoader().getCache().getFile(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] getImage(String dir, String name) {
		try {
			return getImageLoader().getCache().getFile(dir, name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static FamiliarHandler getFamiliar() {
		return familiarHandler;
	}

	public Sprite grabSpriteFromCache(String s, int i) {
		JagexArchive streamLoader = streamLoaderForName(4, "2d graphics",
				"media", expectedCRCs[4], 40);
		Sprite sprite = null;
		try {
			sprite = new Sprite(streamLoader, s, i);
		} catch (Exception _ex) {
			return null;
		}
		return sprite;
	}

	public void resetAllImageProducers() {
		if (super.fullGameScreen != null) {
			return;
		}
		chatAreaIP = null;
		mapAreaIP = null;
		tabAreaIP = null;
		gameScreenIP = null;
		titleScreen = null;
		aRSImageProducer_1123 = null;
		aRSImageProducer_1124 = null;
		aRSImageProducer_1125 = null;
		aRSImageProducer_1107 = null;
		aRSImageProducer_1108 = null;
		title = null;
		aRSImageProducer_1110 = null;
		aRSImageProducer_1111 = null;
		aRSImageProducer_1112 = null;
		aRSImageProducer_1113 = null;
		aRSImageProducer_1114 = null;
		aRSImageProducer_1115 = null;
		super.fullGameScreen = new RSImageProducer(765, 503, getGameComponent());
		welcomeScreenRaised = true;
	}

	public int clientSize = 0;
	public int clientWidth = 765;
	public int clientHeight = 503;
	public int appletWidth = 765;
	public int appletHeight = 503;
	
	public boolean isFixed() {
		return clientSize == 0;
	}

	public int gameAreaWidth = 512, gameAreaHeight = 334;
	
	public void rebuildFrame(int size, int width, int height) {
		gameAreaWidth = (size == 0) ? 512 : width;
		gameAreaHeight = (size == 0) ? 334 : height;
		clientWidth = width;
		clientHeight = height;
		instance.rebuildFrame(size == 2, width, height, size == 1, size == 2);
		updateGameArea();
		super.mouseX = super.mouseY = -1;
	}

	public void updateGameArea() {
		Rasterizer.setBounds(clientWidth, clientHeight);
		fullScreenTextureArray = Rasterizer.lineOffsets;
		Rasterizer.setBounds(isFixed() ? (chatAreaIP != null ? chatAreaIP.width : 519) : clientWidth, isFixed() ? (chatAreaIP != null ? chatAreaIP.height : 165) : clientHeight);
		anIntArray1180 = Rasterizer.lineOffsets;
		Rasterizer.setBounds(isFixed() ? (tabAreaIP != null ? tabAreaIP.width : 246) : clientWidth, isFixed() ? (tabAreaIP != null ? tabAreaIP.height : 345) : clientHeight);
		anIntArray1181 = Rasterizer.lineOffsets;
		Rasterizer.setBounds(gameAreaWidth, gameAreaHeight);
		anIntArray1182 = Rasterizer.lineOffsets;
		int ai[] = new int[9];
		for(int i8 = 0; i8 < 9; i8++) {
			int k8 = 128 + i8 * 32 + 15;
			int l8 = 600 + k8 * 3;
			int i9 = Rasterizer.SINE[k8];
			ai[i8] = l8 * i9 >> 16;
		}
		LandscapeScene.method310(500, 800, gameAreaWidth, gameAreaHeight, ai);
		if (loggedIn) {
			gameScreenIP = new RSImageProducer(gameAreaWidth, gameAreaHeight, getGameComponent());
		} else {
			title = new RSImageProducer(clientWidth, clientHeight, getGameComponent());
		}
	}

	public void toggleSize(int size) {
		if (clientSize != size) {
			clientSize = size;
			int width = 765;
			int height = 503;
			if (isFixed()) {
				width = 765;
				height = 503;
			} else if (clientSize == 1) {
				width = appletWidth;
				height = appletHeight;
			} else if (clientSize == 2) {
				width = getMaxWidth();
				height = getMaxHeight();
			}
			rebuildFrame(size, width, height);
			updateGameArea();
			stream.createFrame(49);
			stream.writeByte(clientSize);
		}
	}

	public void checkSize() {
		if (clientSize == 1 && mainFrame != null && !isApplet) {
			if (clientWidth != mainFrame.getFrameWidth()) {
				clientWidth = mainFrame.getFrameWidth();
				gameAreaWidth = clientWidth;
				updateGameArea();
			}
			if (clientHeight != mainFrame.getFrameHeight()) {
				clientHeight = mainFrame.getFrameHeight();
				gameAreaHeight = clientHeight;
				updateGameArea();
			}
		}
	}

	public int getClientWidth() {
		return clientWidth;
	}

	public int getClientHeight() {
		return clientHeight;
	}

	public int getMaxWidth() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}

	public int getMaxHeight() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}

	public void launchURL(String url) {
		String osName = System.getProperty("os.name");
		try {
			if (osName.startsWith("Mac OS")) {
				Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
				Method openURL = fileMgr.getDeclaredMethod("openURL",
						new Class[] { String.class });
				openURL.invoke(null, new Object[] { url });
			} else if (osName.startsWith("Windows"))
				Runtime.getRuntime().exec(
						"rundll32 url.dll,FileProtocolHandler " + url);
			else { // assume Unix or Linux
				String[] browsers = { "firefox", "opera", "konqueror",
						"epiphany", "mozilla", "netscape", "safari" };
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++)
					if (Runtime.getRuntime()
							.exec(new String[] { "which", browsers[count] })
							.waitFor() == 0)
						browser = browsers[count];
				if (browser == null) {
					throw new Exception("Could not find web browser");
				} else
					Runtime.getRuntime().exec(new String[] { browser, url });
			}
		} catch (Exception e) {
			pushMessage("", "Failed to open URL.", 0);
		}
	}

	static {
		anIntArray1019 = new int[99];
		int i = 0;
		for (int j = 0; j < 99; j++) {
			int l = j + 1;
			int i1 = (int) ((double) l + 300D * Math.pow(2D, (double) l / 7D));
			i += i1;
			anIntArray1019[j] = i / 4;
		}
		anIntArray1232 = new int[32];
		i = 2;
		for (int k = 0; k < 32; k++) {
			anIntArray1232[k] = i - 1;
			i += i;
		}
	}
}
