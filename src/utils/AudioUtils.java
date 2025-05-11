package utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioUtils {
	private static ArrayList<Clip> audioClips = new ArrayList<>();
	private static ArrayList<String> audioNames = new ArrayList<>();
	private static ArrayList<Boolean> loopedAudios = new ArrayList<>();

	public static void loadAudioFile(String path, String name, boolean loop) {
		URL url = getURL(path);
		Clip clip;
		try {
			clip = AudioSystem.getClip();
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
			clip.open(audioInput);
			loopedAudios.add(loop);
			audioClips.add(clip);
			audioNames.add(name);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void playAudio(String name) {
		for (String a : audioNames) {
			if (a.equals(name)) {
				int index = audioNames.indexOf(a);
				Clip clip = audioClips.get(index);
				if (loopedAudios.get(index)) {
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				} else {
					clip.start();
				}

			}
		}
	}

	public static void stopAudio(String name) {
		for (String a : audioNames) {
			if (a.equals(name)) {
				int index = audioNames.indexOf(a);
				Clip clip = audioClips.get(index);
				clip.stop();
			}
		}
	}

	private static URL getURL(String path) {
		return AudioUtils.class.getClassLoader().getResource(path);
	}
}
