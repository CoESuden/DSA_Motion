package de.hft.filereader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import de.hft.objects.Robot;
import de.hft.objects.Room;

public class BMPFileReader {

	private static final String ROOM_URL =  File.separator+ "assets" +File.separator + "room";
	
	private static final String ROBOT_URL =  File.separator+ "assets" +File.separator + "robot";

	private BMPFileReader() {
		// empty
	}
	
	public static List<Robot> getAllRobots() {
		try (Stream<Path> allPathsToRoom = Files.walk(Paths.get(System.getProperty("user.dir") + ROBOT_URL))) {
			return allPathsToRoom //
					.filter(path -> path.toFile().exists()) //
					.filter(BMPFileReader::endsWithbmp) //
					.map(path -> new Robot(path.toString())) //
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Room> getAllRooms() {
		try (Stream<Path> allPathsToRoom = Files.walk(Paths.get(System.getProperty("user.dir") +  ROOM_URL))) {
			return allPathsToRoom //
					.filter(path -> path.toFile().exists()) //
					.filter(BMPFileReader::endsWithbmp) //
					.map(path -> new Room(path.toString())) //
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedImage readFile(String path) {
		try {
			return ImageIO.read(new FileInputStream(new File(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean endsWithbmp(Path path) {
		return path.toString().endsWith("bmp");
	}
	
}
