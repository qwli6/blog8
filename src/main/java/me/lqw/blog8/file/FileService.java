package me.lqw.blog8.file;

import me.lqw.blog8.exception.AbstractBlogException;
import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.exception.ResourceNotFoundException;
import me.lqw.blog8.file.exception.FileAlreadyExistsException;
import me.lqw.blog8.file.exception.FileException;
import me.lqw.blog8.file.exception.FileNotExistsException;
import me.lqw.blog8.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 文件服务接口处理类
 * 1. 图片|视频文件的处理以及缩放，并且获取文件信息
 * 2. 文件|文件夹的重命名
 * 3. 文件的拷贝
 * 4. 文件|文件夹的移动
 * 5. 文件|文件夹的删除
 * 6. 部分文件可在线编辑
 * 7. 文件保护（私有|密码）
 * 8. 文件检索
 * 9. 文件上传
 *
 * @author liqiwen
 * @version 1.0
 */
@Conditional(FileCondition.class)
@Service
public class FileService {

    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    /**
     * 读写锁
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 附件名称最大长度
     */
    private final int MAX_NAME_LENGTH = 255; //附件名称最大长度

    /**
     * 查询最大深度
     */
    private final int MAX_DEEPTH = 10;  //查询最大深度

    /**
     * 根路径
     */
    private final Path rootPath;

    /**
     * 文件属性配置
     */
    private final FileProperties fileProperties;

    /**
     * 构造方法注入
     * @param fileProperties fileProperties
     * @throws IOException IOException
     */
    public FileService(FileProperties fileProperties) throws IOException {
        this.fileProperties = fileProperties;
        String uploadPath = fileProperties.getUploadPath();
        String fileRootPath = fileProperties.getFileRootPath();
        if (StringUtil.isBlank(fileRootPath)) {
            throw new RuntimeException("文件系统已开启, 请提供在系统配置文件[application.yml]中一个上传文件的根路径, " +
                    "参考key: [blog.file.file-root-path]");
        }

        this.rootPath = Paths.get(fileRootPath);
        if (!this.rootPath.toFile().exists()) {
            Files.createDirectories(rootPath);
            logger.info("上传文件夹创建成功");
        }
    }


    /**
     * 保存文件到本地
     * @param fileUpload uploadModel
     * @return FileInfo
     * @throws AbstractBlogException 系统异常
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public List<FileInfo> uploadedFiles(FileUpload fileUpload) throws FileException {

        MultipartFile[] files = fileUpload.getFiles();

        if(files == null || files.length == 0){
            throw new FileException("files.notExists", "请选择上传文件");
        }

        List<FileInfo> fileInfos = new ArrayList<>(files.length);

        try {
            for (MultipartFile multipartFile : files) {
                Path file = Paths.get(getFullPath(fileUpload.getTargetPath()).toString(), multipartFile.getOriginalFilename());

                if(Files.exists(file)){
                    throw new FileAlreadyExistsException("file.already.exists", "文件已经存在");
                }
                fileInfos.add(getFileInfo(file));
                Files.copy(multipartFile.getInputStream(), file);
            }
        } catch (IOException ex){
            ex.printStackTrace();
            logger.error("上传文件异常,[{}]", ex.getMessage(), ex);
            throw new FileException("file.uploaded.failed", "文件上传失败");
        }
        return fileInfos;
    }


    /**
     * 分页查询文件
     * @param queryParam queryParam
     * @return FilePageResult
     */
    public FilePageResult selectPage(FilePageQueryParam queryParam) throws AbstractBlogException {

        return doSearchWithParams2(Paths.get(fileProperties.getUploadPath()), queryParam);
    }

