package de.hft.objects;

public class Edge {

	private Point _start;
	private Point _end;
	
	public Edge(Point start, Point end) {
		_start = start;
		_end = end;
	}

	public Point getStart() {
		return _start;
	}

	public Point getEnd() {
		return _end;
	}
	
}
