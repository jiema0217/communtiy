package io.github.jiema.communtiy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("io.github.jiema.communtiy.mapper")
public class CommuntiyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommuntiyApplication.class, args);
    }

}
