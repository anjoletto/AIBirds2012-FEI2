/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package learner.util;

import representation.APoint;
import Jama.Matrix;

/**
 * Provide a trajectory equation of angry bird based on
 * the initial location of the bird (xy coordinate).
 * 
 * Model for overall: y = (para0)*(yStart) + (para1)*(xStart) + (para2)*(yStart/xStart * x) + (para3)*(yStart^2/xStart * x) + (para4)*(yStart*x)
 * 							+ (para5)*(xStart*x) + (para6)*(yStart*xStart*x) + (para7)*(x^2/xStart^2) + (para8)*(x^2/yStart^2) + (para9)*(x^2 / (yStart*xStart))
 *   i.e. quadratic function in term of x
 *
 * @author Kar Wai Lim
 *
 *
 * TODO: Test and improve the model
 * 
 * Last updated: Long time ago
 */

public class Trajectory_Regression {
	
	private int array_length = 100;		// starting array length
	private final int parameter_length = 10;
	private double lambda = 0.5;	// regularise parameter
	
	private int n;				// number of data point
	private double[] para;		// parameters to be trained
	private double[] y;			// y coordinates (vector)
	private double[][] X;		// basis for training (matrix)

	private double xStart = 0;	// temporary storage for starting point of bird
	private double yStart = 0;	// ..
	
	/** Constructor */
	public Trajectory_Regression() {
		n = 0;
		y = new double[array_length];
		para = new double[parameter_length];
		X = new double[array_length][parameter_length];
	}
	
	/** change the value of lambda, which is the regularisation constant */
	public void changeLambda(double lambda) {
		this.lambda = lambda;
	}
	
	/** Add a starting point (before bird is released) for training purposes. */
	// Do this for each bird released.
	// xStart and yStart should usually be negative
	public void addStartPoint(double xStart, double yStart) {
		this.xStart = xStart;
		this.yStart = yStart;
	}
	
	/** Add a point of the bird's trajectory for training given temporarily stored start points. */
	public void addTrajectoryPoint(double xPoint, double yPoint) {
		if(xStart*yStart==0)
			System.out.println("Potential Error: You did not input start points!");
		if(n == array_length)
			expand();
		y[n] = yPoint;
		X[n][0] = yStart;
		X[n][1] = xStart;
		X[n][2] = (yStart/xStart)*xPoint;
		X[n][3] = (yStart*yStart/xStart)*xPoint;
		X[n][4] = yStart*xPoint;
		X[n][5] = xStart*xPoint;
		X[n][6] = yStart*xStart*xPoint;
		X[n][7] = (xPoint*xPoint)/(xStart*xStart);
		X[n][8] = (xPoint*xPoint)/(yStart*yStart);
		X[n][9] = (xPoint*xPoint)/(yStart*xStart);		
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
//		System.out.print("array_length: " + yTemp.length + " increased to " + y.length + "\n");
	}
	
	/** train parameters */
	public void updatePara() {
		double[] yNew = new double[n];
		double[][] XNew = new double[n][parameter_length];
		System.arraycopy(y, 0, yNew, 0, n);						// trim the array to length n
		System.arraycopy(X, 0, XNew, 0, n);
		Matrix yMatrix = new Matrix(yNew, n);
		Matrix XMatrix = new Matrix(XNew, n, parameter_length);
		Matrix paraMatrix = new Matrix(parameter_length, 1);	// empty matrix (vector)
		Matrix IMatrix = Matrix.identity(parameter_length, parameter_length);	// identity matrix
		Matrix XTranspose = XMatrix.transpose();
		paraMatrix = (XTranspose.times(XMatrix)).plus(IMatrix.times(lambda)).inverse().times(XTranspose).times(yMatrix);
		for(int i=0; i<parameter_length; i++) {
			para[i] = paraMatrix.get(i, 0);
		}
	}
	public double[] getParas() {
		return para;
	}
	public double getPara(int i) {
		return para[i];
	}
	public int getParaLength() {
		return parameter_length;
	}
	
	/** get current y coordinate given x coordinate using stored start points. */
	public double getYpoint(double xPoint) {
		double yTemp = para[0]*yStart + para[1]*xStart + para[2]*(yStart/xStart)*xPoint + para[3]*(yStart*yStart/xStart)*xPoint;
		yTemp += para[4]*yStart*xPoint + para[5]*xStart*xPoint + para[6]*yStart*xStart*xPoint + para[7]*xPoint*xPoint/(xStart*xStart);
		yTemp += para[8]*xPoint*xPoint/(yStart*yStart) + para[9]*xPoint*xPoint/(yStart*xStart);
		return yTemp;
	}
	
	/** return a predicted y location given required (input) start points and the respective x location. */
	public double predictPoint(double xStart, double yStart, double xPoint) {
		double yTemp = para[0]*yStart + para[1]*xStart + para[2]*(yStart/xStart)*xPoint + para[3]*(yStart*yStart/xStart)*xPoint;
		yTemp += para[4]*yStart*xPoint + para[5]*xStart*xPoint + para[6]*yStart*xStart*xPoint + para[7]*xPoint*xPoint/(xStart*xStart);
		yTemp += para[8]*xPoint*xPoint/(yStart*yStart) + para[9]*xPoint*xPoint/(yStart*xStart);
		return yTemp;
	}
	
	/**
	 * return a trajectory that only depends on xPoint given xInput and yInput (start points)
	 * trajectory returned is in the form of parameters of y = para0 + para1 * xPoint + para2 * xPoint^2 (quadratic equation in term of xPoint)
	 */
	public double[] predictTrajectory(double xInput, double yInput) {
		double[] tempPara = new double[3];
		tempPara[0] = para[0]*yInput + para[1]*xInput;
		tempPara[1] = para[2]*(yInput/xInput) + para[3]*(yInput*yInput/xInput) + para[4]*yInput + para[5]*xInput + para[6]*yInput*xInput;
		tempPara[2] = para[7]/(xInput*xInput) + para[8]/(yInput*yInput) + para[9]/(yInput*xInput);
		return tempPara;
	}
	
	/** dependent on the model, need to change if model changes! */
	public double getGradient(double xs, double ys, double xPoint) {
		double b = para[2] * ys/xs + para[3] * ys*ys/xs + para[4] * ys + para[5] * xs + para[6] * ys*xs;
		double c = para[7] / (xs*xs) + para[8] / (ys*ys) + para[9] / (xs*ys);
		double grad = b + 2*c*xPoint;
		return grad;
	}
	
	/** return the trained best fit */
	public String summary() {
		String str = "";
		str += "\n";
		str += "Best y/x model (y in term of x): ";
		str += this.getPara(0) + " (yStart) + ";
		str += this.getPara(1) + " (xStart) + ";
		str += this.getPara(2) + " (yStart/xStart)*x + "; 
		str += this.getPara(3) + " (yStart^2/xStart)*x + "; 
		str += this.getPara(4) + " (yStart)*x + ";
		str += this.getPara(5) + " (xStart)*x + "; 
		str += this.getPara(6) + " (yStart*xStart)*x + "; 
		str += this.getPara(7) + " x^2/(xStart^2) + ";
		str += this.getPara(7) + " x^2/(yStart^2) + ";
		str += this.getPara(7) + " x^2/(yStart*xStart)";
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
	
	/** force the parameters to be certain values */
	public void forcePara(double[] para) {
		assert(para.length == parameter_length);
		this.para = para;
	}
}
