//package me.lqw.blog8.web.configuration;
//
//import com.mitchellbosecke.pebble.PebbleEngine;
//import com.mitchellbosecke.pebble.boot.autoconfigure.PebbleProperties;
//import com.mitchellbosecke.pebble.loader.ClasspathLoader;
//import com.mitchellbosecke.pebble.loader.Loader;
//import com.mitchellbosecke.pebble.loader.ServletLoader;
//import com.mitchellbosecke.pebble.loader.StringLoader;
//import com.mitchellbosecke.pebble.spring.extension.SpringExtension;
//import com.mitchellbosecke.pebble.spring.servlet.PebbleViewResolver;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import sun.rmi.rmic.iiop.ClassPathLoader;
//
//@Configuration
//@EnableConfigurationProperties({PebbleProperties.class})
//@ConditionalOnProperty(prefix = "blog.core", value = "template", havingValue = "pebble", matchIfMissing = true) //缺失属性默认为 true
//public class PebbleConfigurationSupport {
//
//
//    @Bean("pebbleEngine")
//    public PebbleEngine pebbleEngine() {
//        return new PebbleEngine.Builder()
//                .loader(pebbleLoader()).build();
//    }
//
//    @Bean(name = "pebbleViewResolver")
//    public PebbleViewResolver pebbleViewResolver(){
//        PebbleViewResolver viewResolver = new PebbleViewResolver(pebbleEngine());
//        viewResolver.setPrefix("/templates/");
//        viewResolver.setSuffix(".html");
//        return viewResolver;
//    }
//
////    @Bean
////    public SpringExtension springExtension(){
////        return new SpringExtension();
////    }
////
//
//    @Bean
//    public Loader<?> pebbleLoader(){
//        ClasspathLoader classpathLoader = new ClasspathLoader();
//        classpathLoader.setPrefix("/templates/");
//        return new ClasspathLoader();
//    }
//
//}
