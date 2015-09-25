package Renderer;

import java.awt.Color;

import javax.swing.JFrame;

import game.Board;

public class TestFrame extends JFrame {
	
	private Board board;
	
	private GameImagePanel gamePanel;
	//private LevelEditorPanel gamePanel = new LevelEditorPanel();
	
	public TestFrame(Board board){
		super("Test Renderer");
		this.board = board;
		gamePanel = new GameImagePanel(board);
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
