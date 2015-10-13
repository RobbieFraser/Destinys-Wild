package renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import game.Board;
import game.DestinysWild;
import game.Player;
import game.Room;
import game.Tile;
import game.npcs.EnemyStill;
import game.npcs.EnemyWalker;
import game.npcs.NPC;
import game.obstacles.Breakable;

public class GameImagePanel extends JPanel implements MouseListener {

	private Board board; //The main Board object given from the main Game class
	private Player player; //The player that the user controls
	private Point curRoomCoords = new Point(2,2); //Temporary?
	private Room curRoom; //The current Room Object

	private static final String IMAGE_PATH = "data/images/"; //The path to all the images
	private static final int DAY_TIME = 0;
	private static final int DUSK = 1;
	private static final int NIGHT_TIME = 2;
	private static final int DAWN = 3;

	private BufferedImage defaultCube; //The default image
	private BufferedImage waterSprite; //Testing
	private BufferedImage water; //Testing
	private BufferedImage playerIMG = loadImage("playerSpriteSheetWalking.png").getSubimage(104, 0, 26, 82); //The image that is drawn for the player
	private BufferedImage playerOtherIMG = loadImage("otherSpriteSheetWalking.png").getSubimage(104, 0, 26, 82);

	private int time = 0;
	private static int state = DAY_TIME;
	private boolean timeUp = true;

	private static int gX = 200; //Ground x
	private static int gY = 180; //Ground y

	private int obX = 0; //Object x
	private int obY = 104; //Object y
	private int obW = 34; //Object width
	private int obH = 16; //Object Height

	private static int tileX = 0; //Object x
	private static int tileY = 142; //Object y
	private static int tileW = 34; //Object width
	private static int tileH = 16; //Object Height

	private int charX = 24; //Player x
	private int charY = 82; //Player y
	private int charW = 34; //Player width
	private int charH = 16; //Player Height

	private int cX = 950; //Compass x
	private int cY = 375; //Compass y

	private int wnX = gX-34; //Wall North x
	private int wnY = gY-262; //Wall North y

	private int weX = gX+340; //Wall North x
	private int weY = gY-262; //Wall North y

	private String viewDir = "north";

	private boolean hurt = false;
	
	private boolean north = false;
	private boolean east = false;
	private boolean south = false;
	private boolean west = false;

	private TileTest tile = new TileTest(70, 34, new Point(500,200));

//	public GameImagePanel(Board board){
//		red = 120;
//		green = 201;
//		blue = 255;
//		this.setBackground(new Color(red, green, blue));
//		this.board = board;
//		curRoom = board.getBoard()[(int)curRoomCoords.getX()][(int)curRoomCoords.getY()];
//		player = new Player("Matt", new Point(546, 287), curRoom);
//		setDefault();
//		addMouseListener(this);
//		//waterTest();
//	}

	public GameImagePanel(Board board, Player player){
		this.board = board;
		this.player = player;
		curRoom = player.getCurrentRoom();
		setDefault();
		addMouseListener(this);
		updateBackground();
		//waterTest();
	}

//	public void waterTest() {
//		while (true) {
//			for (int i = 0; i < 4; i++) {
//				for (int j = 0; j < 4; j++) {
//					waterSprite = water.getSubimage(j*70, i*34, 70, 34);
//					this.repaint();
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//	}

