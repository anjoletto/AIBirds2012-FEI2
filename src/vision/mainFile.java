/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package vision;

import java.io.IOException;
import java.util.Arrays;

import env.Env;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;


public class mainFile {

	////////////////////////////Main Function Sample Describing Eval and Return From Matlab
	
	public static void main(String[] args) throws MatlabConnectionException, MatlabInvocationException, IOException
	{

		CreateVision myVision = new CreateVision(Env.getMatlabDir());  //directory path with matlab code files
		System.out.println("First Execution Might Take Some Time To Open The Matlab");
		myVision.image = "im.png"; //image to read
		
		
///////////////////////////////////////////////////////////////////////////////////////////////////////// Segmentation 
		
		Object segments[] = myVision.command("Give me current segmentation"); //Command 1
		double[][] objectReference = (double[][]) segments[0]; // contains the objectReference, ie [1]ObjectID in IntegerMap [2]ClusterID [3] Number of Pixels in the object
		double[][] integerMap = (double[][]) segments[1]; // contains the integerMap, each cell contains the object ID on that pixel

///////////////////////////////////////////////////////////////////////////////////////////////////////// Cluster Names 
		
		Object clusters[] = myVision.command("Give me cluster names"); //Command 2
		String[] clusterNames = (String[]) clusters[0]; // contains the clusterNames, 
	
/////////////////////////////////////////////////////////////////////////////////////////////////////// Display All Clusters' Pixels
		
		//myVision.command("Show all clusters"); //Command 3 // show everything detected
		
/////////////////////////////////////////////////////////////////////////////////////////////////////// Display Specific Cluster's Pixels
		
		//myVision.command("Show cluster number 2"); //Command 4 , for all objects in a cluster, change numbers as required

/////////////////////////////////////////////////////////////////////////////////////////////////////// Display All Object's Pixels
		
		//myVision.command("Show object number 10"); //Command 5 for the detected object, change numbers as required

/////////////////////////////////////////////////////////////////////////////////////////////////////// Bounding Box
		
		//Object box[] = myVision.command("Give bounding box of object 4"); //Command 6 bounding box for a given object, change numbers as required
		
		/*returns in the clock wise convention, ie box[0] is top, box[1] is right, ...etc */
		
		
			

		//System.out.println(Arrays.toString(integerMap[300])); //example
		System.out.println(Arrays.toString(clusterNames)); //example
		//System.out.println(box[0]); //example
		

		System.out.println("Total Objects Found: "+objectReference.length); //example
		
		for (int i=0; i<objectReference.length;i++){
			int objectID = (int) objectReference[i][0];
			int clusterID = (int) objectReference[i][1];
			int bodypixels = (int) objectReference[i][2];
			
			System.out.println(i + " - objectID: "+objectID+" - clusterID: "+clusterID + " - bodypixels: "+bodypixels); //example
			
				
			//if (clusterID==11){						// if bounding boxes required at a specific cluster 
					Object box[] = myVision.command("Give bounding box of object "+objectID); 
				myVision.command("plot "+box[0]+" "+box[1]+" "+box[2]+" "+box[3]);
			//}
				

		}
		String levelCheck = myVision.levelEndCheck(objectReference);
		System.out.println("result: "+levelCheck); //example

		myVision.command("plotted image");
		
		
		
	}// main function
	
	
} //class end
