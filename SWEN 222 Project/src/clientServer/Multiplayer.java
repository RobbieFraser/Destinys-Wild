package clientServer;

import java.awt.Canvas;

import game.Board;
import game.DestinysWild;
import game.Player;
import game.PlayerMulti;

import javax.swing.JOptionPane;

import clientServer.packets.LoginPacket;

public class Multiplayer extends Canvas implements Runnable {
	private GameClient client;
	private GameServer server;
	private boolean running = false;
	private Board board;
	public int tickCount = 0;
	private DestinysWild game;

	public Multiplayer(DestinysWild game, Board board) {
		this.game = game;
		this.board = board;
	}

	public void initialise() {
		PlayerMulti player = (PlayerMulti) game.getPlayer();
		LoginPacket packet = new LoginPacket(player.getName());
		if (client != null) {
			// client.sendData("ping".getBytes());
			if (server != null) {
				server.addConnection(player, packet);
				System.out.println("Server trying to add connection");
			}
			packet.writeData(client);
		}
	}

	public synchronized void start() {
		running = true;
		client = new GameClient(board);
		if (JOptionPane.showConfirmDialog(this,
				"Do you want to start the server?") == 0) {
			server = new GameServer(board);
			server.start();
		}
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
				// System.out.println("Ticks: " + ticks);
				ticks = 0;
			}
		}
	}

	private void tick() {
		tickCount++;
	}
}