	/*
	 * Sets the default image
	 *
	 * -The default image is used when a file cannot be found in the data folder
	 * -Useful so that if there is no image for a certain type of obstacle/NPC/item,
	 *  something will still be drawn
	 */
	public void setDefault(){
		try {
			defaultCube = ImageIO.read(new File("data/images/wireframecube.png"));
			water = ImageIO.read(new File("data/images/waterSpriteSheet.png"));
			waterSprite = water.getSubimage(0, 0, 70, 34);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		curRoom = player.getCurrentRoom();
		curRoomCoords = curRoom.getBoardPos();
		super.paintComponent(g);
		
		updatePlayerDirs();
		
		//If the player is hurt, increase the hurt int every time this -
		//method is called until they are no longer invincible
		if (player.isInvincible()){
			hurt = !hurt;
		} else {
			hurt = false;
		}

		drawNorthRoom(g);
		drawNorthWall(g);
		drawEastRoom(g);
		drawEastWall(g);
		drawGround(g);
		drawBoard(g);
		drawDarkness(g);
		updateBackground();
		drawCompass(g);
		drawText(g);
		drawScore(g);
	}
	
	public void updatePlayerDirs(){
		if(viewDir.equals("north")){
			north = player.isNorth();
			east = player.isEast();
			south = player.isSouth();
			west = player.isWest();
		}
		else if(viewDir.equals("east")){
			north = player.isWest();
			east = player.isNorth();
			south = player.isEast();
			west = player.isSouth();
		}
		else if(viewDir.equals("south")){
			north = player.isSouth();
			east = player.isWest();
			south = player.isNorth();
			west = player.isEast();
		}
		else if(viewDir.equals("west")){
			north = player.isEast();
			east = player.isSouth();
			south = player.isWest();
			west = player.isNorth();
		}
	}
	
	public void drawText(Graphics g){
		if(DestinysWild.isTalking()){
			g.setColor(new Color(150, 150, 150, 150));
			g.fillRect(300, 100, 500, 50);
			String text = DestinysWild.getText();
			g.setFont(new Font("Courier new", 20, 15));
			g.setColor(Color.WHITE);
			g.drawString(text, 320, 125);
		}
	}

	/**
	 * This method should be called to update the time field.
	 * When the field reaches certain values, the time of day
	 * should change. The value of time should go up to 63, then
	 * back down to 0.
	 */
	public void changeTime() {
		//sanity check
		if (time < 0 || time > 63) {
			throw new Error("Invalid time value");
		}
		if (timeUp) {
			time++;
			if (time == 40) {
				state = DUSK;
			} else if (time == 63) {
				timeUp = false;
				state = NIGHT_TIME;
			}
		}
		else {
			time--;
			if (time == 20) {
				state = DAWN;
			}
			else if (time == 0) {
				timeUp = true;
				state = DAY_TIME;
			}
		}
	}

	/**
	 * This method should be called when it is not the state is
	 * not DAY_TIME. This method should iterate through every
	 * 10x10 area of pixels on the game panel, and draw variable
	 * levels of darkness depending on the time of day. A circle
	 * of light should be left around the player.
	 * @param g graphics that are doing the drawing
	 */
	private void drawDarkness(Graphics g) {
		if (state == DAY_TIME) {
			//no need to draw the dark
			return;
		}

		for (int i = 0; i < 110; ++i) {
			for (int j = 0; j < 80; ++j) {
				//initially assumed that black drawn
				boolean drawDarkness = true;
				//light should be drawn around every player
				for (Player player: board.getPlayers()) {
					//only draw this player's torch if they are the current player's room
					if (player != null && player.getCurrentRoom().equals(this.player.getCurrentRoom())) {
						if (isPointInside(i*10, j*10+50, player)) {
							//this 10x10 square is inside the player's area
							drawDarkness = false;
						}
					}
				}
				if (drawDarkness) {
					if (state == NIGHT_TIME) {
						g.setColor(new Color(0, 0, 0, 230));
					} else if (state == DUSK) {
						g.setColor(new Color(0, 0, 0, (time - 40) * 9));
					} else if (state == DAWN) {
						g.setColor(new Color(0, 0, 0, time*12));
					}
					g.fillRect(i*10, j*10, 10, 10);
				}
			}
		}
	}

	/**
	 * This method should return true if the two values x and
	 * y are contained within a circle with a radius centred
	 * on the point.
	 * @param x x coord
	 * @param y y coord
	 * @param point centre of circle
	 * @return true if point is inside circle, otherwise false
	 */
	private boolean isPointInside(int x, int y, Player player) {
		Point playerCoords = player.getCoords();
		int radius = 50;

		//check if the players inventory contains the torch
		if (player.getHasTorch()) {
			radius = 150;
		}

		//use the pythagorean theorem
		int xDiff = Math.abs(playerCoords.x - x);
		int yDiff = Math.abs(playerCoords.y - y);
		double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
		return distance <= radius;
	}

	/**
	 * This method should be called when the game panel is redrawn.
	 * It should update the background colour of the game. The
	 * background colour will vary depending on the time of day.
	 */
	private void updateBackground() {
		Color backgroundColour;
		Color current = this.getBackground();
		if (state == DAY_TIME) {
			//day time
			backgroundColour = new Color(120, 201, 255);
		} else if (state == DUSK) {
			//slowly approach 0
			backgroundColour = new Color((int) (current.getRed()*0.9999999999),
					(int) (current.getGreen()*0.9999999999),  (int) (current.getBlue()*0.9999999999));
		} else if (state == DAWN) {
			//go up to light blue
			//we need to make sure we don't exceed the light blue colour for day time

			int red = (int) ((current.getRed())*1.001);
			if (red > 120) {
				red = current.getRed();
			}
			int green = (int) ((1+current.getGreen())*1.001);
			if (green > 201) {
				green = current.getGreen();
			}
			//sky should get blue the fastest
			int blue = (int) ((1+current.getBlue())*1.01);
			if (blue >= 255) {
				blue = current.getBlue();
			}
			
			int alpha = (int) (current.getAlpha()*1.00000001);
			if (alpha >= 255) {
				alpha = current.getAlpha();
			}
			backgroundColour = new Color(red, green, blue, alpha);
		}
		else {
			//night time
			backgroundColour = new Color(0, 0, 0, 230);
		}
		this.setBackground(backgroundColour);
	}

	public void drawNorthRoom(Graphics g){
		if(viewDir.equals("north")){
			if ((int) player.getCurrentRoom().getBoardPos().getX() != 0) {
				Room northRoom = board.getRoomFromCoords((int)player.getCurrentRoom().getBoardPos().getX()-1,
						(int)player.getCurrentRoom().getBoardPos().getY());
				BufferedImage ground = loadImage("ground.png");
				g.drawImage(ground, gX-374, gY-176, null);
				for (int i = 0; i < 10; i++){
					for (int j = 9; j >= 0; j--){
						if (northRoom.getObstacles()[i][j] != null){
							drawDistantObject(g, northRoom.getObstacles()[i][j].getType(), j, i, -1);
						}
						if (northRoom.getItems()[i][j] != null){
							drawDistantObject(g, northRoom.getItems()[i][j].getType(), j, i, -1);
						}
					}
				}
			}
		}
		else if(viewDir.equals("east")){
			if ((int) player.getCurrentRoom().getBoardPos().getY() != 4) {
				Room northRoom = board.getRoomFromCoords((int)player.getCurrentRoom().getBoardPos().getX(),
						(int)player.getCurrentRoom().getBoardPos().getY()-1);
				BufferedImage ground = loadImage("ground.png");
				g.drawImage(ground, gX-374, gY-176, null);
				for (int i = 0; i < 10; i++){
					for (int j = 0; j < 10; j++){
						if (northRoom.getObstacles()[i][j] != null){
							drawDistantObject(g, northRoom.getObstacles()[i][j].getType(), 9-i, j, -1);
						}
						if (northRoom.getItems()[i][j] != null){
							drawDistantObject(g, northRoom.getItems()[i][j].getType(), 9-i, j, -1);
						}
					}
				}
			}
		}
		else if(viewDir.equals("south")){
			if ((int) player.getCurrentRoom().getBoardPos().getX() != 4) {
				Room northRoom = board.getRoomFromCoords((int)player.getCurrentRoom().getBoardPos().getX()+1,
						(int)player.getCurrentRoom().getBoardPos().getY());
				BufferedImage ground = loadImage("ground.png");
				g.drawImage(ground, gX-374, gY-176, null);
				for (int i = 9; i >= 0; i--){
					for (int j = 0; j < 10; j++){
						if (northRoom.getObstacles()[i][j] != null){
							drawDistantObject(g, northRoom.getObstacles()[i][j].getType(), 9-j, 9-i, -1);
						}
						if (northRoom.getItems()[i][j] != null){
							drawDistantObject(g, northRoom.getItems()[i][j].getType(), 9-j, 9-i, -1);
						}
					}
				}
			}
		}
		else if(viewDir.equals("west")){
			if ((int) player.getCurrentRoom().getBoardPos().getY() != 0) {
				Room northRoom = board.getRoomFromCoords((int)player.getCurrentRoom().getBoardPos().getX(),
						(int)player.getCurrentRoom().getBoardPos().getY()+1);
				BufferedImage ground = loadImage("ground.png");
				g.drawImage(ground, gX-374, gY-176, null);
				for (int i = 9; i >= 0; i--){
					for (int j = 9; j >= 0; j--){
						if (northRoom.getObstacles()[i][j] != null){
							drawDistantObject(g, northRoom.getObstacles()[i][j].getType(), i, 9-j, -1);
						}
						if (northRoom.getItems()[i][j] != null){
							drawDistantObject(g, northRoom.getItems()[i][j].getType(), i, 9-j, -1);
						}
					}
				}
			}
		}
	}

	public void drawDistantObject(Graphics g, String file, int x, int y, int SorE){
		int newX = 374*SorE;
		int newY = -176;

		newX = newX + gX + obX + (obW*x);
		newY = newY - (obH*x);

		newX = newX + (obW*y);
		newY = newY + gY + obY + (obH*y);

		BufferedImage object;
		try {
			object = ImageIO.read(new File("data/images/" + file + ".png"));
			g.drawImage(object, newX, newY, null);
		} catch (IOException e) {
			//e.printStackTrace();
			g.drawImage(defaultCube, newX, newY, null);
		}
	}

	public void drawEastRoom(Graphics g){
		if(viewDir.equals("north")){
			if((int)player.getCurrentRoom().getBoardPos().getY() != 4){
				Room eastRoom = board.getRoomFromCoords((int)player.getCurrentRoom().getBoardPos().getX(),
						(int)player.getCurrentRoom().getBoardPos().getY()+1);
				BufferedImage ground = loadImage("ground.png");
				g.drawImage(ground, gX+374, gY-176, null);
				for (int i = 0; i < 10; i++){
					for (int j = 9; j >= 0; j--){
						if (eastRoom.getObstacles()[i][j] != null){
							drawDistantObject(g, eastRoom.getObstacles()[i][j].getType(), j, i, 1);
						}
						if (eastRoom.getItems()[i][j] != null){
							drawDistantObject(g, eastRoom.getItems()[i][j].getType(), j, i, 1);
						}
					}
				}
			}
		}
		else if(viewDir.equals("east")){
			if((int)player.getCurrentRoom().getBoardPos().getX() != 0){
				Room eastRoom = board.getRoomFromCoords((int)player.getCurrentRoom().getBoardPos().getX() - 1,
						(int)player.getCurrentRoom().getBoardPos().getY());
				BufferedImage ground = loadImage("ground.png");
				g.drawImage(ground, gX+374, gY-176, null);
				for (int i = 0; i < 10; i++){
					for (int j = 0; j < 10; j++){
						if (eastRoom.getObstacles()[i][j] != null){
							drawDistantObject(g, eastRoom.getObstacles()[i][j].getType(), 9-i, j, 1);
						}
						if (eastRoom.getItems()[i][j] != null){
							drawDistantObject(g, eastRoom.getItems()[i][j].getType(), 9-i, j, 1);
						}
					}
				}
			}
		}
		else if(viewDir.equals("south")){
			if((int)player.getCurrentRoom().getBoardPos().getY() != 0){
				Room eastRoom = board.getRoomFromCoords((int)player.getCurrentRoom().getBoardPos().getX(),
						(int)player.getCurrentRoom().getBoardPos().getY()-1);
				BufferedImage ground = loadImage("ground.png");
				g.drawImage(ground, gX+374, gY-176, null);
				for (int i = 9; i >= 0; i--){
					for (int j = 0; j < 10; j++){
						if (eastRoom.getObstacles()[i][j] != null){
							drawDistantObject(g, eastRoom.getObstacles()[i][j].getType(), 9-j, 9-i, 1);
						}
						if (eastRoom.getItems()[i][j] != null){
							drawDistantObject(g, eastRoom.getItems()[i][j].getType(), 9-i, 9-i, 1);
						}
					}
				}
			}
		}
		else if(viewDir.equals("west")){
			if((int)player.getCurrentRoom().getBoardPos().getX() != 4){
				Room eastRoom = board.getRoomFromCoords((int)player.getCurrentRoom().getBoardPos().getX()+1,
						(int)player.getCurrentRoom().getBoardPos().getY());
				BufferedImage ground = loadImage("ground.png");
				g.drawImage(ground, gX+374, gY-176, null);
				for (int i = 9; i >= 0; i--){
					for (int j = 9; j >= 0; j--){
						if (eastRoom.getObstacles()[i][j] != null){
							drawDistantObject(g, eastRoom.getObstacles()[i][j].getType(), i, 9-j, 1);
						}
						if (eastRoom.getItems()[i][j] != null){
							drawDistantObject(g, eastRoom.getItems()[i][j].getType(), i, 9-j, 1);
						}
					}
				}
			}
		}
	}

	public void drawGround(Graphics g){
		BufferedImage ground = loadImage("ground.png");
		g.drawImage(ground, gX, gY, null);
	}

	/*
	 * Draws all obstacles in current room
	 *
	 * -Loops through the obstacles array stored in the current room
	 * -Loops from back to front so that all obstacles are displayed correctly
	 * -Calls the drawObject() method to draw each obstacle
	 */
	public void drawBoard(Graphics g){
		if(viewDir.equals("north")){
			for (int i = 0; i < 10; i++){
				for (int j = 9; j >= 0; j--){
					if (curRoom.getObstacles()[i][j] != null){
						if(curRoom.getObstacles()[i][j] instanceof Breakable){
							drawBreakable(g, curRoom.getObstacles()[i][j].getType(), j, i);
						}
						else{
							drawObject(g, curRoom.getObstacles()[i][j].getType(), j, i);
						}
					}
					if (curRoom.getItems()[i][j] != null){
						drawObject(g, curRoom.getItems()[i][j].getType(), j, i);
					}
					for(NPC npc : curRoom.getNpcs()){
						if(npc.getCurrentTile().getRoomCoords().equals(new Point(i, j))){
							drawEnemy(g, npc);
						}
					}
					for(Player p : board.getPlayers()){
						if(p != null && p != player && p.getCurrentRoom() == player.getCurrentRoom()){
							try{
								if (p.getCurrentRoom().calcTile(p.getCoords()).getRoomCoords().equals(new Point(i, j))){
									drawOtherPlayer(g, p);
								}
							}
							catch(NullPointerException e){
								System.out.println("Error drawing other player");
							}
						}
					}
					if (player != null && !hurt){
						if (player.getCurrentTile().getRoomCoords().equals(new Point(i, j))){
							drawPlayer(g);
						}
					}
				}
			}
		}
		else if(viewDir.equals("east")){
			for (int i = 0; i < 10; i++){
				for (int j = 0; j < 10; j++){
					if (curRoom.getObstacles()[i][j] != null){
						if(curRoom.getObstacles()[i][j] instanceof Breakable){
							drawBreakable(g, curRoom.getObstacles()[i][j].getType(), 9-i, j);
						}
						else{
							drawObject(g, curRoom.getObstacles()[i][j].getType(), 9-i, j);
						}
					}
					if (curRoom.getItems()[i][j] != null){
						drawObject(g, curRoom.getItems()[i][j].getType(), 9-i, j);
					}
					for(NPC npc : curRoom.getNpcs()){
						if(npc.getCurrentTile().getRoomCoords().equals(new Point(i, j))){
							drawEnemy(g, npc);
						}
					}
					if (player != null && !hurt){
						if (player.getCurrentTile().getRoomCoords().equals(new Point(i, j))){
							drawPlayer(g);
						}
					}
				}
			}
		}
		else if(viewDir.equals("south")){
			for (int i = 9; i >= 0; i--){
				for (int j = 0; j < 10; j++){
					if (curRoom.getObstacles()[i][j] != null){
						if(curRoom.getObstacles()[i][j] instanceof Breakable){
							drawBreakable(g, curRoom.getObstacles()[i][j].getType(), 9-j, 9-i);
						}
						else{
							drawObject(g, curRoom.getObstacles()[i][j].getType(), 9-j, 9-i);
						}
					}
					if (curRoom.getItems()[i][j] != null){
						drawObject(g, curRoom.getItems()[i][j].getType(), 9-j, 9-i);
					}
					for(NPC npc : curRoom.getNpcs()){
						if(npc.getCurrentTile().getRoomCoords().equals(new Point(i, j))){
							drawEnemy(g, npc);
						}
					}
					if (player != null && !hurt){
						if (player.getCurrentTile().getRoomCoords().equals(new Point(i, j))){
							drawPlayer(g);
						}
					}
				}
			}
		}
		else if(viewDir.equals("west")){
			for (int i = 9; i >= 0; i--){
				for (int j = 9; j >= 0; j--){
					if (curRoom.getObstacles()[i][j] != null){
						if(curRoom.getObstacles()[i][j] instanceof Breakable){
							drawBreakable(g, curRoom.getObstacles()[i][j].getType(), i, 9-j);
						}
						else{
							drawObject(g, curRoom.getObstacles()[i][j].getType(), i, 9-j);
						}
					}
					if (curRoom.getItems()[i][j] != null){
						drawObject(g, curRoom.getItems()[i][j].getType(), i, 9-j);
					}
					for(NPC npc : curRoom.getNpcs()){
						if(npc.getCurrentTile().getRoomCoords().equals(new Point(i, j))){
							drawEnemy(g, npc);
						}
					}
					if (player != null && !hurt){
						if (player.getCurrentTile().getRoomCoords().equals(new Point(i, j))){
							drawPlayer(g);
						}
					}
				}
			}
		}
	}
	
	public BufferedImage loadEnemyImage(String type, int dir, int state){
		BufferedImage sheet = loadImage(type + "SpriteSheet.png");
		BufferedImage enemyImage;
		if(viewDir.equals("east")){
			dir = (dir+=1)%4;
		}
		else if(viewDir.equals("south")){
			dir = (dir+=2)%4;
		}
		else if(viewDir.equals("west")){
			dir = (dir+=3)%4;
		}
		enemyImage = sheet.getSubimage(dir*70, (state/5)*72, 70, 72);
		return enemyImage;
	}

	public void drawEnemy(Graphics g, NPC npc){
		BufferedImage enemyIMG;
		if(npc instanceof EnemyStill){
			enemyIMG = loadImage(npc.getType() + ".png");
		}
		else if(npc instanceof EnemyWalker){
			enemyIMG = loadEnemyImage(npc.getType(), npc.getDir(), npc.getAnimationState());
		}
		else{
			enemyIMG = defaultCube;
		}
		int xDif = (int)(npc.getRealCoords().getX() - npc.getCurrentTile().getRealCoords().getX());
		int yDif = (int)(npc.getRealCoords().getY() - npc.getCurrentTile().getRealCoords().getY());
		int newX = (int)npc.getRealCoords().getX() - (obW);
		int newY = (int)npc.getRealCoords().getY() - (obH*2)-22;
		Point oldPoint = npc.getCurrentTile().getRoomCoords();
		
		if(viewDir.equals("east")){
			Point newPoint = new Point((int)(oldPoint.getY()), 9-(int)oldPoint.getX());
			Tile newTile = curRoom.getTileFromRoomCoords(newPoint);
			newX = (int)newTile.getRealCoords().getX() - obW - (yDif*2);
			newY = (int)newTile.getRealCoords().getY() - ((obH*2)-22) - (-xDif/2)-(obH*3)+4;
		}
		else if(viewDir.equals("south")){
			Point newPoint = new Point((int)(9-oldPoint.getX()), 9-(int)oldPoint.getY());
			Tile newTile = curRoom.getTileFromRoomCoords(newPoint);
			newX = (int)newTile.getRealCoords().getX() - obW - (xDif);
			newY = (int)newTile.getRealCoords().getY() - ((obH*2)-22) - (yDif)-(obH*3)+4;
		}
		else if(viewDir.equals("west")){
			Point newPoint = new Point((int)(9-oldPoint.getY()), (int)oldPoint.getX());
			Tile newTile = curRoom.getTileFromRoomCoords(newPoint);
			newX = (int)newTile.getRealCoords().getX() - obW - (-yDif*2);
			newY = (int)newTile.getRealCoords().getY() - ((obH*2)-22) - (xDif/2)-(obH*3)+4;
		}
		
		g.drawImage(enemyIMG, newX, newY, null);
		if(npc instanceof EnemyWalker){
			drawEnemyHealth(g, newX, newY, npc);
		}
	}

	public void drawPlayer(Graphics g){
		g.setColor(Color.black);
		int xDif = (int)(player.getCoords().getX() - player.getCurrentTile().getRealCoords().getX());
		int yDif = (int)(player.getCoords().getY() - player.getCurrentTile().getRealCoords().getY());
		int newX = (int)player.getCoords().getX() - 25;
		int newY = (int)player.getCoords().getY() - 80;
		Point oldPoint = player.getCurrentTile().getRoomCoords();
		if(viewDir.equals("north")){

		}
		else if(viewDir.equals("east")){
			Point newPoint = new Point((int)(oldPoint.getY()), 9-(int)oldPoint.getX());
			Tile newTile = curRoom.getTileFromRoomCoords(newPoint);
			newX = (int)newTile.getRealCoords().getX() - 25 - (yDif*2);
			newY = (int)newTile.getRealCoords().getY() - 80 - (-xDif/2);
		}
		else if(viewDir.equals("south")){
			Point newPoint = new Point((int)(9-oldPoint.getX()), 9-(int)oldPoint.getY());
			Tile newTile = curRoom.getTileFromRoomCoords(newPoint);
			newX = (int)newTile.getRealCoords().getX() - 25 - (xDif);
			newY = (int)newTile.getRealCoords().getY() - 80 - (yDif);
		}
		else if(viewDir.equals("west")){
			Point newPoint = new Point((int)(9-oldPoint.getY()), (int)oldPoint.getX());
			Tile newTile = curRoom.getTileFromRoomCoords(newPoint);
			newX = (int)newTile.getRealCoords().getX() - 25 - (-yDif*2);
			newY = (int)newTile.getRealCoords().getY() - 80 - (xDif/2);
		}
		updatePlayerImage();
		g.drawImage(playerIMG, newX, newY, null);
		int len = (player.getName().length()*8)/2;
		g.setFont(new Font("Courier new", 20, 15));
		g.drawString(player.getName(), (newX-len)+25, newY - 20);
		drawHealth(g, newX, newY, player);
	}

	public void drawOtherPlayer(Graphics g, Player p){
		g.setColor(Color.black);
		int newX = (int)p.getCoords().getX() - 25;
		int newY = (int)p.getCoords().getY() - 80;
		BufferedImage otherPlayerImage = getOtherPlayerImage(p);
		g.drawImage(otherPlayerImage, newX, newY, null);
		int len = (p.getName().length()*4);
		g.drawString(p.getName(), (newX-len)+13, newY - 20);
		g.drawString(String.valueOf(p.getWalkState()), (newX-len)+13, newY - 30);
		drawHealth(g, newX, newY, p);
	}

	public void drawHealth(Graphics g, int x, int y, Player p){
		g.setColor(Color.black);
		g.fillRect(x, y-10, 50, 5);
		g.setColor(Color.green);
		g.fillRect(x, y-10, p.getHealth()/2, 5);
	}
	
	public void drawEnemyHealth(Graphics g, int x, int y, NPC npc){
		g.setColor(Color.black);
		g.fillRect(x+10, y-10, 50, 5);
		g.setColor(Color.green);
		double health = ((double)npc.getHealth())/((double)npc.getDamage());
		g.fillRect(x+10, y-10, (int)(health*50), 5);
	}

	public void drawScore(Graphics g) {
		//draw the white background
		g.setColor(Color.white);
		g.fillRect(20, 535, 120, 28);

		int score = player.getScore();
		g.setColor(Color.BLACK);
		g.setFont(new Font("Times New Roman", Font.BOLD, 25));
		g.drawString("Score: "+score, 22, 557);
	}

	public void drawNorthWall(Graphics g){
		BufferedImage wall = loadImage("NorthTreesDoor.png");
		if(viewDir.equals("north")){
			if(curRoom.getNorth() == -1){
				wall = loadImage("NorthTrees.png");
			}
		}
		else if(viewDir.equals("east")){
			if(curRoom.getWest() == -1){
				wall = loadImage("NorthTrees.png");
			}
		}
		else if(viewDir.equals("south")){
			if(curRoom.getSouth() == -1){
				wall = loadImage("NorthTrees.png");
			}
		}
		else if(viewDir.equals("west")){
			if(curRoom.getEast() == -1){
				wall = loadImage("NorthTrees.png");
			}
		}
		g.drawImage(wall, wnX, wnY, null);
	}

	public void drawEastWall(Graphics g){
		BufferedImage wall = loadImage("EastTreesDoor.png");
		if(viewDir.equals("north")){
			if(curRoom.getEast() == -1){
				wall = loadImage("EastTrees.png");
			}
		}
		else if(viewDir.equals("east")){
			if(curRoom.getNorth() == -1){
				wall = loadImage("EastTrees.png");
			}
		}
		else if(viewDir.equals("south")){
			if(curRoom.getWest() == -1){
				wall = loadImage("EastTrees.png");
			}
		}
		else if(viewDir.equals("west")){
			if(curRoom.getSouth() == -1){
				wall = loadImage("EastTrees.png");
			}
		}
		g.drawImage(wall, weX, weY, null);
	}

	public void drawCompass(Graphics g){
		BufferedImage compass;
		try {
			compass = ImageIO.read(new File("data/images/compassNorth.png"));
			g.drawImage(compass, cX, cY, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void drawCharacter(Graphics g, String file, int x, int y){
		int newX = 0;
		int newY = 0;

		newX = newX + gX + charX + (charW*x);
		newY = newY - (charH*x);

		newX = newX + (charW*y);
		newY = newY + gY + charY + (charH*y);

		BufferedImage character;
		try {
			character = ImageIO.read(new File("data/images/" + file + ".png"));
			g.drawImage(character, newX, newY, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Draws an object given a file name and an x and y
	 *
	 * -Algorithm works out placement coordinates
	 * -Loads Buffered image using filename
	 * -Draws Buffered image to panel at correct coordinates
	 */
	public void drawObject(Graphics g, String file, int x, int y){
		int newX = 0;
		int newY = 0;

		newX = newX + gX + obX + (obW*x);
		newY = newY - (obH*x);

		newX = newX + (obW*y);
		newY = newY + gY + obY + (obH*y);

		BufferedImage object;
		try {
			object = ImageIO.read(new File("data/images/" + file + ".png"));
			g.drawImage(object, newX, newY, null);
		} catch (IOException e) {
			//e.printStackTrace();
			g.drawImage(defaultCube, newX, newY, null);
		}
	}

	public void drawBreakable(Graphics g, String file, int x, int y){
		BufferedImage breakablesIMG = loadImage("BreakablesSheet.png");
		int subY = 0;
		if(file.contains("cobblestone")){
			subY = 0;
		}
		else if(file.contains("vine")){
			subY = 1;
		}
		else if(file.contains("fire")){
			subY = 2;
		}
		else{
			subY = 3;
		}

		int subX = Integer.valueOf(String.valueOf(file.charAt(file.length()-1))) - 1;

		//System.out.println(subX + " | " + subY);

		int newX = 0;
		int newY = 0;

		newX = newX + gX + obX + (obW*x);
		newY = newY - (obH*x);

		newX = newX + (obW*y);
		newY = newY + gY + obY + (obH*y);

		g.drawImage(breakablesIMG.getSubimage(subX*70, subY*72, 70, 72), newX, newY, null);
	}

	public static Point calcRealCoords(Point p){
		int newX = 0;
		int newY = 0;

		newX = newX + gX + tileX + (tileW*(int)p.getY());
		newY = newY - (tileH*(int)p.getY());

		newX = newX + (tileW*(int)p.getX());
		newY = newY + gY + tileY + (tileH*(int)p.getX());
		return new Point(newX+tileW, newY+tileH);
	}

	/**
	 * This method should load an image in from a filename.
	 * @param filename
	 * @return
	 */
	public BufferedImage loadImage(String filename) {
		// using the URL means the image loads when stored
		// in a jar or expanded into individual files.
		try {
			BufferedImage img = ImageIO.read(new File(IMAGE_PATH + filename));
			return img;
		} catch (IOException e) {
			// we've encountered an error loading the image. There's not much we
			// can actually do at this point, except to abort the game.
			return defaultCube;
		}
	}

	public BufferedImage getOtherPlayerImage(Player player){
		BufferedImage sheet = loadImage("otherSpriteSheetWalking.png");
		BufferedImage playerImage = playerOtherIMG;
		if(player.isNorth()){
			if(player.isEast() && player.isWest()){
				playerImage = sheet.getSubimage(0, 82*player.getWalkState(), 50, 82);
			}
			else if(player.isWest()){
				playerImage = sheet.getSubimage(50, 82*player.getWalkState(), 50, 82);
			}
			else if(player.isEast()){
				playerImage = sheet.getSubimage(350, 82*player.getWalkState(), 50, 82);
			}
			else{
				playerImage = sheet.getSubimage(0, 82*player.getWalkState(), 50, 82);
			}
		}
		else if(player.isEast()){
			if(player.isNorth() && player.isSouth()){
				playerImage = sheet.getSubimage(300, 82*player.getWalkState(), 50, 82);
			}
			else if(player.isNorth()){
				playerImage = sheet.getSubimage(350, 82*player.getWalkState(), 50, 82);
			}
			else if(player.isSouth()){
				playerImage = sheet.getSubimage(250, 82*player.getWalkState(), 50, 82);
			}
			else{
				playerImage = sheet.getSubimage(300, 82*player.getWalkState(), 50, 82);
			}
		}
		else if(player.isSouth()){
			if(player.isEast() && player.isWest()){
				playerImage = sheet.getSubimage(200, 82*player.getWalkState(), 50, 82);
			}
			else if(player.isWest()){
				playerImage = sheet.getSubimage(150, 82*player.getWalkState(), 50, 82);
			}
			else if(player.isEast()){
				playerImage = sheet.getSubimage(250, 82*player.getWalkState(), 50, 82);
			}
			else{
				playerImage = sheet.getSubimage(200, 82*player.getWalkState(), 50, 82);
			}
		}
		else if(player.isWest()){
			if(player.isNorth() && player.isSouth()){
				playerImage = sheet.getSubimage(100, 82*player.getWalkState(), 50, 82);
			}
			else if(player.isNorth()){
				playerImage = sheet.getSubimage(50, 82*player.getWalkState(), 50, 82);
			}
			else if(player.isSouth()){
				playerImage = sheet.getSubimage(150, 82*player.getWalkState(), 50, 82);
			}
			else{
				playerImage = sheet.getSubimage(100, 82*player.getWalkState(), 50, 82);
			}
		}
		return playerImage;
	}

	public void updatePlayerImage(){
		BufferedImage sheet = loadImage("playerSpriteSheetWalking.png");
		BufferedImage playerImage = playerIMG;
		if(north){
			if(east && west){
				playerImage = sheet.getSubimage(0, 82*player.getWalkState(), 50, 82);
			}
			else if(west){
				playerImage = sheet.getSubimage(50, 82*player.getWalkState(), 50, 82);
			}
			else if(east){
				playerImage = sheet.getSubimage(350, 82*player.getWalkState(), 50, 82);
			}
			else{
				playerImage = sheet.getSubimage(0, 82*player.getWalkState(), 50, 82);
			}
		}
		else if(east){
			if(north && south){
				playerImage = sheet.getSubimage(300, 82*player.getWalkState(), 50, 82);
			}
			else if(north){
				playerImage = sheet.getSubimage(350, 82*player.getWalkState(), 50, 82);
			}
			else if(south){
				playerImage = sheet.getSubimage(250, 82*player.getWalkState(), 50, 82);
			}
			else{
				playerImage = sheet.getSubimage(300, 82*player.getWalkState(), 50, 82);
			}
		}
		else if(south){
			if(east && west){
				playerImage = sheet.getSubimage(200, 82*player.getWalkState(), 50, 82);
			}
			else if(west){
				playerImage = sheet.getSubimage(150, 82*player.getWalkState(), 50, 82);
			}
			else if(east){
				playerImage = sheet.getSubimage(250, 82*player.getWalkState(), 50, 82);
			}
			else{
				playerImage = sheet.getSubimage(200, 82*player.getWalkState(), 50, 82);
			}
		}
		else if(west){
			if(north && south){
				playerImage = sheet.getSubimage(100, 82*player.getWalkState(), 50, 82);
			}
			else if(north){
				playerImage = sheet.getSubimage(50, 82*player.getWalkState(), 50, 82);
			}
			else if(south){
				playerImage = sheet.getSubimage(150, 82*player.getWalkState(), 50, 82);
			}
			else{
				playerImage = sheet.getSubimage(100, 82*player.getWalkState(), 50, 82);
			}
		}
		playerIMG = playerImage;
	}

	public Board getBoard(){
		return board;
	}
	
	public static int getState(){
		return state;
	}

	public void setViewDir(String dir){
		this.viewDir = dir;
	}

	public void setTime(int newTime){
		this.time = newTime;
	}

	public int getTime(){
		return this.time;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("\nx: " + e.getX() + "\ny: " + e.getY());
		if (tile.isOn(e.getX(), e.getY())){
			System.out.println("Inside");
		}
		else{
			System.out.println("Outside");
		}
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

}
