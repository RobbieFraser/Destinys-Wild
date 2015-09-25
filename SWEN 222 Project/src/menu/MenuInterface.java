package menu;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

public class MenuInterface {
	private JFrame frame;
	private static final String IMAGE_PATH = "data/images/";


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
		//menuPanel.setOpaque(false);
		menuPanel.setLayout(null);
		menuPanel.setBounds(380, 140, 200, 400);

		//New Game button
		ImageIcon newGameImage = new ImageIcon(loadImage("playgamebutton.png"));
		JButton btnNewGame = new JButton(newGameImage);
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Set up text field
			    JTextField nameInput = new JTextField(30);
			    nameInput.setBounds(430, 13, 200, 20);
				frame.getContentPane().add(nameInput);
			}
		});
		btnNewGame.setBounds(25, 30, 150, 80);
		btnNewGame.setBorder(blackline);
		menuPanel.add(btnNewGame);

		//Load Game button
		JButton btnLoadGame = new JButton("Load Game");
		btnLoadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				//direct the user to the save games folder
				chooser.setCurrentDirectory(new File("data/savegames"));
				int result = chooser.showOpenDialog(btnLoadGame);
				if (result == JFileChooser.APPROVE_OPTION) {
					System.out.println("You chose to open this file: " +
							chooser.getSelectedFile().getName());
					File loadGame = chooser.getSelectedFile();
					//TODO: Once the game is loaded in, begin playing!
				}
			}
		});
		btnLoadGame.setBounds(25, 150, 150, 80);
		btnLoadGame.setBorder(blackline);
		btnLoadGame.setOpaque(false);
		menuPanel.add(btnLoadGame);


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
		btnQuitGame.setBounds(25, 270, 150, 80);
		btnQuitGame.setBorder(blackline);
		menuPanel.add(btnQuitGame);

		//play song
		final PlayMusic music = new PlayMusic();
		//music.playSound("SayMyName48.mp3");

		//Toggle Music Button
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
	 * Outline for saving game state:
	 * There should be a "New Game" button, and
	 * a "Load Game" button. 
	 * New Game - User should be prompted to enter
	 * their player name. A file should be made in
	 * the savedgames folder under their name.
	 * Load Game - File selecting interface should pop
	 * up, which the user should navigate through and
	 * retrieve their save file.
	 *
	 */

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
