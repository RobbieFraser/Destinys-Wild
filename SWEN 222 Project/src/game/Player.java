package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import game.items.Health;
import game.items.Item;
import game.items.Key;

public class Player {
	private String name;
	private Point coords; //Coords relative to the game window
	private int health = 100;
	private Room currentRoom; 
	private List<Room> visitedRooms = new ArrayList<>();
	private List<Item> inventory = new ArrayList<>();
	private int score = 0;
	private int speed = 5;
	private Tile currentTile;

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
		this.currentTile = calcTile();
	}

	/**
	 * Constructor for loading existing player. This will be necessary
	 * because players may load in previous games.
	 * @param name name of the player
	 * @param coords location of the player in terms of the game window
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
		this.currentTile = calcTile();
	}

	/**
	 * After a player has stepped onto a tile, this method calculates whether to push the player
	 * back or whether to pick up an item or whether to do nothing.
	 * @return whether the player can move onto the next tile or not. False = cannot change tile
	 */
	public boolean canChangeTile(){
		if(!currentTile.isOccupied()){
			return true;
		}
		Object occupant = currentRoom.getTileOccupant(currentTile);
		if(occupant instanceof Item){
			if(addInventoryItem((Item)occupant)){
				currentRoom.removeItems((Item)occupant);
				addInventoryItem((Item)occupant);
			}
			return true;
		}
		return false;
		//TODO double check this
	}
	
	public Tile calcTile(){
		for(int row=0; row<currentRoom.getTiles().length; row++){
			for(int col=0; col<currentRoom.getTiles()[0].length; col++){
				Tile current = currentRoom.getTiles()[row][col];
				if(current != null && current.isOn(coords)){
					return current;
				}
			}
		}
		System.out.println("Player isn't on a tile in their currentRoom... (This can't be right)");
		return null;
	}
	
	/**
	 * adds any room object to the visited Room list
	 * @param room room to add
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
	
	/**
	 * adds an item object to the player's inventory
	 * @param item item to add
	 */
	public boolean addInventoryItem(Item item){
		if((item instanceof Health && canAddHealthItem()) || (item instanceof Key && canAddKeyItem())){
			inventory.add(item);
			return true;
		}
		else{
			System.out.println("Too many of that item type in inventory!");
			return false;
		}
	}
	
	/**
	 * Checks whether a health item can be added to the inventory (Max of 5 health items)
	 * @return true/false for above
	 */
	public boolean canAddHealthItem(){
		int count = 0;
		for(Item item : inventory){
			if(item instanceof Health){
				count++;
			}
		}
		return count < 5;
	}
	
	/**
	 * Checks whether a Key item can be added to the inventory (Max of 1 Key item)
	 * @return true/false for above
	 */
	public boolean canAddKeyItem(){
		int count = 0;
		for(Item item : inventory){
			if(item instanceof Key){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * removes an item at 'index' from the player's inventory
	 * @param index index of item to be removed
	 */
	public void removeInventoryItem(int index){
		inventory.remove(index);
	}

	/**
	 * gets the player's name
	 * @return the player's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the current player's name
	 * @param name current player's new name
	 */
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
	
	public void setCurrentTile(Tile currentTile){
		this.currentTile = currentTile;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

}
