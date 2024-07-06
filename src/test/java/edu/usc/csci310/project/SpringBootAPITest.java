package edu.usc.csci310.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringBootAPITests {

    @Test
    void testMainMethodAnnotations() {
        SpringBootAPI api = new SpringBootAPI();
        assertTrue(api.getClass().isAnnotationPresent(SpringBootApplication.class));
    }

    @Test
    void testControllerAnnotation() {
        SpringBootAPI api = new SpringBootAPI();
        assertTrue(api.getClass().isAnnotationPresent(Controller.class));
    }

    @Test
    void testRequestMappingAnnotation() throws NoSuchMethodException {
        RequestMapping mapping = SpringBootAPI.class.getMethod("redirect").getAnnotation(RequestMapping.class);
        assertEquals("{_:^(?!index\\.html|api).*$}", mapping.value()[0]);
    }

    @Test
    void testRedirectMethod() {
        SpringBootAPI api = new SpringBootAPI();
        String result = api.redirect();
        assertEquals("forward:/", result);
    }

    @Test
    void testMainMethod() {
        String[] args = {};
        SpringBootAPI.main(args);
    }
}