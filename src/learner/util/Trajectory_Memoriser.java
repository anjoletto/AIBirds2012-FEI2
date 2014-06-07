/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package learner.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import debug.Debug;

import Jama.Matrix;

/**
 * Memorise all possible trajectories given data.
 * 
 * Model for each start point: y = (para0) + (para1)*(x) + (para2)*(x^2)
 *   i.e. quadratic function in term of x
 *   
 * Training: Start point and trajectory points
 *
 * @author Kar Wai Lim
 * 
 * NOTE: This learner need many training data to work
 * 
 * Last updated: 21 Nov 2012
 *
 */

public class Trajectory_Memoriser implements Serializable {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -7859329114861536748L;
	
	/** Modifiable variables */
	private double acceptPrecision = 0.000001;	// the distance used to deem two coordinates are equivalent (via geometry distance)
	private double lambda = 0.0;					// regularise parameter used in regression
	
	private int array_length = 50;				// starting array length for storing trajectory coordinates
	private static final int parameter_length = 3;
	
	private int n;				// number of data point
	private double[] para;		// parameters to be trained
	private double[] y;			// y coordinates (vector)
	private double[][] X;		// basis for training (matrix)

	private double xStart = 0.0;	// temporary storage for starting point of bird
	private double yStart = 0.0;	// ..

	private HashMap<Point, Trajectory> allTrajectories;	// table of all trajectories


	/** Constructor */
	public Trajectory_Memoriser() {
		n = 0;
		para = new double[parameter_length];
		y = new double[array_length];
		X = new double[array_length][parameter_length];
		allTrajectories = new HashMap<Point, Trajectory>();
	}
	
	// =========================================================================================================================
	// 										Functions to change variables 
	// =========================================================================================================================

	/** function to change lambda, the regularise parameter */
	public void changeLambda(double lambda) {
		this.lambda = lambda;
	}

	/** function to change accept precision, the geometry distance to deem two coordinates are equal */
	public void changeAcceptPrecision(double acceptPrecision) {
		this.acceptPrecision = acceptPrecision;
	}

	// =========================================================================================================================
	// 												Learning/Training
	// =========================================================================================================================
	
	/** Input the starting coordinate (before bird is released) for training.
	 *  Do this once before for each bird before adding trajectory coordinates! */
	//  xStart and yStart are usually negative
	public void addStartPoint(double xStart, double yStart) {
		this.xStart = xStart;
		this.yStart = yStart;
	}

	/** Add a coordinate of the bird's trajectory for training. 
	 *  Only do this after adding the starting coordinate. */
	public void addTrajectoryPoint(double xPoint, double yPoint) {
		if(xStart*yStart==0) {
			Debug.echo("Potential Error: You did not input start points!");
		}
		if(n == array_length)
			expand();
		y[n] = yPoint;
		X[n][0] = 1.0;
		X[n][1] = xPoint;
		X[n][2] = xPoint*xPoint;
		n++;
	}

	/** expand the size of data points array (2 times bigger) */
	// System.arraycopy notes: http://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html
	private void expand() {
		array_length = array_length*2;
		double[] yTemp = y;
		double[][] XTemp = X;
		y = new double[array_length];
		X = new double[array_length][parameter_length];
		System.arraycopy(yTemp, 0, y, 0, n);
		System.arraycopy(XTemp, 0, X, 0, n);
//		System.out.print("array_length: " + yTemp.length + " increased to " + y.length + "\n");
	}

	/** train parameters */
	private void updatePara() {
		if(xStart*yStart==0)
			Debug.echo(this,"Potential Error: You did not input start points! or you assumed it is (0,0)!");
		if(n < parameter_length) {
			Debug.echo("Not enough trajectory coordinates to train this trajectory! This trajectory will be set to NaN!");
			for(int i=0; i<parameter_length; i++) {
				para[i] = Double.NaN;
			}
		} else {
			double[] yNew = new double[n];
			double[][] XNew = new double[n][parameter_length];
			System.arraycopy(y, 0, yNew, 0, n);						// trim the array to length n
			System.arraycopy(X, 0, XNew, 0, n);
			Matrix yMatrix = new Matrix(yNew, n);
			Matrix XMatrix = new Matrix(XNew, n, parameter_length);
			Matrix paraMatrix = new Matrix(parameter_length, 1);	// empty matrix (vector)
			Matrix IMatrix = Matrix.identity(parameter_length, parameter_length);	// identity matrix
			Matrix XTranspose = XMatrix.transpose();
			paraMatrix = (XTranspose.times(XMatrix)).plus(IMatrix.times(lambda)).inverse().times(XTranspose).times(yMatrix);
			for(int i=0; i<parameter_length; i++) {
				para[i] = paraMatrix.get(i, 0);
			}
		}
	}


	/** Train and save trajectory parameters based on stored values, and reset stored values. */
	public void storePara() {
		if(n >= parameter_length) {
			updatePara();
			Point currentPoint = new Point(xStart, yStart);
			Trajectory currentTraj = new Trajectory(para);
			allTrajectories.put(currentPoint, currentTraj);
		} else {
			Debug.echo("Error: You did not input enough trajectory points! No trajectory is saved and stored values removed!");
		}
		
		// reset count and used data points after storing
		clearData();
	}

	/** Reset everything (xStart, yStart and parameters) except trained trajectory parameters */
	public void clearData() {
		n = 0;
		xStart = 0.0;
		yStart = 0.0;
		para = new double[parameter_length];
		y = new double[array_length];
		X = new double[array_length][parameter_length];
	}

	// =========================================================================================================================
	// 												Prediction 
	// =========================================================================================================================

