package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class RemoveItemPacket extends Packet {

	private int roomID;
	private int itemID;

	/**
	 * Constructor for RemoveItemPacket when it is being passed a byte array. The byte array
	 *is read as a String array, which has each value separated from a comma. These
	 *are parsed and passed into the RemoveItemPacket.
	 * @param data ->  data that is being passed in
	 */
	public RemoveItemPacket(byte[] data) {
		super(03);
		String[] dataArray = readData(data).split(",");
		this.roomID = Integer.parseInt(dataArray[0]);
		this.itemID = Integer.parseInt(dataArray[1]);
	}

	/**
	 *Constructor for RemoveItemPacket that takes in the relevant information for item removal
	 * @param roomID -> the ID of the room the item is in
	 * @param itemID -> the ID of the item to be removed
	 */
	public RemoveItemPacket(int roomID, int itemID){
		super(03);
		this.roomID = roomID;
		this.itemID = itemID;
	}

	/**
	 * Writes data to the GameServer, which then sends it to all the clients connected
	 * to that server
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
		return("03" + this.getRoomID() + "," + this.getItemID()).getBytes();
	}

	public int getRoomID(){
		return this.roomID;
	}

	public int getItemID(){
		return this.itemID;
	}

}
