package game;

import game.items.Health;
import game.items.Item;
import game.items.Key;
import game.items.Score;
import game.items.Tool;
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
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * XMLParser deals with all file interactions like loading board, saving board, loading player and saving player
 * @author Rob
 *
 */
public class XMLParser {

	private static String LOCAL_SAVESTATE = "data/savestate.xml"; //The file path at which the changed game board will be saved
	private static String LOCAL_BOARD = "data/board.xml"; //The original board files

	/**
	 * Initialises the board from an XML file
	 * @param filename the file from which the board will be loaded
	 * @return the initialised board object. Stored in DestinysWild.java
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
				int roomRow = Integer.valueOf(room.getChildText("Row")); //room parameters
				int roomCol = Integer.valueOf(room.getChildText("Col"));
				Point point = new Point(roomRow, roomCol);

				int id = Integer.valueOf(room.getChildText("Id"));

				int north = Integer.valueOf(room.getChildText("North"));
				int east = Integer.valueOf(room.getChildText("East"));
				int south = Integer.valueOf(room.getChildText("South"));
				int west = Integer.valueOf(room.getChildText("West")); //----------------

				Room currentRoom = new Room(north, east, south, west, id, point);

				initialiseObstacles(room, currentRoom);
				initialiseNPCS(room, currentRoom);
				initialiseOnBoardItems(room, currentRoom, board);
				currentRoom.initialiseTiles();

				System.out.println("Loading room with ID: " + id);
				board.addRoom(currentRoom, roomRow, roomCol);
			}
			initialiseOffBoardItems(rootNode, board);


		}
		catch (FileNotFoundException e){
			if (filename.equals(LOCAL_SAVESTATE)){
				System.out.println("No current save state found. Loading original board");
				return initialiseBoard("data/board.xml"); //When the savestate.xml folder doesn't yet exist
			}
			else{
				System.out.println(filename);
				System.out.println("Wrong filepath");
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage() + " <-- ERROR");
		}
		return board;

	}

	/**
	 * Loads in all the obstacles in the given room
	 * @param room XML element that contains all the obstacle info for this room
	 * @param currentRoom in use
	 */
	private static void initialiseObstacles(Element room, Room currentRoom) {
		if (room.getChild("Obstacles") == null) {
			return;
		}
		//room contains obstacles
		List<Element> obs = room.getChildren("Obstacles");
		List<Element> obsTags = obs.get(0).getChildren("Obstacle");

		for (Element obstacle : obsTags){ //Initialising Obstacles
			String type = obstacle.getChildText("Type"); //Obstacle parameters-----------
			String obsType = obstacle.getChildText("Obstype");

			int obsRow = Integer.valueOf(obstacle.getChildText("Row"));
			int obsCol = Integer.valueOf(obstacle.getChildText("Col")); //----------------

			Obstacle temp = null;

			Point coords = new Point(obsRow, obsCol);

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
			currentRoom.addObstacle(temp, obsRow, obsCol); //adds each obstacle to the current room
		}
	}

	/**
	 * Loads in all the NPC's in the given room
	 * @param room XML element that contains all the NPC info for this room
	 * @param currentRoom in use
	 */
	private static void initialiseNPCS(Element room, Room currentRoom) {
		if (room.getChild("Npcs") == null) {
			return;
		}
		//Room contains Non playing characters
		List<Element> npcs = room.getChild("Npcs").getChildren("Npc");

		for (Element npc : npcs){ // Initialising NPC's
			String npcType = npc.getChildText("Npctype");

			String type = npc.getChildText("Type"); // NPC parameters-------

			int npcRow = Integer.valueOf(npc.getChildText("Row"));
			int npcCol = Integer.valueOf(npc.getChildText("Col"));
			int id = Integer.valueOf(npc.getChildText("Id"));
			int damage = Integer.valueOf(npc.getChildText("Damage"));
			int speed = Integer.valueOf(npc.getChildText("Speed")); //------

			NPC temp = null;
			Point coords = new Point(npcRow, npcCol);

			switch (npcType){
			case "enemywalker":
				temp = new EnemyWalker(type, id, coords, speed, damage, currentRoom);
				break;

			case "enemystill":
				temp = new EnemyStill(type, id, coords, damage, currentRoom);
				break;

			case "friendlystill":
				temp = new FriendlyStill(type, id, coords, currentRoom);
				break;

			default:
				System.out.println("Misidentifed NPC");
			}
			currentRoom.addNpc(temp); //adds all npc's to the current room
		}
	}


