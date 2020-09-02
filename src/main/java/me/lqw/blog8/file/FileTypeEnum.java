package me.lqw.blog8.file;


import me.lqw.blog8.util.StringUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 文件类型枚举
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public enum FileTypeEnum {

    /**
     * JPG
     */
    JPG("jpg"),

    /**
     * PNG
     */
    PNG("png"),

    /**
     * JPEG
     */
    JPEG("jpeg"),

    /**
     * MBP4
     */
    MBP4("mbp4"),


    /**
     * HTML
     */
    HTML("html"),

    /**
     * HTM
     */
    HTM("htm"),

    /**
     * XLS
     */
    XLS("xls"),

    /**
     * XLSX
     */
    XLSX("xlsx"),

    /**
     * MD
     */
    MD("md"),

    /**
     * DOC
     */
    DOC("doc"),

    /**
     * JSON
     */
    JSON("json"),

    /**
     * LOG
     */
    LOG("log"),

    /**
     * XML
     */
    XML("xml"),

    /**
     * PROPERTIES
     */
    PRO("properties"),

    /**
     * YML
     */
    YML("yml"),

    /**
     * PDF
     */
    PDF("pdf"),

    /**
     * PPT
     */
    PPT("pptx"),

    /**
     * TXT
     */
    TXT("txt"),

    /**
     * MP4
     */
    MP4("mp4"),
    ;

    String code;

    FileTypeEnum(String code) {
        this.code = code;
    }

    /**
     * 根据扩展名称获取文件类型
     *
     * @param ext ext
     * @return FileTypeEnum | null
     */
    public static FileTypeEnum getType(String ext) {

        if (StringUtil.isBlank(ext)) {
            return null;
        }
        FileTypeEnum[] values = FileTypeEnum.values();
        for (FileTypeEnum value : values) {
            if (value.getCode().equals(ext)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取所有的扩展名
     *
     * @return List
     */
    public static List<String> getTypes() {
        List<String> codes = new ArrayList<>();
        FileTypeEnum[] typeEnums = FileTypeEnum.values();
        for (FileTypeEnum fileTypeEnum : typeEnums) {
            codes.add(fileTypeEnum.getCode());
        }
        return codes;
    }

    /**
     * 文件后缀是否支持在线预览
     *
     * @param ext ext
     * @return false | true
     */
    public static boolean canOnlinePreview(String ext) {
        boolean flag = false;
        if (StringUtil.isBlank(ext)) {
            return false;
        }
        if (ext.equals(FileTypeEnum.TXT.getCode()) ||
                ext.equals(FileTypeEnum.PDF.getCode())) {
            flag = true;
        }

        return flag;
    }

    /**
     * 获取所有支持在线编辑的文件后缀
     *
     * @return List
     */
    private static List<String> canEditFiles() {
        return Arrays.asList(FileTypeEnum.TXT.getCode(),
                FileTypeEnum.HTM.getCode(), FileTypeEnum.HTML.getCode(),
                FileTypeEnum.MD.getCode(), FileTypeEnum.LOG.getCode(),
                FileTypeEnum.PRO.getCode(), FileTypeEnum.YML.getCode(),
                FileTypeEnum.JSON.getCode());

    }


    /**
     * 设置文件是否可被编辑
     * @param path path
     * @return true | false
     */
    public static boolean canEdit(Path path) {
        Optional<String> extOp = FileUtil.getExtension(path);
        return extOp.filter(s -> canEditFiles().contains(s)).isPresent();
    }

    /**
     * the file can edit
     *
     * @param name name
     * @return flag
     */
    public static boolean canEdit(String name) {
        boolean flag = false;
        if (StringUtil.isBlank(name)) {
            return false;
        }
        if (name.trim().length() == 0) {
            return false;
        }
        name = name.trim();
        Optional<String> extOp = FileUtil.getExtension(name);
        if (!extOp.isPresent()) {
            return false;
        }
        String ext = extOp.get();
        if (ext.equals(FileTypeEnum.TXT.getCode()) ||
                ext.equals(FileTypeEnum.HTM.getCode()) ||
                ext.equals(FileTypeEnum.HTML.getCode()) ||
                ext.equals(FileTypeEnum.MD.getCode()) ||
                ext.equals(FileTypeEnum.XLS.getCode()) ||
                ext.equals(FileTypeEnum.DOC.getCode()) ||
                ext.equals(FileTypeEnum.LOG.getCode()) ||
                ext.equals(FileTypeEnum.PRO.getCode()) ||
                ext.equals(FileTypeEnum.YML.getCode())) {
            flag = true;
        }
        return flag;
    }

    /**
     * Is it a file type allowed by the system
     *
     * @param name name
     * @return boolean
     */
    public static boolean checkSuffix(String name) {
        if (StringUtil.isBlank(name)) {
            return false;
        }
        if (name.trim().length() == 0) {
            return false;
        }
        name = name.trim();

        List<String> types = getTypes();
        return types.contains(name.substring(name.lastIndexOf(".") + 1));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
