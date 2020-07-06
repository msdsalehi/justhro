package org.justhro.sample.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SampleServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(SampleServiceApplication.class)
                .run(args);
//        System.out.println("\n-----------------");
//        System.out.println(Arrays.toString(context.getBeanDefinitionNames()));
//        System.out.println("-----------------");
    }
}
