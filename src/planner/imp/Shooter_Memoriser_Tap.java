/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package planner.imp;

import java.util.HashSet;
import java.util.Random;

import learner.util.Trajectory_Memoriser.Point;
import learner.util.Trajectory_Memoriser.Trajectory;
import learner.util.Trajectory_Memoriser_Tap;
import learner.util.Trajectory_Memoriser_Tap.PointPlus;
import learner.util.Trajectory_Memoriser_Tap.TapTrajectory;
import representation.APoint;

/**
 * Plan the required starting point/s (xStart, yStart) needed to hit a target, given certain
 * constraints, under a trained trajectories-memorise model.
 * 
 * Model: Trajectory_Memoriser_Tap
 *   
 * Input: A trained Trajectory_Memoriser_Tap learner (learnedModel)
 * 		  Hitting target (xTarget, yTarget)
 * 
 * Optional Input: Required gradient when hitting target (targetGradient)
 * 				   Constraint point to go through (xConstraint, yConstraint)
 * 
 * Output: Starting point/s to shoot the target (as DecisionPoint or HashSet<DecisionPoint>)
 * 		   Return null if there is no solution (NOTE: suggestion needed)
 * 
 * 
 * @author Kar Wai Lim
 * 
 * Last updated: 21 Nov 2012
 */

public class Shooter_Memoriser_Tap {

	// TODO: Tweak the default value
	
	private double pointAcceptanceTolerance = 5;			// to what extent to deem two points are not too different (based on y distance)
	private double gradientAcceptanceTolerance = 1.5;		// to what extent gradient can be differ from the required gradient

	private Trajectory_Memoriser_Tap learnedModel;

	/** Constructor
	 * @param learnedModel = a model trained under Trajectory_Memoriser
	 */
	public Shooter_Memoriser_Tap(Trajectory_Memoriser_Tap learnedModel) {
		this.learnedModel = learnedModel;
	}

	
	// =========================================================================================================================
	// ========================== Functions to tweak default utility parameters ================================================
	// =========================================================================================================================
	
	/** Adjust the class parameter 'pointAcceptanceTolerance' which is the normalised
	 *  y-direction distance to deem two points are not too different
	 *  
	 * @param tolerance = new value for 'pointAcceptanceTolerance'
	 */
	public void changePointTolerance(double tolerance) {
		pointAcceptanceTolerance = tolerance;
	}
	
	/** Adjust the class parameter 'gradientAcceptanceTolerance' which is the accepted
	 *  difference in gradient upon hitting target
	 *  
	 * @param tolerance = new value for 'gradientAcceptanceTolerance'
	 */
	public void changeGradientTolerance(double tolerance) {
		gradientAcceptanceTolerance = tolerance;
	}

	
	// =========================================================================================================================
	// =============================== Sub function used in other methods ======================================================
	// =========================================================================================================================

	/** Calculate gradient at any x-location of a trajectory given the
	 *  trajectory (quadratic equation) parameters
	 *  
	 * @param xPoint = x-location to get gradient
	 * @param para = parameters of trajectory (in an array of 3 values)
	 * @return gradient at corresponding x-location
	 */
	public double getGradient(double xPoint, double[] para) {
		double b = para[1];
		double c = para[2];
		double grad = b + 2*c*xPoint;
		return grad;
	}
	
	
	// =========================================================================================================================
	// ======= Return an array of starting points (xStart, yStart) given a target (xTarget, yTarget) and some constraint. ======
	// =========================================================================================================================
	
	/** Get all starting points to hit (xTarget, yTarget)
	 * 
	 * @param xTarget
	 * @param yTarget
	 * @return all solutions in an array of APoint
	 */
	public HashSet<DecisionPoint> getResults(double xTarget, double yTarget) {
		return getResults(xTarget, yTarget, false, 0, false, 0, 0);
	}
	
	/** Get all starting points to hit (xTarget, yTarget) with gradient = targetGradient
	 * 
	 * @param xTarget
	 * @param yTarget
	 * @param targetGradient
	 * @return all solutions in an array of APoint
	 */
	public HashSet<DecisionPoint> getResults(double xTarget, double yTarget, double targetGradient) {
		return getResults(xTarget, yTarget, true, targetGradient, false, 0, 0);
	}
	public HashSet<DecisionPoint> getResults(double xTarget, double yTarget, boolean boolGradient, double targetGradient) {
		return getResults(xTarget, yTarget, boolGradient, targetGradient, false, 0, 0);
	}

