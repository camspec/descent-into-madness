package frame;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import data.AppData;
import minigames.AdsMinigame;
import minigames.CaptchaMinigame;
import minigames.CheckboxesMinigame;
import minigames.ColoringMinigame;
import minigames.CookieClickerMinigame;
import minigames.CopyTheTextMinigame;
import minigames.MinesweeperMinigame;
import minigames.Minigame;
import minigames.PopupMinigame;
import minigames.ProgressMinigame;
import minigames.QuizMinigame;
import minigames.WaldoPictureMinigame;
import minigames.WaldoTreeMinigame;
import utils.ImageUtils;

public class Game extends JFrame {
	private static final long serialVersionUID = 1L;
	private static Minigame currentMinigame;

	public Game() {
		initializeGUI();
	}

	private void initializeGUI() {
		setTitle(AppData.APPLICATION_TITLE);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		setIconImage(ImageUtils.getImage("images/icon/caedon_icon.png"));

		Screen screen = new Screen();
		screen.setPreferredSize(new Dimension(AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT));
		add(screen);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void runMinigame(int minigame, int x, int y) {
		switch (minigame) {
		case 0:
			currentMinigame = new AdsMinigame(x, y);
			break;
		case 1:
			currentMinigame = new CaptchaMinigame(x, y);
			break;
		case 2:
			currentMinigame = new CheckboxesMinigame(x, y);
			break;
		case 3:
			currentMinigame = new ColoringMinigame(x, y);
			break;
		case 4:
			currentMinigame = new CookieClickerMinigame(x, y);
			break;
		case 5:
			currentMinigame = new CopyTheTextMinigame(x, y);
			break;
		case 6:
			currentMinigame = new MinesweeperMinigame(x, y);
			break;
		case 7:
			currentMinigame = new PopupMinigame(x, y);
			break;
		case 8:
			currentMinigame = new ProgressMinigame(x, y);
			break;
		case 9:
			currentMinigame = new QuizMinigame(x, y);
			break;
		case 10:
			currentMinigame = new WaldoPictureMinigame(x, y);
			break;
		case 11:
			currentMinigame = new WaldoTreeMinigame(x, y);
			break;
		}
	}

	static void closeMinigame() {
		if (currentMinigame != null) {
			currentMinigame.dispose();
		}
	}
}
