package clientServer.packets;

import game.Board;
import clientServer.GameClient;
import clientServer.GameServer;

public class BoardPacket extends Packet {

	private byte[] data;


	public BoardPacket(byte[] data){
		super(03);
		this.data = data;
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
		return data;
	}


}
