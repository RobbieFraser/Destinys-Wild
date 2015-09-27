package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JFrame;

import game.Board;
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
	private Image mapImage;
	private Board board;
	private static Point playerCoord;
	private static final Image STONE_BLOCK = GameInterface.loadImage("stoneblocktiny.png"); 

	public Map(Player player, Board board) {
		this.player = player;
		initialiseMapImage();
	}

	/**
	 * This method should be called to initialise the
	 * base image for what the map of the board will look
	 * like. This image can be stored so that when
	 * paintComponent is called, this image can simply be
	 * painted over. This should reduce the amount of work
	 * that is done in updating the map each time the
	 * player moves.
	 */
	private void initialiseMapImage() {
		//create image that will be the background
		mapImage = new BufferedImage(250, 250, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = ((BufferedImage) mapImage).createGraphics();

		//will be:
		//List<Room> rooms = board.getRooms();
		List<Room> rooms = new ArrayList<Room>();
		/*
		 * Basic room generator being used for testing
		 */
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j) {
				Room room = new Room(0, 0, 0, 0, -100, new Point(i,j));
				room.addObstacle(new Breakable("breakable", null), 2, 4);
				room.addObstacle(new Breakable("breakable", null), 5, 1);
				rooms.add(room);
			}
		}

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

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		//put up a black background so that rooms that aren't drawn
		//are shown to be unexplored
		//List<Room> rooms = player.getVisitedRooms();
		List<Room> visitedRooms = new ArrayList<Room>();

		visitedRooms.add(new Room(0, 0, 0, 0, -100, new Point(2,2)));
		visitedRooms.add(new Room(0, 0, 0, 0, -100, new Point(2,3)));
		visitedRooms.add(new Room(0, 0, 0, 0, -100, new Point(1,2)));

		g.drawImage(mapImage, 0, 0, null, null);

		//draw black onto each room that has not been visited yet
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j) {
				boolean visitedRoom = false;
				//check if this room has been visited before
				for (Room room: visitedRooms) {
					if (room.getBoardPos().equals(new Point(i,j))) {
						//been here before
						visitedRoom = true;
					}
				}
				if (!visitedRoom) {
					//haven't visted this room
					//so user shouldn't be able to see it
					g.setColor(Color.DARK_GRAY);
					g.fillRect(i*50, j*50, 50, 50);
				}
			}
		}

		//now draw location of player
		//Room currentRoom = player.getCurrentRoom();
		Room currentRoom = visitedRooms.get(2);
		Point roomCoord = currentRoom.getBoardPos();
		//Point coord = player.getCoords();
		//playerCoord = new Point(5,8);

		g.setColor(Color.MAGENTA);
		g.fillRect(roomCoord.x * 50 + playerCoord.x * 5,
				roomCoord.y * 50 + playerCoord.y * 5, 5, 5);

	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setPreferredSize(new Dimension(300,300));
					Player player = new Player("Sam", new Point(0,0), new Room(-1, -1, -1, -1, 13, new Point(1,3)));
					Map map = new Map(player, null);
					frame.add(map);
					frame.pack();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		for (int i = 0; i < 10; ++i) {
			for (int j = 0; j < 10; ++j) {
				try {
					//sending the actual Thread of execution to sleep X milliseconds
					playerCoord = new Point(i,j);
					frame.revalidate();
					frame.repaint();
					Thread.sleep(100);
				} catch (InterruptedException ie) {}
			}
		}
	}
}
