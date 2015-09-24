package game;

public class DestinysWild {

	private Board board = XMLParser.initialiseBoard("data/state.xml");

	public DestinysWild(){
		board.printBoard();
		System.out.println();
		board.getBoard()[2][2].printRoom();
		System.out.println();
		board.getBoard()[1][2].printRoom();

	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public static void main(String[] args){
		new DestinysWild();
	}
}
