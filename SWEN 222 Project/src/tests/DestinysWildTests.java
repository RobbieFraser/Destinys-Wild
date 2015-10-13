
package tests;

import static org.junit.Assert.*;

import javax.swing.JFrame;
import org.junit.Test;
import game.DestinysWild;
import game.Room;
import game.XMLParser;
public class DestinysWildTests {
	private DestinysWild game = new DestinysWild();
	private JFrame frame = new JFrame();
	
	@Test
	public void validSetUpGame() {
		game.newGame("Sam", frame);
		if (game.getMultiplayer() == null || game.getBoard() == null
				|| game.getPlayer() == null) {
			fail();
		}
	}
	
	@Test
	public void validBoardCreated() {
		game.newGame("Sam", frame);
		Room[][] board = game.getBoard().getBoard();
		
	}
}




