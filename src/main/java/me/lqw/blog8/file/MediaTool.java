package me.lqw.blog8.file;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * 媒体工具处理类
 * 处理视频，流媒体, 图片
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class MediaTool {

    public static final String PNG = "png";
    public static final String JPG = "JPG";
    public static final String JPEG = "JPEG";
    public static final String GIF = "GIF";
    public static final String WEBP = "WEBP";
    public static final String MP4 = "MP4";
    public static final String MOV = "MOV";


    private MediaTool() {
        super();
    }


    public static boolean isJPEG(String ext){
        return JPEG.equalsIgnoreCase(ext) || JPG.equalsIgnoreCase(ext);
    }

    public static boolean isPNG(String ext){
        return PNG.equalsIgnoreCase(ext);
    }

    public static boolean isGIF(String ext){
        return GIF.equalsIgnoreCase(ext);
    }

    public static boolean isWEBP(String ext) {
        return WEBP.equalsIgnoreCase(ext);
    }

    public static boolean isProcessableImage(String ext){
        return isGIF(ext) || isPNG(ext) || isJPEG(ext) || isWEBP(ext);
    }

    public static boolean isProcessableVideo(String ext){
        return MP4.equalsIgnoreCase(ext) || MOV.equalsIgnoreCase(ext);
    }

    public ImageInfo readImageInfo(Path src) {


        return new ImageInfo();
    }

    public static File toWebpFile(String imageFilePath){
        File imageFile = new File(imageFilePath);

        File webpFile = new File(imageFilePath + ".webp");

        try{
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            ImageIO.write(bufferedImage, WEBP, webpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return webpFile;
    }



    public static void resize(File srcImg, File destImg, int width, int height, boolean equalScale){
        Optional<String> extOp = FileUtils.getExtension(srcImg);
        if(extOp.isPresent()) {

            if (width < 0 || height < 0) {
                return;
            }

            BufferedImage srcImage = null;
            try {
                srcImage = ImageIO.read(srcImg);
                System.out.println("srcImg size=" + srcImage.getWidth() + "X" + srcImage.getHeight());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            // targetW，targetH分别表示目标长和宽
            BufferedImage target = null;
            double sx = (double) width / srcImage.getWidth();
            double sy = (double) height / srcImage.getHeight();
            // 等比缩放
            if (equalScale) {
                if (sx > sy) {
                    sx = sy;
                    width = (int) (sx * srcImage.getWidth());
                } else {
                    sy = sx;
                    height = (int) (sy * srcImage.getHeight());
                }
            }
            System.out.println("destImg size=" + width + "X" + height);
            ColorModel cm = srcImage.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();

            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
            Graphics2D g = target.createGraphics();
            // smoother than exlax:
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.drawRenderedImage(srcImage, AffineTransform.getScaleInstance(sx, sy));
            g.dispose();
            // 将转换后的图片保存
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(target, extOp.get(), baos);
                FileOutputStream fos = new FileOutputStream(destImg);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args){
//        String url = "/Users/liqiwen/Resources/图片/timg的副本2.jpeg";
//
//        File file = toWebpFile(url);
//
//        try {
//            Files.copy(file.toPath(), Paths.get("/Users/liqiwen/Resources/图片/"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(file.getName());

        resize(new File("/Users/liqiwen/Resources/图片/timg的副本2.jpeg"),
                new File("/Users/liqiwen/Resources/图片/thumb/timg的副本2.jpeg"), 200, 200, false);
    }
}
