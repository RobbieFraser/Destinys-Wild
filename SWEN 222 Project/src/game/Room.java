package game;

import game.items.Item;
import game.npcs.NPC;
import game.obstacles.Obstacle;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import clientServer.packets.RemoveItemPacket;

public class Room implements Serializable{

	private int ROOM_SIZE = 10; //the length of a room, size x size

	 //ID's of the surrounding rooms
	private int north;
	private int east;
	private int south;
	private int west;
	private int Id;
	private Point boardPos;
	private Tile[][] tiles = new Tile[ROOM_SIZE][ROOM_SIZE];
	private Obstacle[][] obstacles = new Obstacle[ROOM_SIZE][ROOM_SIZE];
	//private NPC[][] npcs = new NPC[ROOM_SIZE][ROOM_SIZE];
	private List<NPC> npcs = new ArrayList<>();
	private Item[][] items = new Item[ROOM_SIZE][ROOM_SIZE];

	/**
	 * Constructor for Room. Each room has a position on the board, a unique
	 * id, and up to four connecting rooms.
	 */
	public Room(int north, int east, int south, int west, int Id, Point boardPos) {
		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
		this.Id = Id;
		this.boardPos = boardPos;
	}

	public Item getItemFromId(int id){
		for (int row = 0; row < getItems().length; ++row) {
			for (int col = 0; col < getItems()[0].length; ++col) {
				Item item = getItems()[row][col];
				if(item != null && item.getId() == id){
					return item;
				}
			}
		}
		return null;
	}
	
	public NPC getNpcFromId(int id){
		for(NPC npc : npcs){
			if(npc.getId() == id){
				return npc;
			}
		}
		return null;
	}

	/**
	 * Calculates which Tile the player is standing on
	 * @return Tile object that the player is standing on
	 */
	public Tile calcTile(Point coords){
		for (int row = 0; row < getTiles().length; ++row) {
			for (int col = 0; col < getTiles()[0].length; ++col) {
				Tile current = getTiles()[row][col];
				if (current != null && current.isOn(coords)){
					return current;
				}
			}
		}
		return null;
	}

	/**
	 * whether the currentTile is in the room
	 * @return boolean whether the current tile is in the currentRoom
	 */
	public boolean currTileIsInRoom(Tile currentTile){
		if(currentTile == null){
			return false;
		}
		return true;
	}

	public Room(){

	}

	/**
	 * Prints to the console a basic text based representation
	 * of this room
	 */
	public void printRoom(){
		for (int i = 0; i < obstacles.length; i++) {
			System.out.print("| ");
			for (int j = 0; j < obstacles.length; j++) {
				if (obstacles[i][j] != null) {
					System.out.print(obstacles[i][j].getType() + " | ");
				}
				else if (items[i][j] != null) {
					System.out.print(items[i][j].getType() + " | ");
				}
				else {
					System.out.print("null | ");
				}
			}
			System.out.println();
		}
	}


	/**
	 * Initialises the Tile[][] array with Tile objects
	 */
	public void initialiseTiles(){
		for(int row=0; row<tiles.length; row++){
			for(int col=0; col<tiles[0].length; col++){
				Item tempItem = items[row][col];
				Obstacle tempObs = obstacles[row][col];
				boolean occupied = false;
				if(tempItem != null || tempObs != null){
					occupied = true;
				}
				tiles[row][col] = new Tile(new Point(row, col), this, occupied);
			}
		}
		for(NPC npc : npcs){
			tiles[npc.getRoomCoords().x][npc.getRoomCoords().y] = new Tile(new Point(npc.getRoomCoords().x, npc.getRoomCoords().y), this, true);
		}
	}

	/**
	 * Gets the occupant of a tile, or null if there isn't one
	 * @param tile The tile that the occupant is on
	 * @return An OBJECT type which will either be NPC, Item, Obstacle or null
	 */
	public Object getTileOccupant(Tile tile){
		for(NPC npc : npcs){
			System.out.println("Tile coords: " + tile.getRoomCoords());
			System.out.println("NPC coords: " + npc.getCurrentTile().getRoomCoords());
			if(tile.getRoomCoords().equals(npc.getCurrentTile().getRoomCoords())){
				return npc;
			}
		}
		for(int row=0; row<tiles.length; row++){
			for(int col=0; col<tiles[0].length; col++){
				if(tile.getRoomCoords().equals(new Point(row, col))){
					if(obstacles[row][col] != null){
						return obstacles[row][col];
					}
					else if(items[row][col] != null){
						return items[row][col];
					}
				}
			}
		}
		//shouldn't happen
		return null;
	}

	public Tile getTileFromRoomCoords(Point tileCoords){
		for(int row=0; row<tiles.length; row++){
			for(int col=0; col<tiles[0].length; col++){
				if(tiles[row][col].getRoomCoords().equals(tileCoords)){
					return tiles[row][col];
				}
			}
		}
		return null;
	}

	public void addObstacle(Obstacle obs, int x, int y){
		obstacles[x][y] = obs;
	}

	public void addNpc(NPC npc){
		npcs.add(npc);
	}

	public void addItem(Item item, int x, int y){
		items[x][y] = item;
	}

	public void removeObstacle(Obstacle obs){
		obstacles[obs.getCoords().x][obs.getCoords().y] = null;
		Tile tile = getTileFromRoomCoords(new Point(obs.getCoords().x, obs.getCoords().y));
		tile.setOccupied(false);
	}

	public void removeNpcs(NPC npc){
		npcs.remove(npc);
		Tile tile = getTileFromRoomCoords(new Point(npc.getRoomCoords().x, npc.getRoomCoords().y));
		tile.setOccupied(false);
	}

	public void removeItems(Item item){
		RemoveItemPacket removePacket = new RemoveItemPacket(this.getId(),item.getId());
		removePacket.writeData(DestinysWild.getMultiplayer().getClient());
		items[item.getCoords().x][item.getCoords().y] = null;
		Tile tile = getTileFromRoomCoords(new Point(item.getCoords().x, item.getCoords().y));
		tile.setOccupied(false);
	}

	public Item[][] getItems(){
		return items;
	}

	public Point getBoardPos() {
		return boardPos;
	}

	public void setBoardPos(Point boardPos) {
		this.boardPos.setLocation(boardPos);
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	/**
	 * @return the npcs
	 */
	public List<NPC> getNpcs() {
		return npcs;
	}

	/**
	 * @return the obstacles
	 */
	public Obstacle[][] getObstacles() {
		return obstacles;
	}


	/**
	 * @return the north
	 */
	public int getNorth() {
		return north;
	}

	/**
	 * @param north the north to set
	 */
	public void setNorth(int north) {
		this.north = north;
	}

	/**
	 * @return the east
	 */
	public int getEast() {
		return east;
	}

	/**
	 * @param east the east to set
	 */
	public void setEast(int east) {
		this.east = east;
	}

	/**
	 * @return the south
	 */
	public int getSouth() {
		return south;
	}

	/**
	 * @param south the south to set
	 */
	public void setSouth(int south) {
		this.south = south;
	}

	/**
	 * @return the west
	 */
	public int getWest() {
		return west;
	}

	/**
	 * @param west the west to set
	 */
	public void setWest(int west) {
		this.west = west;
	}

	/**
	 * @return the iD
	 */
	public int getId() {
		return Id;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setId(int id) {
		Id = id;
	}


}
