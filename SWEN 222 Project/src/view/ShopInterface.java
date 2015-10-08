package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
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
		Tool[] weapons = {new Tool("machete", 10), new Tool("torch", 20), 
				new Tool("pickaxe",30), new Tool("jetfuel", 40),
				new Tool("bucket", 50), new Tool("spade", 60)};
		this.weapons = weapons;
		initialiseInterface();
	}
	
	private void initialiseInterface() {
		frame = new JFrame();
		frame.setTitle("Shop");
		frame.setBounds(300, 150, 400, 300);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setAlwaysOnTop(true);
		
		//we set up 6 panels which will each contain a tool
		for (int i = 0; i < 2; ++i) {
			for (int j = 0; j < 3; ++j) {
				JLabel shopSlot = new JLabel(new ImageIcon(MenuInterface.loadImage("pickaxeIcon.png")));		
				shopSlot.setBounds(73+88*j, 40+115*i, 78, 78);
				shopSlot.setBorder(BorderFactory.createLineBorder(Color.RED));
				frame.getContentPane().add(shopSlot);
				
				JButton buyItemButton = new JButton("Buy");
				buyItemButton.setBounds(73 + 88*j, 118+115*i, 78, 20);
				buyItemButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						buyTool(e);					
					}
				});
				//focus will be on the buttons always, so we add the key listener here
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
		//int cost = indexOfTool * 100;
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
					//player can no longer buy this tool
				} else {
					int difference = cost - score;
					JOptionPane.showMessageDialog(frame, "You need "+difference+" more coins to buy the "+toolName);
				}
			}
		}
	}
}
