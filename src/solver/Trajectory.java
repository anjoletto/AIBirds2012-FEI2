/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solver;

import java.util.LinkedList;
import java.util.List;

import representation.ABObject;
import representation.APoint;
import representation.util.ObjectCollector;
import ab.framework.ai.ActionRobot;

/**
 *
 * @author Lopespt
 */
public class Trajectory {

    double dx;
    double dy;
    double p_dx;
    double p_dy;
    ObjectCollector oc;
    List<ABObject> restrictions;
    ABObject target;
    List<APoint> possibleSolutions;

    public Trajectory(ObjectCollector oc) {
        this.oc = oc;
    }

    public void setDxDy(double dx, double dy) {

        dy = dy / oc.refLength * 0.67;
        dx = dx / oc.refLength * 0.67;

        double p00 = -235.2;
        double p10 = 23.29;
        double p01 = 4.718;
        double p20 = -0.3907;
        double p11 = -0.4442;
        double p02 = 0.02191;
        double p30 = 0.002376;
        double p21 = 0.002863;
        double p12 = 0.003263;
        double p03 = -0.0009516;
        this.dx = p00 + p10 * dx + p01 * dy + p20 * dx * dx + p11 * dx * dy + p02 * dy * dy + p30 * dx * dx * dx + p21 * dx * dx * dy + p12 * dx * dy * dy + p03 * dy * dy * dy;



        p00 = -129.8;
        p10 = 3.565;
        p01 = 8.13;
        p20 = -0.05937;
        p11 = -0.1142;
        p02 = -0.08806;
        p30 = 0.0003354;
        p21 = 0.0006393;
        p12 = 0.0007805;
        p03 = 0.0002082;

        this.dy = p00 + p10 * dx + p01 * dy + p20 * dx * dx + p11 * dx * dy + p02 * dy * dy + p30 * dx * dx * dx + p21 * dx * dx * dy + p12 * dx * dy * dy + p03 * dy * dy * dy;
    }

    public double getDx() {
        return p_dx;
    }

    public double getDy() {
        return p_dy;
    }

    public double evaluate(double sx) {
        double sy = -((0.0102041 * (-8.88178 * Math.pow(10, -16) * dx * dx * dy * dy - 98 * dx * dy * sx + 480.2 * sx * sx)) / (dx * dx));
        return sy;
    }

    public List<APoint> computeFlight(ABObject target) {

        List<APoint> targetSolutions = new LinkedList<APoint>();
        //Choose solutions that has a flight near the target.
        APoint t = new APoint(oc.displayPoint(target.getCentroid()).getX(), oc.displayPoint(target.getCentroid()).getY());

        for (int i = 20; i < 60; i += 2) {
            for (int j = 20; j < 60; j += 2) {
                setDxDy(i, j);

                APoint targetEstimado = new APoint(t.getX(), oc.focusPoint.getY()-evaluate(t.getX() - oc.focusPoint.getX() ));
                
                if (t.distanceTo(targetEstimado) < 10) {
                    //Check if it violates the restrictions.
                    
                   // for(ABObject obj:restrictions){
                   //     APoint rest = new APoint(obj.getBoundBox().getCenterX(), obj.getBoundBox().getMinY());
                   // }
                    //Falta Continuar
                    
                    targetSolutions.add(new APoint(i, j));
                    
                    
                }
            }
        }
        
        
        return targetSolutions;
    }

//    public static void plotFlight(APoint p, ActionRobot ac, ObjectCollector oc) {
//        Trajectory t = new Trajectory(oc);
//        t.setDxDy(p.getX(), p.getY());
//
//        for (int i = 0; i < 800; i+=25) {
//            ac.PlotTarget((int) (i + oc.focusPoint.getX()), (int) (oc.focusPoint.getY()-t.evaluate(i)));
//        }
//
//    }
    
    public static void shoot(APoint solution,ActionRobot ac,ObjectCollector oc){
        ac.Makemove((int)oc.focusPoint.getX(), (int)oc.focusPoint.getY(), - (int) solution.getX(),  (int) solution.getY(), 5);
    }
    
    
    
}
