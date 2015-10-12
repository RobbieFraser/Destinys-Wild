package view;

import java.io.FileInputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class PlayMusic {
	private static final String MEDIA_PATH = "data/music/";
	private static FileInputStream musicInputStream;
	private static Player playMP3;
	private static boolean isPlaying;
	private static boolean isFinished = false;
	private static boolean running = true;

	public static synchronized void playSound(final String fileName) {
		new Thread(new Runnable() {
			public void run() {
				try {
					playMusic(fileName);
					startPlaying();
					loop();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public static void playMusic(String fileName) {
		try {
			musicInputStream = new FileInputStream(MEDIA_PATH + fileName);
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

	public static void tick() throws JavaLayerException {
		//System.out.println(playMP3.isComplete());
		if(playMP3.isComplete()){
			isFinished = true;
		}
		if(isFinished == playMP3.isComplete()){
			startPlaying();
			//playSound("DestinysWildOST.mp3");
			playSound("DestinysWildOST.mp3");
		}
			//playMP3.play();
		
	}

	public static void loop() throws JavaLayerException {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 30D;
		int ticks = 0;
		double diff = 0;
		long lastTimer = System.currentTimeMillis();
		while (running) {
			// if(!paused){
			long now = System.nanoTime();
			diff += ((now - lastTime) / nsPerTick);
			lastTime = now;
			while (diff >= 1) {
				ticks++;
				try {
					tick();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				diff--;
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				// System.out.println("Daytime changed");
				// System.out.println("Ticks: " + ticks);
				ticks = 0;
			}
		}
	}

	public static void toggleMusic() {
		if (isPlaying) {
			stopPlaying();
			isFinished = true;
		} else {
			playSound("DestinysWildOST.mp3");
		}
	}
}
