package game.items;

import java.awt.Point;
import java.io.Serializable;

/**
 * A class for Health item types
 * @author Rob
 *
 */
public class Health implements Item,Serializable{

	private String type;

	private Point coords;

	private int id;

	private int health; //health that will be restored upon use

	/**
	 * A Health item is an item the player can store in their inventory to later use.
	 * @param type type of item, eg apple, for rendering
	 * @param coords coords at which the item is located
	 * @param health amount of health this item heals
	 * @param id the id of this item
	 */
	public Health(String type, Point coords, int health, int id){
		this.type = type;
		this.id = id;
		this.coords = coords;
		this.health = health;
	}

	/**
	 * @return the type of this item
	 */
	public String getType() {
		return type;
	}
	/**
	 *@return the id of this item 
	 */
	public int getId(){
		return id;
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
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param health the health to set
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * Required because of interface. No use in this class
	 */
	public int getScore() {
		return 0;
	}

	/**
	 * @return the type of Item this is
	 */
	public String toString(){
		return "health";
	}

}
