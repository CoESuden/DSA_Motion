package de.hft.algorithmn;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import de.hft.objects.Edge;
import de.hft.objects.Point;
import de.hft.objects.Robot;

public class SimpleProbabilisticRoadmapMethod {

	public static List<Edge> _edges = new ArrayList<>();

	public static List<Point> getSolution(int[][] roomArray, Robot robot, Point cStartpoints, Point cGoalpoints,
			int searchRadius, int amountOfSamples) {
		List<Point> vertexes = new ArrayList<>();
		SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(
				DefaultWeightedEdge.class);
		vertexes.add(cStartpoints);
		vertexes.add(cGoalpoints);

		CSampleCalculator.getRandomPointSample(amountOfSamples, roomArray, robot).forEach(vertexes::add);
		vertexes.forEach(graph::addVertex);

		vertexes.forEach(point -> vertexes.stream() //
				.filter(point2 -> isCloseVertex(point, point2, searchRadius)) //
				.filter(point2 -> isValidEdge(roomArray, point, point2)) //
				.forEach(point2 -> {
					if (!arePointsEqual(point, point2)) {
						_edges.add(new Edge(point, point2));
						DefaultWeightedEdge edge = graph.addEdge(point, point2);
						graph.setEdgeWeight(edge, (double) Math.hypot((double) point.getX() - (double) point2.getX(),
								(double) point.getY() - (double) point2.getY()));
					}
				}));

		if (DijkstraShortestPath.findPathBetween(graph, cStartpoints, cGoalpoints) == null) {
			_edges = null;
			return null;
		}
		return DijkstraShortestPath.findPathBetween(graph, cStartpoints, cGoalpoints).getVertexList();
	}

	public static List<Point> getSolutionWithGaussian(int[][] roomArray, Robot robot, Point cStartpoints,
			Point cGoalpoints, int searchRadius, int amountOfSamples) {
		List<Point> vertexes = new ArrayList<>();
		SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(
				DefaultWeightedEdge.class);
		vertexes.add(cStartpoints);
		vertexes.add(cGoalpoints);

		GaussianSampling.getRandomPointSample(amountOfSamples, roomArray, robot).forEach(vertexes::add);
		vertexes.forEach(graph::addVertex);

		vertexes.forEach(point -> vertexes.stream() //
				.filter(point2 -> isCloseVertex(point, point2, searchRadius)) //
				.filter(point2 -> isValidEdge(roomArray, point, point2)) //
				.forEach(point2 -> {
					if (!arePointsEqual(point, point2)) {
						_edges.add(new Edge(point, point2));
						DefaultWeightedEdge edge = graph.addEdge(point, point2);
						graph.setEdgeWeight(edge, (double) Math.hypot((double) point.getX() - (double) point2.getX(),
								(double) point.getY() - (double) point2.getY()));
					}
				}));

		if (DijkstraShortestPath.findPathBetween(graph, cStartpoints, cGoalpoints) == null) {
			_edges = null;
			return null;
		}
		return DijkstraShortestPath.findPathBetween(graph, cStartpoints, cGoalpoints).getVertexList();
	}

	private static boolean isValidEdge(int[][] roomArray, Point point, Point point2) {
		List<Point> straightLine = StraightLine.getStraightWithBresenhamAlgo(point.getX(), point.getY(), point2.getX(),
				point2.getY());
		return !CollisionDetection.isStraigthLineInCollision(roomArray, straightLine);
	}

	private static boolean isCloseVertex(Point startPoint, Point endPoint, int searchRadius) {
		return Math.hypot((double) startPoint.getX() - (double) endPoint.getX(),
				(double) startPoint.getY() - (double) endPoint.getY()) <= searchRadius;
	}

	private static boolean arePointsEqual(Point point, Point point2) {
		return point.getX() == point2.getX() && point.getY() == point2.getY();
	}
}
