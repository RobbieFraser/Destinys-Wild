package game;

import java.awt.Point;

import game.items.Item;
import game.npcs.NPC;
import game.obstacles.Obstacle;

public class Room {

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
	private NPC[][] npcs = new NPC[ROOM_SIZE][ROOM_SIZE];
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
				if (npcs[i][j] != null){
					System.out.print(npcs[i][j].getType() + " | ");
				}
				else if (obstacles[i][j] != null) {
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
				NPC tempNpc = npcs[row][col];
				Item tempItem = items[row][col];
				Obstacle tempObs = obstacles[row][col];
				boolean occupied = false;
				if(tempNpc != null || tempItem != null || tempObs != null){
					occupied = true;
				}
				tiles[row][col] = new Tile(new Point(row, col), this, occupied);
			}
		}

	}

	/**
	 * Gets the occupant of a tile, or null if there isn't one
	 * @param tile The tile that the occupant is on
	 * @return An OBJECT type which will either be NPC, Item, Obstacle or null
	 */
	public Object getTileOccupant(Tile tile){
		for(int row=0; row<tiles.length; row++){
			for(int col=0; col<tiles[0].length; col++){
				if(tile.getRoomCoords().equals(new Point(row, col))){
					if(obstacles[row][col] != null){
						return obstacles[row][col];
					}
					else if(npcs[row][col] != null){
						return npcs[row][col];
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

	public void addNpc(NPC npc, int x, int y){
		npcs[x][y] = npc;
	}

	public void addItem(Item item, int x, int y){
		items[x][y] = item;
	}

	public void removeObstacle(Obstacle obs){
		obstacles[obs.getCoords().x][obs.getCoords().y] = null;
	}

	public void removeNpcs(NPC npc){
		npcs[npc.getCoords().x][npc.getCoords().y] = null;
	}

	public void removeItems(Item item){
		items[item.getCoords().x][item.getCoords().y] = null;
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
	public NPC[][] getNpcs() {
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
