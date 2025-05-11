package minigames;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import data.AppData;
import utils.ImageUtils;

public class CheckboxesMinigame extends Minigame {
	private static final long serialVersionUID = 1L;
	private String[] raccoonItems = { "balloon", "sunglasses", "beach ball", "bass clarinet", "raccoon",
			"tungsten cube", "infinity gauntlet", "bread", "lightsaber", "spoon", "rubber chicken" };
	private JCheckBox[] checkBoxes = new JCheckBox[raccoonItems.length];
	private ImageIcon[] itemIcons = new ImageIcon[raccoonItems.length];
	private JLabel[] itemLabels = new JLabel[raccoonItems.length];
	private JLabel[] chosenItemLabels = new JLabel[raccoonItems.length];
	private int[][] itemCoordinates = { { 30, 30 }, { 0, 97 }, { 100, 80 }, { -75, 80 }, { 60, 200 }, { -80, 200 },
			{ 55, 125 }, { 0, 210 }, { 30, 30 }, { 20, 40 }, { -70, 140 } };
	private boolean[] enabledItems = new boolean[raccoonItems.length];
	private boolean[] chosenEnabledItems = new boolean[raccoonItems.length];
	private int correctItems = 0;

	public CheckboxesMinigame(int x, int y) {
		super("Match the images!", new Color(255, 255, 200), x, y);
		ImageIcon raccoonIcon = ImageUtils.getImageIcon("images/minigames/checkboxes/raccoon.png");

		for (int i = 0; i < raccoonItems.length; i++) {
			enabledItems[i] = AppData.RNG.nextBoolean();
		}

		for (int i = 0; i < raccoonItems.length; i++) {
			itemIcons[i] = ImageUtils.getImageIcon("images/minigames/checkboxes/" + i + ".png");
			itemLabels[i] = new JLabel(itemIcons[i]);
			add(itemLabels[i]);
			itemLabels[i].setBounds(itemCoordinates[i][0], itemCoordinates[i][1], 300, 300);
			itemLabels[i].setVisible(enabledItems[i]);
		}

		for (int i = 0; i < raccoonItems.length; i++) {
			chosenItemLabels[i] = new JLabel(itemIcons[i]);
			add(chosenItemLabels[i]);
			chosenItemLabels[i].setBounds(itemCoordinates[i][0] + 320, itemCoordinates[i][1], 300, 300);
			chosenItemLabels[i].setVisible(false);
		}

		JLabel exampleRaccoon = new JLabel(raccoonIcon);
		add(exampleRaccoon);
		exampleRaccoon.setBounds(0, 110, 300, 300);

		JLabel chosenRaccoon = new JLabel(raccoonIcon);
		add(chosenRaccoon);
		chosenRaccoon.setBounds(320, 110, 300, 300);

		JLabel exampleTitle = new JLabel();
		exampleTitle.setForeground(new Color(0, 0, 0));
		add(exampleTitle);
		exampleTitle.setBounds(35, 85, 400, 40);
		exampleTitle.setText("Example Raccoon");
		exampleTitle.setFont(new Font("Serif", Font.BOLD, 30));

		JLabel chosenTitle = new JLabel();
		chosenTitle.setForeground(new Color(0, 0, 0));
		add(chosenTitle);
		chosenTitle.setBounds(380, 85, 400, 40);
		chosenTitle.setText("Your Raccoon");
		chosenTitle.setFont(new Font("Serif", Font.BOLD, 30));

		JButton completeButton = new JButton();
		add(completeButton);
		completeButton.setBounds(210, 395, 200, 40);
		completeButton.setText("Click to submit!");
		completeButton.setFont(new Font("Serif", Font.BOLD, 12));
		completeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				correctItems = 0;
				for (int i = 0; i < raccoonItems.length; i++) {
					if (enabledItems[i] == chosenEnabledItems[i]) {
						correctItems++;
					}
				}
				if (correctItems == raccoonItems.length) {
					completeMinigame();
				}
			}
		});

		for (int i = 0; i < raccoonItems.length; i++) {
			int currentItem = i;
			checkBoxes[i] = new JCheckBox();
			add(checkBoxes[i]);
			checkBoxes[i].setBackground(new Color(255, 255, 200));
			checkBoxes[i].setBounds(50 + i * 50, 20, 25, 25);
			checkBoxes[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean isSelected = checkBoxes[currentItem].isSelected();
					chosenEnabledItems[currentItem] = isSelected;
					chosenItemLabels[currentItem].setVisible(isSelected);
				}
			});
		}

	}
}
