/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package KB.io.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import KB.Knowledge;
import KB.io.KBIO;
import KB.object.Active_Object;

public class AOSerializer<T extends Active_Object> implements KBIO<T> {

	@Override
	public boolean save(Knowledge knowledge) {
	

  		String filename = "."+knowledge.hierarchy();
  		filename+=".obj";
  		
		ObjectOutputStream outputStream = null;
		try {
			File file = new File(filename);
			File dir = new File(file.getParent());
			if(!dir.exists())
				dir.mkdirs();
			FileOutputStream fos = new FileOutputStream(file);
			
			outputStream = new ObjectOutputStream(fos);
			outputStream.writeObject(knowledge);
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            //Close the ObjectOutputStream
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
		}
		
		return false;
	}

	@Override
	public T load(String knowledge_hierarchy) {
		// TODO Auto-generated method stub
		String filename;
		ObjectInputStream inputStream = null;
		
		filename = "."+knowledge_hierarchy + ".obj";
		File file = new File(filename);
		try {
			FileInputStream fis = new FileInputStream(file);
			inputStream = new ObjectInputStream(fis);
			Object obj = inputStream.readObject();
	      
	        T ao = (T)obj;
				
	        
	        return ao;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	

}
