package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import Renderer.GameImagePanel;
import game.Board;
import game.DestinysWild;
import game.Player;
import menu.ImagePanel;
import menu.MenuInterface;

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

	public GameInterface(Player player, DestinysWild game, Board board, CountDownLatch latch) {
		this.latch = latch;
		this.player = player;
		this.board = board;
		this.game = game;
		gamePanel = new GameImagePanel(board, player);
		initialiseInterface();
		updateUI();
		frame.setVisible(true);
		latch.countDown();
		//TODO: Add support for L / R Arrows keys - Change perspective
		//TODO: Improve Pause menu
	}
	
	public void setInterface(Player player, DestinysWild game, Board board, CountDownLatch latch){
		this.latch = latch;
		this.player = player;
		this.board = board;
		this.game = game;
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
				if ((i * 3 + j) == 4) {
					//10th item
					toolBox.setToolTipText("Tool slot "+(i*3+j+1)+" - press 0 to select.");
				} else if ((i * 3 + j) == 5) {
					//11th item
					toolBox.setToolTipText("Tool slot "+(i*3+j+1)+" - press - to select.");
				} else {
					//0 - 9th item
					toolBox.setToolTipText("Tool slot "+(i*3+j+1)+" - press "+(i*3+j+6)+" to select.");
				}
				inventoryPanel.add(toolBox);
			}
		}

		// set up key slot
		// player can only hold 1 key at a time
		JLabel keyBox = new JLabel(new ImageIcon(MenuInterface.loadImage("keyBox.png")));
		keyBox.setToolTipText("Key Slot - press = to select.");
		keyBox.setBounds(670, 20, 100, 100);
		inventoryPanel.add(keyBox);

		frame.getContentPane().add(inventoryPanel);
		//intialise the selected item to be the first food slot as a default
		updateSelectedSlot(0);

		// set up panel which will contain minimap
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

		int delay = 10; //milliseconds
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

		//System.out.println("User interface being updated.");

		// first lets draw the food
		int numFood = player.numHealthItems(); //should extract from player
		for (int i = 0; i < numFood; ++i) {
			//extract the foodLabel from the inventoryPanel
			JLabel foodLabel = (JLabel) inventoryPanel.getComponent(i);
			//set the labels image to be the food
			drawBackgroundImage(foodLabel, "itemBox", i);
		}

		//now we can draw in weapons
		int numTools = player.numToolItems(); //should extract from player
		for (int i = 0; i < numTools; ++i) {
			//extract the tool label from the inventory label
			JLabel toolLabel = (JLabel) inventoryPanel.getComponent(i + MAX_FOOD);
			//set the labels image to be a tool
			drawBackgroundImage(toolLabel, "toolBox", i + MAX_FOOD);
		}

		boolean hasNoKey = player.canAddKeyItem(); // should extract from player
		if (!hasNoKey) {
			//need to draw players key
			JLabel keyLabel = (JLabel) inventoryPanel.getComponent(MAX_FOOD + MAX_TOOLS);
			drawBackgroundImage(keyLabel, "keyBox", MAX_FOOD + MAX_TOOLS);
		}

		//the interface should be updated
		frame.revalidate();
		//the repaint method suggests to the board to repaint the frame
		//this may update the minimap if changes have been made to it
		frame.repaint();
	}

	/**
	 * This method should be called when the image on
	 * an inventory slot should be redrawn with an
	 * item's image.
	 * @param inventoryLabel label that new image
	 * 		is being drawn onto
	 * @param type of inventory item
	 * @param index of slot within the inventoryLabel
	 * 		to draw onlatch
	 */
	private void drawBackgroundImage(JLabel inventoryLabel, String type, int index) {
		//firstly, we set the background image to be the image in question
		String imageName = getName(index);
		Image itemImage = MenuInterface.loadImage(imageName+".png");
		inventoryLabel.setIcon(new ImageIcon(itemImage));

		//now we will draw the border over the top of the image of the tool
		Image slotBackgroundImage = MenuInterface.loadImage(type+".png");
		//get graphics from image to draw with
		Graphics g = itemImage.getGraphics();
		g.drawImage(slotBackgroundImage, 0, 0, null, null);

		//update the panel description (hover over text)
		if (type.equals("toolBox")) {
			if (index == 9) {
				//10th slot
				inventoryLabel.setToolTipText(imageName+" - press 0 to select.");
			} else if (index == 10) {
				//11th slot
				inventoryLabel.setToolTipText(imageName+" - press - to select.");
			} else {
				//6th - 9th slot
				inventoryLabel.setToolTipText(imageName+" - press "+(index+1)+" to select.");
			}
		} else if (type.equals("keyBox")) {
			//12th slot
			inventoryLabel.setToolTipText(imageName+" - press = to select.");
		} else {
			//1st - 5th slot
			inventoryLabel.setToolTipText(imageName+" - press "+(index+1)+" to select.");
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
		if (index < 0 || index > 12) {
			throw new Error("Invalid index exception");
		}

		if (index < 5) {
			return "appleIcon";
		} else if (index < 11) {
			return "pickaxeIcon";
		} else {
			return "key";
		}
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
		if (index < -2 || index > 11) {
			throw new Error("Invalid index");
		}

		ImagePanel inventoryPanel = (ImagePanel) frame.getContentPane().getComponent(0);

		int indexOfCurrentSelectedItem = 0;
		//first, we need to remove the blue border from the previous selected item
		for (int i = 0; i < 12; ++i) {
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
				selectedLabel = (JLabel) inventoryPanel.getComponent(11);
			} else {
				//go to previous slot
				selectedLabel = (JLabel) inventoryPanel.getComponent(indexOfCurrentSelectedItem - 1);
			}
		} else if (index == CYCLE_RIGHT) {
			//check for special case, have to loop around
			if (indexOfCurrentSelectedItem == 11) {
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
	 * @return Name of currently selected item
	 */
	public String getSelectedItem() {
		ImagePanel inventoryPanel = (ImagePanel) frame.getContentPane().getComponent(0);

		for (int i = 0; i < 12; ++i) {
			//number of slots is small, so we can just iterate through them all
			JLabel tempLabel = (JLabel) inventoryPanel.getComponent(i);
			if (!(tempLabel.getBorder() instanceof EmptyBorder)) {
				//this label must contain the selected item
				String text = tempLabel.getToolTipText().toLowerCase();
				if (text.contains("slot")) {
					//empty slot
					return null;
				} else if (text.contains("icon")) {
					//split on the word Icon
					String[] words = text.split("icon");
					return words[0];
				} else {
					//return first word
					System.out.println(text);
					return text.split(" ")[0];
				}
			}
		}
		return null;
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
	
		switch (keyCode) {
		case KeyEvent.VK_W:
			player.setNorth(false);
			break;
		case KeyEvent.VK_A:
			player.setWest(false);
			break;
		case KeyEvent.VK_S:
			player.setSouth(false);
			break;
		case KeyEvent.VK_D:
			player.setEast(false);
			break;
		}
	}

	/**
	 * This method should be called when the user has pressed
	 * a key. If the key they have pressed is relevant to the
	 * game, then this method should carry out that keys action.
	 * Otherwise, the function is exited.
	 * @param arg0 - Keyevent of the keypress
	 */
	protected void handleKeyPress(KeyEvent arg0, Set<Character> chars) {
		int keyCode = arg0.getKeyCode();
		Point currentCoord = player.getCoords();
		
		//handle number presses
		if (48 <= keyCode && keyCode <= 57) {
			//user has pressed a number key
			if (keyCode == 48) {
				//0 is a special case, this corresponds to 10th slot
				updateSelectedSlot(9);
			} else {
				//1-9
				updateSelectedSlot(keyCode-49);
			}
		}
				
		switch (keyCode) {
		case KeyEvent.VK_W:
			player.setNorth(true);
			//up one square
//			MovePacket upPacket = new MovePacket(player.getName(),player.getCoords().x,
//						player.getCoords().y);
			break;
		case KeyEvent.VK_A:
			player.setWest(true);
			//left one square
//			MovePacket leftPacket = new MovePacket(player.getName(),player.getCoords().x,
//					player.getCoords().y);
			break;
		case KeyEvent.VK_S:
			player.setSouth(true);
			//moved down one
//			MovePacket downPacket = new MovePacket(player.getName(),player.getCoords().x,
//					player.getCoords().y);
			break;
		case KeyEvent.VK_D:
			player.setEast(true);
			//moved right one
//			MovePacket rightPacket = new MovePacket(player.getName(),player.getCoords().x,
//					player.getCoords().y);;
			break;
		case KeyEvent.VK_MINUS:
			//user wants to select the 11th slot
			updateSelectedSlot(10);
			break;
			//user wants to select the 12th slot
		case KeyEvent.VK_EQUALS:
			updateSelectedSlot(11);
			break;
		case KeyEvent.VK_SHIFT:
			String selectedItemName = getSelectedItem();
			if (selectedItemName != null) {
				clearKeysPressed();
				JOptionPane.showMessageDialog(frame, "User wants to interact with their "
						+ ""+ selectedItemName+".");
			} else {
				JOptionPane.showMessageDialog(frame, "User cannot interact with an empty slot.");
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
			//JOptionPane.showMessageDialog(frame, "The game has been paused.");
			clearKeysPressed();
			game.setPaused(true);
			JOptionPane.showInputDialog(frame, "The Game has been Paused.", "PAUSED", 1,
					null, new Object[]{"Continue", "Exit and Save"}, "Continue");
			break;
		case KeyEvent.VK_Z:
			updateSelectedSlot(CYCLE_LEFT);
			break;
		case KeyEvent.VK_X:
			updateSelectedSlot(CYCLE_RIGHT);
			break;
		}
		//update the interface (in particular, the mini map)
		//updateUI();
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
	
}
