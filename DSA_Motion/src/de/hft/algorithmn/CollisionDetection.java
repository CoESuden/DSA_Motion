package de.hft.algorithmn;

import java.util.List;

import de.hft.objects.Point;
import de.hft.objects.Robot;

public class CollisionDetection {

	private CollisionDetection() {
		// empty
	}

	public static boolean isInCollision(int[][] roomArray, int[] objectPosition, Robot robot) {
		int startArrayXY[] = new int[2];
		int endArrayXY[] = new int[2];
		int startRobotX = 0;
		int startRobotY = 0;
		if (objectPosition[0] - (robot.getImage().getBounds().width / 2) < 0
				|| objectPosition[1] - (robot.getImage().getBounds().height / 2) < 0
				|| objectPosition[0] + (robot.getImage().getBounds().width / 2) >= roomArray.length
				|| objectPosition[1] + (robot.getImage().getBounds().height / 2) >= roomArray[0].length) {
			return true;
		}
		startArrayXY[0] = objectPosition[0] - (robot.getImage().getBounds().width / 2);
		startArrayXY[1] = objectPosition[1] - (robot.getImage().getBounds().height / 2);
		endArrayXY[0] = objectPosition[0] + (robot.getImage().getBounds().width / 2);
		endArrayXY[1] = objectPosition[1] + (robot.getImage().getBounds().height / 2);

		try {

			for (int x = startArrayXY[0]; x < endArrayXY[0]; x++) {
				for (int y = startArrayXY[1]; y < endArrayXY[1]; y++) {

					if (robot.get2DPixels()[startRobotX][startRobotY] == 1 && roomArray[x][y] == 1) {
						return true;
					}
					startRobotY++;
				}
				startRobotY = 0;
				startRobotX++;
			}

			return false;
		} catch (Throwable t) {
			return true;
		}
	}

	public static boolean isStraigthLineInCollision(int[][] roomArray, List<Point> straightLine) {
		return straightLine.stream().anyMatch(point -> roomArray[point.getX()][point.getY()] == 1);
	}

	public static Point getNonCollisionPoint(int[][] roomArray, Robot robot, List<Point> straightLine) {
		for(int i = 0; i< straightLine.size(); i++) {
			int position[] = new int[2];
			position[0] = straightLine.get(i).getX();
			position[1] = straightLine.get(i).getY();
			if(isInCollision(roomArray,position,robot) && i != 0) {
				return straightLine.get(i-1);
			}
		}
		return straightLine.get(straightLine.size()-1);
	}

}
