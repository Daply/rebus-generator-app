package com.rebusgenerator.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    public void addViewControllers(ViewControllerRegistry registry) {
    	registry.addViewController("/admin").setViewName("admin");
    	registry.addViewController("/login").setViewName("login");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/not_found").setViewName("not_found");
    }
    
    @Override
    public void addResourceHandlers (ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/rebus/**")
                  .addResourceLocations("classpath:/public/rebus/");
        registry.addResourceHandler("/admin/**")
		          .addResourceLocations("classpath:/public/admin/");
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        return container -> {
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
                    "/not_found"));
        };
    }
}
