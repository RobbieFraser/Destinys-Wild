package menu;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

public class MenuInterface {
	private JFrame frame;

	public MenuInterface() {
		initialise();
	}

	public void initialise() {
		frame = new JFrame();
		frame.setBounds(100, 30, 1000, 600);

		ImagePanel imagePanel = new ImagePanel("forest.png");

		imagePanel.getInputMap().put(KeyStroke.getKeyStroke("N"),"New Game");
		ActionTest newGame = new ActionTest("New Game", "This button starts a new game", new Integer(KeyEvent.VK_N));
		imagePanel.getActionMap().put("New Game", newGame);

		imagePanel.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"),"Exit Game");
		ActionTest exitGame = new ActionTest("Exit Game", "This button exits the game.", new Integer(KeyEvent.VK_ESCAPE));
		imagePanel.getActionMap().put("Exit Game", exitGame);

		frame.setContentPane(imagePanel);
		frame.setResizable(false);

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
		Border blackline = BorderFactory.createLineBorder(Color.green, 5);
		menuPanel.setBorder(blackline);
		menuPanel.setOpaque(false);
		menuPanel.setLayout(null);
		menuPanel.setBounds(380, 140, 200, 400);

		//button 1
		JButton btnNewGame = new JButton("New Game");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("New Game button pressed");
			}
		});
		btnNewGame.setBounds(25, 50, 150, 100);
		menuPanel.add(btnNewGame);

		//button 2
		JButton btnQuitGame = new JButton("Quit Game");
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
		menuPanel.add(btnQuitGame);

		//play song
		//PlayMusic.playSound("SayMyName48.mp3");

		frame.getContentPane().requestFocus();
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(menuPanel);
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

	//TODO: Allow class to differentiate between escape and n/N buttons being pressed
	public class ActionTest extends AbstractAction {
		public ActionTest(String text, String desc, Integer mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		public void actionPerformed(ActionEvent e) {
			String id = e.getActionCommand();
			System.out.println(id);
		}
	}
}
