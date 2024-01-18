package ru.denisov.RestControllerPractice.web.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openApiDescription() {
        Server localhostServer = new Server();
        localhostServer.setUrl("http://localhost:8080");
        localhostServer.setDescription("Local env");

        Server productionServer = new Server();
        productionServer.setUrl("http://some.prod.url");
        productionServer.setDescription("Production env");

        Contact contact = new Contact();
        contact.setName("Daniil Denisov");
        contact.setEmail("daniildenisov23@gmail.com");
        contact.setUrl("http://mysite.url");

        License mitLicense = new License().name("GNU AGPLv3")
                .url("https://chooselicense.com/licenses/agpl-3.0/");

        Info info = new Info()
                .title("Client orders API")
                .version("1.0")
                .contact(contact)
                .description("API for client orders")
                .termsOfService("http://some.terms.url")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(localhostServer, productionServer));

    }
}
