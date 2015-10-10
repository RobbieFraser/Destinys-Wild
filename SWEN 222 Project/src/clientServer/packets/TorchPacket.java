package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class TorchPacket extends Packet {

	private int hasTorch;

	public TorchPacket(byte[] data) {
		super(06);
		String dataString = readData(data);
		this.hasTorch = Integer.parseInt(dataString);

	}

	public TorchPacket(boolean hasTorch){
		super(06);
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
		return("06" + getHasTorch()).getBytes();
	}

	public int getHasTorch(){
		return hasTorch;
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
