package game.npcs;

import java.awt.Point;
import java.io.Serializable;

import game.Room;
import game.Tile;

public class EnemyWalker implements NPC,Serializable {
	private String type;
	private Point roomCoords; //room coords
	private Point realCoords; // real coords in respect to the window
	private int speed;
	private int damage;
	private String strategy;
	private Room currentRoom;
	private Tile currentTile;

	public EnemyWalker(String type, Point roomCoords, int speed, int damage){
		this.type = type;
		this.roomCoords = roomCoords;
		this.damage = damage;
		this.speed = speed;
		switch(type){
			case "spider":
				strategy = "follow";
				break;
			case "wolf":
				strategy = "loop";
				break;
			default:
				System.out.println("Couldn't define strategy");
		}
	}

	public boolean tryMove(){
		switch(strategy){
			case "follow":
				return tryFollow();
			case "loop":
				return tryLoop();
			default:
				System.out.println("No strategy defined..");
				return false;
		}
	}
	//TODO these methods. Will also have a canChangeTile method in here for collisions.

	public boolean tryFollow(){
		return false;
	}

	public boolean tryLoop(){
		return false;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}


	/**
	 * @return the strategy
	 */
	public String getStrategy() {
		return strategy;
	}

	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	/**
	 * @return the currentRoom
	 */
	public Room getCurrentRoom() {
		return currentRoom;
	}

	/**
	 * @param currentRoom the currentRoom to set
	 */
	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}

	/**
	 * @return the currentTile
	 */
	public Tile getCurrentTile() {
		return currentTile;
	}

	/**
	 * @param currentTile the currentTile to set
	 */
	public void setCurrentTile(Tile currentTile) {
		this.currentTile = currentTile;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Point getRealCoords() {
		return realCoords;
	}

	public void setRealCoords(Point coords) {
		this.realCoords = coords;
	}

	/**
	 * returns this npc's room coords
	 */
	public Point getRoomCoords() {
		return roomCoords;
	}

	/**
	 * sets this npc's room coords
	 * @param coords to be set
	 */
	public void setRoomCoords(Point coords) {
		this.roomCoords = coords;
	}

	public String toString(){
		return "enemywalker";
	}
}
