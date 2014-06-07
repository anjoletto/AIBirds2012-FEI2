/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package io.trajectory;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import representation.APoint;

public class TrajectoryIO implements ABIO<APoint> {

	private String filename;

public TrajectoryIO(String filename) {
	this.filename = filename;
}
public TrajectoryIO() {
	// TODO Auto-generated constructor stub
}
/* (non-Javadoc)
 * @see io.trajectory.ABIO#load(java.lang.String)
 */
@Override
public  ArrayList<ArrayList<APoint>> load(String filename) throws IOException
{
	
	 ArrayList<ArrayList<APoint>> pointSet = new   ArrayList<ArrayList<APoint>>();
	File file = new File(filename);
	if(file.exists())
	{
		 ArrayList<APoint> points = new  ArrayList<APoint>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		while(br.ready())
		{
			String line = br.readLine();
		if(!line.contains("$"))
		{	// read in form of x,y
			double x = Double.parseDouble(line.substring(0,line.indexOf(" ")));
			String line2 = line.substring(line.indexOf(" ")+1);
			double y = Double.parseDouble(
											line2.substring(0,line.indexOf(" "))
										   );
			APoint apoint = new APoint(x,y);
			points.add(apoint);
		}
		else 
		{ 
			pointSet.add(points);
			points = new ArrayList<APoint>();
		}
		}
		return pointSet;
	}

		return null;
}

/* (non-Javadoc)
 * @see io.trajectory.ABIO#loadHashSet(java.lang.String)
 */
@Override
public  ArrayList<LinkedHashSet<APoint>> loadHashSet(String filename) throws IOException
{
	
	return loadHashSet(new File(filename));
}


/* (non-Javadoc)
 * @see io.trajectory.ABIO#loadHashSet(java.io.File)
 */
@Override
public  ArrayList<LinkedHashSet<APoint>> loadHashSet(File file) throws IOException
{
	
	 ArrayList<LinkedHashSet<APoint>> pointSet = new   ArrayList<LinkedHashSet<APoint>>();
	if(file.exists())
	{
		LinkedHashSet<APoint> points = new LinkedHashSet<APoint>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		while(br.ready())
		{
			String line = br.readLine();
		if(!line.contains("$"))
		{	// read in form of x,y
			double x = Double.parseDouble(line.substring(0,line.indexOf(" ")));
			String line2 = line.substring(line.indexOf(" ")+1);
			double y = Double.parseDouble(
											line2
										   );
			APoint apoint = new APoint(x,y);
			points.add(apoint);
		}
		else 
		{ 
			pointSet.add(points);
			points = new LinkedHashSet<APoint>();
		}
		}
		return pointSet;
	}

		return null;
}

/* (non-Javadoc)
 * @see io.trajectory.ABIO#save(java.util.List, boolean)
 */
@Override
public  void save(List<APoint> traj,boolean append) throws IOException 
{
	save(traj,filename,append);

}



/* (non-Javadoc)
 * @see io.trajectory.ABIO#save(java.util.List, java.lang.String, boolean)
 */
@Override
public  void save(List<APoint> traj,String filename,boolean append) throws IOException 
{
	
	File file = new File(filename);
	BufferedWriter br = new BufferedWriter(new FileWriter(file,append));
	//Debug.echo(this, traj.size(),traj);
	for (APoint apoint: traj)
	{
		//Debug.echo(this,apoint);
		br.append(apoint.getX()+" "+apoint.getY()+"\n");
	}
	br.append("$\n");
    br.close();


}

public static void main(String args[]) throws IOException
{
  //  for ( APoint point: TrajectoryIO.load("point"))
 //   	System.out.println(point);

}
@Override
public void save(Object objs, boolean append) {
	// TODO Auto-generated method stub
	
}
@Override
public void save(Object obj, String filename, boolean append)
		throws IOException {
	// TODO Auto-generated method stub
	
}
}
