package menu;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class PlayMusic {
	private final String MEDIA_PATH = "data/music/";

	public synchronized void playSound(String fileName) {
		new Thread(new Runnable() {
			public void run() {
				try {
					playMusic(fileName);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public void playMusic(String fileName) {
		try {
			FileInputStream musicInputStream = new FileInputStream(MEDIA_PATH + fileName);
			Player playMP3 = new Player(musicInputStream);
			playMP3.play();
		} catch (Exception exc) {
			exc.printStackTrace();
			System.out.println("Failed to play the file.");
		}
	}
}
