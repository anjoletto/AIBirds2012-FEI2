/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package learner.util;

import representation.APoint;

import Jama.Matrix;

/**
 * Provide a trajectory equation after tapping an angry bird based on
 * the initial location of the bird (xy coordinate) and the tapped location y_tap;
 * 
 * Model for overall: y = (para0) * (1/xStart * x) + (para1) * (yStart^2/xStart * x)
 * 							+ (para2) * (yTap/xStart * x) + (para3) * (yStart^2 * yTap / xStart * x)
 * 							+ (para4) * (yStart^4/xStart * x) + (para5) * (yTap^2 / xStart * x)
 * 							+ (para6) * (1/xStart^2 * x^2)
 *   i.e. quadratic function in term of x
 *
 * @author Kar Wai Lim
 * 
 * TODO: Note that this class is not completed
 * 
 * Last updated: Long time ago
 *
 */

public class Trajectory_Regression_Tap {
	
	private int array_length = 100;		// starting array length
	private final int parameter_length = 7;
	
	private int n;				// number of data point
	private double[] para;		// parameters to be trained
	private double[] y;			// y coordinates (vector)
	private double[][] X;		// basis for training (matrix)

	private double xStart = 0;	// temporary storage for starting point and tap point of bird
	private double yStart = 0;	// ..
	private double yTap = 0;	// ..
	
	public Trajectory_Regression_Tap() {
		n = 0;
		y = new double[array_length];
		para = new double[parameter_length];
		X = new double[array_length][parameter_length];
	}
	
	// Add a starting point (before bird is released) for training purposes.
	// Do this for each bird released.
	// xStart and yStart should usually be negative
	public void addStartPoint(double xStart, double yStart) {
		this.xStart = xStart;
		this.yStart = yStart;
	}
	public void addTapPoint(double yTap) {
		this.yTap = yTap;
	}
	
	// Add a point of the bird's trajectory for training.
	public void addTrajectoryPoint(double xPoint, double yPoint) {
		if(xStart*yStart*yTap==0)
			System.out.println("Potential Error: You did not input start points and/or tap point!");
		if(n == array_length)
			expand();
		y[n] = yPoint;
		X[n][0] = (1/xStart)*xPoint;
		X[n][1] = (yStart*yStart/xStart)*xPoint;
		X[n][1] = (yTap/xStart)*xPoint;
		X[n][1] = (yStart*yStart*yTap/xStart)*xPoint;
		X[n][1] = (yStart*yStart*yStart*yStart/xStart)*xPoint;
		X[n][1] = (yTap*yTap/xStart)*xPoint;
		X[n][1] = (1/(xStart*xStart))*xPoint*xPoint;
		n++;
	}
	public void addTrajectoryPoint(APoint p) {
		this.addTrajectoryPoint(p.getX(), p.getY());
	}
	
	// expand the size of array (2 times bigger)
	// System.arraycopy notes: http://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html
	private void expand() {
		array_length = array_length*2;
		double[] yTemp = y;
		double[][] XTemp = X;
		y = new double[array_length];
		X = new double[array_length][parameter_length];
		System.arraycopy(yTemp, 0, y, 0, n);
		System.arraycopy(XTemp, 0, X, 0, n);
		System.out.print("array_length: " + yTemp.length + " increased to " + y.length + "\n");
	}
	
	// train parameters
	public void updatePara() {
		double[] yNew = new double[n];
		double[][] XNew = new double[n][parameter_length];
		System.arraycopy(y, 0, yNew, 0, n);						// trim the array to length n
		System.arraycopy(X, 0, XNew, 0, n);
		Matrix yMatrix = new Matrix(yNew, n);
		Matrix XMatrix = new Matrix(X, n, parameter_length);
		Matrix paraMatrix = new Matrix(parameter_length, 1);	// empty matrix
		paraMatrix = ((((XMatrix.transpose()).times(XMatrix)).inverse()).times(XMatrix.transpose())).times(yMatrix);
		for(int i=0; i<parameter_length; i++) {
			para[i] = paraMatrix.get(i, 0);
		}
	}
	public double getPara(int i) {
		return para[i];
	}
	public int getParaLength() {
		return parameter_length;
	}
	
	// return the trained best fit
	public String summary() {
		String str = "";
		str += "\n";
		str += "------------ \n";
		str += "y data is: ";
		for(int i=0; i<n; i++) {
			str += " " + y[i] + " ";
		}
		str += "\n";
		str += "X matrix data is:";
		for(int i=0; i<parameter_length; i++) {
			for(int j=0; j<n; j++) {
				str += " " + X[j][i] + " ";
			}
			str += "\nand";
		}
		str += " parameters are: ";
		for(int i=0; i<parameter_length; i++) {
			str += " " + this.getPara(i);
		}
		return str;
	}
}

