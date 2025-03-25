package com.adani.drp.portal.utils;

import java.io.UnsupportedEncodingException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@ConfigurationProperties(prefix = "documentcategory")
@PropertySource(value = "classpath:/docs/DocumentCategory.yml", encoding = "UTF-8")
public class ReplaceCategory implements EnvironmentAware {

	private static Environment env;

	@Override
	public void setEnvironment(Environment env) {
		this.env = env;
	}

	public static String getValFromKey(String key) throws UnsupportedEncodingException {
		if (env.getProperty(key) != null) {
			return env.getProperty(key);
		}
		return null;
	}

}
