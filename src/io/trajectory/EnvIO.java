/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package io.trajectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import env.Env;

public class EnvIO {
   
	public void loadEnv(String property) throws NumberFormatException, IOException
   {
       File file = new File("Setup.ini");
       if(file.exists())
   		{
   			BufferedReader br = new BufferedReader(new FileReader(file));
   			while(br.ready())
   			{
   				String line = br.readLine();
   			    if(!line.contains("#"))
   			    {
   			    	if(line.contains(property)&property.equals("focus_pt"))
   			    	{
   			    		String str_xy = line.substring(line.lastIndexOf(":")+1);
   			    		String str_x = str_xy.substring(0,str_xy.lastIndexOf(","));
   			    		String str_y = str_xy.substring(str_xy.lastIndexOf(",")+1);
   			    		int x = Integer.parseInt(str_x);
   			    		int y = Integer.parseInt(str_y);
   			    		Env.setFocus_x(x);
   			    		Env.setFocus_y(y);
   			    		
   			    	}
   			    
   			    }
   			   
   			}
   		
   	}
   }
}
