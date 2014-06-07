/** Insert code here */
package solver;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import representation.ABObject;
import representation.APoint;
import representation.util.ObjectCollector;

public class FeiSolver {

	private LinkedList<ABObject> birds;
	private LinkedList<ABObject> pigs;
	private LinkedList<ABObject> destructables;
	private LinkedList<ABObject> undestructables;
	private LinkedList<ABObject> misc;

	public static String[] birdsN = { "Red Bird", "Blue Bird", "Yellow Bird" };
	public static String[] pigsN = { "Pig" };
	public static String[] destructablesN = { "Wood", "Ice" };
	public static String[] undestructablesN = { "Stone" };
	public static String[] trajectoryN = { "Trajectory", "Tap" };
	public static String[] miscN = { "Sky", "Ground", "Unbreakable Wood" };

	private LinkedList<FeiUtility> utilitySet;

	public FeiSolver() {
		// initialize everything
		birds = new LinkedList<ABObject>();
		pigs = new LinkedList<ABObject>();
		destructables = new LinkedList<ABObject>();
		undestructables = new LinkedList<ABObject>();
		misc = new LinkedList<ABObject>();
	}

	private void sortObjects(HashMap<Double, ABObject> objects) {
		for (ABObject object : objects.values()) {

			for (String s : birdsN)
				if (object.getVision_name().equalsIgnoreCase(s))
					birds.add(object);

			for (String s : pigsN)
				if (object.getVision_name().equalsIgnoreCase(s))
					pigs.add(object);

			for (String s : destructablesN)
				if (object.getVision_name().equalsIgnoreCase(s))
					destructables.add(object);

			for (String s : undestructablesN)
				if (object.getVision_name().equalsIgnoreCase(s))
					undestructables.add(object);

			for (String s : miscN)
				if (object.getVision_name().equalsIgnoreCase(s))
					misc.add(object);

		}
	}

	public LinkedList<ABObject> getTargets(ObjectCollector oc) {

		// divide what it has found
		sortObjects(oc.getObjs());

		// utility stuff
		utilitySet = FeiUtility.getUtilities(birds, pigs, destructables,
				undestructables, misc);

		LinkedList<ABObject> retorno = new LinkedList<ABObject>();

		Collections.sort(utilitySet);
		retorno.add(utilitySet.peekFirst().getObj());

		return retorno;
	}

	public static boolean sameObject(ABObject o1, ABObject o2) {
		double distance = Math
				.sqrt(Math.pow(o1.getCentroid().getX()
						- o2.getCentroid().getX(), 2)
						+ Math.pow(o1.getCentroid().getY()
								- o2.getCentroid().getY(), 2));
		System.out.println("Distance between " + o1 + " and " + o2 + " is "
				+ distance);
		if (distance < 5) {

			return true;
		} else
			return false;
	}

	public ABObject maxProbability(ObjectCollector oc) {

		ABObject retorno = this.utilitySet.peekFirst().getObj();
		Trajectory trj = new Trajectory(oc);

		double prob = 0.0; // maximum found probability

		for (FeiUtility u : this.utilitySet) {
			List<APoint> targets = trj.computeFlight(u.getObj());

			int hits = 0;
			for (APoint t : targets)
				if (Positions.isInside(t, u.getObj()))
					hits++;

			double p = (double) hits / (double) targets.size();
			if (prob < p) {
				prob = p;
				retorno = u.getObj();
			}
		}

		return retorno;
	}
}
