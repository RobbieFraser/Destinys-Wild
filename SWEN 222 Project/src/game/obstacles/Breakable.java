package game.obstacles;

import java.awt.Point;
import java.io.Serializable;

import game.DestinysWild;
import game.Interactable;
import game.Player;

/**
 * a Breakable obstacle is one which can be destroyed if the player contains
 * the correct tool in their inventory
 * @author Rob
 *
 */
public class Breakable implements Obstacle, Interactable,Serializable{

	private String type;
	private Point coords;

	/**
	 * Creates a breakable obstacle
	 * @param type of breakable
	 * @param coords of the breakable
	 */
	public Breakable(String type, Point coords) {
		this.type = type;
		this.coords = coords;
	}

	/**
	 * @return type of breakable
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return room coords of this breakable
	 */
	public Point getCoords() {
		return coords;
	}

	/**
	 * @return the type of obstacle - breakable
	 */
	public String toString(){
		return "breakable";
	}

	/**
	 * Inherited from the Interactable interface. Performs the necessary actions when
	 * a player interacts with this breakable. In this case, change the state of this
	 * breakable if the player has the corresponding tool in their inventory.
	 */
	public void interact() {
		Player player = DestinysWild.getPlayer();
		int typeLen = type.length();
		if(player.hasTool(type.substring(0, typeLen-1)) && !type.equals("steelbeams1")){
			if(type.contains("fire")){
				if(Integer.valueOf(String.valueOf(type.charAt(typeLen-1))) >= 1){
					DestinysWild.getPlayer().getCurrentRoom().removeObstacle(this);
				}
			}
			else if(Integer.valueOf(String.valueOf(type.charAt(typeLen-1))) >= 3){
				DestinysWild.getPlayer().getCurrentRoom().removeObstacle(this);

			}
			else{
				int newStage = Integer.valueOf(String.valueOf(type.charAt(typeLen-1))) + 1;
				String newType = type.substring(0, typeLen-1) + String.valueOf(newStage);
				type = newType;
			}
		}
		else if(player.hasTool(type.substring(0, typeLen-1)) && type.equals("steelbeams1")){
			DestinysWild.startTalking("Hmmmm I would appear that jet fuel can't melt steel beams...");
		}
		else{
			DestinysWild.startTalking("You don't have the correct tool for the job!");
		}
	}



}
