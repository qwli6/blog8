//package me.lqw.blog8.plugins.thymeleaf.dialect;
//
//import me.lqw.blog8.plugins.thymeleaf.data.DataTagProcessors;
//import org.thymeleaf.dialect.AbstractDialect;
//import org.thymeleaf.processor.IProcessor;
//
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Set;
//
//public class DataTagDialect extends AbstractDialect {
//
//    private static final String PREFIX = "data";
//
//    private static final String DIALECT_NAME = "data_tag_dialect";
//
//    private static final Set<IProcessor> processors = new HashSet<>();
//
//    static {
//        processors.add(new DataTagProcessors(PREFIX));
//    }
//
//    public DataTagDialect() {
//        super(DIALECT_NAME);
//    }
//
//    public static Set<IProcessor> getProcessors() {
//        return Collections.unmodifiableSet(processors);
//    }
//
//    public String getPrefix() {
//        return PREFIX;
//    }
//}
