package me.lqw.blog8.web.controller;

import me.lqw.blog8.web.controller.console.BaseController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("error")
public class BlogErrorController extends BaseController implements ErrorController {


    private final ErrorAttributes errorAttributes;

    public BlogErrorController(ErrorAttributes errorAttributes) {
        super();
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }


    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response){
        logger.error("BlogErrorController#error....");
        ModelAndView mav = getModelAndView();
        HttpStatus status = getStatus(request);
        Map<String, Object> model = Collections
                .unmodifiableMap(errorAttributes.getErrorAttributes(new ServletWebRequest(request), ErrorAttributeOptions.defaults()));
        response.setStatus(status.value());
        mav.setViewName("page_error");
        mav.addObject("errors", model);
        return mav;
    }


    @RequestMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> errorJSON(HttpServletRequest request, HttpServletResponse response){
        HttpStatus status = getStatus(request);
        if(!status.isError()){
            return new ResponseEntity<>(status);
        }
        //        Map<String, Object> errors = this.errorAttributes.getErrorAttributes(new ServletWebRequest(request), false);
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        options.including(ErrorAttributeOptions.Include.MESSAGE);
        options.including(ErrorAttributeOptions.Include.BINDING_ERRORS);
        Map<String, Object> errors = this.errorAttributes.getErrorAttributes(new ServletWebRequest(request),
                options);
//        Map<String, Object> errors = Collections.unmodifiableMap();

        return new ResponseEntity<>(errors, status);
    }

    public HttpStatus getStatus(HttpServletRequest request){
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            if(request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI) != null) {
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
