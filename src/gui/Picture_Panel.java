/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package gui;

import img_tool.ImageViewer;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import representation.ABObject;
import representation.util.ObjectCollector;


public class Picture_Panel extends JFrame{


	private JLabel inforlabel = new JLabel();
	private JLabel imageLabel = new JLabel();


    private ObjectCollector oc;
	public static String path;
	
	ArrayList<String> functions;

	int p_width;
	int p_height;
	static Image queryImage;



	public Picture_Panel(String path,ObjectCollector oc) throws Exception {

	
		setLayout(new BorderLayout());
		Image sourceImage = null;
		String sourceImagePath = path;
		try {
			sourceImage = ImageViewer.load(sourceImagePath);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Wrong Path,Please Input Again", " ", 1);
		}

		this.setTitle(sourceImagePath);
		queryImage = sourceImage;
		presentImageInThePanel(sourceImage);
		add(inforlabel, BorderLayout.NORTH);
		add(imageLabel, BorderLayout.CENTER);
       
		//AOSerializer ao = new AOSerializer<Active_Object>();
		
	//	Red_Bird rb = new Red_Bird(null);
	//	ao.load(rb.hierarchy());
		this.oc = oc;
		viewBoundingBox(oc.getObjs());
		setVisible(true);
        this.setResizable(true);
        //JOptionPane.showMessageDialog(null, " 3 \n"+ rb.hierarchy() + "\n has been detected");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	};
	public static Image getQueryImage() {
		return queryImage;
	}

	public static void setQueryImage(Image queryImage) {
		Picture_Panel.queryImage = queryImage;
	}

	public void presentImageInThePanel(Image image) {

		ImageIcon imageIcon = new ImageIcon(image);

		this.imageLabel.setIcon(imageIcon);

		p_width = imageIcon.getIconWidth();
		p_height = imageIcon.getIconHeight();
	//	this.imagePanel.setSize(p_width + 15, p_height + 15);
		this.inforlabel.setSize(p_width, 50);
		this.inforlabel.setText(p_width + "X" + p_height + "pixels");
		setSize(p_width, p_height + 50);
	}
	
	public void viewBoundingBox(Map<Double,ABObject> objs) {

		Graphics2D g = (Graphics2D) queryImage.getGraphics();

		g.setColor(Color.blue);
		BasicStroke basicStroke = new BasicStroke(1);

		g.setStroke(new BasicStroke());
	    Double[] target = {};
		for (Double key:objs.keySet())
	    {
	    	ABObject a0 = objs.get(key);
	    	
	    	 if(!a0.getVision_name().equals("Sky")){
	    		//System.out.println(" a0 "+ a0.getVision_name() + " startpoint "+ a0.getStartPoint() + " end point "+ a0.getEndPoint());
	    		/*  if((a0.getBoundBox().width > (CreateVision.noise_level)/oc.refLength 
	    				  && a0.getBoundBox().height > CreateVision.noise_level/oc.refLength
	    				  || a0.getVision_name().equals("Trajectory"))) //filter noise bounding box;
*/	    	 {
	    			  Rectangle rec = oc.displayRectangle(a0.getBoundBox());
	    			  if(rec!=null)
	    			  {
	    				  g.setColor(Color.blue);
	    				  if((int)a0.getVision_id() == 123 || (int)a0.getVision_id() == 125 ||(int)a0.getVision_id() == 121)
	    					 g.setColor(Color.red);
	    				
	    				  g.draw(basicStroke.createStrokedShape(rec));
	    				  g.setColor(Color.blue);
	    				/*  g.drawString((int)a0.getVision_id()+"",((int)oc.displayPoint(a0.getCentroid()).getX()) , 
	    						  ((int)oc.displayPoint(a0.getCentroid()).getY()));*/
	    				  //	g.drawString((objs.get(key).getVision_name()),((int)objs.get(key).getCentroid().getX()-20) , ((int)objs.get(key).getCentroid().getY()));
	    			    
	    			  }
	    	  }
	    	 }
	    }
	 /*   
	    Trajectory_Learner<Bird> tl = 	new Trajectory_Learner<Bird>();
	    HashMap<Double,ABW_Object> oobjs = oc.objs;
		tl.addStartPoint(176, 669);
		for (Double key:oobjs.keySet())
		{
			if((oobjs.get(key)).getVision_name().equals("5=Trajectory"))
			{
				tl.addTrajectoryPoint(oobjs.get(key).getCentroid());
				
			}
		}
	*/
		presentImageInThePanel(queryImage);
	}


}
