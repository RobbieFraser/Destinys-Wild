package game;

import java.awt.Point;

import game.items.Item;
import game.npcs.NPC;
import game.obstacles.Obstacle;

public class Room {
	private int north; //ID's of the surrounding rooms
	private int east;
	private int south;
	private int west;

	private int ID;

	private Point boardPos;

	private Tile[][] tiles = new Tile[10][10];

	private Obstacle[][] obstacles = new Obstacle[10][10];

	private NPC[][] npcs = new NPC[obstacles.length][obstacles[0].length];
	
	private Item[][] items = new Item[obstacles.length][obstacles[0].length];

	public Room(int north, int east, int south, int west, int ID, Point boardPos){
		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
		this.ID = ID;
		this.boardPos = boardPos;
	}

	public Room(){

	}
	
	public void printRoom(){
		for(int i=0; i<obstacles.length; i++){
			System.out.print("| ");
			for(int j=0; j<obstacles.length; j++){
				if(npcs[i][j] != null){
					System.out.print(npcs[i][j].getType() + " | ");
				}
				else if(obstacles[i][j] != null){
					System.out.print(obstacles[i][j].getType() + " | ");
				}
				else if(items[i][j] != null){
					System.out.print(items[i][j].getType() + " | ");
				}
				else{
					System.out.print("null | ");
				}
			}
			System.out.println();
		}
	}
	
	public void addObstacle(Obstacle obs, int x, int y){
		obstacles[x][y] = obs;
	}
	
	public void addNpcs(NPC npc, int x, int y){
		npcs[x][y] = npc;
	}
	
	public void addItems(Item item, int x, int y){
		items[x][y] = item;
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

	public Tile[][] getTile() {
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
	public int getID() {
		return ID;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}


}
