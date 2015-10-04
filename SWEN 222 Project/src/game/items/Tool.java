package game.items;

import java.awt.Point;

public class Tool implements Item{

	private String type;
	private int id;
	private String breakable; //the breakable that this tool can remove

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
				breakable = "mound";
				break;
		}
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
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

	public int getId(){
		return id;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Point getCoords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHealth() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String toString(){
		return "tool";
	}


}
