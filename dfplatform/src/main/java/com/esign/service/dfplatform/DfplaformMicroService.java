package com.esign.service.dfplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: houlandong
 * @Description: 服务启动类
 * @Date: 2020/9/4 14:02
 */
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@EnableSwagger2
@EnableAsync
public class DfplaformMicroService {
    public static void main(String[] args) {

        SpringApplication.run(DfplaformMicroService.class, args);
    }
}
