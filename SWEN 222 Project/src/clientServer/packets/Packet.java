package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public abstract class Packet {

	/**
	 * A representation of all the different packet types as enums
	 * and the int that represents them
	 */
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

	/**
	 * Constructor for a packet
	 * @param packetID -> the id of the packet
	 */
	public Packet(int packetID){
		this.packetID = (byte) packetID;
	}

	public abstract void writeData(GameClient client);
	public abstract void writeData(GameServer server);
	public abstract byte[] getData();

	/**
	 * Returns the type of packet, based off the ID if it is a string
	 * @param packetID -> the ID that you want to find the packet type of
	 * @return -> the type of packet it is
	 */
	public static PacketTypes getPacket(String packetID){
		try{
			return getPacket(Integer.parseInt(packetID));
		}
		catch(NumberFormatException e){
			return PacketTypes.INVALID;
		}
	}

	/**
	 * Creates a string from an array of bytes and removes the first two
	 * characters. This removes the two integers used for packet identification.
	 * @param data -> data that you want to convert to a string and read
	 * @return returns a String containing the data
	 */
	public String readData(byte[] data){
		String message = new String(data).trim();
		return message.substring(2);
	}

	/**
	 *Returns the relevant packet type based off the ID
	 *An alternative method when an int is used as the identifier
	 * @param id -> the ID that you want to find the packet type of
	 * @return -> the type of packet it is
	 */
	public static PacketTypes getPacket(int id){
		for(PacketTypes pt : PacketTypes.values()){
			if(pt.getID()==id){
				return pt;
			}
		}
		return PacketTypes.INVALID;
	}
}
