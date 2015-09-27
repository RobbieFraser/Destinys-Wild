package view;

import java.awt.Color;
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
import game.obstacles.Block;
import game.obstacles.Breakable;
import game.obstacles.Obstacle;

/**
 * A Minimap should be an item that will be used in the player
 * interface. It will display all rooms that the player has been in
 * so far, and show the connections between the rooms. It should also
 * show the location of the player.
 * @author DanielYska
 *
 */
public class Map extends JComponent {
	private Player player;
	private static final Image STONE_BLOCK = GameInterface.loadImage("stoneblocktiny.png"); 

	public Map(Player player) {
		this.player = player;
	}

	@Override
	protected void paintComponent(Graphics g) {

		//will be:
		//List<Room> rooms = player.getVisitedRooms();
		List<Room> rooms = new ArrayList<Room>();
		/*
		 * Basic room generator being used for testing
		 */
		for (int i = 1; i < 4; ++i) {
			for (int j = 1; j < 4; ++j) {
				Room room = new Room(0, 0, 0, 0, -100, new Point(i,j));
				room.addObstacle(new Block(), 2, 4);
				room.addObstacle(new Breakable("breakable", null), 5, 1);
				rooms.add(room);
			}
		}

		//put up a black background so that rooms that aren't drawn
		//are shown to be unexplored
		for (int i = 0; i < 250; ++i) {
			for (int j = 0; j < 250; ++j) {
				g.setColor(Color.BLACK);
				g.drawRect(i, j, 1, 1);
			}
		}

		super.paintComponent(g);

		//draw each room individually
		for (Room room: rooms) {
			Point point = room.getBoardPos();
			int xCoord = point.x * 50;
			int yCoord = point.y * 50;

			//draw in the background of the room first, including any obstacles
			for (int i = xCoord; i < 50 + xCoord; ++i) {
				for (int j = yCoord; j < 50 + yCoord; ++j) {
					//check if there is an obstacle to draw
					if (room.getObstacles()[(i-xCoord)/5][(j-yCoord)/5] != null) {
						Obstacle obstacle = room.getObstacles()[(i-xCoord)/5][(j-yCoord)/5];
						//draw each obstacle differently
						//TODO: Add images
						if (obstacle instanceof Block) {
							//draw the stone block image
							g.drawImage(STONE_BLOCK, i, j, null, null);
						} else {
							g.setColor(Color.BLACK);
							g.drawRect(i, j, 1, 1);
						}
					} else {
						//print out normal unoccupied square (forest floor)	
						g.setColor(new Color(172,211,115));
						g.drawRect(i, j, 1, 1);
					}
				}
			}

			//draw in walls or doors in y direction
			for (int i = xCoord; i <= xCoord + 50; ++i) {
				for (int j = yCoord; j <= yCoord + 50; j += 50) {
					//check for north door
					if (room.getNorth() != -1 && i >= (20 + xCoord) && i < (30 + xCoord)
							&& j == yCoord) {
						//if we are here, then a north door is present, so nothing will be
						//drawn to show the gap in the wall
					}
					//check for south door
					else if (room.getSouth() != -1 && i >= (20 + xCoord) && i < (30 + xCoord)
							&& j == yCoord + 50) {
						//if we are here, then a south door is present, so nothing will be
						//drawn to show the gap in the wall
					}
					else {
						//safe to draw the wall
						g.setColor(new Color(0,100,0));
						g.drawRect(i, j, 1, 1);
					}

				}
			}
			System.out.println();

			//draw in walls or doors in x direction
			for (int i = xCoord; i <= xCoord + 50; i += 50) {
				for (int j = yCoord; j <= yCoord + 50; ++j) {
					//check for east door
					if (room.getEast() != -1 && j >= (20 + yCoord) && j < (30 + yCoord)
							&& i == xCoord) {
						//if we are here, then a east door is present, so nothing will be
						//drawn to show the gap in the wall
					}
					//check for west door
					else if (room.getWest() != -1 && j >= (20 + yCoord) && j < (30 + yCoord)
							&& i == xCoord + 50) {
						//if we are here, then a west door is present, so nothing will be
						//drawn to show the gap in the wall
					}
					else {
						//safe to draw the wall
						g.setColor(new Color(0,100,0));
						g.drawRect(i, j, 1, 1);
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
