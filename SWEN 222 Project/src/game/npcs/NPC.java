package game.npcs;

import game.Tile;

import java.awt.Point;

public interface NPC {
	public int getDamage();
	public int getSpeed();
	public String getType();
	public Point getRoomCoords();
	public Tile getCurrentTile();
	public Point getRealCoords();
	public String toString();
	public boolean tryMove();
	public void takeDamage(int damage);
}
