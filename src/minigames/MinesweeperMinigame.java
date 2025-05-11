package minigames;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import data.AppData;
import utils.ImageUtils;

public class MinesweeperMinigame extends Minigame {
	private static final long serialVersionUID = 1L;
	private final int boardWidth = 5;
	private final int boardHeight = 5;
	private final int mines = 3;
	private final int cellSize = 70;
	private final Cell[][] minefield = new Cell[boardHeight][boardWidth];
	private final JButton[][] minefieldButtons = new JButton[boardHeight][boardWidth];
	private final int margin = 3;
	private final int fieldX = 35;
	private final int fieldY = 35;
	private final Color unclickedColor = new Color(200, 200, 200);
	private final Color clickedColor = new Color(170, 170, 170);
	private final Color colorOne = new Color(0, 0, 255);
	private final Color colorTwo = new Color(0, 155, 0);
	private final Color colorThree = new Color(255, 0, 0);
	private boolean firstClick = true;
	ImageIcon flagIcon = ImageUtils.getImageIcon("images/minigames/minesweeper/flag.png");
	ImageIcon mineIcon = ImageUtils.getImageIcon("images/minigames/minesweeper/mine.png");

	public MinesweeperMinigame(int x, int y) {
		super("Minesweeper", new Color(255, 255, 255), x, y);
		generateMinefield();
		placeMines(mines);

		JLabel title = new JLabel();
		add(title);
		title.setBounds(455, 10, 640, 40);
		title.setText("Minesweeper");
		title.setFont(new Font("SansSerif", Font.BOLD, 20));
		title.setForeground(new Color(0, 0, 0));

		JTextArea instructions = new JTextArea();
		instructions.setBounds(415, 80, 200, 100);
		add(instructions);
		instructions
				.setText("Left click to open a cell.\nRight click to place a flag.\nSome games require guessing.\n\n"
						+ mines + " mines");
		instructions.setFont(new Font("SansSerif", Font.PLAIN, 14));
		instructions.setEditable(false);
	}

	private class Cell {
		private int x, y;
		private boolean isMine = false;
		private boolean clicked = false;
		private boolean flagged = false;

		public Cell(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public boolean isMine() {
			return isMine;
		}

		public void setMine(boolean isMine) {
			this.isMine = isMine;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public void setClicked(boolean clicked) {
			this.clicked = clicked;
		}

		public boolean isClicked() {
			return clicked;
		}

		public void setFlagged(boolean flagged) {
			this.flagged = flagged;
		}

		public boolean isFlagged() {
			return flagged;
		}
	}

	private void generateMinefield() {
		for (int y = 0; y < boardHeight; y++) {
			for (int x = 0; x < boardWidth; x++) {
				Cell cell = new Cell(x, y);
				JButton cellButton = new JButton();
				cellButton.setBounds(fieldX + x * (cellSize + margin), fieldY + y * (cellSize + margin), cellSize,
						cellSize);
				add(cellButton);
				cellButton.setBackground(unclickedColor);
				cellButton.setFont(new Font("SansSerif", Font.BOLD, 30));
				cellButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						clickCell(cell.getX(), cell.getY());
					}
				});
				cellButton.addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
					}

