/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package KB.object.bird;


import KB.object.Active_Object;
import KB.object.Appearance;

public abstract class Bird extends Active_Object {
private double mass;  // used when predicate trajectory
private Appearance appearance; // appearance of bird,used to identify bird
private boolean hasChildren; // view as a tree of inheritance
public Bird(Appearance appearnce)
{
  super();
  this.appearance = appearnce;
  this.hasChildren = true;
}
public Bird(double mass,  Appearance appearance) {
	super();
	this.mass = mass;
	this.appearance = appearance;
}
public double getMass() {
	return mass;
}
public void setMass(double mass) {
	this.mass = mass;
}

public Appearance getAppearance() {
	return appearance;
}
public void setAppearance(Appearance appearance) {
	this.appearance = appearance;
}
public String hierarchy() {
	return super.hierarchy() + "/Bird";
	
}
public static String title()
{
    return "Bird";	
}
public boolean hasChildren()
{
	return this.hasChildren;
}
public void setHasChildren(boolean hasChildren) {
	this.hasChildren = hasChildren;
}


}
