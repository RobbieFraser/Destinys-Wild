package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import clientServer.packets.DisconnectPacket;
import Renderer.GameImagePanel;
import game.Board;
import game.DestinysWild;
import game.Player;
import game.Room;
import game.XMLParser;
import menu.ImagePanel;
import menu.MenuInterface;

public class GameInterface implements MouseListener {
	private static final String IMAGE_PATH = "data/images/";
	private static final int MAX_FOOD = 5;
	private static final int MAX_TOOLS = 6;
	private JFrame frame;
	private Player player; //player whose game state will be drawn
	private Board board;
	private DestinysWild game;
	private GameImagePanel gamePanel;

	public GameInterface(Player player, GameImagePanel gamePanel,DestinysWild game) {
		this.player = player;
		this.board = gamePanel.getBoard();
		this.game = game;
		this.gamePanel = gamePanel;
		initialiseInterface();
		updateUI();

		//TODO: Add mouse listener
		//TODO: Only draw an image if its just been added
		//TODO: Add support Spacebar / Interact Item
		//TODO: Add support for P / Pause
		//TODO: Add support for L / R Arrows keys - Change perspective
		//TODO: Add support for ESC - Escape to menu
	}

	private void initialiseInterface() {
		frame = new JFrame();
		frame.setBounds(100, 30, 1100, 750);
		frame.setResizable(false);
		frame.addMouseListener(this);
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

		// set up tool belt
		// max 6 tools
		for (int i = 0; i < 2; ++i) {
			//2 rows of tools, each row contains 3 slots
			for (int j = 0; j < 3; ++j) {
				JLabel toolBox = new JLabel(new ImageIcon(MenuInterface.loadImage("toolBox.png")));
				toolBox.setBounds(465 + j * 65, 10 + i * 65, 56, 56);
				if ((i*3+j+6) == 10) {
					toolBox.setToolTipText("Tool slot "+(i*3+j+1)+" - press 0 to select.");
				} else if ((i*3+j+6) == 11){
					toolBox.setToolTipText("Tool slot "+(i*3+j+1)+" - press - to select.");
				} else {
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
		//intialise the selected item to the first food slot as a default
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


		// add score display template
		JPanel scoreDisplay = new JPanel();
		scoreDisplay.setBorder(blackline);
		scoreDisplay.setBounds(20, 535, 135, 30);
		frame.getContentPane().add(scoreDisplay);

		//initialise window listener
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				
				escapeGame();
			}
		});

		//add key listener
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				handleKeyPress(arg0);
			}
			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
		});

		frame.getContentPane().setLayout(null);
	}

	public void updateUI() {

		// at this point, we have access to the players inventory,
		// which will be a list of item types or ids
		// we can use these to read in the corresponding image
		// we should:
		// read in from the list all food, and make a numFood
		// read in from the list all tools, and make numTools
		// read in from the list whether there is a key an make an isKey

		// draw the inventory
		ImagePanel inventoryPanel = (ImagePanel) frame.getContentPane().getComponent(0);

		// first lets draw the food
		int numFood = 3; //should extract from player
		for (int i = 0; i < numFood; ++i) {
			//extract the foodLabel from the inventoryPanel
			JLabel foodLabel = (JLabel) inventoryPanel.getComponent(i);
			//set the labels image to be the food
			drawBackgroundImage(foodLabel, "itemBox", i, "appleIcon");
		}

		int numTools = 2; //should extract from player
		for (int i = 0; i < numTools; ++i) {
			//extract the tool label from the inventory label
			JLabel toolLabel = (JLabel) inventoryPanel.getComponent(i + MAX_FOOD);
			//set the labels image to be a tool
			drawBackgroundImage(toolLabel, "toolBox", i, "pickaxeIcon");
		}

		boolean hasKey = true; // should extract from inventory
		if (hasKey) {
			//need to draw players key
			JLabel keyLabel = (JLabel) inventoryPanel.getComponent(MAX_FOOD + MAX_TOOLS);
			drawBackgroundImage(keyLabel, "keyBox", MAX_FOOD + MAX_TOOLS, "key");
		}

		//suggest that minimap should be updated
		frame.revalidate();
		frame.repaint();
	}

	private void drawBackgroundImage(JLabel inventoryLabel, String type, int i, String imageName) {
		inventoryLabel.setIcon(new ImageIcon(MenuInterface.loadImage(imageName+".png")));
		//now we will draw the border over the top of the image of the tool
		Image backgroundImage = MenuInterface.loadImage(type+".png");
		ImageIcon imageIcon  = (ImageIcon) inventoryLabel.getIcon();
		Image itemImage = imageIcon.getImage();
		Graphics g = itemImage.getGraphics();
		//draw the image
		g.drawImage(backgroundImage, 0, 0, null, null);
		//update the panel description
		if (type.equals("toolBox")) {
			if ((MAX_FOOD +i+1) == 10) {
				inventoryLabel.setToolTipText(imageName+" - press 0 to select.");
			} else if ((MAX_FOOD +i+1) == 11) {
				inventoryLabel.setToolTipText(imageName+" - press - to select.");
			} else {
				inventoryLabel.setToolTipText(imageName+" - press "+(MAX_FOOD +i+1)+" to select.");
			}
		} else if (type.equals("keyBox")) {
			inventoryLabel.setToolTipText(imageName+" - press = to select.");
		} else {
			inventoryLabel.setToolTipText(imageName+" - press "+(i+1)+" to select.");
		}
	}

	/**
	 * This method should take as a parameter a number
	 * between 0 and player.inventory.size().
	 * It should analyse the value of the item at this
	 * position of the inventory, and return a unique
	 * string which will be used to extract the image
	 * of this item.
	 * @param index of item to check
	 * @return name of image to use
	 */
	private String getName(int index) {
		return null;
	}

	private void escapeGame() {
		int result = JOptionPane.showConfirmDialog(frame, "Exit the application?");
		if (result == JOptionPane.OK_OPTION) {
			//User really wants to exit
			DisconnectPacket disconnect = new DisconnectPacket(this.player.getName());
			disconnect.writeData(this.game.getClient());
			System.exit(0);
		}
	}

	/**
	 * This method should draw a blue border around the
	 * slot at the given in the inventory panel. 
	 */
	private void updateSelectedSlot(int index) {
		if (index < 0 || index > 11) {
			throw new Error("Invalid index");
		}

		ImagePanel inventoryPanel = (ImagePanel) frame.getContentPane().getComponent(0);

		//first, we need to remove the blue border from the previous selected item
		for (int i = 0; i < 12; ++i) {
			//number of slots is small, so we can just iterate through them all
			JLabel tempLabel = (JLabel) inventoryPanel.getComponent(i);
			tempLabel.setBorder(BorderFactory.createEmptyBorder());
		}

		//now we draw the selected border on the correct choice
		JLabel selectedLabel = (JLabel) inventoryPanel.getComponent(index);
		Border selectedBorder = BorderFactory.createLineBorder(Color.BLUE, 4);
		selectedLabel.setBorder(selectedBorder);	

		//redraw interface
		frame.revalidate();
		frame.repaint();
	}

	private void handleKeyPress(KeyEvent arg0) {
		int x, y;
		int keyCode = arg0.getKeyCode();
		Point currentCoord = player.getCoords();
		//System.out.println("Current coordinates: "+currentCoord.toString());
		//handle number presses
		if (48 <= keyCode && keyCode <= 57) {
			//user has pressed a number key
			if (keyCode == 48) {
				//0 is a special case, this corresponds to 10
				updateSelectedSlot(9);
			} else {
				//1-9
				updateSelectedSlot(keyCode-49);
			}
		}

		switch (keyCode) {
		case KeyEvent.VK_W:
			x = currentCoord.x;
			y = currentCoord.y - 1; //moved up one
			player.setCoords(new Point(x,y));
			break;
		case KeyEvent.VK_A:
			x = currentCoord.x - 1; //moved left one
			y = currentCoord.y;
			player.setCoords(new Point(x,y));
			break;
		case KeyEvent.VK_S:
			x = currentCoord.x; 
			y = currentCoord.y + 1; //moved down one
			player.setCoords(new Point(x,y));
			break;
		case KeyEvent.VK_D:
			x = currentCoord.x + 1; //moved right one
			y = currentCoord.y; 
			player.setCoords(new Point(x,y));
			break;
		case KeyEvent.VK_MINUS:
			updateSelectedSlot(10);
			break;
		case KeyEvent.VK_EQUALS:
			updateSelectedSlot(11);
		default:
			break;
		}
		//update the interface (in particular, the mini map)
		updateUI();
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Board board = XMLParser.initialiseBoard("data/board.xml");
					Player player = new Player("Sam", new Point(4,6), new Room(-1, -1, -1, -1, 13, new Point(1,3)));
					GameImagePanel gamePanel = new GameImagePanel(board);
					DestinysWild destinysWild = new DestinysWild(board);
					GameInterface game = new GameInterface(player, gamePanel,destinysWild);
					game.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		System.out.println(arg0.getX() + " "+arg0.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() instanceof JPanel) {
			System.out.println("Coming in");
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {	

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}
}

