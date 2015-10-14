//Author: Matt Meyer

package renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import game.Board;
import game.Room;
import game.XMLParser;
import game.items.Health;
import game.items.Item;
import game.items.Key;
import game.items.Score;
import game.npcs.EnemyStill;
import game.npcs.EnemyWalker;
import game.npcs.FriendlyStill;
import game.npcs.NPC;
import game.obstacles.Block;
import game.obstacles.Breakable;
import game.obstacles.Obstacle;

public class LevelEditorPanel extends JPanel implements MouseListener,
		MouseMotionListener {

	private boolean erase = false;

	// Where to start drawing the board
	private int drawX = 50;
	private int drawY = 50;

	// The size of a tile
	private int size = 50;

	// Set the hover x and y to -1 to indicate that it is neither on the board
	// nor the select list
	private int hoverX = -1;
	private int hoverY = -1;
	private Color hoverColor = new Color(255, 255, 255, 128); // The last int is
																// the
																// transparency
																// value

	// The x and y values for the select list
	private int selectX = 650;
	private int selectY = -1;

	// The x and y values for selecting a room on the map
	private int hoverMapX = -1;
	private int hoverMapY = -1;

	// -1 to 4, which door is highlighted (-1 is none)
	private int hoverDoors = -1;

	// The default tile and colours
	private String type = "stone";
	private String full = "stoneblock";
	private Color color = new Color(194, 194, 194);
	private Color color2 = new Color(143, 143, 143);
	private String objectType = "Block"; // What kind of object the currently
											// selected tile is

	// The tiles on the current room
	private EditorTile[][] tiles = new EditorTile[10][10];

	// The list of tiles to select from
	private List<EditorTileSelect> selects;
	private List<EditorTileSelect> obSelects;
	private List<EditorTileSelect> breakSelects;
	private List<EditorTileSelect> healthSelects;
	private List<EditorTileSelect> scoreSelects;
	private List<EditorTileSelect> enemySelects;
	private List<EditorTileSelect> npcSelects;

	private boolean onBoard = false; // Mouse is on the board
	private boolean onSelect = false; // Mouse is on the Select list
	private boolean onMap = false; // Mouse is on the map
	private boolean onDoors = false; // Mouse is on one of the doors

	// Booleans for each wall to say if there is a door there or not
	private boolean north = true;
	private boolean south = true;
	private boolean east = true;
	private boolean west = true;

	// Information about the current Room, including the X and Y values
	private Room curRoom;
	private int roomX = 2;
	private int roomY = 2;

	// The main Board object that everything is run off of
	private Board board;

	private int id = 0;

	// The TilePicker object to help with drawing the board and getting colours
	private TilePicker tp = new TilePicker();

	public LevelEditorPanel(Board board) {
		for (int i = 0; i < board.getBoard().length; i++) {
			for (int j = 0; j < board.getBoard()[0].length; j++) {
				if (board.getBoard()[i][j] != null) {
					id++;
				}
			}
		}
		System.out.println("ID count: " + id);
		selects = tp.getSelects();
		obSelects = tp.getObSelects();
		breakSelects = tp.getBreakSelects();
		healthSelects = tp.getHealthSelects();
		scoreSelects = tp.getScoreSelects();
		enemySelects = tp.getEnemySelects();
		npcSelects = tp.getNPCSelects();
		this.board = board;
		// Make the editor listen for mouse inputs
		addMouseListener(this);
		// Make the editor listen for mouse motion inputs
		addMouseMotionListener(this);
		// Add all the different tile types to the select list
		curRoom = board.getBoard()[roomX][roomY];
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawTrees(g);
		drawSelects(g);
		drawGround(g);
		drawDoors(g);
		// drawTiles(g);
		drawBoard(g);
		drawMap(g);
		drawHover(g);
	}

	/**
	 * Draw all Obstacles, Items, and NPCs on the board, using the stored Board
	 * Object
	 */
	public void drawBoard(Graphics g) {
		Obstacle[][] obs = curRoom.getObstacles();
		Item[][] items = curRoom.getItems();
		List<NPC> npcs = curRoom.getNpcs();
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if (obs[row][col] != null) {
					tp.getTile(obs[row][col].getType()).draw(g,
							(col * size) + drawX, (row * size) + drawY, size);
				}
				if (items[row][col] != null) {
					tp.getTile(items[row][col].getType()).draw(g,
							(col * size) + drawX, (row * size) + drawY, size);
				}
				for (NPC npc : npcs) {
					if (npc.getRoomCoords().equals(new Point(row, col))) {
						tp.getTile(npc.getType()).draw(g, (col * size) + drawX,
								(row * size) + drawY, size);
					}
				}
			}
		}
	}

	/**
	 * Draws the tree tiles around the board, purely cosmetic
	 */
	public void drawTrees(Graphics g) {
		int x = 0;
		int y = 0;
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 12; j++) {
				g.setColor(new Color(1, 156, 67));
				g.fillRect(x, y, size, size);
				g.setColor(new Color(0, 88, 38));
				g.drawRect(x, y, size, size);
				x += size;
			}
			x = 0;
			y += size;
		}
	}

	/**
	 * Draws the map on the side of the screen to display changes made to the
	 * Board
	 */
	public void drawMap(Graphics g) {
		int x = 0;
		int y = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				Room r = board.getBoard()[i][j];
				if (roomY == i && roomX == j) {
					g.setColor(new Color(145, 255, 149));
				} else {
					g.setColor(new Color(125, 235, 129));
				}
				g.fillRect(x + 800, y + 400, size, size);
				if (r != null) {
					Obstacle[][] obs = r.getObstacles();
					Item[][] items = r.getItems();
					List<NPC> npcs = r.getNpcs();
					for (int row = 0; row < 10; row++) {
						for (int col = 0; col < 10; col++) {
							if (obs[row][col] != null) {
								tp.getTile(obs[row][col].getType()).drawDot(g,
										(col * 5) + 800 + x,
										(row * 5) + 400 + y, 5);
							}
							if (items[row][col] != null) {
								tp.getTile(items[row][col].getType()).drawDot(
										g, (col * 5) + 800 + x,
										(row * 5) + 400 + y, 5);
							}
							for (NPC npc : npcs) {
								if (npc.getRoomCoords().equals(
										new Point(row, col))) {
									tp.getTile(npc.getType()).drawDot(g,
											(col * 5) + 800 + x,
											(row * 5) + 400 + y, 5);
								}
							}
						}
					}
				}
				g.setColor(new Color(91, 188, 95));
				g.drawRect(x + 800, y + 400, size, size);
				if (r != null) {
					g.setColor(new Color(125, 235, 129));
					if (r.getNorth() != -1) {
						g.drawRect(x + 800 + 20, y + 400, 10, 1);
					}
					if (r.getSouth() != -1) {
						g.drawRect(x + 800 + 20, y + 400 + size, 10, 1);
					}
					if (r.getWest() != -1) {
						g.drawRect(x + 800, y + 400 + 20, 1, 10);
					}
					if (r.getNorth() != -1) {
						g.drawRect(x + 800 + size, y + 400 + 20, 1, 10);
					}
				}
				x += size;
			}
			x = 0;
			y += size;
		}
		g.setColor(Color.gray);
		g.drawRect(800, 400, 250, 250);
	}

	/**
	 * Draws the doors to the panel, if the corresponding booleans are true
	 */
	public void drawDoors(Graphics g) {
		if (west) {
			if (hoverDoors == 3) {
				g.setColor(new Color(214, 214, 214));
				g.fillRect(0, 250, size, size);
				g.fillRect(0, 300, size, size);
				g.setColor(new Color(163, 163, 163));
				g.drawRect(0, 250, size, size);
				g.drawRect(0, 300, size, size);
			} else {
				g.setColor(new Color(194, 194, 194));
				g.fillRect(0, 250, size, size);
				g.fillRect(0, 300, size, size);
				g.setColor(new Color(143, 143, 143));
				g.drawRect(0, 250, size, size);
				g.drawRect(0, 300, size, size);
			}
		}
		if (east) {
			if (hoverDoors == 1) {
				g.setColor(new Color(214, 214, 214));
				g.fillRect(550, 250, size, size);
				g.fillRect(550, 300, size, size);
				g.setColor(new Color(163, 163, 163));
				g.drawRect(550, 250, size, size);
				g.drawRect(550, 300, size, size);
			} else {
				g.setColor(new Color(194, 194, 194));
				g.fillRect(550, 250, size, size);
				g.fillRect(550, 300, size, size);
				g.setColor(new Color(143, 143, 143));
				g.drawRect(550, 250, size, size);
				g.drawRect(550, 300, size, size);
			}
		}
		if (north) {
			if (hoverDoors == 0) {
				g.setColor(new Color(214, 214, 214));
				g.fillRect(250, 0, size, size);
				g.fillRect(300, 0, size, size);
				g.setColor(new Color(163, 163, 163));
				g.drawRect(300, 0, size, size);
				g.drawRect(250, 0, size, size);
			} else {
				g.setColor(new Color(194, 194, 194));
				g.fillRect(250, 0, size, size);
				g.fillRect(300, 0, size, size);
				g.setColor(new Color(143, 143, 143));
				g.drawRect(300, 0, size, size);
				g.drawRect(250, 0, size, size);
			}
		}
		if (south) {
			if (hoverDoors == 2) {
				g.setColor(new Color(214, 214, 214));
				g.fillRect(250, 550, size, size);
				g.fillRect(300, 550, size, size);
				g.setColor(new Color(163, 163, 163));
				g.drawRect(300, 550, size, size);
				g.drawRect(250, 550, size, size);
			} else {
				g.setColor(new Color(194, 194, 194));
				g.fillRect(250, 550, size, size);
				g.fillRect(300, 550, size, size);
				g.setColor(new Color(143, 143, 143));
				g.drawRect(300, 550, size, size);
				g.drawRect(250, 550, size, size);
			}
		}
	}

	/**
	 * Draw all the tiles in the select list
	 */
	public void drawSelects(Graphics g) {
		int y = 0;
		int x = 649;
		for (EditorTileSelect e : obSelects) {
			e.draw(g, x, y, size);
			x += size;
		}
		x = 649;
		y += size;
		for (EditorTileSelect e : breakSelects) {
			e.draw(g, x, y, size);
			x += size;
		}
		x = 649;
		y += size;
		for (EditorTileSelect e : healthSelects) {
			e.draw(g, x, y, size);
			x += size;
		}
		x = 649;
		y += size;
		for (EditorTileSelect e : scoreSelects) {
			e.draw(g, x, y, size);
			x += size;
		}
		x = 649;
		y += size;
		for (EditorTileSelect e : enemySelects) {
			e.draw(g, x, y, size);
			x += size;
		}
		x = 649;
		y += size;
		for (EditorTileSelect e : npcSelects) {
			e.draw(g, x, y, size);
			x += size;
		}
	}

	/**
	 * Draw all the default grass tiles, a 10*10 grid
	 */
	public void drawGround(Graphics g) {
		int x = 0;
		int y = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				g.setColor(new Color(145, 255, 149));
				g.fillRect(x + drawX, y + drawY, size, size);
				g.setColor(new Color(91, 188, 95));
				g.drawRect(x + drawX, y + drawY, size, size);
				x += size;
			}
			x = 0;
			y += size;
		}
	}

	/**
	 * Draw all the tiles that have been placed
	 */
	public void drawTiles(Graphics g) {
		int x = 0;
		int y = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (tiles[j][i] != null) {
					tiles[j][i].draw(g, x + drawX, y + drawY, size);
				}
				x += size;
			}
			x = 0;
			y += size;
		}
	}

	/**
	 * Highlight the tile that the mouse is hovering over on the board or the
	 * select list
	 */
	public void drawHover(Graphics g) {
		if (onBoard) {
			g.setColor(hoverColor);
			g.fillRect((hoverX * size) + drawX + 1,
					(hoverY * size) + drawY + 1, size - 1, size - 1);
		} else if (onSelect) {
			g.setColor(hoverColor);
			g.fillRect((selectX * size) + 650, (selectY * size) + 1, size - 1,
					size - 1);
		} else if (onMap) {
			g.setColor(hoverColor);
			g.fillRect((hoverMapX * size) + 800 + 1,
					(hoverMapY * size) + 400 + 1, size - 1, size - 1);
		}
	}

	/**
	 * An algorithm to figure out which tile the mouse is hovering over
	 */
	public void checkTile(int x, int y) {
		int oldX = hoverX;
		int oldY = hoverY;

		hoverX = (x - drawX) / size;
		hoverY = (y - drawY) / size;

		if (x < drawX || y < drawY) {
			hoverX = -1;
			hoverY = -1;
			onBoard = false;
		} else if (hoverX > 9 || hoverY > 9) {
			hoverX = -1;
			hoverY = -1;
			onBoard = false;
		} else {
			onBoard = true;
		}

		if (oldX != hoverX || oldY != hoverY) {
			this.repaint();
		}
	}

	/**
	 * Check which tile the mouse is hovering over on the select list
	 */
	public void checkSelect(int x, int y) {
		if (x > 650 && y < 300) {
			int oldY = selectY;
			int oldX = selectX;
			selectY = y / size;
			selectX = (x - 650) / size;
			if (selectY == 0) {
				if (selectX > obSelects.size() - 1) {
					selectY = oldY;
					selectX = oldX;
					onSelect = false;
					return;
				}
			}
			if (selectY == 1) {
				if (selectX > breakSelects.size() - 1) {
					selectY = oldY;
					selectX = oldX;
					onSelect = false;
					return;
				}
			}
			if (selectY == 2) {
				if (selectX > healthSelects.size() - 1) {
					selectY = oldY;
					selectX = oldX;
					onSelect = false;
					return;
				}
			}
			if (selectY == 3) {
				if (selectX > scoreSelects.size() - 1) {
					selectY = oldY;
					selectX = oldX;
					onSelect = false;
					return;
				}
			}
			if (selectY == 4) {
				if (selectX > enemySelects.size() - 1) {
					selectY = oldY;
					selectX = oldX;
					onSelect = false;
					return;
				}
			}
			if (selectY == 5) {
				if (selectX > npcSelects.size() - 1) {
					selectY = oldY;
					selectX = oldX;
					onSelect = false;
					return;
				}
			}
			onSelect = true;
			if (oldY != selectY || oldX != selectX) {
				this.repaint();
			}
		} else {
			onSelect = false;
			this.repaint();
		}
	}

	public void checkDoors(int x, int y) {
		if (x > 0 && x < 50 && y > 250 && y < 350) {
			hoverDoors = 3; // east
			onDoors = true;
		} else if (x > 550 && x < 600 && y > 250 && y < 350) {
			hoverDoors = 1;
			onDoors = true;
		} else if (x > 250 && x < 350 && y > 0 && y < 50) {
			hoverDoors = 0;
			onDoors = true;
		} else if (x > 250 && x < 350 && y > 550 && y < 600) {
			hoverDoors = 2;
			onDoors = true;
		} else {
			hoverDoors = -1;
			onDoors = false;
		}
	}

	public void checkMap(int x, int y) {
		if (x > 800 && x < 1050 && y > 400 && y < 650) {
			int oldX = hoverMapX;
			int oldY = hoverMapY;
			onMap = true;
			hoverMapX = (x - 800) / size;
			hoverMapY = (y - 400) / size;
			if (oldX != hoverMapX || oldY != hoverMapY) {
				this.repaint();
			}
		} else {
			onMap = false;
			this.repaint();
		}
	}

	/**
	 * Add a tile to the tiles array, with the location of the mouse
	 */
	public void createTile() {
		int keyCount = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				Room room = board.getRoomFromCoords(i, j);
				for (int k = 0; k < 10; k++) {
					for (int h = 0; h < 10; h++) {
						if (room.getItems()[k][h] != null) {
							if (room.getItems()[k][h].getType().contains("key")) {
								keyCount++;
							}
						}
					}
				}
			}
		}
		if (erase) {
			curRoom.getObstacles()[hoverY][hoverX] = null;
			curRoom.getItems()[hoverY][hoverX] = null;
			NPC temp = null;
			for (NPC npc : curRoom.getNpcs()) {
				if (npc.getRoomCoords().equals(new Point(hoverY, hoverX))) {
					temp = npc;
				}
			}
			if (temp != null) {
				curRoom.removeNpcs(temp);
			}
		} else {
			int count = 1;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					if (curRoom.getItems()[i][j] != null) {
						count++;
					}
				}
			}
			for(NPC npc : curRoom.getNpcs()){
				System.out.println(npc.getId());
			}
			count += curRoom.getNpcs().size();
			switch (objectType) {
			case "Block":
				curRoom.getObstacles()[hoverY][hoverX] = new Block(full,
						new Point(hoverY, hoverX));
				curRoom.getItems()[hoverY][hoverX] = null;
				break;
			case "Breakable":
				curRoom.getObstacles()[hoverY][hoverX] = new Breakable(full,
						new Point(hoverY, hoverX));
				curRoom.getItems()[hoverY][hoverX] = null;
				break;
			case "Health":
				int val = 0;
				if (full.equals("apple")) {
					val = 15;
				} else {
					val = 50;
				}
				curRoom.getItems()[hoverY][hoverX] = new Health(full,
						new Point(hoverY, hoverX), val, Integer.valueOf(count
								+ "" + curRoom.getId()));
				curRoom.getObstacles()[hoverY][hoverX] = null;
				break;
			case "Score":
				int money = 0;
				if (full.equals("coins1")) {
					money = 10;
				} else if (full.equals("coins2")) {
					money = 20;
				} else if (full.equals("coins3")) {
					money = 30;
				} else {
					money = 100;
				}
				curRoom.getItems()[hoverY][hoverX] = new Score(full, new Point(
						hoverY, hoverX), money, Integer.valueOf(count + ""
						+ curRoom.getId()));
				curRoom.getObstacles()[hoverY][hoverX] = null;
				break;
			case "Key":
				curRoom.getItems()[hoverY][hoverX] = new Key(keyCount + 1,
						new Point(hoverY, hoverX));
				curRoom.getObstacles()[hoverY][hoverX] = null;
				break;
			case "EnemyStill":
				curRoom.addNpc(new EnemyStill(full, Integer.valueOf(count + ""
						+ curRoom.getId()), new Point(hoverY, hoverX), 10,
						curRoom));
				curRoom.getObstacles()[hoverY][hoverX] = null;
				curRoom.getItems()[hoverY][hoverX] = null;
				// curRoom.getNpcs()[hoverY][hoverX] = new EnemyStill(full, new
				// Point(hoverY, hoverX), 10);
				break;
			case "EnemyWalker":
				int speed = 0;
				int damage = 0;
				if (full.equals("bats")) {
					speed = 1;
					damage = 15;
				} else {
					speed = 2;
					damage = 30;
				}
				curRoom.addNpc(new EnemyWalker(full, Integer.valueOf(count + ""
						+ curRoom.getId()), new Point(hoverY, hoverX), speed,
						damage, curRoom));
				curRoom.getObstacles()[hoverY][hoverX] = null;
				curRoom.getItems()[hoverY][hoverX] = null;
				// curRoom.getNpcs()[hoverY][hoverX] = new EnemyWalker(full, new
				// Point(hoverY, hoverX), 10, 10);
				break;
			case "FriendlyStill":
				curRoom.addNpc(new FriendlyStill(full, Integer.valueOf(count
						+ "" + curRoom.getId()), new Point(hoverY, hoverX),
						curRoom));
				curRoom.getObstacles()[hoverY][hoverX] = null;
				curRoom.getItems()[hoverY][hoverX] = null;
				break;
			}
		}
		// curRoom.getObstacles()[hoverY][hoverX] = new Block(full, new
		// Point(hoverY, hoverX));
		// tiles[hoverX][hoverY] = new EditorTile(hoverX, hoverY, type, full,
		// color, color2);
		curRoom.printRoom();
		board.printBoard();
		this.repaint();
	}

	/**
	 * Change the current tile to the tile clicked on the select list
	 */
	public void selectTile() {
		if (selectY == 0) {
			type = obSelects.get(selectX).getType();
			full = obSelects.get(selectX).getFull();
			color = obSelects.get(selectX).getColor();
			color2 = obSelects.get(selectX).getColor2();
			objectType = obSelects.get(selectX).getObjectType();
		} else if (selectY == 1) {
			type = breakSelects.get(selectX).getType();
			full = breakSelects.get(selectX).getFull();
			color = breakSelects.get(selectX).getColor();
			color2 = breakSelects.get(selectX).getColor2();
			objectType = breakSelects.get(selectX).getObjectType();
		} else if (selectY == 2) {
			type = healthSelects.get(selectX).getType();
			full = healthSelects.get(selectX).getFull();
			color = healthSelects.get(selectX).getColor();
			color2 = healthSelects.get(selectX).getColor2();
			objectType = healthSelects.get(selectX).getObjectType();
		} else if (selectY == 3) {
			type = scoreSelects.get(selectX).getType();
			full = scoreSelects.get(selectX).getFull();
			color = scoreSelects.get(selectX).getColor();
			color2 = scoreSelects.get(selectX).getColor2();
			objectType = scoreSelects.get(selectX).getObjectType();
		} else if (selectY == 4) {
			type = enemySelects.get(selectX).getType();
			full = enemySelects.get(selectX).getFull();
			color = enemySelects.get(selectX).getColor();
			color2 = enemySelects.get(selectX).getColor2();
			objectType = enemySelects.get(selectX).getObjectType();
		} else if (selectY == 5) {
			type = npcSelects.get(selectX).getType();
			full = npcSelects.get(selectX).getFull();
			color = npcSelects.get(selectX).getColor();
			color2 = npcSelects.get(selectX).getColor2();
			objectType = npcSelects.get(selectX).getObjectType();
		}
	}

	public void selectRoom() {
		// If the selected room doesn't exist
		if (board.getBoard()[hoverMapY][hoverMapX] == null) {
			// set the id's of the rooms to all be -1 (non existent)
			int northID = -1;
			int eastID = -1;
			int southID = -1;
			int westID = -1;
			// work out if any rooms exist through any of the doors and get the
			// rooms ID's
			for (int i = 0; i < board.getBoard().length; i++) {
				for (int j = 0; j < board.getBoard()[0].length; j++) {
					if (board.getBoard()[j][i] != null) {
						if (j == hoverMapY - 1 && i == hoverMapX) {
							northID = board.getBoard()[j][i].getId();
							board.getBoard()[j][i].setSouth(id);
							System.out.println("north, id was: "
									+ board.getBoard()[j][i].getId());
						}
						if (j == hoverMapY + 1 && i == hoverMapX) {
							southID = board.getBoard()[j][i].getId();
							board.getBoard()[j][i].setNorth(id);
							System.out.println("south, id was: "
									+ board.getBoard()[j][i].getId());
						}
						if (j == hoverMapY && i == hoverMapX + 1) {
							eastID = board.getBoard()[j][i].getId();
							board.getBoard()[j][i].setWest(id);
							System.out.println("east, id was: "
									+ board.getBoard()[j][i].getId());
						}
						if (j == hoverMapY && i == hoverMapX - 1) {
							westID = board.getBoard()[j][i].getId();
							board.getBoard()[j][i].setEast(id);
							System.out.println("west, id was: "
									+ board.getBoard()[j][i].getId());
						}
					}
				}
			}
			board.getBoard()[hoverMapY][hoverMapX] = new Room(northID, eastID,
					southID, westID, id, new Point(hoverMapY, hoverMapX));
			id++;
		}
		curRoom = board.getBoard()[hoverMapY][hoverMapX];
		roomX = (int) curRoom.getBoardPos().getY();
		roomY = (int) curRoom.getBoardPos().getX();
		if (curRoom.getNorth() == -1) {
			north = false;
		} else {
			north = true;
		}
		if (curRoom.getEast() == -1) {
			east = false;
		} else {
			east = true;
		}
		if (curRoom.getSouth() == -1) {
			south = false;
		} else {
			south = true;
		}
		if (curRoom.getWest() == -1) {
			west = false;
		} else {
			west = true;
		}
	}

	public void changeDoors() {
		System.out.println(hoverDoors);
		if (hoverDoors == 0) {
			north = !north;
			if (north) {
				if (board.getRoomFromCoords(roomY - 1, roomX) != null) {
					curRoom.setNorth(board.getRoomFromCoords(roomY - 1, roomX)
							.getId());
					board.getRoomFromCoords(roomY - 1, roomX).setSouth(
							curRoom.getId());
				}
			} else {
				curRoom.setNorth(-1);
				if (board.getRoomFromCoords(roomY - 1, roomX) != null) {
					board.getRoomFromCoords(roomY - 1, roomX).setSouth(-1);
				}
			}
		}
		if (hoverDoors == 1) {
			east = !east;
			if (east) {
				if (board.getRoomFromCoords(roomY, roomX + 1) != null) {
					curRoom.setEast(board.getRoomFromCoords(roomY, roomX + 1)
							.getId());
					board.getRoomFromCoords(roomY, roomX + 1).setWest(
							curRoom.getId());
				}
			} else {
				curRoom.setEast(-1);
				if (board.getRoomFromCoords(roomY, roomX + 1) != null) {
					board.getRoomFromCoords(roomY, roomX + 1).setWest(-1);
				}
			}
		}
		if (hoverDoors == 2) {
			south = !south;
			if (south) {
				if (board.getRoomFromCoords(roomY + 1, roomX) != null) {
					curRoom.setSouth(board.getRoomFromCoords(roomY + 1, roomX)
							.getId());
					board.getRoomFromCoords(roomY + 1, roomX).setNorth(
							curRoom.getId());
				}
			} else {
				curRoom.setSouth(-1);
				if (board.getRoomFromCoords(roomY + 1, roomX) != null) {
					board.getRoomFromCoords(roomY + 1, roomX).setNorth(-1);
				}
			}
		}
		if (hoverDoors == 3) {
			west = !west;
			if (west) {
				if (board.getRoomFromCoords(roomY, roomX - 1) != null) {
					curRoom.setWest(board.getRoomFromCoords(roomY, roomX - 1)
							.getId());
					board.getRoomFromCoords(roomY, roomX - 1).setEast(
							curRoom.getId());
				}
			} else {
				curRoom.setWest(-1);
				if (board.getRoomFromCoords(roomY, roomX - 1) != null) {
					board.getRoomFromCoords(roomY, roomX - 1).setEast(-1);
				}
			}
		}
	}

	public void saveBoard() {
		XMLParser.saveBoard("board.xml", board);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (onBoard) {
			createTile();
		}
		if (onSelect) {
			selectTile();
		}
		if (onMap) {
			selectRoom();
			this.repaint();
		}
		if (onDoors) {
			changeDoors();
		}
	}

	public void setErase(boolean b) {
		erase = b;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		checkTile(e.getX(), e.getY());
		checkSelect(e.getX(), e.getY());
		checkMap(e.getX(), e.getY());
		checkDoors(e.getX(), e.getY());
	}

}