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
	private int speed = 4;
	private String orientation = "north";
	private Tile currentTile;
	private boolean isMoving;



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
				currentTile.setOccupied(false);
			}
			return true;
		}
		return false;
	}
	
	public boolean tryMove(String direction){
		orientation = direction;
		Tile prevTile = currentTile;
		switch(direction){
			case "north":
				setCoords(getCoords().x, getCoords().y - speed/2);
				if(!currTileIsInRoom() && prevTile.isDoorMat().equals("north")){
					changeRoom(prevTile);
				}
				else if(!currTileIsInRoom() || !canChangeTile()){
					setCoords(getCoords().x, getCoords().y + speed/2);
					currentTile = prevTile;
				}
				break;
			case "east":
				setCoords(getCoords().x + speed, getCoords().y);
				if(!currTileIsInRoom() && prevTile.isDoorMat().equals("east")){
					changeRoom(prevTile);
				}
				else if(!currTileIsInRoom() || !canChangeTile()){
					setCoords(getCoords().x - speed, getCoords().y);
					currentTile = prevTile;
				}
				break;
			case "south":
				setCoords(getCoords().x, getCoords().y + speed/2);
				if(!currTileIsInRoom() && prevTile.isDoorMat().equals("south")){
					changeRoom(prevTile);
				}
				else if(!currTileIsInRoom() || !canChangeTile()){
					setCoords(getCoords().x, getCoords().y - speed/2);
					currentTile = prevTile;
				}
				break;
			case "west":
				setCoords(getCoords().x - speed, getCoords().y);
				if(!currTileIsInRoom() && prevTile.isDoorMat().equals("west")){
					changeRoom(prevTile);
				}
				else if(!currTileIsInRoom() || !canChangeTile()){
					setCoords(getCoords().x + speed, getCoords().y);
					currentTile = prevTile;
				}
				break;
				
		}
		return true;
	}
	
	/**
	 * whether the currentTile is in the room
	 * @return boolean whether the current tile is in the currentRoom
	 */
	public boolean currTileIsInRoom(){
		currentTile = calcTile();
		if(currentTile == null){
			return false;
		}
		return true;
	}
	
	/**
	 * Updates everything required upon changing room
	 * @param previousTile the player's previous Tile 
	 */
	public void changeRoom(Tile previousTile){
		currentRoom = DestinysWild.getBoard().getRoomFromId(currentRoom.getNorth());
		if(!visitedRooms.contains(currentRoom)){
			addCurrentRoom();
		}
		
		int prevX = previousTile.getRoomCoords().x;
		int prevY = previousTile.getRoomCoords().y;
		
		Point newPoint;
		
		if(prevX == 0){
			newPoint = currentRoom.getTileFromRoomCoords(new Point(9, previousTile.getRoomCoords().y)).getRealCoords();
			setCoords(newPoint.x, newPoint.y);
		}
		else if(prevX == 9){
			newPoint = currentRoom.getTileFromRoomCoords(new Point(0, previousTile.getRoomCoords().y)).getRealCoords();
			setCoords(newPoint.x, newPoint.y);
		}
		else if(prevY == 0){
			newPoint = currentRoom.getTileFromRoomCoords(new Point(previousTile.getRoomCoords().x, 9)).getRealCoords();
			setCoords(newPoint.x, newPoint.y);
		}
		else if(prevY == 9){
			newPoint = currentRoom.getTileFromRoomCoords(new Point(previousTile.getRoomCoords().x, 0)).getRealCoords();
			setCoords(newPoint.x, newPoint.y);
		}
		
		currentTile = calcTile();
	}

	/**
	 * Calculates which Tile the player is standing on
	 * @return Tile object that the player is standing on
	 */
	public Tile calcTile(){
		for(int row=0; row<currentRoom.getTiles().length; row++){
			for(int col=0; col<currentRoom.getTiles()[0].length; col++){
				Tile current = currentRoom.getTiles()[row][col];
				if(current != null && current.isOn(coords)){
					return current;
				}
			}
		}
		//System.out.println("Player isn't on a tile in their currentRoom... (This can't be right)");
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
	 * adds an item object to the player's inventory if there is room for one more of that Item type
	 * @param item item to add
	 * @return boolean successful
	 */
	public boolean addInventoryItem(Item item){
		if((item instanceof Health && canAddHealthItem()) || (item instanceof Key && canAddKeyItem())){
			return inventory.add(item);
		}
		else{
			System.out.println("Too many " + item.toString() + " items in inventory!");
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

	public void setCoords(int x, int y) {
		if(coords == null){
			coords = new Point(x, y);
		}
		else{
			coords.setLocation(x, y);
		}
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
	
	public Tile getCurrentTile(){
		return currentTile;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

}
