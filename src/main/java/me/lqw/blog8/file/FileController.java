package me.lqw.blog8.file;

import me.lqw.blog8.model.dto.CR;
import me.lqw.blog8.model.dto.ResultDTO;
import me.lqw.blog8.web.controller.console.BaseController;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("console")
@Conditional({FileCondition.class})
public class FileController extends BaseController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("files")
    public String files(){
        return "console/file/index";
    }


    @RequestMapping("file1/{name}")
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) throws Exception {
        byte[] imageContent ;
        String path = "your image path with image_name";
        imageContent = fileToByte(new File("/Users/liqiwen/Downloads/upload/" + name));

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    public static byte[] fileToByte(File img) throws Exception {
        byte[] bytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            BufferedImage bi;
            bi = ImageIO.read(img);
            ImageIO.write(bi, "png", baos);
            bytes = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            baos.close();
        }
        return bytes;
    }


    @GetMapping("file/getQueryCriteriaNew")
    @ResponseBody
    public Map<String, Object> getQueryCriteriaNew(){
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("fileSuffixes", FileTypeEnum.getTypes());
        dataMap.put("paths", new ArrayList<>());

        return dataMap;
    }


    @GetMapping("files/query")
    @ResponseBody
    public FilePageResult selectPage(FileQueryParam queryParam) throws Exception {
        return fileService.selectPage(queryParam);
    }


    @PostMapping("file/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadFile){

        if(uploadFile.isEmpty()){
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        fileService.saveUploadedFiles(Arrays.asList(uploadFile));
        return new ResponseEntity("Successfully uploaded - " +
                uploadFile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }

    @PostMapping("file/upload/multi")
    public ResponseEntity<String> uploadFileMulti(@RequestParam(value = "extraField", required = false) String extraField, @RequestParam("files") MultipartFile[] uploadFiles) {

        logger.debug("Multiple file upload!");


        // Get file name
        String uploadedFileName = Arrays.stream(uploadFiles).map(MultipartFile::getOriginalFilename)
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity<>("please select a file!", HttpStatus.OK);
        }

        for(MultipartFile file : uploadFiles){
            String originalFilename = file.getOriginalFilename();
            boolean flag = FileTypeEnum.checkSuffix(originalFilename);
            if(!flag){
                return new ResponseEntity<>("Illegal file type", HttpStatus.BAD_REQUEST);
            }
        }

        fileService.saveUploadedFiles(Arrays.asList(uploadFiles));

        return new ResponseEntity<>("Successfully uploaded - "
                + uploadedFileName, HttpStatus.OK);

    }

    // 3.1.3 maps html form to a Model
    @PostMapping("api/upload/multi/model")
    @ResponseBody
    public CR<?> multiUploadFileModel(@ModelAttribute UploadModel model) {

        return ResultDTO.create(fileService.saveUploadedFiles(Arrays.asList(model.getFiles())));
    }

}
