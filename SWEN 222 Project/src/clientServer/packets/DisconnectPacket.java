package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class DisconnectPacket extends Packet {
private String userName;
	
	public DisconnectPacket(byte[] data) {
		super(01);
		this.userName = readData(data);
	}

	public DisconnectPacket(String userName) {
		super(01);
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
		return ("00" + this.userName).getBytes();
	}

	public String getUserName() {
		return userName;
	}
}
