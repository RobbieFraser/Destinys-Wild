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
				initialiseOnBoardItems(room, currentRoom);

				System.out.println("Loading room with ID: " + id);
				board.addRoom(currentRoom, roomRow, roomCol);
			}
			initialiseOffBoardItems(rootNode);


		}
		catch (FileNotFoundException e){
			if (filename.equals("data/savestate.xml")){
				System.out.println("No current save state found. Loading original board");
				return initialiseBoard("data/board.xml");
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

			int damage = Integer.valueOf(npc.getChildText("Damage"));
			int speed = Integer.valueOf(npc.getChildText("Speed")); //------

			NPC temp = null;
			Point coords = new Point(npcRow, npcCol);

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
			currentRoom.addNpcs(temp, npcRow, npcCol); //adds all npc's to the current room
		}
	}

	private static void initialiseOnBoardItems(Element room, Room currentRoom) {
		if (room.getChild("Items") == null) {
			return;
		}
		
		List<Element> items = room.getChildren("Items").get(0).getChildren("Item");
		
		initialiseItems(items, currentRoom);

	}
	
	public static void initialiseOffBoardItems(Element rootNode){
		if(rootNode.getChild("Offboarditems") == null){
			System.out.println("No offboard items found");
			return;
		}
		
		List<Element> items = rootNode.getChildren("Items").get(0).getChildren("Item");
		
		initialiseItems(items, null);
		
		
	}
	
	public static void initialiseItems(List<Element> itemList, Room currentRoom){
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
				currentRoom.addItems(temp, itemRow, itemCol); //adds all items to the current room
			}
			else{
				DestinysWild.getBoard().getOffBoardItems().add(temp); //adds items to off board list
			}
		}
	}

	/**
	 * Loads save states for the current game
	 */
	public static void loadGame(File playerFile){
		System.out.println("Loading board from savestate.xml");
		DestinysWild.setBoard(initialiseBoard("data/savestate.xml"));
		DestinysWild.getBoard().printBoard();
		System.out.println("Board Loaded");
		System.out.println("Loading player file from " + playerFile.getName());
		loadPlayer(playerFile);
		System.out.println("Player Loaded");
	}

	public static void loadPlayer(File playerFile){
		try{
			SAXBuilder builder = new SAXBuilder();
			Document document = (Document) builder.build(playerFile);
			Element playerTag = document.getRootElement();
			
			Board board = DestinysWild.getBoard();
			
			Player player = new Player();
			
			String name = playerTag.getChildText("Name"); //Main Player parameters--------------------
			
			System.out.println("1st");
			
			int playerx = Integer.valueOf(playerTag.getChildText("Posx"));
			int playery = Integer.valueOf(playerTag.getChildText("Posy"));
			
			int health = Integer.valueOf(playerTag.getChildText("Health"));
			int score = Integer.valueOf(playerTag.getChildText("Score"));
			int speed = Integer.valueOf(playerTag.getChildText("Speed"));
			
			System.out.println("2nd");
			int currentRoomId = Integer.valueOf(playerTag.getChildText("Currentroom"));
			System.out.println("Room Id: "+currentRoomId);
			Room currentRoom = board.getRoomFromId(currentRoomId);//-----------
			
			System.out.println("3rd");
			
			List<Element> inventory = playerTag.getChild("Inventory").getChildren("Itemid");
			if(!DestinysWild.getBoard().getOffBoardItems().isEmpty()){
				for(Element invItem : inventory){
					int itemId = Integer.valueOf(invItem.getChildText("Itemid"));
					Item tempItem = board.getOffBoardItems().get(itemId); //Depends on initialised board from savestate.xml
					player.addInventoryItem(tempItem);
				}
			}
			
			System.out.println("4th");
			
			List<Element> visitedRooms = playerTag.getChild("Visitedrooms").getChildren("Roomid");
			
			for(Element room : visitedRooms){
				int roomId = Integer.valueOf(room.getText());
				player.addRoom(board.getRoomFromId(roomId));

			}
			
			System.out.println("5th");
			
			player.setName(name);
			player.setCoords(new Point(playerx, playery));
			player.setHealth(health);
			player.setCurrentRoom(currentRoom);
			player.setScore(score);
			player.setSpeed(speed);
			
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
		String SAVE_FILE = "savestate.xml"; //This is the filename (NOT FILE PATH) that will be used and overwritten each time the board is saved
		System.out.println("Saving player");
		savePlayer(); //save the player info separately
		System.out.println("Player saved");
		System.out.println("Saving board state to " + SAVE_FILE);
		saveBoard(SAVE_FILE);
	}
	
	public static void saveBoard(String filename){
		Board board = DestinysWild.getBoard();
		
		
		try{
			Element boardTag = new Element("Board"); //Root element name
			Document doc = new Document(boardTag);
			
			Element roomTags = new Element("Room");//<Board><Room>
			Element obsTags = new Element("Obstacles");//<Board><Room><Obstacles>
			Element npcTags = new Element("Npcs");//<Board><Room><Npcs>
			Element itemTags = new Element("Items");//<Board><Room><Items>
			
			Room[][] roomArray = board.getBoard();
			
			for(int i=0; i<board.getBoard().length; i++){
				for(int j=0; j<board.getBoard()[0].length; j++){
					Room current = roomArray[i][j];
					roomTags.addContent(new Element("Id").setText(String.valueOf(current.getId())));
					roomTags.addContent(new Element("Row").setText(String.valueOf(current.getBoardPos().x)));
					roomTags.addContent(new Element("Col").setText(String.valueOf(current.getBoardPos().y)));
					roomTags.addContent(new Element("North").setText(String.valueOf(current.getNorth())));
					roomTags.addContent(new Element("East").setText(String.valueOf(current.getEast())));
					roomTags.addContent(new Element("South").setText(String.valueOf(current.getSouth())));
					roomTags.addContent(new Element("West").setText(String.valueOf(current.getWest())));
					roomTags.addContent(saveObstacles(current, obsTags));
					roomTags.addContent(saveNpcs(current, npcTags));
					roomTags.addContent(saveItems(current, itemTags));
					
					boardTag.addContent(roomTags);
				}
			}
			
			Element offItems = new Element("Offitems");
			for(Item item : board.getOffBoardItems()){
				offItems.addContent(new Element("Itemid").setText(String.valueOf(item.getId())));
			}
			boardTag.addContent(offItems);
			
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter("data/savegames/" + filename));
			
		}
		catch(Exception e){
			System.out.println(e.getMessage() + " <--- ERROR");
		}
	}
	
	public static Element saveObstacles(Room current, Element obsTag){
		Element obstacle = new Element("Obstacle");
		
		for(int i=0; i<current.getObstacles().length; i++){
			for(int j=0; j<current.getObstacles()[0].length; j++){
				Obstacle currObs = current.getObstacles()[i][j];
				if(currObs != null){
					obstacle.addContent(new Element("Obstype").setText(currObs.toString()));
					obstacle.addContent(new Element("Type").setText(currObs.getType()));
					obstacle.addContent(new Element("Row").setText(String.valueOf(currObs.getCoords().x)));
					obstacle.addContent(new Element("Col").setText(String.valueOf(currObs.getCoords().y)));
				}
			}
		}
		obsTag.addContent(obstacle);
		return obsTag;
	}
	
	public static Element saveNpcs(Room current, Element npcTag){
		Element npc = new Element("Npc");
		
		for(int i=0; i<current.getNpcs().length; i++){
			for(int j=0; j<current.getNpcs()[0].length; j++){
				NPC currNpc = current.getNpcs()[i][j];
				if(currNpc != null){
					npc.addContent(new Element("Npctype").setText(currNpc.toString()));
					npc.addContent(new Element("Type").setText(currNpc.getType()));
					npc.addContent(new Element("Row").setText(String.valueOf(currNpc.getCoords().x)));
					npc.addContent(new Element("Col").setText(String.valueOf(currNpc.getCoords().y)));
					npc.addContent(new Element("Damage").setText(String.valueOf(currNpc.getDamage())));
					npc.addContent(new Element("Speed").setText(String.valueOf(currNpc.getSpeed())));
				}
			}
		}
		npcTag.addContent(npc);
		return npcTag;
	}
	
	public static Element saveItems(Room current, Element itemTag){
		Element item = new Element("Item");
		
		for(int i=0; i<current.getItems().length; i++){
			for(int j=0; j<current.getItems()[0].length; j++){
				Item currItem = current.getItems()[i][j];
				if(currItem != null){
					item.addContent(new Element("Itemtype").setText(currItem.toString()));
					item.addContent(new Element("Type").setText(currItem.getType()));
					item.addContent(new Element("Id").setText(String.valueOf(currItem.getId())));
					item.addContent(new Element("Row").setText(String.valueOf(currItem.getCoords().x)));
					item.addContent(new Element("Col").setText(String.valueOf(currItem.getCoords().y)));
					item.addContent(new Element("Health").setText(String.valueOf(currItem.getHealth())));
					item.addContent(new Element("Speed").setText(String.valueOf(currItem.getScore())));
				}
			}
		}
		itemTag.addContent(item);
		return itemTag;
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
			playerTag.addContent(new Element("Currentroom").setText(String.valueOf(player.getCurrentRoom().getId())));
			playerTag.addContent(new Element("Score").setText(String.valueOf(player.getScore())));
			playerTag.addContent(new Element("Speed").setText(String.valueOf(player.getSpeed())));

			Element visitedRooms = new Element("Visitedrooms");

			for(Room roomId : player.getVisitedRooms()){
				visitedRooms.addContent(new Element("Roomid").setText(String.valueOf(roomId.getId())));
			}

			playerTag.addContent(visitedRooms);

			Element inventory = new Element("Inventory");
			
			System.out.println("inv size: " + player.getInventory().size()); //TODO this is reporting 0 instead of 1

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
