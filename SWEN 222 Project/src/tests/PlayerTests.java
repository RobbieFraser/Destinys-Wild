package tests;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import game.Player;
import game.Room;
import game.items.Health;
import game.items.Item;
import game.items.Key;
import game.items.Tool;

public class PlayerTests {
	private Player player = new Player("Testing Tom", new Point(0,0), 100,
			new Room(-1,-1,-1,-1,50,new Point(0,0)), new ArrayList<Room>(), new ArrayList<Item>(), 100);
	
	/**
	 * Player should be able to cut down vines if they
	 * have the machete.
	 */
	@Test
	public void validHasTool() {
		player.addInventoryItem(new Tool("machete", 50));
		assertTrue(player.hasTool("vine"));
	}
	
	/**
	 * Player shouldn't have the tool to cut down vines
	 * by default.
	 */
	@Test
	public void notValidHasTool() {
		assertFalse(player.hasTool("vine"));
	}
	
	/**
	 * Walk cycle should cycle through 0 to 8 and loop around.
	 */
	@Test
	public void validWalkCycle() {
		for (int i = 0; i < 16; ++i) {
			if (player.getWalkState() != i%8) {
				fail();
			}
			player.updateWalkCycle();
		}
	}
	
	/**
	 * Has key method should return true if the player has
	 * a key.
	 */
	@Test
	public void validKey() {
		player.addInventoryItem(new Key(5, new Point(0,0)));
		assertTrue(player.hasKey());
	}
	
	/**
	 * Should only be able to make a key if the player has 5 pieces.
	 */
	@Test
	public void validKeyPieces() {
		for (int i = 0; i < 4; ++i) {
			player.setKeyPieces(i);
			if (player.tryMakeKey()) {
				fail();
			}
		}
	}
	
	/**
	 * Player should not be able to remove anything from their
	 * empty inventory.
	 */
	@Test
	public void notValidRemoveInventoryIndex() {
		try {
			player.removeInventoryItem(0);
			fail();
		} catch (IndexOutOfBoundsException e) {
			
		}
	}
	
	/**
	 * Inventory should be able to handle a null input.
	 */
	@Test
	public void ValidRemoveInventoryItem() {
		player.removeInventoryItem(null);
	}
	
	/**
	 * Inventory should be sorted with health items first.
	 */
	@Test
	public void validSortedInventory() {
		player.addInventoryItem(new Tool("machete", 40));
		Health apple = new Health("apple", new Point(0,0), 5, 100);
		player.addInventoryItem(apple);
		player.addInventoryItem(new Key(5,new Point(3,2)));
		List<Item> inventory = player.getInventory();
		assertTrue(inventory.get(0).equals(apple));
	}
	
	/**
	 * Inventory should be empty.
	 */
	@Test
	public void validEmptyInventory() {
		List<Item> inventory = player.getInventory();
		assertEquals(inventory.size(), 0);
	}
	
}
