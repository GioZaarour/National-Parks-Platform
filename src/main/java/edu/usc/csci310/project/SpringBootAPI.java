package edu.usc.csci310.project;

import edu.usc.csci310.project.config.WebSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SpringBootApplication()
@Import(WebSecurityConfig.class)
public class SpringBootAPI {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootAPI.class, args);
    }

    @RequestMapping(value = "{_:^(?!index\\.html|api).*$}")
    public String redirect() {
        // Forward to home page so that route is preserved.(i.e forward:/index.html)
        return "forward:/";
    }
}
