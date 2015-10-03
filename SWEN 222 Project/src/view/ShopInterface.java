package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import game.Player;
import game.items.Tool;
import menu.MenuInterface;
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
	private Tool weapons;
	
	/**
	 * Create a new instance of ShopInterface.
	 */
	public ShopInterface() {
		this.player = DestinysWild.getPlayer();
		initialiseInterface();
	}
	
	private void initialiseInterface() {
		frame = new JFrame();
		frame.setTitle("Shop");
		frame.setBounds(300, 150, 400, 300);
		frame.setResizable(false);
		frame.setLayout(null);
		
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
						//Should launch up a pop up which says
						//Are you sure you want to buy the "xyz"? It will cost ...
						//User would then confirm
						//This would then call a method which would check the users
						//coins. If they have enough, then their coins are decremented
						//by the appropriate amount, and the tool is added to their inventory
						//If they cannot afford the tool, then another pop up will say 
						//"Sorry, you can't afford that right now"
						
					}
				});
				
				frame.getContentPane().add(buyItemButton);
			}
		}
	
		frame.revalidate();
		frame.repaint();
	}
	
	/**
	 * This method should be called when the user has clicked
	 * on the buy button underneath a tool.
	 */
	private void buyTool(ActionEvent e) {
		//initialise the 
		
		
		//first we get the index of the button that was pressed
		int indexOfTool = 0;
		for (int i = 0; i < 6; ++i) {
			JButton tempButton = (JButton) frame.getContentPane().getComponent(i*2+1);
			if (tempButton.equals(e.getSource())) {
				indexOfTool = i;
			}
		}
		int cost = indexOfTool * 100;
		int result = JOptionPane.showConfirmDialog(frame, "This will cost "+cost+" coins. Are you sure?");
		if (result == JOptionPane.OK_OPTION) {
			int score = player.getScore();
			
			if (score >= cost) {
				//user can buy this tool
				//tool should be added to user's inventory
				Tool tool = new Tool("pickaxe", 50);
				player.addInventoryItem(tool);
				JOptionPane.showMessageDialog(frame, "The ... has been added to your inventory.");
				player.setScore(score - cost);
			} else {
				JOptionPane.showMessageDialog(frame, "You don't have enough for that...");
			}
		}
	}
	
	public static void main(String[] args) {
		ShopInterface shop = new ShopInterface();
		shop.frame.setVisible(true);
	}
}
