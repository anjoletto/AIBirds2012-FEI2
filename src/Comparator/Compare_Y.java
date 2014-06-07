/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package Comparator;

import java.util.Comparator;

import representation.ABObject;

public class Compare_Y implements Comparator<ABObject>{

    public int compare(ABObject o1, ABObject o2) {
        return (int) (o1.getBoundBox().getY() - o2.getBoundBox().getY());
    }


}

