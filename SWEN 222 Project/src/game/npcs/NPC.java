package game.npcs;

import game.Tile;

import java.awt.Point;

public interface NPC {
	public int getDamage();
	public void setHealth(int health);
	public int getSpeed();
	public String getType();
	public Point getRoomCoords();
	public Tile getCurrentTile();
	public Point getRealCoords();
	public void setRealCoords(Point coords);
	public String toString();
	public boolean tryMove();
	public void takeDamage(int damage);
	public int getId();
}
