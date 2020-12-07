package de.hft.objects;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import de.hft.algorithmn.ImageToArrayCalculator;
import de.hft.filereader.BMPFileReader;

public class Room {

	private Image _image;
	private int[][] _2Dpixels;

	public Room(String path) {
		_image = new Image(Display.getCurrent(), path);
		_2Dpixels = ImageToArrayCalculator.getLayout(BMPFileReader.readFile(path));
	}

	public int[][] get2DPixels() {
		return _2Dpixels;
	}

	public Image getImage() {
		return _image;
	}


}
