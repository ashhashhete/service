package com.adani.sra.connector.smsservice;

import com.adani.sra.connector.smsservice.configurations.ExampleProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;

@EnableConfigurationProperties(ExampleProperties.class)
@SpringBootApplication
public class AdaniSraSmsServiceApplication extends SpringBootServletInitializer
        implements ApplicationListener<ApplicationReadyEvent> {

    public static void main(String[] args) {
        SpringApplication.run(AdaniSraSmsServiceApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Get the port from the environment or use a default value
        String port = event.getApplicationContext().getEnvironment().getProperty("server.port", "8080");

        // Print the Swagger UI URL
        System.out.println("Swagger UI: http://localhost:" + port + "/swagger-ui/");
    }

}
