package me.lqw.blog8.file;

import me.lqw.blog8.web.controller.console.BaseController;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
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
    public List<FileInfo> queryFiles(FileQueryParam queryParam) throws Exception {
        return fileService.queryFiles(queryParam);
    }


    @PostMapping("file/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadFile){

        if(uploadFile.isEmpty()){
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {
            fileService.saveUploadedFiles(Arrays.asList(uploadFile));
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Successfully uploaded - " +
                uploadFile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }

    @PostMapping("file/upload/multi")
    public ResponseEntity<String> uploadFileMulti(@RequestParam("extraField") String extraField, @RequestParam("files") MultipartFile[] uploadFiles) {

        logger.debug("Multiple file upload!");


        // Get file name
        String uploadedFileName = Arrays.stream(uploadFiles).map(MultipartFile::getOriginalFilename)
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity<>("please select a file!", HttpStatus.OK);
        }

        try {
            for(MultipartFile file : uploadFiles){
                String originalFilename = file.getOriginalFilename();
                boolean flag = FileTypeEnum.checkSuffix(originalFilename);
                if(!flag){
                    return new ResponseEntity<>("Illegal file type", HttpStatus.BAD_REQUEST);
                }
            }

            fileService.saveUploadedFiles(Arrays.asList(uploadFiles));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Successfully uploaded - "
                + uploadedFileName, HttpStatus.OK);

    }

    // 3.1.3 maps html form to a Model
    @PostMapping("api/upload/multi/model")
    @ResponseBody
    public ResponseEntity<String> multiUploadFileModel(@ModelAttribute UploadModel model) {

        logger.debug("Multiple file upload! With UploadModel");

        try {

            fileService.saveUploadedFiles(Arrays.asList(model.getFiles()));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Successfully uploaded!", HttpStatus.OK);
    }

}
