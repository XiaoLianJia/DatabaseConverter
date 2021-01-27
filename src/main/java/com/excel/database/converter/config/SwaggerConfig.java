package com.excel.database.converter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 *     Swagger 配置
 * </p>
 * @author zhangbin
 * @date 2020-06-12
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("电子表格数据迁移转储服务接口文档")
                .description("电子表格数据迁移转储服务接口说明")
                .version("v1.0")
                .contact(new Contact("zhangbin", "https://github.com/XiaoLianJia/ExcelDatabaseConverter.git", "m_zhangbin@foxmail.com"))
                .build();
    }
}
