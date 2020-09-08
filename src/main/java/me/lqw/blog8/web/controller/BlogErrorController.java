package me.lqw.blog8.web.controller;

import me.lqw.blog8.constants.BlogConstants;
import me.lqw.blog8.constants.Message;
import me.lqw.blog8.util.JsonUtil;
import me.lqw.blog8.web.controller.console.AbstractBaseController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统出错处理
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Controller
@RequestMapping("error")
public class BlogErrorController extends AbstractBaseController implements ErrorController {

    /**
     * 默认的错误路径
     */
    private static final String ERROR_PATH = "/error";


    /**
     * 错误属性，可以从该属性中获取请求的错误信息
     *
     * @see me.lqw.blog8.exception.resolver.BlogHandlerExceptionResolver
     * 在上面的这个类中，我们移除了系统自带的错误，填充了我们自定义的错误
     */
    private final ErrorAttributes errorAttributes;

    /**
     * 构造方法注入
     *
     * @param errorAttributes errorAttributes
     */
    public BlogErrorController(ErrorAttributes errorAttributes) {
        super();
        this.errorAttributes = errorAttributes;
    }

    /**
     * 获取系统默认的错误路径
     *
     * @return string
     */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    /**
     * 给前端返回 html 格式的错误
     * @param request  request
     * @param response response
     * @return ModelAndView
     */
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorWithHtml(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = getModelAndView();
        HttpStatus status = getStatus(request);
        Map<String, Object> errors = errorAttributes.getErrorAttributes(new ServletWebRequest(request),
                ErrorAttributeOptions.of(Collections.emptyList()));

        //可访问的页面就是 404 405 401 5xx，用一个页面来代替就好了
        response.setStatus(status.value());
        mav.setViewName("error");

        //401 可能没有 error, 这里判断了手动填充
        if(status.value() == 401 && CollectionUtils.isEmpty(errors)){
            errors = new HashMap<>(2);
            Message error = new Message();
            error.setMsg("您无权限访问此资源");
            error.setCode("authorization.required");
            errors.put("errors", error);
        }

        // 500，直接返回系统错误
        if(status.is5xxServerError()){
            errors = new HashMap<>(2);
            errors.put("errors", BlogConstants.SYSTEM_ERROR);
        }


        logger.error("errors:[{}]", JsonUtil.toJsonString(errors));
        mav.addObject("errors", errors);
        return mav;
    }

    /**
     * 给前端返回 json 格式的数据
     *
     * @param request  request
     * @param response response
     * @return ResponseEntity
     */
    @RequestMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> errorWithJson(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        if (!status.isError()) {
            //非 4xx 或者 5xx 的错误
            return new ResponseEntity<>(status);
        }
        Map<String, Object> errors = errorAttributes.getErrorAttributes(new ServletWebRequest(request),
                ErrorAttributeOptions.of(Collections.emptyList()));
        return new ResponseEntity<>(errors, status);
    }

    /**
     * 从 httpServletRequest 中获取本次 http 请求的状态码
     *
     * @param request request
     * @return HttpStatus
     */
    public HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            if (request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI) != null) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            } else {
                return HttpStatus.OK;
            }
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
