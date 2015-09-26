package view;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

public class GameInterface {
	private JFrame frame;
	private static final String IMAGE_PATH = "data/images/";
	private boolean isPlaying;

	public GameInterface() {
		initialise();
	}

	public void initialise() {
		frame = new JFrame();
		frame.setBounds(100, 30, 1100, 750);
		frame.setResizable(false);

		//initialise window listener
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				escapeGame();
			}
		});
		//set up border
		Border blackline = BorderFactory.createLineBorder(Color.BLACK, 2);
		
		// set up inventory panel
		// when drawn, the panel should contain black lines separating out the three sections:
		// Food - Max 4
		// Tools - Max 6
		// Key - Max 1 at a time
		JPanel inventoryPanel = new JPanel(); //will be a custom drawn panel
		inventoryPanel.setBorder(blackline);
		inventoryPanel.setBounds(20, 570, 820, 140);
		inventoryPanel.setLayout(null);
		
		//setting up food panel
		//setting up food slots - these will be filled in when the player has picked up food
		//4 slots
		for (int i = 0; i < 5; ++i) {
			JLabel inventoryBox = new JLabel();
			inventoryBox.setBorder(blackline);
			inventoryBox.setBounds(20 + i * 90, 30, 80, 80);
			inventoryPanel.add(inventoryBox);
		}
		
		//set up tool belt
		//max 6 tools
		for (int i = 0; i < 2; ++i) {
			//2 rows of tools, each row contains 3 slots
			for (int j = 0; j < 3; ++j) {
				JLabel toolBox = new JLabel();
				toolBox.setBorder(blackline);
				toolBox.setBounds(480 + j * 65, 10 + i * 65, 55, 55);
				inventoryPanel.add(toolBox);
			}
		}
		
		// set up key slot
		// player can only hold 1 key at a time
		JLabel inventoryBox = new JLabel();
		inventoryBox.setBorder(blackline);
		inventoryBox.setBounds(690, 20, 100, 100);
		inventoryPanel.add(inventoryBox);
		
		frame.getContentPane().add(inventoryPanel);
		
		//set up panel which will contain minimap
		JPanel mapPanel = new JPanel();
		mapPanel.setBorder(blackline);
		mapPanel.setBounds(860, 490, 220, 220);
		frame.getContentPane().add(mapPanel);
		
		
		
//		//play song
//		final PlayMusic music = new PlayMusic();
//		music.playSound("SayMyName48.mp3");
//		isPlaying = true;
//
//		//Toggle Music Button
//		JButton toggleMusicButton = new JButton("Toggle Music");
//		toggleMusicButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (isPlaying) {
//					music.stopPlaying();
//					isPlaying = false;
//				}
//				else {
//					//final PlayMusic music = new PlayMusic();
//					music.playSound("SayMyName48.mp3");
//					isPlaying = true;
//				}
//			}
//		});
//		toggleMusicButton.setBounds(830, 520, 150, 40);
//		frame.getContentPane().add(toggleMusicButton);

		frame.getContentPane().requestFocus();
		frame.getContentPane().setLayout(null);
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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameInterface game = new GameInterface();
					game.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

