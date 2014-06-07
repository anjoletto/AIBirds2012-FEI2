/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package KB.object.imp;

import java.io.Serializable;


import KB.object.Appearance;

/**
 * @author ABTeam_Gary
 *
 * @param <Feature_Type>
 *
 */
public class Bird_Appearance<Feature_Type> implements Appearance,Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1485939350721704990L;
private int color;
private String type;
public Feature_Type feature;
	public void setColor(int color) {
	this.color = color;
}

public void setType(String type) {
	this.type = type;
}

	@Override
	public int getColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Bird_Appearance(Feature_Type feature_type) {
		super();
		this.feature =feature_type;
	}

	public Bird_Appearance(int color) {
		super();
		this.color = color;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}



}