    /**
     * 查询目录下的文件
     * @param path path
     * @param queryParam queryParam
     * @return FilePageResult
     * @throws LogicException 逻辑异常
     */
    private FilePageResult doSearchWithParams2(Path path, FilePageQueryParam queryParam) throws AbstractBlogException {
        FilePageResult filePageResult;

        if(path == null || !path.toFile().isDirectory()){
            throw new FileException("query.filePath.illegal", "查询文件路径非法");
        }


        boolean needQuery = !CollectionUtils.isEmpty(queryParam.getExtensions()) || StringUtil.isNotBlank(queryParam.getFileName());


        //递归查询深度
        int maxDepth = queryParam.getContainChildDir() != null && queryParam.getContainChildDir() ? Integer.MAX_VALUE : 1;

        //要执行分页
        //计算要跳过的页码
        int skip = queryParam.getOffset();
        //需要查询的数量
        int limit = queryParam.getPageSize();

        String fileName = queryParam.getFileName();

        Predicate<Path> predicate = p -> {
            if(!p.equals(rootPath)){

                return true;

            }

            return false;
        };

        if(needQuery){
            predicate = predicate.and(p -> matchParam(queryParam, p.getFileName().toString()));
        }


        try {
            List<FileInfo> fileInfos = Files.walk(path, maxDepth).skip(skip)
                    .limit(limit).map(this::getFileInfo).collect(Collectors.toList());
            Boolean sortBySize = queryParam.isSortBySize();
            if(sortBySize != null && sortBySize){
                //默认是从小到大, 改变一下，从大到小
                fileInfos = fileInfos.stream().sorted(Comparator.comparingLong(FileInfo::getSize).reversed()).collect(Collectors.toList());
            }

            int count = (int) Files.walk(path, maxDepth).count();
            filePageResult = new FilePageResult(queryParam, count, fileInfos);


            List<String> paths = getPathsBetweenPath(rootPath, getFullPath(queryParam.getTargetPath()));

            filePageResult.setPath(paths);

            return filePageResult;
        } catch (IOException ex){
            logger.error("查询文件列表异常: [{}]", ex.getMessage(), ex);
            throw new FileException("query.files.failed", "查询文件列表异常");
        }
    }


    /**
     * 查询目录下的文件
     * @param path path
     * @param queryParam queryParam
     * @return FilePageResult
     * @throws LogicException 逻辑异常
     */
    private FilePageResult doSearchWithParams(Path path, FilePageQueryParam queryParam) throws AbstractBlogException {

        boolean needQuery = !CollectionUtils.isEmpty(queryParam.getExtensions()) || !StringUtil.isBlank(queryParam.getFileName());

        FilePageResult filePageResult;

        Predicate<Path> predicate = p -> !p.endsWith(path);

        if (needQuery) {
            predicate = predicate.and(p -> matchParam(queryParam, p.getFileName().toString()));
        }

        //是否根据最近修改时间来排序
        if (!queryParam.isSortByLastModify()) {

            if (queryParam.isIgnorePaging()) {
                try {
                    List<FileInfo> fileInfos = Files.walk(path, 1).filter(predicate)
                            .map(this::getFileInfo).collect(Collectors.toList());
                    filePageResult = new FilePageResult(queryParam, fileInfos.size(), fileInfos);
                    return filePageResult;
                } catch (IOException ex){
                    throw new LogicException("", "");
                }
            }
            try {
                List<FileInfo> fileInfos = Files.walk(path, 1).filter(predicate).skip(queryParam.getOffset())
                        .limit(queryParam.getPageSize()).map(this::getFileInfo).collect(Collectors.toList());

                int count = (int) Files.walk(path, 1).filter(predicate).count();
                filePageResult = new FilePageResult(queryParam, count, fileInfos);

                return filePageResult;
            } catch (IOException ex){
                throw new LogicException("", "");
            }
        }

        return new FilePageResult(queryParam, 0, Collections.emptyList());
    }


