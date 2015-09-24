package game.obstacles;

import java.awt.Point;

public class Block implements Obstacle{
	private String type;
	Point coords;
	
	public Block(String type, Point coords){
		this.type = type;
		this.coords = coords;
	}
	
	public Block(){
		
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the coords
	 */
	public Point getCoords() {
		return coords;
	}

	/**
	 * @param coords the coords to set
	 */
	public void setCoords(Point coords) {
		this.coords = coords;
	}
	
}
