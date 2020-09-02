package me.lqw.blog8.file;

import me.lqw.blog8.util.StringUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 文件工具类
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class FileUtil {


    /**
     * 可编辑的文件类型集合
     */
    private static final List<FileTypeEnum> CAN_EDIT = Arrays.asList(
            FileTypeEnum.TXT,
            FileTypeEnum.HTM, FileTypeEnum.HTML,
            FileTypeEnum.MD, FileTypeEnum.LOG,
            FileTypeEnum.PRO, FileTypeEnum.YML,
            FileTypeEnum.JSON);


    /**
     * 构造方法
     */
    private FileUtil() {
        super();
    }


    /**
     * 文件是否能在线编辑
     *
     * @param extension 扩展名
     * @return false | true
     */
    public static boolean canEdit(String extension) {

        if (StringUtil.isBlank(extension)) {
            return false;
        }

        FileTypeEnum fileType = FileTypeEnum.getType(extension);
        return CAN_EDIT.contains(fileType);
    }


    /**
     * 判断文件是否可被编辑
     *
     * @param path path
     * @return false | true
     */
    public static boolean canEdit(Path path) {
        if (path == null) {
            return false;
        }
        String fileName = path.getFileName().toString();
        if (StringUtil.isBlank(fileName)) {
            return false;
        }
        Optional<String> extensionOp = getExtension(fileName);
        return extensionOp.filter(FileUtil::canEdit).isPresent();
    }


    /**
     * 获取文件扩展名
     *
     * @param fileName fileName
     * @return Optional<String>
     */
    public static Optional<String> getExtension(String fileName) {

        if (StringUtil.isBlank(fileName)) {
            return Optional.empty();
        }

        if (fileName.contains(".")) {

            return Optional.of(fileName.substring(fileName.lastIndexOf(".") + 1));
        }

        return Optional.empty();
    }

    /**
     * 获取文件扩展名
     *
     * @param path path
     * @return Optional<String>
     */
    public static Optional<String> getExtension(Path path) {
        if (path == null) {
            return Optional.empty();
        }
        if (path.toFile().isDirectory()) {
            return Optional.empty();
        }
        return getExtension(path.getFileName().toString());
    }

    /**
     * 获取文件扩展名
     *
     * @param file file
     * @return Optional<String>
     */
    public static Optional<String> getExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (StringUtil.isBlank(originalFilename)) {
            return Optional.empty();
        }
        return getExtension(originalFilename);
    }

    /**
     * 获取文件的扩展名
     *
     * @param file file
     * @return Optional<String>
     */
    public static Optional<String> getExtension(File file) {
        return getExtension(file.toPath());
    }


    /**
     * 读取文件内容
     *
     * @param path path
     * @return String
     */
    public static Optional<String> readFile(Path path) {
        try {
            if (Files.isReadable(path)) {
                return Optional.of(Files.readAllLines(path, Charset.defaultCharset())
                        .parallelStream().collect(Collectors.joining()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * 格式化文件时间
     *
     * @param fileTime fileTime
     * @return string
     */
    public static String formatDateTime(FileTime fileTime) {
        LocalDateTime localDateTime = fileTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    public static LocalDateTime transferFileTime(FileTime fileTime){
        if(fileTime == null){
            return LocalDateTime.now();
        }
        return fileTime.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
