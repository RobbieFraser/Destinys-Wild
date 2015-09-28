package game;

import java.awt.Canvas;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import clientServer.GameClient;
import clientServer.GameServer;
import clientServer.packets.LoginPacket;
import game.items.Health;
import game.items.Item;

public class DestinysWild extends Canvas implements Runnable{
	private static Board board;
	private static Player currentPlayer;
	private GameClient client;
	private GameServer server;
	private boolean running = false;
	public int tickCount = 0;
	private Thread thread;

	public DestinysWild() {
		//testPlayerSave();
		//start();
		//testBoardInitialisation();
		testGameLoad();
		testSaveGame();
		//TestFrame test = new TestFrame(board);

	}

	public void testSaveGame(){
		XMLParser.saveGame();
	}
	
	public void testGameLoad(){
		XMLParser.loadGame(new File("data/savegames/Robbie.xml"));
		System.out.println("Parser loadGame good");
		System.out.println("Name: " + getPlayer().getName());
		System.out.println("Score: " + getPlayer().getScore());
	}
	
	public void testPlayerSave(){
		List<Item> inv = new ArrayList<>();
		inv.add(new Health("apple", new Point(5, 5), 10, 555)); // item 1, room 0

		List<Room> roomsV = new ArrayList<>();
		Room room = new Room(-1, -1, -1, -1, 9, new Point(3,3));
		roomsV.add(room);

		currentPlayer = new Player("Robbie", new Point(50, 50), 99, room, roomsV, inv, 9999);
		XMLParser.saveGame();
	}

	public void testBoardInitialisation(){
		board = XMLParser.initialiseBoard("data/board.xml");

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

	public void initialise() {
		board = XMLParser.initialiseBoard("data/board.xml");
		Room room = new Room(-1, -1, -1, -1, 9, new Point(3,3));
		PlayerMulti currentPlayer = new PlayerMulti(JOptionPane.showInputDialog(null, "Please enter a username"), new Point(50, 50),room,	 null,-1);
		System.out.println(currentPlayer.getName());
		board.getPlayers().add(currentPlayer);
		LoginPacket packet = new LoginPacket(currentPlayer.getName());
		if (client != null) {
			//client.sendData("ping".getBytes());
			if(server!=null){
				server.addConnection(currentPlayer, packet);
			}
			packet.writeData(client);
		}
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Destiny's Wild");
		if (JOptionPane.showConfirmDialog(this,
				"Do you want to start the server?") == 0) {
			server = new GameServer(board);
			server.start();
		}
		thread.start();
		client = new GameClient(board, "localhost");
		client.start();
	}

	public synchronized void stop() {
		running = false;
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;
		int ticks = 0;
		double diff = 0;
		long lastTimer = System.currentTimeMillis();
		initialise();
		while (running) {
			long now = System.nanoTime();
			diff += ((now - lastTime) / nsPerTick);
			lastTime = now;
			while (diff >= 1) {
				ticks++;
				tick();
				diff--;
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
			//	System.out.println("Ticks: " + ticks);
				ticks = 0;
			}
		}
	}

	private void tick() {
		tickCount++;
	}

	public static void main(String[] args){
		DestinysWild game = new DestinysWild();
	}
}
