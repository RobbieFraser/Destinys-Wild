package game.npcs;

import game.Room;
import game.Tile;

import java.awt.Point;
import java.io.Serializable;

import renderer.GameImagePanel;

public class EnemyStill implements NPC,Serializable{
	private String type;

	private Point roomCoords;
	private Point realCoords;
	private Room currentRoom;
	private Tile currentTile;

	private int damage;

	public EnemyStill(String type, Point roomCoords, int damage, Room currentRoom){
		this.type = type;
		this.roomCoords = roomCoords;
		this.realCoords = GameImagePanel.calcRealCoords(roomCoords);
		this.currentRoom = currentRoom;
		this.damage = damage;
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

	public Point getRoomCoords() {
		return roomCoords;
	}

	public void setRoomCoords(Point coords) {
		this.roomCoords = coords;
	}

	public int getSpeed() {
		return 0;
	}

	public String toString(){
		return "enemystill";
	}

	public Tile getCurrentTile(){
		return currentTile;
	}

}
