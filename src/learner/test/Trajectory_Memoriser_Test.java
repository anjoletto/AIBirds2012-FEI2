/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package learner.test;

import static org.junit.Assert.*;
import learner.util.Trajectory_Memoriser;
import learner.util.Trajectory_Memoriser.Point;
import learner.util.Trajectory_Memoriser.Trajectory;

public class Trajectory_Memoriser_Test {

	/**
	 * Test class for Trajectory_Memoriser
	 * 
	 * 
	 * @author Kar Wai Lim
	 * 
	 * Last updated: 21 Nov 2012
	 */
	
	public static void main(String[] args) {

		// =========================================================================================================================
		// 									Simple tutorial (the values are chosen arbitrarily) 
		// =========================================================================================================================

		// initialise the learner
		Trajectory_Memoriser trajMemoLearner = new Trajectory_Memoriser();

		// for each trajectory (each bird throw), add starting points and trajectory points
		trajMemoLearner.addStartPoint(-48.3, -55.7);	// always do this before adding trajectory point

		trajMemoLearner.addTrajectoryPoint(0.7, 13.0);
		trajMemoLearner.addTrajectoryPoint(26.9, 77.0);
		trajMemoLearner.addTrajectoryPoint(37.5, 99.1);
		trajMemoLearner.addTrajectoryPoint(105.0, 256.0);
		trajMemoLearner.addTrajectoryPoint(271.4, 155.7);
		
		// store the trajectory as trained trajectory parameters
		trajMemoLearner.storePara();
		
		// display the first learned trajectory
		Trajectory traj1 = trajMemoLearner.getTrajectory(new Point(-48.3, -55.7));
		double[] para1 = traj1.getPara();
		System.out.println(para1[0] + ", " + para1[1] + ", " + para1[2]);
		
		// ... and repeat for other trajectories
		trajMemoLearner.addStartPoint(-44.3, -66.7);
		
		trajMemoLearner.addTrajectoryPoint(20.3, 66.0);
		trajMemoLearner.addTrajectoryPoint(77.1, 205.8);
		trajMemoLearner.addTrajectoryPoint(110.0, 244.6);
		trajMemoLearner.addTrajectoryPoint(124.5, 233.7);
		trajMemoLearner.addTrajectoryPoint(174.6, 185.7);
		
		trajMemoLearner.storePara();

		// predict trajectory given start points
		double[] para = trajMemoLearner.predictTrajectory(-42.0, -99.0);
		System.out.println(para[0] + ", " + para[1] + ", " + para[2]);
		
		// predict corresponding y-location given a x-location of a certain point on the
		// trajectory and start points
		double yPoint = trajMemoLearner.predictPoint(-33.6, -77.8, 59.0);
		System.out.println(yPoint);
		

		// =========================================================================================================================
		// 														Testing 
		// =========================================================================================================================
		
		double xStart = 5.0;
		double yStart = 7.0;
		double xPoint = 65.0;

		// initialise Trajectory_Memoriser
		Trajectory_Memoriser learner = new Trajectory_Memoriser();
		
		assertTrue(learner.get_m() == 0);
		assertEquals(learner.predictTrajectory(xStart, yStart).length, 3);
		assertEquals(learner.predictTrajectory(xStart, yStart)[0], Double.NaN, 0.0000001);
		assertEquals(learner.predictTrajectory(xStart, yStart)[0], Double.NaN, 0.0000001);
		assertEquals(learner.predictTrajectory(xStart, yStart)[0], Double.NaN, 0.0000001);
		
		Double yPred = learner.predictPoint(xStart, yStart, xPoint);
		assertTrue(yPred.equals(Double.NaN));

		// add trivial start points and trajectories points
		for(int i=1; i<=100; i++) {
			for(int j=1; j<=100; j++) {
				learner.addStartPoint(i, j);
				for(int k=1; k<=100; k++) {
					learner.addTrajectoryPoint(k, i + j*k + i*j*k*k);
					assertEquals(learner.get_n(), k);
				}
				learner.storePara();
				assertEquals(learner.get_m(), (i-1)*100+j);
				assertEquals(learner.get_n(), 0);
				
				// test storing correctness
				Trajectory trajTest = learner.getTrajectory(new Point(i, j));
				double[] paraTest = trajTest.getPara();
				assertEquals(paraTest[0], i, 0.0001);
				assertEquals(paraTest[1], j, 0.0001);
				assertEquals(paraTest[2], i*j, 0.0001);
			}
		}
		
		// verify correctness in predicting trajectories and predicting points 
		for(int i=1; i<100; i++) {
			for(int j=1; j<100; j++) {
				double[] paraTest = learner.predictTrajectory(i, j);
				assertEquals(paraTest[0], i, 0.0001);
				assertEquals(paraTest[1], j, 0.0001);
				assertEquals(paraTest[2], i*j, 0.0001);
				
				for(int k=1; k<100; k++) {
					double yPredTest = learner.predictPoint(i, j, k);
					assertEquals(yPredTest, i + j*k + i*j*k*k, 0.0001);
				}
			}
		}
		
		// test a few possibility and see how things might go wrong
		// predict trajectory given start points
		para = learner.predictTrajectory(xStart, yStart);
		System.out.println(para[0] + ", " + para[1] + ", " + para[2]); 	 // should be 5, 7, 35
		
		// predict trajectory point given start points and x location
		yPred = learner.predictPoint(xStart, yStart, xPoint);
		System.out.println("Predict: " + yPred);
		System.out.println("True: " + (xStart + yStart*xPoint + xStart*yStart*xPoint*xPoint));
		
		
		// demonstrate limitation of interpolation
		xStart = 5.5;
		yStart = 7.5;
		xPoint = 55.5;
		
		para = learner.predictTrajectory(xStart, yStart);
		System.out.println(para[0] + ", " + para[1] + ", " + para[2]); 	 // hopefully be 5.5, 7.5, 41.25
		
		yPred = learner.predictPoint(xStart, yStart, xPoint);
		System.out.println("Predict: " + yPred);
		System.out.println("True: " + (xStart + yStart*xPoint + xStart*yStart*xPoint*xPoint));
	}

}
