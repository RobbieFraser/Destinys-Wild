package game.npcs;

import java.awt.Point;

public class FriendlyStill implements NPC{
	private String type;

	private Point coords;

	public FriendlyStill(String type, Point coords){
		this.type = type;
		this.coords = coords;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Point getCoords() {
		return coords;
	}

	public void setCoords(Point coords) {
		this.coords = coords;
	}

	public int getDamage() {
		return 0;
	}

	public int getSpeed() {
		return 0;
	}
	
	public String toString(){
		return "friendlystill";
	}


}
