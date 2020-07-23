package me.lqw.blog8.file;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum FileTypeEnum {
    JPG("jpg"),
    PNG("png"),
    JPEG("jpeg"),
    MBP4("mbp4"),
    MP4("mp4"),

    HTML("html"),
    HTM("htm"),
    XLS("xls"),
    MD("md"),
    DOC("doc"),
    LOG("log"),
    XML("xml"),
    PRO("properties"),
    YML("yml"),
    PDF("pdf"),
    PPT("pptx"),
    TXT("txt");

    String code;

    FileTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public static List<String> getTypes(){
        List<String> codes = new ArrayList<>();
        FileTypeEnum[] typeEnums = FileTypeEnum.values();
        for(FileTypeEnum fileTypeEnum : typeEnums){
            codes.add(fileTypeEnum.getCode());
        }
        return codes;
    }


    /**
     * the file can online preview
     * @param ext ext
     * @return flag
     */
    public static boolean canOnlinePreview(String ext){
        boolean flag = false;
        if(StringUtils.isEmpty(ext)){
            return false;
        }
        if(ext.equals(FileTypeEnum.TXT.getCode()) ||
                ext.equals(FileTypeEnum.PDF.getCode())){
            flag = true;
        }

        return flag;
    }

    /**
     * the file can edit
     * @param name name
     * @return flag
     */
    public static boolean canEdit(String name){
        boolean flag = false;
        if(StringUtils.isEmpty(name)){
            return false;
        }
        if(name.trim().length() == 0){
            return false;
        }
        name = name.trim();
        String ext = name.substring(name.lastIndexOf(".")+1);
        if(ext.equals(FileTypeEnum.TXT.getCode()) ||
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
     * @param name name
     * @return boolean
     */
    public static boolean checkSuffix(String name){
        if(StringUtils.isEmpty(name)){
            return false;
        }
        if(name.trim().length() == 0){
            return false;
        }
        name = name.trim();

        List<String> types = getTypes();
        return types.contains(name.substring(name.lastIndexOf(".")+1));
    }
}
