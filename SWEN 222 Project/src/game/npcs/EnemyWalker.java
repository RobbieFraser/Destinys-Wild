package game.npcs;

import game.DestinysWild;
import game.Interactable;
import game.Player;
import game.Room;
import game.Tile;

import java.awt.Point;
import java.io.Serializable;

import renderer.GameImagePanel;

/**
 * An EnemyWalker is an NPC that causes harm to the player on impact, can be killed by the player,
 * and moves around the board. It has a strategy field which dictates the type of movement.
 * @author Rob
 *
 */
public class EnemyWalker implements NPC, Serializable, Interactable {
	private String type;
	private Point roomCoords; //room coords
	private Point realCoords; // real coords in respect to the window
	private int speed;
	private int damage;
	private int health;
	private int id;
	private String strategy;
	private Room currentRoom;
	private Tile currentTile;
	private Tile prevTile;
	private int dir = 0;
	private int animationState = 0;

	/**
	 * Constructs a new EnemyWalker, setting its strategy based on its type,
	 * and calculating it's real coordinates.
	 * @param type of this enemy walker
	 * @param id of this enemy walker
	 * @param roomCoords of this enemy walker
	 * @param speed of this enemy walker
	 * @param damage of this enemy walker
	 * @param currentRoom of this enemy walker
	 */
	public EnemyWalker(String type, int id, Point roomCoords, int speed, int damage, Room currentRoom){
		this.type = type;
		this.id = id;
		this.roomCoords = roomCoords;
		this.realCoords = GameImagePanel.calcRealCoords(roomCoords);
		this.currentRoom = currentRoom;
		this.damage = damage;
		this.health = damage;
		this.speed = speed;
		switch(type){
			case "bats":
				strategy = "follow";
				break;
			case "snail":
				strategy = "loop";
				break;
			default:
				System.out.println("Couldn't define strategy: Must be 'bats' or 'snail' type");
		}
	}

	/**
	 * tries to move this EnemyWalker
	 * @return boolean if move occurs
	 */
	public boolean tryMove(){
		prevTile = currentTile;
		currentTile = currentRoom.calcTile(realCoords);
		switch(strategy){
			case "follow":
				return tryFollow();
			case "loop":
				return tryLoop();
			default:
				System.out.println("No strategy defined..");
				return false;
		}
	}

	/**
	 * Tries to follow the nearest player
	 * @return boolean if can move
	 */
	public boolean tryFollow(){
		animationState++;
		if(animationState == 20){
			animationState = 0;
		}
		Player nearestPlayer = null;
		for(Player player : DestinysWild.getBoard().getPlayers()){
			if((nearestPlayer == null && player.getCurrentRoom() == getCurrentRoom()) || ((nearestPlayer != null) && player.getCurrentRoom() == getCurrentRoom() && (player.getCoords().distance(realCoords) < nearestPlayer.getCoords().distance(realCoords)))){
				nearestPlayer = player;
			}
		}
		checkHitPlayer();
		if(nearestPlayer != null && nearestPlayer.getCoords().x > realCoords.x){
			if(nearestPlayer.getCoords().y > realCoords.y){
				dir = 2;
				realCoords.translate(speed, speed);
				tryChangeTile(speed, speed);
			}
			else{
				dir = 1;
				realCoords.translate(speed, -speed);
				tryChangeTile(speed, -speed);
			}
			return true;
		}
		else if(nearestPlayer != null && nearestPlayer.getCoords().y > realCoords.y){
			dir = 3;
			realCoords.translate(-speed, speed);
			tryChangeTile(-speed, speed);
			return true;
		}
		else if(nearestPlayer != null && !(nearestPlayer.getCoords().equals(realCoords))){
			dir = 0;
			realCoords.translate(-speed, -speed);
			tryChangeTile(-speed, -speed);
			return true;
		}
		return false; //Doesn't need to move if on top of player
	}

	/**
	 * Tries to move this walker in a loop
	 * @return boolean if can move
	 */
	public boolean tryLoop(){
		if(dir == 0){
			//move north
			realCoords.translate(0, -speed);
			if(!tryChangeTile(0, -speed)){
				dir = 2;
			}
		}
		else if(dir == 2){
			//move south
			realCoords.translate(0, speed);
			if(!tryChangeTile(0, speed)){
				dir = 0;
			}
		}
		return true;
	}

