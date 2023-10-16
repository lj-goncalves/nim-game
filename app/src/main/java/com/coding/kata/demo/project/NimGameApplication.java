package com.coding.kata.demo.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "com.coding.kata.demo.project")
@PropertySource({"classpath:properties/application.properties", "classpath:properties/datasource.properties"})
public class NimGameApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(NimGameApplication.class, args);
    }

}