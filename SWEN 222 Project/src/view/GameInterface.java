package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import clientServer.packets.MovePacket;
import renderer.GameImagePanel;
import game.Board;
import game.DestinysWild;
import game.Player;
import game.items.Health;
import game.items.Item;
import game.items.Tool;

public class GameInterface{
	private static final int MAX_FOOD = 5;
	private static final int MAX_TOOLS = 6;
	private static final int CYCLE_LEFT = -2;
	private static final int CYCLE_RIGHT = -1;

	public JFrame frame;
	private Player player; //player whose game state will be drawn
	private Board board;
	private DestinysWild game;
	private GameImagePanel gamePanel;
	private CountDownLatch latch;
	private String orientation;

	public GameInterface(Player player, DestinysWild game, Board board, CountDownLatch latch) {
		this.latch = latch;
		this.player = player;
		this.board = board;
		this.game = game;
		this.orientation = "north";
		gamePanel = new GameImagePanel(board, player);
		initialiseInterface();
		updateUI();
		frame.setVisible(true);
		latch.countDown();
	}

	/**
	 * This method should initialise the game interface.
	 * It should load the gamePanel to be the background
	 * image, and then set up the inventory panel and
	 * the minimap.
	 */
	private void initialiseInterface() {
		frame = new JFrame();
		frame.setTitle("Destiny's Wild");
		frame.setBounds(100, 30, 1100, 750);
		frame.setResizable(false);
		//frame.addMouseListener(this);
		//set the background to be the game panel
		frame.setContentPane(gamePanel);

		// set up border
		Border blackline = BorderFactory.createLineBorder(Color.BLACK, 2);

		// set up inventory panel
		ImagePanel inventoryPanel = new ImagePanel("inventory.png");
		inventoryPanel.setOpaque(false);
		inventoryPanel.setBounds(20, 570, 790, 140);
		inventoryPanel.setLayout(null);

		// setting up food panel
		// setting up food slots - these will be filled in when the player has picked up food
		// 5 slots
		for (int i = 0; i < MAX_FOOD; ++i) {
			JLabel inventoryBox = new JLabel(new ImageIcon(MenuInterface.loadImage("itemBox.png")));
			inventoryBox.setBounds(20 + i * 88, 30, 78, 78);
			inventoryBox.setToolTipText("Food slot "+(i+1)+" - press "+(i+1)+" to select.");
			inventoryPanel.add(inventoryBox);
		}

		// set up tool panel
		// max 6 tools
		for (int i = 0; i < 2; ++i) {
			//2 rows of tools, each row contains 3 slots
			for (int j = 0; j < 3; ++j) {
				JLabel toolBox = new JLabel(new ImageIcon(MenuInterface.loadImage("toolBox.png")));
				toolBox.setBounds(465 + j * 65, 10 + i * 65, 56, 56);
				inventoryPanel.add(toolBox);
			}
		}

		// set up key slot
		// player can only hold 1 key at a time
		JLabel keyBox = new JLabel(new ImageIcon(MenuInterface.loadImage("keyBox.png")));
		keyBox.setBounds(670, 20, 100, 100);
		inventoryPanel.add(keyBox);

		frame.getContentPane().add(inventoryPanel);

		//intialise the selected item to be the first food slot as a default
		updateSelectedSlot(0);

		//set up panel which will contain minimap
		JPanel mapPanel = new JPanel(new BorderLayout());
		mapPanel.setBorder(blackline);
		mapPanel.setBounds(830, 460, 250, 250);
		//extract the board we will use
		//create the map that will be drawn
		Map map = new Map(player, board);
		mapPanel.add(map, BorderLayout.CENTER);
		frame.getContentPane().add(mapPanel);

		//initialise window listener
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				escapeGame();
			}
		});

		//add key listener
		//note that is a multi key listener, so multiple keys
		//can be held down at once
		frame.addKeyListener(new MultiKeyListener(this));

		int delay = 33; //milliseconds
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				updateUI();
			}
		};
		new Timer(delay, taskPerformer).start();

		frame.getContentPane().setLayout(null);
	}

	/**
	 * This method should be called whenever the game
	 * state changes. It should update the user interface,
	 * drawing in all of the player's items onto the
	 * inventory panel. It should then redraw the interface.
	 */
	public void updateUI() {
		// draw the inventory
		ImagePanel inventoryPanel = (ImagePanel) frame.getContentPane().getComponent(0);

		// first lets draw the food
		int numFood = player.numHealthItems();
		for (int i = 0; i < numFood; ++i) {
			//extract the foodLabel from the inventoryPanel
			JLabel foodLabel = (JLabel) inventoryPanel.getComponent(i);
			//set the labels image to be the food
			drawBackgroundImage(foodLabel, "itemBox", i);
		}

		//we should make sure that any slots that do not contain food
		//do not display food, so we reset the image back to an empty slot
		for (int i = numFood; i < 5; ++i) {
			JLabel toolLabel = (JLabel) inventoryPanel.getComponent(i);
			Image slotBackgroundImage = MenuInterface.loadImage("itemBox.png");
			toolLabel.setIcon(new ImageIcon(slotBackgroundImage));
		}

		//now we can draw in weapons
		int numTools = player.numToolItems();
		for (int i = 0; i < numTools; ++i) {
			//extract the tool label from the inventory label
			JLabel toolLabel = (JLabel) inventoryPanel.getComponent(i + MAX_FOOD);
			//set the labels image to be a tool
			drawBackgroundImage(toolLabel, "toolBox", i + MAX_FOOD);
		}

		JLabel keyLabel = (JLabel) inventoryPanel.getComponent(MAX_FOOD + MAX_TOOLS);
		Image keySlotBackgroundImage = MenuInterface.loadImage("keyBox.png");
		
		int numKeyItems = player.numKeyItems();
		keyLabel.setIcon(new ImageIcon(keySlotBackgroundImage));
		if (numKeyItems == 5) {
			//player has all pieces of the key, and the key itself
			//only the key should be drawn
			drawBackgroundImage(keyLabel, "keyBox", numKeyItems+MAX_FOOD+MAX_TOOLS);
		}
		//player has some key pieces
		else if (numKeyItems > 0) {
			for (int i = 0; i < numKeyItems; ++i) {
				//need to draw each key piece individually
				drawBackgroundImage(keyLabel, "keyBox", i+MAX_FOOD+MAX_TOOLS);
			}
		}

		//the interface should be updated
		frame.revalidate();
		//the repaint method suggests to the board to repaint the frame
		//this may update the minimap if changes have been made to it
		frame.repaint();
	}

	/**
	 * This method should be called when the image of
	 * an inventory slot should be redrawn with an
	 * item's image.
	 * @param inventoryLabel label that new image
	 * 		is being drawn onto
	 * @param type of inventory item
	 * @param index of slot within the inventoryLabel
	 * 		to draw on latch
	 */
	private void drawBackgroundImage(JLabel inventoryLabel, String type, int index) {
		//firstly, we set the background image to be the image in question
		Image itemImage = null;
		
		if (type.equals("keyBox")) {
			//first of all, we want to extract the current background image
			Image currentBackgroundImage = ((ImageIcon) inventoryLabel.getIcon()).getImage();
			
			//get graphics from background to draw with
			Graphics g = currentBackgroundImage.getGraphics();
			
			//depending on the number of key pieces the player has picked
			//up, different parts of the key should be drawn
			BufferedImage keyImage = (BufferedImage) MenuInterface.loadImage("keyTest.png");
			if (index == 11) {
				//draw piece 1
				itemImage = keyImage.getSubimage(0, 24, 27, 40);
				g.drawImage(itemImage, 7, 1, null);
			} else if (index == 12) {
				//draw piece 2
				itemImage = keyImage.getSubimage(27, 30, 15, 35);
				g.drawImage(itemImage, 60, 15, null);
			} else if (index == 13) {
				//draw piece 3
				itemImage = keyImage.getSubimage(39, 0, 15, 65);
				g.drawImage(itemImage, 20, 40, null);
			} else if (index == 14) {
				//draw piece 4
				itemImage = keyImage.getSubimage(53, 0, 27, 65);
				g.drawImage(itemImage, 60, 55, null);
			} else {
				//draw entire key
				g.drawImage(keyImage, 8, 18, null);
			}

			//update background image
			inventoryLabel.setIcon(new ImageIcon(currentBackgroundImage));
		} 
		else {
			//food or tool slot
			String imageName = getName(index);
			itemImage = MenuInterface.loadImage(imageName+".png");
			inventoryLabel.setIcon(new ImageIcon(itemImage));

			//now we will draw the border over the top of the image of the tool
			Image slotBackgroundImage = MenuInterface.loadImage(type+".png");
			//get graphics from image to draw with
			Graphics g = itemImage.getGraphics();
			g.drawImage(slotBackgroundImage, 0, 0, null, null);

			//update the panel description (hover over text)
			if (type.equals("itemBox")) {
				//1st - 5th slot
				inventoryLabel.setToolTipText(imageName+" - press "+(index+1)+" to select.");
			}
		}
	}

	/**
	 * This method should take as a parameter a number
	 * between 0 and 12.
	 * It should analyse the value of the item at this
	 * position of the inventory, and return a unique
	 * string which will be used to extract the image
	 * of this item.
	 * @param index of item to check
	 * @return name of image to use
	 */
	private String getName(int index) {
		//first, sanity check
		if (index < 0 || index > 11) {
			throw new Error("Invalid index exception");
		}

		if (index < 5) {
			//health
			String healthItemName = player.getInventory().get(index).getType();
			return healthItemName + "Icon";
		} else {
			String toolItemName = ((Tool) player.getInventory().get(index - MAX_FOOD + player.numHealthItems())).getType();
			return toolItemName + "Icon";
		}
	}

	public void changeTime(){
		gamePanel.changeTime();
	}

	/**
	 * This method should be called when the user has
	 * indicated they want to exit the game. It should
	 * prompt the user if they want to exit the game,
	 * and if they do, then the game should be exited.
	 */
	private void escapeGame() {
		int result = JOptionPane.showConfirmDialog(frame, "Exit the application?");
		if (result == JOptionPane.OK_OPTION) {
			//User really wants to exit
			//Send a disconnection packet to the client
			game.disconnect();
			System.exit(0);
		}
	}


	/**
	 * This method should draw a blue border around the
	 * an inventory slot at the given index the inventory panel.
	 */
	private void updateSelectedSlot(int index) {
		if (index < -2 || index > 5) {
			throw new Error("Invalid index");
		}

		ImagePanel inventoryPanel = (ImagePanel) frame.getContentPane().getComponent(0);

		int indexOfCurrentSelectedItem = 0;
		//first, we need to remove the blue border from the previous selected item
		for (int i = 0; i < 5; ++i) {
			//number of slots is small, so we can just iterate through them all
			JLabel tempLabel = (JLabel) inventoryPanel.getComponent(i);
			if (!(tempLabel.getBorder() instanceof EmptyBorder)) {
				//found currently selected item
				indexOfCurrentSelectedItem = i;
			}
			tempLabel.setBorder(BorderFactory.createEmptyBorder());
		}
		//selected border set up
		Border selectedBorder = BorderFactory.createLineBorder(Color.BLUE, 4);
		JLabel selectedLabel = null;

		if (index == CYCLE_LEFT) {
			//check for special case, have to loop around
			if (indexOfCurrentSelectedItem == 0) {
				selectedLabel = (JLabel) inventoryPanel.getComponent(4);
			} else {
				//go to previous slot
				selectedLabel = (JLabel) inventoryPanel.getComponent(indexOfCurrentSelectedItem - 1);
			}
		} else if (index == CYCLE_RIGHT) {
			//check for special case, have to loop around
			if (indexOfCurrentSelectedItem == 4) {
				selectedLabel = (JLabel) inventoryPanel.getComponent(0);
			} else {
				//go to previous slot
				selectedLabel = (JLabel) inventoryPanel.getComponent(indexOfCurrentSelectedItem + 1);
			}
		} else {
			//not cycling through
			selectedLabel = (JLabel) inventoryPanel.getComponent(index);
		}

		//now we draw the selected border on the correct choice
		selectedLabel.setBorder(selectedBorder);

		//redraw interface
		frame.revalidate();
		frame.repaint();
	}


	/**
	 * This method should return the item in the inventory
	 * that is currently "selected". If an empty slot is
	 * currently selected, null is returned.
	 * @return Item that is currently selected
	 */
	public Item getSelectedItem() {
		ImagePanel inventoryPanel = (ImagePanel) frame.getContentPane().getComponent(0);

		for (int i = 0; i < 5; ++i) {
			//only food can be selected
			JLabel tempLabel = (JLabel) inventoryPanel.getComponent(i);
			if (!(tempLabel.getBorder() instanceof EmptyBorder)) {
				//this label must contain the selected item
				//get the ith item
				return player.getInventory().get(i);
			}
		}
		return null;
	}

	/**
	 * This method should be called when the user has pressed
	 * a key. If the key they have pressed is relevant to the
	 * game, then this method should carry out that keys action.
	 * Otherwise, the function is exited.
	 * @param arg0 - Keyevent of the keypress
	 */
	public void handleKeyPress(KeyEvent arg0, Set<Character> chars) {
		int keyCode = arg0.getKeyCode();

		//handle number presses
		if (49 <= keyCode && keyCode < 54) {
			//user has pressed 1-5
			updateSelectedSlot(keyCode-49);
		}

		switch (keyCode) {
		case KeyEvent.VK_W:
			//player moved up one square
			setPlayerDirection(keyCode);
			createMovePacket();
			break;
		case KeyEvent.VK_A:
			//player moved left one square
			setPlayerDirection(keyCode);
			createMovePacket();
			break;
		case KeyEvent.VK_S:
			//player moved down one square
			setPlayerDirection(keyCode);
			createMovePacket();
			break;
		case KeyEvent.VK_D:
			//player moved right one square
			setPlayerDirection(keyCode);
			createMovePacket();
			break;
		case KeyEvent.VK_SHIFT:
			Item selectedItem = getSelectedItem();
			if (selectedItem != null && selectedItem instanceof Health) {
				clearKeysPressed();
				player.tryEat(selectedItem.getId());
			}
			break;
		case KeyEvent.VK_SPACE:
			player.tryInteract();
			break;
		case KeyEvent.VK_ESCAPE:
			//check if the user wants to escape the game
			escapeGame();
			break;
		case KeyEvent.VK_P:
			//user wants to pause the game.
			gamePaused();
			break;
		case KeyEvent.VK_Q:
			updateSelectedSlot(CYCLE_LEFT);
			break;
		case KeyEvent.VK_E:
			updateSelectedSlot(CYCLE_RIGHT);
			break;
		case KeyEvent.VK_M:
			PlayMusic.toggleMusic();
			break;
		case KeyEvent.VK_LEFT:
			setNextOrientation(CYCLE_LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			setNextOrientation(CYCLE_RIGHT);
			break;
		}
	}
	
	/**
	 * This method should be called whenever a played has moved.
	 * It should extract the player's current state, and create
	 * a movePacket out of these, which should be sent to the
	 * server.
	 */
	private void createMovePacket() {
		String name = game.getCurrentPlayer().getName();
		int xCoord = game.getCurrentPlayer().getCoords().x;
		int yCoord = game.getCurrentPlayer().getCoords().y;
		int id = game.getCurrentPlayer().getCurrentRoom().getId();
		int health = game.getCurrentPlayer().getHealth();
		boolean isNorth = game.getCurrentPlayer().isNorth();
		boolean isEast = game.getCurrentPlayer().isEast();
		boolean isSouth = game.getCurrentPlayer().isSouth();
		boolean isWest = game.getCurrentPlayer().isWest();
		int walkState = game.getCurrentPlayer().getWalkState();
		boolean allowGate = game.getCurrentPlayer().getAllowGate();
		MovePacket movePacket = new MovePacket(name, xCoord, yCoord,
				id, health, isNorth, isEast, isSouth, isWest, walkState,allowGate);
		movePacket.writeData(DestinysWild.getMultiplayer().getClient());
	}

	/**
	 * This method should set the player's direction according
	 * to their key press as well as their current
	 * orientation field.
	 * @param keyCode
	 */
	private void setPlayerDirection (int keyCode) {
		if (keyCode == KeyEvent.VK_W && orientation.equals("north")
				|| keyCode == KeyEvent.VK_D && orientation.equals("east")
				|| keyCode == KeyEvent.VK_S && orientation.equals("south")
				|| keyCode == KeyEvent.VK_A && orientation.equals("west")) {
			player.setNorth(true);
		}
		if (keyCode == KeyEvent.VK_D && orientation.equals("north")
				|| keyCode == KeyEvent.VK_S && orientation.equals("east")
				|| keyCode == KeyEvent.VK_A && orientation.equals("south")
				|| keyCode == KeyEvent.VK_W && orientation.equals("west")) {
			player.setEast(true);
		}
		if (keyCode == KeyEvent.VK_S && orientation.equals("north")
				|| keyCode == KeyEvent.VK_A && orientation.equals("east")
				|| keyCode == KeyEvent.VK_W && orientation.equals("south")
				|| keyCode == KeyEvent.VK_D && orientation.equals("west")) {
			player.setSouth(true);
		}
		if (keyCode == KeyEvent.VK_A && orientation.equals("north")
				|| keyCode == KeyEvent.VK_W && orientation.equals("east")
				|| keyCode == KeyEvent.VK_D && orientation.equals("south")
				|| keyCode == KeyEvent.VK_S && orientation.equals("west")) {
			player.setWest(true);
		}
	}

	/**
	 * This method should update the orientation field to be
	 * the next orientation in the north, east, south, west
	 * cycle. The direction that the cycle is traversed comes
	 * from the direction parameter.
	 * @param direction to go through cycle
	 */
	private void setNextOrientation(int direction) {
		String[] orientations = {"north", "east", "south", "west"};

		if (direction == CYCLE_RIGHT) {
			if (orientation.equals("west")) {
				orientation = "north";
			} else {
				loop: for (int i = 0; i < 3; ++i) {
					if (orientations[i].equals(orientation)) {
						orientation = orientations[i+1];
						break loop;
					}
				}
			}
		}
		if (direction == CYCLE_LEFT) {
			if (orientation.equals("north")) {
				orientation = "west";
			} else {
				loop: for (int i = 3; i > 0; --i) {
					if (orientations[i].equals(orientation)) {
						orientation = orientations[i-1];
						break loop;
					}
				}
			}
		}
		gamePanel.setViewDir(orientation);
	}

	/**
	 * This method should be called when the KeyListener
	 * detects that the user has released a key. If the
	 * key was a direction key, then they should stop moving
	 * in that direction.
	 * @param e
	 */
	protected void handleKeyRelease(KeyEvent e){
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_W && orientation.equals("north")
				|| keyCode == KeyEvent.VK_D && orientation.equals("east")
				|| keyCode == KeyEvent.VK_S && orientation.equals("south")
				|| keyCode == KeyEvent.VK_A && orientation.equals("west")) {
			player.setNorth(false);
			createMovePacket();
		}
		if (keyCode == KeyEvent.VK_D && orientation.equals("north")
				|| keyCode == KeyEvent.VK_S && orientation.equals("east")
				|| keyCode == KeyEvent.VK_A && orientation.equals("south")
				|| keyCode == KeyEvent.VK_W && orientation.equals("west")) {
			player.setEast(false);
			createMovePacket();

		}
		if (keyCode == KeyEvent.VK_S && orientation.equals("north")
				|| keyCode == KeyEvent.VK_A && orientation.equals("east")
				|| keyCode == KeyEvent.VK_W && orientation.equals("south")
				|| keyCode == KeyEvent.VK_D && orientation.equals("west")) {
			player.setSouth(false);
			createMovePacket();

		}
		if (keyCode == KeyEvent.VK_A && orientation.equals("north")
				|| keyCode == KeyEvent.VK_W && orientation.equals("east")
				|| keyCode == KeyEvent.VK_D && orientation.equals("south")
				|| keyCode == KeyEvent.VK_S && orientation.equals("west")) {
			player.setWest(false);
			createMovePacket();
		}
	}

	/**
	 * This method should be called when an interface pops up
	 * in front of the game screen. It should empty the set of
	 * keys currently being held down, so the player does not
	 * continue moving while the game is paused.
	 */
	private void clearKeysPressed() {
		player.setNorth(false);
		player.setWest(false);
		player.setEast(false);
		player.setSouth(false);
		player.setMoving(false);
	}

	/**
	 * This method should be called when the player has pressed 'p'
	 * and it should bring up the paused game interface. It should
	 * handle the various options the interface provides.
	 */
	private void gamePaused() {
		clearKeysPressed();
		game.setPaused(true);

		String[] buttons = {"Quit and Save Game", "Toggle Music", "Resume Game" };

		int rc = JOptionPane.showOptionDialog(null, "The Game has been Paused - What would you like to do?", "Settings",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[2]);
		if (rc == 2) {
			//user wants to continue playing the game
			game.setPaused(false);
		} else if (rc == 1) {
			//player wants to toggle the music
			PlayMusic.toggleMusic();
		} else if (rc == 0) {
			//player wants to quit the game
			escapeGame();
		} else {
			//dead code
			throw new Error("Invalid pause menu option.");
		}
	}

	public GameImagePanel getGameImagePanel(){
		return gamePanel;
	}
}
