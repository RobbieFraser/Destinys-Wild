package game;

import game.items.Health;
import game.items.Item;
import game.items.Key;
import game.items.Score;
import game.npcs.EnemyStill;
import game.npcs.EnemyWalker;
import game.npcs.FriendlyStill;
import game.npcs.NPC;
import game.obstacles.Block;
import game.obstacles.Breakable;
import game.obstacles.Obstacle;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XMLParser {

	/**
	 * Initialises the board from the XML file
	 */
	public static Board initialiseBoard(String filename){

		Board board = new Board();

		try {
			SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File(filename);
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List<Element> roomTags = rootNode.getChildren("Room");

			for (Element room : roomTags) { //Initialise Rooms
				int roomX = Integer.valueOf(room.getChildText("Posx")); //room parameters
				int roomY = Integer.valueOf(room.getChildText("Posy"));
				Point point = new Point(roomX, roomY);

				int id = Integer.valueOf(room.getChildText("Id"));

				int north = Integer.valueOf(room.getChildText("North"));
				int east = Integer.valueOf(room.getChildText("East"));
				int south = Integer.valueOf(room.getChildText("South"));
				int west = Integer.valueOf(room.getChildText("West")); //----------------

				Room currentRoom = new Room(north, east, south, west, id, point);

				initialiseObstacles(room, currentRoom);
				initialiseNPCS(room, currentRoom);
				initialiseItems(room, currentRoom);

				System.out.println("Loading room with ID: " + id);
				board.addRoom(currentRoom, roomX, roomY);
			}


		}
		catch (FileNotFoundException e){
//			if (filename.equals("data/state.xml")){
//				System.out.println("No current save state found. Loading original board");
//				return initialiseBoard("data/board.xml");
//			}
			System.out.println(filename);
			System.out.println("Wrong filepath");
		}
		catch(Exception e){
			System.out.println(e.getMessage() + " <-- ERROR");
		}
		return board;

	}

	private static void initialiseObstacles(Element room, Room currentRoom) {
		if (room.getChild("Obstacles") == null) {
			return;
		}
		//room contains obstacles
		List<Element> obs = room.getChildren("Obstacles");
		List<Element> obsTags = obs.get(0).getChildren("Obstacle");

		for (Element obstacle : obsTags){ //Initialising Obstacles
			String type = obstacle.getChildText("Type"); //-----------Obstacle parameters
			String obsType = obstacle.getChildText("Obstype");

			int obsx = Integer.valueOf(obstacle.getChildText("Posx"));
			int obsy = Integer.valueOf(obstacle.getChildText("Posy")); //----------------

			Obstacle temp = null;

			Point coords = new Point(obsx, obsy);

			switch (obsType){
			case "breakable":
				temp = new Breakable(type, coords);
				break;
			case "block":
				temp = new Block(type, coords);
				break;
			default:
				System.out.println("Misidentifed Obstacle");
				break;
			}
			currentRoom.addObstacle(temp, obsx, obsy); //adds each obstacle to the current room
		}
	}

	private static void initialiseNPCS(Element room, Room currentRoom) {
		if (room.getChild("Npcs") == null) {
			return;
		}
		//Room contains Non playing characters
		List<Element> npcs = room.getChildren("Npcs");
		List<Element> npcTags = npcs.get(0).getChildren("Npc");

		for (Element npc : npcTags){ // Initialising NPC's
			String npcType = npc.getChildText("Npctype");

			String type = npc.getChildText("Type"); // NPC parameters-------

			int npcx = Integer.valueOf(npc.getChildText("Posx"));
			int npcy = Integer.valueOf(npc.getChildText("Posy"));

			int damage = Integer.valueOf(npc.getChildText("Damage"));
			int speed = Integer.valueOf(npc.getChildText("Speed")); //------

			NPC temp = null;
			Point coords = new Point(npcx, npcy);

			switch (npcType){
			case "enemywalker":
				temp = new EnemyWalker(type, coords, speed, damage);
				break;

			case "enemystill":
				temp = new EnemyStill(type, coords, damage);
				break;

			case "friendlystill":
				temp = new FriendlyStill(type, coords);
				break;

			default:
				System.out.println("Misidentifed NPC");
			}
			currentRoom.addNpcs(temp, npcx, npcy); //adds all npc's to the current room
		}
	}

	private static void initialiseItems(Element room, Room currentRoom) {
		if (room.getChild("Items") == null) {
			return;
		}

		List<Element> items = room.getChildren("Items");
		List<Element> itemTags = items.get(0).getChildren("Item");

		for (Element item : itemTags){ // Initialising Items
			String itemType = item.getChildText("Itemtype");

			String type = item.getChildText("Type"); // Item parameters-------

			int itemx = Integer.valueOf(item.getChildText("Posx"));
			int itemy = Integer.valueOf(item.getChildText("Posy"));

			int itemid = Integer.valueOf(item.getChildText("Id"));

			int health = Integer.valueOf(item.getChildText("Health"));
			int score = Integer.valueOf(item.getChildText("Score")); //------

			Item temp = null;

			Point coords = new Point(itemx, itemy);

			switch (itemType){
			case "health":
				temp = new Health(type, coords, health, itemid);
				break;
			case "score":
				temp = new Score(type, coords, score, itemid);
				break;
			case "key":
				temp = new Key(itemid, coords);
				break;
			default:
				System.out.println("Misidentifed Item");
			}
			currentRoom.addItems(temp, itemx, itemy); //adds all items to the current room
		}
	}

	/**
	 * Loads save states for the current game
	 */
	public static void loadState(File playerFile){
		initialiseBoard("data/savestate.xml");
		loadPlayer(playerFile);
	}

	public static void loadPlayer(File playerFile){
		try{
			SAXBuilder builder = new SAXBuilder();
			Document document = (Document) builder.build(playerFile);
			Element playerTag = document.getRootElement();
			
			String name = playerTag.getChildText("Name");
			
			Player player = new Player();
			
			int playerx = Integer.valueOf(playerTag.getChildText("Posx"));
			int playery = Integer.valueOf(playerTag.getChildText("Posy"));
			
			int health = Integer.valueOf(playerTag.getChildText("Health"));
			int currentRoom = Integer.valueOf(playerTag.getChildText("Currentroom"));
			int score = Integer.valueOf(playerTag.getChildText("Score"));
			int speed = Integer.valueOf(playerTag.getChildText("Speed"));
			
			List<Element> inventory = playerTag.getChild("Inventory").getChildren("Itemid");
			
			for(Element invItem : inventory){
				int itemId = Integer.valueOf(invItem.getChildText("Itemid"));

			}
			
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}



	/**
	 * Saves the current game board to an XML file
	 */
	public static void saveBoard(){
		System.out.println("Saving player");
		savePlayer(); //save the player info separately
		System.out.println("Player saved");
	}

	public static void savePlayer(){
		Player player = DestinysWild.getPlayer();

		try{
			Element playerTag = new Element("Player"); //Root element name
			Document doc = new Document(playerTag);

			playerTag.addContent(new Element("Name").setText(player.getName()));
			playerTag.addContent(new Element("Posx").setText(String.valueOf(player.getCoords().x)));
			playerTag.addContent(new Element("Posy").setText(String.valueOf(player.getCoords().y)));
			playerTag.addContent(new Element("Health").setText(String.valueOf(player.getHealth())));
			playerTag.addContent(new Element("Currentroom").setText(String.valueOf(player.getCurrentRoom())));
			playerTag.addContent(new Element("Score").setText(String.valueOf(player.getScore())));
			playerTag.addContent(new Element("Speed").setText(String.valueOf(player.getSpeed())));

			Element visitedRooms = new Element("Visitedrooms");

			for(Room roomId : player.getVisitedRooms()){
				visitedRooms.addContent(new Element("Roomid").setText(String.valueOf(roomId.getId())));
			}

			playerTag.addContent(visitedRooms);

			Element inventory = new Element("Inventory");

			for(Item itemId : player.getInventory()){
				inventory.addContent(new Element("Itemid").setText(String.valueOf(itemId.getId())));
			}

			playerTag.addContent(inventory);


			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter("data/savegames/"+ player.getName() + ".xml"));
		}
		catch(Exception e){
			System.out.println(e.getMessage() + "<--- ERROR");
		}
	}


}
