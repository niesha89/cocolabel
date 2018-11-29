package ns.coco.cocolabel.utils;


import ns.coco.cocolabel.exception.CocoRuntimeException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

public class ImgUtils {
    public static BufferedImage loadImgFromFile(String imgfile) {
        File file = new File(imgfile);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (Exception e) {
            throw new CocoRuntimeException(e);
        }
        return bi;
    }

    public static BufferedImage loadImgFromBASE64(String base64) {
        //File file = new File(imgfile);
        ByteArrayInputStream is = new ByteArrayInputStream(ByteUtils.base64ToBytes(base64));
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(is);
        } catch (Exception e) {
            throw new CocoRuntimeException(e);
        }
        return bi;
    }

    public static float[][][] img2rgb(BufferedImage bi){

        int width = bi.getWidth();
        int height = bi.getHeight();
        int minx = bi.getMinX();
        int miny = bi.getMinY();
        //Color[][] allcolor=new Color[height-miny][width-minx];
        float[][][] rs = new float[height-miny][width-minx][3];
        for (int j = miny; j < height; j++) {
            for (int i = minx; i < width; i++) {
                int pixel = bi.getRGB(i, j); //
                Color temp = new Color(pixel);

                rs[j-miny][i-minx][0] = temp.getRed();
                rs[j-miny][i-minx][1] = temp.getGreen();
                rs[j-miny][i-minx][2] = temp.getBlue();

                //System.out.println(Arrays.toString(rs[j-miny][i-minx]));
            }
        }

        return rs;
    }
}
