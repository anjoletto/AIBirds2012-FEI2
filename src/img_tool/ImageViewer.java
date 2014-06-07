/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package img_tool;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.media.jai.PlanarImage;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
public class ImageViewer  {

  
 public ImageViewer(){}	
 public static Image load(byte[] data) throws Exception
  {
    Image image = null;
    SeekableStream stream = new ByteArraySeekableStream(data);
    String[] names = ImageCodec.getDecoderNames(stream);
    ImageDecoder dec = 
    ImageCodec.createImageDecoder(names[0], stream, null);
    RenderedImage im = dec.decodeAsRenderedImage();
    image = PlanarImage.wrapRenderedImage(im).getAsBufferedImage();
    return image;
  }
  public static Image load(String path) throws Exception{
	   
	
	  FileInputStream in = new FileInputStream(path);
	    FileChannel channel = in.getChannel();
	    ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
	    channel.read(buffer);

	    Image  image = load(buffer.array());
	  
        return image;
  
  }
  
 }


