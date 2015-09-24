package menu;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

public class MenuInterface {
	private JFrame frame;
	private static final String IMAGE_PATH = "media/";


	public MenuInterface() {
		initialise();
	}

	public void initialise() {
		frame = new JFrame();
		frame.setBounds(100, 30, 1000, 600);

		ImagePanel imagePanel = new ImagePanel("forest.png");
		frame.setContentPane(imagePanel);
		frame.setResizable(false);
		
		//initialise key binding
		imagePanel.getInputMap().put(KeyStroke.getKeyStroke("N"), "New Game");
		ButtonAction newGame = new ButtonAction("New Game", "This button starts a new game", new Integer(KeyEvent.VK_N));
		imagePanel.getActionMap().put("New Game", newGame);

		imagePanel.getInputMap().put(KeyStroke.getKeyStroke("Q"), "Quit Game");
		ButtonAction exitGame = new ButtonAction("Quit Game", "This button exits the game.", new Integer(KeyEvent.VK_Q));
		imagePanel.getActionMap().put("Quit Game", exitGame);

		//initialise window listener
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//Should get in here if the window is closed
				int result = JOptionPane.showConfirmDialog(frame, "Exit the application?");
				if (result == JOptionPane.OK_OPTION) {
					//User really wants to exit
					System.exit(0);
				}
			}
		});

		//set up menupanel
		JPanel menuPanel = new JPanel();
		Border blackline = BorderFactory.createLineBorder(Color.BLACK, 2);
		menuPanel.setBorder(blackline);
		menuPanel.setOpaque(false);
		menuPanel.setLayout(null);
		menuPanel.setBounds(380, 140, 200, 400);

		//New Game button
		ImageIcon newGameImage = new ImageIcon(loadImage("playgamebutton.png"));
		JButton btnNewGame = new JButton(newGameImage);
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("New Game button pressed");
			}
		});
		btnNewGame.setBounds(25, 50, 150, 100);
		btnNewGame.setBorder(blackline);
		menuPanel.add(btnNewGame);
		

		//Quit Game button
		ImageIcon quitGameImage = new ImageIcon(loadImage("quitgamebutton.png"));
		JButton btnQuitGame = new JButton(quitGameImage);
		btnQuitGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(frame, "Exit the application?");
				if (result == JOptionPane.OK_OPTION) {
					//User really wants to exit
					System.exit(0);
				}
			}
		});
		btnQuitGame.setBounds(25, 200, 150, 100);
		btnQuitGame.setBorder(blackline);
		menuPanel.add(btnQuitGame);
		
//		//play song
//		final PlayMusic music = new PlayMusic();
//		//music.playSound("SayMyName48.mp3");
//		
//		//Toggle Music Button
//		JButton toggleMusicButton = new JButton("Toggle Music");
//		toggleMusicButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				music.toggleMusic();
//			}
//		});
//		toggleMusicButton.setBounds(830, 520, 150, 40);
//		frame.getContentPane().add(toggleMusicButton);
				
		frame.getContentPane().requestFocus();
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(menuPanel);
	}
	
	/**
	 * This method should load an image in from a filename.
	 * @param filename
	 * @return
	 */
	public static Image loadImage(String filename) {
		// using the URL means the image loads when stored
		// in a jar or expanded into individual files.
		java.net.URL imageURL = ImagePanel.class.getResource(IMAGE_PATH + filename);
		try {
			Image img = ImageIO.read(imageURL);
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
					MenuInterface menu = new MenuInterface();
					menu.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public class ButtonAction extends AbstractAction {
		public ButtonAction(String text, String desc, Integer mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		public void actionPerformed(ActionEvent e) {
			String id = e.getActionCommand();
			switch(id.toLowerCase()) {
			case "q":
				int result = JOptionPane.showConfirmDialog(frame, "Exit the application?");
				if (result == JOptionPane.OK_OPTION) {
					//User really wants to exit
					System.exit(0);
				}
				break;
			case "n":
				System.out.println("N pressed");
				break;
			}
		}
	}
}
