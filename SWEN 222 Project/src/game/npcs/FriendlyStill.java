package game.npcs;

import java.awt.Point;

import game.Interactable;
import view.ShopInterface;

public class FriendlyStill implements NPC, Interactable{
	private String type;
	private Point roomCoords;
	private Point realCoords;
	
	private boolean isTalking;
	
	private String text;

	public FriendlyStill(String type, Point coords){
		this.type = type;
		this.roomCoords = coords;
	}
	
	public boolean tryMove(){
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


}
