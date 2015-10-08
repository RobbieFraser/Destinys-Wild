package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class MovePacket extends Packet {
	private String userName;
	private int x;
	private int y;
	private int roomID;
	private int health;
	private int north;
	private int south;
	private int east;
	private int west;

	public MovePacket(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		this.userName = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);
		this.roomID = Integer.parseInt(dataArray[3]);
		this.health = Integer.parseInt(dataArray[4]);
		this.north = Integer.parseInt(dataArray[5]);
		this.east = Integer.parseInt(dataArray[6]);
		this.south = Integer.parseInt(dataArray[7]);
		this.west = Integer.parseInt(dataArray[8]);
	}

	public MovePacket(String userName, int x, int y, int roomID, int health,
			boolean north, boolean east, boolean south, boolean west) {
		super(02);
		this.userName = userName;
		this.x = x;
		this.y = y;
		this.roomID = roomID;
		this.health = health;
		this.north = boolToInt(north);
		this.west = boolToInt(west);
		this.east = boolToInt(east);
		this.south = boolToInt(south);
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

	public int getNorth() {
		return north;
	}

	public int getEast() {
		return east;
	}

	public int getSouth() {
		return south;
	}

	public int getWest() {
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

	public int boolToInt(boolean bool){
		int boolInt = bool ? 1 : 0;
		return boolInt;
	}

	public boolean intToBool(int i){
		if(i==1){
			return true;
		}
		else{
			return false;
		}
	}
}
