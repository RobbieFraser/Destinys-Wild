package game.items;

import java.awt.Point;

public class Tool implements Item{

	private String type;
	private String breakable; //the breakable that this tool can remove

	public Tool(String type){
		this.type = type;
		switch(type){
			case "machete":
				breakable = "vine";
				break;

			case "axe":
				breakable = "thorn";
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


}
