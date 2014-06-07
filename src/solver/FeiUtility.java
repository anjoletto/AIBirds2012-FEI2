/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solver;

import java.util.Collections;
import java.util.LinkedList;

import representation.ABObject;
import representation.APoint;

/**
 * 
 * @author Lopespt
 */
public class FeiUtility implements Comparable<FeiUtility> {

	private ABObject obj;
	private double utility;

	public ABObject getObj() {
		return obj;
	}

	public void setObj(ABObject obj) {
		this.obj = obj;
	}

	public double getUtility() {
		return utility;
	}

	public void setUtility(double utility) {
		this.utility = utility;
	}

	public void addToUtility(double utility) {
		this.utility += utility;
	}

	public FeiUtility(ABObject obj, double utility) {
		this.obj = obj;
		this.utility = utility;
	}

	public static LinkedList<FeiUtility> getUtilities(
			LinkedList<ABObject> birds, LinkedList<ABObject> pigs,
			LinkedList<ABObject> destructables,
			LinkedList<ABObject> undestructables, LinkedList<ABObject> misc) {

		// calculates the utility for each part of the problem
		LinkedList<FeiUtility> birdsU = birdsU(birds, pigs);
		LinkedList<FeiUtility> pigsU = pigsU(pigs, destructables,
				undestructables);
		LinkedList<FeiUtility> desU = destructablesU(pigs, destructables);
		LinkedList<FeiUtility> udesU = undestructablesU(pigs, undestructables);

		// combine those utilities
		LinkedList<FeiUtility> retorno = utility(birdsU, pigsU, desU, udesU);

		// sort and returns
		Collections.sort(retorno);
		return retorno;
	}

	public static LinkedList<FeiUtility> utility(LinkedList<FeiUtility> birdsU,
			LinkedList<FeiUtility> pigsU, LinkedList<FeiUtility> desU,
			LinkedList<FeiUtility> udesU) {

		LinkedList<FeiUtility> retorno = new LinkedList<FeiUtility>();

		for (FeiUtility u : desU)
			retorno.add(u);

		for (FeiUtility u : udesU)
			retorno.add(u);

		for (FeiUtility u : pigsU)
			retorno.add(u);

		return retorno;
	}

	public static LinkedList<FeiUtility> destructablesU(
			LinkedList<ABObject> pigs, LinkedList<ABObject> destructables) {

		LinkedList<FeiUtility> retorno = new LinkedList<FeiUtility>();
		LinkedList<FeiUtility> ices = new LinkedList<FeiUtility>();

		APoint ref = getCentroidPigs(pigs);

		for (ABObject obj : destructables) {
			FeiUtility u = new FeiUtility(obj, 0.0);
			if (Positions.tAbove(obj, ref))
				u.addToUtility(-2.0);
			else if (Positions.tBelow(obj, ref))
				u.addToUtility(0.0);
			else if (Positions.tLeft(obj, ref))
				u.addToUtility(-1.0);
			else if (Positions.tRight(obj, ref))
				u.addToUtility(+1.0);
			else if (Positions.isAboveLeft(obj, ref))
				u.addToUtility(-2.0);
			else if (Positions.isAboveRight(obj, ref))
				u.addToUtility(+1.0);
			else if (Positions.isBelowLeft(obj, ref))
				u.addToUtility(+1.0);
			else if (Positions.isBelowRight(obj, ref))
				u.addToUtility(+1.0);

			if (obj.getVision_name().equalsIgnoreCase("Ice")) {
				u.addToUtility(+1.0);
				ices.add(u);
			}

			retorno.add(u);
		}

		for (FeiUtility ice : ices) {
			for (FeiUtility ice2 : ices)
				if (!FeiSolver.sameObject(ice.getObj(), ice2.getObj())) {
					if (Positions.isBelowOf(ice.getObj(), ice2.getObj())
							&& Positions.tLeft(ice.getObj(), ref))
						ice.addToUtility(+1.0);
					if (Positions.isLeftOf(ice.getObj(), ice2.getObj()))
						ice.addToUtility(+1.0);
				}
		}

		return retorno;
	}

	public static LinkedList<FeiUtility> undestructablesU(
			LinkedList<ABObject> pigs, LinkedList<ABObject> undestructables) {

		LinkedList<FeiUtility> retorno = new LinkedList<FeiUtility>();

		APoint ref = getCentroidPigs(pigs);

		for (ABObject obj : undestructables) {
			FeiUtility u = new FeiUtility(obj, 0.0);
			if (Positions.tAbove(obj, ref))
				u.addToUtility(-2.0);
			else if (Positions.tBelow(obj, ref))
				u.addToUtility(0.0);
			else if (Positions.tLeft(obj, ref))
				u.addToUtility(-2.0);
			else if (Positions.tRight(obj, ref))
				u.addToUtility(+1.0);
			else if (Positions.isAboveLeft(obj, ref))
				u.addToUtility(+1.0);
			else if (Positions.isAboveRight(obj, ref))
				u.addToUtility(-2.0);
			else if (Positions.isBelowLeft(obj, ref))
				u.addToUtility(-2.0);
			else if (Positions.isBelowRight(obj, ref))
				u.addToUtility(-2.0);

			retorno.add(u);
		}

		return retorno;
	}

