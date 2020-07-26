package me.lqw.blog8.file;

import me.lqw.blog8.exception.LogicException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 文件服务接口处理类
 * @author liqiwen
 * @version 1.0
 */
@Conditional(FileCondition.class)
@Service
public class FileService implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final int MAX_NAME_LENGTH = 255; //附件名称最大长度
    private final int MAX_DEEPTH = 10;  //查询最大深度

    private final Path rootPath;

    private final FileProperties fileProperties;

    public FileService(FileProperties fileProperties) throws IOException {
        this.fileProperties = fileProperties;
        String uploadPath = fileProperties.getUploadPath();
        String fileRootPath = fileProperties.getFileRootPath();
        if (StringUtils.isEmpty(fileRootPath)) {
            throw new RuntimeException("文件系统已开启, 请提供在系统配置文件[application.yml]中一个上传文件的个路径, 参考key:[blog.file.file-root-path]");
        }

        this.rootPath = Paths.get(fileRootPath);
        if(!this.rootPath.toFile().exists()){
//            FileUtils.forceMkdir(this.rootPath);
            Files.createDirectories(rootPath);
            logger.info("上传文件夹创建成功");
        }
    }


    public FilePageResult selectPage(FileQueryParam queryParam) throws Exception {

        List<FileInfo> fileInfos = new ArrayList<>();

//        List<String> fileSuffixes = queryParam.getFileSuffixes();


        int visitDepth = queryParam.getContainChildDir() ? Integer.MAX_VALUE : 1;




//        Set<FileVisitOption> optionSet = new HashSet<>();
//        optionSet.add(FileVisitOption.FOLLOW_LINKS);
//        Path path = Files.walkFileTree(rootPath, optionSet, visitDepth, new SimpleFileVisitor<Path>() {
//
//            // dir pre
//            @Override
//            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//                boolean directory = dir.toFile().isDirectory();
//                System.out.println("文件夹：" + directory);
//
//
//                String s = dir.toRealPath(LinkOption.values()).toString();
//                System.out.println("文件夹路径：" + s);
//
//                return super.preVisitDirectory(dir, attrs);
//            }
//
//            // only file
//            @Override
//            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
//
//                FileInfo fileInfo = new FileInfo();
//
//                File file = path.toFile();
//                String fileName = file.getName();
//
//                if (!CollectionUtils.isEmpty(fileSuffixes)) {
//
//                    String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
//                    if (!fileSuffixes.contains(fileSuffix)) {
//                        return FileVisitResult.CONTINUE;
//                    }
//                }
//
//
//                if (!StringUtils.isEmpty(queryParam.getFileName())) {
//                    if (fileName.contains(queryParam.getFileName())) {
////                            fileInfo.setFileName(fileName.getFileName().toString());
//
//                        long lastModified = file.lastModified();
//                        fileInfo.setLastModified(lastModified);
//
//                        fileInfo.setFileName(fileName);
//                        fileInfo.setFileSuffix(fileName.substring(fileName.lastIndexOf(".") + 1));
//
//                        fileInfo.setDir(false);
//                        fileInfo.setSize(file.length());
//
//                        fileInfo.setCanEdit(FileTypeEnum.canEdit(fileName));
//
//                        fileInfos.add(fileInfo);
//                    }
//                } else {
//                    long lastModified = file.lastModified();
//                    fileInfo.setLastModified(lastModified);
//
//                    fileInfo.setFileName(fileName);
//                    fileInfo.setFileSuffix(fileName.substring(fileName.lastIndexOf(".") + 1));
//
//                    fileInfo.setDir(false);
//                    fileInfo.setSize(file.length());
//                    fileInfo.setCanEdit(FileTypeEnum.canEdit(fileName));
//                    fileInfos.add(fileInfo);
//
//                }
//                return FileVisitResult.CONTINUE;
//            }
//        });

//
//        Files.walkFileTree(rootPath, new FilterFileVisitor(fileInfos));
//
//        // sort by modified desc
//        fileInfos.sort((o1, o2) -> {
//            Long lastModified = o1.getLastModified();
//            Long lastModified1 = o2.getLastModified();
//            return lastModified1.compareTo(lastModified);
//        });
//
//        //dir sort head
//        fileInfos.sort((o1, o2) -> {
//            Boolean dir = o1.getDir();
//            Boolean dir2 = o2.getDir();
//            return dir2.compareTo(dir);
//        });

        return doQuery(rootPath, queryParam);
    }


    public FilePageResult doQuery(Path path, FileQueryParam queryParam) throws IOException {

        boolean needQuery = !CollectionUtils.isEmpty(queryParam.getExtensions()) || !StringUtils.isEmpty(queryParam.getFileName());


        FilePageResult filePageResult;

        Predicate<Path> predicate = p -> {
            if(!p.endsWith(path)){

                return true;
            }
            return false;
        };

        if(needQuery){
            predicate = predicate.and(p -> matchParam(queryParam, p.getFileName().toString()));
        }

//        if(queryParam.isHidden()){
//            predicate = predicate.and(p -> qu)
//        }

        //是否根据最近修改时间来排序
        if(!queryParam.isSortByLastModify()) {

            if(queryParam.isIgnorePaging()) {
                List<FileInfo> fileInfos = Files.walk(path, 1).filter(predicate)
                        .map(this::getFileInfo).collect(Collectors.toList());

                filePageResult =  new FilePageResult(queryParam, fileInfos.size(), fileInfos);

                return filePageResult;
            }

            List<FileInfo> fileInfos = Files.walk(path, 1).filter(predicate).skip(queryParam.getOffset())
                    .limit(queryParam.getPageSize()).map(this::getFileInfo).collect(Collectors.toList());

            int count = (int) Files.walk(path, 1).filter(predicate).count();

            filePageResult = new FilePageResult(queryParam, count, fileInfos);

            return filePageResult;

        }

        return new FilePageResult(queryParam, 0, Collections.emptyList());
    }


    public boolean matchParam(FileQueryParam queryParam, String queryFileName) {
        Optional<String> extOp = FileUtils.getExtension(queryFileName);
        if(extOp.isPresent() && !CollectionUtils.isEmpty(queryParam.getExtensions()) &&
                queryParam.getExtensions().stream().noneMatch(ex -> ex.equalsIgnoreCase(extOp.get()))){
            return false;
        }
        String fileName = queryParam.getFileName();
        return StringUtils.isEmpty(fileName) || queryFileName.contains(fileName);
    }


    public FileInfoDetail getFileInfoDetail(String path){

        System.out.println("根目录的地址：" + this.rootPath.toString());

//        Path filePath = this.rootPath.resolve(path);


        Path filePath = Paths.get(this.fileProperties.getFileRootPath(), path);



        logger.info("filePath:[{}]", filePath.toString());

        FileInfoDetail fid = new FileInfoDetail(getFileInfo(filePath));

        if(fid.getCanEdit() && Files.isReadable(filePath)){
            try {
                fid.setContent(String.join("", Files.readAllLines(filePath, Charset.defaultCharset())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fid;
    }


    public FileInfo getFileInfo(Path path) {

        FileInfo fileInfo = new FileInfo();
        FileUtils.getExtension(path).ifPresent(e -> fileInfo.setExt(FileTypeEnum.getType(e)));

        fileInfo.setFileName(path.getFileName().toString());

        fileInfo.setDirectory(Files.isDirectory(path));
        fileInfo.setCanEdit(FileTypeEnum.canEdit(path));

        if(Files.isDirectory(path)){

        }

//        return getFileInfoDetail(path);

//        BasicFileAttributes fileAttributes = Files.readAttributes(path, BasicFileAttributes.class);

        return fileInfo;
    }


//    private Map<String, Object> getFileProperties(Path path){
//        Map<String, Object> propMap = new LinkedHashMap<>();
//
//        if(Files.isDirectory(path)){
//
//        }
//
//        if(Files.isRegularFile(path)){
//
//
//        }
//    }
//
//
//    private FileStatistic getFileStatistic(Path path) throws LogicException {
//
//        if (!Files.exists(path)) {
//            return new FileStatistic(0, 0, 0, null);
//        }
//
//        Files.walk(path).parallel().forEach(p -> {
//            if(Files.isRegularFile(p)){
//
//            }
//        });
//
//    }


    private void writeFile(Path file, String content) throws LogicException {

        if(!FileTypeEnum.canEdit(file)){
            throw new LogicException("fileService.file.unable", "文件不能被编辑");
        }

        if(!Files.isWritable(file)){
            throw new LogicException("fileService.file.unwriteable", "文件不可被写");
        }

        try {
            Files.write(file, content.getBytes(Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new LogicException("fileService.file.writeFail", "文件写入失败");
        }

    }



    @Transactional(propagation = Propagation.REQUIRED)
    public List<FileInfo> saveUploadedFiles(List<MultipartFile> files) throws LogicException {


        List<FileInfo> fileInfos = new ArrayList<>();

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue;
            }

            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(fileProperties.getUploadPath() + file.getOriginalFilename());

                if(Files.exists(path)){
                    throw new LogicException("fileService.fileExists", "名称为[" + file.getOriginalFilename() +"]的文件已经存在");
                }

                //写文件
                Path filePath = Files.write(path, bytes);



                FileInfo fileInfo = new FileInfo();

                fileInfo.setSize(file.getSize());
                fileInfo.setFileName(file.getOriginalFilename());


                String ext = FileUtils.getExtension(file).orElseThrow(()
                        -> new LogicException("fileService.invalid.extension", "无效的文件扩展名"));
                fileInfo.setExt(FileTypeEnum.getType(ext));

                fileInfo.setCanEdit(FileTypeEnum.canEdit(file.getOriginalFilename()));



                BasicFileAttributes fileAttributes = Files.readAttributes(path, BasicFileAttributes.class);

                //文件创建时间
                Instant instant = fileAttributes.creationTime().toInstant();

                //文件更新时间
//                Instant lastModifiedInstant = fileAttributes.lastModifiedTime().toInstant();

                //文件上次访问时间
//                Instant lastAccessInstant = fileAttributes.lastAccessTime().toInstant();

                String format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault()).format(instant);


                fileInfos.add(fileInfo);
            } catch (IOException ex){
                logger.error("上传文件失败:[{}]", ex.getMessage(), ex);
                throw new LogicException("fileService.upload.fail", "文件上传失败");
            }
        }

        if(fileInfos.isEmpty()){
            return Collections.emptyList();
        }

        return fileInfos;

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


    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("文件系统服务处理类已初始化");
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


    public static void main(String[] args){

        Path path1 = Paths.get("folder1", "sub1");
        Path path2 = Paths.get("folder2", "sub2");

        String s = path1.relativize(path2).toString();
        System.out.println(s);

        Path resolve = path1.resolve(path2);
        System.out.println(resolve.toString());
//        path1.resolve(path2); //folder1\sub1\folder2\sub2
//        path1.resolveSibling(path2); //folder1\folder2\sub2
//        path1.relativize(path2); //..\..\folder2\sub2
//        path1.subpath(0, 1); //folder1
//        path1.startsWith(path2); //false
//        path1.endsWith(path2); //false
//        Paths.get("folder1/./../folder2/my.text").normalize(); //folder2\my.te

    }
}
