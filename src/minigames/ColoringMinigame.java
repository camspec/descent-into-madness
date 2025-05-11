package minigames;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.AppData;

public class ColoringMinigame extends Minigame {
	private static final long serialVersionUID = 1L;
	private Point[] shapePositions = { new Point(50, 50), new Point(190, 120), new Point(50, 190), new Point(190, 260),
			new Point(50, 330) };
	private final int shapeSize = 60;
	private int numShapes = shapePositions.length;

	private int[] shapes = new int[numShapes];
	private Color[] possibleColors = { new Color(255, 0, 0), new Color(255, 204, 255), new Color(0, 0, 255),
			new Color(204, 255, 204), new Color(255, 255, 0), new Color(0, 255, 0), new Color(255, 204, 204) };
	private int numPossibleColors = possibleColors.length;
	private Color[] exampleColors = new Color[numShapes];
	private Color[] chosenColors = new Color[numShapes];

	public ColoringMinigame(int x, int y) {
		super("Color Matching", new Color(255, 255, 255), x, y);

		randomizeShapes();
		randomizeColors();

		DrawingPanel panel = new DrawingPanel();
		add(panel);
		repaint();

		JLabel infoText = new JLabel();
		infoText.setForeground(new Color(0, 0, 0));
		panel.add(infoText);
		infoText.setText("Click the shapes on the right to match them with the shapes on the left!");
		infoText.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
	}

	private class DrawingPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public DrawingPanel() {
			setBounds(0, 0, MINIGAME_WIDTH, MINIGAME_HEIGHT);
			setVisible(true);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					int mouseX = e.getX();
					int mouseY = e.getY();
					for (int i = 0; i < numShapes; i++) {
						// iterate through each shape's position
						int shapeX = shapePositions[i].x + MINIGAME_WIDTH / 2;
						int shapeY = shapePositions[i].y;
						if (mouseX >= shapeX && mouseX <= shapeX + shapeSize && mouseY >= shapeY
								&& mouseY <= shapeY + shapeSize) {
							Color selectedColor;
							if (chosenColors[i] != null) {
								// the default color will be the shape's color if it already has one
								selectedColor = selectColor(chosenColors[i]);
							} else {
								selectedColor = selectColor(new Color(0, 0, 0));
							}

							// only change the color if the selected one isn't null
							// it can be null if the user exits out of the menu
							if (selectedColor != null) {
								chosenColors[i] = selectedColor;
							}
						}
					}
					repaint();
					if (checkColors()) {
						completeMinigame();
					}
				}
			});
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(new Color(155, 155, 155));
			g.drawLine(MINIGAME_WIDTH / 2, 0, MINIGAME_WIDTH / 2, MINIGAME_HEIGHT);
			// example shapes
			for (int i = 0; i < numShapes; i++) {
				int x = shapePositions[i].x;
				int y = shapePositions[i].y;
				g.setColor(exampleColors[i]);
				drawShape(g, x, y, shapes[i]);
			}
			// shapes to color in
			for (int i = 0; i < numShapes; i++) {
				// move the second set of shapes to the right of the separator
				int x = shapePositions[i].x + MINIGAME_WIDTH / 2;
				int y = shapePositions[i].y;
				g.setColor(chosenColors[i]);
				if (chosenColors[i] == null) {
					g.setColor(new Color(0, 0, 0));
				}
				drawShape(g, x, y, shapes[i]);
			}
		}
	}

	private void drawShape(Graphics g, int x, int y, int type) {
		switch (shapes[type]) {
		case 0:
			// square
			g.fillRect(x, y, shapeSize, shapeSize);
			break;
		case 1:
			// circle
			g.fillOval(x, y, shapeSize, shapeSize);
			break;
		case 2:
			// triangle
			g.fillPolygon(new int[] { x, x + shapeSize / 2, x + shapeSize },
					new int[] { y + shapeSize, y, y + shapeSize }, 3);
			break;
		case 3:
			// rectangle
			g.fillRect(x, y + shapeSize / 6, shapeSize, shapeSize - shapeSize / 3);
			break;
		case 4:
			// oval
			g.fillOval(x + shapeSize / 6, y, shapeSize - shapeSize / 3, shapeSize);
			break;
		default:
			// square
			g.fillRect(x, y, shapeSize, shapeSize);
			break;
		}
	}

	private void randomizeShapes() {
		// randomize first shape, then go in order
		int currentShape = AppData.RNG.nextInt(numShapes);
		for (int i = 0; i < numShapes; i++) {
			shapes[i] = currentShape;
			currentShape++;
			// go back to beginning of array if the end is met
			if (currentShape == numShapes) {
				currentShape = 0;
			}
		}
	}

	private void randomizeColors() {
		// randomize first color, then go in order
		int currentColor = AppData.RNG.nextInt(numPossibleColors);
		for (int i = 0; i < numShapes; i++) {
			exampleColors[i] = possibleColors[currentColor];
			currentColor++;
			// go back to beginning of array if the end is met
			if (currentColor == numPossibleColors) {
				currentColor = 0;
			}
		}
	}

	private Color selectColor(Color defaultColor) {
		return JColorChooser.showDialog(this, "Select a color", defaultColor);
	}

	private boolean checkColors() {
		int correctColors = 0;
		for (int i = 0; i < numShapes; i++) {
			if (chosenColors[i] != null) {
				double colorDistance = getColorDistance(exampleColors[i], chosenColors[i]);
				// if the two colors are a little off it's okay
				if (colorDistance <= 100) {
					correctColors++;
				}
			}
		}
		return correctColors == numShapes;
	}

	private double getColorDistance(Color c1, Color c2) {
		// distance between two 3d points in RGB color space
		return Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2) + Math.pow(c1.getGreen() - c2.getGreen(), 2)
				+ Math.pow(c1.getBlue() - c2.getBlue(), 2));
	}
}
