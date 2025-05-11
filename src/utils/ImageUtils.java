package utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtils {
	public static BufferedImage getImage(String path) {
		URL url = getURL(path);
		try {
			return ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ImageIcon getImageIcon(String path) {
		URL url = getURL(path);
		return new ImageIcon(url);
	}

	private static URL getURL(String path) {
		return ImageUtils.class.getClassLoader().getResource(path);
	}
}
