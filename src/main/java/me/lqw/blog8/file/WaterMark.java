package me.lqw.blog8.file;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 水印
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class WaterMark {


    public void createMark(String filePath, String markContent, Color markContentColor, float qualNum){
        ImageIcon imageIcon = new ImageIcon(filePath);
        Image theImage = imageIcon.getImage();

        int width = theImage.getWidth(null);
        int height = theImage.getHeight(null);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(markContentColor);
        g.setBackground(Color.white);
        g.drawImage(theImage, 0, 0, null);
        g.drawString(markContent, width/3, height/20);
        g.dispose();

        try {
            FileOutputStream out = new FileOutputStream(filePath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
            param.setQuality(qualNum, true);
            encoder.encode(bufferedImage, param);
            out.close();
        } catch (Exception ex){
            return ;
        }
    }
    public static void main(String[] args) {
        WaterMark wm = new WaterMark();
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String valueOf = sdf.format(date);
        wm.createMark("/Users/liqiwen/Resources/图片/timg的副本2.jpeg",valueOf,Color.WHITE,100f);
    }
}
