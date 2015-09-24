package Renderer;

import java.awt.Color;
import java.awt.Graphics;

public class EditorTileSelect {
	private EditorTile tile;
	
	public EditorTileSelect(EditorTile tile){
		this.tile = tile;
	}
	
	public void draw(Graphics g, int x, int y, int size){
		tile.draw(g, x, y, size);
	}
	
	public String getType() {
		return tile.getType();
	}

	public Color getColor() {
		return tile.getColor();
	}

	public Color getColor2() {
		return tile.getColor2();
	}
}
