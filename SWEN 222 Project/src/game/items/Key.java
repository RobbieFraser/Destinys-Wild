package game.items;

import java.awt.Point;

public class Key implements Item{

	private String id; //1-4
	private Point coords;

	public Key(String id, Point coords){
		this.id = id;
		this.coords = coords;
	}

	public String getId(){
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

}
