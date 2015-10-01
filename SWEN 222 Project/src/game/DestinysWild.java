package game;

import game.items.Health;
import game.items.Item;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.SwingUtilities;

import view.GameInterface;
import clientServer.Multiplayer;
import clientServer.packets.DisconnectPacket;

public class DestinysWild{
	private static Board board;
	private static Player currentPlayer;
	private GameInterface ui;
	private boolean running = true;
	private DestinysWild game = this;
	private CountDownLatch latch;
	private Multiplayer multiplayer = null;
	private boolean paused = false;

	public DestinysWild() {
		//board = XMLParser.initialiseBoard("data/board.xml");
		//initialiseTestPlayer();
		//XMLParser.loadPlayer(new File("data/savegames/Robbie.xml"));
		testLoadGame();
		multiplayer  = new Multiplayer(this,board,currentPlayer);
		multiplayer.start();
		latch = new CountDownLatch(1);
		setUpUI();
		gameLoop();
	}

	public void setUpUI(){
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		        ui = new GameInterface(currentPlayer, game, board, latch);
		    }
		});
	}

	public void disconnect(){
		DisconnectPacket disconnect = new DisconnectPacket(currentPlayer.getName());
		disconnect.writeData(multiplayer.getClient());
		XMLParser.saveGame();
	}

	public void gameLoop(){
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running){
			while(!paused){
				long now = System.nanoTime();
			    delta += (now - lastTime) / ns;
			    lastTime = now;
			    while(delta >= 1) {
				    updateGame();
				    delta--;
			    }
			    if(running){
			    	//updateUI();
			    }
			    frames++;
			    if(System.currentTimeMillis() - timer > 1000) {
				    timer += 1000;
				    System.out.println("FPS: " + frames);
				    frames = 0;
			    }
			}
		}
		//stop();
	}

	public void updateGame(){
		currentPlayer.updatePlayer();
	}

	public void updateUI(){
		ui.updateUI();
	}

	public void setPaused(boolean paused){
		this.paused = paused;
	}

	public void testSaveGame(){
		XMLParser.saveGame();
	}

	public static void initialiseTestPlayer(){
		List<Item> inv = new ArrayList<>();
		inv.add(new Health("apple", new Point(5, 5), 10, 555)); // item 1, room 0
		inv.add(new Health("apple", new Point(5, 5), 10, 555));
		inv.add(new Health("apple", new Point(5, 5), 10, 555));
		inv.add(new Health("apple", new Point(5, 5), 10, 555));
		System.out.println("Inv size: " + inv.size());

		List<Room> roomsV = new ArrayList<>();
		//Room room = new Room(-1, -1, -1, -1, 0, new Point(2,2));
		Room room = board.getBoard()[2][2];
		roomsV.add(room);

		currentPlayer = new Player("Robbieg", new Point(500, 300), 99, room, roomsV, inv, 9999);
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