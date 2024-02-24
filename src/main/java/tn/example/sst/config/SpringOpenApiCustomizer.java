package tn.example.sst.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class SpringOpenApiCustomizer implements OpenApiCustomizer, Ordered {
    public static final int DEFAULT_ORDER = 0;
    private final ApplicationProperties.ApiDocs properties;
    @Setter
    @Getter
    private int order = DEFAULT_ORDER;

    public SpringOpenApiCustomizer(ApplicationProperties properties) {
        this.properties = properties.getApiDocs();
    }

    public void customise(OpenAPI openAPI) {
        Contact contact = (new Contact())
                .name(this.properties.getContactName())
                .url(this.properties.getContactUrl())
                .email(this.properties.getContactEmail());
        openAPI.info((new Info())
                .contact(contact)
                .title(this.properties.getTitle())
                .description(this.properties.getDescription())
                .version(this.properties.getVersion())
                .termsOfService(this.properties.getTermsOfServiceUrl())
                .license((new License())
                        .name(this.properties.getLicense())
                        .url(this.properties.getLicenseUrl())));

        for (ApplicationProperties.ApiDocs.Server server : this.properties.getServers()) {
            openAPI.addServersItem((new Server()).url(server.getUrl()).description(server.getDescription()));
        }
    }
}
