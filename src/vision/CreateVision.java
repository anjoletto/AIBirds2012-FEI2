/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package vision;


import java.io.IOException;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;
import env.Env;

public class CreateVision {

    //Create a proxy, which we will use to control MATLAB
	public static MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder().setUsePreviouslyControlledSession(true).build();
	public static MatlabProxyFactory factory = new MatlabProxyFactory(options);
	public String globalPath = "empty";
	public String image="im.png";
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	public static double noise_level = 5;
		
	
	public CreateVision(String path) throws MatlabConnectionException, MatlabInvocationException{
		

	    MatlabProxy proxy = factory.getProxy();
	    proxy.eval("addpath('"+ path +"')");
	    proxy.eval("readSpriteColors();");

	    proxy.disconnect();
	    globalPath = path;
	}
	
	public Object[] getBoundingBox(int object_id)
	{
	
		Object[] returnObject = new Object[4];
		try {
			
			 MatlabProxy	proxy = factory.getProxy();
            
			proxy.eval("[r,c,v]=find(integerMap=="+object_id+");miny=min(r);minx=min(c);maxy=max(r);maxx=max(c);");
			returnObject[0] = ((double[]) proxy.getVariable("minx"))[0]; 
			returnObject[1] = ((double[]) proxy.getVariable("miny"))[0];
			returnObject[2] = ((double[]) proxy.getVariable("maxx"))[0];
			returnObject[3] = ((double[]) proxy.getVariable("maxy"))[0];
			  proxy.disconnect();
			return returnObject; 
		} catch (MatlabInvocationException e) {
		
			e.printStackTrace();
		} catch (MatlabConnectionException e) {
		
			e.printStackTrace();
		}
		return returnObject; 
		

		
		
	}

	public Object[] command(String args) throws MatlabConnectionException, MatlabInvocationException, IOException
	{
		MatlabProxy proxy = factory.getProxy(); // this won't open a new Matlab session 
		Object[] returnObject = new Object[4];
		
		
		if ( args=="score" ) {

			proxy.eval("finalScore = getScore('"+image+"');");
		    
			int score = (int)((double[]) proxy.getVariable("finalScore"))[0];
			
		    returnObject[0] = score;

			
		}else if( args=="gameState" ) {

			proxy.eval("stateNum = gameState('"+image+"');");
		    
			int state = (int)((double[]) proxy.getVariable("stateNum"))[0];
			
		    returnObject[0] = state;

			
		}else if ( args=="Give me current segmentation" ) {
		
			proxy.eval("[objectReference,integerMap, image]=segmentation('"+image+"');");

		    //Get the array from MATLAB
		    MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
		    MatlabNumericArray matlabArray = processor.getNumericArray("objectReference");
		    
		    //Convert to a Java array and print the same value again    
		    double[][] objectReference = matlabArray.getRealArray2D();
		    
		    matlabArray = processor.getNumericArray("integerMap");
		    double[][] integerMap = matlabArray.getRealArray2D();
		    
		    
		    returnObject[0] = objectReference;
		    returnObject[1] = integerMap;
		
		}else if(args=="Give me cluster names"){
			String[] clusterNames={"Sky","Red Bird","Wood","Ground","Trajectory","Tap","Pig", "Ice", "Stone", "Unbreakable Wood", "Blue Bird", "Yellow Bird"};
			
			returnObject[0] = clusterNames;
			
		}else if(args=="Show all clusters"){

			proxy.eval("imsave(imshow(integerMap))");
		    
		    returnObject[0] = 0;
		    
		}else if(args=="plotted image"){

			proxy.eval("imsave(imshow(image));");
		    returnObject[0] = 0;

		}else if("plot ".equals(args.substring(0, 5))){
			String[] tokens = args.split(" ");

			//System.out.println("image("+tokens[1]+":"+(tokens[1]+3)+","+tokens[4]+":"+tokens[2]+",1)=1;");
			proxy.eval("image("+tokens[1]+","+tokens[4]+":"+tokens[2]+",1:3)=0;");
			proxy.eval("image("+tokens[3]+","+tokens[4]+":"+tokens[2]+",1:3)=0;");
			
			proxy.eval("image("+tokens[1]+":"+tokens[3]+","+tokens[2]+",1:3)=0;");
			proxy.eval("image("+tokens[1]+":"+tokens[3]+","+tokens[4]+",1:3)=0;");
		    
		    returnObject[0] = 0;

		}else if("Show object number ".equals(args.substring(0, 19))){//"Show object number "
			
			proxy.eval("imsave(imshow(ismember(integerMap,"+args.substring(19)+")));");
		    
		    returnObject[0] = 0;

		}else if("Show cluster number ".equals(args.substring(0, 20))){//"Show cluster number "
			
			proxy.eval("[r,c,v] = find(objectReference(:,2,:)=="+args.substring(20)+");imsave(imshow(ismember(integerMap,objectReference(r,1,:))));");
		    
		    returnObject[0] = 0;

		}else if("Give bounding box of object ".equals(args.substring(0, 28))){
			
			proxy.eval("[r,c,v]=find(integerMap=="+args.substring(28)+");miny=min(r);minx=min(c);maxy=max(r);maxx=max(c);");
		    
			int miny = (int)((double[]) proxy.getVariable("miny"))[0];
			int maxx = (int)((double[]) proxy.getVariable("maxx"))[0];
			int maxy = (int)((double[]) proxy.getVariable("maxy"))[0];
			int minx = (int)((double[]) proxy.getVariable("minx"))[0];
			
			returnObject[0] = miny; 
			returnObject[1] = maxx;
			returnObject[2] = maxy;
			returnObject[3] = minx;
			
			
		}
		
		
		
		
	    //Disconnect the proxy from MATLAB
	    proxy.disconnect();

	    
		return returnObject;

	}

	public String levelEndCheck(double[][] objectReference) throws MatlabConnectionException 
	{
		String result = "initialized";
		int pigCount = 0;
		int clusterID = 0;
		int bodypixels = 0;
		int lostPigFound = 0;


		for (int i=1; i<objectReference.length && pigCount<2;i++){
			clusterID = (int) objectReference[i][1];
			bodypixels = (int) objectReference[i][2];
			if (clusterID == 7){
				pigCount++;				
				if (bodypixels > 13000 ){
					lostPigFound++;
				}	
			}
		}
		
		if (pigCount == 0){
			result = "WIN";
		}else if(pigCount == 1 && lostPigFound>0){
			result = "LOSE";			
		}else{
			result = "PLAYING";			
		}

		
		return result;
	}


	
	


	
	
}
