/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package KB.object;



/**
 * @author admin-xiaoyuge
 *
 * @param <T>
 */
public abstract class Object_Manager<T extends Active_Object> {

	

	// update if not
	public abstract void update(Active_Object newao);
	public String toString(){return "This is Object_Manger from super class";}
}
