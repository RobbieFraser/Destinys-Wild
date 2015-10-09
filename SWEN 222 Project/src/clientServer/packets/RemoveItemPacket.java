package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class RemoveItemPacket extends Packet {

	private int roomID;
	private int itemID;


	public RemoveItemPacket(byte[] data) {
		super(04);
		String[] dataArray = readData(data).split(",");
		this.roomID = Integer.parseInt(dataArray[0]);
		this.itemID = Integer.parseInt(dataArray[1]);
	}

	public RemoveItemPacket(int roomID, int itemID){
		super(04);
		this.roomID = roomID;
		this.itemID = itemID;
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
		return("04" + this.getRoomID() + "," + this.getItemID()).getBytes();
	}

	public int getRoomID(){
		return this.roomID;
	}

	public int getItemID(){
		return this.itemID;
	}

}
