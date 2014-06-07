/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package learner.imp;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import learner.TrajectoryLearner;
import learner.util.Trajectory_Memoriser;
import representation.APoint;
import KB.object.bird.Plain_Bird;
import KB.trajectory.KB_Trajectory;
import KB.trajectory.imp.KB_NonActionTrajectory;
import exception.ExEmptySet;

/**
 * @author ABTeam_Gary
 * @param <B>    Any Bird extends from Plain_Bird Type
 * It is a learner for learning trajectories of non-action (no-tap action)
 */

public class NonAction_TrajectoryMemorizer<B extends Plain_Bird> implements TrajectoryLearner<KB_Trajectory<B>> {
	
	/**The learning core model*/
	private Trajectory_Memoriser trajReg;
	/** Reference Point for normalisation. Reference Point should be consistent in all resulting models/planners */
	private APoint referencePoint;
	/** maximum length of string */
	private double maximum_string_length;
	
	public double getMaximum_string_length() {
		return maximum_string_length;
	}

	public void setMaximum_string_length(double maximum_string_length) {
		this.maximum_string_length = maximum_string_length;
	}

	public APoint getReferencePoint() {
		return referencePoint;
	}

	public void setReferencePoint(APoint referencePoint) {
		this.referencePoint = referencePoint;
	}
    /** Initialise a normal trajectory learner. The core model will be initialised at the same time */
	public NonAction_TrajectoryMemorizer() {
		trajReg = new Trajectory_Memoriser();
		//this.setReferencePoint(new APoint(226,444));
	}

	// return formula
   

	//
	/**
	 * @param startPoint  the start(release) point of the shoot
	 */
	public void addStartPoint(APoint startPoint) {
		trajReg.addStartPoint(startPoint.getX(), startPoint.getY());
	}
	//
	/**
	 * @param xStart    x coordinate of the start point
	 * @param yStart    y coordinate of the start point
	 */
	public void addStartPoint(double xStart, double yStart) {
		trajReg.addStartPoint(xStart, yStart);
	}
	
	//
	
	/**
	 * @param trajPoint a point along a certain trajectory
	 */
	public void addTrajectoryPoint(APoint trajPoint) {
		trajReg.addTrajectoryPoint(trajPoint.getX(),trajPoint.getY());
	}
	//
	/**
	 * @param xPoint   x coordinate of a trajectory point
	 * @param yPoint   y coordinate of a trajectory point
	 */
	public void addTrajectoryPoint(double xPoint, double yPoint) {
		trajReg.addTrajectoryPoint(xPoint, yPoint);
	}
	//
	/**
	 * @param a   a point needs to be normalised
	 * @param ref the reference point
	 * @return the normalised point
	 */
	private static APoint normalise(APoint a, APoint ref) {
		//System.out.println(ref);
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
	/**
     * @param dataSet  Add data set for training
	 * @throws ExEmptySet 
	 *  
     * **/
	private void addTrainingSet(Set<APoint> dataSet) throws ExEmptySet 
	{
	  if(dataSet.size() > 1){
       int count = 0;
       Iterator<APoint> it = dataSet.iterator(); 
       APoint refer = it.next();
       this.referencePoint = refer; //The first point is reference point, the second one is start_point
	    for (APoint ap: dataSet)
 	      {
		
	    	APoint normalisedPoint = normalise(ap, referencePoint);
	       // Debug.echo(this,normalisedPoint);
	    	if(count == 1) {  
	    		trajReg.addStartPoint( normalisedPoint.getX(), normalisedPoint.getY()); count ++;}
	    	else if(count == 0)
	    	{ 
	    		count++; 
	    		continue;
	    	}
	    	else
	    		trajReg.addTrajectoryPoint(normalisedPoint.getX(),normalisedPoint.getY());
 	      }
 	      
	  }
	  else
		  throw new ExEmptySet();
	  
	  //store data
	  trajReg.storePara();
	}
    


	@Override
	//If the formula is better than the one we have, return the new knowledge.
	//If is not better, return the old knowledge...//delayed to user's choice
	
	
	public  KB_NonActionTrajectory<B> gainKnowledge() {
	/*
		KB_NonActionTrajectory<B> kbTraj = new KB_NonActionTrajectory<B>(this.getFormula());	// cannot instantiate! (don't know why)
		kbTraj.setReference_point(this.referencePoint);
		kbTraj.setMaximum_string_length(this.getMaximum_string_length());
	
		if(compareFormula(this.getFormula()))
		{
			//store in KB
			KBSerializer<KB_NormalTrajectory<B>> kbtsave = new KBSerializer<KB_NormalTrajectory<B>>();
			kbtsave.save(kbTraj);
		}
		
		
		return kbTraj;*/
		return null;
	}

	@Override
	public void train(Set<APoint> dataset) throws ExEmptySet {
		
		
		
	}
	
	public void train(List<? extends Set<APoint>> datasets) throws ExEmptySet
	{
		for (Set<APoint> aps: datasets)
		{
			addTrainingSet(aps);
		}
		
	}

	public Trajectory_Memoriser getTrajReg() {
		return trajReg;
	}

	public void setTrajReg(Trajectory_Memoriser trajReg) {
		this.trajReg = trajReg;
	}

}