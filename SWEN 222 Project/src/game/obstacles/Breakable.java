package game.obstacles;

import game.items.Tool;

import java.awt.Point;

public class Breakable implements Obstacle {

	private String type;
	private Point coords;
	private Tool tool;

	public Breakable(String type, Point coords, Tool tool) {
		this.type = type;
		this.coords = coords;
		this.tool = tool;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public Point getCoords() {
		return coords;
	}

	public Tool getTool(){
		return tool;
	}



}
