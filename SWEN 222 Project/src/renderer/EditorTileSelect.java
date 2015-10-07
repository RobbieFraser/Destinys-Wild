package renderer;

import java.awt.Color;
import java.awt.Graphics;

public class EditorTileSelect {
	private EditorTile tile;
	private String objectType;
	
	public EditorTileSelect(EditorTile tile, String objectType){
		this.objectType = objectType;
		this.tile = tile;
	}
	
	public void draw(Graphics g, int x, int y, int size){
		tile.draw(g, x, y, size);
	}
	
	public void drawDot(Graphics g, int x, int y, int size){
		tile.drawDot(g, x, y, size);
	}
	
	public String getObjectType(){
		return objectType;
	}
	
	public String getType() {
		return tile.getType();
	}
	
	public String getFull(){
		return tile.getFull();
	}

	public Color getColor() {
		return tile.getColor();
	}

	public Color getColor2() {
		return tile.getColor2();
	}
}