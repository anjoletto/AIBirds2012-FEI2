/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package ab.framework.ai;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;

import ab.framework.server.Server;
import ab.framework.server.commands.ClickCommand;
import ab.framework.server.commands.DragCommand;
import ab.framework.server.commands.MouseWheelCommand;
import ab.framework.server.commands.ScreenshotCommand;
import env.Env;

public class ActionRobot {
private static Server server;
public ActionRobot()
{
if(server==null){	
	try {
		server = new Server(9000) {
			@Override
			public void onOpen() {
				System.out.println("Client connected");
			}

			@Override
			public void onClose() {
				System.out.println("Client disconnected");
			}
		};
		server.start();

		System.out.println("Server started on port: " + server.getPort());

		System.out.println("Waiting for client to connect");
		server.waitForClients(1);
		
		
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
public void ZoomingOut()
{
	System.out.println("Zooming out");
	for (int i = 0; i < 15; i++) {
		server.send(new MouseWheelCommand(-1));
	}
	
	System.out.println("Waiting 2 seconds for zoom animation");
	try {
		Thread.sleep(2000);
	} catch (InterruptedException e) {
	}	

}
public void Makemove(int x, int y, int toX,int toY,int wait)
{
	server.send(new DragCommand(x, y, toX, toY));	
	try {
		Thread.sleep(wait * 500);
	} catch (InterruptedException e) {
	}


}
public void Click(int x, int y, int wait)
{
	server.send(new ClickCommand(x, y));	
	try {
		Thread.sleep(wait * 500);
	} catch (InterruptedException e) {
	}


}
public void restartLevel(int wait)
{
	server.send(new ClickCommand(158,50));	
	try {
		Thread.sleep(wait * 500);
	} catch (InterruptedException e) {
	}

}
public void screenShot(String imageName)
{
	byte[] imageBytes = server.send(new ScreenshotCommand());
	FileOutputStream f;
	try {
		f = new FileOutputStream(new File(Env.getMatlabDir() +imageName));
		//System.out.println(Env.getMatlabDir() +imageName+".png");
		f.write(imageBytes);
		f.close();
		System.out.println("Screenshot saved");
	} catch (IOException e) {
	}	

}
public static void main(String args[])
{
  ActionRobot ar = new ActionRobot();
  ar.Click(229, 568, 5);
  System.exit(0);
}
}
