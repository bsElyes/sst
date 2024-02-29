package tn.example.sst.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Data
@NoArgsConstructor
public class ApplicationProperties {
    private final Async async = new Async();
    private final Http http = new Http();
    private final Mail mail = new Mail();
    private final Cache cache = new Cache();
    private final Security security = new Security();
    private final ApiDocs apiDocs = new ApiDocs();
    private final Logging logging = new Logging();
    private final CorsConfiguration cors = new CorsConfiguration();
    private final ClientApp clientApp = new ClientApp();
    private final AuditEvents auditEvents = new AuditEvents();

    @Data
    @NoArgsConstructor
    public static class Cache {
        private final Hazelcast hazelcast = new Hazelcast();
        private final Redis redis = new Redis();

        @Data
        @NoArgsConstructor
        public static class Hazelcast {
            private int timeToLiveSeconds = 3600;
            private int backupCount = 1;
        }

        @Data
        @NoArgsConstructor
        public static class Redis {
            private String[] server;
            private int expiration;
            private boolean cluster;
            private int connectionPoolSize;
            private int connectionMinimumIdleSize;
            private int subscriptionConnectionPoolSize;
            private int subscriptionConnectionMinimumIdleSize;
        }
    }

    @Data
    @NoArgsConstructor
    public static class Async {
        private int corePoolSize = 2;
        private int maxPoolSize = 50;
        private int queueCapacity = 10000;
    }


    @Data
    @NoArgsConstructor
    public static class Http {
        private final Cache cache = new Cache();

        @Data
        @NoArgsConstructor
        public static class Cache {
            private int timeToLiveInDays = 1461;
        }
    }

    @Data
    @NoArgsConstructor
    public static class Mail {
        private boolean enabled = false;
        private String from = "";
        private String baseUrl = "";
    }

    @Data
    @NoArgsConstructor
    public static class Security {
        private final ClientAuthorization clientAuthorization = new ClientAuthorization();
        private final Authentication authentication = new Authentication();
        private final RememberMe rememberMe = new RememberMe();
        private final OAuth2 oauth2 = new OAuth2();
        private String contentSecurityPolicy = "default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:";

        @Data
        public static class ClientAuthorization {
            private String accessTokenUri;
            private String tokenServiceId;
            private String clientId;
            private String clientSecret;

            public ClientAuthorization() {
                this.accessTokenUri = ApplicationDefaults.Security.ClientAuthorization.accessTokenUri;
                this.tokenServiceId = ApplicationDefaults.Security.ClientAuthorization.tokenServiceId;
                this.clientId = ApplicationDefaults.Security.ClientAuthorization.clientId;
                this.clientSecret = ApplicationDefaults.Security.ClientAuthorization.clientSecret;
            }
        }

        @Data
        @NoArgsConstructor
        public static class Authentication {
            private final Jwt jwt = new Jwt();


            @Data
            public static class Jwt {
                private String secret;
                private String base64Secret;
                private long tokenValidityInSeconds;
                private long tokenValidityInSecondsForRememberMe;

                public Jwt() {
                    this.secret = ApplicationDefaults.Security.Authentication.Jwt.secret;
                    this.base64Secret = ApplicationDefaults.Security.Authentication.Jwt.base64Secret;
                    this.tokenValidityInSeconds = 1800L;
                    this.tokenValidityInSecondsForRememberMe = 2592000L;
                }
            }
        }

        @Data
        public static class RememberMe {
            @NotNull
            private String key;

            public RememberMe() {
                this.key = ApplicationDefaults.Security.RememberMe.key;
            }
        }

        @Data
        @NoArgsConstructor
        public static class OAuth2 {
            private final List<String> audience = new ArrayList<>();
        }
    }


    @Data
    public static class ApiDocs {
        private String title = "Application API";
        private String description = "API documentation";
        private String version = "0.0.1";
        private String termsOfServiceUrl;
        private String contactName;
        private String contactUrl;
        private String contactEmail;
        private String license;
        private String licenseUrl;
        private String[] defaultIncludePattern;
        private String[] managementIncludePattern;
        private Server[] servers;

        public ApiDocs() {
            this.termsOfServiceUrl = ApplicationDefaults.ApiDocs.termsOfServiceUrl;
            this.contactName = ApplicationDefaults.ApiDocs.contactName;
            this.contactUrl = ApplicationDefaults.ApiDocs.contactUrl;
            this.contactEmail = ApplicationDefaults.ApiDocs.contactEmail;
            this.license = ApplicationDefaults.ApiDocs.license;
            this.licenseUrl = ApplicationDefaults.ApiDocs.licenseUrl;
            this.defaultIncludePattern = ApplicationDefaults.ApiDocs.defaultIncludePattern;
            this.managementIncludePattern = ApplicationDefaults.ApiDocs.managementIncludePattern;
            this.servers = new Server[0];
        }

        @Data
        @NoArgsConstructor
        public static class Server {
            private String url;
            private String description;
        }
    }

    @Data
    @NoArgsConstructor
    public static class Logging {
        private final Logstash logstash = new Logstash();
        private boolean useJsonFormat = false;

        @Data
        @NoArgsConstructor
        public static class Logstash {
            private boolean enabled = false;
            private String host = "localhost";
            private int port = 5000;
            private int ringBufferSize = 512;
        }
    }

    @Data
    @NoArgsConstructor
    public static class ClientApp {
        private String name = "testApp";
    }

    @Data
    @NoArgsConstructor
    public static class AuditEvents {
        private int retentionPeriod = 30;
    }
}
