/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package debug;

import java.lang.reflect.Method;


public class Debug {
 public static boolean debugMode = true;
 public static void echo(Object ob, Object... message)
 {
   if(debugMode){
	 String result = "";
	 for (int i = 0; i < message.length; i++)
		 result+= "  "+ message[i];
	 String classname = "";
	 if(ob==null)
		 classname = " main class";
	 else
		 classname = ob.getClass().getName();
 
	 System.out.println("Debug: In: "+ classname + " Message: " + result );
 }
 }
}
