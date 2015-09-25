package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class LoginPacket extends Packet{

	private String userName;
	
	public LoginPacket(byte[] data) {
		super(00);
		this.userName = readData(data);
	}

	public LoginPacket(String userName) {
		super(00);
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
