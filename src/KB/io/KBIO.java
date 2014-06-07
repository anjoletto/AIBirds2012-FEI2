/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package KB.io;

import java.io.FileNotFoundException;
import java.io.IOException;

import exception.ExKnowledgeNotExsist;

import KB.Knowledge;
import KB.object.Active_Object;

/**
 * @author ABTeam_Gary
 * @interface I/O interface
 * @parameter T: return type
 */
public interface KBIO<T> {
	//
	/**
	 * @param knowledge  the knowledge that would be saved
	 * @return  true save successfully
	 *          false fail to successfully
	 */
	public boolean save(Knowledge knowledge);
	//given the directory of knowledge, load it
	
	/**
	 * @param hierarchy valid knowledge hierarchy
	 * @return Knowledge of Type T
	 * @throws FileNotFoundException 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws ExKnowledgeNotExsist       No knowledge exists under the specified hierarchy
	 */
	public T load(String hierarchy) throws FileNotFoundException, IOException, ClassNotFoundException, ExKnowledgeNotExsist;
}
