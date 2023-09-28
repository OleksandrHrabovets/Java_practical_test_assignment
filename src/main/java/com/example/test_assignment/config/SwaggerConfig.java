package com.example.test_assignment.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(version = "${my.api.version}",
    title = "${my.api.title}", description = "${my.api.description}"))
public class SwaggerConfig {

}
