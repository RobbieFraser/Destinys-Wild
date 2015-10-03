package Renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import game.Board;
import game.Player;
import game.Room;

public class GameImagePanel extends JPanel implements MouseListener, KeyListener {
	
	private Board board; //The main Board object given from the main Game class
	private Player player; //The player that the user controls
	private Point curRoomCoords = new Point(2,2); //Temporary?
	private Room curRoom; //The current Room Object
	
	private static final String IMAGE_PATH = "data/images/"; //The path to all the images
	
	private BufferedImage defaultCube; //The default image
	private BufferedImage waterSprite; //Testing
	private BufferedImage water; //Testing
	private BufferedImage playerIMG = loadImage("playerSpriteSheet.png").getSubimage(104, 0, 26, 82); //The image that is drawn for the player
	
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
	private int cY = 50; //Compass y
	
	private int wnX = -24; //Wall North x
	private int wnY = 54; //Wall North y
	
	private int viewDir = 0;
	
	private int red;
	private int green;
	private int blue;
	
	private boolean up = true;
	
	private TileTest tile = new TileTest(70, 34, new Point(500,200));
	
	public GameImagePanel(Board board){
		red = 120;
		green = 201;
		blue = 255;
		this.setBackground(new Color(red, green, blue));
		this.board = board;
		curRoom = board.getBoard()[(int)curRoomCoords.getX()][(int)curRoomCoords.getY()];
		player = new Player("Matt", new Point(546, 287), curRoom);
		setDefault();
		addMouseListener(this);
		addKeyListener(this);
		//waterTest();
	}
	
	public GameImagePanel(Board board, Player player){
		red = 120;
		green = 201;
		blue = 255;
		this.setBackground(new Color(red, green, blue));
		this.board = board;
		this.player = player;
		curRoom = player.getCurrentRoom();
		setDefault();
		addMouseListener(this);
		//waterTest();
	}
	
	public void waterTest(){
		while(true){
			for(int i = 0; i < 4; i++){
				for(int j = 0; j < 4; j++){
					waterSprite = water.getSubimage(j*70, i*34, 70, 34);
					this.repaint();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
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
//        drawNorthWall(g, "treesNorth1");
        drawGround(g);
        drawCompass(g);
//        drawObject(g, "stoneCube", 6, 5);
//        drawObject(g, "stoneCube", 5, 5);
//        drawCharacter(g, "personIdleWest", 6, 6);
//        drawObject(g, "mossyStone1", 6, 7);
//        drawObject(g, "brokenStone1", 3, 2);
//        drawCharacter(g, "personIdleWest", 4, 5);
//        drawCharacter(g, "personIdleSouth", 2, 5);
//        tile.Draw(g);
        drawObstacles(g);
//        g.drawImage(waterSprite, 384, 428, null);
//        g.drawImage(waterSprite, 384+obW, 428+obH, null);
//        g.drawImage(waterSprite, 384, 428+(obH*2), null);
//        g.drawImage(waterSprite, 384-obW, 428+obH, null);
    }
	
	public void drawGround(Graphics g){
		BufferedImage ground;
		try {
			ground = ImageIO.read(new File("data/images/ground.png"));
			g.drawImage(ground, gX, gY, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Draws all obstacles in current room
	 * 
	 * -Loops through the obstacles array stored in the current room
	 * -Loops from back to front so that all obstacles are displayed correctly
	 * -Calls the drawObject() method to draw each obstacle 
	 */
	public void drawObstacles(Graphics g){
		for(int i = 0; i < 10; i++){
			for(int j = 9; j >= 0; j--){
				if(curRoom.getObstacles()[i][j] != null){
					drawObject(g, curRoom.getObstacles()[i][j].getType(), j, i);
				}
				if(curRoom.getItems()[i][j] != null){
					drawObject(g, curRoom.getItems()[i][j].getType(), j, i);
				}
				if(curRoom.getNpcs()[i][j] != null){
					drawObject(g, curRoom.getNpcs()[i][j].getType(), j, i);
				}
				if(player != null){
					if(player.calcTile().getRoomCoords().equals(new Point(i, j))){
						drawPlayer(g);
					}
				}
			}
		}
	}
	
	public void drawPlayer(Graphics g){
		int newX = (int)player.getCoords().getX() - 12;
		int newY = (int)player.getCoords().getY() - 80;
		updatePlayerImage();
		g.drawImage(playerIMG, newX, newY, null);
	}
	
	public void drawNorthWall(Graphics g, String file){
		BufferedImage wall;
		try {
			wall = ImageIO.read(new File("data/images/treesNorth1.png"));
			g.drawImage(wall, wnX, wnY, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public static BufferedImage loadImage(String filename) {
		// using the URL means the image loads when stored
		// in a jar or expanded into individual files.
		try {
			BufferedImage img = ImageIO.read(new File(IMAGE_PATH + filename));
			return img;
		} catch (IOException e) {
			// we've encountered an error loading the image. There's not much we
			// can actually do at this point, except to abort the game.
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}
	
//	public void loadPlayerImages(){
//		BufferedImage sheet = loadImage("playerSpriteSheet");
//		BufferedImage playerImage = sheet.getSubimage(0, 0, 26, 82);
//	}
	
	public void updatePlayerImage(){
		BufferedImage sheet = loadImage("playerSpriteSheet.png");
		BufferedImage playerImage = playerIMG;
		if(player.isNorth()){
			if(player.isEast() && player.isWest()){
				playerImage = sheet.getSubimage(0, 0, 26, 82);
			}
			else if(player.isWest()){
				playerImage = sheet.getSubimage(26, 0, 26, 82);
			}
			else if(player.isEast()){
				playerImage = sheet.getSubimage(182, 0, 26, 82);
			}
			else{
				playerImage = sheet.getSubimage(0, 0, 26, 82);
			}
		}
		else if(player.isEast()){
			if(player.isNorth() && player.isSouth()){
				playerImage = sheet.getSubimage(156, 0, 26, 82);
			}
			else if(player.isNorth()){
				playerImage = sheet.getSubimage(182, 0, 26, 82);
			}
			else if(player.isSouth()){
				playerImage = sheet.getSubimage(130, 0, 26, 82);
			}
			else{
				playerImage = sheet.getSubimage(156, 0, 26, 82);
			}
		}
		else if(player.isSouth()){
			if(player.isEast() && player.isWest()){
				playerImage = sheet.getSubimage(104, 0, 26, 82);
			}
			else if(player.isWest()){
				playerImage = sheet.getSubimage(78, 0, 26, 82);
			}
			else if(player.isEast()){
				playerImage = sheet.getSubimage(182, 0, 26, 82);
			}
			else{
				playerImage = sheet.getSubimage(104, 0, 26, 82);
			}
		}
		else if(player.isWest()){
			if(player.isNorth() && player.isSouth()){
				playerImage = sheet.getSubimage(52, 0, 26, 82);
			}
			else if(player.isNorth()){
				playerImage = sheet.getSubimage(26, 0, 26, 82);
			}
			else if(player.isSouth()){
				playerImage = sheet.getSubimage(78, 0, 26, 82);
			}
			else{
				playerImage = sheet.getSubimage(52, 0, 26, 82);
			}
		}
		playerIMG = playerImage;
	}
	
	public Board getBoard(){
		return board;
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

	@Override
	public void keyPressed(KeyEvent arg0) {
		System.out.println("Key Pressed");
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
