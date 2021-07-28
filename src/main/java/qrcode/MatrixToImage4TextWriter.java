package qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MatrixToImage4TextWriter {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int GRAY = 0xC0C0C0;

    private MatrixToImage4TextWriter() {
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height - 50; y++) {
                image.setRGB(x, y, matrix.get(x, y + 50) ? BLACK : WHITE);
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = height - 50; y < height; y++) {
                image.setRGB(x, y, WHITE);
            }
        }
        return image;
    }


    public static BufferedImage writeToFile(BitMatrix matrix, String format)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        return image;
    }

    public static BufferedImage writeToFile2(BitMatrix matrix, String format, int width1, int height1)
            throws IOException {

        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width1; x++) {
            for (int y = 0; y < height1; y++) {
                image.setRGB(x, y, WHITE);
            }
        }

        for (int x = 183 + 56; x < width1 - 26; x++) {
            image.setRGB(x, 162, BLACK);
            image.setRGB(x, 162 + 54, BLACK);
            image.setRGB(x, 162 + 108, BLACK);

        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x + 22, y + 132, matrix.get(x, y) ? BLACK : WHITE);
            }
        }

        return image;
    }


    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    /**
     * 给二维码图片添加Logo
     *
     * @param barCodeImage
     * @param logoPic
     */
    public static BufferedImage addLogo_QRCode(BufferedImage barCodeImage, File logoPic, LogoConfig logoConfig) {
        try {
            if (!logoPic.isFile()) {
                System.out.print("file not find !");
                throw new IOException("file not find !");
            }

            /**
             * 读取二维码图片，并构建绘图对象
             */
            Graphics2D g = barCodeImage.createGraphics();

            /**
             * 读取Logo图片
             */
            BufferedImage logo = ImageIO.read(logoPic);

            int widthLogo = barCodeImage.getWidth() / logoConfig.getLogoPart();
            int heightLogo = barCodeImage.getWidth() / logoConfig.getLogoPart(); //保持二维码是正方形的

            // 计算图片放置位置
            int x = (barCodeImage.getWidth() - widthLogo) / 2;
            int y = (barCodeImage.getHeight() - heightLogo) / 2 - 50;


            //开始绘制图片
            g.drawImage(logo, x, y, widthLogo, heightLogo, null);
            g.drawRoundRect(x, y, widthLogo, heightLogo, 10, 10);
            g.setStroke(new BasicStroke(logoConfig.getBorder()));
            g.setColor(logoConfig.getBorderColor());
            g.drawRect(x, y, widthLogo, heightLogo);

            g.dispose();
            return barCodeImage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param pressText 文字
     * @param newImg    带文字的图片
     * @param image     需要添加文字的图片
     * @param fontStyle
     * @param color
     * @param fontSize  文字大小
     * @param width
     * @param height
     * @为图片添加文字
     */
    public static void pressText(String pressText, String title, String text2, String newImg, BufferedImage image, int fontStyle, Color color, int fontSize, int width, int height) {

        //计算文字开始的位置
        //x开始的位置：（图片宽度-字体大小*字的个数）/2
        int startX = (width - (fontSize * pressText.length())) / 2;
        int startX2 = (width - (fontSize * title.length())) / 2;
        int startX3 = (width - (fontSize * text2.length())) / 2;
       /* int startX = 2;
        int startX2 = 2;
        int startX3 = 2;*/
        //y开始的位置：图片高度-（图片高度-图片宽度）/2
        int startY = height - (height - width) / 2;

        System.out.println("startX: " + startX);
        System.out.println("startX2: " + startX2);
        System.out.println("startY: " + startY);
        System.out.println("height: " + height);
        System.out.println("width: " + width);
        System.out.println("fontSize: " + fontSize);
        System.out.println("pressText.length(): " + pressText.length());

        try {
//            File file = new File(targetImg);
//            Image src = ImageIO.read(file);
            int imageW = image.getWidth();
            int imageH = image.getHeight();
//            BufferedImage image = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(image, 0, 0, imageW, imageH, null);
            g.setColor(color);
            g.setFont(new Font("粗体", fontStyle, 20));

            //标题
            g.drawString(title, startX2, startY);
            //内容
            g.drawString(pressText, startX, startY + 30);
            //第三行内容
            g.drawString(text2, startX3, startY + 60);
            g.dispose();

            FileOutputStream out = new FileOutputStream(newImg);
            ImageIO.write(image, "JPEG", out);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
            System.out.println("image press success");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public static void pressText2(String pressText, String title, String text2, String newImg, BufferedImage image, int fontStyle, Color color, int fontSize, int width, int height) {

        //计算文字开始的位置
        //x开始的位置：（图片宽度-字体大小*字的个数）/2
        int startX = 185;//(width - (fontSize * pressText.length())) / 2;
        int startX2 = 185;//(width - (fontSize * title.length())) / 2;
        int startX3 = 185;//(width - (fontSize * text2.length())) / 2;
       /* int startX = 2;
        int startX2 = 2;
        int startX3 = 2;*/
        //y开始的位置：图片高度-（图片高度-图片宽度）/2
        int startY = 156;//height - (height - width) / 2;

        System.out.println("startX: " + startX);
        System.out.println("startX2: " + startX2);
        System.out.println("startY: " + startY);
        System.out.println("height: " + height);
        System.out.println("width: " + width);
        System.out.println("fontSize: " + fontSize);
        System.out.println("pressText.length(): " + pressText.length());

        try {
//            File file = new File(targetImg);
//            Image src = ImageIO.read(file);
            int imageW = image.getWidth();
            int imageH = image.getHeight();
//            BufferedImage image = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
            //文字的标题
            Graphics g2 = image.createGraphics();
            g2.drawImage(image, 0, 0, imageW, imageH, null);
            g2.setColor(color);
            g2.setFont(new Font("粗体", fontStyle, 20));

            //标题
            g2.drawString("名称：", startX2, startY);
            //内容
            g2.drawString("编码：", startX, startY + 54);
            //第三行内容
            g2.drawString("购置：", startX3, startY + 108);
            g2.dispose();

            //文字的内容
            Graphics g = image.createGraphics();
            g.drawImage(image, 0, 0, imageW, imageH, null);
            g.setColor(color);
            g.setFont(new Font("粗体", fontStyle, 18));

            //标题
            g.drawString(title, startX2+60+5, startY);
            //内容
            g.drawString(pressText, startX+60+5, startY + 54);
            //第三行内容
            g.drawString(text2, startX3+60+5, startY + 108);
            g.dispose();



            FileOutputStream out = new FileOutputStream(newImg);
            ImageIO.write(image, "JPEG", out);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
            System.out.println("image press success");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public static void pressTest3(BufferedImage barCodeImage, String title, String text, String text2) {
        String newImg = "C:\\Users\\djf\\Desktop\\temp\\result.png";
        try {
            BufferedImage imgNew = new BufferedImage(700, 500, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = imgNew.createGraphics();
            g2d.setColor(Color.WHITE);//设置笔刷白色
            g2d.fillRect(0,0,700,500);//填充整个屏幕
            g2d.setColor(Color.BLACK); //设置笔刷
            g2d.drawImage(barCodeImage, 50, 250, 200, 200, null);

            //标题样式
            g2d.setFont(new Font("粗体", 1, 50));
            //标题
            g2d.drawString(title, 50, 180);

            //内容样式
            g2d.setFont(new Font("粗体", 1, 45));

            g2d.drawString("箱码号：1654", 370, 80);

            //内容样式
            g2d.setFont(new Font("黑体", 1, 33));
            //内容2
            g2d.drawString(text, 310, 280);
            //内容3
            g2d.drawString(text2, 310, 440);

            g2d.dispose();


            FileOutputStream out = new FileOutputStream(newImg);
            ImageIO.write(imgNew, "JPEG", out);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(imgNew);
            out.close();
            System.out.println("生成完成");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
