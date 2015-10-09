package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public abstract class Packet {


	public static enum PacketTypes{
		INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02), REMOVEITEM(03);
		private int packetID;
		private PacketTypes(int packetID){
			this.packetID = packetID;
		}

		public int getID(){
			return packetID;
		}
	}

	public byte packetID;

	public Packet(int packetID){
		this.packetID = (byte) packetID;
	}

	public abstract void writeData(GameClient client);
	public abstract void writeData(GameServer server);
	public abstract byte[] getData();

	public static PacketTypes getPacket(String packetID){
		try{
			return getPacket(Integer.parseInt(packetID));
		}
		catch(NumberFormatException e){
			return PacketTypes.INVALID;
		}
	}

	public String readData(byte[] data){
		String message = new String(data).trim();
		return message.substring(2);
	}

	public static PacketTypes getPacket(int id){
		for(PacketTypes pt : PacketTypes.values()){
			if(pt.getID()==id){
				return pt;
			}
		}
		return PacketTypes.INVALID;
	}
}
