package game.obstacles;

import java.awt.Point;

/**
 * Interface for all obstacle types
 * @author Rob
 *
 */
public interface Obstacle {
	public String getType();
	public Point getCoords();
	public String toString();
}
