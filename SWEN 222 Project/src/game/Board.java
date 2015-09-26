package game;

import java.util.ArrayList;
import java.util.List;

import game.items.Item;

public class Board {

	private Room[][] board = new Room[5][5];
	private List<Item> offBoard = new ArrayList<>();

	public Board() {

	}
	
	public void addOffBoardItem(Item item){
		offBoard.add(item);
	}
	
	public List<Item> getOffBoardItems(){
		return offBoard;
	}
	
	public Room getRoomFromId(int id){
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				if(id == board[i][j].getId()){
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

	

}
