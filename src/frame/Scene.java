package frame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import data.AppData;
import utils.ImageUtils;

public class Scene {
	private BufferedImage[] frames;
	private int numFrames;
	private int currentFrame = 0;
	private String name;

	public Scene(int numFrames, String name) {
		this.numFrames = numFrames;
		frames = new BufferedImage[numFrames];
		this.name = name;
		initializeScene();
	}

	private void initializeScene() {
		for (int i = 0; i < numFrames; i++) {
			frames[i] = ImageUtils.getImage("images/cutscenes/" + name + "/" + i + ".png");
		}
	}

	public void drawFrame(Graphics g) {
		g.drawImage(frames[currentFrame], 0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT, null);
		currentFrame++;
		if (currentFrame >= numFrames) {
			currentFrame = 0;
		}
	}

	public int getFrame() {
		return currentFrame;
	}
}