    /**
     * 是否匹配参数
     * @param queryParam queryParam 查询参数
     * @param queryFileName queryFileName 传入的文件名称
     * @return true | false
     */
    private boolean matchParam(FilePageQueryParam queryParam, String queryFileName) {

        //获取传入名称的扩展名
        Optional<String> extOp = FileUtil.getExtension(queryFileName);

        //扩展名存在
        if (extOp.isPresent() && !CollectionUtils.isEmpty(queryParam.getExtensions()) &&
                queryParam.getExtensions().stream().noneMatch(ex -> ex.equalsIgnoreCase(extOp.get()))) {
            return false;
        }
        String fileName = queryParam.getFileName();
        return StringUtil.isBlank(fileName) || queryFileName.contains(fileName);
    }


    /**
     * 获取文件详细信息
     * @param path path
     * @return FileInfoDetail
     */
    public FileInfoDetail getFileInfoDetail(String path) {
        lock.readLock().lock();
        try {
            Path filePath = Paths.get(this.fileProperties.getFileRootPath(), path);
            return getFileInfoDetail(filePath);
        } finally {
            lock.readLock().unlock();
        }

    }

    /**
     * 获取文件详细信息
     * @param path path
     * @return FileInfoDetail
     */
    public FileInfoDetail getFileInfoDetail(Path path) throws FileException {

        FileInfoDetail fid = new FileInfoDetail(getFileInfo(path));

        fid.setFileAttributes(getFileAttributes(path));

        if (fid.getCanEdit() && Files.isReadable(path)) {
            try {
                fid.setContent(String.join("", Files.readAllLines(path, Charset.defaultCharset())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fid;

    }

    /**
     * 获取文件信息
     * @param path path
     * @return FileInfo
     */
    public FileInfo getFileInfo(Path path) throws FileException {

        if(path == null){
            throw new FileNotExistsException("file.notExists", "文件不存在");
        }

        FileInfo fileInfo = new FileInfo();
        FileUtil.getExtension(path).ifPresent(e -> fileInfo.setExt(FileTypeEnum.getType(e)));

        fileInfo.setFileName(path.getFileName().toString());

        fileInfo.setDirectory(Files.isDirectory(path));
        fileInfo.setCanEdit(Files.isDirectory(path) || FileTypeEnum.canEdit(path));
//        fileInfo.setFilePath(Files.isRegularFile(path) ? "/"+ path);

        if (Files.isRegularFile(path)) {
            fileInfo.setSize(path.toFile().length());
        }
        return fileInfo;
    }


    /**
     * 获取文件属性
     * @param path path
     * @return FileAttributes
     */
    private FileAttributes getFileAttributes(Path path) {

        Assert.notNull(path, "path not be null");

        FileAttributes fileAttributes = new FileAttributes();

        fileAttributes.setOsReadPermission(path.toFile().canRead());
        fileAttributes.setOsExecutePermission(path.toFile().canExecute());
        fileAttributes.setOsWritePermission(path.toFile().canWrite());

        //文件
        if(path.toFile().isDirectory()){
            BasicFileAttributeView fileAttributeView = Files.getFileAttributeView(path, BasicFileAttributeView.class);
            try {
                BasicFileAttributes basicFileAttributes = fileAttributeView.readAttributes();

                fileAttributes.setHumanCanReadSize(String.valueOf(basicFileAttributes.size()));

                fileAttributes.setLastAccess(FileUtil.transferFileTime(basicFileAttributes.lastAccessTime()));
                fileAttributes.setLastModified(FileUtil.transferFileTime(basicFileAttributes.lastModifiedTime()));
                return fileAttributes;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { //目录

        }


        return fileAttributes;
    }



    /**
     * 写文件
     * @param file file
     * @param content content
     * @throws AbstractBlogException 逻辑异常
     */
    private void writeFile(Path file, String content) throws FileException {

        if (!FileTypeEnum.canEdit(file)) {
            throw new FileException("file.canNot.editing", "文件不能被编辑");
        }

        if(!Files.exists(file)){
            try {
                Files.createFile(file);
            } catch (IOException e) {
                e.printStackTrace();
                throw new FileException("file.write.failed", "无法创建文件写入");
            }
        }

        if (!Files.isWritable(file)) {
            throw new FileException("file.not.writeable", "文件无写权限");
        }

        try {
            Files.write(file, content.getBytes(Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileException("file.write.failed", "文件写入失败");
        }
    }

    /**
     * 写文件
     * @param fileUpdated 待更新的文件
     * @throws FileException 文件异常
     */
    public void writeFile(FileUpdated fileUpdated) throws FileException {

        String filePath = fileUpdated.getPath();

        if(filePath.contains("/")){
            throw new LogicException("file.format.error", "文件路径错误");
        }

        writeFile(getFilePath(filePath), fileUpdated.getContent());

    }

    /**
     * 保存上传文件
     * @param files files
     * @return List<FileInfo>
     * @throws AbstractBlogException 逻辑异常
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<FileInfo> saveUploadedFiles(List<MultipartFile> files) throws FileException {
        List<FileInfo> fileInfos = new ArrayList<>();

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue;
            }

            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(fileProperties.getUploadPath() + file.getOriginalFilename());

                if (Files.exists(path)) {
                    throw new LogicException("fileService.fileExists", "名称为[" + file.getOriginalFilename() + "]的文件已经存在");
                }

                //写文件
                Path filePath = Files.write(path, bytes);


                FileInfo fileInfo = new FileInfo();

                fileInfo.setSize(file.getSize());
                fileInfo.setFileName(file.getOriginalFilename());


                String ext = FileUtil.getExtension(file).orElseThrow(()
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
            } catch (IOException ex) {
                logger.error("上传文件失败:[{}]", ex.getMessage(), ex);
                throw new LogicException("fileService.upload.fail", "文件上传失败");
            }
        }

        if (fileInfos.isEmpty()) {
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


    private Path resolve(Path root, String path) {
        Path resolve;
        if (StringUtil.isBlank(path)) {
            resolve = root;
        } else {
//            String cleanPath = StringUtils.cleanPath(path);
            String cleanPath = path;
            resolve = StringUtil.isBlank(cleanPath) ? root : root.resolve(path);
        }
        return resolve;
    }


    /**
     * 找出两个 Path 之间的路径
     *
     * @param root root 根目录
     * @param dir  dir 磁盘目录
     * @return List<Path>
     */
    private List<Path> betweenPaths(Path root, Path dir) {
        if (root.equals(dir)) {
            return Collections.emptyList();
        }
        Path parent = dir;
        List<Path> paths = new ArrayList<>();
        while ((parent = parent.getParent()) != null) {
            if (parent.getParent().equals(root)) {
                if (!paths.isEmpty()) {
                    Collections.reverse(paths);
                }
                return paths;
            }
            paths.add(parent);
        }
        throw new RuntimeException("无法找出两个 Path 之间的路径");
    }


    private Optional<Path> lookup(Lookup lookup) {
        Path p;
        try {
            p = resolve(this.rootPath, lookup.path);
        } catch (InvalidPathException e) {
            return Optional.empty();
        }
//        if (!StringUtils.isSub(p, this.rootPath)) {
//            return Optional.empty();
//        }
        if (lookup.mustExists && !Files.exists(p)) {
            return Optional.empty();
        }
        if (lookup.ignoreRoot && p == this.rootPath) {
            return Optional.empty();
        }
        if (lookup.mustRegularFile && !Files.isRegularFile(p)) {
            return Optional.empty();
        }
        if (lookup.mustDir && !Files.isDirectory(p)) {
            return Optional.empty();
        }
        return Optional.of(p);
    }

    /**
     * 文件编辑
     * @param fileName fileName
     * @return FileInfoDetail
     * @throws AbstractBlogException
     * <ul>
     *     <li>文件未找到</li>
     *     <li>文件无读权限</li>
     *     <li>文件不可编辑</li>
     * </ul>
     */
    public FileInfoDetail fileEdit(String fileName) throws AbstractBlogException {
        Path filePath = getFilePath(fileName);

        if(!filePath.toFile().exists()){
            throw new ResourceNotFoundException("file.notFound", "未找到名称为" + fileName + "的文件");
        }

        if(filePath.toFile().isDirectory()){
            throw new LogicException("directory.cannot.edit", "文件夹不可被编辑");
        }

        //操作系统的读权限
        //文件必须要可读可写, 只要有一个条件不满足，就不能编辑文件
        if(!(filePath.toFile().canRead() && filePath.toFile().canWrite())){
            throw new LogicException("file.not.read", "文件无权限");
        }

        if(!filePath.toFile().isFile()){

            throw new LogicException("regular.file", "不规则文件");

        }
        FileInfoDetail fid = getFileInfoDetail(filePath);

        if(!fid.getCanEdit()){
            throw new LogicException("file.cannot.edit", "文件不可被编辑(用户设置)");
        }

        return fid;
    }

    /**
     * 创建文件
     * @param fileCreated fileCreated
     * @return FileInfoDetail
     * @throws FileException FileException
     */
    public FileInfoDetail fileCreate(FileCreated fileCreated) throws FileException {

        //创建文件还是文件夹
        boolean directory = fileCreated.getFileType() == null || fileCreated.getFileType() == 0;

        String fileName = fileCreated.getFileName();
        if(!directory){
            //文件, 检查后缀是否支持
            FileUtil.getExtension(fileName).orElseThrow(()
                    -> new FileException("file.extension.illegal", "请输入合法的文件后缀名称"));

        }



        Path fullPath = getFullPath(fileCreated.getTargetPath());

        Integer fileType = fileCreated.getFileType();

        Path filePath = getFilePath(fileName);

        if(filePath.toFile().exists()){
            throw new FileAlreadyExistsException("file.alreadyExists", "目录|文件已经存在");
        }
        if(fileType == null || fileType == 0){
            try {
                Files.createDirectories(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new FileException("directory.create.failed", "目录创建失败");
            }
        } else {

            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new FileException("file.create.failed", "文件创建失败");
            }
        }

        return getFileInfoDetail(filePath);

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


    /**
     * 获取两个 Path 之间的路径，target 必须要是 root 子目录
     * 如果不是子目录，则无法获取两个路径之间的距离
     *
     * root   /usr/local/tomcat
     * target /usr/local/tomcat/pos-web.lp.com/logs
     *
     * 将返回 [logs, pos-web.lp.com]
     *
     * @param root root
     * @param target target
     * @return List
     */
    public static List<String> getPathsBetweenPath(Path root, Path target) {
        if(root.equals(target) || target.getNameCount() < root.getNameCount()){
            return new ArrayList<>();
        }

        LinkedList<String> paths = new LinkedList<>();
        while (!target.getParent().equals(root)){
            paths.addFirst(target.getFileName().toString());
            target = target.getParent();
        }
        return paths;
    }


    private Path getFilePath(String path){
        return Paths.get(fileProperties.getUploadPath(), path);
    }


    /**
     * 获取完整的路径，转换成 path
     * root: /usr/local
     * path: upload
     *
     * fullPath: /usr/local/upload
     *
     * root: /usr/local/
     * path: /upload
     *
     * fullPath: /usr/local/upload
     *
     * @param path path
     * @return Path
     */
    private Path getFullPath(String path) {
        if(StringUtil.isBlank(path)){
            return rootPath;
        }

        if(path.startsWith("/")){
            path = path.substring(1);
        }
        String uploadPath = fileProperties.getUploadPath();

        if(!uploadPath.endsWith("/")){
            uploadPath = uploadPath + "/";
        }
        return Paths.get(uploadPath, path);
    }


    public static void main(String[] args){
        Path root = Paths.get("/1/2");
        Path target = Paths.get("/1/");

        List<String> pathsBetweenPath = getPathsBetweenPath(root, target);
        System.out.println(pathsBetweenPath);

    }
}
