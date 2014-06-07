/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package learner;

import java.util.List;
import java.util.Set;

import learner.regression.Formula;
import representation.APoint;
import KB.TrajectoryModel;
import exception.ExEmptySet;

/**
 * @author ABTeam_Gary
 *
 * @param <T>      The Trajectory Model of a certain kind of bird
 */
public interface TrajectoryLearner<T extends TrajectoryModel<?>> 
{
	
	/**
	 * @return Format knowledge and return a Trajectory Model after refinement. 
	 * The newly learned model is not necessarily always better than the old model, so the return knowledge might not be always the latest knowledge.
	 */
	public T  gainKnowledge();
  	
     //-- -- train on Dataset and write formula----------------//
	
    /**
     * @param dataset a set of training data. Refers to a set of sample trajectory dots of a certain scenario
     * @throws ExEmptySet          if the training data set is empty
     */
    public void train(Set<APoint> dataset) throws ExEmptySet;
    //---- train on a set of data set
    
    /**
     * @param datasets contains a set of trajectories of different scenarios
     * @throws ExEmptySet if the training data set is empty
     */
    public void train(List<? extends Set<APoint>> datasets) throws ExEmptySet;
   
 }
