package org.nryotaro.amanatsu;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.*;
import java.io.IOException;
import java.util.logging.LogRecord;

@RestController
@SpringBootApplication
public class Edgar {


    @RequestMapping("/")
    @ResponseBody
    public String home(ServletRequest req) {
        return "Hello, Spring Boot!";
    }

    public static void main(String[] args) {
        SpringApplication.run(Edgar.class, args);
    }


    @Bean
    public FilterRegistrationBean someFilterRegistration() {

        FilterRegistrationBean registration = new
                FilterRegistrationBean();
        registration.setFilter(new Filter() {


            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                System.out.println("!!!!");

            }

            @Override
            public void destroy() {

            }
        });
        registration.setOrder(1);
        return registration;
    }



}
