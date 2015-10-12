package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class TimePacket extends Packet{

	private int newTime;

	/**
	 * Constructor for a TimePacket when passed an array of bytes.
	 * Obtains the relevant time from the array
	 * @param data
	 */
	public TimePacket(byte[] data){
		super(04);
		String dataString = readData(data);
		this.newTime = Integer.parseInt(dataString);
	}

	/**
	 * Constructor for a TimePacket when passed the new time.
	 * @param newTime
	 */
	public TimePacket(int newTime){
		super(04);
		this.newTime = newTime;
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
		return("04" + this.getNewTime()).getBytes();
	}

	public int getNewTime(){
		return newTime;
	}
}
