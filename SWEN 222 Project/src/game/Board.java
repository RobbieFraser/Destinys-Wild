package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import game.items.Item;

public class Board implements Serializable{

	private Room[][] board = new Room[5][5];
	private List<Item> offBoardItems = new ArrayList<>();
	private Set<Player> players = new HashSet<Player>();

	public Board() {

	}

	public void addOffBoardItem(Item item){
		offBoardItems.add(item);
	}

	public List<Item> getOffBoardItems(){
		return offBoardItems;
	}

	public Item getOffItemFromId(int id){
		for(Item item : offBoardItems){
			if(item.getId() == id){
				return item;
			}
		}
		System.out.println("No such Item in offBoardItem list");
		return null;
	}

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

	public Set<Player> getPlayers() {
		return players;
	}

	public void addPlayers(Player player){
		players.add(player);
	}

	public Player getPlayer(String userName){
		for(Player player : this.players){
		//	System.out.println(player.getName() + " is in the board");
			if(player.getName().equalsIgnoreCase(userName)){
				return player;
			}
		}
		System.out.println("No player with that name found");
		return null;
	}

	public void removePlayers(Player player){
		players.remove(player);
	}


}
