package game;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import clientServer.Multiplayer;
import game.items.Health;
import game.items.Item;

public class DestinysWild{
	private static Board board;
	private static Player currentPlayer;

	public DestinysWild() {
		board = XMLParser.initialiseBoard("data/board.xml");
		initialiseTestPlayer();
		Multiplayer multiplayer  = new Multiplayer(this,board);
		multiplayer.start();
	}

	public void testSaveGame(){
		XMLParser.saveGame();
	}

	public static void initialiseTestPlayer(){
		List<Item> inv = new ArrayList<>();
		inv.add(new Health("apple", new Point(5, 5), 10, 555)); // item 1, room 0
		System.out.println("Inv size: " + inv.size());

		List<Room> roomsV = new ArrayList<>();
		Room room = new Room(-1, -1, -1, -1, 0, new Point(3,3));
		roomsV.add(room);

		currentPlayer = new Player("Robbieg", new Point(500, 500), 99, room, roomsV, inv, 9999);
	}

	public void testLoadGame(){
		XMLParser.loadGame(new File("data/savegames/Robbie.xml"));
		System.out.println("Parser loadGame good");
		System.out.println("Name: " + getPlayer().getName());
		System.out.println("Score: " + getPlayer().getScore());
		System.out.println("Tile's real coords: " + getPlayer().calcTile().getRealCoords().getX() + ", " + getPlayer().calcTile().getRealCoords().getY());
		System.out.println("Tile's room coords: " + getPlayer().calcTile().getRoomCoords().getX() + ", " + getPlayer().calcTile().getRoomCoords().getY());
		System.out.println("Inventory size: " + getPlayer().getInventory().size());
	}

	public void testPlayerSave(){
		XMLParser.savePlayer();
	}

	public void testBoardInitialisation(){
		board.printBoard();
		System.out.println();
		board.getBoard()[2][2].printRoom();
		System.out.println();
		board.getBoard()[1][2].printRoom();
	}

	public static Board getBoard() {
		return board;
	}

	public static Player getPlayer(){
		return currentPlayer;
	}

	public static void setBoard(Board b) {
		board = b;
	}

	public static void setPlayer(Player player){
		currentPlayer = player;
	}

	public static void main(String[] args){
		DestinysWild game = new DestinysWild();
		//game.testLoadGame();
		//game.testSaveGame();
	}
}