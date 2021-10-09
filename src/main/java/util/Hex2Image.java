package util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
/**
 * @author DJF
 * @version 0.1.0
 * @Description
 * @create 2021-08-18 11:20
 * @since 0.1.0
 **/
public class Hex2Image {
    public static void main(String[] args) throws Exception {

        try{
            StringBuffer sb = new StringBuffer();
            FileInputStream fis = new FileInputStream("C:\\Users\\djf\\Desktop\\temp\\qrcode.jpg");

            File img = new File("C:\\Users\\djf\\Desktop\\temp\\qrcode.jpg");
            File img1 = new File("C:\\Users\\djf\\Desktop\\temp\\qrcode1.jpg");
            Image image = ImageIO.read(img);
            int srcH = image.getHeight(null);
            int srcW = image.getWidth(null);
            BufferedImage bufferedImage = new BufferedImage(srcW, srcH,BufferedImage.TYPE_3BYTE_BGR);
            bufferedImage.getGraphics().drawImage(image, 0,0, srcW, srcH, null);
            bufferedImage=new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY),null).filter (bufferedImage,null);
            FileOutputStream fos = new FileOutputStream(img1);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
            encoder.encode(bufferedImage);
            fos.close();


            BufferedInputStream bis = new BufferedInputStream(fis);
            java.io.ByteArrayOutputStream bos=new java.io.ByteArrayOutputStream();

            byte[] buff=new byte[1024];
            int len=0;
            while((len=fis.read(buff))!=-1){
                bos.write(buff,0,len);
            }
            //得到图片的字节数组
            byte[] result=bos.toByteArray();

            System.out.println("++++"+byte2HexStr(result));
            //字节数组转成十六进制
            String str=byte2HexStr(result);
            /*
             * 将十六进制串保存到txt文件中
             */
            PrintWriter   pw   =   new   PrintWriter(new   FileWriter("C:\\Users\\djf\\Desktop\\temp\\today.txt"));
            pw.println(str);
            pw.close();
        }catch(IOException e){
        }

    }
    /*
     *  实现字节数组向十六进制的转换方法一
     */
    public static String byte2HexStr(byte[] b) {
        String hs="";
        String stmp="";
        for (int n=0;n<b.length;n++) {
            stmp=(Integer.toHexString(b[n] & 0XFF));
            if (stmp.length()==1) {
                hs=hs+"0"+stmp;
            }
            else {
                hs=hs+stmp;
            }
        }
        return hs.toUpperCase();
    }

    private static byte uniteBytes(String src0, String src1) {
        byte b0 = Byte.decode("0x" + src0).byteValue();
        b0 = (byte) (b0 << 4);
        byte b1 = Byte.decode("0x" + src1).byteValue();
        byte ret = (byte) (b0 | b1);
        return ret;
    }
    /*
     *实现字节数组向十六进制的转换的方法二
     */
    public static String bytesToHexString(byte[] src){

        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();

    }

}
