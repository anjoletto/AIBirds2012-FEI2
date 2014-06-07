/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package planner.imp;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import planner.imp.Shooter_Memoriser_Tap.DecisionPoint;
import learner.util.Trajectory_Memoriser_Tap;

public class Shooter_Memoriser_Tap_Test {

	/**
	 * Test class for Shooter_Memoriser
	 * 
	 * @author Kar Wai Lim
	 * 
	 * Last updated: 21 Nov 2012
	 */

	public static void main(String[] args) {
		
		// generate a trivial Trajectory_Memoriser
		Trajectory_Memoriser_Tap trajMemo = new Trajectory_Memoriser_Tap();
		for(int i=1; i<=20; i++) {
			for(int j=1; j<=20; j++) {
				for(int t=1; t<4; t++) {
					trajMemo.addStartPoint(i, j);
					trajMemo.addTapPoint(10*t, i + 10*j*t + 100*i*j*t*t, i*i*j*j*t*t);
					for(int k=1; k<=100; k++) {
						trajMemo.addTrajectoryPoint(k, i + j*k + i*j*k*k);
					}
					trajMemo.storePara();
				}
			}
		}
		// Initialise Shooter utility
		Shooter_Memoriser_Tap shooter = new Shooter_Memoriser_Tap(trajMemo);

		// function to tweak default utility parameters
		shooter.changePointTolerance(3.5);								// to what extent to deem two points are not too different (based on y distance)
		shooter.changeGradientTolerance(2000.5);						// to what extent gradient can be differ from the required gradient

		// find a random starting point which shoot a target (xTarget, yTarget) given constraint: (1) gradient when hit, (2) additional point to pass through
		// return null when no solution
		double xTarget = 4.0;
		double yTarget = 21.0 + 6.0*xTarget + 21.0*6.0*xTarget*xTarget;
		double targetGradient = 5.0;
		double xCons = 4.0;
		double yCons = 21.0 + 6.0*xTarget + 21.0*6.0*xTarget*xTarget - 3.45;

		DecisionPoint startPoint1 = shooter.getResult(xTarget, yTarget);									// no constraint
		DecisionPoint startPoint2 = shooter.getResult(xTarget, yTarget, targetGradient);					// with (1)
		DecisionPoint startPoint3 = shooter.getResult(xTarget, yTarget, xCons, yCons);						// with (2)
		DecisionPoint startPoint4 = shooter.getResult(xTarget, yTarget, targetGradient, xCons, yCons);		// with (1) and (2)

		// show the random solution
		System.out.println(startPoint1);
		System.out.println("-----------");
		System.out.println(startPoint2);
		System.out.println("-----------");
		System.out.println(startPoint3);
		System.out.println("-----------");
		System.out.println(startPoint4);
		System.out.println("-----------");
		
		// Test if the solution is correct
		if(startPoint1 != null) {
			DecisionPoint s = startPoint1;
			assertEquals(trajMemo.predictPoint(s.getXStart(), s.getYStart(), s.getXTap(), s.getYTap(), xTarget), yTarget, 2.0);
		}

		// return an array of starting points which shoot a target (xTarget, yTarget) given constraint: (1) gradient when hit, (2) additional point to pass through
		// return empty array when no solution
		// Note that the only different is the "s" in "getResults"!
		HashSet<DecisionPoint> startPoints1 = shooter.getResults(xTarget, yTarget);									// no constraint
		HashSet<DecisionPoint> startPoints2 = shooter.getResults(xTarget, yTarget, targetGradient);					// with (1)
		HashSet<DecisionPoint> startPoints3 = shooter.getResults(xTarget, yTarget, xCons, yCons);						// with (2)
		HashSet<DecisionPoint> startPoints4 = shooter.getResults(xTarget, yTarget, targetGradient, xCons, yCons);		// with (1) and (2)
		
		// check the number of solutions
		System.out.println(startPoints1.size());
		System.out.println(startPoints1);
		System.out.println("-----------");
		System.out.println(startPoints2.size());
		System.out.println(startPoints2);
		System.out.println("-----------");
		System.out.println(startPoints3.size());
		System.out.println(startPoints3);
		System.out.println("-----------");
		System.out.println(startPoints4.size());
		System.out.println(startPoints4);
		System.out.println("-----------");
		
		// Test if the solutions are correct
		if(!startPoints1.isEmpty()) {
			for(DecisionPoint s : startPoints1) {
				System.out.println("Predict: " + trajMemo.predictPoint(s.getXStart(), s.getYStart(), s.getXTap(), s.getYTap(), xTarget) + "; Wanted: " + yTarget);
				assertEquals(trajMemo.predictPoint(s.getXStart(), s.getYStart(), s.getXTap(), s.getYTap(), xTarget), yTarget, 5.0);
			}
		}
	}
}
