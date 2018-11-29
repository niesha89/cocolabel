package ns.coco.cocolabel.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.lang3.StringUtils;

public class ChartHelper {

	private static String[] supported = { "JPG", "PNG", "GIF", "BMP", "JPEG" };

	protected String fileName;

	protected String type;

	protected int width;

	protected int height;

	protected BufferedImage image;

	public ChartHelper(String fileName) {
        this.fileName = fileName;
	}

	public ChartHelper init() {
		if (StringUtils.isEmpty(fileName)) {
			return this;
		}

		try {
			image = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return this;
		}
		// 获取原始图片宽高
		width = image.getWidth();
		height = image.getHeight();

		this.type = getFormatName(fileName);
		return this;
	}

	private static String getFormatName(String fileName) {
		try {
			// Create an image input stream on the image
			ImageInputStream iis = ImageIO.createImageInputStream(new File(fileName));

			// Find all image readers that recognize the image format
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) {
				// No readers found
				return null;
			}

			// Use the first reader
			ImageReader reader = iter.next();

			// Close stream
			iis.close();

			// Return the format name
			return reader.getFormatName();
		} catch (IOException e) {
			//
			e.printStackTrace();
		}

		// The image could not be read
		return null;
	}

	public ChartHelper output(String outputFilename) {

		return output(this.type, outputFilename);
	}

	public ChartHelper output(String type, String outputFilename) {

		try {
			ImageIO.write(image, type, new FileOutputStream(new File(outputFilename)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	public ChartHelper output(OutputStream os) throws IOException {
		ImageIO.write(image, type, os);
		return this;
	}

	/**
	 * 缩放
	 * 
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public ChartHelper scala(Integer newWidth, Integer newHeight) {
		if (newWidth == null) {
			newWidth = 0;
		}
		if (newHeight == null) {
			newHeight = 0;
		}
		if (newWidth <= 0 && newHeight <= 0) {
			return this;
		}
		if (newWidth <= 0) {
			newWidth = newHeight * width / height;
		} else if (newHeight <= 0) {
			newHeight = newWidth * height / width;
		}

		// 设置缩放图片流
		BufferedImage scaledBI = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

		// 设置缩放图片画笔
		Graphics2D g = scaledBI.createGraphics();
		g.setComposite(AlphaComposite.Src);

		// 绘制缩放图片
		g.drawImage(image, 0, 0, newWidth, newHeight, null);
		g.dispose();

		image = scaledBI;
		return this;
	}
	
	/**
	 * 图片旋转
	 */
	public ChartHelper rotate(int degree) {
		return rotate(degree, Color.white);
	}
	
	/**
	 * 图片旋转
	 */
	public ChartHelper rotate(int degree, Color bgcolor) {
		int iw = image.getWidth();// 原始图象的宽度
		int ih = image.getHeight();// 原始图象的高度
		int w = 0;
		int h = 0;
		int x = 0;
		int y = 0;
		degree = degree % 360;
		if (degree < 0)
			degree = 360 + degree;// 将角度转换到0-360度之间
		double ang = Math.toRadians(degree);// 将角度转为弧度

		/**
		 * 确定旋转后的图象的高度和宽度
		 */

		if (degree == 180 || degree == 0 || degree == 360) {
			w = iw;
			h = ih;
		} else if (degree == 90 || degree == 270) {
			w = ih;
			h = iw;
		} else {
			int d = iw + ih;
			w = (int) (d * Math.abs(Math.cos(ang)));
			h = (int) (d * Math.abs(Math.sin(ang)));
		}

		x = (w / 2) - (iw / 2);// 确定原点坐标
		y = (h / 2) - (ih / 2);
		BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
		Graphics2D gs = (Graphics2D) rotatedImage.getGraphics();
		if (bgcolor == null) {
			rotatedImage = gs.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.OPAQUE);
		} else {
			gs.setColor(bgcolor);
			gs.fillRect(0, 0, w, h);// 以给定颜色绘制旋转后图片的背景
		}

		AffineTransform at = new AffineTransform();
		at.rotate(ang, w / 2, h / 2);// 旋转图象
		at.translate(x, y);
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		op.filter(image, rotatedImage);
		image = rotatedImage;
		return this;
	}
	
	public void cutImage(String dest,int x,int y,int w,int h) throws IOException{   
        Iterator iterator = ImageIO.getImageReadersByFormatName(type);   
        ImageReader reader = (ImageReader)iterator.next();   

        ImageInputStream iis = ImageIO.createImageInputStream(image);   
        reader.setInput(iis, true);   
        ImageReadParam param = reader.getDefaultReadParam();   
        Rectangle rect = new Rectangle(x, y, w,h);    
        param.setSourceRegion(rect);   
        BufferedImage bi = reader.read(0,param); 
        
        ImageIO.write(bi, type, new File(dest));

 	}

 	public String getType() {
		return type;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public static void main(String[] args) {
		ChartHelper c = new ChartHelper("F:\\test.png");
		c.init().scala(1000, 0).rotate(45).output("jpg", "F:\\test2.jpg");
	}
}
