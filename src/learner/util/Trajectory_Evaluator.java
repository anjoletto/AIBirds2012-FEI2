/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package learner.util;


/**
 * Evaluate the performance of a learned trajectory model.
 * 
 * 
 * @author Kar Wai Lim
 *
 * Last updated: Long time ago
 */

public class Trajectory_Evaluator {
	
	private int n;				// number of data point
	
	private double xStart = 0;	// temporary storage for starting point of bird
	private double yStart = 0;	// ..
	
	private double sqErrors = 0; // temporary storage to sum the squared errors
	
	// copies of learned model (default to learner1)
	private Trajectory_Regression learner1;
	private Trajectory_Memoriser learner2;
	private int model = 1;		// 1 for learner1 and 2 for learner2
	
	public Trajectory_Evaluator(double[] para) {
		n = 0;
		model = 1;
		learner1 = new Trajectory_Regression();
		learner1.forcePara(para);
	}
	public Trajectory_Evaluator(Trajectory_Regression learner) {
		n = 0;
		model = 1;
		learner1 = new Trajectory_Regression();
		learner1.forcePara(learner.getParas());
	}
	public Trajectory_Evaluator(Trajectory_Memoriser learner) {
		n = 0;
		model = 2;
		this.learner2 = learner;
	}
	
	/** clear data points while retain the learning model */
	public void reset() {
		n = 0;
		xStart = 0;
		yStart = 0;
		sqErrors = 0;
	}
	
	// Do this for each bird released.
	// xStart and yStart should usually be negative
	/** add the starting point (bird released point) of a trajctory */ 
	public void addStartPoint(double xStart, double yStart) {
		this.xStart = xStart;
		this.yStart = yStart;
	}
	
	/** get current y coordinate given x coordinate using stored start points. */
	public double getYpoint(double xPoint) {
		double yTemp = Double.NaN;
		if(model == 1) {
			yTemp = learner1.predictPoint(xStart, yStart, xPoint);
			
			// check, remove when finish testing
			double[] para = learner1.getParas();
			double yTemp2 = para[0]*yStart + para[1]*xStart + para[2]*(yStart/xStart)*xPoint + para[3]*(yStart*yStart/xStart)*xPoint;
			yTemp2 += para[4]*yStart*xPoint + para[5]*xStart*xPoint + para[6]*yStart*xStart*xPoint + para[7]*xPoint*xPoint/(xStart*xStart);
			yTemp2 += para[8]*xPoint*xPoint/(yStart*yStart) + para[9]*xPoint*xPoint/(yStart*xStart);
			assert(yTemp == yTemp2);
			// =================================
			
		} else if(model == 2) {
			yTemp = learner2.predictPoint(xStart, yStart, xPoint);
		} else {
			System.out.println("Error with program coding!");
		}
		return yTemp;
	}
	
	/** Add a point of the bird's trajectory for evaluation given temporarily stored start points. */
	public void addTrajectoryPoint(double xPoint, double yPoint) {
		double yEstimate = this.getYpoint(xPoint);
		sqErrors += (yPoint-yEstimate)*(yPoint-yEstimate);
		n++;
	}
	
	public double getSqErrors() {
		return sqErrors;
	}
	public int get_n() {
		return n;
	}
	public double getAveError() {
		return sqErrors/n;
	}
	public int getModel() {
		return model;
	}
	
	public String toString() {
		String str = "";
		str += "The total squared errors are: " + sqErrors + "\n";
		str += "The total number of data points is: " + n + "\n";
		str += "Squared errors divided by n = " + sqErrors/n + "\n";
		return str;
	}
}
