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

	private boolean isTalking;

	private String text;

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

	public void interact() {
		if(type.equals("shopkeeper")){
			new ShopInterface();
		}
		else if(type.equals("fladnag")){
			if(DestinysWild.getPlayer().tryMakeKey()){
				DestinysWild.startTalking("I have used my wizardly prowess to craft this key! Use it wisely.");
			}
			else{
				DestinysWild.startTalking("You shall not pass!!!!!");
			}
		}
		else{
			DestinysWild.startTalking("The shop is closed! Come back tomorrow.");
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
