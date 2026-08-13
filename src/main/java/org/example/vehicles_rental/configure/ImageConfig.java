package org.example.vehicles_rental.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/profileImage/**").addResourceLocations("file:profileImage/");
        registry.addResourceHandler("/upload/**").addResourceLocations("file:upload/");
    }
}
