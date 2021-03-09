package net.bytecms.web.config;

import net.bytecms.core.config.ThinkCmsConfig;
import net.bytecms.core.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig  implements WebMvcConfigurer {

    @Autowired
    AuthInterceptor authInterceptor;

    @Autowired
    ThinkCmsConfig thinkCmsConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 自定义拦截器，添加拦截路径和排除拦截路径
        // registry.addInterceptor(authInterceptor).addPathPatterns("/**");
        // 排除配置
        registry.addInterceptor(authInterceptor).excludePathPatterns("/static/**", "/app/**", "/webjars/**",
                "/*.html", "/*.htm");
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //添加资源全局拦截器
        registry.addResourceHandler(Constants.localtionUploadPattern+"**").addResourceLocations("file:///"+thinkCmsConfig.getFileResourcePath());
        //registry.addResourceHandler(Constants.localtionUploadPattern+"**").addResourceLocations("file:E:/blog");

        //三种映射方式 webapp下、当前目录下、jar内
        registry.addResourceHandler("/app/**").addResourceLocations("/app/","file:app/", "classpath:/app/");
        registry.addResourceHandler("/static/**").addResourceLocations("/static/","file:static/","classpath:/static/","classpath:/META-INF/resources/");
        registry.addResourceHandler("/api/**").addResourceLocations("/api/","file:api/", "classpath:/api/");
    }

}
