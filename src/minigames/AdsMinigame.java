package minigames;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import data.AppData;
import utils.ImageUtils;

public class AdsMinigame extends Minigame {
	private static final long serialVersionUID = 1L;
	private int numAds = 25;
	private String[] adTitles = { "Free GTA MOBILE Game Down", "Escape Zip-Tie NOW!", "Download Rise of Kingdoms Free",
			"Marvel is DOOMED", "Have you tried... YELLING?", "Silksong releases 20...", "Pokémon on Nintendo Gameboy",
			"Try Diet Coke Today!", "FORTNITE TOURNAMENT - BATTLE ROYALE", "Burger King's All New",
			"Intersection of Three Planes", "New Hit Indie Game", "New Diet Blows Away Minds",
			"This Is So Fried, Learn More" };
	private int numPossibleAds = adTitles.length;
	private ImageIcon[] adIcons = new ImageIcon[numPossibleAds];

	public AdsMinigame(int x, int y) {
		super("Click the Download button to win the minigame!", new Color(255, 255, 255), x, y);

		for (int i = 0; i < numPossibleAds; i++) {
			adIcons[i] = ImageUtils.getImageIcon("images/minigames/ads/" + i + ".png");
		}

		for (int i = 0; i < numAds; i++) {
			int randomAd = AppData.RNG.nextInt(numPossibleAds);
			int randomX = AppData.RNG.nextInt(400);
			int randomY = AppData.RNG.nextInt(200);
			JInternalFrame frame = new JInternalFrame();
			frame.setVisible(true);
			add(frame);
			frame.setBounds(randomX, randomY, 200, 200);
			frame.setTitle(adTitles[randomAd]);
			frame.setClosable(true);

			ImageIcon adIcon = adIcons[randomAd];
			JLabel adPicture = new JLabel(adIcon);
			frame.add(adPicture);
		}

		ImageIcon downloadIcon = ImageUtils.getImageIcon("images/minigames/ads/download.png");
		JLabel downloadPicture = new JLabel(downloadIcon);
		downloadPicture.setBounds(220, 180, 185, 72);
		add(downloadPicture);
		downloadPicture.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				showMessage("File downloaded.", "The download is complete.");
				completeMinigame();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

		ImageIcon minigameIcon = ImageUtils.getImageIcon("images/minigames/ads/pc_background.png");
		JLabel minigamePicture = new JLabel(minigameIcon);
		add(minigamePicture);
		minigamePicture.setBounds(0, 0, minigameIcon.getIconWidth(), minigameIcon.getIconHeight());

	}
}
