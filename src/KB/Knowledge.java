package KB;


/**
 * @author ABTeam_Gary
 *
 */
public interface Knowledge {
// return the hierarchy of certain knowledge. used to create directory and track
//knowledge tree. the return type string will be replaced with complicated object later
public String  hierarchy();
public boolean hasChildren();//view knowledge hierarchy as a tree. The leaf node is the object that cannot be extended. like red-bird
public boolean save();
public boolean delete();
public boolean update();
}
