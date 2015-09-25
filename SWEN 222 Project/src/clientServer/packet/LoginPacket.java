package clientServer.packet;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeData(GameServer server) {
		// TODO Auto-generated method stub
		
	}

}
