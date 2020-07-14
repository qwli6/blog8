package me.lqw.blog8.file;

import com.sun.mail.util.ReadableMime;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;

public class BlogResourceHttpRequestHandler extends ResourceHttpRequestHandler {


    public BlogResourceHttpRequestHandler(FileResourceResolver fileResourceResolver,
                                          ResourceProperties resourceProperties) {
        super();
        this.setResourceResolvers(Collections.singletonList(fileResourceResolver));
        Duration period = resourceProperties.getCache().getPeriod();
        if(period != null){
            this.setCacheSeconds((int) period.getSeconds());
        }
        CacheControl cacheControl = resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
        this.setCacheControl(cacheControl);
    }

    @Override
    protected MediaType getMediaType(HttpServletRequest request, Resource resource) {
        return super.getMediaType(request, resource);
    }

//    @Override
//    protected Resource getResource(HttpServletRequest request) throws IOException {
//        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
//        if(path == null){
//            throw new IllegalStateException("Required request attribute '" +
//                    HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE + "' is not set");
//        }
//        return fileService.getFileInfo(path).orElse(null);
//    }
}
