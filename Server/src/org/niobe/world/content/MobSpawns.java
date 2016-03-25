package org.niobe.world.content;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.niobe.GameServer;
import org.niobe.model.Direction;
import org.niobe.model.Position;
import org.niobe.util.XmlUtil;
import org.niobe.world.Mob;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This file is used to read and parse an mob spawn value from the
 * {@code DIRECTORY} file and registers them in the world.
 * 
 * @author relex lawl
 */
public final class MobSpawns {
	
	/**
	 * The directory of the file containing all mob spawns.
	 */
	private final static String DIRECTORY = "./data/mob_spawns.xml";
	
	/**
	 * Loads all mob spawns found in the {@code DIRECTORY} file.
	 */
	public static void init() {
		long startup = System.currentTimeMillis();
		int amount = 0;
		try {
			System.out.println("Loading mob spawns...");
			File fXmlFile = new File(DIRECTORY);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("spawn");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nNode;
					try {
						int id = Integer.parseInt(XmlUtil.getEntry("id", element));
						int x = Integer.parseInt(XmlUtil.getEntry("x", element));
						int y = Integer.parseInt(XmlUtil.getEntry("y", element));
						int z = Integer.parseInt(XmlUtil.getEntry("z", element));
						Position position = new Position(x, y, z);
						Direction direction = Direction.valueOf(XmlUtil.getEntry("face", element));
						int walkDistance = Integer.valueOf(XmlUtil.getEntry("walkDistance", element));
						GameServer.getWorld().register(new Mob(id, position, direction).setWalkDistance(walkDistance));
						amount++;
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}
			System.out.println("Loaded " + amount + " mob spawn" + (amount > 1 ? "s" : "") + " in " + (System.currentTimeMillis() - startup) + "ms");
		} catch (IOException | SAXException | ParserConfigurationException exception) {
			exception.printStackTrace();
		}
	}
}
