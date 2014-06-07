package KB;

import java.util.LinkedHashSet;

import representation.APoint;
import KB.object.bird.Bird;

/**
 * @author ABTeam_Gary
 *
 * @param <E>
 * Trajectory Model of Birds
 */
public interface TrajectoryModel<E extends Bird> extends Model<E> {
//
	/**
	 * @param start_point    released point
	 * @param start_x        the x coordinate of the start point. Start point is not necessarily the release point. It could be any point after released point 
	 * @param end_x          the x coordinate of the end point
	 * @param otherparams    step_size, etc
	 * @return               y coordinate values from start_x to end_x
	 */
	public LinkedHashSet<APoint> predict(APoint start_point,int start_x, int end_x,int... otherparams);
}
