package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class MovePacket extends Packet {
private String userName;
private int x;
private int y;
private int roomID;

	public MovePacket(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		this.userName = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);
		this.roomID = Integer.parseInt(dataArray[3]);
	}

	public MovePacket(String userName, int x, int y, int roomID) {
		super(02);
		this.userName = userName;
		this.x = x;
		this.y = y;
		this.roomID = roomID;
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
		return ("02" + this.userName + "," + this.getX() + "," + this.getY() + "," + this.getRoomID()).getBytes();
	}

	public String getUserName() {
		return userName;
	}

	public int getX(){
		return this.x;
	}

	public int getY(){
		return this.y;
	}

	public int getRoomID(){
		return this.roomID;
	}
}
