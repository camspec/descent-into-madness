package minigames;

import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import data.AppData;
import game.GridMap;

public class Minigame extends JFrame {
	private static final long serialVersionUID = 1L;
	final int MINIGAME_WIDTH = 640;
	final int MINIGAME_HEIGHT = 480;
	private int x, y;

	public Minigame(String title, Color backgroundColor, int x, int y) {
		initializeGUI(title, backgroundColor);
		this.x = x;
		this.y = y;
	}

	public void initializeGUI(String title, Color backgroundColor) {
		setResizable(false);
		setBounds(0, 0, MINIGAME_WIDTH, MINIGAME_HEIGHT);
		setLocationRelativeTo(null);
		setTitle(title);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(backgroundColor);
		setLayout(null);
		setVisible(true);
		setAlwaysOnTop(true);
		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
				AppData.inMinigame = false;
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

	public void completeMinigame() {
		GridMap.gameMap[x][y] = 7;
		AppData.nextMinigame++;
		AppData.inMinigame = false;
		this.dispose();
	}

	public void showMessage(String title, String message) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	public void showErrorMessage(String title, String message) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
