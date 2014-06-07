/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package representation.util;

import io.trajectory.ABIO;
import io.trajectory.TrajectoryIO;

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import representation.ABObject;
import representation.APoint;
import vision.CreateVision;
import ab.framework.ai.ActionRobot;
import debug.Debug;
import env.Env;


/**
 * @author ABTeam_Gary
 * Util class used to collect trajectory points
 */
public class TrajectoryCollector extends ObjectCollector{
   
	protected ActionRobot ar;
	//protected ActionRobot robot;
	protected APoint startPoint;
	protected int x;
	protected int y;
	String filename = "im.png";
	public TrajectoryCollector() throws MatlabConnectionException, MatlabInvocationException
	{
		super();
		ar = new ActionRobot();

		ActionRobot ar = new ActionRobot();
		ar.screenShot(filename);
		CreateVision   myVision = new CreateVision(Env.getMatlabDir());
	    myVision.setImage(filename);
	    System.out.println("Begin to Segmentation (Calculate the reference point)");
		collect(myVision);
		 System.out.println("Segmentation Completed");
		x = (int) ((Env.getFocus_x() != -1)?Env.getFocus_x():((int)focusPoint.getX()));
		y = (int) ((Env.getFocus_y() != -1)?Env.getFocus_y():((int)focusPoint.getY()));
	}
	
    /**
     * @param release_x    the x coordinate of release point
     * @param release_y    the y coordinate of release point
     * @param filename     the file name of the image (the one will be segmented)
     * @param tio          input/output util will write data to the specified files
     */
    public void collectTrajectories(int release_x, int release_y, ABIO<APoint> tio)
    {
    	//myAction.makeMoveWithoutOffset((int)Env.getFocus_x(),(int)Env.getFocus_y(), release_x, release_y, true, 16);
		//ActionRobot.
    System.out.println("------------Start Collecting-------------------");
		ar.ZoomingOut();
		System.out.println("Shoot!!");
		ar.Makemove(x,y,release_x,release_y,15);
		ar.screenShot(filename);
		//myAction.captureScreenAndCrop();
		//myAction.captureScreen();
		ar.restartLevel(5);   // 
		startPoint = new APoint(x + release_x,y + release_y);
		saveToFiles(filename,tio);

    }
    private void saveToFiles(String imageName,ABIO<APoint> tio)
    {
    	double time = System.currentTimeMillis();
    	
      
         System.out.println("Begin to segmentation, it may take around 5-10 seconds......");
    	collect(imageName);
		ArrayList<APoint> trajectory_points = new ArrayList<APoint>();
		//Get Referent Point after normalization
		trajectory_points.add(this.refPoint);
		//trajectory_points.add(new APoint((startPoint.getX() - myAction.gameWindowStartX) / this.refLength,(startPoint.getY() - myAction.gameWindowStartY)/this.refLength));
		trajectory_points.add(new APoint(startPoint.getX()/this.refLength, startPoint.getY()/this.refLength));
		//
		for (Double obj:objs.keySet())
		{
			ABObject ob = (ABObject)( getObjs().get(obj));
			if (ob.getVision_name().equals("Trajectory"))
			{
				trajectory_points.add(ob.getCentroid());}
		
		}
		try {
			System.out.println("save data");
			tio.save(trajectory_points,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   System.out.println("-------------Collection Completed,time spent "+ (System.currentTimeMillis() - time)+"------------");
    }
    
	public void collectTrajectories(int n,String imageName,ABIO tio) throws IOException, MatlabConnectionException, MatlabInvocationException, AWTException, InterruptedException
	{
		

	for (int i = 0; i < n; i++)
	{
		Debug.echo(this, "begin to make action");
		//Please manually specify the target the third and fourth parameter, which is the point where the mouse will be dragged to.
		ar.ZoomingOut();
		ar.Makemove((int)Env.getFocus_x(), (int)Env.getFocus_y(),(300 - i * 50)- (int)Env.getFocus_x(), 475- (int)Env.getFocus_y(), 10);
	    ar.screenShot("im.png");
		
		//myAction.restartLevel();   // 
	    saveToFiles(imageName,tio);
	
	  
	}
	}
	public static void main(String args[]) 	{
		
		try {
			ABIO tio = new TrajectoryIO(("trapts_1015"));
		
			String filename = "im.png";
			TrajectoryCollector tc;
			tc = new TrajectoryCollector();
			 int startOffSetX = 30;
			 int endOffSetX = 100;
			 int startOffSetY = 30;
			 int endOffSetY = 100;
		/*	 for(int i = startOffSetX; i <= endOffSetX; i++)
				 for(int j = startOffSetY; j <=endOffSetY; j++)
				 {
					 tc.collectTrajectories((int)Env.getFocus_x() - i,(int)Env.getFocus_y() + j,filename,tio);
				 }*/
			 tc.collectTrajectories(-50,15,tio);
			
		} catch (MatlabConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
	}
	
	
	
}
