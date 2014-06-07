/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package representation;

public class APoint {
	private double x;
	private double y;

	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public APoint(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public APoint(double result_x, double result_y) {
		// TODO Auto-generated constructor stub
		super();
		this.x = result_x;
		this.y = result_y;
	}

	public String toString() {
		return "APoint   x: " + x + "  y:  " + y;

	}

	public double distanceTo(APoint a) {
		return Math.sqrt(Math.pow(a.getX() - this.getX(), 2)
				+ Math.pow(a.getY() - this.getY(), 2));
	}
}
