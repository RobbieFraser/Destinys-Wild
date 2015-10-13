package game.obstacles;

import java.awt.Point;
import java.io.Serializable;

/**
 * A Block obstacle is any obstacle that can't be destroyed and will block the player from that tile
 * @author Rob
 *
 */
public class Block implements Obstacle,Serializable{
	private String type;
	private Point coords;

	/**
	 * Creates a Block obstacle
	 * @param type of obstacle
	 * @param coords of the obstacle
	 */
	public Block(String type, Point coords){
		this.type = type;
		this.coords = coords;
	}

	/**
	 * Empty constructor for use in the file parser
	 */
	public Block(){

	}

	/**
	 * @return the type of block
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

	/**
	 * Returns the type of obstacle - block
	 */
	public String toString() {
		return "block";
	}

}
