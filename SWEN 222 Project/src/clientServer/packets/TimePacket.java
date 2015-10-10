package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class TimePacket extends Packet{

	private int newTime;

	public TimePacket(byte[] data){
		super(04);
		String dataString = readData(data);
		this.newTime = Integer.parseInt(dataString);
	}

	public TimePacket(int newTime){
		super(04);
		this.newTime = newTime;
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
		return("04" + this.getNewTime()).getBytes();
	}

	public int getNewTime(){
		return newTime;
	}
}
