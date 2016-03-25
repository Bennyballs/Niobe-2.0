package src;


import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class SoundPlayer implements Runnable {

	private AudioInputStream stream;
	private DataLine.Info info;
	private Clip sound;

	private InputStream musicFile;
	private Thread player;

	public SoundPlayer(InputStream musicfile) {
		System.out.println("Test");
		this.musicFile = musicfile;
		player = new Thread(this);
		player.start();
	}

	@Override
	public void run() {
		try {
			stream = AudioSystem.getAudioInputStream(musicFile);
			info = new DataLine.Info(Clip.class, stream.getFormat());
			sound = (Clip) AudioSystem.getLine(info);
			sound.open(stream);
			sound.start();
			while (sound.isActive()) {
				Thread.sleep(250);
			}
			Thread.sleep(10000);
			sound.close();
			stream.close();
			player.interrupt();
		} catch (Exception e) {
			player.interrupt();
			System.out.println("Error player sounds");
		}
	}
}