package renderer;

import game.Board;
import game.XMLParser;

public class LevelEditor {
	Board board;

	public LevelEditor(){
		board = XMLParser.initialiseBoard("data/board.xml");
		TestFrame test = new TestFrame(true, board);
	}

	public static void main(String[] args){
		LevelEditor main = new LevelEditor();
	}
}