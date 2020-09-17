package com.ssafy.shalendar.springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.util.Arrays;
import java.util.List;

//@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // private final LoginUserArgumentResolver loginUserArgumentResolver;
    private final String uploadImagePath;

    public WebConfig(@Value("${custom.path.upload-images}") String uploadedImagePath) {
        this.uploadImagePath = uploadedImagePath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        List<String> imageFolders = Arrays.asList("feed", "profile", "banner");
        for (String imageFolder : imageFolders) {
//            System.out.println("/multimedia/" + imageFolder + "/**");
//            System.out.println("file:///" + uploadImagePath + imageFolder + "/");
            registry.addResourceHandler("/multimedia/" + imageFolder + "/**")
                    .addResourceLocations("file:///" + uploadImagePath + imageFolder + "/")
                    .setCachePeriod(3600)
                    .resourceChain(true)
                    .addResolver(new PathResourceResolver());
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }


//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(loginUserArgumentResolver);
//    }
}
