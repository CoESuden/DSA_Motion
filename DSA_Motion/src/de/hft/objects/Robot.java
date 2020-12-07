package de.hft.objects;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import de.hft.algorithmn.ImageToArrayCalculator;
import de.hft.filereader.BMPFileReader;

public class Robot {

	private Image _image = null;
	private int[][] _2Dpixels;

	public Robot(String path) {
		_image = new Image(Display.getCurrent(), path);
		_2Dpixels = ImageToArrayCalculator.getLayout(BMPFileReader.readFile(path));
	}

	public Image getImage() {
		return _image;
	}

	public int[][] get2DPixels() {
		return _2Dpixels;
	}
	
}
