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
import solver.FeiSolver;
import vision.CreateVision;
import KB.object.bird.Plain_Bird;
import ab.framework.ai.ClientActionRobot;
import ab.framework.other.Shot;
import ab.framework.other.StateInfo;
import ab.framework.player.Configuration;
import debug.Debug;
import env.Env;
import exception.ExEmptySet;
import exception.ExKnowledgeNotExsist;
import exception.plan.ExNoPlanResult;

public class FeiAgentClient {
   
	private int focus_x;
	private int focus_y;
	private String trajectoryFileName = "trapts";
	private ClientActionRobot ar;
	private ObjectCollector oc;
    public FeiAgentClient()
    {
    	 ar = new ClientActionRobot();
    }
    public FeiAgentClient(String serverip)
    {
    	 ar = new ClientActionRobot(serverip);
    }


	public String getTrajectoryFileName() {
		return trajectoryFileName;
	}
	public void setTrajectoryFileName(String trajectoryFileName) {
		this.trajectoryFileName = trajectoryFileName;
	}
	public static void main(String args[]) throws MatlabConnectionException, MatlabInvocationException, IOException, AWTException, InterruptedException, ClassNotFoundException, ExKnowledgeNotExsist, ExNoPlanResult, ExEmptySet
   {
	   String server_ip = "localhost";
	   if(args.length > 0)
		   server_ip = args[0];
		   
	   FeiAgentClient na = new  FeiAgentClient(server_ip);
	   na.setTrajectoryFileName("trapts");
	   Debug.debugMode = false;	   
	   na.run();
	    
	    
	    
   }
   
   public void run() throws MatlabConnectionException, MatlabInvocationException, IOException, AWTException, InterruptedException, ExEmptySet, ExNoPlanResult
   {
	   System.out.println("use file: "+ this.getTrajectoryFileName() +" for trajectory planning");
	  
	   ar.configureWithResolution();
	   ar.loadLevel(-1);
	   while(true)
	   {
		   StateInfo state_info = bangbangbang();
		    Configuration conf = ar.getConfiguration();
		    if((state_info.getState().equalsIgnoreCase("WIN") || state_info.getState().equalsIgnoreCase("LOSE")))
		           ar.loadLevel(conf.getMax_level());
		   
	   }
	   
   }
   
   public StateInfo bangbangbang() throws MatlabConnectionException, MatlabInvocationException, IOException, AWTException, InterruptedException, ExEmptySet, ExNoPlanResult

   {
	   
     /*========Action Robot (GI Module), Capture the Game Window ==========*/

	   ar.screenShot("im.png");
	 
	   System.out.println("Begin to segmentation, it may take around 5-10 seconds......");
	   
	
	   /*============ Run Segementation, and Collect the Object ==========*/
 	    CreateVision cv = new CreateVision(System.getProperty("user.dir") + Env.getSystemSeparator() + Env.getMatlabDir());
 	    cv.image = "im.png";
 	    oc = new ObjectCollector();
        oc.collect(cv);
        Debug.echo(null,oc.printObjects());
        /*=========== From now on, you can access the objects in the scenario by calling oc.getObjs()=====*/
     
        
        /*Store centroid of target */
        System.out.println("-------- Locating Targets:No Pigs will be Forgiven------");
        LinkedList<APoint> targetpts = new LinkedList<APoint>();
        
        
  /*=========== Naive agent will shoot directly to the pigs. You can add more complicated rules here to choose the object=====*/

        FeiSolver ns = new FeiSolver();
        
        for (ABObject target: ns.getTargets(oc))
        {
        	   APoint target_pt = target.getCentroid();
               targetpts.add(target_pt);
               
        	   System.out.println("Targets: " + target);
      
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
	    
		ArrayList<Shot> shots = new ArrayList<Shot>();
	
		if(!targetpts.isEmpty())
		{
			 APoint tpt = targetpts.get(0);
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
		        /*=======Execute Actions using drag command =========================*/      
			    //ar.makeMove(focus_x, focus_y,  (int)screenPoint.getX() - focus_x, (int)screenPoint.getY() - focus_y, 10);
			    /* ====== Add the shot to shot sequence and then send to the server all the shot togehter ===== */
		       
		        //shots.add(new Shot(focus_x, focus_y,  (int)screenPoint.getX() - focus_x, (int)screenPoint.getY() - focus_y,(shots.size()+1) * 5000,0));
		       int tap_time = (int)(600 + Math.random() * 1000); 
		        shots.add(new Shot(focus_x, focus_y,  (int)screenPoint.getX() - focus_x, (int)screenPoint.getY() - focus_y,0,tap_time));
			}
			else
				System.out.println("Out of Knowledge");
			}
		  /*=======Execute Actions =========================*/      
		
		

			
		
			return   ar.shootWithStateInfoReturned(shots);
	   
   }


}
