//package me.lqw.blog8.plugins.thymeleaf.data;
//
//import org.thymeleaf.context.ITemplateContext;
//import org.thymeleaf.context.IWebContext;
//import org.thymeleaf.model.IAttribute;
//import org.thymeleaf.model.IProcessableElementTag;
//import org.thymeleaf.processor.element.AbstractElementTagProcessor;
//import org.thymeleaf.processor.element.IElementTagStructureHandler;
//import org.thymeleaf.standard.StandardDialect;
//import org.thymeleaf.templatemode.TemplateMode;
//
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.Charset;
//
//public class DataTagProcessors extends AbstractElementTagProcessor {
//
//    private static final String TAG_NAME = "data";
//    private static final String NAME_ATTR = "name";
//    private static final String ALIAS_ATTR = "alias";
//
//
//    public DataTagProcessors(String dialectPrefix) {
////        super(templateMode, dialectPrefix, elementName, prefixElementName,
////                attributeName, prefixAttributeName, precedence);
//        super(TemplateMode.HTML, dialectPrefix, TAG_NAME, false,
//                null, false, StandardDialect.PROCESSOR_PRECEDENCE);
//    }
//
//    @Override
//    protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
//                             IElementTagStructureHandler structureHandler) {
//        System.out.println("处理模板");
//
//        IAttribute[] allAttributes = tag.getAllAttributes();
//
////        for(IAttribute attribute: allAttributes){
////            attribute.
////        }
//
//        IWebContext webContext = (IWebContext) context;
//        try {
//            webContext.getRequest().setCharacterEncoding(Charset.defaultCharset().name());
//            webContext.getResponse().setCharacterEncoding(Charset.defaultCharset().name());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        webContext.getRequest().setAttribute("test", "张三");
//
//
//        structureHandler.removeBody();
//    }
//}
