/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package KB.object;

import java.io.Serializable;

import KB.Knowledge;

/**
 * @author ABTeam_Gary
 * Basic Object used to represent all the objects in AB world that would change other objects or be effected by others.
 */
public class Active_Object implements Knowledge,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5262517659302823395L;


	public String hierarchy() {
		// TODO Auto-generated method stub
		return "/KB"+"/ActiveObjects";
	}

	@Override
	public boolean save() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Knowledge of same hierarchy may have different titles. Titles are like names 
	 * @return title of knowledge
	 */
	public static String title(){
		return "ActiveObjects";
	}

	@Override
	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return true;
	}


}
