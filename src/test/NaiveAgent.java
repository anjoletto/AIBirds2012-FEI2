/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package test;

import io.trajectory.ABIO;
import io.trajectory.TrajectoryIO;

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import learner.imp.NonAction_TrajectoryMemorizer;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import planner.imp.Shooter_Memoriser;
import representation.ABObject;
import representation.APoint;
import representation.util.ObjectCollector;
import solver.NaiveSolver;
import vision.CreateVision;
import KB.object.bird.Plain_Bird;
import ab.framework.ai.ActionRobot;
import debug.Debug;
import env.Env;
import exception.ExEmptySet;
import exception.ExKnowledgeNotExsist;
import exception.plan.ExNoPlanResult;

public class NaiveAgent {
   
	private int focus_x;
	private int focus_y;
	private int shootCount = 0;
	private int attemptsLeft = -1;
	private String trajectoryFileName = "trapts";
	private ObjectCollector oc;
    public NaiveAgent()
    {
 
    }
    


	public String getTrajectoryFileName() {
		return trajectoryFileName;
	}
	public void setTrajectoryFileName(String trajectoryFileName) {
		this.trajectoryFileName = trajectoryFileName;
	}
	public static void main(String args[]) throws MatlabConnectionException, MatlabInvocationException, IOException, AWTException, InterruptedException, ClassNotFoundException, ExKnowledgeNotExsist, ExNoPlanResult, ExEmptySet
   {
	 
	   NaiveAgent na = new  NaiveAgent();
	   na.setTrajectoryFileName("trapts");
	   Debug.debugMode = false;
	   na.run();
	    
	    
	    
   }
   
   public void run() throws MatlabConnectionException, MatlabInvocationException, IOException, AWTException, InterruptedException, ExEmptySet, ExNoPlanResult
   {
	   System.out.println("use file: "+ this.getTrajectoryFileName() +" for trajectory planning");
	   while(true)
	   {
		   bangbangbang();
	   }
   }
   
   public void bangbangbang() throws MatlabConnectionException, MatlabInvocationException, IOException, AWTException, InterruptedException, ExEmptySet, ExNoPlanResult

   {

     /** open a browser and load a game window  */
	   
	
     if(this.attemptsLeft == 0)
     {
    	   System.out.println("Game Finish");
 		   System.exit(0);
     }
     /*========Action Robot (GI Module), Capture the Game Window ==========*/
	   ActionRobot ar = new ActionRobot();
	   ar.ZoomingOut();
	   ar.screenShot("im.png");
	 
	   System.out.println("Begin to segmentation, it may take around 5-10 seconds......");
	   
	
	   /*============ Run Segementation, and Collect the Object ==========*/
 	    CreateVision cv = new CreateVision(Env.getMatlabDir());
 	    cv.image = "im.png";
 	    oc = new ObjectCollector();
        oc.collect(cv);
        System.out.println(" Current Score is: "+oc.score);
        Debug.echo(null,oc.printObjects());
        /*=========== From now on, you can access the objects in the scenario by calling oc.getObjs()=====*/
        if(this.attemptsLeft == -1)
        	this.attemptsLeft = oc.birdCount;
     
        if(checkEnd())
 	   {
 		   System.out.println("Game Finish");
 		   System.exit(0);
 	   }
 	 
        
     
     
        
        /*Store centroid of target */
        System.out.println("-------- Locating Targets:No Pigs will be Forgiven------");
        LinkedList<APoint> targetpts = new LinkedList<APoint>();
        
        
  /*=========== Naive agent will shoot directly to the pigs. You can add more complicated rules here to choose the object=====*/

        NaiveSolver ns = new NaiveSolver();
        
        for (ABObject target: ns.getTargets(oc.getObjs()))
        {
        	   APoint target_pt = target.getCentroid();
             
               
        	   targetpts.add(target_pt);
               
        	   System.out.println("Targets: " + target);
        	Debug.echo(null,"Target",target);
        }
        
        /*=========== Load the trajectory file for trajectory prediction====*/  
		ABIO tio = new TrajectoryIO();
		ArrayList<LinkedHashSet<APoint>> ps = tio.loadHashSet(this.getTrajectoryFileName());	
		/*=========== Train the trajectory learner====*/
		NonAction_TrajectoryMemorizer<Plain_Bird> ntm = new NonAction_TrajectoryMemorizer<Plain_Bird>();
		ntm.train(ps);
		ntm.setReferencePoint(oc.refPoint);
		
		Shooter_Memoriser shooter = new Shooter_Memoriser(ntm.getTrajReg());
		shooter.changePointTolerance(15);
		
	  
		/* Get the centroid of bird */
		 focus_x = (int) ((Env.getFocus_x() != -1)?Env.getFocus_x():((int)oc.focusPoint.getX()));
		 focus_y = (int) ((Env.getFocus_y() != -1)?Env.getFocus_y():((int)oc.focusPoint.getY()));
	    
		
		for (APoint tpt:targetpts)
		{
			
			  // do normalization 
			  APoint _tpt = ntm.normalise(tpt);
			  Debug.echo(null, " Reference Point is: ",oc.refPoint);
			  Debug.echo(null, " target",tpt,"normalized to ",_tpt );
			
			   /*=========== Get the release point from the trajectory prediction module====*/  
			   APoint releasePoint = shooter.getResult(_tpt.getX(),_tpt.getY());
			   
			   Debug.echo(this, "centroid of target",_tpt.getX(),_tpt.getY());
	       System.out.println("Shoot!!");
			if(releasePoint !=null)
			{
				Debug.echo(null, " Release Point before normalization: ",releasePoint);
			    releasePoint = ntm.inverse_normalise(releasePoint);
			    Debug.echo(null,"Release Point after normalization : ",releasePoint);
			    
			    APoint screenPoint = oc.displayPoint(releasePoint);
			    Debug.echo(null,"Screen Point: ",screenPoint);
		
		        Debug.echo(null,focus_x,focus_y, (int)screenPoint.getX(), (int)screenPoint.getY());
		        /*=======Execute Actions =========================*/      
			    ar.Makemove(focus_x, focus_y,  (int)screenPoint.getX() - focus_x, (int)screenPoint.getY() - focus_y, 10);
			    attemptsLeft--;
			   
			}
			else
				System.out.println("Out of Knowledge");
			}
		   
		    System.out.println(" This run finishes ----- wait 3 seconds(state might not be static at the moment) -----");
			try {
				Thread.sleep(6 * 500);
			} catch (InterruptedException e) {
			}
		
		
			   
	   
   }
   /* End when neither birds or pigs are present. And shootCount > num of Inibirds*/
   private boolean checkEnd()
   {
	   System.out.println(" There are "+oc.pigCount + " pigs and we have " +   this.attemptsLeft + " attempts left ");
	   if((oc.birdCount == 0||oc.pigCount == 0 ))
		   return true;
	   else
		   return false;
	   
	   
	   
   }


}
