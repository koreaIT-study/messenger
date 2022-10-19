package com.teamride.messenger.client.config;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.teamride.messenger.client.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    HttpSession httpSession;
    
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
		.addResourceLocations("classpath:/templates/","classpath:/static/")
		.setCachePeriod(0);
//		.setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
	}
	
	 @Override
	    public void addInterceptors(InterceptorRegistry registry) {
	        registry.addInterceptor(new LoginCheckInterceptor(httpSession))
	                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/scripts/**");
	    }
}
