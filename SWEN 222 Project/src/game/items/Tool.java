package game.items;

import java.awt.Point;
import java.io.Serializable;

/**
 * Tools are used throughout the world to break corresponding breakables obstructing ares of the map.
 * They can be bought at the Store.
 * @author Rob
 *
 */
public class Tool implements Item,Serializable{

	private String type;
	private int id;
	private String breakable; //the breakable that this tool can remove

	/**
	 * Constructs a Tool and assigns the appropriate breakable to it.
	 * @param type of tool
	 * @param id of tool
	 */
	public Tool(String type, int id){
		this.id = id;
		this.type = type;
		switch(type){
			case "machete":
				breakable = "vine";
				break;

			case "torch":
				breakable = "";
				break;

			case "pickaxe":
				breakable = "cobblestone";
				break;

			case "jetfuel":
				breakable = "";
				break;

			case "bucket":
				breakable = "fire";
				break;

			case "spade":
				breakable = "dirt";
				break;
		}
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Hashcode which uses the id of the tool
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/**
	 * Equals method based on type and id of the Tool
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tool other = (Tool) obj;
		if (id != other.id)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	/**
	 * @return id of tool
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
	 * getter for breakable
	 * @return breakable for this tool
	 */
	public String getBreakable(){
		return breakable;
	}

	/**
	 * Implemented from interface. No purpose for Tool
	 */
	public int getScore() {
		return 0;
	}

	/**
	 * Implemented from interface. No purpose for Key
	 */
	public Point getCoords() {
		return null;
	}

	/**
	 * Implemented from interface. No purpose for Key
	 */
	public int getHealth() {
		return 0;
	}

	/**
	 * @return the type of Item this is - Tool
	 */
	public String toString(){
		return "tool";
	}


}
