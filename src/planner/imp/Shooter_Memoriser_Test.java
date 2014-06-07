/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package planner.imp;

import learner.util.Trajectory_Memoriser;
import representation.APoint;

public class Shooter_Memoriser_Test {

	/**
	 * Test class for Shooter_Memoriser
	 * 
	 * @author Kar Wai Lim
	 * 
	 * Last updated: 21 Nov 2012
	 */

	public static void main(String[] args) {
		
		// =========================================================================================================================
		// 												Example
		// =========================================================================================================================
		
		// generate a trivial Trajectory_Memoriser
		Trajectory_Memoriser trajMemo = new Trajectory_Memoriser();
		for(int i=1; i<25; i++) {
			for(int j=1; j<25; j++) {
				trajMemo.addStartPoint(i, j);
				for(int k=1; k<15; k++) {
					trajMemo.addTrajectoryPoint(k, i + j*k + i*j*k*k);
				}
				trajMemo.storePara();
			}
		}
		// Initialise Shooter utility
		Shooter_Memoriser shooter = new Shooter_Memoriser(trajMemo);

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

		APoint startPoint1 = shooter.getResult(xTarget, yTarget);									// no constraint
		APoint startPoint2 = shooter.getResult(xTarget, yTarget, targetGradient);					// with (1)
		APoint startPoint3 = shooter.getResult(xTarget, yTarget, xCons, yCons);						// with (2)
		APoint startPoint4 = shooter.getResult(xTarget, yTarget, targetGradient, xCons, yCons);		// with (1) and (2)

		// show the random solution
		System.out.println(startPoint1);
		System.out.println("-----------");
		System.out.println(startPoint2);
		System.out.println("-----------");
		System.out.println(startPoint3);
		System.out.println("-----------");
		System.out.println(startPoint4);
		System.out.println("-----------");

		// return an array of starting points which shoot a target (xTarget, yTarget) given constraint: (1) gradient when hit, (2) additional point to pass through
		// return empty array when no solution
		// Note that the only different is the "s" in "getResults"!
		APoint[] startPoints1 = shooter.getResults(xTarget, yTarget);									// no constraint
		APoint[] startPoints2 = shooter.getResults(xTarget, yTarget, targetGradient);					// with (1)
		APoint[] startPoints3 = shooter.getResults(xTarget, yTarget, xCons, yCons);						// with (2)
		APoint[] startPoints4 = shooter.getResults(xTarget, yTarget, targetGradient, xCons, yCons);		// with (1) and (2)
		
		// check the number of solutions
		System.out.println(startPoints1.length);
		System.out.println("-----------");
		System.out.println(startPoints2.length);
		System.out.println("-----------");
		System.out.println(startPoints3.length);
		System.out.println("-----------");
		System.out.println(startPoints4.length);
		System.out.println("-----------");
		
		// =========================================================================================================================
		// 											Specific Example Testing 
		// =========================================================================================================================
			
		Trajectory_Memoriser trajMemo2 = new Trajectory_Memoriser();
		trajMemo2.addStartPoint(272.09302325581393-297.09302325581393, 660.4651162790698-666.2790697674419);
		trajMemo2.addTrajectoryPoint(	329.6511628	,	320.9302326	);
		trajMemo2.addTrajectoryPoint(	366.8604651	,	330.2325581	);
		trajMemo2.addTrajectoryPoint(	733.7209302	,	168.0232558	);
		trajMemo2.addTrajectoryPoint(	405.2325581	,	334.8837209	);
		trajMemo2.addTrajectoryPoint(	498.255814	,	325.5813953	);
		trajMemo2.addTrajectoryPoint(	540.6976744	,	311.627907	);
		trajMemo2.addTrajectoryPoint(	162.2093023	,	220.3488372	);
		trajMemo2.addTrajectoryPoint(	229.0697674	,	272.6744186	);
		trajMemo2.addTrajectoryPoint(	682.5581395	,	218.6046512	);
		trajMemo2.addTrajectoryPoint(	254.0697674	,	287.2093023	);
		trajMemo2.addTrajectoryPoint(	708.1395349	,	194.1860465	);
		trajMemo2.addTrajectoryPoint(	343.0232558	,	325	);
		trajMemo2.addTrajectoryPoint(	380.2325581	,	333.1395349	);
		trajMemo2.addTrajectoryPoint(	-13.95348837	,	9.302325581	);
		trajMemo2.addTrajectoryPoint(	422.6744186	,	335.4651163	);
		trajMemo2.addTrajectoryPoint(	460.4651163	,	333.1395349	);
		trajMemo2.addTrajectoryPoint(	485.4651163	,	327.9069767	);
		trajMemo2.addTrajectoryPoint(	111.627907	,	170.9302326	);
		trajMemo2.addTrajectoryPoint(	137.2093023	,	197.0930233	);
		trajMemo2.addTrajectoryPoint(	86.62790698	,	142.4418605	);
		trajMemo2.addTrajectoryPoint(	61.62790698	,	112.7906977	);
		trajMemo2.addTrajectoryPoint(	615.6976744	,	270.9302326	);
		trajMemo2.addTrajectoryPoint(	577.9069767	,	293.6046512	);
		trajMemo2.addTrajectoryPoint(	195.9302326	,	248.8372093	);
		trajMemo2.addTrajectoryPoint(	653.4883721	,	243.0232558	);
		trajMemo2.addTrajectoryPoint(	27.3255814	,	68.60465116	);
		trajMemo2.addTrajectoryPoint(	36.04651163	,	81.39534884	);
		trajMemo2.addTrajectoryPoint(	267.4418605	,	294.7674419	);
		trajMemo2.storePara();
		
		trajMemo2.addStartPoint(272.09302325581393-297.09302325581393, 660.4651162790698-658.1395348837209);
		trajMemo2.addTrajectoryPoint(	160.4651163	,	172.0930233	);
		trajMemo2.addTrajectoryPoint(	236.0465116	,	206.9767442	);
		trajMemo2.addTrajectoryPoint(	273.255814	,	217.4418605	);
		trajMemo2.addTrajectoryPoint(	424.4186047	,	210.4651163	);
		trajMemo2.addTrajectoryPoint(	-15.69767442	,	12.20930233	);
		trajMemo2.addTrajectoryPoint(	33.72093023	,	68.60465116	);
		trajMemo2.addTrajectoryPoint(	59.88372093	,	93.60465116	);
		trajMemo2.addTrajectoryPoint(	84.88372093	,	115.6976744	);
		trajMemo2.addTrajectoryPoint(	605.2325581	,	97.6744186	);
		trajMemo2.addTrajectoryPoint(	197.6744186	,	191.8604651	);
		trajMemo2.addTrajectoryPoint(	311.627907	,	222.6744186	);
		trajMemo2.addTrajectoryPoint(	348.255814	,	222.6744186	);
		trajMemo2.addTrajectoryPoint(	387.2093023	,	219.7674419	);
		trajMemo2.addTrajectoryPoint(	411.0465116	,	212.7906977	);
		trajMemo2.addTrajectoryPoint(	500	,	176.744186	);
		trajMemo2.addTrajectoryPoint(	50.58139535	,	84.88372093	);
		trajMemo2.addTrajectoryPoint(	462.7906977	,	195.9302326	);
		trajMemo2.addTrajectoryPoint(	75.58139535	,	108.1395349	);
		trajMemo2.addTrajectoryPoint(	575.5813953	,	123.8372093	);
		trajMemo2.addTrajectoryPoint(	538.372093	,	153.4883721	);
		trajMemo2.addTrajectoryPoint(	118.6046512	,	143.0232558	);
		trajMemo2.addTrajectoryPoint(	595.9302326	,	104.6511628	);

		trajMemo2.storePara();
		
		Shooter_Memoriser shooter2 = new Shooter_Memoriser(trajMemo2);

		// function to tweak default utility parameters
		shooter2.changePointTolerance(10.5);				// to what extent to deem two points are not too different (based on y distance)
		shooter2.changeGradientTolerance(2000.5);			// to what extent gradient can be differ from the required gradient

		// find a random starting point which shoot a target (xTarget, yTarget) given constraint: (1) gradient when hit, (2) additional point to pass through
		// return null when no solution
		double xTarget2 = 800.0;
		double yTarget2 =  95.34883720930236;
		
		APoint startPoint1x = shooter2.getResult(xTarget2, yTarget2);	
		
		System.out.println(startPoint1x);
		
		APoint[] startPoints1x = shooter2.getResults(xTarget2, yTarget2);	
		
		System.out.println(startPoints1x.length);
		System.out.println(startPoints1x[0]);
//		System.out.println(startPoints1x[1]);
		
		double ypred2 = trajMemo2.predictPoint(startPoint1x.getX(), startPoint1x.getY(), xTarget2);
		System.out.println(ypred2);
	}
}
