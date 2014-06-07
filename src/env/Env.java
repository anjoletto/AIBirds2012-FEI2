/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package env;

import java.io.IOException;

import io.trajectory.EnvIO;

public class Env {
private static String matlabDir;
private static int focus_x = -1;
private static int focus_y = -1;

public static int getFocus_x() {
	return focus_x;
}

public static void setFocus_x(int focus_x) {
	Env.focus_x = focus_x;
}

public static int getFocus_y() {
	return focus_y;
}

public static void setFocus_y(int focus_y) {
	Env.focus_y = focus_y;
}
static
{
	 String separator = System.getProperty("file.separator");
     matlabDir	= "vision"+ separator+"Matlab"+separator;
     EnvIO eio = new EnvIO();
     try {
			eio.loadEnv("focus_pt");
	
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public static String getMatlabDir() {
	return matlabDir;
}

public static void setMatlabDir(String matlabDir) {
	Env.matlabDir = matlabDir;
}
public static String getSystemSeparator()
{
    return System.getProperty("file.separator");	
}



}