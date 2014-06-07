/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package KB.trajectory;

import java.io.Serializable;

import learner.regression.Formula;
import learner.regression.imp.TrajectoryFormula;

import representation.APoint;
import KB.TrajectoryModel;
import KB.object.bird.Bird;


public abstract class KB_Trajectory<T extends Bird> implements TrajectoryModel<T>,Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = -5533368886813468926L;
protected TrajectoryFormula formula;
protected APoint reference_point;
protected double maximum_string_length;

public APoint getReference_point() {
	return reference_point;
}

public void setReference_point(APoint reference_point) {
	this.reference_point = reference_point;
}

public double getMaximum_string_length() {
	return maximum_string_length;
}

public void setMaximum_string_length(double maximum_string_length) {
	this.maximum_string_length = maximum_string_length;
}

// modified by Kar Wai
public KB_Trajectory(TrajectoryFormula formula) {
	this.formula = formula;
}

public KB_Trajectory(){}

public TrajectoryFormula getFormula() {
	return formula;
}

public KB_Trajectory(TrajectoryFormula formula,
		APoint reference_point, double maximum_string_length) {
	super();
	this.formula = formula;
	this.reference_point = reference_point;
	this.maximum_string_length = maximum_string_length;
}

public void setFormula(TrajectoryFormula formula) {
	this.formula = formula;
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
public  String  hierarchy()
{
  	return "/Trajectory";
}

public String description() {
	// TODO Auto-generated method stub
	return null;
}

}