	/**
	 * gathers on board items to be initialised and sends them to initialiseItems
	 * @param rootNode the XML element that contains the list of on board items
	 * @param board the board
	 */
	private static void initialiseOnBoardItems(Element room, Room currentRoom, Board board) {
		if (room.getChild("Items") == null) {
			return;
		}

		List<Element> items = room.getChildren("Items").get(0).getChildren("Item");

		initialiseItems(items, currentRoom, board);

	}

	/**
	 * gathers off board items to be initialised and sends them to initialiseItems
	 * @param rootNode the XML element that contains the list of off board items
	 * @param board the board
	 */
	public static void initialiseOffBoardItems(Element rootNode, Board board){
		if(rootNode.getChild("Offitems") == null){
			System.out.println("No offboard items found");
			return;
		}

		List<Element> items = rootNode.getChildren("Offitems").get(0).getChildren("Item");
		System.out.println("OffBoardItems size: "+items.size());

		initialiseItems(items, null, board);


	}

	/**
	 * Loads in all the Items in the given room
	 * @param itemList items to be initialised
	 * @param room XML element that contains all the Items info for this room
	 * @param currentRoom in use
	 */
	public static void initialiseItems(List<Element> itemList, Room currentRoom, Board board){
		for (Element item : itemList){ // Initialising Items
			String itemType = item.getChildText("Itemtype");

			String type = item.getChildText("Type"); // Item parameters-------

			int itemRow = Integer.valueOf(item.getChildText("Row"));
			int itemCol = Integer.valueOf(item.getChildText("Col"));

			int itemid = Integer.valueOf(item.getChildText("Id"));

			int health = Integer.valueOf(item.getChildText("Health"));
			int score = Integer.valueOf(item.getChildText("Score")); //-------

			Item temp = null;

			Point coords = new Point(itemRow, itemCol);

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

			if(currentRoom != null){
				currentRoom.addItem(temp, itemRow, itemCol); //adds all items to the current room
			}
			else{
				board.addOffBoardItem(temp); //adds items to off board list
			}
		}
	}

	/**
	 * Loads save states for the current game
	 * @param playerFile to be loaded. Null if new game
	 */
	public static void loadGame(File playerFile){
		if(playerFile == null){
			DestinysWild.setBoard(initialiseBoard(LOCAL_BOARD));
			DestinysWild.setPlayer(new Player());
		}
		else{
			DestinysWild.setBoard(initialiseBoard(LOCAL_SAVESTATE));
			loadPlayer(playerFile);
		}
	}

	/**
	 * Loads a player from their xml file
	 * @param playerFile to be loaded
	 */
	public static void loadPlayer(File playerFile){
		try{
			SAXBuilder builder = new SAXBuilder();
			Document document = (Document) builder.build(playerFile);
			Element playerTag = document.getRootElement();

			Board board = DestinysWild.getBoard();

			Player player = new Player();

			String name = playerTag.getChildText("Name"); //Main Player parameters--------------------

			int playerx = Integer.valueOf(playerTag.getChildText("Posx"));
			int playery = Integer.valueOf(playerTag.getChildText("Posy"));

			int health = Integer.valueOf(playerTag.getChildText("Health"));
			int score = Integer.valueOf(playerTag.getChildText("Score"));
			int speed = Integer.valueOf(playerTag.getChildText("Speed"));

			int currentRoomId = Integer.valueOf(playerTag.getChildText("Currentroom"));
			Room currentRoom = board.getRoomFromId(currentRoomId);//-----------

			List<Element> inventory = playerTag.getChild("Inventory").getChildren("Itemid");

			System.out.println("Inventory is empty: "+inventory.isEmpty());

			System.out.println(DestinysWild.getBoard().getOffBoardItems().size() + " <-- offBoardItems size");

			for(Element invItem : inventory){
				int itemId = Integer.valueOf(invItem.getText());
				Item tempItem = null;
				try{tempItem = board.getOffItemFromId(itemId);}//Depends on initialised board from savestate.xml
				catch(NullPointerException e){
					if(itemId < 10){ //must be a key
						tempItem = new Key(itemId, null);
					}
					else{
						String toolName;
						switch(itemId){
							case 10:
								toolName = "machete";
								break;
							case 20:
								toolName = "torch";
								break;
							case 30:
								toolName = "pickaxe";
								break;
							case 40:
								toolName = "jetfuel";
								break;
							case 50:
								toolName = "bucket";
								break;
							case 60:
								toolName = "spade";
								break;
							default:
								toolName = "error";
								System.out.println("Misidentified Tool");
								break;
						}
						tempItem = new Tool(toolName, itemId);
					}
				}
				System.out.println("Added to inventory: " + tempItem.toString());
				player.addInventoryItem(tempItem);
			}


			List<Element> visitedRooms = playerTag.getChild("Visitedrooms").getChildren("Roomid");

			for(Element room : visitedRooms){
				int roomId = Integer.valueOf(room.getText());
				player.addRoom(board.getRoomFromId(roomId));

			}

			player.setName(name);
			player.setCoords(playerx, playery);
			player.setHealth(health);
			player.setCurrentRoom(currentRoom);
			player.setScore(score);
			player.setSpeed(speed);
			player.setCurrentTile(currentRoom.calcTile(new Point(playerx, playery)));

			DestinysWild.setPlayer(player);
		}
		catch(Exception e){
			System.out.println(e.getMessage() + " <----- ERROR");
		}
	}



