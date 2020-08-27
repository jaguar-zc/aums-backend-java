package cn.stackflow.aums.common.configuration;


import cn.stackflow.aums.common.shiro.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * 在线文档 swagger 配置
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

//    @Autowired
//    AppConfigProperties properties;

    @Bean
    public Docket docsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("aiinspectorApi")
                .apiInfo(apiWebInfo())
                .useDefaultResponseMessages(false)
//                .enable(properties.getEnableSwagger())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.stackflow.aums.web"))
                .paths(PathSelectors.regex("^(?!auth).*$"))
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                ;
    }

    private ApiInfo apiWebInfo() {
        return new ApiInfoBuilder()
                .title("Nix Systems")
                .description("Nix Systems Api Doc")
                .version("1.0")
                .build();
    }



    private List<ApiKey> securitySchemes() {
        return newArrayList(
                new ApiKey(JwtFilter.HEADER_AUTHORIZATION, JwtFilter.HEADER_AUTHORIZATION, "header"));
    }

    private List<SecurityContext> securityContexts() {
        return newArrayList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build()
        );
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference(JwtFilter.HEADER_AUTHORIZATION, authorizationScopes));
    }

}
