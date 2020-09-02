package me.lqw.blog8.file;

import me.lqw.blog8.exception.ResourceNotFoundException;
import me.lqw.blog8.model.dto.common.CR;
import me.lqw.blog8.model.dto.common.QR;
import me.lqw.blog8.model.dto.common.ResultDTO;
import me.lqw.blog8.util.JsonUtil;
import me.lqw.blog8.web.controller.console.AbstractBaseController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件系统 controller
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Conditional({FileCondition.class})
@ConditionalOnClass(FileService.class)
@ConditionalOnWebApplication
@Controller
@RequestMapping("console")
public class FileController extends AbstractBaseController {

    /**
     * 文件服务
     */
    private final FileService fileService;

    /**
     * 构造函数注入
     *
     * @param fileService fileService
     */
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 获取文件管理页面
     *
     * @return String
     */
    @GetMapping("files")
    public String files() {
        return "console/file/index";
    }


    /**
     * 查询文件
     *
     * @param queryParam queryParam
     * @return FilePageResult
     */
    @GetMapping("file/query")
    @ResponseBody
    public QR selectPage(FilePageQueryParam queryParam) {
        return ResultDTO.createQR(fileService.selectPage(queryParam));
    }


    /**
     * 上传文件
     * @param uploadModel uploadModel
     * @return CR<?>
     */
    @PostMapping("file/upload")
    @ResponseBody
    public CR<?> fileUpload(@ModelAttribute UploadModel uploadModel) {

        List<FileInfo> fileInfos = fileService.uploadedFiles(uploadModel);

        logger.info("upload success! [{}]", JsonUtil.toJsonString(fileInfos));
        return ResultDTO.create(fileInfos);
    }


    /**
     * 更新文件内容
     * @param fileUpdated fileUpdated
     * @return CR<?>
     */
    @PostMapping("file/update")
    @ResponseBody
    public CR<?> fileUpdate(@RequestBody FileUpdated fileUpdated){
        fileService.writeFile(fileUpdated);

        return ResultDTO.create();
    }


    @PostMapping("file/create")
    @ResponseBody
    public CR<?> fileCreate(@RequestBody FileCreate fileCreate) {
        return ResultDTO.create(fileService.fileCreate(fileCreate));
    }


    /**
     * 编辑文件
     * @param fileName 文件名称
     * @return String
     */
    @GetMapping("file/edit")
    public ModelAndView fileEdit(@RequestParam("fileName") String fileName, Model model) {

        FileInfoDetail fid = fileService.fileEdit(fileName);

        model.addAttribute("fid", fid);

        String ext = fid.getExt().getCode();
        if(FileTypeEnum.HTM.getCode().equals(ext)){
            ext = FileTypeEnum.HTML.getCode();
        }

        ModelAndView mav = getModelAndView();
        mav.setViewName("console/file/edit/edit_" + ext);
        return mav;
    }


    /**
     * 文件重命名
     * @return CR<?>
     */
    @PostMapping("file/rename")
    @ResponseBody
    public CR<?> fileRename(){

        return ResultDTO.create();
    }


    /**
     * 删除文件
     * @param fileUpdated fileDelete
     * @return CR<?>
     */
    @DeleteMapping("file/delete")
    @ResponseBody
    public CR<?> fileDelete(@RequestBody FileUpdated fileUpdated){

        return ResultDTO.create();
    }


    /**
     * 拷贝文件
     * @param fileUpdated fileUpdated
     * @return CR<?>
     */
    @PostMapping("file/copy")
    @ResponseBody
    public CR<?> fileCopy(@RequestBody FileUpdated fileUpdated){

        return ResultDTO.create();
    }

}
