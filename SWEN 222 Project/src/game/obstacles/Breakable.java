package game.obstacles;

import java.awt.Point;

public class Breakable implements Obstacle{

	private String type;
	private Point coords;

	public Breakable(String type, Point coords){
	this.type = type;
	this.coords = coords;

	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public Point getCoords() {
		return coords;
	}

}
