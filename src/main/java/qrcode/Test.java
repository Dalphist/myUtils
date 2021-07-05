package qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author DJF
 * @version 0.1.0
 * @Description
 * @create 2021-07-05 09:06
 * @since 0.1.0
 **/
public class Test {
    public static void main(String[] args) {

        // 生成二维码
        try {

            String content = "CD105123-FD5465";
            String path = "C:\\Users\\djf\\Desktop\\temp\\";
//            String fullPath = "C:\\Users\\39925\\Desktop\\temp\\aa.jpg";
//            String logoPath = "C:\\Users\\39925\\Desktop\\temp\\";
            String logoName = "logo.jpg";

		     MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

		     Map hints = new HashMap();
		     hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            //设置二维码四周白色区域的大小
            hints.put(EncodeHintType.MARGIN, 0);
            //设置二维码的容错性
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

		     BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400,hints);
		     File file1 = new File(path,"qrcode.jpg");
		     MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file1);

            // 生成流
            MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig();
            BufferedImage barCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);

            MatrixToImage4TextWriter.pressTest3(barCodeImage,"滑之尊虾滑S 500g", "规格：500g*16包", "生产日期：2021-1-23");
//            WriteQrCode.writeTextAndImageCode(content, "滑之尊虾滑S 500g", "规格：500g*16包", "生产日期：2021-1-23", fullPath, logoPath, logoName);
//            WriteQrCode.writeTextAndImageCode(content,"滑之尊虾滑S 500g", "规格：500g*16包", "生产日期：2021-1-23",fullPath, logoPath, logoName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
