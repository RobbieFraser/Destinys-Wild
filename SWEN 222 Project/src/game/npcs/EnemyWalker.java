package game.npcs;

import java.awt.Point;

public class EnemyWalker implements NPC {
	private String type;
	private Point coords;
	private int speed;
	private int damage;

	public EnemyWalker(String type, Point coords, int speed, int damage){
		this.type = type;
		this.coords = coords;
		this.damage = damage;
		this.speed = speed;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
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

	public Point getCoords() {
		return coords;
	}

	public void setCoords(Point coords) {
		this.coords = coords;
	}
	
	public String toString(){
		return "enemywalker";
	}
}
