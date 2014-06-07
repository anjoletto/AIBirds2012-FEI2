/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package learner.util;

import java.io.Serializable;
import debug.Debug;
import java.util.HashMap;
import java.util.Set;

import Jama.Matrix;

/**
 * Memorise all possible trajectories given data.
 * Work for normal and tap birds.
 * 
 * Model for each start point: y = (para0) + (para1)*(x) + (para2)*(x^2)
 *   i.e. quadratic function in term of x
 *
 * @author Kar Wai Lim
 * 
 * NOTE: This learner need many training data to work
 * Potential problem: memory usage, efficiency
 * 
 * TODO: clean up and simplify duplicate codes (especially for blue bird extension)
 * Note: module not fully tested! (test case not exhaustive)
 * 
 * Last updated: 21 Nov 2012
 *
 */

public class Trajectory_Memoriser_Tap implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 4981325037594914688L;

	/** Modifiable variables */
	private double acceptPrecision = 0.000001;	// the distance used to deem two coordinates are equivalent (via geometry distance)
	private double lambda = 0.0;				// regularise parameter used in regression

	private int array_length = 50;		// starting array length to store trajectory points
	private final int parameter_length = 3;
	
	private int n;				// number of data point
	private double[] para;		// parameters to be trained
	private double[] y;			// y coordinates (vector)
	private double[][] X;		// basis for training (matrix)
	
	/** For blue birds */
	private boolean isBlueBird = false;
	
	private int n2;				
	private double[] para2;		
	private double[] y2;			
	private double[][] X2;		
	private int n3;				
	private double[] para3;		
	private double[] y3;			
	private double[][] X3;
	/** End for blue birds */
	
	private double xStart = 0;	// temporary storage for starting point and tap point of bird
	private double yStart = 0;	// ..
	private double xTap = Double.POSITIVE_INFINITY;	// ..
	private double yTap = Double.POSITIVE_INFINITY;	// ..
	private double tapTime = Double.POSITIVE_INFINITY;	// ..
	
	private HashMap<PointPlus, TapTrajectory> allTapTrajectories;	// table of all trajectories
	
	public Trajectory_Memoriser nonTapMemoriser;
	
	public double xTaptoTapTimeRatio = 0;
	private double xTaptoTapTimeRatioCount = 0;
	
	/** Constructor */
	public Trajectory_Memoriser_Tap() {
		n = 0;
		para = new double[parameter_length];
		y = new double[array_length];
		X = new double[array_length][parameter_length];
		allTapTrajectories = new HashMap<PointPlus, TapTrajectory>();
		this.nonTapMemoriser = new Trajectory_Memoriser();
	}
	
	/** Constructor for blue bird */
	public Trajectory_Memoriser_Tap(boolean isSpecialBird) {
		this.isBlueBird = isSpecialBird;
		allTapTrajectories = new HashMap<PointPlus, TapTrajectory>();
		this.nonTapMemoriser = new Trajectory_Memoriser();
		
		n = 0;
		para = new double[parameter_length];
		y = new double[array_length];
		X = new double[array_length][parameter_length];
		
		if(isSpecialBird) {
			n2 = 0;
			para2 = new double[parameter_length];
			y2 = new double[array_length];
			X2 = new double[array_length][parameter_length];
			n3 = 0;
			para3 = new double[parameter_length];
			y3 = new double[array_length];
			X3 = new double[array_length][parameter_length];
		}
	}
	
	/** function to tweak parameters */
	public void changeLambda(double lambda) {
		this.lambda = lambda;
	}

	/** function to tweak parameters */
	public void changeAcceptPrecision(double acceptPrecision) {
		this.acceptPrecision = acceptPrecision;
	}
	
	
	// =========================================================================================================================
	// ================ Learning ===============================================================================================
	// =========================================================================================================================
	
	/** add a starting point (before bird is released) for training purposes. */
	// Do this for each bird released.
	// xStart and yStart should usually be negative
	public void addStartPoint(double xStart, double yStart) {
		this.xStart = xStart;
		this.yStart = yStart;
		nonTapMemoriser.addStartPoint(xStart, yStart);
	}
	
	/** add a tap point for training purposes. */
	// Do this for each bird released.
	// xStart should always be positive
	public void addTapPoint(double xTap, double yTap, double tapTime) {
		this.xTap = xTap;
		this.yTap = yTap;
		this.tapTime = tapTime;
		
		// save the ratio of xTap to tapTime for estimation purpose later
		xTaptoTapTimeRatioCount++;
		xTaptoTapTimeRatio = ((xTaptoTapTimeRatio*(xTaptoTapTimeRatioCount-1)) + xTap/tapTime)/ xTaptoTapTimeRatioCount;
	}
	
	/** add a point of the bird's trajectory for training given temporarily stored start points. */
	public void addTrajectoryPoint(double xPoint, double yPoint) {
		if(xStart*yStart==0) {
			Debug.echo("Potential Error: You did not input start points! or you assumed they are (0,0)!");
		}
		if(xPoint < xTap) {
			nonTapMemoriser.addTrajectoryPoint(xPoint, yPoint);
		} else {
			if(n == array_length)
				expand();
			y[n] = yPoint;
			X[n][0] = 1;
			X[n][1] = xPoint;
			X[n][2] = xPoint*xPoint;
			n++;
		}
	}
	
	/** For birds with multiple (3) trajectories, 
	 * add a point of the bird's trajectory for training given temporarily stored start points.
	 * Note: only work with 3 trajectories, can be extended to multiple if needed (use array) */
	public void addMultipleTrajectoryPoint(Double x1Point, Double y1Point, Double x2Point, Double y2Point, Double x3Point, Double y3Point) {
		if(!isBlueBird) {
			Debug.echo("Warning: This method should only be called for blue birds!");
			return;
		}
		if(xStart*yStart*xTap*yTap==0) {
			Debug.echo("Potential Error: You did not input start points! or you assumed they are (0,0)!");
		}
		if(n == array_length || n2 == array_length || n3 == array_length) {
			expand();
		}
		if(x1Point != null && y1Point != null) {
			y[n] = y1Point;
			X[n][0] = 1;
			X[n][1] = x1Point;
			X[n][2] = x1Point*x1Point;
			n++;
		}
		if(x2Point != null && y2Point != null) {
			y2[n2] = y2Point;
			X2[n2][0] = 1;
			X2[n2][1] = x2Point;
			X2[n2][2] = x2Point*x2Point;
			n2++;
		}
		if(x2Point != null && y2Point != null) {
			y3[n3] = y3Point;
			X3[n3][0] = 1;
			X3[n3][1] = x3Point;
			X3[n3][2] = x3Point*x3Point;
			n3++;
		}
	}

	/** expand the size of data points array (2 times bigger)
	 *  Note: yTemp and XTemp act as pointers to point to old arrays */
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
		
		if(isBlueBird) {
			yTemp = y2;
			XTemp = X2;
			y2 = new double[array_length];
			X2 = new double[array_length][parameter_length];
			System.arraycopy(yTemp, 0, y2, 0, n2);
			System.arraycopy(XTemp, 0, X2, 0, n2);
			
			yTemp = y3;
			XTemp = X3;
			y3 = new double[array_length];
			X3 = new double[array_length][parameter_length];
			System.arraycopy(yTemp, 0, y3, 0, n3);
			System.arraycopy(XTemp, 0, X3, 0, n3);
		}
	}

	/** train parameters */
	private void updatePara() {
		if(xStart*yStart*xTap*yTap==0)
			Debug.echo("Potential Error: You did not input start points and/or tap points! or you assumed they are (0,0)!");
		
		// for birds other than blue bird
		if(!isBlueBird) {
			if(n < parameter_length) {
				Debug.echo("Potential Error: There might not be enough trajectory points after xTap!");
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
		} else {
			// for blue bird
			if(Math.min(Math.min(n, n2), n3) < parameter_length) {
				Debug.echo("Potential Error: There might not be enough trajectory points after xTap!");
				for(int i=0; i<parameter_length; i++) {
					para[i] = Double.NaN;
					para2[i] = Double.NaN;
					para3[i] = Double.NaN;
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
				
				double[] yNew2 = new double[n2];
				double[][] XNew2 = new double[n2][parameter_length];
				System.arraycopy(y2, 0, yNew2, 0, n2);						// trim the array to length n
				System.arraycopy(X2, 0, XNew2, 0, n2);
				Matrix yMatrix2 = new Matrix(yNew2, n2);
				Matrix XMatrix2 = new Matrix(XNew2, n2, parameter_length);
				Matrix paraMatrix2 = new Matrix(parameter_length, 1);	// empty matrix (vector)
				Matrix IMatrix2 = Matrix.identity(parameter_length, parameter_length);	// identity matrix
				Matrix XTranspose2 = XMatrix2.transpose();
				paraMatrix2 = (XTranspose2.times(XMatrix2)).plus(IMatrix2.times(lambda)).inverse().times(XTranspose2).times(yMatrix2);
				for(int i=0; i<parameter_length; i++) {
					para2[i] = paraMatrix2.get(i, 0);
				}
				
				double[] yNew3 = new double[n3];
				double[][] XNew3 = new double[n3][parameter_length];
				System.arraycopy(y3, 0, yNew3, 0, n3);						// trim the array to length n
				System.arraycopy(X3, 0, XNew3, 0, n3);
				Matrix yMatrix3 = new Matrix(yNew3, n3);
				Matrix XMatrix3 = new Matrix(XNew3, n3, parameter_length);
				Matrix paraMatrix3 = new Matrix(parameter_length, 1);	// empty matrix (vector)
				Matrix IMatrix3 = Matrix.identity(parameter_length, parameter_length);	// identity matrix
				Matrix XTranspose3 = XMatrix3.transpose();
				paraMatrix3 = (XTranspose3.times(XMatrix3)).plus(IMatrix3.times(lambda)).inverse().times(XTranspose3).times(yMatrix3);
				for(int i=0; i<parameter_length; i++) {
					para[i] = paraMatrix3.get(i, 0);
				}
			}
		}
	}
	
	/** store parameters into 'allTapTrajectories' and reset count */
	public void storePara() {
		// non blue bird:
		if(!isBlueBird) {
			if(n >= parameter_length) {
				updatePara();
				PointPlus currentPoint = new PointPlus(xStart, yStart, xTap, yTap, tapTime);
				TapTrajectory currentTraj = new TapTrajectory(para);
				allTapTrajectories.put(currentPoint, currentTraj);
			} else {
				Debug.echo("Potential Error: There might not be enough trajectory points after xTap!");
			}
		} else {
			// blue bird:
			if(Math.min(Math.min(n, n2), n3) >= parameter_length) {
				updatePara();
				double[] threePara = new double[3*parameter_length];
				for(int i = 0; i < parameter_length; i++) {
					threePara[i] = para[i];
					threePara[i+parameter_length] = para2[i];
					threePara[i+2*parameter_length] = para3[i];
				}
				PointPlus currentPoint = new PointPlus(xStart, yStart, xTap, yTap, tapTime);
				TapTrajectory currentTraj = new TapTrajectory(threePara);
				allTapTrajectories.put(currentPoint, currentTraj);
			} else {
				Debug.echo("Potential Error: There might not be enough trajectory points after xTap!");
			}
		}
		// reset count and used data points after storing
		clearData();
		
		nonTapMemoriser.storePara();
	}
	
	/** reset everything except stored trajectories (xStart, yStart, xTap, yTap and parameters) */
	public void clearData() {
		xStart = 0;
		yStart = 0;
		xTap = Double.POSITIVE_INFINITY;
		yTap = Double.POSITIVE_INFINITY;
		tapTime = Double.POSITIVE_INFINITY;
		n = 0;
		para = new double[parameter_length];
		y = new double[array_length];
		X = new double[array_length][parameter_length];
		
		if(isBlueBird) {
			n2 = 0;
			para2 = new double[parameter_length];
			y2 = new double[array_length];
			X2 = new double[array_length][parameter_length];
			n3 = 0;
			para3 = new double[parameter_length];
			y3 = new double[array_length];
			X3 = new double[array_length][parameter_length];
		}
	}

	// =========================================================================================================================
	// ================ Predicting =============================================================================================
	// =========================================================================================================================

	private static double weight = 1000; // weight used in distance function
	
	/** calculate the distance between (w1, x1, y1, z1) and (w2, x2, y2, z2) */
	public static double distance(double w1, double x1, double y1, double z1, double w2, double x2, double y2, double z2) {
		double wDiff = weight * (w1 - w2);
		double xDiff = weight * (x1 - x2);
		double yDiff = y1 - y2;
		double zDiff = z1 - z2;
		return Math.sqrt(wDiff*wDiff + xDiff*xDiff + yDiff*yDiff + zDiff*zDiff);
	}
	
	/** return a predicted trajectory (6 values array: 3 for normal trajectory, 3 for trajectory after tap) given xStart, yStart, xTap and yTap */
	/** for blue bird: 12 values array: 3 for normal, 9 for after tap */
	public double[] predictTrajectory(double xStart, double yStart, double xTap, double yTap) {

		// retrieve normal trajectory
		double[] normalTraj = this.nonTapMemoriser.predictTrajectory(xStart, yStart);
		double[] tapTraj = new double[parameter_length];
		if(isBlueBird) { tapTraj = new double[3*parameter_length]; }	// special case for blue bird
		for(int i=0; i<tapTraj.length; i++) {
			tapTraj[i] = Double.NaN;
		}
		double[] fullTraj = new double[normalTraj.length + tapTraj.length];
		for(int i=0; i<normalTraj.length; i++) {
			fullTraj[i] = normalTraj[i];
		}
		for(int i=0; i<tapTraj.length; i++) {
			fullTraj[normalTraj.length + i] = tapTraj[i];
		}
		
		Set<PointPlus> allPoints = allTapTrajectories.keySet();
		
		if(allPoints.size() == 0) {
			Debug.echo("ERROR in predictTrajectory(): No Tap Trajectory found!");
			return fullTraj;
		} else if(allPoints.size() == 1) {
			tapTraj = allTapTrajectories.get(allPoints.iterator().next()).getPara();
			for(int i=0; i<tapTraj.length; i++) {
				fullTraj[normalTraj.length + i] = tapTraj[i];
			}
			return fullTraj;
		}
		
		PointPlus point = new PointPlus(xStart, yStart, xTap, yTap, -1);
		
		if(allPoints.contains(point)) {
			tapTraj = allTapTrajectories.get(point).getPara();
			for(int i=0; i<tapTraj.length; i++) {
				fullTraj[normalTraj.length + i] = tapTraj[i];
			}
			return fullTraj;
		}

		// INTERPOLATION
		double minDistance1, minDistance2;
		minDistance1 = Double.POSITIVE_INFINITY;
		minDistance2 = Double.POSITIVE_INFINITY;
		PointPlus smallest1;
		PointPlus smallest2;
		smallest1 = null;
		smallest2 = null;
		
		// keep track of the points closest to required start point
		for(PointPlus key : allPoints) {
			TapTrajectory traj = allTapTrajectories.get(key);
			double trajXStart = key.getXStart();
			double trajYStart = key.getYStart();
			double trajXTap = key.getXTap();
			double trajYTap = key.getYTap();
			double tempDistance = distance(xStart, yStart, xTap, yTap, trajXStart, trajYStart, trajXTap, trajYTap);
			
			if((tempDistance < acceptPrecision)) {
				tapTraj = traj.getPara();
				for(int j=0; j<tapTraj.length; j++) {
					fullTraj[normalTraj.length + j] = tapTraj[j];
				}
				return fullTraj;
			} else if(tempDistance < minDistance1) {																// if it is the smallest, i.e minimum
				minDistance2 = minDistance1;
				smallest2 = smallest1;
				minDistance1 = tempDistance;
				smallest1 = key;
			} else if(tempDistance < minDistance2) {															// if it is the second smallest
				minDistance2 = tempDistance;
				smallest2 = key;																						// i.e. no changes to the minimum
			}		
		}
		

		// now we have 2 points correspond to the closest points to the required (input) start points, and we can interpolate
		tapTraj = interpolate(xStart, yStart, smallest1, allTapTrajectories.get(smallest1), smallest2, allTapTrajectories.get(smallest2));
		
		for(int i=0; i<tapTraj.length; i++) {
			fullTraj[normalTraj.length + i] = tapTraj[i];
		}
		
		return fullTraj;
	}
	
	/** simple interpolation */
	private double[] interpolate(double x0, double y0, PointPlus p1, TapTrajectory t1, PointPlus p2, TapTrajectory t2) {
		double x1 = p1.getXTap();
		double y1 = p1.getYTap();
		double x2 = p2.getXTap();
		double y2 = p2.getYTap();
		double[] para1 = t1.getPara();
		double[] para2 = t2.getPara();
		
		double[] paraEst = new double[para1.length];
		
		if(x2-x1 != 0) {
			for(int i=0; i<para1.length; i++) {
				paraEst[i] = para1[i] + (para2[i] - para1[i]) * (x0 - x1) / (x2 - x1);
			}
		} else if(y2-y1 != 0) {
			for(int i=0; i<para1.length; i++) {
				paraEst[i] = para1[i] + (para2[i] - para1[i]) * (y0 - y1) / (y2 - y1);
			}
		} else {
			paraEst = para1;
		}
		
		return paraEst;
	}
	
	
	/** return a predicted y location given required (input) start points, tap points and the respective x location. */
	public double predictPoint(double xStart, double yStart, double xTap, double yTap, double xPoint) {
		if(xPoint < xTap) {
			return nonTapMemoriser.predictPoint(xStart, yStart, xPoint);
		} else {
			double[] tempPara = predictTrajectory(xStart, yStart, xTap, yTap);
			double yPoint = tempPara[3] + tempPara[4] * xPoint + tempPara[5] * xPoint * xPoint;
			return yPoint;
		}
	}
	
	/** For blue bird: 
	 * return predicted y locations (in array) given required (input) start points, tap points and the respective x location. 
	 * Note: one value array is returned if xPoint is before xTap */
	public double[] predictMultiplePoint(double xStart, double yStart, double xTap, double yTap, double xPoint) {
		if(xPoint < xTap) {
			double pointBeforeTap = nonTapMemoriser.predictPoint(xStart, yStart, xPoint);
			return new double[]{pointBeforeTap};
		} else {
			double[] tempPara = predictTrajectory(xStart, yStart, xTap, yTap);
			if(tempPara.length <= 3) {
				Debug.echo("Error in predictPoint(): No Trajectory found!");
				return new double[]{Double.NaN};
			}
			double yPoint1 = tempPara[3] + tempPara[4] * xPoint + tempPara[5] * xPoint * xPoint;
			double yPoint2 = tempPara[6] + tempPara[7] * xPoint + tempPara[8] * xPoint * xPoint;
			double yPoint3 = tempPara[9] + tempPara[10] * xPoint + tempPara[11] * xPoint * xPoint;
			return new double[]{yPoint1, yPoint2, yPoint3};
		}
	}
	
	/** return the number of learned trajectories */
	public int get_m() {
		return allTapTrajectories.keySet().size();
	}
	
	/** return a learned trajectory stored at array index i */
	public TapTrajectory getTrajectory(PointPlus key) {
		return allTapTrajectories.get(key);
	}
	
	/** return all learned trajectories stored */
	public HashMap<PointPlus, TapTrajectory> getAllTrajectories() {
		return allTapTrajectories;
	}
	
	/** return the number of unstored trajectory points */
	public int get_n() {
		return n;
	}
	
	// =========================================================================================================================
	/**
	 *  Internal data structure for storing simple coordinate (Point) and Trajectory
	 */
	public static class TapTrajectory {
		private double[] para;			// 3 values array (9 values for blue bird)

		public TapTrajectory(double[] para) {
			this.para = para;
		}

		public double[] getPara() {
			return para;
		}

		@Override
		public String toString() {
			String str = "{" + para[0] + ", " + para[1] + ", " + para[2] + "}";
			if(para.length > 3) {
				str += "{" + para[3] + ", " + para[4] + ", " + para[5] + "}";
				str += "{" + para[6] + ", " + para[7] + ", " + para[8] + "}";
			}
			return str;
		}
	}

	public static class PointPlus implements Comparable<PointPlus> {
		private Double xStart;
		private Double yStart;
		private Double xTap;
		private Double yTap;
		private Double tapTime;

		public PointPlus(double xStart, double yStart, double xTap, double yTap, double tapTime) {
			this.xStart = xStart;
			this.yStart = yStart;
			this.xTap = xTap;
			this.yTap = yTap;
			this.tapTime = tapTime;
		}

		public double getXStart() {
			return xStart;
		}
		public double getYStart() {
			return yStart;
		}
		public double getXTap() {
			return xTap;
		}
		public double getYTap() {
			return yTap;
		}
		public double getTapTime() {
			return tapTime;
		}

		@Override
		public String toString() {
			String str = "(" + xStart + ", " + yStart + ", " + xStart + ", " + yTap + ", " + tapTime + ")";
			return str;
		}

		@Override
		public int compareTo(PointPlus obj) {
			if(xStart.compareTo(obj.getXStart()) != 0) {
				return xStart.compareTo(obj.getXStart());
			} else if(yStart.compareTo(obj.getYStart()) != 0) {
				return yStart.compareTo(obj.getYStart());
			} else if(xTap.compareTo(obj.getXTap()) != 0) {
				return xTap.compareTo(obj.getXTap());
			} else if(yTap.compareTo(obj.getYTap()) != 0) {
				return yTap.compareTo(obj.getYTap());
			} else {
				return tapTime.compareTo(obj.getTapTime());
			}
		}

		@Override
		public boolean equals(Object o) {
			PointPlus obj = (PointPlus) o;
			return xStart == (obj).getXStart() && yStart == (obj).getYStart()
					&& xTap == (obj).getXTap() && yTap == (obj).getYTap()
					&& tapTime == (obj).getTapTime();
		}

		@Override
		public int hashCode() {
			String str = xStart + "," + yStart + "," + xTap + "," + yTap + "," + tapTime;
			return str.hashCode();
		}
	}

	// =========================================================================================================================
	
	
	
	/** Under development */
	/** return a predicted trajectory (6 values array: 3 for normal trajectory, 3 for trajectory after tap) given xStart, yStart, tapTime */
	/** for blue bird: 12 values array: 3 for normal, 9 for after tap */
	public double[] predictTrajectory(double xStart, double yStart, double tapTime) {

		// retrieve normal trajectory
		double[] normalTraj = this.nonTapMemoriser.predictTrajectory(xStart, yStart);
		double[] tapTraj = new double[parameter_length];
		if(isBlueBird) { tapTraj = new double[3*parameter_length]; }	// special case for blue bird
		for(int i=0; i<tapTraj.length; i++) {
			tapTraj[i] = Double.NaN;
		}
		double[] fullTraj = new double[normalTraj.length + tapTraj.length];
		for(int i=0; i<normalTraj.length; i++) {
			fullTraj[i] = normalTraj[i];
		}
		for(int i=0; i<tapTraj.length; i++) {
			fullTraj[normalTraj.length + i] = tapTraj[i];
		}
		
		Set<PointPlus> allPoints = allTapTrajectories.keySet();
		
		if(allPoints.size() == 0) {
			Debug.echo("ERROR in predictTrajectory(): No Tap Trajectory found!");
			return fullTraj;
		} else if(allPoints.size() == 1) {
			tapTraj = allTapTrajectories.get(allPoints.iterator().next()).getPara();
			for(int i=0; i<tapTraj.length; i++) {
				fullTraj[normalTraj.length + i] = tapTraj[i];
			}
			return fullTraj;
		}
		
		PointPlus point = new PointPlus(xStart, yStart, -1, -1, tapTime);
		
		if(allPoints.contains(point)) {
			tapTraj = allTapTrajectories.get(point).getPara();
			for(int i=0; i<tapTraj.length; i++) {
				fullTraj[normalTraj.length + i] = tapTraj[i];
			}
			return fullTraj;
		}

		// INTERPOLATION
		double minDistance1, minDistance2;
		minDistance1 = Double.POSITIVE_INFINITY;
		minDistance2 = Double.POSITIVE_INFINITY;
		PointPlus smallest1;
		PointPlus smallest2;
		smallest1 = null;
		smallest2 = null;
		
		// keep track of the points closest to required start point
		for(PointPlus key : allPoints) {
			TapTrajectory traj = allTapTrajectories.get(key);
			double trajXStart = key.getXStart();
			double trajYStart = key.getYStart();
//			double trajXTap = key.getXTap();
//			double trajYTap = key.getYTap();
			double trajTapTime = key.getTapTime();
			double tempDistance = distance(xStart, yStart, tapTime, 0, trajXStart, trajYStart, trajTapTime, 0);
			
			if((tempDistance < acceptPrecision)) {
				tapTraj = traj.getPara();
				for(int j=0; j<tapTraj.length; j++) {
					fullTraj[normalTraj.length + j] = tapTraj[j];
				}
				return fullTraj;
			} else if(tempDistance < minDistance1) {																// if it is the smallest, i.e minimum
				minDistance2 = minDistance1;
				smallest2 = smallest1;
				minDistance1 = tempDistance;
				smallest1 = key;
			} else if(tempDistance < minDistance2) {															// if it is the second smallest
				minDistance2 = tempDistance;
				smallest2 = key;																						// i.e. no changes to the minimum
			}		
		}
		

		// now we have 2 points correspond to the closest points to the required (input) start points, and we can interpolate
		tapTraj = interpolate(xStart, yStart, smallest1, allTapTrajectories.get(smallest1), smallest2, allTapTrajectories.get(smallest2));
		
		for(int i=0; i<tapTraj.length; i++) {
			fullTraj[normalTraj.length + i] = tapTraj[i];
		}
		
		return fullTraj;
	}
	
	/** return a predicted y location given required (input) start points, tap points and the respective x location. */
	public double predictPoint(double xStart, double yStart, double tapTime, double xPoint) {
		double xTapEstimate = tapTime * this.xTaptoTapTimeRatio;
		if(xPoint < xTapEstimate) {
			return nonTapMemoriser.predictPoint(xStart, yStart, xPoint);
		} else {
			double[] tempPara = predictTrajectory(xStart, yStart, tapTime);
			double yPoint = tempPara[3] + tempPara[4] * xPoint + tempPara[5] * xPoint * xPoint;
			return yPoint;
		}
	}
}
