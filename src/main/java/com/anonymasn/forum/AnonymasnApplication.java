package com.anonymasn.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableWebMvc
public class AnonymasnApplication implements WebMvcConfigurer {

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
			if (!registry.hasMappingForPattern("/images/**")) {
				registry
          .addResourceHandler("/images/**")
          .addResourceLocations("classpath:/public/images/");
			} 
    }

	public static void main(String[] args) {
		SpringApplication.run(AnonymasnApplication.class, args);
	}

}
