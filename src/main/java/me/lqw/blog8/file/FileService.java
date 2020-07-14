package me.lqw.blog8.file;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.omg.SendingContext.RunTime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

@Conditional(FileCondition.class)
@Service
public class FileService implements InitializingBean {

    private final Path root;

    private final FileProperties fileProperties;



    public FileService(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
        String uploadPath = fileProperties.getUploadPath();
        if(StringUtils.isEmpty(uploadPath)){
            throw new RuntimeException("Please provide [blog.file.upload-path] in application.yml");
        }
        this.root = Paths.get(uploadPath);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public List<FileInfo> queryFiles(FileQueryParam queryParam) throws Exception {

        List<FileInfo> fileInfos = new ArrayList<>();

        List<String> fileSuffixes = queryParam.getFileSuffixes();



        int visitDepth = queryParam.getContainChildDir() ? Integer.MAX_VALUE : 1;




        Set<FileVisitOption> optionSet = new HashSet<>();
        optionSet.add(FileVisitOption.FOLLOW_LINKS);
        Path path = Files.walkFileTree(root, optionSet, visitDepth, new SimpleFileVisitor<Path>() {

            // dir pre
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                boolean directory = dir.toFile().isDirectory();
                System.out.println("文件夹：" + directory);


                String s = dir.toRealPath(LinkOption.values()).toString();
                System.out.println("文件夹路径：" + s);

                return super.preVisitDirectory(dir, attrs);
            }

            // only file
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {

                FileInfo fileInfo = new FileInfo();

                File file = path.toFile();
                String fileName = file.getName();

                if (!CollectionUtils.isEmpty(fileSuffixes)) {

                    String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (!fileSuffixes.contains(fileSuffix)) {
                        return FileVisitResult.CONTINUE;
                    }
                }


                if (!StringUtils.isEmpty(queryParam.getFileName())) {
                    if (fileName.contains(queryParam.getFileName())) {
//                            fileInfo.setFileName(fileName.getFileName().toString());

                        long lastModified = file.lastModified();
                        fileInfo.setLastModified(lastModified);

                        fileInfo.setFileName(fileName);
                        fileInfo.setFileSuffix(fileName.substring(fileName.lastIndexOf(".") + 1));

                        fileInfo.setDir(false);
                        fileInfo.setSize(file.length());

                        fileInfo.setCanEdit(FileTypeEnum.canEdit(fileName));

                        fileInfos.add(fileInfo);
                    }
                } else {
                    long lastModified = file.lastModified();
                    fileInfo.setLastModified(lastModified);

                    fileInfo.setFileName(fileName);
                    fileInfo.setFileSuffix(fileName.substring(fileName.lastIndexOf(".") + 1));

                    fileInfo.setDir(false);
                    fileInfo.setSize(file.length());
                    fileInfo.setCanEdit(FileTypeEnum.canEdit(fileName));
                    fileInfos.add(fileInfo);

                }
                return FileVisitResult.CONTINUE;
            }
        });

        // sort by modified desc
        fileInfos.sort((o1, o2) -> {
            Long lastModified = o1.getLastModified();
            Long lastModified1 = o2.getLastModified();
            return lastModified1.compareTo(lastModified);
        });

        //dir sort head
        fileInfos.sort((o1, o2) -> {
            Boolean dir = o1.getDir();
            Boolean dir2 = o2.getDir();
            return dir2.compareTo(dir);
        });

        return fileInfos;
    }

    //save file
    public void saveUploadedFiles(List<MultipartFile> files) throws IOException {

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; //next pls
            }


            byte[] bytes = file.getBytes();
            Path path = Paths.get(fileProperties.getUploadPath() + file.getOriginalFilename());
            Path filePath = Files.write(path, bytes);

        }

    }

    public Optional<ReadablePath> getProcessedFile(String path) {
        System.out.println("访问图片地址为：" + path);
//        this.sm.check(FileUtils.cleanPath(path));
//        Optional<Path> op = lookupFile(Lookup.newLookup(path).setMustRegularFile(true));
//        if (op.isEmpty())
//            return Optional.empty();
//        Path file = op.get();
//        if (thumb == null)
//            return Optional.of(new _ReadablePath(file));
//        String ext = FileUtils.getFileExtension(file);
//        if (mediaTool.canHandle(ext) && MediaTool.isProcessableVideo(ext)) {
//            // compress video
//            Path compressed = thumb.resolve(root.relativize(file)).resolve(file.getFileName().toString() + ".mp4");
//            if (Files.exists(compressed))
//                return Optional.of(new _ReadablePath(compressed));
//            boolean isHandled = handleFile(compressed, true, new Runnable() {
//
//                @Override
//                public void run() {
//                    try {
//                        mediaTool.toMP4(fileProperties.getVideoSize(), file, compressed);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e.getMessage(), e);
//                    }
//                }
//            });
//
//            return isHandled ? Optional.of(new _ReadablePath(compressed)) : Optional.empty();
//        }
        return Optional.of(new _ReadablePath(Paths.get("/Users/liqiwen/Downloads/banner.png")));
    }


    private static class _ReadablePath implements ReadablePath {
        private final Path path;

        public _ReadablePath(Path path) {
            super();
            this.path = path;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return Files.newInputStream(path);
        }

        @Override
        public String fileName() {
            return path.getFileName().toString();
        }

        @Override
        public String getExtension() {
            return "png";
        }

        @Override
        public long size() {
//            return FileUtils.size(path);
            return 44;
        }

        @Override
        public long lastModified() {
//            return FileUtils.lastModified(path);
            return 0;
        }
    }
    public Optional<Resource> getFileInfo(String path) {
        File file = new File("/Users/liqiwen/Downloads/banner.png");

//        FileInputStream fileInputStream = new FileInputStream(file);

//        fileInputStream.

        return null;
    }
}
