package frame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sprite {
	private BufferedImage image;
	private double x, y;
	private double width, height;

	public Sprite(BufferedImage image, double x, double y, double width, double height) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void drawSprite(Graphics g) {
		g.drawImage(image, (int) Math.round(x), (int) Math.round(y), (int) Math.round(width), (int) Math.round(height),
				null);
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
