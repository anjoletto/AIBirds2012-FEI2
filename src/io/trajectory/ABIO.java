/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package io.trajectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public interface ABIO<AB> {

	public  ArrayList<ArrayList<AB>> load(String filename)
			throws IOException;

	public  ArrayList<LinkedHashSet<AB>> loadHashSet(String filename)
			throws IOException;

	public  ArrayList<LinkedHashSet<AB>> loadHashSet(File file)
			throws IOException;

	public  void save(List<AB> objs, boolean append)
			throws IOException;

	public  void save(List<AB> objs, String filename, boolean append)
			throws IOException;
	
	public void save(Object obj,boolean append) throws IOException;
	
	public  void save(Object obj, String filename, boolean append)
			throws IOException;


}