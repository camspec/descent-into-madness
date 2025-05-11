package game;

import java.awt.Color;
import java.awt.image.BufferedImage;

import frame.Screen;
import utils.ImageUtils;

public class GridMap {
	static final int MAP_WIDTH = 100;
	static final int MAP_HEIGHT = 100;
	static final int UNIT_SIZE = 100;
	public static int[][] gameMap = new int[MAP_WIDTH][MAP_HEIGHT];
	// 100x100 textures
	static String[] textures = { "black", "yellow", "blue", "red", "magenta", "cyan", "minigame", "minigame_finished" };
	// # of wall slices = # of pixels in each block-width
	static BufferedImage[][] wallSlices = new BufferedImage[textures.length][UNIT_SIZE];

	// load wall textures and initialize array
	public static void initializeMap() {
		BufferedImage mapImage;
		mapImage = ImageUtils.getImage("images/game/maps/map2.png");
		BufferedImage[] wallImages = new BufferedImage[textures.length];
		for (int i = 0; i < textures.length; i++) {
			wallImages[i] = ImageUtils.getImage("images/game/walls/" + textures[i] + ".png");
		}

		// split the wall textures into slices to be scaled later
		for (int i = 0; i < textures.length; i++) {
			for (int x = 0; x < UNIT_SIZE; x++) {
				wallSlices[i][x] = wallImages[i].getSubimage(x, 0, 1, UNIT_SIZE);
			}
		}

		// initialize map array
		for (int y = 0; y < MAP_HEIGHT; y++) {
			for (int x = 0; x < MAP_WIDTH; x++) {
				// simplify int colours
				if (mapImage.getRGB(x, y) == new Color(255, 255, 255).getRGB()) {
					gameMap[x][y] = -1; // white (empty space)
				} else if (mapImage.getRGB(x, y) == new Color(0, 0, 0).getRGB()) {
					gameMap[x][y] = 0; // black (border)
				} else if (mapImage.getRGB(x, y) == new Color(255, 255, 0).getRGB()) {
					gameMap[x][y] = 1; // yellow
				} else if (mapImage.getRGB(x, y) == new Color(0, 0, 255).getRGB()) {
					gameMap[x][y] = 2; // blue
				} else if (mapImage.getRGB(x, y) == new Color(255, 0, 0).getRGB()) {
					gameMap[x][y] = 3; // red
				} else if (mapImage.getRGB(x, y) == new Color(255, 0, 255).getRGB()) {
					gameMap[x][y] = 4; // magenta
				} else if (mapImage.getRGB(x, y) == new Color(0, 255, 255).getRGB()) {
					gameMap[x][y] = 5; // cyan
				} else if (mapImage.getRGB(x, y) == new Color(255, 128, 0).getRGB()) {
					gameMap[x][y] = 6; // orange (minigame)
				} else if (mapImage.getRGB(x, y) == new Color(0, 255, 0).getRGB()) {
					gameMap[x][y] = -1; // green (player spawn point)
					Player.setX(x * UNIT_SIZE + (double) UNIT_SIZE / 2);
					Player.setY(y * UNIT_SIZE + (double) UNIT_SIZE / 2);
				} else if (mapImage.getRGB(x, y) == new Color(168, 168, 168).getRGB()) {
					gameMap[x][y] = -1; // gray (caedon spawn point)
					Screen.caedon.setX(x * UNIT_SIZE + UNIT_SIZE / 2);
					Screen.caedon.setY(y * UNIT_SIZE + UNIT_SIZE / 2);
				}
			}
		}
	}

	public static boolean checkValidPath(int gridX, int gridY) {
		return gameMap[gridX][gridY] == -1 && gridX >= 0 && gridX < MAP_WIDTH && gridY >= 0 && gridY < MAP_HEIGHT;
	}
}
