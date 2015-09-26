package view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import game.Player;
import game.Room;

/**
 * A Minimap should be an item that will be used in the player
 * interface. It will display all rooms that the player has been in
 * so far, and show the connections between the rooms. It should also
 * show the location of the player.
 * @author DanielYska
 *
 */
public class Map extends JComponent {
	private static Image BLANK = GameInterface.loadImage("creme.jpeg");
	private static Image DOOR = GameInterface.loadImage("black.png");
	private static Image WALL = GameInterface.loadImage("red.jpeg");
	private Player player;
	
	public Map(Player player) {
		this.player = player;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		List<Room> rooms = new ArrayList<Room>();
		/*
		 * Basic room generator being used for testing
		 */
		for (int i = 1; i < 2; ++i) {
			for (int j = 1; j < 2; ++j) {
				Room room = new Room(0, 0, -1, -1, -100, new Point(i,j));
				rooms.add(room);
			}
		}

		super.paintComponent(g);

		//draw each room individually
		//TODO: Make minimap more readable - cannot distinguish between walls and doors
		for (Room room: rooms) {
			Point point = room.getBoardPos();
			int xCoord = point.x * 50;
			int yCoord = point.y * 50;
			
			//draw in the room first
			for (int i = xCoord; i < 50 + xCoord; ++i) {
				for (int j = yCoord; j < 50 + yCoord; ++j) {
					g.drawImage(BLANK, i, j, null, null);
				}
			}
			
			//draw in walls or doors in y direction
			for (int i = xCoord; i <= xCoord + 50; ++i) {
				for (int j = yCoord; j <= yCoord + 50; j += 50) {
					//check for north door
					if (room.getNorth() != -1 && i >= (24 + yCoord) && i <= (26 + yCoord)
							&& j == xCoord) {
						g.drawImage(DOOR, i, j, null, null);
					}
					//check for south door
					else if (room.getSouth() != -1 && i >= (24 + yCoord) && i <= (26 + yCoord)
							&& j == xCoord + 50) {
						g.drawImage(DOOR, i, j, null, null);
					}
					else {
						//safe to draw the wall
						g.drawImage(WALL, i, j, null, null);
					}

				}
			}

			//draw in walls or doors in x direction
			for (int i = xCoord; i <= xCoord + 50; i += 50) {
				for (int j = yCoord; j <= yCoord + 50; ++j) {
					//check for east door
					if (room.getEast() != -1 && j >= (24 + yCoord) && j <= (26 + yCoord)
							&& i == xCoord) {
						g.drawImage(DOOR, i, j, null, null);
					}
					//check for west door
					else if (room.getWest() != -1 && j >= (24 + yCoord) && j <= (26 + yCoord)
							&& i == xCoord + 50) {
						g.drawImage(DOOR, i, j, null, null);
					}
					else {
						//safe to draw the wall
						g.drawImage(WALL, i, j, null, null);
					}
				}
			}
		}
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new JFrame();
					frame.setPreferredSize(new Dimension(300,300));
					
					Player player = new Player("Sam", new Point(0,0), new Room(-1, -1, -1, -1, 13, new Point(1,3)));
					Map map = new Map(player);
					frame.add(map);
					
					frame.pack();
					frame.setVisible(true);	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
