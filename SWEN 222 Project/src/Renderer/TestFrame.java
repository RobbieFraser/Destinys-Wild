package Renderer;

import java.awt.Color;

import javax.swing.JFrame;

import game.Board;

public class TestFrame extends JFrame {
	
	private Board board;
	
	private int red;
	private int green;
	private int blue;
	
	private boolean up = true;
	
	private GameImagePanel gamePanel;
	private LevelEditorPanel editPanel = new LevelEditorPanel();
	private boolean editor = false;
	
	
	/*
	 * This version of the TestFrame constructor inits the Game Panel
	 */
	public TestFrame(Board board){
		super("Test Renderer");
		this.board = board;
		gamePanel = new GameImagePanel(board);
		setUp();
		//gamePanel.waterTest();
		//loop();
	}
	
	/*
	 * This version of the TestFrame constructor inits the Level editor
	 */
	public TestFrame(boolean editor){
		super("Level Editor");
		this.editor = editor;
		setUp();
	}
	
	/*
	 * Just for fun, a crazy Hotline Miami style background for the game panel
	 */
	private void loop(){
		while(true){
			while(up){
				green++;
				gamePanel.setBackground(new Color(red, green, blue));
				gamePanel.repaint();
				if(green == 255){
					up = false;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			while(!up){
				green--;
				gamePanel.setBackground(new Color(red, green, blue));
				gamePanel.repaint();
				if(green == 123){
					up = true;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			while(up){
				red++;
				gamePanel.setBackground(new Color(red, green, blue));
				gamePanel.repaint();
				if(red == 255){
					up = false;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			while(!up){
				red--;
				gamePanel.setBackground(new Color(red, green, blue));
				gamePanel.repaint();
				if(red == 123){
					up = true;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setUp(){
		setResizable(false); //Makes the window not to be able to change size
		pack();
		setSize(700, 700); //set size to 700 by 700
		setFocusable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Terminate when window is closed
		if(editor){
			addEditPanel();
		}
		else{
			addGamePanel();
		}
		setVisible(true);
	}
	
	public void addGamePanel(){
		red = 123;
		green = 123;
		blue = 255;
		gamePanel.setBackground(new Color(120, 201, 255));
		getContentPane().add(gamePanel);
	}
	
	public void addEditPanel(){
		editPanel.setBackground(new Color(120, 201, 255));
		getContentPane().add(editPanel);
	}
}
