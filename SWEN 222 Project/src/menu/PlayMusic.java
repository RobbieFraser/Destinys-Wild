package menu;

import java.io.FileInputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class PlayMusic {
	private static final String MEDIA_PATH = "data/music/";
	private static FileInputStream musicInputStream;
	private static Player playMP3;
	private static boolean isPlaying;

	public static synchronized void playSound(final String fileName) {
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

	public static void playMusic(String fileName) {
		try {
			musicInputStream = new FileInputStream(MEDIA_PATH
					+ fileName);
			playMP3 = new Player(musicInputStream);
		} catch (Exception exc) {
			exc.printStackTrace();
			System.out.println("Failed to play the file.");
		}
	}

	public static void startPlaying() {
		try {
			isPlaying = true;
			playMP3.play();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}

	public static void stopPlaying() {
		isPlaying = false;
		playMP3.close();
	}
	
	public static boolean getIsPlaying() {
		return isPlaying;
	}
}
