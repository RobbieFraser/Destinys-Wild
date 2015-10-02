package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import menu.MenuInterface;

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
	
	/**
	 * Create a new instance of ShopInterface.
	 */
	public ShopInterface() {
		initialiseInterface();
	}
	
	private void initialiseInterface() {
		frame = new JFrame();
		frame.setTitle("Shop");
		frame.setBounds(300, 150, 400, 300);
		frame.setResizable(false);
		frame.setLayout(null);
		
		//we set up 6 panels which will each contain a tool
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 2; ++j) {
				JLabel shopSlot = new JLabel(new ImageIcon(MenuInterface.loadImage("pickaxeIcon.png")));		
				shopSlot.setBounds(73+88*i, 40+115*j, 78, 78);
				shopSlot.setBorder(BorderFactory.createLineBorder(Color.RED));
				frame.getContentPane().add(shopSlot);
				
				JButton buyItemButton = new JButton("Buy");
				buyItemButton.setBounds(73 + 88*i, 118+115*j, 78, 20);
				buyItemButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.out.println("Button pressed.");
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
	
	public static void main(String[] args) {
		ShopInterface shop = new ShopInterface();
		shop.frame.setVisible(true);
	}
}
