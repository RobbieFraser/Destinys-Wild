package game.npcs;

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

	private boolean isTalking;

	private String text;

	public FriendlyStill(String type, Point roomCoords, Room currentRoom){
		this.type = type;
		this.roomCoords = roomCoords;
		this.realCoords = GameImagePanel.calcRealCoords(roomCoords);
		this.currentRoom = currentRoom;
	}

	public boolean tryMove(){
		this.currentTile = currentRoom.calcTile(realCoords);
		return false;
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

	/**
	 * Makes this FriendlyStill shut their mouth
	 */
	public void shutUp(){
		isTalking = false;
	}

	/**
	 * Honours this FriendlyStill with the power of free(ish) speech
	 */
	public void speak(){
		if(type.equals("fladnag")){
			text = "You shall not pass!!!!!";
		}
		isTalking = true;
	}

	@Override
	public void interact() {
		if(type.equals("shopkeeper")){
			new ShopInterface();
		}
		else{
			speak();
		}

	}

	public Tile getCurrentTile(){
		return currentTile;
	}


}
