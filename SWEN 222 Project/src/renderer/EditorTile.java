package renderer;

import java.awt.Color;
import java.awt.Graphics;

public class EditorTile {
	private int x;
	private int y;
	
	private String type;
	private String full;
	
	private Color color;
	private Color color2;
	
	public EditorTile(int x, int y, String type, String full, Color color, Color color2){
		this.x = x;
		this.y = y;
		this.type = type;
		this.full = full;
		this.color = color;
		this.color2 = color2;
	}
	
	public void draw(Graphics g, int dx, int dy, int size){
		g.setColor(color);
		g.fillRect(dx, dy, size, size);
		g.setColor(color2);
		g.drawRect(dx, dy, size, size);
		g.drawString(type, dx+3, dy+13);
	}
	
	public void drawDot(Graphics g, int dx, int dy, int size){
		g.setColor(color2);
		g.fillRect(dx, dy, size, size);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getType() {
		return type;
	}
	
	public String getFull() {
		return full;
	}

	public Color getColor() {
		return color;
	}

	public Color getColor2() {
		return color2;
	}
}
