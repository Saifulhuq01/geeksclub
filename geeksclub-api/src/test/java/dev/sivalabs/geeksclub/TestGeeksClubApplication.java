package dev.sivalabs.geeksclub;

import org.springframework.boot.SpringApplication;

public class TestGeeksClubApplication {

    public static void main(String[] args) {
        System.setProperty("spring.docker.compose.enabled", "false");
        SpringApplication.from(GeeksClubApplication::main)
                .with(TestcontainersConfig.class)
                .run(args);
    }
}
