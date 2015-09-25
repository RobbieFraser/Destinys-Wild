package game;

import Renderer.TestFrame;

public class DestinysWild {
	private Board board;

	public DestinysWild() {
		//read board in from file
		board = XMLParser.initialiseBoard("data/state.xml");
		
		board.printBoard();
		System.out.println();
		board.getBoard()[2][2].printRoom();
		System.out.println();
		board.getBoard()[1][2].printRoom();
		//TestFrame test = new TestFrame(board);
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public static void main(String[] args){
		DestinysWild game = new DestinysWild();
	}
}
