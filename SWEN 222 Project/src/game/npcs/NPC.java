package game.npcs;

import java.awt.Point;

public interface NPC {
	public int getDamage();
	public int getSpeed();
	public String getType();
	public Point getCoords();
}
