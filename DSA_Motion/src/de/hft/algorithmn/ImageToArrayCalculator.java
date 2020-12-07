package de.hft.algorithmn;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageToArrayCalculator {
	
	private ImageToArrayCalculator() {
		// empty
	}

	public static int[][] getLayout(BufferedImage image) {
		int[][] roomArray = new int[image.getWidth()][image.getHeight()];
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				createLayout(roomArray, image.getRGB(x, y), x, y);
			}
		}
		return roomArray;
	}

	private static void createLayout(int[][] roomArray, int color, int x, int y) {
		if (color != Color.WHITE.getRGB()) {
			roomArray[x][y] = 1;
		} else {
			roomArray[x][y] = 0;
		}
	}
	
}
