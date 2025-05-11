package minigames;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import data.AppData;
import utils.ImageUtils;

public class WaldoPictureMinigame extends Minigame {
	private static final long serialVersionUID = 1L;
	private int[][] waldoSolutions = { { 500, 320, 46, 59 }, { 468, 68, 37, 38 }, { 402, 172, 27, 37 },
			{ 188, 347, 38, 46 }, { 71, 139, 33, 51 }, { 252, 155, 27, 47 }, { 430, 184, 26, 26 }, { 403, 169, 25, 29 },
			{ 534, 307, 39, 73 }, { 113, 65, 39, 52 }, { 204, 325, 36, 38 } };
	private final int numWaldoPictures = waldoSolutions.length;

	public WaldoPictureMinigame(int x, int y) {
		super("Where's Waldo?", new Color(255, 255, 255), x, y);

		// x, y, width, height

		int randomWaldoPicture = AppData.RNG.nextInt(numWaldoPictures);

		ImageIcon waldoIcon = ImageUtils.getImageIcon("images/minigames/find_waldo/" + randomWaldoPicture + ".png");
		JLabel waldoPicture = new JLabel(waldoIcon);
		add(waldoPicture);
		waldoPicture.setBounds(0, 0, waldoIcon.getIconWidth(), waldoIcon.getIconHeight());

		JLabel waldoHitbox = new JLabel();
		add(waldoHitbox);
		int waldoX = waldoSolutions[randomWaldoPicture][0];
		int waldoY = waldoSolutions[randomWaldoPicture][1];
		int width = waldoSolutions[randomWaldoPicture][2];
		int height = waldoSolutions[randomWaldoPicture][3];
		waldoHitbox.setBounds(waldoX, waldoY, width, height);
		waldoHitbox.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				completeMinigame();
			}
		});
	}
}
