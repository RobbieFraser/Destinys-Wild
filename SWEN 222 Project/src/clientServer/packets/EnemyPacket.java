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
	private int roomCoordsX;
	private int roomCoordsY;
	private Room currentRoom;
	private int currentRoomID;
	private int damage;
	private int health;
	private int id;
	
	/**
	 * Constructor for EnemyPacket when using an array of bytes.
	 * From the array, it obtains the co-ordinates, room ID, health and
	 * NPC ID.
	 * @param data
	 */
	public EnemyPacket(byte[] data) {
		super(06);
		String[] dataArray = readData(data).split(",");
		this.realCoordsX = Integer.parseInt(dataArray[0]);
		this.realCoordsY = Integer.parseInt(dataArray[1]);
		this.currentRoomID = Integer.parseInt(dataArray[2]);
		this.health = Integer.parseInt(dataArray[3]);
		this.id = Integer.parseInt(dataArray[4]);
		this.roomCoordsX = Integer.parseInt(dataArray[5]);
		this.roomCoordsY = Integer.parseInt(dataArray[6]);
	}

	/**
	 * Constructor for EnemyPacket when it is being passed an NPC's co-ordinates,
	 * current room, health and ID.
	 * @param realCoordsX
	 * @param realCoordsY
	 * @param currentRoomID
	 * @param health
	 * @param id
	 */
	public EnemyPacket(int realCoordsX, int realCoordsY, int currentRoomID,
			int health, int id, int roomCoordsX, int roomCoordsY) {
		super(06);
		this.realCoordsX = realCoordsX;
		this.realCoordsY = realCoordsY;
		this.currentRoomID = currentRoomID;
		this.health = health;
		this.id = id;
		this.roomCoordsX = roomCoordsX;
		this.roomCoordsY = roomCoordsY;
	}
	
	/**
	 * Writes data to the GameClient, sending it to the server
	 */
	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());

	}

	/**
	 * Writes data to the GameServer, which then sends it to all the clients connected
	 * to that server
	 */
	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());

	}

	/**
	 * Returns a byte array of the items contained in this packet
	 */
	@Override
	public byte[] getData() {
		return ("06" + this.getRealCoordsX() + "," + this.getRealCoordsY()
				+ "," + this.getCurrentRoomID() + "," + this.getHealth() + "," + this
					.getID() + "," + this.getRoomCoordsX() + "," + this.getRoomCoordsY()).getBytes();
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
	
	public int getRoomCoordsX(){
		return this.roomCoordsX;
	}
	
	public int getRoomCoordsY(){
		return this.roomCoordsY;
	}

}
