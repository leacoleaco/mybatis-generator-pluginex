package com.fuyo.cloud.db;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.fuyo.cloud.db.**.dao",
        "com.fuyo.cloud.db.**.daos",
})
@MapperScan("com.fuyo.cloud.db.**.dao")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
