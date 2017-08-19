package com.shzejing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class Multipart {

	@Bean
	public CommonsMultipartResolver getMultipartResolver() {
		CommonsMultipartResolver mr = new CommonsMultipartResolver();
		 mr.setMaxUploadSize(10000);
		return mr;
	}
}
