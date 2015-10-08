package clientServer;

import game.Board;
import game.DestinysWild;
import game.Player;
import game.Room;

import java.awt.Canvas;
import java.awt.HeadlessException;
import java.awt.Point;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import clientServer.packets.LoginPacket;

public class Multiplayer extends Canvas implements Runnable {
	private GameClient client;
	private GameServer server;
	private boolean running = false;
	private Board board;
	public int tickCount = 0;
	private DestinysWild game;
	private Player currentPlayer;
	private Thread thread;

	public Multiplayer(DestinysWild game, Board board, Player currentPlayer) {
		this.game = game;
		this.board = board;
		this.currentPlayer = currentPlayer;
		board.addPlayers(currentPlayer);
		System.out.println("Current Player is: " + currentPlayer.getName());
	}

	public void initialise() {
		Room room = new Room(-1, -1, -1, -1, 0, new Point(3,3));
//		PlayerMulti player = new PlayerMulti(JOptionPane.showInputDialog(null, "Please enter a username"),
//					 new Point(500,300), room, null, -1);
		//PlayerMulti player = new PlayerMulti(currentPlayer.getName(),currentPlayer.getCoords(),currentPlayer.getCurrentRoom(),null,-1);
		
		LoginPacket packet = new LoginPacket(currentPlayer.getName());
		if (client != null) {
			// client.sendData("ping".getBytes());
			if (server != null) {
				server.addConnection(currentPlayer, packet);
				System.out.println("Server trying to add connection");
			}
			packet.writeData(client);
		}
	}

	public synchronized void start(){
//		running = true;
//		//thread = new Thread(this, "Multiplayer");
//		if (JOptionPane.showConfirmDialog(this,
//				"Do you want to start the server?") == 0) {
//			server = new GameServer(board,this);
//			server.start();
//			try {
//				JOptionPane.showMessageDialog(null, "The server's IP address is: " + InetAddress.getLocalHost().getHostAddress());
//			} catch (HeadlessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		if(server==null){
//		String ipAddress =	JOptionPane.showInputDialog(null,"Enter the server's IP Address");
//		client = new GameClient(board,ipAddress,this);
//			}
//		else{
//			client = new GameClient(board,null,this);
//		}
//		client.start();
//		initialise();
//		//thread.start();
	}
	
	public synchronized void startServer() {
		running = true;
		server = new GameServer(board,this);
		server.start();
		try {
			JOptionPane.showMessageDialog(null, "The server's IP address is: " + InetAddress.getLocalHost().getHostAddress());
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client = new GameClient(board,null,this);
		client.start();
		initialise();
	}
	
	public synchronized void joinServer() {
		running = true;
		String ipAddress = JOptionPane.showInputDialog(null,"Enter the server's IP Address");
		if (ipAddress.equals("")) {
			JOptionPane.showMessageDialog(null, "No IP Address was entered, so a regular game was created.");
		}
		client = new GameClient(board,ipAddress,this);
		client.start();
		initialise();

	}

	public synchronized void stop() {
		running = false;
	}
	
	public Player getCurrentPlayer(){
		return currentPlayer;
	}

	public GameClient getClient(){
		return client;
	}

	public GameServer getServer(){
		return server;
	}

	@Override
	public void run() {
		//System.out.println("Running");
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
				//System.out.println("Ticks: " + ticks);
				ticks = 0;
			}
		}
	}

	private void tick() {
		tickCount++;
		//game.updateGame();
	}
}
