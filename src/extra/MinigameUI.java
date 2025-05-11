package extra;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import data.AppData;
import frame.Game;

public class MinigameUI extends JFrame {
	private static final long serialVersionUID = 1L;

	public MinigameUI() {
		initializeGUI();
	}

	private void initializeGUI() {
		setResizable(false);
		setBounds(0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT);
		setLocationRelativeTo(null);
		setTitle(AppData.APPLICATION_TITLE);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(null);
		setVisible(true);

		String[] minigames = { "ads", "captcha", "checkboxes", "coloring", "cookieClicker", "copyTheText",
				"minesweeper", "popup", "progress", "quiz", "waldoPicture", "waldoTree" };

		for (int i = 0; i < 12; i++) {
			JButton captchaButton = new JButton();
			add(captchaButton);
			if (i > 7) {
				captchaButton.setBounds(150 * (i - 8), 160, 100, 100);
			} else {
				captchaButton.setBounds(150 * i, 10, 100, 100);
			}

			int minigameIndex = i;

			captchaButton.setText(minigames[i]);
			captchaButton.setFont(new Font("SansSerif", Font.PLAIN, 10));
			captchaButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Game.runMinigame(minigameIndex, 0, 0);
					AppData.inMinigame = true;
				}
			});
		}
	}
}
