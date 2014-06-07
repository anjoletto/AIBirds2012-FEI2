/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package KB.shooting;

public abstract class Shooting {
  private double gravity;
  private double effective_elastic_energy;
  private final static double maximum_string_length = 30;
  private final static double string_start_y = 349;
  private final static double string_start_x  = 173;
  public static double getStringStartY() {
	return string_start_y;
}
public static double getStringStartX() {
	return string_start_x;
}
private double maximum_velocity;
 public abstract double getMaximumVelocity ();
public static double getMaximum_string_length() {
	return maximum_string_length;
}
public double getGravity() {
	return gravity;
}
public void setGravity(double gravity) {
	this.gravity = gravity;
}
public double getEffective_elastic_energy() {
	return effective_elastic_energy;
}
public void setEffective_elastic_energy(double effective_elastic_energy) {
	this.effective_elastic_energy = effective_elastic_energy;
}
 
}
