    package com.apps.erte.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.MediaType;
    import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
    import springfox.documentation.builders.ApiInfoBuilder;
    import springfox.documentation.builders.PathSelectors;
    import springfox.documentation.builders.RequestHandlerSelectors;
    import springfox.documentation.service.ApiInfo;
    import springfox.documentation.service.Contact;
    import springfox.documentation.spi.DocumentationType;
    import springfox.documentation.spring.web.plugins.Docket;
    import springfox.documentation.swagger2.annotations.EnableSwagger2;

    import java.util.Collections;

    @Configuration
    @EnableSwagger2
    public class SwaggerConfig extends WebMvcConfigurationSupport {

        @Bean
        public Docket api() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.apps.erte"))
                    .paths(PathSelectors.regex("/.*"))
                    .build()
                    .consumes(Collections.singleton(MediaType.APPLICATION_JSON_VALUE))
                    .consumes(Collections.singleton(MediaType.MULTIPART_FORM_DATA_VALUE))
                    .produces(Collections.singleton(MediaType.ALL_VALUE))
                    .apiInfo(apiEndPointsInfo());
        }
        private ApiInfo apiEndPointsInfo() {
            return new ApiInfoBuilder().title("Spring Boot Minio")
                    .description("The usage of Minio in Spring Boot App")
                    .contact(new Contact("Noyan Germiyanoğlu", "github.com/Rapter1990", "sngermiyanoglu@hotmail.com"))
                    .license("Apache 2.0")
                    .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                    .version("1.12.3")
                    .build();
        }

        @Override
        protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//            registry.addResourceHandler("swagger-ui.html")
            registry.addResourceHandler("swagger-ui.html", "/webjars/**", "/v2/api-docs", "/configuration/**", "/swagger-resources/**")
                    .addResourceLocations("classpath:/META-INF/resources/");

            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }
