package game.obstacles;

import game.DestinysWild;
import game.Interactable;
import game.items.Tool;

import java.awt.Point;

public class Breakable implements Obstacle, Interactable{

	private String type;
	private Point coords;

	public Breakable(String type, Point coords) {
		this.type = type;
		this.coords = coords;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public Point getCoords() {
		return coords;
	}
	
	public String toString(){
		return "breakable";
	}

	@Override
	public void interact() {
		int typeLen = type.length();
		if(Integer.valueOf(String.valueOf(type.charAt(typeLen-1))) >= 3){
			DestinysWild.getPlayer().getCurrentRoom().removeObstacle(this);
			
		}
		else{
			int newStage = Integer.valueOf(String.valueOf(type.charAt(typeLen-1))) + 1;
			String newType = type.substring(0, typeLen-1) + String.valueOf(newStage);
			type = newType;
		}
		
	}



}
