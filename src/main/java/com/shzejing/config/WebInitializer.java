package com.shzejing.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class WebInitializer implements WebApplicationInitializer {

	public void onStartup(ServletContext servletContext)
			throws ServletException {

		FilterRegistration.Dynamic fr = servletContext.addFilter(
				"encodingFilter", new CharacterEncodingFilter());
		fr.setInitParameter("encoding", "UTF-8");
		fr.setInitParameter("forceEncoding", "true");
		fr.addMappingForUrlPatterns(null, true, "/*");

		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(Config.class);
		ctx.register(PersistenceConfig.class);
		ctx.register(Multipart.class);

		servletContext.getServletRegistration("default").addMapping("*.js",
				"*.css", "*.jpg", "*.gif", "*.png", "*.bmp", "*.ttf", "*.woff", "*.mp3", "*.eot", "*.svg", "*.JPG", "*.GIF",
				"*.PNG", "*.BMP", "*.JS", "*.CSS", "*.MP3", "*.swf", "*.mp4", "*.MP4", "*.Mp4", "*.html", "*.HTML", "*.txt");

		ctx.setServletContext(servletContext);

		Dynamic servlet = servletContext.addServlet("dispatcher",
				new DispatcherServlet(ctx));
		servlet.addMapping("/");
		servlet.setLoadOnStartup(1);
		
	}

}