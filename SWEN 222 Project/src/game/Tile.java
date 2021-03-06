package game;

import java.awt.Point;
import java.io.Serializable;

import renderer.GameImagePanel;
import renderer.GameImagePanel;

/**
 * A Tile is represented by a single ground square in the game. It is used to represent
 * Locations of npcs, players, obstacles and items.
 * @author Rob
 *
 */
public class Tile implements Serializable{
	private Point realCoords; //centre of the tile relative to the game window
	private Point roomCoords; //coords relative to the Room
	private Room room; //room that this tile belongs to
	private boolean occupied = false; //states whether the tile is occupied or not. eg can be walked on

	private double height = 34; //height of the tile in px
	private double width = 70; //width of the tile in px

	/**
	 * Tile constructor
	 * @param realCoords coords relative to the game window
	 * @param roomCoords coords relative to the Room
	 * @param room Room that this Tile belongs to
	 * @param occupied Whether this tile is occupied or not
	 */
	public Tile(Point roomCoords, Room room, boolean occupied) {
		this.roomCoords = roomCoords;
		this.room = room;
		this.occupied = occupied;
		this.realCoords = GameImagePanel.calcRealCoords(roomCoords);
	}

	/**
	 * checks whether this tile is occupied by an Item, NPC, or Obstacle
	 * @return boolean occupied
	 */
	public boolean isOccupied(){
		return occupied;
	}

	/**
	 * sets the occupied boolean
	 * @param occupied
	 */
	public void setOccupied(boolean occupied){
		this.occupied = occupied;
	}

	/**
	 * Calculates whether this tile is a doormat or not. (The two tiles in front of any doorway)
	 * @return Direction of the door from this doormat. If not a doormat, returns a useless string
	 */
	public String isDoorMat(){
		if(room.getNorth() != -1 && (roomCoords.equals(new Point(0,4)) || roomCoords.equals(new Point(0,5)))){
			return "north";
		}
		else if(room.getEast() != -1 && (roomCoords.equals(new Point(4,9)) || roomCoords.equals(new Point(5,9)))){
			return "east";
		}
		else if(room.getSouth() != -1 && (roomCoords.equals(new Point(9,4)) || roomCoords.equals(new Point(9,5)))){
			return "south";
		}
		else if(room.getWest() != -1 && (roomCoords.equals(new Point(4,0)) || roomCoords.equals(new Point(5,0)))){
			return "west";
		}
		return "not a door mat";
	}

	/**
	 * This method should check whether a given point is
	 * contained within this tile.
	 * @param p point being checked
	 * @return true if point is contained within tile,
	 * 		otherwise false.
	 */
	public boolean isOn(Point p){
		double px = p.getX();
		double py = p.getY();

		double dx = Math.abs(px - realCoords.getX());
		double dy = Math.abs(py - realCoords.getY());

		if ((dx/(width/2)) + (dy/(height/2)) <= 1){
			return true;
		}
		return false;

	}

	/**
	 * @return the realCoords
	 */
	public Point getRealCoords() {
		return realCoords;
	}

	/**
	 * @param realCoords the realCoords to set
	 */
	public void setRealCoords(Point realCoords) {
		this.realCoords = realCoords;
	}

	/**
	 * @return the roomCoords
	 */
	public Point getRoomCoords() {
		return roomCoords;
	}

	/**
	 * @param roomCoords the roomCoords to set
	 */
	public void setRoomCoords(Point roomCoords) {
		this.roomCoords = roomCoords;
	}

	/**
	 * @return the room
	 */
	public Room getRoom() {
		return room;
	}

	/**
	 * @param room the room to set
	 */
	public void setRoom(Room room) {
		this.room = room;
	}


}