					@Override
					public void mousePressed(MouseEvent e) {
						if (e.getButton() == 3) {
							if (cell.isFlagged()) {
								cell.setFlagged(false);
								cellButton.setIcon(null);
							} else if (!cell.isClicked()) {
								cell.setFlagged(true);
								cellButton.setIcon(flagIcon);
								repaint();
							}
						}
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
				minefield[y][x] = cell;
				minefieldButtons[y][x] = cellButton;
			}
		}
	}

	private void placeMines(int mines) {
		for (int i = 0; i < mines; i++) {
			// keep attempting to set mines until a spot is free
			boolean placed = false;
			while (!placed) {
				int randomX = AppData.RNG.nextInt(boardWidth);
				int randomY = AppData.RNG.nextInt(boardHeight);
				if (!minefield[randomY][randomX].isMine()) {
					minefield[randomY][randomX].setMine(true);
					placed = true;
				}
			}
		}
	}

	private void clickCell(int x, int y) {
		Cell cell = minefield[y][x];
		JButton cellButton = minefieldButtons[y][x];
		cell.setClicked(true);
		cellButton.setBackground(clickedColor);
		if (cell.isFlagged()) {
			cell.setFlagged(false);
			cellButton.setIcon(null);
		}
		if (firstClick) {
			if (cell.isMine()) {
				cell.setMine(false);
				resetFirstMine(x, y);
			}
			firstClick = false;
		}
		if (cell.isMine()) {
			cellButton.setIcon(mineIcon);
			revealBoard();
			showErrorMessage("You died!", "You clicked on a mine, haha, haha,");
			resetMinefield();
		} else {
			int numMines = countAdjacentMines(x, y);
			if (numMines == 0) {
				int[] dx = { -1, 0, 1, 1, 1, 0, -1, -1 };
				int[] dy = { -1, -1, -1, 0, 1, 1, 1, 0 };
				for (int i = 0; i < 8; i++) {
					int newX = x + dx[i];
					int newY = y + dy[i];
					if (isValid(newX, newY)) {
						// skip over mines and already clicked cells
						if (!minefield[newY][newX].isMine() && !minefield[newY][newX].isClicked()) {
							clickCell(newX, newY);
						}
					}
				}
			} else {
				switch (numMines) {
				case 1:
					cellButton.setForeground(colorOne);
					break;
				case 2:
					cellButton.setForeground(colorTwo);
					break;
				case 3:
					cellButton.setForeground(colorThree);
					break;
				}
				cellButton.setText(String.valueOf(numMines));
			}
		}
		if (isBoardCompleted() && AppData.inMinigame) {
			revealBoard();
			showMessage("You won!", "Congratulations!");
			completeMinigame();
		}
	}

	private int countAdjacentMines(int x, int y) {
		int numMines = 0;
		// start from top left and go clockwise
		int[] dx = { -1, 0, 1, 1, 1, 0, -1, -1 };
		int[] dy = { -1, -1, -1, 0, 1, 1, 1, 0 };
		for (int i = 0; i < 8; i++) {
			int newX = x + dx[i];
			int newY = y + dy[i];
			if (isValid(newX, newY)) {
				if (minefield[newY][newX].isMine()) {
					numMines++;
				}
			}
		}
		return numMines;
	}

	private boolean isValid(int x, int y) {
		return x >= 0 && x < boardWidth && y >= 0 && y < boardHeight;
	}

	private boolean isBoardCompleted() {
		// board is complete when all non-mine cells are clicked
		int unsolvedCells = 0;
		for (int y = 0; y < boardHeight; y++) {
			for (int x = 0; x < boardWidth; x++) {
				if (!minefield[y][x].isClicked() && !minefield[y][x].isMine()) {
					unsolvedCells++;
				}
			}
		}
		return unsolvedCells == 0;
	}

	private void resetMinefield() {
		for (int y = 0; y < boardHeight; y++) {
			for (int x = 0; x < boardWidth; x++) {
				minefield[y][x].setClicked(false);
				minefield[y][x].setMine(false);
				minefieldButtons[y][x].setIcon(null);
				minefieldButtons[y][x].setText("");
				minefieldButtons[y][x].setBackground(unclickedColor);
				firstClick = true;
			}
		}
		placeMines(mines);
	}

	private void resetFirstMine(int firstMineX, int firstMineY) {
		for (int y = 0; y < boardHeight; y++) {
			for (int x = 0; x < boardWidth; x++) {
				if (!minefield[y][x].isMine() && (firstMineX != x || firstMineY != y)) {
					minefield[y][x].setMine(true);
					return;
				}
			}
		}
	}

	private void revealBoard() {
		for (int y = 0; y < boardHeight; y++) {
			for (int x = 0; x < boardWidth; x++) {
				if (minefield[y][x].isMine()) {
					minefieldButtons[y][x].setBackground(clickedColor);
					minefieldButtons[y][x].setIcon(mineIcon);
				}
			}
		}
	}
}
