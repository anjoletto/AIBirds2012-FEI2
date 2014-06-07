/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package learner.test;

import static org.junit.Assert.assertEquals;
import learner.util.Trajectory_Memoriser_Tap;
import static org.junit.Assert.assertTrue;
import learner.util.Trajectory_Memoriser_Tap.PointPlus;
import learner.util.Trajectory_Memoriser_Tap.TapTrajectory;

public class Trajectory_Memoriser_Tap_Test {

	/**
	 * Test class for Trajectory_Memoriser_Tap
	 * 
	 * Not fully completed
	 * @param args
	 * 
	 * Note: We need at least 3 points for each trajectory
	 * 
	 * Last updated: 21 Nov 2012
	 */
	public static void main(String[] args) {
		// TODO modify this to perform proper test

		// =========================================================================================================================
		// ============ Simple tutorial (the values are chosen arbitrarily) ========================================================
		// =========================================================================================================================

		// initialise the learner
		Trajectory_Memoriser_Tap trajMemoLearner = new Trajectory_Memoriser_Tap();

		// for each trajectory (each bird throw), add starting points and trajectory points
		trajMemoLearner.addStartPoint(-48.3, -55.7);
		trajMemoLearner.addTapPoint(45.9, 88.9, 5.0);	// xTap, yTap and tapTime

		trajMemoLearner.addTrajectoryPoint(0.7, 13.0);
		trajMemoLearner.addTrajectoryPoint(26.9, 77.0);
		trajMemoLearner.addTrajectoryPoint(37.5, 99.1);
		trajMemoLearner.addTrajectoryPoint(105.0, 256.0);
		trajMemoLearner.addTrajectoryPoint(271.4, 155.7);
		trajMemoLearner.addTrajectoryPoint(361.8, 175.7);

		// store the trajectory as trained trajectory parameters
		trajMemoLearner.storePara();

		// display the first learned trajectory
		TapTrajectory traj1 = trajMemoLearner.getTrajectory(new PointPlus(-48.3, -55.7, 45.9, 88.9, 5.0));
		System.out.println(traj1);
		double xStart1 = -48.3;
		double yStart1 = -55.7;
		double[] para1 = traj1.getPara();
		System.out.println(xStart1 + ", " + yStart1);
		System.out.println(para1[0] + ", " + para1[1] + ", " + para1[2]);
		
		// ... and repeat for other trajectories
		trajMemoLearner.addStartPoint(-44.3, -66.7);
		trajMemoLearner.addTapPoint(45.9, 88.9, 5.0);

		trajMemoLearner.addTrajectoryPoint(0.7, 13.0);
		trajMemoLearner.addTrajectoryPoint(20.3, 66.0);
		trajMemoLearner.addTrajectoryPoint(30.3, 86.0);
		trajMemoLearner.addTrajectoryPoint(77.1, 205.8);
		trajMemoLearner.addTrajectoryPoint(110.0, 244.6);
		trajMemoLearner.addTrajectoryPoint(124.5, 233.7);
		trajMemoLearner.addTrajectoryPoint(174.6, 185.7);
		trajMemoLearner.addTrajectoryPoint(361.8, 175.7);

		trajMemoLearner.storePara();

		// predict trajectory given start points
		double[] para = trajMemoLearner.predictTrajectory(-42.0, -99.0, 43.9, 86.9);
		System.out.println(para.length);
		System.out.println(para[0] + ", " + para[1] + ", " + para[2]);
		System.out.println(para[3] + ", " + para[4] + ", " + para[5]);

		// predict corresponding y-location given a x-location of a certain point on the
		// trajectory and start points
		double yPoint = trajMemoLearner.predictPoint(-33.6, -77.8, 59.0, 99.9, 62.1);
		System.out.println(yPoint);



		// =========================================================================================================================
		// ======================== Testing ========================================================================================
		// =========================================================================================================================

		double xStart = 5.0;
		double yStart = 7.0;
		double xTap = 30.0;
		double yTap = xStart + yStart*xTap + xStart*yStart*xTap*xTap;
		double xPoint = 65.0;

		// initialise Trajectory_Memoriser
		Trajectory_Memoriser_Tap learner = new Trajectory_Memoriser_Tap();

		assertEquals(learner.get_m(), 0);
		assertEquals(learner.predictTrajectory(xStart, yStart, xTap, yTap).length, 6);
		Double yPred = learner.predictPoint(xStart, yStart, xTap, yTap, xPoint);
		assertTrue(yPred.equals(Double.NaN));

		// add trivial start points and trajectories points
		for(int i=1; i<=100; i++) {
			for(int j=1; j<=100; j++) {
				for(int t=1; t<5; t++) {
					learner.addStartPoint(i, j);
					learner.addTapPoint(10*t, i + 10*j*t + 100*i*j*t*t, i*i*j*j*t*t);
					int count = 0;
					for(int k=1; k<=100; k++) {
						if(k >= 10*t) { count++; }
						if(k < 10*t)
							learner.addTrajectoryPoint(k, i + j*k + i*j*k*k);
						if(k >= 10*t)
							learner.addTrajectoryPoint(k, j + i*k + i*j*k*k);
						assertEquals(learner.get_n(), count);
					}
					learner.storePara();
//				assertEquals(learner.get_m(), (i-1)*100+j);
					assertEquals(learner.get_n(), 0);
				
				// test storing correctness
//				TrajectoryTap trajTest = learner.getTrajectory((i-1)*100+j-1);
//				double[] paraTest = trajTest.getPara();
//				assertEquals(trajTest.getXStart(), i, 0.0001);
//				assertEquals(trajTest.getYStart(), j, 0.0001);
//				assertEquals(paraTest[0], i, 0.0001);
//				assertEquals(paraTest[1], j, 0.0001);
//				assertEquals(paraTest[2], i*j, 0.0001);
				}
			}
		}
		
		

		// verify correctness in predicting trajectories and predicting points 
		for(int i=1; i<=100; i++) {
			for(int j=1; j<=100; j++) {
				for(int t=1; t<5; t++) {
					double[] paraTest = learner.predictTrajectory(i, j, 10*t, i + 10*j*t + 100*i*j*t*t);
					assertEquals(paraTest[0], i, 0.0001);
					assertEquals(paraTest[1], j, 0.0001);
					assertEquals(paraTest[2], i*j, 0.0001);
					assertEquals(paraTest[3], j, 0.0001);
					assertEquals(paraTest[4], i, 0.0001);
					assertEquals(paraTest[5], i*j, 0.0001);
					
					for(int k=1; k<=100; k++) {
						double yPredTest = learner.predictPoint(i, j, 10*t, i + 10*j*t + 100*i*j*t*t, k);
						if(k < 10*t)
							assertEquals(yPredTest, i + j*k + i*j*k*k, 0.0001);
						if(k >= 10*t)
							assertEquals(yPredTest, j + i*k + i*j*k*k, 0.0001);
					}
				}
			}
		}
		


		// test a few possibility and see how things might go wrong
		// predict trajectory given start points
		para = learner.predictTrajectory(xStart, yStart, xTap, yTap);
		System.out.println(para[0] + ", " + para[1] + ", " + para[2]); 	 // should be 5, 7, 35
		System.out.println(para[3] + ", " + para[4] + ", " + para[5]);

		// predict trajectory point given start points and x location
		yPred = learner.predictPoint(xStart, yStart, xTap, yTap, xPoint);
		System.out.println(yPred);
		System.out.println(xStart + yStart*xPoint + xStart*yStart*xPoint*xPoint);
		System.out.println(para[3] + para[4]*xPoint + para[5]*xPoint*xPoint);

		//
		xStart = 5.5;
		yStart = 7.5;
		xTap = 30.0;
		yTap = xStart + yStart*xTap + xStart*yStart*xTap*xTap;
		xPoint = 55.5;

		para = learner.predictTrajectory(xStart, yStart, xTap, yTap);
		System.out.println(para[0] + ", " + para[1] + ", " + para[2]); 	// hopefully be 5.5, 7.5, 41.25
		System.out.println(para[3] + ", " + para[4] + ", " + para[5]);	

		yPred = learner.predictPoint(xStart, yStart, xTap, yTap, xPoint);
		System.out.println(yPred);
		System.out.println(yStart + xStart*xPoint + xStart*yStart*xPoint*xPoint);
	}
	
	public static void print(double[] obj) {
		System.out.print("[");
		for(double o : obj) {
			System.out.print(o);
			System.out.print(" ");
		}
		System.out.print("]\n");
	}


}
