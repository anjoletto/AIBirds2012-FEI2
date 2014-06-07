/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package representation;
import java.awt.Rectangle;
import java.util.LinkedList;

import debug.Debug;

import vision.CreateVision;


public class ABObject {

	protected APoint startPoint;
	protected double angle;
	protected double vision_id;
	protected String vision_name;
	public double getVision_id() {
		return vision_id;
	}
	public void setVision_id(double vision_id) {
		this.vision_id = vision_id;
	}
	public String getVision_name() {
		return vision_name;
	}
	public void setVision_name(String vision_name) {
		this.vision_name = vision_name;
	}
	protected APoint endPoint;
	protected APoint centroid;
	protected Integer[][] integerMap;      //store pixels in that object, optional,memory consumption 
	


	protected LinkedList<ABObject> tangential_part;

	public APoint getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(APoint startPoint) {
		this.startPoint = startPoint;
	}
	public APoint getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(APoint endPoint) {
		this.endPoint = endPoint;
	}
	public ABObject(APoint startPoint, APoint endPoint, int angle, int id) {
		super();
		this.setStartPoint(startPoint);
		this.setEndPoint(endPoint);
		this.setAngle(angle);
		this.setId(id);
	}
	public ABObject(ABObject o)
	{
		this.setAngle(o.getAngle());
		this.setId(o.getId());
        this.startPoint = o.startPoint;
        this.endPoint = o.endPoint;
		this.setTangential_part(o.getTangential_part());
		this.setCentroid(o.getCentroid());
		this.setVision_name(o.getVision_name());
		this.setVision_id(o.getVision_id());
		
		}
	public ABObject(int id, APoint startPoint, double angle, 
			APoint endPoint, APoint centroid,
			LinkedList<ABObject> tangential_part) {
		super();
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.angle = angle;
		this.vision_id = id;
	
		this.centroid = centroid;
		this.tangential_part = tangential_part;
	}
	public ABObject(double vision_id, String vision_name,APoint startPoint, 
			APoint endPoint) {
		super();
		this.startPoint = startPoint;
		this.vision_id = vision_id;
		this.vision_name = vision_name;
		this.endPoint = endPoint;
	}
	public ABObject() {
		// TODO Auto-generated constructor stub
	}
	public double getId() {
		return vision_id;
	}

	public void setId(double id) {
		this.vision_id = id;
	}

	public LinkedList<ABObject> getTangential_part() {
		return tangential_part;
	}


	public void setTangential_part(LinkedList<ABObject> tangential_part) {
		this.tangential_part = tangential_part;
	}



	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle2) {
		this.angle = angle2;
	}
	public APoint getCentroid() {
		if(centroid ==null)
			{
			
			//Debug.echo(this, this.startPoint);
			//Debug.echo(this, this.endPoint);
			 centroid = new APoint((this.startPoint.getX() + this.endPoint.getX())/2, (this.startPoint.getY() + this.endPoint.getY())/2);
			}
		return centroid;
	}
	public void setCentroid(APoint centroid) {
		this.centroid = centroid;
	}
    public String toString()
    {
    	return " The Object ID is " + this.getId();
    }
    public Rectangle getBoundBox()
    {
	    if(this.startPoint!=null){
	    	
	    	int width = (int) Math.abs(this.startPoint.getX() - this.endPoint.getX());
	    	int height = (int) Math.abs(this.startPoint.getY() - this.endPoint.getY());
	   // 	System.out.println(" The Object ID is: "+this.getVision_id() + "  width " + width + " height " + height);
	    	
	    	return new Rectangle((int)this.startPoint.getX(),(int)this.startPoint.getY(),width,height);
	    }
	    return null;
    }
    /**
     * assuming, the object fits bounding box exactly. 
     * return false if o is not neighbour [ bounding box ]
     * **/
    public boolean tryToAddNeighbour(ABObject o)
    {
    	//If neighbour, assuming, the object fits bounding box exactly. 
    	double diff_x = Math.abs(this.getCentroid().getX() - o.getCentroid().getX());
    	double diff_y = Math.abs(this.getCentroid().getY() - o.getCentroid().getY());
    	double threshold_x = (this.getBoundBox().getWidth() + o.getBoundBox().getWidth())/2 + CreateVision.noise_level;
    	double threshold_y = (this.getBoundBox().getHeight() + o.getBoundBox().getHeight())/2 + CreateVision.noise_level;
    	if(threshold_x > diff_x && threshold_y > diff_y)
    		return true;
    	return false;
    	
    	
    }
       
}