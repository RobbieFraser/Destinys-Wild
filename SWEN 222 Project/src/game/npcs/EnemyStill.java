package game.npcs;

import game.Interactable;
import game.Room;
import game.Tile;

import java.awt.Point;
import java.io.Serializable;

import renderer.GameImagePanel;

/**
 * An EnemyStill at this stage just represents Spikes. It is classified this way as it deals damage
 * to the player, yet remains still. It cannot be damaged or killed.
 * @author Rob
 *
 */
public class EnemyStill implements NPC, Serializable, Interactable{
	private String type;

	private Point roomCoords;
	private Point realCoords;
	private Room currentRoom;
	private Tile currentTile;

	private int damage;
	private int id;

	/**
	 * Constructs a new EnemyStill and calculates its real coordinates.
	 * @param type of enemyStill
	 * @param id of this enemyStill
	 * @param roomCoords of this enemyStill
	 * @param damage of this enemyStill
	 * @param currentRoom of this enemyStill
	 */
	public EnemyStill(String type, int id, Point roomCoords, int damage, Room currentRoom){
		this.type = type;
		this.roomCoords = roomCoords;
		this.realCoords = GameImagePanel.calcRealCoords(roomCoords);
		this.currentRoom = currentRoom;
		this.damage = damage;
		this.id = id;
	}

	/**
	 * Inherited from the NPC interface. No use for EnemyStill
	 */
	public boolean tryMove(){
		this.currentTile = currentRoom.calcTile(realCoords);
		return false;
	}

	/**
	 * Inherited from the NPC interface. No use for EnemyStill
	 */
	public void setCurrentTile(Tile tile){
		
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
	 * @return the damage this enemy still does
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * 
	 * @param damage to be set
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * @return the type of this enemy still
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type to be set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the room coordinates of this enemy
	 */
	public Point getRoomCoords() {
		return roomCoords;
	}

	/**
	 * @param the room coordinates of this enemy to be set
	 */
	public void setRoomCoords(Point coords) {
		this.roomCoords = coords;
	}

	/**
	 * inherited from the NPC interface. No use in EnemyStill
	 */
	public int getSpeed() {
		return 0;
	}

	/**
	 * @return the type of this NPC - enemystill
	 */
	public String toString(){
		return "enemystill";
	}

	/**
	 * @return the current tile of this enemy
	 */
	public Tile getCurrentTile(){
		return currentTile;
	}

	/**
	 * inherited from the Interactbale interface. No use in EnemyStill
	 */
	public void interact() {
		
	}

	/**
	 * @return the id of this enemy
	 */
	public int getId() {
		return id;
	}

	/**
	 * inherited from the NPC interface. No use in EnemyStill
	 */
	public void setHealth(int health) {
	}
	
	/**
	 * @return the current room of this enemy
	 */
	public Room getCurrentRoom(){
		return currentRoom;
	}
	
	/**
	 * inherited from the NPC interface. No use in EnemyStill
	 */
	public int getHealth(){
		return 0;
	}

	/**
	 * inherited from the NPC interface. No use in EnemyStill
	 */
	public void takeDamage(int damage) {
		
	}

	/**
	 * @return the direction of the npc
	 */
	@Override
	public int getDir() {
		return -1;
	}

}
