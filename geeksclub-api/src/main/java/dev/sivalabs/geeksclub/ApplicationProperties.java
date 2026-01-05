package dev.sivalabs.geeksclub;

import jakarta.validation.Valid;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "app")
public record ApplicationProperties(
        @DefaultValue("support@sivalabs.dev") String supportEmail,
        String applicationUrl,
        @Valid CorsProperties cors,
        @Valid OpenAPIProperties openApi) {

    public record CorsProperties(
            @DefaultValue("/api/**") String pathPattern,
            @DefaultValue("*") String allowedOrigins,
            @DefaultValue("*") String allowedMethods,
            @DefaultValue("*") String allowedHeaders) {}

    public record OpenAPIProperties(
            @DefaultValue("GeeksClub API") String title,

            @DefaultValue("GeeksClub API Swagger Documentation")
            String description,

            @DefaultValue("v1.0.0") String version,
            @Valid OpenAPIProperties.Contact contact) {

        public record Contact(
                @DefaultValue("SivaLabs") String name,
                @DefaultValue("support@sivalabs.in") String email) {}
    }
}
