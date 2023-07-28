package com.example.springbatchjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.springbatchjob.repository")
public class SpringBatchJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchJobApplication.class, args);
    }

}
