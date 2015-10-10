package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class TorchPacket extends Packet {

	private int hasTorch;
	private String userName;

	public TorchPacket(byte[] data) {
		super(05);
		String[] dataString = readData(data).split(",");
		this.userName = dataString[0];
		this.hasTorch = Integer.parseInt(dataString[1]);

	}

	public TorchPacket(String userName, boolean hasTorch){
		super(05);
		this.userName = userName;
		this.hasTorch = boolToInt(hasTorch);
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
		return("05" + getUserName() + "," + getHasTorch()).getBytes();
	}

	public int getHasTorch(){
		return this.hasTorch;
	}

	public String getUserName(){
		return this.userName;
	}

	/**
	 * Helper method that converts booleans to ints
	 * @param bool -> the boolean that you want to represent as an int
	 * @return boolInt -> the resultant int(0 for false, 1 for true)
	 */
	public int boolToInt(boolean bool){
		int boolInt = bool ? 1 : 0;
		return boolInt;
	}

	/**
	 * Helper method that converts ints to booleans
	 * @param i -> the int that you want to represent as a boolean
	 * @return Returns true or false, based on the int input
	 */
	public boolean intToBool(int i){
		if(i==1){
			return true;
		}
		else{
			return false;
		}
	}


}
