package tests;

import static org.junit.Assert.*;
import java.awt.Point;
import org.junit.Test;
import game.Room;
import game.Tile;
import game.items.Health;
import game.items.Item;
import game.npcs.EnemyWalker;
import game.npcs.NPC;

public class RoomTests {
	
	private Room testingRoom = new Room(0,0,0,0,100,new Point(0,0));

	@Test
	public void validItemFromId() {
		Item apple = new Health("apple", new Point(0,0), 0, 0);
		testingRoom.addItem(apple, 0, 0);
		assertEquals(apple, testingRoom.getItemFromId(0));
	}
	
	@Test
	public void notValidItemFromId() {
		assertEquals(null, testingRoom.getItemFromId(0));
	}
	
	@Test
	public void validNPCFromId() {
		NPC walker = new EnemyWalker("bats", 0, new Point(0,0), 0, 0, testingRoom);
		testingRoom.addNpc(walker);
		assertEquals(testingRoom.getNpcFromId(0), walker);
	}
	
	@Test
	public void notValidNPCFromId() {
		assertEquals(testingRoom.getNpcFromId(0), null);
	}
	
	@Test
	public void notValidCalcTile() {
		assertEquals(null,testingRoom.calcTile(new Point(0,0)));
	}
	
	@Test
	public void notValidInRoom() {
		assertFalse(testingRoom.currTileIsInRoom(null));
	}
	
	@Test
	public void validInRoom() {
		assertTrue(testingRoom.currTileIsInRoom(new Tile(new Point(0,0), testingRoom, true)));
	}
}
