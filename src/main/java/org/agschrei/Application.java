package org.agschrei;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Micronaut Hasher",
                version = "${api.version}",
                description = "${openapi.description}",
                license = @License(name = "MIT", url = "https://raw.githubusercontent.com/agschrei/mn-hasher/master/LICENSE"),
                contact = @Contact(url = "https://agschrei.me")
        )
)
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
