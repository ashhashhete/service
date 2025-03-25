package com.adani.drp.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableAsync;

import com.adani.drp.portal.configurations.PortalProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties({PortalProperties.class})
public class DemoApplication extends SpringBootServletInitializer
        implements ApplicationListener<ApplicationReadyEvent>  {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Get the port from the environment or use a default value
        String port = event.getApplicationContext().getEnvironment().getProperty("server.port", "8080");

        // Print the Swagger UI URL
        log.info("Swagger UI: http://localhost:" + port + "/swagger-ui/");
    }

}
