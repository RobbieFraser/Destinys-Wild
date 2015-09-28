package menu;

import java.io.FileInputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class PlayMusic {
	private static final String MEDIA_PATH = "data/music/";
	private FileInputStream musicInputStream;
	private Player playMP3;

	public synchronized void playSound(final String fileName) {
		new Thread(new Runnable() {
			public void run() {
				try {
					playMusic(fileName);
					startPlaying();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public void playMusic(String fileName) {
		try {
			musicInputStream = new FileInputStream(MEDIA_PATH
					+ fileName);
			playMP3 = new Player(musicInputStream);
		} catch (Exception exc) {
			exc.printStackTrace();
			System.out.println("Failed to play the file.");
		}
	}

	public void startPlaying(){
		try {
			playMP3.play();
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stopPlaying(){
		playMP3.close();
	}
}
