package fr.insa.dorgli.projetbat.utils;

import java.awt.geom.Point2D;
import java.util.Comparator;

// classe uniquement pour trier des points dans le sens horaire (ou trigo) autour d'un point central
public class PointPolarCompare implements Comparator<Point2D.Double> {
	private final Point2D.Double center;
	private final Origin origin;
	private final Direction direction;

	public enum Origin {
		TOPLEFT(1),
		BOTTOMLEFT(-1);

		public final int value;
		private Origin(int value) {
			this.value = value;
		}
	}

	public enum Direction {
		CLOCKWISE(1),
		ANTICLOCKWISE(-1);

		public final int value;
		private Direction(int value) {
			this.value = value;
		}
	}

	public PointPolarCompare(Point2D.Double center) {
		this(center, Origin.TOPLEFT, Direction.CLOCKWISE);
	}
	public PointPolarCompare(Point2D.Double center, Origin origin, Direction direction) {
		this.center = center;
		this.origin = origin;
		this.direction = direction;
	}

	@Override
	public int compare(final Point2D.Double a, final Point2D.Double b) {
		double valueA = Math.atan2(a.getY() - center.getY(), a.getX() - center.getX());
		double valueB = Math.atan2(b.getY() - center.getY(), b.getX() - center.getX());

		if (valueA < valueB)
			return - origin.value * direction.value;
		else if (valueA > valueB)
			return origin.value * direction.value;
		else
			return 0;
	}
}

