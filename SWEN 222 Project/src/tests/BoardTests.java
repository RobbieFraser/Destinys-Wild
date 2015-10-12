package tests;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import game.Board;
import game.Player;
import game.Room;
import game.XMLParser;
import game.items.Tool;

public class BoardTests {
	Board board = XMLParser.initialiseBoard("data/board.xml");
	
	@Test
	public void validItemFromId() {
		board.addOffBoardItem(new Tool("machete",4));
		assertNotEquals(board.getOffItemFromId(4), null);
	}
	
	@Test
	public void notValidItemFromId() {
		board.addOffBoardItem(new Tool("machete",4));
		assertEquals(board.getOffItemFromId(-1), null);
	}
	
	@Test
	public void validGetRoom() {
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j) {
				if (board.getRoomFromCoords(i,j) == null) {
					fail();
				}
			}
		}
	}
	
	@Test
	public void notValidGetRoom() {
		assertEquals(board.getRoomFromCoords(0,-1), null);
	}
	
	@Test
	public void validGetRoomFromID() {
		for (int i = 0; i < 25; ++i) {
			assertNotEquals(board.getRoomFromId(i), null);
		}
	}
	
	@Test
	public void notValidGetRoomFromID() {
		assertEquals(board.getRoomFromId(26), null);
	}
	
	@Test
	public void validGetPlayer() {
		board.addPlayers(new Player("Tom", new Point(2,2), new Room(0,0,0,0,0,new Point(0,0))));
		assertNotEquals(board.getPlayer("Tom"), null);
	}
	
	@Test
	public void notValidGetPlayer() {
		assertEquals(board.getPlayer("Tom"), null);
	}
}
