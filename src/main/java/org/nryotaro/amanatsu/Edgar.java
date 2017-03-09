package org.nryotaro.amanatsu;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.CommandLineRunner;

import javax.servlet.*;

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
    public Runner runner() {
        return new Runner();
    }


}

class Runner implements CommandLineRunner {

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("hoge---");
    }
}