	/** Calculate the geometry distance between (x1, y1) and (x2, y2) */
	public static double distance(double x1, double y1, double x2, double y2) {
		double xDiff = x1 - x2;
		double yDiff = y1 - y2;
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}
	
	/** Predict the y-coordinate given xStart and yStart and the position of bird at x-coordinate.
	 *  NaN is returned if prediction failed!															 */
	public double predictPoint(double xStart, double yStart, double xPosition) {
		double[] tempPara = predictTrajectory(xStart, yStart);
		double yPoint = tempPara[0] + tempPara[1] * xPosition + tempPara[2] * xPosition * xPosition;
		return yPoint;
	}

	/** Predict trajectory given xStart and yStart.
	 *  Trajectory returned in the form of double array (3 values). 
	 *  If trajectory cannot be predicted, the values are NaN! 			*/
	public double[] predictTrajectory(double xStart, double yStart) {
		
		Point point = new Point(xStart, yStart);
		Set<Point> allPoints = allTrajectories.keySet();
		
		if(allPoints.size() == 0) {
			Debug.echo("ERROR in predictTrajectory(): No Trajectory found!");
			return new double[]{Double.NaN, Double.NaN, Double.NaN};
		} else if(allPoints.size() == 1) {
			return allTrajectories.get(allPoints.iterator().next()).getPara();
		}
		
		// exact match
		if(allPoints.contains(point)) {
			Trajectory traj = allTrajectories.get(point);
			return traj.getPara();
		}

		// INTERPOLATION
		double minDistance1, minDistance2;
		minDistance1 = Double.POSITIVE_INFINITY;
		minDistance2 = Double.POSITIVE_INFINITY;
		Point smallest1, smallest2;
		smallest1 = null;
		smallest2 = null;
		
		// keep track of the points closest to required start point
		for(Point key : allPoints) {
			Trajectory traj = allTrajectories.get(key);
			double trajXStart = key.getXStart();
			double trajYStart = key.getYStart();
			double tempDistance = distance(xStart, yStart, trajXStart, trajYStart);
			if((tempDistance < acceptPrecision)) {
				return traj.getPara();
			} else if(tempDistance < minDistance1) {	// if it is the smallest, i.e minimum
				minDistance2 = minDistance1;
				smallest2 = smallest1;
				minDistance1 = tempDistance;
				smallest1 = key;
			} else if(tempDistance < minDistance2) {	// if it is the second smallest
				minDistance2 = tempDistance;
				smallest2 = key;						// i.e. no changes to the minimum
			}		
		}

		// now we have 2 points correspond to the closest points to the required (input) start points, and we can interpolate
		double[] tempPara = interpolate(xStart, yStart, smallest1, allTrajectories.get(smallest1), smallest2, allTrajectories.get(smallest2));
		
		return tempPara;
	}

	/** simple interpolation */
	private double[] interpolate(double x0, double y0, Point p1, Trajectory t1, Point p2, Trajectory t2) {
		double x1 = p1.getXStart();
		double y1 = p1.getYStart();
		double x2 = p2.getXStart();
		double y2 = p2.getYStart();
		double[] para1 = t1.getPara();
		double[] para2 = t2.getPara();
		
		double[] paraEst = new double[parameter_length];
		
		if(x2-x1 != 0) {
			for(int i=0; i<parameter_length; i++) {
				paraEst[i] = para1[i] + (para2[i] - para1[i]) * (x0 - x1) / (x2 - x1);
			}
		} else if(y2-y1 != 0) {
			for(int i=0; i<parameter_length; i++) {
				paraEst[i] = para1[i] + (para2[i] - para1[i]) * (y0 - y1) / (y2 - y1);
			}
		} else {
			paraEst = para1;
		}
		
		return paraEst;
	}

	// =========================================================================================================================
	// 												Get function/Debugging function
	// =========================================================================================================================
	
	/** return the number of unstored trajectory points */
	public int get_n() {
		return n;
	}
	
	/** Return the number of learned trajectories */
	public int get_m() {
		return allTrajectories.keySet().size();
	}
	
	/** Retrieved the learned trajectory at Point p */
	public Trajectory getTrajectory(Point p) {
		return allTrajectories.get(p);
	}
	
	/** Return all learned trajectories stored */
	public HashMap<Point, Trajectory> getAllTrajectories() {
		return allTrajectories;
	}
	
	// =========================================================================================================================
	/**
	 *  Internal data structure for storing simple coordinate (Point) and Trajectory
	 */
	public static class Trajectory {
		private double[] para;			// 3 values array

		public Trajectory(double[] para) {
			this.para = para;
		}

		public double[] getPara() {
			return para;
		}
		
		@Override
		public String toString() {
			String str = "{" + para[0] + ", " + para[1] + ", " + para[2] + "}";
			return str;
		}
	}
	
	public static class Point implements Comparable<Point> {
		private Double xStart;
		private Double yStart;
		
		public Point(double xStart, double yStart) {
			this.xStart = xStart;
			this.yStart = yStart;
		}
		
		public double getXStart() {
			return xStart;
		}
		public double getYStart() {
			return yStart;
		}
		
		@Override
		public String toString() {
			String str = "(" + xStart + ", " + yStart + ")";
			return str;
		}

		@Override
		public int compareTo(Point obj) {
			if(xStart.compareTo(obj.getXStart()) != 0) {
				return xStart.compareTo(obj.getXStart());
			}
			return yStart.compareTo(obj.getYStart());
		}
		
		@Override
		public boolean equals(Object obj) {
			return xStart == ((Point) obj).getXStart() && yStart == ((Point) obj).getYStart();
		}
		
		@Override
		public int hashCode() {
			String str = xStart + "," + yStart;
			return str.hashCode();
		}
	}
	// =========================================================================================================================
}