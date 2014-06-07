/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package planner.trajectory.manager;

import io.trajectory.ABIO;
import io.trajectory.TrajectoryIO;

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import learner.imp.NonAction_TrajectoryMemorizer;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import planner.TrajectoryPlanner;
import planner.imp.Shooter_Memoriser;

import representation.APoint;
import representation.util.TrajectoryCollector;
import test.NaiveAgent;
import KB.object.bird.Plain_Bird;
import KB.trajectory.KB_Trajectory;
import env.Env;
import exception.ExEmptySet;
import exception.plan.ExNoPlanResult;


public class PlanTrajectory_Manager {
     private TrajectoryPlanner<? extends Plain_Bird> tp;
     private ArrayList<LinkedHashSet<APoint>> ps;
     private APoint referencePoint;
	public APoint getReferencePoint() {
		return referencePoint;
	}
	public void setReferencePoint(APoint referencePoint) {
		this.referencePoint = referencePoint;
	}

	public APoint plan(double x, double y) throws ExNoPlanResult
	{
		APoint result = this.tp.getResult(x,y);
		
		return result;
	}
	public APoint plan(APoint target) throws ExNoPlanResult
	{
		return plan(target.getX(),target.getY());
	}

	public void loadData(String datafile)
	{
		ABIO tio = new TrajectoryIO();
		
		try {
		
			ps = tio.loadHashSet(datafile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public TrajectoryPlanner<? extends Plain_Bird> loadPlannerMemo(String bird_type)
	{
		if(bird_type.equalsIgnoreCase("NonAction"))
		{
			ABIO tio = new TrajectoryIO();
			
			try 
			{
				
				if(ps == null)
					{
					   //S String filename = "points6test_2.2";
					    String filename = "pointsnew";
					  	System.out.println(" Using default data "+ filename);
						ps = tio.loadHashSet(filename);
			}
				NonAction_TrajectoryMemorizer<Plain_Bird> ntm = new NonAction_TrajectoryMemorizer<Plain_Bird>();
				ntm.train(ps);
				Shooter_Memoriser shooter = new Shooter_Memoriser(ntm.getTrajReg());
				 shooter.changePointTolerance(50);
				this.tp = shooter;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExEmptySet e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		}
		return this.tp;
	}
	
	public void loadPlanner(TrajectoryPlanner<? extends Plain_Bird> tp)
	{
	          this.tp = tp;	
	}
	
	//
	/**
	 * @param a   a point needs to be normalised
	 * @param ref the reference point
	 * @return the normalised point
	 */
	private static APoint normalise(APoint a, APoint ref) {
		APoint result = new APoint(a.getX()  - ref.getX(), ref.getY()- a.getY());
		return result;
	}
	//
	/**
	 * @param a point needs to be normalised
	 * @return the normalised point
	 */
	public APoint normalise(APoint a)
	{
		
		return normalise(a,referencePoint);
	}
	//
	
	/**
	 * @param a point needs to be normalised inversely. Recover from normalisation
	 * @return the result of inverse normalizaion
	 */
	public APoint inverse_normalise(APoint a)
	{
		APoint result = new APoint(a.getX()  + this.referencePoint.getX(), this.referencePoint.getY()- a.getY());
		return result;
	}
	
	
	
	
}
