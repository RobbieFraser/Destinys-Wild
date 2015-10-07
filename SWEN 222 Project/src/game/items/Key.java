package game.items;

import java.awt.Point;
import java.io.Serializable;

public class Key implements Item,Serializable{

	private int id;
	private Point coords;

	public Key(int id, Point coords){
		this.id = id;
		this.coords = coords;
	}

	public int getId(){
		return id;
	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public int getScore() {
		return 0;
	}

	@Override
	public Point getCoords() {
		return null;
	}

	@Override
	public int getHealth() {
		return 0;
	}

	public String toString(){
		return "key";
	}

}
