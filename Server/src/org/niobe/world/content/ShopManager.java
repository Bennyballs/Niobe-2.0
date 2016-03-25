package org.niobe.world.content;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.niobe.model.Item;
import org.niobe.model.container.impl.Shop;
import org.niobe.model.container.impl.Shop.Currency;
import org.niobe.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Loads and manages all the {@link org.niobe.model.container.impl.Shop}s
 * in the world.  
 *
 * @author relex lawl
 */
public final class ShopManager {
	
	/**
	 * The map holds all the {@link org.niobe.model.container.impl.Shop} 
	 * loaded from the file in directory {@link #FILE_DIRECTORY}.
	 */
	private static final Map<Integer, Shop> shops = new HashMap<Integer, Shop>();
	
	/**
	 * The directory where the shop file is located.
	 */
	private static final String FILE_DIRECTORY = "./data/shops.xml";
	
	/**
	 * Loads all the {@link org.niobe.model.container.impl.Shop}
	 * information and populates the {@link #shops} map.
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			System.out.println("Loading shop definitions...");
			long startup = System.currentTimeMillis();
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(FILE_DIRECTORY);
			NodeList shopList = doc.getElementsByTagName("shop");
			for (int i = 0; i < shopList.getLength(); i++) {
				Element shopElement = (Element) shopList.item(i);
				int shopId = Integer.parseInt(XmlUtil.getEntry("id", shopElement));
				String name = XmlUtil.getEntry("name", shopElement);
				@SuppressWarnings("unchecked")
				Class<Currency> currencyClass = (Class<Currency>) Class.forName(XmlUtil.getEntry("currency", shopElement));
				Currency currency = currencyClass.newInstance();
				NodeList stockList = shopElement.getElementsByTagName("item");
				Item[] stock = new Item[stockList.getLength()];
				int index = 0;
				for (int j = 0; j < stockList.getLength(); j++) {
					Element stockElement = (Element) stockList.item(j);
					Item item = new Item(Integer.valueOf(XmlUtil.getEntry("id", stockElement)),
											Integer.valueOf(XmlUtil.getEntry("amount", stockElement)));
					stock[index] = item;
					index++;
				}
				shops.put(shopId, new Shop(null, shopId, name, currency, stock));
			}
			System.out.println("Loaded " + shops.size() + " shop" + (shops.size() == 1 ? "s" : "") + " in " + (System.currentTimeMillis() - startup) + "ms");
		} catch (IOException | ParserConfigurationException | SAXException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Gets all the shops in the world.
	 * @return	The shops map.
	 */
	public static Map<Integer, Shop> getShops() {
		return shops;
	}
}
