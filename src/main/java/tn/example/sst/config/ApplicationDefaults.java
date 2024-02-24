package tn.example.sst.config;

public class ApplicationDefaults {
    public interface AuditEvents {
        int retentionPeriod = 30;
    }

    public interface ClientApp {
        String name = "test-app";
    }


    public interface Social {
        String redirectAfterSignIn = "/#/home";
    }

    public interface ApiDocs {
        String title = "Application API";
        String description = "API documentation";
        String version = "0.0.1";
        String termsOfServiceUrl = null;
        String contactName = null;
        String contactUrl = null;
        String contactEmail = null;
        String license = null;
        String licenseUrl = null;
        String[] defaultIncludePattern = new String[]{"/api/**"};
        String[] managementIncludePattern = new String[]{"/management/**"};
        String host = null;
        String[] protocols = new String[0];
        boolean useDefaultResponseMessages = true;
    }

    public interface Security {
        String contentSecurityPolicy = "default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:";

        interface RememberMe {
            String key = null;
        }

        interface Authentication {
            interface Jwt {
                String secret = null;
                String base64Secret = null;
                long tokenValidityInSeconds = 1800L;
                long tokenValidityInSecondsForRememberMe = 2592000L;
            }
        }

        interface ClientAuthorization {
            String accessTokenUri = null;
            String tokenServiceId = null;
            String clientId = null;
            String clientSecret = null;
        }
    }

    public interface Mail {
        boolean enabled = false;
        String from = "";
        String baseUrl = "";
    }

    public interface Cache {
        interface Redis {
            String[] server = new String[]{"redis://localhost:6379"};
            int expiration = 300;
            boolean cluster = false;
            int connectionPoolSize = 64;
            int connectionMinimumIdleSize = 24;
            int subscriptionConnectionPoolSize = 50;
            int subscriptionConnectionMinimumIdleSize = 1;
        }

        interface Memcached {
            boolean enabled = false;
            String servers = "localhost:11211";
            int expiration = 300;
            boolean useBinaryProtocol = true;

            interface Authentication {
                boolean enabled = false;
            }
        }
    }

    public interface Http {
        interface Cache {
            int timeToLiveInDays = 1461;
        }
    }

    public interface Async {
        int corePoolSize = 2;
        int maxPoolSize = 50;
        int queueCapacity = 10000;
    }
}
