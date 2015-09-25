package game;

import java.awt.Canvas;

import javax.swing.JOptionPane;

import clientServer.GameClient;
import clientServer.GameServer;
import Renderer.TestFrame;

public class DestinysWild extends Canvas implements Runnable{
	private Board board;
	private Player currentPlayer;
	private GameClient client;
	private GameServer server;
	private boolean running = false;
	public int tickCount = 0;

	public DestinysWild() {
		//read board in from file
//		board = XMLParser.initialiseBoard("data/state.xml");
//		
//		board.printBoard();
//		System.out.println();
//		board.getBoard()[2][2].printRoom();
//		System.out.println();
//		board.getBoard()[1][2].printRoom();
		//TestFrame test = new TestFrame(board);
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
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
			server = new GameServer(board);
			server.start();
		}
		client = new GameClient(board, "localhost");
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

	public static void main(String[] args){
		//DestinysWild game = new DestinysWild();
		new DestinysWild().start();
	}
}
