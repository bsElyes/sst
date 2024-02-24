package tn.example.sst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tn.example.sst.config.ApplicationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ ApplicationProperties.class })
public class SstApplication {

    public static void main(String[] args) {
        SpringApplication.run(SstApplication.class, args);
    }

}
