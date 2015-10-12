package game.items;

import java.awt.Point;

/**
 * Item interface for all item types
 * @author Rob
 *
 */
public interface Item {
	public String getType();
	public int getScore();
	public Point getCoords();
	public int getHealth();
	public int getId();
	public String toString();
}
