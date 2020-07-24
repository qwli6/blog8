package me.lqw.blog8.file;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 文件工具类
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class FileUtils extends org.apache.tomcat.util.http.fileupload.FileUtils {


    private FileUtils() {
        super();
    }


    /**
     * 获取文件扩展名
     * @param fileName fileName
     * @return Optional<String>
     */
    public static Optional<String> getExtension(String fileName) {

        if(StringUtils.isEmpty(fileName)){
            return Optional.empty();
        }

        if(fileName.contains(".")){

            return Optional.of(fileName.substring(fileName.lastIndexOf(".")+1));
        }

        return Optional.empty();
    }

    /**
     * 获取文件扩展名
     * @param path path
     * @return Optional<String>
     */
    public static Optional<String> getExtension(Path path) {
        if(path == null){
            return Optional.empty();
        }
        if(path.toFile().isDirectory()){
            return Optional.empty();
        }
        Path fileName = path.getFileName();
        if(fileName.toString().contains(".")){
            return Optional.of(fileName.toString().substring(fileName.toString().lastIndexOf(".")+1));
        }
        return Optional.empty();
    }

    /**
     * 获取文件扩展名
     * @param file file
     * @return Optional<String>
     */
    public static Optional<String> getExtension(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        if(StringUtils.isEmpty(originalFilename)){
            return Optional.empty();
        }
        if(originalFilename.contains(".")){
            return Optional.of(originalFilename.substring(originalFilename.lastIndexOf(".")+1));
        }
        return Optional.empty();
    }

    /**
     * 获取文件的扩展名
     * @param file file
     * @return Optional<String>
     */
    public static Optional<String> getExtension(File file){
        return getExtension(file.toPath());
    }


    /**
     * 读取文件内容
     * @param path path
     * @return String
     * @throws IOException IOException
     */
    public static Optional<String> readFile(Path path) throws IOException {
        if(Files.isReadable(path)){
            return Optional.of(Files.readAllLines(path, Charset.defaultCharset())
                    .parallelStream().collect(Collectors.joining()));
        }
        return Optional.empty();
    }
}
