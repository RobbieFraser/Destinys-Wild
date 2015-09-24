package game;

import game.npcs.EnemyStill;
import game.npcs.EnemyWalker;
import game.npcs.FriendlyStill;
import game.npcs.NPC;
import game.obstacles.Block;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class XMLParser {

	/**
	 * Initialises the board from the XML file
	 */
	public static Board initialiseBoard(String filename){
		
		Board board = new Board();

		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(filename);

        try{
    		Document document = (Document) builder.build(xmlFile);
    		Element rootNode = document.getRootElement();
    		List<Element> roomTags = rootNode.getChildren("Room");

    		for(Element e : roomTags) { //Initialising Rooms
    			int roomx = Integer.valueOf(e.getChildText("Posx")); //room parameters
    			int roomy = Integer.valueOf(e.getChildText("Posy"));

    			int id = Integer.valueOf(e.getChildText("Id"));

    			int north = Integer.valueOf(e.getChildText("North"));
    			int east = Integer.valueOf(e.getChildText("East"));
    			int south = Integer.valueOf(e.getChildText("South"));
    			int west = Integer.valueOf(e.getChildText("West")); //----------------
    			
    			Room currentRoom = new Room(north, east, south, west, id, new Point(roomx, roomy));

    			if(e.getChild("Obstacles") != null){
        			List<Element> obs = e.getChildren("Obstacles");
        			List<Element> obsTags = obs.get(0).getChildren("Obstacle");

        			for(Element el : obsTags){ //Initialising Obstacles
        				String type = el.getChildText("Type"); //-----------Obstacle parameters

        				int obsx = Integer.valueOf(el.getChildText("Posx"));
        				int obsy = Integer.valueOf(el.getChildText("Posy")); //----------------

        				currentRoom.addObstacle(new Block(type, new Point(obsx, obsy)), obsx, obsy); //adds each obstacle to the current room

        			}
    			}
    			
    			if(e.getChild("Npcs") != null){
        			List<Element> npc = e.getChildren("Npcs");
        			List<Element> npcTags = npc.get(0).getChildren("Npc");

        			for(Element ele : npcTags){ // Initialising NPC's
        				String npcType = ele.getChildText("Npctype");
        				
        				String type = ele.getChildText("Type"); // NPC parameters

           				int npcx = Integer.valueOf(ele.getChildText("Posx"));
        				int npcy = Integer.valueOf(ele.getChildText("Posy"));

        				int damage = Integer.valueOf(ele.getChildText("Damage"));
        				int speed = Integer.valueOf(ele.getChildText("Speed")); //---

        				NPC temp = null;

        				Point coords = new Point(npcx, npcy);

        				switch(npcType){
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

    			System.out.println("Loading room with ID: " + id);
    			board.addRoom(currentRoom, roomx, roomy);
    		}



        }
        catch(FileNotFoundException e){
        	if(filename.equals("data/state.xml")){
        		System.out.println("No current save state found. Loading original board");
        		initialiseBoard("data/board.xml");
        	}
        	System.out.println("wrong filepath");
        }
        catch(Exception e){
        	System.out.println(e.getMessage() + " <-- ERROR");
        }
        
        return board;

	}

	/**
	 * Loads save states for the current game
	 */
	public void loadState(){
		initialiseBoard("data/state.xml");
	}

	/**
	 * Saves the game states to an XML file
	 */
	public void saveState(){

	}


}
