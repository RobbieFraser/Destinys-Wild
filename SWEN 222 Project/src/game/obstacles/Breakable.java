package game.obstacles;

import java.awt.Point;
import java.io.Serializable;

import game.DestinysWild;
import game.Interactable;
import game.Player;

public class Breakable implements Obstacle, Interactable,Serializable{

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
		Player player = DestinysWild.getPlayer();
		int typeLen = type.length();
		if(player.hasTool(type.substring(0, typeLen-1))){
			if(Integer.valueOf(String.valueOf(type.charAt(typeLen-1))) >= 3){
				DestinysWild.getPlayer().getCurrentRoom().removeObstacle(this);

			}
			else{
				int newStage = Integer.valueOf(String.valueOf(type.charAt(typeLen-1))) + 1;
				String newType = type.substring(0, typeLen-1) + String.valueOf(newStage);
				type = newType;
			}
		}
		else{
			DestinysWild.startTalking("You don't have the correct tool for the job!");
		}
	}



}
