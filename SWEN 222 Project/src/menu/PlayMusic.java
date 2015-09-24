package menu;
import javazoom.jl.player.Player;

public class PlayMusic {
	private static final String MEDIA_PATH = "data/music/";

	public static synchronized void playSound(String fileName) {
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

	public static void playMusic(String fileName) {
		try {
			java.net.URL songURL = PlayMusic.class.getResource(MEDIA_PATH + fileName);
			Player playMP3 = new Player(songURL.openStream());
			playMP3.play();
		} catch (Exception exc) {
			exc.printStackTrace();
			System.out.println("Failed to play the file.");
		}
	}
}
