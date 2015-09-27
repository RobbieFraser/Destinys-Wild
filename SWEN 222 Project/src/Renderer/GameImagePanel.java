package Renderer;

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
import game.Room;

public class GameImagePanel extends JPanel implements MouseListener, KeyListener {
	
	private Board board; //The main Board object given from the main Game class
	private Point curRoomCoords = new Point(2,2); //Temporary?
	private Room curRoom; //The current Room Object
	
	private BufferedImage defaultCube; //The default image
	private BufferedImage waterSprite; //Testing
	private BufferedImage water; //Testing
	
	private int gX = 10; //Ground x
	private int gY = 300; //Ground y
	
	private int obX = 0; //Object x
	private int obY = 104; //Object y
	private int obW = 34; //Object width
	private int obH = 16; //Object Height
	
	private int charX = 24; //Player x
	private int charY = 82; //Player y
	private int charW = 34; //Player width 
	private int charH = 16; //Player Height
	
	private int cX = 500; //Compass x
	private int cY = 50; //Compass y
	
	private int wnX = -24; //Wall North x
	private int wnY = 54; //Wall North y
	
	private int viewDir = 0;
	
	private TileTest tile = new TileTest(70, 34, new Point(500,200));
	
	public GameImagePanel(Board board){
		this.board = board;
		curRoom = board.getBoard()[(int)curRoomCoords.getX()][(int)curRoomCoords.getY()];
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
			}
		}
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
