/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package img_tool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class ImagePreview extends JPanel implements PropertyChangeListener {
     private JFileChooser jfc;
     private Image img;

     public ImagePreview(JFileChooser jfc) {
          this.jfc = jfc;
          Dimension sz = new Dimension(400,400);
          setPreferredSize(sz);
     }

     public void propertyChange(PropertyChangeEvent evt) {
         try {
          //     System.out.println("updating");
        	  
               File file = jfc.getSelectedFile();
            if (file!=null)
            {
              if (file.isFile())
                 updateImage(file);
            }
              } catch (IOException ex) {
                   System.out.println(ex.getMessage());
                   ex.printStackTrace();
          }
     }
     public void updateImage(File file) throws IOException {
      if(file == null) {
           return;
      }
      img = ImageIO.read(file);
      repaint();
     }
     public void paintComponent(Graphics g) {
           g.setColor(Color.gray);
           g.fillRect(0,0,getWidth(),getHeight());
      if(img != null) {
           int w = img.getWidth(null);
           int h = img.getHeight(null);
           String dim = w + " x " + h;
           int side = Math.max(w,h);
           double scale = 400.0/(double)side;
           
           w = scale>1?w:(int)(scale * (double)w);
           h = scale>1?h:(int)(scale * (double)h);
           g.drawImage(img,0,0,w,h,null);
   
          
           g.setColor(Color.white);
           g.drawString(dim,0,h);
      }
 }
      public static void main(String[] args) {
           JFileChooser jfc = new JFileChooser();
           ImagePreview preview = new ImagePreview(jfc);
           jfc.addPropertyChangeListener(preview);
           jfc.setAccessory(preview);
           jfc.showOpenDialog(null);
      }

}
