package me.lqw.blog8.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义文件过滤
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class FilterFileVisitor extends SimpleFileVisitor<Path> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());


    List<FileInfo> fileInfos = new ArrayList<>();


    public FilterFileVisitor(List<FileInfo> fileInfos) {
        this.fileInfos = fileInfos;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

        logger.info("准备访问文件，准备访问的文件名是: [{}], 文件大小为: [{}]", dir.getFileName(), attrs.size());

        return super.preVisitDirectory(dir, attrs);
    }

    /**
     * 浏览文件
     * @param file file
     * @param attrs attrs
     * @return FileResult
     * @throws IOException IOException
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        logger.info("正在访问文件, 访问的文件名是: [{}], 文件大小为: [{}]", file.getFileName(), attrs.size());


        return super.visitFile(file,attrs);
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return super.visitFileFailed(file, exc);
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        logger.info("访问文件结束，准备访问下一个文件:[{}]", dir.getFileName());
        return super.postVisitDirectory(dir, exc);
    }
}
