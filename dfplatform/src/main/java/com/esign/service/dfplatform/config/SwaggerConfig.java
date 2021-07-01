package com.esign.service.dfplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: huangtai
 * @Description: swagger配置
 * @Date: 2020/4/8 11:01
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //显示那些接口
                .apis(RequestHandlerSelectors.basePackage("com.esign.service.dfplatform.controller"))
                .build();
    }

    /**
     * api信息描述
     *
     * @return
     */
    private ApiInfo apiInfo() {

        //api信息描述
        return new ApiInfoBuilder().title("dfplatform接口文档")
                .contact(new Contact("huangtai", "", ""))
                .description("swagger_2生成的接口文档")
                .version("v2.0")
                .build();
    }
}