	public static LinkedList<FeiUtility> pigsU(LinkedList<ABObject> pigs,
			LinkedList<ABObject> destructables,
			LinkedList<ABObject> undestructables) {

		LinkedList<FeiUtility> retorno = new LinkedList<FeiUtility>();

		for (ABObject pig : pigs) {
			FeiUtility u = new FeiUtility(pig, 0.0);

			for (ABObject obj : destructables) {
				if (Positions.tAbove(obj, pig))
					u.addToUtility(-2.0);
				else if (Positions.tBelow(obj, pig))
					u.addToUtility(0.0);
				else if (Positions.tLeft(obj, pig))
					u.addToUtility(-1.0);
				else if (Positions.tRight(obj, pig))
					u.addToUtility(+1.0);
				else if (Positions.isAboveLeft(obj, pig))
					u.addToUtility(-2.0);
				else if (Positions.isAboveRight(obj, pig))
					u.addToUtility(0.0);
				else if (Positions.isBelowLeft(obj, pig))
					u.addToUtility(+2.0);
				else if (Positions.isBelowRight(obj, pig))
					u.addToUtility(+1.0);
			}

			for (ABObject obj : undestructables) {
				if (Positions.tAbove(obj, pig))
					u.addToUtility(-2.0);
				else if (Positions.tBelow(obj, pig))
					u.addToUtility(0.0);
				else if (Positions.tLeft(obj, pig))
					u.addToUtility(-2.0);
				else if (Positions.tRight(obj, pig))
					u.addToUtility(+1.0);
				else if (Positions.isAboveLeft(obj, pig))
					u.addToUtility(-2.0);
				else if (Positions.isAboveRight(obj, pig))
					u.addToUtility(0.0);
				else if (Positions.isBelowLeft(obj, pig))
					u.addToUtility(+2.0);
				else if (Positions.isBelowRight(obj, pig))
					u.addToUtility(+1.0);
			}

			retorno.add(u);
		}

		for (FeiUtility pig1 : retorno) {
			for (FeiUtility pig2 : retorno) {
				if (!FeiSolver.sameObject(pig1.getObj(), pig2.getObj())) {
					if (Positions.isAboveRight(pig1.getObj(), pig2.getObj()))
						pig1.addToUtility(+2.0);
				}

			}
		}

		return retorno;
	}

	public static LinkedList<FeiUtility> birdsU(LinkedList<ABObject> birds,
			LinkedList<ABObject> pigs) {

		/*
		 * returns the utility of the birds considering the type, position and
		 * relation between birds and pigs
		 */

		LinkedList<FeiUtility> retorno = new LinkedList<FeiUtility>();

		// relation between number of birds and pigs
		double step = (double) pigs.size() / (double) birds.size();

		// init
		for (ABObject bird : birds)
			retorno.add(new FeiUtility(bird, 0.0));

		// by fixed position
		retorno.peekFirst().setUtility(1);
		if (retorno.size() >= 3)
			retorno.get(1).setUtility(1 - step);
		if (retorno.size() >= 4)
			retorno.get(retorno.size() - 2).setUtility(1 - step);
		retorno.peekLast().setUtility(1);

		// decreases for each position
		for (int i = 2; i < retorno.size() - 2; i++)
			retorno.get(i).addToUtility(1 - i * step);

		// by type
		for (FeiUtility bird : retorno) {
			if (bird.getObj().getVision_name()
					.equalsIgnoreCase(FeiSolver.birdsN[0]))
				bird.addToUtility(0.0);
			if (bird.getObj().getVision_name()
					.equalsIgnoreCase(FeiSolver.birdsN[1]))
				bird.addToUtility(2.0);
			if (bird.getObj().getVision_name()
					.equalsIgnoreCase(FeiSolver.birdsN[2]))
				bird.addToUtility(3.0);
		}

		return retorno;
	}

	public static APoint getCentroidPigs(LinkedList<ABObject> oponents) {
		double numPigs = 0;
		APoint retorno = new APoint(0, 0);
		for (ABObject obj : oponents) {
			numPigs++;
			retorno.setX((int) (retorno.getX() + obj.getCentroid().getX()));
			retorno.setY((int) (retorno.getY() + obj.getCentroid().getY()));
		}

		retorno.setX((int) (retorno.getX() / numPigs));
		retorno.setY((int) (retorno.getY() / numPigs));
		return retorno;
	}

	@Override
	public int compareTo(FeiUtility o) {
		if (this.utility == o.utility) {
			if (Positions.isLeftOf(this.obj, o.obj))
				return -1;
			else if (Positions.isRightOf(this.obj, o.obj))
				return 1;
		}

		return this.utility < o.utility ? 1 : -1;
	}

	@Override
	public String toString() {
		return "FeiUtility{" + "obj=" + obj.toString() + ", utility=" + utility
				+ '}';
	}
}
