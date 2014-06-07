/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package planner;

import java.io.IOException;
import java.util.List;

import representation.APoint;
import KB.Model;
import KB.TrajectoryModel;
import KB.object.bird.Bird;
import KB.trajectory.KB_Trajectory;
import exception.ExKnowledgeNotExsist;
import exception.plan.ExNoPlanResult;

/**
 * @author ABTeam_Gary
 *
 * @param <E> The class which refers to a certain type of bird
 * 
 */
public interface TrajectoryPlanner<E extends Bird>  {
/**
 * @param TrajectoryModel<E>       the trajectory model by which the planner make a planning.
 * 
 * */
public void loadModel(TrajectoryModel<E> model);
/**
 * Loading procedure of one model/several models could be embedded in this method
 * 
 * */
public void loadModel() throws IOException, ClassNotFoundException, ExKnowledgeNotExsist;
/**
 * @param target       The target on the screen(Image, (0,0) is left upper corner) you want to shoot
 * @param TrajectoryModel<E>    The trajectory model by which the planner make a planning.
 * @return return the plan result, the released point
 * @exception ExNoPlanResult    throws ExNoPlanResult Exception if return null,i.e no valid plans exist
 * */
public APoint getResult(APoint target,TrajectoryModel<E> kbt) throws ExNoPlanResult;
/**
 * @param target       The target on the screen(Image, (0,0) is left upper corner) you want to shoot
 * @param TrajectoryModel<E>    The trajectory model by which the planner make a planning.
 * @return return the plan result, the released points
 * @exception ExNoPlanResult    throws ExNoPlanResult Exception if return null,i.e no valid plans exist
 * */
public   List<APoint> getResults(APoint target,TrajectoryModel<E> kbt);
/**
 * @param target       The target on the screen(Image, (0,0) is left upper corner) you want to shoot
 * @param TrajectoryModel<E>    The trajectory model by which the planner make a planning.
 * @return return the estimated results. used when no valid plans exist
 * @exception ExNoPlanResult    throws ExNoPlanResult Exception if return null,i.e no valid plans exist
 * */

public APoint getEstimatedResults(APoint target,TrajectoryModel<E> kbt);
/**
 * @param x
 * @param y
 * @return
 * @throws ExNoPlanResult
 */
public APoint getResult(double x, double y)throws ExNoPlanResult;
/**
 * @param target
 * @return
 * @throws ExNoPlanResult
 */
APoint getResult(APoint target) throws ExNoPlanResult;


}
