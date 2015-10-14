package game;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import clientServer.Multiplayer;
import clientServer.packets.DisconnectPacket;
import clientServer.packets.EnemyPacket;
import clientServer.packets.HealthPacket;
import clientServer.packets.MovePacket;
import clientServer.packets.TimePacket;
import game.items.Health;
import game.items.Item;
import game.npcs.NPC;
import view.GameInterface;
import view.MenuInterface;

/**
 * Main game class. This is run in order to start the game. Firstly it opens the Menu interface which then
 * continues on appropriately.
 * @author Rob
 *
 */
public class DestinysWild implements Runnable {
	private static Board board;
	private static Player currentPlayer;
	private static GameInterface ui;
	private boolean running = true;
	private DestinysWild game = this;
	private CountDownLatch latch;
	private static Multiplayer multiplayer = null;
	private boolean paused;
	private MenuInterface mainMenu;
	private JFrame frame;
	public int tickCount = 0;
	private static boolean isTalking;
	private static String text;
	private static int talkCount = 150;
	private boolean prompted = false;
	private int promptCount = -1;

	/**
	 * Constructor
	 */
	public DestinysWild() {
		mainMenu = new MenuInterface(this);
	}

	/**
	 * Sets up the game according to whether this is a host or a client
	 * @param startServer whether to start the server or not
	 */
	public void setUpGame(boolean startServer) {
		multiplayer = new Multiplayer(this, board, currentPlayer);
		if (startServer) {
			multiplayer.startServer();
		} else {
			multiplayer.joinServer();
		}
		latch = new CountDownLatch(1);
		setUpUI();
		mainMenu.remove();
		Thread thread = new Thread(this);
		thread.start();
	}

	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	/**
	 * The list of instructions and comments that appear when a new player joins the game
	 */
	public void startUpPrompt() {
		if (promptCount == 0) {
			startTalking("Welcome to Destiny's Wild! Move around using WASD");
		} else if (promptCount == 1) {
			startTalking("Change view by using the LEFT or RIGHT arrow key");
		} else if (promptCount == 2) {
			startTalking("Navigate your inventory by using Q and E");
		} else if (promptCount == 3) {
			startTalking("Use a selected inventory item by pressing SHIFT");
		} else if (promptCount == 4) {
			startTalking("Interact with objects or attack enemies using SPACE");
		} else if (promptCount == 5) {
			startTalking("Press P to pause, and M to toggle the (awesome) music");
		} else if (promptCount == 6) {
			startTalking("And finally: Have fun, and good luck - you'll need it");
			prompted = true;
		}
	}

	/**
	 * Creates a new player for a new host game.
	 * @param playerName name of the player
	 * @param frame JFrame to be set
	 */
	public void newGame(String playerName, JFrame frame) {
		this.frame = frame;
		setBoard(XMLParser.initialiseBoard("data/board.xml"));
		setPlayer(new Player(playerName, new Point(500, 300),
				board.getRoomFromCoords(2, 2)));
		setUpGame(true);
		promptCount = 0;
	}

	/**
	 * Creates a new player for a new client which will join a host
	 * @param playerName name of the player
	 * @param frame JFrame to be set
	 */
	public void joinGame(String playerName, JFrame frame) {
		this.frame = frame;
		setBoard(XMLParser.initialiseBoard("data/board.xml"));
		setPlayer(new Player(playerName, new Point(500, 300),
				board.getRoomFromCoords(2, 2)));
		setUpGame(false);
		promptCount = 0;
	}

	/**
	 * sets up the game with an existing player file
	 * @param currentPlayerFile the file of the player to be loaded
	 * @param frame JFrame to be set
	 */
	public void loadGame(File currentPlayerFile, JFrame frame) {
		this.frame = frame;
		XMLParser.loadGame(currentPlayerFile);
		setUpGame(true);
		prompted = true;
	}

