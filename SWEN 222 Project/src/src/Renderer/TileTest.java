package Renderer;

import java.awt.Graphics;
import java.awt.Point;

public class TileTest {
	
	private int height, width;
	private Point middle;
	
	public TileTest(int width, int height, Point middle){
		this.width = width;
		this.height = height;
		this.middle = middle;
	}
	
	public void Draw(Graphics g){
		g.drawLine((int)middle.getX(), (int)middle.getY()-(height/2), (int)middle.getX()+(width/2), (int)middle.getY());
		g.drawLine((int)middle.getX()+(width/2), (int)middle.getY(), (int)middle.getX(), (int)middle.getY()+(height/2));
		g.drawLine((int)middle.getX(), (int)middle.getY()+(height/2), (int)middle.getX()-(width/2), (int)middle.getY());
		g.drawLine((int)middle.getX()-(width/2), (int)middle.getY(), (int)middle.getX(), (int)middle.getY()-(height/2));
	}
	
	public Boolean isOn(double x, double y){

		double dx = Math.abs(x - middle.getX());
		double dy = Math.abs(y - middle.getY());

		if((dx/(width/2)) + (dy/(height/2)) <= 1){
			return true;
		}
		return false;
	}
}
