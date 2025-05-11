package data;

import java.util.Random;

public class AppData {
	public static final int SCREEN_WIDTH = 1024;
	public static final int SCREEN_HEIGHT = 768;
	public static String gameState = "opening";
	public static final String APPLICATION_TITLE = "Caedon's Descent into Madness";
	public static final Random RNG = new Random();
	public static final int NUM_MINIGAMES = 12;
	public static final boolean SKIP_OPENING = false;
	public static int nextMinigame = 0;
	public static boolean inMinigame = false;
	public static final boolean minigameUIEnabled = false;
}
