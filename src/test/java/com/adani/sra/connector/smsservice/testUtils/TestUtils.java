package com.adani.sra.connector.smsservice.testUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestUtils {

    private static ObjectMapper objectMapper;
    public static String readTestResourceFile(String path) throws IOException {
        try (InputStream inputStream = TestUtils.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream != null) {
                return new String(inputStream.readAllBytes());
            } else {
                throw new IOException("Resource not found: " + path);
            }
        }
    }

    public static Object createSampleHoHResponseList(String path,Object response) {
        Object customObject= new Object();
        try {
            objectMapper = new ObjectMapper();

            String input = readTestResourceFile(path);

            customObject = objectMapper.readValue(input, new TypeReference<Object>() {
            });

            return customObject;
        } catch (Exception e) {
            e.printStackTrace();
            return customObject;
        }
    }



}
