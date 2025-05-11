package frame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import data.AppData;
import game.Entity;
import game.GridMap;
import game.Player;
import game.Ray;
import utils.AudioUtils;
import utils.ImageUtils;
import utils.Timestamp;

public class Screen extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Timer timer = new Timer(20, this);

	private Scene openingScene = new Scene(64, "opening");
	private Sprite menuBackground = new Sprite(ImageUtils.getImage("images/menu/background.png"), -500, 0, 2199,
			AppData.SCREEN_HEIGHT);
	private Sprite menuTitle = new Sprite(ImageUtils.getImage("images/menu/title.png"), 0, 0, AppData.SCREEN_WIDTH,
			AppData.SCREEN_HEIGHT);
	private BufferedImage menuFilmEffect = ImageUtils.getImage("images/menu/film_effect_new.png");
	private BufferedImage menuButtons = ImageUtils.getImage("images/menu/menu_buttons.png");
	private BufferedImage controlsMenu = ImageUtils.getImage("images/menu/controls_menu.png");
	private BufferedImage infoText = ImageUtils.getImage("images/game/overlay/info_text.png");
	private BufferedImage lightning = ImageUtils.getImage("images/game/overlay/lightning.png");
	private BufferedImage emptyLightning = ImageUtils.getImage("images/game/overlay/lightning_empty.png");
	private BufferedImage pressEPrompt = ImageUtils.getImage("images/game/overlay/press_e.png");
	private BufferedImage deathScreen = ImageUtils.getImage("images/game/overlay/you_died.png");
	private BufferedImage pauseMenu = ImageUtils.getImage("images/game/overlay/pause_menu.png");
	private BufferedImage[] creditsImages = new BufferedImage[6];
	private double titleVelocity = -0.5;
	private double backgroundVelocity = -1;

	private int alpha = 255;

	private double mouseX = -1, mouseY = -1;
	private int[] selectionXs = { 0, 0, 0 }, selectionAlphas = { 0, 0, 0 };
	private int buttonHighlighted = -1, buttonSelected = -1;

	private Timestamp loadingTimestamp = new Timestamp();

	private Color skyColor = new Color(33, 5, 5);

	private ArrayList<Ray> rays = new ArrayList<>();

	private int[] minigames = new int[AppData.NUM_MINIGAMES];

	private boolean playedLaugh = false;
	private boolean playedShutdownSound = false;

	private boolean controlsMenuEnabled = false;
	private boolean pauseMenuEnabled = false;

	public static Entity caedon = new Entity("images/game/entities/caedon_face.png");

	private int playerLastGridX, playerLastGridY;

	private Timestamp speedTimestamp = new Timestamp();

	public Screen() {
		addKeyListener(new Adapter());
		addMouseListener(new Listener());
		setFocusable(true);

		scrambleMinigames();
		initializeRays();
		loadCredits();
		loadAudio();

		GridMap.initializeMap();

		playerLastGridX = Player.getGridX();
		playerLastGridY = Player.getGridY();
		caedon.generatePath();

		timer.start();
		loadingTimestamp.setTimestamp();
		speedTimestamp.setTimestamp();
		AudioUtils.playAudio("menu");
	}

	private void scrambleMinigames() {
		for (int i = 0; i < AppData.NUM_MINIGAMES; i++) {
			minigames[i] = i;
		}

		for (int i = AppData.NUM_MINIGAMES - 1; i > 0; i--) {
			int index = AppData.RNG.nextInt(i + 1);
			// swap the minigames
			int temp = minigames[index];
			minigames[index] = minigames[i];
			minigames[i] = temp;
		}
	}

	private void initializeRays() {
		for (int i = -AppData.SCREEN_WIDTH / 2; i < 0; i++) {
			rays.add(new Ray(i));
		}
		for (int i = 1; i <= AppData.SCREEN_WIDTH / 2; i++) {
			rays.add(new Ray(i));
		}
	}

	private void loadCredits() {
		for (int i = 0; i < 6; i++) {
			creditsImages[i] = ImageUtils.getImage("images/credits/" + i + ".png");
		}
	}

	private void loadAudio() {
		AudioUtils.loadAudioFile("audio/sounds/menu_select.wav", "menu_select", false);
		AudioUtils.loadAudioFile("audio/sounds/laugh.wav", "laugh", false);
		AudioUtils.loadAudioFile("audio/music/Caedon't Stop Running.wav", "game", true);
		AudioUtils.loadAudioFile("audio/sounds/death.wav", "death", false);
		AudioUtils.loadAudioFile("audio/sounds/shutdown.wav", "shutdown", false);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		switch (AppData.gameState) {
		case "opening":
			openingScene.drawFrame(g);
			if (AppData.SKIP_OPENING) {
				AppData.gameState = "menu";
			}
			if (loadingTimestamp.getTimeElapsedMillis() >= 5000) {
				AppData.gameState = "menu";
			}
			break;
		case "menu":
			// switch from opening cutscene frame rate
			timer.setDelay(5);

			// pan background
			menuBackground.setX(menuBackground.getX() + backgroundVelocity);
			if (menuBackground.getX() < -800) {
				backgroundVelocity += 0.02;
			} else if (menuBackground.getX() > -200) {
				backgroundVelocity -= 0.02;
			}
			menuBackground.drawSprite(g);

			// darken background a bit
			g.setColor(new Color(0, 0, 0, 70));
			g.fillRect(0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT);

			// floating title
			menuTitle.setY(menuTitle.getY() + titleVelocity);
			if (menuTitle.getY() < -10) {
				titleVelocity += 0.01;
			} else if (menuTitle.getY() > 10) {
				titleVelocity -= 0.01;
			}
			menuTitle.drawSprite(g);

			drawSelectionBoxes(g);

			// menu buttons and overlay
			g.drawImage(menuButtons, 0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT, null);
			g.drawImage(menuFilmEffect, 0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT, null);

			if (controlsMenuEnabled) {
				g.drawImage(controlsMenu, 0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT, null);
			}

			// fade in
			if (alpha > 0) {
				g.setColor(new Color(0, 0, 0, alpha));
				alpha -= 4;
				g.fillRect(0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT);
			}
			break;
		case "loading":
			// fade out
			if (alpha < 255) {
				alpha += 10;
			}
			if (alpha >= 255) {
				alpha = 255;
			}
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT);
			if (loadingTimestamp.getTimeElapsedMillis() > 1200 && !playedLaugh) {
				AudioUtils.playAudio("laugh");
				playedLaugh = true;
			}
			if (loadingTimestamp.getTimeElapsedMillis() > 3000) {
				AppData.gameState = "game";
				AudioUtils.playAudio("game");
			}
			break;
		case "game":
			g.setColor(skyColor);
			g.fillRect(0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT);

			for (Ray r : rays) {
				r.cast();
				r.drawRay(g);
			}

			caedon.drawEntity(g);
			if (caedon.getEntityScreenX() + caedon.getEntitySize() > 0
					&& caedon.getEntityScreenX() < AppData.SCREEN_WIDTH) {
				for (Ray ray : rays) {
					if (ray.getDistance() < caedon.getEntityDistance()) {
						ray.drawRay(g);
					}
				}
			}

			// if player changed grid location, regenerate path
			if (playerLastGridX != Player.getGridX() || playerLastGridY != Player.getGridY()) {
				playerLastGridX = Player.getGridX();
				playerLastGridY = Player.getGridY();
				caedon.path.clear();
				caedon.generatePath();
			}

			g.setColor(new Color(0, 0, 0, 100));
			g.fillRect(0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT);

			// screen turns red when you lose health
			g.setColor(new Color(255, 0, 0, 100 - Player.getHealth()));
			g.fillRect(0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT);

			// overlay
			drawStaminaBox(g);
			g.drawImage(infoText, 0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT, null);
			g.setColor(new Color(255, 255, 255, 15));
			g.fillRect(AppData.SCREEN_WIDTH / 2 - 175, 85, 353, lightning.getHeight() + 10);
			for (int i = 0; i < AppData.NUM_MINIGAMES; i++) {
				if (AppData.nextMinigame > i) {
					g.drawImage(lightning, AppData.SCREEN_WIDTH / 2 - 170 + i * 30, 90, lightning.getWidth(), lightning.getHeight(), null);
				} else {
					g.drawImage(emptyLightning, AppData.SCREEN_WIDTH / 2 - 170 + i * 30, 90, emptyLightning.getWidth(), emptyLightning.getHeight(),
							null);
				}
			}
			// draw "press e" popup
			Point minigameLocation = Player.getAdjacentMinigame();
			if (minigameLocation != null) {
				g.drawImage(pressEPrompt, 0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT, null);
			}

			if (AppData.nextMinigame == AppData.NUM_MINIGAMES) {
				if (!playedShutdownSound) {
					AudioUtils.stopAudio("game");
					AudioUtils.playAudio("shutdown");
					playedShutdownSound = true;
				}
				// fade out
				if (alpha < 255) {
					alpha += 10;
				}
				if (alpha >= 255) {
					alpha = 255;
					AppData.gameState = "credits loading";
					loadingTimestamp.setTimestamp();
				}
				g.setColor(new Color(0, 0, 0, alpha));
				g.fillRect(0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT);
			}

			if (Player.isDead()) {
				Game.closeMinigame();
				// fade out
				if (alpha < 255) {
					alpha += 10;
				}
				if (alpha >= 255) {
					alpha = 255;
					AudioUtils.stopAudio("game");
					AudioUtils.playAudio("death");
					AppData.gameState = "dead";
				}
			} else if (pauseMenuEnabled) {
				g.setColor(new Color(0, 0, 0, 150));
				g.fillRect(0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT);
				g.drawImage(pauseMenu, 0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT, null);
				setCursor(true);
				drawPauseSelectionBoxes(g);
			}

			if (controlsMenuEnabled) {
				g.drawImage(controlsMenu, 0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT, null);
			}

			// fade in
			if (alpha > 0) {
				g.setColor(new Color(0, 0, 0, alpha));
				alpha -= 4;
				g.fillRect(0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT);
			}
			break;
		case "dead":
			g.drawImage(deathScreen, 0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT, null);
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT);
			if (alpha > 0) {
				alpha -= 10;
			}
			if (alpha < 0) {
				alpha = 0;
			}
			// display lightning
			g.setColor(new Color(255, 255, 255, 15));
			g.fillRect(AppData.SCREEN_WIDTH / 2 - 175, 600, 353, lightning.getHeight() + 10);
			for (int i = 0; i < AppData.NUM_MINIGAMES; i++) {
				if (AppData.nextMinigame > i) {
					g.drawImage(lightning, AppData.SCREEN_WIDTH / 2 - 170 + i * 30, 605, lightning.getWidth(), lightning.getHeight(), null);
				} else {
					g.drawImage(emptyLightning, AppData.SCREEN_WIDTH / 2 - 170 + i * 30, 605, emptyLightning.getWidth(),
							emptyLightning.getHeight(), null);
				}
			}
			break;
		case "credits loading":
			g.setColor(new Color(0, 0, 0));
			g.fillRect(0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT);
			if (loadingTimestamp.getTimeElapsedMillis() > 5000) {
				AppData.gameState = "credits";
				AudioUtils.playAudio("credits");
				loadingTimestamp.setTimestamp();
			}
			break;
		case "credits":
			g.setColor(new Color(0, 0, 0));
			g.fillRect(0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT);
			if (loadingTimestamp.getTimeElapsedMillis() > 43438) {
				g.drawImage(creditsImages[5], 0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT, null);
			} else if (loadingTimestamp.getTimeElapsedMillis() > 39568) {
				g.drawImage(creditsImages[4], 0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT, null);
			} else if (loadingTimestamp.getTimeElapsedMillis() > 31826) {
				g.drawImage(creditsImages[3], 0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT, null);
			} else if (loadingTimestamp.getTimeElapsedMillis() > 24084) {
				g.drawImage(creditsImages[2], 0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT, null);
			} else if (loadingTimestamp.getTimeElapsedMillis() > 16342) {
				g.drawImage(creditsImages[1], 0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT, null);
			} else if (loadingTimestamp.getTimeElapsedMillis() > 8600) {
				g.drawImage(creditsImages[0], 0, 0, AppData.SCREEN_WIDTH, AppData.SCREEN_HEIGHT, null);
			}
			break;
		}
	}

	private void drawSelectionBoxes(Graphics g) {
		for (int i = 0; i < 3; i++) {
			int topYBoundary = 400 + 80 * i;
			if (mouseX < 540 && mouseY > topYBoundary && mouseY < topYBoundary + 75 && !controlsMenuEnabled) {
				if (selectionAlphas[i] < 40) {
					selectionAlphas[i] += 5;
				}
				if (selectionXs[i] < 540) {
					selectionXs[i] += 60;
				}
				buttonHighlighted = i;
			} else {
				if (selectionAlphas[i] > 0) {
					selectionAlphas[i] -= 5;
				} else {
					selectionXs[i] = 0;
				}
				// reset selected button
				if (buttonHighlighted == i) {
					buttonHighlighted = -1;
					buttonSelected = -1;
				}
			}
			g.setColor(new Color(200, 200, 200, selectionAlphas[i]));
			g.fillRect(0, topYBoundary, selectionXs[i], 60);
		}
	}

	private void drawPauseSelectionBoxes(Graphics g) {
		for (int i = 0; i < 3; i++) {
			int topYBoundary = 332 + 100 * i;
			if (mouseX > 332 && mouseX < 692 && mouseY > topYBoundary && mouseY < topYBoundary + 80
					&& !controlsMenuEnabled) {
				if (selectionAlphas[i] < 40) {
					selectionAlphas[i] += 5;
				}
				buttonHighlighted = i;
			} else {
				// if user was holding button
				if (selectionAlphas[i] == 60) {
					selectionAlphas[i] = 40;
				}
				if (selectionAlphas[i] > 0) {
					selectionAlphas[i] -= 5;
				}
				// reset selected button
				if (buttonHighlighted == i) {
					buttonHighlighted = -1;
					buttonSelected = -1;
				}
			}
			g.setColor(new Color(200, 200, 200, selectionAlphas[i]));
			g.fillRect(332, topYBoundary, 360, 80);
		}
	}

	private void drawStaminaBox(Graphics g) {
		// stamina background
		g.setColor(new Color(5, 5, 5, 150));
		g.fillRect(AppData.SCREEN_WIDTH / 2 - 150, AppData.SCREEN_HEIGHT - 30, 300, 20);
		// stamina bar
		if (Player.isStaminaBlocked()) {
			g.setColor(new Color(100, 20, 20));
		} else {
			g.setColor(new Color(255, 250, 80));
		}
		g.fillRect(AppData.SCREEN_WIDTH / 2 - 150, AppData.SCREEN_HEIGHT - 30, (int) (Player.getStamina() / 100 * 300),
				20);

		// stamina bar outline
		g.setColor(new Color(100, 100, 100));
		g.drawRect(AppData.SCREEN_WIDTH / 2 - 150, AppData.SCREEN_HEIGHT - 30, 300, 20);
	}

	private class Adapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (!AppData.inMinigame && !pauseMenuEnabled) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_W:
					Player.forward = true;
					break;
				case KeyEvent.VK_A:
					Player.left = true;
					break;
				case KeyEvent.VK_S:
					Player.backward = true;
					break;
				case KeyEvent.VK_D:
					Player.right = true;
					break;
				case KeyEvent.VK_LEFT:
					Player.turningLeft = true;
					break;
				case KeyEvent.VK_RIGHT:
					Player.turningRight = true;
					break;
				case KeyEvent.VK_SHIFT:
					Player.sprinting = true;
					break;
				case KeyEvent.VK_E:
					Point minigameLocation = Player.getAdjacentMinigame();
					if (minigameLocation != null) {
						AppData.inMinigame = true;
						Player.stopControls();
						if (AppData.nextMinigame < AppData.NUM_MINIGAMES) {
							Game.runMinigame(minigames[AppData.nextMinigame], (int) minigameLocation.getX(),
									(int) minigameLocation.getY());
						}
					}
					break;
				}
			}
			// since other keybinds are disabled when the pause menu is open
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				if (AppData.gameState.equals("game") && !controlsMenuEnabled) {
					pauseMenuEnabled = !pauseMenuEnabled;
				}
				controlsMenuEnabled = false;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				Player.forward = false;
				break;
			case KeyEvent.VK_A:
				Player.left = false;
				break;
			case KeyEvent.VK_S:
				Player.backward = false;
				break;
			case KeyEvent.VK_D:
				Player.right = false;
				break;
			case KeyEvent.VK_LEFT:
				Player.turningLeft = false;
				break;
			case KeyEvent.VK_RIGHT:
				Player.turningRight = false;
				break;
			case KeyEvent.VK_SHIFT:
				Player.sprinting = false;
				break;
			case KeyEvent.VK_I:
				caedon.generatePath();
				break;
			}
		}
	}

	private class Listener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (buttonHighlighted != -1 && (AppData.gameState.equals("menu") || pauseMenuEnabled)) {
				selectionAlphas[buttonHighlighted] = 60;
				buttonSelected = buttonHighlighted;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (pauseMenuEnabled) {
				switch (buttonSelected) {
				case 0:
					pauseMenuEnabled = false;
					break;
				case 1:
					controlsMenuEnabled = true;
					break;
				case 2:
					System.exit(0);
					break;
				}
			} else if (AppData.gameState.equals("menu")) {
				switch (buttonSelected) {
				case 0:
					AudioUtils.stopAudio("menu");
					AudioUtils.playAudio("menu_select");
					alpha = 0;
					AppData.gameState = "loading";
					buttonHighlighted = -1;
					buttonSelected = -1;
					loadingTimestamp.setTimestamp();
					setCursor(false);
					break;
				case 1:
					controlsMenuEnabled = true;
					break;
				case 2:
					System.exit(0);
					break;
				}
			}
			if (mouseX > 842 && mouseX < 902 && mouseY > 81 && mouseY < 141 && controlsMenuEnabled) {
				controlsMenuEnabled = false;
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

	}

	private void setCursor(boolean enabled) {
		if (enabled) {
			setCursor(Cursor.getDefaultCursor());

		} else {
			// make a transparent cursor image
			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

			// create blank cursor using image
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0),
					"blank cursor");
			setCursor(blankCursor);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (AppData.gameState.equals("game") && !pauseMenuEnabled) {
			Player.updateMovement();
			caedon.updateEntity();
			// change of base formula :)
			double caedonSpeed = Math.log(speedTimestamp.getTimeElapsedMillis() / 1000 + 1) / Math.log(7);
			if (AppData.inMinigame) {
				caedon.setSpeed(caedonSpeed / 10);
				setCursor(true);
			} else {
				caedon.setSpeed(caedonSpeed);
				setCursor(false);
			}
			buttonHighlighted = -1;
			buttonSelected = -1;
			for (int i = 0; i < 3; i++) {
				selectionAlphas[i] = 0;
			}
		}
		repaint();
		// update mouse coordinates
		if (AppData.gameState.equals("menu") || AppData.gameState.equals("game")) {
			updateMouseLocation();
		}
	}

	private void updateMouseLocation() {
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mouseLocation, this);
		mouseX = mouseLocation.getX();
		mouseY = mouseLocation.getY();
	}
}
