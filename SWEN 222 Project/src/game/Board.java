package game;

import java.awt.Canvas;

import javax.swing.JOptionPane;

import clientServer.GameClient;
import clientServer.GameServer;

public class Board extends Canvas implements Runnable {

	private Room[][] board = new Room[5][5];
	private GameClient client;
	private GameServer server;
	private boolean running = false;
	public int tickCount = 0;

	public Board() {

	}

	/**
	 * @return the board
	 */
	public Room[][] getBoard() {
		return board;
	}

	public void addRoom(Room room, int x, int y) {
		board[x][y] = room;
	}

	/**
	 * This method should provide a basic, text based look at the board in its
	 * current state.
	 */
	public void printBoard() {
		for (int i = 0; i < board.length; i++) {
			System.out.print("| ");
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == null) {
					System.out.print("null | ");
				} else {
					System.out.print(board[i][j].getID() + " | ");
				}
			}
			System.out.println();
		}
	}

	public void initialise() {
		if (client != null) {
			client.sendData("ping".getBytes());
		}
	}

	public synchronized void start() {
		running = true;
		if (JOptionPane.showConfirmDialog(this,
				"Do you want to start the server?") == 0) {
			server = new GameServer(this);
			server.start();
		}
		client = new GameClient(this, "localhost");
		client.start();
		new Thread(this).start();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				System.out.println("Ticks: " + ticks);
				ticks = 0;
			}
		}
	}

	private void tick() {
		tickCount++;
	}

	public static void main(String[] args) {
		new Board().start();
	}

}
