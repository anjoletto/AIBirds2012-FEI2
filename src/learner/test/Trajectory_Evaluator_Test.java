/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package learner.test;

import learner.util.Trajectory_Evaluator;
import learner.util.Trajectory_Memoriser;


public class Trajectory_Evaluator_Test {

	private static final int traj_length = 100;

	/**
	 * Test class for Trajectory_Evaluator
	 * 
	 * @author Kar Wai Lim
	 * 
	 * Last updated: Long time ago
	 */

	// TODO? Formal testing
	
	public static void main(String[] args) {
		
		// =========================================================================================================================
		// ===== For Trajectory_Regression learner =================================================================================
		// =========================================================================================================================

		int paraLength = 10;
		double xStart = 5.0;
		double yStart = 10.0;

		double[] para = new double[paraLength];
		for(int i=0; i<paraLength; i++) {
			para[i] = i;
		}

		// ==== Test Constructor ======================================
		Trajectory_Evaluator trajEval = new Trajectory_Evaluator(para);

		assert(trajEval.get_n() == 0);
		Double aveError = trajEval.getAveError();
		assert(aveError.equals(Double.NaN));
		assert(trajEval.getSqErrors() == 0);
		assert(trajEval.getModel() == 1);


		// ============================================================
		trajEval.addStartPoint(xStart, yStart);

		double xPoint = 3.0;
		//		double yPoint = trajEval.getYpoint(xPoint);

		double yTemp = para[0]*yStart + para[1]*xStart + para[2]*(yStart/xStart)*xPoint + para[3]*(yStart*yStart/xStart)*xPoint;
		yTemp += para[4]*yStart*xPoint + para[5]*xStart*xPoint + para[6]*yStart*xStart*xPoint + para[7]*xPoint*xPoint/(xStart*xStart);
		yTemp += para[8]*xPoint*xPoint/(yStart*yStart) + para[9]*xPoint*xPoint/(yStart*xStart);
		double yPoint = yTemp;

		System.out.println(xPoint + ", " + yPoint);

		trajEval.addTrajectoryPoint(xPoint, yPoint);
		int n = trajEval.get_n();
		double sqerrors = trajEval.getSqErrors();

		System.out.println(n);								// should be 1
		System.out.println(sqerrors);						// should be 0

		trajEval.addTrajectoryPoint(xPoint, yPoint+1);
		n = trajEval.get_n();
		sqerrors = trajEval.getSqErrors();

		System.out.println(n);								// should be 2
		System.out.println(sqerrors);						// should be 1

		trajEval.addTrajectoryPoint(xPoint, yPoint+2);
		n = trajEval.get_n();
		sqerrors = trajEval.getSqErrors();

		System.out.println(n);								// should be 3
		System.out.println(sqerrors);						// should be 5

		System.out.println(trajEval.toString());


		// =========================================================================================================================
		// ===== For Trajectory_Memoriser learner ==================================================================================
		// =========================================================================================================================

		// initialise Trajectory_Memoriser
		Trajectory_Memoriser trajMemoLearner = new Trajectory_Memoriser();

		assert(trajMemoLearner.get_m() == 0);
		for(int i=0; i<traj_length; i++) {
		}

		// add trivial start points and trajectories points
		for(int i=1; i<=100; i++) {
			for(int j=1; j<=100; j++) {
				trajMemoLearner.addStartPoint(i, j);
				for(int k=1; k<=100; k++) {
					trajMemoLearner.addTrajectoryPoint(k, i + j*k + i*j*k*k);
				}
				trajMemoLearner.storePara();
			}
		}

		Trajectory_Evaluator trajEval2 = new Trajectory_Evaluator(trajMemoLearner);

		for(int i=1; i<=100; i++) {
			for(int j=1; j<=100; j++) {
				trajEval2.addStartPoint(i, j);
				for(int k=1; k<=100; k++) {
					trajEval2.addTrajectoryPoint(k, i + j*k + i*j*k*k);
				}
			}
		}

		System.out.println("====================================================");
		System.out.println(trajEval2.get_n());
		System.out.println(trajEval2.getSqErrors());	// should be close to no error
		System.out.println(trajEval2.getAveError());
	}
}
