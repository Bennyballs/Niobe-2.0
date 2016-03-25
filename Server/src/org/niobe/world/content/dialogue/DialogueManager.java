package org.niobe.world.content.dialogue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.niobe.model.definition.MobDefinition;
import org.niobe.util.XmlUtil;
import org.niobe.world.Player;
import org.niobe.world.content.dialogue.Dialogue.DialogueExpression;
import org.niobe.world.content.dialogue.Dialogue.DialogueType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Manages the loading and start of dialogues.
 * 
 * @author relex lawl
 */
public final class DialogueManager {

	/**
	 * The directory where the dialogues file is located.
	 */
	private static final String FILE_DIRECTORY = "./data/dialogues.xml";
	
	/**
	 * Contains all dialogues loaded from said file.
	 */
	private static final Map<Integer, Dialogue> dialogues = new HashMap<Integer, Dialogue>();
	
	/**
	 * The {@link DialogueManager} logger instance.
	 */
	private static final Logger logger = Logger.getLogger(DialogueManager.class.getName());

	/**
	 * Parses the information from the dialogue file.
	 */
	public static void init() {
		long startup = System.currentTimeMillis();
		System.out.println("Loading dialogues...");
		try {
			File fXmlFile = new File(FILE_DIRECTORY);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("dialogue");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nNode;
					try {
						final int id = Integer.valueOf(XmlUtil.getEntry("id", element));
						final DialogueType type = DialogueType.valueOf(XmlUtil.getEntry("type", element));
						DialogueExpression dialogueExpression = null;
						if (type == DialogueType.MOB_STATEMENT || type == DialogueType.PLAYER_STATEMENT)
							dialogueExpression = DialogueExpression.valueOf(XmlUtil.getEntry("animation", element));
						final DialogueExpression animation = dialogueExpression;
						final int totalLines = Integer.valueOf(XmlUtil.getEntry("lines", element));
						String[] lines = new String[totalLines];
						for (int i = 0; i < lines.length; i++) {
							lines[i] = XmlUtil.getEntry("line" + (i + 1), element);
						}
						int mob = -1, item = -1, zoom = -1;
						String header = null;
						if (type == DialogueType.MOB_STATEMENT) {
							mob = Integer.valueOf(XmlUtil.getEntry("mobId", element));
						} else if (type == DialogueType.ITEM_STATEMENT) {
							item = Integer.valueOf(XmlUtil.getEntry("itemId", element));
							zoom = Integer.valueOf(XmlUtil.getEntry("itemZoom", element));
							header = XmlUtil.getEntry("header", element);
						}
						final int nextDialogue = Integer.valueOf(XmlUtil.getEntry("next", element));
						final int mobId = mob, itemId = item, itemZoom = zoom;
						final String dialogueHeader = header;
						final String[] finalLines = lines;
						Dialogue dialogue = new Dialogue() {
							@Override
							public int getId() {
								return id;
							}
							
							@Override
							public DialogueType getType() {
								return type;
							}
							
							@Override
							public DialogueExpression getAnimation() {
								return animation;
							}
							
							@Override
							public String[] getDialogues() {
								return finalLines;
							}

							@Override
							public int getNextDialogueId() {
								return nextDialogue;
							}

							@Override
							public int getMobId() {
								return mobId;
							}
							
							@Override
							public String[] getItems() {
								return new String[] {
									String.valueOf(itemId),
									String.valueOf(itemZoom),
									dialogueHeader
								};
							}
						};
						dialogues.put(id, dialogue);
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}
			System.out.println("Loaded " + dialogues.size() + " dialogue" + (dialogues.size() > 1 ? "s" : "") + " in " + (System.currentTimeMillis() - startup) + "ms");
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts a dialogue gotten from the dialogues map.
	 * @param player	The player to dialogue with.
	 * @param id		The id of the dialogue to retrieve from dialogues map.
	 */
	public static void start(Player player, int id) {
		Dialogue dialogue = dialogues.get(id);
		start(player, dialogue);
	}
	
	/**
	 * Starts a dialogue.
	 * @param player	The player to dialogue with.	
	 * @param dialogue	The dialogue to show the player.
	 */
	public static void start(Player player, Dialogue dialogue) {
		player.getFields().setDialogue(dialogue);
		if (dialogue == null || dialogue.getId() < 0) {
			player.getPacketSender().sendInterfaceRemoval();
		} else {
			showDialogue(player, dialogue);
			dialogue.specialAction();
		}
	}
	
	/**
	 * Handles the clicking of 'click here to continue', option1, option2 and so on.
	 * @param player	The player who will continue the dialogue.
	 */
	public static void next(Player player) {
		if (player.getFields().getDialogue() == null) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Dialogue next = player.getFields().getDialogue().getNextDialogue();
		if (next == null)
			next = dialogues.get(player.getFields().getDialogue().getNextDialogueId());
		if (next == null || next.getId() < 0) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		start(player, next);
	}

	/**
	 * Configures the dialogue's type and shows the dialogue interface
	 * and sets its child id's.
	 * @param player		The player to show dialogue for.
	 * @param dialogue		The dialogue to show.
	 */
	private static void showDialogue(Player player, Dialogue dialogue) {
		String[] lines = dialogue.getDialogues();
		switch (dialogue.getType()) {
		case MOB_STATEMENT:
			if (dialogue.getMobId() <= 0 || dialogue.getMobId() == 65535) {
				logger.warning("Mob statement dialogue contains no mob id: " + dialogue.getId());
				player.getPacketSender().sendInterfaceRemoval();
				break;
			}
			int startDialogueChildId = MOB_DIALOGUE_ID[lines.length - 1];
			int headChildId = startDialogueChildId - 2;
			player.getPacketSender().sendNpcHeadOnInterface(dialogue.getMobId(), headChildId);
			player.getPacketSender().sendInterfaceAnimation(headChildId, dialogue.getAnimation().getAnimation());
			player.getPacketSender().sendString(startDialogueChildId - 1, MobDefinition.forId(dialogue.getMobId()).getName());
			for (int i = 0; i < lines.length; i++) {
				player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
			}
			player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
			break;
		case PLAYER_STATEMENT:
			startDialogueChildId = PLAYER_DIALOGUE_ID[lines.length - 1];
			headChildId = startDialogueChildId - 2;
			player.getPacketSender().sendPlayerHeadOnInterface(headChildId);
			player.getPacketSender().sendInterfaceAnimation(headChildId, dialogue.getAnimation().getAnimation());
			player.getPacketSender().sendString(startDialogueChildId - 1, player.getCredentials().getUsername());
			for (int i = 0; i < lines.length; i++) {
				player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
			}
			player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
			break;
		case ITEM_STATEMENT:
			startDialogueChildId = MOB_DIALOGUE_ID[lines.length - 1];
			headChildId = startDialogueChildId - 2;
			player.getPacketSender().sendInterfaceModel(headChildId, Integer.valueOf(dialogue.getItems()[0]), Integer.valueOf(dialogue.getItems()[1]));
			player.getPacketSender().sendString(startDialogueChildId - 1, dialogue.getItems()[2]);
			for (int i = 0; i < lines.length; i++) {
				player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
			}
			player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
			break;
		case STATEMENT:
			player.getPacketSender().sendString(357, dialogue.getDialogues()[0]);
			player.getPacketSender().sendString(358, "Click here to continue");
			player.getPacketSender().sendChatboxInterface(356);
			break;
		case OPTIONS:
			int firstChildId = OPTION_DIALOGUE_ID[lines.length - 1];
			player.getPacketSender().sendString(firstChildId - 1, "Choose an option");
			for (int i = 0; i < lines.length; i++) {
				player.getPacketSender().sendString(firstChildId + i, lines[i]);
			}
			player.getPacketSender().sendChatboxInterface(firstChildId - 2);
			break;
		}
	}
	
	/**
	 * Gets an empty id for a dialogue.
	 * @return	An empty index from the map or the map's size itself.
	 */
	public static int getDefaultId() {
		int id = dialogues.size();
		for (int i = 0; i < dialogues.size(); i++) {
			if (dialogues.get(i) == null) {
				id = i;
				break;
			}
		}
		return id;
	}
	
	/**
	 * Retrieves the dialogues map.
	 * @return	dialogues.
	 */
	public static Map<Integer, Dialogue> getDialogues() {
		return dialogues;
	}
	
	/**
	 * This array contains the child id where the dialogue
	 * statement starts for mob and item dialogues.
	 */
	private static final int[] MOB_DIALOGUE_ID = {
		4885,
		4890,
		4896,
		4903
	};
	
	/**
	 * This array contains the child id where the dialogue
	 * statement starts for player dialogues.
	 */
	private static final int[] PLAYER_DIALOGUE_ID = {
		971,
		976,
		982,
		989
	};
	
	/**
	 * This array contains the child id where the dialogue
	 * statement starts for option dialogues.
	 */
	private static final int[] OPTION_DIALOGUE_ID = {
		13760,
		2461,
		2471,
		2482,
		2494,
	};
}
