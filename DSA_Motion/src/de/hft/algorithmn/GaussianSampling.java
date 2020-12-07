package de.hft.algorithmn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;

import de.hft.objects.Point;
import de.hft.objects.Robot;

public class GaussianSampling {

	
	
	
public static List<Point> getRandomPointSample(int amountOfSmpling,int[][] roomArray, Robot robot){
		
		List<Point> samplingPoints = new ArrayList<>();
		NormalDistribution normalDistribution = new NormalDistribution(0,100);
		for(int i = 0; i<amountOfSmpling;i++) {
			int objectPosition1[] = new int[2];
			int objectPosition2[] = new int[2];

			Point point1 = new Point((int) (Math.random()*roomArray.length),(int)(Math.random() *roomArray[0].length));
			objectPosition1[0] = point1.getX();
			objectPosition1[1] = point1.getY();
			int distance = (int) Math.abs(normalDistribution.sample());
			
			List<Point> straightLine = StraightLine.getStraightWithBresenhamAlgo(point1.getX(), point1.getY(), (int) (Math.random()*roomArray.length), (int)(Math.random() *roomArray[0].length)
					, distance);
			
			
			Point point2 = straightLine.get((int) (Math.random() * (straightLine.size() - 1)));
			objectPosition2[0] = point2.getX();
			objectPosition2[1] = point2.getY();
			
			if(!CollisionDetection.isInCollision(roomArray, objectPosition1, robot) && CollisionDetection.isInCollision(roomArray, objectPosition2, robot)) {
				samplingPoints.add(point1);
			} else if(!CollisionDetection.isInCollision(roomArray, objectPosition2, robot) && CollisionDetection.isInCollision(roomArray, objectPosition1, robot)) {
				samplingPoints.add(point2);
			}
		}
		
	
		return samplingPoints;
	}
}
