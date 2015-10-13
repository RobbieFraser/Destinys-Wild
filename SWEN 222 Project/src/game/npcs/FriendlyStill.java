package game.npcs;

import game.DestinysWild;
import game.Interactable;
import game.Room;
import game.Tile;

import java.awt.Point;
import java.io.Serializable;

import renderer.GameImagePanel;
import view.ShopInterface;

/**
 * A FriendlyStill is an NPC who can be interacted with by the player in a positive way.
 * @author Rob
 *
 */
public class FriendlyStill implements NPC, Interactable,Serializable{
	private String type;
	private Point roomCoords;
	private Point realCoords;
	private Room currentRoom;
	private Tile currentTile;
	private int id;

	/**
	 * Creates a new FriendlyStill
	 * @param type of friendly 
	 * @param id of the friendly 
	 * @param roomCoords of the friendly
	 * @param currentRoom of the friendly
	 */
	public FriendlyStill(String type, int id, Point roomCoords, Room currentRoom){
		this.type = type;
		this.id = id;
		this.roomCoords = roomCoords;
		this.realCoords = GameImagePanel.calcRealCoords(roomCoords);
		this.currentRoom = currentRoom;
	}

	/**
	 * Calculates the current Tile here since when this still is constructed
	 * the tiles in the room haven't been initialised. Doesn't do any movement
	 * as it is a friendly still, inherited this method from NPC interface
	 */
	public boolean tryMove(){
		this.currentTile = currentRoom.calcTile(realCoords);
		return false;
	}

	/**
	 * Inherited from NPC interface, no use for FriendlyStill
	 */
	public void takeDamage(int damage){

	}	
	
	/**
	 * Inherited from NPC interface, no use for FriendlyStill
	 */
	public void setCurrentTile(Tile tile){
		
	}
	
	/**
	 * Inherited from NPC interface, no use for FriendlyStill
	 */
	public int getHealth(){
		return 0;
	}
	
	/**
	 * @return the current room of this FriendlyStill
	 */
	public Room getCurrentRoom(){
		return currentRoom;
	}

	/**
	 * @return the realCoords
	 */
	public Point getRealCoords() {
		return realCoords;
	}

	/**
	 * @param realCoords the realCoords to set
	 */
	public void setRealCoords(Point realCoords) {
		this.realCoords = realCoords;
	}
	
	/**
	 * @return the type of this FriendlyStill
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param the type of this FriendlyStill to be set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the room coords of this FriendlyStill
	 */
	public Point getRoomCoords() {
		return roomCoords;
	}

	/**
	 * @param the room coords of this FriendlyStill to be set
	 */
	public void setRoomCoords(Point coords) {
		this.roomCoords = coords;
	}

	/**
	 * Inherited from NPC interface, no use for FriendlyStill
	 */
	public int getDamage() {
		return 0;
	}

	/**
	 * Inherited from NPC interface, no use for FriendlyStill
	 */
	public int getSpeed() {
		return 0;
	}

	/**
	 * @return the type of NPC this is - FriendlyStill
	 */
	public String toString(){
		return "friendlystill";
	}

	/**
	 * Inherited from NPC interface, no use for FriendlyStill
	 */
	public void setHealth(int health) {
		
	}
	
	/**
	 * Inherited from the Interactable Interface. Performs different operations based on which Friendly this is
	 * shopkeeper - Opens the shop if it is day time.
	 * fladnag - Tries to create the complete key from the 4 pieces
	 */
	public void interact() {
		if(type.equals("shopkeeper") && GameImagePanel.getState() == 0){
			new ShopInterface();
		}
		else if(type.equals("fladnag")){
			if(DestinysWild.getPlayer().tryMakeKey()){
				DestinysWild.startTalking("You shall not pass!!! (until you have all 4 key pieces)");
				
			}
			else{
				DestinysWild.startTalking("I have used my wizardry to craft this key! Here you go!");
			}
		}
		else if(type.equals("shopkeeper")){
			DestinysWild.startTalking("The shop is closed. Please come back tomorrow.");
		}

	}

	/**
	 * @return the current Tile of this FriendlyStill
	 */
	public Tile getCurrentTile(){
		return currentTile;
	}

	/**
	 * @return the id of this FriendlyStill
	 */
	public int getId() {
		return id;
	}


}
