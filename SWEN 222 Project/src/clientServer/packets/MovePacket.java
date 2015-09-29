package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class MovePacket extends Packet{
private String userName;
	
	public MovePacket(byte[] data) {
		super(02);
		this.userName = readData(data);
	}

	public MovePacket(String userName) {
		super(02);
		this.userName = userName;
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
		return ("02" + this.userName).getBytes();
	}

	public String getUserName() {
		return userName;
	}
}
