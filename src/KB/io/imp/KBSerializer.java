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
import exception.ExKnowledgeNotExsist;

/**
 * @author ABTeam_Gary
 *
 * @param <T> The knowledge type you want to save
 */
public class KBSerializer<T extends Knowledge> implements KBIO<T> {

	@Override
	public boolean save(Knowledge knowledge) {
	

  		String filename = "."+knowledge.hierarchy();
  		
  		filename+=".obj";
  		System.out.println(filename);
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
	public T load(String knowledge_hierarchy) throws IOException, ClassNotFoundException, ExKnowledgeNotExsist {
		// TODO Auto-generated method stub
		String filename;
		ObjectInputStream inputStream = null;
		T ao;
		if(!knowledge_hierarchy.contains(".obj"))
			filename = "."+knowledge_hierarchy + ".obj";
		else
			filename = knowledge_hierarchy;
		File file = new File(filename);
		if(!file.exists())
		    //throw new ExKnowledgeNotExsist();
		{
			ao = null;
		}
		else {
			FileInputStream fis = new FileInputStream(file);
		
		inputStream = new ObjectInputStream(fis);
		Object obj = inputStream.readObject();
        ao = (T)obj;
	}
        return ao;
		
		
		
	}
	

}
