package game.npcs;

import java.awt.Point;

public class EnemyStill implements NPC{
	private String type;

	private Point coords;

	private int damage;

	public EnemyStill(String type, Point coords, int damage){
		this.type = type;
		this.coords = coords;
		this.damage = damage;
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

	public int getSpeed() {
		return 0;
	}


}
