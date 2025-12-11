package com.example.onlinetutors.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ánh xạ tất cả tài nguyên trong static/admin
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/admin/css/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/admin/js/");

        registry.addResourceHandler("/lib/**")
                .addResourceLocations("classpath:/static/admin/lib/");

        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/admin/img/");

        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/admin/assets/");

        registry.addResourceHandler("/client/css/**")
                .addResourceLocations("classpath:/static/client/css/");

        registry.addResourceHandler("/client/js/**")
                .addResourceLocations("classpath:/static/client/js/");

        registry.addResourceHandler("/client/lib/**")
                .addResourceLocations("classpath:/static/client/lib/");

        registry.addResourceHandler("/client/img/**")
                .addResourceLocations("classpath:/static/client/img/");

        registry.addResourceHandler("/client/images/**")
                .addResourceLocations("file:uploads/client/images/");

        registry.addResourceHandler("/adminImg/images/**")
                .addResourceLocations("file:uploads/adminImg/images/");

        registry.addResourceHandler("/ebook/images/**")
                .addResourceLocations("file:uploads/ebook/images/");

        registry.addResourceHandler("/ebook/files/**")
                .addResourceLocations("file:uploads/ebook/files/");

    }
}
