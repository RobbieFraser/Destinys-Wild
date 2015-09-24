package game.items;

import game.Room;

import java.awt.Point;

public class Pickup implements Item{

	private String type;

	private Point coords;

	private Room room;

	public Pickup(String type, Point coords, Room room){
		this.type = type;
		this.coords = coords;
		this.room = room;
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

}
