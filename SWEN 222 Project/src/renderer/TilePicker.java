package renderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class TilePicker {

	//The list of tiles to select from
	private List<EditorTileSelect> selects = new ArrayList<EditorTileSelect>();
	private List<EditorTileSelect> obSelects = new ArrayList<EditorTileSelect>();
	private List<EditorTileSelect> breakSelects = new ArrayList<EditorTileSelect>();
	private List<EditorTileSelect> healthSelects = new ArrayList<EditorTileSelect>();
	private List<EditorTileSelect> scoreSelects = new ArrayList<EditorTileSelect>();
	private List<EditorTileSelect> enemySelects = new ArrayList<EditorTileSelect>();
	private List<EditorTileSelect> npcSelects = new ArrayList<EditorTileSelect>();

	public TilePicker(){
		initSelects();
	}

	/*
	 * Add all tiles to the select list
	 */
	public void initSelects(){
		selects.add(new EditorTileSelect(new EditorTile(99, 99, "???", "???", new Color(255, 167, 246), new Color(255, 65, 235)), "Block"));
		obSelects.add(new EditorTileSelect(new EditorTile(99, 99, "???", "???", new Color(255, 167, 246), new Color(255, 65, 235)), "Block"));
		obSelects.add(new EditorTileSelect(new EditorTile(99, 99, "stone", "stoneblock", new Color(194, 194, 194), new Color(143, 143, 143)), "Block"));
		breakSelects.add(new EditorTileSelect(new EditorTile(99, 99, "cobble", "cobblestone1", new Color(194, 194, 194), new Color(143, 143, 143)), "Breakable"));
		obSelects.add(new EditorTileSelect(new EditorTile(99, 99, "Bstone", "brokenstone1", new Color(194, 194, 194), new Color(143, 143, 143)), "Block"));
		obSelects.add(new EditorTileSelect(new EditorTile(99, 99, "Mstone", "mossyStone1", new Color(194, 194, 194), new Color(25, 123, 48)), "Block"));
		scoreSelects.add(new EditorTileSelect(new EditorTile(99, 99, "Coin", "coin", new Color(255, 215, 0), new Color(205, 173, 0)), "Score"));
		obSelects.add(new EditorTileSelect(new EditorTile(99, 99, "Water", "water", new Color(105, 232, 255), new Color(25, 152, 255)), "Block"));
		healthSelects.add(new EditorTileSelect(new EditorTile(99, 99, "Apple", "apple", new Color(255, 59, 59), new Color(156, 38, 38)), "Health"));
		enemySelects.add(new EditorTileSelect(new EditorTile(99, 99, "Spike", "spike", new Color(167, 167, 167), new Color(210, 210, 210)), "EnemyStill"));
	}

	/*
	 * Returns a Color based on the String you send it
	 */
	public Color getColour(String full){
		//Loop through the tile list to find the matching full name
		for(EditorTileSelect t : selects){
			if(t.getFull().equals(full)){
				return t.getColor2();
			}
		}
		//Return a default colour of red
		return new Color(255,0,0);
	}

	public EditorTileSelect getTile(String full){
		//Loop through the tile list to find the matching full name
		for(EditorTileSelect t : obSelects){
			if(t.getFull().equals(full)){
				return t;
			}
		}
		for(EditorTileSelect t : breakSelects){
			if(t.getFull().equals(full)){
				return t;
			}
		}
		for(EditorTileSelect t : healthSelects){
			if(t.getFull().equals(full)){
				return t;
			}
		}
		for(EditorTileSelect t : scoreSelects){
			if(t.getFull().equals(full)){
				return t;
			}
		}
		for(EditorTileSelect t : enemySelects){
			if(t.getFull().equals(full)){
				return t;
			}
		}
		for(EditorTileSelect t : npcSelects){
			if(t.getFull().equals(full)){
				return t;
			}
		}
		//Return a default tile of whatever is first in the list
		return selects.get(0);
	}

	public List<EditorTileSelect> getSelects(){
		return selects;
	}

	public List<EditorTileSelect> getObSelects() {
		return obSelects;
	}

	public List<EditorTileSelect> getBreakSelects() {
		return breakSelects;
	}

	public List<EditorTileSelect> getHealthSelects() {
		return healthSelects;
	}

	public List<EditorTileSelect> getScoreSelects() {
		return scoreSelects;
	}

	public List<EditorTileSelect> getEnemySelects() {
		return enemySelects;
	}

	public List<EditorTileSelect> getNPCSelects() {
		return npcSelects;
	}
}