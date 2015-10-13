package game.items;

import java.awt.Point;
import java.io.Serializable;

/**
 * A Key item with id 1-4 represents a piece of the final Key item, which has an id of 5 
 * The complete Key is used to open the gate north of the home room.
 * @author Rob
 *
 */
public class Key implements Item,Serializable{

	private int id;
	private Point coords;

	/**
	 * 
	 * @param id of key
	 * @param coords in the room
	 */
	public Key(int id, Point coords){
		this.id = id;
		this.coords = coords;
	}

	/**
	 * @return id of key
	 */
	public int getId(){
		return id;
	}

	/**
	 * Implemented from interface. No purpose for Key
	 */
	public String getType() {
		return null;
	}

	/**
	 * Implemented from interface. No purpose for Key
	 */
	public int getScore() {
		return 0;
	}

	/**
	 * @return coords of the key
	 */
	public Point getCoords() {
		return coords;
	}
	
	public void setCoords(Point coords){
		this.coords = coords;
	}

	/**
	 * Implemented from interface. No purpose for Key
	 */
	public int getHealth() {
		return 0;
	}

	/**
	 * returns the type of key for rendering purposes
	 */
	public String toString(){
		return "key" + String.valueOf(id);
	}

}
