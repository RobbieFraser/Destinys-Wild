package Renderer;

import game.Board;

public class LevelEditor {
	Board board;
	
	public LevelEditor(){
		board = new Board();
		TestFrame test = new TestFrame(true, board);
	}
	
	public static void main(String[] args){
		LevelEditor main = new LevelEditor();
	}
}
