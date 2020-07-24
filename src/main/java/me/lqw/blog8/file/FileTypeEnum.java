package me.lqw.blog8.file;

import org.springframework.util.StringUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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
    JSON("json"),
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


    public static FileTypeEnum getType(String ext){

        if(StringUtils.isEmpty(ext)){
            return null;
        }
        FileTypeEnum[] values = FileTypeEnum.values();
        for(FileTypeEnum value : values){
            if(value.getCode().equals(ext)){
                return value;
            }
        }
        return null;
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

    private static List<String> canEditFiles(){

        return Arrays.asList(FileTypeEnum.TXT.getCode(),
                FileTypeEnum.HTM.getCode(), FileTypeEnum.HTML.getCode(),
                FileTypeEnum.MD.getCode(),FileTypeEnum.LOG.getCode(),
                FileTypeEnum.PRO.getCode(),FileTypeEnum.YML.getCode(),
                FileTypeEnum.JSON.getCode());

    }


    public static boolean canEdit(Path path){
        Optional<String> extOp = FileUtils.getExtension(path);
        return extOp.filter(s -> canEditFiles().contains(s)).isPresent();
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
        Optional<String> extOp = FileUtils.getExtension(name);
        if(!extOp.isPresent()){
            return false;
        }
        String ext = extOp.get();
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
