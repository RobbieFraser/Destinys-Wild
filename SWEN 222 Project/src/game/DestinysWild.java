package game;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import clientServer.Multiplayer;
import clientServer.packets.BoardPacket;
import clientServer.packets.DisconnectPacket;
import clientServer.packets.MovePacket;
import game.items.Health;
import game.items.Item;
import game.npcs.EnemyWalker;
import game.npcs.NPC;
import view.GameInterface;
import view.MenuInterface;

public class DestinysWild implements Runnable {
	private static Board board;
	private static Player currentPlayer;
	private GameInterface ui;
	private boolean running = true;
	private DestinysWild game = this;
	private CountDownLatch latch;
	private Multiplayer multiplayer = null;
	private boolean paused;
	private MenuInterface mainMenu;
	private JFrame frame;
	public int tickCount = 0;

	public DestinysWild() {
		mainMenu = new MenuInterface(this);
		// mainMenu = new MenuInterface(this);
		// newGame("guy");
		// loadGame(new File("data/savegames/Robbie.xml"));
		// board = XMLParser.initialiseBoard("data/board.xml");
		// initialiseTestPlayer();
	}

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

	public void newGame(String playerName, JFrame frame) {
		this.frame = frame;
		setBoard(XMLParser.initialiseBoard("data/test.xml"));
		setPlayer(new Player(playerName, new Point(500, 300),
				board.getRoomFromCoords(2, 2)));
		setUpGame(true);
	}

	public void joinGame(String playerName, JFrame frame) {
		this.frame = frame;
		setBoard(XMLParser.initialiseBoard("data/test.xml"));
		setPlayer(new Player(playerName, new Point(500, 300),
				board.getRoomFromCoords(2, 2)));
		setUpGame(false);
	}

	public void loadGame(File currentPlayerFile, JFrame frame) {
		this.frame = frame;
		XMLParser.loadGame(currentPlayerFile);
		setUpGame(true);
	}

	public void setUpUI() {
		System.out.println("Setting up UI");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.out.println("Setting up UI - in run method");
				ui = new GameInterface(currentPlayer, game, board, latch);
			}
		});
		// ui.setInterface(currentPlayer, game, board, latch);
	}

	public void disconnect() {
		DisconnectPacket disconnect = new DisconnectPacket(
				currentPlayer.getName());
		disconnect.writeData(multiplayer.getClient());
		XMLParser.saveGame();
	}

	public void gameLoop() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("Running");
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 30D;
		int ticks = 0;
		double diff = 0;
		long lastTimer = System.currentTimeMillis();
		while (running) {
			// if(!paused){
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
				// System.out.println("Ticks: " + ticks);
				ticks = 0;
			}
			// }
		}
	}

	private void tick() {
		tickCount++;
		updateGame();
	}

	public void updateGame() {
		//currentPlayer.updatePlayer();
		for (NPC enemy : currentPlayer.getCurrentRoom().getNpcs()) {
			if (enemy instanceof EnemyWalker) {
				enemy.tryMove();
			}
		}
		MovePacket movePacket = new MovePacket(currentPlayer.getName(),
				currentPlayer.getCoords().x, currentPlayer.getCoords().y,
				currentPlayer.getCurrentRoom().getId(),
				currentPlayer.getHealth(), currentPlayer.isNorth(),
				currentPlayer.isEast(), currentPlayer.isSouth(),
				currentPlayer.isWest(),currentPlayer.getWalkState());
		movePacket.writeData(multiplayer.getClient());
		byte[] currentBoard = null;
		try {
			currentBoard = convertToBytes(currentPlayer.getCurrentRoom());
			// System.out.println("Byte array length is: " +
			// currentBoard.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BoardPacket boardPacket = new BoardPacket(currentBoard);
		boardPacket.writeData(multiplayer.getClient());
	}

	public byte[] convertToBytes(Object object) throws IOException {
		byte[] bytes = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			oos.flush();
			bytes = bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (oos != null) {
				oos.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
		return bytes;
	}

	private Object convertFromBytes(byte[] bytes) throws IOException,
			ClassNotFoundException {
		Object obj = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (ois != null) {
				ois.close();
			}
		}
		return obj;
	}

	public void updateUI() {
		ui.updateUI();
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void testSaveGame() {
		XMLParser.saveGame();
	}

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

	public void testLoadGame() {
		XMLParser.loadGame(new File("data/savegames/Robbie.xml"));
		System.out.println("Parser loadGame good");
		System.out.println("Name: " + getPlayer().getName());
		System.out.println("Score: " + getPlayer().getScore());
		// System.out.println("Tile's real coords: " +
		// getPlayer().calcTile().getRealCoords().getX() + ", " +
		// getPlayer().calcTile().getRealCoords().getY());
		// System.out.println("Tile's room coords: " +
		// getPlayer().calcTile().getRoomCoords().getX() + ", " +
		// getPlayer().calcTile().getRoomCoords().getY());
		System.out.println("Inventory size: "
				+ getPlayer().getInventory().size());
	}

	public void testPlayerSave() {
		XMLParser.savePlayer();
	}

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

	public static void setPlayer(Player player) {
		currentPlayer = player;
	}

	@Override
	public void run() {
		gameLoop();
	}

	public static void main(String[] args) {
		DestinysWild game = new DestinysWild();
		// game.testLoadGame();
		// game.testSaveGame();
	}
}
