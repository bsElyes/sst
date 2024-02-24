package tn.example.sst.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(Constants.SPRING_PROFILE_API_DOCS)
public class OpenApiConfiguration {

    public static final String API_FIRST_PACKAGE = "tn.eternity.sample.web.api";

    @Bean
    @ConditionalOnMissingBean(name = "apiFirstGroupedOpenAPI")
    public GroupedOpenApi apiFirstGroupedOpenAPI(
        SpringOpenApiCustomizer springOpenApiCustomizer,
        ApplicationProperties applicationProperties
    ) {
        ApplicationProperties.ApiDocs properties = applicationProperties.getApiDocs();
        return GroupedOpenApi
            .builder()
            .group("openapi")
            .addOpenApiCustomizer(springOpenApiCustomizer)
            .packagesToScan(API_FIRST_PACKAGE)
            .pathsToMatch(properties.getDefaultIncludePattern())
            .build();
    }
}