	/**
	 * Acts as collision detection for this walker. This includes triggering damage to the player
	 * upon impact via the following method: checkHitPlayer(). If the walker cannot change tile,
	 * it is moved back to the tile it came from.
	 *
	 * @param x the x position that the walker is trying to move to
	 * @param y the y position that the walker is trying to move to
	 * @return boolean if can change tile
	 */
	public boolean tryChangeTile(int x, int y){
		currentTile.setOccupied(false);
		currentTile = currentRoom.calcTile(realCoords);
		boolean loop = false;
		if(strategy == "loop"){
			loop = true;
		}
		if(currentTile == null || (currentTile.isOccupied() && loop)){
			realCoords.translate(-x, -y);
			currentTile = currentRoom.calcTile(realCoords);
			currentTile.setOccupied(true);
			checkHitPlayer();
			return false;
		}
		currentTile.setOccupied(true);
		checkHitPlayer();
		return true;
	}

	/**
	 * Checks whether this walker has come into contact with a player. If so, the
	 * appropriate amount of damage is done to the player.
	 */
	public void checkHitPlayer(){
		for(Player player : DestinysWild.getBoard().getPlayers()){
			if(currentTile.getRoomCoords().equals(player.getCurrentTile().getRoomCoords())){
				player.takeDamage(damage);
			}
		}
	}

	/**
	 * Called when the player attacks this walker. Lowers this walker's health accordingly,
	 * and checks if it is still alive. If not, handles death.
	 */
	public void takeDamage(int damage){
		health = health - damage;
		if(!checkPulse()){
			currentTile.setOccupied(false);
			currentRoom.removeNpcs(this);
		}
	}

	/**
	 * checks whether this walker is still alive after taking damage
	 * @return boolean has a pulse
	 */
	public boolean checkPulse(){
		if(health <= 0){
			return false;
		}
		return true;
	}

	/**
	 * resets the walker to its original place
	 */
	public void resetPos(){
		if(prevTile != null){
			prevTile.setOccupied(false);
		}
		realCoords = GameImagePanel.calcRealCoords(roomCoords);
		currentTile = currentRoom.calcTile(realCoords);
	}

	/**
	 * @return the speed of this walker
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * @return the health of this walker
	 */
	public int getHealth(){
		return health;
	}

	/**
	 * @param the speed of this walker to be set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}


	/**
	 * @return the strategy
	 */
	public String getStrategy() {
		return strategy;
	}

	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	/**
	 * @return the currentRoom
	 */
	public Room getCurrentRoom() {
		return currentRoom;
	}

	/**
	 * @param currentRoom the currentRoom to set
	 */
	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}

	/**
	 * @return the currentTile
	 */
	public Tile getCurrentTile() {
		return currentTile;
	}

	/**
	 * @param currentTile the currentTile to set
	 */
	public void setCurrentTile(Tile currentTile) {
		this.currentTile = currentTile;
	}

	/**
	 * @return the damage of this walker
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * @param the damage of this walker to be set
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * @return the type of this walker
	 */
	public String getType() {
		return type;
	}

	/**
	 *
	 * @param type of this walker to be set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the real coordinates of this walker
	 */
	public Point getRealCoords() {
		return realCoords;
	}

	/**
	 * @param the real coords of this walker to be set
	 */
	public void setRealCoords(Point coords) {
		this.realCoords = coords;
	}

	/**
	 * @param the health of this walker to be set
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * returns this npc's room coords
	 */
	public Point getRoomCoords() {
		return roomCoords;
	}

	/**
	 * sets this npc's room coords
	 * @param coords to be set
	 */
	public void setRoomCoords(Point coords) {
		this.roomCoords = coords;
	}

	/**
	 * @return the type of NPC thos is - enemywalker
	 */
	public String toString(){
		return "enemywalker";
	}

	/**
	 * Inherited from the Interactable interface. Deals with being attacked by the player.
	 */
	public void interact() {
		takeDamage(2);

	}

	/**
	 * @return the id of this walker
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the direction of the npc
	 */
	@Override
	public int getDir() {
		return dir;
	}

	/**
	 * @return the animationState of the npc
	 */
	@Override
	public int getAnimationState() {
		return animationState;
	}
}
