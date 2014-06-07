/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package test;

import gui.Picture_Panel;
import io.trajectory.ABIO;
import io.trajectory.TrajectoryIO;

import java.awt.AWTException;
import java.io.IOException;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import planner.trajectory.manager.PlanTrajectory_Manager;
import representation.APoint;
import representation.util.ObjectCollector;
import representation.util.TrajectoryCollector;
import vision.CreateVision;
import debug.Debug;
import env.Env;
import exception.ExEmptySet;
import exception.plan.ExNoPlanResult;

public class TestMain {
 public static void main(String args[]) throws ExNoPlanResult
 {

	   String command = args[0];
	   if(command.equals("-traj"))
	   {
		   String trajType = args[1];
		   PlanTrajectory_Manager pm = new PlanTrajectory_Manager();
		   //System.out.println(birdtype.equals("-pb") + " "+birdtype);
		   if(trajType.equals("-na"))
		   {
			  // System.out.println(args[4]+"  "+args[4].equals("-data"));
			   if(args.length > 4)
			   {
				   
				  if(args[4].equals("-data")){
				   
				
				   String filename = args[5];
				   pm.loadData(filename);
				  }
			   }
			   
			   pm.loadPlannerMemo("NonAction");
		  
		   double target_x = Double.parseDouble(args[2]);
		   double target_y = Double.parseDouble(args[3]);
		   try {
			
			  
			   APoint result = pm.plan(target_x,target_y);
			   		System.out.println(" You can release point at: "+ result);
		} catch (ExNoPlanResult e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   }
	   }
	   else
		   if(command.equalsIgnoreCase("-naive"))
		   {
			   Debug.debugMode = false;
			   FeiAgentClient tpbbb = new  FeiAgentClient();
			   if(args.length > 1)
			   {
				  if(args[args.length - 1].equals("-debug"))
				  { 
					   Debug.debugMode = true;
				  
				  }
				
				 
				
				  if(args[1].equals("-data")&&args.length > 2)
				   tpbbb.setTrajectoryFileName(args[2]); //Trajectory File Name
				   
			   }
			  
			  
			   try {
				 
				   tpbbb.run();
	
			} catch (MatlabConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MatlabInvocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExEmptySet e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   
		   }
		   else
			   if(command.equalsIgnoreCase("-ctraj"))
			   {
				  Debug.debugMode = false;
				  String saveFileName = args[1];
				  int startOffSetX = Integer.parseInt(args[2]);
				  int endOffSetX = Integer.parseInt(args[3]);
				  int startOffSetY = Integer.parseInt(args[4]);
				  int endOffSetY = Integer.parseInt(args[5]);
				  if(args.length > 6)
				  {
					  if(args[6].equals("-debug"))
						  Debug.debugMode = true;
				  }
					try {
						ABIO tio = new TrajectoryIO(saveFileName);
				
						TrajectoryCollector tc;
						tc = new TrajectoryCollector();
						
					for(int i = startOffSetX; i <= endOffSetX; i++)
						for(int j = startOffSetY; j <=endOffSetY; j++)
						{
							
							tc.collectTrajectories(-i,j,tio);
						}
					System.out.println("done-------------");
					System.exit(0);
						
					} catch (MatlabConnectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MatlabInvocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
			   }
			   else if(command.equalsIgnoreCase("-seg"))
			   {
				   
				    Debug.debugMode = false;
				 
			
					try {
					    String filename = "";
						   
		    	        CreateVision myVision;
		    	        myVision = new CreateVision(Env.getMatlabDir());
		    	        filename = "im.png";
						myVision.setImage(filename);
					if(args.length > 1)
						{
						   filename = args[1];
						   myVision.setImage(filename);
						}
						
						ObjectCollector oc = new ObjectCollector();
						   System.out.println("Begin to Segmentation (around 5-10 seconds)");
						oc.collect(myVision);
						new Picture_Panel(Env.getMatlabDir() + filename,oc);
				
					
					
					} catch (MatlabConnectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MatlabInvocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    	      
					
				   
				   
			   }
	
		
	}
	
	
 
}
