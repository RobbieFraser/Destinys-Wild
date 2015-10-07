package game.npcs;

import java.awt.Point;
import java.io.Serializable;

public class EnemyStill implements NPC,Serializable{
	private String type;

	private Point roomCoords;
	private Point realCoords;

	private int damage;

	public EnemyStill(String type, Point coords, int damage){
		this.type = type;
		this.roomCoords = coords;
		this.damage = damage;
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


}
