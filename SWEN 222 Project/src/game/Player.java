package game;

import game.items.Item;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Player {
	private String name;
	private Point coords;
	private int health = 100;
	private Room currentRoom; //id of room
	private List<Room> visitedRooms = new ArrayList<>();
	private List<Item> inventory = new ArrayList<>();
	private int score = 0;
	private int speed = 5;

	public Player(){

	}

	/**
	 * Constructor for new players
	 * @param name name of player
	 * @param coords coords of player
	 * @param currentRoom starting room for the player
	 */
	public Player(String name, Point coords, Room currentRoom){
		this.name = name;
		this.coords = coords;
		this.currentRoom = currentRoom;
	}

	/**
	 * Constructor for loading existing player. This will be necessary
	 * because players may load in previous games.
	 * @param name name of the player
	 * @param coords location of the player
	 * @param health player's health
	 * @param currentRoom room player is currently in
	 * @param visitedRooms rooms player has visited
	 * @param inventory list of items the player currently has
	 * @param score current score of the player
	 */
	public Player(String name, Point coords, int health,
			Room currentRoom, List<Room> visitedRooms, List<Item> inventory, int score){
		this.name = name;
		this.coords = coords;
		this.health = health;
		this.currentRoom = currentRoom;
		this.visitedRooms = visitedRooms;
		this.inventory = inventory;
		this.score = score;
	}

	/**
	 * adds any room (by id) to the visited Room list
	 */
	public void addRoom(Room room){
		visitedRooms.add(room);
	}

	/**
	 * adds the current room to the list of visited rooms for the player
	 */
	public void addCurrentRoom(){
		visitedRooms.add(currentRoom);
	}
	
	public void addInventoryItem(Item item){
		inventory.add(item);
	}
	
	public void removeInventoryItem(int index){
		inventory.remove(index);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Point getCoords() {
		return coords;
	}

	public void setCoords(Point coords) {
		this.coords = coords;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}

	public List<Room> getVisitedRooms() {
		return visitedRooms;
	}

	public List<Item> getInventory() {
		return inventory;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

}
