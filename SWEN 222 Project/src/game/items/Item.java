package game.items;

import java.awt.Point;

public interface Item {
	public String getType();
	public int getScore();
	public Point getCoords();
	public int getHealth();
}
