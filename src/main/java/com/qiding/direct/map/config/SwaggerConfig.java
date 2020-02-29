package com.qiding.direct.map.config;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Predicates;
import com.qiding.direct.map.common.AppConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.Example;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        Docket docket= new Docket(DocumentationType.SWAGGER_2)
               .apiInfo(apiInfo())
               .select()
               .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
               .paths(PathSelectors.any())
               .build().enable(true);
        List<Parameter> parameters=new ArrayList<>(2);
        Parameter username=new ParameterBuilder().name(AppConstant.USER_NAME)
                .description("用户名，admin类的接口必传").required(false)
                .modelRef(new ModelRef("String")).parameterType("header")
                .build();


        Parameter userToken=new ParameterBuilder().name(AppConstant.USER_TOKEN).description("用户token，admin类的接口必传")
                .required(false)
                .modelRef(new ModelRef("String")).parameterType("header")
                .build();
        parameters.add(username);
        parameters.add(userToken);
        docket.globalOperationParameters(parameters);
        return docket;
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder().title("室内导航API接口").build();
    }
}
