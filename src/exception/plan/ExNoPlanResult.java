/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package exception.plan;

import representation.APoint;

public class ExNoPlanResult extends Exception{
public ExNoPlanResult(APoint apoint)
{
   super(" I am not able to produce the plan for target at " + apoint);	

}
}
