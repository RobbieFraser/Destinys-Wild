package game;

import java.awt.Canvas;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import clientServer.GameClient;
import clientServer.GameServer;
import clientServer.packets.DisconnectPacket;
import clientServer.packets.LoginPacket;
import game.items.Health;
import game.items.Item;

public class DestinysWild extends Canvas implements Runnable{
	private static Board board;
	private static Player currentPlayer;
	private Thread thread;

	public DestinysWild() {
		board = XMLParser.initialiseBoard("data/board.xml");

	}

	public void testSaveGame(){
		XMLParser.saveGame();
	}
	
	public void initialiseTestPlayer(){
		List<Item> inv = new ArrayList<>();
		inv.add(new Health("apple", new Point(5, 5), 10, 555)); // item 1, room 0
		System.out.println("Inv size: " + inv.size());

		List<Room> roomsV = new ArrayList<>();
		Room room = new Room(-1, -1, -1, -1, 0, new Point(3,3));
		roomsV.add(room);

		currentPlayer = new Player("Robbie", new Point(50, 50), 99, room, roomsV, inv, 9999);
	}
	
	public void testLoadGame(){
		XMLParser.loadGame(new File("data/savegames/Robbie.xml"));
		System.out.println("Parser loadGame good");
		System.out.println("Name: " + getPlayer().getName());
		System.out.println("Score: " + getPlayer().getScore());
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
		game.testLoadGame();
		game.testSaveGame();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}