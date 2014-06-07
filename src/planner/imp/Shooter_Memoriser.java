/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package planner.imp;

import java.util.List;
import java.util.Random;

import exception.plan.ExNoPlanResult;

import planner.TrajectoryPlanner;
import KB.TrajectoryModel;
import KB.object.bird.Plain_Bird;
import learner.util.Trajectory_Memoriser;
import learner.util.Trajectory_Memoriser.Point;
import learner.util.Trajectory_Memoriser.Trajectory;
import representation.APoint;

/**
 * Plan the required starting point/s (xStart, yStart) needed to hit a target, given certain
 * constraints, under a trained trajectories-memorise model.
 * 
 * Model: Trajectory_Memoriser
 *   
 * Input: A trained Trajectory_Memoriser learner (learnedModel)
 * 		  Hitting target (xTarget, yTarget)
 * 
 * Optional Input: Required gradient when hitting target (targetGradient)
 * 				   Constraint point to go through (xConstraint, yConstraint)
 * 
 * Output: Starting point/s to shoot the target (as APoint or APoint[])
 * 		   Return null if there is no solution (NOTE: suggestion needed)
 * 
 * 
 * @author Kar Wai Lim
 * 
 * Last updated: 21 Nov 2012
 * 
 */

public class Shooter_Memoriser implements TrajectoryPlanner<Plain_Bird> {

	// TODO: Tweak the default value
	
	/** Modifiable variables */
	private double pointAcceptanceTolerance = 15;			// to what extent to deem two points are not too different (based on y distance)
	private double gradientAcceptanceTolerance = 1.5;		// to what extent gradient can be differ from the required gradient

	private Trajectory_Memoriser learnedModel;

	
	/** Constructor
	 * @param learnedModel = a model trained under Trajectory_Memoriser
	 */
	public Shooter_Memoriser(Trajectory_Memoriser learnedModel) {
		this.learnedModel = learnedModel;
	}
	
	// =========================================================================================================================
	// 										Functions to change variables 
	// =========================================================================================================================
	
	/** Adjust the class parameter 'pointAcceptanceTolerance' which is the
	 *  y-distance to deem two points are not too different.
	 *  
	 * @param pointAcceptanceTolerance = new value for 'pointAcceptanceTolerance'
	 */
	public void changePointTolerance(double pointAcceptanceTolerance) {
		this.pointAcceptanceTolerance = pointAcceptanceTolerance;
	}
	
	/** Adjust the class parameter 'gradientAcceptanceTolerance' which is the 
	 *  accepted difference in gradient upon hitting target
	 *  
	 * @param gradientAcceptanceTolerance = new value for 'gradientAcceptanceTolerance'
	 */
	public void changeGradientTolerance(double gradientAcceptanceTolerance) {
		this.gradientAcceptanceTolerance = gradientAcceptanceTolerance;
	}

	
	// =========================================================================================================================
	// 									Sub function used in other methods 
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
	// 			Return an array of starting points (xStart, yStart) given a target (xTarget, yTarget) and some constraint. 
	// =========================================================================================================================
	
	/** Get all starting points to hit (xTarget, yTarget)
	 * 
	 * @param xTarget
	 * @param yTarget
	 * @return all solutions in an array of APoint
	 */
	public APoint[] getResults(double xTarget, double yTarget) {
		return getResults(xTarget, yTarget, false, 0, false, 0, 0);
	}
	