	/**
	 * initialises the user interface for the game
	 */
	public void setUpUI() {
		System.out.println("Setting up UI");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.out.println("Setting up UI - in run method");
				ui = new GameInterface(currentPlayer, game, board, latch);
			}
		});
	}

	/**
	 * disconnects a user from the server
	 */
	public void disconnect() {
		DisconnectPacket disconnect = new DisconnectPacket(
				currentPlayer.getName());
		disconnect.writeData(multiplayer.getClient());
		XMLParser.saveGame();
	}

	/**
	 * the main game loop. Used to restrict the game speed for each user to a standard rate
	 */
	public void gameLoop() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 30D;
		int ticks = 0;
		double diff = 0;
		long lastTimer = System.currentTimeMillis();
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
				ui.changeTime();
				ticks = 0;
			}
		}
	}

	/**
	 * Updates the game per each tick
	 */
	private void tick() {
		tickCount++;
		updateGame();
		// System.out.println("test");
		// for(Player player : board.getPlayers()){
		// player.updatePlayer();
		// }
		// currentPlayer.updatePlayer();
	}

	public static Multiplayer getMultiplayer() {
		return multiplayer;
	}

	/**
	 * Updates the game. Including talking count, sending multiplayer packets, and updating the player's info
	 */
	public void updateGame() {
		TimePacket timePacket = new TimePacket(getGameInterface()
				.getGameImagePanel().getTime());
		// System.out.println("TimePacket: " + timePacket);
		timePacket.writeData(multiplayer.getClient());
		updateTalking();
		if (!prompted) {
			startUpPrompt();
		}
		for (NPC enemy : currentPlayer.getCurrentRoom().getNpcs()) {
			enemy.tryMove();
			EnemyPacket enemyPacket = new EnemyPacket(enemy.getRealCoords().x,
					enemy.getRealCoords().y, enemy.getCurrentRoom().getId(),
					enemy.getHealth(), enemy.getId(), enemy.getRoomCoords().x,
					enemy.getRoomCoords().y);
			enemyPacket.writeData(multiplayer.getClient());
		}
		// currentPlayer.updatePlayer();
		for (Player player : board.getPlayers()) {
			player.updatePlayer();
		}
		HealthPacket healthPacket = new HealthPacket(currentPlayer.getName(),
				currentPlayer.getHealth());
		healthPacket.writeData(multiplayer.getClient());
	}

	/**
	 * calls to update the user interface
	 */
	public void updateUI() {
		ui.updateUI();
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	/**
	 * Test method that tests the save game function of XMLParser.java
	 */
	public void testSaveGame() {
		XMLParser.saveGame();
	}

	/**
	 * Test method that initialises a new player ready for use
	 */
	public static void initialiseTestPlayer() {
		List<Item> inv = new ArrayList<>();
		inv.add(new Health("apple", new Point(5, 5), 10, 555)); // item 1, room
																// 0
		inv.add(new Health("apple", new Point(5, 5), 10, 555));
		inv.add(new Health("apple", new Point(5, 5), 10, 555));
		inv.add(new Health("apple", new Point(5, 5), 10, 555));
		System.out.println("Inv size: " + inv.size());

		List<Room> roomsV = new ArrayList<>();
		// Room room = new Room(-1, -1, -1, -1, 0, new Point(2,2));
		Room room = board.getBoard()[2][2];
		roomsV.add(room);

		currentPlayer = new Player("Robbieg", new Point(500, 300), 99, room,
				roomsV, inv, 9999);
	}

	/**
	 * Test method that tests the load game function of XMLParser.java
	 */
	public void testLoadGame() {
		XMLParser.loadGame(new File("data/savegames/Robbie.xml"));
		System.out.println("Parser loadGame good");
		System.out.println("Name: " + getPlayer().getName());
		System.out.println("Score: " + getPlayer().getScore());
		System.out.println("Inventory size: "
				+ getPlayer().getInventory().size());
	}

	/**
	 * Test method that tests the save player function of XMLParser.java
	 */
	public void testPlayerSave() {
		XMLParser.savePlayer();
	}

	/**
	 * Test method that tests the initialisation of the board by printing out a text representation
	 */
	public void testBoardInitialisation() {
		board.printBoard();
		System.out.println();
		board.getBoard()[2][2].printRoom();
		System.out.println();
		board.getBoard()[1][2].printRoom();
	}

	public static Board getBoard() {
		return board;
	}

	public static Player getPlayer() {
		return currentPlayer;
	}

	public static void setBoard(Board b) {
		board = b;
	}

	/**
	 * Starts the talking process for the game to display text
	 * @param talk speech to be displayed
	 */
	public static void startTalking(String talk) {
		text = talk;
		isTalking = true;
	}

	/**
	 * updates the talking timer
	 */
	public void updateTalking() {
		if (isTalking) {
			talkCount--;
			if (talkCount == 0) {
				isTalking = false;
				talkCount = 120;
				if (!prompted) {
					promptCount++;
				}
			}
		}
	}

	public static boolean isTalking() {
		return isTalking;
	}

	public static String getText() {
		return text;
	}

	public static GameInterface getGameInterface() {
		return ui;
	}

	public static void setPlayer(Player player) {
		currentPlayer = player;
	}

	/**
	 * Initially calls the game loop. Implemented as part of the thread.
	 */
	public void run() {
		gameLoop();
	}

	/**
	 * main method to initialise the game
	 * @param args 
	 */
	public static void main(String[] args) {
		DestinysWild game = new DestinysWild();
	}
}
