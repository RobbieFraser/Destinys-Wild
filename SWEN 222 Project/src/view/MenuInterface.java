package view;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import game.DestinysWild;
import game.XMLParser;

public class MenuInterface {
	private JFrame frame;
	private static final String IMAGE_PATH = "data/images/";
	private DestinysWild game;

	public MenuInterface(DestinysWild game) {
		this.game = game;
		initialise();
	}

	public void remove(){
		frame.setVisible(false);
		frame.dispose();
	}

	public void initialise() {
		frame = new JFrame();
		frame.setBounds(100, 30, 1100, 750);

		frame.setTitle("Destiny's Wild");

		ImagePanel imagePanel = new ImagePanel("titleScreen.png");
		frame.setContentPane(imagePanel);
		frame.setResizable(false);

		//initialise window listener
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				escapeGame();
			}
		});

		//set up menupanel
		JPanel menuPanel = new JPanel();
		//Border blackline = BorderFactory.createLineBorder(Color.BLACK, 2);
		//menuPanel.setBorder(blackline);
		menuPanel.setLayout(null);
		menuPanel.setBackground(new Color(0,0,0,0));
		menuPanel.setOpaque(false);
		menuPanel.setBounds(430, 240, 200, 400);

		//New Game button
		ImageIcon newGameImage = new ImageIcon(loadImage("playgamebutton.png"));
		JButton btnNewGame = new JButton(newGameImage);
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newGameButtonPressed();
			}
		});
		btnNewGame.setBounds(25, 30, 150, 80);
		addKeyListener(btnNewGame);
		//btnNewGame.setBorder(blackline);
		menuPanel.add(btnNewGame);

		//Load Game button
		JButton btnLoadGame = new JButton("Load Game");
		btnLoadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadGame();
			}
		});
		btnLoadGame.setBounds(25, 150, 150, 80);
	    //btnLoadGame.setBorder(blackline);
		addKeyListener(btnLoadGame);
		btnLoadGame.setOpaque(false);
		menuPanel.add(btnLoadGame);

		//Quit Game button
		ImageIcon quitGameImage = new ImageIcon(loadImage("quitgamebutton.png"));
		JButton btnQuitGame = new JButton(quitGameImage);
		btnQuitGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				escapeGame();
			}
		});
		addKeyListener(btnQuitGame);
		btnQuitGame.setBounds(25, 270, 150, 80);
		menuPanel.add(btnQuitGame);

		PlayMusic.playSound("DestinysWildOST.mp3");

		//Toggle Music Button
		JButton toggleMusicButton = new JButton("Toggle Music");
		toggleMusicButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PlayMusic.toggleMusic();
			}
		});
		addKeyListener(toggleMusicButton);
		toggleMusicButton.setBounds(925, 675, 150, 40);
		frame.getContentPane().add(toggleMusicButton);

		frame.getContentPane().requestFocus();
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(menuPanel);
		frame.setVisible(true);
	}
	
	/**
	 * The focus of the menu interface will by default always
	 * be on one of the buttons. Therefore the key listener
	 * for key shortcuts should be added to all buttons, which
	 * will be done in here.
	 * @param button button that keylistener should be added to
	 */
	private void addKeyListener(JButton button) {
		button.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				char buttonPressed = arg0.getKeyChar();
				switch(buttonPressed) {
				case 'q':
					escapeGame();
					break;
				case 'l':
					loadGame();
					break;
				case 'n':
					newGameButtonPressed();
					break;
				case 'm':
					PlayMusic.toggleMusic();
					break;
				}
				
			}
			@Override
			public void keyReleased(KeyEvent arg0) {}
			@Override
			public void keyTyped(KeyEvent arg0) {}
		});
	}

	private void newGameButtonPressed() {
		String name = JOptionPane.showInputDialog("Enter your name adventurer!");
		if (name != null) {
			game.newGame(name, frame);
		}
	}

	private void loadGame() {
		JFileChooser chooser = new JFileChooser();
		//direct the user to the save games folder
		chooser.setCurrentDirectory(new File("data/savegames"));
		int result = chooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			//get the saved game from the save games file
			File saveGame = chooser.getSelectedFile();
			//send file through to the parser
			game.loadGame(saveGame, frame);

		}
	}

	private void escapeGame() {
		int result = JOptionPane.showConfirmDialog(frame, "Exit the application?");
		if (result == JOptionPane.OK_OPTION) {
			//User really wants to exit
			System.exit(0);
		}
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

}
