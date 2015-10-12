package game.items;

import java.awt.Point;
import java.io.Serializable;
 
/**
  * A score item is one which increases the player's score when picked up.
  * The player's score is the form of currency in Destiny's Wild
  * @author Rob
  *
  */
public class Score implements Item,Serializable{

	private String type;

	private Point coords;

	private int id;

	private int score; //score that will be gained upon pickup

	/**
	 * Creates a Score item
	 * @param type of score item
	 * @param coords of this item
	 * @param score that the player will gain from this item
	 * @param id of this item
	 */
	public Score(String type, Point coords, int score, int id){
		this.type = type;
		this.coords = coords;
		this.id = id;
		this.score = score;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @return id of this item
	 */
	public int getId(){
		return id;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
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

	/**
	 * Implemented from interface. No purpose for Score
	 */
	public int getHealth() {
		return 0;
	}

	/**
	 * returns the type of item - Score
	 */
	public String toString(){
		return "score";
	}

}
