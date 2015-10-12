package clientServer.packets;

import game.Room;
import game.Tile;

import java.awt.Point;

import clientServer.GameClient;
import clientServer.GameServer;

public class EnemyPacket extends Packet {

	private Point realCoords;
	private int realCoordsX;
	private int realCoordsY;
	private Room currentRoom;
	private int currentRoomID;
	private int damage;
	private int health;
	private int id;

	public EnemyPacket(byte[] data) {
		super(06);
		String[] dataArray = readData(data).split(",");
		this.realCoordsX = Integer.parseInt(dataArray[0]);
		this.realCoordsY = Integer.parseInt(dataArray[1]);
		this.currentRoomID = Integer.parseInt(dataArray[2]);
		this.health = Integer.parseInt(dataArray[3]);
		this.id = Integer.parseInt(dataArray[4]);
	}

	public EnemyPacket(int realCoordsX, int realCoordsY, int currentRoomID,
			int health, int id) {
		super(06);
		this.realCoordsX = realCoordsX;
		this.realCoordsY = realCoordsY;
		this.currentRoomID = currentRoomID;
		this.health = health;
		this.id = id;
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());

	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());

	}

	@Override
	public byte[] getData() {
		return ("06" + this.getRealCoordsX() + "," + this.getRealCoordsY()
				+ "," + this.getCurrentRoomID() + "," + this.getHealth() + "," + this
					.getID()).getBytes();
	}

	public int getRealCoordsX() {
		return this.realCoordsX;
	}

	public int getRealCoordsY() {
		return this.realCoordsY;
	}

	public int getCurrentRoomID() {
		return this.currentRoomID;
	}

	public int getHealth() {
		return this.health;
	}

	public int getID() {
		return this.id;
	}

}
