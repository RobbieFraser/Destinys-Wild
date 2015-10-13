package game.npcs;

import game.DestinysWild;
import game.Interactable;
import game.Room;
import game.Tile;

import java.awt.Point;
import java.io.Serializable;

import renderer.GameImagePanel;
import view.ShopInterface;

public class FriendlyStill implements NPC, Interactable,Serializable{
	private String type;
	private Point roomCoords;
	private Point realCoords;
	private Room currentRoom;
	private Tile currentTile;
	private int id;

	public FriendlyStill(String type, int id, Point roomCoords, Room currentRoom){
		this.type = type;
		this.id = id;
		this.roomCoords = roomCoords;
		this.realCoords = GameImagePanel.calcRealCoords(roomCoords);
		this.currentRoom = currentRoom;
	}

	public boolean tryMove(){
		this.currentTile = currentRoom.calcTile(realCoords);
		return false;
	}


	public void takeDamage(int damage){

	}	
	
	public void setCurrentTile(Tile tile){
		
	}
	
	
	public int getHealth(){
		return 0;
	}
	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Point getRoomCoords() {
		return roomCoords;
	}

	public void setRoomCoords(Point coords) {
		this.roomCoords = coords;
	}

	public int getDamage() {
		return 0;
	}

	public int getSpeed() {
		return 0;
	}

	public String toString(){
		return "friendlystill";
	}

	@Override
	public void setHealth(int health) {
		
	}
	
	public void interact() {
		if(type.equals("shopkeeper") && GameImagePanel.getState() == 0){
			new ShopInterface();
		}
		else if(type.equals("fladnag")){
			if(DestinysWild.getPlayer().tryMakeKey()){
				DestinysWild.startTalking("I have used my advanced wizardry to craft this key! Use it wisely.");
			}
			else{
				DestinysWild.startTalking("You shall not pass!!!!!");
			}
		}
		else if(type.equals("shopkeeper")){
			DestinysWild.startTalking("The shop is closed. Please come back tomorrow.");
		}

	}

	public Tile getCurrentTile(){
		return currentTile;
	}

	@Override
	public int getId() {
		return id;
	}


}
