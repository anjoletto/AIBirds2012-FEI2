/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package KB.trajectory.imp;

import java.util.LinkedHashSet;

import learner.regression.imp.TrajectoryFormula;

import representation.APoint;
import KB.object.bird.Bird;
import KB.trajectory.KB_Trajectory;

/**
 * @author ABTeam_Gary
 *
 * @param <T> 
 * 
 *  NonActionTrajectory refers to trajectories without tapping involved
 *  It is a knowledge stored in KB
 */
public class KB_NonActionTrajectory<T extends Bird> extends KB_Trajectory<T>{
    
	private static final long serialVersionUID = 5971614617232906181L;

    public KB_NonActionTrajectory(){
    	super();
    }
//    
	/**
	 * @param formula    polynomial functions of the trajectory
	 * Construct a knowledge using known formula
	 */
	public KB_NonActionTrajectory(TrajectoryFormula formula) {
		super(formula);
		// TODO Auto-generated constructor stub
	}
    public String toString()
    {
    	String result = "hierarchy: " + this.hierarchy() + "\n";
    	result+= "Name: " + this.title() + "\n";
    	result+=" Model Information: " + "\n";
    	result+= " Reference Ponit: "+ this.getReference_point() + "\n";
    	result+= "Formula " + this.getFormula() + "\n";
    	
    	return result;
    }


	
	public String hierarchy() {
	
	
		return super.hierarchy() + "/NonActionTrajecotry/"+T.title();
	}


	public String title() {
		
		// TODO Auto-generated method stub
		
		return " Traj_"+T.title();
		
	}

	@Override
	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc)
	 * @see KB.TrajectoryModel#predict(representation.APoint, int, int, int[])
	 */
	@Override
	/** only need one additional parameter stepsize. 
	 * 
	 * predict(APoint start_point, int start_x, int end_x, int step_size)
	 * 
	 * */
	
	public LinkedHashSet<APoint> predict(APoint start_point,int start_x, int end_x,int... otherparams)
	{
		LinkedHashSet<APoint> rpts = new LinkedHashSet<APoint>();
		int step_size = otherparams[0];
		for (int i = start_x; i < end_x; i+=step_size )
		{
			APoint rpt = new APoint(i,this.getReference_point().getY()- this.formula.getPredictedValule(start_point.getX() - this.reference_point.getX(),this.reference_point.getY() 
					- start_point.getY(),i-this.getReference_point().getX() ));
			rpts.add(rpt);
		}
		return rpts;
	}
	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object evaluate() {
		// TODO Auto-generated method stub
		return null;
	}


}
