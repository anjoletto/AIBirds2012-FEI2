/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package solver;

import java.util.HashMap;
import java.util.LinkedList;

import representation.ABObject;

public class NaiveSolver {


	public LinkedList<ABObject> getTargets(HashMap<Double, ABObject> objects)
	{
		LinkedList<ABObject> targets = new LinkedList<ABObject>();
		for ( Double key:objects.keySet())
		{
			ABObject o0 = objects.get(key);
			if(o0.getVision_name().equalsIgnoreCase("Pig"))
			   {
				//System.out.println("target: "+o0);
				if(targets.isEmpty())
					targets.add(o0);
				else{
						boolean same = false;
						for(ABObject expig:targets)
						{ 
							same |= sameObject(o0,expig);
						}
						if(!same)
							targets.add(o0);
				}
			   }
		}
	    return targets;	
	}
	public boolean sameObject(ABObject o1,ABObject o2)
	{
		double distance = Math.sqrt(Math.pow(o1.getCentroid().getX() - o2.getCentroid().getX(), 2) + Math.pow(o1.getCentroid().getY()-o2.getCentroid().getY(), 2));
		//System.out.println("Distance between " + o1 +" and " +o2 + " is " + distance);
		if( distance < 5)
		{
		
			return true;}
		else 
			return false;
	}
	
}
