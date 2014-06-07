/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package representation.util;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import representation.ABObject;
import representation.APoint;
import vision.CreateVision;
import ab.framework.ai.ActionRobot;
import env.Env;
import gui.Picture_Panel;


/**
 * @author ABTeam_Gary
 *
 * @param <T>
 */
public class ObjectCollector {
    protected HashMap<Double,ABObject> objs;


	public CreateVision cv;
    public double refLength = 1;
    public APoint refPoint = new APoint(0,0);
    public APoint focusPoint = new APoint(0,0);
    public int birdCount = 0;
    public int pigCount = 0;
    public int score  = -1;
    
    
    public void collect(CreateVision cv)
    {

    	Object segments[];
    	this.cv = cv;
    	if(this.objs!=null)
        this.objs.clear();
		
		try {
				segments = this.cv.command("Give me current segmentation");
				Object clusters[] = this.cv.command("Give me cluster names");
		    	String[] clusterNames = (String[]) clusters[0];
		    	collectObject(segments, clusterNames);
		    	boundingBox(objs);
		        filter(objs);
		    	System.out.println("Segmentation Finished");
	    	
		} catch (MatlabConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
    
    /**
     * @param segments
     * @param clusterNames
     */ 
    public void collect(String imageName)
    {
    	Object segments[];
    	if(this.objs!=null)
        this.objs.clear();
		
		
		try {
				this.cv = new CreateVision(Env.getMatlabDir());
				this.cv.image = imageName;
				segments = this.cv.command("Give me current segmentation");
				Object clusters[] = this.cv.command("Give me cluster names");
		    	String[] clusterNames = (String[]) clusters[0];
		    	collectObject(segments, clusterNames);
		    	boundingBox(objs);
		    	filter(objs);
		    	//System.out.println(printObjects());
	    	
		} catch (MatlabConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
    }
    
    
    private void calRef(ABObject obj)
    {
   
	    	Object box[] = this.cv.getBoundingBox((int) (obj.getVision_id()));
	    	double sx = ((Double)(box[0])).intValue();
	    	double sy = ((Double)(box[1])).intValue();
	    	double ex = ((Double)(box[2])).intValue();
	    	double ey = ((Double)(box[3])).intValue();
	    if(this.focusPoint.getX() ==0 || sx < this.focusPoint.getX()){	
	    	this.refLength = Math.abs(
	    			sy - ey
	    			)/100;
	    	
	    	
	    	this.refPoint = new APoint(
	    	       (sx+ex)/(2*this.refLength), (sy+ey)/(2*this.refLength)		    			
	    			);		
	    	
	    	this.focusPoint = new APoint((sx + ex)/2, sy);
	    	
    	}
    }
    public HashMap<Double, ABObject> getObjs() {
		
    	return objs;
	}

	public void setObjs(HashMap<Double, ABObject> objs) {
		this.objs = objs;
	}

	private void collectObject(Object segments[],String clusterNames[])
    {
    /*	try {
    		
			score = (Integer)(cv.command("score")[0]);
			
		} catch (MatlabConnectionException e) {
			e.printStackTrace();
		} catch (MatlabInvocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		objs = new HashMap<Double,ABObject>();
  
        double[][] objref	= (double[][]) segments[0]; // OBJECT REFERENCE ID AT PIXEL X,Y
       
      
        int size_of_objects = objref.length;
    
        for (int i = 0; i < size_of_objects;i++)
        {    

            ABObject obj = new ABObject();
        	
  
        	
        	obj.setVision_id(objref[i][0]);
  
        	double cluster_id = (objref[i][1]-1);
            obj.setVision_name(clusterNames[(int) cluster_id]);
            if(obj.getVision_name().equalsIgnoreCase("Unbreakable Wood"))
            {
            	calRef(obj);
                //Debug.echo(this, " REF: ",this.refPoint);
            }
            
              	objs.put(obj.getId(), obj);
           
           	}
             
    }
    private void boundingBox(HashMap<Double,ABObject> boxobjs)
    {
        for(Double key:boxobjs.keySet())
        {
    
        	 
        	ABObject abobj = boxobjs.get(key);
        	if(abobj.getVision_name() == "Pig")
        		this.pigCount++;
        	else
        		if(abobj.getVision_name() == "Red Bird"||abobj.getVision_name() == "Blue Bird"||abobj.getVision_name() == "Yellow Bird")
        			this.birdCount ++;
        	Object box[] = this.cv.getBoundingBox((int) (abobj.getVision_id()));   
        	
        	double sx = ((Double)(box[0])).intValue()/this.refLength;
        	double sy = ((Double)(box[1])).intValue()/this.refLength;
        	double ex = ((Double)(box[2])).intValue()/this.refLength;
        	double ey = ((Double)(box[3])).intValue()/this.refLength;
        	
            abobj.setStartPoint(new APoint(sx,sy));
            abobj.setEndPoint(new APoint(ex,ey));
 
         
        }
    }
 
    public String printObjects()
    {
    	StringBuilder objects= new StringBuilder();
        for(Double key:objs.keySet())
        {	
        	ABObject obj = objs.get(key);
        	objects.append("  obj ID: " + obj.getVision_id() + " obj name: " + obj.getVision_name()+ "  Start_Point " + obj.getStartPoint() +
       		       "   End Point  "+obj.getEndPoint()+"\n"); 
        	}	
        return objects.toString();
    }

    private void filter(HashMap<Double,ABObject> objs)
    {
    	ArrayList<Double> noise_key = new ArrayList<Double>();
    	 for ( Double key: objs.keySet())
    	 {
    		 ABObject object = objs.get(key);
    		 if(  object.getStartPoint()==null
    	    		 ||object.getEndPoint() == null || (object.getCentroid().getY() < 100 / this.refLength && !object.getVision_name().equals("Trajectory")))
    			 noise_key.add(key);
    	 }
    	 for (Double key: noise_key)
    	 {
    		 objs.remove(key);
    	 }
    }
    public static void main(String args[]) throws MatlabConnectionException, MatlabInvocationException
    {
    
		    
	            ActionRobot ar = new ActionRobot();
	            ar.screenShot("im.png");
    	        CreateVision   myVision = new CreateVision(System.getProperty("user.dir") + Env.getSystemSeparator() + Env.getMatlabDir());
    	        myVision.setImage("im.png");
				ObjectCollector oc = new ObjectCollector();

               long time = System.currentTimeMillis();
				oc.collect(myVision);
                System.out.println(oc.birdCount);
	       
			
		
                String image_path = Env.getMatlabDir() + Env.getSystemSeparator() + "im.png";
		
				try {
					new Picture_Panel(image_path,oc);
				} 
				catch (Exception e1) {

					e1.printStackTrace();
				}
				
			System.out.println(System.currentTimeMillis() - time);
			
				}
				


	public APoint displayPoint(APoint point) {
	
		return new APoint(point.getX()*this.refLength, point.getY()*this.refLength);
	}


	public Rectangle displayRectangle(Rectangle boundBox) {
	
		//Debug.echo(this, this.refLength);
		Rectangle displayRec = new Rectangle((int)(boundBox.getX() * this.refLength), 
				(int)(boundBox.getY() * this.refLength),
				(int)(boundBox.getWidth()*this.refLength), (int)(boundBox.getHeight() * this.refLength));
		return displayRec;
	}
	
}
    

