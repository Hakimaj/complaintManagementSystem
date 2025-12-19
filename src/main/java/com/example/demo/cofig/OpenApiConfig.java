package com.example.demo.cofig;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Complaint Management System API",
                version = "1.0",
                description = "RESTful API for managing complaints in the organization",
                contact = @Contact(
                        name = "Complaint Management Team",
                        email = "complaintTeam@gmail.com"//exapmle email
        )),

        servers = {
                        @Server(
            url = "http://localhost:8080",
            description = "Local server"
        )
        },
        security={
                @SecurityRequirement(name = "bearerAuth")
        }

)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in= SecuritySchemeIn.HEADER,
        description = "JWT Authentication. Use the format: Bearer {token}"
)


public class OpenApiConfig {
}
