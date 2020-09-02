package me.lqw.blog8.util;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputField;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.springframework.util.Assert;
import org.thymeleaf.util.StringUtils;

import java.io.*;
import java.util.List;

/**
 * JPEG 图片工具类
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
public class FileUtil {

    private FileUtil() {
        super();
    }

    public static byte[] file2byte(File file){

        Assert.notNull(file, "target file must not be empty.");

        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e){
            e.printStackTrace();
        }
        return buffer;

    }



    /**
     * 移除 jpeg 图片中的 exif 信息
     * @param bytes bytes
     * @return byte[]
     * @throws Exception Exception
     */
    public static byte[] removeExif(final byte[] bytes) throws Exception {
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            TiffOutputSet outputSet = null;
            final ImageMetadata metadata = Imaging.getMetadata(bytes);
            if (!(metadata instanceof JpegImageMetadata)) {
                return bytes;
            }

            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            final TiffImageMetadata exif = jpegMetadata.getExif();
            if (null != exif) {
                outputSet = exif.getOutputSet();
            }

            if (null == outputSet) {
                return bytes;
            }

            final List<TiffOutputDirectory> directories = outputSet.getDirectories();
            for (final TiffOutputDirectory directory : directories) {
                final List<TiffOutputField> fields = directory.getFields();
                for (final TiffOutputField field : fields) {
                    if (!StringUtil.equalsIgnoreCase("Orientation", field.tagInfo.name)) {
                        outputSet.removeField(field.tagInfo);
                    }
                }
            }

            new ExifRewriter().updateExifMetadataLossless(bytes, os, outputSet);
            return os.toByteArray();
        }
    }
}
