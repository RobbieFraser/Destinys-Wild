package clientServer.packets;

import clientServer.GameClient;
import clientServer.GameServer;

public class MovePacket extends Packet {
	private String userName;
	private int x;
	private int y;
	private int roomID;
	private int health;
	private int north;
	private int south;
	private int east;
	private int west;
	private int walkstate;
	private int allowGate;

	/**
	 *Constructor for a MovePacket when it is passed a byte array. The byte array
	 *is read as a String array, which has each value separated from a comma. These
	 *are parsed and passed into the MovePacket.
	 * @param data -> data that is being passed in
	 */
	public MovePacket(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		this.userName = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);
		this.roomID = Integer.parseInt(dataArray[3]);
		this.health = Integer.parseInt(dataArray[4]);
		this.north = Integer.parseInt(dataArray[5]);
		this.east = Integer.parseInt(dataArray[6]);
		this.south = Integer.parseInt(dataArray[7]);
		this.west = Integer.parseInt(dataArray[8]);
		this.walkstate = Integer.parseInt(dataArray[9]);
		this.allowGate = Integer.parseInt(dataArray[10]);
	}

	/**
	 *Constructor for a MovePacket that takes in all relevant information for player movement
	 * @param userName -> username of the relevant player
	 * @param x -> x position of the player
	 * @param y -> y position of the player
	 * @param roomID -> ID of the room the player is in
	 * @param health -> player's health
	 * @param north -> whether the player is moving north
	 * @param east -> whether the player is moving east
	 * @param south -> whether the player is moving south
	 * @param west -> whether the player is moving west
	 * @param walkstate
	 */
	public MovePacket(String userName, int x, int y, int roomID, int health,
			boolean north, boolean east, boolean south, boolean west, int walkstate,boolean allowGate) {
		super(02);
		this.userName = userName;
		this.x = x;
		this.y = y;
		this.roomID = roomID;
		this.health = health;
		this.north = boolToInt(north);
		this.west = boolToInt(west);
		this.east = boolToInt(east);
		this.south = boolToInt(south);
		this.walkstate = walkstate;
		this.allowGate = boolToInt(allowGate);
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
		return ("02" + this.userName + "," + this.getX() + "," + this.getY()
				+ "," + this.getRoomID() + "," + this.getHealth() + ","
				+ this.getNorth() + "," + this.getEast() + ","
				+ this.getSouth() + "," + this.getWest() +"," + this.getWalkstate() + "," + this.getAllowGate()).getBytes();
	}
	
	public int getAllowGate(){
		return this.allowGate;
	}

	public int getNorth() {
		return north;
	}

	public int getEast() {
		return east;
	}

	public int getSouth() {
		return south;
	}

	public int getWest() {
		return west;
	}

	public String getUserName() {
		return userName;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getRoomID() {
		return this.roomID;
	}

	public int getHealth() {
		return this.health;
	}

	public int getWalkstate(){
		return this.walkstate;
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
