package edu.usc.csci310.project;
import edu.usc.csci310.project.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(AppConfig.class)
public class AppConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void httpClientBeanIsLoaded() {
        HttpClient httpClient = context.getBean(HttpClient.class);
        assertNotNull(httpClient, "HttpClient bean should be loaded");
    }
}

