package game.npcs;

import game.Interactable;
import game.Room;
import game.Tile;

import java.awt.Point;
import java.io.Serializable;

import renderer.GameImagePanel;

public class EnemyStill implements NPC, Serializable, Interactable{
	private String type;

	private Point roomCoords;
	private Point realCoords;
	private Room currentRoom;
	private Tile currentTile;

	private int damage;
	private int health;
	private int id;

	public EnemyStill(String type, int id, Point roomCoords, int damage, Room currentRoom){
		this.type = type;
		this.roomCoords = roomCoords;
		this.realCoords = GameImagePanel.calcRealCoords(roomCoords);
		this.currentRoom = currentRoom;
		this.damage = damage;
		this.health = damage;
		this.id = id;
	}

	public boolean tryMove(){
		this.currentTile = currentRoom.calcTile(realCoords);
		return false;
	}

	public void takeDamage(int damage){
		health = health - damage;
		if(!checkPulse()){
			currentTile.setOccupied(false);
			currentRoom.removeNpcs(this);
		}
	}

	public boolean checkPulse(){
		if(health <= 0){
			return false;
		}
		return true;
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

	@Override
	public void interact() {
		takeDamage(2);

	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setHealth(int health) {
		this.health = health;
	}
	
	@Override
	public Room getCurrentRoom(){
		return currentRoom;
	}
	
	public int getHealth(){
		return health;
	}

}
