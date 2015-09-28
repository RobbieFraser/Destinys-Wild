package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

import game.Board;
import game.Player;
import game.Room;
import game.XMLParser;
import menu.ImagePanel;

public class GameInterface {
	private JFrame frame;
	private static final String IMAGE_PATH = "data/images/";
	private static final int MAX_FOOD = 5;
	private static final int MAX_TOOLS = 6;
	private Player player; //player whose game state will be drawn

	public GameInterface(Player player) {
		this.player = player;
		initialise();
		updateUI();
	}

	private void initialise() {
		frame = new JFrame();
		frame.setBounds(100, 30, 1100, 750);
		frame.setResizable(false);

		// set up border
		Border blackline = BorderFactory.createLineBorder(Color.BLACK, 2);

		// set up inventory panel
		ImagePanel inventoryPanel = new ImagePanel("inventory.png");
		inventoryPanel.setOpaque(false);
		//inventoryPanel.setBorder(blackline);
		inventoryPanel.setBounds(20, 570, 790, 140);
		inventoryPanel.setLayout(null);

		// setting up food panel
		// setting up food slots - these will be filled in when the player has picked up food
		// 4 slots
		for (int i = 0; i < MAX_FOOD; ++i) {
			JLabel inventoryBox = new JLabel(new ImageIcon(loadImage("itemBox.png")));
			//inventoryBox.setBorder(blackline);
			inventoryBox.setBounds(20 + i * 88, 30, 78, 78);
			inventoryPanel.add(inventoryBox);
		}

		// set up tool belt
		// max 6 tools
		for (int i = 0; i < 2; ++i) {
			//2 rows of tools, each row contains 3 slots
			for (int j = 0; j < 3; ++j) {
				JLabel toolBox = new JLabel(new ImageIcon(loadImage("toolBox.png")));
				toolBox.setBounds(465 + j * 65, 10 + i * 65, 56, 56);
				inventoryPanel.add(toolBox);
			}
		}

		// set up key slot
		// player can only hold 1 key at a time
		JLabel keyBox = new JLabel(new ImageIcon(loadImage("keyBox.png")));
		keyBox.setBounds(670, 20, 100, 100);
		inventoryPanel.add(keyBox);

		frame.getContentPane().add(inventoryPanel);

		// set up panel which will contain minimap
		JPanel mapPanel = new JPanel(new BorderLayout());
		mapPanel.setBorder(blackline);
		mapPanel.setBounds(830, 460, 250, 250);
		Board board = XMLParser.initialiseBoard("data/board.xml");
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
		int numFood = 5; //should extract from player
		for (int i = 0; i < numFood; ++i) {
			//extract the foodLabel from the inventoryPanel
			JLabel foodLabel = (JLabel) inventoryPanel.getComponent(i);
			//set the labels image to be the food
			drawBackgroundImage(foodLabel, "itemBox", i, "appleIcon");
		}

		int numTools = 6; //should extract from player
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
		inventoryLabel.setIcon(new ImageIcon(loadImage(imageName+".png")));
		//now we will draw the border over the top of the image of the tool
		Image backgroundImage = loadImage(type+".png");
		ImageIcon imageIcon  = (ImageIcon) inventoryLabel.getIcon();
		Image itemImage = imageIcon.getImage();
		Graphics g = itemImage.getGraphics();
		//draw the image
		g.drawImage(backgroundImage, 0, 0, null, null);
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
			System.exit(0);
		}
	}
	
	private void handleKeyPress(KeyEvent arg0) {
		int x, y;
		int keyCode = arg0.getKeyCode();
		Point currentCoord = player.getCoords();
		//System.out.println("Current coordinates: "+currentCoord.toString());
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
		default:
			System.out.println("Invalid key press.");
			break;
		}
		//System.out.println("Updated coordinates: "+player.getCoords().toString());
		updateUI();
	}

	/**
	 * This method should load an image in from a filename.
	 * @param filename
	 * @return
	 */
	public static Image loadImage(String filename) {
		// using the URL means the image loads when stored
		// in a jar or expanded into individual files.
		try {
			Image img = ImageIO.read(new File(IMAGE_PATH + filename));
			return img;
		} catch (IOException e) {
			// we've encountered an error loading the image. There's not much we
			// can actually do at this point, except to abort the game.
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Player player = new Player("Sam", new Point(0,0), new Room(-1, -1, -1, -1, 13, new Point(1,3)));
					GameInterface game = new GameInterface(player);
					game.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

