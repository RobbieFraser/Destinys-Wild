package game.items;

import java.awt.Point;

public class Score implements Item{

	private String type;

	private Point coords;
	
	private int score; //score that will be gained upon pickup

	private int room;

	public Score(String type, Point coords, int room, int score){
		this.type = type;
		this.coords = coords;
		this.room = room;
		this.score = score;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return the room
	 */
	public int getRoom() {
		return room;
	}

	/**
	 * @param room the room to set
	 */
	public void setRoom(int room) {
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
