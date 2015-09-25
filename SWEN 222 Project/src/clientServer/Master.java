package clientServer;

import java.net.Socket;

import game.Board;

public final class Master extends Thread {

	private final Board board;
	private final int broadcastClock;
	private final int playerID;
	private final Socket socket;

	public Master(Socket socket, int playerID, int broadcastClock, Board board) {
		this.board = board;
		this.broadcastClock = broadcastClock;
		this.socket = socket;
		this.playerID = playerID;
	}

}
