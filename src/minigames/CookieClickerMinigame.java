package minigames;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import utils.ImageUtils;

public class CookieClickerMinigame extends Minigame {
	private static final long serialVersionUID = 1L;
	private int cookies = 0;
	private int cookiesPerClick = 1;
	private int cookieUpgradeCost = 15;
	private int eliteUpgradeCost = 100;
	private int winCost = 800;

	private JLabel cookiesText = new JLabel();
	private JLabel cookiesPerClickText = new JLabel();

	public CookieClickerMinigame(int x, int y) {
		super("Cookie Clicker", new Color(50, 50, 100), x, y);

		ImageIcon cookieIcon = ImageUtils.getImageIcon("images/minigames/cookie_clicker/cookie.png");
		JLabel bigCookie = new JLabel(cookieIcon);
		add(bigCookie);
		bigCookie.setBounds(170, 110, 300, 300);
		bigCookie.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				cookies += cookiesPerClick;
				updateCookieAmounts();
			}
		});

		JLabel titleText = new JLabel();
		titleText.setForeground(new Color(255, 255, 255));
		add(titleText);
		titleText.setBounds(215, 20, 400, 40);
		titleText.setText("Cookie Clicker");
		titleText.setFont(new Font("Comic Sans MS", Font.BOLD, 30));

		add(cookiesText);
		cookiesText.setForeground(new Color(255, 255, 255));
		cookiesText.setBounds(270, 70, 400, 40);
		cookiesText.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));

		cookiesPerClickText.setForeground(new Color(255, 255, 255));
		add(cookiesPerClickText);
		cookiesPerClickText.setBounds(10, 140, 400, 40);
		cookiesPerClickText.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));

		JButton upgradeButton = new JButton();
		add(upgradeButton);
		upgradeButton.setBounds(10, 180, 160, 40);
		upgradeButton.setText("Cookie Upgrade: " + cookieUpgradeCost + " cookies");
		upgradeButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 10));
		upgradeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cookies >= cookieUpgradeCost) {
					cookies -= cookieUpgradeCost;
					cookiesPerClick += 1;
					updateCookieAmounts();
				}
			}
		});

		JButton eliteUpgradeButton = new JButton();
		add(eliteUpgradeButton);
		eliteUpgradeButton.setBounds(10, 230, 160, 40);
		eliteUpgradeButton.setText("Elite Upgrade: " + eliteUpgradeCost + " cookies");
		eliteUpgradeButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 10));
		eliteUpgradeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cookies >= eliteUpgradeCost) {
					cookies -= eliteUpgradeCost;
					cookiesPerClick += 25;
					updateCookieAmounts();
				}
			}
		});

		JButton winButton = new JButton();
		add(winButton);
		winButton.setBounds(10, 280, 160, 40);
		winButton.setText("Win the game: " + winCost + " cookies");
		winButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 10));
		winButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cookies >= winCost) {
					completeMinigame();
				}
			}
		});

		ImageIcon minigameIcon = ImageUtils.getImageIcon("images/minigames/cookie_clicker/cookie_background.png");
		JLabel minigamePicture = new JLabel(minigameIcon);
		add(minigamePicture);
		minigamePicture.setBounds(0, 0, minigameIcon.getIconWidth(), minigameIcon.getIconHeight());

		updateCookieAmounts();
	}

	private void updateCookieAmounts() {
		cookiesText.setText(cookies + " cookies");

		if (cookiesPerClick == 1) {
			cookiesPerClickText.setText(cookiesPerClick + " cookie per click");
		} else {
			cookiesPerClickText.setText(cookiesPerClick + " cookies per click");
		}
	}
}
