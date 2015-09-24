package Renderer;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

public class TestFrame extends JFrame {
	
	GameImagePanel gamePanel = new GameImagePanel();
	//LevelEditorPanel gamePanel = new LevelEditorPanel();
	
	public TestFrame(){
		super("Test Renderer");
		setUp();
	}
	
	public void setUp(){
		setResizable(false); //Makes the window not to be able to change size
		pack();
		setSize(700, 700); //set size to 700 by 700
		setFocusable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Terminate when window is closed
		addGamePanel();
		setVisible(true);
	}
	
	public void addGamePanel(){
		gamePanel.setBackground(new Color(120, 201, 255));
		getContentPane().add(gamePanel);
	}
}
