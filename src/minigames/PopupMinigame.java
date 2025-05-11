package minigames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import data.AppData;
import utils.ImageUtils;

public class PopupMinigame extends Minigame {
	private static final long serialVersionUID = 1L;
	private final int windowsToClose = 18;
	private int closedWindows = 0;
	private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private final int popupSize = 200;
	private boolean minigameClosed = false;

	public PopupMinigame(int x, int y) {
		super("Please close all pop-ups before accessing the minigame.", new Color(255, 255, 255), x, y);

		ImageIcon minigameIcon = ImageUtils.getImageIcon("images/minigames/popup/abby.png");
		JLabel minigamePicture = new JLabel(minigameIcon);
		add(minigamePicture);
		minigamePicture.setBounds(0, 0, minigameIcon.getIconWidth(), minigameIcon.getIconHeight());

		ImageIcon[] rumiIcons = new ImageIcon[4];
		for (int i = 0; i < 4; i++) {
			rumiIcons[i] = ImageUtils.getImageIcon("images/minigames/popup/rumi_" + i + ".png");
		}

		JFrame[] windows = new JFrame[windowsToClose];

		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
				minigameClosed = true;
				for (JFrame w : windows) {
					w.dispose();
				}
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}
		});

		for (int i = 0; i < windowsToClose; i++) {
			windows[i] = new JFrame();
			windows[i].setResizable(false);

			int frameX;
			int frameY;
			// regenerate new coordinates if they lie on top of the minigame
			do {
				frameX = AppData.RNG.nextInt((int) (screenSize.getWidth() - popupSize));
				frameY = AppData.RNG.nextInt((int) (screenSize.getHeight() - popupSize));
			} while (frameX > screenSize.getWidth() / 2 - MINIGAME_WIDTH / 2 - popupSize
					&& frameX < screenSize.getWidth() / 2 + MINIGAME_WIDTH / 2
					&& frameY > screenSize.getHeight() / 2 - MINIGAME_HEIGHT / 2 - popupSize
					&& frameY < screenSize.getHeight() / 2 + MINIGAME_HEIGHT / 2);

			windows[i].setBounds(frameX, frameY, popupSize, popupSize);
			windows[i].setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			windows[i].setVisible(true);
			windows[i].setAlwaysOnTop(true);

			JLabel popupPicture = new JLabel(rumiIcons[AppData.RNG.nextInt(4)]);
			windows[i].add(popupPicture);
			popupPicture.setBounds(0, 0, rumiIcons[0].getIconWidth(), rumiIcons[0].getIconHeight());

			windows[i].addWindowListener(new WindowListener() {
				@Override
				public void windowOpened(WindowEvent e) {
				}

				@Override
				public void windowClosing(WindowEvent e) {
				}

				@Override
				public void windowClosed(WindowEvent e) {
					closedWindows++;
					// doesn't count if you exited the minigame
					if (closedWindows == windowsToClose && !minigameClosed) {
						completeMinigame();
					}
				}

				@Override
				public void windowIconified(WindowEvent e) {
				}

				@Override
				public void windowDeiconified(WindowEvent e) {
				}

				@Override
				public void windowActivated(WindowEvent e) {
				}

				@Override
				public void windowDeactivated(WindowEvent e) {
				}
			});
		}
	}
}