	/**
	 * Saves the current game board and player to XML files
	 */
	public static void saveGame(){
		String SAVE_FILE = "savestate.xml"; //This is the FILE PATH (Not Including 'data/') that will be used and overwritten each time the board is saved
		System.out.println("Saving player");
		savePlayer(); //save the player info separately
		System.out.println("Player saved");
		System.out.println("Saving board state to " + SAVE_FILE);
		Board board = DestinysWild.getBoard();
		saveBoard(SAVE_FILE, board);
	}

	/**
	 * Saves the board to savestate.xml save file.
	 * @param filename name of the file to be saved
	 * @param board game board
	 */
	public static void saveBoard(String filename, Board board){
		try{
			Element boardTag = new Element("Board"); //Root element name
			Document doc = new Document(boardTag);

			for(int i=0; i<board.getBoard().length; i++){
				for(int j=0; j<board.getBoard()[0].length; j++){

					Element roomTags = new Element("Room");//<Board><Room>
					Element obsTags = new Element("Obstacles");//<Board><Room><Obstacles>
					Element npcTags = new Element("Npcs");//<Board><Room><Npcs>
					Element itemTags = new Element("Items");//<Board><Room><Items>

					Room current = board.getRoomFromCoords(i, j);
					if(current == null){
						continue;
					}
					roomTags.addContent(new Element("Id").setText(String.valueOf(current.getId())));
					roomTags.addContent(new Element("Row").setText(String.valueOf(current.getBoardPos().x)));
					roomTags.addContent(new Element("Col").setText(String.valueOf(current.getBoardPos().y)));
					roomTags.addContent(new Element("North").setText(String.valueOf(current.getNorth())));
					roomTags.addContent(new Element("East").setText(String.valueOf(current.getEast())));
					roomTags.addContent(new Element("South").setText(String.valueOf(current.getSouth())));
					roomTags.addContent(new Element("West").setText(String.valueOf(current.getWest())));
					obsTags.addContent(saveObstacles(current));
					npcTags.addContent(saveNpcs(current));
					itemTags.addContent(saveItems(current));

					if(roomTags.getChildren().isEmpty()){
						continue;
					}
					roomTags.addContent(obsTags);
					roomTags.addContent(npcTags);
					roomTags.addContent(itemTags);
					boardTag.addContent(roomTags);
				}
			}

			Element offItems = new Element("Offitems");
			for(Item item : board.getOffBoardItems()){
				Element offItemTags = new Element("Item");
				offItemTags.addContent(new Element("Itemtype").setText(String.valueOf(item.toString())));
				offItemTags.addContent(new Element("Type").setText(String.valueOf(item.getType())));
				offItemTags.addContent(new Element("Id").setText(String.valueOf(item.getId())));
				offItemTags.addContent(new Element("Row").setText(String.valueOf(item.getCoords().x)));
				offItemTags.addContent(new Element("Col").setText(String.valueOf(item.getCoords().y)));
				offItemTags.addContent(new Element("Health").setText(String.valueOf(item.getHealth())));
				offItemTags.addContent(new Element("Score").setText(String.valueOf(item.getScore())));

				offItems.addContent(offItemTags);

				System.out.println("Saving " + item.toString() + " to off board items");
			}
			boardTag.addContent(offItems);

			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter("data/" + filename));

		}
		catch(Exception e){
			System.out.println(e.getMessage() + " <--- ERROR");
		}
	}

	/**
	 * creates a list of the obstacle elements to be saved to the file for the given room
	 * @param current room that's being saved
	 * @return the List of elements to be saved
	 */
	public static List<Element> saveObstacles(Room current){
		List<Element> obstacleList = new ArrayList<>();
		for(int i=0; i<current.getObstacles().length; i++){
			for(int j=0; j<current.getObstacles()[0].length; j++){
				Obstacle currObs = current.getObstacles()[i][j];
				Element obstacle = new Element("Obstacle");
				if(currObs != null){
					obstacle.addContent(new Element("Obstype").setText(currObs.toString()));
					obstacle.addContent(new Element("Type").setText(currObs.getType()));
					obstacle.addContent(new Element("Row").setText(String.valueOf(currObs.getCoords().x)));
					obstacle.addContent(new Element("Col").setText(String.valueOf(currObs.getCoords().y)));
					obstacleList.add(obstacle);
				}
			}
		}
		return obstacleList;
	}

	/**
	 * creates a list of the NPC elements to be saved to the file for the given room
	 * @param current room that's being saved
	 * @return the List of elements to be saved
	 */
	public static List<Element> saveNpcs(Room current){

		List<Element> npcList = new ArrayList<>();

		for(NPC currNpc : current.getNpcs()){
			Element npc = new Element("Npc");
			if(currNpc != null){
				npc.addContent(new Element("Npctype").setText(currNpc.toString()));
				npc.addContent(new Element("Type").setText(currNpc.getType()));
				npc.addContent(new Element("Id").setText(String.valueOf(currNpc.getId())));
				npc.addContent(new Element("Row").setText(String.valueOf(currNpc.getRoomCoords().x)));
				npc.addContent(new Element("Col").setText(String.valueOf(currNpc.getRoomCoords().y)));
				npc.addContent(new Element("Damage").setText(String.valueOf(currNpc.getDamage())));
				npc.addContent(new Element("Speed").setText(String.valueOf(currNpc.getSpeed())));
				npcList.add(npc);
			}
		}
		return npcList;
	}

	/**
	 * creates a list of the Item elements to be saved to the file for the given room
	 * @param current room that's being saved
	 * @return the List of elements to be saved
	 */
	public static List<Element> saveItems(Room current){

		List<Element> itemList = new ArrayList<>();

		for(int i=0; i<current.getItems().length; i++){
			for(int j=0; j<current.getItems()[0].length; j++){
				Item currItem = current.getItems()[i][j];
				Element item = new Element("Item");
				if(currItem != null){
					item.addContent(new Element("Itemtype").setText(currItem.toString()));
					item.addContent(new Element("Type").setText(currItem.getType()));
					item.addContent(new Element("Id").setText(String.valueOf(currItem.getId())));
					item.addContent(new Element("Row").setText(String.valueOf(currItem.getCoords().x)));
					item.addContent(new Element("Col").setText(String.valueOf(currItem.getCoords().y)));
					item.addContent(new Element("Health").setText(String.valueOf(currItem.getHealth())));
					item.addContent(new Element("Score").setText(String.valueOf(currItem.getScore())));
					itemList.add(item);
				}
			}
		}
		return itemList;
	}

	/**
	 * Saves the current player to an xml file that can then be loaded in the future
	 */
	public static void savePlayer(){
		Player player = DestinysWild.getPlayer();

		try{
			Element playerTag = new Element("Player"); //Root element name
			Document doc = new Document(playerTag);

			playerTag.addContent(new Element("Name").setText(player.getName()));
			playerTag.addContent(new Element("Posx").setText(String.valueOf(player.getCoords().x)));
			playerTag.addContent(new Element("Posy").setText(String.valueOf(player.getCoords().y)));
			playerTag.addContent(new Element("Health").setText(String.valueOf(player.getHealth())));
			playerTag.addContent(new Element("Currentroom").setText(String.valueOf(player.getCurrentRoom().getId())));
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
			System.out.println(e.getMessage() + " <--- ERROR");
		}
	}


}
