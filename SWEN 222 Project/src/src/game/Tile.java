package game;

import java.awt.Point;

public class Tile {
	private Point coords; //centre of the tile
	private Room room; //room that this tile belongs to
	private int test;

	private double height = 34; //height of the tile
	private double width = 70; //width of the tile


	public Tile(Point coords) {
		this.coords = coords;
	}

	/**
	 * This method should check whether a given point is
	 * contained within this tile.
	 * @param p point being checked
	 * @return true if point is contained within tile,
	 * 		otherwise false.
	 */
	public boolean isOn(Point p){
		double px = p.getX();
		double py = p.getY();

		double dx = Math.abs(px - coords.getX());
		double dy = Math.abs(py - coords.getY());

		if ((dx/(width/2)) + (dy/(height/2)) <= 1){
			return true;
		}
		return false;

	}
}
