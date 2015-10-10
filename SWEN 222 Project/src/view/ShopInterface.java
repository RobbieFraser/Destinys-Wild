package view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import game.Player;
import game.items.Tool;
import view.MenuInterface;
import game.DestinysWild;

/**
 * An instance of ShopInterface should be created whenever a
 * player opens up the shop (by talking to the shopkeeper).
 * Users should be able to open the shop interface, see the
 * items on sale, and buy items if they have enough money.
 * @author DanielYska
 *
 */
public class ShopInterface {
	private JFrame frame;
	private Player player;
	private Tool[] weapons;
	
	/**
	 * Create a new instance of ShopInterface.
	 */
	public ShopInterface() {
		this.player = DestinysWild.getPlayer();
		Tool[] weapons = {new Tool("machete", 10),
				new Tool("torch", 20), new Tool("pickaxe",30),
				new Tool("jetfuel", 40), new Tool("bucket", 50),
				new Tool("spade", 60)};
		this.weapons = weapons;
		initialiseInterface();
	}
	
	/**
	 * This method should set up the look of the shop interface.
	 * All the tools should be added to the shop, along with the
	 * 'buy' buttons underneath each tool.
	 */
	private void initialiseInterface() {
		frame = new JFrame();
		frame.setTitle("Shop");
		frame.setBounds(400, 150, 330, 280);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setAlwaysOnTop(true);
		
		//we set up 6 panels which will each contain a tool
		for (int i = 0; i < 2; ++i) {
			for (int j = 0; j < 3; ++j) {
				
				//set in the item box image in the background, then we drawn the
				//individual tool on top
				Image itemBoxImage = MenuInterface.loadImage("itemBox.png");
				Graphics graphics = itemBoxImage.getGraphics();
				//draw tool on top of the background image
				
				Image toolImage = MenuInterface.loadImage(getToolImageName(i*3+j));
				graphics.drawImage(toolImage, 12, 12, null);
				JLabel shopSlot = new JLabel(new ImageIcon(itemBoxImage));		
				shopSlot.setBounds(36+88*j, 20+115*i, 78, 78);
				frame.getContentPane().add(shopSlot);
				
				JButton buyItemButton = new JButton("Buy");
				buyItemButton.setBounds(36+88*j, 103+115*i, 78, 20);
				buyItemButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						buyTool(e);					
					}
				});
				//focus will always be on the buttons, so we add the key listener here
				buyItemButton.addKeyListener(new KeyListener() {
					@Override
					public void keyPressed(KeyEvent arg0) {
						if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
							//user wants to exit the shop interface
							frame.dispose();
						}
					}
					@Override
					public void keyReleased(KeyEvent arg0) {}
					@Override
					public void keyTyped(KeyEvent arg0) {}
				});
				
				frame.getContentPane().add(buyItemButton);
			}
		}
	
		frame.revalidate();
		frame.repaint();
		frame.setVisible(true);
	}
	
	/**
	 * This method should return a string containing
	 * the name of the image that corresponds to a
	 * certain slot in the shop.
	 * @param index of tool that's image name should be returned
	 * @return string of tool image name
	 */
	private String getToolImageName(int index) {
		//TODO: Add in correct tool images
		switch(index) {
		case 0:
			//machete
			return "pickaxeIcon.png";
		case 1:
			//torch
			return "pickaxeIcon.png";
		case 2:
			//pickaxe
			return "pickaxeIcon.png";
		case 3:
			//jetfuel
			return "pickaxeIcon.png";
		case 4:
			//bucket
			return "pickaxeIcon.png";
		case 5:
			//spade
			return "pickaxeIcon.png";
		default:
			//dead code, if we get here an error will be thrown
			return null;
		}
	}
	
	/**
	 * This method should be called when the user has clicked
	 * on the buy button underneath a tool.
	 */
	private void buyTool(ActionEvent e) {	
		//first we get the index of the button that was pressed
		int indexOfTool = 0;
		for (int i = 0; i < 6; ++i) {
			JButton tempButton = (JButton) frame.getContentPane().getComponent(i*2+1);
			if (tempButton.equals(e.getSource())) {
				indexOfTool = i;
			}
		}
		//int cost = (indexOfTool * 50) + 50;
		int cost = 0;
		Tool tool = weapons[indexOfTool];
		//capitalise first letter of tool's name
		String toolName = tool.getType().substring(0, 1).toUpperCase() + tool.getType().substring(1);
		
		if (player.getInventory().contains(tool)) {
			JOptionPane.showMessageDialog(frame, "You've already bought the "+toolName+".");
		}
		else {
			int result = JOptionPane.showConfirmDialog(frame, "The "+toolName+" will cost "+cost+" coins. Are you sure?");
			if (result == JOptionPane.OK_OPTION) {
				int score = player.getScore();
				if (score >= cost) {
					//user can buy this tool
					//tool should be added to user's inventory
					player.addInventoryItem(tool);
					JOptionPane.showMessageDialog(frame, "The "+toolName+" has been added to your inventory.");
					player.setScore(score - cost);
					//player cannot buy this tool again
				} else {
					int difference = cost - score;
					JOptionPane.showMessageDialog(frame, "You need "+difference+" more coins to buy the "+toolName+".");
				}
			}
		}
	}
}
