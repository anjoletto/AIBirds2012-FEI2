/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package KB.shooting;

public class Shooting_No_Action extends Shooting {
	/**
  * For testing purpose, initialize to normal bird
  * will retrieve from database later
  */
	public Shooting_No_Action()
	{
		this.setEffective_elastic_energy(671);
		this.setGravity(100);
	}

	@Override
	public double getMaximumVelocity() {
		// TODO Auto-generated method stub
		return Math.sqrt(this.getEffective_elastic_energy() * this.getMaximum_string_length());
		}

}
