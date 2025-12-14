package com.cloudnote;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cloudnote.mapper")
public class CloudNoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudNoteApplication.class, args);
    }

}
