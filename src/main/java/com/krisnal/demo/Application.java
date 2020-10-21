package com.krisnal.demo;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	  public ServletRegistrationBean<CamelHttpTransportServlet> camelServletRegistrationBean() {
	    ServletRegistrationBean<CamelHttpTransportServlet> registration = new ServletRegistrationBean<>(new CamelHttpTransportServlet(), "/camel/*");
	    registration.setName("CamelServlet");
	    return registration;
	  }
}
