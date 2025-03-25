package com.adani.sra.connector.smsservice.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

import java.util.ResourceBundle;


@Configuration
@RequiredArgsConstructor
public class PropertyConfig {

//    private final PortalProperties portalProperties;

    ResourceBundle bundle = ResourceBundle.getBundle("application");

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
//        properties.setLocation(new FileSystemResource("D:/Oreo_properties/adani/application-dev.properties"));
		properties.setLocation(new FileSystemResource(bundle.getString("customDevPath")));
        properties.setIgnoreResourceNotFound(false);

        return properties;
    }
}
