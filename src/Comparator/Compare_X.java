/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package Comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import representation.ABObject;
import representation.APoint;

public class Compare_X implements Comparator<ABObject>{

    public int compare(ABObject o1, ABObject o2) {
    
    	 return (int) (o1.getCentroid().getX() - o2.getCentroid().getX());
     //   return (int) (o1.getBoundBox().getX() - o2.getBoundBox().getX());
    }

public static void main(String args[])
{
	ArrayList<ABObject> abos = new ArrayList<ABObject>();
	ABObject abo = new ABObject();
	ABObject abo1 = new ABObject();
	abo.setCentroid(new APoint(10,20));
    
	abo1.setCentroid(new APoint(0,20));
    abos.add(abo);
    abos.add(abo1);
    
    Collections.sort(abos,new Compare_X());
    System.out.println(abos.get(0).getCentroid());
}
}

