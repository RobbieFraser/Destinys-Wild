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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
 */
public class Map extends JComponent {
	private Player player;
	private Image mapImage;
	private Board board;
	private static final int BOARD_LENGTH = 5;

	public Map(Player player, Board board) {
		this.player = player;
		this.board = board;
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
		Graphics2D graphics = ((BufferedImage) mapImage).createGraphics();

		//read in the rooms from the board
		Room[][] rooms = board.getBoard();
		//draw each room individually
		for (int i = 0; i < BOARD_LENGTH; ++i) {
			for (int j = 0; j < BOARD_LENGTH; ++j) {
				Room room = rooms[i][j];
				//only draw rooms that have been initialised
				if (room != null) {
					initialiseRoomImage(graphics, room);
				}
			}
		}
	}

	/**
	 * This method should draw to the map image a single room.
	 * @param graphics
	 * @param room
	 */
	private void initialiseRoomImage(Graphics graphics, Room room) {
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
					switch (obstacle.getType()) {
					case "brokenblock":
						graphics.setColor(new Color(143, 143, 143));
						graphics.fillRect(j, i, 1, 1);
						break;
					case "brokenstone1":
						graphics.setColor(new Color(143, 143, 143));
						graphics.fillRect(j, i, 1, 1);
						break;
					case "stoneblock":
						graphics.setColor(new Color(143, 143, 143));
						graphics.fillRect(j, i, 1, 1);
						break;
					case "cobblestone":
						graphics.setColor(new Color(143, 143, 143));
						graphics.fillRect(j, i, 1, 1);
						break;
					}
				} else {
					//print out normal unoccupied square (forest floor)
					graphics.setColor(new Color(172,211,115));
					graphics.fillRect(j, i, 1, 1);
				}
			}
		}

		//draw in walls or doors in y direction
		for (int j = xCoord; j <= xCoord + 50; j += 50) {
			for (int i = yCoord; i <= yCoord + 50; ++i) {
				//check for north door
				if (room.getNorth() != -1 && i >= (20 + yCoord) && i < (30 + yCoord)
						&& j == xCoord) {
					//if we are here, then a north door is present, so nothing will be
					//drawn to show the gap in the wall
				}
				//check for south door
				else if (room.getSouth() != -1 && i >= (20 + yCoord) && i < (30 + yCoord)
						&& j == xCoord + 50) {
					//if we are here, then a south door is present, so nothing will be
					//drawn to show the gap in the wall
				}
				else {
					//safe to draw the wall
					graphics.setColor(new Color(0,100,0));
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}

		//draw in walls or doors in x direction
		for (int i = xCoord; i <= xCoord + 50; ++i) {
			for (int j = yCoord; j <= yCoord + 50; j += 50) {
				//check for west door
				if (room.getWest() != -1 && i >= (20 + xCoord) && i < (30 + xCoord)
						&& j == yCoord) {
					//if we are here, then a west door is present, so nothing will be
					//drawn to show the gap in the wall
				}
				//check for east door
				else if (room.getEast() != -1 && i >= (20 + xCoord) && i < (30 + xCoord)
						&& j == yCoord + 50) {
					//if we are here, then a east door is present, so nothing will be
					//drawn to show the gap in the wall
				}
				else {
					//safe to draw the wall
					graphics.setColor(new Color(0,100,0));
					graphics.fillRect(j, i, 1, 1);
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
		List<Room> visitedRooms = player.getVisitedRooms();

		g.drawImage(mapImage, 0, 0, null, null);

		boolean drawBlack = true; //just used for testing
		if (drawBlack) {
			//draw black onto each room that has not been visited yet
			for (int i = 0; i < BOARD_LENGTH; ++i) {
				for (int j = 0; j < BOARD_LENGTH; ++j) {
					//check if this room has been visited before
					boolean visitedRoom = false;
					for (Room room: visitedRooms) {
						if (room.getBoardPos().equals(new Point(i,j))) {
							//been here before
							visitedRoom = true;
						}
					}

					if (!visitedRoom) {
						//haven't visited this room
						//so user shouldn't be able to see it
						g.setColor(Color.DARK_GRAY);
						g.fillRect(j*50, i*50, 50, 50);

						//draw in gaps for doors
						drawDoorGaps(g, i, j);
					}
				}
			}
		}

		//draw all the players
		Set<Player> players = board.getPlayers();
		Iterator<Player> iterator = players.iterator();
		while (iterator.hasNext()) {
			Player player = iterator.next();
			//now draw location of player
			Room currentRoom = player.getCurrentRoom();
			Point roomCoord = currentRoom.getBoardPos();
			Point coord = player.getCurrentTile().getRoomCoords();

			if (player.equals(this.player)) {
				//draw current player as magenta
				g.setColor(new Color(109, 137, 153));
			} else {
				//draw every other player as cyan
				g.setColor(new Color(171, 150, 110));
				//System.out.println("Other player is on :"+roomCoord.x + " " +roomCoord.y);
			}
			//need to check if the players room is inside the current players visited rooms
			//otherwise their dot will be floating in space
			if ((this.player.getVisitedRooms().contains(currentRoom))) {
				g.fillRect(roomCoord.y * 50 + coord.y * 5, roomCoord.x * 50 + coord.x * 5, 5, 5);
			}
		}
	}

	/**
	 * This method should be called whenever a grey square
	 * is drawn on top of a room. This square could
	 * obscure the doors leading to discovered rooms,
	 * making it hard for the player to see where they can go.
	 * This method should draw gaps for the doors onto the map.
	 * @param g graphics that will do the drawing
	 * @param i x coordinate on the board
	 * @param j y coordinate on the board
	 */
	private void drawDoorGaps(Graphics g, int i, int j) {
		List<Room> visitedRooms = player.getVisitedRooms();
		Room room = board.getBoard()[i][j];
		//set colour to default green
		g.setColor(new Color(172,211,115));

		if (room.getNorth() != -1 && i > 0) {
			//there is a door to the north
			if (visitedRooms.contains(board.getBoard()[i-1][j])) {
				//we've been in this room to the north
				g.fillRect(j*50+20, i*50, 10, 1);
			}
		}
		if (room.getEast() != -1 && j < BOARD_LENGTH - 1) {
			//there is a door to the east
			if (visitedRooms.contains(board.getBoard()[i][j+1])) {
				//we've been in this room to the east
				g.fillRect((j+1)*50, i*50+20, 1, 10);
			}
		}
		if (room.getSouth() != -1 && i < BOARD_LENGTH - 1) {
			//there is a door to the south
			if (visitedRooms.contains(board.getBoard()[i+1][j])) {
				//we've been in this room to the south
				g.fillRect(j*50+20, (i+1)*50, 10, 1);
			}
		}
		if (room.getWest() != -1 && j > 0) {
			//there is a door to the west
			if (visitedRooms.contains(board.getBoard()[i][j-1])) {
				//we've been in this room to the west
				g.fillRect(j*50, i*50+20, 1, 10);
			}
		}
	}
}
