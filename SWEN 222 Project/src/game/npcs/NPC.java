package game.npcs;

import game.Room;
import game.Tile;

import java.awt.Point;

/**
 * An interface for all NPC's in the game
 * @author Rob
 *
 */
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
	public Room getCurrentRoom();
	public boolean tryMove();
	public void takeDamage(int damage);
	public int getId();
	public int getHealth();
	public void setCurrentTile(Tile tile);
	public void setRoomCoords(Point point);
}
