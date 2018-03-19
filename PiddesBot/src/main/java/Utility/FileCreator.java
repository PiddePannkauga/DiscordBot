
package Utility;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class for reading a file from disk and writing it back after it has been processed
 * @author Petter Månsson 2017-11-22
 */
public class FileCreator {
    private BufferedImage image;
    private File file;

    /**
     * Reads file from disk
     * @param fname
     * @return
     */
    public BufferedImage readFile(String fname){
        file = new File(fname);

        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    /**
     * Writes file to disk
     * @param image
     */
    public void writeProccesedImage(BufferedImage image,String op){
        try {
            ImageIO.write(image, "PNG", new File(op+file+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
