
package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import game.items.Item;

/**
 * The board consists of a 5x5 array of Rooms. Each Room contains 10x10 tiles.The board is initialised from the board.xml
 * file or savestate.xml should it exist. The initialised Board is stored as a field in DestinysWild.java.
 * @author Rob
 */
public class Board implements Serializable{

	private Room[][] board = new Room[5][5];
	private List<Item> offBoardItems = new ArrayList<>(); //iterated through to figure out items in player's inventory
	private Set<Player> players = new HashSet<Player>();

	/**
	 * Empty constructor for initialising from file
	 */
	public Board() {

	}

	/**
	 * adds an item to the list of off board items.
	 * @param item to be added to list of off board items
	 */
	public void addOffBoardItem(Item item){
		offBoardItems.add(item);
	}

	/**
	 *Returns the list of off board items
	 * @return List<Item> off board items
	 */
	public List<Item> getOffBoardItems(){
		return offBoardItems;
	}

	/**
	 * Gets an off board item by its id
	 * @param id of item to get
	 * @return The item requested
	 */
	public Item getOffItemFromId(int id){
		for(Item item : offBoardItems){
			if(item.getId() == id){
				return item;
			}
		}
		System.out.println("No such Item in offBoardItem list");
		throw new NullPointerException();
	}

	/**
	 * Gets a room on the board from its board coordinates
	 * @param row of the requested room
	 * @param col of the requested room
	 * @return the Room requested
	 */
	public Room getRoomFromCoords(int row, int col){
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				if(board[i][j] != null && (board[i][j].getBoardPos().x == row && board[i][j].getBoardPos().y == col)){
					return board[i][j];
				}
			}
		}
		System.out.println("Could not find room with coords: " + row + ", " + col + " on the board :(");
		return null;
	}

	/**
	 * Gets a room on the board from its id
	 * @param id of the room requested
	 * @return the Room requested
	 */
	public Room getRoomFromId(int id){
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				if(board[i][j] != null && id == board[i][j].getId()){
					return board[i][j];
				}
			}
		}
		System.out.println("Could not find room with id: " + id + " on the board :(");
		return null;
	}

	/**
	 * @return the board
	 */
	public Room[][] getBoard() {
		return board;
	}

	/**
	 * Adds a room to the board at a given location
	 * @param room to be added
	 * @param x row of the room on the board
	 * @param y col of the room on the board
	 */
	public void addRoom(Room room, int x, int y) {
		board[x][y] = room;
	}

	/**
	 * This method should provide a basic, text based look at the board in its
	 * current state.
	 */
	public void printBoard() {
		for (int i = 0; i < board.length; i++) {
			System.out.print("| ");
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == null) {
					System.out.print("null | ");
				} else {
					System.out.print(board[i][j].getId() + " | ");
				}
			}
			System.out.println();
		}
	}

	/**
	 * Gets the set of all players on this server
	 * @return Set<Player> players in server
	 */
	public Set<Player> getPlayers() {
		return players;
	}

	/**
	 * adds a player to the set of players
	 * @param player to be added
	 */
	public void addPlayers(Player player){
		players.add(player);
	}

	/**
	 * gets a player by user name from the set of players
	 * @param userName of player requested
	 * @return Player requested
	 */
	public Player getPlayer(String userName){
		for (Player player : this.players){
			//	System.out.println(player.getName() + " is in the board");
			if (player.getName().equalsIgnoreCase(userName)){
				return player;
			}
		}
		System.out.println("No player with that name found");
		return null;
	}

	/**
	 * removes a player from the set of players
	 * @param player to be removed
	 */
	public void removePlayers(Player player){
		players.remove(player);
	}

}