	/** Get all starting points to hit (xTarget, yTarget) with gradient = targetGradient
	 * 
	 * @param xTarget
	 * @param yTarget
	 * @param targetGradient
	 * @return all solutions in an array of APoint
	 */
	public APoint[] getResults(double xTarget, double yTarget, double targetGradient) {
		return getResults(xTarget, yTarget, true, targetGradient, false, 0, 0);
	}
	public APoint[] getResults(double xTarget, double yTarget, boolean boolGradient, double targetGradient) {
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
	public APoint[] getResults(double xTarget, double yTarget, double xConstraint, double yConstraint) {
		return getResults(xTarget, yTarget, false, 0, true, xConstraint, yConstraint);
	}
	public APoint[] getResults(double xTarget, double yTarget, boolean boolConstraint, double xConstraint, double yConstraint) {
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
	public APoint[] getResults(double xTarget, double yTarget, double targetGradient, double xConstraint, double yConstraint) {
		return getResults(xTarget, yTarget, true, targetGradient, true, xConstraint, yConstraint);
	}
	public APoint[] getResults(double xTarget, double yTarget, boolean boolGradient, double targetGradient, boolean boolConstraint, double xConstraint, double yConstraint) {
		int m = learnedModel.get_m();
		APoint[] solutions = new APoint[m];

		int k = 0;		// counter for number of accepted solutions
		
		double yDiffSmallest1 = Double.POSITIVE_INFINITY;
		double yDiffSmallest2 = Double.POSITIVE_INFINITY;
		double ySmallestPred1 = 0;
		double ySmallestPred2 = 0;
		Point index1 = new Point(-1, -1);
		Point index2 = new Point(-1, -1);
		
		for(Point key : learnedModel.getAllTrajectories().keySet()) {
			Trajectory traj = learnedModel.getTrajectory(key);
			double[] para = traj.getPara();

			double yPred = para[0] + para[1]*xTarget + para[2]*xTarget*xTarget;
			double yDiff = Math.abs(yTarget - yPred);
			if(yDiff < yDiffSmallest1) {
				yDiffSmallest2 = yDiffSmallest1;
				yDiffSmallest1 = yDiff;
				index2 = index1;
				index1 = key;
				ySmallestPred2 = ySmallestPred1;
				ySmallestPred1 = yPred;
			} else if(yDiff < yDiffSmallest2) {
				yDiffSmallest2 = yDiff;
				index2 = key;
				ySmallestPred2 = yPred;
			}
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
								solutions[k] = new APoint(xStart, yStart);
								k++;
							}
						} else {
							solutions[k] = new APoint(xStart, yStart);
							k++;
						}
					}
				} else {

					if(boolConstraint) {
						double yPred2 = para[0] + para[1]*xConstraint + para[2]*xConstraint*xConstraint;;
						double yDiff2 = Math.abs(yPred2 - yConstraint);
						if(yDiff2 <= pointAcceptanceTolerance) {
							solutions[k] = new APoint(xStart, yStart);
							k++;
						}
					} else {
						solutions[k] = new APoint(xStart, yStart);
						k++;
					}
				}
			}
		}

		if(k == 0) {
			if(!boolGradient && !boolConstraint) {
				double xStart1 = index1.getXStart();
				double xStart2 = index2.getXStart();
				double yStart1 = index1.getYStart();
				double yStart2 = index2.getYStart();
				double xStart, yStart;

				if(ySmallestPred2-ySmallestPred1 != 0) {
					xStart = xStart1 + (xStart2 - xStart1) * (yTarget - ySmallestPred1) / (ySmallestPred2 - ySmallestPred1);
					yStart = yStart1 + (yStart2 - yStart1) * (yTarget - ySmallestPred1) / (ySmallestPred2 - ySmallestPred1);
				} else {
					xStart = xStart1;
					yStart = yStart1;
				}
				solutions[k] = new APoint(xStart, yStart);
				k++;
			} else {

				System.out.println("There is no solution given the constraints!");
			}
		}

		// truncate solutions array
		APoint[] tempSolutions = solutions;
		solutions = new APoint[k];
		System.arraycopy(tempSolutions, 0, solutions, 0, k);
		return solutions;
	}

	// =========================================================================================================================
	// 					From the array of starting points solutions, choose one of the starting point randomly. 
	// =========================================================================================================================

	/** Get a random starting points to hit (xTarget, yTarget)
	 * 
	 * @param xTarget
	 * @param yTarget
	 * @return a random solution as APoint or null if no solution
	 */
	@Override
	public APoint getResult(double xTarget, double yTarget) {
		return getResult(xTarget, yTarget, false, 0, false, 0, 0);
	}

	/** Get a random starting points to hit (xTarget, yTarget) with gradient = targetGradient
	 * 
	 * @param xTarget
	 * @param yTarget
	 * @param targetGradient
	 * @return a random solution as APoint or null if no solution
	 */
	public APoint getResult(double xTarget, double yTarget, double targetGradient) {
		return getResult(xTarget, yTarget, true, targetGradient, false, 0, 0);
	}
	public APoint getResult(double xTarget, double yTarget, boolean boolGradient, double targetGradient) {
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
	public APoint getResult(double xTarget, double yTarget, double xConstraint, double yConstraint) {
		return getResult(xTarget, yTarget, false, 0, true, xConstraint, yConstraint);
	}
	public APoint getResult(double xTarget, double yTarget, boolean boolConstraint, double xConstraint, double yConstraint) {
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
	public APoint getResult(double xTarget, double yTarget, double targetGradient, double xConstraint, double yConstraint) {
		return getResult(xTarget, yTarget, true, targetGradient, true, xConstraint, yConstraint);
	}
	public APoint getResult(double xTarget, double yTarget, boolean boolGradient, double targetGradient, boolean boolConstraint, double xConstraint, double yConstraint) {
		APoint[] solutions = getResults(xTarget, yTarget, boolGradient, targetGradient, boolConstraint, xConstraint, yConstraint);		

		int m = solutions.length;
		if(m == 0) {		// no solutions!
			return null;
		}
		// return 1 solution randomly
		Random rand = new Random();
		int randomInt = rand.nextInt(m);
		return solutions[randomInt];
	}
	// =========================================================================================================================

	
	
	
	
	// unused
	
	@Override
	public void loadModel(TrajectoryModel<Plain_Bird> model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadModel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public APoint getResult(APoint target, TrajectoryModel<Plain_Bird> kbt)
			throws ExNoPlanResult {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<APoint> getResults(APoint target,
			TrajectoryModel<Plain_Bird> kbt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public APoint getEstimatedResults(APoint target,
			TrajectoryModel<Plain_Bird> kbt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public APoint getResult(APoint target) throws ExNoPlanResult {
		// TODO Auto-generated method stub
		return null;
	}
}
