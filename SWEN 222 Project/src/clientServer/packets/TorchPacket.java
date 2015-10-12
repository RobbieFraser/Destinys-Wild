package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class TorchPacket extends Packet {

	private int hasTorch;
	private String userName;

	/**
	 * Constructor for a TorchPacket when passed an array of bytes. Obtains the
	 * player's username and whether or not they have a torch or not.
	 * 
	 * @param data
	 */
	public TorchPacket(byte[] data) {
		super(05);
		String[] dataString = readData(data).split(",");
		this.userName = dataString[0];
		this.hasTorch = Integer.parseInt(dataString[1]);

	}

	/**
	 * Constructor for a TorchPacket when passed a player's username and a
	 * boolean that represents whether or not they have a torch.
	 * 
	 * @param userName
	 * @param hasTorch
	 */
	public TorchPacket(String userName, boolean hasTorch) {
		super(05);
		this.userName = userName;
		this.hasTorch = boolToInt(hasTorch);
	}

	/**
	 * Writes data to the GameClient, sending it to the server
	 */
	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	/**
	 * Writes data to the GameServer, which then sends it to all the clients
	 * connected to that server
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
		return ("05" + getUserName() + "," + getHasTorch()).getBytes();
	}

	public int getHasTorch() {
		return this.hasTorch;
	}

	public String getUserName() {
		return this.userName;
	}

	/**
	 * Helper method that converts booleans to ints
	 * 
	 * @param bool -> the boolean that you want to represent as an int
	 * @return boolInt -> the resultant int(0 for false, 1 for true)
	 */
	public int boolToInt(boolean bool) {
		int boolInt = bool ? 1 : 0;
		return boolInt;
	}

	/**
	 * Helper method that converts ints to booleans
	 * 
	 * @param i -> the int that you want to represent as a boolean
	 * @return Returns true or false, based on the int input
	 */
	public boolean intToBool(int i) {
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

}
