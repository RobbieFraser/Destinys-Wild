package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class LoginPacket extends Packet{

	private String userName;

	/**
	 * Constructor for a LoginPacket that takes in an array of bytes
	 * and obtains the username of the relevant player from it
	 * @param data -> the data that is being passed in
	 */
	public LoginPacket(byte[] data) {
		super(00);
		this.userName = readData(data);
	}

	/**
	 *Constructor for a LoginPacket when it is passed a player's username
	 * @param userName -> username of the player logging in
	 */
	public LoginPacket(String userName) {
		super(00);
		this.userName = userName;
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
		return ("00" + this.userName).getBytes();
	}

	public String getUserName() {
		return userName;
	}

}
