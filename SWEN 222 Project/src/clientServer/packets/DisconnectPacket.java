package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class DisconnectPacket extends Packet {
private String userName;

	/**
	 * Constructor for DisconnectPacket when it is being passed a byte array. Obtains the
	 * username of the relevant player from the byte array.
	 * @param data ->  data that is being passed in
	 */
	public DisconnectPacket(byte[] data) {
		super(01);
		this.userName = readData(data);
	}

	/**
	 * Constructor for DisconnectPacket when it is being passed a player's username
	 * @param userName -> username of the player that is disconnecting
	 */
	public DisconnectPacket(String userName) {
		super(01);
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
		return ("01" + this.userName).getBytes();
	}

	public String getUserName() {
		return userName;
	}
}
