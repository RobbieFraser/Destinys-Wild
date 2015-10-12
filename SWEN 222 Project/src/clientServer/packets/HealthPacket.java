package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class HealthPacket extends Packet {

	private String userName;
	private int health;
	
	/**
	 * Constructor for a HealthPacket when passed an array of bytes.
	 * From the array, it obtains the player's username and their health
	 * @param data
	 */
	public HealthPacket(byte[] data){
		super(07);
		String[] dataArray = readData(data).split(",");
		this.userName = dataArray[0];
		this.health = Integer.parseInt(dataArray[1]);
	}
	
	/**
	 * Constructor for a HealthPacket when passed a player's username
	 * and their health
	 * @param userName
	 * @param health
	 */
	public HealthPacket(String userName,int health){
		super(07);
		this.userName = userName;
		this.health = health;
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
		return("07" + this.getUserName() + "," + getHealth()).getBytes();
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public int getHealth(){
		return this.health;
	}
}
