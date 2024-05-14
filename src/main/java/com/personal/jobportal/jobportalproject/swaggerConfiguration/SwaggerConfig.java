package com.personal.jobportal.jobportalproject.swaggerConfiguration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SwaggerConfig {
        String bearerScheme ="bearerScheme";
        @Bean
        public OpenAPI springShopOpenAPI() {
                return new OpenAPI()
                        .addSecurityItem(new SecurityRequirement()
                                .addList(bearerScheme))
                        .components(new Components()
                                .addSecuritySchemes(bearerScheme, new SecurityScheme()
                                        .name(bearerScheme)
                                        .type(SecurityScheme.Type.HTTP)
                                        .bearerFormat("JWT")
                                        .scheme("bearer")
                                )
                        )
                        .info(new Info().title("Job Portal API")
                                .description("Job Portal sample application")
                                .version("v0.0.1")
                                .license(new License().name("Apache 2.0")));
        }
}
