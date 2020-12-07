package de.hft.algorithmn;

import java.util.ArrayList;
import java.util.List;

import de.hft.objects.Point;
import de.hft.objects.Robot;
import de.hft.objects.TreeNode;

public class RapidlyExploringRandomTree {

private static TreeNode _tree;

	public static List<Point> getSolution(int[][] roomArray, Robot robot, Point cStartPoint, Point cEndPoint, int range,
			int timeInMs) {

		TreeNode root = new TreeNode(cStartPoint);

		long startingTime = System.currentTimeMillis();
		long endTime = startingTime + timeInMs;

		while (System.currentTimeMillis() <= endTime) {
			List<Point> list = CSampleCalculator.getRandomPointSample(1, roomArray, robot);
			if(list.isEmpty()) {
				continue;
			}
			Point cRandomPoint = list.get(0);

			TreeNode cNear = getNearestPointToTree(root, cRandomPoint);
			
			Point cNew = CollisionDetection.getNonCollisionPoint(roomArray, robot, StraightLine.getStraightWithBresenhamAlgo(cNear.getData().getX(), cNear.getData().getY(),
					cRandomPoint.getX(), cRandomPoint.getY(), range));
			
			if(cNew != null) {
				TreeNode newNode = new TreeNode(cNew);
				cNear.addChild(newNode);
				double distance = Math.hypot((double) cNew.getX() - (double) cEndPoint.getX(),
						(double) cNew.getY() - (double) cEndPoint.getY());
				if(distance <= 50 && !CollisionDetection.isStraigthLineInCollision(roomArray, StraightLine.getStraightWithBresenhamAlgo(cNew.getX(), cNew.getY(), cEndPoint.getX(), cEndPoint.getY()))) {
					_tree = root;
					return getPath(newNode,cEndPoint);
				}
			}

		}

		
		return null;
	}

	private static TreeNode getNearestPointToTree(TreeNode root, Point cRandomPoint) {
		double currentShortestDistance = Math.hypot((double) root.getData().getX() - (double) cRandomPoint.getX(),
				(double) root.getData().getY() - (double) cRandomPoint.getY());

		ArrayList<TreeNode> allTreeNodes = new ArrayList<>();

		addAllTreeNodes(root, allTreeNodes);
		
		TreeNode nearestTreeNode = null;

		for (int i = 0; i < allTreeNodes.size(); i++) {
			double newShortDistance = Math.hypot(
					(double) allTreeNodes.get(i).getData().getX() - (double) cRandomPoint.getX(),
					(double) allTreeNodes.get(i).getData().getY() - (double) cRandomPoint.getY());
			if (newShortDistance <= currentShortestDistance) {
				nearestTreeNode = allTreeNodes.get(i);
				currentShortestDistance = newShortDistance;
			}
		}
		return nearestTreeNode;
	}

	public static void addAllTreeNodes(TreeNode root, List<TreeNode> allTreeNodes) {
		allTreeNodes.add(root);
		if(root.getChildrens() == null) {
			return;
		}
		
		for (int i = 0; i < root.getChildrens().size(); i++) {
			addAllTreeNodes(root.getChildrens().get(i), allTreeNodes);
		}
	}
	
	
	public static List<Point> getPath(TreeNode treeNode, Point cEnd){
		ArrayList<Point> solution = new ArrayList<>();
		solution.add(cEnd);
		while(true) {
			if(!listContainstElement(solution,treeNode.getData())) {
				solution.add(treeNode.getData());
			}
			treeNode = treeNode.getParent();
			if(treeNode == null) {
				break;
			}
		}
		return solution;
	}
	
	public static TreeNode getRoot() {
		return _tree;
	}
	
	public static boolean listContainstElement(List<Point> list ,Point p) {
		
		return list.stream().anyMatch(point -> {
			
			return p.getX() == point.getX() && p.getY() == point.getY();
		});
	}

}
