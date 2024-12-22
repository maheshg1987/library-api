package com.libraryapi.configurations;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BeanConfig {

    @Bean
    public Pipeline pipeline(List<Command.Handler> handlers) {
        return new Pipelinr()
                .with(() -> handlers.stream());
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Library Management API")
                        .version("1.0")
                        .description("API documentation for library management system"));
    }


}
