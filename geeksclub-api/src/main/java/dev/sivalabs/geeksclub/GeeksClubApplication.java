package dev.sivalabs.geeksclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GeeksClubApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeeksClubApplication.class, args);
    }
}
