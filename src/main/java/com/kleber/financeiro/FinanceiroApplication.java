package com.kleber.financeiro;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinanceiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceiroApplication.class, args);
    }

    @Bean
    CommandLineRunner listarBeans(ApplicationContext ctx) {
        return args -> {

            String[] beans = ctx.getBeanDefinitionNames();

            for (String bean : beans) {

                if (bean.toLowerCase().contains("controller")) {
                    System.out.println("BEAN: " + bean);
                }
            }
        };
    }
}