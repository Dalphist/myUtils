package qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WriteQrCode {
    //生成的文件路径+文件名
    public static String newImageWithText = "";

    /**
     * guohao
     * <p>
     * 生成带图片+文字的二维码
     *
     * @param content 二维码内容
     * @param title   添加的文字标题
     * @param text    添加的文字内容
     * @param text2
     * @param fullPath
     * @param logoPath
     * @param logoName
     * @return
     */
    public static String writeTextAndImageCode(String content, String title, String text, String text2, String fullPath, String logoPath, String logoName) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

            @SuppressWarnings("rawtypes")
            Map hints = new HashMap();

            //设置UTF-8， 防止中文乱码
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            //设置二维码四周白色区域的大小
            hints.put(EncodeHintType.MARGIN, 0);
            //设置二维码的容错性
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            //存放二维码的文件夹
            // String path = "D:/qrcodeTest";

            //width:图片完整的宽;height:图片完整的高
            //因为要在二维码下方附上文字，所以把图片设置为长方形（高大于宽）
            int width = 352;//352
            int height = 500;//612
            //设置文字大小
            int fontSize = 30;

            //用来生成logo的图片位置和名字
            /*String photoPath = logoPath;
            String photoName = "uiblog.jpg";*/

            //画二维码，记得调用multiFormatWriter.encode()时最后要带上hints参数，不然上面设置无效
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            //qrcFile用来存放生成的二维码图片（无logo，无文字）
//            File logoFile = new File(logoPath + "/" + logoName);

            //开始画二维码
            BufferedImage barCodeImage = MatrixToImage4TextWriter.writeToFile(bitMatrix, "jpg");

//            //在二维码中加入图片
//            LogoConfig logoConfig = new LogoConfig(); //LogoConfig中设置Logo的属性
//            BufferedImage image = MatrixToImage4TextWriter.addLogo_QRCode(barCodeImage, logoFile, logoConfig);

            int fontStyle = 1; //字体风格

            // String filename = UUID.randomUUID().toString().replace("-", "");
            //String filepath = MyStringUtil.getFilePath(filename);

            //用来存放带有logo+文字的二维码图片
            /*String newImageWithText = path + filepath + "/" + filename + ".jpg";*/
            // newImageWithText = path + "/" + filename + ".jpg";

            //中间加logo调取这个
//            MatrixToImage4TextWriter.pressText(text, title, text2, fullPath, image, fontStyle, Color.black, fontSize, width, height);

            //拼接二维码和文本
            MatrixToImage4TextWriter.pressTest3(barCodeImage,title,text,text2);

            return newImageWithText;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newImageWithText;
    }

    /**
     * guohao
     * <p>
     * 生成带文字的二维码
     *
     * @param content 二维码内容
     * @param title   添加的文字标题
     * @param text    添加的文字内容
     * @return
     */
    public static String writeTextCode(String content, String title, String text, String text2,String fullPath) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

            @SuppressWarnings("rawtypes")
            Map hints = new HashMap();

            //设置UTF-8， 防止中文乱码
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            //设置二维码四周白色区域的大小
            hints.put(EncodeHintType.MARGIN, 0);
            //设置二维码的容错性
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //存放logo的文件夹
            String path = "D:/qrcodeTest";

            //width:图片完整的宽;height:图片完整的高
            //因为要在二维码下方附上文字，所以把图片设置为长方形（高大于宽）
            int width = 150;//352
            int height = 150;//612

            //设置文字大小
            int fontSize = 10; //30

            //画二维码，记得调用multiFormatWriter.encode()时最后要带上hints参数，不然上面设置无效
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            //qrcFile用来存放生成的二维码图片（无logo，无文字）
            File logoFile = new File(path, "tomcat.png");

            //开始画二维码
            BufferedImage barCodeImage = MatrixToImage4TextWriter.writeToFile2(bitMatrix, "jpg",592,384);

            int fontStyle = 1; //字体风格

            String filename = UUID.randomUUID().toString().replace("-", "");
            //String filepath = MyStringUtil.getFilePath(filename);

            //二维码全路径
            /*String newImageWithText = path + filepath + "/" + filename + ".jpg";*/
           // newImageWithText = path + "/" + filename + ".jpg";

            //在二维码下方添加文字（文字居中）
            MatrixToImage4TextWriter.pressText2(text, title, text2, fullPath, barCodeImage, fontStyle, Color.black, fontSize, 592, 384);
            return newImageWithText;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newImageWithText;
    }
}