package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class HealthPacket extends Packet {

	private String userName;
	private int health;
	
	public HealthPacket(byte[] data){
		super(07);
		String[] dataArray = readData(data).split(",");
		this.userName = dataArray[0];
		this.health = Integer.parseInt(dataArray[1]);
	}
	
	public HealthPacket(String userName,int health){
		super(07);
		this.userName = userName;
		this.health = health;
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
		return("07" + this.getUserName() + "," + getHealth()).getBytes();
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public int getHealth(){
		return this.health;
	}
}