	/** Get all starting points to hit (xTarget, yTarget) and to pass by point (xConstraint, yConstraint)
	 * 
	 * @param xTarget
	 * @param yTarget
	 * @param xConstraint
	 * @param yConstraint
	 * @return all solutions in an array of APoint
	 */
	public HashSet<DecisionPoint> getResults(double xTarget, double yTarget, double xConstraint, double yConstraint) {
		return getResults(xTarget, yTarget, false, 0, true, xConstraint, yConstraint);
	}
	public HashSet<DecisionPoint> getResults(double xTarget, double yTarget, boolean boolConstraint, double xConstraint, double yConstraint) {
		return getResults(xTarget, yTarget, false, 0, boolConstraint, xConstraint, yConstraint);
	}

	/** Get all starting points to hit (xTarget, yTarget) with gradient = targetGradient and to pass by point (xConstraint, yConstraint)
	 * 
	 * @param xTarget
	 * @param yTarget
	 * @param targetGradient
	 * @param xConstraint
	 * @param yConstraint
	 * @return all solutions in an array of APoint
	 */
	public HashSet<DecisionPoint> getResults(double xTarget, double yTarget, double targetGradient, double xConstraint, double yConstraint) {
		return getResults(xTarget, yTarget, true, targetGradient, true, xConstraint, yConstraint);
	}
	public HashSet<DecisionPoint> getResults(double xTarget, double yTarget, boolean boolGradient, double targetGradient, boolean boolConstraint, double xConstraint, double yConstraint) {
		
		HashSet<DecisionPoint> solutions = new HashSet<DecisionPoint>();

		int k = 0;		// counter for number of accepted solutions
		
		for(Point key : learnedModel.nonTapMemoriser.getAllTrajectories().keySet()) {

			Trajectory traj = learnedModel.nonTapMemoriser.getTrajectory(key);
			double[] para = traj.getPara();

			double yPred = para[0] + para[1]*xTarget + para[2]*xTarget*xTarget;
			double yDiff = Math.abs(yTarget - yPred);
			// accept if the predicted y is not too different from yTarget
			if(yDiff <= pointAcceptanceTolerance) {
				double xStart = key.getXStart();
				double yStart = key.getYStart();

				if(boolGradient) {								// to check gradient or not?
					double gradient = this.getGradient(xTarget, para);
					if(Math.abs(gradient - targetGradient) <= gradientAcceptanceTolerance) {

						if(boolConstraint) {
							double yPred2 = para[0] + para[1]*xConstraint + para[2]*xConstraint*xConstraint;;
							double yDiff2 = Math.abs(yPred2 - yConstraint);
							if(yDiff2 <= pointAcceptanceTolerance) {
								solutions.add(new DecisionPoint(xStart, yStart, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
								k++;
							}
						} else {
							solutions.add(new DecisionPoint(xStart, yStart, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
							k++;
						}
					}
				} else {

					if(boolConstraint) {
						double yPred2 = para[0] + para[1]*xConstraint + para[2]*xConstraint*xConstraint;;
						double yDiff2 = Math.abs(yPred2 - yConstraint);
						if(yDiff2 <= pointAcceptanceTolerance) {
							solutions.add(new DecisionPoint(xStart, yStart, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
							k++;
						}
					} else {
						solutions.add(new DecisionPoint(xStart, yStart, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
						k++;
					}
				}
			}
		}
		
		for(PointPlus key : learnedModel.getAllTrajectories().keySet()) {

			TapTrajectory traj = learnedModel.getTrajectory(key);
			double[] para = traj.getPara();

			double yPred = para[0] + para[1]*xTarget + para[2]*xTarget*xTarget;
			double yDiff = Math.abs(yTarget - yPred);
			// accept if the predicted y is not too different from yTarget
			if(yDiff <= pointAcceptanceTolerance) {
				double xStart = key.getXStart();
				double yStart = key.getYStart();
				double xTap = key.getXTap();
				double yTap = key.getYTap();
				double tapTime = key.getTapTime();

				if(boolGradient) {								// to check gradient or not?
					double gradient = this.getGradient(xTarget, para);
					if(Math.abs(gradient - targetGradient) <= gradientAcceptanceTolerance) {

						if(boolConstraint) {
							double yPred2 = para[0] + para[1]*xConstraint + para[2]*xConstraint*xConstraint;;
							double yDiff2 = Math.abs(yPred2 - yConstraint);
							if(yDiff2 <= pointAcceptanceTolerance) {
								if(xTap > xTarget)
									solutions.add(new DecisionPoint(xStart, yStart, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
								else
									solutions.add(new DecisionPoint(xStart, yStart, xTap, yTap, tapTime));
								k++;
							}
						} else {
							if(xTap > xTarget)
								solutions.add(new DecisionPoint(xStart, yStart, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
							else
								solutions.add(new DecisionPoint(xStart, yStart, xTap, yTap, tapTime));
							k++;
						}
					}
				} else {

					if(boolConstraint) {
						double yPred2 = para[0] + para[1]*xConstraint + para[2]*xConstraint*xConstraint;;
						double yDiff2 = Math.abs(yPred2 - yConstraint);
						if(yDiff2 <= pointAcceptanceTolerance) {
							if(xTap > xTarget)
								solutions.add(new DecisionPoint(xStart, yStart, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
							else
								solutions.add(new DecisionPoint(xStart, yStart, xTap, yTap, tapTime));
							k++;
						}
					} else {
						if(xTap > xTarget)
							solutions.add(new DecisionPoint(xStart, yStart, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
						else
							solutions.add(new DecisionPoint(xStart, yStart, xTap, yTap, tapTime));
						k++;
					}
				}
			}
		}

		if(k == 0) {
			System.out.println("There is no solution given the constraints!");
		}

		return solutions;
	}

	// =========================================================================================================================
	// ============== From the array of starting points, choose one of the starting point randomly. ============================
	// =========================================================================================================================

	/** Get a random starting points to hit (xTarget, yTarget)
	 * 
	 * @param xTarget
	 * @param yTarget
	 * @return a random solution as APoint or null if no solution
	 */
	public DecisionPoint getResult(double xTarget, double yTarget) {
		return getResult(xTarget, yTarget, false, 0, false, 0, 0);
	}

	/** Get a random starting points to hit (xTarget, yTarget) with gradient = targetGradient
	 * 
	 * @param xTarget
	 * @param yTarget
	 * @param targetGradient
	 * @return a random solution as APoint or null if no solution
	 */
	public DecisionPoint getResult(double xTarget, double yTarget, double targetGradient) {
		return getResult(xTarget, yTarget, true, targetGradient, false, 0, 0);
	}
	public DecisionPoint getResult(double xTarget, double yTarget, boolean boolGradient, double targetGradient) {
		return getResult(xTarget, yTarget, boolGradient, targetGradient, false, 0, 0);
	}

	/** Get a random starting points to hit (xTarget, yTarget) and to pass by point (xConstraint, yConstraint)
	 *
	 * @param xTarget
	 * @param yTarget
	 * @param xConstraint
	 * @param yConstraint
	 * @return a random solution as APoint or null if no solution
	 */
	public DecisionPoint getResult(double xTarget, double yTarget, double xConstraint, double yConstraint) {
		return getResult(xTarget, yTarget, false, 0, true, xConstraint, yConstraint);
	}
	public DecisionPoint getResult(double xTarget, double yTarget, boolean boolConstraint, double xConstraint, double yConstraint) {
		return getResult(xTarget, yTarget, false, 0, boolConstraint, xConstraint, yConstraint);
	}

	/** Get a random starting points to hit (xTarget, yTarget) with gradient = targetGradient and to pass by point (xConstraint, yConstraint)
	 * 
	 * @param xTarget
	 * @param yTarget
	 * @param targetGradient
	 * @param xConstraint
	 * @param yConstraint
	 * @return a random solution as APoint or null if no solution
	 */
	public DecisionPoint getResult(double xTarget, double yTarget, double targetGradient, double xConstraint, double yConstraint) {
		return getResult(xTarget, yTarget, true, targetGradient, true, xConstraint, yConstraint);
	}
	public DecisionPoint getResult(double xTarget, double yTarget, boolean boolGradient, double targetGradient, boolean boolConstraint, double xConstraint, double yConstraint) {
		HashSet<DecisionPoint> solutions = getResults(xTarget, yTarget, boolGradient, targetGradient, boolConstraint, xConstraint, yConstraint);		

		int m = solutions.size();
		if(m == 0) {		// no solutions!
			return null;
		}
		// return 1 solution randomly
		Random rand = new Random();
		int randomInt = rand.nextInt(m);
		return (DecisionPoint) solutions.toArray()[randomInt];
	}
	// =========================================================================================================================
	
	/**
	 * Decision Point gives 4 values correspond to xStart, yStart, xTap, yTap
	 */
	public class DecisionPoint {
		private Double xStart;
		private Double yStart;
		private Double xTap;
		private Double yTap;
		private Double tapTime;
		
		public DecisionPoint(double xStart, double yStart, double xTap, double yTap, double tapTime) {
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
		
		public APoint getStartPoint() {
			return new APoint(xStart, yStart);
		}
		
		public APoint getTapPoint() {
			return new APoint(xTap, yTap);
		}
		
		@Override
		public String toString() {
			String str = "(" + xStart + ", " + yStart + ", " + xTap + ", " + yTap + ", " + tapTime + ")";
			return str;
		}
		
		@Override
		public boolean equals(Object obj) {
			DecisionPoint o = (DecisionPoint) obj;
			return (xStart == o.getXStart() && yStart == o.getYStart() && xTap == o.getXTap() && yTap == o.getYTap() && tapTime == o.getTapTime());
		}
		
		@Override
		public int hashCode() {
			String str = xStart + "," + yStart + "," + xTap + "," + yTap + "," + tapTime;
			return str.hashCode();
		}
		
	}
	
}
