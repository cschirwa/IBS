package com.kt.ibs.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.ibs"))

                .build();
    }

    private ApiInfo apiInfo() {
    	//ApiInfo.DEFAULT.
       // ApiInfo apiInfo = new ApiInfo("IBS REST API", "IBS REST API", "1.0",
       ///         "Terms of service",
          //      new Contact("IBS", "www.kemtech.co.za", "support@kemtech.co.za"),
            ///    "Apache License Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0");
        ///return apiInfo;
    	return ApiInfo.DEFAULT; 
    }
}
