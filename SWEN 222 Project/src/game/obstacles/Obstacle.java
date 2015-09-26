package game.obstacles;

import java.awt.Point;

public interface Obstacle {
	public String getType();
	public Point getCoords();
	public String toString();
}
