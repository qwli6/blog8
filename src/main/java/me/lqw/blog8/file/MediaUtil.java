package me.lqw.blog8.file;


import me.lqw.blog8.file.meta.AudioMetaInfo;
import me.lqw.blog8.file.meta.ImageMetaInfo;
import me.lqw.blog8.file.thread.PrintStream;
import me.lqw.blog8.file.thread.ProcessKiller;
import me.lqw.blog8.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.file.Files;
import java.sql.Time;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 媒体工具处理类
 * 处理视频，流媒体, 图片
 * 利用 ffpemg 来处理音视频
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class MediaUtil {


    private static final Logger logger = LoggerFactory.getLogger(MediaUtil.class.getName());

    //图片
    public static final String PNG = "png";
    public static final String JPG = "JPG";
    public static final String JPEG = "JPEG";
    public static final String GIF = "GIF";
    public static final String WEBP = "WEBP";

    //视频
    public static final String MP4 = "MP4";
    public static final String MOV = "MOV";
    public static final String WMV = "WMV";

    //音频
    public static final String AAC = "AAC";

    /**
     * 可以处理的视频格式
     */
    private final static String[] VIDEO_TYPE = new String[]{MP4, WMV};

    /**
     * 可以处理的图片格式
     */
    private final static String[] IMAGE_TYPE = new String[]{JPG, JPEG, PNG, GIF};


    /**
     * 可以处理的音频格式
     */
    private final static String[] AUDIO_TYPE = new String[]{AAC};


    /**
     * 视频帧抽取时的默认时间点，第10s（秒）
     * （Time类构造参数的单位:ms）
     */
    private static final Time DEFAULT_TIME = new Time(0, 0, 10);

    /**
     * 视频帧抽取的默认宽度值，单位：px
     */
    private static final int DEFAULT_WIDTH = 320;

    /**
     * 视频帧抽取的默认时长，单位：s（秒）
     */
    private static final int DEFAULT_TIME_LENGTH = 10;

    /**
     * 抽取多张视频帧以合成gif动图时，gif的播放速度
     */
    private static final int DEFAULT_GIF_PLAYTIME = 110;


    /**
     * FFmpeg程序执行路径
     * 当前系统安装好ffmpeg程序并配置好相应的环境变量后，值为ffmpeg可执行程序文件在实际系统中的绝对路径
     */
    private static String FFMPEG_PATH = "/Users/liqiwen/develop/ffmpeg-20200831-4a11a6f-macos64-static/bin/ffmpeg"; // /usr/bin/ffmpeg

    /**
     * 构造方法
     */
    private MediaUtil() {
        super();
    }

    /**
     * 判断文件是否是以 jpeg 结尾的文件
     *
     * @param ext ext
     * @return boolean
     */
    public static boolean isJPEG(String ext) {
        return JPEG.equalsIgnoreCase(ext) || JPG.equalsIgnoreCase(ext);
    }

    /**
     * 判断文件是否是 png 结尾的文件
     *
     * @param ext ext
     * @return Boolean
     */
    public static boolean isPNG(String ext) {
        return PNG.equalsIgnoreCase(ext);
    }

    /**
     * 判断文件是否是 gif 结尾的文件
     *
     * @param ext ext
     * @return boolean
     */
    public static boolean isGIF(String ext) {
        return GIF.equalsIgnoreCase(ext);
    }

    /**
     * 判断文件是否是 webp 结尾的文件
     *
     * @param ext ext
     * @return boolean
     */
    public static boolean isWEBP(String ext) {
        return WEBP.equalsIgnoreCase(ext);
    }

    /**
     * 判断文件是否是可被处理的图片
     *
     * @param ext ext
     * @return boolean
     */
    public static boolean isProcessableImage(String ext) {
        return isGIF(ext) || isPNG(ext) || isJPEG(ext) || isWEBP(ext);
    }

    /**
     * 判断文件是否是可被处理的视频
     *
     * @param ext ext
     * @return boolean
     */
    public static boolean isProcessableVideo(String ext) {
        return MP4.equalsIgnoreCase(ext) || MOV.equalsIgnoreCase(ext);
    }

    public static File toWebpFile(String imageFilePath) {
        File imageFile = new File(imageFilePath);

        File webpFile = new File(imageFilePath + ".webp");

        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            ImageIO.write(bufferedImage, WEBP, webpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return webpFile;
    }

    /**
     * 图片缩放处理
     *
     * @param srcImg     src
     * @param destImg    destImage
     * @param width      width
     * @param height     height
     * @param equalScale equalScale
     */
    public static void resize(File srcImg, File destImg, int width, int height, boolean equalScale) {
        Optional<String> extOp = FileUtil.getExtension(srcImg);
        if (extOp.isPresent()) {

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


    public static boolean detectFfempgAvailable() {
        File file = new File(FFMPEG_PATH);
        if(!file.exists()){
            logger.error("--- 工作状态异常，因为传入的ffmpeg可执行程序路径下的ffmpeg文件不存在！ ---");
            return false;
        }
        List<String> commands = new ArrayList<>(1);
        commands.add("-version");
        String ffmpegVersionStr = executeCommand(commands);
        if (StringUtil.isBlank(ffmpegVersionStr)) {
            logger.error("--- 工作状态异常，因为ffmpeg命令执行失败！ ---");
            return false;
        }
        logger.info("--- 工作状态正常 ---");
        return true;
    }


    /**
     * 执行FFmpeg命令
     * @param commands 要执行的FFmpeg命令
     * @return FFmpeg程序在执行命令过程中产生的各信息，执行出错时返回null
     */
    public static String executeCommand(List<String> commands) {
        if (CollectionUtils.isEmpty(commands)) {
            logger.error("--- 指令执行失败，因为要执行的FFmpeg指令为空！ ---");
            return null;
        }
        LinkedList<String> ffmpegCmds = new LinkedList<>(commands);
        ffmpegCmds.addFirst(FFMPEG_PATH); // 设置ffmpeg程序所在路径
        logger.info("--- 待执行的FFmpeg指令为：---" + ffmpegCmds);

        Runtime runtime = Runtime.getRuntime();
        Process ffmpeg = null;
        try {
            // 执行ffmpeg指令
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(ffmpegCmds);
            ffmpeg = builder.start();
            logger.info("--- 开始执行FFmpeg指令：--- 执行线程名：" + builder.toString());

            // 取出输出流和错误流的信息
            // 注意：
            // 必须要取出ffmpeg在执行命令过程中产生的输出信息，如果不取的话当输出流信息填满 jvm 存储输出留信息的缓冲区时，
            // 线程就回阻塞住
            PrintStream errorStream = new PrintStream(ffmpeg.getErrorStream());
            PrintStream inputStream = new PrintStream(ffmpeg.getInputStream());

            errorStream.start();
            inputStream.start();
            // 等待ffmpeg命令执行完
            ffmpeg.waitFor();

            // 获取执行结果字符串
            String result = errorStream.getStringBuffer().append(inputStream.getStringBuffer()).toString();

            // 输出执行的命令信息
            String cmdStr = Arrays.toString(ffmpegCmds.toArray()).replace(",", "");
            String resultStr = StringUtil.isBlank(result) ? "【异常】" : "正常";
            logger.info("--- 已执行的FFmepg命令： ---" + cmdStr + " 已执行完毕, 执行结果： " + resultStr);
            return result;

        } catch (Exception e) {
            logger.error("--- FFmpeg命令执行出错！ --- 出错信息： " + e.getMessage());
            return null;

        } finally {
            if (null != ffmpeg) {
                ProcessKiller ffmpegKiller = new ProcessKiller(ffmpeg);
                // JVM 退出时，先通过钩子关闭 FFmepg 进程
                runtime.addShutdownHook(ffmpegKiller);
            }
        }
    }


    /**
     * 设置 ffmpegPath
     * @param ffmpegPath ffmpegPath
     * @return true | false
     */
    public static boolean setFfmpegPath(String ffmpegPath) {
        if(StringUtil.isBlank(ffmpegPath)){
            logger.error("MediaUtil >>>>>>>  设置 ffmpeg 执行路径失败, 传入的 ffmpeg 可执行程序路径为空");
            return false;
        }
        File ffmpegFile = new File(ffmpegPath);
        if(!ffmpegFile.exists()){
            logger.error("MediaUtil >>>>>> 设置 ffmpeg 执行路径失败, 传入的路径转换成文件不存在");
            return false;
        }
        FFMPEG_PATH = ffmpegPath;
        logger.info("MediaUtil >>>>>> 设置 ffmpeg 执行路径成功! 当前 ffmpeg 可执行程序路径为: [{}] ", ffmpegPath);
        return true;
    }


    /**
     * 从视频中提取多少帧做成动图
     * @param videoFile videoFile
     * @param start 起始时间
     * @param end 结束时间
     * @param frameCount 帧数
     */
    public static void extractGifFromVideo(File videoFile, long start, long end, int frameCount) {

        if(videoFile == null || !videoFile.exists()){
            logger.error("视频文件不存在");
            return;
        }
    }


    /***
     * 处理视频 | 视频转换
     * @param fileInput 原视频路径
     * @param fileOutput 转换后的视频路径
     * @param withAudio 是否携带音频，true 保留音频，false 不保留
     * @param crf 指定视频的质量系数（值越小，视频质量越高，体积越大；该系数取值为0-51，直接影响视频码率大小）,取值参考
     * @param preset 指定视频的编码速率（速率越快压缩率越低），取值参考
     * @param width 视频宽度；为空则保持源视频宽度
     * @param height 视频高度；为空则保持源视频高度
     */
    public static void processVideo(File fileInput, File fileOutput, boolean withAudio, Integer crf, String preset,
                                    Integer width, Integer height){

        if(fileInput == null || !fileInput.exists() || !fileInput.isFile()){
            logger.error("原视频不存在, 无法处理");
            return;
        }

        if(fileOutput == null){
            throw new RuntimeException("目标视频地址不存在, 请检查地址是否正确");
        }

        //目标地址不存在，创建一个文件
        if(!fileOutput.exists()){
            try {
                boolean newFile = fileOutput.createNewFile();
                if(!newFile){
                    throw new RuntimeException("文件创建失败, 无法创建处理视频地址");
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("创建文件失败, 视频无法被处理");
            }
        }

        Optional<String> extOptional = FileUtil.getExtension(fileInput);

        if(extOptional.isPresent()){
            String ext = extOptional.get();

            if(Arrays.stream(VIDEO_TYPE).anyMatch(e -> e.equalsIgnoreCase(ext))){

                List<String> commands = new ArrayList<>();
                commands.add("-i");
                commands.add(fileInput.getAbsolutePath()); //获取视频绝对路径
                if(!withAudio){
                    //去掉音频
                    commands.add("-an"); //加上此参数表示去除音频
                }

                if(null != width && width > 0 && null != height && height > 0){
                    //宽高有值
                    commands.add("-s"); //重新设置宽高
                    commands.add(width.toString() + "x" + height.toString());
                }

                // 指定输出视频文件时使用的编码器
                commands.add("-vcodec");

                // 指定使用x264编码器
                commands.add("libx264");

                // 当使用x264时需要带上该参数
                commands.add("-preset");

                // 指定preset参数
                commands.add(preset);

                // 指定输出视频质量
                commands.add("-crf");

                // 视频质量参数，值越小视频质量越高
                commands.add(crf.toString());

                // 当已存在输出文件时，不提示是否覆盖
                commands.add("-y");
                // 文件输出地址
                commands.add(fileOutput.getAbsolutePath());

                executeCommand(commands);
            }
        }
    }


    /**
     * 获取音频元数据
     * @param audioFile audioFile
     * @return AudioMetaInfo
     */
    public static AudioMetaInfo getAudioMetaInfo(File audioFile) {
        if(null == audioFile || !audioFile.exists()){
            logger.error("无法获取音频信息, 传入文件为空");
            return null;
        }

        //获取音频信息字符串
        String metaInfoString = getMetaInfoFromFfmpeg(audioFile);


        long duration = 0; //音频持续时间
        Integer audioBitRate = 0; //音频码率
        long sampleRate = 0; //音频采样率
        String audioFormat = ""; //音频格式
        long audioSize = audioFile.length();



        AudioMetaInfo audioMetaInfo = new AudioMetaInfo();
        audioMetaInfo.setAudioSize(audioSize);

        return audioMetaInfo;
    }


    /**
     * 获取图片的元信息（从流中获取）
     * @param inputStream inputStream
     * @return ImageMetaInfo
     */
    public static ImageMetaInfo getImageInfo(InputStream inputStream){
        Assert.notNull(inputStream, "inputStream must be not null.");
        BufferedImage image;
        ImageMetaInfo imageMetaInfo = new ImageMetaInfo();
        try {

            image = ImageIO.read(inputStream);
            imageMetaInfo.setWidth(image.getWidth());
            imageMetaInfo.setHeight(image.getHeight());
            imageMetaInfo.setSize(Long.parseLong(String.valueOf(inputStream.available())));

            return imageMetaInfo;
        } catch (IOException ex){
            ex.printStackTrace();
            logger.error("MediaUtil >>>>>> 从流中获取图片元信息失败:[{}]", ex.getMessage(), ex);
        }

        return new ImageMetaInfo(-1, -1, -1);
    }


    /**
     * 从文件中获取图片信息
     * @param imageFile imageFile
     * @return ImageMetaInfo
     */
    public static ImageMetaInfo getImageInfo(File imageFile) {

        BufferedImage image;
        ImageMetaInfo imageMetaInfo = new ImageMetaInfo();

        try{
            if (null == imageFile || !imageFile.exists()) {
                return null;
            }
            image = ImageIO.read(imageFile);
            imageMetaInfo.setWidth(image.getWidth());
            imageMetaInfo.setHeight(image.getHeight());
            imageMetaInfo.setSize(imageFile.length());
//            imageMetaInfo.setFormat(getFormat(imageFile));

            return imageMetaInfo;

        } catch (IOException ex){
            ex.printStackTrace();
            logger.error("MediaUtil >>>>>> 从文件中获取图片元信息失败:[{}]", ex.getMessage(), ex);
        }

        return new ImageMetaInfo(-1, -1, -1);
    }


    /**
     * 获取视频的元信息 ffmpeg -i 原视频.mp4
     * @param inputFile inputFile
     * @return String
     *
     * 不推荐用
     */
    public static String getMetaInfoFromFfmpeg(File inputFile) {
        if(inputFile == null || !inputFile.exists()){
            throw new RuntimeException("");
        }
        List<String> commands = new ArrayList<>();
        commands.add("-i");
        commands.add(inputFile.getAbsolutePath());

        return executeCommand(commands);
    }



    public static void main(String[] args) {

//        boolean b = detectFfempgAvaliable();
//        System.out.println(b);


        String metaInfoFromFfmpeg = getMetaInfoFromFfmpeg(new File("/Users/liqiwen/Resources/图片/1547447428020839.mp4"));
        System.out.println("视频信息为：" + metaInfoFromFfmpeg);

//        processVideo(new File("/Users/liqiwen/Resources/图片/1547447428020839.mp4"),
//                new File("/Users/liqiwen/Resources/图片/newVideo.mp4"), true, 25, )
    }


    /**
     * 获取系统中 ffmpeg 安装的位置
     * @return string
     */
    public static String getFfmpegPath(){
        return FFMPEG_PATH;
    }
}
