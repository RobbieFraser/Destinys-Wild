package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class MovePacket extends Packet {
	private String userName;
	private int x;
	private int y;
	private int roomID;
	private int health;
	private boolean north;
	private boolean south;
	private boolean east;
	private boolean west;

	public MovePacket(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		this.userName = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);
		this.roomID = Integer.parseInt(dataArray[3]);
		this.health = Integer.parseInt(dataArray[4]);
	}

	public MovePacket(String userName, int x, int y, int roomID, int health,
			boolean north, boolean east, boolean west, boolean south) {
		super(02);
		this.userName = userName;
		this.x = x;
		this.y = y;
		this.roomID = roomID;
		this.health = health;
		this.north = north;
		this.west = west;
		this.east = east;
		this.south = south;
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
		return ("02" + this.userName + "," + this.getX() + "," + this.getY()
				+ "," + this.getRoomID() + "," + this.getHealth() + ","
				+ this.getNorth() + "," + this.getEast() + ","
				+ this.getSouth() + "," + this.getWest()).getBytes();
	}

	public boolean getNorth() {
		return north;
	}

	public boolean getEast() {
		return east;
	}

	public boolean getSouth() {
		return south;
	}

	public boolean getWest() {
		return west;
	}

	public String getUserName() {
		return userName;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getRoomID() {
		return this.roomID;
	}

	public int getHealth() {
		return this.health;
	}
}
