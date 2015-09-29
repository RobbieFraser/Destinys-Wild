package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class InteractionPacket extends Packet {
private String userName;
	
	public InteractionPacket(byte[] data) {
		super(03);
		this.userName = readData(data);
	}

	public InteractionPacket(String userName) {
		super(03);
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
		return ("03" + this.userName).getBytes();
	}

	public String getUserName() {
		return userName;
	}
}
