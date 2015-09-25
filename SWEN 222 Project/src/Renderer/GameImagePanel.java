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
	
	private Board board;
	private Point curRoomCoords = new Point(2,2);
	private Room curRoom;
	
	private int gX = 10;
	private int gY = 300;
	
	private int obX = 0;
	private int obY = 104;
	private int obW = 34;
	private int obH = 16;
	
	private int charX = 24;
	private int charY = 82;
	private int charW = 34;
	private int charH = 16;
	
	private int cX = 500;
	private int cY = 50;
	
	private int wnX = -24;
	private int wnY = 54;
	
	private TileTest tile = new TileTest(70, 34, new Point(500,200));
	
	public GameImagePanel(Board board){
		this.board = board;
		curRoom = board.getBoard()[(int)curRoomCoords.getX()][(int)curRoomCoords.getY()];
		addMouseListener(this);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        drawNorthWall(g, "treesNorth1");
        drawGround(g);
//        drawCompass(g);
//        drawObject(g, "stoneCube", 6, 5);
//        drawObject(g, "stoneCube", 5, 5);
//        drawCharacter(g, "personIdleWest", 6, 6);
//        drawObject(g, "mossyStone1", 6, 7);
//        drawObject(g, "brokenStone1", 3, 2);
//        drawCharacter(g, "personIdleWest", 4, 5);
//        drawCharacter(g, "personIdleSouth", 2, 5);
//        tile.Draw(g);
        drawObstacles(g);
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
			e.printStackTrace();
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
