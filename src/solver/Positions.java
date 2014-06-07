package solver;

import java.awt.Point;

import representation.ABObject;
import representation.APoint;

public class Positions {

	// "touches"
	public static boolean tAbove(ABObject obj, ABObject ref) {
		if (!isRightOf(obj, ref) && !isLeftOf(obj, ref) && isAboveOf(obj, ref))
			return true;

		return false;
	}

	public static boolean tBelow(ABObject obj, ABObject ref) {
		if (!isRightOf(obj, ref) && !isLeftOf(obj, ref) && isBelowOf(obj, ref))
			return true;

		return false;
	}

	public static boolean tLeft(ABObject obj, ABObject ref) {
		if (!isAboveOf(obj, ref) && !isBelowOf(obj, ref) && isLeftOf(obj, ref))
			return true;

		return false;
	}

	public static boolean tRight(ABObject obj, ABObject ref) {
		if (!isAboveOf(obj, ref) && !isBelowOf(obj, ref) && isRightOf(obj, ref))
			return true;

		return false;
	}

	public static boolean tAbove(ABObject obj, APoint ref) {
		if (!isRightOf(obj, ref) && !isLeftOf(obj, ref) && isAboveOf(obj, ref))
			return true;

		return false;
	}

	public static boolean tBelow(ABObject obj, APoint ref) {
		if (!isRightOf(obj, ref) && !isLeftOf(obj, ref) && isBelowOf(obj, ref))
			return true;

		return false;
	}

	public static boolean tLeft(ABObject obj, APoint ref) {
		if (!isAboveOf(obj, ref) && !isBelowOf(obj, ref) && isLeftOf(obj, ref))
			return true;

		return false;
	}

	public static boolean tRight(ABObject obj, APoint ref) {
		if (!isAboveOf(obj, ref) && !isBelowOf(obj, ref) && isRightOf(obj, ref))
			return true;

		return false;
	}

	// diagonals
	public static boolean isAboveLeft(ABObject obj, ABObject ref) {
		if (isAboveOf(obj, ref) && isLeftOf(obj, ref))
			return true;

		return false;
	}

	public static boolean isAboveRight(ABObject obj, ABObject ref) {
		if (isAboveOf(obj, ref) && isRightOf(obj, ref))
			return true;

		return false;
	}

	public static boolean isBelowLeft(ABObject obj, ABObject ref) {
		if (isBelowOf(obj, ref) && isLeftOf(obj, ref))
			return true;

		return false;
	}

	public static boolean isBelowRight(ABObject obj, ABObject ref) {
		if (isBelowOf(obj, ref) && isRightOf(obj, ref))
			return true;

		return false;
	}

	public static boolean isAboveLeft(ABObject obj, APoint ref) {
		if (isAboveOf(obj, ref) && isLeftOf(obj, ref))
			return true;

		return false;
	}

	public static boolean isAboveRight(ABObject obj, APoint ref) {
		if (isAboveOf(obj, ref) && isRightOf(obj, ref))
			return true;

		return false;
	}

	public static boolean isBelowLeft(ABObject obj, APoint ref) {
		if (isBelowOf(obj, ref) && isLeftOf(obj, ref))
			return true;

		return false;
	}

	public static boolean isBelowRight(ABObject obj, APoint ref) {
		if (isBelowOf(obj, ref) && isRightOf(obj, ref))
			return true;

		return false;
	}

	// vertical and horizontal with objects
	public static boolean isRightOf(ABObject obj, ABObject ref) {
		if (obj.getBoundBox().getMinX() >= ref.getBoundBox().getMaxX())
			return true;
		return false;
	}

	public static boolean isLeftOf(ABObject obj, ABObject ref) {
		if (obj.getBoundBox().getMaxX() <= ref.getBoundBox().getMinX())
			return true;
		return false;
	}

	public static boolean isAboveOf(ABObject obj, ABObject ref) {
		// image is numbered from top to bottom
		if (obj.getBoundBox().getMaxY() <= ref.getBoundBox().getMinY())
			return true;

		return false;
	}

	public static boolean isBelowOf(ABObject obj, ABObject ref) {
		// image is numbered from top to bottom
		if (obj.getBoundBox().getMinY() >= ref.getBoundBox().getMaxY())
			return true;

		return false;
	}

	public static boolean isRightOf(ABObject obj, APoint ref) {
		if (obj.getBoundBox().getMinX() > ref.getX())
			return true;

		return false;
	}

	public static boolean isLeftOf(ABObject obj, APoint ref) {
		if (obj.getBoundBox().getMaxX() < ref.getX())
			return true;

		return false;

	}

	public static boolean isAboveOf(ABObject obj, APoint ref) {
		if (obj.getBoundBox().getMaxY() <= ref.getY())
			return true;

		return false;

	}

	public static boolean isBelowOf(ABObject obj, APoint ref) {
		if (obj.getBoundBox().getMinY() >= ref.getY())
			return true;

		return false;

	}

	public static boolean isInside(APoint obj, ABObject ref) {
		Point p = new Point((int) obj.getX(), (int) obj.getY());
		return ref.getBoundBox().contains(p);
	}
}
