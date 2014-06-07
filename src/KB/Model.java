package KB;

import KB.object.Active_Object;


/**
 * 
 * @author ABTeam_Gary
 *
 * @param <E> 
 * behaviour models of active objects
 */
public interface Model<E extends Active_Object> extends Knowledge {
//
	/**
	 * @return evaluated result. e.g. square errors
	 */
	public Object evaluate();
 
}
