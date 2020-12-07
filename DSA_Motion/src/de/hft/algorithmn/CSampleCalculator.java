package de.hft.algorithmn;

import java.util.ArrayList;
import java.util.List;

import de.hft.objects.Point;
import de.hft.objects.Robot;

public class CSampleCalculator {

	
	public static List<Point> getRandomPointSample(int amountOfSmpling,int[][] roomArray, Robot robot){
		
		List<Point> samplingPoints = new ArrayList<>();
		
		for(int i = 0; i<amountOfSmpling;i++) {
			Point samplePoint = new Point((int) (Math.random()*roomArray.length),(int)(Math.random() *roomArray[0].length));
			int objectPosition[] = new int[2];
			objectPosition[0] = samplePoint.getX();
			objectPosition[1] = samplePoint.getY();
			if(!CollisionDetection.isInCollision(roomArray, objectPosition, robot)) {
				samplingPoints.add(samplePoint);
			}
		}
		
	
		return samplingPoints;
	}
}
