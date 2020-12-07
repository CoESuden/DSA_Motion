package de.hft.algorithmn;

import java.util.ArrayList;
import java.util.List;

import de.hft.objects.Point;

public class StraightLine {

	private StraightLine() {
		// empty
	}

	public static List<Point> getStraightWithBresenhamAlgo(int xstart, int ystart, int xend, int yend) {
		List<Point> straightLine = new ArrayList<>();
		straightLine.add(new Point(xstart,ystart));
		int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;

		dx = xend - xstart;
		dy = yend - ystart;

		incx = sign(dx);
		incy = sign(dy);

		if (dx < 0)
			dx = -dx;
		if (dy < 0)
			dy = -dy;

		if (dx > dy) {
			pdx = incx;
			pdy = 0;
			es = dy;
			el = dx;
		} else {
			pdx = 0;
			pdy = incy;
			es = dx;
			el = dy;
		}

		x = xstart;
		y = ystart;
		err = el / 2;

		for (int t = 0; t < el; t++) {
			err -= es;
			if (err < 0) {
				err += el;
				x += incx;
				y += incy;
			} else {
				x += pdx;
				y += pdy;
			}
			straightLine.add(new Point(x, y));
		}
		
		return straightLine;
	}
	
	
	public static List<Point> getStraightWithBresenhamAlgo(int xstart, int ystart, int xend, int yend, int lineLength) {
		List<Point> straightLine = new ArrayList<>();
		straightLine.add(new Point(xstart,ystart));
		int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;

		dx = xend - xstart;
		dy = yend - ystart;

		incx = sign(dx);
		incy = sign(dy);

		if (dx < 0)
			dx = -dx;
		if (dy < 0)
			dy = -dy;

		if (dx > dy) {
			pdx = incx;
			pdy = 0;
			es = dy;
			el = dx;
		} else {
			pdx = 0;
			pdy = incy;
			es = dx;
			el = dy;
		}

		x = xstart;
		y = ystart;
		err = el / 2;

		for (int t = 0; t < el; t++) {
			if(t == lineLength)
				break;
			err -= es;
			if (err < 0) {
				err += el;
				x += incx;
				y += incy;
			} else {
				x += pdx;
				y += pdy;
			}
			straightLine.add(new Point(x, y));
			
		}
		
		return straightLine;
	}
	
	private static int sign (int x) {
        return (x > 0) ? 1 : (x < 0) ? -1 : 0;
}
}
