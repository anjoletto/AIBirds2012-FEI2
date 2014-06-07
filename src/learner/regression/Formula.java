/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package learner.regression;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author ABTeam_Gary
 *
 * @param <InputType>
 * @param <CoType>
 * @param <ReturnType>
 */
public abstract class Formula<InputType,CoType,ReturnType> implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = -7663900140097652853L;
private ArrayList<CoType> coE;
// private String[] coE_name;
 
 public Formula(int i)
 {
	  coE = new ArrayList<CoType>(i);
     
 }
public abstract InputType getPredictedValule(ReturnType... e);

public void setCoefficient(int i, CoType coefficient)
{
    this.coE.set(i, coefficient);
}
public CoType getCoefficient(int i)
{
   return this.coE.get(i);	
}

// added by Kar Wai: return the number of coefficients
public int getCoefficientLength() {
	return coE.size();
}


}
