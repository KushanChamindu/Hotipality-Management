package com.example.apigatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API gateway", version = "1.0", description = "API gatway of megabliss"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer", in = SecuritySchemeIn.HEADER)
public class MegaBlissAPiGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MegaBlissAPiGatewayServiceApplication.class, args);
    }

}
