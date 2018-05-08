package chromossomes.ohyeh;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.paim.commons.Image;
import org.paim.commons.ImageConverter;
import org.paim.commons.ImageFactory;

/**
 * Class for comparing two images
 */
public class ImageComparer {

    public static void main(String[] args) throws Exception {
//        for (String string : new File("C:\\Users\\Pichau\\Desktop\\test").list()) {
//            String news = string.replace(".bmp", "_.png");
//
//            Image image = ImageFactory.buildRGBImage(ImageIO.read(new File("C:\\Users\\Pichau\\Desktop\\test\\" + string)));
//            for (int x = 0; x < image.getWidth(); x++) {
//                for (int y = 0; y < image.getHeight(); y++) {
//                    boolean e = !(image.get(0, x, y) == 255 && image.get(1, x, y) == 0 && image.get(2, x, y) == 0);
//                    image.set(0, x, y, e ? 255 : 0);
//                    image.set(1, x, y, e ? 255 : 0);
//                    image.set(2, x, y, e ? 255 : 0);
//                }
//            }
//            
//            ImageIO.write(ImageConverter.toBufferedImage(image), "png", new File("C:\\Users\\Pichau\\Desktop\\test\\" + news));
//
//        }
        
        Image i = ImageFactory.buildRGBImage(ImageIO.read(new File("C:\\Users\\Pichau\\Desktop\\test\\wat.png")));
        Image e = ImageFactory.buildRGBImage(ImageIO.read(new File("C:\\Users\\Pichau\\Desktop\\test\\2.bmp")));
        System.out.println(new ImageComparer().compare(i, e));
        
    }

    /**
     * Compares two images
     *
     * @param result
     * @param expected
     * @return ImageCompareResult
     */
    public ImageCompareResult compare(Image result, Image expected) {
        if (result.getWidth() != expected.getWidth() || result.getHeight() != expected.getHeight()) {
            return new ImageCompareResult(0);
        }
        double correct = 0;
        for (int x = 0; x < result.getWidth(); x++) {
            for (int y = 0; y < result.getHeight(); y++) {
                boolean r = result.get(0, x, y) > 0;
                boolean e = !(expected.get(0, x, y) == 255 && expected.get(1, x, y) == 0 && expected.get(2, x, y) == 0);
                if (r == e) {
                    correct++;
                }
            }
        }
        correct = (correct / (result.getWidth() * result.getHeight()));
        return new ImageCompareResult(correct);

    }

}
