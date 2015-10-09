package game.npcs;

import java.awt.Point;
import java.io.Serializable;

import game.DestinysWild;
import game.Player;
import game.Room;
import game.Tile;
import renderer.GameImagePanel;

public class EnemyWalker implements NPC,Serializable {
	private String type;
	private Point roomCoords; //room coords
	private Point realCoords; // real coords in respect to the window
	private int speed;
	private int damage;
	private String strategy;
	private Room currentRoom;
	private Tile currentTile;
	private int dir = 0;

	public EnemyWalker(String type, Point roomCoords, int speed, int damage, Room currentRoom){
		this.type = type;
		this.roomCoords = roomCoords;
		this.realCoords = GameImagePanel.calcRealCoords(roomCoords);
		this.currentRoom = currentRoom;
		this.damage = damage;
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
		Player nearestPlayer = null;
		for(Player player : DestinysWild.getBoard().getPlayers()){
			if(nearestPlayer == null || player.getCoords().distance(realCoords) < nearestPlayer.getCoords().distance(realCoords)){
				nearestPlayer = player;
			}
		}
		checkHitPlayer();
		if(nearestPlayer != null && nearestPlayer.getCoords().x > realCoords.x){
			if(nearestPlayer.getCoords().y > realCoords.y){
				realCoords.translate(speed, speed);
				tryChangeTile(speed, speed);
			}
			else{
				realCoords.translate(speed, -speed);
				tryChangeTile(speed, -speed);
			}
			return true;
		}
		else if(nearestPlayer != null && nearestPlayer.getCoords().y > realCoords.y){
			realCoords.translate(-speed, speed);
			tryChangeTile(-speed, speed);
			return true;
		}
		else if(nearestPlayer != null && !(nearestPlayer.getCoords().equals(realCoords))){
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
				dir = 1;
			}
		}
		else if(dir == 1){
			//move south
			realCoords.translate(0, speed);
			if(!tryChangeTile(0, speed)){
				dir = 0;
			}
		}
		return true;
	}

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

	public void checkHitPlayer(){
		for(Player player : DestinysWild.getBoard().getPlayers()){
			if(currentTile.getRoomCoords().equals(player.getCurrentTile().getRoomCoords())){
				player.takeDamage(damage);
			}
		}
	}

	public void resetPos(){
		realCoords = GameImagePanel.calcRealCoords(roomCoords);
	}

	public int getSpeed() {
		return speed;
	}

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

	public Point getRealCoords() {
		return realCoords;
	}

	public void setRealCoords(Point coords) {
		this.realCoords = coords;
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

	public String toString(){
		return "enemywalker";
	}
}
